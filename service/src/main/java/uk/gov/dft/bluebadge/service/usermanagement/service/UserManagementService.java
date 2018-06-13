package uk.gov.dft.bluebadge.service.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;
import uk.gov.dft.bluebadge.model.usermanagement.Password;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadResponseException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BlueBadgeBusinessException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.UserEntityValidationException;

@Service
@Transactional
public class UserManagementService {

  private final UserManagementRepository repository;
  private static final Pattern emailPattern =
      Pattern.compile(
          "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
          Pattern.CASE_INSENSITIVE);
  private static final Pattern passwordPattern =
      Pattern.compile("^[^\\s-]{8,}$", Pattern.CASE_INSENSITIVE);

  @Autowired
  UserManagementService(UserManagementRepository repository) {
    this.repository = repository;
  }

  public Optional<UserEntity> retrieveUserById(Integer userId) {
    return repository.retrieveUserById(userId);
  }

  /**
   * Create user entity.
   *
   * @param userEntity User to create.
   * @return Create count.
   * @throws BlueBadgeBusinessException if validation fails.
   */
  public int createUser(UserEntity userEntity) throws BlueBadgeBusinessException {
    List<ErrorErrors> businessErrors = nonBeanValidation(userEntity);
    if (null != businessErrors) {
      throw new UserEntityValidationException(businessErrors);
    }
    return repository.createUser(userEntity);
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
   * Version working on User API object. Called from bean validation to add extra business
   * validation when there is already falied bean validation.
   *
   * @param user User API bean.
   * @return List of errors.
   */
  public List<ErrorErrors> nonBeanValidation(User user) {
    UserConverter converter = new UserConverter();
    return nonBeanValidation(converter.convertToEntity(user));
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

    if (!validEmailFormat(userEntity.getEmailAddress())) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("emailAddress")
          .message("Pattern.user.emailAddress")
          .reason("Not a valid email address.");
      errorsList = new ArrayList<>();
      errorsList.add(error);
    } else if (repository.emailAddressAlreadyUsed(userEntity)) {
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

  boolean validEmailFormat(String emailAddress) {
    Matcher matcher = emailPattern.matcher(emailAddress);
    return matcher.find();
  }

  public boolean validPasswordFormat(String password) {
    Matcher matcher = passwordPattern.matcher(password);
    return matcher.find();
  }

  /**
   * Update user entity.
   *
   * @param userEntity Entity to update.
   * @return Update count.
   * @throws BlueBadgeBusinessException if validation fails.
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
  @Transactional
  public int updatePassword(Password passwords) {

    String password = passwords.getPassword();
    String uuid = passwords.getUuid();
    String passwordConfirm = passwords.getPasswordConfirm();

    boolean isPasswordValid = this.validPasswordFormat(password);
    boolean isPasswordConfirmValid = this.validPasswordFormat(passwordConfirm);

    BadResponseException badResponseException = new BadResponseException();

    if (!isPasswordValid || !isPasswordConfirmValid) {
      ErrorErrors error = new ErrorErrors();
      error.setField("password or passwordConfirm");
      error.setMessage("Pattern.user.password");
      error.setReason("Invalid password format.");
      badResponseException.addError(error);
    }

    if (!password.equals(passwordConfirm)) {
      ErrorErrors error = new ErrorErrors();
      error.setField("passwordConfirm");
      error.setMessage("Pattern.user.password.confirm");
      error.setReason("Passwords do not match");
      badResponseException.addError(error);
    }

    if (badResponseException.hasErrors()) {
      throw badResponseException;
    }

    EmailLink link = this.repository.retrieveEmailLinkWithUuid(uuid);

    if (link == null || !link.getActive()) {
      ErrorErrors error = new ErrorErrors();
      error.setField("uuid");
      if (link == null) {
        error.setMessage("Invalid.password.uuid");
      } else {
        error.setMessage("Inactive.password.uuid");
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
}
