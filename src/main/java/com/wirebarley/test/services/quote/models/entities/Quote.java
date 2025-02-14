package com.wirebarley.test.services.quote.models.entities;

import com.wirebarley.test.services.balance.models.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "quotes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long fromUserId;

    private long toUserId;

    @NonNull
    @Enumerated(EnumType.STRING)
    // Set as default to KRW. This can later be changed to a user-specific currency.
    private Currency fromCurrency = Currency.KRW;

    @NonNull
    @Enumerated(EnumType.STRING)
    // Set as default to KRW. This can later be changed to a user-specific currency.
    private Currency toCurrency = Currency.KRW;

    @NonNull
    private BigDecimal fromAmount;

    @NonNull
    private BigDecimal toAmount;

    @NonNull
    private BigDecimal fee;

    @NonNull
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    public Quote(final long fromUserId, final long toUserId, @NonNull final BigDecimal fromAmount,
                 @NonNull final BigDecimal toAmount, @NonNull final BigDecimal fee) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.fee = fee;
    }

    @PrePersist
    void addTimestamp() {
        createdAt = Calendar.getInstance();
    }
}
