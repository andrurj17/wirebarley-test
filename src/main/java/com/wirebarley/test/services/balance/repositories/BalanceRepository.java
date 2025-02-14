package com.wirebarley.test.services.balance.repositories;

import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.models.entities.BalanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, BalanceId> {

    List<Balance> findAllByUserId(final long userId);
}
