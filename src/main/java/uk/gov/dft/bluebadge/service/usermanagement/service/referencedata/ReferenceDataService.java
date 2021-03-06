package uk.gov.dft.bluebadge.service.usermanagement.service.referencedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.dft.bluebadge.service.client.referencedataservice.ReferenceDataApiClient;
import uk.gov.dft.bluebadge.service.client.referencedataservice.model.ReferenceData;

@Component
@Slf4j
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReferenceDataService {

  private final Map<String, ReferenceData> authorities = new HashMap<>();
  private final ReferenceDataApiClient referenceDataApiClient;
  private AtomicBoolean isLoaded = new AtomicBoolean(false);

  @Autowired
  public ReferenceDataService(@Validated ReferenceDataApiClient referenceDataApiClient) {
    this.referenceDataApiClient = referenceDataApiClient;
  }

  /**
   * Load the ref data first time required. Chose not to do PostConstruct so that can start service
   * if ref data service is still starting. Not done on startup. Else would be start order
   * dependency between services.
   */
  private void init() {
    if (!isLoaded.getAndSet(true)) {

      log.info("Loading reference data.");
      List<ReferenceData> referenceDataList = referenceDataApiClient.retrieveReferenceData("USER");

      for (ReferenceData item : referenceDataList) {
        if ("LA".equals(item.getGroupShortCode())) {
          authorities.put(item.getShortCode(), item);
        }
      }
      if (authorities.isEmpty()) {
        log.warn("No LA reference data found.");
        isLoaded.set(false);
      }
    }
  }

  public String getLocalAuthorityName(String shortCode) {
    init();
    ReferenceData authority = authorities.get(shortCode);
    if (null != authority) {
      return authority.getDescription();
    }
    return null;
  }

  public boolean isValidLocalAuthorityCode(String code) {
    init();
    return null != authorities.get(code);
  }
}
