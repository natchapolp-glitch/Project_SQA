"""Record local prerequisites without claiming that allocation implies readiness."""
from __future__ import annotations

import argparse
import os
from pathlib import Path
import platform
import re
import shutil

from .common import write_json
from evaluate import run_command, utc_now


def inspect_environment(d4j, output):
    output = Path(output).resolve()
    output.mkdir(parents=True, exist_ok=False)
    report = {"checked_at_utc": utc_now(), "platform": platform.platform(), "cpu_slots_initial": 1,
              "cpu_count": os.cpu_count(), "disk_free_bytes": shutil.disk_usage(output).free,
              "memory_total_bytes": None, "defects4j_version": None, "checks": {}, "issues": []}
    if platform.system() != "Linux":
        report["issues"].append("Defects4J worker requires Linux/WSL, not Windows")
    if hasattr(os, "sysconf"):
        report["memory_total_bytes"] = os.sysconf("SC_PAGE_SIZE") * os.sysconf("SC_PHYS_PAGES")
    for name, command in (("java", ["java", "-version"]), ("javac", ["javac", "-version"]),
                          ("git", ["git", "--version"]), ("svn", ["svn", "--version", "--quiet"]),
                          ("perl", ["perl", "-e", "print $^V"]), ("cpanm", ["cpanm", "--version"]),
                          ("defects4j", [str(d4j), "pids"])):
        stage = run_command(command, output, output / name, 30)
        text = (output / name / "command.log").read_text(encoding="utf-8", errors="replace")
        report["checks"][name] = stage
        if stage["exit_code"] != 0 or stage["timed_out"]:
            report["issues"].append(f"{name} command unavailable/failed")
        if name in {"java", "javac"}:
            match = re.search(r'(?:version\s+"?|javac\s+)(\d+)', text)
            if not match or match[1] != "11":
                report["issues"].append(f"{name} must be version 11")
    executable = shutil.which(str(d4j))
    if executable:
        resolved = Path(executable).resolve()
        if len(resolved.parents) >= 3:
            readme = resolved.parents[2] / "README.md"
            if readme.is_file():
                match = re.search(r"Defects4J\s*--\s*version\s+([\d.]+)", readme.read_text(encoding="utf-8"))
                if match:
                    report["defects4j_version"] = match[1]
    if report["defects4j_version"] != "3.0.1":
        report["issues"].append("Defects4J installation is not verified as 3.0.1")
    report["ready"] = not report["issues"]
    write_json(output / "environment.json", report)
    return report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--d4j", default="defects4j")
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()
    result = inspect_environment(args.d4j, args.output)
    print(f"ready={result['ready']}; evidence={args.output / 'environment.json'}")
    return 0 if result["ready"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
