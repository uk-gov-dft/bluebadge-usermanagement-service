package uk.gov.dft.bluebadge.service.usermanagement.service;

import static uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException.Operation.DELETE;
import static uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException.Operation.RETRIEVE;
import static uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException.Operation.UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  private final MessageApiClient messageApiClient;
  private final String laWebappEmailLinkURI;

  @Autowired
  UserManagementService(
      UserManagementRepository repository,
      MessageApiClient messageApiClient,
      @Value("${la-webapp.email-link-uri}") String laWebappEmailLinkURI) {
    this.repository = repository;
    this.messageApiClient = messageApiClient;
    this.laWebappEmailLinkURI = laWebappEmailLinkURI;
  }

  public UserEntity retrieveUserById(Integer userId) {
    Optional<UserEntity> userEntity = repository.retrieveUserById(userId);
    if (!userEntity.isPresent()) {
      log.info("Request to retrieve user id:{} that did not exist", userId);
      throw new NotFoundException("user", RETRIEVE);
    }
    return userEntity.get();
  }

  /**
   * Create user entity.
   *
   * @param userEntity User to create.
   * @throws BadRequestException if validation fails.
   */
  public void createUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = businessValidateUser(userEntity);
    if (null != businessErrors) {
      log.debug("Business validation failed for user:{}", userEntity.getName());
      throw new BadRequestException(businessErrors);
    }
    repository.createUser(userEntity);
    requestPasswordResetEmail(userEntity);
    log.debug("Created user {}", userEntity.getId());
  }

  private void requestPasswordResetEmail(UserEntity userEntity) {
    EmailLink emailLink =
        EmailLink.builder()
            .webappUri(this.laWebappEmailLinkURI)
            .uuid(UUID.randomUUID().toString())
            .userId(userEntity.getId())
            .build();

    PasswordResetRequest resetRequest =
        PasswordResetRequest.builder()
            .emailAddress(userEntity.getEmailAddress())
            .name(userEntity.getName())
            .passwordLink(emailLink.getLink())
            .build();

    messageApiClient.sendPasswordResetEmail(resetRequest);

    repository.createEmailLink(emailLink);
    repository.updateUserToInactive(userEntity.getId());
    log.debug("Successfully changed password for user:{}", userEntity.getId());
  }

  /**
   * Requests reset of a users password via message service.
   *
   * @param userId The user PK.
   */
  public void requestPasswordResetEmail(int userId) {
    log.debug("Resetting password for user:{}", userId);
    Optional<UserEntity> optionalUserEntity = repository.retrieveUserById(userId);
    UserEntity userEntity;
    if (optionalUserEntity.isPresent()) {
      userEntity = optionalUserEntity.get();
    } else {
      throw new NotFoundException("user", RETRIEVE);
    }
    requestPasswordResetEmail(userEntity);
  }

  /**
   * Delete a user.
   *
   * @param id PK of user to delete.
   */
  public void deleteUser(int id) {
    if (repository.deleteUser(id) == 0) {
      throw new NotFoundException("user", DELETE);
    }
  }

  public List<UserEntity> retrieveUsersByAuthorityId(int authorityId, String nameFilter) {
    if (null != nameFilter) {
      nameFilter = "%" + nameFilter + "%";
    }
    UserEntity queryParams = new UserEntity();
    queryParams.setLocalAuthorityId(authorityId);
    queryParams.setName(nameFilter);
    queryParams.setEmailAddress(nameFilter);
    return repository.findUsers(queryParams);
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
   * @throws BadRequestException if validation fails.
   */
  public void updateUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = businessValidateUser(userEntity);
    if (null != businessErrors) {
      throw new BadRequestException(businessErrors);
    }
    if (repository.updateUser(userEntity) == 0) {
      throw new NotFoundException("user", UPDATE);
    }
  }

  /** Update user password. */
  public void updatePassword(String uuid, Password passwords) {

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
    if (repository.updatePassword(user) == 0) {
      throw new NotFoundException("user", UPDATE);
    }
  }

  public UserEntity retrieveUserUsingUuid(String uuid) {

    Optional<UserEntity> userEntity = repository.retrieveUserUsingEmailLinkUuid(uuid);
    if (!userEntity.isPresent()) {
      throw new NotFoundException("user", RETRIEVE);
    }
    return userEntity.get();
  }
}
