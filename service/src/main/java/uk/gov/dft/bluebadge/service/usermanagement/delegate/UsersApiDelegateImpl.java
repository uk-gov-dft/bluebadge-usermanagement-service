package uk.gov.dft.bluebadge.service.usermanagement.delegate;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;
import uk.gov.dft.bluebadge.service.usermanagement.controller.UsersApiDelegate;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.UserManagementService;

@Component
class UsersApiDelegateImpl implements UsersApiDelegate {

  private final UserManagementService service;

  @Autowired
  public UsersApiDelegateImpl(UserManagementService service) {
    this.service = service;
  }

  @Override
  public ResponseEntity<UserResponse> authoritiesAuthorityIdUsersUserIdGet(
      Integer authorityId, Integer userId) {
    Optional<UserEntity> userEntity = service.retrieveUserById(userId);
    UserConverter converter = new UserConverter();
    User user = converter.convertToModel(userEntity.get());
    UserResponse userResponse = new UserResponse();
    userResponse.setData(user);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserResponse> authoritiesAuthorityIdUsersPost(
      Integer authorityId, User user) {
    UserConverter converter = new UserConverter();
    UserEntity entity = converter.convertToEntity(user);
    //int result =
    service.createUser(entity);
    user = converter.convertToModel(entity);
    UserResponse userResponse = new UserResponse();
    userResponse.setData(user);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  /**
   * Existence check for user email address
   *
   * @param emailAddress Email address to check for
   * @return true if exists and request ok else false.
   */
  @Override
  public ResponseEntity<Boolean> usersGet(String emailAddress) {
    if (StringUtils.isEmpty(emailAddress)) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    try {
      boolean exists = service.checkUserExistsForEmail(emailAddress);
      return new ResponseEntity<>(exists, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
