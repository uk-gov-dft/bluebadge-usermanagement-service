package uk.gov.dft.bluebadge.service.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class PasswordResetSuccessRequest extends GenericMessageRequest {
  /** This is the name of the template within the Message service */
  @SuppressWarnings("squid:S2068")
  public static final String PASSWORD_RESET_SUCCESS_TEMPLATE = "PASSWORD_RESET_SUCCESS";

  private String fullName;

  @Builder
  private PasswordResetSuccessRequest(@NonNull String emailAddress, @NonNull String fullName) {
    super(
        PASSWORD_RESET_SUCCESS_TEMPLATE,
        emailAddress,
        ImmutableMap.<String, String>builder().put("fullName", fullName).build());
    this.fullName = fullName;
  }
}
