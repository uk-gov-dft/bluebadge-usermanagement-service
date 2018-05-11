package uk.gov.dft.bluebadge.service.usermanagement.repository;

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
  public void createLocalAuthority() {
    givenNoLocalAuthorityWithId(-1L);
    UserEntity la = new UserEntity();
    la.setId(-1);
    la.setName("TestCreate");
    //    la.setLocalAuthorityAreaId("Scotland");
    //    int result = userManagementRepository.createLocalAuthority(la);
    //    Assert.assertEquals(result, 1);
  }

  @Test
  public void selectLocalAuthorityById() {
    givenALocalAuthorityExistsWithId(-1L);
    // When local authority selected.
    //    Optional<UserEntity> selected =
    //        userManagementRepository.selectLocalAuthorityById(-1L);
    //    // Then local authority exists.
    //    Assert.assertTrue(selected.isPresent());
  }

  @Test
  public void selectLocalAuthorities() {
    givenALocalAuthorityExistsWithId(-1L);
    //    List<UserEntity> las = userManagementRepository.selectLocalAuthorities();
    //    Assert.assertTrue(las.size() > 0);
  }

  @Test
  public void updateLocalAuthority() {
    givenALocalAuthorityExistsWithId(-1L);
    UserEntity toUpdate = new UserEntity();
    toUpdate.setName("TestUpdatedName");
    toUpdate.setId(-1);
    //    toUpdate.setLocalAuthorityAreaId("Scotland");
    //    int result = userManagementRepository.updateLocalAuthority(toUpdate);
    //    Assert.assertEquals(result, 1);
  }

  @Test
  public void deleteLocalAuthority() {
    givenALocalAuthorityExistsWithId(-1L);
    //    int result = userManagementRepository.deleteLocalAuthority(-1L);
    //    Assert.assertEquals(result, 1);
    //    Optional<UserEntity> la = userManagementRepository.selectLocalAuthorityById(-1L);
    //    Assert.assertFalse(la.isPresent());
  }

  private void givenALocalAuthorityExistsWithId(long id) {
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
