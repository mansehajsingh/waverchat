package com.waverchat.api.v1.resources.organizationinvitation.entity;

import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.resources.organization.entity.Organization;
import com.waverchat.api.v1.resources.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "organization_invitations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "organization_id"})
        }
)
@Data
public class OrganizationInvitation extends AbstractEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User candidate;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Transient
    private OrganizationInvitationDecision decision = OrganizationInvitationDecision.UNKNOWN;

    public OrganizationInvitation() {}

}
