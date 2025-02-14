package com.wirebarley.test.services.user.exceptions;

public class InvalidUserCredentialsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid email or password";

    public InvalidUserCredentialsException() {
        super(DEFAULT_MESSAGE);
    }
}
