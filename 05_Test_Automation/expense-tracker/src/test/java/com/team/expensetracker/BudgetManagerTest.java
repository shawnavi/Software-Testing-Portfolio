package com.team.expensetracker;

import static com.team.expensetracker.TestFixtures.amt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Module M3 - Budget management and alerts (Decision Table + BVA on the 80% / 100% thresholds). */
@DisplayName("M3 Budget and alerts")
class BudgetManagerTest {
    private static final YearMonth OCT = YearMonth.of(2026, 10);
    private ExpenseTracker tracker;
    private BudgetManager budgets;

    @BeforeEach
    void setUp() {
        tracker = TestFixtures.tracker();
        budgets = new BudgetManager(tracker);
    }

    private void spend(String amount) {
        tracker.addExpense(amt(amount), Category.FOOD, "Food", LocalDate.of(2026, 10, 12));
    }

    /**
     * Decision table rules R1-R8 (see 02_Test_Case_Design/03_Decision_Table.md).
     * Columns: rule, budget set?, amount spent of a 1000 budget, alerts on?, expected status, alert sent?
     */
    @ParameterizedTest(name = "TC-DT-{0} budgetSet={1} spent={2} alertsOn={3} -> {4}, alert={5}")
    @CsvSource({
            "R1, false, 500,  true,  NO_BUDGET, false",
            "R2, true,  500,  true,  WITHIN,    false",
            "R3, true,  850,  true,  WARNING,   true",
            "R4, true,  1200, true,  EXCEEDED,  true",
            "R5, false, 500,  false, NO_BUDGET, false",
            "R6, true,  500,  false, WITHIN,    false",
            "R7, true,  850,  false, WARNING,   false",
            "R8, true,  1200, false, EXCEEDED,  false"
    })
    void decisionTable(String rule, boolean budgetSet, String spent, boolean alertsOn,
                       BudgetStatus expectedStatus, boolean expectedAlert) {
        if (budgetSet) {
            budgets.setBudget(Category.FOOD, OCT, amt("1000"));
        }
        budgets.setAlertsEnabled(alertsOn);
        spend(spent);
        assertEquals(expectedStatus, budgets.getStatus(Category.FOOD, OCT));
        assertEquals(expectedAlert, budgets.shouldSendAlert(Category.FOOD, OCT));
    }

    @ParameterizedTest(name = "TC-BVA-10 spent {0} of 1000 -> {1}")
    @CsvSource({
            "799.99,  WITHIN",
            "800.00,  WARNING",
            "800.01,  WARNING",
            "999.99,  WARNING",
            "1000.00, WARNING",
            "1000.01, EXCEEDED"
    })
    void thresholdBoundaries(String spent, BudgetStatus expected) {
        budgets.setBudget(Category.FOOD, OCT, amt("1000"));
        spend(spent);
        assertEquals(expected, budgets.getStatus(Category.FOOD, OCT));
    }

    @Test @DisplayName("TC-FN-11 spending in another month does not count")
    void otherMonthIgnored() {
        budgets.setBudget(Category.FOOD, OCT, amt("1000"));
        tracker.addExpense(amt("900"), Category.FOOD, "Food", LocalDate.of(2026, 9, 30));
        assertEquals(BudgetStatus.WITHIN, budgets.getStatus(Category.FOOD, OCT));
    }

    @Test @DisplayName("TC-FN-12 zero or negative budget is rejected")
    void invalidBudget() {
        assertThrows(ValidationException.class, () -> budgets.setBudget(Category.FOOD, OCT, amt("0")));
        assertThrows(ValidationException.class, () -> budgets.setBudget(Category.FOOD, OCT, amt("-100")));
    }

    @Test @DisplayName("TC-FN-13 percentage used is calculated to 2 decimals")
    void percentUsed() {
        budgets.setBudget(Category.FOOD, OCT, amt("3000"));
        spend("1000");
        assertEquals(amt("33.33"), budgets.percentUsed(Category.FOOD, OCT));
    }
}
