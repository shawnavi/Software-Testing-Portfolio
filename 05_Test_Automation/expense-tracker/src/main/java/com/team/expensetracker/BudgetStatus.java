package com.team.expensetracker;

/**
 * NO_BUDGET - no budget set for the category and month
 * WITHIN    - spent less than 80% of the budget
 * WARNING   - spent 80% to 100% of the budget (inclusive)
 * EXCEEDED  - spent more than 100% of the budget
 */
public enum BudgetStatus {
    NO_BUDGET, WITHIN, WARNING, EXCEEDED
}
