package com.budgetapp.backend.controller;

import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.service.CategoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestParam String name, @AuthenticationPrincipal User user) {
        return categoryService.createCategory(name, user);
    }

    @GetMapping
    public List<Category> GetCategoriesForUser(@AuthenticationPrincipal User user) {
        return categoryService.getCategoriesForUser(user);
    }
}
