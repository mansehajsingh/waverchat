package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.resources.organization.Organization;
import com.waverchat.api.v1.resources.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "organization_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "organization_id"})
        }
)
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

    public OrganizationMember(RQRSLifecycleProperties props) {
        this.type = (String) props.getRequestBody().get("type");;

        Organization org = new Organization();
        org.setId(props.getPathVariableIds().get("organizationId"));
        this.organization = org;

        User user = new User();
        UUID memberId = UUID.fromString((String) props.getRequestBody().get("userId"));
        user.setId(memberId);
        this.member = user;
    }

    public OrganizationMembershipType getType() {
        return OrganizationMembershipType.parse(this.type);
    }

    public void setType(OrganizationMembershipType type) {
        this.type = type.getValue();
    }

    @Override
    public void validateForCreate() throws ValidationException {
        boolean isValidType = false;

        for (OrganizationMembershipType currType : OrganizationMembershipType.values()) {
            if (this.type.equalsIgnoreCase(currType.getValue())) {
                isValidType = true;
                break;
            }
        }

        if (!isValidType) {
            List<String> validationExceptionMessages = new ArrayList<>();
            validationExceptionMessages.add("Invalid type for organization member.");
            throw new ValidationException(validationExceptionMessages);
        }
    }
}
