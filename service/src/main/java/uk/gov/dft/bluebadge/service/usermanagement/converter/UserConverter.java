package uk.gov.dft.bluebadge.service.usermanagement.converter;

import uk.gov.dft.bluebadge.model.usermanagement.User;
import uk.gov.dft.bluebadge.model.usermanagement.UserData;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

public class UserConverter implements BiConverter<UserEntity, User, UserData> {

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

  @Override
  public UserData convertToData(UserEntity userEntity, int totalItems, int updates, int deletes) {
    UserData data = new UserData();
    data.setId(userEntity.getId());
    data.setLocalAuthorityId(userEntity.getLocalAuthorityId());
    data.setName(userEntity.getName());
    data.setEmailAddress(userEntity.getEmailAddress());
    data.setTotalItems(totalItems);
    data.setRoleId(userEntity.getRoleId());
    data.setRoleName(userEntity.getRoleName());
    return data;
  }
}
