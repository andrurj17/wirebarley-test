package com.wirebarley.test.services.transfer.controllers;

import com.wirebarley.test.services.transfer.models.dtos.WithdrawalRequest;
import com.wirebarley.test.services.transfer.services.TransferService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/public/v1/users/{userId}/withdrawals")
@RequiredArgsConstructor
public class WithdrawalController {

    @NonNull
    private final TransferService transferService;

    @PostMapping("")
    public ResponseEntity<Void> withdraw(@PathVariable final long userId,
                                         @RequestBody @NonNull final WithdrawalRequest request) {
        // Withdraw money from the user
        transferService.withdraw(userId, request.amount());
        return ResponseEntity.ok().build();
    }
}
