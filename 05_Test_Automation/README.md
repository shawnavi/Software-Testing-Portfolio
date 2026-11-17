# Test Automation – JUnit 5 + JaCoCo

This is the **additional activity** for Stage 3: every designed test case is automated as a JUnit 5 test, and code coverage is measured with JaCoCo.

## Folder contents

```
05_Test_Automation/
├── expense-tracker/                 Maven project
│   ├── pom.xml                      JUnit 5.10.2, Surefire 3.2.5, JaCoCo 0.8.12
│   └── src/
│       ├── main/java/.../           Application under test (v1.1)
│       │   ├── ExpenseValidator.java   M1 input rules
│       │   ├── ExpenseTracker.java     M2 add / edit / delete / search
│       │   ├── BudgetManager.java      M3 budget status and alerts
│       │   ├── ReportService.java      M4 reports and savings
│       │   └── UserAccount.java        M5 login state machine
│       └── test/java/.../           Test scripts
│           ├── ExpenseValidatorTest.java   EP + BVA          (27 tests)
│           ├── ExpenseTrackerTest.java     Functional        (10 tests)
│           ├── BudgetManagerTest.java      Decision table + BVA (17 tests)
│           ├── ReportServiceTest.java      Functional        (7 tests)
│           ├── UserAccountTest.java        State transition  (11 tests)
│           └── TestFixtures.java           Fixed clock (10-11-2026) and helpers
└── tools/results_to_md.py           Turns JUnit XML results into a Markdown table
```

## How to run

Requirements: Java 17+ and Maven 3.8+.

```bash
cd 05_Test_Automation/expense-tracker
mvn clean verify
```

- Test results: `target/surefire-reports/`
- Coverage report: `target/site/jacoco/index.html`
- Results as a Markdown table:

```bash
python3 ../tools/results_to_md.py target/surefire-reports
```

## JUnit features used

| Feature | Where | Why |
|---|---|---|
| `@DisplayName` with the TC ID | every test | Links each test to its test case in `02_Test_Case_Design` |
| `@ParameterizedTest` + `@CsvSource` | decision table, budget and date boundaries | One test method runs each row of the table |
| `@ParameterizedTest` + `@ValueSource` | description lengths | Tests 1, 2, 99, 100 characters in one method |
| `@Nested` | `ExpenseValidatorTest` | Groups EP and BVA tests by input field |
| `@BeforeEach` | most test classes | Fresh data for every test so tests don't affect each other |
| `assertThrows` / `assertDoesNotThrow` | validation and state tests | Checks that invalid input is rejected |
| Fixed `Clock` | `TestFixtures` | "Today" is always 10-11-2026, so date tests give the same result every day |

## Reproduce the v1.0 failures

The changes between v1.0 and v1.1 are in [`04_Defect_Reports/fixes_v1.0_to_v1.1.diff`](../04_Defect_Reports/fixes_v1.0_to_v1.1.diff). To see the v1.0 failures again, reverse the patch, run the tests, then apply it again:

```bash
cd 05_Test_Automation/expense-tracker/src/main
patch -R -p1 < ../../../../04_Defect_Reports/fixes_v1.0_to_v1.1.diff
```
