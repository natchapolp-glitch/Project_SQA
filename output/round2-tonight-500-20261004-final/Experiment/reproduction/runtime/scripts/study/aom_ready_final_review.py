"""Audit public result bytes with native Git/filesystem access; reuse recorded regression logs."""
from __future__ import annotations

import csv
import hashlib
from pathlib import Path
import re
import shutil

from aom_ready_csv import ROOT, DAY, INTAKE, RUN, PAIR, digest, load, save, seal, verify_manifest, git
from aom_ready_report import REPORT as FIRST_REPORT
from aom_ready_messages import RECEIVED
from aom_ready_messages_d4j_v2 import MEASURED
from aom_ready_messages_report import REPORT

FINAL = ROOT / DAY / "aom-ready-results-publication-v3"


def main():
    audited = ROOT / DAY / "aom-ready-csv-publication-v2"
    verify_manifest(audited)
    FINAL.mkdir(parents=True, exist_ok=False)
    folders = [INTAKE, RUN, FIRST_REPORT, RECEIVED, MEASURED, REPORT,
        ROOT / DAY / "aom-ready-messages-d4j-v1",
        ROOT / DAY / "aom-ready-csv-publication-v1", audited]
    verified = {v.name: verify_manifest(v) for v in folders}
    historical = 0
    for entry in git("ls-files", "-s", "-z", "--", "output").split(b"\0"):
        if not entry:
            continue
        mode, obj, rest = entry.split(b" ", 2)
        stage, name = rest.split(b"\t", 1)
        if stage != b"0" or mode != b"100644":
            raise ValueError("Unexpected historical object")
        data = (ROOT / name.decode()).read_bytes()
        if hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest().encode() != obj:
            raise ValueError("Historical output differs from Git object")
        historical += 1
    protocol = load(INTAKE / "baseline" / PAIR / "protocol.proposal.json")
    for name, value in protocol["source_sha256"].items():
        if digest((INTAKE / "baseline" / name).read_bytes()) != value:
            raise ValueError("Frozen v12 implementation changed")
        if (ROOT / name).read_bytes() != git("show", "HEAD:" + name):
            raise ValueError("Root v13 runtime/config changed while ready-results work was underway")
    regression = {}
    for name, expected in (("test_aom_ready_report", 7), ("test_evaluate", 16)):
        folder = ROOT / DAY / "aom-ready-csv-publication-v1" / name
        command = load(folder / "command.json")
        log = (folder / "command.log").read_text(encoding="utf-8")
        if command["exit_code"] != 0 or command["timed_out"] or f"Ran {expected} tests" not in log or not log.rstrip().endswith("OK"):
            raise ValueError("Regression receipt does not establish declared test result")
        regression[name] = {"tests": expected, "command_sha256": digest((folder / "command.json").read_bytes()),
                            "log_sha256": digest((folder / "command.log").read_bytes())}
    latest = load(REPORT / "full-d4j-results.json")["records"]
    if len(latest) != 8 or len({(v["project"], v["bug_id"]) for v in latest}) != 1:
        raise ValueError("Condition rows miscounted as unique bugs")
    if len([v for v in latest if v["status"].startswith("invalid")]) != 2:
        raise ValueError("Baseline invalid outcomes were lost")
    current = latest[4:]
    if len(current) != 4 or any(v["status"] != "complete" for v in current):
        raise ValueError("Latest condition not complete for all four")
    if next(v for v in current if v["approach"] == "kku-claude")["total_tokens"] is not None:
        raise ValueError("Provider's missing total token field was fabricated")
    if load(REPORT / "cli-quarantine.json")["included_in_scientific_fault_count"]:
        raise ValueError("Known oracle false positive counted as a fault")
    for item in latest:
        if digest((ROOT / item["record_path"]).read_bytes()) != item["record_sha256"]:
            raise ValueError("Result row lineage changed")
        if item["primary_result"]:
            raise ValueError("Development result incorrectly marked primary")
    with (REPORT / "full-d4j-results.csv").open(encoding="utf-8", newline="") as stream:
        csv_rows = list(csv.DictReader(stream))
    if len(csv_rows) != 8 or any(row["record_sha256"] != item["record_sha256"] for row, item in zip(csv_rows, latest)):
        raise ValueError("CSV and JSON results disagree")
    for item in current:
        if item["approach"].startswith("kku-"):
            if any(v["skipped"] is not None or v["target_checks"] is not None for v in item["stage_counts"].values()):
                raise ValueError("Unmeasured AI counters were supplied")
    scanned = 0
    for folder in folders:
        for file in folder.rglob("*"):
            if file.is_file() and file.suffix in (".json", ".txt", ".log", ".csv", ".md"):
                if re.search(rb"(?:sk|kku)[-_][A-Za-z0-9_-]{24,}|Bearer\s+[A-Za-z0-9_.-]{20,}", file.read_bytes()):
                    raise ValueError(f"Potential public credential: {file.relative_to(ROOT)}")
                scanned += 1
    copied = FINAL / "producers"
    copied.mkdir()
    for file in sorted((ROOT / "scripts/study").glob("aom_ready*.py")):
        shutil.copyfile(file, copied / file.name)
    save(FINAL / "receipt.json", {"sealed_manifest_entries": verified,
        "historical_output_git_blobs_unchanged": historical, "frozen_runtime_v12_pins": len(protocol["source_sha256"]),
        "root_runtime_v13_unchanged": True, "regression": regression, "regression_tests_passed": 23,
        "actual_data_checks": ["8 rows/1 unique D4J bug", "2 baseline invalids retained", "4 latest complete methods",
          "Claude provider total null retained", "Cli false positive quarantined", "all row SHA bindings",
          "CSV/JSON agree", "AI missing skip/target counters remain null"],
        "credential_pattern_scan_files": scanned, "credential_pattern_matches": 0,
        "api_requests": 0, "queue_mutations": 0, "primary_results": 0, "gate_a_approved": False})
    seal(FINAL)
    print(f"Final review: historical={historical}, manifests={sum(verified.values())}, regression=23 passed, new condition four complete")


if __name__ == "__main__":
    main()
