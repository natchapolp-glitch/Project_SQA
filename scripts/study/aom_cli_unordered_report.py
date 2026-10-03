"""Append separate Cli conditions to existing report without rewriting history."""
from __future__ import annotations
import csv
import shutil
from pathlib import Path

from aom_ready_csv import ROOT, DAY, digest, load, save, seal, verify_manifest
from aom_ready_report import stage_counts, flatten, write_csv
from cli_unordered_oracle import OUTPUT, CONDITION
from aom_cli_unordered_measure import MEASURED
from aom_cli_unordered_replay import REPLAY

OLD = ROOT / DAY / "aom-ready-results-report-v2"
REPORT = ROOT / DAY / "aom-ready-results-report-v3"


def main():
    for directory in (OLD,OUTPUT,MEASURED,REPLAY):
        verify_manifest(directory)
    REPORT.mkdir(exist_ok=False)
    original=load(OLD / "full-d4j-results.json")["records"]
    additions=[]
    for directory in (MEASURED,REPLAY):
        receipt=load(directory / "receipt.json")
        for item in receipt["records"]:
            path=ROOT / item["record_path"]
            if digest(path.read_bytes())!=item["record_sha256"]:
                raise ValueError("Evaluation record differs")
            raw=load(path)
            row={**raw,"project":"Cli","bug_id":1,"approach":item["approach"],
                "condition":receipt["condition"],"generation_condition":CONDITION,"declared_tests":30,
                "record_path":item["record_path"],"record_sha256":item["record_sha256"],
                "primary_result":False,"full_defects4j_completed":raw["status"]=="complete",
                "semantic_validity":"bounded oracle controls passed; peer review pending; no full domain equivalence claim",
                "prompt_tokens":None,"completion_tokens":None,"total_tokens":None,"stage_counts":{}}
            for stage in ("fixed-1","fixed-2","buggy","coverage"):
                if raw["status"]=="complete":
                    counts=stage_counts(path.parent / stage,30,require_target=True)
                    counts["failed"]=raw["stages"].get(stage,{}).get("failure_count")
                    if stage=="coverage": counts["failed"]=0
                else:
                    counts={"executed":None,"skipped":None,"target_checks":None,"failed":None,
                        "reason":"runner linkage failure before test methods; no measured zeros"}
                row["stage_counts"][stage]=counts
            additions.append(row)
    pending=[]
    for model,approach in (("claude-sonnet-5","kku-claude"),("gemini-3.5-flash-lite","kku-gemini")):
        pending.append({"project":"Cli","bug_id":1,"approach":approach,"model":model,"condition":CONDITION,
            "status":"pending_not_generated_in_new_condition","declared_tests":None,"fault_detected":None,
            "primary_result":False,"full_defects4j_completed":False,"stage_counts":{},
            "reason":"old model outcomes belong to old oracle; no imputation or condition transfer"})
    records=original+additions
    save(REPORT / "full-d4j-results.json",{"records":records,"pending_new_condition":pending,
        "original_report_manifest_sha256":digest((OLD / "checksums.json").read_bytes())})
    write_csv(REPORT / "full-d4j-results.csv",[flatten(v) for v in records])
    write_csv(REPORT / "cli-latest-four-methods.csv",[flatten(v) for v in additions[-2:]+pending])
    save(REPORT / "cli-latest-four-methods.json",{"records":additions[-2:]+pending})
    for name in ("native-results.json","native-results.csv","cli-quarantine.json"):
        shutil.copyfile(OLD / name,REPORT / name)
    with (OLD / "cohort20-worklist.csv").open(encoding="utf-8",newline="") as stream:
        work=list(csv.DictReader(stream))
    new={row["approach"]:row for row in additions[-2:]+pending}
    prompt_sha=load(OUTPUT / "receipt.json")["prompt_sha256"]
    for row in work:
        if row["project"]=="Cli" and row["bug_id"]=="1":
            item=new[row["approach"]]
            row.update(prompt_sha256=prompt_sha,latest_received_status=item["status"],
                full_d4j_evidence=item.get("record_path",""),condition=item["condition"],
                limitation="New scoped oracle condition; old FSCS raw mismatch retained/quarantined; AI new generation pending; not primary")
    write_csv(REPORT / "cohort20-worklist.csv",work)
    controls=(MEASURED / "controls-new/command.log").read_text()
    reference=load(MEASURED / "reference-cases.json")
    restoration=load(REPLAY / "framework-restoration.json")
    if "SQA_CONTROLS=15" not in controls or len(reference)!=32 or not restoration["restored_exactly"]:
        raise ValueError("Control/reference/restoration receipt absent")
    save(REPORT / "receipt.json",{"d4j_condition_outcomes":len(records),"d4j_unique_bugs":2,
        "d4j_complete_suite_rows":sum(r["status"]=="complete" for r in records),
        "prior_invalid_outcomes_preserved":True,"cli_environment_failed_attempts_preserved":2,
        "cli_latest_completed_algorithms":sum(r["status"]=="complete" for r in additions[-2:]),
        "cli_latest_ai_pending":2,"oracle_java_controls":15,"reference_cases":32,
        "fixed_reference_observations":64,"old_policy_equivalence_cases":32,
        "old_vector_diagnostic_only":True,"old_cli_raw_fault_quarantined":True,
        "framework_restored_exactly":True,"primary_results":0,"gate_a_approved":False,
        "api_requests":0,"queue_mutations":0,"final_reserve":None,
        "other_worklist_pending_means_not_received_by_aom_not_peer_incomplete":True,
        "producer_sha256":digest(Path(__file__).read_bytes())})
    seal(REPORT)
    print(REPORT)


if __name__ == "__main__":
    main()
