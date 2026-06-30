package com.bugboard.api.exceptions;

public class ResourceNotFoundException extends BugboardException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
