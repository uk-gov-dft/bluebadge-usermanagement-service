package uk.gov.dft.bluebadge.client.usermanagement.api;

import static uk.gov.dft.bluebadge.client.usermanagement.api.UserManagementService.Endpoints.CREATE_ENDPOINT;
import static uk.gov.dft.bluebadge.client.usermanagement.api.UserManagementService.Endpoints.DELETE_ENDPOINT;
import static uk.gov.dft.bluebadge.client.usermanagement.api.UserManagementService.Endpoints.GET_BY_ID_ENDPOINT;
import static uk.gov.dft.bluebadge.client.usermanagement.api.UserManagementService.Endpoints.GET_USERS_FOR_AUTHORITY_ENDPOINT;
import static uk.gov.dft.bluebadge.client.usermanagement.api.UserManagementService.Endpoints.GET_USER_BY_EMAIL_ENDPOINT;
import static uk.gov.dft.bluebadge.client.usermanagement.api.UserManagementService.Endpoints.UPDATE_ENDPOINT;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.dft.bluebadge.client.usermanagement.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.usermanagement.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;
import uk.gov.dft.bluebadge.model.usermanagement.UsersResponse;

@Service
public class UserManagementService {

  class Endpoints {
    static final String GET_USER_BY_EMAIL_ENDPOINT = "/users?emailAddress={emailAddress}";
    static final String GET_BY_ID_ENDPOINT = "/authorities/{authorityId}/users/{userId}";
    static final String CREATE_ENDPOINT = "/authorities/{authorityId}/users";
    static final String GET_USERS_FOR_AUTHORITY_ENDPOINT =
        "/authorities/{authorityId}/users?name={name}";
    static final String UPDATE_ENDPOINT = "/authorities/{authorityId}/users/{userId}";
    static final String DELETE_ENDPOINT = "/authorities/{authorityId}/users/{userId}";
  }

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
                serviceConfiguration.getUrlPrefix() + GET_USER_BY_EMAIL_ENDPOINT,
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

    HttpEntity<User> request = new HttpEntity<>(user);

    String uri =
        UriComponentsBuilder.fromUriString(serviceConfiguration.getUrlPrefix() + UPDATE_ENDPOINT)
            .build()
            .toUriString();

    UserResponse response =
        restTemplateFactory
            .getInstance()
            .exchange(
                uri,
                HttpMethod.PUT,
                request,
                UserResponse.class,
                user.getLocalAuthorityId(),
                user.getId())
            .getBody();

    return response;
  }

  public void deleteUser(Integer localAuthorityId, Integer userId) {
    Assert.notNull(localAuthorityId, "must be set");
    Assert.notNull(userId, "must be set");

    String uri =
        UriComponentsBuilder.fromUriString(serviceConfiguration.getUrlPrefix() + DELETE_ENDPOINT)
            .build()
            .toUriString();

    restTemplateFactory
        .getInstance()
        .delete(
            uri,
            localAuthorityId,
            userId);
  }
}
