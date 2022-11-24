package com.waverchat.api.v1.resources.usercreationconfirmation;

import com.waverchat.api.v1.customframework.RequestProperties;
import com.waverchat.api.v1.resources.usercreationconfirmation.dto.*;
import com.waverchat.api.v1.customframework.ResponseDTOFactory;
import com.waverchat.api.v1.exceptions.NotImplementedException;

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
            RequestProperties props
    )
            throws NotImplementedException
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
            UUID id, UserCreationConfirmation queriedEntity, RequestProperties props
    )
            throws NotImplementedException, NoSuchElementException
    {
        UserCreationConfirmationViewResponse responseBody =
                new UserCreationConfirmationViewResponse("Created user with provided details successfully.");

        return responseBody;
    }
}