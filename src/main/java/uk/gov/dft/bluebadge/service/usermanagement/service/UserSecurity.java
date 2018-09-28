package uk.gov.dft.bluebadge.service.usermanagement.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

@Service("userSecurity")
public class UserSecurity {
  @Autowired private final UserManagementService userManagementService;
  @Autowired private final SecurityUtils securityUtils;

  @Autowired
  public UserSecurity(UserManagementService userManagementService, SecurityUtils securityUtils) {
    this.userManagementService = userManagementService;
    this.securityUtils = securityUtils;
  }

  public boolean isAuthorised(UUID userUuid) {
    UserEntity userEntity = userManagementService.retrieveUserById(userUuid);
    return securityUtils.isAuthorisedLACode(userEntity.getAuthorityCode());
  }
}
