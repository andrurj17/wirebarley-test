package com.wirebarley.test.services.quote.exceptions;

public class QuoteDoesNotBelongToUserException extends RuntimeException {

  private static final String DEFAULT_MESSAGE_1 = "Quote ";
  private static final String DEFAULT_MESSAGE_2 = " does not belong to user ";

  public QuoteDoesNotBelongToUserException(final long quoteId, final long userId) {
    super(DEFAULT_MESSAGE_1 + quoteId + DEFAULT_MESSAGE_2 + userId);
  }
}
