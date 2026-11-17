package com.team.expensetracker;

import static com.team.expensetracker.TestFixtures.TODAY;
import static com.team.expensetracker.TestFixtures.amt;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Module M1 - Add Expense input validation (Equivalence Partitioning + Boundary Value Analysis). */
@DisplayName("M1 Expense input validation")
class ExpenseValidatorTest {
    private final ExpenseValidator validator = TestFixtures.validator();

    private void validAmount(String amount) {
        assertDoesNotThrow(() -> validator.validate(amt(amount), Category.FOOD, "Lunch", TODAY));
    }

    private void invalidAmount(String amount) {
        assertThrows(ValidationException.class, () -> validator.validate(amt(amount), Category.FOOD, "Lunch", TODAY));
    }

    @Nested
    @DisplayName("Amount - Equivalence Partitioning")
    class AmountEP {
        @Test @DisplayName("TC-EP-01 valid amount 500.00 is accepted")
        void validPartition() { validAmount("500.00"); }

        @Test @DisplayName("TC-EP-02 negative amount -50.00 is rejected")
        void negativePartition() { invalidAmount("-50.00"); }

        @Test @DisplayName("TC-EP-03 zero amount is rejected")
        void zeroPartition() { invalidAmount("0"); }

        @Test @DisplayName("TC-EP-04 amount above maximum 2,500,000 is rejected")
        void aboveMaxPartition() { invalidAmount("2500000.00"); }

        @Test @DisplayName("TC-EP-05 amount with 3 decimal places 10.555 is rejected")
        void tooManyDecimals() { invalidAmount("10.555"); }

        @Test @DisplayName("TC-EP-06 missing amount is rejected")
        void nullAmount() {
            assertThrows(ValidationException.class, () -> validator.validate(null, Category.FOOD, "Lunch", TODAY));
        }
    }

    @Nested
    @DisplayName("Amount - Boundary Value Analysis (min 0.01, max 1,000,000.00)")
    class AmountBVA {
        @Test @DisplayName("TC-BVA-01 just below minimum 0.00 is rejected")
        void belowMin() { invalidAmount("0.00"); }

        @Test @DisplayName("TC-BVA-02 minimum 0.01 is accepted")
        void atMin() { validAmount("0.01"); }

        @Test @DisplayName("TC-BVA-03 just above minimum 0.02 is accepted")
        void aboveMin() { validAmount("0.02"); }

        @Test @DisplayName("TC-BVA-04 just below maximum 999,999.99 is accepted")
        void belowMax() { validAmount("999999.99"); }

        @Test @DisplayName("TC-BVA-05 maximum 1,000,000.00 is accepted")
        void atMax() { validAmount("1000000.00"); }

        @Test @DisplayName("TC-BVA-06 just above maximum 1,000,000.01 is rejected")
        void aboveMax() { invalidAmount("1000000.01"); }
    }

    @Nested
    @DisplayName("Description - EP and BVA (1 to 100 characters)")
    class Description {
        @Test @DisplayName("TC-EP-07 empty description is rejected")
        void empty() {
            assertThrows(ValidationException.class, () -> validator.validate(amt("10"), Category.FOOD, "", TODAY));
        }

        @Test @DisplayName("TC-EP-08 whitespace-only description is rejected")
        void whitespaceOnly() {
            assertThrows(ValidationException.class, () -> validator.validate(amt("10"), Category.FOOD, "    ", TODAY));
        }

        @ParameterizedTest(name = "TC-BVA-07 description of {0} characters is accepted")
        @ValueSource(ints = {1, 2, 99, 100})
        void validLengths(int length) {
            assertDoesNotThrow(() -> validator.validate(amt("10"), Category.FOOD, "a".repeat(length), TODAY));
        }

        @Test @DisplayName("TC-BVA-08 description of 101 characters is rejected")
        void tooLong() {
            assertThrows(ValidationException.class,
                    () -> validator.validate(amt("10"), Category.FOOD, "a".repeat(101), TODAY));
        }
    }

    @Nested
    @DisplayName("Date and category - EP and BVA")
    class DateAndCategory {
        @ParameterizedTest(name = "TC-BVA-09 date {0} gives valid={1}")
        @CsvSource({
                "1999-12-31, false",
                "2000-01-01, true",
                "2000-01-02, true",
                "2026-11-09, true",
                "2026-11-10, true",
                "2026-11-11, false"
        })
        void dateBoundaries(LocalDate date, boolean valid) {
            if (valid) {
                assertDoesNotThrow(() -> validator.validate(amt("10"), Category.FOOD, "Lunch", date));
            } else {
                assertThrows(ValidationException.class, () -> validator.validate(amt("10"), Category.FOOD, "Lunch", date));
            }
        }

        @Test @DisplayName("TC-EP-09 missing category is rejected")
        void nullCategory() {
            assertThrows(ValidationException.class, () -> validator.validate(amt("10"), null, "Lunch", TODAY));
        }

        @Test @DisplayName("TC-EP-10 missing date is rejected")
        void nullDate() {
            assertThrows(ValidationException.class, () -> validator.validate(amt("10"), Category.FOOD, "Lunch", null));
        }
    }
}
