package com.budgetapp.backend.controller;

import com.budgetapp.backend.dto.CategoryRequestDto;
import com.budgetapp.backend.dto.CategoryResponseDto;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.payload.ApiResponse;
import com.budgetapp.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 1. Create
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @RequestBody @Valid CategoryRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto, user);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // 2. Read (Get all for user)
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesForUser(
            @AuthenticationPrincipal User user) {
        List<CategoryResponseDto> categories = categoryService.getCategoriesForUser(user);
        return ResponseEntity.ok(categories);
    }

    // 3. Update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryRequestDto requestDto,
            @AuthenticationPrincipal User user) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, requestDto, user);
        return ResponseEntity.ok(updatedCategory);
    }

    // 4. Delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User user) {
        categoryService.deleteCategory(categoryId, user);
        return ResponseEntity.ok(new ApiResponse(true, "Category deleted successfully"));
    }
}
