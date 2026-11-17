"""Turn Maven Surefire XML results into a Markdown table.

Usage: python3 results_to_md.py <surefire-reports-folder>
"""
import glob
import os
import re
import sys
import xml.etree.ElementTree as ET

folder = sys.argv[1]
rows = []
for path in sorted(glob.glob(os.path.join(folder, "TEST-*.xml"))):
    for case in ET.parse(path).getroot().iter("testcase"):
        name = re.sub(r"^\w+\([^)]*\) ", "", case.get("name"))
        problem = case.find("failure")
        if problem is None:
            problem = case.find("error")
        if problem is None:
            status, actual = "PASS", "As expected"
        else:
            status = "FAIL"
            actual = (problem.get("message") or problem.get("type") or "").splitlines()[0]
            actual = actual.replace("com.team.expensetracker.", "").replace("|", "/")
        rows.append((name, status, actual))

passed = sum(1 for r in rows if r[1] == "PASS")
print(f"**Total: {len(rows)} | Passed: {passed} | Failed: {len(rows) - passed}**\n")
print("| # | Test case | Result | Actual result |")
print("|---|---|---|---|")
for i, (name, status, actual) in enumerate(rows, 1):
    mark = "✅ PASS" if status == "PASS" else "❌ FAIL"
    print(f"| {i} | {name} | {mark} | {actual} |")
