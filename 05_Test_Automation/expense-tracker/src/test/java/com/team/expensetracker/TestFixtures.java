package com.team.expensetracker;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;

/** Shared setup: the clock is fixed to 10-11-2026 so date tests are repeatable. */
final class TestFixtures {
    static final LocalDate TODAY = LocalDate.of(2026, 11, 10);
    static final Clock CLOCK = Clock.fixed(TODAY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private TestFixtures() {
    }

    static ExpenseValidator validator() {
        return new ExpenseValidator(CLOCK);
    }

    static ExpenseTracker tracker() {
        return new ExpenseTracker(validator());
    }

    static BigDecimal amt(String value) {
        return new BigDecimal(value);
    }
}
