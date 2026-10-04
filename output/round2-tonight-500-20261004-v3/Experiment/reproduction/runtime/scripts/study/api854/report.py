"""Evidence-based counts and reproducible packages from a single frozen snapshot."""
from __future__ import annotations

import argparse
from collections import Counter, defaultdict
from datetime import datetime, timezone, timedelta
import hashlib
import json
import math
from pathlib import Path
import re
import tempfile
import zipfile

from .inventory import APPROACHES, canonical_hash, file_hash
from .queue import TERMINAL


def percentiles(values):
    if not values:
        return {"n": 0, "p50": None, "p95": None}
    ordered = sorted(values)
    return {"n": len(values), "p50": ordered[math.ceil(len(ordered) * .5) - 1],
            "p95": ordered[math.ceil(len(ordered) * .95) - 1]}


def summarize(snapshot):
    jobs, attempts = snapshot["jobs"], snapshot["attempts"]
    protocols = {j["protocol_hash"] for j in jobs}
    if len(protocols) > 1:
        raise ValueError("report one frozen protocol condition at a time")
    groups = defaultdict(list)
    for job in jobs:
        groups[(job["project"], job["bug_id"], job["protocol_hash"], job["repeat_index"])].append(job)

    def counts(rows):
        usable = [j for j in rows if j["status"] == "complete"]
        records = [json.loads(j["evaluation_record"]) for j in usable]
        detected = sum(record["fault_detected"] for record in records)
        coverage = {"observations": len(records)}
        for label, covered, total in (("line", "line_covered", "line_total"),
                                      ("condition", "condition_covered", "condition_total")):
            c, t = sum(r[covered] for r in records), sum(r[total] for r in records)
            ratios = [r[covered] / r[total] for r in records if r[total] > 0]
            coverage[label + "_covered"] = c if records else None
            coverage[label + "_total"] = t if records else None
            coverage[label + "_ratio_micro"] = c / t if t else None
            coverage[label + "_ratio_macro"] = sum(ratios) / len(ratios) if ratios else None
        ids = {j["job_id"] for j in rows}
        cost_rows = [a for a in attempts if a["job_id"] in ids and a["status"] in TERMINAL | {"generated"}]
        seconds = [a["ended_at"] - a["started_at"] for a in cost_rows
                   if a["ended_at"] is not None and a["ended_at"] >= a["started_at"]]
        return {"keys": len(rows), "attempted": sum(bool(j["attempted"]) for j in rows),
                "not_attempted": sum(not j["attempted"] for j in rows),
                "terminal": sum(j["status"] in TERMINAL for j in rows),
                "usable": len(usable), "fault_detected": detected,
                "fault_detection_rate_eligible": detected / len(usable) if usable else None,
                "coverage": coverage, "attempt_workflow_seconds_nearest_rank": percentiles(seconds),
                "status_counts": dict(Counter(j["status"] for j in rows))}

    matched = [rows for rows in groups.values() if len(rows) == 4
               and {j["approach"] for j in rows} == set(APPROACHES)
               and all(j["status"] == "complete" for j in rows)]
    summary = counts(jobs)
    usage_known, usage_unknown, tokens = 0, 0, 0
    models = Counter()
    for attempt in attempts:
        record = json.loads(attempt["record"]) if attempt.get("record") else {}
        if attempt["stage"] != "generate":
            continue  # Never double-count API tokens carried into evaluation records.
        if record.get("actual_model"):
            models[str(record["actual_model"])] += 1
            total = (record.get("usage") or {}).get("total_tokens")
            if type(total) is int and total >= 0:
                tokens += total
                usage_known += 1
            else:
                usage_unknown += 1
        elif attempt.get("dispatched_at") is not None and attempt["status"] != "not_executed":
            job = next(j for j in jobs if j["job_id"] == attempt["job_id"])
            if job["approach"].startswith("kku-"):
                usage_unknown += 1
    return {"schema_version": 1, "protocol_hash": next(iter(protocols), None),
            "bugs_in_snapshot": len(groups), "expected_bugs": 854, "expected_job_keys": 3416,
            "job_keys_in_snapshot": summary["keys"], "attempted_keys": summary["attempted"],
            "not_attempted_keys": summary["not_attempted"], "terminal_outcomes": summary["terminal"],
            "usable_keys": summary["usable"], "matched_usable_bugs": len(matched),
            "matched_usable_fraction": len(matched) / 854,
            "terminal_fraction": summary["terminal"] / 3416,
            "attempts": len(attempts), "attempt_status_counts": dict(Counter(a["status"] for a in attempts)),
            "needs_reconciliation_keys": sum(j["status"] == "needs_reconciliation" for j in jobs),
            "api_tokens_known": tokens, "api_usage_known_attempts": usage_known,
            "api_usage_unknown_attempts": usage_unknown, "actual_models": dict(models),
            "quota_remaining": None, "eta_at": None,
            "limitations": ["Dispatch intent is conservative; uncertain attempts require reconciliation.",
                            "No measured throughput or quota means ETA/remaining stay null.",
                            "Counts exclude historical datasets and keep failed/missing work."],
            "by_approach": {a: counts([j for j in jobs if j["approach"] == a]) for a in APPROACHES},
            "matched_by_approach": {a: counts([j for rows in matched for j in rows if j["approach"] == a]) for a in APPROACHES},
            "by_owner": {o: counts([j for j in jobs if j["owner"] == o]) for o in ("champ", "beam", "aom")},
            "by_project": {p: counts([j for j in jobs if j["project"] == p]) for p in sorted({j["project"] for j in jobs})}}


def write_report(snapshot, output):
    summary = summarize(snapshot)
    destination = Path(output)
    destination.mkdir(parents=True, exist_ok=True)
    (destination / "progress.json").write_text(json.dumps(summary, ensure_ascii=False, indent=2) + "\n", encoding="utf-8", newline="\n")
    local = datetime.fromtimestamp(snapshot["snapshot_at"], timezone(timedelta(hours=7))).isoformat()
    text = f"""# API854 — รายงานสถานะจาก snapshot (partial)

เวลาสรุป Asia/Bangkok: {local}
Snapshot SHA-256: `{canonical_hash(snapshot)}`
Protocol SHA-256: `{summary['protocol_hash']}`

มี {summary['job_keys_in_snapshot']} job keys ใน snapshot จากเป้าหมาย 3,416 งาน / 854 bugs
บันทึก dispatch intent {summary['attempted_keys']} keys; มีผล terminal พร้อมหลักฐาน {summary['terminal_outcomes']} keys
ยังไม่เริ่ม {summary['not_attempted_keys']} keys; ต้องตรวจสอบผลไม่แน่นอน {summary['needs_reconciliation_keys']} keys
Usable suites {summary['usable_keys']}; matched bugs ครบสี่วิธี {summary['matched_usable_bugs']}/854
Attempts ทั้งหมด {summary['attempts']} (ไม่เพิ่มจำนวน bugs)

## วิธีทดลองที่เตรียม

CMA-ES และ FSCS-ART: seed 101, 30 proposed inputs, fixed observations สองครั้งต่อ proposal
AI: KKU Claude Haiku และ Gemini Flash Lite; ต้องตรึง exact model/settings ก่อน pilot
ใช้ target eligibility และ fixed-source context เดียวกัน ไม่ส่ง compile/test logs กลับ AI
Fixed ต้องผ่านสองครั้ง ใช้ suite เดียวกันบน buggy และ coverage; fault_detected=false เป็นผลที่ยอมรับได้
เทสว่าง ไม่มี assertions หรือ return เพราะ fixture null ไม่เป็น usable

## ผลและข้อจำกัด

ข้อมูลตัวเลขแยกวิธี/project อยู่ใน progress.json ซึ่งสร้างจาก snapshot เดียวกัน
ผลเก่า 17 bugs/204 รอบเป็น historical และไม่ถูกรวมใน cohort นี้
Token ที่ทราบ {summary['api_tokens_known']}; attempts ที่ไม่ทราบ usage {summary['api_usage_unknown_attempts']}
Quota remaining และ ETA ยังไม่มีข้อมูลวัดที่ยืนยัน จึงคง null
การสร้างคิวหรือรายงานนี้ไม่ใช่หลักฐานว่าได้รัน Defects4J/API แล้ว

## งานก่อนสรุปส่ง

- ตรวจ Gate A/B และ installed inventory จากบีม; exact models/quota/context จากแชมป์
- ตรวจหลักฐาน raw/processed differences, failures, exclusions และ provenance
- Freeze inventory/protocol/records; สร้าง slides/ZIP จาก snapshot เดียวกัน
- ทีมตรวจ reproducibility, secrets และยอดก่อนรวมเข้า test และส่ง
"""
    (destination / "REPORT_TH.md").write_text(text, encoding="utf-8", newline="\n")
    return summary


def reject_secrets(name, data):
    if any(s in name.lower() for s in (".env", ".sqlite", "credentials", "secrets")):
        raise ValueError("private/runtime file excluded from evidence package")
    text = data.decode("utf-8", errors="ignore")
    patterns = (r"(?i)authorization\s*[:=]\s*[\"']?bearer\s+\S+",
                r"(?i)[\"']?(?:api_key|apikey|access_token|password)[\"']?\s*[:=]\s*[\"']?[^\s\"',}]+",
                r"\bsk-[A-Za-z0-9_-]{16,}\b", r"[\w.+-]+@[\w.-]+\.[A-Za-z]{2,}")
    if any(re.search(pattern, text) for pattern in patterns):
        raise ValueError("possible secret/email in evidence; redact before publication")


def freeze(snapshot, config_paths, destination):
    """Freeze exact bytes and referenced evidence. Never package runtime SQLite/secrets."""
    summarize(snapshot)
    payloads = {"snapshot.json": (json.dumps(snapshot, ensure_ascii=False, sort_keys=True, indent=2) + "\n").encode()}
    with tempfile.TemporaryDirectory() as report_directory:
        write_report(snapshot, report_directory)
        for name in ("REPORT_TH.md", "progress.json"):
            payloads["report/" + name] = (Path(report_directory) / name).read_bytes()
    for path in config_paths:
        p = Path(path)
        name = "config/" + p.name
        if name in payloads:
            raise ValueError("duplicate configuration filename")
        payloads[name] = p.read_bytes()
    for attempt in snapshot["attempts"]:
        record = json.loads(attempt["record"]) if attempt.get("record") else {}
        for index, artifact in enumerate(record.get("artifacts", [])):
            p = Path(artifact["path"])
            data = p.read_bytes()
            if hashlib.sha256(data).hexdigest() != artifact["sha256"]:
                raise ValueError("artifact changed before freeze")
            payloads[f"evidence/{attempt['attempt_id']}/{index:03d}-{p.name}"] = data
    manifest = {"schema_version": 1, "snapshot_hash": canonical_hash(snapshot),
                "files": {name: hashlib.sha256(data).hexdigest() for name, data in sorted(payloads.items())}}
    for name, data in payloads.items():
        reject_secrets(name, data)
    payloads["SHA256SUMS.json"] = (json.dumps(manifest, indent=2) + "\n").encode()
    target = Path(destination)
    target.parent.mkdir(parents=True, exist_ok=True)
    if target.exists():
        raise ValueError("frozen archive already exists; use a new snapshot name")
    with zipfile.ZipFile(target, "x", compression=zipfile.ZIP_DEFLATED) as archive:
        for name, data in sorted(payloads.items()):
            info = zipfile.ZipInfo(name, date_time=(2026, 10, 3, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            archive.writestr(info, data)
    verify_package(target)
    target.with_suffix(target.suffix + ".sha256").write_text(file_hash(target) + "\n", encoding="utf-8", newline="\n")
    return manifest


def verify_package(path):
    with zipfile.ZipFile(path) as archive:
        if archive.testzip() is not None:
            raise ValueError("corrupt ZIP")
        manifest = json.loads(archive.read("SHA256SUMS.json"))
        if len(archive.namelist()) != len(set(archive.namelist())):
            raise ValueError("duplicate archive entry")
        if set(archive.namelist()) != set(manifest["files"]) | {"SHA256SUMS.json"}:
            raise ValueError("archive manifest mismatch")
        for name, digest in manifest["files"].items():
            if name.startswith("/") or ".." in Path(name).parts or "\\" in name:
                raise ValueError("unsafe ZIP path")
            if hashlib.sha256(archive.read(name)).hexdigest() != digest:
                raise ValueError("archive checksum mismatch")
        if canonical_hash(json.loads(archive.read("snapshot.json"))) != manifest["snapshot_hash"]:
            raise ValueError("snapshot checksum mismatch")
    return manifest


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--snapshot", required=True)
    parser.add_argument("--output", required=True)
    parser.add_argument("--freeze-zip")
    parser.add_argument("--config", action="append", default=[])
    args = parser.parse_args()
    snapshot = json.loads(Path(args.snapshot).read_text(encoding="utf-8"))
    summary = write_report(snapshot, args.output)
    print(json.dumps({k: summary[k] for k in ("job_keys_in_snapshot", "attempted_keys", "not_attempted_keys",
                                            "terminal_outcomes", "usable_keys", "matched_usable_bugs")}))
    if args.freeze_zip:
        freeze(snapshot, args.config, args.freeze_zip)


if __name__ == "__main__":
    main()
