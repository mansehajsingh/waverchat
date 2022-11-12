package com.waverchat.api.v1.applicationresource.user;

import com.waverchat.api.v1.applicationresource.user.dto.*;
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
    public List<UserViewAllResponseComponent> createViewAllResponse(
            Map<String, String> queryParams,
            List<User> queriedEntities,
            Optional<UUID> requestingUser
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
}
