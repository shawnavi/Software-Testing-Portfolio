package com.team.expensetracker;

import static com.team.expensetracker.TestFixtures.amt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Module M4 - Reports and savings. */
@DisplayName("M4 Reports and savings")
class ReportServiceTest {
    private static final YearMonth OCT = YearMonth.of(2026, 10);
    private ExpenseTracker tracker;
    private ReportService reports;

    @BeforeEach
    void setUp() {
        tracker = TestFixtures.tracker();
        reports = new ReportService(tracker);
        tracker.addExpense(amt("12000.00"), Category.RENT, "October rent", LocalDate.of(2026, 10, 1));
        tracker.addExpense(amt("3500.50"), Category.FOOD, "Groceries", LocalDate.of(2026, 10, 14));
        tracker.addExpense(amt("1499.50"), Category.FOOD, "Restaurant", LocalDate.of(2026, 10, 31));
        tracker.addExpense(amt("800.00"), Category.TRANSPORT, "Metro card", LocalDate.of(2026, 10, 20));
        tracker.addExpense(amt("999.00"), Category.FOOD, "Next month", LocalDate.of(2026, 11, 1));
    }

    @Test @DisplayName("TC-RP-01 monthly total includes the first and last day of the month")
    void monthlyTotal() {
        assertEquals(amt("17800.00"), reports.monthlyTotal(OCT));
    }

    @Test @DisplayName("TC-RP-02 category breakdown adds up each category")
    void breakdown() {
        Map<Category, BigDecimal> map = reports.categoryBreakdown(OCT);
        assertEquals(amt("12000.00"), map.get(Category.RENT));
        assertEquals(amt("5000.00"), map.get(Category.FOOD));
        assertEquals(amt("800.00"), map.get(Category.TRANSPORT));
    }

    @Test @DisplayName("TC-RP-03 savings = income - monthly total")
    void savings() {
        assertEquals(amt("7200.00"), reports.savings(amt("25000.00"), OCT));
    }

    @Test @DisplayName("TC-RP-04 savings rate is a percentage of income")
    void savingsRate() {
        assertEquals(amt("28.80"), reports.savingsRate(amt("25000.00"), OCT));
    }

    @Test @DisplayName("TC-RP-05 month with no expenses has total 0")
    void emptyMonth() {
        assertEquals(0, reports.monthlyTotal(YearMonth.of(2026, 5)).compareTo(BigDecimal.ZERO));
    }

    @Test @DisplayName("TC-RP-06 negative income is rejected")
    void negativeIncome() {
        assertThrows(ValidationException.class, () -> reports.savings(amt("-1"), OCT));
    }

    @Test @DisplayName("TC-RP-07 zero income gives savings rate 0.00 (no divide-by-zero)")
    void zeroIncome() {
        assertEquals(amt("0.00"), reports.savingsRate(BigDecimal.ZERO, OCT));
    }
}
