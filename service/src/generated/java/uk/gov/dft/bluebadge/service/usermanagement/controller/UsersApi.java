/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */
package uk.gov.dft.bluebadge.service.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.model.usermanagement.CommonResponse;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;
import uk.gov.dft.bluebadge.model.usermanagement.UsersResponse;

@Api(value = "Users", description = "the Users API")
public interface UsersApi {

  Logger log = LoggerFactory.getLogger(UsersApi.class);

  default Optional<ObjectMapper> getObjectMapper() {
    return Optional.empty();
  }

  default Optional<HttpServletRequest> getRequest() {
    return Optional.empty();
  }

  default Optional<String> getAcceptHeader() {
    return getRequest().map(r -> r.getHeader("Accept"));
  }

  @ApiOperation(
    value = "List of all Users",
    nickname = "authoritiesAuthorityIdUsersGet",
    notes = "Returns a list of Users ",
    response = UsersResponse.class,
    tags = {
      "Users",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "An array of users", response = UsersResponse.class),
      @ApiResponse(code = 200, message = "Unexpected error", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/authorities/{authorityId}/users",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<UsersResponse> authoritiesAuthorityIdUsersGet(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Name or email address fragment to filter on.")
          @Valid
          @RequestParam(value = "name", required = false)
          Optional<String> name) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", UsersResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Create a user",
    nickname = "authoritiesAuthorityIdUsersPost",
    notes = "Create a user",
    response = UserResponse.class,
    tags = {
      "Users",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = UserResponse.class),
      @ApiResponse(code = 400, message = "Bad request", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/authorities/{authorityId}/users",
    produces = {"application/json"},
    method = RequestMethod.POST
  )
  default ResponseEntity<UserResponse> authoritiesAuthorityIdUsersPost(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "") @Valid @RequestBody User user) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", UserResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Removes a User from a Local Authority",
    nickname = "authoritiesAuthorityIdUsersUserIdDelete",
    notes = "Removes a User from a Local Authority",
    tags = {
      "Users",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Resource Successfully Removed")})
  @RequestMapping(
    value = "/authorities/{authorityId}/users/{userId}",
    produces = {"application/json"},
    method = RequestMethod.DELETE
  )
  default ResponseEntity<Void> authoritiesAuthorityIdUsersUserIdDelete(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user.", required = true) @PathVariable("userId")
          Integer userId) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Retrieve a specific user",
    nickname = "authoritiesAuthorityIdUsersUserIdGet",
    notes = "Retrieve a user and their roles",
    response = UserResponse.class,
    tags = {
      "Users",
    }
  )
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = UserResponse.class)})
  @RequestMapping(
    value = "/authorities/{authorityId}/users/{userId}",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<UserResponse> authoritiesAuthorityIdUsersUserIdGet(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user.", required = true) @PathVariable("userId")
          Integer userId) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", UserResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Request password reset for user.",
    nickname = "authoritiesAuthorityIdUsersUserIdPasswordResetGet",
    notes = "Email user with password reset link.",
    tags = {
      "Users",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Bad request.", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/authorities/{authorityId}/users/{userId}/passwordReset",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<Void> authoritiesAuthorityIdUsersUserIdPasswordResetGet(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user.", required = true) @PathVariable("userId")
          Integer userId) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Update a user",
    nickname = "authoritiesAuthorityIdUsersUserIdPut",
    notes = "Update a user",
    response = UserResponse.class,
    tags = {
      "Users",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Resource valid and updated if exists.",
        response = UserResponse.class
      ),
      @ApiResponse(code = 400, message = "Bad request", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/authorities/{authorityId}/users/{userId}",
    produces = {"application/json"},
    method = RequestMethod.PUT
  )
  default ResponseEntity<UserResponse> authoritiesAuthorityIdUsersUserIdPut(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId,
      @ApiParam(value = "Numeric ID of the user.", required = true) @PathVariable("userId")
          Integer userId,
      @ApiParam(value = "") @Valid @RequestBody User user) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", UserResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Check user for email address exists.",
    nickname = "usersGet",
    notes = "Returns user if user exists ",
    response = UserResponse.class,
    tags = {
      "Users",
    }
  )
  @ApiResponses(
    value = {@ApiResponse(code = 200, message = "The user.", response = UserResponse.class)}
  )
  @RequestMapping(
    value = "/users",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<UserResponse> usersGet(
      @NotNull
          @ApiParam(value = "User email address to check for.", required = true)
          @Valid
          @RequestParam(value = "emailAddress", required = true)
          String emailAddress) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", UserResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
