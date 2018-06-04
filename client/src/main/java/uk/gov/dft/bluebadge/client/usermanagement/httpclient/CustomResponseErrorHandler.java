package uk.gov.dft.bluebadge.client.usermanagement.httpclient;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    HttpStatus statusCode = response.getStatusCode();
    if(HttpStatus.Series.SUCCESSFUL == statusCode.series() ||
        HttpStatus.BAD_REQUEST == statusCode) {
      return false;
    }
    return true;
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    super.handleError(response);
  }
}
