#!/usr/bin/env python3
"""Single-operator full inventory and offline pilot; never sends an AI request.

Run in WSL with Java 11. Completed stages are hash-checked on resume. An
interrupted or failed stage is retained and requires explicit recovery rather
than silently repeating generation. The old shared queues are never accessed.
"""
from __future__ import annotations

import argparse
import csv
from collections import Counter
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import sys
import time

from evaluate import EvaluationConfig, evaluate_run, run_command, runtime_versions, validate_worktree
from generate import generate_suite
from run import shared_targets
from api854.common import cpu_slot

ROOT = Path(__file__).resolve().parents[2]
BLUEPRINT = ROOT / "docs/api854/plans/friend-layout-full-v1"
METHODS = {
    "cmaes": "Algorithm1_CMAES", "fscs-art": "Algorithm2_FSCSART",
    "kku-claude": "AI1_KKU_Claude", "kku-gemini": "AI2_KKU_Gemini",
}
FIELDS = ["case", "project", "bug_id", "method", "state", "attempted",
          "provider_requested", "evaluated", "valid_on_fixed", "fault_detected",
          "executed_tests", "generated_test_methods", "line_covered", "line_total", "branch_covered", "branch_total",
          "generation_seconds", "evaluation_seconds", "protocol_hash", "result_path"]


def read(path):
    return json.loads(Path(path).read_text(encoding="utf-8"))


def write(path, value):
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    temp = path.with_name(path.name + ".tmp")
    temp.write_text(json.dumps(value, indent=2, ensure_ascii=False, allow_nan=False) + "\n", encoding="utf-8")
    temp.replace(path)


def sha(path):
    return hashlib.sha256(Path(path).read_bytes()).hexdigest()


def pins():
    names = ["scripts/study/solo_batch.py", "scripts/study/generate.py", "scripts/study/evaluate.py",
             "scripts/study/run.py", "scripts/study/api854/common.py", "algorithms/java/SqaProbe.java"]
    names += [p.relative_to(ROOT).as_posix() for p in sorted((ROOT / "algorithms/python/atcg").glob("*.py"))]
    return {n: sha(ROOT / n) for n in names}


class StageError(RuntimeError):
    def __init__(self, message, state="INFRA_ERROR"):
        super().__init__(message)
        self.state = state


def stage(command, folder, timeout, products=()):
    """Reuse a completed command only with the same intent and retained bytes."""
    command = [str(x) for x in command]
    checkpoint = folder / "checkpoint.json"
    intent = {"command": command, "cwd": str(ROOT), "timeout": timeout}
    if folder.exists():
        if not checkpoint.exists():
            raise StageError(f"Interrupted stage preserved: {folder}; reconcile before recovery")
        saved = read(checkpoint)
        if saved["intent"] != intent or any(not Path(n).is_file() or sha(n) != h
                                            for n, h in saved["artifacts"].items()):
            raise StageError(f"Stage binding/evidence changed: {folder}")
        result = saved["result"]
    else:
        print(f"  stage {folder.name}", flush=True)
        result = run_command(command, ROOT, folder, timeout)
        paths = [folder / "command.json", folder / "command.log", *products]
        write(checkpoint, {"intent": intent, "result": result,
                           "artifacts": {str(p): sha(p) for p in paths if Path(p).is_file()}})
    if result["timed_out"] or result["exit_code"] != 0:
        raise StageError(f"Command failed; see {folder / 'command.log'}",
                         "TIMEOUT" if result["timed_out"] else "INFRA_ERROR")
    if any(not Path(p).is_file() for p in products):
        raise StageError(f"Command output missing: {folder}")
    return (folder / "command.log").read_text(encoding="utf-8", errors="replace")


def init(output, d4j, worktrees, timeout):
    output.mkdir(parents=True, exist_ok=False)
    inventory = output / "Experiment/inventory"
    projects = stage([d4j, "pids"], inventory / "projects", 120).strip().splitlines()
    expected = read(ROOT / "experiments/configs/api854-20261003/installed-active-bugs.json")["projects"]
    if set(projects) != set(expected) or len(projects) != len(set(projects)):
        raise ValueError("Installed project inventory differs from the planned 17 projects")
    actual = {}
    for project in sorted(projects):
        if not re.fullmatch(r"[A-Za-z]+", project):
            raise ValueError("Unsafe project name")
        bugs = [int(n) for n in stage([d4j, "bids", "-p", project], inventory / project, 120).splitlines()]
        if len(bugs) != len(set(bugs)) or sorted(bugs) != sorted(expected[project]):
            raise ValueError(f"Installed active bugs differ: {project}")
        actual[project] = sorted(bugs)
    environment = output / "Experiment/environment"
    environment.mkdir(parents=True)
    versions = runtime_versions(str(d4j), environment, timeout)
    if versions["defects4j"] != "3.0.1" or not re.search(r'version "11\.', versions["java"] or ""):
        raise ValueError("Defects4J 3.0.1 and Java 11 required")
    write(environment / "versions.json", versions)
    for name in ("cases.csv", "jobs.csv"):
        shutil.copyfile(BLUEPRINT / name, output / "Experiment" / name)
    config = {
        "schema": "solo-offline-pilot.v1", "stage": "OFFLINE_PILOT_AI_NOT_ENABLED",
        "scope_bugs": sum(map(len, actual.values())), "scope_jobs": 4 * sum(map(len, actual.values())),
        "single_operator": True, "peer_approval_required": False, "cpu_slots": 1,
        "d4j": str(d4j), "worktrees_root": str(worktrees), "timezone": "America/Los_Angeles",
        "timeout_seconds": timeout, "observation_timeout_seconds": 10, "seed": 101, "budget": 30,
        "algorithm_fixture_policy": "legacy-recursive-null-v1",
        "algorithm_target_selection": "shared-declaration-signatures-v1",
        "algorithm_oracle": "fixed observations; opaque object type/nullness; limited recursive fixtures",
        "ai_source_character_budget": 12000, "ai_signature_character_budget": 6000,
        "ai_context": "buggy-only; sorted modified classes, equal source shares; no fixed source or patch",
        "ai_models": {"kku-claude": "claude-sonnet-5", "kku-gemini": "gemini-3.5-flash-lite"},
        "ai_enabled": False, "old_results_pooled": False, "old_gate_or_queue_mutated": False,
        "source_sha256": pins(), "inventory": actual, "runtime": versions,
    }
    for name in config["source_sha256"]:
        destination = output / "Experiment/automation/runtime" / name
        destination.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(ROOT / name, destination)
    write(output / "Experiment/protocol/offline.json", config)
    report(output, config)
    return config


def load_config(output):
    config = read(output / "Experiment/protocol/offline.json")
    if config["source_sha256"] != pins():
        raise ValueError("Implementation changed; preserve this cohort and create a new output")
    return config


def file_pins(folder):
    return {p.relative_to(folder).as_posix(): sha(p) for p in sorted(folder.rglob("*"))
            if p.is_file() and p.name != "receipt.json" and not p.name.endswith(".tmp")}


def seal(folder):
    write(folder / "receipt.json", {"files": file_pins(folder)})


def verify(folder):
    if read(folder / "receipt.json")["files"] != file_pins(folder):
        raise StageError(f"Sealed evidence changed: {folder}")


def compact_sources(sources, budget):
    """Equal per-file source allocation, independent of fixed/buggy outcomes."""
    if not sources:
        return []
    share, remainder = divmod(budget, len(sources))
    return [{"class": name, "excerpt": text[:share + (i < remainder)],
             "original_characters": len(text), "truncated": len(text) > share + (i < remainder)}
            for i, (name, text) in enumerate(sorted(sources.items()))]


def prepare_case(output, case, config):
    project, bug_text = case.rsplit("-", 1)
    bug = int(bug_text)
    d4j, timeout = config["d4j"], config["timeout_seconds"]
    folder = output / "Experiment/contexts" / case
    folder.mkdir(parents=True, exist_ok=True)
    if (folder / "prepared.json").exists():
        verify(folder)
        meta = read(folder / "prepared.json")
        for rev, tree in meta["trees"].items():
            validate_worktree(Path(tree), project, f"{bug}{rev}")
        for path, digest in meta["source_sha256"].items():
            if sha(path) != digest:
                raise StageError("Checkout source bytes changed")
        return meta
    trees, properties = {}, {}
    for rev in ("b", "f"):
        tree = Path(config["worktrees_root"]) / output.name / case / rev
        tree.parent.mkdir(parents=True, exist_ok=True)
        checkout = folder / f"checkout-{rev}"
        if not tree.exists() or checkout.exists():
            stage([d4j, "checkout", "-p", project, "-v", f"{bug}{rev}", "-w", tree], checkout, timeout)
        validate_worktree(tree, project, f"{bug}{rev}")
        stage([d4j, "compile", "-w", tree], folder / f"compile-{rev}", timeout)
        trees[rev] = str(tree)
        props = {}
        for prop in ("classes.modified", "cp.test", "dir.src.classes", "dir.bin.classes"):
            path = folder / f"{rev}.{prop}.txt"
            stage([d4j, "export", "-w", tree, "-p", prop, "-o", path],
                  folder / f"export-{rev}-{prop}", timeout, (path,))
            props[prop] = path.read_text(encoding="utf-8").strip()
        properties[rev] = props
    classes = properties["b"]["classes.modified"].splitlines()
    if not classes or classes != properties["f"]["classes.modified"].splitlines():
        raise StageError("Modified-class scope differs between revisions")
    source_hashes, buggy_sources = {}, {}
    for rev in ("b", "f"):
        for name in sorted(classes):
            if not re.fullmatch(r"[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*", name):
                raise StageError("Unsafe class name")
            tree = Path(trees[rev]).resolve()
            path = (tree / properties[rev]["dir.src.classes"] / (name.split("$")[0].replace(".", "/") + ".java")).resolve()
            if not path.is_relative_to(tree) or not path.is_file():
                raise StageError(f"Modified source unavailable: {name}")
            source_hashes[str(path)] = sha(path)
            if rev == "b":
                buggy_sources[name] = path.read_text(encoding="utf-8", errors="replace")
    parts = compact_sources(buggy_sources, config["ai_source_character_budget"])
    signatures = stage(["javap", "-private", "-classpath", properties["b"]["cp.test"], *sorted(classes)],
                       folder / "buggy-signatures", timeout)
    context = {"case": case, "classes_modified": sorted(classes), "source_excerpts": parts,
               "buggy_signatures": signatures[:config["ai_signature_character_budget"]],
               "signatures_truncated": len(signatures) > config["ai_signature_character_budget"],
               "source_character_count": sum(len(p["excerpt"]) for p in parts),
               "context_knowledge": "buggy production source and API metadata only"}
    write(folder / "ai-context.json", context)
    prompt_context = f"Case: {case}\nJava 11, JUnit 4\nModified classes: {', '.join(sorted(classes))}\n\n"
    prompt_context += "BUGGY API signatures (may include private members):\n" + context["buggy_signatures"] + "\n"
    for p in parts:
        prompt_context += f"\nBUGGY source excerpt: {p['class']} (truncated={p['truncated']})\n```java\n{p['excerpt']}\n```\n"
    (folder / "ai-context.txt").write_text(prompt_context, encoding="utf-8")
    meta = {"case": case, "project": project, "bug_id": bug, "trees": trees, "properties": properties,
            "classes_file": str(folder / "f.classes.modified.txt"), "source_sha256": source_hashes,
            "ai_context_sha256": sha(folder / "ai-context.txt")}
    write(folder / "prepared.json", meta)
    seal(folder)
    return meta


def discover(output, case, meta, config):
    folder = output / "Experiment/algorithm-targets" / case
    folder.mkdir(parents=True, exist_ok=True)
    if (folder / "targets.json").exists():
        verify(folder)
        return read(folder / "targets.json")["targets"], str(folder / "probe-classes")
    helper = folder / "probe-classes"
    helper.mkdir(exist_ok=True)
    stage(["javac", "-source", "7", "-target", "7", "-d", helper, ROOT / "algorithms/java/SqaProbe.java"],
          folder / "compile-probe", config["timeout_seconds"], (helper / "SqaProbe.class",))
    sets = []
    for rev in ("b", "f"):
        binary = Path(meta["trees"][rev]) / meta["properties"][rev]["dir.bin.classes"]
        sets.append({str(p.relative_to(binary).with_suffix("")).replace(os.sep, ".")
                     for p in binary.rglob("*.class") if "$" not in p.name})
    fixtures = folder / "fixture-classes.txt"
    fixtures.write_text("\n".join(sorted(sets[0] & sets[1])) + "\n", encoding="utf-8")
    inventories = {}
    for rev in ("b", "f"):
        log = stage(["java", "-Xmx256m", "-cp", meta["properties"][rev]["cp.test"] + os.pathsep + str(helper),
                     "SqaProbe", "discover", "--fixtures", fixtures,
                     *meta["properties"][rev]["classes.modified"].splitlines()],
                    folder / f"discover-{rev}", config["timeout_seconds"])
        lines = [line for line in log.splitlines() if line.startswith('{"targets":')]
        if len(lines) != 1:
            raise StageError("Discovery did not produce a single target inventory")
        inventories[rev] = json.loads(lines[0])
        write(folder / f"targets-{rev}.json", inventories[rev])
    targets, excluded = shared_targets(inventories["f"]["targets"], inventories["b"]["targets"])
    write(folder / "targets.json", {"targets": targets, "excluded": excluded,
                                   "errors": {rev: inv["errors"] for rev, inv in inventories.items()}})
    seal(folder)
    return targets, str(helper)


def result_path(output, case, method):
    return output / "Experiment/evaluations" / case / method / "run-final"


def normalize(result):
    fixed = result.get("fixed_validation")
    if result["status"] == "complete":
        state = "DONE"
    elif result["status"] == "invalid" or result.get("compile_status") == "failed":
        state = "INVALID_GENERATED_SUITE"
    elif any(s.get("timed_out") for s in result.get("stages", {}).values() if isinstance(s, dict)):
        state = "TIMEOUT"
    else:
        state = "INFRA_ERROR"
    return {"state": state, "evaluated": "fixed-1" in result.get("stages", {}),
            "valid_on_fixed": True if fixed == "passed_twice" else (False if state == "INVALID_GENERATED_SUITE" else None),
            "fault_detected": result.get("fault_detected"),
            "executed_tests": None, "generated_test_methods": result.get("test_count"),
            **{k: result.get(k) for k in ("line_covered", "line_total", "branch_covered", "branch_total", "generation_seconds")},
            "evaluation_seconds": result.get("duration_seconds"), "error": result.get("error")}


def run_algorithms(output, cases, config, methods):
    protocol_hash = sha(output / "Experiment/protocol/offline.json")
    for case in cases:
        project, bug = case.rsplit("-", 1)
        if project not in config["inventory"] or int(bug) not in config["inventory"][project]:
            raise ValueError(f"Case outside active inventory: {case}")
    with cpu_slot(config["worktrees_root"]):
        for case in cases:
            pending = []
            for method in methods:
                folder = result_path(output, case, method)
                if (folder / "outcome.json").exists():
                    verify(folder)
                    print(f"{case} {method}: retained {read(folder / 'outcome.json')['state']}", flush=True)
                else:
                    pending.append(method)
            if not pending:
                continue
            started = time.monotonic()
            setup_error = None
            try:
                meta = prepare_case(output, case, config)
                targets, helper = discover(output, case, meta, config)
            except Exception as error:
                setup_error = error
            for method in pending:
                print(f"{case} {method}: starting", flush=True)
                folder = result_path(output, case, method)
                folder.mkdir(parents=True, exist_ok=True)
                project, bug = case.rsplit("-", 1)
                outcome = {"case": case, "project": project, "bug_id": int(bug), "method": method,
                           "attempted": True, "provider_requested": False, "evaluated": False,
                           "protocol_hash": protocol_hash, "review": "single-operator automated checks/self-review"}
                try:
                    if setup_error:
                        raise setup_error
                    if not targets:
                        raise StageError("No shared signatures supported by the bounded generic adapter", "UNSUPPORTED")
                    generation = output / METHODS[method] / "Result_Round1" / case / "run-final"
                    if (generation / "generation.json").exists():
                        verify(generation)
                        generated = read(generation / "generation.json")
                    elif generation.exists():
                        raise StageError("Interrupted generation preserved; no automatic regeneration")
                    else:
                        generated = generate_suite(project, int(bug), method, config["budget"], config["seed"], targets,
                                                   meta["properties"]["f"]["cp.test"] + os.pathsep + helper, generation,
                                                   config["observation_timeout_seconds"])
                        seal(generation)
                    if not generated["test_count"]:
                        raise StageError("No stable fixed-reference observations; see raw ledger", "UNSUPPORTED")
                    test = output / METHODS[method] / "Test" / case / "run-final"
                    test.mkdir(parents=True, exist_ok=True)
                    for name in ("GeneratedStudyTest.java", "SqaProbe.java"):
                        shutil.copyfile(generation / name, test / name)
                    measurement = folder / "measurement"
                    if (measurement / "receipt.json").exists():
                        verify(measurement)
                        result = read(measurement / "record.json")
                    elif measurement.exists():
                        raise StageError("Interrupted evaluation preserved; reconcile before recovery")
                    else:
                        result = evaluate_run(EvaluationConfig(project, int(bug), method, config["seed"], config["budget"],
                            Path(generated["suite"]), Path(meta["trees"]["b"]), Path(meta["trees"]["f"]), measurement,
                            config["d4j"], Path(meta["classes_file"]), generated["generation_seconds"],
                            generated["test_count"], config["timeout_seconds"]))
                        seal(measurement)
                    outcome.update(normalize(result))
                    outcome["generation_path"] = generation.relative_to(output).as_posix()
                    outcome["suite_sha256"] = generated["suite_sha256"]
                except Exception as error:
                    outcome.update(state=getattr(error, "state", "INFRA_ERROR"), error=f"{type(error).__name__}: {error}")
                outcome["elapsed_including_shared_setup_seconds"] = time.monotonic() - started
                write(folder / "outcome.json", outcome)
                seal(folder)
                report(output, config)
                print(f"{case} {method}: {outcome['state']}, fault={outcome.get('fault_detected')}", flush=True)


def report(output, config):
    rows = []
    protocol_hash = sha(output / "Experiment/protocol/offline.json")
    for project, bugs in config["inventory"].items():
        for bug in bugs:
            case = f"{project}-{bug}"
            for method in METHODS:
                folder = result_path(output, case, method)
                path = folder / "outcome.json"
                record = read(path) if path.exists() else {"state": "PENDING", "attempted": False,
                                                          "provider_requested": False, "evaluated": False}
                if path.exists():
                    verify(folder)
                rows.append({**{k: record.get(k) for k in FIELDS}, "case": case, "project": project,
                             "bug_id": bug, "method": method, "protocol_hash": protocol_hash,
                             "result_path": folder.relative_to(output).as_posix() if path.exists() else None})
    folder = output / "Report/data"
    folder.mkdir(parents=True, exist_ok=True)
    with (folder / "final_comparison.csv").open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=FIELDS)
        writer.writeheader()
        writer.writerows(rows)
    stats = {"planned_bugs": config["scope_bugs"], "planned_jobs": len(rows),
             "attempted_jobs": sum(r["attempted"] is True for r in rows),
             "provider_requested_jobs": sum(r["provider_requested"] is True for r in rows),
             "evaluated_jobs": sum(r["evaluated"] is True for r in rows),
             "states": dict(Counter(r["state"] for r in rows)), "new_api_requests": 0,
             "condition": config["schema"], "phase": "offline algorithms/context; AI workflow pending",
             "per_method": {method: dict(Counter(r["state"] for r in rows if r["method"] == method)) for method in METHODS}}
    write(folder / "summary.json", stats)
    (output / "Report/benchmark_status.md").write_text(
        "# Full-scope experiment status\n\nOffline pilot only; no new KKU requests.\n\n"
        + "```json\n" + json.dumps(stats, indent=2) + "\n```\n\n"
        + "Missing metrics are empty/null, not zero. Historical suites are not pooled. "
        + "Fixed observations are used by algorithms; AI context is buggy-only. "
        + "Generated method count is not a verified executed-test count.\n", encoding="utf-8")
    return stats


def main():
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("action", choices=("init", "run", "report"))
    cli.add_argument("--output", type=Path, required=True)
    cli.add_argument("--d4j", type=Path, default=Path("/home/team/sqa-round2/defects4j/framework/bin/defects4j"))
    cli.add_argument("--worktrees", type=Path, default=Path("/home/team/sqa-round2/worktrees"))
    cli.add_argument("--timeout", type=int, default=300)
    cli.add_argument("--cases", nargs="+", default=["Csv-1"])
    cli.add_argument("--methods", nargs="+", choices=("cmaes", "fscs-art"), default=["cmaes", "fscs-art"])
    args = cli.parse_args()
    if sys.platform != "linux":
        cli.error("Use WSL/Linux with Java 11")
    output = args.output.resolve()
    if not re.fullmatch(r"[A-Za-z0-9][A-Za-z0-9_-]{0,80}", output.name) or args.timeout < 1:
        cli.error("Unsafe cohort name or timeout")
    if args.action == "init":
        config = init(output, args.d4j.resolve(), args.worktrees.resolve(), args.timeout)
    else:
        config = load_config(output)
    if args.action == "run":
        run_algorithms(output, args.cases, config, args.methods)
    print(json.dumps(report(output, config), indent=2), flush=True)


if __name__ == "__main__":
    main()
