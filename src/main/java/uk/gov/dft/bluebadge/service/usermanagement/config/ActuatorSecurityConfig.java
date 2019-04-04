package uk.gov.dft.bluebadge.service.usermanagement.config;

import static uk.gov.dft.bluebadge.service.usermanagement.config.ActuatorSecurityConfig.BEFORE_RESOURCE_SERVER_ORDER;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.dft.bluebadge.common.actuator.MyBatisInfo;

@Configuration
@Order(BEFORE_RESOURCE_SERVER_ORDER)
public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {
  /** Order before the resource server (which is 3) */
  public static final int BEFORE_RESOURCE_SERVER_ORDER = 2;

  @Autowired DataSource dataSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.requestMatcher(EndpointRequest.toAnyEndpoint())
        .authorizeRequests()
        .anyRequest()
        .permitAll();
  }

  @Bean
  public MyBatisInfo getMyBatisInfo() {
    return new MyBatisInfo(new JdbcTemplate(dataSource));
  }
}
