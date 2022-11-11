package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import com.waverchat.api.v1.applicationresource.user.User;
import com.waverchat.api.v1.applicationresource.user.UserService;
import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-creation-confirmations")
public class UserCreationConfirmationResource extends AbstractApplicationResource<
        UserCreationConfirmation, UserCreationConfirmationService, UserCreationConfirmationResponseFactory
> {

    @Autowired
    private UserService userService;

    @Override
    public boolean hasCreatePermissions(UserCreationConfirmation entityToCreate, Map<String, Object> requestBody, Optional<UUID> requestingUser) {
        return true;
    }

    @Override
    public boolean hasViewPermissions(UUID id, Map<String, String> pathVariables, Optional<UUID> requestingUser) {
        return true;
    }

    @Override
    public void afterGet(UUID id, Map<String, String> pathVariables, Optional<UserCreationConfirmation> queriedEntity, Optional<UUID> requestingUser) {
        if (queriedEntity.isEmpty()) return;

        UserCreationConfirmation toDelete = queriedEntity.get();

        // TODO: Send email template containing id

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
