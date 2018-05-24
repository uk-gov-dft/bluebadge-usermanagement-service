package uk.gov.dft.bluebadge.client.usermanagement.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.client.usermanagement.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.usermanagement.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.usermanagement.Error;
import uk.gov.dft.bluebadge.model.usermanagement.*;

import java.util.List;

@Service
public class UserManagementService {

  private class UserTypeRef extends ParameterizedTypeReference<List<User>> {}

  private static final String GET_USER_FOR_EMAIL = "/users?emailAddress={emailAddress}";
  private static final String GET_BY_ID_ENDPOINT = "/authorities/{authorityId}/users/{userId}";
  private static final String CREATE_ENDPOINT = "/authorities/{authorityId}/users";
  private static final String GET_USERS_FOR_AUTHORITY_ENDPOINT =
      "/authorities/{authorityId}/users?name={name}";
  private RestTemplateFactory restTemplateFactory;
  private ServiceConfiguration serviceConfiguration;

  @Autowired
  public UserManagementService(
      ServiceConfiguration serviceConfiguration, RestTemplateFactory restTemplateFactory) {
    this.serviceConfiguration = serviceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  public boolean checkUserExistsForEmail(String emailAddress) {
    Assert.notNull(emailAddress, "emailAddress must be supplied");

    UserResponse userResponse = getUserForEmail(emailAddress);
    return 1 == userResponse.getData().getTotalItems();
  }

  public UserResponse getUserForEmail(String emailAddress) {

    String trimmedAddress = StringUtils.trimToNull(emailAddress);
    Assert.notNull(trimmedAddress, "emailAddress must be provided for getUserForEmail");

    UserResponse response =
        restTemplateFactory
            .getInstance()
            .getForEntity(
                serviceConfiguration.getUrlPrefix() + GET_USER_FOR_EMAIL,
                UserResponse.class,
                trimmedAddress)
            .getBody();

    return response;
  }

  /**
   * Retrieve a list of users for a Local Authority wrapped in a UsersResponse.
   *
   * @param authorityId The Local Authority to retrieve for.
   * @return UsersResponse containing list of Users.
   */
  public UsersResponse getUsersForAuthority(Integer authorityId, String nameFilter) {

    Assert.notNull(authorityId, "Local Authority Id must be provided");
    /*
        ResponseEntity<UsersResponse> userListResponse =
            restTemplateFactory
                .getInstance()
                .getForEntity(
                    UriComponentsBuilder.fromUriString(
                            serviceConfiguration.getUrlPrefix() + GET_USERS_FOR_AUTHORITY_ENDPOINT)
                        .queryParam("name", nameFilter)
                        .build(authorityId),
                    UsersResponse.class);
    */
    ResponseEntity<UsersResponse> userListResponse =
        restTemplateFactory
            .getInstance()
            .getForEntity(
                serviceConfiguration.getUrlPrefix() + GET_USERS_FOR_AUTHORITY_ENDPOINT,
                UsersResponse.class,
                authorityId,
                nameFilter);
    return userListResponse.getBody();
  }

  public UserResponse getById(Integer authorityId, Integer userId) {
    Assert.notNull(authorityId, "authorityId must be provided for getById");
    Assert.notNull(userId, "userId must be provided for getById");

    UserResponse response =
        restTemplateFactory
            .getInstance()
            .getForEntity(
                serviceConfiguration.getUrlPrefix() + GET_BY_ID_ENDPOINT,
                UserResponse.class,
                authorityId,
                userId)
            .getBody();

    return response;
  }

  public UserResponse createUser(Integer authorityId, User user) {
    Assert.notNull(authorityId, "Authority Id must be provided");
    Assert.notNull(user, "must be set");

    HttpEntity<User> request = new HttpEntity<>(user);

    UserResponse response =
        restTemplateFactory
            .getInstance()
            .postForObject(
                serviceConfiguration.getUrlPrefix() + CREATE_ENDPOINT,
                request,
                UserResponse.class,
                authorityId);
    return response;
  }

  public UserResponse updateUser(User user) {
    Assert.notNull(user, "must be set");

    UserResponse response = new UserResponse();
    // Error response.
    if (StringUtils.stripToNull(user.getName()) == null) {
      Error error = new Error();
      ErrorErrors nameError = new ErrorErrors();
      nameError.message("NotNull.user.name").field("name");
      error.addErrorsItem(nameError);
    } else if (user.getName().toLowerCase().startsWith("notexists")) {
      // Return a success
      UserData data = new UserData();
      data.setUpdated(0);
      data.setRoleName(user.getRoleName());
      data.name(user.getName())
          .id(user.getId())
          .localAuthorityId(user.getLocalAuthorityId())
          .roleId(user.getRoleId())
          .roleName(user.getRoleName());
      response.setData(data);
    } else {
      // Return a success
      UserData data = new UserData();
      data.setUpdated(1);
      data.setRoleName(user.getRoleName());
      data.name(user.getName())
          .id(user.getId())
          .localAuthorityId(user.getLocalAuthorityId())
          .roleId(user.getRoleId())
          .roleName(user.getRoleName());
      response.setData(data);
    }
    return response;
  }

  public UserResponse deleteUser(User user) {
    Assert.notNull(user, "must be set");
    Assert.notNull(user.getName(), "must be set");

    UserResponse response = new UserResponse();
    if (user.getName().toLowerCase().startsWith("notexists")) {
      UserData data = new UserData();
      data.setUpdated(0);
      response.setData(data);
    } else {
      // Return a success
      UserData data = new UserData();
      data.setUpdated(1);
      response.setData(data);
    }
    return response;
  }
}
