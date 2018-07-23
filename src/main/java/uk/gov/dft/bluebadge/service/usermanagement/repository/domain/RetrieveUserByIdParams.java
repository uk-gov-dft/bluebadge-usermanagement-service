package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetrieveUserByIdParams {
  int userId;
  int localAuthority;
}
