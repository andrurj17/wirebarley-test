package com.wirebarley.test.services.quote.models.dtos;

import com.wirebarley.test.services.quote.models.entities.Quote;
import lombok.NonNull;

public record GetQuoteResponse(@NonNull Quote quote) {
}
