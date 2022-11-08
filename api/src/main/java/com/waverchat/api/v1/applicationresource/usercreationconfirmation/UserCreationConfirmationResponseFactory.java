package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import com.waverchat.api.v1.applicationresource.usercreationconfirmation.dto.*;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class UserCreationConfirmationResponseFactory extends ResponseDTOFactory<
        UserCreationConfirmation,
        UserCreationConfirmationCreationResponse,
        UserCreationConfirmationViewResponse,
        UserCreationConfirmationViewAllResponseComponent,
        UserCreationConfirmationEditResponse,
        UserCreationConfirmationDeleteResponse
>
{

    public UserCreationConfirmationResponseFactory() {}

    @Override
    public UserCreationConfirmationCreationResponse createCreationResponse(
            Optional<UserCreationConfirmation> createdUCCOpt,
            Map<String, Object> requestBody,
            Optional<UUID> requestingUser
    )
            throws NotImplementedException, ValidationException
    {
        UserCreationConfirmation createdUCC = createdUCCOpt.get();

        UserCreationConfirmationCreationResponse responseBody = new UserCreationConfirmationCreationResponse(
                createdUCC.getEmail(),
                createdUCC.getUsername(),
                createdUCC.getFirstName(),
                createdUCC.getLastName()
        );

        return responseBody;
    }

    @Override
    public UserCreationConfirmationViewResponse createViewResponse(
            UUID id, UserCreationConfirmation queriedEntity,
            Map<String, String> pathVariables, Optional<UUID>
                    requestingUser
    )
            throws NotImplementedException, NoSuchElementException, ResourceNotFoundException
    {
        UserCreationConfirmationViewResponse responseBody =
                new UserCreationConfirmationViewResponse("Created user with provided details successfully.");

        return responseBody;
    }
}