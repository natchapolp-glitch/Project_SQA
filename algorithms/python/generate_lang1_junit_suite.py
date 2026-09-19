#!/usr/bin/env python3
"""Create a JUnit suite from every unique comparable Lang-1 b/f difference."""

from __future__ import annotations

import argparse
import json
from pathlib import Path


def java_literal(value: str) -> str:
    return json.dumps(value)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--comparison", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--class-name", default="GeneratedLang1RegressionTest")
    args = parser.parse_args()
    rows = json.loads(args.comparison.read_text(encoding="utf-8"))
    selected = []
    seen_inputs: set[str] = set()
    for row in rows:
        fixed = row["fixed"]
        value = str(row["input"])
        if (
            row["different"]
            and fixed["status"] == "value"
            and fixed["type"] != "null"
            and value not in seen_inputs
        ):
            selected.append(row)
            seen_inputs.add(value)
    if not selected:
        raise SystemExit("No fixed-value behavioural differences are available for JUnit generation")
    methods = []
    for index, row in enumerate(selected, start=1):
        expected = row["fixed"]
        methods.append(
            f'''    @Test
    public void generatedCase{index}() {{
        Number actual = NumberUtils.createNumber({java_literal(str(row["input"]))});
        assertEquals({java_literal(str(expected["type"]))}, actual.getClass().getName());
        assertEquals({java_literal(str(expected["value"]))}, actual.toString());
    }}'''
        )
    source = f'''import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/** Generated from {len(selected)} unique fixed-revision observations. */
public class {args.class_name} {{
{chr(10).join(methods)}
}}
'''
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(source, encoding="utf-8")
    print(json.dumps({"generated_tests": len(selected)}))


if __name__ == "__main__":
    main()
