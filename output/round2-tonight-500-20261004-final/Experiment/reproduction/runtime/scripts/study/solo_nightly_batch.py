#!/usr/bin/env python3
"""Deadline-bounded batch; immutable per-job account assignment, one CPU slot."""
import argparse
from datetime import datetime, timezone
from pathlib import Path

import solo_ai_nightly as ai
import solo_batch as batch


def schedule(config, aliases, limit):
    existing = {"Csv-1", "Lang-1", "Math-1"}
    projects = sorted(config["inventory"])
    cases = []
    for index in range(max(len(config["inventory"][p]) for p in projects)):
        for project in projects:
            bugs = config["inventory"][project]
            if index < len(bugs):
                case = f"{project}-{bugs[index]}"
                if case not in existing:
                    cases.append(case)
    return [{"case": c, "account_alias": aliases[i % len(aliases)]} for i, c in enumerate(cases[:limit])]


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--output", type=Path, required=True)
    cli.add_argument("--offline", type=Path, required=True)
    cli.add_argument("--preflight", type=Path, required=True)
    cli.add_argument("--secrets", type=Path, required=True)
    cli.add_argument("--limit", type=int, default=14)
    cli.add_argument("--cutoff", default="2026-10-04T15:00:00+00:00")
    args = cli.parse_args()
    output, offline = args.output.resolve(), args.offline.resolve()
    if not (output / "Experiment/protocol/ai.json").exists():
        ai.init(output, offline, args.preflight.resolve())
    protocol, parent, config = ai.load_protocol(output)
    if parent != offline or args.limit < 1:
        cli.error("Invalid parent/limit")
    aliases = [a for a in protocol["known_accounts"] if a != "a01"] + ["a01"]
    plan_path = output / "Experiment/batch-plan.json"
    planned = {"schema": "nightly-plan.v1", "jobs": schedule(config, aliases, args.limit),
               "cutoff_utc": args.cutoff, "stop_dispatch_seconds_before_cutoff": 1200,
               "policy": "preserve each failed outcome; no retry to obtain success; aliases fixed per case before dispatch",
               "cpu_slots": 1, "ai_protocol_sha256": batch.sha(output / "Experiment/protocol/ai.json"),
               "controller_sha256": batch.sha(Path(__file__)), "scope": "first remaining bug per project, then round-robin expansion"}
    if plan_path.exists():
        if batch.read(plan_path) != planned:
            cli.error("Batch plan changed; create a new manifest/output")
    else:
        batch.write(plan_path, planned)
    cutoff = datetime.fromisoformat(planned["cutoff_utc"])
    for job in planned["jobs"]:
        if (cutoff - datetime.now(timezone.utc)).total_seconds() < 1200:
            print("Cutoff guard: no new case dispatched", flush=True)
            break
        case, alias = job["case"], job["account_alias"]
        print(f"CASE {case} ACCOUNT {alias}", flush=True)
        batch.run_algorithms(offline, [case], config, ["cmaes", "fscs-art"])
        for method in ai.METHODS:
            if (cutoff - datetime.now(timezone.utc)).total_seconds() < 1200:
                print("Cutoff guard: AI job not dispatched", flush=True)
                return
            ai.run_case(output, offline, case, method, alias, args.secrets, protocol, config)


if __name__ == "__main__":
    main()
