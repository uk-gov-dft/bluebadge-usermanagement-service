package uk.gov.dft.bluebadge.service.usermanagement.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.LocalAuthorityEntity;

@SuppressWarnings("unused")
@Mapper
public interface LocalAuthorityMapper {
  LocalAuthorityEntity retrieveLocalAuthorityById(int id);
}
