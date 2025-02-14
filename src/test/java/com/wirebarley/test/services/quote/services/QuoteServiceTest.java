package com.wirebarley.test.services.quote.services;

import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.quote.exceptions.NoQuoteFoundException;
import com.wirebarley.test.services.quote.exceptions.QuoteDoesNotBelongToUserException;
import com.wirebarley.test.services.quote.models.entities.Quote;
import com.wirebarley.test.services.quote.repositories.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    private QuoteRepository repository;
    @InjectMocks
    private QuoteService service;

    @Test
    void shouldCreateQuote() {
        final var quote = new Quote(1, 1, 2, Currency.KRW, Currency.KRW,
                new BigDecimal(100), new BigDecimal(99), BigDecimal.ONE, Calendar.getInstance());

        doReturn(quote).when(repository).save(any());

        final var result = service.createQuote(1, 2, new BigDecimal(100));

        assertEquals(quote, result);
        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldNotCreateQuoteWhenFromUserIdAndToUserIdAreTheSame() {
        assertThrows(AssertionError.class, () -> service.createQuote(1, 1, new BigDecimal(100)));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldGetQuote() {
        final var quote = new Quote(1, 1, 2, Currency.KRW, Currency.KRW,
                new BigDecimal(100), new BigDecimal(99), BigDecimal.ONE, Calendar.getInstance());

        doReturn(Optional.of(quote)).when(repository).findById(1L);

        final var result = service.getQuote(1, 1);

        assertEquals(quote, result);
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowNoQuoteFoundException() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThrows(NoQuoteFoundException.class, () -> service.getQuote(1, 1));
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowQuoteDoesNotBelongToUserException() {
        final var quote = new Quote(1, 1, 2, Currency.KRW, Currency.KRW,
                new BigDecimal(100), new BigDecimal(99), BigDecimal.ONE, Calendar.getInstance());

        doReturn(Optional.of(quote)).when(repository).findById(1L);

        assertThrows(QuoteDoesNotBelongToUserException.class, () -> service.getQuote(1, 2));
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);
    }
}