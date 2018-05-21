package uk.gov.dft.bluebadge.service.usermanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

@Mapper
public interface UserManagementMapper {
  UserEntity retrieveUserById(int id);

  List<UserEntity> retrieveUsersByAuthorityId(UserEntity userEntity);

  boolean checkUserExistsForEmail(String emailAddress);

  void updateUser(UserEntity userEntity);

  void createUser(UserEntity userEntity);

  void deleteUser(int id);
}
