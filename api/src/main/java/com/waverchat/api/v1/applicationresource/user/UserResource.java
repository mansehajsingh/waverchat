package com.waverchat.api.v1.applicationresource.user;

import com.waverchat.api.v1.customframework.AbstractApplicationResource;
import com.waverchat.api.v1.exceptions.NotImplementedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/v1/users")
public class UserResource extends AbstractApplicationResource<User, UserService, UserResponseFactory> {

    @Override
    public boolean hasViewAllPermissions(Map<String, String> pathVariables, Map<String, String> queryParams, Optional<UUID> requestingUser) {
        return true;
    }

}
