package uk.gov.dft.bluebadge.client.usermanagement.api;

import org.junit.Assert;
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
import uk.gov.dft.bluebadge.model.usermanagement.UsersResponse;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserManagementServiceTest {

  @Autowired private UserManagementService userManagementService;

  @Autowired private RestTemplateFactory restTemplateFactory;

  MockRestServiceServer mockServer;

  @Autowired private ServiceConfiguration serviceConfiguration;

  @Before
  @Autowired
  public void setUp() {
    mockServer = MockRestServiceServer.createServer(restTemplateFactory.getInstance());
  }


  /*
  @Test
  public void deleteMe(){
    User user = new User();
    user.setName("Peter");
    user.setLocalAuthorityId(2);
    user.setEmailAddress("valid666@dft.gov.uk");
    UserResponse ur = userManagementService.getUserForEmail("Vaklid666@dft.gov.uk");
    System.out.println(user);
  }
  */

  @Test
  public void getUsersForAuthority() {
    String requestUrl = serviceConfiguration.getUrlPrefix() + "/authorities/2/users?name=Blah";
    mockServer
        .expect(method(HttpMethod.GET))
        .andExpect(requestTo(requestUrl))
        .andRespond(
            withSuccess(
                "{\"apiVersion\":null,\"context\":null,\"id\":null," +
                        "\"method\":null,\"errors\":null,\"data\":{\"totalItems" +
                        "\":2,\"users\":[{\"id\":1,\"name\":\"Bob\"," +
                        "\"emailAddress\":\"blah@blah.com\",\"localAuthorityId\":2}" +
                        ",{\"id\":3,\"name\":\"Bob2\",\"emailAddress\":" +
                        "\"blah2@blah.com\",\"localAuthorityId\":2}]}}",
                MediaType.APPLICATION_JSON));

    UsersResponse userList = userManagementService.getUsersForAuthority(2, "Blah");
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

    Boolean exists = userManagementService.checkUserExistsForEmail("blah@blah.com");
    Assert.assertTrue(exists);
    mockServer.verify();
  }
}
