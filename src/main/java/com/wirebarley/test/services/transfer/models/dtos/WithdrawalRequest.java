package com.wirebarley.test.services.transfer.models.dtos;

import lombok.NonNull;

import java.math.BigDecimal;

public record WithdrawalRequest(@NonNull BigDecimal amount) {
}
