package uk.gov.dft.bluebadge.service.usermanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dft.bluebadge.common.api.common.VersionAcceptFilter;
import uk.gov.dft.bluebadge.common.esapi.EsapiFilter;
import uk.gov.dft.bluebadge.common.logging.JwtMdcFilter;
import uk.gov.dft.bluebadge.common.logging.VersionLoggingFilter;

@Configuration
public class FilterConfig {

  @Bean
  public EsapiFilter getEsapiFilter() {
    return new EsapiFilter();
  }

  @Bean
  JwtMdcFilter getJwtMdcFilter() {
    return new JwtMdcFilter();
  }

  @Bean
  public VersionLoggingFilter getVersionLoggingFilter(
      @Value("${api.version}") @NotNull String apiVersion) {
    return new VersionLoggingFilter(apiVersion);
  }

  @Bean
  public VersionAcceptFilter getVersionAcceptFilter(
      @Value("${api.version}") @NotNull String apiVersion, ObjectMapper objectMapper) {
    return new VersionAcceptFilter(apiVersion, objectMapper);
  }
}
