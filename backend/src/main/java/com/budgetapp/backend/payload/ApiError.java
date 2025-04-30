package com.budgetapp.backend.payload;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter

public class ApiError {
    private int status;
    private String message;
    private List<String> errors;
    private LocalDateTime timestamp;
    private String path;

    // Constructors

    public ApiError(int status, String message, List<String> errors, String path) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ApiError(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

}
