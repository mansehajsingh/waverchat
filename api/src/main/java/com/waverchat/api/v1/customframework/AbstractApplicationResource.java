package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.customframework.dto.CreationResponse;
import com.waverchat.api.v1.customframework.dto.EditResponse;
import com.waverchat.api.v1.customframework.dto.ViewAllResponseComponent;
import com.waverchat.api.v1.customframework.dto.ViewResponse;
import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.http.response.MessageResponse;
import com.waverchat.api.v1.http.response.MultiMessageResponse;
import com.waverchat.api.v1.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class AbstractApplicationResource<
        E extends AbstractApplicationEntity,
        S extends AbstractApplicationService<E>,
        R extends ResponseDTOFactory
> {

    @Autowired
    protected S service;

    private R createResponseFactoryInstance() throws Exception {
        Class<R> responseFactoryType = (Class<R>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[2];
        Constructor<R> rfCtor = responseFactoryType.getConstructor();
        return rfCtor.newInstance();
    }

    private E createEntityInstance(Map<String, Object> requestBody) throws Exception {
        Class<E> entityType = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        Constructor<E> eCtor = entityType.getConstructor(Map.class);
        return eCtor.newInstance(requestBody);
    }

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
        E entityToCreate;
        try {
            entityToCreate = this.createEntityInstance(requestBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("There was an error processing the request."));
        }


        // validates the entity body
        try {
            entityToCreate.validateForCreate();
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
        Optional<E> createdEntity = this.service.create(entityToCreate);
        this.afterCreate(createdEntity, requestBody, requestingUser);

        // creating instance of the entity's response factory
        R responseFactory;
        try {
            responseFactory = this.createResponseFactoryInstance();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("There was an error processing the request."));
        }

        // forming the response body
        CreationResponse responseBody;
        try {
            responseBody = responseFactory.createCreationResponse(createdEntity, requestBody, requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id,
            HttpServletRequest request
    ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
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

        // initializing the entity's response factory
        R responseFactory;
        try {
            responseFactory = this.createResponseFactoryInstance();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("There was an error processing the request."));
        }

        // forming the response body
        ViewResponse responseBody;
        try {
            responseBody = responseFactory.createViewResponse(uuid, queriedEntityOpt.get(), requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No resource with id " + uuid + " could be found."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam Map<String, String> queryParams,
            HttpServletRequest request
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Optional<UUID> requestingUser = RequestUtil.getRequestingUser(request);

        if (!this.hasViewAllPermissions(queryParams, requestingUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to get resource."));
        }

        this.beforeGetAll(queryParams, requestingUser);

        Page<E> queriedEntitiesPage = this.service.getAll(queryParams);
        List<E> queriedEntities = queriedEntitiesPage.toList();

        this.afterGetAll(queryParams, queriedEntities, requestingUser);

        // initializing the entity's response factory
        R responseFactory;
        try {
            responseFactory = this.createResponseFactoryInstance();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("There was an error processing the request."));
        }

        List<ViewAllResponseComponent> responseBody;
        try {
            responseBody = responseFactory.createViewAllResponse(queryParams, queriedEntities, requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        }

        Map<String, Object> responseObj = new HashMap<>();
        responseObj.put("entities", responseBody);
        responseObj.put("pageCount", queriedEntitiesPage.getTotalPages());
        responseObj.put("entityCount", queriedEntitiesPage.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(
            @PathVariable String id,
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Optional<UUID> requestingUser = RequestUtil.getRequestingUser(request);

        // Parse uuid from string
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MessageResponse("Invalid UUID format provided in request parameter."));
        }

        Optional<E> existingEntityOpt = this.service.getById(uuid);

        if (existingEntityOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No resource with id " + uuid + " could be found."));
        }

        E candidateEntity = existingEntityOpt.get();

        candidateEntity.edit(requestBody);

        // validates the entity body
        try {
            candidateEntity.validateForEdit();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MultiMessageResponse(e.getMessages()));
        }

        // check if requesting user has creation permissions
        if (!this.hasEditPermissions(uuid, candidateEntity, requestingUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to edit resource."));
        }



        // checking if creation of the entity would cause any problems
        try {
            this.service.auditForEdit(candidateEntity);
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(e.getMessage()));
        }


        // creating the entity
        this.beforeEdit(uuid, candidateEntity, requestingUser);
        Optional<E> editedEntity = this.service.edit(uuid, candidateEntity);
        this.afterEdit(uuid, editedEntity.get(), requestingUser);

        // creating instance of the entity's response factory
        R responseFactory;
        try {
            responseFactory = this.createResponseFactoryInstance();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("There was an error processing the request."));
        }

        // forming the response body
        EditResponse responseBody;
        try {
            responseBody = responseFactory.createEditResponse(uuid, editedEntity, requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public boolean hasCreatePermissions(E entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeCreate(E entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {}

    public void afterCreate(Optional<E> createdEntity, Map<String, Object> requestBody, Optional<UUID> requestingUser) {}

    public boolean hasViewPermissions(UUID id, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeGet(UUID id, Optional<UUID> requestingUser) {}

    public void afterGet(UUID id, Optional<E> queriedEntity, Optional<UUID> requestingUser) {}

    public boolean hasViewAllPermissions(Map<String, String> queryParams, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeGetAll(Map<String, String> queryParams, Optional<UUID> requestingUser) {}

    public void afterGetAll(Map<String, String> queryParams, List<E> queriedEntities, Optional<UUID> requestingUser) {}

    public boolean hasEditPermissions(UUID id, E candidateEntity, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeEdit(UUID id, E candidateEntity, Optional<UUID> requestingUser) {}

    public void afterEdit(UUID id, E editedEntity, Optional<UUID> requestingUser) {}

    public boolean hasDeletePermissions(UUID id, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

}
