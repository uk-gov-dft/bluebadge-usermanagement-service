package uk.gov.dft.bluebadge.service.usermanagement.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

@Service
@Transactional
public class UserManagementService {

  private final UserManagementRepository repository;

  @Autowired
  public UserManagementService(UserManagementRepository repository) {
    this.repository = repository;
  }

  public Optional<UserEntity> retrieveUserById(Integer userId) {
    return repository.retrieveUserById(userId);
  }

  public int createUser(UserEntity user) {
    return repository.createUser(user);
  }

  public boolean checkUserExistsForEmail(String emailAddress) {
    return repository.checkUserExistsForEmail(emailAddress);
  }
}
