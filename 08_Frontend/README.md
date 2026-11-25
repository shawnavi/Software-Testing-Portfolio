# Frontend – Expense Tracker web app

A web page for the Expense Tracking and Personal Finance Management System. Every button calls the same Java classes that the JUnit tests check (build v1.1), so the rules you see on screen are the tested rules.

| Screen | Module | What you can try |
|---|---|---|
| Login | M5 | Enter a wrong password 3 times: the state changes to **LOCKED**. Use *Admin: unlock account*, then log in. |
| Expenses | M1, M2 | Add, edit, delete, filter by category, search a date range. Try an amount of 1000000.01 or a description of only spaces: the validation message appears. |
| Budgets | M3 | Set a budget per category and month. Status shows Within / Warning / Exceeded; alerts can be turned off. |
| Monthly report | M4 | Total spent, savings and savings rate for any month and income, plus spending by category. |

Demo account: `student01` / `Budget@2026`. Sample expenses for this month and last month are loaded at start. Data lives in memory and resets when the server stops.

## How to run

Needs Java 17+ and Maven. From the repository root:

```bash
mvn -q -f 05_Test_Automation/expense-tracker/pom.xml compile
```

```bash
java -cp 05_Test_Automation/expense-tracker/target/classes 08_Frontend/WebApp.java
```

Then open http://localhost:8090. Press Ctrl+C in the terminal to stop it.

## Files

| File | Purpose |
|---|---|
| `WebApp.java` | Small web server (JDK built-in, no extra libraries). Turns each request into a call on `ExpenseTracker`, `BudgetManager`, `ReportService` or `UserAccount`. |
| `index.html` | The page: HTML, CSS and JavaScript in one file. |

The frontend is kept outside the Maven project so that the test coverage figures in `06_Test_Reports` still describe only the business logic.
