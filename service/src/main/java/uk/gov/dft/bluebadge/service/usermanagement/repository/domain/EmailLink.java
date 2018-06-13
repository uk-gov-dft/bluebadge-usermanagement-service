package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

public class EmailLink {

  private String uuid;
  private int userId;
  private Boolean isActive;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }
}
