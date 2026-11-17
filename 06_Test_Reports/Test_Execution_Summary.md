# Test Execution Summary Report

**Project:** Expense Tracking and Personal Finance Management System
**Report ID:** TSR-ETPFMS-01 | **Date:** 10-11-2026 | **Prepared by:** Shawn Avinash Robin (Test lead)

## 1. Purpose
This report sums up the testing of builds v1.0 and v1.1 against the [test plan](../01_Test_Plan/Test_Plan.md) and says whether the exit criteria are met.

## 2. Test cycles

| Cycle | Build | Date | Run | Passed | Failed | Pass rate | Evidence |
|---|---|---|---|---|---|---|---|
| 1 – first run | v1.0 | 10-11-2026 | 72 | 59 | 13 | 81.9 % | [Test_Execution_v1.0.md](../03_Test_Execution/Test_Execution_v1.0.md) |
| 2 – retest + regression | v1.1 | 10-11-2026 | 72 | **72** | **0** | **100 %** | [run_log_v1.1_retest.txt](run_log_v1.1_retest.txt) · [surefire_v1.1/](surefire_v1.1) |

## 3. Results by technique (cycle 1 → cycle 2)

| Technique | Test cases | Failed in v1.0 | Failed in v1.1 | Defects found |
|---|---|---|---|---|
| Equivalence Partitioning | 10 | 1 | 0 | DEF-002 |
| Boundary Value Analysis | 23 | 4 | 0 | DEF-001, DEF-004, DEF-005 |
| Decision Table | 8 | 0 | 0 | – |
| State Transition | 11 | 3 | 0 | DEF-006 |
| Functional | 20 | 5 | 0 | DEF-003 |
| **Total** | **72** | **13** | **0** | **6** |

**Observation:** Boundary Value Analysis found the most defects (3 of 6). DEF-005 would not have been found without testing the value just below a boundary (799.99).

## 4. Defects

| Severity | Found | Fixed | Retested & closed | Open |
|---|---|---|---|---|
| High | 2 | 2 | 2 | 0 |
| Medium | 3 | 3 | 3 | 0 |
| Low | 1 | 1 | 1 | 0 |
| **Total** | **6** | **6** | **6** | **0** |

Details: [Defect_Reports.md](../04_Defect_Reports/Defect_Reports.md).

## 5. Code coverage (JaCoCo, build v1.1)

| Class | Lines covered | Branches covered |
|---|---|---|
| ExpenseValidator | 25 / 25 (100 %) | 22 / 24 (91.7 %) |
| ExpenseTracker | 26 / 26 (100 %) | 8 / 10 (80.0 %) |
| BudgetManager | 32 / 33 (97.0 %) | 16 / 18 (88.9 %) |
| ReportService | 18 / 18 (100 %) | 5 / 6 (83.3 %) |
| UserAccount | 33 / 36 (91.7 %) | 16 / 18 (88.9 %) |
| Other (enums, record, exception) | 9 / 9 (100 %) | – |
| **Total** | **143 / 147 (97.3 %)** | **67 / 76 (88.2 %)** |

Full HTML report: open [coverage_report_html/index.html](coverage_report_html/index.html) in a browser.

## 6. Exit criteria check

| Exit criterion | Result |
|---|---|
| All planned test cases executed | ✅ 72 / 72 |
| No open Critical or High defects | ✅ 0 open |
| All fixed defects retested and closed | ✅ 6 / 6 |
| Line coverage ≥ 80 % | ✅ 97.3 % |

**Conclusion:** all exit criteria are met. Build v1.1 is recommended for release.

## 7. Cycle 2 detailed results (generated from the JUnit XML)

**Total: 72 | Passed: 72 | Failed: 0**

| # | Test case | Result | Actual result |
|---|---|---|---|
| 1 | TC-FN-12 zero or negative budget is rejected | ✅ PASS | As expected |
| 2 | TC-DT-R1 budgetSet=false spent=500 alertsOn=true -> NO_BUDGET, alert=false | ✅ PASS | As expected |
| 3 | TC-DT-R2 budgetSet=true spent=500 alertsOn=true -> WITHIN, alert=false | ✅ PASS | As expected |
| 4 | TC-DT-R3 budgetSet=true spent=850 alertsOn=true -> WARNING, alert=true | ✅ PASS | As expected |
| 5 | TC-DT-R4 budgetSet=true spent=1200 alertsOn=true -> EXCEEDED, alert=true | ✅ PASS | As expected |
| 6 | TC-DT-R5 budgetSet=false spent=500 alertsOn=false -> NO_BUDGET, alert=false | ✅ PASS | As expected |
| 7 | TC-DT-R6 budgetSet=true spent=500 alertsOn=false -> WITHIN, alert=false | ✅ PASS | As expected |
| 8 | TC-DT-R7 budgetSet=true spent=850 alertsOn=false -> WARNING, alert=false | ✅ PASS | As expected |
| 9 | TC-DT-R8 budgetSet=true spent=1200 alertsOn=false -> EXCEEDED, alert=false | ✅ PASS | As expected |
| 10 | TC-FN-11 spending in another month does not count | ✅ PASS | As expected |
| 11 | TC-BVA-10 spent 799.99 of 1000 -> WITHIN | ✅ PASS | As expected |
| 12 | TC-BVA-10 spent 800.00 of 1000 -> WARNING | ✅ PASS | As expected |
| 13 | TC-BVA-10 spent 800.01 of 1000 -> WARNING | ✅ PASS | As expected |
| 14 | TC-BVA-10 spent 999.99 of 1000 -> WARNING | ✅ PASS | As expected |
| 15 | TC-BVA-10 spent 1000.00 of 1000 -> WARNING | ✅ PASS | As expected |
| 16 | TC-BVA-10 spent 1000.01 of 1000 -> EXCEEDED | ✅ PASS | As expected |
| 17 | TC-FN-13 percentage used is calculated to 2 decimals | ✅ PASS | As expected |
| 18 | TC-FN-08 filter by category returns only that category | ✅ PASS | As expected |
| 19 | TC-FN-06 delete an expense removes it | ✅ PASS | As expected |
| 20 | TC-FN-02 description is saved without leading and trailing spaces | ✅ PASS | As expected |
| 21 | TC-FN-05 edit a missing expense shows an error | ✅ PASS | As expected |
| 22 | TC-FN-03 invalid expense is not stored | ✅ PASS | As expected |
| 23 | TC-FN-10 date range with start after end shows an error | ✅ PASS | As expected |
| 24 | TC-FN-01 add a valid expense stores it with a new id | ✅ PASS | As expected |
| 25 | TC-FN-07 delete a missing expense shows an error | ✅ PASS | As expected |
| 26 | TC-FN-04 edit an expense changes its amount | ✅ PASS | As expected |
| 27 | TC-FN-09 date range search includes both the start and end dates | ✅ PASS | As expected |
| 28 | TC-BVA-05 maximum 1,000,000.00 is accepted | ✅ PASS | As expected |
| 29 | TC-BVA-02 minimum 0.01 is accepted | ✅ PASS | As expected |
| 30 | TC-BVA-06 just above maximum 1,000,000.01 is rejected | ✅ PASS | As expected |
| 31 | TC-BVA-03 just above minimum 0.02 is accepted | ✅ PASS | As expected |
| 32 | TC-BVA-04 just below maximum 999,999.99 is accepted | ✅ PASS | As expected |
| 33 | TC-BVA-01 just below minimum 0.00 is rejected | ✅ PASS | As expected |
| 34 | TC-EP-02 negative amount -50.00 is rejected | ✅ PASS | As expected |
| 35 | TC-EP-03 zero amount is rejected | ✅ PASS | As expected |
| 36 | TC-EP-05 amount with 3 decimal places 10.555 is rejected | ✅ PASS | As expected |
| 37 | TC-EP-01 valid amount 500.00 is accepted | ✅ PASS | As expected |
| 38 | TC-EP-06 missing amount is rejected | ✅ PASS | As expected |
| 39 | TC-EP-04 amount above maximum 2,500,000 is rejected | ✅ PASS | As expected |
| 40 | TC-EP-10 missing date is rejected | ✅ PASS | As expected |
| 41 | TC-EP-09 missing category is rejected | ✅ PASS | As expected |
| 42 | TC-BVA-09 date 1999-12-31 gives valid=false | ✅ PASS | As expected |
| 43 | TC-BVA-09 date 2000-01-01 gives valid=true | ✅ PASS | As expected |
| 44 | TC-BVA-09 date 2000-01-02 gives valid=true | ✅ PASS | As expected |
| 45 | TC-BVA-09 date 2026-11-09 gives valid=true | ✅ PASS | As expected |
| 46 | TC-BVA-09 date 2026-11-10 gives valid=true | ✅ PASS | As expected |
| 47 | TC-BVA-09 date 2026-11-11 gives valid=false | ✅ PASS | As expected |
| 48 | TC-EP-08 whitespace-only description is rejected | ✅ PASS | As expected |
| 49 | TC-BVA-08 description of 101 characters is rejected | ✅ PASS | As expected |
| 50 | TC-BVA-07 description of 1 characters is accepted | ✅ PASS | As expected |
| 51 | TC-BVA-07 description of 2 characters is accepted | ✅ PASS | As expected |
| 52 | TC-BVA-07 description of 99 characters is accepted | ✅ PASS | As expected |
| 53 | TC-BVA-07 description of 100 characters is accepted | ✅ PASS | As expected |
| 54 | TC-EP-07 empty description is rejected | ✅ PASS | As expected |
| 55 | TC-RP-02 category breakdown adds up each category | ✅ PASS | As expected |
| 56 | TC-RP-07 zero income gives savings rate 0.00 (no divide-by-zero) | ✅ PASS | As expected |
| 57 | TC-RP-05 month with no expenses has total 0 | ✅ PASS | As expected |
| 58 | TC-RP-04 savings rate is a percentage of income | ✅ PASS | As expected |
| 59 | TC-RP-01 monthly total includes the first and last day of the month | ✅ PASS | As expected |
| 60 | TC-RP-06 negative income is rejected | ✅ PASS | As expected |
| 61 | TC-RP-03 savings = income - monthly total | ✅ PASS | As expected |
| 62 | TC-ST-10 invalid transition: login while already LOGGED_IN is refused | ✅ PASS | As expected |
| 63 | TC-ST-05 LOCKED + correct password -> login refused | ✅ PASS | As expected |
| 64 | TC-ST-08 invalid transition: logout while LOGGED_OUT is refused | ✅ PASS | As expected |
| 65 | TC-ST-03 LOGGED_OUT + 3 wrong passwords in a row -> LOCKED | ✅ PASS | As expected |
| 66 | TC-ST-06 LOGGED_IN + logout -> LOGGED_OUT | ✅ PASS | As expected |
| 67 | TC-ST-07 LOCKED + admin unlock -> LOGGED_OUT, then login works | ✅ PASS | As expected |
| 68 | TC-ST-09 invalid transition: unlock an account that is not locked is refused | ✅ PASS | As expected |
| 69 | TC-ST-04 2 wrong then correct -> LOGGED_IN and counter reset | ✅ PASS | As expected |
| 70 | TC-ST-11 password shorter than 8 characters is rejected at sign-up | ✅ PASS | As expected |
| 71 | TC-ST-02 LOGGED_OUT + 1 wrong password -> stays LOGGED_OUT | ✅ PASS | As expected |
| 72 | TC-ST-01 LOGGED_OUT + correct password -> LOGGED_IN | ✅ PASS | As expected |
