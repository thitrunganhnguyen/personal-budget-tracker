package com.budgetapp.backend.repository;

import com.budgetapp.backend.model.Budget;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserAndCategoryAndYearAndMonth(User user, Category category, int year, int month);

    List<Budget> findAllByUserAndYearAndMonth(User user, int year, int month);


    List<Budget> findAllByUser(User user);
}
