package uk.gov.dft.bluebadge.service.usermanagement.repository.domain;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Bean to hold a UserEntity record.
 */
@Alias("UserEntity")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer localAuthorityId;
    private String name;
    private String emailAddress;

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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", localAuthorityId=" + localAuthorityId +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
