package uk.gov.dft.bluebadge.service.usermanagement.converter;

import java.util.UUID;
import uk.gov.dft.bluebadge.common.converter.ToEntityConverter;
import uk.gov.dft.bluebadge.common.converter.ToModelConverter;
import uk.gov.dft.bluebadge.model.usermanagement.generated.User;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

public class UserConverter
    implements ToModelConverter<UserEntity, User>, ToEntityConverter<UserEntity, User> {

  @Override
  public UserEntity convertToEntity(User model) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.fromString(model.getUuid()));
    userEntity.setEmailAddress(model.getEmailAddress());
    userEntity.setAuthorityCode(model.getLocalAuthorityShortCode());
    userEntity.setName(model.getName());
    userEntity.setRoleId(model.getRoleId());
    userEntity.setRoleName(model.getRoleName());
    return userEntity;
  }

  @Override
  public User convertToModel(UserEntity entity) {
    User user = new User();
    user.setUuid(entity.getUuid().toString());
    user.setLocalAuthorityShortCode(entity.getAuthorityCode());
    user.setName(entity.getName());
    user.setEmailAddress(entity.getEmailAddress());
    user.setRoleId(entity.getRoleId());
    user.setRoleName(entity.getRoleName());
    return user;
  }
}
