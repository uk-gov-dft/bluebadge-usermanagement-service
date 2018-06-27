package uk.gov.dft.bluebadge.service.usermanagement.service.exception;

import com.google.common.collect.Lists;
import java.util.List;
import uk.gov.dft.bluebadge.model.usermanagement.generated.ErrorErrors;

public class BadRequestException extends ServiceException {

  public BadRequestException(List<ErrorErrors> fieldErrors) {
    super();
    commonResponse.getError().setErrors(fieldErrors);
  }

  public BadRequestException(ErrorErrors fieldError) {
    this(Lists.newArrayList(fieldError));
  }
}
