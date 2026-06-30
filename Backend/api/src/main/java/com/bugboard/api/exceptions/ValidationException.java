package com.bugboard.api.exceptions;

public class ValidationException extends BugboardException {
    public ValidationException(String message) {
        super(message);
    }
}