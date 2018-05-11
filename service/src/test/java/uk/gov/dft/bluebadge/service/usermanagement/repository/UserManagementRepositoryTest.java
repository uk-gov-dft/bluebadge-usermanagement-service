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
    boolean exists = userManagementRepository.checkUserExistsForEmail("blah@blah.com");

    // Then existance is false
    Assert.assertFalse(exists);
  }

  private void givenAUserExistsWithEmailAddress(String emailAddress) {
//    Optional<UserEntity> existing = userManagementRepository.selectLocalAuthorityById(id);
/*
    if (!existing.isPresent()) {
      UserEntity newLA = new UserEntity();
      newLA.setId(-1);
      newLA.setLocalAuthorityAreaId("England");
      newLA.setName("TestLA");
      userManagementRepository.createLocalAuthority(newLA);
    }
    */
  }

  private void givenNoLocalAuthorityWithId(long id) {
  //  Optional<UserEntity> existing = userManagementRepository.selectLocalAuthorityById(id);

//    if (existing.isPresent()) {
 //     userManagementRepository.deleteLocalAuthority(id);
 //   }
  }

}
