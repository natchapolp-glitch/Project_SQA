"""Receive exact peer evidence and report hosts/conditions without counting replays as bugs."""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import subprocess

from aom_ready_csv import ROOT, DAY, BASE, INTAKE, export, git, digest, load, save, seal, verify_manifest
from aom_ready_messages import RECEIVED

CHAMP_JSOUP="a4c723ad6cd58b3e837af28036ebefcfdba025d1"
BEAM="2c0e92fcca561ddd82f7881e8f6a0536ccdfef92"
CHAMP_LATEST="e9c6371892b3521d363b9c9b548ed348a1c15b5a"
INTAKE_NEW=ROOT / DAY / "aom-ready-peer-intake-v1"
REPORT=ROOT / DAY / "aom-ready-results-report-v4"
OLD=ROOT / DAY / "aom-ready-results-report-v3"
PACKETS={
    "champ-jsoup":(CHAMP_JSOUP,("champ-jsoup-development-generation-v1","champ-jsoup-native-measurement-v1",
        "champ-jsoup-native-measurement-v2","champ-ready-results-audit-v2"),
        ("CHAMP_JSOUP_READY_RESULTS_TH.md",)),
    "beam":(BEAM,("beam-champ-csv-messages-d4j-v3","beam-champ-jsoup-intake-v1",
        "beam-champ-jsoup-d4j-v1","beam-jsoup-results-return-v1"),
        ("BEAM_READY_RESULTS_RETURN_TH.md","BEAM_JSOUP_READY_RESULTS_RETURN_TH.md")),
    "champ-latest":(CHAMP_LATEST,("champ-gson-development-generation-v1","champ-gson-native-measurement-v1",
        "champ-compress-development-generation-v1","champ-compress-native-measurement-v1",
        "champ-compress-native-measurement-v2","champ-ready-progress-audit-v1",
        "champ-beam-ready-results-review-v5"),("CHAMP_BEAM_RESULTS_AND_COMPRESS_RETURN_TH.md",))}


def blob_verify(commit,folder):
    files=[p for p in sorted(folder.rglob("*")) if p.is_file()]
    names=[p.relative_to(folder).as_posix() for p in files]
    result=subprocess.run(["git","cat-file","--batch"],cwd=ROOT,
        input=("\n".join(commit+":"+n for n in names)+"\n").encode(),capture_output=True,check=True)
    offset=0
    for path,name in zip(files,names):
        end=result.stdout.index(b"\n",offset)
        header=result.stdout[offset:end].split()
        if len(header)!=3 or header[1]!=b"blob": raise ValueError("Missing original peer blob: "+name)
        length=int(header[2]); offset=end+1
        data=result.stdout[offset:offset+length]; offset+=length+1
        if digest(data)!=digest(path.read_bytes()): raise ValueError("Peer byte mismatch: "+name)
    return len(files)


def intake():
    INTAKE_NEW.mkdir(exist_ok=False)
    receipts=[]
    for label,(commit,packets,documents) in PACKETS.items():
        destination=INTAKE_NEW / label
        export(commit,[f"{DAY}/{p}" for p in packets]+["docs/api854/"+d for d in documents],destination)
        checked={p:verify_manifest(destination / DAY / p) for p in packets}
        original=blob_verify(commit,destination)
        receipts.append({"label":label,"commit":commit,"manifest_entries":checked,"original_git_blobs":original})
    save(INTAKE_NEW / "receipt.json",{"peers":receipts,"baseline_commit":BASE,
        "runtime_merge_performed":False,"original_outputs_unchanged":True,
        "no_new_cpu_evaluations":True,"api_requests":0,"queue_mutations":0,"primary_results":0,
        "producer_sha256":digest(Path(__file__).read_bytes())})
    seal(INTAKE_NEW)
    print(json.dumps({"peers":receipts}))


if __name__=="__main__":
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument("stage",choices=("intake","report"))
    stage=parser.parse_args().stage
    if stage=="intake": intake()
    else: raise SystemExit("Report stage pending implementation")
