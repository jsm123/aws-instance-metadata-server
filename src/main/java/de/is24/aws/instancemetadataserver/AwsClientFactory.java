/**
 * AWS Instance Metadata Server
 * Copyright 2015 Immobilien Scout GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.is24.aws.instancemetadataserver;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.System.getProperty;

@Service
public class AwsClientFactory {

  private static final ClientConfiguration CLIENT_CONFIGURATION = new ClientConfiguration();

  static {
    CLIENT_CONFIGURATION.setProtocol(Protocol.HTTPS);

    final String proxyHostSystemProperty = getProperty("https.proxyHost");
    if (StringUtils.isNotBlank(proxyHostSystemProperty)) {
      CLIENT_CONFIGURATION.setProxyHost(proxyHostSystemProperty);
    }

    final String proxyPortSystemProperty = getProperty("https.proxyPort");
    if (StringUtils.isNotBlank(proxyPortSystemProperty)) {
      CLIENT_CONFIGURATION.setProxyPort(Integer.parseInt(proxyPortSystemProperty));
    }

  }

  private final AWSCredentials credentials;

  @Autowired
  public AwsClientFactory(AWSCredentials credentials) {
    this.credentials = credentials;
  }

  public AWSSecurityTokenService awsSecurityTokenService() {
    return new AWSSecurityTokenServiceClient(credentials, CLIENT_CONFIGURATION);
  }

  public AmazonIdentityManagement amazonIdentityManagement() {
    return new AmazonIdentityManagementClient(credentials, CLIENT_CONFIGURATION);
  }

}
