package com.wirebarley.test.services.quote.models.dtos;

import lombok.NonNull;

import java.math.BigDecimal;

// TODO: Extend to include from currency and to currency when more currencies are allowed
public record CreateQuoteRequest(long toUserId, @NonNull BigDecimal amount) {
}
