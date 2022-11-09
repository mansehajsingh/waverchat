package com.waverchat.api.v1.customframework;

import com.waverchat.api.v1.customframework.dto.CreationResponse;
import com.waverchat.api.v1.customframework.dto.ViewAllResponseComponent;
import com.waverchat.api.v1.customframework.dto.ViewResponse;
import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
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
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new MultiMessageResponse(e.getMessages()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id,
            @PathVariable Map<String, String> pathVariables,
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
        if (!this.hasViewPermissions(uuid, pathVariables, requestingUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to get resource."));
        }

        // fetching the entity
        this.beforeGet(uuid, pathVariables, requestingUser);
        Optional<E> queriedEntityOpt = this.service.getById(uuid);
        this.afterGet(uuid, pathVariables, queriedEntityOpt, requestingUser);

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
            responseBody = responseFactory.createViewResponse(uuid, queriedEntityOpt.get(), pathVariables, requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No resource with id " + uuid + " could be found."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PathVariable Map<String, String> pathVariables,
            @RequestParam Map<String, String> queryParams,
            HttpServletRequest request
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Optional<UUID> requestingUser = RequestUtil.getRequestingUser(request);

        if (!this.hasViewAllPermissions(pathVariables, queryParams, requestingUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to get resource."));
        }

        this.beforeGetAll(pathVariables, queryParams, requestingUser);

        Page<E> queriedEntitiesPage = this.service.getAll(pathVariables, queryParams);
        List<E> queriedEntities = queriedEntitiesPage.toList();

        this.afterGetAll(pathVariables, queryParams, queriedEntities, requestingUser);

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
            responseBody = responseFactory.createViewAllResponse(pathVariables, queryParams, queriedEntities, requestingUser);
        } catch (NotImplementedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error processing the request."));
        }

        Map<String, Object> responseObj = new HashMap<>();
        responseObj.put("entities", responseBody);
        responseObj.put("pageCount", queriedEntitiesPage.getTotalPages());
        responseObj.put("entityCount", queriedEntitiesPage.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(responseObj);
    }

    public boolean hasCreatePermissions(E entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeCreate(E entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {}

    public void afterCreate(Optional<E> createdEntity, Map<String, Object> requestBody, Optional<UUID> requestingUser) {}

    public boolean hasViewPermissions(UUID id, Map<String, String> pathVariables, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeGet(UUID id, Map<String, String> pathVariables, Optional<UUID> requestingUser) {}

    public void afterGet(UUID id, Map<String, String> pathVariables, Optional<E> queriedEntity, Optional<UUID> requestingUser) {}

    public boolean hasViewAllPermissions(Map<String, String> pathVariables, Map<String, String> queryParams, Optional<UUID> requestingUser) {
        return false;
    }

    public void beforeGetAll(Map<String, String> pathVariables, Map<String, String> queryParams, Optional<UUID> requestingUser) {}

    public void afterGetAll(Map<String, String> pathVariables, Map<String, String> queryParams, List<E> queriedEntities, Optional<UUID> requestingUser) {}

    public boolean hasEditPermissions(UUID id, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

    public boolean hasDeletePermissions(UUID id, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return false;
    }

}
