# Equivalence Partitioning – Module M1 (Expense input validation)

Equivalence Partitioning splits each input into groups (classes) that the system should treat the same way. One value from each class is enough to test the whole class.

## Equivalence classes

| Input | Valid classes | Invalid classes |
|---|---|---|
| Amount (R1) | V1: 0.01 ≤ amount ≤ 1,000,000.00 with ≤ 2 decimals | I1: amount < 0 · I2: amount = 0 · I3: amount > 1,000,000.00 · I4: more than 2 decimals · I5: empty |
| Description (R2) | V2: 1–100 characters | I6: empty · I7: only spaces · I8: more than 100 characters |
| Date (R3) | V3: 01-01-2000 to today | I9: before 01-01-2000 · I10: in the future · I11: empty |
| Category (R4) | V4: one of FOOD, TRANSPORT, RENT, UTILITIES, ENTERTAINMENT, HEALTH, SHOPPING, EDUCATION, OTHER | I12: empty |

## Test cases

Base valid values used when another field is being tested: amount 10.00, category FOOD, description "Lunch", date 10-11-2026 (today).

| TC ID | Class | Input | Expected result | JUnit test |
|---|---|---|---|---|
| TC-EP-01 | V1 | Amount 500.00 | Accepted | `ExpenseValidatorTest.AmountEP.validPartition` |
| TC-EP-02 | I1 | Amount −50.00 | Rejected: "Amount must be between 0.01 and 1,000,000.00" | `negativePartition` |
| TC-EP-03 | I2 | Amount 0 | Rejected | `zeroPartition` |
| TC-EP-04 | I3 | Amount 2,500,000.00 | Rejected | `aboveMaxPartition` |
| TC-EP-05 | I4 | Amount 10.555 | Rejected: "at most 2 decimal places" | `tooManyDecimals` |
| TC-EP-06 | I5 | Amount empty | Rejected: "Amount is required" | `nullAmount` |
| TC-EP-07 | I6 | Description "" | Rejected: "Description is required" | `Description.empty` |
| TC-EP-08 | I7 | Description "␣␣␣␣" (4 spaces) | Rejected: "Description is required" | `Description.whitespaceOnly` |
| TC-EP-09 | I12 | Category empty | Rejected: "Category is required" | `DateAndCategory.nullCategory` |
| TC-EP-10 | I11 | Date empty | Rejected: "Date is required" | `DateAndCategory.nullDate` |

Classes V2, V3, V4, I8, I9, I10 are covered by the boundary value test cases in [02_Boundary_Value_Analysis.md](02_Boundary_Value_Analysis.md).
