package uk.gov.dft.bluebadge.service.usermanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.LocalAuthorityEntity;

@RunWith(SpringRunner.class)
@Transactional
public class LocalAuthorityRepositoryTest extends ApplicationContextTests {
  public static final int LOCAL_AUTHORITY_ID = 2;

  @Autowired LocalAuthorityRepository localAuthorityRepository;

  @Test
  public void retrieveUserById_exists() throws Exception {
    LocalAuthorityEntity localAuthority =
        localAuthorityRepository.retrieveLocalAuthorityById(LOCAL_AUTHORITY_ID);
    assertThat(localAuthority).isNotNull();
    assertThat(localAuthority.getId()).isEqualTo(LOCAL_AUTHORITY_ID);
    assertThat(localAuthority.getName()).isEqualTo("Aberdeenshire Council");
    assertThat(localAuthority.getAreaName()).isEqualTo("Scotland");
  }

  @Test
  public void retrieveUserById_notExists() throws Exception {
    LocalAuthorityEntity localAuthority =
        localAuthorityRepository.retrieveLocalAuthorityById(-99999);
    assertThat(localAuthority).isNull();
  }
}
