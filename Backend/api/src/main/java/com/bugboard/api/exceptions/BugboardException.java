package com.bugboard.api.exceptions;

public abstract class BugboardException extends RuntimeException {
    protected BugboardException(String message) {
        super(message);
    }

    protected BugboardException(String message, Throwable cause) {
        super(message, cause);
    }
}
