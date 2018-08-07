package uk.gov.dft.bluebadge.service.usermanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UuidAuthorityCodeParams;

@SuppressWarnings("unused")
@Mapper
public interface UserManagementMapper {
  UserEntity retrieveUserById(int id);

  List<UserEntity> retrieveUsersByAuthorityId(UserEntity userEntity);

  int updateUser(UserEntity userEntity);

  void createUser(UserEntity userEntity);

  int updatePassword(UserEntity userEntity);

  void deleteUser(int id);

  boolean emailAddressAlreadyUsed(UserEntity userEntity);

  EmailLink retrieveEmailLinkWithUuid(String uuid);

  void createEmailLink(EmailLink emailLink);

  int updateEmailLinkToInvalid(String uuid);

  int updateUserToInactive(UuidAuthorityCodeParams params);
}
