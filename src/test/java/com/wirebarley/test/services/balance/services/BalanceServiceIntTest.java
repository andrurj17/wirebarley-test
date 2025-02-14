package com.wirebarley.test.services.balance.services;

import com.wirebarley.test.configuration.BaseIntTest;
import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import com.wirebarley.test.services.limit.repositories.LimitRepository;
import com.wirebarley.test.services.transfer.repositories.WithdrawalRepository;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceServiceIntTest extends BaseIntTest {

    private static final int NUM_THREADS = 20;
    private static final int NUM_OPERATIONS_PER_THREAD = 10;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private WithdrawalRepository withdrawalRepository;
    @Autowired
    private LimitRepository limitRepository;

    @Autowired
    private BalanceService service;

    User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("John Doe", "john.doe@email.com", "password"));
        balanceRepository.save(new Balance(user.getId(), Currency.KRW, BigDecimal.valueOf(10000)));
    }

    @AfterEach
    void tearDown() {
        withdrawalRepository.deleteAll();
        balanceRepository.deleteAll();
        limitRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldWithdrawAndTopUpCorrectlyOnMultipleThreads() {


        final var executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (var i = 0; i < NUM_THREADS; i++) {
            final var threadId = i;
            executor.submit(() -> {
                for (var j = 0; j < NUM_OPERATIONS_PER_THREAD; j++) {
                    if (threadId % 2 == 0) {
                        service.topUp(user.getId(), BigDecimal.TEN);
                    } else {
                        service.withdraw(user.getId(), new BigDecimal(5));
                    }
                }
            });
        }

        executor.shutdown();

        while (!executor.isTerminated());

        final var balance = balanceRepository.findAllByUserId(user.getId()).getFirst();
        final var expectedFinalBalance = getFinalBalance();

        assertEquals(0, expectedFinalBalance.compareTo(balance.getAmount()));
    }

    private static BigDecimal getFinalBalance() {
        final var initialBalance = BigDecimal.valueOf(10000); // Initial balance set in setUp
        final var expectedChangePerThread = (BigDecimal.TEN
                .multiply(new BigDecimal(NUM_OPERATIONS_PER_THREAD))
                .divide(new BigDecimal(2))
        ).subtract(new BigDecimal(5)
                .multiply(new BigDecimal(NUM_OPERATIONS_PER_THREAD))
                .divide(new BigDecimal(2))
        );
        return initialBalance.add(expectedChangePerThread.multiply(new BigDecimal(NUM_THREADS)));
    }
}