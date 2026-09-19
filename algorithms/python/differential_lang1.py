#!/usr/bin/env python3
"""Compare NumberUtils outcomes in two Defects4J revisions."""

from __future__ import annotations

import argparse
import base64
import csv
import json
import subprocess
from pathlib import Path

COMPARABLE_STATUSES = {"value", "exception"}


def is_comparable(buggy: dict[str, object], fixed: dict[str, object]) -> bool:
    return buggy.get("status") in COMPARABLE_STATUSES and fixed.get("status") in COMPARABLE_STATUSES


def execute(classpath: str, harness: Path, value: str) -> dict[str, object]:
    result = subprocess.run(
        ["java", "-cp", f"{classpath}:{harness}", "NumberUtilsOracle", value],
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        check=False,
    )
    if result.returncode != 0:
        return {"status": "execution_error", "return_code": result.returncode, "stderr": result.stderr.strip()}
    try:
        payload = json.loads(result.stdout)
        status = payload["status"]
        outcome_type = payload["type"]
        if status == "value":
            return {"status": status, "type": outcome_type, "value": base64.b64decode(payload["value_base64"]).decode("utf-8")}
        if status == "exception":
            return {"status": status, "type": outcome_type, "message": base64.b64decode(payload["message_base64"]).decode("utf-8")}
    except (KeyError, TypeError, ValueError, UnicodeDecodeError) as error:
        return {"status": "protocol_error", "detail": str(error), "stdout": result.stdout.strip()}
    return {"status": "protocol_error", "detail": "unknown oracle status", "stdout": result.stdout.strip()}


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--inputs", type=Path, required=True)
    parser.add_argument("--buggy-classpath", required=True)
    parser.add_argument("--fixed-classpath", required=True)
    parser.add_argument("--buggy-harness", type=Path, required=True)
    parser.add_argument("--fixed-harness", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    cases = json.loads(args.inputs.read_text(encoding="utf-8"))["cases"]
    rows = []
    for index, case in enumerate(cases, start=1):
        buggy = execute(args.buggy_classpath, args.buggy_harness, str(case["input"]))
        fixed = execute(args.fixed_classpath, args.fixed_harness, str(case["input"]))
        comparable = is_comparable(buggy, fixed)
        different = comparable and buggy != fixed
        rows.append({"case_id": index, **case, "buggy": buggy, "fixed": fixed, "comparable": comparable, "different": different})
    args.output.mkdir(parents=True, exist_ok=True)
    (args.output / "comparison.json").write_text(json.dumps(rows, indent=2) + "\n", encoding="utf-8")
    with (args.output / "comparison.csv").open("w", newline="", encoding="utf-8") as stream:
        writer = csv.writer(stream)
        writer.writerow(("case_id", "input", "leading_zeros", "suffix_digits", "buggy_outcome", "fixed_outcome", "different"))
        for row in rows:
            writer.writerow((row["case_id"], row["input"], row["leading_zeros"], row["suffix_digits"], json.dumps(row["buggy"], sort_keys=True), json.dumps(row["fixed"], sort_keys=True), row["different"]))
    unique_inputs = {str(row["input"]) for row in rows}
    different_inputs = {str(row["input"]) for row in rows if row["different"]}
    comparable_rows = [row for row in rows if row["comparable"]]
    summary = {"cases": len(rows), "unique_inputs": len(unique_inputs), "comparable_cases": len(comparable_rows), "harness_errors": len(rows) - len(comparable_rows), "difference_rows": sum(bool(row["different"]) for row in rows), "unique_differences": len(different_inputs)}
    (args.output / "summary.json").write_text(json.dumps(summary, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(summary))


if __name__ == "__main__":
    main()
