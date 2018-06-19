package uk.gov.dft.bluebadge.service.usermanagement.repository;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;

/** Provides CRUD operations on UserEntity entity + user management. */
@SuppressWarnings("WeakerAccess")
@Component
public class UserManagementRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementRepository.class);
  private final SqlSession sqlSession;

  public UserManagementRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  /**
   * Retrieve a single UserEntity by id.
   *
   * @param id PK of UserEntity to select.
   * @return The retrieved UserEntity.
   */
  public Optional<UserEntity> retrieveUserById(int id) {
    UserEntity userEntity = this.sqlSession.selectOne("retrieveUserById", id);
    if (null == userEntity) {
      LOGGER.warn("Attempt to retrieve UserEntity id:{} that does not exist.", id);
    }
    return Optional.ofNullable(userEntity);
  }

  /**
   * Retrieve a single UserEntity by Email Address.
   *
   * @param emailAddress Address to filter on.
   * @return The retrieved UserEntity.
   */
  public Optional<UserEntity> retrieveUserByEmail(String emailAddress) {
    UserEntity userEntity = this.sqlSession.selectOne("retrieveUserByEmailAddress", emailAddress);

    return Optional.ofNullable(userEntity);
  }

  /**
   * Retrieve all UserEntity's for a local authority.
   *
   * @return List of all UserEntity's.
   */
  public List<UserEntity> retrieveUsersByAuthorityId(UserEntity userEntity) {
    List<UserEntity> result = sqlSession.selectList("retrieveUsersByAuthorityId", userEntity);
    if (result == null) {
      result = Lists.newArrayList();
    }
    return result;
  }

  /**
   * Update a UserEntity.
   *
   * @param user UserEntity bean with updated values.
   * @return Update count.
   */
  public int updateUser(UserEntity user) {
    Assert.notNull(user, "updateUser called with null entity to update");
    int result = sqlSession.update("updateUser", user);
    if (0 == result) {
      LOGGER.warn("Attempt to update UserEntity id: {} that does not exist.", user.getId());
    } else {
      LOGGER.info("Updated UserEntity id: {}.", user.getId());
    }
    return result;
  }

  public int updateEmailLinkToInvalid(String uuid) {
    return sqlSession.update("updateEmailLinkToInvalid", uuid);
  }
  /**
   * Update Password a UserEntity.
   *
   * @param user UserEntity bean with updated values.
   * @return Update count.
   */
  public int updatePassword(UserEntity user) {
    Assert.notNull(user, "updatePassword called with null entity to update");
    int result = sqlSession.update("updatePassword", user);
    if (0 == result) {
      LOGGER.warn("Attempt to update UserEntity id: {} that does not exist.", user.getId());
    } else {
      LOGGER.info("Updated UserEntity id: {}.", user.getId());
    }
    return result;
  }

  public EmailLink retrieveEmailLinkWithUuid(String uuid) {

    Assert.notNull(uuid, "No uuid is present");

    return sqlSession.selectOne("retrieveEmailLinkWithUuid", uuid);
  }

  public Optional<UserEntity> retrieveUserUsingUuid(String uuid) {
    Assert.notNull(uuid, "No uuid is present");

    UserEntity userEntity = sqlSession.selectOne("retrieveUserUsingUuid", uuid);
    if (null == userEntity) {
      LOGGER.warn("Attempt to retrieve UserEntity id:{} that does not exist.", uuid);
    }

    return Optional.ofNullable(userEntity);
  }

  /**
   * Create a UserEntity.
   *
   * @param user UserEntity to create.
   * @return Insert count.
   */
  public int createUser(UserEntity user) {
    Assert.notNull(user, "createUser called with null entity to update");
    LOGGER.info("Created UserEntity id: {}.", user.getId());
    return sqlSession.insert("createUser", user);
  }

  /**
   * Delete a UserEntity.
   *
   * @param id PK of UserEntity to delete.
   * @return Delete count
   */
  public int deleteUser(int id) {
    int result = sqlSession.delete("deleteUser", id);
    if (0 == result) {
      LOGGER.warn("Attempt to delete UserEntity id: {} that did not exist.", id);
    } else {
      LOGGER.info("Deleted UserEntity id: {}.", id);
    }
    return result;
  }

  /**
   * Check if email address used by another user.
   *
   * @param userEntity Entity with email to check.
   * @return true if exists.
   */
  public boolean emailAddressAlreadyUsed(UserEntity userEntity) {
    return sqlSession.selectOne("emailAddressAlreadyUsed", userEntity);
  }

  public int createEmailLink(EmailLink emailLink) {
    return sqlSession.insert("createEmailLink", emailLink);
  }

  public int updateUserToInactive(Integer id){
    return sqlSession.update("updateUserToInactive", id);
  }
}
