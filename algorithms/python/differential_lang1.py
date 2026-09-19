#!/usr/bin/env python3
"""Compare NumberUtils outcomes in two Defects4J revisions."""

from __future__ import annotations

import argparse
import csv
import json
import subprocess
from pathlib import Path


def execute(classpath: str, harness: Path, value: str) -> dict[str, object]:
    result = subprocess.run(
        ["java", "-cp", f"{classpath}:{harness}", "NumberUtilsOracle", value],
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        check=False,
    )
    if result.returncode == 0:
        return {"status": "value", "value": result.stdout.strip()}
    last_line = next((line for line in reversed(result.stderr.splitlines()) if line.strip()), "")
    return {"status": "exception", "value": last_line, "return_code": result.returncode}


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
        rows.append({"case_id": index, **case, "buggy": buggy, "fixed": fixed, "different": buggy != fixed})
    args.output.mkdir(parents=True, exist_ok=True)
    (args.output / "comparison.json").write_text(json.dumps(rows, indent=2) + "\n", encoding="utf-8")
    with (args.output / "comparison.csv").open("w", newline="", encoding="utf-8") as stream:
        writer = csv.writer(stream)
        writer.writerow(("case_id", "input", "leading_zeros", "suffix_digits", "buggy_status", "buggy_value", "fixed_status", "fixed_value", "different"))
        for row in rows:
            writer.writerow((row["case_id"], row["input"], row["leading_zeros"], row["suffix_digits"], row["buggy"]["status"], row["buggy"]["value"], row["fixed"]["status"], row["fixed"]["value"], row["different"]))
    summary = {"cases": len(rows), "differences": sum(bool(row["different"]) for row in rows)}
    (args.output / "summary.json").write_text(json.dumps(summary, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(summary))


if __name__ == "__main__":
    main()
