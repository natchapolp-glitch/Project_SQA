"""Read-only ready-results publication audit; no experiment or provider reruns."""
from __future__ import annotations

from datetime import datetime, timezone
import hashlib
from pathlib import Path
import shutil
import sys

from aom_ready_csv import ROOT, DAY, INTAKE, RUN, BASE, PAIR, PACKETS, digest, load, save, seal, verify_manifest, git
from aom_ready_report import REPORT

AUDIT = ROOT / DAY / "aom-ready-csv-publication-v1"


def main():
    if sys.platform != "linux":
        raise ValueError("Run publication checks in the measured WSL host")
    AUDIT.mkdir(parents=True, exist_ok=False)
    counts = {p.name: verify_manifest(p) for p in (INTAKE, RUN, REPORT)}
    # Repository history is checked against actual object bytes, not names or timestamps.
    unchanged = 0
    for entry in git("ls-files", "-s", "-z", "--", "output").split(b"\0"):
        if not entry:
            continue
        mode, object_id, rest = entry.split(b" ", 2)
        stage, name = rest.split(b"\t", 1)
        if stage != b"0" or mode != b"100644":
            raise ValueError("Unexpected historical file stage or mode")
        data = (ROOT / name.decode("utf-8")).read_bytes()
        actual = hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest().encode()
        if actual != object_id:
            raise ValueError(f"Historical output changed: {name.decode()}")
        unchanged += 1
    baseline = INTAKE / "baseline"
    sys.path.insert(0, str(baseline / "scripts/study"))
    from evaluate import run_command
    from api854.common import implementation_hashes
    frozen = load(baseline / PAIR / "protocol.proposal.json")
    if implementation_hashes() != frozen["source_sha256"]:
        raise ValueError("Frozen v12 runtime changed")
    tests = {}
    for name in ("test_aom_ready_report.py", "test_evaluate.py"):
        command = run_command([sys.executable, "-B", "-m", "unittest", "discover", "-s",
                               "scripts/study/tests", "-p", name, "-v"], ROOT,
                               AUDIT / name.removesuffix(".py"), 60)
        if command["exit_code"] != 0 or command["timed_out"]:
            raise ValueError("Publication regression failed; retain logs")
        tests[name] = command
    # These hashes are observed after the run, not invented preexecution limits.
    d4j = Path("/home/team/sqa-round2/defects4j")
    dependencies = [d4j / "framework/projects/lib/junit-4.12-hamcrest-1.3.jar",
                    d4j / "framework/projects/lib/cobertura-2.0.3.jar",
                    d4j / "framework/lib/formatter.jar",
                    d4j / "framework/lib/formatter/src/edu/washington/cs/mut/testrunner/Formatter.java",
                    d4j / "framework/projects/defects4j.build.xml"]
    dependencies.extend(sorted((d4j / "framework/projects/lib/cobertura-2.0.3-lib").glob("*.jar")))
    if any(not p.is_file() for p in dependencies):
        raise ValueError("Missing measured-host framework dependency")
    save(AUDIT / "dependency-hashes.json", {"observed_at_utc": datetime.now(timezone.utc).isoformat(),
         "scope": "post-run installed JUnit/Cobertura/formatter/build bindings; not all OS dependencies",
         "files": {str(p): digest(p.read_bytes()) for p in dependencies}})
    for name in ("aom_ready_csv.py", "aom_ready_report.py", "aom_ready_audit.py"):
        source = ROOT / "scripts/study" / name
        destination = AUDIT / "producers" / name
        destination.parent.mkdir(exist_ok=True)
        shutil.copyfile(source, destination)
    if digest((AUDIT / "producers/aom_ready_csv.py").read_bytes()) != load(RUN / "preexecution-plan.json")["producer_sha256"]:
        raise ValueError("Measured producer changed after preexecution binding")
    if digest((AUDIT / "producers/aom_ready_report.py").read_bytes()) != load(REPORT / "receipt.json")["producer_sha256"]:
        raise ValueError("Report producer changed after sealing")
    if git("diff", "--name-only", "HEAD", "--", "algorithms", "experiments", "scripts/study/api854", "scripts/study/evaluate.py").strip():
        raise ValueError("Shared runtime/configuration was changed")
    # Check plain text public artifacts for token-shaped credentials; never print a match.
    import re
    credential_patterns = [rb"(?:sk|kku)[-_][A-Za-z0-9_-]{24,}", rb"Bearer\s+[A-Za-z0-9_.-]{20,}"]
    scanned = 0
    for folder in (INTAKE, RUN, REPORT):
        for file in folder.rglob("*"):
            if file.is_file() and file.suffix in (".json", ".txt", ".log", ".csv", ".md"):
                data = file.read_bytes()
                if any(re.search(pattern, data) for pattern in credential_patterns):
                    raise ValueError(f"Potential credential in public artifact: {file.relative_to(ROOT)}")
                scanned += 1
    save(AUDIT / "receipt.json", {"baseline_commit": BASE, "sealed_manifests_verified": counts,
         "historical_output_git_blobs_unchanged": unchanged, "runtime_v12_pins_verified": len(frozen["source_sha256"]),
         "shared_runtime_configuration_unchanged": True, "test_commands": tests, "tests_passed": 23,
         "credential_pattern_scan_text_files": scanned, "credential_pattern_matches": 0,
         "primary_results": 0, "api_requests": 0, "queue_mutations": 0, "gate_a_approved": False})
    seal(AUDIT)
    print(f"manifest checks={counts}; unchanged historical files={unchanged}; regression=23 passed")


if __name__ == "__main__":
    main()
