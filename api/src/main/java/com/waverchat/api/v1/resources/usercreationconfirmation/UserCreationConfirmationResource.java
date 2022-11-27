package com.waverchat.api.v1.resources.usercreationconfirmation;

import com.waverchat.api.v1.customframework.RQRSLifecycleProperties;
import com.waverchat.api.v1.resources.user.User;
import com.waverchat.api.v1.resources.user.UserService;
import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public boolean hasCreatePermissions(UserCreationConfirmation entityToCreate, RQRSLifecycleProperties props) {
        return true;
    }

    @Override
    public boolean hasViewPermissions(UUID id, RQRSLifecycleProperties props) {
        return true;
    }

    @Override
    public void afterGet(UUID id, Optional<UserCreationConfirmation> queriedEntity, RQRSLifecycleProperties props) {
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
