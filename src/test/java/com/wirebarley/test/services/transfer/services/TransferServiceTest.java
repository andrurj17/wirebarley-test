package com.wirebarley.test.services.transfer.services;

import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.services.BalanceService;
import com.wirebarley.test.services.limit.services.LimitService;
import com.wirebarley.test.services.quote.models.entities.Quote;
import com.wirebarley.test.services.quote.services.QuoteService;
import com.wirebarley.test.services.transfer.models.entities.Transfer;
import com.wirebarley.test.services.transfer.models.entities.Withdrawal;
import com.wirebarley.test.services.transfer.repositories.TransferRepository;
import com.wirebarley.test.services.transfer.repositories.WithdrawalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;
    @Mock
    private WithdrawalRepository withdrawalRepository;

    @Mock
    private BalanceService balanceService;
    @Mock
    private LimitService limitService;
    @Mock
    private QuoteService quoteService;
    @InjectMocks
    private TransferService service;

    @Test
    void shouldTransfer() {
        final var fromUserId = 1L;
        final var toUserId = 2L;
        final var transferAmount = new BigDecimal(100);
        final var toAmount = new BigDecimal(99);
        final var quote = new Quote(1, fromUserId, toUserId, Currency.KRW, Currency.KRW,
                transferAmount, toAmount, BigDecimal.ONE, Calendar.getInstance());
        final var transfer = new Transfer(1, quote.getId(), Calendar.getInstance());

        doReturn(quote).when(quoteService).getQuote(1, fromUserId);
        doNothing().when(limitService).updateTransferLimit(fromUserId, transferAmount);
        doReturn(transfer).when(transferRepository).save(any());
        doNothing().when(balanceService).withdraw(fromUserId, transferAmount);
        doNothing().when(balanceService).topUp(toUserId, toAmount);

        service.transfer(fromUserId, 1);

        verify(quoteService, times(1)).getQuote(1, fromUserId);
        verify(limitService, times(1)).updateTransferLimit(fromUserId, transferAmount);
        verify(transferRepository, times(1)).save(any());
        verify(balanceService, times(1)).withdraw(fromUserId, transferAmount);
        verify(balanceService, times(1)).topUp(toUserId, toAmount);
        verifyNoMoreInteractions(transferRepository, withdrawalRepository, balanceService, limitService, quoteService);
    }

    @Test
    void shouldWithdraw() {
        final var userId = 1L;
        final var amount = new BigDecimal(100);
        final var withdrawal = new Withdrawal(1, userId, Currency.KRW, amount, Calendar.getInstance());

        doNothing().when(limitService).updateWithdrawalLimit(userId, amount);
        doReturn(withdrawal).when(withdrawalRepository).save(any());
        doNothing().when(balanceService).withdraw(userId, amount);

        service.withdraw(userId, amount);

        verify(limitService, times(1)).updateWithdrawalLimit(userId, amount);
        verify(withdrawalRepository, times(1)).save(any());
        verify(balanceService, times(1)).withdraw(userId, amount);
        verifyNoMoreInteractions(transferRepository, withdrawalRepository, balanceService, limitService, quoteService);
    }

}