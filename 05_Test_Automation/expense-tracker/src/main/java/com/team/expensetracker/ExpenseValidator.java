package com.team.expensetracker;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

/**
 * Business rules for an expense:
 *  - amount: 0.01 to 1,000,000.00 (inclusive), at most 2 decimal places
 *  - description: 1 to 100 characters after trimming spaces
 *  - date: between 01-01-2000 and today (inclusive)
 *  - category: required
 */
public class ExpenseValidator {
    public static final BigDecimal MIN_AMOUNT = new BigDecimal("0.01");
    public static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000.00");
    public static final int MAX_DESCRIPTION = 100;
    public static final LocalDate EARLIEST_DATE = LocalDate.of(2000, 1, 1);

    private final Clock clock;

    public ExpenseValidator(Clock clock) {
        this.clock = clock;
    }

    public void validate(BigDecimal amount, Category category, String description, LocalDate date) {
        if (amount == null) {
            throw new ValidationException("Amount is required");
        }
        if (amount.scale() > 2 && amount.stripTrailingZeros().scale() > 2) {
            throw new ValidationException("Amount can have at most 2 decimal places");
        }
        if (amount.compareTo(MIN_AMOUNT) < 0 || amount.compareTo(MAX_AMOUNT) > 0) {
            throw new ValidationException("Amount must be between 0.01 and 1,000,000.00");
        }
        if (category == null) {
            throw new ValidationException("Category is required");
        }
        if (description == null || description.isBlank()) {
            throw new ValidationException("Description is required");
        }
        if (description.trim().length() > MAX_DESCRIPTION) {
            throw new ValidationException("Description must be at most 100 characters");
        }
        if (date == null) {
            throw new ValidationException("Date is required");
        }
        if (date.isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Date cannot be before 01-01-2000");
        }
        if (date.isAfter(LocalDate.now(clock))) {
            throw new ValidationException("Date cannot be in the future");
        }
    }
}
