package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

import java.util.List;

public class EmailExistsException extends BlueBadgeBusinessException {
  public EmailExistsException(List<ErrorErrors> errorsList) {
    super(errorsList);
  }
}
