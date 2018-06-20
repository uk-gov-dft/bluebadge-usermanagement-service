package uk.gov.dft.bluebadge.service.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.model.usermanagement.CommonResponse;
import uk.gov.dft.bluebadge.model.usermanagement.Password;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserData;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;
import uk.gov.dft.bluebadge.model.usermanagement.UsersData;
import uk.gov.dft.bluebadge.model.usermanagement.UsersResponse;
import uk.gov.dft.bluebadge.service.usermanagement.controller.UsersApi;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.UserManagementService;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;

@Controller
public class UsersApiControllerImpl implements UsersApi {

  private UserManagementService service;
  private UserConverter userConverter = new UserConverter();
  private ObjectMapper objectMapper;
  private HttpServletRequest request;

  @SuppressWarnings("unused")
  @Autowired
  public UsersApiControllerImpl(
      ObjectMapper objectMapper, HttpServletRequest request, UserManagementService service) {
    this.objectMapper = objectMapper;
    this.request = request;
    this.service = service;
  }

  @SuppressWarnings("unused")
  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<CommonResponse> handleBadRequestException(BadRequestException e) {
    return ResponseEntity.badRequest().body(e.getCommonResponse());
  }

  @SuppressWarnings("unused")
  @ExceptionHandler
  public ResponseEntity handleNotFoundException(NotFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<UserResponse> updatePassword(
      @ApiParam(value = "UUID for  ***REMOVED***)
          String uuid,
      @ApiParam(value = "") @Valid @RequestBody Password passwords) {

    UserResponse userResponse = new UserResponse();

    Optional<UserEntity> userEntity = service.retrieveUserUsingUuid(uuid);

    if (userEntity.isPresent()) {
      userResponse.setData(userConverter.convertToData(userEntity.get(), 1, 0, 0));
    } else {
      UserData userData = new UserData();
      userData.setTotalItems(0);
      userResponse.setData(userData);
    }

    service.updatePassword(uuid, passwords);
    return ResponseEntity.ok(userResponse);
  }

  @Override
  public Optional<ObjectMapper> getObjectMapper() {
    return Optional.ofNullable(objectMapper);
  }

  @Override
  public Optional<HttpServletRequest> getRequest() {
    return Optional.ofNullable(request);
  }

  /**
   * Retrieve a single user.
   *
   * @param authorityId Authority of user.
   * @param userId PK of user to retrieve
   * @return The User wrapped in a UserResponse
   */
  @Override
  public ResponseEntity<UserResponse> retrieveUser(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user to get.", required = true) @PathVariable("userId")
          Integer userId) {
    UserResponse userResponse = new UserResponse();

    UserEntity userEntity = service.retrieveUserById(userId);
    userResponse.setData(userConverter.convertToData(userEntity, 1, 0, 0));

    return ResponseEntity.ok(userResponse);
  }

  /**
   * Creates User.
   *
   * @param authorityId Authority of user.
   * @param user User to create.
   * @return The created user with id populated.
   */
  @Override
  public ResponseEntity<UserResponse> createUser(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam() @Valid @RequestBody User user) {
    UserEntity entity = userConverter.convertToEntity(user);
    UserResponse userResponse = new UserResponse();

    int result = service.createUser(entity);
    userResponse.setData(userConverter.convertToData(entity, 1, result, 0));
    return ResponseEntity.ok(userResponse);
  }

  /**
   * Existence check for user email address.
   *
   * @param emailAddress Email address to check for.
   * @return true if exists and request ok else false.
   */
  @Override
  public ResponseEntity<UserResponse> usersGet(
      @NotNull
          @ApiParam(value = "User email address to check for.", required = true)
          @Valid
          @RequestParam(value = "emailAddress")
          String emailAddress) {

    Optional<UserEntity> userEntity = service.retrieveUserByEmail(emailAddress);
    UserResponse userResponse = new UserResponse();
    if (userEntity.isPresent()) {
      userResponse.setData(userConverter.convertToData(userEntity.get(), 1, 0, 0));
    } else {
      UserData userData = new UserData();
      userData.setTotalItems(0);
      userResponse.setData(userData);
    }
    return ResponseEntity.ok(userResponse);
  }

  /**
   * Get a list of Users for a given Local Authority id.
   *
   * @param authorityId The Local Authority id.
   * @return List of Users
   */
  @Override
  public ResponseEntity<UsersResponse> findUsers(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Name or email address fragment to filter on.")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name) {

    List<UserEntity> userEntityList =
        service.retrieveUsersByAuthorityId(authorityId, name.orElse(null));
    UsersData data = new UsersData().users(Lists.newArrayList());
    for (UserEntity userEntity : userEntityList) {
      data.addUsersItem(userConverter.convertToModel(userEntity));
    }

    data.setTotalItems(userEntityList.size());
    data.setDeleted(0);
    data.setUpdated(0);
    return ResponseEntity.ok(new UsersResponse().data(data));
  }

  @Override
  public ResponseEntity<UserResponse> updateUser(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user.", required = true) @PathVariable("userId")
          Integer userId,
      @ApiParam() @Valid @RequestBody User user) {
    UserEntity entity = userConverter.convertToEntity(user);
    UserResponse userResponse = new UserResponse();

    int result = service.updateUser(entity);
    userResponse.setData(userConverter.convertToData(entity, 1, result, 0));
    return ResponseEntity.ok(userResponse);
  }

  @Override
  public ResponseEntity<Void> deleteUser(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user to remove.", required = true)
          @PathVariable("userId")
          Integer userId) {
    Assert.notNull(userId, "User id must be provided for delete.");
    service.deleteUser(userId);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> requestPasswordReset(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user.", required = true) @PathVariable("userId")
          Integer userId) {

    UserEntity user = service.retrieveUserById(userId);
    service.requestPasswordResetEmail(user, false);
    return ResponseEntity.ok().build();
  }
}
