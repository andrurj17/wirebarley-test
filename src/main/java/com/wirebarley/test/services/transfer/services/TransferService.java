package com.wirebarley.test.services.transfer.services;

import com.wirebarley.test.services.balance.services.BalanceService;
import com.wirebarley.test.services.limit.services.LimitService;
import com.wirebarley.test.services.quote.services.QuoteService;
import com.wirebarley.test.services.transfer.models.entities.Transfer;
import com.wirebarley.test.services.transfer.models.entities.Withdrawal;
import com.wirebarley.test.services.transfer.repositories.TransferRepository;
import com.wirebarley.test.services.transfer.repositories.WithdrawalRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    @NonNull
    private final TransferRepository transferRepository;
    @NonNull
    private final WithdrawalRepository withdrawalRepository;

    @NonNull
    private final BalanceService balanceService;
    @NonNull
    private final LimitService limitService;
    @NonNull
    private final QuoteService quoteService;

    @Transactional
    public void transfer(final long fromUserId, final long quoteId) {
        log.info("Fetching quote {} to transfer", quoteId);
        final var quote = quoteService.getQuote(quoteId, fromUserId);

        // Update the user's transfer daily use
        // LimitService will throw an exception if the user has exceeded the transfer limit
        log.info("Updating transfer limit for user {}", quote.getFromUserId());
        limitService.updateTransferLimit(quote.getFromUserId(), quote.getFromAmount());

        // Create transfer in database
        final var transfer = new Transfer(quoteId);
        log.info("Creating transfer from user {} to user {}", quote.getFromUserId(), quote.getToUserId());
        transferRepository.save(transfer);

        // Deduct the amount from the fromUserId's balance
        // BalanceService will throw an exception if the fromUserId has not enough balance to transfer the amount
        balanceService.withdraw(quote.getFromUserId(), quote.getFromAmount());

        // Add the amount to the toUserId's balance
        log.info("Topping up amount to user {}", quote.getToUserId());
        balanceService.topUp(quote.getToUserId(), quote.getToAmount());
    }

    @Transactional
    public void withdraw(final long userId, @NonNull final BigDecimal amount) {
        // Update the user's withdrawal daily use
        // LimitService will throw an exception if the user has exceeded the withdrawal limit
        log.info("Updating withdrawal limit for user {}", userId);
        limitService.updateWithdrawalLimit(userId, amount);

        // Create withdrawal in database
        final var withdrawal = new Withdrawal(userId, amount);
        log.info("Creating withdrawal for user {}", userId);
        withdrawalRepository.save(withdrawal);

        // Deduct the amount from the user's balance
        // BalanceService will throw an exception if the user has not enough balance to withdraw the amount
        balanceService.withdraw(userId, amount);
    }
}
