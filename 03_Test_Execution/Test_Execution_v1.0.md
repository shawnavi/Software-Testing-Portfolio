# Test Execution – Build v1.0

| | |
|---|---|
| **Build** | v1.0 |
| **Executed on** | 10-11-2026 |
| **Executed by** | Shawn Avinash Robin |
| **Command** | `mvn -B test` (JUnit 5.10.2, Maven Surefire 3.2.5, OpenJDK 21) |
| **Raw evidence** | [run_log_v1.0.txt](run_log_v1.0.txt) (full Maven console output) · [surefire_v1.0/](surefire_v1.0) (JUnit XML results) |

## Summary

| Module | Run | Passed | Failed |
|---|---|---|---|
| M1 Expense input validation | 27 | 25 | 2 |
| M2 Manage expenses | 10 | 9 | 1 |
| M3 Budget and alerts | 17 | 14 | 3 |
| M4 Reports and savings | 7 | 3 | 4 |
| M5 Login state transitions | 11 | 8 | 3 |
| **Total** | **72** | **59** | **13** |

Pass rate: **81.9 %**. The 13 failures were caused by **6 defects** (some defects make more than one test fail).

## Failed test cases and the defect behind each

| Test case | Expected | Actual | Defect |
|---|---|---|---|
| TC-BVA-05 amount 1,000,000.00 | Accepted | Rejected "Amount must be between 0.01 and 1,000,000.00" | [DEF-001](../04_Defect_Reports/Defect_Reports.md#def-001) |
| TC-EP-08 description of only spaces | Rejected | Accepted | [DEF-002](../04_Defect_Reports/Defect_Reports.md#def-002) |
| TC-FN-09 date range 01-10 to 31-10 | 3 expenses | 2 expenses | [DEF-003](../04_Defect_Reports/Defect_Reports.md#def-003) |
| TC-RP-01 October total | 17,800.00 | 16,300.50 | DEF-003 |
| TC-RP-02 FOOD in breakdown | 5,000.00 | 3,500.50 | DEF-003 |
| TC-RP-03 savings | 7,200.00 | 8,699.50 | DEF-003 |
| TC-RP-04 savings rate | 28.80 | 34.80 | DEF-003 |
| TC-BVA-10e spent 1000.00 of 1000 | WARNING | EXCEEDED | [DEF-004](../04_Defect_Reports/Defect_Reports.md#def-004) |
| TC-BVA-10a spent 799.99 of 1000 | WITHIN | WARNING | [DEF-005](../04_Defect_Reports/Defect_Reports.md#def-005) |
| TC-BVA-10d spent 999.99 of 1000 | WARNING | EXCEEDED | DEF-005 |
| TC-ST-03 3 wrong passwords | LOCKED | LOGGED_OUT | [DEF-006](../04_Defect_Reports/Defect_Reports.md#def-006) |
| TC-ST-05 login when locked | Refused | Not refused (account was never locked) | DEF-006 |
| TC-ST-07 unlock after 3 wrong passwords | LOGGED_OUT | Error "Account is not locked" | DEF-006 |

## Full results (generated from the JUnit XML)

Generated with `python3 05_Test_Automation/tools/results_to_md.py 03_Test_Execution/surefire_v1.0`.

**Total: 72 | Passed: 59 | Failed: 13**

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
| 11 | TC-BVA-10 spent 799.99 of 1000 -> WITHIN | ❌ FAIL | expected: <WITHIN> but was: <WARNING> |
| 12 | TC-BVA-10 spent 800.00 of 1000 -> WARNING | ✅ PASS | As expected |
| 13 | TC-BVA-10 spent 800.01 of 1000 -> WARNING | ✅ PASS | As expected |
| 14 | TC-BVA-10 spent 999.99 of 1000 -> WARNING | ❌ FAIL | expected: <WARNING> but was: <EXCEEDED> |
| 15 | TC-BVA-10 spent 1000.00 of 1000 -> WARNING | ❌ FAIL | expected: <WARNING> but was: <EXCEEDED> |
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
| 27 | TC-FN-09 date range search includes both the start and end dates | ❌ FAIL | expected: <3> but was: <2> |
| 28 | TC-BVA-05 maximum 1,000,000.00 is accepted | ❌ FAIL | Unexpected exception thrown: ValidationException: Amount must be between 0.01 and 1,000,000.00 |
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
| 48 | TC-EP-08 whitespace-only description is rejected | ❌ FAIL | Expected ValidationException to be thrown, but nothing was thrown. |
| 49 | TC-BVA-08 description of 101 characters is rejected | ✅ PASS | As expected |
| 50 | TC-BVA-07 description of 1 characters is accepted | ✅ PASS | As expected |
| 51 | TC-BVA-07 description of 2 characters is accepted | ✅ PASS | As expected |
| 52 | TC-BVA-07 description of 99 characters is accepted | ✅ PASS | As expected |
| 53 | TC-BVA-07 description of 100 characters is accepted | ✅ PASS | As expected |
| 54 | TC-EP-07 empty description is rejected | ✅ PASS | As expected |
| 55 | TC-RP-02 category breakdown adds up each category | ❌ FAIL | expected: <5000.00> but was: <3500.50> |
| 56 | TC-RP-07 zero income gives savings rate 0.00 (no divide-by-zero) | ✅ PASS | As expected |
| 57 | TC-RP-05 month with no expenses has total 0 | ✅ PASS | As expected |
| 58 | TC-RP-04 savings rate is a percentage of income | ❌ FAIL | expected: <28.80> but was: <34.80> |
| 59 | TC-RP-01 monthly total includes the first and last day of the month | ❌ FAIL | expected: <17800.00> but was: <16300.50> |
| 60 | TC-RP-06 negative income is rejected | ✅ PASS | As expected |
| 61 | TC-RP-03 savings = income - monthly total | ❌ FAIL | expected: <7200.00> but was: <8699.50> |
| 62 | TC-ST-10 invalid transition: login while already LOGGED_IN is refused | ✅ PASS | As expected |
| 63 | TC-ST-05 LOCKED + correct password -> login refused | ❌ FAIL | Expected java.lang.IllegalStateException to be thrown, but nothing was thrown. |
| 64 | TC-ST-08 invalid transition: logout while LOGGED_OUT is refused | ✅ PASS | As expected |
| 65 | TC-ST-03 LOGGED_OUT + 3 wrong passwords in a row -> LOCKED | ❌ FAIL | expected: <LOCKED> but was: <LOGGED_OUT> |
| 66 | TC-ST-06 LOGGED_IN + logout -> LOGGED_OUT | ✅ PASS | As expected |
| 67 | TC-ST-07 LOCKED + admin unlock -> LOGGED_OUT, then login works | ❌ FAIL | Account is not locked |
| 68 | TC-ST-09 invalid transition: unlock an account that is not locked is refused | ✅ PASS | As expected |
| 69 | TC-ST-04 2 wrong then correct -> LOGGED_IN and counter reset | ✅ PASS | As expected |
| 70 | TC-ST-11 password shorter than 8 characters is rejected at sign-up | ✅ PASS | As expected |
| 71 | TC-ST-02 LOGGED_OUT + 1 wrong password -> stays LOGGED_OUT | ✅ PASS | As expected |
| 72 | TC-ST-01 LOGGED_OUT + correct password -> LOGGED_IN | ✅ PASS | As expected |
