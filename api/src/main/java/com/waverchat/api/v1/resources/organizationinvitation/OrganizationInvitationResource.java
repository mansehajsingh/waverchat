package com.waverchat.api.v1.resources.organizationinvitation;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/invitations")
public class OrganizationInvitationResource extends AbstractApplicationResource<
    OrganizationInvitation, OrganizationInvitationService, OrganizationInvitationResponseFactory
> {

    @Autowired
    private OrganizationMemberService organizationMemberService;

    @Override
    public boolean hasCreatePermissions(OrganizationInvitation entityToCreate, RQRSLifecycleProperties props) {
        return this.organizationMemberService.isOwnerOfOrganization(
                props.getRequestingUserId(), props.getPathVariableIds().get("organizationId"));
    }

}
