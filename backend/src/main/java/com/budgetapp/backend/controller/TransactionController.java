package com.budgetapp.backend.controller;

import com.budgetapp.backend.dto.TransactionRequestDto;
import com.budgetapp.backend.dto.TransactionResponseDto;
import com.budgetapp.backend.dto.TransactionSummaryDto;
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

    // Get transaction
    @GetMapping
    public List<TransactionResponseDto> getTransactions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        if (year != null && month != null) {
            return transactionService.getTransactionsForMonth(user, year, month);
        } else {
            return transactionService.getAllTransactions(user);
        }
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

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryDto> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal User user) {
        TransactionSummaryDto summary = transactionService.getMonthlySummary(year, month, user);
        return ResponseEntity.ok(summary);
    }

}
