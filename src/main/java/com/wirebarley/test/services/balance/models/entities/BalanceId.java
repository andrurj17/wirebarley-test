package com.wirebarley.test.services.balance.models.entities;

import com.wirebarley.test.services.balance.models.Currency;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
public class BalanceId {

    private long userId;

    @NonNull
    // Set as default to KRW. This can later be changed to a user-specific currency.
    private Currency currency = Currency.KRW;

    public BalanceId(final long userId) {
        this.userId = userId;
    }
}
