package uk.gov.dft.bluebadge.client.usermanagement.api;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import uk.gov.dft.bluebadge.client.usermanagement.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.usermanagement.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserManagementServiceTest {

  @Autowired
  private UserManagementService userManagementService;

  @Autowired
  private RestTemplateFactory restTemplateFactory;

  MockRestServiceServer mockServer;

  @Autowired
  private ServiceConfiguration serviceConfiguration;

  @Before
  @Autowired
  public void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplateFactory.getInstance());
  }

  @Ignore
  @org.junit.Test
  public void createUser() {
    // TODO add real test.  Used this to invoke client->server in dev.
    User newUser = new User();
    newUser.setEmailAddress("ryy65.y@z");
    newUser.setLocalAuthorityId(2);
    newUser.setName("Paul");
    UserResponse response = userManagementService.createUser(2, newUser);
  }


  @Test
  public void checkUserExistsByEmail() {
    String requestUrl = serviceConfiguration.getUrlPrefix()
            + "/users?emailAddress=blah@blah.com";
    mockServer.expect(method(HttpMethod.GET)).andExpect(requestTo(requestUrl))
            .andRespond(withSuccess("false", MediaType.APPLICATION_JSON));

    Boolean exists = userManagementService.checkUserExistsForEmail("blah@blah.com");
    mockServer.verify();
  }
}