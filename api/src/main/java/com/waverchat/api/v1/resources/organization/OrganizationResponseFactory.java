package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.resources.organization.dto.*;

public class OrganizationResponseFactory extends ResponseDTOFactory<
        Organization, OrganizationCreationResponse, OrganizationViewResponse, OrganizationViewAllResponseComponent,
        OrganizationEditResponse, OrganizationDeleteResponse
> {

    public OrganizationResponseFactory() {
        super();
    }

}
