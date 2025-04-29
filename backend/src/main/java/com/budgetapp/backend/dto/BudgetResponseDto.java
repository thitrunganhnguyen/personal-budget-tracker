package com.budgetapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BudgetResponseDto {

    private Long id;
    private String categoryName;
    private BigDecimal initialBudget;
    private BigDecimal adjustedBudget;
    private BigDecimal spentAmount;
    private BigDecimal remainingBudget;
}
