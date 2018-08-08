package uk.gov.dft.bluebadge.service.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.model.usermanagement.generated.User;
import uk.gov.dft.bluebadge.model.usermanagement.generated.UserResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.UsersResponse;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.generated.controller.UsersApi;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.UserManagementService;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.ServiceException;

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
  @ExceptionHandler({ServiceException.class})
  public ResponseEntity<CommonResponse> handleServiceException(ServiceException e) {
    return e.getResponse();
  }

  @SuppressWarnings("unused")
  @ExceptionHandler
  public ResponseEntity handleNotFoundException(NotFoundException e) {
    return ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<UserResponse> updatePassword(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "UUID for password reset.", required = true)
          @PathVariable("uuid")
          String uuid,
      @ApiParam() @Valid @RequestBody Password password) {

    UserResponse userResponse = new UserResponse();

    service.updatePassword(uuid, password);
    UserEntity userEntity = service.retrieveUserUsingUuid(uuid);
    userResponse.setData(userConverter.convertToModel(userEntity));

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
   * @param uuid PK of user to retrieve
   * @return The User wrapped in a UserResponse
   */
  @Override
  public ResponseEntity<UserResponse> retrieveUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "UUID of the user.", required = true)
          @PathVariable("uuid")
          String uuid) {
    UserResponse userResponse = new UserResponse();

    UserEntity userEntity = service.retrieveUserById(UUID.fromString(uuid));
    userResponse.setData(userConverter.convertToModel(userEntity));

    return ResponseEntity.ok(userResponse);
  }

  /**
   * Creates User.
   *
   * @param user User to create.
   * @return The created user with id populated.
   */
  @Override
  public ResponseEntity<UserResponse> createUser(@ApiParam() @Valid @RequestBody User user) {
    user.setUuid(UUID.randomUUID().toString());
    UserEntity entity = userConverter.convertToEntity(user);
    UserResponse userResponse = new UserResponse();

    service.createUser(entity);
    userResponse.setData(userConverter.convertToModel(entity));
    return ResponseEntity.ok(userResponse);
  }

  /**
   * Get a list of Users for a given Local Authority id.
   *
   * @param authorityShortCode The Local Authority id.
   * @return List of Users
   */
  @Override
  public ResponseEntity<UsersResponse> findUsers(
      @ApiParam(value = "Name or email address fragment to filter on.")
      @Valid
      @RequestParam(value = "name", required = false)
          Optional<String> name,
      @NotNull
      @ApiParam(value = "To Be Removed. LA id will passed in token", required = true)
      @Valid
      @RequestParam(value = "authorityShortCode")
          String authorityShortCode) {

    log.info("Finding users for authority {}, with filter:{}", authorityShortCode, name);
    List<UserEntity> userEntityList =
        service.retrieveUsersByAuthorityId(authorityShortCode, name.orElse(null));

    return ResponseEntity.ok(
        new UsersResponse().data(userConverter.convertToModelList(userEntityList)));
  }

  @Override
  public ResponseEntity<UserResponse> updateUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "UUID of the user.", required = true)
          @PathVariable("uuid")
          String uuid,
      @ApiParam() @Valid @RequestBody User user) {
    UserEntity entity = userConverter.convertToEntity(user);
    UserResponse userResponse = new UserResponse();

    service.updateUser(entity);
    userResponse.setData(userConverter.convertToModel(entity));
    return ResponseEntity.ok(userResponse);
  }

  @Override
  public ResponseEntity<Void> deleteUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "UUID of the user.", required = true)
          @PathVariable("uuid")
          String uuid) {
    Assert.notNull(uuid, "User id must be provided for delete.");
    service.deleteUser(UUID.fromString(uuid));
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> requestPasswordReset(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "Uuid of the user.", required = true)
          @PathVariable("uuid")
          String uuid) {
    service.requestPasswordResetEmail(UUID.fromString(uuid));
    return ResponseEntity.ok().build();
  }
}
