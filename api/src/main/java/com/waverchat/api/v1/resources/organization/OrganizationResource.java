package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMember;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberService;
import com.waverchat.api.v1.resources.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationResource extends AbstractApplicationResource<Organization, OrganizationService, OrganizationResponseFactory> {

    @Autowired
    private OrganizationMemberService organizationMemberService;

    @Override
    public boolean hasCreatePermissions(Organization entityToCreate, RQRSLifecycleProperties props) {
        return true;
    }

    @Override
    public boolean hasViewPermissions(UUID id, RQRSLifecycleProperties props) {
        return this.organizationMemberService.isMemberOfOrganization(props.getRequestingUserId(), id);
    }

    @Override
    public void afterGet(UUID id, Optional<Organization> queriedEntity, RQRSLifecycleProperties props) {
        OrganizationMember owner = this.organizationMemberService.getOrganizationOwnerByOrganizationId(id);
        props.attach("owner", owner.getMember());
    }
}
