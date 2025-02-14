package com.wirebarley.test.services;

import com.wirebarley.test.services.balance.exceptions.InsufficientBalanceException;
import com.wirebarley.test.services.balance.exceptions.NoBalancesFoundException;
import com.wirebarley.test.services.limit.exceptions.TransferLimitExceeded;
import com.wirebarley.test.services.limit.exceptions.WithdrawalLimitExceeded;
import com.wirebarley.test.services.quote.exceptions.NoQuoteFoundException;
import com.wirebarley.test.services.quote.exceptions.QuoteDoesNotBelongToUserException;
import com.wirebarley.test.services.user.exceptions.InvalidUserCredentialsException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.wirebarley.test.services")
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ExceptionHandler({InvalidUserCredentialsException.class})
    public ResponseEntity<String> handleInvalidUserCredentialsException(@NonNull final InvalidUserCredentialsException e) {
        log.warn("Invalid user credentials exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({InsufficientBalanceException.class})
    public ResponseEntity<String> handleInsufficientBalanceException(@NonNull final InsufficientBalanceException e) {
        log.warn("Insufficient balance exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({NoBalancesFoundException.class})
    public ResponseEntity<String> handleNoBalancesFoundException(@NonNull final NoBalancesFoundException e) {
        log.error("No balances found exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({NoQuoteFoundException.class})
    public ResponseEntity<String> handleNoQuoteFoundException(@NonNull final NoQuoteFoundException e) {
        log.warn("No quote found exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({QuoteDoesNotBelongToUserException.class})
    public ResponseEntity<String> handleQuoteDoesNotBelongToUserException(@NonNull final QuoteDoesNotBelongToUserException e) {
        log.error("Quote does not belong to user exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({TransferLimitExceeded.class})
    public ResponseEntity<String> handleTransferLimitExceeded(@NonNull final TransferLimitExceeded e) {
        log.info("Transfer limit exceeded exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({WithdrawalLimitExceeded.class})
    public ResponseEntity<String> handleWithdrawalLimitExceeded(@NonNull final WithdrawalLimitExceeded e) {
        log.info("Withdrawal limit exceeded exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({AssertionError.class})
    public ResponseEntity<String> handleAssertionError(@NonNull final AssertionError e) {
        log.error("Assertion error encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleException(@NonNull final Exception e) {
        log.error("Exception encountered with message: {} and cause: {}", e.getMessage(), e.getCause(), e);
        return ResponseEntity.internalServerError().build();
    }
}
