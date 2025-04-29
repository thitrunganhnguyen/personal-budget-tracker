package com.budgetapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class YearlyBudgetSummaryDto {
    private int month;
    private List<CategoryBudgetSummaryDto> categories = new ArrayList<>();

    private BigDecimal totalInitialBudget = BigDecimal.ZERO;
    private BigDecimal totalAdjustedBudget = BigDecimal.ZERO;
    private BigDecimal totalSpent = BigDecimal.ZERO;
    private BigDecimal totalRemaining = BigDecimal.ZERO;
}
