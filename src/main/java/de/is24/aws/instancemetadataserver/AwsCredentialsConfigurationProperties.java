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

import com.amazonaws.auth.AWSCredentials;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class AwsCredentialsConfigurationProperties implements AWSCredentials {

  private String accessKey;
  private String secretKey;

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public String getAWSAccessKeyId() {
    return getAccessKey();
  }

  @Override
  public String getAWSSecretKey() {
    return getSecretKey();
  }
}
