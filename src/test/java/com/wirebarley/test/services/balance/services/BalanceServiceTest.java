package com.wirebarley.test.services.balance.services;

import com.wirebarley.test.services.balance.exceptions.InsufficientBalanceException;
import com.wirebarley.test.services.balance.models.Currency;
import com.wirebarley.test.services.balance.models.entities.Balance;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository repository;
    @Mock
    private LockManagerService lockManagerService;
    @InjectMocks
    private BalanceService service;

    @Test
    void shouldCreateBalance() {
        final var balance = new Balance(1);

        doReturn(balance).when(repository).save(any());

        service.createBalance(1);

        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository, lockManagerService);
    }

    @Test
    void shouldGetBalance() {
        final var balance = new Balance(1);

        doReturn(Optional.of(balance)).when(repository).findById(any());

        final var result = service.getBalance(1, Currency.KRW);

        assertEquals(balance, result);
        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository, lockManagerService);
    }

    @Test
    void shouldGetBalanceWhenNoCurrencyPassed() {
        final var balance = new Balance(1);

        doReturn(Optional.of(balance)).when(repository).findById(any());

        final var result = service.getBalance(1);

        assertEquals(balance, result);
        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository, lockManagerService);
    }

    @Test
    void shouldFetchBalances() {
        final var balance = new Balance(1);

        doReturn(List.of(balance)).when(repository).findAllByUserId(1);

        final var result = service.getBalances(1);

        assertEquals(1, result.size());
        assertEquals(balance, result.getFirst());
        verify(repository, times(1)).findAllByUserId(1);
        verifyNoMoreInteractions(repository, lockManagerService);
    }

    @Test
    void shouldTopUp() {
        final var balance = new Balance(1, Currency.KRW, BigDecimal.ONE);
        final var lock = mock(ReentrantLock.class);

        doReturn(Optional.of(balance)).when(repository).findById(any());
        doReturn(balance).when(repository).save(any());
        doReturn(lock).when(lockManagerService).getLockForUser(1);

        service.topUp(1, BigDecimal.ONE);

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        verify(lockManagerService, times(1)).getLockForUser(1);
        verify(lock, times(1)).lock();
        verify(lock, times(1)).unlock();
        verifyNoMoreInteractions(repository, lockManagerService, lock);
    }

    @Test
    void shouldNotTopUpWhenAmountIsZero() {
        assertThrows(AssertionError.class, () -> service.topUp(1, BigDecimal.ZERO));
        verifyNoMoreInteractions(repository, lockManagerService);
    }

    @Test
    void shouldWithdraw() {
        final var balance = new Balance(1, Currency.KRW, BigDecimal.TEN);
        final var lock = mock(ReentrantLock.class);

        doReturn(Optional.of(balance)).when(repository).findById(any());
        doReturn(balance).when(repository).save(any());
        doReturn(lock).when(lockManagerService).getLockForUser(1);

        service.withdraw(1, BigDecimal.ONE);

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        verify(lockManagerService, times(1)).getLockForUser(1);
        verify(lock, times(1)).lock();
        verify(lock, times(1)).unlock();
        verifyNoMoreInteractions(repository, lockManagerService, lock);
    }

    @Test
    void shouldNotWithdrawWhenAmountIsZero() {
        assertThrows(AssertionError.class, () -> service.withdraw(1, BigDecimal.ZERO));
        verifyNoMoreInteractions(repository, lockManagerService);
    }

    @Test
    void shouldThrowInsufficientBalanceExceptionWhenWithdrawingMoreThanBalance() {
        final var balance = new Balance(1);
        final var lock = mock(ReentrantLock.class);

        doReturn(Optional.of(balance)).when(repository).findById(any());
        doReturn(lock).when(lockManagerService).getLockForUser(1);

        assertThrows(InsufficientBalanceException.class, () -> service.withdraw(1, BigDecimal.TEN));

        verify(repository, times(1)).findById(any());
        verify(lockManagerService, times(1)).getLockForUser(1);
        verify(lock, times(1)).lock();
        verify(lock, times(1)).unlock();
        verifyNoMoreInteractions(repository, lockManagerService, lock);
    }
}