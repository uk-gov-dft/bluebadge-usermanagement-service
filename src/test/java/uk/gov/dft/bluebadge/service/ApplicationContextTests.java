package uk.gov.dft.bluebadge.service;

import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.dft.bluebadge.service.usermanagement.UserManagementServiceApplication;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = UserManagementServiceApplication.class
)
@ActiveProfiles({"test", "dev"})
public abstract class ApplicationContextTests {
  @LocalManagementPort protected int managementPort;
}
