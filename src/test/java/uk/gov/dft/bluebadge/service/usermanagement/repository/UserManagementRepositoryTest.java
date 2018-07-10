package uk.gov.dft.bluebadge.service.usermanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class UserManagementRepositoryTest extends ApplicationContextTests {
  public static final int DEFAULT_USER_ID = -1;
  public static final String DEFAULT_USER_EMAIL_LINK_UUID = "7d75652a-4e84-41e2-bd82-e5b5933b81da";

  @Autowired UserManagementRepository userManagementRepository;

  @Test
  public void retrieveUserById_exists() throws Exception {
    Optional<UserEntity> maybeUserEntity =
        userManagementRepository.retrieveUserById(DEFAULT_USER_ID);
    UserEntity userEntity = checkDefaultUser(maybeUserEntity);

    assertThat(userEntity.getPassword()).isNull();
  }

  @Test
  public void retrieveUserById_notExists() throws Exception {
    Optional<UserEntity> maybeUserEntity = userManagementRepository.retrieveUserById(-99999);
    assertThat(maybeUserEntity).isNotNull();
    assertThat(maybeUserEntity.isPresent()).isFalse();
  }

  @Test
  public void findUsers_byAuthorityId() throws Exception {
    UserEntity params = new UserEntity();
    params.setLocalAuthorityId(2);
    List<UserEntity> users = userManagementRepository.findUsers(params);
    assertThat(users).extracting("localAuthorityId").containsOnly(2);
  }
  @Test
  public void findUsers_byAuthorityId_noResults() throws Exception {
    UserEntity params = new UserEntity();
    params.setLocalAuthorityId(200);
    List<UserEntity> users = userManagementRepository.findUsers(params);
    assertThat(users).isEmpty();
  }
  @Test
  public void findUsers_byAuthorityIdAndName() throws Exception {
    UserEntity params = new UserEntity();
    params.setLocalAuthorityId(2);
    params.setName("Sampath");
    List<UserEntity> users = userManagementRepository.findUsers(params);
    assertThat(users).extracting("localAuthorityId").containsOnly(2);
    assertThat(users).extracting("name").containsOnly("Sampath");
  }

  @Test
  public void updateUser_fieldsUpdated() throws Exception {
    UserEntity userEntity = userManagementRepository.retrieveUserById(-1).get();
    userEntity.setName("Bob");
    userEntity.setEmailAddress("bob@bob.com");
    userEntity.setLocalAuthorityId(99);
    userEntity.setRoleId(3);

    int i = userManagementRepository.updateUser(userEntity);
    assertThat(i).isEqualTo(1);

    UserEntity updatedUserEntity = userManagementRepository.retrieveUserById(-1).get();
    assertThat(updatedUserEntity).isNotSameAs(userEntity);
    assertThat(updatedUserEntity.getName()).isEqualTo("Bob");
    assertThat(updatedUserEntity.getEmailAddress()).isEqualTo("bob@bob.com");
    assertThat(updatedUserEntity.getLocalAuthorityId()).isEqualTo(99);
    assertThat(updatedUserEntity.getRoleId()).isEqualTo(3);
  }

  @Test
  public void updateUser_userNotExist() throws Exception {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(99999);
    int i = userManagementRepository.updateUser(userEntity);
    assertThat(i).isEqualTo(0);
  }

  @Test
  public void updatePassword() throws Exception {
    UserEntity userEntity = userManagementRepository.retrieveUserById(-1).get();
    userEntity.setPassword("newPassword");
    int i = userManagementRepository.updatePassword(userEntity);
    assertThat(i).isEqualTo(1);
  }
  @Test
  public void updatePassword_notExist() throws Exception {
    UserEntity userEntity = userManagementRepository.retrieveUserById(-1001).get();
    userEntity.setPassword("newPassword");
    int i = userManagementRepository.updatePassword(userEntity);
    assertThat(i).isEqualTo(0);
  }

  @Test
  public void retrieveEmailLinkWithUuid() throws Exception {
    EmailLink emailLink =
        userManagementRepository.retrieveEmailLinkWithUuid(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink).isNotNull();
    assertThat(emailLink.getUuid()).isEqualTo(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink.getUserId()).isEqualTo(-1);
    assertThat(emailLink.getIsActive()).isEqualTo(true);
  }

  @Test
  public void updateEmailLinkToInvalid() throws Exception {
    userManagementRepository.updateEmailLinkToInvalid(DEFAULT_USER_EMAIL_LINK_UUID);

    EmailLink emailLink =
        userManagementRepository.retrieveEmailLinkWithUuid(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink).isNotNull();
    assertThat(emailLink.getUuid()).isEqualTo(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink.getUserId()).isEqualTo(-1);

    assertThat(emailLink.getIsActive()).isEqualTo(false);
  }

  @Test
  public void retrieveUserUsingEmailLinkUuid() throws Exception {
    Optional<UserEntity> userEntity =
        userManagementRepository.retrieveUserUsingEmailLinkUuid(DEFAULT_USER_EMAIL_LINK_UUID);
    checkDefaultUser(userEntity);
  }
  @Test
  public void retrieveUserUsingEmailLinkUuid_notExist() throws Exception {
    Optional<UserEntity> userEntity =
        userManagementRepository.retrieveUserUsingEmailLinkUuid(UUID.randomUUID().toString());
    assertThat(userEntity).isNotNull();
    assertThat(userEntity.isPresent()).isFalse();
  }

  @Test
  public void createUser() throws Exception {
    UserEntity userEntity = new UserEntity();
    userEntity.setName("Jane");
    userEntity.setEmailAddress("jane@jane.com");
    userEntity.setLocalAuthorityId(45);
    userEntity.setRoleId(2);

    userManagementRepository.createUser(userEntity);

    UserEntity search = new UserEntity();
    search.setLocalAuthorityId(45);
    search.setName("Jane");
    search.setEmailAddress("jane@jane.com");
    List<UserEntity> users = userManagementRepository.findUsers(search);
    assertThat(users).isNotEmpty();
    assertThat(users.size()).isEqualTo(1);

    UserEntity updatedUser = users.get(0);
    assertThat(updatedUser.getId()).isNotNull();
    assertThat(updatedUser.getName()).isEqualTo("Jane");
    assertThat(updatedUser.getEmailAddress()).isEqualTo("jane@jane.com");
    assertThat(updatedUser.getLocalAuthorityId()).isEqualTo(45);
    assertThat(updatedUser.getRoleId()).isEqualTo(2);
    assertThat(updatedUser.getRoleName()).isEqualTo("LA Admin");
  }

  @Test
  public void deleteUser() throws Exception {
    int i = userManagementRepository.deleteUser(-1);
    assertThat(i).isEqualTo(1);

    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserById(-1);
    assertThat(userEntity.isPresent()).isFalse();
  }
  @Test
  public void deleteUser_notExists() throws Exception {
    int i = userManagementRepository.deleteUser(-1001);
    assertThat(i).isEqualTo(0);
  }

  @Test
  public void emailAddressAlreadyUsed() throws Exception {
    UserEntity userEntity = new UserEntity();
    userEntity.setEmailAddress("abc@dft.gov.uk");
    boolean result = userManagementRepository.emailAddressAlreadyUsed(userEntity);
    assertThat(result).isTrue();

    userEntity.setEmailAddress("xxyyxyxyx@dft.gov.uk");
    result = userManagementRepository.emailAddressAlreadyUsed(userEntity);
    assertThat(result).isFalse();
  }

  @Test
  public void createEmailLink() throws Exception {
    String uuid = UUID.randomUUID().toString();
    EmailLink emailLink = EmailLink.builder().userId(-1).webappUri("blah").uuid(uuid).build();
    userManagementRepository.createEmailLink(emailLink);

    EmailLink dbEmailLink = userManagementRepository.retrieveEmailLinkWithUuid(uuid);
    assertThat(dbEmailLink).isNotNull();
    assertThat(dbEmailLink.getUuid()).isEqualTo(uuid);
    assertThat(dbEmailLink.getUserId()).isEqualTo(-1);
    assertThat(dbEmailLink.getIsActive()).isTrue();
  }

  @Test
  public void updateUserToInactive() throws Exception {
    int i = userManagementRepository.updateUserToInactive(-1);
    assertThat(i).isEqualTo(1);
    // Currently no way of retrieving the inactive flag
  }

  private UserEntity checkDefaultUser(Optional<UserEntity> maybeUserEntity) {
    assertThat(maybeUserEntity).isNotNull();
    assertThat(maybeUserEntity.isPresent()).isTrue();
    UserEntity userEntity = maybeUserEntity.get();
    assertThat(userEntity.getId()).isEqualTo(-1);
    assertThat(userEntity.getName()).isEqualTo("Sampath");
    assertThat(userEntity.getEmailAddress()).isEqualTo("abc@dft.gov.uk");
    assertThat(userEntity.getLocalAuthorityId()).isEqualTo(2);
    assertThat(userEntity.getRoleId()).isEqualTo(2);
    assertThat(userEntity.getRoleName()).isEqualTo("LA Admin");
    return userEntity;
  }
}
