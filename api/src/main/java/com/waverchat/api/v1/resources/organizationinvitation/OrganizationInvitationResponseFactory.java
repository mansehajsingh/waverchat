package com.waverchat.api.v1.resources.organizationinvitation;

import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.resources.organizationinvitation.dto.OrganizationInvitationCreationResponse;
import com.waverchat.api.v1.resources.organizationinvitation.dto.OrganizationInvitationEditResponse;
import com.waverchat.api.v1.resources.organizationinvitation.dto.OrganizationInvitationViewResponse;
import com.waverchat.api.v1.resources.organizationmember.dto.OrganizationMemberDeleteResponse;
import com.waverchat.api.v1.resources.organizationmember.dto.OrganizationMemberViewAllResponseComponent;

import java.util.Optional;

public class OrganizationInvitationResponseFactory extends ResponseDTOFactory<
        OrganizationInvitation, OrganizationInvitationCreationResponse,
        OrganizationInvitationViewResponse, OrganizationMemberViewAllResponseComponent,
        OrganizationInvitationEditResponse, OrganizationMemberDeleteResponse
> {

    @Override
    public OrganizationInvitationCreationResponse createCreationResponse(
            Optional<OrganizationInvitation> createdEntityOpt, RQRSLifecycleProperties props)
            throws NotImplementedException
    {
        OrganizationInvitation invitation = createdEntityOpt.get();

        OrganizationInvitationCreationResponse creationResponse = new OrganizationInvitationCreationResponse();

        creationResponse.setId(invitation.getId());
        creationResponse.setUserId(invitation.getCandidate().getId());
        creationResponse.setOrganizationId(props.getPathVariableIds().get("organizationId"));
        creationResponse.setCreatedAt(invitation.getCreatedAt());
        creationResponse.setUpdatedAt(invitation.getUpdatedAt());

        return creationResponse;
    }
}
