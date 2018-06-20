package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailLink {

  private String uuid;
  private int userId;
  private Boolean isActive;
}
