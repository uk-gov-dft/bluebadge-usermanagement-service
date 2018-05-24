package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.io.Serializable;
import org.apache.ibatis.type.Alias;

/** Bean to hold a UserEntity record. */
@Alias("UserEntity")
public class UserEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer localAuthorityId;
  private String name;
  private String emailAddress;
  private Integer roleId;
  private String roleName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getLocalAuthorityId() {
    return localAuthorityId;
  }

  public void setLocalAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public String toString() {
    return "UserEntity{"
        + "id="
        + id
        + ", localAuthorityId="
        + localAuthorityId
        + ", name='"
        + name
        + '\''
        + ", emailAddress='"
        + emailAddress
        + '\''
        + '}';
  }
}
