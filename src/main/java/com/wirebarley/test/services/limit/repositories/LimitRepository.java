package com.wirebarley.test.services.limit.repositories;

import com.wirebarley.test.services.limit.models.entities.Limit;
import com.wirebarley.test.services.limit.models.entities.LimitId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LimitRepository extends JpaRepository<Limit, LimitId> {
}
