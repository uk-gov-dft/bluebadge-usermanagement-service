/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen Do not edit the class manually.
 */
package uk.gov.dft.bluebadge.service.usermanagement.generated.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.dft.bluebadge.model.usermanagement.generated.AuthoritiesResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.AuthorityResponse;
import uk.gov.dft.bluebadge.model.usermanagement.generated.CommonResponse;

@Api(value = "Authorities", description = "the Authorities API")
public interface AuthoritiesApi {

  Logger log = LoggerFactory.getLogger(AuthoritiesApi.class);

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
    value = "Create an authority.",
    nickname = "createAuthority",
    notes = "Create a Local Authority",
    response = AuthorityResponse.class,
    tags = {
      "Authorities",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK", response = AuthorityResponse.class),
      @ApiResponse(code = 400, message = "Bad request", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/authorities",
    produces = {"application/json"},
    method = RequestMethod.POST
  )
  default ResponseEntity<AuthorityResponse> createAuthority() {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", AuthorityResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default AuthoritiesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Get list of Local Authorities",
    nickname = "retrieveAuthorities",
    notes = "List of local authorities for users with cross authority roles.",
    response = AuthoritiesResponse.class,
    tags = {
      "Authorities",
    }
  )
  @ApiResponses(
    value = {@ApiResponse(code = 200, message = "OK", response = AuthoritiesResponse.class)}
  )
  @RequestMapping(
    value = "/authorities",
    produces = {"application/json"},
    method = RequestMethod.GET
  )
  default ResponseEntity<AuthoritiesResponse> retrieveAuthorities() {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
      if (getAcceptHeader().get().contains("application/json")) {
        try {
          return new ResponseEntity<>(
              getObjectMapper().get().readValue("\"\"", AuthoritiesResponse.class),
              HttpStatus.NOT_IMPLEMENTED);
        } catch (IOException e) {
          log.error("Couldn't serialize response for content type application/json", e);
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      }
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default AuthoritiesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @ApiOperation(
    value = "Update an authority.",
    nickname = "updateAuthority",
    notes = "Update an authority.",
    tags = {
      "Authorities",
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 400, message = "Bad request.", response = CommonResponse.class)
    }
  )
  @RequestMapping(
    value = "/authorities/{authorityId}",
    produces = {"application/json"},
    method = RequestMethod.PUT
  )
  default ResponseEntity<Void> updateAuthority(
      @ApiParam(value = "ID of the authority.", required = true) @PathVariable("authorityId")
          Integer authorityId) {
    if (getObjectMapper().isPresent() && getAcceptHeader().isPresent()) {
    } else {
      log.warn(
          "ObjectMapper or HttpServletRequest not configured in default AuthoritiesApi interface so no example is generated");
    }
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}