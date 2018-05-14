package uk.gov.dft.bluebadge.service.usermanagement.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dft.bluebadge.service.usermanagement.ServiceApplication;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class UserManagementRepositoryTest {

  @Autowired private UserManagementRepository userManagementRepository;

  @Test
  public void checkUserExistsForEmail_false() {
    // Given address not in database
    String emailAddress = UUID.randomUUID().toString();

    // When check user exists for email address is called
    boolean exists = userManagementRepository.checkUserExistsForEmail(emailAddress);

    // Then existance is false
    Assert.assertFalse(exists);
  }
}
