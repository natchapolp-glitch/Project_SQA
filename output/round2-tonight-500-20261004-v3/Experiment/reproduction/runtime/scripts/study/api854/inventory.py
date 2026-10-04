"""Validate explicit active IDs, hash inputs, and prepare held job manifests."""
from __future__ import annotations

import argparse
from collections import Counter
import hashlib
import json
from pathlib import Path
import re
import subprocess

APPROACHES = ("cmaes", "fscs-art", "kku-claude", "kku-gemini")
OWNERS = ("champ", "beam", "aom")


def canonical_hash(value):
    return hashlib.sha256(json.dumps(value, sort_keys=True, separators=(",", ":"),
                                     ensure_ascii=False, allow_nan=False).encode()).hexdigest()


def file_hash(path):
    digest = hashlib.sha256()
    with Path(path).open("rb") as handle:
        for block in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def validate_inventory(rows, expected_bugs=854, expected_projects=17):
    pairs = set()
    result = []
    for row in rows:
        project, bid, owner = row["project"], row["bug_id"], row["owner"]
        if not isinstance(project, str) or not re.fullmatch(r"[A-Za-z][A-Za-z0-9]*", project):
            raise ValueError("invalid project")
        if type(bid) is not int or bid <= 0 or owner not in OWNERS:
            raise ValueError("invalid active ID or owner")
        if (project, bid) in pairs:
            raise ValueError("duplicate active bug")
        if "approaches" in row and sorted(row["approaches"]) != sorted(APPROACHES):
            raise ValueError("ownership must include all four approaches exactly once")
        pairs.add((project, bid))
        result.append({"project": project, "bug_id": bid, "owner": owner})
    if len(result) != expected_bugs or len({r["project"] for r in result}) != expected_projects:
        raise ValueError("unexpected inventory size/projects")
    return sorted(result, key=lambda r: (r["project"], r["bug_id"]))


def compare_installed(rows, installed):
    expected = {(r["project"], r["bug_id"]) for r in rows}
    actual = set()
    for project, ids in installed.items():
        for bid in ids:
            if type(bid) is not int or bid <= 0 or (project, bid) in actual:
                raise ValueError("invalid/duplicate installed ID")
            actual.add((project, bid))
    if actual != expected:
        raise ValueError(f"installed active IDs differ: missing={len(expected-actual)}, extra={len(actual-expected)}")


def export_installed(executable):
    def run(*args):
        output = subprocess.run([executable, *args], check=True, capture_output=True,
                                text=True, timeout=120).stdout
        return [s.strip() for s in output.splitlines() if s.strip()]
    install_root = Path(executable).resolve().parents[2]
    readme = install_root / "README.md"
    version_text = readme.read_text(encoding="utf-8")
    if not re.search(r"Defects4J\s*--\s*version\s+3\.0\.1\b", version_text):
        raise ValueError("Defects4J 3.0.1 is required")
    revision = subprocess.run(["git", "-C", str(install_root), "rev-parse", "HEAD"],
                              check=True, capture_output=True, text=True, timeout=30).stdout.strip()
    installed = {pid: [int(bid) for bid in run("bids", "-p", pid)] for pid in run("pids")}
    return {"defects4j_version": "3.0.1", "projects": installed,
            "installation_revision": revision, "version_readme_sha256": file_hash(readme),
            "commands": [[executable, "pids"],
                         *[[executable, "bids", "-p", pid] for pid in installed]]}


def build_jobs(rows, protocol_hash):
    if not re.fullmatch(r"[a-f0-9]{64}", protocol_hash):
        raise ValueError("protocol hash must be SHA-256")
    jobs = []
    for row in sorted(rows, key=lambda r: (r["project"], r["bug_id"])):
        for approach in APPROACHES:
            identity = {"project": row["project"], "bug_id": row["bug_id"],
                        "approach": approach, "protocol_hash": protocol_hash, "repeat_index": 1}
            jobs.append({**identity, "job_id": canonical_hash(identity), "owner": row["owner"]})
    return jobs


def select_pilot(rows):
    # Prospective deterministic IDs; representative large context must be confirmed by Beam.
    projects = sorted({r["project"] for r in rows})
    selected = []
    for project in projects:
        candidates = sorted((r for r in rows if r["project"] == project), key=lambda r: r["bug_id"])
        selected.append(candidates[0])
        if project in ("Closure", "JxPath", "JacksonDatabind"):
            selected.append(candidates[-1])
    return sorted(selected, key=lambda r: (r["project"], r["bug_id"]))


def prepare(ownership_path, protocol_path, destination, installed_path=None):
    ownership = json.loads(Path(ownership_path).read_text(encoding="utf-8"))
    protocol = json.loads(Path(protocol_path).read_text(encoding="utf-8"))
    rows = validate_inventory(ownership["bugs"])
    counts = Counter(r["owner"] for r in rows)
    if counts != {"champ": 285, "beam": 285, "aom": 284}:
        raise ValueError("ownership counts differ from confirmed allocation")
    installed = None
    if installed_path:
        installed = json.loads(Path(installed_path).read_text(encoding="utf-8"))
        if installed["defects4j_version"] != "3.0.1":
            raise ValueError("wrong installed Defects4J version")
        compare_installed(rows, installed["projects"])
    bugs = {"schema_version": 1, "bugs": rows, "inventory_hash": canonical_hash(rows),
            "ownership_file_sha256": file_hash(ownership_path),
            "installed_verified": installed is not None,
            "installed_export_sha256": file_hash(installed_path) if installed_path else None,
            "warning": "Allocation is not adapter readiness or observed experiment results."}
    ph = canonical_hash(protocol)
    output = Path(destination)
    output.mkdir(parents=True, exist_ok=True)
    artifacts = {"bugs.json": bugs,
                 "jobs.json": {"protocol_hash": ph, "inventory_hash": bugs["inventory_hash"],
                               "jobs": build_jobs(rows, ph)},
                 "pilot20.json": {"selection": "lowest active ID per project plus highest Closure/JxPath/JacksonDatabind; confirm context breadth before activation",
                                  "preregistered": True, "bugs": select_pilot(rows),
                                  "protocol_hash": ph, "inventory_hash": bugs["inventory_hash"]}}
    for name, value in artifacts.items():
        (output / name).write_text(json.dumps(value, ensure_ascii=False, indent=2) + "\n", encoding="utf-8", newline="\n")
    return {"bugs": len(rows), "jobs": len(artifacts["jobs.json"]["jobs"]),
            "owners": dict(counts), "installed_verified": installed is not None, "protocol_hash": ph}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    sub = parser.add_subparsers(dest="command", required=True)
    export = sub.add_parser("export-installed")
    export.add_argument("--defects4j", required=True)
    export.add_argument("--output", required=True)
    prep = sub.add_parser("prepare")
    prep.add_argument("--ownership", required=True)
    prep.add_argument("--protocol", required=True)
    prep.add_argument("--output", required=True)
    prep.add_argument("--installed")
    args = parser.parse_args()
    if args.command == "export-installed":
        value = export_installed(args.defects4j)
        Path(args.output).write_text(json.dumps(value, indent=2) + "\n", encoding="utf-8", newline="\n")
    else:
        print(json.dumps(prepare(args.ownership, args.protocol, args.output, args.installed)))


if __name__ == "__main__":
    main()
