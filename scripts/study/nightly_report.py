#!/usr/bin/env python3
"""Aggregate sealed jobs across the pilot/compact protocols without hiding failures."""
import argparse
from collections import Counter
import csv
from datetime import datetime, timezone
from pathlib import Path
from statistics import mean

import solo_batch as batch
from solo_scope import load_scope

NAMES = {"cmaes": "CMA-ES", "fscs-art": "FSCS-ART", "kku-claude": "KKU Sonnet 5",
         "kku-gemini": "KKU Gemini 3.5 Flash Lite"}
EXTRA = ["condition", "request_attempts", "completed_provider_responses", "prompt_tokens",
         "completion_tokens", "selected_suite", "account_alias", "model_id", "elapsed_seconds"]


def summarize(rows):
    result = {}
    for method in batch.METHODS:
        selected = [r for r in rows if r["method"] == method]
        measured = [r for r in selected if r["state"] == "DONE" and isinstance(r.get("fault_detected"), bool)]
        counts = dict(Counter(r["state"] for r in selected))
        stats = {"name": NAMES[method], "states": counts,
                 "attempted_cases": sum(r.get("attempted") is True for r in selected),
                 "successfully_evaluated": counts.get("DONE", 0),
                 "fault_detection_denominator": len(measured),
                 "fault_detecting_cases": sum(r["fault_detected"] for r in measured),
                 "fault_detection_percent": 100 * sum(r["fault_detected"] for r in measured) / len(measured) if measured else None,
                 "request_attempts": sum(r.get("request_attempts") or 0 for r in selected)}
        for label, covered, total in (("line", "line_covered", "line_total"), ("condition", "branch_covered", "branch_total")):
            values = [100 * r[covered] / r[total] for r in selected if r["state"] == "DONE"
                      and isinstance(r.get(covered), (int, float)) and isinstance(r.get(total), (int, float)) and r[total] > 0]
            stats[f"mean_fixed_{label}_coverage_percent"] = mean(values) if values else None
            stats[f"{label}_coverage_cases"] = len(values)
        for field in ("prompt_tokens", "completion_tokens"):
            values = [r[field] for r in selected if isinstance(r.get(field), int)]
            stats[f"recorded_{field}"] = sum(values) if values else None
            stats[f"{field}_cases"] = len(values)
        for field in ('generation_seconds', 'evaluation_seconds', 'elapsed_seconds'):
            values = [r[field] for r in selected if isinstance(r.get(field), (int, float))]
            stats[f'mean_{field}'] = mean(values) if values else None
            stats[f'{field}_cases'] = len(values)
        result[method] = stats
    return result


def collect(offline, ais, scope=None):
    config = batch.read(offline / "Experiment/protocol/offline.json")
    parent_hash = batch.sha(offline / "Experiment/protocol/offline.json")
    selection = load_scope(scope, config, parent_hash) if scope else None
    selected_cases = set(selection['cases']) if selection else None
    roots = [("offline", offline, parent_hash)]
    for root in ais:
        protocol = batch.read(root / "Experiment/protocol/ai.json")
        if protocol["offline_protocol_sha256"] != parent_hash:
            raise ValueError("AI parent condition differs")
        roots.append((root.name, root, batch.sha(root / "Experiment/protocol/ai.json")))
    outcomes = {}
    for condition, root, digest in roots:
        for path in (root / "Experiment/evaluations").glob("*/*/run-final/outcome.json"):
            # Outcome is published before the final receipt: skip that brief active window.
            if not (path.parent / "receipt.json").is_file():
                continue
            batch.verify(path.parent)
            row = batch.read(path)
            case, method = path.parents[2].name, path.parents[1].name
            project, bug = case.rsplit("-", 1)
            if (row["case"] != case or row["method"] != method or row["protocol_hash"] != digest
                    or project not in config["inventory"] or int(bug) not in config["inventory"][project]
                    or method not in batch.METHODS or method.startswith("kku-") != (condition != "offline")):
                raise ValueError("Outcome not bound to its case/method/protocol/inventory")
            if (case, method) in outcomes:
                raise ValueError("Duplicate case/method across conditions; requires explicit continuation policy")
            if selected_cases is not None and case not in selected_cases:
                raise ValueError('Recorded outcome excluded by selected scope; preserve it in a separate explicit cohort')
            outcomes[case, method] = {**row, "condition": condition, "result_path": str(path)}
    rows = []
    for project, bugs in config["inventory"].items():
        for bug in bugs:
            case = f"{project}-{bug}"
            if selected_cases is not None and case not in selected_cases:
                continue
            for method in batch.METHODS:
                values = outcomes.get((case, method), {"state": "PENDING", "attempted": False,
                                                       "evaluated": False, "provider_requested": False})
                rows.append({**{k: values.get(k) for k in batch.FIELDS + EXTRA}, "case": case,
                             "project": project, "bug_id": bug, "method": method})
    cases = {(r["project"], r["bug_id"]) for r in rows if r["attempted"] is True}
    stats = {"snapshot_at_utc": datetime.now(timezone.utc).isoformat(),
             "planned_bugs": len(selected_cases) if selection else config["scope_bugs"],
             "full_inventory_bugs": sum(map(len, config['inventory'].values())),
             "scope_manifest_sha256": batch.sha(scope) if scope else None,
             "planned_jobs": len(rows), "attempted_jobs": sum(r["attempted"] is True for r in rows),
             "recorded_bugs": len(cases), "recorded_projects": len({p for p, _ in cases}),
             "states": dict(Counter(r["state"] for r in rows)), "per_method": summarize(rows),
             "bugs_four_methods_evaluated": sum(all(outcomes.get((f"{p}-{b}", m), {}).get("state") == "DONE"
                                                       for m in batch.METHODS) for p, b in cases),
             "bugs_four_methods_recorded": sum(all((f"{p}-{b}", m) in outcomes for m in batch.METHODS) for p, b in cases),
             "protocols": {n: digest for n, _, digest in roots},
             "limitations": ["Snapshot includes sealed outcomes only; dispatched jobs still running are not yet terminal results.",
                              "One generation per method/bug; fixed repeats are validation, not independent repeats.",
                              "Pilot and compact AI protocols are labelled separately; aggregate means are descriptive only.",
                              "Algorithms use fixed-reference oracles; AI uses buggy context and fixed feedback.",
                              "Missing measurements remain null; infrastructure/quota errors are not detected faults."]}
    return rows, stats


def markdown_table(rows, fields):
    def cell(value):
        if value is None:
            return "N/A"
        return f"{value:.2f}" if isinstance(value, float) else str(value)
    return "| " + " | ".join(fields) + " |\n|" + "|".join("---" for _ in fields) + "|\n" + "".join(
        "| " + " | ".join(cell(row.get(f)) for f in fields) + " |\n" for row in rows)


def write_report(offline, ais, destination, scope=None):
    rows, stats = collect(offline, ais, scope)
    destination.mkdir(parents=True, exist_ok=True)
    data = destination / "data"
    data.mkdir(exist_ok=True)
    with (data / "final_comparison.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=batch.FIELDS + EXTRA)
        writer.writeheader()
        writer.writerows(rows)
    batch.write(data / "summary.json", stats)
    summaries = destination / "summaries"
    summaries.mkdir(exist_ok=True)
    common = (f"Snapshot: {stats['snapshot_at_utc']}\n\nPlanned: {stats['planned_bugs']} bugs / {stats['planned_jobs']} jobs. "
              f"Recorded: {stats['recorded_bugs']} bugs / {stats['recorded_projects']} projects. "
              f"Installed inventory: {stats['full_inventory_bugs']} bugs; this report's chosen scope is separate. "
              "Not all planned cases were attempted. DONE means complete evaluation, not fault detection.\n\n")
    fields = ["name", "attempted_cases", "successfully_evaluated", "fault_detection_denominator", "fault_detecting_cases",
              "fault_detection_percent", "line_coverage_cases", "mean_fixed_line_coverage_percent",
              "condition_coverage_cases", "mean_fixed_condition_coverage_percent"]
    for filename, methods in (("ai_summary.md", ["kku-claude", "kku-gemini"]),
                               ("cmaes_summary.md", ["cmaes"]), ("fscs_art_summary.md", ["fscs-art"]),
                               ("final_comparison.md", list(batch.METHODS))):
        text = "# Measured experiment summary\n\n" + common + markdown_table([stats["per_method"][m] for m in methods], fields)
        for method in methods:
            text += f"\n{NAMES[method]} states: {stats['per_method'][method]['states']}\n"
        text += "\n" + "\n".join("- " + n for n in stats["limitations"]) + "\n"
        (summaries / filename).write_text(text, encoding="utf-8")
    return rows, stats


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--offline", type=Path, required=True)
    cli.add_argument("--ai", type=Path, nargs="+", required=True)
    cli.add_argument("--destination", type=Path, required=True)
    cli.add_argument('--scope', type=Path)
    args = cli.parse_args()
    _, stats = write_report(args.offline.resolve(), [p.resolve() for p in args.ai], args.destination.resolve(), args.scope)
    print({k: stats[k] for k in ("recorded_bugs", "recorded_projects", "attempted_jobs", "states")})
