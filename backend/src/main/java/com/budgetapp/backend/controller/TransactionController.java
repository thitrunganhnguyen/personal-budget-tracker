package com.budgetapp.backend.controller;

import com.budgetapp.backend.dto.TransactionRequestDto;
import com.budgetapp.backend.dto.TransactionResponseDto;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.payload.ApiResponse;
import com.budgetapp.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create a transaction
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody TransactionRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        TransactionResponseDto createdTransaction = transactionService.createTransaction(requestDto, user);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    // Get all transactions for user
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions(
            @AuthenticationPrincipal User user) {
        List<TransactionResponseDto> transactions = transactionService.getAllTransactions(user);
        return ResponseEntity.ok(transactions);
    }

    // Update an existing transaction
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        TransactionResponseDto updatedTransaction = transactionService.updateTransaction(id, requestDto, user);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(id, user);
        return ResponseEntity.ok(new ApiResponse(true, "Transaction deleted successfully"));
    }

}
