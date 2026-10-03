"""Independently validate Champ/Beam Math getField intake; no execution or approvals.

Run as a module with --output <new-path>. Outputs are created exclusively.
inspect() checks the received files against exact immutable branch blobs.
"""
from __future__ import annotations
import argparse
from datetime import datetime, timezone
import io
import json
from pathlib import Path
import re
import subprocess
import xml.etree.ElementTree as ET
from .review_beam_readiness import digest, sha256, require, validate_archive

ROOT = Path(__file__).resolve().parents[3]
COMMIT = "4c9ccf7ed5fcaee1fb0c70a2b7249da61c17e8f6"
BASE = "docs/api854/evidence/beam-champ-math-field-20261003"
LATEST = BASE + "-attempt2"
ACCEPTANCE = "output/api854-provider-preflight-20261003/champ-math-field-candidate-acceptance-v1.json"
ACCEPTOR = "output/api854-provider-preflight-20261003/accept-math-field-candidates.py"
SOURCE = "output/api854-20261003/champ-prepare-v7-current-v1/Math-1/fixed-source/src/main/java/org/apache/commons/math3/fraction"
V7_SOURCE = "output/api854-20261003/prepare-v7-twenty-bug-development/Math-1/fixed-source/src/main/java/org/apache/commons/math3/fraction"
ORIGINAL_SWEEP = "docs/api854/evidence/beam-v7-fixed-sweep-20261003/Math-1/record.json"
CLASSES = ("BigFraction", "Fraction")
FIELDS = ("class", "constructor_types", "method", "parameter_types")
ADDITION = (
    '            if (pilot && (name.equals("org.apache.commons.math3.fraction.BigFractionField")\n'
    '                    || name.equals("org.apache.commons.math3.fraction.FractionField")))\n'
    '                return "fraction-field:runtime=" + projection(call(result, "getRuntimeClass", new Class<?>[]{}), depth + 1)\n'
    '                    + ":zero=" + projection(call(result, "getZero", new Class<?>[]{}), depth + 1)\n'
    '                    + ":one=" + projection(call(result, "getOne", new Class<?>[]{}), depth + 1);\n'
)


def signature(target):
    return tuple(target[field] for field in FIELDS)


def target(name):
    return {"class": "org.apache.commons.math3.fraction." + name,
            "constructor_types": "double", "method": "getField", "parameter_types": ""}


def git_blobs(root):
    result = subprocess.run(["git", "-C", str(root), "ls-tree", "-r", "--name-only", COMMIT, "--", BASE, LATEST],
                            check=True, capture_output=True, timeout=60)
    names = result.stdout.decode().splitlines()
    names += [ACCEPTANCE, ACCEPTOR, ORIGINAL_SWEEP, "algorithms/java/SqaProbe.java"]
    names += [folder + "/" + name + ".java" for folder in (SOURCE, V7_SOURCE) for name in CLASSES]
    names = sorted(set(names))
    result = subprocess.run(["git", "-C", str(root), "cat-file", "--batch"],
                            input=("\n".join(COMMIT + ":" + name for name in names) + "\n").encode(),
                            check=True, capture_output=True, timeout=60)
    stream, blobs = io.BytesIO(result.stdout), {}
    for name in names:
        header = stream.readline().decode("ascii").strip().split()
        require(len(header) == 3 and header[1] == "blob", "Missing pinned blob: " + name)
        data = stream.read(int(header[2]))
        require(stream.read(1) == b"\n", "Malformed pinned blob: " + name)
        blobs[name] = data
    return blobs


def inspect(root: Path = ROOT, require_received: bool = True):
    root = Path(root).resolve()
    report = {
        "schema": "aom-math-field-intake-audit-v1", "source_commit": COMMIT,
        "reviewed_at_utc": datetime.now(timezone.utc).isoformat(),
        "validation_kind": "received_file_validation_not_new_defects4j_execution",
        "status": "fail", "issues": [], "safe_to_integrate_into_next_development_preparation": False,
        "primary": False, "joint_semantic_approval": False, "gate_a_passed": False,
        "new_defects4j_executions": 0, "live_requests": 0, "queue_mutations": 0,
        "limitations": ["Exactly two reviewed getField signatures; this does not approve other constructors/methods or the candidate helper policy wholesale.",
                       "Received logs/results are validated; no Defects4J stage is rerun.",
                       "Red JSON records have no separate red-test command/log; they are cross-checked with the original fixed diagnostic sweep.",
                       "Finite constructor examples do not cover the full constructor input domain.",
                       "The field result is invariant across receiver values; differing complete observations come from preserved receiver state.",
                       "Historical attempt 1 failed the runner's class/type string expectation before evaluation; it remains preserved."],
    }
    try:
        blobs = git_blobs(root)
        load = lambda relative: json.loads(blobs[relative])
        received = 0
        for name, data in blobs.items():
            if name == "algorithms/java/SqaProbe.java":
                continue
            path = root / name
            if path.is_file():
                require(path.read_bytes() == data, "Received file differs from pinned intake: " + name)
                received += 1
            elif require_received and name != "algorithms/java/SqaProbe.java":
                raise ValueError("Received file missing: " + name)
        # Current runtime may contain newer accepted recipes; historical helper is verified from Git.
        helper_name = "algorithms/java/SqaProbe.java"
        # Exclude the current helper from received equality: it is deliberately composed separately.
        report["received_files_checked"] = received
        report["received_files_required"] = require_received
        packet_counts = {}
        for packet in (BASE, LATEST):
            manifest = load(packet + "/checksums.json")
            actual = {name[len(packet) + 1:] for name in blobs if name.startswith(packet + "/")}
            require(actual == set(manifest) | {"checksums.json"}, "Packet manifest incomplete: " + packet)
            for name, expected in manifest.items():
                require(not Path(name).is_absolute() and ".." not in Path(name).parts, "Unsafe packet path")
                require(digest(blobs[packet + "/" + name]) == expected, "Pinned packet checksum differs: " + packet + "/" + name)
            packet_counts[packet] = {"files_checked": len(manifest), "checksums_sha256": digest(blobs[packet + "/checksums.json"])}
        report["packet_checksums"] = packet_counts
        first_failure = load(BASE + "/failure.json")
        require(first_failure["status"] == "development_runner_assertion_failed" and first_failure["evaluation_attempted"] is False,
                "Historical failed attempt was relabelled")
        require(first_failure["script_sha256"] == digest(blobs[BASE + "/run_candidate.py"]), "First attempt runner binding differs")
        receipt, acceptance = load(LATEST + "/receipt.json"), load(ACCEPTANCE)
        require(receipt["primary"] is False and receipt["shared_policy_changed"] is False and receipt["joint_semantic_approval"] is False,
                "Math development receipt claims global approval")
        require(receipt["real_kku_requests"] == receipt["queue_mutations"] == 0, "Math receipt claims mutations")
        require(acceptance["accepted_count"] == 2 and acceptance["denominator"] == 691, "Acceptance scope differs")
        require({signature(row["target"]) for row in acceptance["accepted_candidates"]} == {signature(target(name)) for name in CLASSES},
                "Accepted exact signatures differ")
        require(acceptance["auditor_sha256"] == digest(blobs[ACCEPTOR]), "Champ source-review script hash differs")
        require(acceptance["gate_a_passed"] is False and acceptance["pilot_authorized"] is False and acceptance["final_condition_approved"] is False
                and acceptance["primary_results_added"] == acceptance["provider_requests"] == acceptance["queue_mutations"] == 0,
                "Champ acceptance claims global authorization")
        for field in ("candidate_probe_sha256", "candidate_policy_id", "suite_sha256", "result_sha256"):
            require(acceptance[field] == receipt[field], "Acceptance/receipt binding differs: " + field)
        original = blobs[helper_name].decode()
        candidate = blobs[LATEST + "/helper-src/algorithms/java/SqaProbe.java"].decode()
        require(digest(blobs[helper_name]) == receipt["original_probe_sha256"], "Original helper SHA differs")
        require(digest(candidate.encode()) == receipt["candidate_probe_sha256"], "Candidate helper SHA differs")
        anchor = "            if (pilot && result instanceof java.lang.reflect.Type)"
        expected = original.replace(anchor, ADDITION + anchor).replace("beam-explicit-fixtures-v5-proposal", receipt["candidate_policy_id"])
        require(original.count(anchor) == 1 and candidate == expected, "Candidate contains unreviewed helper changes")
        require(receipt["runner_sha256"] == digest(blobs[LATEST + "/run_candidate.py"]), "Latest runner SHA differs")
        sources = []
        for name in CLASSES:
            accepted = next(row for row in acceptance["accepted_candidates"] if row["target"] == target(name))
            source_data = blobs[SOURCE + "/" + name + ".java"]
            require(source_data == blobs[V7_SOURCE + "/" + name + ".java"], "Champ/original V7 production source differs: " + name)
            require(digest(source_data) == accepted["target_source_sha256"], "Target source SHA differs: " + name)
            text = source_data.decode()
            require(re.search(r"public\s+" + name + r"Field\s+getField\(\)\s*\{\s*return\s+" + name + r"Field\.getInstance\(\);\s*\}", text),
                    "getField source reads/mutates receiver or changes factory: " + name)
            factory_path = LATEST + "/supplemental-fixed-source/" + name + "Field.java"
            factory = blobs[factory_path].decode()
            source_key = "src/main/java/org/apache/commons/math3/fraction/" + name + "Field.java"
            factory_hash = digest(blobs[factory_path])
            require(factory_hash == accepted["supplemental_factory_sha256"] == receipt["supplemental_fixed_source_sha256"][source_key],
                    "Factory source binding differs: " + name)
            for method, returned in (("getZero", name + ".ZERO"), ("getOne", name + ".ONE"), ("getRuntimeClass", name + ".class")):
                require(re.search(method + r"\(\)\s*\{\s*return\s+" + re.escape(returned) + r";\s*\}", factory),
                        "Factory structure differs: " + name + "." + method)
            require(re.search(r"getInstance\(\)\s*\{\s*return\s+LazyHolder\.INSTANCE;\s*\}", factory),
                    "Factory singleton source differs: " + name)
            sources.append({"target": target(name), "source_sha256": digest(source_data), "factory_source_path": factory_path,
                            "factory_sha256": factory_hash, "source_returns_singleton_without_receiver_mutation": True})
        red, green = load(LATEST + "/red.json"), load(LATEST + "/green.json")
        require(red["passed"] is False and red["candidate"] is False and green["passed"] is True and green["candidate"] is True,
                "Red/green status differs")
        regression_hash = digest(blobs[LATEST + "/test_field_oracle.py"])
        require(red["regression_probe_sha256"] == green["regression_probe_sha256"] == regression_hash, "Regression source hash differs")
        original_cases = load(ORIGINAL_SWEEP)["cases"]
        for name in CLASSES:
            original_case = next(row for row in original_cases if row["target"] == target(name))
            red_case = next(row for row in red["records"] if row["target"] == target(name))
            require(red_case["first"] == red_case["second"] == original_case["first"] == original_case["second"]
                    == {"status": "fixture_error", "reason": "No structural oracle: org.apache.commons.math3.fraction." + name + "Field"},
                    "Red result does not match original missing field oracle")
        observations = load(LATEST + "/observations.json")
        require(len(observations) == 4, "Math observation count differs")
        for name in CLASSES:
            rows = [row for row in observations if row["target"] == target(name)]
            require(len(rows) == 2 and {tuple(row["vector"]) for row in rows} == {(0.5, 0.5, 0.5), (-0.5, -0.5, -0.5)}, "Receiver vectors differ")
            for row in rows:
                state = "7/4" if row["vector"][0] > 0 else "-3/4"
                outcome = "value:fraction-field:runtime=type:org.apache.commons.math3.fraction." + name + ":zero=fraction:0/1:one=fraction:1/1|state=fraction:" + state
                require(row["retained"] is True and row["fixed_first"] == row["fixed_second"]
                        == {"status": "ok", "outcome": outcome, "target_invoked": True}, "Structural field/receiver observation differs")
            green_case = next(row for row in green["records"] if row["target"] == target(name))
            require(green_case["first"] == green_case["second"] == rows[0]["fixed_first"] and green_case["passed"] is True,
                    "Green result/observation differs")
        require(len(red["records"]) == len(green["records"]) == 2, "Regression target count differs")
        result = load(LATEST + "/evaluation/record.json")
        require(receipt["result_sha256"] == digest(blobs[LATEST + "/evaluation/record.json"]), "Evaluation result SHA differs")
        require(result["status"] == receipt["status"] == "complete" and result["test_count"] == 4
                and result["fixed_validation"] == "passed_twice" and result["compile_status"] == "passed" and result["fault_detected"] is receipt["fault_detected"] is False,
                "Evaluation status/count differs")
        require(result["project"] == "Math" and result["bug_id"] == 1, "Evaluation project/version differs")
        stages = {}
        classes = ["org.apache.commons.math3.fraction." + name for name in CLASSES]
        require(result["instrument_classes"] == classes, "Instrumented classes differ")
        require(blobs[LATEST + "/evaluation/instrument-classes.txt"].decode().splitlines() == classes, "Instrumentation file differs")
        for stage in ("fixed-1", "fixed-2", "buggy", "coverage"):
            relative = LATEST + "/evaluation/" + stage
            command = load(relative + "/command.json")
            require(command["exit_code"] == 0 and command["timed_out"] is False, "Retained stage command failed: " + stage)
            require(all(result["stages"][stage].get(key) == value for key, value in command.items()), "Stage record binding differs: " + stage)
            words = command["command"]
            require(("coverage" if stage == "coverage" else "test") in words and words[words.index("-s") + 1] == result["suite_path"], "Wrong command/suite: " + stage)
            require(words[words.index("-w") + 1] == result["buggy_worktree" if stage == "buggy" else "fixed_worktree"], "Wrong condition: " + stage)
            log = blobs[relative + "/command.log"].decode()
            require(re.search(r"compile.gen.tests.*OK", log) and re.search(r"run.gen.tests.*OK", log), "Missing compile/run success log: " + stage)
            if stage != "coverage":
                require("Failing tests: 0" in log and result["stages"][stage]["failure_count"] == 0
                        and result["stages"][stage]["failing_tests"] == [], "Recorded test failures: " + stage)
            require(not blobs[relative + "/failing_tests"].strip(), "Retained failing_tests nonempty")
            tests = blobs[relative + "/all_tests"].decode().splitlines()
            require(len(tests) == len(set(tests)) == 4, "Executed test identities differ")
            counts = load(relative + "/sqa-stage-counts.json")
            require(counts == {"schema_version": 1, "executed": 4, "skipped": 0, "target_checks": 4}, "Stage execution counts differ")
            stages[stage] = counts
        for relative in ("compile-candidate", "green-test", "checkout-f", "checkout-b",
                         "evaluation/environment", "evaluation/java-version", "evaluation/d4j-version"):
            command = load(LATEST + "/" + relative + "/command.json")
            require(command["exit_code"] == 0 and command["timed_out"] is False, "Retained setup/regression command failed")
            if relative in ("checkout-f", "checkout-b"):
                words = command["command"]
                require("checkout" in words and words[words.index("-p") + 1] == "Math"
                        and words[words.index("-v") + 1] == ("1f" if relative == "checkout-f" else "1b"),
                        "Retained source checkout project/version differs")
        green_log = json.loads(blobs[LATEST + "/green-test/command.log"])
        require(green_log == {"passed": True, "candidate": True, "statuses": ["ok", "ok"]}, "Green regression log differs")
        xml = ET.fromstring(blobs[LATEST + "/evaluation/coverage/coverage.xml"])
        coverage = []
        for name in CLASSES:
            class_name = "org.apache.commons.math3.fraction." + name
            descriptor = "()Lorg/apache/commons/math3/fraction/" + name + "Field;"
            methods = [method for cls in xml.iter("class") if cls.get("name") == class_name
                       for method in cls.findall("./methods/method") if method.get("name") == "getField" and method.get("signature") == descriptor]
            require(len(methods) == 1, "Exact getField return descriptor missing/ambiguous")
            hits = [int(line.get("hits", "0")) for line in methods[0].findall("./lines/line")]
            require(hits and any(hit > 0 for hit in hits), "Exact getField has no positive coverage")
            coverage.append({"class": class_name, "method": "getField", "descriptor": descriptor, "line_hits": hits})
        # Archive helper validates evaluated archive copies, Java bytes and source manifest.
        archive = validate_archive(root / LATEST, receipt, result) if require_received else {"suite_sha256": receipt["suite_sha256"], "deferred_received_archive_check": True}
        report.update({"status": "pass", "safe_to_integrate_into_next_development_preparation": require_received,
                       "accepted_signatures": [target(name) for name in CLASSES], "source_review": sources,
                       "receipt_sha256": digest(blobs[LATEST + "/receipt.json"]), "champ_acceptance_sha256": digest(blobs[ACCEPTANCE]),
                       "suite_sha256": receipt["suite_sha256"], "result_sha256": receipt["result_sha256"],
                       "original_probe_sha256": receipt["original_probe_sha256"], "candidate_probe_sha256": receipt["candidate_probe_sha256"],
                       "candidate_policy_id": receipt["candidate_policy_id"], "helper_delta": {"projection": ADDITION,
                           "other_change": "Candidate changes PILOT_FIXTURES policy ID; port projection only under the new composed policy.",
                           "wholesale_candidate_replacement_accepted": False},
                       "structural_oracle": {"runtime": "element class", "zero": "fraction:0/1", "one": "fraction:1/1",
                           "receiver_states": ["fraction:7/4", "fraction:-3/4"], "fixed_observations": 4,
                           "repeats_per_observation": 2, "target_invoked": True, "field_result_invariant": True},
                       "stages": stages, "coverage": coverage, "archive": archive,
                       "historical_attempt1_preserved": first_failure,
                       "fixed_source_version": "Math-1f",
                       "supplemental_source_verification": "Retained runner compares supplemental factory checkout bytes with that fixed checkout HEAD Git bytes.",
                       "historical_capability_counts": {"selected": acceptance["current_shared_supported"],
                           "unsupported": acceptance["current_shared_unsupported"], "denominator": acceptance["denominator"]},
                       "integration_conditions": ["Port the five-line field projection into the latest helper under a new policy; keep existing setter/JDOM recipes.",
                           "Add both immutable field factory sources/knowledge to identical shared inputs for all four study approaches.",
                           "Rebuild and verify source/context/recipe/targets/prompt/index/protocol/runner/consumer/execution bindings before changing preparation counts.",
                           "Recalculate final limits/reserve from the newly composed preparation.",
                           "Preserve historical v7/v8 and both Math candidate attempt packets; primary results and Gate A remain closed."]})
    except (OSError, ValueError, KeyError, TypeError, subprocess.SubprocessError, ET.ParseError) as error:
        report["issues"].append(str(error))
    report["inspector_sha256"] = sha256(Path(__file__))
    return report


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, help="Exclusively create a new received-file audit JSON")
    parser.add_argument("--allow-unmerged", action="store_true", help="Preview immutable branch blobs; defer received archive validation")
    args = parser.parse_args()
    report = inspect(require_received=not args.allow_unmerged)
    if args.output:
        with args.output.open("x", encoding="utf-8", newline="\n") as stream:
            json.dump(report, stream, indent=2, ensure_ascii=False)
            stream.write("\n")
    print(json.dumps({"status": report["status"], "issues": report["issues"],
                      "safe_for_development_composition": report["safe_to_integrate_into_next_development_preparation"],
                      "received_files_required": report.get("received_files_required")}))
    return 0 if report["status"] == "pass" else 1


if __name__ == "__main__":
    raise SystemExit(main())

