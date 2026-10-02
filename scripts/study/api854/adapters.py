"""Inventory readiness, then exercise the existing generic probe per explicit bug."""
from __future__ import annotations

import argparse
from collections import Counter
from pathlib import Path

from .common import ROOT, read_json, sha256, write_json, implementation_hashes, finite_positive, cpu_slot
from .environment import inspect_environment
from .worker import contained
from run import prepare, query


def allocation_rows(path):
    rows = read_json(path)["bugs"]
    keys = [(r["project"], r["bug_id"]) for r in rows]
    if len(keys) != 854 or len(set(keys)) != 854 or len({p for p, _ in keys}) != 17:
        raise ValueError("Expected 854 unique bugs across 17 projects")
    if Counter(r["owner"] for r in rows) != Counter(champ=285, beam=285, aom=284):
        raise ValueError("Ownership allocation differs from confirmed plan")
    return rows


def verify_installed_inventory(d4j, rows):
    installed = {(project, int(bug)) for project in query(d4j, "pids").splitlines()
                 for bug in query(d4j, "bids", "-p", project).splitlines()}
    expected = {(row["project"], row["bug_id"]) for row in rows}
    if installed != expected:
        raise ValueError(f"Installed inventory mismatch: missing={sorted(expected - installed)}, extra={sorted(installed - expected)}")
    return installed


def prepare_adapter(d4j, job, worktrees, output, timeout):
    if job["bug_id"] not in {int(bug) for bug in query(d4j, "bids", "-p", job["project"]).splitlines()}:
        raise ValueError("Bug is not active in the installed Defects4J inventory")
    # ROOT is unique to this stage/attempt, never the legacy shared project tree.
    metadata, targets = prepare(str(d4j), {"project": job["project"], "bug_id": job["bug_id"]},
                                Path(worktrees), Path(output), timeout)
    metadata["source_sha256"] = implementation_hashes()
    metadata["targets_sha256"] = sha256(metadata["targets_file"])
    metadata["classes_sha256"] = sha256(metadata["classes_file"])
    metadata["adapter_readiness"] = "supported"
    metadata["semantic_validity"] = "pending_review"
    write_json(Path(output) / "adapter.json", metadata)
    return metadata, targets


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--ownership", type=Path, default=ROOT / "experiments/configs/api854-20261003/ownership.json")
    parser.add_argument("--output", required=True, type=Path)
    parser.add_argument("--d4j", default="defects4j")
    parser.add_argument("--verify-installed", action="store_true")
    parser.add_argument("--scan", nargs="*", metavar="PROJECT:BUG", help="Explicit per-bug preflights, e.g. Closure:3 JxPath:1")
    parser.add_argument("--worktrees", type=Path)
    parser.add_argument("--timeout", type=float, default=900)
    args = parser.parse_args()
    finite_positive(args.timeout, "timeout")
    rows = allocation_rows(args.ownership)
    args.output.mkdir(parents=True, exist_ok=False)
    verified = False
    issues = []
    selected = set()
    for item in args.scan or []:
        project, bug = item.split(":")
        selected.add((project, int(bug)))
    if selected and (not args.worktrees or not selected <= {(r['project'], r['bug_id']) for r in rows}):
        parser.error("Scan requires worktrees and allocated active project:bug pairs")
    if args.verify_installed or selected:
        environment = inspect_environment(args.d4j, args.output / "environment")
        if environment["ready"]:
            try:
                verify_installed_inventory(args.d4j, rows)
                verified = True
            except Exception as error:
                issues.append(str(error))
        else:
            issues.extend(environment["issues"])
    readiness = [{**r, "readiness": "needs_adapter", "reason": "per_bug_preflight_not_attempted", "attempted": False} for r in rows]
    if verified:
        for row in readiness:
            if (row["project"], row["bug_id"]) not in selected:
                continue
            name = f"{row['project']}-{row['bug_id']}"
            tree_root = contained(args.worktrees, Path(args.output.name, name))
            try:
                with cpu_slot(args.worktrees):
                    row["attempted"] = True
                    tree_root.mkdir(parents=True, exist_ok=False)
                    metadata, targets = prepare_adapter(args.d4j, row, tree_root, args.output / name, args.timeout)
                row.update(readiness="supported", reason="checkout_build_and_shared_discovery_passed",
                           target_count=len(targets), semantic_validity="pending_review", evidence=str(args.output / name / "adapter.json"))
            except Exception as error:
                row.update(readiness="needs_adapter", reason="preflight_failed" if row["attempted"] else "deferred_cpu_slot",
                           error=f"{type(error).__name__}: {error}")
    manifest = {"schema_version": 1, "ownership_sha256": sha256(args.ownership),
                "inventory_verified_installed": verified, "issues": issues,
                "note": "Supported means checkout/build/shared declaration discovery only; pilot/semantic review is still required.",
                "bugs": readiness}
    write_json(args.output / "adapter-readiness.json", manifest)
    print(f"854 allocated bugs; installed_verified={verified}; supported={sum(r['readiness'] == 'supported' for r in readiness)}")
    return 0 if not (args.verify_installed or selected) or verified else 1


if __name__ == "__main__":
    raise SystemExit(main())
