#!/usr/bin/env python3
"""Execute the Lang-1 smoke configuration without a third-party YAML parser."""

from __future__ import annotations
from datetime import datetime, timezone

import argparse
import json
import re
import shutil
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


def utc_timestamp(moment: datetime) -> str:
    return moment.isoformat().replace("+00:00", "Z")


def write_manifest(path: Path, payload: dict[str, object]) -> None:
    path.write_text(json.dumps(payload, indent=2) + "\n", encoding="utf-8")


RUN_ID_PATTERN = re.compile(r"[A-Za-z0-9][A-Za-z0-9._-]{0,63}")


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
    algorithm_values = {name: read_algorithm(content, name) for name in ("fscs-art", "cmaes")}
    started_at = datetime.now(timezone.utc)
    if args.run_id:
        if not RUN_ID_PATTERN.fullmatch(args.run_id):
            raise ValueError("--run-id must be a single safe path component of at most 64 characters")
        run_id = args.run_id
        run_id_source = "explicit"
    else:
        run_id = started_at.strftime("%Y%m%dT%H%M%SZ")
        run_id_source = "utc-clock"
    run_root = args.results_root / run_id
    if run_root.exists():
        raise FileExistsError(f"Refusing to overwrite existing run directory: {run_root}")
    run_root.mkdir(parents=True)
    shutil.copy2(args.config, run_root / "config.yaml")
    manifest_path = run_root / "run.json"
    manifest: dict[str, object] = {
        "run_id": run_id,
        "run_id_source": run_id_source,
        "started_at_utc": utc_timestamp(started_at),
        "status": "running",
        "config_snapshot": "config.yaml",
    }
    write_manifest(manifest_path, manifest)
    script = Path(__file__).with_name("lang1-numberutils-differential.sh")
    validator = Path(__file__).with_name("validate-lang1-regression-test.sh")
    try:
        for algorithm, values in algorithm_values.items():
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
    except Exception as error:
        manifest.update({
            "status": "failed",
            "ended_at_utc": utc_timestamp(datetime.now(timezone.utc)),
            "error_type": type(error).__name__,
        })
        write_manifest(manifest_path, manifest)
        raise
    manifest.update({
        "status": "complete",
        "ended_at_utc": utc_timestamp(datetime.now(timezone.utc)),
    })
    write_manifest(manifest_path, manifest)


if __name__ == "__main__":
    main()
