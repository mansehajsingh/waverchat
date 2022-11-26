package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.RequestProperties;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.resources.organization.dto.*;

import java.util.Optional;

public class OrganizationResponseFactory extends ResponseDTOFactory<
        Organization, OrganizationCreationResponse, OrganizationViewResponse, OrganizationViewAllResponseComponent,
        OrganizationEditResponse, OrganizationDeleteResponse
> {

    public OrganizationResponseFactory() {
        super();
    }

    @Override
    public OrganizationCreationResponse createCreationResponse(Optional<Organization> createdEntityOpt, RequestProperties props)
            throws NotImplementedException
    {
        Organization createdEntity = createdEntityOpt.get();

        OrganizationCreationResponse creationResponse = new OrganizationCreationResponse();

        creationResponse.setId(createdEntity.getId());
        creationResponse.setName(createdEntity.getName());
        creationResponse.setDescription(createdEntity.getDescription());

        return creationResponse;
    }
}
