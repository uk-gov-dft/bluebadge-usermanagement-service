package uk.gov.dft.bluebadge.service.client.messageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetRequest {
  @JsonProperty("userId")
  private Integer userId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("localAuthorityId")
  private Integer localAuthorityId = null;

  @JsonProperty("isNewUser")
  private Boolean isNewUser = null;
}
