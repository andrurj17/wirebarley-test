package com.wirebarley.test.services.balance.services;

import com.wirebarley.test.services.balance.exceptions.InsufficientBalanceException;
import com.wirebarley.test.services.balance.exceptions.NoBalancesFoundException;
import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.models.entities.BalanceId;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.wirebarley.test.services.balance.models.Currency.KRW;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

    @NonNull
    private final BalanceRepository balanceRepository;

    public void createBalance(final long userId) {
        balanceRepository.save(new Balance(userId));
    }

    public Balance getBalance(final long userId, @NonNull final Currency currency) throws NoBalancesFoundException {
        final var balanceId = new BalanceId(userId, currency);
        log.info("Fetching balance for user {} with currency {}", userId, currency);
        return balanceRepository.findById(balanceId).orElseThrow(() -> new NoBalancesFoundException(userId));
    }

    // Aux function while only one currency is supported.
    public Balance getBalance(final long userId) {
        return getBalance(userId, KRW);
    }

    public List<Balance> getBalances(final long userId) throws NoBalancesFoundException {
        log.info("Fetching all balances for user {}", userId);
        final var balances = balanceRepository.findAllByUserId(userId);

        if (balances == null || balances.isEmpty()) {
            throw new NoBalancesFoundException(userId);
        }
        return balances;
    }

    @Transactional
    public void topUp(final long userId, @NonNull final BigDecimal amount) {
        assert amount.compareTo(BigDecimal.ZERO) > 0;

        final var balance = getBalance(userId);

        log.info("Topping up {} to user {}", amount, userId);
        updateBalance(balance, amount);
    }

    @Transactional
    public void withdraw(final long userId, @NonNull final BigDecimal amount) throws InsufficientBalanceException {
        assert amount.compareTo(BigDecimal.ZERO) > 0;

        final var balance = getBalance(userId);

        if (balance.getAmount().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(userId);
        }
        log.info("Withdrawing {} from user {}", amount, userId);
        updateBalance(balance, amount.negate());
    }

    private void updateBalance(@NonNull final Balance balance, @NonNull final BigDecimal deltaAmount) {
        final var newBalance = new Balance(balance.getUserId(), balance.getCurrency(), balance.getAmount().add(deltaAmount));
        log.info("Updating balance for user {} from {} to {}", balance.getUserId(), balance.getAmount(), newBalance.getAmount());
        balanceRepository.save(newBalance);
    }
}
