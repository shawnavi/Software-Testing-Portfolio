package com.team.expensetracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.Map;

/** Monthly summaries: total spend, category breakdown and savings. */
public class ReportService {
    private final ExpenseTracker tracker;

    public ReportService(ExpenseTracker tracker) {
        this.tracker = tracker;
    }

    public BigDecimal monthlyTotal(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return tracker.findBetween(start, end).stream()
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Category, BigDecimal> categoryBreakdown(YearMonth month) {
        Map<Category, BigDecimal> result = new EnumMap<>(Category.class);
        tracker.findBetween(month.atDay(1), month.atEndOfMonth())
                .forEach(e -> result.merge(e.category(), e.amount(), BigDecimal::add));
        return result;
    }

    public BigDecimal savings(BigDecimal income, YearMonth month) {
        if (income == null || income.signum() < 0) {
            throw new ValidationException("Income cannot be negative");
        }
        return income.subtract(monthlyTotal(month));
    }

    /** Savings as a percentage of income. Returns 0 when income is 0. */
    public BigDecimal savingsRate(BigDecimal income, YearMonth month) {
        if (income.signum() == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return savings(income, month).multiply(new BigDecimal("100")).divide(income, 2, RoundingMode.HALF_UP);
    }
}
