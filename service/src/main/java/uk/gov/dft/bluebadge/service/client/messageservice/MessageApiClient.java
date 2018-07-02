package uk.gov.dft.bluebadge.service.client.messageservice;

import static uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient.Endpoints.SEND_EMAIL_ENDPOINT;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.client.RestTemplateFactory;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.UuidResponse;

@Slf4j
@Service
public class MessageApiClient {

  static class Endpoints {
    public Endpoints() {}

    static final String SEND_EMAIL_ENDPOINT = "/messages/send***REMOVED***-email";
  }

  private MessageServiceConfiguration messageServiceConfiguration;

  private RestTemplateFactory restTemplateFactory;

  @Autowired
  public MessageApiClient(
      MessageServiceConfiguration serviceConfiguration, RestTemplateFactory restTemplateFactory) {
    this.messageServiceConfiguration = serviceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  /**
   * @param resetRequest with the user id property set to the user we want to send the email to.
   * @return a user with the given user id and the guid set.
   */
  public UUID sendPasswordResetEmail(PasswordResetRequest resetRequest) {
    log.debug(
        "Calling message service to request password email. User:{}", resetRequest.getUserId());
    Assert.notNull(resetRequest, "must be set");

    HttpEntity<PasswordResetRequest> request = new HttpEntity<>(resetRequest);

    UuidResponse response =
        restTemplateFactory
            .getInstance()
            .postForObject(
                messageServiceConfiguration.getUrlPrefix() + SEND_EMAIL_ENDPOINT,
                request,
                UuidResponse.class);
    return UUID.fromString(response.getData().getUuid());
  }
}
