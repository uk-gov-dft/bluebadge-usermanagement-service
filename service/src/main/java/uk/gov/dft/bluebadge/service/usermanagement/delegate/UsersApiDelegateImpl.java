package uk.gov.dft.bluebadge.service.usermanagement.delegate;

import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.usermanagement.*;
import uk.gov.dft.bluebadge.service.usermanagement.controller.UsersApiDelegate;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.UserManagementService;

@Component
class UsersApiDelegateImpl implements UsersApiDelegate {

  private final UserManagementService service;
  private final UserConverter userConverter = new UserConverter();

  @SuppressWarnings("unused")
  @Autowired
  public UsersApiDelegateImpl(UserManagementService service) {
    this.service = service;
  }

  /**
   * Retrieve a single user.
   *
   * @param authorityId Authority of user.
   * @param userId PK of user to retrieve
   * @return The User wrapped in a UserResponse
   */
  @Override
  public ResponseEntity<UserResponse> authoritiesAuthorityIdUsersUserIdGet(
      Integer authorityId, Integer userId) {
    Optional<UserEntity> userEntity = service.retrieveUserById(userId);
    UserResponse userResponse = new UserResponse();
    userResponse.setData(userConverter.convertToData(userEntity.get(), 1));
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  /**
   * Creates User.
   *
   * @param authorityId Authority of user.
   * @param user User to create.
   * @return The created user with id populated.
   */
  @Override
  public ResponseEntity<UserResponse> authoritiesAuthorityIdUsersPost(
      Integer authorityId, User user) {
    UserEntity entity = userConverter.convertToEntity(user);
    service.createUser(entity);
    UserResponse userResponse = new UserResponse();
    userResponse.setData(userConverter.convertToData(entity, 1));
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  /**
   * Existence check for user email address.
   *
   * @param emailAddress Email address to check for.
   * @return true if exists and request ok else false.
   */
  @Override
  public ResponseEntity<Boolean> usersGet(String emailAddress) {
    if (StringUtils.isEmpty(emailAddress)) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    boolean exists = service.checkUserExistsForEmail(emailAddress);
    return new ResponseEntity<>(exists, HttpStatus.OK);
  }

  /**
   * Get a list of Users for a given Local Authority id.
   *
   * @param authorityId The Local Authority id.
   * @return List of Users
   */
  @Override
  public ResponseEntity<UsersResponse> authoritiesAuthorityIdUsersGet(Integer authorityId) {

    List<UserEntity> userEntityList = service.retrieveUsersByAuthorityId(authorityId);
    UsersData data = new UsersData();
    for (UserEntity userEntity : userEntityList) {
      data.addUsersItem(userConverter.convertToModel(userEntity));
    }

    data.setTotalItems(userEntityList.size());
    return new ResponseEntity<>(new UsersResponse().data(data), HttpStatus.OK);
  }
}
