package com.waverchat.api.v1.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException (String resourceName, UUID id) {
        super(resourceName + " with id " + id.toString() + " does not exist or could not be found.");
    }

}
