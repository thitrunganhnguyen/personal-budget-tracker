package com.budgetapp.backend.service;

import com.budgetapp.backend.dto.BudgetRequestDto;
import com.budgetapp.backend.dto.BudgetResponseDto;
import com.budgetapp.backend.dto.CategoryBudgetSummaryDto;
import com.budgetapp.backend.dto.YearlyBudgetSummaryDto;
import com.budgetapp.backend.exception.CategoryNotFoundException;
import com.budgetapp.backend.mapper.BudgetMapper;
import com.budgetapp.backend.model.Budget;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.repository.BudgetRepository;
import com.budgetapp.backend.repository.CategoryRepository;
import com.budgetapp.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetMapper budgetMapper;

    public BudgetService(BudgetRepository budgetRepository,
                         CategoryRepository categoryRepository,
                         TransactionRepository transactionRepository,
                         BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.budgetMapper = budgetMapper;
    }

    public BudgetResponseDto createOrUpdateBudget(BudgetRequestDto requestDto, User user) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Optional<Budget> optionalBudget = budgetRepository.findByUserAndCategoryAndYearAndMonth(
                user, category, requestDto.getYear(), requestDto.getMonth());

        Budget budget;
        if (optionalBudget.isPresent()) {
            // Update existing Budget
            budget = optionalBudget.get();
            budgetMapper.updateEntity(budget, requestDto);
        } else {
            // Create new Budget
            budget = budgetMapper.toEntity(requestDto, user, category);
        }

        Budget savedBudget = budgetRepository.save(budget);

        return budgetMapper.toDtoWithSpentAndLeftover(savedBudget, BigDecimal.ZERO, BigDecimal.ZERO); // No spent yet when creating
    }


    public List<BudgetResponseDto> getBudgetsForMonth(int year, int month, User user) {
        // Calculate previous month
        int prevMonth = month - 1;
        int prevYear = year;

        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear = year - 1;
        }
        List<Budget> budgets = budgetRepository.findAllByUserAndYearAndMonth(user, year, month);
        List<Budget> previousBudgets = budgetRepository.findAllByUserAndYearAndMonth(user, prevYear, prevMonth);

        List<Budget> combined = new ArrayList<>();
        combined.addAll(budgets);
        combined.addAll(previousBudgets);

        Map<String, Budget> budgetMap = combined.stream()
                .collect(Collectors.toMap(
                        b -> b.getCategory().getId() + "-" + b.getYear() + "-" + b.getMonth(),
                        b -> b
                ));

        return budgets.stream()
                .map(budget -> enrichBudget(budget, user, budgetMap))
                .collect(Collectors.toList());
    }

    public List<BudgetResponseDto> getAllBudgets(User user) {
        List<Budget> allBudgets = budgetRepository.findAllByUser(user);

        // Pre-map for fast previous month lookup
        Map<String, Budget> allBudgetsMap = allBudgets.stream()
                .collect(Collectors.toMap(
                        b -> b.getCategory().getId() + "-" + b.getYear() + "-" + b.getMonth(),
                        b -> b
                ));
        return allBudgets.stream()
                .map(budget -> enrichBudget(budget, user, allBudgetsMap))
                .collect(Collectors.toList());
    }

    private BudgetResponseDto enrichBudget(Budget budget, User user, Map<String, Budget> allBudgetsMap) {
        int year = budget.getYear();
        int month = budget.getMonth();
        Category category = budget.getCategory();

        // Calculate previous month
        int prevMonth = month - 1;
        int prevYear = year;

        if (prevMonth == 0) {
            prevMonth = 12;
            prevYear = year - 1;
        }

        String prevKey = category.getId() + "-" + prevYear + "-" + prevMonth;
        Budget previousBudget= allBudgetsMap.get(prevKey);

        BigDecimal leftover = BigDecimal.ZERO;

        if (previousBudget != null) {
            BigDecimal lastMonthSpent = transactionRepository.sumExpensesForCategoryAndMonth(user, category, prevMonth, prevYear);
            leftover = previousBudget.getAdjustedBudget().subtract(lastMonthSpent);

            BigDecimal expectedAdjusted = budget.getInitialBudget().add(leftover);
            if (budget.getAdjustedBudget().compareTo(expectedAdjusted) != 0) {
                budget.setAdjustedBudget(expectedAdjusted);
                budgetRepository.save(budget);
            }
        }

        // Calculate this month's spending
        BigDecimal spentAmount = transactionRepository.sumExpensesForCategoryAndMonth(
                user, category, year, month
        );

        return budgetMapper.toDtoWithSpentAndLeftover(budget, spentAmount, leftover);
    }

    public List<YearlyBudgetSummaryDto> getYearlyBudgetSummary(int year, User user) {
        List<YearlyBudgetSummaryDto> yearlySummary = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            List<Budget> budgets = budgetRepository.findAllByUserAndYearAndMonth(user, year, month);

            YearlyBudgetSummaryDto monthSummary = new YearlyBudgetSummaryDto();
            monthSummary.setMonth(month);

            BigDecimal initialTotal = BigDecimal.ZERO;
            BigDecimal adjustedTotal = BigDecimal.ZERO;
            BigDecimal spentTotal = BigDecimal.ZERO;

            for (Budget budget : budgets) {
                BigDecimal spentAmount = transactionRepository.sumExpensesForCategoryAndMonth(
                        user, budget.getCategory(), year, month
                );

                CategoryBudgetSummaryDto categoryDto = new CategoryBudgetSummaryDto();
                categoryDto.setCategoryName(budget.getCategory().getName());
                categoryDto.setInitialBudget(budget.getInitialBudget());
                categoryDto.setAdjustedBudget(budget.getAdjustedBudget());
                categoryDto.setSpent(spentAmount);
                categoryDto.setRemaining(budget.getAdjustedBudget().subtract(spentAmount));

                monthSummary.getCategories().add(categoryDto);

                // Add to totals
                initialTotal = initialTotal.add(budget.getInitialBudget());
                adjustedTotal = adjustedTotal.add(budget.getAdjustedBudget());
                spentTotal = spentTotal.add(spentAmount);
            }

            // Set totals
            monthSummary.setTotalInitialBudget(initialTotal);
            monthSummary.setTotalAdjustedBudget(adjustedTotal);
            monthSummary.setTotalSpent(spentTotal);
            monthSummary.setTotalRemaining(adjustedTotal.subtract(spentTotal));

            yearlySummary.add(monthSummary);
        }

        return yearlySummary;
    }


}
