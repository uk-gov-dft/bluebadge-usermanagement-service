package uk.gov.dft.bluebadge.model.usermanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** UserData */
@Validated
public class UserData extends Data {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("localAuthorityId")
  private Integer localAuthorityId = null;

  @JsonProperty("roleId")
  private Integer roleId = null;

  @JsonProperty("roleName")
  private String roleName = null;

  public UserData id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier representing a specific user. Autogenerated on create.
   *
   * @return id
   */
  @ApiModelProperty(
    example = "1",
    value = "Unique identifier representing a specific user. Autogenerated on create."
  )
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public UserData name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Users name.
   *
   * @return name
   */
  @ApiModelProperty(example = "Robert Worthington", required = true, value = "Users name.")
  @NotNull
  @Pattern(regexp = "^[\\p{L} \\.'\\-]+$")
  @Size(max = 100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserData emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  /**
   * email address.
   *
   * @return emailAddress
   */
  @ApiModelProperty(
    example = "rob.worthington@norealserver.com",
    required = true,
    value = "email address."
  )
  @NotNull
  @Pattern(
    regexp =
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
  )
  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public UserData localAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
    return this;
  }

  /**
   * Id of Local Authority user belongs to
   *
   * @return localAuthorityId
   */
  @ApiModelProperty(example = "2", value = "Id of Local Authority user belongs to")
  public Integer getLocalAuthorityId() {
    return localAuthorityId;
  }

  public void setLocalAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
  }

  public UserData roleId(Integer roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * Id of users role
   *
   * @return roleId
   */
  @ApiModelProperty(example = "2", required = true, value = "Id of users role")
  @NotNull
  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public UserData roleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  /**
   * User role description
   *
   * @return roleName
   */
  @ApiModelProperty(example = "LA Admin", value = "User role description")
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserData userData = (UserData) o;
    return Objects.equals(this.id, userData.id)
        && Objects.equals(this.name, userData.name)
        && Objects.equals(this.emailAddress, userData.emailAddress)
        && Objects.equals(this.localAuthorityId, userData.localAuthorityId)
        && Objects.equals(this.roleId, userData.roleId)
        && Objects.equals(this.roleName, userData.roleName)
        && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, name, emailAddress, localAuthorityId, roleId, roleName, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserData {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    localAuthorityId: ").append(toIndentedString(localAuthorityId)).append("\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("    roleName: ").append(toIndentedString(roleName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
