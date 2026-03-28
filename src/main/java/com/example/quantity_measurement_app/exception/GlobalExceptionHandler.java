package com.example.quantity_measurement_app.exception;

import com.example.quantity_measurement_app.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * Global Exception Handler for the REST API
 * Handles all exceptions and returns consistent error responses
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle QuantityMeasurementException
     */
    @ExceptionHandler(QuantityMeasurementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<?>> handleQuantityMeasurementException(
            QuantityMeasurementException ex,
            WebRequest request) {
        log.error("Quantity Measurement Error: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(400, ex.getMessage()));
    }

    /**
     * Handle DatabaseException
     */
    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponseDTO<?>> handleDatabaseException(
            DatabaseException ex,
            WebRequest request) {
        log.error("Database Error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error(500, "Database error: " + ex.getMessage()));
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<?>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        log.error("Invalid Argument: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(400, "Invalid argument: " + ex.getMessage()));
    }

    /**
     * Handle NumberFormatException
     */
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<?>> handleNumberFormatException(
            NumberFormatException ex,
            WebRequest request) {
        log.error("Number Format Error: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(400, "Invalid number format: " + ex.getMessage()));
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponseDTO<?>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected Error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error(500, "An unexpected error occurred: " + ex.getMessage()));
    }
}
