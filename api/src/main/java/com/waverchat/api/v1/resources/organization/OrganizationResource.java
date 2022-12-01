package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.organizationmember.OrganizationMemberService;
import com.waverchat.api.v1.resources.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationResource extends AbstractApplicationResource<Organization, OrganizationService, OrganizationResponseFactory> {

    @Autowired
    private OrganizationMemberService organizationMemberService;

    @Autowired
    private UserService userService;

    @Override
    public boolean hasCreatePermissions(Organization entityToCreate, RQRSLifecycleProperties props) {
        return true;
    }

}
