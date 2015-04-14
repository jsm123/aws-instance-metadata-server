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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RemoteHostRoleNameProducer implements RoleNameProducer {
  private static final Logger LOG = LoggerFactory.getLogger(RemoteHostRoleNameProducer.class);

  @Override
  public Optional<String> getRoleName(HttpServletRequest request) {
    String hostName = request.getRemoteHost();
    Optional<String> roleName = toRoleName(hostName);
    if (!roleName.isPresent()) {
      LOG.info("Unable to generate a role name for host {}.", hostName);
    }
    return roleName;
  }

  private Optional<String> toRoleName(String hostName) {
    return Optional.of(hostName)
      .flatMap(this::getHostPart)
      .flatMap(this::stripHostNr)
      .flatMap(this::unifyHamAndBerToPro)
      .map(this::addRzPrefix);
  }

  private String addRzPrefix(String rawLocTyp) {
    return "rz_" + rawLocTyp;
  }

  private Optional<String> unifyHamAndBerToPro(String rawLocTyp) {
    Matcher matcher = Pattern.compile("^((ber)|(ham))(.+)$").matcher(rawLocTyp);

    if (!matcher.find()) {
      return Optional.of(rawLocTyp);
    }

    return Optional.ofNullable(matcher.group(4)).map((typ) -> "pro" + typ);
  }

  private Optional<String> stripHostNr(String rawHostName) {
    Matcher matcher = Pattern.compile("^([a-zA-Z]+)[0-9]+$").matcher(rawHostName);
    
    if (!matcher.find()) {
      return Optional.empty();
    }
    
    return Optional.ofNullable(matcher.group(1));

  }

  private Optional<String> getHostPart(String rawFqdn) {
    Matcher matcher = Pattern.compile("^([a-zA-Z0-9]+)(\\.d)?\\.rz\\.is$").matcher(rawFqdn);

    if (!matcher.find()) {
      return Optional.empty();
    }

    return Optional.ofNullable(matcher.group(1));
  }
}
