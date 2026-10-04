"""Seal focused checks and verify every published evidence byte against the Git index."""
from __future__ import annotations
import argparse
import subprocess
import sys
from pathlib import Path

from aom_ready_csv import ROOT, DAY, digest, load, save, seal, verify_manifest
from cli_unordered_oracle import OUTPUT
from aom_cli_unordered_measure import MEASURED
from aom_cli_unordered_replay import REPLAY
from aom_cli_unordered_report import REPORT

AUDIT=ROOT / DAY / "aom-cli-publication-v1"
PACKETS=(ROOT / DAY / "aom-cli-preparation-review-v1",
    ROOT / DAY / "aom-cli-unordered-preparation-v1",OUTPUT,MEASURED,REPLAY,REPORT)


def verify_staged(include_audit=False):
    directories=PACKETS+((AUDIT,) if include_audit else ())
    files=[p for directory in directories for p in sorted(directory.rglob("*")) if p.is_file()]
    names=[p.relative_to(ROOT).as_posix() for p in files]
    result=subprocess.run(["git","cat-file","--batch"],input=("\n".join(":"+n for n in names)+"\n").encode(),
        cwd=ROOT,stdout=subprocess.PIPE,stderr=subprocess.PIPE,check=True)
    data=result.stdout
    offset=0
    for path,name in zip(files,names):
        end=data.index(b"\n",offset)
        head=data[offset:end].split()
        if len(head)!=3 or head[1]!=b"blob":
            raise ValueError("Evidence missing from Git index: "+name)
        length=int(head[2]); offset=end+1
        content=data[offset:offset+length]; offset+=length+1
        if digest(content)!=digest(path.read_bytes()):
            raise ValueError("Staged evidence bytes differ: "+name)
    changed=subprocess.run(["git","diff","--name-status","HEAD","--","output"],cwd=ROOT,
        capture_output=True,text=True,check=True).stdout.splitlines()
    prefixes=[d.relative_to(ROOT).as_posix()+"/" for d in directories]
    for row in changed:
        code,name=row.split("\t",1)
        if code!="A" or not any(name.startswith(prefix) for prefix in prefixes):
            raise ValueError("Historical/foreign output mutation: "+row)
    return len(files)


def produce():
    counts={p.name:verify_manifest(p) for p in PACKETS}
    staged=verify_staged()
    AUDIT.mkdir(exist_ok=False)
    command=[sys.executable,"-B","-X","utf8","-m","unittest","discover","-s","scripts/study/tests","-p","test_*ready_report.py","-v"]
    logs=[]
    for pattern in ("test_aom_ready_report.py","test_cli_unordered_packet.py"):
        command[-2]=pattern
        run=subprocess.run(command,cwd=ROOT,capture_output=True)
        (AUDIT / (pattern+".stdout.log")).write_bytes(run.stdout)
        (AUDIT / (pattern+".stderr.log")).write_bytes(run.stderr)
        logs.append({"pattern":pattern,"command":command[:],"exit_code":run.returncode})
        if run.returncode:
            raise ValueError("Focused checks failed; retain logs")
    for path in PACKETS:
        verify_manifest(path)
    save(AUDIT / "receipt.json",{"verified_packet_files":counts,"staged_evidence_blobs":staged,
        "focused_tests":logs,"historical_tracked_outputs_unchanged":True,
        "primary_results":0,"gate_a_approved":False,"api_requests":0,"queue_mutations":0,
        "producer_sha256":digest(Path(__file__).read_bytes())})
    seal(AUDIT)
    print({"verified_packet_files":counts,"staged_evidence_blobs":staged,"tests_passed":15})


if __name__ == "__main__":
    parser=argparse.ArgumentParser()
    parser.add_argument("mode",choices=("produce","verify-staged"))
    mode=parser.parse_args().mode
    if mode=="produce": produce()
    else:
        for p in PACKETS+(AUDIT,): verify_manifest(p)
        print({"all_staged_blobs":verify_staged(True)})
