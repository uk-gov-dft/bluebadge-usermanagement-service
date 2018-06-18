package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;

public class BadRequestException extends RuntimeException {
  private String message;
  private List<ErrorErrors> errorsList;

  public BadRequestException() {
    this.errorsList = new ArrayList<ErrorErrors>();
  }

  public BadRequestException(List<ErrorErrors> errors) {
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

  public Boolean hasErrors() {
    return this.errorsList.size() > 0;
  }
}
