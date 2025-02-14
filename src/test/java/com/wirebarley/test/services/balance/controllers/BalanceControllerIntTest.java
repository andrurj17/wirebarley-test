package com.wirebarley.test.services.balance.controllers;

import com.wirebarley.test.configuration.BaseIntTest;
import com.wirebarley.test.services.balance.models.dtos.TopUpRequest;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceControllerIntTest extends BaseIntTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    User user;
    Balance balance;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("John Doe", "john.doe@email.com", "password"));
        balance = balanceRepository.save(new Balance(user.getId()));
    }

    @AfterEach
    void tearDown() {
        balanceRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldGetBalances() {
        webTestClient.get()
                .uri("/public/v1/users/{userId}/balances", user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Balance.class)
                .value(balances -> {
                    assertFalse(balances.isEmpty());
                    assertEquals(balance.getCurrency(), balances.getFirst().getCurrency());
                    assertEquals(balance.getAmount(), balances.getFirst().getAmount());
                });
    }

    @Test
    void shouldTopUp() {
        final var amount = new BigDecimal(100);
        final var request = new TopUpRequest(amount);

        webTestClient.post()
                .uri("public/v1/users/{userId}/balances/topUp", user.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        final var updatedBalances = balanceRepository.findAllByUserId(user.getId());
        assertEquals(1, updatedBalances.size());
        final var updatedBalance = updatedBalances.getFirst();
        assertEquals(0, amount.compareTo(updatedBalance.getAmount()));
    }

}