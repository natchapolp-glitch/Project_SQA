#!/usr/bin/env python3
"""Execute the Lang-1 smoke configuration without a third-party YAML parser."""

from __future__ import annotations
from datetime import datetime, timezone

import argparse
import shutil
import re
import subprocess
from pathlib import Path


def read_scalar(text: str, name: str) -> str:
    match = re.search(rf"^{re.escape(name)}:\s*(.+)$", text, re.MULTILINE)
    if not match:
        raise ValueError(f"missing {name} in smoke configuration")
    return match.group(1).strip().strip('"')


def read_algorithm(text: str, name: str) -> dict[str, str]:
    match = re.search(rf"^  {re.escape(name)}:\s*$\n((?:^    .+$\n?)+)", text, re.MULTILINE)
    if not match:
        raise ValueError(f"missing algorithms.{name} in smoke configuration")
    values: dict[str, str] = {}
    for line in match.group(1).splitlines():
        key, value = line.strip().split(":", 1)
        values[key] = value.strip().strip('"')
    return values


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--config", type=Path, required=True)
    parser.add_argument("--buggy-worktree", type=Path, required=True)
    parser.add_argument("--fixed-worktree", type=Path, required=True)
    parser.add_argument("--run-id")
    parser.add_argument("--results-root", type=Path, required=True)
    args = parser.parse_args()
    content = args.config.read_text(encoding="utf-8")
    if (read_scalar(content, "project"), read_scalar(content, "bug_id"), read_scalar(content, "revisions")) != ("Lang", "1", "[1b, 1f]"):
        raise ValueError("this runner accepts only the validated Lang-1 smoke configuration")
    run_id = args.run_id or datetime.now(timezone.utc).strftime("%Y%m%dT%H%M%SZ")
    run_root = args.results_root / run_id
    if run_root.exists():
        raise FileExistsError(f"Refusing to overwrite existing run directory: {run_root}")
    run_root.mkdir(parents=True)
    shutil.copy2(args.config, run_root / "config.yaml")
    script = Path(__file__).with_name("lang1-numberutils-differential.sh")
    validator = Path(__file__).with_name("validate-lang1-regression-test.sh")
    for algorithm in ("fscs-art", "cmaes"):
        values = read_algorithm(content, algorithm)
        budget = values["budget"]
        seed = values["seed"]
        command = [
            "bash", str(script), algorithm, str(args.buggy_worktree), str(args.fixed_worktree),
            str(run_root / f"{algorithm}-seed{seed}-b{budget}"), budget,
            "--seed", seed,
        ]
        if algorithm == "fscs-art":
            command.extend(("--candidate-set-size", values["candidate_set_size"]))
        else:
            command.extend(("--sigma", values["initial_step_size"]))
        result_directory = run_root / f"{algorithm}-seed{seed}-b{budget}"
        subprocess.run(command, check=True)
        subprocess.run(
            [
                "bash", str(validator), str(result_directory / "comparison.json"),
                str(args.buggy_worktree), str(args.fixed_worktree),
                str(result_directory / "regression-test"),
            ],
            check=True,
        )


if __name__ == "__main__":
    main()
