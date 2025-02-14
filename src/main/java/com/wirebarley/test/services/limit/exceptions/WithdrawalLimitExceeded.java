package com.wirebarley.test.services.limit.exceptions;

public class WithdrawalLimitExceeded extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "Withdrawal limit exceeded for user ";

  public WithdrawalLimitExceeded(final long userId) {
    super(DEFAULT_MESSAGE + userId);
  }
}
