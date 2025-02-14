package com.wirebarley.test.services.balance.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Insufficient balance for user ";

    public InsufficientBalanceException(final long userId) {
        super(DEFAULT_MESSAGE + userId);
    }
}
