package uk.gov.dft.bluebadge.service.usermanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import uk.gov.dft.bluebadge.service.client.common.ServiceConfiguration;

@Configuration
public class ApiConfig {

  @ConfigurationProperties("blue-badge.messageservice.servicehost")
  @Bean
  public ServiceConfiguration messageServiceConfiguration() {
    return ServiceConfiguration.builder().build();
  }

  /**
   * OAuth rest template configured with a token forwarding context. So if a bearer token is found
   * on the security context, then it is used for the rest template.
   */
  @Bean("messageServiceRestTemplate")
  RestTemplate messageServiceRestTemplate(
      ClientCredentialsResourceDetails clientCredentialsResourceDetails,
      ServiceConfiguration userManagementApiConfig) {
    OAuth2RestTemplate result =
        new OAuth2RestTemplate(
            clientCredentialsResourceDetails, new TokenForwardingClientContext());
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    result.setRequestFactory(requestFactory);
    result.setUriTemplateHandler(
        new DefaultUriBuilderFactory(userManagementApiConfig.getUrlPrefix()));
    return result;
  }
}
