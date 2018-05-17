package uk.gov.dft.bluebadge.service.usermanagement.repository;

import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dft.bluebadge.service.usermanagement.ServiceApplication;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

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

    // Then existence is false
    Assert.assertFalse(exists);
  }

  @Test
  public void retrieveUsersByAuthorityId() {
    // Given the authority has no users
    int authorityId = -1;

    // When users are retrieved for the authority
    List<UserEntity> userEntityList =
        userManagementRepository.retrieveUsersByAuthorityId(authorityId);

    // Then an empty list is returned
    Assert.assertEquals(0, userEntityList.size());
  }
}
