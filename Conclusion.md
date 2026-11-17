# Conclusion

## What we did
We tested the **Expense Tracking and Personal Finance Management System** across five modules: expense validation, expense management, budgets and alerts, reports and savings, and login. We wrote a test plan, designed **72 test cases** using four black-box techniques, automated all of them with **JUnit 5**, and ran them on two builds.

## Results
- **Build v1.0:** 59 of 72 passed. The 13 failures led to **6 defects**: 2 High, 3 Medium, 1 Low.
- **Build v1.1:** after the fixes, **all 72 tests passed** and no defects are open.
- **Coverage:** 97.3 % of lines and 88.2 % of branches are covered by the tests.
- All exit criteria in the test plan are met.

## What each technique taught us
| Technique | Lesson |
|---|---|
| Equivalence Partitioning | A few well-chosen values cover a whole range of inputs. The "spaces only" class found DEF-002, which a normal valid/empty test would miss. |
| Boundary Value Analysis | The most useful technique here: it found 3 of the 6 defects. Off-by-one mistakes (`>=` vs `>`) and rounding problems (DEF-005) sit right at the boundaries. |
| Decision Table | Made sure every combination of budget / spending / alert setting was tested. No defects were found, which gives confidence that the alert rules are right. |
| State Transition | Found the security defect DEF-006 (one extra password guess). Testing the invalid transitions also checked that the system refuses wrong actions. |

## Most important defect
**DEF-003** made the monthly report leave out expenses on the last day of the month. A user would see a higher savings figure than the real one – a serious problem for a finance app. It was found by a simple functional test that included an expense on the 31st.

## Lessons learned
1. Write the requirement limits down clearly (for example, "exactly 100 % is Warning") before testing; otherwise nobody can say whether the result is a defect.
2. Use exact decimal types (`BigDecimal`) for money, and test what happens with rounding.
3. Make tests repeatable: we fixed "today" to one date so date tests don't change from day to day.
4. Automated tests make retesting fast. Re-running all 72 tests after the fixes took a few seconds.
5. Linking every test to a test case ID (TC-xx) makes it easy to trace a failure back to a requirement.

## Future work
- UI testing with Selenium once a web front end is added.
- API testing with Postman if the logic is exposed as a REST service.
- Mutation testing with PIT to check how strong the tests are.
