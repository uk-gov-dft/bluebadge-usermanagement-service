package uk.gov.dft.bluebadge.service.client.messageservice.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class GenericMessageRequest {
  @NonNull private final String template;
  @NonNull private final String emailAddress;
  private final Map<String, ?> attributes;
}
