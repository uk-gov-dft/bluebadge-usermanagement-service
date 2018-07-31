package uk.gov.dft.bluebadge.service.client.messageservice;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.service.client.messageservice.model.GenericMessageRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetSuccessRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.UuidResponse;

@Slf4j
@Service
public class MessageApiClient {

  static final String SEND_MESSAGE_URL = "/messages";

  private final RestTemplate restTemplate;

  @Autowired
  public MessageApiClient(@Qualifier("messageServiceRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  private UuidResponse sendMessage(GenericMessageRequest messageRequest) {
    return restTemplate.postForObject(SEND_MESSAGE_URL, messageRequest, UuidResponse.class);
  }

  public UUID sendEmailLinkMessage(GenericMessageRequest resetRequest) {
    log.debug("Calling message service to request email link message:{}", resetRequest);
    Assert.notNull(resetRequest, "must be set");
    UuidResponse response = sendMessage(resetRequest);
    return UUID.fromString(response.getData().getUuid());
  }

  public UUID sendPasswordResetSuccessMessage(PasswordResetSuccessRequest request) {
    log.debug("Calling message service to request message:{}", request);
    Assert.notNull(request, "must be set");
    UuidResponse response = sendMessage(request);
    return UUID.fromString(response.getData().getUuid());
  }
}
