package com.wirebarley.test.services.transfer.controllers;

import com.wirebarley.test.services.transfer.services.TransferService;
import com.wirebarley.test.services.transfer.models.dtos.TransferRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1/users/{userId}/transfers")
@RequiredArgsConstructor
public class TransferController {

    @NonNull
    private final TransferService transferService;

    @PostMapping("")
    public ResponseEntity<Void> transfer(@PathVariable final long userId,
                                         @RequestBody @NonNull final TransferRequest request) {
        // Transfer money from one user to another
        transferService.transfer(userId, request.quoteId());
        return ResponseEntity.ok().build();
    }
}
