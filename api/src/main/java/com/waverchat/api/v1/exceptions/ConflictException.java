package com.waverchat.api.v1.exceptions;

public class ConflictException extends Exception {

    public ConflictException (String resourceName, String fieldName, String conflictValue) {
        super(resourceName + "." + fieldName + " has conflict with existing value: " + conflictValue);
    }

    public ConflictException (String customMessage) {
        super(customMessage);
    }

}
