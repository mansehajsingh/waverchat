package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.resources.organization.Organization;
import com.waverchat.api.v1.resources.user.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "organization_members")
@Data
public class OrganizationMember extends AbstractApplicationEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User member;
    
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    private String type;

    public OrganizationMember() {}

    public OrganizationMember(User member, Organization organization, OrganizationMembershipType type) {
        this.member = member;
        this.organization = organization;
        this.type = type.getValue();
    }

    public OrganizationMembershipType getType() {
        return OrganizationMembershipType.parse(this.type);
    }

    public void setType(OrganizationMembershipType type) {
        this.type = type.getValue();
    }

}
