package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUserParams {
  // TODO use more generic uuid nd auth params
  UUID userUuid;
  String localAuthority;
}
