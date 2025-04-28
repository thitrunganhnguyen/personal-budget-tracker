package com.budgetapp.backend.service;

import com.budgetapp.backend.exception.CategoryAlreadyExistsException;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, User user) {
        // Check if user already has a category with this name
        List<Category> existingCategories = categoryRepository.findByUser(user);
        boolean nameAlreadyExists = existingCategories.stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(name.trim()));
        if (nameAlreadyExists) {
            throw new CategoryAlreadyExistsException("Category with the same name already exists");
        }
        Category category = new Category();
        category.setName(name);
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesForUser(User user) {
        return categoryRepository.findByUser(user);
    }
}
