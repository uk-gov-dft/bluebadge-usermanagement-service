package uk.gov.dft.bluebadge.service.usermanagement.repository;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.ApplicationContextTests;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.DeleteUserParams;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.RetrieveUserByIdParams;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SqlGroup({@Sql(scripts = "classpath:/test-data.sql")})
@Transactional
public class UserManagementRepositoryTest extends ApplicationContextTests {
    private static final int DEFAULT_USER_ID = -1;
    private static final int DEFAULT_LOCAL_AUTHORITY_ID = 2;
    private static final int OTHER_LOCAL_AUTHORITY_ID = 45;
    private static final RetrieveUserByIdParams DEFAULT_RETRIEVE_BY_USER_ID_PARAMS =
            RetrieveUserByIdParams.builder()
                    .userId(DEFAULT_USER_ID)
                    .localAuthority(DEFAULT_LOCAL_AUTHORITY_ID)
                    .build();
    private static final RetrieveUserByIdParams NO_LOCAL_AUTHORITY_RETRIEVE_BY_USER_ID_PARAMS =
            RetrieveUserByIdParams.builder().userId(DEFAULT_USER_ID).localAuthority(null).build();
    private static final DeleteUserParams DEFAULT_DELETE_USER_PARAMS =
            DeleteUserParams.builder()
                    .userId(DEFAULT_USER_ID)
                    .localAuthority(DEFAULT_LOCAL_AUTHORITY_ID)
                    .build();

    public static final String DEFAULT_USER_EMAIL_LINK_UUID = "7d75652a-4e84-41e2-bd82-e5b5933b81da";

    @Autowired
    UserManagementRepository userManagementRepository;

    @Test
    public void retrieveUserById_exists() throws Exception {
        Optional<UserEntity> maybeUserEntity =
                userManagementRepository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS);
        UserEntity userEntity = checkDefaultUser(maybeUserEntity);

        assertThat(userEntity.getPassword()).isNull();
    }

    @Test
    public void retrieveUserById_existsAndNoLocalAuthorityProvided() throws Exception {
        Optional<UserEntity> maybeUserEntity =
                userManagementRepository.retrieveUserById(NO_LOCAL_AUTHORITY_RETRIEVE_BY_USER_ID_PARAMS);
        UserEntity userEntity = checkDefaultUser(maybeUserEntity);

        assertThat(userEntity.getPassword()).isNull();
    }

    @Test
    public void retrieveUserById_existsInDifferentLocalAuthority() throws Exception {
        RetrieveUserByIdParams params =
                RetrieveUserByIdParams.builder()
                        .userId(DEFAULT_USER_ID)
                        .localAuthority(OTHER_LOCAL_AUTHORITY_ID)
                        .build();
        Optional<UserEntity> maybeUserEntity = userManagementRepository.retrieveUserById(params);

        assertThat(maybeUserEntity).isEmpty();
    }

    @Test
    public void retrieveUserById_notExists() throws Exception {
        RetrieveUserByIdParams params =
                RetrieveUserByIdParams.builder()
                        .userId(-99999)
                        .localAuthority(DEFAULT_LOCAL_AUTHORITY_ID)
                        .build();

        Optional<UserEntity> maybeUserEntity = userManagementRepository.retrieveUserById(params);
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
    public void
    findUsers_byAuthorityIdAndName_ShouldReturn50FirstResultsOrderedByNameAscendingOrder() {
        final int FIRST_ID = 200000;
        final int LAST_ID = FIRST_ID + 100;
        final int RESULTS_LIMIT = 50;

        List<UserEntity> userEntityList = Lists.newArrayList();
        for (int id = LAST_ID; id > FIRST_ID; id--) {
            UserEntity userEntity =
                    createUserEntityExample(
                            id, "Jane" + id, "jane" + id + "@jane.com", OTHER_LOCAL_AUTHORITY_ID, 2, "LA Admin");
            userManagementRepository.createUser(userEntity);
            userEntityList.add(userEntity);
        }
        UserEntity params = new UserEntity();
        params.setLocalAuthorityId(OTHER_LOCAL_AUTHORITY_ID);
        params.setName("Jane%");
        List<UserEntity> users = userManagementRepository.findUsers(params);

        for (int id = LAST_ID; id > FIRST_ID; id--) {
            DeleteUserParams deleteParams =
                    DeleteUserParams.builder().userId(id).localAuthority(OTHER_LOCAL_AUTHORITY_ID).build();
            userManagementRepository.deleteUser(deleteParams);
        }

        Collections.sort(userEntityList, (u1, u2) -> u1.getName().compareTo(u2.getName()));
        List<UserEntity> expectedUerEntityList =
                userEntityList.stream().limit(RESULTS_LIMIT).collect(Collectors.toList());

        assertThat(users).hasSize(RESULTS_LIMIT);
        assertThat(users).isEqualTo(expectedUerEntityList);
    }

    @Test
    public void updateUser_fieldsUpdated() throws Exception {
        UserEntity userEntity =
                userManagementRepository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS).get();
        userEntity.setName("Bob");
        userEntity.setEmailAddress("bob@bob.com");
        userEntity.setLocalAuthorityId(DEFAULT_LOCAL_AUTHORITY_ID);
        userEntity.setRoleId(3);

        int i = userManagementRepository.updateUser(userEntity);
        assertThat(i).isEqualTo(1);

        UserEntity updatedUserEntity =
                userManagementRepository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS).get();
        assertThat(updatedUserEntity).isNotSameAs(userEntity);
        assertThat(updatedUserEntity.getName()).isEqualTo("Bob");
        assertThat(updatedUserEntity.getEmailAddress()).isEqualTo("bob@bob.com");
        assertThat(updatedUserEntity.getLocalAuthorityId()).isEqualTo(DEFAULT_LOCAL_AUTHORITY_ID);
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
        UserEntity userEntity =
                userManagementRepository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS).get();
        userEntity.setPassword("newPassword");
        int i = userManagementRepository.updatePassword(userEntity);
        assertThat(i).isEqualTo(1);
    }

    @Test
    public void updatePassword_notExist() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(-1001);
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
        userEntity.setLocalAuthorityId(OTHER_LOCAL_AUTHORITY_ID);
        userEntity.setRoleId(2);

        userManagementRepository.createUser(userEntity);

        UserEntity search = new UserEntity();
        search.setLocalAuthorityId(OTHER_LOCAL_AUTHORITY_ID);
        search.setName("Jane");
        search.setEmailAddress("jane@jane.com");
        List<UserEntity> users = userManagementRepository.findUsers(search);
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);

        UserEntity updatedUser = users.get(0);
        assertThat(updatedUser.getId()).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Jane");
        assertThat(updatedUser.getEmailAddress()).isEqualTo("jane@jane.com");
        assertThat(updatedUser.getLocalAuthorityId()).isEqualTo(OTHER_LOCAL_AUTHORITY_ID);
        assertThat(updatedUser.getRoleId()).isEqualTo(2);
        assertThat(updatedUser.getRoleName()).isEqualTo("LA Admin");
    }

    @Test
    public void deleteUser() throws Exception {
        int deletedRecords = userManagementRepository.deleteUser(DEFAULT_DELETE_USER_PARAMS);
        assertThat(deletedRecords).isEqualTo(1);

        Optional<UserEntity> userEntity =
                userManagementRepository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS);
        assertThat(userEntity).isEmpty();
    }

    @Test
    public void deleteUser_existsInDifferentLocalAuthority() throws Exception {
        DeleteUserParams deleteParams =
                DeleteUserParams.builder()
                        .userId(DEFAULT_USER_ID)
                        .localAuthority(OTHER_LOCAL_AUTHORITY_ID)
                        .build();

        int deletedRecords = userManagementRepository.deleteUser(deleteParams);
        assertThat(deletedRecords).isEqualTo(0);

        Optional<UserEntity> userEntity =
                userManagementRepository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS);
        assertThat(userEntity).isPresent();
    }

    @Test
    public void deleteUser_notExists() throws Exception {
        DeleteUserParams deleteParams =
                DeleteUserParams.builder().userId(-1001).localAuthority(DEFAULT_LOCAL_AUTHORITY_ID).build();
        int i = userManagementRepository.deleteUser(deleteParams);
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

    private UserEntity createUserEntityExample(
            int id, String name, String emailAddress, int localAuthority, int roleId, String roleName) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(name);
        userEntity.setEmailAddress(emailAddress);
        userEntity.setLocalAuthorityId(localAuthority);
        userEntity.setRoleId(roleId);
        userEntity.setRoleName(roleName);
        return userEntity;
    }
}
