package com.budgetapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CategoryBudgetSummaryDto {
    private String categoryName;
    private BigDecimal initialBudget = BigDecimal.ZERO;
    private BigDecimal adjustedBudget = BigDecimal.ZERO;
    private BigDecimal spent = BigDecimal.ZERO;
    private BigDecimal remaining = BigDecimal.ZERO;
}
