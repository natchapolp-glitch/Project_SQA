"""Immutable Csv intake and full Defects4J development evaluation, without API/queue use.

Run intake in the repository; run measure in WSL with Java 11. Outputs are exclusive.
This is an evaluation of existing suites, not a new generation or a Gate A approval.
"""
from __future__ import annotations

import argparse
import hashlib
import io
import json
import os
from pathlib import Path, PurePosixPath
import subprocess
import sys
import tarfile
import time

ROOT = Path(__file__).resolve().parents[2]
BASE = "63ad195623c2ed3f67f3ae232c00c54d3160ce72"
PEER = "a4a38a5e"
DAY = "output/api854-20261004"
INTAKE = ROOT / DAY / "aom-ready-csv-intake-v1"
RUN = ROOT / DAY / "aom-ready-csv-d4j-v1"
PAIR = "output/api854-20261003/aom-continuation-v12-development-v1"
PREP = "output/api854-20261003/prepare-v12-graphics-development-v1"
PACKETS = ("champ-csv-four-approach-summary-v1", "champ-csv-development-generation-v1",
           "champ-csv-native-measurement-v4")
CONDITION = "api854-20261004-csv-v12-d4j-development-v1"
APPROACHES = ("cmaes", "fscs-art", "kku-claude", "kku-gemini")


def digest(data):
    return hashlib.sha256(data).hexdigest()


def load(path):
    return json.loads(Path(path).read_text(encoding="utf-8-sig"))


def save(path, value):
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("x", encoding="utf-8", newline="\n") as stream:
        stream.write(json.dumps(value, indent=2, ensure_ascii=False, allow_nan=False) + "\n")


def safe_child(root, name):
    relative = PurePosixPath(name)
    if relative.is_absolute() or ".." in relative.parts or "\\" in name or ":" in name:
        raise ValueError(f"Unsafe path: {name}")
    path = (root / name).resolve()
    if not path.is_relative_to(root.resolve()) or path == root.resolve():
        raise ValueError(f"Path escapes root: {name}")
    return path


def git(*args):
    return subprocess.check_output(["git", "-c", "core.autocrlf=false", *args], cwd=ROOT)


def export(commit, paths, destination):
    data = git("archive", commit, "--", *paths)
    with tarfile.open(fileobj=io.BytesIO(data)) as archive:
        for member in archive:
            path = safe_child(destination, member.name)
            if member.isdir():
                path.mkdir(parents=True, exist_ok=True)
            elif member.isfile():
                path.parent.mkdir(parents=True, exist_ok=True)
                with path.open("xb") as stream:
                    stream.write(archive.extractfile(member).read())
            else:
                raise ValueError(f"Non-regular git archive entry: {member.name}")


def verify_manifest(folder):
    values = load(folder / "checksums.json")
    for name, expected in values.items():
        if digest(safe_child(folder, name).read_bytes()) != expected:
            raise ValueError(f"Checksum differs: {folder.name}/{name}")
    return len(values)


def seal(folder):
    save(folder / "checksums.json", {
        p.relative_to(folder).as_posix(): digest(p.read_bytes())
        for p in sorted(folder.rglob("*")) if p.is_file() and p != folder / "checksums.json"
    })


def intake():
    INTAKE.mkdir(parents=True, exist_ok=False)
    peer = git("rev-parse", PEER).decode().strip()
    export(peer, [f"{DAY}/{name}" for name in PACKETS] +
           ["docs/api854/CHAMP_READY_RESULTS_FIRST_TH.md"], INTAKE / "peer")
    protocol = json.loads(git("show", f"{BASE}:{PAIR}/protocol.proposal.json"))
    paths = list(protocol["source_sha256"]) + [f"{PAIR}/protocol.proposal.json",
            f"{PAIR}/runner-plan.json", f"{PREP}/index.json"]
    export(BASE, paths, INTAKE / "baseline")
    count = sum(verify_manifest(INTAKE / "peer" / DAY / p) for p in PACKETS)
    for name, expected in protocol["source_sha256"].items():
        if digest((INTAKE / "baseline" / name).read_bytes()) != expected:
            raise ValueError(f"Baseline pin differs: {name}")
    archives = {}
    native = INTAKE / "peer" / DAY / PACKETS[2]
    for approach in ("cmaes", "fscs-art", "kku-gemini"):
        archive_path = native / approach / "packaged-suite/suite.tar.bz2"
        manifest = load(archive_path.with_name("suite-manifest.json"))
        with tarfile.open(archive_path) as archive:
            source_hashes = {}
            for entry in archive:
                safe_child(INTAKE, entry.name)
                if not entry.isfile() or not entry.name.endswith(".java"):
                    raise ValueError("Suite archive must contain only regular Java source files")
                if entry.name in source_hashes:
                    raise ValueError("Duplicate source in suite")
                source_hashes[entry.name] = digest(archive.extractfile(entry).read())
        if source_hashes != manifest["source_sha256"]:
            raise ValueError(f"Packaged sources differ: {approach}")
        archives[approach] = {"path": archive_path.relative_to(ROOT).as_posix(),
                              "sha256": digest(archive_path.read_bytes()),
                              "source_sha256": source_hashes,
                              "test_count": manifest["test_count"]}
    save(INTAKE / "receipt.json", {"baseline_commit": BASE, "peer_commit": peer,
        "peer_manifest_entries_verified": count, "runtime_pins_verified": len(protocol["source_sha256"]),
        "archives": archives, "condition": CONDITION, "primary_result": False,
        "gate_a_approved": False, "api_requests": 0, "queue_mutations": 0,
        "v13_status": "retained intact; parked for ready-results-first work"})
    seal(INTAKE)
    print(json.dumps({"intake": str(INTAKE), "verified": count, "archives": list(archives)}))


def measure():
    if sys.platform != "linux":
        raise ValueError("Measurement requires Linux/WSL and Java 11")
    verify_manifest(INTAKE)
    baseline = INTAKE / "baseline"
    sys.path.insert(0, str(baseline / "scripts/study"))
    from evaluate import EvaluationConfig, evaluate_run, run_command, inspect_test_stage, copy_evidence, clear_generated_evidence
    from api854.common import cpu_slot, implementation_hashes
    from api854.environment import inspect_environment
    from api854.worker import prepare_evaluation
    protocol = load(baseline / PAIR / "protocol.proposal.json")
    if implementation_hashes() != protocol["source_sha256"]:
        raise ValueError("Frozen v12 runtime differs")
    RUN.mkdir(parents=True, exist_ok=False)
    started = time.monotonic()
    d4j = "/home/team/sqa-round2/defects4j/framework/bin/defects4j"
    lock_root = Path("/home/team/sqa-round2/worktrees")
    trees = lock_root / "aom-ready-csv-d4j-v1"
    plan = {"condition": CONDITION, "baseline_commit": BASE, "host_id": "aom-pc1",
            "actual_host": os.uname().nodename, "cpu_slots": 1, "cpu_lock_root": str(lock_root),
            "worktrees": str(trees), "timezone": "America/Los_Angeles", "java_major": 11,
            "defects4j": "3.0.1", "suite_policy": "unchanged received archives, no regeneration/repair/pruning",
            "coverage_scope": "classes.modified from installed Defects4J Csv-1f",
            "gemini_policy": "fixed-1 then a separate unchanged fixed-2 confirmation; no buggy/coverage",
            "native_condition": "api854-20261004-csv-six-target-native-development-v1",
            "generation_timezone": "UTC", "generation_java_major": 17,
            "primary_result": False, "gate_a_approved": False, "api_requests": 0,
            "queue_mutations": 0, "final_reserve": None,
            "protocol_sha256": digest((baseline / PAIR / "protocol.proposal.json").read_bytes()),
            "runner_sha256": digest((baseline / PAIR / "runner-plan.json").read_bytes()),
            "index_sha256": digest((baseline / PREP / "index.json").read_bytes()),
            "evaluator_sha256": digest((baseline / "scripts/study/evaluate.py").read_bytes()),
            "producer_sha256": digest(Path(__file__).read_bytes())}
    save(RUN / "preexecution-plan.json", plan)
    records = {}
    with cpu_slot(lock_root):
        env = inspect_environment(d4j, RUN / "environment")
        if not env["ready"]:
            raise ValueError("Environment failed; preserve preexecution packet")
        trees.mkdir(exist_ok=False)
        paths, classes, sources = prepare_evaluation(d4j, {"project": "Csv", "bug_id": 1},
            trees, RUN / "prepare", 900)
        # Source binding is exact bytes, retained independently of Champ's native extraction.
        save(RUN / "host-binding.json", {**plan, "environment": env,
             "fixed_source_sha256": sources,
             "checkout_config_sha256": {r: digest((t / ".defects4j.config").read_bytes()) for r, t in paths.items()}})
        received = load(INTAKE / "receipt.json")["archives"]
        for approach in ("cmaes", "fscs-art", "kku-gemini"):
            item = received[approach]
            suite = ROOT / item["path"]
            if digest(suite.read_bytes()) != item["sha256"]:
                raise ValueError("Received suite changed before evaluation")
            record = evaluate_run(EvaluationConfig(project="Csv", bug_id=1,
                generator=approach, seed=101, budget=30, suite=suite,
                buggy_worktree=paths["b"], fixed_worktree=paths["f"],
                output=RUN / approach, d4j=d4j, classes_file=classes,
                test_count=item["test_count"], timeout_seconds=900))
            records[approach] = record
            # The sealed evaluator stops at first fixed failure. Confirm repeatability
            # separately without patching it or granting eligibility to the invalid suite.
            if approach == "kku-gemini" and record.get("fixed_validation") == "failed":
                folder = RUN / "kku-gemini-fixed-repeat"
                clear_generated_evidence(paths["f"], ("failing_tests", "all_tests", "sqa-stage-counts.json"))
                command = run_command([d4j, "test", "-w", str(paths["f"]), "-s", record["suite_path"]],
                                      RUN, folder, 900)
                copy_evidence(paths["f"], folder, ("failing_tests", "all_tests", "sqa-stage-counts.json"))
                inspect_test_stage(command, folder)
                save(folder / "confirmation.json", {"command": command, "suite_sha256": item["sha256"],
                    "purpose": "confirm rejected fixed suite unchanged; no eligibility or model retry"})
            print(json.dumps({"approach": approach, "status": record["status"],
                              "error": record.get("error"), "seconds": record["duration_seconds"]}), flush=True)
        if implementation_hashes() != protocol["source_sha256"]:
            raise ValueError("Frozen runtime changed during evaluation")
    save(RUN / "receipt.json", {"condition": CONDITION, "records": {k: v["status"] for k,v in records.items()},
        "duration_seconds": time.monotonic() - started, "runtime_pins_unchanged": True,
        "suite_hashes_unchanged": all(digest((ROOT / v["path"]).read_bytes()) == v["sha256"] for v in received.values()),
        "claude_outcome": "invalid_generation_truncated_no_suite; no Defects4J command",
        "primary_result": False, "gate_a_approved": False, "api_requests": 0, "queue_mutations": 0})
    seal(RUN)


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("stage", choices=("intake", "measure", "verify"))
    arg = cli.parse_args()
    if arg.stage == "intake":
        intake()
    elif arg.stage == "measure":
        measure()
    else:
        print(json.dumps({"intake": verify_manifest(INTAKE), "run": verify_manifest(RUN)}))
