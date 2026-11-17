package com.team.expensetracker;

import java.math.BigDecimal;
import java.time.LocalDate;

/** A single expense entry. */
public record Expense(int id, BigDecimal amount, Category category, String description, LocalDate date) {
}
