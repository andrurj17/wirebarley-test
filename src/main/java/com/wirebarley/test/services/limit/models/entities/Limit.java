package com.wirebarley.test.services.limit.models.entities;

import com.wirebarley.test.services.limit.models.LimitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "amount_limits")
@IdClass(LimitId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Limit {

    @Id
    private long userId;

    @Id
    @NonNull
    @Enumerated(EnumType.STRING)
    private LimitType limitType;

    @NonNull
    private BigDecimal amount;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar limitAt;
}
