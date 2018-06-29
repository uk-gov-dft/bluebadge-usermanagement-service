package uk.gov.dft.bluebadge.service.client.messageservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetRequest {
  @JsonProperty("userId")
  private Integer userId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("emailAddress")
  private String emailAddress;

  @JsonProperty("localAuthorityId")
  private Integer localAuthorityId;

  @JsonProperty("isNewUser")
  private Boolean isNewUser;
}
