package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

public class BadResponseException extends RuntimeException {
  private String message;
  private List<ErrorErrors> errorsList;

  public BadResponseException() {
    this.errorsList = new ArrayList<ErrorErrors>();
  }

  public BadResponseException(List<ErrorErrors> errors) {
    this.errorsList = errors;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<ErrorErrors> getErrorsList() {
    return errorsList;
  }

  public void addError(ErrorErrors error) {
    this.errorsList.add(error);
  }

  public void setErrorsList(List<ErrorErrors> errorsList) {
    this.errorsList = errorsList;
  }
}
