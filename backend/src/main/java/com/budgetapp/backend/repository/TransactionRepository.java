package com.budgetapp.backend.repository;

import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.Transaction;
import com.budgetapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = 'INCOME' AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    BigDecimal getTotalIncomeForMonth(@Param("user") User user, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user = :user AND t.type = 'EXPENSE' AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    BigDecimal getTotalExpenseForMonth(@Param("user") User user, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user = :user AND t.category = :category AND t.type = 'EXPENSE' AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    BigDecimal sumExpensesForCategoryAndMonth(
            @Param("user") User user,
            @Param("category") Category category,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.user = :user " +
            "AND YEAR(t.date) = :year " +
            "AND MONTH(t.date) = :month")
    List<Transaction> findByUserAndMonthYear(@Param("user") User user,
                                             @Param("year") int year,
                                             @Param("month") int month);


}
