package com.wirebarley.test.services.quote.services;


import com.wirebarley.test.services.quote.exceptions.NoQuoteFoundException;
import com.wirebarley.test.services.quote.exceptions.QuoteDoesNotBelongToUserException;
import com.wirebarley.test.services.quote.models.entities.Quote;
import com.wirebarley.test.services.quote.repositories.QuoteRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@AllArgsConstructor
public class QuoteService {

    /**
     * TODO: Move fee to some internal tooling where it can be configured by currencies
     * or, at least, where it can easily be updated without code changes.
     */
    final static BigDecimal FEE_PERCENTAGE = new BigDecimal("0.01");

    @NonNull
    private final QuoteRepository quoteRepository;

    public Quote createQuote(final long fromUserId, final long toUserId, @NonNull final BigDecimal amount) {
        assert fromUserId != toUserId;

        log.info("Calculating fee for amount {}", amount);
        final var fee = amount.multiply(FEE_PERCENTAGE);
        final var toAmount = amount.subtract(fee);

        final var quote = new Quote(fromUserId, toUserId, amount, toAmount, fee);
        log.info("Creating quote from user {} to user {}", fromUserId, toUserId);
        return quoteRepository.save(quote);
    }

    public Quote getQuote(final long quoteId, final long userId) throws NoQuoteFoundException, QuoteDoesNotBelongToUserException {
        log.info("Fetching quote {}", quoteId);
        final var quoteOptional = quoteRepository.findById(quoteId);

        if (quoteOptional.isEmpty()) {
            throw new NoQuoteFoundException(quoteId);
        }

        final var quote = quoteOptional.get();
        if (quote.getFromUserId() != userId) {
            throw new QuoteDoesNotBelongToUserException(quoteId, userId);
        }
        return quote;
    }
}
