package de.is24.aws.instancemetadataserver;

import com.amazonaws.services.identitymanagement.model.GetRoleRequest;
import com.amazonaws.services.identitymanagement.model.NoSuchEntityException;
import com.amazonaws.services.identitymanagement.model.Role;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping(value = "/{version}/meta-data/iam/security-credentials", method = RequestMethod.GET)
public class SecurityCredentialsController {

  private static TimeZone tz = TimeZone.getTimeZone("UTC");
  private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

  private static final Logger LOG = LoggerFactory.getLogger(SecurityCredentialsController.class);
  private static final ResponseEntity<Map<String,String>> HTTP_NOT_FOUND = new ResponseEntity<>(NOT_FOUND);

  private final RoleNameProducer roleNameProducer;
  private final AwsClientFactory awsClientFactory;

  static {
    df.setTimeZone(tz);
  }

  @Autowired
  public SecurityCredentialsController(RoleNameProducer roleNameProducer, AwsClientFactory awsClientFactory) {
    this.roleNameProducer = roleNameProducer;
    this.awsClientFactory = awsClientFactory;
  }

  @RequestMapping
  @ResponseBody
  public String list(HttpServletRequest request) {
    return allowedRoleName(request)
      .flatMap(this::getAwsRole)
      .map(Role::getRoleName)
      .orElse("");
  }

  @RequestMapping("/{requestedRoleName}")
  public ResponseEntity<Map<String,String>> credential(HttpServletRequest request, @PathVariable String requestedRoleName) {
    return allowedRoleName(request)
      .filter(requestedRoleName::equals)
      .flatMap(this::getAwsRole)
      .map(this::assumeRole)
      .map((r) -> {
          LOG.info("Assumed role {} for {} valid till {}", requestedRoleName, request.getRemoteHost(), df.format(r.getExpiration()));
          return r;
        }
      )
      .map(this::asJson)
      .map(ResponseEntity::ok)
      .orElse(HTTP_NOT_FOUND);
  }

  private Optional<String> allowedRoleName(HttpServletRequest request) {
    return roleNameProducer.getRoleName(request);
  }

  private Optional<Role> getAwsRole(String roleName) {
    try {
      return Optional.of(awsClientFactory.amazonIdentityManagement().getRole(new GetRoleRequest().withRoleName(roleName)).getRole());
    } catch (NoSuchEntityException e) {
      LOG.info("No AWS role named '{}' exists", roleName);
      return Optional.empty();
    }
  }

  private Credentials assumeRole(Role role) {
    return awsClientFactory.awsSecurityTokenService().assumeRole(new AssumeRoleRequest().withRoleSessionName(role.getRoleName()).withRoleArn(role.getArn())).getCredentials();
  }

  private Map<String,String> asJson(Credentials credentials) {
    Map<String,String> json = new HashMap<String, String>();
    json.put("AccessKeyId", credentials.getAccessKeyId());
    json.put("SecretAccessKey", credentials.getSecretAccessKey());
    json.put("Token", credentials.getSessionToken());
    json.put("Expiration", df.format(credentials.getExpiration()));
    json.put("Code","Success");
    json.put("Type","AWS-HMAC");

    return json;
  }
}
