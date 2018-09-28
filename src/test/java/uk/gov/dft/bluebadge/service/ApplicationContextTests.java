package uk.gov.dft.bluebadge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.dft.bluebadge.service.usermanagement.UserManagementServiceApplication;

@SpringBootTest(
  classes = UserManagementServiceApplication.class,
  properties = {"management.server.port=0"}
)
@ActiveProfiles({"test", "dev"})
public abstract class ApplicationContextTests {
  @Value("${local.management.port}")
  protected int managementPort;
}
