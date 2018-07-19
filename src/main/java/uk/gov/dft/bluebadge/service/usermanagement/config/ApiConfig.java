package uk.gov.dft.bluebadge.service.usermanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dft.bluebadge.service.client.common.ServiceConfiguration;

@Configuration
public class ApiConfig {

  @ConfigurationProperties("blue-badge.messageservice.servicehost")
  @Bean
  public ServiceConfiguration messageServiceConfiguration() {
    return ServiceConfiguration.builder().build();
  }
}
