"""Report sealed ready-subset outcomes without merging execution conditions or imputing zeros."""
from __future__ import annotations

import csv
import json
from pathlib import Path
import re
import sys

from aom_ready_csv import ROOT, INTAKE, RUN, DAY, PAIR, PREP, PACKETS, BASE, CONDITION, APPROACHES
from aom_ready_csv import digest, load, save, seal, verify_manifest, git

REPORT = ROOT / DAY / "aom-ready-csv-report-v1"


def read_started(folder):
    path = folder / "all_tests"
    if not path.is_file():
        return None
    values = path.read_text(encoding="utf-8").splitlines()
    if not values or len(values) != len(set(values)):
        raise ValueError("Started-test evidence is empty or duplicated")
    if any(not re.fullmatch(r"[\w$]+\([\w.$]+\)", v) for v in values):
        raise ValueError("Unexpected test-start evidence")
    return len(values)


def stage_counts(folder, declared, require_target=False):
    if not folder.is_dir():
        return {"started": None, "executed": None, "skipped": None, "target_checks": None}
    started = read_started(folder)
    path = folder / "sqa-stage-counts.json"
    if not path.is_file():
        if require_target:
            raise ValueError("Required algorithm counters missing")
        return {"started": started, "executed": started, "skipped": None, "target_checks": None,
                "source": "Defects4J Formatter.startTest; no separate skip/target counter"}
    counts = load(path)
    for field in ("executed", "skipped", "target_checks"):
        if type(counts.get(field)) is not int or not 0 <= counts[field] <= declared:
            raise ValueError(f"Invalid stage count: {field}")
    if counts["executed"] != started:
        raise ValueError("Embedded execution counter disagrees with Defects4J start events")
    if require_target and (counts["executed"] != declared or counts["skipped"] != 0
                           or counts["target_checks"] != declared):
        raise ValueError("Algorithm did not execute/check every declared case")
    return {**counts, "started": started, "source": "unchanged suite counters plus Defects4J start events"}


def flatten(item):
    # Preserve null: missing metrics are not failures, zero coverage or no fault.
    row = {k: item.get(k) for k in ("project", "bug_id", "approach", "condition", "status", "declared_tests",
        "compile_status", "fault_detected", "line_covered", "line_total", "branch_covered", "branch_total",
        "duration_seconds", "prompt_tokens", "completion_tokens", "total_tokens", "primary_result",
        "suite_sha256", "record_path", "record_sha256")}
    for name in ("fixed-1", "fixed-2", "buggy", "coverage"):
        stage = item.get("stage_counts", {}).get(name, {})
        for field in ("executed", "skipped", "target_checks", "failed"):
            row[f"{name}_{field}"] = stage.get(field)
    return row


def write_csv(path, rows):
    with path.open("x", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=list(rows[0]))
        writer.writeheader()
        writer.writerows(rows)


def main():
    verify_manifest(INTAKE)
    verify_manifest(RUN)
    REPORT.mkdir(parents=True, exist_ok=False)
    baseline = INTAKE / "baseline"
    index = load(baseline / PREP / "index.json")
    protocol = load(baseline / PAIR / "protocol.proposal.json")
    peer = INTAKE / "peer" / DAY
    native = load(peer / PACKETS[0] / "results.json")
    original = {v["approach"]: v for v in native["records"]}
    if set(original) != set(APPROACHES):
        raise ValueError("Must preserve exactly four received outcomes")
    metadata = load(peer / PACKETS[1] / "received/prepare-metadata.json")
    host = load(RUN / "host-binding.json")
    csv_index = next(v for v in index["records"] if v["project"] == "Csv" and v["bug_id"] == 1)
    if host["fixed_source_sha256"] != metadata["fixed_source_sha256"] or host["fixed_source_sha256"] != csv_index["fixed_source_sha256"]:
        raise ValueError("Host fixed source does not match frozen context")
    for name, field in (("prompt.md", "prompt_sha256"), ("targets.json", "targets_sha256"),
                        ("fixture-recipes.json", "fixture_recipes_sha256")):
        actual = digest((peer / PACKETS[1] / "received" / name).read_bytes())
        if actual != metadata[field] or actual != csv_index[field]:
            raise ValueError(f"Received v12 context mismatch: {name}")
    source_bound = {"baseline_commit": BASE, "fixed_source_sha256": host["fixed_source_sha256"],
                    "target_count": csv_index["target_count"], "matching_received_context": True,
                    "matching_frozen_v12_index": True, "scope": "Csv-1 only, not full403 semantic approval"}
    save(REPORT / "source-binding-verification.json", source_bound)
    records = []
    for approach in APPROACHES:
        old = original[approach]
        item = {"project": "Csv", "bug_id": 1, "approach": approach, "condition": CONDITION,
                "generation_condition": native["condition"], "declared_tests": old["declared_tests"],
                "prompt_tokens": old["prompt_tokens"], "completion_tokens": old["completion_tokens"],
                "total_tokens": old["total_tokens"], "primary_result": False,
                "semantic_validity": "not_full_input_domain_equivalence_approved", "stage_counts": {}}
        old_receipt = INTAKE / "peer" / old["receipt_path"]
        if digest(old_receipt.read_bytes()) != old["receipt_sha256"]:
            raise ValueError("Peer outcome receipt binding differs")
        item["generation_receipt_path"] = old_receipt.relative_to(ROOT).as_posix()
        item["generation_receipt_sha256"] = old["receipt_sha256"]
        if approach == "kku-claude":
            item.update(status="invalid_generation_truncated_no_executable_suite", compile_status="not_attempted",
                fault_detected=None, line_covered=None, line_total=None, branch_covered=None, branch_total=None,
                duration_seconds=None, full_defects4j_completed=False,
                record_path=old_receipt.relative_to(ROOT).as_posix(), record_sha256=old["receipt_sha256"])
        else:
            record_path = RUN / approach / "record.json"
            record = load(record_path)
            item.update({k: record.get(k) for k in ("status", "compile_status", "fault_detected", "line_covered",
                "line_total", "branch_covered", "branch_total", "duration_seconds", "suite_sha256")})
            item.update(record_path=record_path.relative_to(ROOT).as_posix(), record_sha256=digest(record_path.read_bytes()),
                full_defects4j_completed=record["status"] == "complete", timezone=record["timezone"],
                instrument_classes=record.get("instrument_classes"))
            archive = load(INTAKE / "receipt.json")["archives"][approach]
            manifest = load((ROOT / archive["path"]).with_name("suite-manifest.json"))
            if item["suite_sha256"] != archive["sha256"] or item["suite_sha256"] != manifest["suite_sha256"]:
                raise ValueError("Archive bytes changed")
            if not 0 < item["declared_tests"] <= protocol["test_method_cap"]:
                raise ValueError("Suite cap exceeded")
            for name in ("fixed-1", "fixed-2", "buggy", "coverage"):
                folder = RUN / approach / name
                counts = stage_counts(folder, item["declared_tests"], require_target=approach in ("cmaes", "fscs-art"))
                counts["failed"] = record.get("stages", {}).get(name, {}).get("failure_count")
                if name == "coverage" and record["status"] == "complete":
                    counts["failed"] = 0  # evaluator required existing empty failing_tests, not an assumption.
                item["stage_counts"][name] = counts
            if approach == "kku-gemini" and record["status"] == "invalid":
                confirmation = load(RUN / "kku-gemini-fixed-repeat/confirmation.json")
                if confirmation["suite_sha256"] != record["suite_sha256"]:
                    raise ValueError("Rejected suite repeat differs")
                counts = stage_counts(RUN / "kku-gemini-fixed-repeat", item["declared_tests"])
                counts["failed"] = confirmation["command"]["failure_count"]
                item["stage_counts"]["fixed-2"] = counts
                item["status"] = "invalid_fixed_suite_rejected_twice"
                item["fixed_repeat_seconds"] = confirmation["command"]["duration_seconds"]
                item["compile_status_in_sealed_record"] = record["compile_status"]
                log = (RUN / approach / "fixed-1/command.log").read_text(encoding="utf-8")
                if not re.search(r"Running ant \(compile.gen.tests\).*OK", log):
                    raise ValueError("Cannot establish rejected suite compiled")
                item["compile_status"] = "passed_from_compile_stage_log"
                item["limitations"] = ["D4J formatter counts15 starts; separate skip and target-check counters unavailable",
                     "No buggy/coverage eligibility; the fixed assertion failure is not a detected production fault"]
        records.append(item)
    save(REPORT / "full-d4j-results.json", {"condition": CONDITION, "records": records,
         "missing_values_are_unavailable_not_zero": True, "coverage_scope": "ExtendedBufferedReader only"})
    write_csv(REPORT / "full-d4j-results.csv", [flatten(v) for v in records])
    # Native rows remain a separate original table; do not average or pool them.
    for name in ("results.json", "results.csv"):
        with (REPORT / f"native-{name}").open("xb") as stream:
            stream.write((peer / PACKETS[0] / name).read_bytes())
    cohort = []
    for record in index["records"]:
        for approach in APPROACHES:
            actual = next((v for v in records if record["project"] == "Csv" and record["bug_id"] == 1 and v["approach"] == approach), None)
            cohort.append({"project": record["project"], "bug_id": record["bug_id"], "owner": record["owner"],
                "evaluation_host": "beam-pc1" if record["owner"] == "beam" else "aom-pc1",
                "approach": approach, "baseline_commit": BASE,
                "selected_declarations": record["target_count"], "prompt_sha256": record["prompt_sha256"],
                "status": actual["status"] if actual else "pending_not_received_by_aom",
                "evidence": actual["record_path"] if actual else None,
                "eligibility": "Csv received, frozen-source bound" if actual else "must verify received suite and exact source/host binding before run"})
    if len(cohort) != 80:
        raise ValueError("Expected prepared20 x four worklist, not an 854 results table")
    write_csv(REPORT / "cohort20-worklist.csv", cohort)
    capacity_path = "output/api854-20261004/champ-24h-capacity-plan-v1/capacity-and-deadline.json"
    capacity = json.loads(git("show", f"{load(INTAKE / 'receipt.json')['peer_commit']}:{capacity_path}"))
    save(REPORT / "deadline-source.json", {"path": capacity_path, "sha256": digest(git("show", f"{load(INTAKE / 'receipt.json')['peer_commit']}:{capacity_path}")), "observed_proposal": capacity})
    success = sum(v.get("full_defects4j_completed", False) for v in records)
    save(REPORT / "receipt.json", {"condition": CONDITION, "baseline_commit": BASE,
        "actual_outcomes": len(records), "valid_full_d4j_suites": success,
        "invalid_outcomes": sum(v["status"].startswith("invalid") for v in records),
        "cohort20_worklist_rows": len(cohort), "pending_not_received_rows": sum(v["evidence"] is None for v in cohort),
        "compute_cutoff_utc": capacity["working_compute_cutoff_utc"], "working_deadline_utc": capacity["working_deadline_utc"],
        "deadline_is_internal_estimate_not_confirmed_official": True,
        "api_requests": 0, "queue_mutations": 0, "primary_results": 0, "gate_a_approved": False,
        "runtime_v13": "retained; no composition requested while collecting results",
        "full_854_claim": False, "final_reserve": None,
        "producer_sha256": digest(Path(__file__).read_bytes())})
    seal(REPORT)
    print(json.dumps({"actual_outcomes": len(records), "full_d4j_complete": success,
                       "invalid": 4-success, "pending_cohort_rows": 76}))


if __name__ == "__main__":
    main()
