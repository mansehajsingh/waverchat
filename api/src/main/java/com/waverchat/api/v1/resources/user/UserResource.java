package com.waverchat.api.v1.resources.user;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.customframework.RequestProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserResource extends AbstractApplicationResource<User, UserService, UserResponseFactory> {

    @Override
    public boolean hasViewPermissions(UUID id, RequestProperties props) {
        return true;
    }

    @Override
    public boolean hasViewAllPermissions(RequestProperties props) {
        return true;
    }

    @Override
    public boolean hasEditPermissions(UUID id, User candidateEntity, RequestProperties props) {
        if (!props.hasRequestingUserId())
            return false;

        if (!props.getRequestingUserId().equals(id))
            return false;

        return true;
     }
}
