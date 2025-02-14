package com.wirebarley.test.services.transfer.controllers;

import com.wirebarley.test.configuration.BaseIntTest;
import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import com.wirebarley.test.services.limit.repositories.LimitRepository;
import com.wirebarley.test.services.quote.models.entities.Quote;
import com.wirebarley.test.services.quote.repositories.QuoteRepository;
import com.wirebarley.test.services.transfer.models.dtos.TransferRequest;
import com.wirebarley.test.services.transfer.repositories.TransferRepository;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferControllerIntTest extends BaseIntTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private LimitRepository limitRepository;

    User user;
    User user2;
    Quote quote;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("John Doe", "john.doe@email.com", "password"));
        balanceRepository.save(new Balance(user.getId(), Currency.KRW, BigDecimal.valueOf(1000)));
        user2 = userRepository.save(new User("John Doe", "john.doe2@email.com", "password"));
        balanceRepository.save(new Balance(user2.getId(), Currency.KRW, BigDecimal.ZERO));
        quote = quoteRepository.save(new Quote(user.getId(), user2.getId(), new BigDecimal(100), new BigDecimal(99), BigDecimal.ONE));
    }

    @AfterEach
    void tearDown() {
        transferRepository.deleteAll();
        quoteRepository.deleteAll();
        balanceRepository.deleteAll();
        limitRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldTransfer() {
        final var request = new TransferRequest(quote.getId());

        webTestClient.post()
                .uri("public/v1/users/{userId}/transfers", user.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        final var transfer = transferRepository.findAll().getFirst();
        assertEquals(quote.getId(), transfer.getQuoteId());

        final var userBalance = balanceRepository.findAllByUserId(user.getId()).getFirst();
        assertEquals(0, (new BigDecimal(900)).compareTo(userBalance.getAmount()));
        final var user2Balance = balanceRepository.findAllByUserId(user2.getId()).getFirst();
        assertEquals(0, (new BigDecimal(99)).compareTo(user2Balance.getAmount()));
    }
}