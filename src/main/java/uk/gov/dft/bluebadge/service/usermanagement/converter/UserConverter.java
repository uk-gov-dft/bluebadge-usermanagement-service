package uk.gov.dft.bluebadge.service.usermanagement.converter;

import uk.gov.dft.bluebadge.model.usermanagement.generated.User;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

public class UserConverter implements BiConverter<UserEntity, User> {

  @Override
  public UserEntity convertToEntity(User model) {
    UserEntity userEntity = new UserEntity();
    userEntity.setId(model.getId());
    userEntity.setEmailAddress(model.getEmailAddress());
    userEntity.setLocalAuthorityId(model.getLocalAuthorityId());
    userEntity.setName(model.getName());
    userEntity.setRoleId(model.getRoleId());
    userEntity.setRoleName(model.getRoleName());
    return userEntity;
  }

  @Override
  public User convertToModel(UserEntity entity) {
    User user = new User();
    user.setId(entity.getId());
    user.setLocalAuthorityId(entity.getLocalAuthorityId());
    user.setName(entity.getName());
    user.setEmailAddress(entity.getEmailAddress());
    user.setRoleId(entity.getRoleId());
    user.setRoleName(entity.getRoleName());
    return user;
  }
}