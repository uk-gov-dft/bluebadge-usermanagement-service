package uk.gov.dft.bluebadge.service.usermanagement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;

@Component
@Slf4j
public class CommonPasswordsFilter {

  private UserManagementRepository repository;

  public CommonPasswordsFilter(UserManagementRepository repository) {
    this.repository = repository;
  }

  public void validatePasswordBlacklisted(String password) {
    boolean blackListed = repository.isPasswordBlacklisted(password);

    if (blackListed) {
      log.info("Password is blacklisted: {}", password);
      throw new BadRequestException(passwordTooCommon());
    }
  }

  private ErrorErrors passwordTooCommon() {
    ErrorErrors error = new ErrorErrors();
    error.setField("password");
    error.setMessage("Blacklisted.password.password");
    error.setReason("Password is too common");

    return error;
  }
}
