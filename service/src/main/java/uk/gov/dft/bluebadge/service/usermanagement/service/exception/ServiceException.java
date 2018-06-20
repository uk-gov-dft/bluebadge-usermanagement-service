package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import uk.gov.dft.bluebadge.model.usermanagement.CommonResponse;
import uk.gov.dft.bluebadge.model.usermanagement.Error;

class ServiceException extends RuntimeException {
  CommonResponse commonResponse;

  ServiceException() {
    super();
    commonResponse = new CommonResponse();
    Error error = new Error();
    commonResponse.setError(error);
  }

  public CommonResponse getCommonResponse() {
    return commonResponse;
  }
}
