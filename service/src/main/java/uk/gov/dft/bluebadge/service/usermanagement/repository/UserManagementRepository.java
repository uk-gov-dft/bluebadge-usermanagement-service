package uk.gov.dft.bluebadge.service.usermanagement.repository;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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
   * Retrieve all UserEntity's for a local authority.
   *
   * @return List of all UserEntity's.
   */
  public List<UserEntity> retrieveUsersByAuthorityId(UserEntity userEntity) {
    return sqlSession.selectList("retrieveUsersByAuthorityId", userEntity);
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

  /**
   * Create a UserEntity.
   *
   * @param user UserEntity to create.
   * @return Insert count.
   */
  public int createUser(UserEntity user) {
    Assert.notNull(user, "createLocalAuthority called with null entity to update");
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
   * Temporary method for login validation.
   *
   * @param emailAddress Email to confirm.
   * @return True if exists.
   */
  public boolean checkUserExistsForEmail(String emailAddress) {
    return sqlSession.selectOne("checkUserExistsForEmail", emailAddress);
  }
}
