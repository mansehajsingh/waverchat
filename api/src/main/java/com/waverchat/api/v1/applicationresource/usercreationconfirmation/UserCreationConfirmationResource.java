package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import com.waverchat.api.v1.applicationresource.user.User;
import com.waverchat.api.v1.applicationresource.user.UserService;
import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-creation-confirmations")
public class UserCreationConfirmationResource extends AbstractApplicationResource<
        UserCreationConfirmation, UserCreationConfirmationService
> {

    @Autowired
    private UserService userService;

    @Override
    public boolean hasCreatePermissions(UserCreationConfirmation entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return true;
    }

    @Override
    public boolean hasViewPermissions(UUID id, Optional<UUID> requestingUser) {
        return true;
    }

    @Override
    public Map<String, Object> formCreationRequestBody(
            Optional<UserCreationConfirmation> createdUCCOpt,
            Map<String, Object> requestBody,
            Optional<UUID> requestingUser
    )
            throws NotImplementedException
    {
        Map responseBody = new HashMap<String, Object>();

        UserCreationConfirmation createdUCC = createdUCCOpt.get();

        responseBody.put("email", createdUCC.getEmail());
        responseBody.put("username", createdUCC.getUsername());
        responseBody.put("firstName", createdUCC.getFirstName());
        responseBody.put("lastName", createdUCC.getLastName());

        return responseBody;
    }

    @Override
    public Map<String, Object> formViewRequestBody(
            UUID id,
            UserCreationConfirmation queriedUCC,
            Optional<UUID> requestingUser
    )
            throws NotImplementedException
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Created user with provided details successfully.");
        return responseBody;
    }

    @Override
    public void afterGet(UUID id, Optional<UserCreationConfirmation> queriedEntity, Optional<UUID> requestingUser) {
        if (queriedEntity.isEmpty()) return;

        UserCreationConfirmation toDelete = queriedEntity.get();

        User userToCreate = new User(
                toDelete.getEmail(),
                toDelete.getUsername(),
                toDelete.getPasswordHash(),
                toDelete.getFirstName(),
                toDelete.getLastName(),
                toDelete.isSuperUser(),
                toDelete.isDeleted()
        );

        this.userService.create(userToCreate);
        this.service.deleteAllByEmail(toDelete.getEmail());
    }

}
