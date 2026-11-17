# Test Plan
**Project:** Expense Tracking and Personal Finance Management System
**Document ID:** TP-ETPFMS-01 | **Version:** 1.0 | **Prepared by:** Shawn Avinash Robin, Ronnie Thomson, SriHari

---

## 1. Introduction
This test plan describes how the team will test the Expense Tracking and Personal Finance Management System. The goal is to check that the system records expenses correctly, applies its input rules, tracks budgets, produces correct reports, and protects the user's account.

## 2. Test items
| Item | Version | Location |
|---|---|---|
| Expense Tracker business logic (Java) | v1.0 (first test), v1.1 (retest) | `05_Test_Automation/expense-tracker/src/main` |

## 3. Features to be tested

| ID | Module | Requirements |
|---|---|---|
| M1 | Expense input validation | **R1** Amount must be 0.01 – 1,000,000.00 with at most 2 decimal places. **R2** Description must be 1 – 100 characters (spaces at the ends don't count; spaces only is not allowed). **R3** Date must be from 01-01-2000 up to today. **R4** Category is required. |
| M2 | Manage expenses | **R5** Add, edit and delete an expense. **R6** Filter by category. **R7** Search by date range; both start and end dates are included. |
| M3 | Budget and alerts | **R8** Budget per category per month must be greater than 0. **R9** Status: *Within* (< 80 % used), *Warning* (80 % – 100 %), *Exceeded* (> 100 %), *No budget*. **R10** An alert is sent only if alerts are on and the status is Warning or Exceeded. |
| M4 | Reports and savings | **R11** Monthly total and category breakdown include every day of the month. **R12** Savings = income − monthly total; savings rate = savings ÷ income × 100; 0 when income is 0. |
| M5 | Login and account lock | **R13** Correct password logs in. **R14** 3 wrong passwords in a row lock the account. **R15** A locked account cannot log in until an admin unlocks it. **R16** Password must be at least 8 characters. |

## 4. Features not to be tested
- User interface layout and look (the system under test is the business-logic layer).
- Performance and load testing, database storage, bank/UPI integration.

## 5. Approach

| Level / type | How |
|---|---|
| Unit and functional testing | Black-box test cases run as JUnit 5 tests |
| Equivalence Partitioning | Amount, description, date, category inputs (M1) |
| Boundary Value Analysis | Amount min/max, description length, date limits (M1); 80 % and 100 % budget limits (M3) |
| Decision Table | Budget status and alert rules (M3) |
| State Transition | Login / lock / unlock (M5) |
| Regression / retest | Full suite re-run on v1.1 after the fixes |
| Code coverage | JaCoCo line and branch coverage on v1.1 |

## 6. Entry and exit criteria
**Entry:** requirements R1–R16 agreed; build v1.0 compiles; test environment ready; test cases reviewed.

**Exit:** all planned test cases executed; no open Critical or High defects; all fixed defects retested and closed; line coverage ≥ 80 %.

## 7. Suspension and resumption
Testing stops if the build does not compile or more than 50 % of tests fail for an environment reason. It resumes when a new working build is delivered.

## 8. Test environment
| Item | Detail |
|---|---|
| OS | macOS / Windows / Linux |
| Language | Java 17+ (run on OpenJDK 21) |
| Build tool | Apache Maven 3.9 |
| Test framework | JUnit 5.10 (Jupiter, parameterized tests) |
| Coverage | JaCoCo 0.8.12 |
| Version control | Git and GitHub |

## 9. Deliverables
Test plan, test case design documents, test execution results, defect reports, JUnit test scripts, test execution summary report, coverage report, screenshots.

## 10. Schedule

| # | Activity | Owner | Week |
|---|---|---|---|
| 1 | Create GitHub repo and README | Shawn Avinash Robin | 1 |
| 2 | Understand application, write test plan | Shawn Avinash Robin | 1 |
| 3 | Design EP and BVA test cases | Ronnie Thomson | 2 |
| 4 | Design decision table, state transition and functional test cases | SriHari | 2 |
| 5 | Write JUnit tests and run on v1.0 | Shawn Avinash Robin | 3 |
| 6 | Log defects | Ronnie Thomson | 3 |
| 7 | Retest v1.1, coverage, summary report | Shawn Avinash Robin | 4 |
| 8 | Demonstration and viva | All | 5 |

## 11. Roles and responsibilities
See the team table in the [README](../README.md).

## 12. Risks and mitigation

| Risk | Mitigation |
|---|---|
| Date tests depend on today's date | Tests use a fixed clock (10-11-2026) so results are repeatable |
| Money calculations lose precision | Use `BigDecimal`; test rounding at the boundaries |
| Requirements unclear at the limits (e.g., exactly 100 %) | Limits written down in R1–R16 before designing tests |
| Team member unavailable | Every test case is automated and documented, so anyone can run it |

## 13. Approvals
| Name | Role | Signature | Date |
|---|---|---|---|
| | Test lead | | |
| | Faculty | | |
