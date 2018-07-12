package uk.gov.dft.bluebadge.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.dft.bluebadge.service.usermanagement.UserManagementServiceApplication;

@SpringBootTest(
  classes = UserManagementServiceApplication.class,
  properties = {"management.server.port=19991"}
)
@ActiveProfiles({"test", "dev"})
public abstract class ApplicationContextTests {}
