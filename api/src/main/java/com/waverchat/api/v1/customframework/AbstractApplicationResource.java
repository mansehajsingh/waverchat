package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.http.response.MessageResponse;
import com.waverchat.api.v1.http.response.MultiMessageResponse;
import com.waverchat.api.v1.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractApplicationResource<
        E extends AbstractApplicationEntity,
        S extends AbstractApplicationService<E>
> {

    @Autowired
    protected S service;

    @PostMapping(consumes={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request,
            HttpServletResponse response
    )
            throws NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
        Optional<UUID> requestingUser = RequestUtil.getRequestingUser(request);


        // initializing the entity
        Class<E> entityType;
        try {
            entityType = (Class<E>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("There was an error processing the request."));
        }
        Constructor<E> ctor = entityType.getConstructor(Map.class);
        E entityToCreate = ctor.newInstance(requestBody);


        // validates the entity body
        try {
            entityToCreate.validate();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MultiMessageResponse(e.getMessages()));
        }

        // check if requesting user has creation permissions
        if (!this.hasCreatePermissions(entityToCreate, requestBody, requestingUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to create resource."));
        }


        // checking if creation of the entity would cause any problems
        try {
            this.service.auditForCreate(entityToCreate);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        }


        // creating the entity
        this.beforeCreate(entityToCreate, requestBody, requestingUser);
        Optional<E> createdEntity = service.create(entityToCreate);
        this.afterCreate(createdEntity, requestBody, requestingUser);


        // forming the response body
        Map<String, Object> responseBody;
        try {
            responseBody = this.formCreationRequestBody(createdEntity, requestBody, requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        }

        if (createdEntity.isPresent()) {
            responseBody.put("createdAt", createdEntity.get().getCreatedAt());
            responseBody.put("updatedAt", createdEntity.get().getUpdatedAt());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id,
            HttpServletRequest request
    )
    {
        Optional<UUID> requestingUser = RequestUtil.getRequestingUser(request);

        // Parse uuid from string
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Invalid UUID format provided in request parameter."));
        }

        // Verifying that the requesting user has permissions to get this object
        if (!this.hasViewPermissions(uuid, requestingUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to get resource."));
        }

        // fetching the entity
        this.beforeGet(uuid, requestingUser);
        Optional<E> queriedEntityOpt = this.service.getById(uuid);
        this.afterGet(uuid, queriedEntityOpt, requestingUser);

        // forming the response body
        Map<String, Object> responseBody;
        try {
            responseBody = this.formViewRequestBody(uuid, queriedEntityOpt.get(), requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No resource with id " + uuid + " could be found."));
        }

        responseBody.put("createdAt", queriedEntityOpt.get().getCreatedAt());
        responseBody.put("updatedAt", queriedEntityOpt.get().getUpdatedAt());

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public boolean hasCreatePermissions(E entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeCreate(E entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {}

    public void afterCreate(Optional<E> createdEntity, Map<String, Object> requestBody, Optional<UUID> requestingUser) {}

    public Map<String, Object> formCreationRequestBody(Optional<E> createdEntity, Map<String, Object> requestBody, Optional<UUID> requestingUser) throws NotImplementedException {
        throw new NotImplementedException("Did not implement formCreationRequestBody method.");
    }

    public boolean hasViewPermissions(UUID id, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeGet(UUID id, Optional<UUID> requestingUser) {}

    public void afterGet(UUID id, Optional<E> queriedEntity, Optional<UUID> requestingUser) {}

    public Map<String, Object> formViewRequestBody(UUID id, E queriedEntity, Optional<UUID> requestingUser) throws NotImplementedException {
        throw new NotImplementedException("Did not implement formViewRequestBody method.");
    }

    public boolean hasEditPermissions(UUID id, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

    public boolean hasDeletePermissions(UUID id, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

}
