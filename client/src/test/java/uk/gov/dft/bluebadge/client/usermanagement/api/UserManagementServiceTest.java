package uk.gov.dft.bluebadge.client.usermanagement.api;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserManagementServiceTest {

  @Autowired
  private UserManagementService userManagementService;

  @Ignore
  @org.junit.Test
  public void getById() {
  }


  @Ignore
  @org.junit.Test
  public void createUser() {
    // TODO add read test.  Used this to invoke client->server in dev.
    User newUser = new User();
    newUser.setEmailAddress("ryy65.y@z");
    newUser.setLocalAuthorityId(2);
    newUser.setName("Paul");
    UserResponse response = userManagementService.createUser(2, newUser);
  }
}