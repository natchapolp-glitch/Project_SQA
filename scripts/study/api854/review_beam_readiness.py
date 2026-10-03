"""Validate received Beam 532baa31 evidence without running Defects4J or granting approval.

Run: python -m scripts.study.api854.review_beam_readiness --output <new-path>
inspect() is read-only; the CLI creates its output exclusively and never replaces it.
Only immutable Git blobs are used to verify the historical runtime/helper bindings.
"""
from __future__ import annotations

import argparse
from collections import Counter
from datetime import datetime, timezone
import hashlib
import io
import json
from pathlib import Path
import re
import subprocess
import tarfile
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[3]
COMMIT = "532baa317cd0c6895a0f1d7a3b1ca9f5544132f3"
EVIDENCE = "docs/api854/evidence"
OUTPUT = "output/api854-20261003"
V3 = f"{OUTPUT}/prepare-v3"
V7 = f"{OUTPUT}/prepare-v7-twenty-bug-development"
PACKETS = {
    "sweep": f"{EVIDENCE}/beam-v7-fixed-sweep-20261003",
    "readiness": f"{EVIDENCE}/beam-v7-readiness-20261003",
    "setter": f"{EVIDENCE}/beam-v7-setter-recipe-20261003",
    "jdom": f"{EVIDENCE}/beam-v7-jdom-oracle-20261003",
}
FIELDS = ("class", "constructor_types", "method", "parameter_types")
NORMAL = "stable_normal_observation_oracle_review_needed"
EXCEPTION = "target_exception_review_needed"


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def sha256(path: Path) -> str:
    return digest(path.read_bytes())


def read_json(path: Path):
    return json.loads(path.read_text(encoding="utf-8"))


def require(condition: bool, reason: str):
    if not condition:
        raise ValueError(reason)


def identity(target):
    return tuple(target[field] for field in FIELDS)


def category(case):
    first = case["first"]
    if first["status"] != "ok":
        return first["status"]
    if not case["repeat_equal"]:
        return "repeat_unstable"
    if first.get("target_invoked") is not True:
        return "invocation_unverified"
    return EXCEPTION if first.get("outcome", "").startswith("exception:") else NORMAL


def git_blobs(root: Path, paths):
    """Read exact bytes in one Git process; never use shell/text-mode conversion."""
    names = sorted(set(paths))
    command = ["git", "-C", str(root), "cat-file", "--batch"]
    result = subprocess.run(
        command, input=("\n".join(f"{COMMIT}:{name}" for name in names) + "\n").encode(),
        capture_output=True, check=True, timeout=60,
    )
    stream = io.BytesIO(result.stdout)
    blobs = {}
    for name in names:
        header = stream.readline().decode("ascii").strip().split()
        require(len(header) == 3 and header[1] == "blob", f"Missing pinned Git blob: {name}")
        size = int(header[2])
        data = stream.read(size)
        require(len(data) == size and stream.read(1) == b"\n", f"Invalid Git blob response: {name}")
        blobs[name] = data
    require(stream.read() == b"", "Unexpected trailing Git blob output")
    return blobs


def validate_packet(packet: Path, pinned_manifest: bytes):
    """Public helper also permits isolated tamper tests without touching evidence."""
    manifest = packet / "checksums.json"
    require(manifest.read_bytes() == pinned_manifest, f"Checksum manifest differs from {COMMIT}: {packet.name}")
    checks = json.loads(pinned_manifest)
    require(checks, f"Empty checksum manifest: {packet.name}")
    for name, expected in checks.items():
        relative = Path(name)
        require(not relative.is_absolute() and ".." not in relative.parts, f"Unsafe checksum path: {name}")
        path = (packet / relative).resolve(strict=True)
        require(path.is_relative_to(packet.resolve()), f"Escaping checksum path: {name}")
        require(sha256(path) == expected, f"Checksum mismatch: {packet.name}/{name}")
    actual = {p.relative_to(packet).as_posix() for p in packet.rglob("*") if p.is_file()}
    require(actual == set(checks) | {"checksums.json"}, f"Packet has unlisted/missing files: {packet.name}")
    return {"status": "pass", "files_checked": len(checks), "checksums_sha256": digest(pinned_manifest)}


def validate_archive(packet: Path, receipt, result):
    manifest = read_json(packet / "package/suite-manifest.json")
    archive = packet / "package/suite.tar.bz2"
    expected = receipt["suite_sha256"]
    require(sha256(archive) == expected == result["suite_sha256"] == manifest["suite_sha256"], "Suite SHA bindings differ")
    copied = packet / "evaluation" / Path(result["suite_path"]).name
    require(sha256(copied) == expected, "Evaluated archive differs from packaged archive")
    require(manifest["size_bytes"] == archive.stat().st_size, "Suite size differs")
    source_map = manifest["source_sha256"]
    with tarfile.open(archive, "r:bz2") as suite:
        files = [member for member in suite.getmembers() if member.isfile()]
        require(all(not member.issym() and not member.islnk() for member in suite.getmembers()), "Suite contains links")
        require({member.name for member in files} == set(source_map), "Suite source member list differs")
        require(len(files) == len(source_map) == result["java_source_files"], "Suite Java source count differs")
        total = 0
        for member in files:
            require(member.name.endswith(".java"), f"Non-Java suite member: {member.name}")
            require(not Path(member.name).is_absolute() and ".." not in Path(member.name).parts, "Unsafe suite member")
            data = suite.extractfile(member).read()
            source = packet / "sources" / member.name
            require(data == source.read_bytes() and digest(data) == source_map[member.name], f"Retained source differs: {member.name}")
            total += len(data)
        require(total == manifest["source_bytes"], "Suite source byte count differs")
    return {"suite_sha256": expected, "java_files": len(source_map), "source_sha256": source_map}


class Audit:
    def __init__(self, root):
        self.root = Path(root).resolve()
        self.report = {
            "schema": "aom-beam532baa31-readiness-audit-v1",
            "reviewed_at_utc": datetime.now(timezone.utc).isoformat(),
            "source_commit": COMMIT,
            "validation_kind": "received_file_validation_not_new_defects4j_execution",
            "status": "fail",
            "issues": [],
            "checked_sections": [],
            "packet_checksums": {},
            "receipt_sha256": {},
            "primary": False,
            "team_semantic_approval": False,
            "gate_a_passed": False,
            "new_defects4j_executions": 0,
            "live_requests": 0,
            "queue_mutations": 0,
            "limitations": [
                "Received commands, logs, counts and source/archive bytes are checked; no Defects4J command is rerun.",
                "Midpoint diagnostics do not prove a meaningful oracle, full input-domain coverage, or host acceptance.",
                "Two retained fixed runs do not prove absence of flakiness.",
                "JDOM red.json has no separate red-test command/log receipt; it is cross-checked against the retained original sweep case.",
                "All four empty-enum targets remain unresolved; invalid-input boundary policy requires a real joint decision.",
            ],
        }

    def phase(self, name, function):
        try:
            function()
            self.report["checked_sections"].append(name)
        except (OSError, ValueError, KeyError, TypeError, subprocess.SubprocessError, tarfile.TarError, ET.ParseError) as error:
            self.report["issues"].append({"section": name, "reason": str(error)})

    def pin(self, relative):
        actual = (self.root / relative).read_bytes()
        require(actual == self.blobs[relative], f"Received file differs from pinned commit: {relative}")
        return actual

    def initialize(self):
        ready = read_json(self.root / PACKETS["readiness"] / "readiness.json")
        sweep = read_json(self.root / PACKETS["sweep"] / "index.json")
        names = [f"{folder}/checksums.json" for folder in PACKETS.values()]
        names += [f"{V3}/index.json", f"{V7}/index.json",
                  "algorithms/java/SqaProbe.java", "scripts/study/api854/inspect_v7_readiness.py",
                  "scripts/study/api854/compose_v7_development.py",
                  f"{EVIDENCE}/beam-pilot-v5-capabilities-20261003/index.json"]
        names += list(ready["runtime_source_sha256"])
        for record in sweep["records"]:
            bug = f"{record['project']}-{record['bug_id']}"
            names += [f"{V3}/{bug}/targets.json", f"{V7}/{bug}/targets.json",
                      f"{V7}/{bug}/capability-exclusions.json", f"{V7}/{bug}/context-manifest.json"]
        self.blobs = git_blobs(self.root, names)

    def packet(self, label):
        relative = PACKETS[label]
        self.report["packet_checksums"][label] = validate_packet(
            self.root / relative, self.blobs[f"{relative}/checksums.json"]
        )

    def historical_bindings(self):
        ready = read_json(self.root / PACKETS["readiness"] / "readiness.json")
        sweep = read_json(self.root / PACKETS["sweep"] / "index.json")
        v7 = json.loads(self.pin(f"{V7}/index.json"))
        v3_hash = digest(self.pin(f"{V3}/index.json"))
        require(v7["source_v3_index_sha256"] == v3_hash, "V7 original inventory index binding differs")
        prep_hash = digest(self.blobs[f"{V7}/index.json"])
        require(ready["preparation_index_sha256"] == sweep["preparation_index_sha256"] == prep_hash, "Preparation index binding differs")
        require(ready["runtime_source_sha256"] == v7["runtime_source_sha256"], "Historical runtime maps differ")
        for name, expected in ready["runtime_source_sha256"].items():
            require(digest(self.blobs[name]) == expected, f"Historical runtime hash differs: {name}")
        helper_hash = digest(self.blobs["algorithms/java/SqaProbe.java"])
        require(helper_hash == sweep["probe_sha256"], "Sweep original helper binding differs")
        require(sweep["inspector_sha256"] == sha256(self.root / PACKETS["readiness"] / "fixed_sweep.py"), "Sweep inspector binding differs")
        require(ready["inspector_sha256"] == digest(self.blobs["scripts/study/api854/inspect_v7_readiness.py"]), "Readiness inspector binding differs")
        require(ready["composer_sha256"] == digest(self.blobs["scripts/study/api854/compose_v7_development.py"]), "Readiness composer binding differs")
        require(ready["builder_sha256"] == v7["builder_sha256"], "Readiness builder binding differs")
        require(ready["capability_index_sha256"] == digest(self.pin(f"{EVIDENCE}/beam-pilot-v5-capabilities-20261003/index.json")), "Capability index binding differs")
        require((ready["required_common_declarations"], ready["selected"], ready["unsupported"]) == (691, 377, 314), "Historical readiness counts differ")
        require(ready["gate_a_passed"] is False and ready["generation_authorized"] is False, "Historical readiness claims authorization")
        self.report["historical_bindings"] = {
            "original_inventory_index_sha256": v3_hash, "preparation_index_sha256": prep_hash,
            "original_helper_sha256": helper_hash, "runtime_source_files": len(ready["runtime_source_sha256"]),
            "runtime_checked_against": f"Exact Git blobs at {COMMIT}, not modified current runtime",
        }

    def command(self, folder, expected_operation=None):
        command = read_json(folder / "command.json")
        require(command["exit_code"] == 0 and command["timed_out"] is False, f"Command failed/timed out: {folder.name}")
        require(command["duration_seconds"] >= 0, f"Invalid duration: {folder.name}")
        require(Path(command["log"]).name == "command.log" and (folder / "command.log").is_file(), "Missing command log")
        if expected_operation:
            require(expected_operation in command["command"], f"Command operation differs: {folder.name}")
        return command

    def sweep(self):
        index = read_json(self.root / PACKETS["sweep"] / "index.json")
        summary = read_json(self.root / PACKETS["readiness"] / "sweep-summary.json")
        require(index["primary"] is False and index["semantic_approved"] is False, "Sweep claims semantic approval")
        require(index["real_kku_requests"] == index["queue_mutations"] == index["capability_changes"] == 0, "Sweep claims mutations")
        require(len(index["records"]) == 20, "Sweep bug count differs")
        seen, categories, unsupported, per_bug, worklist = set(), Counter(), Counter(), [], []
        source_files = 0
        self.command(self.root / PACKETS["sweep"] / "compile-probe", "javac")
        for record in index["records"]:
            bug = f"{record['project']}-{record['bug_id']}"
            require(bug not in seen, f"Repeated bug: {bug}")
            seen.add(bug)
            folder = self.root / PACKETS["sweep"] / bug
            require(record == read_json(folder / "record.json"), f"Index/raw record differs: {bug}")
            require(record["status"] == "fixed_sweep_complete" and record["primary"] is False and record["semantic_approved"] is False, f"Incomplete/approved sweep: {bug}")
            original = json.loads(self.pin(f"{V3}/{bug}/targets.json"))["targets"]
            selected = json.loads(self.pin(f"{V7}/{bug}/targets.json"))["targets"]
            excluded = json.loads(self.pin(f"{V7}/{bug}/capability-exclusions.json"))["excluded"]
            original_keys = {identity(t) for t in original}
            selected_keys = {identity(t) for t in selected}
            exclusion_map = {identity(row["target"]): row["reason"] for row in excluded}
            require(len(original) == len(original_keys), f"Duplicate original declaration: {bug}")
            require(not selected_keys & exclusion_map.keys() and selected_keys | exclusion_map.keys() == original_keys, f"V7 partition differs: {bug}")
            require([case["target"] for case in record["cases"]] == original, f"Sweep differs from original inventory: {bug}")
            expected_raw = {f"case-{n:03d}.json" for n in range(len(original))}
            require({path.name for path in folder.glob("case-*.json")} == expected_raw, f"Raw case list differs: {bug}")
            sources = json.loads(self.pin(f"{V7}/{bug}/context-manifest.json"))["source_files"]
            java = {s["path"]: s["sha256"] for s in sources if s["path"].endswith(".java")}
            comparison = record["fixed_source_comparison"]
            require(not comparison["mismatches"] and comparison["matched"], f"Fixed source mismatch: {bug}")
            require({item["path"]: item["expected"] for item in comparison["matched"]} == java, f"Compared Java source list differs: {bug}")
            for item in comparison["matched"]:
                require(item["actual"] == item["expected"] == sha256(self.root / V7 / bug / "fixed-source" / item["path"]), f"Retained source bytes differ: {bug}/{item['path']}")
                source_files += 1
            counts, statuses = Counter(), Counter()
            for n, case in enumerate(record["cases"]):
                require(case == read_json(folder / f"case-{n:03d}.json"), f"Raw case differs: {bug}/{n}")
                count = sum(bool(p) for p in (case["target"]["constructor_types"] + "," + case["target"]["parameter_types"]).split(","))
                require(case["vector"] == [0.5] * max(3, count * 3), f"Non-midpoint vector: {bug}/{n}")
                require(case["first"] == case["second"] and case["repeat_equal"] is True, f"Repeated diagnostic differs: {bug}/{n}")
                require(case["oracle_approved"] is False, f"Diagnostic claims oracle approval: {bug}/{n}")
                key = identity(case["target"])
                is_unsupported = key in exclusion_map
                require(case["v7_capability"] == ("unsupported" if is_unsupported else "selected"), f"Capability differs: {bug}/{n}")
                require(case["exclusion_reason"] == exclusion_map.get(key), f"Exclusion reason differs: {bug}/{n}")
                kind = category(case)
                counts[kind] += 1
                categories[kind] += 1
                statuses[case["first"]["status"]] += 1
                if is_unsupported:
                    unsupported[kind] += 1
                    worklist.append({"project": record["project"], "bug_id": record["bug_id"], "owner": record["owner"], "target": case["target"], "category": kind, "reason": case["first"].get("reason"), "outcome": case["first"].get("outcome"), "existing_exclusion": case["exclusion_reason"], "oracle_approved": False})
            require(dict(statuses) == record["status_counts"], f"Record status counts differ: {bug}")
            require(record["repeated_stable_invocations"] == sum(case["repeat_equal"] and case["first"].get("target_invoked") is True and case["first"]["status"] == "ok" for case in record["cases"]), f"Invocation count differs: {bug}")
            for stage, operation in (("checkout", "checkout"), ("compile", "compile"), ("export-cp", "export")):
                self.command(folder / stage, operation)
            per_bug.append({"project": record["project"], "bug_id": record["bug_id"], "status": record["status"], "attempted_declarations": len(record["cases"]), "categories": dict(counts), "error": record.get("error")})
        require(dict(categories) == {"fixture_error": 127, NORMAL: 514, EXCEPTION: 50}, "Re-derived diagnostic categories differ")
        require(dict(unsupported) == {"fixture_error": 126, NORMAL: 147, EXCEPTION: 41}, "Re-derived unsupported categories differ")
        require(summary["category_counts"] == dict(categories) and summary["bugs"] == per_bug and summary["unsupported_worklist"] == worklist, "Retained summarizer output differs from re-derived data")
        require(summary["complete_sweep"] is True and summary["attempted_declarations"] == 691 and len(worklist) == 314, "Sweep denominator/worklist differs")
        require(summary["sweep_index_sha256"] == sha256(self.root / PACKETS["sweep"] / "index.json"), "Summary sweep index binding differs")
        handoff = read_json(self.root / PACKETS["readiness"] / "handoff.json")
        historical = handoff["historical_summarizer_path"]
        require(Path(historical).name == historical, "Unsafe historical summarizer path")
        require(summary["summarizer_sha256"] == sha256(self.root / PACKETS["readiness"] / historical), "Historical summary script binding differs")
        self.report["sweep"] = {"bugs": 20, "declarations": sum(categories.values()), "fixed_attempts": sum(categories.values()) * 2, "categories": dict(categories), "unsupported": sum(unsupported.values()), "unsupported_categories": dict(unsupported), "fixed_java_source_comparisons": source_files, "raw_case_inventory_matches": True, "source_bytes_match_retained_v7": True}

    def suite(self, label, expected_tests, class_name, method_name, signature):
        packet = self.root / PACKETS[label]
        receipt = read_json(packet / "receipt.json")
        result = read_json(packet / "evaluation/record.json")
        require(receipt["primary"] is False and receipt["shared_policy_changed"] is False and receipt["team_semantic_approval"] is False, f"{label} receipt claims approval")
        require(receipt["real_kku_requests"] == receipt["queue_mutations"] == 0, f"{label} receipt claims mutations")
        require(receipt["result_sha256"] == sha256(packet / "evaluation/record.json"), f"{label} result SHA differs")
        runner = "run_recipe.py" if label == "setter" else "run_candidate.py"
        require(receipt["runner_sha256"] == sha256(packet / runner), f"{label} runner SHA differs")
        require(result["status"] == receipt["status"] == "complete" and result["fixed_validation"] == "passed_twice" and result["compile_status"] == "passed", f"{label} evaluation incomplete")
        require(result["fault_detected"] is receipt["fault_detected"] is False, f"{label} unexpected fault claim")
        require(result["test_count"] == expected_tests, f"{label} test count differs")
        archive = validate_archive(packet, receipt, result)
        manifest = read_json(packet / "package/suite-manifest.json")
        require(manifest["test_count"] == expected_tests and manifest["queue_published"] is False, f"{label} manifest count/publication differs")
        require(result["instrument_classes"] == [class_name] and (packet / "evaluation/instrument-classes.txt").read_text().splitlines() == [class_name], f"{label} instrumentation differs")
        expected_counts = {"schema_version": 1, "executed": expected_tests, "skipped": 0, "target_checks": expected_tests}
        stages = {}
        all_tests = None
        for stage in ("fixed-1", "fixed-2", "buggy", "coverage"):
            folder = packet / "evaluation" / stage
            command = self.command(folder, "coverage" if stage == "coverage" else "test")
            require(all(result["stages"][stage].get(k) == v for k, v in command.items()), f"{label}/{stage} record/command differs")
            words = command["command"]
            require(words[words.index("-w") + 1] == result["buggy_worktree" if stage == "buggy" else "fixed_worktree"], f"{label}/{stage} wrong condition")
            require(words[words.index("-s") + 1] == result["suite_path"], f"{label}/{stage} wrong suite")
            log = (folder / "command.log").read_text()
            require(re.search(r"compile.gen.tests.*OK", log) is not None and re.search(r"run.gen.tests.*OK", log) is not None, f"{label}/{stage} missing successful compile/run log")
            if stage != "coverage":
                require("Failing tests: 0" in log and result["stages"][stage]["failure_count"] == 0 and result["stages"][stage]["failing_tests"] == [], f"{label}/{stage} failures recorded")
            else:
                require("coverage.report" in log and "-i" in words, f"{label} coverage command/log incomplete")
            require(not (folder / "failing_tests").read_text().strip(), f"{label}/{stage} failing_tests nonempty")
            tests = (folder / "all_tests").read_text().splitlines()
            require(len(tests) == len(set(tests)) == expected_tests, f"{label}/{stage} executed tests differ")
            require(all_tests is None or set(tests) == all_tests, f"{label}/{stage} test identities differ")
            all_tests = set(tests)
            counts = read_json(folder / "sqa-stage-counts.json")
            require(counts == expected_counts, f"{label}/{stage} stage counts differ")
            stages[stage] = counts
        for stage in ("environment", "java-version", "d4j-version"):
            self.command(packet / "evaluation" / stage)
        for stage in ("checkout-f", "checkout-b"):
            self.command(packet / stage, "checkout")
        xml = ET.parse(packet / "evaluation/coverage/coverage.xml").getroot()
        methods = [method for cls in xml.iter("class") if cls.get("name") == class_name for method in cls.findall("./methods/method") if method.get("name") == method_name and method.get("signature") == signature]
        require(len(methods) == 1, f"{label} exact target coverage method missing/ambiguous")
        hits = [int(line.get("hits", "0")) for line in methods[0].findall("./lines/line")]
        require(hits and any(hit > 0 for hit in hits), f"{label} exact target has no coverage")
        if label == "setter":
            source = self.root / V7 / "Codec-1/fixed-source/src/java/org/apache/commons/codec/language/Metaphone.java"
            require(receipt["fixed_source_sha256"] == sha256(source), "Setter production-source hash differs")
        self.report["receipt_sha256"][label] = sha256(packet / "receipt.json")
        self.report[label] = {"status": "retained_development_proof_validated", "primary": False, "fault_detected": False, "test_count": expected_tests, "stages": stages, "archive": archive, "result_sha256": receipt["result_sha256"], "coverage_target": {"class": class_name, "method": method_name, "signature": signature, "line_hits": hits}}

    def retained_reports(self):
        base = self.root / PACKETS["readiness"]
        handoff = read_json(base / "handoff.json")
        verification = read_json(base / "verification.json")
        final = read_json(base / "verification-v2.json")
        gate = read_json(base / "gate-a.json")
        require(handoff["verification_sha256"] == final["prior_verification_sha256"] == sha256(base / "verification.json"), "Prior verification SHA differs")
        require(handoff["final_verification_sha256"] == sha256(base / "verification-v2.json"), "Final verification SHA differs")
        require(verification["verifier_sha256"] == sha256(base / "verify_packet.py"), "Original verifier script SHA differs")
        require(final["verifier_sha256"] == sha256(base / "verify_jdom.py"), "Final verifier script SHA differs")
        require(verification["validation_log_sha256"] == sha256(base / "validation.log"), "Validation log binding differs")
        for name, expected in handoff["docs_sha256"].items():
            require(Path(name).name == name, "Unsafe handoff document path")
            require(sha256(self.root / "docs/api854" / name) == expected, "Handoff document hash differs: " + name)
        require(handoff["declarations_probed"] == 691 and handoff["fixed_probe_attempts"] == 1382, "Handoff probe counts differ")
        require((handoff["fixture_errors"], handoff["normal_stable_observations"], handoff["target_exceptions_needing_review"]) == (127, 514, 50), "Handoff diagnostic grouping differs")
        require(handoff["unsupported_shared_declarations"] == 314 and handoff["primary_completed"] == 0, "Handoff denominator/results differ")
        require(handoff["shared_target_support_approved"] is False and handoff["team_semantic_approval"] is False and handoff["gate_a_passed"] is False, "Handoff claims approval")
        require(verification["status"] == final["status"] == "pass" and verification["primary"] is False and final["primary"] is False, "Retained verification status differs")
        require(verification["setter_coverage_line_hits"] == self.report["setter"]["coverage_target"]["line_hits"], "Original setter verification hits differ")
        require(final["attribute_iterator_coverage_hits"] == self.report["jdom"]["coverage_target"]["line_hits"], "Final JDOM verification hits differ")
        require(verification["recipe_stage_counts"] == self.report["setter"]["stages"] and final["stage_counts"] == self.report["jdom"]["stages"], "Retained verification stage counts differ")
        require(gate["gate_a_passed"] is False and gate["generation_authorized"] is False and gate["live_requests"] == gate["queue_mutations"] == 0, "Retained Gate A claims authorization")
        self.report["retained_reports"] = {
            "readiness_sha256": sha256(base / "readiness.json"),
            "handoff_sha256": sha256(base / "handoff.json"),
            "verification_sha256": sha256(base / "verification.json"),
            "verification_v2_sha256": sha256(base / "verification-v2.json"),
            "gate_a_sha256": sha256(base / "gate-a.json"),
            "historical_summarizer_path": handoff["historical_summarizer_path"],
            "historical_summarizer_sha256": sha256(base / handoff["historical_summarizer_path"]),
            "wording_correction": handoff["report_wording_correction"],
        }

    def jdom_regression(self):
        packet = self.root / PACKETS["jdom"]
        receipt = read_json(packet / "receipt.json")
        red, green = read_json(packet / "red.json"), read_json(packet / "green.json")
        require(receipt["original_probe_sha256"] == digest(self.blobs["algorithms/java/SqaProbe.java"]), "JDOM original probe differs")
        require(receipt["candidate_probe_sha256"] == sha256(packet / "helper-src/algorithms/java/SqaProbe.java"), "JDOM candidate probe differs")
        require(red["regression_probe_sha256"] == green["regression_probe_sha256"] == sha256(packet / "test_oracle.py"), "JDOM regression script binding differs")
        require(red["passed"] is False and red["candidate"] is False and green["passed"] is True and green["candidate"] is True, "JDOM red/green statuses differ")
        require(red["first"] == red["second"] == {"status": "fixture_error", "reason": "No structural oracle: org.jdom.Attribute"}, "JDOM original missing-attribute failure differs")
        original = read_json(self.root / PACKETS["sweep"] / "JxPath-1/record.json")
        failed = [case for case in original["cases"] if case["v7_capability"] == "selected" and case["first"]["status"] != "ok"]
        require(len(failed) == 1 and failed[0]["target"] == red["target"] == green["target"] and failed[0]["first"] == red["first"] and failed[0]["second"] == red["second"], "JDOM red evidence differs from original selected-target failure")
        observations = read_json(packet / "observations.json")
        require(len(observations) == 2, "JDOM observation count differs")
        require(observations[0]["vector"] != observations[1]["vector"], "JDOM vectors do not differ")
        require(observations[0]["fixed_first"]["outcome"] != observations[1]["fixed_first"]["outcome"], "JDOM observations do not differ")
        for row in observations:
            require(row["target"] == green["target"] and row["retained"] is True, "JDOM observed target differs")
            require(row["fixed_first"] == row["fixed_second"] and row["fixed_first"]["status"] == "ok" and row["fixed_first"]["target_invoked"] is True, "JDOM repeated fixed observation differs")
            require(all(term in row["fixed_first"]["outcome"] for term in ("jdom-attribute:name=", ":namespace=", ":value=")), "JDOM attribute projection incomplete")
        require(green["first"] == green["second"] == observations[0]["fixed_first"], "JDOM green/observation differs")
        command = self.command(packet / "green-test")
        green_log = json.loads((packet / "green-test/command.log").read_text())
        require("--candidate" in command["command"] and green_log["passed"] is True and green_log["candidate"] is True and green_log["first"] == green["first"], "JDOM green command/log differs")
        self.report["jdom_regression"] = {"red_missing_attribute_result_matches_original_sweep": True, "green_command_exit": command["exit_code"], "fixed_observation_vectors": 2, "fixed_repeats_per_vector": 2, "attribute_observations_differ": True, "original_probe_sha256": receipt["original_probe_sha256"], "candidate_probe_sha256": receipt["candidate_probe_sha256"], "candidate_policy_id": receipt["candidate_policy_id"]}


def inspect(root: Path = ROOT):
    """Return a read-only audit report. Pass another root for isolated fixture checks."""
    audit = Audit(root)
    audit.phase("immutable_git_provenance", audit.initialize)
    if not hasattr(audit, "blobs"):
        return audit.report
    for label in PACKETS:
        audit.phase(f"checksums:{label}", lambda label=label: audit.packet(label))
    audit.phase("historical_source_bindings", audit.historical_bindings)
    audit.phase("original_691_inventory_and_diagnostic_counts", audit.sweep)
    audit.phase("setter_archive_stages_and_exact_target_coverage", lambda: audit.suite("setter", 4, "org.apache.commons.codec.language.Metaphone", "setMaxCodeLen", "(I)V"))
    audit.phase("jdom_archive_stages_and_exact_target_coverage", lambda: audit.suite("jdom", 2, "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "attributeIterator", "(Lorg/apache/commons/jxpath/ri/QName;)Lorg/apache/commons/jxpath/ri/model/NodeIterator;"))
    audit.phase("jdom_original_red_candidate_green_and_repeated_observations", audit.jdom_regression)
    audit.phase("handoff_verification_and_gate_guardrails", audit.retained_reports)
    audit.report["status"] = "fail" if audit.report["issues"] else "pass"
    audit.report["inspector_sha256"] = sha256(Path(__file__))
    return audit.report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, help="Create a new audit JSON; existing output is never overwritten")
    args = parser.parse_args()
    report = inspect()
    if args.output:
        with args.output.open("x", encoding="utf-8", newline="\n") as stream:
            json.dump(report, stream, indent=2, ensure_ascii=False)
            stream.write("\n")
    print(json.dumps({"status": report["status"], "issues": report["issues"], "packet_files_checked": sum(row["files_checked"] for row in report["packet_checksums"].values()), "primary": False}))
    return 0 if report["status"] == "pass" else 1


if __name__ == "__main__":
    raise SystemExit(main())

