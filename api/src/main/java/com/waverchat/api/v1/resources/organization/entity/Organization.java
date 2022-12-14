package com.waverchat.api.v1.resources.organization.entity;

import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.resources.organization.OrganizationConstants;
import com.waverchat.api.v1.resources.organization.OrganizationUtil;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMember;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
@Data
public class Organization extends AbstractEntity {

    @NotNull
    @Length(min= OrganizationConstants.MIN_ORG_NAME_LENGTH, max=OrganizationConstants.MAX_ORG_NAME_LENGTH)
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

    @Override
    public void validateForCreate() throws ValidationException {
        ArrayList<String> validationExceptionMessages = new ArrayList<String>();
        boolean throwExcept = false;

        if (!OrganizationUtil.isValidName(this.name)) {
            validationExceptionMessages.add("Name is invalid.");
            throwExcept = true;
        }
        if (!OrganizationUtil.isValidDescription(this.description)) {
            validationExceptionMessages.add("Description is invalid.");
            throwExcept = true;
        }

        if (throwExcept)
            throw new ValidationException(validationExceptionMessages);
    }
}
