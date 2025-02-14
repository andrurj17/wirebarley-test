package com.wirebarley.test.services.quote.repositories;

import com.wirebarley.test.services.quote.models.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
