package com.bugboard.api.exceptions;

public class CognitoServiceException extends RuntimeException {
    public CognitoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}