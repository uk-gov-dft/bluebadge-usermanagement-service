package uk.gov.dft.bluebadge.service.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.client.message.api.MessageApiClient;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;
import uk.gov.dft.bluebadge.model.usermanagement.Password;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadResponseException;

@Service
@Transactional
public class UserManagementService {

  private final UserManagementRepository repository;
  private MessageApiClient messageApiClient;

  @Autowired
  UserManagementService(UserManagementRepository repository, MessageApiClient messageApiClient) {
    this.repository = repository;
    this.messageApiClient = messageApiClient;
  }

  public Optional<UserEntity> retrieveUserById(Integer userId) {
    return repository.retrieveUserById(userId);
  }

  /**
   * Create user entity.
   *
   * @param userEntity User to create.
   * @return Create count.
   * @throws BadResponseException if validation fails.
   */
  public int createUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = nonBeanValidation(userEntity);
    if (null != businessErrors) {
      throw new BadResponseException(businessErrors);
    }
    int createCount = repository.createUser(userEntity);
    uk.gov.dft.bluebadge.model.message.User messageUser =
        new uk.gov.dft.bluebadge.model.message.User();
    // TODO common user object?
    // Create a password reset message
    BeanUtils.copyProperties(userEntity, messageUser);
    UUID uuid = messageApiClient.sendPasswordResetEmail(messageUser);
    EmailLink emailLink = new EmailLink();
    emailLink.setUuid(uuid.toString());
    emailLink.setUserId(userEntity.getId());
    repository.createEmailLink(emailLink);
    return createCount;
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
  private List<ErrorErrors> nonBeanValidation(UserEntity userEntity) {
    if (StringUtils.isEmpty(userEntity.getEmailAddress())) {
      // Already sorted in bean validation.
      return null;
    }
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
   * @throws BadResponseException if validation fails.
   */
  public int updateUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = nonBeanValidation(userEntity);
    if (null != businessErrors) {
      throw new BadResponseException(businessErrors);
    }
    return repository.updateUser(userEntity);
  }

  /**
   * Update user password.
   *
   * @return Update count.
   */
  public int updatePassword(String uuid, Password passwords) {

    String password = passwords.getPassword();
    String passwordConfirm = passwords.getPasswordConfirm();

    BadResponseException badResponseException = new BadResponseException();

    if (!password.equals(passwordConfirm)) {
      ErrorErrors error = new ErrorErrors();
      error.setField("passwordConfirm");
      error.setMessage("Pattern.password.passwordConfirm");
      error.setReason("Password confirm field does not match with password field");
      badResponseException.addError(error);
    }

    if (badResponseException.hasErrors()) {
      throw badResponseException;
    }

    EmailLink link = this.repository.retrieveEmailLinkWithUuid(uuid);

    if (link == null || !link.getActive()) {
      ErrorErrors error = new ErrorErrors();
      error.setField("password");
      if (link == null) {
        error.setMessage("Invalid.uuid");
      } else {
        error.setMessage("Inactive.uuid");
      }
      error.setReason("uuid is not valid");
      badResponseException.addError(error);

      throw badResponseException;
    }

    String hash = BCrypt.hashpw(password, BCrypt.gensalt());
    UserEntity user = new UserEntity();
    user.setId(link.getUserId());
    user.setPassword(hash);

    repository.updateEmailLinkToInvalid(uuid);
    return repository.updatePassword(user);
  }

  public Optional<UserEntity> retrieveUserUsingUuid(String uuid) {
    return repository.retrieveUserUsingUuid(uuid);
  }
}
