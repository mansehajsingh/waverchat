package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.customframework.dto.*;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;

import java.util.*;

public class ResponseDTOFactory<
        E extends AbstractApplicationEntity,
        C extends CreationResponse,
        V extends ViewResponse,
        VAC extends ViewAllResponseComponent,
        ED extends EditResponse,
        D extends DeleteResponse
> {

    public ResponseDTOFactory() {}

    public C createCreationResponse(Optional<E> createdEntity, Map<String, Object> requestBody, Optional<UUID> requestingUser)
            throws NotImplementedException, ValidationException
    {
        throw new NotImplementedException("Did not implement createCreationResponseBody method.");
    }

    public V createViewResponse(UUID id, E queriedEntity, Map<String, String> pathVariables, Optional<UUID> requestingUser)
            throws NotImplementedException, NoSuchElementException, ResourceNotFoundException
    {
        throw new NotImplementedException("Did not implement createViewResponse method.");
    }

    public List<VAC> createViewAllResponse(Map<String, String> pathVariables, Map<String, String> queryParams, List<E> queriedEntities, Optional<UUID> requestingUser)
            throws NotImplementedException
    {
        throw new NotImplementedException("Did not implement createViewAllResponse method.");
    }

}
