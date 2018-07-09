package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class EmailLink {
  @NonNull private final String webappUri;
  @NonNull private final String uuid;
  @NonNull private final Integer userId;
  private final Boolean isActive;

  public String getLink() {
    return webappUri + uuid;
  }
}
