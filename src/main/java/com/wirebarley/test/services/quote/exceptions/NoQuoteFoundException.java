package com.wirebarley.test.services.quote.exceptions;

public class NoQuoteFoundException extends RuntimeException {

  private static final String DEFAULT_MESSAGE = "No quote found with quote id ";

  public NoQuoteFoundException(final long quoteId) {
    super(DEFAULT_MESSAGE + quoteId);
  }
}
