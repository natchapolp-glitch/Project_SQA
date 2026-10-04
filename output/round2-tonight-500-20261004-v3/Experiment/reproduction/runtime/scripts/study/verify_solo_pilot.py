#!/usr/bin/env python3
"""Verify all offline pilot counters/context and prove resume retains evidence."""
import argparse
import json
from pathlib import Path
import sys

import solo_batch as batch


def verify_pilot(output, cases):
    config = batch.load_config(output)
    evidence = output / "Experiment/diagnostics/offline-acceptance"
    evidence.mkdir(parents=True, exist_ok=False)
    checks, original = [], {}
    for case in cases:
        folder = output / "Experiment/contexts" / case
        batch.verify(folder)
        meta, context = batch.read(folder / "prepared.json"), batch.read(folder / "ai-context.json")
        assert batch.sha(folder / "ai-context.txt") == meta["ai_context_sha256"]
        assert sum(len(row["excerpt"]) for row in context["source_excerpts"]) <= config["ai_source_character_budget"]
        for row in context["source_excerpts"]:
            source = Path(meta["trees"]["b"]) / meta["properties"]["b"]["dir.src.classes"] / (row["class"].replace(".", "/") + ".java")
            assert row["excerpt"] == source.read_text(encoding="utf-8", errors="replace")[:len(row["excerpt"])]
        for method in ("cmaes", "fscs-art"):
            result = batch.result_path(output, case, method)
            batch.verify(result)
            record = batch.read(result / "outcome.json")
            assert record["state"] == "DONE" and record["valid_on_fixed"] is True
            assert record["provider_requested"] is False
            measurement = result / "measurement"
            measured = batch.read(measurement / "record.json")
            counts = batch.invocation_counts(measurement, record["generated_test_methods"])
            assert counts == record["stage_counts"]
            assert all(measured["stages"][name]["failure_count"] == 0 for name in ("fixed-1", "fixed-2"))
            assert record["fault_detected"] == (measured["stages"]["buggy"]["failure_count"] > 0)
            generation = output / record["generation_path"]
            batch.verify(generation)
            rows = batch.read(generation / "observations.json")
            assert len(rows) == config["budget"]
            retained = [r for r in rows if r["retained"]]
            assert len(retained) == record["generated_test_methods"]
            assert all(r[k].get("target_invoked") is True for r in retained for k in ("fixed_first", "fixed_second"))
            original[str(result)] = batch.file_pins(result)
            original[str(generation)] = batch.file_pins(generation)
            checks.append({"case": case, "method": method, "state": record["state"],
                           "executed_tests": record["executed_tests"], "target_checks": record["target_checks"],
                           "line_coverage": [record["line_covered"], record["line_total"]],
                           "conditions_coverage": [record["branch_covered"], record["branch_total"]],
                           "fault_detected": record["fault_detected"]})
    batch.stage([sys.executable, "-B", batch.ROOT / "scripts/study/solo_batch.py", "run",
                 "--output", output, "--cases", *cases], evidence / "resume-check", 120)
    for folder, hashes in original.items():
        assert batch.file_pins(Path(folder)) == hashes, f"Resume changed retained evidence: {folder}"
    stats = batch.read(output / "Report/data/summary.json")
    assert stats["attempted_jobs"] == stats["evaluated_jobs"] == 2 * len(cases)
    assert stats["states"] == {"PENDING": config["scope_jobs"] - 2 * len(cases), "DONE": 2 * len(cases)}
    assert stats["new_api_requests"] == stats["provider_requested_jobs"] == 0
    assert batch.pins() == config["source_sha256"]
    batch.write(evidence / "receipt.json", {
        "review": "single operator automated checks; not peer approval",
        "protocol_sha256": batch.sha(output / "Experiment/protocol/offline.json"),
        "verification_script_sha256": batch.sha(Path(__file__)), "cases": cases,
        "checks": checks, "resume_preserved_evidence": True, "runtime_pins_unchanged": True,
        "buggy_only_context_checked": True, "new_api_requests": 0,
        "diagnostic_null_fixture_cohort_excluded": True, "summary": stats,
    })
    print(json.dumps(checks, indent=2))


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--output", type=Path, required=True)
    cli.add_argument("--cases", nargs="+", default=["Csv-1", "Lang-1", "Math-1"])
    args = cli.parse_args()
    verify_pilot(args.output.resolve(), args.cases)
