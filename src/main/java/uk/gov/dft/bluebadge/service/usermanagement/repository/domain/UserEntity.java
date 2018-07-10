package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import java.io.Serializable;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/** Bean to hold a UserEntity record. */
@Alias("UserEntity")
@Data
public class UserEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;
  private Integer localAuthorityId;
  private String name;
  private String emailAddress;
  private Integer roleId;
  private String roleName;
  private String password;
}
