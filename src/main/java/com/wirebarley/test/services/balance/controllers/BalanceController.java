package com.wirebarley.test.services.balance.controllers;

import com.wirebarley.test.services.balance.models.dtos.TopUpRequest;
import com.wirebarley.test.services.balance.services.BalanceService;
import com.wirebarley.test.services.balance.models.dtos.GetBalancesResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1/users/{userId}/balances")
@RequiredArgsConstructor
public class BalanceController {

    @NonNull
    private final BalanceService balanceService;

    // TODO: Use JWT and get user id form it rather than from the path.
    @GetMapping("")
    public ResponseEntity<GetBalancesResponse> getBalances(@PathVariable final long userId) {
        // Transfer money from one user to another
        final var balances = balanceService.getBalances(userId);
        final var response = new GetBalancesResponse(balances);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/topUp")
    public ResponseEntity<Void> topUp(@PathVariable final long userId,
                                      @RequestBody final TopUpRequest request) {
        balanceService.topUp(userId, request.amount());
        return ResponseEntity.ok().build();
    }
}

