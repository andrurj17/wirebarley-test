package com.wirebarley.test.services.quote.controllers;

import com.wirebarley.test.services.quote.models.dtos.CreateQuoteRequest;
import com.wirebarley.test.services.quote.models.dtos.CreateQuoteResponse;
import com.wirebarley.test.services.quote.models.dtos.GetQuoteResponse;
import com.wirebarley.test.services.quote.services.QuoteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1/users/{userId}/quotes")
@RequiredArgsConstructor
public class QuoteController {

    @NonNull
    private final QuoteService quoteService;

    @PostMapping("")
    public ResponseEntity<CreateQuoteResponse> createQuote(@PathVariable final long userId,
                                                           @RequestBody @NonNull final CreateQuoteRequest request) {
        final var quote = quoteService.createQuote(userId, request.toUserId(), request.amount());
        final var response = new CreateQuoteResponse(quote);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{quoteId}")
    public ResponseEntity<GetQuoteResponse> getQuote(@PathVariable final long userId,
                                                     @PathVariable final long quoteId) {
        final var quote = quoteService.getQuote(quoteId, userId);
        final var response = new GetQuoteResponse(quote);
        return ResponseEntity.ok(response);
    }
}
