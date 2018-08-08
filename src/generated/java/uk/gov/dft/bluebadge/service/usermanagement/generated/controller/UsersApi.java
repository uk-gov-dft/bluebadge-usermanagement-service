/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */
package uk.gov.dft.bluebadge.service.usermanagement.generated.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.dft.bluebadge.common.api.model.CommonResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.model.usermanagement.generated.User;
import uk.gov.dft.bluebadge.model.usermanagement.generated.UserResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.UsersResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Optional;

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
      value = "Create a user",
      nickname = "createUser",
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
      value = "/users",
      produces = {"application/json"},
      method = RequestMethod.POST
  )
  default ResponseEntity<UserResponse> createUser(
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
      nickname = "deleteUser",
      notes = "Removes a User from a Local Authority",
      tags = {
          "Users",
      }
  )
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Resource Successfully Removed"),
          @ApiResponse(code = 404, message = "User not found.", response = CommonResponse.class)
      }
  )
  @RequestMapping(
      value = "/users/{uuid}",
      produces = {"application/json"},
      method = RequestMethod.DELETE
  )
  default ResponseEntity<Void> deleteUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
      @ApiParam(value = "UUID of the user.", required = true)
      @PathVariable("uuid")
          String uuid) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
      value = "List of all Users",
      nickname = "findUsers",
      notes = "Returns a list of Users ",
      response = UsersResponse.class,
      tags = {
          "Users",
      }
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 200,
              message = "An array Zero, One or many users",
              response = UsersResponse.class
          ),
          @ApiResponse(code = 200, message = "Unexpected error", response = CommonResponse.class)
      }
  )
  @RequestMapping(
      value = "/users",
      produces = {"application/json"},
      method = RequestMethod.GET
  )
  default ResponseEntity<UsersResponse> findUsers(
      @ApiParam(value = "Name or email address fragment to filter on.")
      @Valid
      @RequestParam(value = "name", required = false)
          Optional<String> name,
      @NotNull
      @ApiParam(value = "To Be Removed. LA id will passed in token", required = true)
      @Valid
      @RequestParam(value = "authorityShortCode", required = true)
          String authorityShortCode) {
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
      value = "Request password reset for user.",
      nickname = "requestPasswordReset",
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
      value = "/users/{uuid}/passwordReset",
      produces = {"application/json"},
      method = RequestMethod.GET
  )
  default ResponseEntity<Void> requestPasswordReset(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
      @ApiParam(value = "Uuid of the user.", required = true)
      @PathVariable("uuid")
          String uuid) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default UsersApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
      value = "Retrieve a specific user",
      nickname = "retrieveUser",
      notes = "Retrieve a user and their roles",
      response = UserResponse.class,
      tags = {
          "Users",
      }
  )
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK", response = UserResponse.class),
          @ApiResponse(code = 404, message = "User not found.", response = CommonResponse.class)
      }
  )
  @RequestMapping(
      value = "/users/{uuid}",
      produces = {"application/json"},
      method = RequestMethod.GET
  )
  default ResponseEntity<UserResponse> retrieveUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
      @ApiParam(value = "UUID of the user.", required = true)
      @PathVariable("uuid")
          String uuid) {
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
      value = "Update password reset for user.",
      nickname = "updatePassword",
      notes = "Update password reset for user.",
      response = UserResponse.class,
      tags = {
          "Users",
      }
  )
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "OK", response = UserResponse.class),
          @ApiResponse(code = 400, message = "Bad request.", response = CommonResponse.class),
          @ApiResponse(code = 404, message = "User not found.", response = CommonResponse.class)
      }
  )
  @RequestMapping(
      value = "/user/password/{uuid}",
      produces = {"application/json"},
      method = RequestMethod.PATCH
  )
  default ResponseEntity<UserResponse> updatePassword(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
      @ApiParam(value = "UUID for password reset.", required = true)
      @PathVariable("uuid")
          String uuid,
      @ApiParam(value = "") @Valid @RequestBody Password password) {
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
      value = "Update a user.",
      nickname = "updateUser",
      notes = "Update a user.",
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
          @ApiResponse(code = 400, message = "Bad request", response = CommonResponse.class),
          @ApiResponse(code = 404, message = "User not found.", response = CommonResponse.class)
      }
  )
  @RequestMapping(
      value = "/users/{uuid}",
      produces = {"application/json"},
      method = RequestMethod.PUT
  )
  default ResponseEntity<UserResponse> updateUser(
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
      @ApiParam(value = "UUID of the user.", required = true)
      @PathVariable("uuid")
          String uuid,
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
}
