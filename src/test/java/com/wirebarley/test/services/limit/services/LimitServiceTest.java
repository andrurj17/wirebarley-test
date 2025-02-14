package com.wirebarley.test.services.limit.services;

import com.wirebarley.test.services.limit.exceptions.TransferLimitExceeded;
import com.wirebarley.test.services.limit.exceptions.WithdrawalLimitExceeded;
import com.wirebarley.test.services.limit.models.LimitType;
import com.wirebarley.test.services.limit.models.entities.Limit;
import com.wirebarley.test.services.limit.repositories.LimitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimitServiceTest {

    @Mock
    private LimitRepository repository;
    @InjectMocks
    private LimitService service;

    private static Stream<Arguments> updateWithdrawalLimitScenarios() {
        return Stream.of(
                Arguments.of(Optional.empty()),
                Arguments.of(Optional.of(
                        new Limit(1, LimitType.WITHDRAW_LIMIT, BigDecimal.TEN, Calendar.getInstance())
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("updateWithdrawalLimitScenarios")
    void shouldUpdateWithdrawalLimit(final Optional<Limit> limit) {
        doReturn(limit).when(repository).findById(any());

        service.updateWithdrawalLimit(1, BigDecimal.TEN);

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldUpdateWithdrawalLimitWhenCurrentLimitIsFromPreviousDay() {
        final var limit = new Limit(1, LimitType.WITHDRAW_LIMIT, BigDecimal.TEN, Calendar.getInstance());
        limit.getLimitAt().add(Calendar.DAY_OF_YEAR, -1);

        doReturn(Optional.of(limit)).when(repository).findById(any());

        service.updateWithdrawalLimit(1, BigDecimal.valueOf(1_000_001));

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository);
    }

    private static Stream<Arguments> updateWithdrawalLimitZeroOrLessScenarios() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO),
                Arguments.of(new BigDecimal(-1))
        );
    }

    @ParameterizedTest
    @MethodSource("updateWithdrawalLimitZeroOrLessScenarios")
    void shouldNotUpdateWithdrawalLimitWhenAmountIsZero(final BigDecimal amount) {
        assertThrows(AssertionError.class, () -> service.updateWithdrawalLimit(1, amount));
        verifyNoInteractions(repository);
    }

    @Test
    void shouldNotUpdateWithdrawalLimitWhenLimitIsExceeded() {
        final var limit = new Limit(1, LimitType.WITHDRAW_LIMIT, BigDecimal.ZERO, Calendar.getInstance());

        doReturn(Optional.of(limit)).when(repository).findById(any());

        assertThrows(WithdrawalLimitExceeded.class, () -> service.updateWithdrawalLimit(1, BigDecimal.valueOf(1_000_001)));

        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository);
    }

    private static Stream<Arguments> updateTransferLimitScenarios() {
        return Stream.of(
                Arguments.of(Optional.empty()),
                Arguments.of(Optional.of(
                        new Limit(1, LimitType.TRANSFER_LIMIT, BigDecimal.TEN, Calendar.getInstance())
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("updateTransferLimitScenarios")
    void shouldUpdateTransferLimit(final Optional<Limit> limit) {
        doReturn(limit).when(repository).findById(any());

        service.updateTransferLimit(1, BigDecimal.TEN);

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldUpdateTransferLimitWhenCurrentLimitIsFromPreviousDay() {
        final var limit = new Limit(1, LimitType.TRANSFER_LIMIT, BigDecimal.TEN, Calendar.getInstance());
        limit.getLimitAt().add(Calendar.DAY_OF_YEAR, -1);

        doReturn(Optional.of(limit)).when(repository).findById(any());

        service.updateTransferLimit(1, BigDecimal.valueOf(3_000_000));

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
        verifyNoMoreInteractions(repository);
    }

    private static Stream<Arguments> updateTransferLimitZeroOrLessScenarios() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO),
                Arguments.of(new BigDecimal(-1))
        );
    }

    @ParameterizedTest
    @MethodSource("updateTransferLimitZeroOrLessScenarios")
    void shouldNotUpdateTransferLimitWhenAmountIsZeroOrLess(final BigDecimal amount) {
        assertThrows(AssertionError.class, () -> service.updateTransferLimit(1, amount));
        verifyNoInteractions(repository);
    }

    @Test
    void shouldNotUpdateTransferLimitWhenLimitIsExceeded() {
        final var limit = new Limit(1, LimitType.TRANSFER_LIMIT, BigDecimal.ZERO, Calendar.getInstance());

        doReturn(Optional.of(limit)).when(repository).findById(any());

        assertThrows(TransferLimitExceeded.class, () -> service.updateTransferLimit(1, BigDecimal.valueOf(3_000_001)));

        verify(repository, times(1)).findById(any());
        verifyNoMoreInteractions(repository);
    }
}