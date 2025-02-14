package com.wirebarley.test.services.quote.controllers;

import com.wirebarley.test.configuration.BaseIntTest;
import com.wirebarley.test.services.quote.models.dtos.CreateQuoteRequest;
import com.wirebarley.test.services.quote.models.dtos.CreateQuoteResponse;
import com.wirebarley.test.services.quote.models.dtos.GetQuoteResponse;
import com.wirebarley.test.services.quote.models.entities.Quote;
import com.wirebarley.test.services.quote.repositories.QuoteRepository;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static com.wirebarley.test.services.balance.models.Currency.KRW;
import static org.junit.jupiter.api.Assertions.*;

class QuoteControllerIntTest extends BaseIntTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuoteRepository quoteRepository;

    User user;
    User user2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("John Doe", "john.doe@email.com", "password"));
        user2 = userRepository.save(new User("John Doe", "john.doe2@email.com", "password"));
    }

    @AfterEach
    void tearDown() {
        quoteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateQuote() {
        final var amount = new BigDecimal(100);
        final var toAmount = new BigDecimal(99);
        final var fee = BigDecimal.ONE;
        final var request = new CreateQuoteRequest(user2.getId(), amount);

        webTestClient.post()
                .uri("/public/v1/users/{userId}/quotes", user.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CreateQuoteResponse.class)
                .value(response -> {
                    final var quote = response.quote();
                    assertEquals(user.getId(), quote.getFromUserId());
                    assertEquals(user2.getId(), quote.getToUserId());
                    assertEquals(KRW, quote.getFromCurrency());
                    assertEquals(KRW, quote.getToCurrency());
                    assertEquals(0, amount.compareTo(quote.getFromAmount()));
                    assertEquals(0, toAmount.compareTo(quote.getToAmount()));
                    assertEquals(0, fee.compareTo(quote.getFee()));
                    assertNotNull(quote.getCreatedAt());
                });
    }

    @Test
    void shouldGetQuote() {
        final var amount = new BigDecimal(100);
        final var toAmount = new BigDecimal(99);
        final var fee = BigDecimal.ONE;
        final var quote = quoteRepository.save(new Quote(user.getId(), user2.getId(), amount, toAmount, fee));

        webTestClient.get()
                .uri("/public/v1/users/{userId}/quotes/{quoteId}", user.getId(), quote.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(GetQuoteResponse.class)
                .value(response -> {
                    assertEquals(user.getId(), response.quote().getFromUserId());
                    assertEquals(user2.getId(), response.quote().getToUserId());
                    assertEquals(KRW, response.quote().getFromCurrency());
                    assertEquals(KRW, response.quote().getToCurrency());
                    assertEquals(0, amount.compareTo(response.quote().getFromAmount()));
                    assertEquals(0, toAmount.compareTo(response.quote().getToAmount()));
                    assertEquals(0, fee.compareTo(response.quote().getFee()));
                    assertNotNull(response.quote().getCreatedAt());
                });
    }

}