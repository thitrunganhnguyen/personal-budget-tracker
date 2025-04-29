package com.budgetapp.backend.controller;

import com.budgetapp.backend.dto.BudgetRequestDto;
import com.budgetapp.backend.dto.BudgetResponseDto;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // 1. Create or Update Budget
    @PostMapping
    public ResponseEntity<BudgetResponseDto> createOrUpdateBudget(
            @RequestBody @Valid BudgetRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        BudgetResponseDto response = budgetService.createOrUpdateBudget(requestDto, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponseDto>> getBudgetsForMonth(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal User user) {
        List<BudgetResponseDto> budgets = budgetService.getBudgetsForMonth(year, month, user);
        return ResponseEntity.ok(budgets);
    }

}
