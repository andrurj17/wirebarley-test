package com.wirebarley.test.services.limit.exceptions;

public class TransferLimitExceeded extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "Transfer limit exceeded for user ";

  public TransferLimitExceeded(final long userId) {
    super(DEFAULT_MESSAGE + userId);
  }
}
