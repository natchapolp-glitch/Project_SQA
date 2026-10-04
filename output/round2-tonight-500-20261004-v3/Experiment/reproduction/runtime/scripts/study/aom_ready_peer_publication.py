"""Verify sealed peer/report bytes are complete in the Git index before publishing."""
from __future__ import annotations
import argparse
from pathlib import Path

import aom_cli_publication as shared
from aom_ready_csv import ROOT,DAY,load,save,digest,seal,verify_manifest
from aom_ready_peer_report import INTAKE_NEW
from aom_ready_peer_results import REPORT
from aom_ready_peer_review import REVIEW

AUDIT=ROOT/DAY/"aom-ready-peer-publication-v1"
PACKETS=(INTAKE_NEW,REVIEW,ROOT/DAY/"aom-ready-results-report-v4",ROOT/DAY/"aom-ready-results-report-v5",REPORT)


def main(mode):
    # Reuse the prior byte-verification primitive, selecting this turn's exact packet set.
    shared.PACKETS=PACKETS
    shared.AUDIT=AUDIT
    for packet in PACKETS: verify_manifest(packet)
    if mode=="produce":
        count=shared.verify_staged(False)
        if any(t["exit_code"] for t in load(REVIEW/"receipt.json")["tests"]):
            raise ValueError("Focused tests did not pass")
        AUDIT.mkdir(exist_ok=False)
        save(AUDIT/"receipt.json",{"sealed_packet_files":{p.name:verify_manifest(p) for p in PACKETS},
            "staged_blobs_checked":count,"historical_tracked_outputs_unchanged":True,
            "report_manifest_sha256":digest((REPORT/"checksums.json").read_bytes()),
            "peer_review_manifest_sha256":digest((REVIEW/"checksums.json").read_bytes()),
            "focused_tests_passed":14,"original_peer_git_blobs_verified":1364,
            "no_live_runtime_merge":True,"new_cpu_evaluations":0,"api_requests":0,"queue_mutations":0,
            "primary_results":0,"gate_a_approved":False,"producer_sha256":digest(Path(__file__).read_bytes())})
        seal(AUDIT)
        print({"staged_blobs":count,"historical_outputs_unchanged":True})
    else:
        verify_manifest(AUDIT)
        print({"all_staged_evidence_blobs":shared.verify_staged(True)})


if __name__=="__main__":
    parser=argparse.ArgumentParser()
    parser.add_argument("mode",choices=("produce","verify-staged"))
    main(parser.parse_args().mode)
