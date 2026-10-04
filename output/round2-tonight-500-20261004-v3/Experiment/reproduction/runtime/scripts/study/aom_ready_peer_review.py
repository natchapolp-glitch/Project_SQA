"""Supplementary fixed-input and source/host acceptance; no evaluation or provider calls."""
from __future__ import annotations
import subprocess
import sys
from pathlib import Path

from aom_ready_csv import ROOT,DAY,load,save,digest,seal,verify_manifest
from aom_ready_peer_report import INTAKE_NEW,PACKETS,blob_verify
from aom_ready_peer_results import REPORT,received_path

REVIEW=ROOT / DAY / "aom-ready-peer-review-v1"


def main():
    verify_manifest(INTAKE_NEW)
    verify_manifest(REPORT)
    results=[]
    for project,packet,generation in (("Csv","beam-champ-csv-messages-d4j-v3","champ-csv-messages-generation-v2"),
        ("Jsoup","beam-champ-jsoup-d4j-v1","champ-jsoup-development-generation-v1")):
        root=INTAKE_NEW/"beam"/DAY/packet
        preseal=load(root/"preexecution-seal.json")
        inputs={name:received_path(f"{DAY}/{generation}/received/{name}",expected)
            for name,expected in preseal["frozen_inputs_sha256"].items()}
        fixed=load(inputs["prepare-metadata.json"])["fixed_source_sha256"]
        for row in load(root/"results.json"):
            if row["status"]!="complete": continue
            if load(root/row["approach"]/"setup/fixed-source-hashes.json")!=fixed:
                raise ValueError("Actual fixed source differs from generation context")
        derive=root/"isolated-bug-reference/derivation.json"
        derivation=load(derive)
        official=root/"isolated-bug-reference"/("1.src.patch" if project=="Csv" else "official.src.patch")
        if digest(official.read_bytes())!=derivation["official_patch_sha256"]:
            raise ValueError("Retained official patch differs")
        if derivation["active_bug_record"]["bug.id"]!="1": raise ValueError("Source bug metadata mismatch")
        if project=="Jsoup" and digest(derive.read_bytes())!=preseal["source_derivation_sha256"]:
            raise ValueError("Source derivation differs from preexecution pin")
        results.append({"project":project,"generation_input_files_bound":len(inputs),
            "fixed_source_sha256":fixed,"official_patch_sha256":digest(official.read_bytes()),
            "host":"beam-pc1","cpu_slots":preseal["cpu_slots"],"inputs_unchanged":True})
    original_blobs={label:blob_verify(commit,INTAKE_NEW/label) for label,(commit,_,_) in PACKETS.items()}
    REVIEW.mkdir(exist_ok=False)
    tests=[]
    for pattern in ("test_aom_ready_report.py","test_ready_peer_reporting.py"):
        command=[sys.executable,"-B","-X","utf8","-m","unittest","discover","-s","scripts/study/tests","-p",pattern,"-v"]
        run=subprocess.run(command,cwd=ROOT,capture_output=True)
        (REVIEW/(pattern+".stdout.log")).write_bytes(run.stdout)
        (REVIEW/(pattern+".stderr.log")).write_bytes(run.stderr)
        tests.append({"command":command,"exit_code":run.returncode})
        if run.returncode: raise ValueError("Reporting tests failed; retain logs")
    save(REVIEW/"receipt.json",{"input_source_acceptance":results,"peer_original_git_blobs":original_blobs,
        "peer_original_git_blob_count":sum(original_blobs.values()),"tests":tests,
        "report_manifest_sha256":digest((REPORT/"checksums.json").read_bytes()),
        "peer_review_only_no_local_rerun":True,"new_cpu_evaluations":0,"api_requests":0,"queue_mutations":0,
        "primary_results":0,"gate_a_approved":False,"producer_sha256":digest(Path(__file__).read_bytes())})
    seal(REVIEW)
    print({"fixed_inputs_checked":results,"original_peer_blobs":sum(original_blobs.values()),"tests":"passed"})


if __name__=="__main__": main()
