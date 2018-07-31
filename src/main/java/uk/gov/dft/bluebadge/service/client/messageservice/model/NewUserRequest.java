package uk.gov.dft.bluebadge.service.client.messageservice.model;

import com.google.common.collect.ImmutableMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public class NewUserRequest extends GenericMessageRequest {
  /** This is the name of the template within the Message service */
  public static final String NEW_USER_TEMPLATE = "NEW_USER";

  private final String fullName;
  private final String localAuthority;
  private final String passwordLink;

  @Builder
  private NewUserRequest(
      @NonNull String emailAddress,
      @NonNull String fullName,
      @NonNull String localAuthority,
      @NonNull String passwordLink) {
    super(
        NEW_USER_TEMPLATE,
        emailAddress,
        ImmutableMap.<String, String>builder()
            .put("fullName", fullName)
            .put("localAuthority", localAuthority)
            .put("passwordLink", passwordLink)
            .build());
    this.fullName = fullName;
    this.localAuthority = localAuthority;
    this.passwordLink = passwordLink;
  }
}
