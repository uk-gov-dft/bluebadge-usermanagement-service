package uk.gov.dft.bluebadge.service.client.messageservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient.SEND_MESSAGE_URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.service.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.client.RestTemplateFactory;
import uk.gov.dft.bluebadge.service.client.TestServiceConfiguration;
import uk.gov.dft.bluebadge.service.client.common.ServiceConfiguration;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.UuidResponse;
import uk.gov.dft.bluebadge.service.client.messageservice.model.UuidResponseData;

@RunWith(MockitoJUnitRunner.class)
public class MessageApiClientTest extends ApplicationContextTests {
  @Mock private RestTemplateFactory mockRestTemplateFactory;

  private MessageApiClient client;
  private MockRestServiceServer mockServer;
  private ObjectMapper om = new ObjectMapper();
  private UuidResponse uuidResponse;
  private ServiceConfiguration serviceConfiguration;

  @Before
  public void setUp() throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    when(mockRestTemplateFactory.getInstance()).thenReturn(restTemplate);

    serviceConfiguration = TestServiceConfiguration.full().contextpath("testMessageClient").build();

    client = new MessageApiClient(serviceConfiguration, mockRestTemplateFactory);

    UUID uuid = UUID.randomUUID();
    uuidResponse = new UuidResponse().data(new UuidResponseData().uuid(uuid.toString()));
  }

  @SneakyThrows
  @Test
  public void whenPasswordResetRequest_thenGenericMessageSent_andAttributesSet() {
    mockServer
        .expect(once(), requestTo(serviceConfiguration.getUrlPrefix() + SEND_MESSAGE_URL))
        .andExpect(method(HttpMethod.POST))
        .andExpect(jsonPath("template", equalTo(PasswordResetRequest.PASSWORD_RESET_TEMPLATE)))
        .andExpect(jsonPath("emailAddress", equalTo("bob@bob.com")))
        .andExpect(jsonPath("attributes.name", equalTo("bob")))
        .andExpect(jsonPath("attributes. ***REMOVED***)))
        .andRespond(withSuccess(om.writeValueAsString(uuidResponse), MediaType.APPLICATION_JSON));

    PasswordResetRequest passwordResetRequest =
        PasswordResetRequest.builder()
            .emailAddress("bob@bob.com")
            .name("bob")
            . ***REMOVED***)
            .build();
    UUID uuid = client.sendPasswordResetEmail(passwordResetRequest);

    assertThat(uuid).isNotNull();
    assertThat(uuid.toString()).isEqualTo(uuidResponse.getData().getUuid());
  }
}
