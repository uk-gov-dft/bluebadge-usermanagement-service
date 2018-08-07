package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.ibatis.type.Alias;

@Alias("EmailLink")
@Data
@Builder
public class EmailLink {
  @NonNull private final String webappUri;
  @NonNull private final String uuid;
  @NonNull private final UUID userUuid;
  private final Boolean isActive;

  public String getLink() {
    return webappUri + uuid;
  }
}
