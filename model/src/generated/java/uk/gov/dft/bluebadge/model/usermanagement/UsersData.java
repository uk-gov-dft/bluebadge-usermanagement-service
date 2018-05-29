package uk.gov.dft.bluebadge.model.usermanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** UsersData */
@Validated
public class UsersData extends Data {
  @JsonProperty("users")
  @Valid
  private List<User> users = Lists.newArrayList();

  public UsersData users(List<User> users) {
    this.users = users;
    return this;
  }

  public UsersData addUsersItem(User usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    this.users.add(usersItem);
    return this;
  }

  /**
   * Get users
   *
   * @return users
   */
  @ApiModelProperty(value = "")
  @Valid
  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UsersData usersData = (UsersData) o;
    return Objects.equals(this.users, usersData.users) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(users, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UsersData {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
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
