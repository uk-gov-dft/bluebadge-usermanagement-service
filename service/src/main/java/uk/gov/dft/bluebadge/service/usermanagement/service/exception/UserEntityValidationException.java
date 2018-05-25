package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import java.util.List;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

public class UserEntityValidationException extends BlueBadgeBusinessException {
  public UserEntityValidationException(List<ErrorErrors> errorsList) {
    super(errorsList);
  }
}
