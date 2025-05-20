package com.budgetapp.backend.mapper;

import com.budgetapp.backend.dto.BudgetRequestDto;
import com.budgetapp.backend.dto.BudgetResponseDto;
import com.budgetapp.backend.model.Budget;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    // Create new Budget from request + user + category
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "adjustedBudget", source = "dto.initialBudget") // Initially adjusted = initial
    Budget toEntity(BudgetRequestDto dto, User user, Category category);

    // Update an existing Budget entity from BudgetRequestDto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "adjustedBudget", source = "dto.initialBudget") // Reset adjustedBudget
    void updateEntity(@MappingTarget Budget budget, BudgetRequestDto dto);

    // Map Budget entity → BudgetResponseDto (basic fields)
    @Mapping(source = "category.name", target = "categoryName")
    BudgetResponseDto toDto(Budget budget);

    // 4. Map Budget entity + spentAmount + leftover → BudgetResponseDto (with calculations)
    default BudgetResponseDto toDtoWithSpentAndLeftover(Budget budget, BigDecimal spentAmount, BigDecimal leftover) {
        if (budget == null) {
            return null;
        }
        BudgetResponseDto dto = toDto(budget); // Map basic fields automatically
        dto.setSpentAmount(spentAmount);
        dto.setRemainingBudget(budget.getAdjustedBudget().subtract(spentAmount));
        dto.setLeftoverFromLastMonth(leftover);
        return dto;
    }


}
