"""Shared local stage mechanics. Never claim/publish to an unspecified queue."""
from __future__ import annotations

from pathlib import Path
import re

from .common import ROOT, contained, job_relative, sha256, write_json
from run import stage, query
from evaluate import validate_worktree


def new_worktrees(root, job, phase):
    base = contained(root, job_relative(job) / phase)
    # Refuse reuse, including remnants left by an interrupted checkout.
    base.mkdir(parents=True, exist_ok=False)
    return base


def fixed_sources(tree, classes, source_dir):
    tree = Path(tree).resolve()
    source_root = contained(tree, Path(source_dir))
    result = {}
    for name in classes:
        if not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", name):
            raise ValueError("Invalid modified class")
        relative = Path(*name.split("$", 1)[0].split(".")).with_suffix(".java")
        path = contained(source_root, relative)
        if not path.is_file():
            raise ValueError(f"Missing modified fixed source: {name}")
        result[path.relative_to(tree).as_posix()] = sha256(path)
    if not result:
        raise ValueError("No fixed source evidence")
    return result


def prepare_evaluation(d4j, job, trees, output, timeout):
    output.mkdir()
    if job["bug_id"] not in {int(bug) for bug in query(d4j, "bids", "-p", job["project"]).splitlines()}:
        raise ValueError("Bug is not active in installed Defects4J")
    paths = {}
    for revision in ("f", "b"):
        tree = trees / job["project"] / str(job["bug_id"]) / revision
        tree.parent.mkdir(parents=True, exist_ok=True)
        stage([d4j, "checkout", "-p", job["project"], "-v", f"{job['bug_id']}{revision}", "-w", tree],
              ROOT, output / f"checkout-{revision}", timeout)
        validate_worktree(tree, job["project"], f"{job['bug_id']}{revision}")
        stage([d4j, "compile", "-w", tree], ROOT, output / f"compile-{revision}", timeout)
        paths[revision] = tree
    for prop in ("classes.modified", "dir.src.classes"):
        stage([d4j, "export", "-w", paths["f"], "-p", prop, "-o", output / f"{prop}.txt"],
              ROOT, output / f"export-{prop}", timeout)
    classes = (output / "classes.modified.txt").read_text(encoding="utf-8").strip().splitlines()
    source_dir = (output / "dir.src.classes.txt").read_text(encoding="utf-8").strip()
    sources = fixed_sources(paths["f"], classes, source_dir)
    write_json(output / "fixed-source-hashes.json", sources)
    return paths, output / "classes.modified.txt", sources


def artifact_index(output):
    return {p.relative_to(output).as_posix(): sha256(p) for p in sorted(output.rglob("*")) if p.is_file()}


def normalize_evaluation(record):
    if record["status"] == "complete":
        return "complete"
    if record.get("compile_status") == "failed":
        return "compile_failed"
    if record.get("fixed_validation") in {"failed", "unstable"}:
        return "fixed_failed"
    failed = record.get("failed_stage")
    current = record.get("stages", {}).get(failed, {})
    if current.get("timed_out"):
        return "timeout"
    if failed == "coverage":
        return "coverage_failed"
    return "environment_failed"
