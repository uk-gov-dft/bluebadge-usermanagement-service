package uk.gov.dft.bluebadge.service.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class PasswordResetRequest extends GenericMessageRequest {
  public static final String PASSWORD_RESET_TEMPLATE = "PASSWORD_RESET_TEMPLATE";
  private String fullName;
  private String passwordLink;

  @Builder
  private PasswordResetRequest(
      @NonNull String emailAddress, @NonNull String fullName, @NonNull String passwordLink) {
    super(
        PASSWORD_RESET_TEMPLATE,
        emailAddress,
        ImmutableMap.<String, String>builder()
            .put("fullName", fullName)
            .put("passwordLink", passwordLink)
            .build());
    this.fullName = fullName;
    this.passwordLink = passwordLink;
  }
}
