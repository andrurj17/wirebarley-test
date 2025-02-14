package com.wirebarley.test.services.transfer.controllers;

import com.wirebarley.test.configuration.BaseIntTest;
import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import com.wirebarley.test.services.limit.repositories.LimitRepository;
import com.wirebarley.test.services.transfer.models.dtos.WithdrawalRequest;
import com.wirebarley.test.services.transfer.repositories.WithdrawalRepository;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WithdrawalControllerIntTest extends BaseIntTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private LimitRepository limitRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("John Doe", "john.doe@email.com", "password"));
        balanceRepository.save(new Balance(user.getId(), Currency.KRW, BigDecimal.valueOf(1000)));
    }

    @AfterEach
    void tearDown() {
        withdrawalRepository.deleteAll();
        balanceRepository.deleteAll();
        limitRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldWithdraw() {
        final var amount = BigDecimal.valueOf(100);
        final var request = new WithdrawalRequest(amount);

        webTestClient.post()
                .uri("/public/v1/users/{userId}/withdrawals", user.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        final var withdrawal = withdrawalRepository.findAll().getFirst();
        assertEquals(user.getId(), withdrawal.getUserId());
        assertEquals(0, amount.compareTo(withdrawal.getAmount()));

        final var balance = balanceRepository.findAllByUserId(user.getId()).getFirst();
        assertEquals(0, BigDecimal.valueOf(900).compareTo(balance.getAmount()));
    }
}