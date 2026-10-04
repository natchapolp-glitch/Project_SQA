#!/usr/bin/env python3
"""Verify pinned AI evidence and prove completed resume sends no new requests."""
import argparse
from pathlib import Path
import subprocess
import sys

import solo_ai as ai
import solo_batch as batch
import solo_results


def verify(output, offline, secrets):
    protocol, parent, _ = ai.load_protocol(output)
    if parent != offline:
        raise ValueError("Wrong offline parent")
    expected_hash = batch.sha(output / "Experiment/protocol/ai.json")
    jobs, requests, starts = [], 0, 0
    for outcome_path in sorted((output / "Experiment/evaluations").glob("*/*/run-final/outcome.json")):
        folder = outcome_path.parent
        batch.verify(folder)
        row = batch.read(outcome_path)
        if row["protocol_hash"] != expected_hash:
            raise ValueError("Outcome binding changed")
        case, method = row["case"], row["method"]
        context = output / "Experiment/contexts" / case
        batch.verify(context)
        if batch.sha(context / "ai-context.txt") != batch.sha(offline / "Experiment/contexts" / case / "ai-context.txt"):
            raise ValueError("AI buggy context differs from algorithm preparation")
        result_path = output / ai.METHODS[method][0] / "Result" / case / "run-final"
        for path in sorted(result_path.glob("*/request-intent.json")):
            batch.verify(path.parent)
            intent = batch.read(path)
            actual_prompt = output / ai.METHODS[method][0] / "Prompt" / case / "run-final" / f"{path.parent.name}.txt"
            if (intent["protocol_sha256"] != expected_hash or intent["model"] != ai.METHODS[method][1]
                    or intent["prompt_sha256"] != batch.sha(actual_prompt)
                    or intent["automatic_retry"] is not False):
                raise ValueError("Changed request/model/prompt binding")
            requests += 1
        if row["state"] == "DONE":
            measurement = output / row["selected_measurement"]
            batch.verify(measurement)
            observed = ai.observed_starts(measurement, row["generated_test_methods"])
            if observed != row["observed_test_starts"]:
                raise ValueError("Actual started-test counts changed")
            record = batch.read(measurement / "record.json")
            if record["status"] != "complete" or record["fixed_validation"] != "passed_twice":
                raise ValueError("DONE without complete fixed/buggy/coverage evidence")
            if batch.sha(Path(record["suite_path"])) != row["suite_sha256"]:
                raise ValueError("Selected archive changed")
            selected_sources = batch.read(folder / row["selected_suite"] / "package/suite-manifest.json")["source_sha256"]
            final = output / ai.METHODS[method][0] / "TestCode" / case / "run-final/final"
            if {p.relative_to(final).as_posix(): batch.sha(p) for p in final.rglob("*.java")} != selected_sources:
                raise ValueError("Published source differs from measured suite")
            starts += sum(observed.values())
        jobs.append({"case": case, "method": method, "state": row["state"]})
    before = batch.file_pins(output)
    cases = sorted({r["case"] for r in jobs})
    result = subprocess.run([sys.executable, "-B", str(batch.ROOT / "scripts/study/solo_ai.py"), "run",
                             "--output", str(output), "--secrets", str(secrets), "--account", "a01", "--cases", *cases],
                            cwd=batch.ROOT, text=True, capture_output=True, timeout=120)
    if result.returncode != 0 or batch.file_pins(output) != before:
        raise ValueError("Completed resume changed evidence or failed")
    rows, stats = solo_results.collect(offline, output)
    receipt = {"schema": "solo-ai-acceptance.v1", "jobs": jobs, "provider_requests": requests,
               "selected_suite_test_start_events": starts, "resume_changed_files": 0,
               "resume_stdout": result.stdout, "source_pins": ai.source_pins(),
               "verifier_sha256": batch.sha(Path(__file__)), "combined_summary": stats,
               "scope": "Automated binding/execution checks and operator source review; no peer verdict/Gate A claim"}
    folder = output / "Experiment/diagnostics/ai-acceptance"
    folder.mkdir(parents=True, exist_ok=False)
    batch.write(folder / "receipt.json", receipt)
    print({"jobs": len(jobs), "requests": requests, "test_start_events": starts, "resume_changed_files": 0})


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--output", required=True, type=Path)
    cli.add_argument("--offline", required=True, type=Path)
    cli.add_argument("--secrets", required=True, type=Path)
    args = cli.parse_args()
    verify(args.output.resolve(), args.offline.resolve(), args.secrets.resolve())
