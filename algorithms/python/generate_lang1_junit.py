#!/usr/bin/env python3
"""Turn one observed Lang-1 b/f difference into a standalone JUnit regression test."""

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
    selected = next(
        (
            row for row in rows
            if row["different"]
            and row["fixed"]["status"] == "value"
            and row["fixed"]["type"] != "null"
        ),
        None,
    )
    if selected is None:
        raise SystemExit("No fixed-value behavioural difference is available for JUnit generation")
    expected = selected["fixed"]
    source = f'''import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/** Generated from fixed revision behaviour for {selected["input"]}. */
public class {args.class_name} {{
    @Test
    public void preservesLeadingZeroHexClassification() {{
        Number actual = NumberUtils.createNumber({java_literal(str(selected["input"]))});
        assertEquals({java_literal(str(expected["type"]))}, actual.getClass().getName());
        assertEquals({java_literal(str(expected["value"]))}, actual.toString());
    }}
}}
'''
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(source, encoding="utf-8")


if __name__ == "__main__":
    main()
