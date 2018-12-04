package uk.gov.dft.bluebadge.service.usermanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.client.RestTemplate;
import uk.gov.dft.bluebadge.common.security.BBAccessTokenConverter;
import uk.gov.dft.bluebadge.common.security.Permissions;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {
  public static final int BCRYPT_WORK_FACTOR = 11;
  public static final String OAUTH_2_HAS_SCOPE_LA_WEB_APP = "#oauth2.hasScope('la-webapp')";

  @Value("${blue-badge.auth-server.url}")
  private String authServerUrl;

  @Value("${blue-badge.auth-server.client-id}")
  private String clientId;

  @Value("${blue-badge.auth-server.client-secret}")
  private String clientSecret;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/users/me")
        .fullyAuthenticated()
        .antMatchers("/users", "/users/**")
        .hasAuthority(Permissions.VIEW_USER_DETAILS.getPermissionName())
        .antMatchers("/user", "/user/**")
        .access(OAUTH_2_HAS_SCOPE_LA_WEB_APP)
        .anyRequest()
        .denyAll();
  }

  @Bean
  public RemoteTokenServices tokenService() {
    RemoteTokenServices tokenService = new RemoteTokenServices();
    tokenService.setCheckTokenEndpointUrl(authServerUrl + "/oauth/check_token");
    tokenService.setClientId(clientId);
    tokenService.setClientSecret(clientSecret);
    tokenService.setAccessTokenConverter(jwtAccessTokenConverter());
    tokenService.setRestTemplate(authRestTemplate());
    return tokenService;
  }

  @Bean
  public RestTemplate authRestTemplate() {
    return new RestTemplate();
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(accessTokenConverter());
    return converter;
  }

  @Bean
  public BBAccessTokenConverter accessTokenConverter() {
    return new BBAccessTokenConverter();
  }

  @Bean
  @ConfigurationProperties("blue-badge.auth-server")
  ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
    return new ClientCredentialsResourceDetails();
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCRYPT_WORK_FACTOR);
  }

  @Bean
  public SecurityUtils securityUtils() {
    return new SecurityUtils();
  }
}
