"""Preserve failed strict-source/audit attempts; prepare separately bound v2 producers."""
from pathlib import Path
import difflib
import subprocess
import sys

from aom_ready_csv import ROOT, DAY, INTAKE, PAIR, digest, load, save, seal, git
from aom_ready_messages import RECEIVED, MEASURED


def main():
    if sys.platform != "linux":
        raise ValueError("Read the actual WSL benchmark checkout")
    source = "src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java"
    tree = Path("/home/team/sqa-round2/worktrees") / MEASURED.name / "cmaes/b"
    native = subprocess.check_output(["git", "-C", str(tree), "show",
        "0833f45bffd40f44ba6f294d84e9bac8a9ba0a37:" + source])
    actual = (tree / source).read_bytes()
    expected_native = "d725202c7a4989c6ec37289578d7f8497d1f0a8c623c64fe3e1aabb9f00fa4a4"
    expected_benchmark = "4ddf7df1c7c8b9e3d45dc6a3410f802a2e5b641f37089655c3eb2f52cdfd5185"
    if digest(native) != expected_native or digest(actual) != expected_benchmark:
        raise ValueError("Source mismatch diagnosis itself does not match observed pins")
    failure = {"status": "strict_source_preflight_failed_before_tests", "api_requests": 0,
        "error": "Native upstream buggy ExtendedBufferedReader differs from reconstructed Defects4J Csv-1b",
        "native_buggy_sha256": expected_native, "benchmark_buggy_sha256": expected_benchmark,
        "buggy_checkout_commit": subprocess.check_output(["git", "-C", str(tree), "rev-parse", "HEAD"]).decode().strip(),
        "primary_results": 0, "no_test_outcomes_from_this_attempt": True,
        "resolution": "Preserve v1; prospective v2 explicitly binds installed benchmark source, unchanged suites"}
    save(MEASURED / "failed-attempt.json", failure)
    (MEASURED / "native-upstream-buggy.java").write_bytes(native)
    (MEASURED / "d4j-reconstructed-buggy.java").write_bytes(actual)
    (MEASURED / "native-vs-benchmark.diff").write_text("".join(difflib.unified_diff(
        native.decode().splitlines(keepends=True), actual.decode().splitlines(keepends=True),
        fromfile="native upstream 0833f45b", tofile="D4J_Csv_1_BUGGY_VERSION")), encoding="utf-8")
    (MEASURED / "producer.py").write_bytes((ROOT / "scripts/study/aom_ready_messages.py").read_bytes())
    seal(MEASURED)
    audit = ROOT / DAY / "aom-ready-csv-publication-v1"
    save(audit / "failed-attempt.json", {"status": "publication_git_worktree_scan_interrupted",
         "reason": "WSL Git diff blocked on Windows filesystem metadata RPC; stopped child after over five minutes",
         "tests_passed_before_interruption": 23, "experiment_runs": 0,
         "resolution": "v2 compares runtime Git blobs directly; no whole-worktree Git refresh"})
    seal(audit)
    text = (ROOT / "scripts/study/aom_ready_messages.py").read_text(encoding="utf-8")
    text = text.replace('"aom-ready-messages-d4j-v1"', '"aom-ready-messages-d4j-v2"')
    text = text.replace('receipt["condition"] + "-d4j-java11-los-angeles-replay-v1"',
                        'receipt["condition"] + "-d4j-benchmark-csv1-java11-los-angeles-v2"')
    text = text.replace('"generation_condition": receipt["condition"], "baseline_commit": BASE,',
        '"generation_condition": receipt["condition"], "baseline_commit": BASE,\n'
        '        "buggy_source_override": {"path": "' + source + '", "sha256": "' + expected_benchmark + '",\n'
        '            "reason": "actual reconstructed Defects4J Csv-1b, distinct from native upstream; v1 failure retained"},')
    text = text.replace('expected = preseal["production_source_sha256"][label]["sources"]',
        'expected = dict(preseal["production_source_sha256"][label]["sources"])\n'
        '                if revision == "b":\n'
        '                    expected["' + source + '"] = "' + expected_benchmark + '"')
    new = ROOT / "scripts/study/aom_ready_messages_d4j_v2.py"
    with new.open("x", encoding="utf-8", newline="\n") as stream:
        stream.write(text)
    text = (ROOT / "scripts/study/aom_ready_audit.py").read_text(encoding="utf-8")
    text = text.replace('"aom-ready-csv-publication-v1"', '"aom-ready-csv-publication-v2"')
    text = text.replace('if git("diff", "--name-only", "HEAD", "--", "algorithms", "experiments", "scripts/study/api854", "scripts/study/evaluate.py").strip():\n'
                        '        raise ValueError("Shared runtime/configuration was changed")',
        'for name in frozen["source_sha256"]:\n'
        '        if (ROOT / name).read_bytes() != git("show", "HEAD:" + name):\n'
        '            raise ValueError("Shared runtime/configuration changed from current branch HEAD")')
    new = ROOT / "scripts/study/aom_ready_audit_v2.py"
    with new.open("x", encoding="utf-8", newline="\n") as stream:
        stream.write(text)
    print("Retained v1 failures; v2 sources created exclusively, no evaluator/test/production repair")


if __name__ == "__main__":
    main()
