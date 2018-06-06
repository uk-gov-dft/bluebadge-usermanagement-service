package uk.gov.dft.bluebadge.model.usermanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** AuthoritiesResponse */
@Validated
public class AuthoritiesResponse extends CommonResponse {
  @JsonProperty("data")
  @Valid
  private List<Authority> data = null;

  public AuthoritiesResponse data(List<Authority> data) {
    this.data = data;
    return this;
  }

  public AuthoritiesResponse addDataItem(Authority dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   *
   * @return data
   */
  @ApiModelProperty(value = "")
  @Valid
  public List<Authority> getData() {
    return data;
  }

  public void setData(List<Authority> data) {
    this.data = data;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthoritiesResponse authoritiesResponse = (AuthoritiesResponse) o;
    return Objects.equals(this.data, authoritiesResponse.data) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthoritiesResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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
