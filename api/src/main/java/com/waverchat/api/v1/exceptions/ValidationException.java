package com.waverchat.api.v1.exceptions;

import java.util.List;

public class ValidationException extends Exception {

    private List<String> messages;

    public ValidationException(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public List<String> getMessages() {
        return this.messages;
    }

}
