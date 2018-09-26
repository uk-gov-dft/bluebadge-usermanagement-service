package uk.gov.dft.bluebadge.service.usermanagement.repository.mapper;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UuidAuthorityCodeParams;

@SuppressWarnings("unused")
@Mapper
public interface UserManagementMapper {

  Optional<UserEntity> retrieveUserByUuid(UuidAuthorityCodeParams id);

  List<UserEntity> findUsers(UserEntity userEntity);

  int updateUser(UserEntity userEntity);

  void createUser(UserEntity userEntity);

  int updatePassword(UserEntity userEntity);

  int deleteUser(UuidAuthorityCodeParams id);

  boolean emailAddressAlreadyUsed(UserEntity userEntity);

  EmailLink retrieveEmailLinkWithUuid(String uuid);

  void createEmailLink(EmailLink emailLink);

  int updateEmailLinkToInvalid(String uuid);

  int updateUserToInactive(UuidAuthorityCodeParams params);

  public boolean isPasswordBlacklisted(String password);
}
