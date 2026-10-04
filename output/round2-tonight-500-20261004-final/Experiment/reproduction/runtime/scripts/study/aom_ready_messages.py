"""Receive Champ's separate Messages condition; preserve baseline invalid outcomes intact."""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import sys
import time

from aom_ready_csv import ROOT, INTAKE, DAY, BASE, PAIR, digest, load, save, seal, verify_manifest, export, git

PEER = "d98fcceee344edb9015854914c659722578d0ffe"
RECEIVED = ROOT / DAY / "aom-ready-messages-intake-v1"
MEASURED = ROOT / DAY / "aom-ready-messages-d4j-v1"
PACKETS = ("champ-csv-messages-generation-v1", "champ-csv-messages-generation-v2",
           "champ-csv-messages-native-measurement-v1", "champ-provider-messages-calibration-v1",
           "champ-cli-development-generation-v2", "champ-cli-native-measurement-v1",
           "champ-cli-order-oracle-audit-v2", "champ-ready-results-audit-v1")


def intake():
    RECEIVED.mkdir(parents=True, exist_ok=False)
    export(PEER, [f"{DAY}/{name}" for name in PACKETS] +
        ["docs/api854/CHAMP_READY_RESULTS_UPDATE_TH.md",
         "scripts/study/api854/replay_csv_development_d4j.py"], RECEIVED / "peer")
    counts = {name: verify_manifest(RECEIVED / "peer" / DAY / name) for name in PACKETS}
    save(RECEIVED / "receipt.json", {"peer_commit": PEER, "verified_packets": counts,
         "baseline": BASE, "prior_aom_intake_manifest_sha256": digest((INTAKE / "checksums.json").read_bytes()),
         "api_requests": 0, "queue_mutations": 0, "baseline_invalid_outcomes_preserved": True,
         "cli_fscs_raw_fault": "quarantine; known unordered-options oracle mismatch, not accepted as scientific fault"})
    seal(RECEIVED)
    print(json.dumps({"verified_packets": counts}))


def measure():
    if sys.platform != "linux":
        raise ValueError("Use WSL/Java11 actual Aom host")
    verify_manifest(INTAKE)
    verify_manifest(RECEIVED)
    baseline = INTAKE / "baseline"
    sys.path.insert(0, str(baseline / "scripts/study"))
    from evaluate import EvaluationConfig, evaluate_run, validate_archive, run_command, validate_worktree
    from api854.common import cpu_slot, implementation_hashes
    from api854.worker import fixed_sources
    protocol = load(baseline / PAIR / "protocol.proposal.json")
    if implementation_hashes() != protocol["source_sha256"]:
        raise ValueError("v12 runtime does not match")
    native = RECEIVED / "peer" / DAY / "champ-csv-messages-native-measurement-v1"
    receipt = load(native / "receipt.json")
    preseal = load(native / "preexecution-seal.json")
    if preseal["runtime_source_sha256"] != protocol["source_sha256"]:
        raise ValueError("Generation/runtime mismatch")
    if len(receipt["records"]) != 4 or any(v["status"] != "native_fixed_twice_buggy_coverage_measured" for v in receipt["records"]):
        raise ValueError("Expected four unchanged measured suites in this declared condition")
    MEASURED.mkdir(parents=True, exist_ok=False)
    d4j = "/home/team/sqa-round2/defects4j/framework/bin/defects4j"
    worktrees = Path("/home/team/sqa-round2/worktrees")
    trees = worktrees / MEASURED.name
    condition = receipt["condition"] + "-d4j-java11-los-angeles-replay-v1"
    save(MEASURED / "preexecution-plan.json", {"condition": condition,
        "generation_condition": receipt["condition"], "baseline_commit": BASE,
        "host": "aom-pc1", "cpu_slots": 1, "cpu_lock_root": str(worktrees), "fresh_worktrees": str(trees),
        "timezone": "America/Los_Angeles", "java_major": 11, "defects4j_version": "3.0.1",
        "runtime_source_sha256": protocol["source_sha256"],
        "protocol_sha256": digest((baseline / PAIR / "protocol.proposal.json").read_bytes()),
        "received_native_manifest_sha256": digest((native / "checksums.json").read_bytes()),
        "producer_sha256": digest(Path(__file__).read_bytes()),
        "primary_result": False, "gate_a_approved": False, "api_requests": 0, "queue_mutations": 0,
        "rules": "Fresh checkout per approach, unchanged Java/archive, no provider replay or compatibility repairs"})
    (MEASURED / "instrument-classes.txt").write_bytes(b"org.apache.commons.csv.ExtendedBufferedReader\n")
    observations = []
    started = time.monotonic()
    with cpu_slot(worktrees):
        trees.mkdir(exist_ok=False)
        for item in receipt["records"]:
            approach = item["approach"]
            folder = MEASURED / approach
            folder.mkdir()
            base = trees / approach
            base.mkdir()
            paths = {}
            for revision, label in (("f", "fixed"), ("b", "buggy")):
                tree = base / revision
                command = run_command([d4j, "checkout", "-p", "Csv", "-v", f"1{revision}", "-w", str(tree)],
                                      ROOT, folder / f"checkout-{revision}", 300)
                if command["exit_code"] != 0 or command["timed_out"]:
                    raise ValueError("Fresh checkout failed; preserve this attempt")
                validate_worktree(tree, "Csv", f"1{revision}")
                expected = preseal["production_source_sha256"][label]["sources"]
                for name, value in expected.items():
                    if digest((tree / name).read_bytes()) != value:
                        raise ValueError("Actual production bytes do not match native revisions")
                paths[revision] = tree
            archive = native / approach / "packaged-suite/suite.tar.bz2"
            manifest = load(archive.with_name("suite-manifest.json"))
            validate_archive(archive)
            if digest(archive.read_bytes()) != manifest["suite_sha256"]:
                raise ValueError("Suite manifest differs")
            if not 0 < item["declared_test_count"] <= 30:
                raise ValueError("Suite cap exceeded")
            result = evaluate_run(EvaluationConfig(project="Csv", bug_id=1, generator=approach,
                seed=101, budget=30, suite=archive, buggy_worktree=paths["b"], fixed_worktree=paths["f"],
                output=folder / "measurement", d4j=d4j, classes_file=MEASURED / "instrument-classes.txt",
                test_count=item["declared_test_count"], timeout_seconds=300))
            observations.append({"approach": approach, "status": result["status"],
                "record_path": (folder / "measurement/record.json").relative_to(ROOT).as_posix(),
                "record_sha256": digest((folder / "measurement/record.json").read_bytes()),
                "suite_sha256": result.get("suite_sha256"), "fault_detected": result["fault_detected"]})
            print(json.dumps({"approach": approach, "status": result["status"],
                              "fault_detected": result["fault_detected"], "error": result.get("error")}), flush=True)
        if implementation_hashes() != protocol["source_sha256"]:
            raise ValueError("Frozen runtime changed")
    save(MEASURED / "receipt.json", {"condition": condition, "generation_condition": receipt["condition"],
        "observations": observations, "duration_seconds": time.monotonic() - started,
        "runtime_pins_unchanged": True, "primary_results": 0, "api_requests": 0, "queue_mutations": 0,
        "gate_a_approved": False, "limits": "Review raw counters, semantic failures and unequal input domains before comparison"})
    seal(MEASURED)


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("stage", choices=("intake", "measure"))
    args = cli.parse_args()
    if args.stage == "intake":
        intake()
    else:
        measure()
