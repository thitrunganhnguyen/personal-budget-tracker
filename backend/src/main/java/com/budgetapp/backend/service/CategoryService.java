package com.budgetapp.backend.service;

import com.budgetapp.backend.dto.CategoryRequestDto;
import com.budgetapp.backend.dto.CategoryResponseDto;
import com.budgetapp.backend.exception.CategoryAlreadyExistsException;
import com.budgetapp.backend.exception.CategoryNotFoundException;
import com.budgetapp.backend.exception.UnauthorizedActionException;
import com.budgetapp.backend.mapper.CategoryMapper;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    // 1. Create
    public CategoryResponseDto createCategory(CategoryRequestDto requestDto, User user) {
        boolean exists = categoryRepository.findByUser(user).stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(requestDto.getName().trim()));

        if (exists) {
            throw new CategoryAlreadyExistsException("Category with the same name already exists");
        }

        Category category = categoryMapper.toEntity(requestDto);
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    // 2. Read
    public List<CategoryResponseDto> getCategoriesForUser(User user) {
        return categoryRepository.findByUser(user).stream()
                .map(c -> {
                    CategoryResponseDto dto = new CategoryResponseDto();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 3. Update
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("Unauthorized to update this category");
        }

        category.setName(requestDto.getName().trim());
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    // 4. Delete
    public void deleteCategory(Long categoryId, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("Unauthorized to delete this category");
        }

        categoryRepository.delete(category);
    }
}
