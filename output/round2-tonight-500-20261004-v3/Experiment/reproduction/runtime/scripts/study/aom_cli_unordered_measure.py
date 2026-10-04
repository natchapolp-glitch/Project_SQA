"""Fresh scoped Cli trials and real Java oracle regression; never reuse old suites."""
from __future__ import annotations

import argparse
import json
import os
from pathlib import Path
import sys
import time

from aom_ready_csv import ROOT, DAY, INTAKE, BASE, digest, load, save, seal, verify_manifest
from aom_ready_messages import RECEIVED
from cli_unordered_oracle import OUTPUT, RUNTIME, POLICY, CONDITION

MEASURED = ROOT / DAY / "aom-cli-unordered-d4j-v1"


def measure():
    if sys.platform != "linux":
        raise ValueError("Run under WSL Java11 on Aom host")
    verify_manifest(OUTPUT)
    verify_manifest(RECEIVED)
    sys.path.insert(0, str(RUNTIME / "scripts/study"))
    from run import stage
    from generate import observe, generate_suite
    from evaluate import EvaluationConfig, evaluate_run
    from api854.worker import prepare_evaluation
    from api854.common import cpu_slot, implementation_hashes
    expected = load(OUTPUT / "receipt.json")["runtime_source_sha256"]
    if implementation_hashes() != expected:
        raise ValueError("New frozen runtime differs")
    MEASURED.mkdir(exist_ok=False)
    d4j = "/home/team/sqa-round2/defects4j/framework/bin/defects4j"
    lockroot = Path("/home/team/sqa-round2/worktrees")
    trees = lockroot / MEASURED.name
    source = "src/java/org/apache/commons/cli/CommandLine.java"
    original = RECEIVED / "peer" / DAY / "champ-cli-native-measurement-v1"
    targets = load(original / "algorithm-targets.json")["targets"]
    prepared_targets = load(OUTPUT / "prepared/Cli-1/targets.json")["targets"]
    if [{k:v for k,v in t.items() if k != "dimensions"} for t in targets] != prepared_targets:
        raise ValueError("Algorithm/AI target sets differ")
    preseal = load(original / "preexecution-seal.json")
    save(MEASURED / "preexecution-plan.json", {"condition": CONDITION + "-d4j-java11-la-v1",
        "generation_condition": CONDITION, "baseline_commit": BASE, "host": "aom-pc1", "cpu_slots": 1,
        "cpu_lock_root": str(lockroot), "fresh_worktrees": str(trees), "java_major": 11,
        "timezone": "America/Los_Angeles", "seed":101, "budget":30, "target_declarations":16,
        "prepared_manifest_sha256":digest((OUTPUT / "checksums.json").read_bytes()),
        "runtime_source_sha256":expected, "producer_sha256":digest(Path(__file__).read_bytes()),
        "reference_cases_not_scientific_suite":32, "primary_results":0, "gate_a_approved":False,
        "api_requests":0,"queue_mutations":0, "source_rule":"actual Defects4J sources; no production edit; fixed context must match before generation"})
    records = []
    with cpu_slot(lockroot):
        trees.mkdir(exist_ok=False)
        stage(["java","-version"], ROOT, MEASURED / "java-version", 30)
        paths, classes_file, fixed = prepare_evaluation(d4j,{"project":"Cli","bug_id":1},trees,MEASURED / "checkout",300)
        if fixed.get(source) != preseal["production_source_sha256"]["fixed"]["sources"][source]:
            raise ValueError("Fixed production context differs; no generation")
        actual_sources = {rev: {name:digest((tree/name).read_bytes()) for name in preseal["production_source_sha256"]["fixed"]["sources"]} for rev,tree in paths.items()}
        save(MEASURED / "production-source-binding.json", {"actual":actual_sources,
            "native":preseal["production_source_sha256"], "actual_benchmark_used":True,
            "native_buggy_differences":[name for name,value in actual_sources["b"].items() if value != preseal["production_source_sha256"]["buggy"]["sources"][name]],
            "production_edits":0, "source_scope":"new D4J condition; old native pair retained"})
        deps = {}
        for rev,tree in paths.items():
            exported = MEASURED / f"cp-{rev}.txt"
            stage([d4j,"export","-w",tree,"-p","cp.test","-o",exported],ROOT,MEASURED / f"export-cp-{rev}",300)
            deps[rev] = exported.read_text().strip()
        helperclasses = trees / "new-helper-classes"
        helperclasses.mkdir()
        baseclasses = trees / "base-helper-classes"
        baseclasses.mkdir()
        harness = ROOT / "scripts/study/fixtures/CliOracleRegression.java"
        for label, helper, dest in (("new",RUNTIME / "algorithms/java/SqaProbe.java",helperclasses),
                                    ("base",INTAKE / "baseline/algorithms/java/SqaProbe.java",baseclasses)):
            stage(["javac","--release","8","-d",dest,helper,harness],ROOT,MEASURED / f"compile-{label}-helper",90)
            stage(["java","-cp",str(dest)+":"+deps["f"],"CliOracleRegression", "derived" if label=="new" else "base"],
                  ROOT,MEASURED / f"controls-{label}",30)
        classpaths = {rev:str(helperclasses)+":"+cp for rev,cp in deps.items()}
        old_policy = "aom-beam-champ-graphics-fixtures-v12-development"
        reference = []
        for target in targets:
            for sign in (-0.25,0.25):
                vector = [sign] * target["dimensions"]
                new = [observe(classpaths["f"],target,vector,10,POLICY) for _ in range(2)]
                base = observe(str(baseclasses)+":"+deps["f"],target,vector,10,old_policy)
                unchanged = observe(classpaths["f"],target,vector,10,old_policy)
                if any(r.get("status")!="ok" or not r.get("target_invoked") for r in new) or new[0]["outcome"] != new[1]["outcome"]:
                    raise ValueError("New reference case unstable or not invoked")
                if base != unchanged:
                    raise ValueError("Old policy behaviour changed in derived runtime")
                reference.append({"target":target,"vector":vector,"fixed_observations":new,
                                  "legacy_base":base,"legacy_derived":unchanged})
        save(MEASURED / "reference-cases.json",reference)
        diagnosis = load(RECEIVED / "peer" / DAY / "champ-cli-order-oracle-audit-v2/preexecution-plan.json")
        trigger = next(t for t in targets if t["method"]=="addOption")
        diagnostic = {"purpose":"post-result regression only; excluded from prompt and generation selection",
            "input_vector":diagnosis["input_vector"],"new":{},"old":{},"accepted_as_scientific_result":False}
        for rev in paths:
            diagnostic["new"][rev]=[observe(classpaths[rev],trigger,diagnosis["input_vector"],10,POLICY) for _ in range(2)]
            diagnostic["old"][rev]=observe(classpaths[rev],trigger,diagnosis["input_vector"],10,old_policy)
        diagnostic["new_fixed_buggy_equal"] = diagnostic["new"]["f"][0].get("outcome") == diagnostic["new"]["b"][0].get("outcome")
        save(MEASURED / "old-vector-diagnostic.json",diagnostic)
        for algorithm in ("cmaes","fscs-art"):
            folder = MEASURED / algorithm
            folder.mkdir()
            generation = generate_suite("Cli",1,algorithm,30,101,targets,classpaths["f"],folder / "generation",fixture_policy=POLICY)
            if generation["test_count"] !=30:
                raise ValueError("Not all proposed cases stable; retain ledger")
            record = evaluate_run(EvaluationConfig(project="Cli",bug_id=1,generator=algorithm,seed=101,budget=30,
                suite=Path(generation["suite"]),buggy_worktree=paths["b"],fixed_worktree=paths["f"],
                output=folder / "measurement",d4j=d4j,classes_file=classes_file,test_count=30,timeout_seconds=300))
            records.append({"approach":algorithm,"status":record["status"],"fault_detected":record["fault_detected"],
                "record_path":(folder / "measurement/record.json").relative_to(ROOT).as_posix(),
                "record_sha256":digest((folder / "measurement/record.json").read_bytes()),
                "generation_sha256":digest((folder / "generation/generation.json").read_bytes()),
                "suite_sha256":record.get("suite_sha256")})
            print(json.dumps(records[-1]),flush=True)
        if implementation_hashes()!=expected:
            raise ValueError("Runtime changed during evaluation")
    save(MEASURED / "receipt.json", {"condition":CONDITION + "-d4j-java11-la-v1","records":records,
        "reference_cases":32,"fixed_reference_observations":64,"legacy_policy_unchanged_cases":32,
        "diagnostic_fixed_buggy_equal":diagnostic["new_fixed_buggy_equal"],
        "ai_status":{"claude-sonnet-5":"pending_not_generated_in_new_condition","gemini-3.5-flash-lite":"pending_not_generated_in_new_condition"},
        "host":"aom-pc1","cpu_slots":1,"prepared_manifest_sha256":digest((OUTPUT / "checksums.json").read_bytes()),
        "primary_results":0,"gate_a_approved":False,"api_requests":0,"queue_mutations":0})
    seal(MEASURED)


if __name__ == "__main__":
    measure()
