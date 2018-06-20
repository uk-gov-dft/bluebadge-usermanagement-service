package uk.gov.dft.bluebadge.service.usermanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.model.usermanagement.Password;
import uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;

public class UserManagementServiceTest {

  private UserManagementService service;

  @Mock private UserManagementRepository repository;

  @Mock private MessageApiClient messageApiClient;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service = new UserManagementService(repository, messageApiClient);
  }

  @Test
  public void createUser_all_ok() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setName("test");
    user.setId(-1);
    user.setEmailAddress("ggg");

    when(repository.emailAddressAlreadyUsed(user)).thenReturn(false);
    when(repository.createUser(user)).thenReturn(1);
    when(messageApiClient.sendPasswordResetEmail(any(PasswordResetRequest.class)))
        .thenReturn(UUID.randomUUID());

    // When user is created
    service.createUser(user);

    // Then check email address not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user);
    // And user is created
    verify(repository, times(1)).createUser(user);
    // And password reset email is created
    verify(messageApiClient, times(1)).sendPasswordResetEmail(any(PasswordResetRequest.class));
    // And user is set inactive
    verify(repository, times(1)).updateUserToInactive(-1);
    // And email_link is created
    verify(repository, times(1)).createEmailLink(any(EmailLink.class));
  }

  @Test(expected = BadRequestException.class)
  public void createUser_emailexisted() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setName("test");
    user.setId(-1);
    user.setEmailAddress("ggg");

    when(repository.emailAddressAlreadyUsed(user)).thenReturn(true);
    when(repository.createUser(user)).thenReturn(1);
    when(messageApiClient.sendPasswordResetEmail(any(PasswordResetRequest.class)))
        .thenReturn(UUID.randomUUID());

    // When user is created
    service.createUser(user);

    // Then check email address not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user);
    // And user is not created
    verify(repository, never()).createUser(user);
    // And password reset email is not created
    verify(messageApiClient, never()).sendPasswordResetEmail(any(PasswordResetRequest.class));
    // And user is not set inactive
    verify(repository, never()).updateUserToInactive(-1);
    // And email_link is not created
    verify(repository, never()).createEmailLink(any(EmailLink.class));
  }

  @Test
  public void requestPasswordResetEmail() {
    // Given a new valid user
    UserEntity user = new UserEntity();
    user.setName("test");
    user.setId(-1);

    when(messageApiClient.sendPasswordResetEmail(any(PasswordResetRequest.class)))
        .thenReturn(UUID.randomUUID());

    // When a password change is requested
    service.requestPasswordResetEmail(user, false);

    // Then password reset email is created
    verify(messageApiClient, times(1)).sendPasswordResetEmail(any(PasswordResetRequest.class));
    // And user is set inactive
    verify(repository).updateUserToInactive(-1);
    // And email_link is created
    verify(repository).createEmailLink(any(EmailLink.class));
  }

  @Test
  public void retrieveUserById_ok() {
    // Given the user exists
    UserEntity user = new UserEntity();
    when(repository.retrieveUserById(-1)).thenReturn(Optional.of(user));

    // When retrieving the user then the user is returned
    Assert.assertTrue(user.equals(service.retrieveUserById(-1)));
  }

  @Test(expected = NotFoundException.class)
  public void retrieveUserById_notExists() {
    // Given the user does not exist
    when(repository.retrieveUserById(-1)).thenReturn(Optional.empty());

    // When retrieved then NotFoundException
    service.retrieveUserById(-1);
  }

  @Test
  public void updateUser_ok() {
    UserEntity user = new UserEntity();

    // Given the user is valid
    when(repository.updateUser(user)).thenReturn(1);
    when(repository.emailAddressAlreadyUsed(user)).thenReturn(false);

    // When the user is updated
    service.updateUser(user);

    // Then check email address is not in use
    verify(repository, times(1)).emailAddressAlreadyUsed(user);
    // And update the user
    verify(repository, times(1)).updateUser(user);
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
    String uuid = UUID.randomUUID().toString();
    EmailLink link = EmailLink.builder().userId(-1).uuid(uuid).isActive(true).build();

    when(repository.updatePassword(any())).thenReturn(1);
    when(repository.updateEmailLinkToInvalid(uuid)).thenReturn(1);
    when(repository.retrieveEmailLinkWithUuid(uuid)).thenReturn(link);

    // When update password requested
    service.updatePassword(uuid, password);

    // Then the email link is set inactive
    verify(repository, times(1)).updateEmailLinkToInvalid(uuid);
    // And the password is stored
    verify(repository, times(1)).updatePassword(any());
  }

  @Test(expected = BadRequestException.class)
  public void updatePassword_linkInactive() {
    // Given the new password is valid
    Password password = new Password();
     ***REMOVED***);
     ***REMOVED***);
    String uuid = UUID.randomUUID().toString();
    EmailLink link = EmailLink.builder().userId(-1).uuid(uuid).isActive(false).build();

    when(repository.retrieveEmailLinkWithUuid(uuid)).thenReturn(link);

    // When update password requested
    service.updatePassword(uuid, password);

    // Then the email link is set inactive
    verify(repository, never()).updateEmailLinkToInvalid(uuid);
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
}
