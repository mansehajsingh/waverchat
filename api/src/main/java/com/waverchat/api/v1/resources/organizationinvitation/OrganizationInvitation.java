package com.waverchat.api.v1.resources.organizationinvitation;

import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.organization.Organization;
import com.waverchat.api.v1.resources.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(
        name = "organization_invitations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "organization_id"})
        }
)
@Data
public class OrganizationInvitation extends AbstractApplicationEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User candidate;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public OrganizationInvitation() {}

    public OrganizationInvitation(RQRSLifecycleProperties props) {
        Organization org = new Organization();
        org.setId(props.getPathVariableIds().get("organizationId"));
        this.organization = org;

        User cand = new User();
        cand.setId(UUID.fromString((String) props.getRequestBody().get("userId")));
        this.candidate = cand;
    }

}
