#!/usr/bin/env python3
"""Execute the Lang-1 smoke configuration without a third-party YAML parser."""

from __future__ import annotations

import argparse
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
    parser.add_argument("--results-root", type=Path, required=True)
    args = parser.parse_args()
    content = args.config.read_text(encoding="utf-8")
    if read_scalar(content, "project") != "Lang" or read_scalar(content, "bug_id") != "1":
        raise ValueError("this runner accepts only the validated Lang-1 smoke configuration")
    script = Path(__file__).with_name("lang1-numberutils-differential.sh")
    for algorithm in ("fscs-art", "cmaes"):
        values = read_algorithm(content, algorithm)
        budget = values["budget"]
        seed = values["seed"]
        command = [
            "bash", str(script), algorithm, str(args.buggy_worktree), str(args.fixed_worktree),
            str(args.results_root / f"{algorithm}-seed{seed}-b{budget}"), budget,
            "--seed", seed,
        ]
        if algorithm == "fscs-art":
            command.extend(("--candidate-set-size", values["candidate_set_size"]))
        else:
            command.extend(("--sigma", values["initial_step_size"]))
        subprocess.run(command, check=True)


if __name__ == "__main__":
    main()
