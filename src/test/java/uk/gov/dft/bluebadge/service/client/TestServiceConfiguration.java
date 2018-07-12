package uk.gov.dft.bluebadge.service.client;

import uk.gov.dft.bluebadge.service.client.common.ServiceConfiguration;

public class TestServiceConfiguration {
  public static ServiceConfiguration.ServiceConfigurationBuilder full() {
    return ServiceConfiguration.builder()
        .contextpath("test")
        .host("someHost")
        .port(7897)
        .scheme("scheme");
  }
}
