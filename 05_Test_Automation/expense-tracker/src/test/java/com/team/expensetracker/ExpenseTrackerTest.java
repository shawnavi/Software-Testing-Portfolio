package com.team.expensetracker;

import static com.team.expensetracker.TestFixtures.TODAY;
import static com.team.expensetracker.TestFixtures.amt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Module M2 - Manage expenses (add, edit, delete, search). */
@DisplayName("M2 Manage expenses")
class ExpenseTrackerTest {
    private ExpenseTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = TestFixtures.tracker();
    }

    @Test @DisplayName("TC-FN-01 add a valid expense stores it with a new id")
    void addExpense() {
        Expense e = tracker.addExpense(amt("250.00"), Category.FOOD, "Groceries", TODAY);
        assertEquals(1, e.id());
        assertEquals(1, tracker.getAll().size());
    }

    @Test @DisplayName("TC-FN-02 description is saved without leading and trailing spaces")
    void trimsDescription() {
        Expense e = tracker.addExpense(amt("40.00"), Category.TRANSPORT, "  Bus pass  ", TODAY);
        assertEquals("Bus pass", e.description());
    }

    @Test @DisplayName("TC-FN-03 invalid expense is not stored")
    void invalidNotStored() {
        assertThrows(ValidationException.class, () -> tracker.addExpense(amt("-1"), Category.FOOD, "Bad", TODAY));
        assertTrue(tracker.getAll().isEmpty());
    }

    @Test @DisplayName("TC-FN-04 edit an expense changes its amount")
    void updateExpense() {
        Expense e = tracker.addExpense(amt("100.00"), Category.SHOPPING, "Shoes", TODAY);
        tracker.updateExpense(e.id(), amt("120.00"), Category.SHOPPING, "Shoes", TODAY);
        assertEquals(amt("120.00"), tracker.findById(e.id()).orElseThrow().amount());
    }

    @Test @DisplayName("TC-FN-05 edit a missing expense shows an error")
    void updateMissing() {
        assertThrows(ValidationException.class,
                () -> tracker.updateExpense(99, amt("1"), Category.FOOD, "X", TODAY));
    }

    @Test @DisplayName("TC-FN-06 delete an expense removes it")
    void deleteExpense() {
        Expense e = tracker.addExpense(amt("75.00"), Category.HEALTH, "Medicine", TODAY);
        tracker.deleteExpense(e.id());
        assertTrue(tracker.findById(e.id()).isEmpty());
    }

    @Test @DisplayName("TC-FN-07 delete a missing expense shows an error")
    void deleteMissing() {
        assertThrows(ValidationException.class, () -> tracker.deleteExpense(42));
    }

    @Test @DisplayName("TC-FN-08 filter by category returns only that category")
    void filterByCategory() {
        tracker.addExpense(amt("10"), Category.FOOD, "Tea", TODAY);
        tracker.addExpense(amt("20"), Category.TRANSPORT, "Auto", TODAY);
        tracker.addExpense(amt("30"), Category.FOOD, "Dinner", TODAY);
        assertEquals(2, tracker.findByCategory(Category.FOOD).size());
    }

    @Test @DisplayName("TC-FN-09 date range search includes both the start and end dates")
    void dateRangeInclusive() {
        tracker.addExpense(amt("10"), Category.FOOD, "Start day", LocalDate.of(2026, 10, 1));
        tracker.addExpense(amt("20"), Category.FOOD, "Middle", LocalDate.of(2026, 10, 15));
        tracker.addExpense(amt("30"), Category.FOOD, "End day", LocalDate.of(2026, 10, 31));
        tracker.addExpense(amt("40"), Category.FOOD, "Next month", LocalDate.of(2026, 11, 1));
        List<Expense> found = tracker.findBetween(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));
        assertEquals(3, found.size());
    }

    @Test @DisplayName("TC-FN-10 date range with start after end shows an error")
    void invalidRange() {
        assertThrows(ValidationException.class,
                () -> tracker.findBetween(LocalDate.of(2026, 10, 31), LocalDate.of(2026, 10, 1)));
    }
}
