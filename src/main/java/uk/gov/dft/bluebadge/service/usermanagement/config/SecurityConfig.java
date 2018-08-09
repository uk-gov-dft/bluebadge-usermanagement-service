package uk.gov.dft.bluebadge.service.usermanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import uk.gov.dft.bluebadge.common.security.BBAccessTokenConverter;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;

@Configuration
@EnableResourceServer
public class SecurityConfig {
  public static final int BCRYPT_WORK_FACTOR = 11;

  @Value("${blue-badge.auth-server.url}")
  private String authServerUrl;

  @Value("${blue-badge.auth-server.client-id}")
  private String clientId;

  @Value("${blue-badge.auth-server.client-secret}")
  private String clientSecret;

  @Bean
  public RemoteTokenServices tokenService() {
    RemoteTokenServices tokenService = new RemoteTokenServices();
    tokenService.setCheckTokenEndpointUrl(authServerUrl + "/oauth/check_token");
    tokenService.setClientId(clientId);
    tokenService.setClientSecret(clientSecret);
    tokenService.setAccessTokenConverter(jwtAccessTokenConverter());
    return tokenService;
  }

  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setAccessTokenConverter(accessTokenConverter());
    return converter;
  }

  @Bean
  public BBAccessTokenConverter accessTokenConverter() {
    BBAccessTokenConverter converter = new BBAccessTokenConverter();
    return converter;
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
