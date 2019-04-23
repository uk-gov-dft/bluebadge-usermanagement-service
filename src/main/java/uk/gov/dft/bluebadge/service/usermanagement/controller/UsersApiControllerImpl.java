package uk.gov.dft.bluebadge.service.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dft.bluebadge.common.api.common.CommonResponseHandler;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.model.usermanagement.generated.User;
import uk.gov.dft.bluebadge.model.usermanagement.generated.UserResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.UsersResponse;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.generated.controller.UsersApi;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.UserManagementService;

@RestController
@CommonResponseHandler
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
  @PreAuthorize("hasAuthority('PERM_VIEW_USER_DETAILS')")
  @PostAuthorize(
      "@securityUtils.isAuthorisedLACode(returnObject.body.data.localAuthorityShortCode)")
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

  @Override
  public ResponseEntity<UserResponse> retrieveCurrentUser() {
    UserResponse userResponse = new UserResponse();

    UserEntity userEntity = service.retrieveCurrentUser();
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
  @PreAuthorize(
      "hasAuthority('PERM_CREATE_USER') and @securityUtils.isAuthorisedLACode(#user.localAuthorityShortCode)")
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
   * @return List of Users
   */
  @Override
  @PreAuthorize("hasAuthority('PERM_FIND_USERS')")
  public ResponseEntity<UsersResponse> findUsers(
      @ApiParam(value = "Name or email address fragment to filter on.")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name) {

    log.debug("Finding users with filter:{}", name);
    List<UserEntity> userEntityList = service.findUsers(name.orElse(null));

    return ResponseEntity.ok(
        new UsersResponse().data(userConverter.convertToModelList(userEntityList)));
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_UPDATE_USER') and @userSecurity.isAuthorised(#uuid)")
  public ResponseEntity<UserResponse> updateUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "UUID of the user.", required = true)
          @PathVariable("uuid")
          String uuid,
      @ApiParam() @Valid @RequestBody User user) {
    if (!uuid.equals(user.getUuid())) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("uuid")
          .message("Invalid user uuid")
          .reason("Model uuid and path variable uuid do not match.");
      throw new BadRequestException(error);
    }
    UserEntity entity = userConverter.convertToEntity(user);
    service.updateUser(entity);
    UserResponse userResponse = new UserResponse();
    userResponse.setData(userConverter.convertToModel(entity));
    return ResponseEntity.ok(userResponse);
  }

  @Override
  @PreAuthorize("hasAuthority('PERM_DELETE_USER') and @userSecurity.isAuthorised(#uuid)")
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
  @PreAuthorize("hasAuthority('PERM_RESET_USER_PASSWORD') and @userSecurity.isAuthorised(#uuid)")
  public ResponseEntity<Void> requestPasswordReset(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
          @ApiParam(value = "Uuid of the user.", required = true)
          @PathVariable("uuid")
          String uuid) {
    service.requestPasswordResetEmail(UUID.fromString(uuid));
    return ResponseEntity.ok().build();
  }
}
