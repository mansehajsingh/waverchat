package com.waverchat.api.v1.resources.user;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserResource extends AbstractApplicationResource<User, UserService, UserResponseFactory> {

    @Override
    public boolean hasViewPermissions(UUID id, Optional<UUID> requestingUser) {
        return true;
    }

    @Override
    public boolean hasViewAllPermissions(Map<String, String> queryParams, Optional<UUID> requestingUser) {
        return true;
    }

    @Override
    public boolean hasEditPermissions(UUID id, User candidateEntity, Optional<UUID> requestingUser) {
        if (!requestingUser.isPresent())
            return false;

        if (!requestingUser.get().equals(id))
            return false;

        return true;
     }
}
