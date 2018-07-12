package uk.gov.dft.bluebadge.service.usermanagement.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.LocalAuthorityEntity;
import uk.gov.dft.bluebadge.service.usermanagement.repository.mapper.LocalAuthorityMapper;

@Component
@Slf4j
public class LocalAuthorityRepository implements LocalAuthorityMapper{
  private final SqlSession sqlSession;

  public LocalAuthorityRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public LocalAuthorityEntity retrieveLocalAuthorityById(int id) {
    LocalAuthorityEntity result = this.sqlSession.selectOne("retrieveLocalAuthorityById", id);
    if (null == result) {
      log.info("Attempt to retrieve LocalAuthorityEntity id:{} that does not exist.", id);
    }
    return result;
  }
}
