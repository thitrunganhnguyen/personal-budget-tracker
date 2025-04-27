package com.budgetapp.backend.service;

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
        Category category = new Category();
        category.setName(name);
        category.setUser(user);
        categoryRepository.save(category);
    }

    public List<Category> getCategoriesForUser(User user) {
        return categoryRepository.findByUser(user);
    }
}
