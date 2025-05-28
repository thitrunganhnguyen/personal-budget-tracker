package com.budgetapp.backend.mapper;

import com.budgetapp.backend.dto.TransactionRequestDto;
import com.budgetapp.backend.dto.TransactionResponseDto;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.Transaction;
import com.budgetapp.backend.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Mapping entity → response DTO
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    TransactionResponseDto toDto(Transaction transaction);

    // Mapping request DTO → entity (excluding User and Category)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    Transaction toEntity(TransactionRequestDto dto);

    // After `toEntity`, you can manually set category and user in the service
    default Transaction toEntityWithRelations(TransactionRequestDto dto, Category category, User user) {
        Transaction transaction = toEntity(dto);
        transaction.setCategory(category);
        transaction.setUser(user);
        return transaction;
    }
}
