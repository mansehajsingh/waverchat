package com.waverchat.api.v1.resources.organization;

import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.resources.organization.dto.*;
import com.waverchat.api.v1.resources.user.User;
import com.waverchat.api.v1.resources.user.dto.UserViewResponse;

import java.util.Optional;

public class OrganizationResponseFactory extends ResponseDTOFactory<
        Organization, OrganizationCreationResponse, OrganizationViewResponse, OrganizationViewAllResponseComponent,
        OrganizationEditResponse, OrganizationDeleteResponse
> {

    public OrganizationResponseFactory() {
        super();
    }

    @Override
    public OrganizationCreationResponse createCreationResponse(Optional<Organization> createdEntityOpt, RQRSLifecycleProperties props)
            throws NotImplementedException
    {
        Organization createdEntity = createdEntityOpt.get();

        OrganizationCreationResponse creationResponse = new OrganizationCreationResponse();

        creationResponse.setId(createdEntity.getId());
        creationResponse.setName(createdEntity.getName());
        creationResponse.setDescription(createdEntity.getDescription());
        creationResponse.setCreatedAt(createdEntity.getCreatedAt());
        creationResponse.setUpdatedAt(createdEntity.getUpdatedAt());

        User owner = (User) props.getAttached("owner");

        UserViewResponse ownerResponse = new UserViewResponse(
                owner.getId(), owner.getUsername(), owner.getEmail(), owner.getFirstName(),
                owner.getLastName(), owner.getCreatedAt(), owner.getUpdatedAt()
        );

        creationResponse.setOwner(ownerResponse);

        return creationResponse;
    }
}
