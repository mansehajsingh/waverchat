package com.waverchat.api.v1.exceptions;

public class NotFoundException extends Exception {

    public NotFoundException(String resourceName, Long id) {
        super(resourceName + " with id " + id.toString() + " does not exist or could not be found.");
    }

    public NotFoundException(String message) { super(message); }

}
