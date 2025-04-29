package com.budgetapp.backend.service;

import com.budgetapp.backend.dto.TransactionRequestDto;
import com.budgetapp.backend.dto.TransactionResponseDto;
import com.budgetapp.backend.exception.CategoryNotFoundException;
import com.budgetapp.backend.exception.TransactionNotFoundException;
import com.budgetapp.backend.exception.UnauthorizedActionException;
import com.budgetapp.backend.mapper.TransactionMapper;
import com.budgetapp.backend.model.Category;
import com.budgetapp.backend.model.Transaction;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.repository.CategoryRepository;
import com.budgetapp.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionResponseDto createTransaction(TransactionRequestDto dto, User user) {
        // Find category
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // Verify ownership
        if (!category.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not own this category");
        }

        // Map DTO → Entity
        Transaction transaction = transactionMapper.toEntityWithRelations(dto, category, user);

        // Save
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Map Entity → Response DTO
        return transactionMapper.toDto(savedTransaction);
    }

    public List<TransactionResponseDto> getAllTransactions(User user) {
        List<Transaction> transactions = transactionRepository.findByUser(user);
        return transactions.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto dto, User user) {
        // Find existing transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        // Verify ownership
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not own this transaction");
        }

        // Find category (because it might change)
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // Verify ownership of new category
        if (!category.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not own this category");
        }

        // Update fields
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());
        transaction.setCategory(category);

        // Save
        Transaction updatedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toDto(updatedTransaction);
    }

    public void deleteTransaction(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("You do not own this transaction");
        }

        transactionRepository.delete(transaction);
    }

}
