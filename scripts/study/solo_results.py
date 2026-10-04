#!/usr/bin/env python3
"""Read-only aggregation of separate pinned offline and KKU pilot conditions."""
import argparse
from collections import Counter
import csv
from pathlib import Path

import solo_batch as batch

EXTRA = ["condition", "request_attempts", "completed_provider_responses", "prompt_tokens",
         "completion_tokens", "selected_suite", "account_alias", "model_id", "base_usable", "extension_state"]


def collect(offline, ai):
    config = batch.read(offline / "Experiment/protocol/offline.json")
    aiconfig = batch.read(ai / "Experiment/protocol/ai.json")
    offline_hash = batch.sha(offline / "Experiment/protocol/offline.json")
    if aiconfig["offline_protocol_sha256"] != offline_hash:
        raise ValueError("Different parent offline condition")
    hashes = {"offline": offline_hash, "ai": batch.sha(ai / "Experiment/protocol/ai.json")}
    outcomes = {}
    for kind, root in (("offline", offline), ("ai", ai)):
        for path in (root / "Experiment/evaluations").glob("*/*/run-final/outcome.json"):
            batch.verify(path.parent)
            row = batch.read(path)
            case, method = path.parents[2].name, path.parents[1].name
            project, bug = case.rsplit("-", 1)
            if (project not in config["inventory"] or int(bug) not in config["inventory"][project]
                    or method not in batch.METHODS or row["case"] != case or row["method"] != method
                    or row["protocol_hash"] != hashes[kind]):
                raise ValueError("Unbound outcome/inventory")
            if (method.startswith("kku-") != (kind == "ai")) or (case, method) in outcomes:
                raise ValueError("Duplicate or wrong method condition")
            outcomes[case, method] = {**row, "condition": kind,
                                      "result_path": path.relative_to(batch.ROOT).as_posix()}
    rows = []
    for project, bugs in config["inventory"].items():
        for bug in bugs:
            case = f"{project}-{bug}"
            for method in batch.METHODS:
                kind = "ai" if method.startswith("kku-") else "offline"
                row = outcomes.get((case, method), {"state": "PENDING", "attempted": False,
                                                    "provider_requested": False, "evaluated": False})
                rows.append({**{k: row.get(k) for k in batch.FIELDS + EXTRA}, "case": case,
                             "project": project, "bug_id": bug, "method": method,
                             "condition": kind, "protocol_hash": hashes[kind]})
    stats = {"planned_bugs": config["scope_bugs"], "planned_jobs": len(rows),
             "attempted_jobs": sum(r["attempted"] is True for r in rows),
             "evaluated_jobs": sum(r["evaluated"] is True for r in rows),
             "states": dict(Counter(r["state"] for r in rows)),
             "provider_request_attempts": sum(r.get("request_attempts") or 0 for r in rows),
             "confirmed_provider_responses": sum(r.get("completed_provider_responses") or 0 for r in rows),
             "bugs_four_methods_done": sum(all(outcomes.get((f"{project}-{bug}", m), {}).get("state") == "DONE"
                                                    for m in batch.METHODS)
                                            for project, bugs in config["inventory"].items() for bug in bugs),
             "protocols": hashes, "old_results_pooled": False,
             "per_method": {m: dict(Counter(r["state"] for r in rows if r["method"] == m)) for m in batch.METHODS},
             "limitations": ["DONE means completed evaluation, not necessarily fault detected.",
                              "Offline fixed-observation oracle and AI contract oracle differ.",
                              "P01/P02/P03/P04 and base/extension are stages, not independent bug/model jobs.",
                              "Missing metrics remain null; full inventory is pending outside the pilot."]}
    return rows, stats


def report(offline, ai, destination):
    rows, stats = collect(offline, ai)
    destination.mkdir(parents=True, exist_ok=True)
    with (destination / "combined_comparison.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=batch.FIELDS + EXTRA)
        writer.writeheader()
        writer.writerows(rows)
    batch.write(destination / "combined_summary.json", stats)
    print(stats)


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--offline", required=True, type=Path)
    cli.add_argument("--ai", required=True, type=Path)
    cli.add_argument("--destination", required=True, type=Path)
    args = cli.parse_args()
    report(args.offline.resolve(), args.ai.resolve(), args.destination.resolve())
