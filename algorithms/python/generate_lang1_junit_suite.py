#!/usr/bin/env python3
"""Create a JUnit suite from every unique comparable Lang-1 b/f difference."""

from __future__ import annotations

import argparse
import json
from pathlib import Path


def java_literal(value: str) -> str:
    return json.dumps(value)


def select_unique_differences(rows: list[dict[str, object]]) -> list[dict[str, object]]:
    selected = []
    seen_inputs: set[str] = set()
    for row in rows:
        fixed = row["fixed"]
        value = str(row["input"])
        if row["different"] and fixed["status"] in {"value", "exception"} and value not in seen_inputs:
            selected.append(row)
            seen_inputs.add(value)
    return selected


def render_method(index: int, row: dict[str, object]) -> str:
    fixed = row["fixed"]
    input_literal = java_literal(str(row["input"]))
    if fixed["status"] == "exception":
        return f'''    @Test
    public void generatedCase{index}() {{
        Throwable observed = null;
        try {{
            NumberUtils.createNumber({input_literal});
        }} catch (Throwable actual) {{
            observed = actual;
        }}
        assertEquals({java_literal(str(fixed["type"]))}, observed == null ? "<no exception>" : observed.getClass().getName());
        assertEquals({java_literal(str(fixed["message"]))}, observed == null || observed.getMessage() == null ? "" : observed.getMessage());
    }}'''
    return f'''    @Test
    public void generatedCase{index}() {{
        Number actual = null;
        Throwable observed = null;
        try {{
            actual = NumberUtils.createNumber({input_literal});
        }} catch (Throwable caught) {{
            observed = caught;
        }}
        assertEquals({java_literal(str(fixed["type"]))}, observed == null ? (actual == null ? "null" : actual.getClass().getName()) : "exception:" + observed.getClass().getName());
        assertEquals({java_literal(str(fixed["value"]))}, observed == null && actual != null ? actual.toString() : "");
    }}'''


def render_suite(rows: list[dict[str, object]], class_name: str) -> tuple[str, int]:
    selected = select_unique_differences(rows)
    if not selected:
        raise ValueError("No comparable behavioural differences are available for JUnit generation")
    methods = [render_method(index, row) for index, row in enumerate(selected, start=1)]
    source = f'''import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/** Generated from {len(selected)} unique fixed-revision observations. */
public class {class_name} {{
{chr(10).join(methods)}
}}
'''
    return source, len(selected)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--comparison", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--class-name", default="GeneratedLang1RegressionTest")
    args = parser.parse_args()
    rows = json.loads(args.comparison.read_text(encoding="utf-8"))
    try:
        source, test_count = render_suite(rows, args.class_name)
    except ValueError as error:
        raise SystemExit(str(error)) from error
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(source, encoding="utf-8")
    print(json.dumps({"generated_tests": test_count}))


if __name__ == "__main__":
    main()
