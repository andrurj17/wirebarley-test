package com.wirebarley.test.services.transfer.models.entities;

import com.wirebarley.test.services.balance.models.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "withdrawals")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    @NonNull
    @Enumerated(EnumType.STRING)
    // Set as default to KRW. This can later be changed to a user-specific currency.
    private Currency currency = Currency.KRW;

    @NonNull
    private BigDecimal amount;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar withdrawnAt;

    public Withdrawal(final long userId, @NonNull final BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    @PrePersist
    void addTimestamp() {
        withdrawnAt = Calendar.getInstance();
    }
}
