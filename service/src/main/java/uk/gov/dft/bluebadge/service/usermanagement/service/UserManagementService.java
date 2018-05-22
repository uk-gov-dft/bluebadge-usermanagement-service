package uk.gov.dft.bluebadge.service.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.model.usermanagement.ErrorErrors;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BlueBadgeBusinessException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.EmailExistsException;

@Service
@Transactional
public class UserManagementService {

  private final UserManagementRepository repository;

  @Autowired
  public UserManagementService(UserManagementRepository repository) {
    this.repository = repository;
  }

  public Optional<UserEntity> retrieveUserById(Integer userId) {
    return repository.retrieveUserById(userId);
  }

  public int createUser(UserEntity user) throws BlueBadgeBusinessException {
    List<ErrorErrors> businessErrors = nonBeanValidation(user);
    if (null != businessErrors) {
      throw new EmailExistsException(businessErrors);
    }
    return repository.createUser(user);
  }

  public boolean checkUserExistsForEmail(String emailAddress) {
    return repository.checkUserExistsForEmail(emailAddress);
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
  public List<ErrorErrors> nonBeanValidation(UserEntity userEntity) {
    if (StringUtils.isEmpty(userEntity.getEmailAddress())) {
      // Already sorted in bean validation.
      return null;
    }
    List<ErrorErrors> errorsList;
    if (repository.emailAddressAlreadyUsed(userEntity)) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("emailAddress")
          .message("AlreadyExists.user.emailAddress")
          .reason("Email Address already used.");
      errorsList = new ArrayList<>();
      errorsList.add(error);
      return errorsList;
    }

    return null;
  }
}
