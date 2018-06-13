package uk.gov.dft.bluebadge.client.usermanagement.api;

import org.junit.Assert;
import org.junit.Before;
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
import uk.gov.dft.bluebadge.model.usermanagement.UsersResponse;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserManagementClientTest {

  @Autowired private UserManagementClient userManagementClient;

  @Autowired private RestTemplateFactory restTemplateFactory;

  private MockRestServiceServer mockServer;

  @Autowired private ServiceConfiguration serviceConfiguration;

  @Before
  @Autowired
  public void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplateFactory.getInstance());
  }

  @Test
  public void getUsersForAuthority() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/authorities/2/users?name=Blah";
    mockServer
        .expect(method(HttpMethod.GET))
        .andExpect(requestTo(requestUrl))
        .andRespond(
            withSuccess(
                "{\"apiVersion\":null,\"context\":null,\"id\":null,"
                    + "\"method\":null,\"errors\":null,\"data\":{\"totalItems"
                    + "\":2,\"users\":[{\"id\":1,\"name\":\"Bob\","
                    + "\"emailAddress\":\"blah@blah.com\",\"localAuthorityId\":2}"
                    + ",{\"id\":3,\"name\":\"Bob2\",\"emailAddress\":"
                    + "\"blah2@blah.com\",\"localAuthorityId\":2}]}}",
                MediaType.APPLICATION_JSON));

    UsersResponse userList = userManagementClient.getUsersForAuthority(2, "Blah");
    Assert.assertTrue(userList.getData().getTotalItems().equals(2));
    Assert.assertTrue(userList.getData().getUsers().size() == 2);

    mockServer.verify();
  }

  @Test
  public void checkUserExistsByEmail() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/users?emailAddress=blah@blah.com";
    mockServer
        .expect(method(HttpMethod.GET))
        .andExpect(requestTo(requestUrl))
        .andRespond(withSuccess("{\"data\":{\"totalItems\":1}}", MediaType.APPLICATION_JSON));

    Boolean exists = userManagementClient.checkUserExistsForEmail("blah@blah.com");
    Assert.assertTrue(exists);
    mockServer.verify();
  }

  @Test
  public void updateUser() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/authorities/2/users/-1";
    mockServer
        .expect(method(HttpMethod.PUT))
        .andExpect(requestTo(requestUrl))
        .andRespond(withSuccess("{\"data\":{\"updated\":1}}", MediaType.APPLICATION_JSON));
    User user = new User();
    user.setId(-1);
    user.setLocalAuthorityId(2);
    UserResponse ur = userManagementClient.updateUser(user);
    Assert.assertTrue(ur.getData().getUpdated() == 1);
    mockServer.verify();
  }

  @Test
  public void deleteUser() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/authorities/2/users/-1";
    mockServer
        .expect(method(HttpMethod.DELETE))
        .andExpect(requestTo(requestUrl))
        .andRespond(withNoContent());
    userManagementClient.deleteUser(2, -1);
    mockServer.verify();
  }

  @Test
  public void createUser() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/authorities/2/users";
    mockServer
        .expect(method(HttpMethod.POST))
        .andExpect(requestTo(requestUrl))
        .andRespond(withSuccess("{\"data\":{\"updated\":1}}", MediaType.APPLICATION_JSON));
    User user = new User();
    user.setId(-1);
    user.setLocalAuthorityId(2);
    UserResponse ur = userManagementClient.createUser(2, user);
    Assert.assertTrue(ur.getData().getUpdated() == 1);
    mockServer.verify();
  }

  @Test
  public void getById() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/authorities/2/users/-1";
    mockServer
        .expect(method(HttpMethod.GET))
        .andExpect(requestTo(requestUrl))
        .andRespond(withSuccess("{\"data\":{\"updated\":0}}", MediaType.APPLICATION_JSON));

    UserResponse ur = userManagementClient.getById(2, -1);
    Assert.assertTrue(ur.getData().getUpdated() == 0);
    mockServer.verify();
  }
}
