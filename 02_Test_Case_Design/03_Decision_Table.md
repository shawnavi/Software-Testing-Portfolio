# Decision Table Testing – Module M3 (Budget status and alerts)

The budget status and the alert depend on a combination of conditions, so a decision table lists every combination as a rule.

## Conditions and actions
- **C1** Is a budget set for the category and month? (Y / N)
- **C2** How much of the budget is spent? (< 80 % / 80–100 % / > 100 %)
- **C3** Are alerts turned on? (Y / N)
- **A1** Status shown
- **A2** Alert sent?

## Decision table (budget = 1000.00)

| | R1 | R2 | R3 | R4 | R5 | R6 | R7 | R8 |
|---|---|---|---|---|---|---|---|---|
| **C1** Budget set | N | Y | Y | Y | N | Y | Y | Y |
| **C2** Spent | – (500) | < 80 % (500) | 80–100 % (850) | > 100 % (1200) | – (500) | < 80 % (500) | 80–100 % (850) | > 100 % (1200) |
| **C3** Alerts on | Y | Y | Y | Y | N | N | N | N |
| **A1** Status | NO_BUDGET | WITHIN | WARNING | EXCEEDED | NO_BUDGET | WITHIN | WARNING | EXCEEDED |
| **A2** Alert sent | No | No | **Yes** | **Yes** | No | No | No | No |

When no budget is set, C2 does not matter ("–"), so the 12 possible combinations reduce to 8 rules.

## Test cases

| TC ID | Rule | Steps | Expected | JUnit test |
|---|---|---|---|---|
| TC-DT-R1 | R1 | Alerts on, no budget, spend 500 on FOOD in Oct 2026 | NO_BUDGET, no alert | `BudgetManagerTest.decisionTable[1]` |
| TC-DT-R2 | R2 | Alerts on, budget 1000, spend 500 | WITHIN, no alert | `decisionTable[2]` |
| TC-DT-R3 | R3 | Alerts on, budget 1000, spend 850 | WARNING, alert | `decisionTable[3]` |
| TC-DT-R4 | R4 | Alerts on, budget 1000, spend 1200 | EXCEEDED, alert | `decisionTable[4]` |
| TC-DT-R5 | R5 | Alerts off, no budget, spend 500 | NO_BUDGET, no alert | `decisionTable[5]` |
| TC-DT-R6 | R6 | Alerts off, budget 1000, spend 500 | WITHIN, no alert | `decisionTable[6]` |
| TC-DT-R7 | R7 | Alerts off, budget 1000, spend 850 | WARNING, no alert | `decisionTable[7]` |
| TC-DT-R8 | R8 | Alerts off, budget 1000, spend 1200 | EXCEEDED, no alert | `decisionTable[8]` |

The exact edges between the C2 ranges (80 % and 100 %) are tested in [BVA section 4](02_Boundary_Value_Analysis.md#4-budget-thresholds-r9-warning-from-80--exceeded-above-100---budget--100000).
