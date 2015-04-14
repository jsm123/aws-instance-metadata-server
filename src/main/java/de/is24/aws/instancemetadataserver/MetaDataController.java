package de.is24.aws.instancemetadataserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MetaDataController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  @ResponseBody
  public String versions() {
    return "latest";
  }

  @RequestMapping(value = "/{version}", method = RequestMethod.GET)
  @ResponseBody
  public String metaData() {
    return "meta-data";
  }

  @RequestMapping(value = "/{version}/meta-data", method = RequestMethod.GET)
  @ResponseBody
  public String iam() {
    return "iam";
  }

  @RequestMapping(value = "/{version}/meta-data/iam", method = RequestMethod.GET)
  @ResponseBody
  public String securityCredentials() {
    return "security-credentials";
  }

}
