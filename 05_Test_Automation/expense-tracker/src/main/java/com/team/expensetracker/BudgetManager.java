package com.team.expensetracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/** Monthly budgets per category, budget status and alert decisions. */
public class BudgetManager {
    private static final BigDecimal WARNING_PERCENT = new BigDecimal("80");
    private static final BigDecimal FULL_PERCENT = new BigDecimal("100");

    private final ExpenseTracker tracker;
    private final Map<String, BigDecimal> budgets = new HashMap<>();
    private boolean alertsEnabled = true;

    public BudgetManager(ExpenseTracker tracker) {
        this.tracker = tracker;
    }

    public void setBudget(Category category, YearMonth month, BigDecimal limit) {
        if (limit == null || limit.signum() <= 0) {
            throw new ValidationException("Budget must be greater than 0");
        }
        budgets.put(key(category, month), limit);
    }

    public void setAlertsEnabled(boolean enabled) {
        this.alertsEnabled = enabled;
    }

    public BigDecimal spent(Category category, YearMonth month) {
        return tracker.findByCategory(category).stream()
                .filter(e -> YearMonth.from(e.date()).equals(month))
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Percentage of the budget used, rounded to 2 decimals. */
    public BigDecimal percentUsed(Category category, YearMonth month) {
        BigDecimal limit = budgets.get(key(category, month));
        if (limit == null) {
            return BigDecimal.ZERO;
        }
        return spent(category, month).multiply(FULL_PERCENT).divide(limit, 2, RoundingMode.HALF_UP);
    }

    public BudgetStatus getStatus(Category category, YearMonth month) {
        if (!budgets.containsKey(key(category, month))) {
            return BudgetStatus.NO_BUDGET;
        }
        // Compare exact amounts. Using the rounded percentage made 799.99/1000 look like 80.00%.
        BigDecimal limit = budgets.get(key(category, month));
        BigDecimal spentTimes100 = spent(category, month).multiply(FULL_PERCENT);
        if (spentTimes100.compareTo(limit.multiply(FULL_PERCENT)) > 0) {
            return BudgetStatus.EXCEEDED;
        }
        if (spentTimes100.compareTo(limit.multiply(WARNING_PERCENT)) >= 0) {
            return BudgetStatus.WARNING;
        }
        return BudgetStatus.WITHIN;
    }

    /** An alert is sent only when alerts are on and the status is WARNING or EXCEEDED. */
    public boolean shouldSendAlert(Category category, YearMonth month) {
        BudgetStatus status = getStatus(category, month);
        return alertsEnabled && (status == BudgetStatus.WARNING || status == BudgetStatus.EXCEEDED);
    }

    private static String key(Category category, YearMonth month) {
        return category + "@" + month;
    }
}
