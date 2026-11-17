# Functional Test Cases – Modules M2, M3 and M4

## M2 – Manage expenses

| TC ID | Requirement | Steps / input | Expected result | JUnit test |
|---|---|---|---|---|
| TC-FN-01 | R5 | Add 250.00, FOOD, "Groceries", today | Saved with id 1; list has 1 expense | `ExpenseTrackerTest.addExpense` |
| TC-FN-02 | R2 | Add with description "␣␣Bus pass␣␣" | Saved as "Bus pass" | `trimsDescription` |
| TC-FN-03 | R1, R5 | Add amount −1 | Error shown; nothing saved | `invalidNotStored` |
| TC-FN-04 | R5 | Add 100.00 then edit to 120.00 | Amount is 120.00 | `updateExpense` |
| TC-FN-05 | R5 | Edit expense id 99 (does not exist) | Error "Expense 99 not found" | `updateMissing` |
| TC-FN-06 | R5 | Add then delete | Expense no longer found | `deleteExpense` |
| TC-FN-07 | R5 | Delete id 42 (does not exist) | Error "Expense 42 not found" | `deleteMissing` |
| TC-FN-08 | R6 | Add FOOD, TRANSPORT, FOOD; filter FOOD | 2 expenses | `filterByCategory` |
| TC-FN-09 | R7 | Add on 01-10, 15-10, 31-10, 01-11; search 01-10 to 31-10 | 3 expenses (start and end day included) | `dateRangeInclusive` |
| TC-FN-10 | R7 | Search 31-10 to 01-10 | Error "Start date must not be after end date" | `invalidRange` |

## M3 – Budget (extra cases)

| TC ID | Requirement | Steps / input | Expected result | JUnit test |
|---|---|---|---|---|
| TC-FN-11 | R9 | Budget 1000 for Oct; spend 900 on 30-09 | Oct status WITHIN (Sep spending not counted) | `BudgetManagerTest.otherMonthIgnored` |
| TC-FN-12 | R8 | Set budget 0, then −100 | Both rejected | `invalidBudget` |
| TC-FN-13 | R9 | Budget 3000, spend 1000 | 33.33 % used | `percentUsed` |

## M4 – Reports and savings

**Test data (October 2026):** Rent 12,000.00 on 01-10 · Groceries 3,500.50 on 14-10 · Metro 800.00 on 20-10 · Restaurant 1,499.50 on 31-10 · plus 999.00 on 01-11 (next month, must not be counted).

| TC ID | Requirement | Steps / input | Expected result | JUnit test |
|---|---|---|---|---|
| TC-RP-01 | R11 | Monthly total for Oct 2026 | 17,800.00 | `ReportServiceTest.monthlyTotal` |
| TC-RP-02 | R11 | Category breakdown for Oct 2026 | RENT 12,000.00 · FOOD 5,000.00 · TRANSPORT 800.00 | `breakdown` |
| TC-RP-03 | R12 | Savings with income 25,000.00 | 7,200.00 | `savings` |
| TC-RP-04 | R12 | Savings rate with income 25,000.00 | 28.80 % | `savingsRate` |
| TC-RP-05 | R11 | Total for May 2026 (no data) | 0 | `emptyMonth` |
| TC-RP-06 | R12 | Savings with income −1 | Error "Income cannot be negative" | `negativeIncome` |
| TC-RP-07 | R12 | Savings rate with income 0 | 0.00 (no divide-by-zero crash) | `zeroIncome` |

## Count of designed test cases

| Technique | Test cases (JUnit runs) |
|---|---|
| Equivalence Partitioning | 10 |
| Boundary Value Analysis | 23 |
| Decision Table | 8 |
| State Transition | 11 |
| Functional (M2, M3, M4) | 20 |
| **Total** | **72** |

Each test case is one JUnit run (parameterized tests run once per input row), so the 72 designed test cases match the 72 runs in the execution results.
