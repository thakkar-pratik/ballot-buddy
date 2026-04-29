package com.ballotbuddy.exception;

import com.ballotbuddy.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler providing standardized error DTOs.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors.
     * @param ex The exception.
     * @return ErrorResponse with field-level details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .errorCode("VALIDATION_ERROR")
                .message("Input validation failed")
                .details(errors)
                .build());
    }

    /**
     * Handles all uncaught exceptions.
     * @param ex The exception.
     * @return Standardized ErrorResponse.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(Map.of("technicalMessage", ex.getMessage()))
                .build());
    }
}
