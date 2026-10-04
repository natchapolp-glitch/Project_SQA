#!/usr/bin/env python3
"""Show stored measurements or replay a measured archive; never request AI."""
import argparse
import hashlib
import json
from pathlib import Path
import sys


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--snapshot", type=Path, default=Path(__file__).resolve().parents[1])
    cli.add_argument("--case", default="Csv-1")
    cli.add_argument("--method", choices=("cmaes", "fscs-art", "kku-claude", "kku-gemini"))
    cli.add_argument("--replay", action="store_true")
    cli.add_argument("--output", type=Path)
    cli.add_argument("--worktrees", type=Path, default=Path("/home/team/sqa-round2/worktrees/round2-solo-pilot-20261004"))
    cli.add_argument("--d4j", default="/home/team/sqa-round2/defects4j/framework/bin/defects4j")
    args = cli.parse_args()
    root = args.snapshot.resolve()
    manifest = json.loads((root / "Experiment/snapshot.json").read_text(encoding="utf-8"))
    entries = [r for r in manifest["results"] if r["case"] == args.case and (not args.method or r["method"] == args.method)]
    if not entries:
        cli.error("No recorded result for the selected case/method")
    for entry in entries:
        row = json.loads((root / entry["outcome"]).read_text(encoding="utf-8"))
        print(json.dumps({k: row.get(k) for k in ("case", "method", "state", "valid_on_fixed", "fault_detected",
                                                   "generated_test_methods", "executed_tests", "line_covered", "line_total",
                                                   "branch_covered", "branch_total", "error")}, ensure_ascii=False))
    if not args.replay:
        return
    if sys.platform != "linux" or not args.output:
        cli.error("Fresh replay needs Linux/WSL and an explicit new --output directory")
    if args.output.exists():
        cli.error("Output already exists: old evidence will not be overwritten")
    selected = [r for r in entries if r["state"] == "DONE" and r["archive"]]
    if len(selected) != len(entries):
        cli.error("Only completed measured archives can be replayed; failed evidence remains displayed")
    runtime = root / "Experiment/reproduction/runtime/scripts/study"
    sys.path.insert(0, str(runtime))
    from evaluate import EvaluationConfig, evaluate_run
    from api854.common import cpu_slot
    project, bug = args.case.rsplit("-", 1)
    observations = []
    with cpu_slot(args.worktrees.parent):
        args.output.mkdir(parents=True, exist_ok=False)
        for entry in selected:
            archive = root / entry["archive"]
            if hashlib.sha256(archive.read_bytes()).hexdigest() != entry["archive_sha256"]:
                raise ValueError("Archive hash differs from the measured suite")
            scope = args.output / f"{entry['method']}-classes.txt"
            scope.write_text("\n".join(entry["instrument_classes"]) + "\n", encoding="utf-8")
            record = evaluate_run(EvaluationConfig(project, int(bug), entry["method"], 101, 30, archive,
                                    args.worktrees / args.case / "b", args.worktrees / args.case / "f",
                                    args.output / entry["method"], args.d4j, scope,
                                    test_count=entry["generated_test_methods"], timeout_seconds=300))
            observations.append({"method": entry["method"], "status": record["status"],
                                 "fault_detected": record.get("fault_detected"), "suite_sha256": record["suite_sha256"],
                                 "duration_seconds": record["duration_seconds"]})
            print(json.dumps(observations[-1]), flush=True)
    (args.output / "receipt.json").write_text(json.dumps({"case": args.case, "observations": observations,
          "ai_requests": 0, "independent_generation_repeat": False}, indent=2) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
