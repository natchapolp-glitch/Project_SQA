#!/usr/bin/env python3
"""Evaluate an explicitly supplied JUnit source archive with Defects4J.

Run this in the Java 11 / Defects4J environment (normally Linux or WSL).
Every invocation creates a new output directory and retains raw evidence.
"""

from __future__ import annotations

import argparse
import csv
from dataclasses import dataclass
from datetime import datetime, timezone
import hashlib
import io
import json
import math
import os
from pathlib import Path, PurePosixPath
import re
import shutil
import signal
import subprocess
import tarfile
import time
from typing import Any


@dataclass(frozen=True)
class EvaluationConfig:
    project: str
    bug_id: int
    generator: str
    seed: int
    budget: int
    suite: Path
    buggy_worktree: Path
    fixed_worktree: Path
    output: Path
    d4j: str = "defects4j"
    classes_file: Path | None = None
    generation_seconds: float | None = None
    test_count: int | None = None
    timeout_seconds: float = 300.0


class EvidenceError(ValueError):
    """A command did not provide internally consistent measurement evidence."""


def utc_now() -> str:
    return datetime.now(timezone.utc).isoformat().replace("+00:00", "Z")


def write_record(path: Path, record: dict[str, Any]) -> None:
    temporary = path.with_suffix(".tmp")
    temporary.write_text(json.dumps(record, indent=2, allow_nan=False) + "\n", encoding="utf-8")
    temporary.replace(path)


def validate_config(config: EvaluationConfig) -> None:
    for name in ("project", "generator"):
        if not re.fullmatch(r"[A-Za-z][A-Za-z0-9_-]*", getattr(config, name)):
            raise ValueError(f"{name} must be a safe, nonempty identifier")
    if config.bug_id < 1 or config.budget < 1 or config.seed < 0:
        raise ValueError("bug_id and budget must be positive; seed must be nonnegative")
    if config.test_count is not None and config.test_count < 1:
        raise ValueError("test_count, when supplied, must be positive")
    if not math.isfinite(config.timeout_seconds) or config.timeout_seconds <= 0:
        raise ValueError("timeout_seconds must be finite and positive")
    if config.generation_seconds is not None and (
        not math.isfinite(config.generation_seconds) or config.generation_seconds < 0
    ):
        raise ValueError("generation_seconds must be finite and nonnegative")


def validate_worktree(path: Path, project: str, revision: str) -> None:
    fields: dict[str, str] = {}
    for line in (path / ".defects4j.config").read_text(encoding="utf-8").splitlines():
        if "=" in line and not line.lstrip().startswith("#"):
            key, value = line.split("=", 1)
            key = key.strip()
            if key in fields:
                raise EvidenceError(f"duplicate {key} in {path}/.defects4j.config")
            fields[key] = value.strip()
    if (fields.get("pid"), fields.get("vid")) != (project, revision):
        raise EvidenceError(f"Expected {project}-{revision} checkout at {path}")


def validate_archive(path: Path) -> int:
    """Reject paths/links that Defects4J's extractor must not follow."""
    java_files = 0
    seen: set[str] = set()
    with tarfile.open(path, "r:bz2") as archive:
        for member in archive:
            name = PurePosixPath(member.name)
            if (name.is_absolute() or ".." in name.parts or "\\" in member.name
                    or ":" in member.name or not (member.isfile() or member.isdir())):
                raise EvidenceError(f"Unsafe source archive member: {member.name}")
            normalized = str(name)
            if normalized in seen and member.isfile():
                raise EvidenceError(f"Duplicate source archive member: {member.name}")
            seen.add(normalized)
            if member.isfile() and member.name.endswith(".java"):
                java_files += 1
    if not java_files:
        raise EvidenceError("The supplied archive contains no Java source files")
    return java_files


def parse_test_evidence(log: str, failing_text: str | None) -> dict[str, Any]:
    counts = re.findall(r"^Failing tests:\s*(\d+)\s*$", log, flags=re.MULTILINE)
    if len(counts) != 1:
        raise EvidenceError("Expected exactly one Defects4J 'Failing tests' result")
    if failing_text is None:
        raise EvidenceError("Defects4J did not produce failing_tests")
    if re.search(r'SQA_HARNESS|TestTimedOutException|OutOfMemoryError|NoClassDefFoundError|NoSuchMethodError', failing_text):
        raise EvidenceError("Harness, timeout, or linkage error cannot be counted as a detected fault")
    failures = re.findall(r"^---\s+(\S[^\r\n]*)\s*$", failing_text, flags=re.MULTILINE)
    failures = [failure.strip() for failure in failures]
    count = int(counts[0])
    if count != len(failures) or len(set(failures)) != len(failures):
        raise EvidenceError("Failure count disagrees with unique failing_tests entries")
    if count == 0 and failing_text.strip():
        raise EvidenceError("Nonempty failing_tests contradicts a passing test run")
    return {"failure_count": count, "failing_tests": failures}


def parse_coverage_csv(content: str) -> dict[str, int]:
    rows = list(csv.DictReader(io.StringIO(content)))
    names = {
        "line_total": "LinesTotal", "line_covered": "LinesCovered",
        "branch_total": "ConditionsTotal", "branch_covered": "ConditionsCovered",
    }
    if len(rows) != 1:
        raise EvidenceError("Expected one coverage summary row")
    result: dict[str, int] = {}
    for destination, source in names.items():
        raw = rows[0].get(source)
        if raw is None or not re.fullmatch(r"\d+", raw.strip()):
            raise EvidenceError(f"Missing or invalid coverage counter: {source}")
        result[destination] = int(raw)
    for kind in ("line", "branch"):
        if result[f"{kind}_covered"] > result[f"{kind}_total"]:
            raise EvidenceError(f"Covered {kind} count exceeds total")
    return result


def run_command(command: list[str], cwd: Path, directory: Path, timeout: float) -> dict[str, Any]:
    directory.mkdir(parents=True, exist_ok=False)
    started = time.monotonic()
    stage: dict[str, Any] = {
        "command": command, "cwd": str(cwd), "started_at_utc": utc_now(),
        "timeout_seconds": timeout, "exit_code": None, "timed_out": False,
        "log": str(directory / "command.log"),
    }
    environment = os.environ.copy()
    environment["TZ"] = "America/Los_Angeles"
    try:
        with (directory / "command.log").open("w", encoding="utf-8") as stream:
            process = subprocess.Popen(
                command, cwd=cwd, env=environment, stdout=stream, stderr=subprocess.STDOUT,
                start_new_session=os.name != "nt",
            )
            try:
                stage["exit_code"] = process.wait(timeout=timeout)
            except subprocess.TimeoutExpired:
                stage["timed_out"] = True
                # Kill descendant build/JVM processes in the supported Linux runtime.
                if os.name != "nt":
                    try:
                        os.killpg(process.pid, signal.SIGKILL)
                    except ProcessLookupError:
                        pass
                else:
                    process.kill()
                stage["exit_code"] = process.wait()
    except OSError as error:
        stage["error"] = f"{type(error).__name__}: {error}"
    stage["ended_at_utc"] = utc_now()
    stage["duration_seconds"] = time.monotonic() - started
    (directory / "command.json").write_text(json.dumps(stage, indent=2) + "\n", encoding="utf-8")
    return stage


def copy_evidence(checkout: Path, destination: Path, names: tuple[str, ...]) -> None:
    for name in names:
        source = checkout / name
        if source.is_file():
            # Evidence provenance is bound by retained bytes/hashes. Unix metadata
            # copying can fail on a Windows-mounted output directory in WSL.
            shutil.copyfile(source, destination / name)


def runtime_versions(d4j: str, output: Path, timeout: float) -> dict[str, Any]:
    """Collect version evidence without interpreting an unavailable version as known."""
    result: dict[str, Any] = {"defects4j": None, "defects4j_commit": None, "java": None}
    java = run_command(["java", "-version"], output, output / "java-version", min(timeout, 30.0))
    java_text = (output / "java-version" / "command.log").read_text(encoding="utf-8", errors="replace")
    if java["exit_code"] == 0 and not java["timed_out"]:
        result["java"] = java_text.strip()
    result["java_command"] = java
    executable = shutil.which(d4j)
    if executable:
        resolved = Path(executable).resolve()
        if len(resolved.parents) >= 3:
            root = resolved.parents[2]
            for readme in (root / "README.md", root / "README"):
                if readme.is_file():
                    match = re.search(r"Defects4J\s*--\s*version\s+([\d.]+)", readme.read_text(encoding="utf-8", errors="replace"))
                    if match:
                        result["defects4j"] = match.group(1)
                        break
            if (root / ".git").exists():
                git = run_command(["git", "-C", str(root), "rev-parse", "HEAD"], output, output / "d4j-version", min(timeout, 30.0))
                if git["exit_code"] == 0 and not git["timed_out"]:
                    commit = (output / "d4j-version" / "command.log").read_text(encoding="utf-8").strip()
                    if re.fullmatch(r"[0-9a-f]{40,64}", commit):
                        result["defects4j_commit"] = commit
                result["defects4j_commit_command"] = git
    return result


def clear_generated_evidence(checkout: Path, names: tuple[str, ...]) -> None:
    # Fixed, framework-owned output filenames only; no recursive deletion.
    for name in names:
        candidate = checkout / name
        if candidate.is_file() or candidate.is_symlink():
            candidate.unlink()


def inspect_test_stage(stage: dict[str, Any], directory: Path) -> dict[str, Any]:
    if stage["timed_out"] or stage["exit_code"] != 0:
        raise EvidenceError("Test command failed or timed out; no test result inferred")
    failures = directory / "failing_tests"
    parsed = parse_test_evidence(
        (directory / "command.log").read_text(encoding="utf-8", errors="replace"),
        failures.read_text(encoding="utf-8", errors="replace") if failures.is_file() else None,
    )
    stage.update(parsed)
    return parsed


def evaluate_run(config: EvaluationConfig) -> dict[str, Any]:
    """Execute an explicit suite; return its record, including failed/partial runs.

    Invalid configuration and an existing output directory raise immediately.
    Later failures are captured in record.json. Existing run data is never reused.
    """
    validate_config(config)
    output = Path(config.output).resolve()
    output.mkdir(parents=True, exist_ok=False)
    started = time.monotonic()
    fixed = Path(config.fixed_worktree).resolve()
    buggy = Path(config.buggy_worktree).resolve()
    record: dict[str, Any] = {
        "schema_version": 1, "run_id": output.name,
        "project": config.project, "bug_id": config.bug_id, "generator": config.generator,
        "seed": config.seed, "budget": config.budget, "status": "running",
        "test_count": config.test_count,
        "test_count_source": "generator_manifest" if config.test_count is not None else None,
        "compile_status": "unknown", "fault_detected": None,
        "line_covered": None, "line_total": None, "branch_covered": None, "branch_total": None,
        "duration_seconds": None, "generation_seconds": config.generation_seconds,
        "artifact_path": str(output), "started_at_utc": utc_now(),
        "timezone": "America/Los_Angeles", "coverage_engine": "Cobertura",
        "fixed_worktree": str(fixed), "buggy_worktree": str(buggy),
        "stages": {}, "limitations": [
            "Two passing fixed runs check repeatability but cannot prove absence of flakiness."
        ],
    }
    record_path = output / "record.json"
    write_record(record_path, record)
    stage_name = "preflight"
    try:
        validate_worktree(fixed, config.project, f"{config.bug_id}f")
        validate_worktree(buggy, config.project, f"{config.bug_id}b")
        suite = Path(config.suite).resolve()
        record["java_source_files"] = validate_archive(suite)
        archive = output / f"{config.project}-{config.bug_id}f-{config.generator}.{config.seed}.tar.bz2"
        shutil.copyfile(suite, archive)
        record["suite_path"] = str(archive)
        record["source_suite_path"] = str(suite)
        record["suite_sha256"] = hashlib.sha256(archive.read_bytes()).hexdigest()
        d4j = str(Path(config.d4j).resolve()) if Path(config.d4j).is_file() else config.d4j
        record["d4j_executable"] = d4j
        record["stages"]["environment"] = run_command(
            [d4j, "env"], output, output / "environment", min(config.timeout_seconds, 60.0),
        )
        record["versions"] = runtime_versions(d4j, output, config.timeout_seconds)
        java_version = re.search(r'version\s+"?(\d+)', record["versions"]["java"] or "")
        if java_version is None or java_version.group(1) != "11":
            raise EvidenceError("Java 11 must be available on PATH for Defects4J evaluation")
        classes = output / "instrument-classes.txt"
        if config.classes_file is not None:
            shutil.copyfile(Path(config.classes_file).resolve(), classes)
            record["coverage_scope"] = "explicit_classes"
        else:
            stage_name = "export-classes"
            stage = run_command(
                [d4j, "export", "-w", str(fixed), "-p", "classes.modified", "-o", str(classes)],
                output, output / stage_name, config.timeout_seconds,
            )
            record["stages"][stage_name] = stage
            if stage["timed_out"] or stage["exit_code"] != 0:
                raise EvidenceError("Could not export coverage class scope")
            record["coverage_scope"] = "classes_modified_by_fix"
        class_names = [line.strip() for line in classes.read_text(encoding="utf-8").splitlines() if line.strip()]
        if not class_names or any(not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", name) for name in class_names):
            raise EvidenceError("Coverage class list is empty or invalid")
        record["instrument_classes"] = class_names

        for stage_name, checkout in (("fixed-1", fixed), ("fixed-2", fixed), ("buggy", buggy)):
            clear_generated_evidence(checkout, ("failing_tests", "all_tests", "sqa-stage-counts.json"))
            directory = output / stage_name
            stage = run_command(
                [d4j, "test", "-w", str(checkout), "-s", str(archive)],
                output, directory, config.timeout_seconds,
            )
            record["stages"][stage_name] = stage
            copy_evidence(checkout, directory, ("failing_tests", "all_tests", "sqa-stage-counts.json"))
            try:
                result = inspect_test_stage(stage, directory)
            except EvidenceError:
                log = (directory / "command.log").read_text(encoding="utf-8", errors="replace")
                if re.search(r"Cannot compile|Compilation failed|compile.*FAILED", log, flags=re.IGNORECASE):
                    record["compile_status"] = "failed"
                raise
            if config.test_count is not None and result["failure_count"] > config.test_count:
                raise EvidenceError("Failures exceed declared generated test count")
            if stage_name.startswith("fixed") and result["failure_count"]:
                record["status"] = "invalid"
                record["fixed_validation"] = "failed" if stage_name == "fixed-1" else "unstable"
                raise EvidenceError("Generated suite must pass on both fixed-revision runs")
            write_record(record_path, record)

        record["compile_status"] = "passed"
        record["fixed_validation"] = "passed_twice"
        record["fault_detected"] = record["stages"]["buggy"]["failure_count"] > 0
        record["triggering_tests"] = record["stages"]["buggy"]["failing_tests"]
        stage_name = "coverage"
        clear_generated_evidence(fixed, ("failing_tests", "all_tests", "summary.csv", "coverage.xml", "sqa-stage-counts.json"))
        directory = output / stage_name
        stage = run_command(
            [d4j, "coverage", "-w", str(fixed), "-s", str(archive), "-i", str(classes)],
            output, directory, config.timeout_seconds,
        )
        record["stages"][stage_name] = stage
        copy_evidence(fixed, directory, ("failing_tests", "all_tests", "summary.csv", "coverage.xml", "sqa-stage-counts.json"))
        if stage["timed_out"] or stage["exit_code"] != 0:
            raise EvidenceError("Coverage command failed or timed out")
        failures = directory / "failing_tests"
        if not failures.is_file() or failures.read_text(encoding="utf-8").strip():
            raise EvidenceError("Coverage run did not establish passing fixed tests")
        if not (directory / "coverage.xml").is_file():
            raise EvidenceError("Coverage XML evidence is missing")
        record.update(parse_coverage_csv((directory / "summary.csv").read_text(encoding="utf-8")))
        record["status"] = "complete"
    except Exception as error:
        if record["status"] != "invalid":
            record["status"] = "partial" if record["fault_detected"] is not None else "failed"
        record["failed_stage"] = stage_name
        record["error"] = f"{type(error).__name__}: {error}"
    finally:
        record["ended_at_utc"] = utc_now()
        record["duration_seconds"] = time.monotonic() - started
        if config.generation_seconds is not None:
            record["total_seconds"] = record["duration_seconds"] + config.generation_seconds
        write_record(record_path, record)
    return record


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--project", required=True)
    parser.add_argument("--bug-id", required=True, type=int)
    parser.add_argument("--generator", required=True)
    parser.add_argument("--seed", required=True, type=int)
    parser.add_argument("--budget", required=True, type=int)
    for option in ("suite", "buggy-worktree", "fixed-worktree", "output"):
        parser.add_argument(f"--{option}", required=True, type=Path)
    parser.add_argument("--classes-file", type=Path)
    parser.add_argument("--d4j", default="defects4j")
    parser.add_argument("--generation-seconds", type=float)
    parser.add_argument("--test-count", type=int)
    parser.add_argument("--timeout-seconds", type=float, default=300.0)
    args = parser.parse_args()
    try:
        record = evaluate_run(EvaluationConfig(**vars(args)))
    except (ValueError, OSError) as error:
        parser.error(str(error))
    print(json.dumps(record, indent=2))
    return 0 if record["status"] == "complete" else 1


if __name__ == "__main__":
    raise SystemExit(main())
