package com.team.expensetracker;

/** Thrown when user input breaks a business rule. */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
