package uk.gov.dft.bluebadge.service.usermanagement.delegate;

import java.util.Optional;
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
public class UsersApiDelegateImpl implements UsersApiDelegate {

  private UserManagementService service;

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
}
