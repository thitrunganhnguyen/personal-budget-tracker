package com.budgetapp.backend.dto;

import com.budgetapp.backend.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate date;
    private String categoryName;
}
