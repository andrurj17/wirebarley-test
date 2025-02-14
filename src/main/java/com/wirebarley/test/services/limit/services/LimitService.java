package com.wirebarley.test.services.limit.services;

import com.wirebarley.test.services.limit.exceptions.TransferLimitExceeded;
import com.wirebarley.test.services.limit.exceptions.WithdrawalLimitExceeded;
import com.wirebarley.test.services.limit.models.entities.Limit;
import com.wirebarley.test.services.limit.models.entities.LimitId;
import com.wirebarley.test.services.limit.models.LimitType;
import com.wirebarley.test.services.limit.repositories.LimitRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimitService {

    final private static BigDecimal MAX_TRANSFER_LIMIT = new BigDecimal(3_000_000);
    final private static BigDecimal MAX_WITHDRAWAL_LIMIT = new BigDecimal(1_000_000);

    @NonNull
    private LimitRepository limitRepository;

    @Transactional
    public void updateWithdrawalLimit(final long userId, @NonNull final BigDecimal amount) throws WithdrawalLimitExceeded {
        assert amount.compareTo(BigDecimal.ZERO) > 0;

        // Fetch the user's withdrawal daily use and check update won't exceed the limit
        final var limit = getWithdrawalLimit(userId);

        // Check if today is same date as limit as the limit is daily
        // If not, reset the limit
        final var today = Calendar.getInstance();
        if (limit.isEmpty() ||
                today.get(Calendar.DAY_OF_YEAR) != limit.get().getLimitAt().get(Calendar.DAY_OF_YEAR) ||
                today.get(Calendar.YEAR) != limit.get().getLimitAt().get(Calendar.YEAR)
        ) {
            updateLimit(userId, LimitType.WITHDRAW_LIMIT, amount, today);
            return;
        }

        final var newAmount = amount.add(limit.get().getAmount());
        if (newAmount.compareTo(MAX_WITHDRAWAL_LIMIT) > 0) {
            throw new WithdrawalLimitExceeded(userId);
        }

        // Update the user's withdrawal daily use
        updateLimit(userId, LimitType.WITHDRAW_LIMIT, newAmount, today);
    }

    @Transactional
    public void updateTransferLimit(final long userId, @NonNull final BigDecimal amount) throws TransferLimitExceeded {
        assert amount.compareTo(BigDecimal.ZERO) > 0;

        // Fetch the user's transfer daily use and check update won't exceed the limit
        final var limit = getTransferLimit(userId);

        // Check if today is same date as limit as the limit is daily
        // If not, reset the limit
        final var today = Calendar.getInstance();
        if (limit.isEmpty() ||
                today.get(Calendar.DAY_OF_YEAR) != limit.get().getLimitAt().get(Calendar.DAY_OF_YEAR) ||
                today.get(Calendar.YEAR) != limit.get().getLimitAt().get(Calendar.YEAR)
        ) {
            updateLimit(userId, LimitType.TRANSFER_LIMIT, amount, today);
            return;
        }

        final var newAmount = amount.add(limit.get().getAmount());
        if (newAmount.compareTo(MAX_TRANSFER_LIMIT) > 0) {
            throw new TransferLimitExceeded(userId);
        }

        // Update the user's withdrawal daily use
        updateLimit(userId, LimitType.TRANSFER_LIMIT, newAmount, today);
    }

    private Optional<Limit> getWithdrawalLimit(final long userId) {
        // Fetch the user's withdrawal daily use
        final var limitId = new LimitId(userId, LimitType.WITHDRAW_LIMIT);
        return limitRepository.findById(limitId);
    }

    private Optional<Limit> getTransferLimit(final long userId) {
        // Fetch the user's transfer daily use
        final var limitId = new LimitId(userId, LimitType.TRANSFER_LIMIT);
        return limitRepository.findById(limitId);
    }

    private void updateLimit(final long userId, @NonNull final LimitType type, @NonNull final BigDecimal amount,
                             @NonNull final Calendar limitAt) {
        final var limit = new Limit(userId, type, amount, limitAt);
        log.info("Updating limit {} for user {} with amount {}", type, userId, amount);
        limitRepository.save(limit);
    }
}
