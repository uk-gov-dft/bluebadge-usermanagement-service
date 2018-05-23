package uk.gov.dft.bluebadge.service.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.model.usermanagement.*;
import uk.gov.dft.bluebadge.model.usermanagement.Error;
import uk.gov.dft.bluebadge.service.usermanagement.controller.UsersApi;
import uk.gov.dft.bluebadge.service.usermanagement.converter.UserConverter;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.service.UserManagementService;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BlueBadgeBusinessException;

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
  public ResponseEntity<UserResponse> authoritiesAuthorityIdUsersUserIdGet(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user to get.", required = true) @PathVariable("userId")
          Integer userId) {
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
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "") @Valid @RequestBody User user) {
    UserEntity entity = userConverter.convertToEntity(user);
    UserResponse userResponse = new UserResponse();
    HttpStatus status;

    try {
      service.createUser(entity);
      userResponse.setData(userConverter.convertToData(entity, 1));
      status = HttpStatus.OK;
    } catch (BlueBadgeBusinessException e) {
      Error error = new Error();
      for (ErrorErrors errorItem : e.getErrorsList()) {
        error.addErrorsItem(errorItem);
      }
      userResponse.setError(error);
      status = HttpStatus.BAD_REQUEST;
    }

    return new ResponseEntity<>(userResponse, status);
  }

  /**
   * Existence check for user email address.
   *
   * @param emailAddress Email address to check for.
   * @return true if exists and request ok else false.
   */
  @Override
  public ResponseEntity<Boolean> usersGet(
      @NotNull
          @ApiParam(value = "User email address to check for.", required = true)
          @Valid
          @RequestParam(value = "emailAddress", required = true)
          String emailAddress) {
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
  public ResponseEntity<UsersResponse> authoritiesAuthorityIdUsersGet(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Name or email address fragment to filter on.")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name) {

    List<UserEntity> userEntityList =
        service.retrieveUsersByAuthorityId(authorityId, name.orElse(null));
    UsersData data = new UsersData();
    for (UserEntity userEntity : userEntityList) {
      data.addUsersItem(userConverter.convertToModel(userEntity));
    }

    data.setTotalItems(userEntityList.size());
    return new ResponseEntity<>(new UsersResponse().data(data), HttpStatus.OK);
  }
}
