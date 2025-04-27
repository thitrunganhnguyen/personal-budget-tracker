package com.budgetapp.backend.repository;

import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
