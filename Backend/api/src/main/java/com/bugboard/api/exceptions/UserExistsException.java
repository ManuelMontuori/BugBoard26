package com.bugboard.api.exceptions;

public class UserExistsException extends BugboardException {
    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}