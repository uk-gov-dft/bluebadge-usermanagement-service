package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.io.Serializable;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("LocalAuthorityEntity")
@Data
public class LocalAuthorityEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private String areaName;
}
