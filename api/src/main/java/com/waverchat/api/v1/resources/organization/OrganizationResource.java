package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.customframework.RequestProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationResource extends AbstractApplicationResource<Organization, OrganizationService, OrganizationResponseFactory> {

    @Override
    public boolean hasCreatePermissions(Organization entityToCreate, RequestProperties props) {
        return true;
    }

}
