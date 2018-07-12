package uk.gov.dft.bluebadge.model.usermanagement.generated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

import javax.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

/** Authority */
@Validated
public class Authority {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("shortCode")
  private String shortCode = null;

  public Authority id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   *
   * @return id
   */
  @ApiModelProperty(example = "1", required = true, value = "")
  @NotNull
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Authority name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Display name of local authority
   *
   * @return name
   */
  @ApiModelProperty(
    example = "Shropshire County Council",
    required = true,
    value = "Display name of local authority"
  )
  @NotNull
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Authority shortCode(String shortCode) {
    this.shortCode = shortCode;
    return this;
  }

  /**
   * Short identifier of LA
   *
   * @return shortCode
   */
  @ApiModelProperty(example = "SCC", required = true, value = "Short identifier of LA")
  @NotNull
  public String getShortCode() {
    return shortCode;
  }

  public void setShortCode(String shortCode) {
    this.shortCode = shortCode;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Authority authority = (Authority) o;
    return Objects.equals(this.id, authority.id)
        && Objects.equals(this.name, authority.name)
        && Objects.equals(this.shortCode, authority.shortCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, shortCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Authority {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    shortCode: ").append(toIndentedString(shortCode)).append("\n");
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
