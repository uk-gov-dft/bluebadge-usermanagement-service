package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUserParams {
  Integer userId;
  Integer localAuthority;
}
