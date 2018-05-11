package uk.gov.dft.bluebadge.model.usermanagement;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import uk.gov.dft.bluebadge.model.usermanagement.Data;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * User
 */
@Validated

public class User extends Data  {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("emailAddress")
  private String emailAddress = null;

  @JsonProperty("localAuthorityId")
  private Integer localAuthorityId = null;

  public User id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier representing a specific user.
   * @return id
  **/
  @ApiModelProperty(example = "1", value = "Unique identifier representing a specific user.")


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Users name.
   * @return name
  **/
  @ApiModelProperty(example = "Robert Worthington", value = "Users name.")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User emailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  /**
   * email address.
   * @return emailAddress
  **/
  @ApiModelProperty(example = "rob.worthington@norealserver.com", value = "email address.")


  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public User localAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
    return this;
  }

  /**
   * Id of Local Authority user belongs to
   * @return localAuthorityId
  **/
  @ApiModelProperty(example = "2", value = "Id of Local Authority user belongs to")


  public Integer getLocalAuthorityId() {
    return localAuthorityId;
  }

  public void setLocalAuthorityId(Integer localAuthorityId) {
    this.localAuthorityId = localAuthorityId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.name, user.name) &&
        Objects.equals(this.emailAddress, user.emailAddress) &&
        Objects.equals(this.localAuthorityId, user.localAuthorityId) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, emailAddress, localAuthorityId, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    emailAddress: ").append(toIndentedString(emailAddress)).append("\n");
    sb.append("    localAuthorityId: ").append(toIndentedString(localAuthorityId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

