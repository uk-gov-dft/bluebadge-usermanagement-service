package uk.gov.dft.bluebadge.service.usermanagement.repository;

import java.util.List;
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
  public void retrieveUsersByAuthorityId() {
    // Given the authority has no users
    int authorityId = -1;
    UserEntity queryParams = new UserEntity();
    queryParams.setLocalAuthorityId(authorityId);

    // When users are retrieved for the authority
    List<UserEntity> userEntityList =
        userManagementRepository.retrieveUsersByAuthorityId(queryParams);

    // Then an empty list is returned
    Assert.assertEquals(0, userEntityList.size());
  }
}
