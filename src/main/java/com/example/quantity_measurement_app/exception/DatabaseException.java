package com.example.quantity_measurement_app.exception;

/**
 * Custom checked exception for database-related errors.
 * Encapsulates database-specific errors with meaningful messages.
 */
public class DatabaseException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new DatabaseException with the specified detail message.
     */
    public DatabaseException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DatabaseException with the specified detail message and cause.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new DatabaseException with the specified cause.
     */
    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
