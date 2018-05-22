package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

import java.util.List;

public abstract class BlueBadgeBusinessException extends Exception{
  private List<ErrorErrors> errorsList;

  public BlueBadgeBusinessException(List<ErrorErrors> errorsList) {
    this.errorsList = errorsList;
  }

  public List<ErrorErrors> getErrorsList() {
    return errorsList;
  }
}
