package com.budgetapp.backend.mapper;

import com.budgetapp.backend.dto.CategoryRequestDto;
import com.budgetapp.backend.dto.CategoryResponseDto;
import com.budgetapp.backend.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Map Category → Response DTO
    CategoryResponseDto toDto(Category category);

    // Map Request DTO → Category Entity (id and user must be set manually)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Category toEntity(CategoryRequestDto dto);
}
