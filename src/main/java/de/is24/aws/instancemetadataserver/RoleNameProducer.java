package de.is24.aws.instancemetadataserver;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface RoleNameProducer {
  Optional<String> getRoleName(HttpServletRequest request);
}
