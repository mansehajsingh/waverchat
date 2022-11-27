package com.waverchat.api.v1.resources.organizationmember;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/members")
public class OrganizationMemberResource extends AbstractApplicationResource<
        OrganizationMember, OrganizationMemberService, OrganizationMemberResponseFactory
>
{

    @Override
    public boolean hasViewAllPermissions(RQRSLifecycleProperties props) {
        return this.service.isMemberOfOrganization(
                props.getRequestingUserId(), props.getPathVariableIds().get("organizationId"));
    }

}
