package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import java.util.List;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

public abstract class BlueBadgeBusinessException extends Exception {
  private List<ErrorErrors> errorsList;

  public BlueBadgeBusinessException(List<ErrorErrors> errorsList) {
    this.errorsList = errorsList;
  }

  public List<ErrorErrors> getErrorsList() {
    return errorsList;
  }
}
