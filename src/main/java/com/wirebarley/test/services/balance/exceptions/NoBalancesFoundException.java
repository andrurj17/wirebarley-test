package com.wirebarley.test.services.balance.exceptions;

public class NoBalancesFoundException extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "No balances found for user ";

  public NoBalancesFoundException(final long userId) {
    super(DEFAULT_MESSAGE + userId);
  }
}
