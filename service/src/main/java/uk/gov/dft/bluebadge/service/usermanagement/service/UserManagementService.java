package uk.gov.dft.bluebadge.service.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.usermanagement.generated.ErrorErrors;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;

@Service
@Transactional
@Slf4j
public class UserManagementService {

  private final UserManagementRepository repository;
  private MessageApiClient messageApiClient;

  @Autowired
  UserManagementService(UserManagementRepository repository, MessageApiClient messageApiClient) {
    this.repository = repository;
    this.messageApiClient = messageApiClient;
  }

  public UserEntity retrieveUserById(Integer userId) {
    Optional<UserEntity> userEntity = repository.retrieveUserById(userId);
    if (!userEntity.isPresent()) {
      log.info("Request to retrieve user id:{} that did not exist", userId);
      throw new NotFoundException();
    }
    return userEntity.get();
  }

  /**
   * Create user entity.
   *
   * @param userEntity User to create.
   * @return Create count.
   * @throws BadRequestException if validation fails.
   */
  public int createUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = businessValidateUser(userEntity);
    if (null != businessErrors) {
      log.debug("Business validation failed for user:{}", userEntity.getName());
      throw new BadRequestException(businessErrors);
    }
    int createCount = repository.createUser(userEntity);
    requestPasswordResetEmail(userEntity, true);
    log.debug("Created user {}", userEntity.getId());
    return createCount;
  }

  /**
   * Requests reset of a users password via message service.
   *
   * @param userEntity The user.
   * @param isNewUser true if create, false for existing. Different email template used.
   */
  public void requestPasswordResetEmail(UserEntity userEntity, boolean isNewUser) {
    log.debug("Resetting password for user:{}", userEntity.getId());
    PasswordResetRequest resetRequest =
        PasswordResetRequest.builder()
            .emailAddress(userEntity.getEmailAddress())
            .userId(userEntity.getId())
            .name(userEntity.getName())
            .localAuthorityId(userEntity.getLocalAuthorityId())
            .isNewUser(isNewUser)
            .build();

    UUID uuid = messageApiClient.sendPasswordResetEmail(resetRequest);
    EmailLink emailLink =
        EmailLink.builder().uuid(uuid.toString()).userId(userEntity.getId()).build();

    repository.createEmailLink(emailLink);
    repository.updateUserToInactive(userEntity.getId());
    log.debug("Successfully changed password for user:{}", userEntity.getId());
  }

  /**
   * Delete a user.
   *
   * @param id PK of user to delete.
   * @return Delete count
   */
  public int deleteUser(int id) {
    return repository.deleteUser(id);
  }

  /**
   * Should not be required once authentication implemented.
   *
   * @param emailAddress address to search for.
   * @return The user entity.
   */
  public Optional<UserEntity> retrieveUserByEmail(String emailAddress) {
    return repository.retrieveUserByEmail(emailAddress);
  }

  public List<UserEntity> retrieveUsersByAuthorityId(int authorityId, String nameFilter) {
    if (null != nameFilter) {
      nameFilter = "%" + nameFilter + "%";
    }
    UserEntity queryParams = new UserEntity();
    queryParams.setLocalAuthorityId(authorityId);
    queryParams.setName(nameFilter);
    queryParams.setEmailAddress(nameFilter);
    return repository.retrieveUsersByAuthorityId(queryParams);
  }

  /**
   * Apply business validation (non bean).
   *
   * @param userEntity User Entity bean.
   * @return List of errors or null if validation ok.
   */
  private List<ErrorErrors> businessValidateUser(UserEntity userEntity) {
    List<ErrorErrors> errorsList = null;

    if (repository.emailAddressAlreadyUsed(userEntity)) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("emailAddress")
          .message("AlreadyExists.user.emailAddress")
          .reason("Email Address already used.");
      errorsList = new ArrayList<>();
      errorsList.add(error);
    }

    return errorsList;
  }

  /**
   * Update user entity.
   *
   * @param userEntity Entity to update.
   * @return Update count.
   * @throws BadRequestException if validation fails.
   */
  public int updateUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = businessValidateUser(userEntity);
    if (null != businessErrors) {
      throw new BadRequestException(businessErrors);
    }
    return repository.updateUser(userEntity);
  }

  /**
   * Update user password.
   *
   * @return Update count.
   */
  public int updatePassword(String uuid, Password passwords) {

    log.debug("Updating password for guid:{}", uuid);
    String password = passwords.getPassword();
    String passwordConfirm = passwords.getPasswordConfirm();

    if (!password.equals(passwordConfirm)) {
      log.debug("Passwords did not match. {}", uuid);
      ErrorErrors error = new ErrorErrors();
      error.setField("passwordConfirm");
      error.setMessage("Pattern.password.passwordConfirm");
      error.setReason("Password confirm field does not match with password field");
      throw new BadRequestException(error);
    }

    EmailLink link = this.repository.retrieveEmailLinkWithUuid(uuid);

    if (link == null || !link.getIsActive()) {
      ErrorErrors error = new ErrorErrors();
      error.setField("password");
      if (link == null) {
        error.setMessage("Invalid.uuid");
      } else {
        error.setMessage("Inactive.uuid");
      }
      error.setReason("uuid is not valid");
      throw new BadRequestException(error);
    }

    String hash = BCrypt.hashpw(password, BCrypt.gensalt());
    UserEntity user = new UserEntity();
    user.setId(link.getUserId());
    user.setPassword(hash);

    repository.updateEmailLinkToInvalid(uuid);
    log.debug("Passwords updated. {}", uuid);
    return repository.updatePassword(user);
  }

  public Optional<UserEntity> retrieveUserUsingUuid(String uuid) {
    return repository.retrieveUserUsingUuid(uuid);
  }
}
