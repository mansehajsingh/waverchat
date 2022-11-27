package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.resources.organizationmember.dto.*;
import com.waverchat.api.v1.resources.user.dto.UserViewResponse;

import java.util.ArrayList;
import java.util.List;

public class OrganizationMemberResponseFactory extends ResponseDTOFactory<
    OrganizationMember, OrganizationMemberCreationResponse, OrganizationMemberViewResponse,
    OrganizationMemberViewAllResponseComponent, OrganizationMemberEditResponse, OrganizationMemberDeleteResponse
> {

    @Override
    public List<OrganizationMemberViewAllResponseComponent> createViewAllResponse(
            List<OrganizationMember> queriedEntities, RQRSLifecycleProperties props
    ) throws NotImplementedException {

        List<OrganizationMemberViewAllResponseComponent> response = new ArrayList<>();

        for (OrganizationMember orgMember : queriedEntities) {
            OrganizationMemberViewAllResponseComponent component
                    = new OrganizationMemberViewAllResponseComponent();

            component.setId(orgMember.getId());
            component.setCreatedAt(orgMember.getCreatedAt());
            component.setUpdatedAt(orgMember.getUpdatedAt());
            component.setOrganizationId(orgMember.getOrganization().getId());
            component.setType(orgMember.getType());

            UserViewResponse userComponent = new UserViewResponse(
                    orgMember.getMember().getId(),
                    orgMember.getMember().getUsername(),
                    orgMember.getMember().getEmail(),
                    orgMember.getMember().getFirstName(),
                    orgMember.getMember().getLastName(),
                    orgMember.getCreatedAt(),
                    orgMember.getMember().getUpdatedAt()
            );

            component.setUser(userComponent);
            response.add(component);
        }

        return response;
    }
}
