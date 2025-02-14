package com.wirebarley.test.services.balance.models.dtos;

import lombok.NonNull;

import java.math.BigDecimal;

public record TopUpRequest(@NonNull BigDecimal amount) {
}
