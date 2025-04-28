package com.budgetapp.backend.exception;

import com.budgetapp.backend.payload.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle validation errors (e.g., @NotBlank)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors,
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    // 2. Handle Invalid Credentials (login error)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // 3. Handle Category Already Exists (duplicate category name)
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 4. Handle Category Not Found
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(CategoryNotFoundException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 5. Handle Unauthorized Actions
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiError> handleUnauthorizedAction(UnauthorizedActionException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 6. Handle all other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong",
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
