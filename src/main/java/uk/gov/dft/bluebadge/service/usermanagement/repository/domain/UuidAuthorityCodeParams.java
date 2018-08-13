package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("UuidAuthorityCodeParams")
@Builder
@Data
public class UuidAuthorityCodeParams {
  UUID uuid;
  String authorityCode;
}
