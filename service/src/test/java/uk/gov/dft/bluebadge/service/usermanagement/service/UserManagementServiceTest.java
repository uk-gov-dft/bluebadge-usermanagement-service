package uk.gov.dft.bluebadge.service.usermanagement.service;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class UserManagementServiceTest {

  @Test
  public void validEmailFormat() {
    UserManagementService userManagementService = new UserManagementService(null);
    Assert.assertTrue(
        "Check valid email format.", userManagementService.validEmailFormat("paul@does.not.exist"));
    Assert.assertFalse(
        "Check invalid email format1.", userManagementService.validEmailFormat("@does.not.exist"));
    Assert.assertFalse(
        "Check invalid email format2.", userManagementService.validEmailFormat("iijjijijijj"));
    Assert.assertFalse(
        "Check invalid email format3.", userManagementService.validEmailFormat("paul@valtech"));
  }
}
