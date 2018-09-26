package uk.gov.dft.bluebadge.service.usermanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.gov.dft.bluebadge.common.security.Role;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.security.model.BBPrincipal;
import uk.gov.dft.bluebadge.common.util.TestBBPrincipal;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.client.messageservice.model.NewUserRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetSuccessRequest;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UuidAuthorityCodeParams;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.usermanagement.service.referencedata.ReferenceDataService;

public class UserManagementServiceTest {
  private static final UUID DEFAULT_USER_UUID = UUID.randomUUID();
  private static final String DEFAULT_LOCAL_AUTHORITY_SHORT_CODE = "MANC";
  private static final String OTHER_LOCAL_AUTHORITY_SHORT_CODE = "BRI";
  private static final String ANOTHER_LOCAL_AUTHORITY_SHORT_CODE = "MANC";

  private static final UuidAuthorityCodeParams DEFAULT_RETRIEVE_BY_USER_ID_PARAMS =
      UuidAuthorityCodeParams.builder()
          .uuid(DEFAULT_USER_UUID)
          .authorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE)
          .build();
  private static final UuidAuthorityCodeParams RETRIEVE_BY_USER_ID_PARAMS_NO_LOCAL_AUTHORITY =
      UuidAuthorityCodeParams.builder().uuid(DEFAULT_USER_UUID).build();
  private static final UuidAuthorityCodeParams DEFAULT_DELETE_USER_PARAMS =
      UuidAuthorityCodeParams.builder()
          .uuid(DEFAULT_USER_UUID)
          .authorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE)
          .build();

  private static final String WEBAPP_URI = "http://somewhere";
  private UserManagementService service;

  @Mock private UserManagementRepository repository;
  @Mock private MessageApiClient messageApiClient;
  @Mock private SecurityUtils securityUtils;
  @Mock private ReferenceDataService referenceDataService;
  @Mock private CommonPasswordsFilter filter;

  private UserEntity user1;
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service =
        new UserManagementService(
            repository,
            messageApiClient,
            WEBAPP_URI,
            securityUtils,
            passwordEncoder,
            referenceDataService,
            filter);
    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);

    user1 = new UserEntity();
    user1.setName("test");
    user1.setUuid(DEFAULT_USER_UUID);
    user1.setEmailAddress("ggg");
    user1.setAuthorityCode(OTHER_LOCAL_AUTHORITY_SHORT_CODE);
  }

  @Test
  public void createUser_all_ok() {

    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(OTHER_LOCAL_AUTHORITY_SHORT_CODE);
    when(repository.emailAddressAlreadyUsed(user1)).thenReturn(false);
    when(referenceDataService.getLocalAuthorityName(OTHER_LOCAL_AUTHORITY_SHORT_CODE))
        .thenReturn("Manchester");
    when(referenceDataService.isValidLocalAuthorityCode(any())).thenReturn(true);
    String preCreatePassword = user1.getPassword();

    // When user is created
    service.createUser(user1);

    // Then check email address not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user1);
    // And user is created
    verify(repository, times(1)).createUser(user1);
    // And password reset email is created
    verify(messageApiClient, times(1)).sendEmailLinkMessage(any(NewUserRequest.class));
    // And user is set inactive
    verify(repository, times(1)).updateUserToInactive(any());
    // And email_link is created
    verify(repository, times(1)).createEmailLink(any(EmailLink.class));

    assertThat(user1.getPassword()).isNotNull();
    assertThat(user1.getPassword()).isNotEqualTo(preCreatePassword);
  }

  @Test(expected = BadRequestException.class)
  public void createUser_existsButInDifferentLocalAuthority() {
    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(ANOTHER_LOCAL_AUTHORITY_SHORT_CODE);
    when(repository.emailAddressAlreadyUsed(user1)).thenReturn(false);

    service.createUser(user1);

    verify(repository, times(1)).emailAddressAlreadyUsed(user1);
    verify(repository, times(0)).createUser(any());
    verify(messageApiClient, times(0)).sendEmailLinkMessage(any());
    verify(repository, times(0)).updateUserToInactive(any());
    verify(repository, times(0)).createEmailLink(any(EmailLink.class));
  }

  @Test(expected = BadRequestException.class)
  public void createUser_emailexisted() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setName("test");
    user.setUuid(UUID.randomUUID());
    user.setEmailAddress("ggg");
    user.setRoleId(Role.LA_ADMIN.getRoleId());

    when(repository.emailAddressAlreadyUsed(user)).thenReturn(true);
    when(messageApiClient.sendEmailLinkMessage(any(PasswordResetRequest.class)))
        .thenReturn(UUID.randomUUID());

    // When user is created
    service.createUser(user);

    // Then check email address not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user);
    // And user is not created
    verify(repository, never()).createUser(user);
    // And password reset email is not created
    verify(messageApiClient, never()).sendEmailLinkMessage(any(PasswordResetRequest.class));
    // And user is not set inactive
    verify(repository, never()).updateUserToInactive(any());
    // And email_link is not created
    verify(repository, never()).createEmailLink(any(EmailLink.class));
  }

  @Test
  public void requestPasswordResetEmail() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setEmailAddress("test@email.com");
    user.setName("test");
    user.setUuid(UUID.randomUUID());

    when(messageApiClient.sendEmailLinkMessage(any(PasswordResetRequest.class)))
        .thenReturn(UUID.randomUUID());

    when(repository.retrieveUserByUuid(any())).thenReturn(Optional.of(user));
    // When a password change is requested
    service.requestPasswordResetEmail(UUID.randomUUID());

    // Then password reset email is created
    ArgumentCaptor<PasswordResetRequest> passwordRequest =
        ArgumentCaptor.forClass(PasswordResetRequest.class);
    verify(messageApiClient, times(1)).sendEmailLinkMessage(passwordRequest.capture());
    assertThat(passwordRequest).isNotNull();
    assertThat(passwordRequest.getValue()).isNotNull();
    assertThat( ***REMOVED***);
    assertThat( ***REMOVED***);

    // And user is set inactive
    verify(repository).updateUserToInactive(any());
    // And email_link is created
    verify(repository).createEmailLink(any(EmailLink.class));
  }

  @Test(expected = NotFoundException.class)
  public void requestPasswordResetEmail_no_user() {
    when(repository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.empty());
    service.requestPasswordResetEmail(DEFAULT_USER_UUID);
  }

  @Test
  public void retrieveUserById_ok() {
    // Given the user exists
    UserEntity user = new UserEntity();
    UuidAuthorityCodeParams authorityCodeParams =
        UuidAuthorityCodeParams.builder().uuid(DEFAULT_USER_UUID).build();
    when(repository.retrieveUserByUuid(authorityCodeParams)).thenReturn(Optional.of(user));

    // When retrieving the user then the user is returned
    Assert.assertEquals(user, service.retrieveUserById(DEFAULT_USER_UUID));
  }

  @Test(expected = NotFoundException.class)
  public void retrieveUserById_existsButInDifferentLocalAuthority() {
    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(OTHER_LOCAL_AUTHORITY_SHORT_CODE);

    UserEntity user = new UserEntity();
    when(repository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.of(user));

    service.retrieveUserById(DEFAULT_USER_UUID);
  }

  @Test(expected = NotFoundException.class)
  public void retrieveUserById_notExists() {
    // Given the user does not exist
    when(repository.retrieveUserByUuid(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.empty());

    // When retrieved then NotFoundException
    service.retrieveUserById(DEFAULT_USER_UUID);
  }

  @Test
  public void retrieveCurrentUser_ok() {
    BBPrincipal authenticatedUser = TestBBPrincipal.user().emailAddress("bob@bob.com").build();
    when(securityUtils.getCurrentAuth()).thenReturn(authenticatedUser);
    UserEntity user = new UserEntity();
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
    when(repository.findUsers(userEntityArgumentCaptor.capture()))
        .thenReturn(ImmutableList.of(user));

    assertThat(service.retrieveCurrentUser()).isSameAs(user);
    assertThat(userEntityArgumentCaptor.getValue().getEmailAddress()).isEqualTo("bob@bob.com");
  }

  @Test
  public void retrieveCurrentUser_authenticationNotUser() {
    BBPrincipal authenticatedUser = TestBBPrincipal.clientCreds().build();
    when(securityUtils.getCurrentAuth()).thenReturn(authenticatedUser);

    try {
      service.retrieveCurrentUser();
      fail("No exception.");
    } catch (BadRequestException bre) {
    }

    verify(repository, never()).findUsers(any());
  }

  @Test(expected = NotFoundException.class)
  public void retrieveCurrentUser_emailNotFoundThenNotFoundException() {
    BBPrincipal authenticatedUser = TestBBPrincipal.user().emailAddress("unknown@bob.com").build();
    when(securityUtils.getCurrentAuth()).thenReturn(authenticatedUser);
    when(repository.findUsers(any())).thenReturn(new ArrayList<UserEntity>());

    service.retrieveCurrentUser();
  }

  @Test
  public void updateUser_ok() {
    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(ANOTHER_LOCAL_AUTHORITY_SHORT_CODE);

    UserEntity user = new UserEntity();
    user.setUuid(DEFAULT_USER_UUID);
    user.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);
    UserEntity expectedUser = new UserEntity();
    expectedUser.setUuid(DEFAULT_USER_UUID);
    expectedUser.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);

    // Given the user is valid
    when(repository.updateUser(expectedUser)).thenReturn(1);
    when(repository.emailAddressAlreadyUsed(expectedUser)).thenReturn(false);
    when(referenceDataService.isValidLocalAuthorityCode(any())).thenReturn(true);
    // When the user is updated
    service.updateUser(user);

    // Then check email address is not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(expectedUser);
    // And update the user
    verify(repository, times(1)).updateUser(expectedUser);
  }

  @Test(expected = NotFoundException.class)
  public void updateUser_no_user() {
    when(referenceDataService.isValidLocalAuthorityCode(any())).thenReturn(true);
    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(ANOTHER_LOCAL_AUTHORITY_SHORT_CODE);
    UserEntity user = new UserEntity();
    user.setUuid(DEFAULT_USER_UUID);
    user.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);
    UserEntity expectedUser = new UserEntity();
    expectedUser.setUuid(DEFAULT_USER_UUID);
    expectedUser.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);
    when(repository.updateUser(expectedUser)).thenReturn(0);
    service.updateUser(user);
  }

  @Test(expected = BadRequestException.class)
  public void updateUser_existsButInDifferentLocalAuthority() {
    when(securityUtils.getCurrentLocalAuthorityShortCode())
        .thenReturn(OTHER_LOCAL_AUTHORITY_SHORT_CODE);

    UserEntity user = new UserEntity();
    user.setUuid(DEFAULT_USER_UUID);
    user.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);
    UserEntity expectedUser = new UserEntity();
    expectedUser.setUuid(DEFAULT_USER_UUID);
    expectedUser.setAuthorityCode(DEFAULT_LOCAL_AUTHORITY_SHORT_CODE);

    // Given the user is valid
    when(repository.updateUser(expectedUser)).thenReturn(1);
    when(repository.emailAddressAlreadyUsed(expectedUser)).thenReturn(false);

    // When the user is updated
    service.updateUser(user);

    verify(repository, times(1)).emailAddressAlreadyUsed(expectedUser);
    verify(repository, times(0)).updateUser(any());
  }

  @Test(expected = BadRequestException.class)
  public void updateUser_alreadyExists() {
    UserEntity user = new UserEntity();
    user.setRoleId(Role.LA_ADMIN.getRoleId());

    // Given the user is valid
    when(repository.updateUser(user)).thenReturn(1);
    when(repository.emailAddressAlreadyUsed(user)).thenReturn(true);

    // When the user is updated
    service.updateUser(user);

    // Then check email address is not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user);
    // And do not update the user
    verify(repository, never()).updateUser(user);
  }

  @Test
  public void updatePassword_ok() {
    // Given the new password is valid
    Password password = new Password();
    String plainPassword = "12345678";
    password.setPassword(plainPassword);
    password.setPasswordConfirm(plainPassword);
    UUID uuid = UUID.randomUUID();
    EmailLink link =
        EmailLink.builder()
            .webappUri(WEBAPP_URI)
            .userUuid(DEFAULT_USER_UUID)
            .uuid(uuid.toString())
            .isActive(true)
            .build();
    UserEntity userEntity = new UserEntity();
    userEntity.setName("Jane Test");
    userEntity.setEmailAddress("janetest@email.com");

    doNothing().when(filter).validatePasswordBlacklisted(any(String.class));
    when(repository.updatePassword(any())).thenReturn(1);
    when(repository.updateEmailLinkToInvalid(uuid.toString())).thenReturn(1);
    when(repository.retrieveEmailLinkWithUuid(uuid.toString())).thenReturn(link);
    when(repository.retrieveUserByUuid(RETRIEVE_BY_USER_ID_PARAMS_NO_LOCAL_AUTHORITY))
        .thenReturn(Optional.of(userEntity));

    // When update password requested
    service.updatePassword(uuid.toString(), password);

    // Then the email link is set inactive
    verify(repository, times(1)).updateEmailLinkToInvalid(uuid.toString());

    // And the password is stored
    ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(repository, times(1)).updatePassword(userEntityArgumentCaptor.capture());
    assertThat(userEntityArgumentCaptor).isNotNull();
    assertThat(userEntityArgumentCaptor.getValue()).isNotNull();
    assertThat(userEntityArgumentCaptor.getValue().getPassword()).isNotNull();
    assertThat(userEntityArgumentCaptor.getValue().getPassword()).isNotEqualTo(plainPassword);
    assertThat(
            passwordEncoder.matches(
                plainPassword, userEntityArgumentCaptor.getValue().getPassword()))
        .isTrue();

    ArgumentCaptor<PasswordResetSuccessRequest> passwordSuccessRequest =
        ArgumentCaptor.forClass(PasswordResetSuccessRequest.class);
    verify(messageApiClient, times(1))
        .sendPasswordResetSuccessMessage(passwordSuccessRequest.capture());
    assertThat(passwordSuccessRequest).isNotNull();
    assertThat(passwordSuccessRequest.getValue()).isNotNull();
    assertThat( ***REMOVED***);
    assertThat( ***REMOVED***);
  }

  @Test(expected = BadRequestException.class)
  public void updatePassword_linkInactive() {
    // Given the new password is valid
    Password password = new Password();
     ***REMOVED***);
     ***REMOVED***);
    UUID uuid = UUID.randomUUID();
    EmailLink link =
        EmailLink.builder()
            .webappUri(WEBAPP_URI)
            .userUuid(DEFAULT_USER_UUID)
            .uuid(uuid.toString())
            .isActive(false)
            .build();

    doNothing().when(filter).validatePasswordBlacklisted(any(String.class));
    when(repository.retrieveEmailLinkWithUuid(uuid.toString())).thenReturn(link);

    // When update password requested
    service.updatePassword(uuid.toString(), password);

    // Then the email link is set inactive
    verify(repository, never()).updateEmailLinkToInvalid(uuid.toString());
    // And the password is stored
    verify(repository, never()).updatePassword(any());
  }

  @Test(expected = BadRequestException.class)
  public void updatePassword_linkDoesNotExist() {
    // Given the new password is valid
    Password password = new Password();
     ***REMOVED***);
     ***REMOVED***);
    String uuid = UUID.randomUUID().toString();

    doNothing().when(filter).validatePasswordBlacklisted(any(String.class));
    when(repository.retrieveEmailLinkWithUuid(uuid)).thenReturn(null);

    // When update password requested
    service.updatePassword(uuid, password);

    // Then the email link is not set inactive
    verify(repository, never()).updateEmailLinkToInvalid(uuid);
    // And the password not is stored
    verify(repository, never()).updatePassword(any());
  }

  @Test(expected = BadRequestException.class)
  public void updatePassword_passwordsDoNotMatch() {
    // Given the new password is valid
    Password password = new Password();
     ***REMOVED***);
     ***REMOVED***);
    String uuid = UUID.randomUUID().toString();

    doNothing().when(filter).validatePasswordBlacklisted(any(String.class));
    // When update password requested
    service.updatePassword(uuid, password);

    // Then the email link is not set inactive
    verify(repository, never()).updateEmailLinkToInvalid(uuid);
    // And the password is not stored
    verify(repository, never()).updatePassword(any());
    // And the link is not retrieved
    verify(repository, never()).retrieveEmailLinkWithUuid(any());
  }

  @Test
  public void deleteUser() {
    UuidAuthorityCodeParams authorityCodeParams =
        UuidAuthorityCodeParams.builder().uuid(DEFAULT_USER_UUID).build();

    when(repository.deleteUser(authorityCodeParams)).thenReturn(1);
    service.deleteUser(DEFAULT_USER_UUID);
  }

  @Test(expected = NotFoundException.class)
  public void deleteUser_no_user() {
    when(repository.deleteUser(DEFAULT_DELETE_USER_PARAMS)).thenReturn(0);
    service.deleteUser(DEFAULT_USER_UUID);
  }

  @Test
  public void findUsersTest() {
    List<UserEntity> userList = new ArrayList<>();
    when(repository.findUsers(any())).thenReturn(userList);
    service.findUsers("abc");
  }

  @Test
  public void retrieveUserUsingUuid_ok() {
    UUID uuid = UUID.randomUUID();
    UserEntity userEntity = new UserEntity();
    when(repository.retrieveUserUsingEmailLinkUuid(uuid.toString()))
        .thenReturn(Optional.of(userEntity));

    Assert.assertEquals(userEntity, service.retrieveUserUsingUuid(uuid.toString()));
  }

  @Test(expected = NotFoundException.class)
  public void retrieveUserUsingUuid_no_user() {
    UUID uuid = UUID.randomUUID();

    when(repository.retrieveUserUsingEmailLinkUuid(uuid.toString())).thenReturn(Optional.empty());

    service.retrieveUserUsingUuid(uuid.toString());
  }
}
