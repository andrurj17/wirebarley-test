package com.wirebarley.test.services.balance.repositories;

import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.models.entities.BalanceId;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, BalanceId> {

    List<Balance> findAllByUserId(final long userId);

    @Modifying
    @Query("UPDATE Balance b SET b.amount = b.amount + :deltaAmount WHERE b.userId = :userId AND b.currency = :currency")
    void updateBalance(long userId, @NonNull final Currency currency, @NonNull final BigDecimal deltaAmount);
}
