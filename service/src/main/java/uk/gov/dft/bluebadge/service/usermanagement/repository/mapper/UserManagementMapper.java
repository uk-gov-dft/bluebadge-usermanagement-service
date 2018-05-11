package uk.gov.dft.bluebadge.service.usermanagement.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

import java.util.List;

@Mapper
public interface UserManagementMapper {
  UserEntity retrieveUserById(int id);
  List<UserEntity> retrieveUsersByAuthorityId(int id);
  void updateUser(UserEntity userEntity);
  void createUser(UserEntity userEntity);
  void deleteUser(int id);
}
