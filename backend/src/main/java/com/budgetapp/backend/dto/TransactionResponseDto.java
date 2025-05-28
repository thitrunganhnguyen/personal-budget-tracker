package com.budgetapp.backend.dto;

import com.budgetapp.backend.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate date;
    private long categoryId;
    private String categoryName;
}
