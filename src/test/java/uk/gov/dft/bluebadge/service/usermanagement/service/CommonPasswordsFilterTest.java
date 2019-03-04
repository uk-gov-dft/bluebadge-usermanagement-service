package uk.gov.dft.bluebadge.service.usermanagement.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;

public class CommonPasswordsFilterTest {

  private UserManagementRepository repository = mock(UserManagementRepository.class);
  private CommonPasswordsFilter filter = new CommonPasswordsFilter(repository);

  private static final String PASSWORD = "password";

  @Test(expected = Test.None.class /* no exception expected */)
  public void test_password_unique_enough() {
    when(repository.isPasswordBlacklisted(PASSWORD)).thenReturn(false);

    filter.validatePasswordBlacklisted(PASSWORD);

    verify(repository, times(1)).isPasswordBlacklisted(PASSWORD);
  }

  @Test(expected = BadRequestException.class)
  public void test_password_is_common() {
    when(repository.isPasswordBlacklisted(PASSWORD)).thenReturn(true);

    filter.validatePasswordBlacklisted(PASSWORD);

    verify(repository, times(1)).isPasswordBlacklisted(PASSWORD);
  }
}
