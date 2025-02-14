package com.wirebarley.test.services.transfer.repositories;

import com.wirebarley.test.services.transfer.models.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
