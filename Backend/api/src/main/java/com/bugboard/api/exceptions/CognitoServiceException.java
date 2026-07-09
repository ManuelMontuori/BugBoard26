package com.bugboard.api.exceptions;

public class CognitoServiceException extends BugboardException {
    public CognitoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}