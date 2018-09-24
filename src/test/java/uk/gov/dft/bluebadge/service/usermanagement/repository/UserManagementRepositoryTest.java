package uk.gov.dft.bluebadge.service.usermanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.*;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class UserManagementRepositoryTest extends ApplicationContextTests {
  private static final UUID DEFAULT_USER_UUID =
      UUID.fromString("0093daf9-2782-47f8-93dc-bdf073204d6c");
  private static final String DEFAULT_LOCAL_AUTHORITY_CODE = "BIRM";
  private static final String OTHER_LOCAL_AUTHORITY_CODE = "MANC";
  private static final UuidAuthorityCodeParams DEFAULT_RETRIEVE_BY_USER_ID_PARAMS =
      UuidAuthorityCodeParams.builder()
          .uuid(DEFAULT_USER_UUID)
          .authorityCode(DEFAULT_LOCAL_AUTHORITY_CODE)
          .build();
  private static final UuidAuthorityCodeParams NO_LOCAL_AUTHORITY_RETRIEVE_BY_USER_ID_PARAMS =
      UuidAuthorityCodeParams.builder().uuid(DEFAULT_USER_UUID).authorityCode(null).build();
  private static final UuidAuthorityCodeParams DEFAULT_DELETE_USER_PARAMS =
      UuidAuthorityCodeParams.builder()
          .uuid(DEFAULT_USER_UUID)
          .authorityCode(DEFAULT_LOCAL_AUTHORITY_CODE)
          .build();

  private static final String DEFAULT_USER_EMAIL_LINK_UUID = "7d75652a-4e84-41e2-bd82-e5b5933b81da";

  @Autowired private UserManagementRepository userManagementRepository;

  @Test
  public void retrieveUserById_exists() {
    Optional<UserEntity> maybeUserEntity =
        userManagementRepository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS);
    UserEntity userEntity = checkDefaultUser(maybeUserEntity);

    assertThat(userEntity.getPassword()).isNull();
  }

  @Test
  public void retrieveUserById_existsAndNoLocalAuthorityProvided() {
    Optional<UserEntity> maybeUserEntity =
        userManagementRepository.retrieveUserByUuid(NO_LOCAL_AUTHORITY_RETRIEVE_BY_USER_ID_PARAMS);
    UserEntity userEntity = checkDefaultUser(maybeUserEntity);

    assertThat(userEntity.getPassword()).isNull();
  }

  @Test
  public void retrieveUserById_existsInDifferentLocalAuthority() {
    UuidAuthorityCodeParams params =
        UuidAuthorityCodeParams.builder()
            .uuid(DEFAULT_USER_UUID)
            .authorityCode(OTHER_LOCAL_AUTHORITY_CODE)
            .build();
    Optional<UserEntity> maybeUserEntity = userManagementRepository.retrieveUserByUuid(params);

    assertThat(maybeUserEntity).isEmpty();
  }

  @Test
  public void retrieveUserById_notExists() {
    UuidAuthorityCodeParams params =
        UuidAuthorityCodeParams.builder()
            .uuid(UUID.randomUUID())
            .authorityCode(DEFAULT_LOCAL_AUTHORITY_CODE)
            .build();

    Optional<UserEntity> maybeUserEntity = userManagementRepository.retrieveUserByUuid(params);
    assertThat(maybeUserEntity).isNotNull();
    assertThat(maybeUserEntity.isPresent()).isFalse();
  }

  @Test
  public void findUsers_byAuthorityCode() {
    UserEntity params = new UserEntity();
    params.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_CODE);
    List<UserEntity> users = userManagementRepository.findUsers(params);
    assertThat(users).extracting("authorityCode").containsOnly(DEFAULT_LOCAL_AUTHORITY_CODE);
  }

  @Test
  public void findUsers_byAuthorityCode_noResults() {
    UserEntity params = new UserEntity();
    params.setAuthorityCode("ABCD");
    List<UserEntity> users = userManagementRepository.findUsers(params);
    assertThat(users).isEmpty();
  }

  @Test
  public void findUsers_byAuthorityCodeAndName() {
    UserEntity params = new UserEntity();
    params.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_CODE);
    params.setName("Sampath");
    List<UserEntity> users = userManagementRepository.findUsers(params);
    assertThat(users).extracting("authorityCode").containsOnly(DEFAULT_LOCAL_AUTHORITY_CODE);
    assertThat(users).extracting("name").containsOnly("Sampath");
    assertThat(users).extracting("uuid").isNotNull();
  }

  @Test
  public void
      findUsers_byAuthorityCodeAndName_ShouldReturn50FirstResultsOrderedByNameAscendingOrder() {
    final int FIRST_ID = 200000;
    final int LAST_ID = FIRST_ID + 100;
    final int RESULTS_LIMIT = 50;

    List<UserEntity> userEntityList = Lists.newArrayList();
    for (int id = LAST_ID; id > FIRST_ID; id--) {
      UserEntity userEntity =
          createUserEntityExample(
              UUID.randomUUID(),
              "Jane" + id,
              "jane" + id + "@jane.com",
              OTHER_LOCAL_AUTHORITY_CODE,
              2,
              "LA Admin");
      userManagementRepository.createUser(userEntity);
      userEntityList.add(userEntity);
    }
    UserEntity params = new UserEntity();
    params.setAuthorityCode(OTHER_LOCAL_AUTHORITY_CODE);
    params.setName("Jane%");
    List<UserEntity> users = userManagementRepository.findUsers(params);

    userEntityList.sort(Comparator.comparing(UserEntity::getName));
    List<UserEntity> expectedUerEntityList =
        userEntityList
            .stream()
            .limit(RESULTS_LIMIT)
            .peek(u -> u.setPassword(null))
            .collect(Collectors.toList());

    assertThat(users).hasSize(RESULTS_LIMIT);
    assertThat(users).isEqualTo(expectedUerEntityList);
  }

  @Test
  public void updateUser_fieldsUpdated() {
    UserEntity userEntity =
        userManagementRepository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS).get();
    userEntity.setName("Bob");
    userEntity.setEmailAddress("bob@bob.com");
    userEntity.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_CODE);
    userEntity.setRoleId(3);

    int i = userManagementRepository.updateUser(userEntity);
    assertThat(i).isEqualTo(1);

    UserEntity updatedUserEntity =
        userManagementRepository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS).get();
    assertThat(updatedUserEntity).isNotSameAs(userEntity);
    assertThat(updatedUserEntity.getName()).isEqualTo("Bob");
    assertThat(updatedUserEntity.getEmailAddress()).isEqualTo("bob@bob.com");
    assertThat(updatedUserEntity.getAuthorityCode()).isEqualTo(DEFAULT_LOCAL_AUTHORITY_CODE);
    assertThat(updatedUserEntity.getRoleId()).isEqualTo(3);
  }

  @Test
  public void updateUser_userNotExist() {
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.randomUUID());
    int i = userManagementRepository.updateUser(userEntity);
    assertThat(i).isEqualTo(0);
  }

  @Test
  public void updatePassword() {
    UserEntity userEntity =
        userManagementRepository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS).get();
    userEntity.setPassword("newPassword");
    int i = userManagementRepository.updatePassword(userEntity);
    assertThat(i).isEqualTo(1);
  }

  @Test
  public void updatePassword_notExist() {
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.randomUUID());
    userEntity.setPassword("newPassword");
    int i = userManagementRepository.updatePassword(userEntity);
    assertThat(i).isEqualTo(0);
  }

  @Test
  public void retrieveEmailLinkWithUuid() {
    EmailLink emailLink =
        userManagementRepository.retrieveEmailLinkWithUuid(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink).isNotNull();
    assertThat(emailLink.getUuid()).isEqualTo(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
    assertThat(emailLink.getIsActive()).isEqualTo(true);
  }

  @Test
  public void updateEmailLinkToInvalid() {
    userManagementRepository.updateEmailLinkToInvalid(DEFAULT_USER_EMAIL_LINK_UUID);

    EmailLink emailLink =
        userManagementRepository.retrieveEmailLinkWithUuid(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink).isNotNull();
    assertThat(emailLink.getUuid()).isEqualTo(DEFAULT_USER_EMAIL_LINK_UUID);
    assertThat(emailLink.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);

    assertThat(emailLink.getIsActive()).isEqualTo(false);
  }

  @Test
  public void retrieveUserUsingEmailLinkUuid() {
    Optional<UserEntity> userEntity =
        userManagementRepository.retrieveUserUsingEmailLinkUuid(DEFAULT_USER_EMAIL_LINK_UUID);
    checkDefaultUser(userEntity);
  }

  @Test
  public void retrieveUserUsingEmailLinkUuid_notExist() {
    Optional<UserEntity> userEntity =
        userManagementRepository.retrieveUserUsingEmailLinkUuid(UUID.randomUUID().toString());
    assertThat(userEntity).isNotNull();
    assertThat(userEntity.isPresent()).isFalse();
  }

  @Test
  public void createUser() {
    UserEntity userEntity = new UserEntity();
    userEntity.setName("Jane");
    userEntity.setEmailAddress("jane@jane.com");
    userEntity.setAuthorityCode(OTHER_LOCAL_AUTHORITY_CODE);
    userEntity.setRoleId(2);
    userEntity.setPassword("the_password");
    userEntity.setUuid(UUID.randomUUID());

    userManagementRepository.createUser(userEntity);

    UserEntity search = new UserEntity();
    search.setAuthorityCode(OTHER_LOCAL_AUTHORITY_CODE);
    search.setName("Jane");
    search.setEmailAddress("jane@jane.com");
    List<UserEntity> users = userManagementRepository.findUsers(search);
    assertThat(users).isNotEmpty();
    assertThat(users.size()).isEqualTo(1);

    UserEntity updatedUser = users.get(0);
    assertThat(updatedUser.getUuid()).isNotNull();
    assertThat(updatedUser.getName()).isEqualTo("Jane");
    assertThat(updatedUser.getEmailAddress()).isEqualTo("jane@jane.com");
    assertThat(updatedUser.getAuthorityCode()).isEqualTo(OTHER_LOCAL_AUTHORITY_CODE);
    assertThat(updatedUser.getRoleId()).isEqualTo(2);
    assertThat(updatedUser.getRoleName()).isEqualTo("LA Admin");
    // Password should never be retrieved
    assertThat(updatedUser.getPassword()).isNull();
  }

  @Test
  public void deleteUser() {
    int deletedRecords = userManagementRepository.deleteUser(DEFAULT_DELETE_USER_PARAMS);
    assertThat(deletedRecords).isEqualTo(1);

    Optional<UserEntity> userEntity =
        userManagementRepository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS);
    assertThat(userEntity).isEmpty();
  }

  @Test
  public void deleteUser_existsInDifferentLocalAuthority() {
    UuidAuthorityCodeParams deleteParams =
        UuidAuthorityCodeParams.builder()
            .uuid(DEFAULT_USER_UUID)
            .authorityCode(OTHER_LOCAL_AUTHORITY_CODE)
            .build();

    int deletedRecords = userManagementRepository.deleteUser(deleteParams);
    assertThat(deletedRecords).isEqualTo(0);

    Optional<UserEntity> userEntity =
        userManagementRepository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS);
    assertThat(userEntity).isPresent();
  }

  @Test
  public void deleteUser_notExists() {
    UuidAuthorityCodeParams deleteParams =
        UuidAuthorityCodeParams.builder()
            .uuid(UUID.randomUUID())
            .authorityCode(DEFAULT_LOCAL_AUTHORITY_CODE)
            .build();
    int i = userManagementRepository.deleteUser(deleteParams);
    assertThat(i).isEqualTo(0);
  }

  @Test
  public void emailAddressAlreadyUsed() {
    UserEntity userEntity = new UserEntity();
    userEntity.setEmailAddress("abc@dft.gov.uk");
    boolean result = userManagementRepository.emailAddressAlreadyUsed(userEntity);
    assertThat(result).isTrue();

    userEntity.setEmailAddress("xxyyxyxyx@dft.gov.uk");
    result = userManagementRepository.emailAddressAlreadyUsed(userEntity);
    assertThat(result).isFalse();
  }

  @Test
  public void createEmailLink() {
    String uuid = UUID.randomUUID().toString();
    EmailLink emailLink =
        EmailLink.builder().userUuid(DEFAULT_USER_UUID).webappUri("blah").uuid(uuid).build();
    userManagementRepository.createEmailLink(emailLink);

    EmailLink dbEmailLink = userManagementRepository.retrieveEmailLinkWithUuid(uuid);
    assertThat(dbEmailLink).isNotNull();
    assertThat(dbEmailLink.getUuid()).isEqualTo(uuid);
    assertThat(dbEmailLink.getUserUuid()).isEqualTo(DEFAULT_USER_UUID);
    assertThat(dbEmailLink.getIsActive()).isTrue();
  }

  @Test
  public void updateUserToInactive() {
    UuidAuthorityCodeParams params =
        UuidAuthorityCodeParams.builder().uuid(DEFAULT_USER_UUID).build();
    int i = userManagementRepository.updateUserToInactive(params);
    assertThat(i).isEqualTo(1);
    // Currently no way of retrieving the inactive flag
  }

  @Test
  public void passwordIsBlacklisted() {
    assertTrue(userManagementRepository.isPasswordBlacklisted("password"));
  }

  @Test
  public void passwordIsUniqueEnough() {
    assertFalse(userManagementRepository.isPasswordBlacklisted("S0r0kTysy@ch0b3z'j@n;"));
  }

  private UserEntity checkDefaultUser(Optional<UserEntity> maybeUserEntity) {
    assertThat(maybeUserEntity).isNotNull();
    assertThat(maybeUserEntity.isPresent()).isTrue();
    UserEntity userEntity = maybeUserEntity.get();
    assertThat(userEntity.getUuid()).isEqualTo(DEFAULT_USER_UUID);
    assertThat(userEntity.getName()).isEqualTo("Sampath");
    assertThat(userEntity.getEmailAddress()).isEqualTo("abc@dft.gov.uk");
    assertThat(userEntity.getAuthorityCode()).isEqualTo(DEFAULT_LOCAL_AUTHORITY_CODE);
    assertThat(userEntity.getRoleId()).isEqualTo(2);
    assertThat(userEntity.getRoleName()).isEqualTo("LA Admin");
    return userEntity;
  }

  private UserEntity createUserEntityExample(
      UUID id,
      String name,
      String emailAddress,
      String localAuthority,
      int roleId,
      String roleName) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(id);
    userEntity.setName(name);
    userEntity.setEmailAddress(emailAddress);
    userEntity.setAuthorityCode(localAuthority);
    userEntity.setRoleId(roleId);
    userEntity.setRoleName(roleName);
    userEntity.setPassword(UUID.randomUUID().toString());
    return userEntity;
  }
}
