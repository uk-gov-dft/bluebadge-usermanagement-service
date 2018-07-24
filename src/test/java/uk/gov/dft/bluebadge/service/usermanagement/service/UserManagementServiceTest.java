package uk.gov.dft.bluebadge.service.usermanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.security.model.LocalAuthority;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.client.messageservice.model.NewUserRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetSuccessRequest;
import uk.gov.dft.bluebadge.service.usermanagement.repository.LocalAuthorityRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.*;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;

public class UserManagementServiceTest {
  private static final int DEFAULT_USER_ID = -1;
  private static final int DEFAULT_LOCAL_AUTHORITY_ID = 2;
  private static final String DEFAULT_LOCAL_AUTHORITY_SHORTCODE = "MANC";
  private static final LocalAuthority DEFAULT_LOCAL_AUTHORITY =
      LocalAuthority.builder()
          .id(DEFAULT_LOCAL_AUTHORITY_ID)
          .shortCode(DEFAULT_LOCAL_AUTHORITY_SHORTCODE)
          .build();
  private static final int OTHER_LOCAL_AUTHORITY_ID = 99;
  private static final String OTHER_LOCAL_AUTHORITY_SHORTCODE = "BRI";
  private static final LocalAuthority OTHER_LOCAL_AUTHORITY =
      LocalAuthority.builder()
          .id(OTHER_LOCAL_AUTHORITY_ID)
          .shortCode(OTHER_LOCAL_AUTHORITY_SHORTCODE)
          .build();
  private static final int ANOTHER_LOCAL_AUTHORITY_ID = 1;
  private static final String ANOTHER_LOCAL_AUTHORITY_SHORTCODE = "MANC";
  private static final LocalAuthority ANOTHER_LOCAL_AUTHORITY =
      LocalAuthority.builder()
          .id(ANOTHER_LOCAL_AUTHORITY_ID)
          .shortCode(ANOTHER_LOCAL_AUTHORITY_SHORTCODE)
          .build();

  private static final RetrieveUserByIdParams DEFAULT_RETRIEVE_BY_USER_ID_PARAMS =
      RetrieveUserByIdParams.builder()
          .userId(DEFAULT_USER_ID)
          .localAuthority(DEFAULT_LOCAL_AUTHORITY_ID)
          .build();
  private static final RetrieveUserByIdParams RETRIEVE_BY_USER_ID_PARAMS_NO_LOCALAUTHORITY =
      RetrieveUserByIdParams.builder().userId(DEFAULT_USER_ID).build();
  private static final DeleteUserParams DEFAULT_DELETE_USER_PARAMS =
      DeleteUserParams.builder()
          .userId(DEFAULT_USER_ID)
          .localAuthority(DEFAULT_LOCAL_AUTHORITY_ID)
          .build();

  private static final String WEBAPP_URI = "http://somewhere";
  private UserManagementService service;

  @Mock private UserManagementRepository repository;
  @Mock private LocalAuthorityRepository localAuthorityRepository;
  @Mock private MessageApiClient messageApiClient;
  @Mock private SecurityUtils securityUtils;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service =
        new UserManagementService(
            repository, localAuthorityRepository, messageApiClient, WEBAPP_URI, securityUtils);
    when(securityUtils.getCurrentLocalAuthority()).thenReturn(DEFAULT_LOCAL_AUTHORITY);
  }

  @Test
  public void createUser_all_ok() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setName("test");
    user.setId(-1);
    user.setEmailAddress("ggg");
    user.setLocalAuthorityId(OTHER_LOCAL_AUTHORITY_ID);
    LocalAuthorityEntity localAuthority = new LocalAuthorityEntity();
    localAuthority.setName("Bob");

    when(securityUtils.getCurrentLocalAuthority()).thenReturn(OTHER_LOCAL_AUTHORITY);
    when(repository.emailAddressAlreadyUsed(user)).thenReturn(false);
    when(localAuthorityRepository.retrieveLocalAuthorityById(OTHER_LOCAL_AUTHORITY_ID))
        .thenReturn(localAuthority);

    // When user is created
    service.createUser(user);

    // Then check email address not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user);
    // And user is created
    verify(repository, times(1)).createUser(user);
    // And password reset email is created
    verify(messageApiClient, times(1)).sendEmailLinkMessage(any(NewUserRequest.class));
    // And user is set inactive
    verify(repository, times(1)).updateUserToInactive(-1);
    // And email_link is created
    verify(repository, times(1)).createEmailLink(any(EmailLink.class));
    verify(localAuthorityRepository, times(1)).retrieveLocalAuthorityById(OTHER_LOCAL_AUTHORITY_ID);
  }

  @Test(expected = BadRequestException.class)
  public void createUser_emailexisted() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setName("test");
    user.setId(-1);
    user.setEmailAddress("ggg");

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
    verify(repository, never()).updateUserToInactive(-1);
    // And email_link is not created
    verify(repository, never()).createEmailLink(any(EmailLink.class));
  }

  @Test
  public void requestPasswordResetEmail() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setEmailAddress("test@email.com");
    user.setName("test");
    user.setId(-1);

    when(messageApiClient.sendEmailLinkMessage(any(PasswordResetRequest.class)))
        .thenReturn(UUID.randomUUID());

    when(repository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.of(user));
    // When a password change is requested
    service.requestPasswordResetEmail(-1);

    // Then password reset email is created
    ArgumentCaptor<PasswordResetRequest> passwordRequest =
        ArgumentCaptor.forClass(PasswordResetRequest.class);
    verify(messageApiClient, times(1)).sendEmailLinkMessage(passwordRequest.capture());
    assertThat(passwordRequest).isNotNull();
    assertThat(passwordRequest.getValue()).isNotNull();
    assertThat( ***REMOVED***);
    assertThat( ***REMOVED***);

    // And user is set inactive
    verify(repository).updateUserToInactive(-1);
    // And email_link is created
    verify(repository).createEmailLink(any(EmailLink.class));
  }

  @Test(expected = NotFoundException.class)
  public void requestPasswordResetEmail_no_user() {
    when(repository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.empty());
    service.requestPasswordResetEmail(-1);
  }

  @Test
  public void retrieveUserById_ok() {
    // Given the user exists
    UserEntity user = new UserEntity();
    when(repository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.of(user));

    // When retrieving the user then the user is returned
    Assert.assertTrue(user.equals(service.retrieveUserById(-1)));
  }

  @Test(expected = NotFoundException.class)
  public void retrieveUserById_notExists() {
    // Given the user does not exist
    when(repository.retrieveUserById(DEFAULT_RETRIEVE_BY_USER_ID_PARAMS))
        .thenReturn(Optional.empty());

    // When retrieved then NotFoundException
    service.retrieveUserById(-1);
  }

  @Test
  public void updateUser_ok() {
    when(securityUtils.getCurrentLocalAuthority()).thenReturn(ANOTHER_LOCAL_AUTHORITY);

    UserEntity user = new UserEntity();
    user.setId(1);
    user.setLocalAuthorityId(1);
    UserEntity expectedUser = new UserEntity();
    expectedUser.setId(1);
    expectedUser.setLocalAuthorityId(1);

    // Given the user is valid
    when(repository.updateUser(expectedUser)).thenReturn(1);
    when(repository.emailAddressAlreadyUsed(expectedUser)).thenReturn(false);

    // When the user is updated
    service.updateUser(user);

    // Then check email address is not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(expectedUser);
    // And update the user
    verify(repository, times(1)).updateUser(expectedUser);
  }

  @Test(expected = NotFoundException.class)
  public void updateUser_no_user() {
    when(securityUtils.getCurrentLocalAuthority()).thenReturn(ANOTHER_LOCAL_AUTHORITY);
    UserEntity user = new UserEntity();
    user.setId(1);
    user.setLocalAuthorityId(1);
    UserEntity expectedUser = new UserEntity();
    expectedUser.setId(1);
    expectedUser.setLocalAuthorityId(1);
    when(repository.updateUser(expectedUser)).thenReturn(0);
    service.updateUser(user);
  }

  @Test(expected = BadRequestException.class)
  public void updateUser_alreadyExists() {
    UserEntity user = new UserEntity();

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
     ***REMOVED***);
     ***REMOVED***);
    UUID uuid = UUID.randomUUID();
    EmailLink link =
        EmailLink.builder()
            .webappUri(WEBAPP_URI)
            .userId(-1)
            .uuid(uuid.toString())
            .isActive(true)
            .build();
    UserEntity userEntity = new UserEntity();
    userEntity.setName("Jane Test");
    userEntity.setEmailAddress("janetest@email.com");

    when(repository.updatePassword(any())).thenReturn(1);
    when(repository.updateEmailLinkToInvalid(uuid.toString())).thenReturn(1);
    when(repository.retrieveEmailLinkWithUuid(uuid.toString())).thenReturn(link);
    when(repository.retrieveUserById(RETRIEVE_BY_USER_ID_PARAMS_NO_LOCALAUTHORITY))
        .thenReturn(Optional.of(userEntity));

    // When update password requested
    service.updatePassword(uuid.toString(), password);

    // Then the email link is set inactive
    verify(repository, times(1)).updateEmailLinkToInvalid(uuid.toString());
    // And the password is stored
    verify(repository, times(1)).updatePassword(any());

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
            .userId(-1)
            .uuid(uuid.toString())
            .isActive(false)
            .build();

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
    when(repository.deleteUser(DEFAULT_DELETE_USER_PARAMS)).thenReturn(1);
    service.deleteUser(-1);
  }

  @Test(expected = NotFoundException.class)
  public void deleteUser_no_user() {
    when(repository.deleteUser(DEFAULT_DELETE_USER_PARAMS)).thenReturn(0);
    service.deleteUser(-1);
  }

  @Test
  public void retrieveUsersByAuthorityId() {
    List<UserEntity> userList = new ArrayList<>();
    when(repository.findUsers(any())).thenReturn(userList);
    service.retrieveUsersByAuthorityId(1, "abc");
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
    UserEntity userEntity = new UserEntity();
    when(repository.retrieveUserUsingEmailLinkUuid(uuid.toString())).thenReturn(Optional.empty());

    service.retrieveUserUsingUuid(uuid.toString());
  }
}
