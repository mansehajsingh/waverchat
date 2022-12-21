package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMember;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Data
public class Organization extends AbstractEntity {

    @NotNull
    @Length(min=OrganizationConstants.MIN_ORG_NAME_LENGTH, max=OrganizationConstants.MAX_ORG_NAME_LENGTH)
    private String name;

    @NotNull
    @Length(min=OrganizationConstants.MIN_ORG_DESC_LENGTH, max=OrganizationConstants.MAX_ORG_DESC_LENGTH)
    private String description;

    @OneToMany(mappedBy = "organization")
    private Set<OrganizationMember> members = new HashSet<>();

    public Organization() {}

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
