package uk.gov.dft.bluebadge.service.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class NewUserRequest extends GenericMessageRequest {
  public static final String NEW_USER_TEMPLATE = "NEW_USER";
  private String fullName;
  private final String localAuthorityName;
  private String passwordLink;

  @Builder
  private NewUserRequest(
      @NonNull String emailAddress,
      @NonNull String fullName,
      @NonNull String localAuthorityName,
      @NonNull String passwordLink) {
    super(
        NEW_USER_TEMPLATE,
        emailAddress,
        ImmutableMap.<String, String>builder()
            .put("fullName", fullName)
            .put("localAuthorityName", localAuthorityName)
            .put("passwordLink", passwordLink)
            .build());
    this.fullName = fullName;
    this.localAuthorityName = localAuthorityName;
    this.passwordLink = passwordLink;
  }
}
