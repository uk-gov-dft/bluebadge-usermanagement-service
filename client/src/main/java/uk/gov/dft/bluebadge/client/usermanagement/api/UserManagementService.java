package uk.gov.dft.bluebadge.client.usermanagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.client.usermanagement.configuration.ServiceConfiguration;
import uk.gov.dft.bluebadge.client.usermanagement.httpclient.RestTemplateFactory;
import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserResponse;

@Service
public class UserManagementService {

  private final static String GET_BY_ID_ENDPOINT = "/authorities/{authorityId}/users/{userId}";
  private final static String CREATE_ENDPOINT = "/authorities/{authorityId}/users";
  private RestTemplateFactory restTemplateFactory;
  private ServiceConfiguration serviceConfiguration;

  @Autowired
  public UserManagementService(ServiceConfiguration serviceConfiguration
      , RestTemplateFactory restTemplateFactory) {
    this.serviceConfiguration = serviceConfiguration;
    this.restTemplateFactory = restTemplateFactory;
  }

  public UserResponse getById(Integer authorityId, Integer userId) {
    Assert.notNull(authorityId, "authorityId must be provided for getById");
    Assert.notNull(userId, "userId must be provided for getById");

    UserResponse response
        = restTemplateFactory.getInstance().getForEntity(
        serviceConfiguration.getUrlPrefix() + GET_BY_ID_ENDPOINT
        , UserResponse.class, authorityId, userId).getBody();

    return response;
  }

  public UserResponse createUser(Integer authorityId, User user) {
    Assert.notNull(authorityId, "Authority Id must be provided");
    Assert.notNull(user, "must be set");

    HttpEntity<User> request = new HttpEntity<>(user);


    UserResponse response
        = restTemplateFactory.getInstance().postForObject(serviceConfiguration.getUrlPrefix() + CREATE_ENDPOINT
        ,request,UserResponse.class, authorityId);
    return response;
  }
}
