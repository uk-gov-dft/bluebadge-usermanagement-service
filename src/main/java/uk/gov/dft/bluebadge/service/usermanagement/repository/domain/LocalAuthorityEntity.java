package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("LocalAuthorityEntity")
@Data
@Builder
public class LocalAuthorityEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private String code;
  private String name;
}
