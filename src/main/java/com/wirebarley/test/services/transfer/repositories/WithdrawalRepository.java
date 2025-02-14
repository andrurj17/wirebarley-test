package com.wirebarley.test.services.transfer.repositories;

import com.wirebarley.test.services.transfer.models.entities.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
