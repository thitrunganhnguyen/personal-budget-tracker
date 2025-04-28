package com.budgetapp.backend.service;

import com.budgetapp.backend.dto.CategoryResponseDto;
import com.budgetapp.backend.exception.CategoryAlreadyExistsException;
import com.budgetapp.backend.exception.CategoryNotFoundException;
import com.budgetapp.backend.exception.UnauthorizedActionException;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 1. Create
    public void createCategory(String name, User user) {
        boolean exists = categoryRepository.findByUser(user).stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name.trim()));

        if (exists) {
            throw new CategoryAlreadyExistsException("Category with the same name already exists");
        }

        Category category = new Category();
        category.setName(name.trim());
        category.setUser(user);
        categoryRepository.save(category);
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
    public void updateCategory(Long categoryId, String newName, User user) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("Unauthorized to update this category");
        }

        category.setName(newName.trim());
        categoryRepository.save(category);
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
