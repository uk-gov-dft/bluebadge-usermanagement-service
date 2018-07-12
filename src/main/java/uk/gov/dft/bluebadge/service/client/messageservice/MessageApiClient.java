package uk.gov.dft.bluebadge.service.client.messageservice;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.client.RestTemplateFactory;
import uk.gov.dft.bluebadge.service.client.common.ServiceConfiguration;
import uk.gov.dft.bluebadge.service.client.messageservice.model.GenericMessageRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetSuccessRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.UuidResponse;

@Slf4j
@Service
public class MessageApiClient {

  static final String SEND_MESSAGE_URL = "/messages";

  private ServiceConfiguration messageServiceConfiguration;
  private RestTemplateFactory restTemplateFactory;

  @Autowired
  public MessageApiClient(
      ServiceConfiguration messageServiceConfiguration, RestTemplateFactory restTemplateFactory) {
    this.messageServiceConfiguration = messageServiceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  private UuidResponse sendMessage(GenericMessageRequest messageRequest) {
    return restTemplateFactory
        .getInstance()
        .postForObject(
            messageServiceConfiguration.getUrlPrefix() + SEND_MESSAGE_URL,
            messageRequest,
            UuidResponse.class);
  }

  /**
   * @param resetRequest with the user id property set to the user we want to send the email to.
   * @return a user with the given user id and the guid set.
   */
  public UUID sendPasswordResetEmail(PasswordResetRequest resetRequest) {
    log.debug(
        "Calling message service to request password email. PasswordResetRequest:{}", resetRequest);
    Assert.notNull(resetRequest, "must be set");
    UuidResponse response = sendMessage(resetRequest);
    return UUID.fromString(response.getData().getUuid());
  }
  public UUID sendEmailLinkMessage(GenericMessageRequest resetRequest) {
    log.debug(
        "Calling message service to request email link message:{}", resetRequest);
    Assert.notNull(resetRequest, "must be set");
    UuidResponse response = sendMessage(resetRequest);
    return UUID.fromString(response.getData().getUuid());
  }
  public UUID sendPasswordResetSuccessMessage(PasswordResetSuccessRequest request) {
    log.debug(
        "Calling message service to request message:{}", request);
    Assert.notNull(request, "must be set");
    UuidResponse response = sendMessage(request);
    return UUID.fromString(response.getData().getUuid());
  }
}
