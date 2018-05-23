package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import java.util.List;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

public class EmailExistsException extends BlueBadgeBusinessException {
  public EmailExistsException(List<ErrorErrors> errorsList) {
    super(errorsList);
  }
}
