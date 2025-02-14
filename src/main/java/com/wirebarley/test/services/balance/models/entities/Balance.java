package com.wirebarley.test.services.balance.models.entities;

import com.wirebarley.test.services.balance.models.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Entity
@Table(name = "balances")
@IdClass(BalanceId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Balance {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @NonNull
    @Enumerated(EnumType.STRING)
    // Set as default to KRW. This can later be changed to a user-specific currency.
    private Currency currency = Currency.KRW;

    @NonNull
    // Set as default to 0 when creating a new balance.
    private BigDecimal amount = BigDecimal.ZERO;

    public Balance(final long userId) {
        this.userId = userId;
    }
}
