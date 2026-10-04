"""Separate native and benchmark conditions; retain first Csv invalids and Cli quarantine."""
from __future__ import annotations

import json
from aom_ready_csv import ROOT, DAY, INTAKE, BASE, PREP, digest, load, save, seal, verify_manifest
from aom_ready_report import REPORT as FIRST_REPORT, flatten, stage_counts, write_csv
from aom_ready_messages import RECEIVED, PACKETS
from aom_ready_messages_d4j_v2 import MEASURED

REPORT = ROOT / DAY / "aom-ready-results-report-v2"


def main():
    for folder in (INTAKE, FIRST_REPORT, RECEIVED, MEASURED):
        verify_manifest(folder)
    REPORT.mkdir(parents=True, exist_ok=False)
    native = RECEIVED / "peer" / DAY / "champ-csv-messages-native-measurement-v1"
    receipt = load(native / "receipt.json")
    actual = load(MEASURED / "receipt.json")
    records = []
    for row in actual["observations"]:
        old = next(v for v in receipt["records"] if v["approach"] == row["approach"])
        path = ROOT / row["record_path"]
        if digest(path.read_bytes()) != row["record_sha256"]:
            raise ValueError("Measurement record changed")
        measured = load(path)
        item = {"project": "Csv", "bug_id": 1, "approach": row["approach"],
                "condition": actual["condition"], "generation_condition": receipt["condition"],
                "declared_tests": old["declared_test_count"], "primary_result": False,
                "record_path": row["record_path"], "record_sha256": row["record_sha256"],
                "instrument_classes": measured["instrument_classes"], "stage_counts": {},
                "input_domain_equivalence": "not_approved; AI Reader inputs wider than bounded algorithm fixtures"}
        for field in ("status", "compile_status", "fault_detected", "line_covered", "line_total", "branch_covered",
                      "branch_total", "duration_seconds", "suite_sha256", "triggering_tests"):
            item[field] = measured.get(field)
        for field in ("prompt_tokens", "completion_tokens", "total_tokens"):
            item[field] = old.get("generation", {}).get("usage", {}).get(field)
        for name in ("fixed-1", "fixed-2", "buggy", "coverage"):
            folder = path.parent / name
            counts = stage_counts(folder, item["declared_tests"], require_target=row["approach"] in ("cmaes", "fscs-art"))
            counts["failed"] = measured.get("stages", {}).get(name, {}).get("failure_count")
            if name == "coverage" and measured["status"] == "complete":
                counts["failed"] = 0
            item["stage_counts"][name] = counts
        records.append(item)
    first = load(FIRST_REPORT / "full-d4j-results.json")["records"]
    all_d4j = first + records
    save(REPORT / "full-d4j-results.json", {"records": all_d4j, "distinct_bugs": 1,
        "conditions_are_separate_not_repeats": True, "unavailable_values_are_not_zero": True})
    write_csv(REPORT / "full-d4j-results.csv", [flatten(v) for v in all_d4j])
    peer_audit = RECEIVED / "peer" / DAY / "champ-ready-results-audit-v1"
    native_records = load(peer_audit / "results.json")["records"]
    for record in native_records:
        original_path = record["receipt_path"]
        file = RECEIVED / "peer" / original_path
        if not file.exists():
            file = INTAKE / "peer" / original_path
        if digest(file.read_bytes()) != record["receipt_sha256"]:
            raise ValueError("Native row receipt differs")
        record["original_receipt_path"] = original_path
        record["receipt_path"] = file.relative_to(ROOT).as_posix()
    save(REPORT / "native-results.json", {"records": native_records,
         "conditions_must_not_be_pooled_as_repeats": True, "unique_bugs": 2,
         "cli_fscs_semantic_fault": "quarantined, not counted"})
    write_csv(REPORT / "native-results.csv", native_records)
    index = load(INTAKE / "baseline" / PREP / "index.json")
    cohort = []
    approaches = ("cmaes", "fscs-art", "kku-claude", "kku-gemini")
    for row in index["records"]:
        for approach in approaches:
            current = next((v for v in records if row["project"] == "Csv" and row["bug_id"] == 1 and v["approach"] == approach), None)
            cli = row["project"] == "Cli" and row["bug_id"] == 1
            cohort.append({"project": row["project"], "bug_id": row["bug_id"], "owner": row["owner"],
                "evaluation_host": "beam-pc1" if row["owner"] == "beam" else "aom-pc1",
                "approach": approach, "baseline_commit": BASE, "prompt_sha256": row["prompt_sha256"],
                "latest_received_status": current["status"] if current else "native_received_d4j_pending_oracle_quarantine" if cli else "pending_not_received_by_aom",
                "full_d4j_evidence": current["record_path"] if current else None,
                "condition": current["condition"] if current else None,
                "limitation": "wider AI input domains; not primary" if current else "Cli FSCS option-order false positive quarantined; prospective repair needed" if cli else "Verify suite/source/runtime/domain/host before evaluation"})
    write_csv(REPORT / "cohort20-worklist.csv", cohort)
    cli_audit = RECEIVED / "peer" / DAY / "champ-cli-order-oracle-audit-v2/receipt.json"
    save(REPORT / "cli-quarantine.json", {"receipt_path": cli_audit.relative_to(ROOT).as_posix(),
        "receipt_sha256": digest(cli_audit.read_bytes()), "received_verdict": load(cli_audit),
        "raw_fault_retained": True, "included_in_scientific_fault_count": False,
        "next": "prospective unordered option projection + regress exact16 targets before new generation condition",
        "do_not_sort": ["positional arguments", "buffers", "ordered result arrays"],
        "current_shared_v12_v13_runtime_modified": False})
    receipt_summary = {"baseline_commit": BASE, "peer_commit": load(RECEIVED / "receipt.json")["peer_commit"],
        "native_condition_outcomes": len(native_records), "native_unique_bugs": 2,
        "d4j_condition_outcomes": len(all_d4j), "d4j_unique_bugs": 1,
        "d4j_complete_suite_rows": sum(v["status"] == "complete" for v in all_d4j),
        "d4j_invalid_outcome_rows": sum(v["status"].startswith("invalid") for v in all_d4j),
        "latest_csv_condition_complete_methods": sum(v["status"] == "complete" for v in records),
        "latest_csv_condition_raw_fault_methods": [v["approach"] for v in records if v["fault_detected"]],
        "cli_fscs_fault_quarantined": True, "native_and_d4j_conditions_separate": True,
        "baseline_invalids_preserved": True, "cohort20_rows": 80, "pending_not_received_rows": 72,
        "native_cli_received_full_d4j_pending_rows": 4,
        "primary_results": 0, "gate_a_approved": False, "api_requests": 0, "queue_mutations": 0,
        "final_reserve": None, "producer_sha256": digest(__import__('pathlib').Path(__file__).read_bytes())}
    save(REPORT / "receipt.json", receipt_summary)
    seal(REPORT)
    print(json.dumps(receipt_summary))


if __name__ == "__main__":
    main()
