package uk.gov.dft.bluebadge.service.usermanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.usermanagement.generated.User;

// TODO: Review with Stephen what we need and what we dont.
@Component
public class SecurityUtils {
  private User getCurrentUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Would be a coding error if this called for a non-authenticated area.
    if (null == authentication) {
      throw new NullPointerException("No user currently authenticated.");
    }

    User userData = new User();
    userData.setLocalAuthorityId(22);
    userData.setEmailAddress(authentication.getName());
    userData.setName("TODO SecurityUtils");
    // @see uk.gov.dft.bluebadge.webapp.la.controller.CreateANewUserController.submit()
    userData.setRoleId(1);

    return userData;
  }

  public int getLocalAuthority() {
    User user = getCurrentUserDetails();
    return user.getLocalAuthorityId();
  }
}
