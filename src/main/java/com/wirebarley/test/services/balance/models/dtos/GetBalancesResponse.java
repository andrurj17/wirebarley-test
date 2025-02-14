package com.wirebarley.test.services.balance.models.dtos;

import com.wirebarley.test.services.balance.models.entities.Balance;
import lombok.NonNull;

import java.util.List;

public record GetBalancesResponse(@NonNull List<Balance> balances) {
}
