# Boundary Value Analysis – Modules M1 and M3

Defects often hide at the edges of a range. For each limit we test the value **just below**, **at**, and **just above** it.

## 1. Amount (R1: 0.01 to 1,000,000.00)

| TC ID | Boundary | Input | Expected | JUnit test |
|---|---|---|---|---|
| TC-BVA-01 | min − 0.01 | 0.00 | Rejected | `AmountBVA.belowMin` |
| TC-BVA-02 | min | 0.01 | Accepted | `AmountBVA.atMin` |
| TC-BVA-03 | min + 0.01 | 0.02 | Accepted | `AmountBVA.aboveMin` |
| TC-BVA-04 | max − 0.01 | 999,999.99 | Accepted | `AmountBVA.belowMax` |
| TC-BVA-05 | max | 1,000,000.00 | Accepted | `AmountBVA.atMax` |
| TC-BVA-06 | max + 0.01 | 1,000,000.01 | Rejected | `AmountBVA.aboveMax` |

## 2. Description length (R2: 1 to 100 characters)

| TC ID | Boundary | Input length | Expected | JUnit test |
|---|---|---|---|---|
| TC-BVA-07a | min | 1 | Accepted | `Description.validLengths[1]` |
| TC-BVA-07b | min + 1 | 2 | Accepted | `Description.validLengths[2]` |
| TC-BVA-07c | max − 1 | 99 | Accepted | `Description.validLengths[3]` |
| TC-BVA-07d | max | 100 | Accepted | `Description.validLengths[4]` |
| TC-BVA-08 | max + 1 | 101 | Rejected | `Description.tooLong` |

(min − 1 = 0 characters is TC-EP-07.)

## 3. Expense date (R3: 01-01-2000 to today; today = 10-11-2026 in the tests)

| TC ID | Boundary | Input | Expected | JUnit test |
|---|---|---|---|---|
| TC-BVA-09a | min − 1 day | 31-12-1999 | Rejected | `dateBoundaries[1]` |
| TC-BVA-09b | min | 01-01-2000 | Accepted | `dateBoundaries[2]` |
| TC-BVA-09c | min + 1 day | 02-01-2000 | Accepted | `dateBoundaries[3]` |
| TC-BVA-09d | max − 1 day | 09-11-2026 | Accepted | `dateBoundaries[4]` |
| TC-BVA-09e | max (today) | 10-11-2026 | Accepted | `dateBoundaries[5]` |
| TC-BVA-09f | max + 1 day | 11-11-2026 | Rejected | `dateBoundaries[6]` |

## 4. Budget thresholds (R9: Warning from 80 %, Exceeded above 100 %) – budget = 1000.00

| TC ID | Boundary | Spent | Expected status | JUnit test |
|---|---|---|---|---|
| TC-BVA-10a | 80 % − 0.01 | 799.99 | WITHIN | `thresholdBoundaries[1]` |
| TC-BVA-10b | 80 % | 800.00 | WARNING | `thresholdBoundaries[2]` |
| TC-BVA-10c | 80 % + 0.01 | 800.01 | WARNING | `thresholdBoundaries[3]` |
| TC-BVA-10d | 100 % − 0.01 | 999.99 | WARNING | `thresholdBoundaries[4]` |
| TC-BVA-10e | 100 % | 1000.00 | WARNING | `thresholdBoundaries[5]` |
| TC-BVA-10f | 100 % + 0.01 | 1000.01 | EXCEEDED | `thresholdBoundaries[6]` |
