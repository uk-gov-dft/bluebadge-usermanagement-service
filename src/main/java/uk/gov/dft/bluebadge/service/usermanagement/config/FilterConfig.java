package uk.gov.dft.bluebadge.service.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dft.bluebadge.common.esapi.EsapiFilter;
import uk.gov.dft.bluebadge.common.logging.JwtMdcFilter;

@Configuration
public class FilterConfig {

  @Bean
  public EsapiFilter getEsapiFilter() {
    return new EsapiFilter();
  }

  @Bean
  JwtMdcFilter getJwtMdcFilter() { return new JwtMdcFilter(); }

}
