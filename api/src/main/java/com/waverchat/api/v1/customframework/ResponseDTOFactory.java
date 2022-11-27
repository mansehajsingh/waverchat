package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.customframework.dto.*;
import com.waverchat.api.v1.exceptions.NotImplementedException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ResponseDTOFactory<
        E extends AbstractApplicationEntity,
        C extends CreationResponse,
        V extends ViewResponse,
        VAC extends ViewAllResponseComponent,
        ED extends EditResponse,
        D extends DeleteResponse
> {

    public ResponseDTOFactory() {}

    public C createCreationResponse(Optional<E> createdEntity, RQRSLifecycleProperties props)
            throws NotImplementedException
    {
        throw new NotImplementedException("Did not implement createCreationResponseBody method.");
    }

    public V createViewResponse(UUID id, E queriedEntity, RQRSLifecycleProperties props)
            throws NotImplementedException
    {
        throw new NotImplementedException("Did not implement createViewResponse method.");
    }

    public List<VAC> createViewAllResponse(List<E> queriedEntities, RQRSLifecycleProperties props)
            throws NotImplementedException
    {
        throw new NotImplementedException("Did not implement createViewAllResponse method.");
    }

    public ED createEditResponse(UUID id, Optional<E> editedEntity, RQRSLifecycleProperties props)
            throws NotImplementedException
    {
        throw new NotImplementedException("Did not implement createEditResponse method.");
    }

}
