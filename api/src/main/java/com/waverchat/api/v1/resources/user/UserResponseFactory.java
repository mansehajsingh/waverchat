package com.waverchat.api.v1.resources.user;

import com.waverchat.api.v1.customframework.RequestProperties;
import com.waverchat.api.v1.resources.user.dto.*;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;

import java.util.*;

public class UserResponseFactory extends ResponseDTOFactory<
    User, UserCreationResponse, UserViewResponse, UserViewAllResponseComponent, UserEditResponse, UserDeleteResponse
> {

    public UserResponseFactory() {
        super();
    }

    @Override
    public UserViewResponse createViewResponse(UUID id, User queriedEntity, RequestProperties props)
            throws NotImplementedException
    {
        return new UserViewResponse(
                queriedEntity.getId(),
                queriedEntity.getUsername(),
                queriedEntity.getEmail(),
                queriedEntity.getFirstName(),
                queriedEntity.getLastName(),
                queriedEntity.getCreatedAt(),
                queriedEntity.getUpdatedAt()
        );
    }

    @Override
    public List<UserViewAllResponseComponent> createViewAllResponse(
            List<User> queriedEntities,
            RequestProperties props
    ) {
        List<UserViewAllResponseComponent> responseComponents = new ArrayList<>();

        for (User qe : queriedEntities) {
            UserViewAllResponseComponent component = new UserViewAllResponseComponent(
                    qe.getId(),
                    qe.getEmail(),
                    qe.getUsername(),
                    qe.getFirstName(),
                    qe.getLastName(),
                    qe.getCreatedAt(),
                    qe.getUpdatedAt()
            );
            responseComponents.add(component);
        }

        return responseComponents;
    }

    @Override
    public UserEditResponse createEditResponse(
            UUID id,
            Optional<User> editedEntityOpt,
            RequestProperties props
    ) throws NotImplementedException
    {

        User editedEntity = editedEntityOpt.get();

        UserEditResponse editResponse = new UserEditResponse(
                editedEntity.getId(),
                editedEntity.getUsername(),
                editedEntity.getEmail(),
                editedEntity.getFirstName(),
                editedEntity.getLastName(),
                editedEntity.getCreatedAt(),
                editedEntity.getUpdatedAt()
        );

        return editResponse;
    }
}
