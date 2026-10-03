"""Replay identical Cli suites with a scoped JUnit/Hamcrest framework repair."""
from __future__ import annotations
import json
from pathlib import Path
import sys
import zipfile

from aom_ready_csv import ROOT, DAY, load, save, digest, seal, verify_manifest
from cli_unordered_oracle import OUTPUT, RUNTIME, CONDITION
from aom_cli_unordered_measure import MEASURED

REPLAY = ROOT / DAY / "aom-cli-unordered-d4j-v2"


def main():
    if sys.platform != "linux":
        raise ValueError("WSL Aom host required")
    verify_manifest(MEASURED)
    verify_manifest(OUTPUT)
    sys.path.insert(0,str(RUNTIME / "scripts/study"))
    from api854.common import cpu_slot, implementation_hashes
    from api854.worker import prepare_evaluation
    from evaluate import EvaluationConfig, evaluate_run
    # Same exact dependency substitution documented by repair_cli_framework_v5.
    OLD = b'file://${d4j.home}/framework/projects/lib/junit-4.12.jar'
    NEW = b'file://${d4j.home}/framework/projects/lib/junit-4.12-hamcrest-1.3.jar'
    expected = load(OUTPUT / "receipt.json")["runtime_source_sha256"]
    if implementation_hashes()!=expected:
        raise ValueError("Frozen new runtime differs")
    REPLAY.mkdir(exist_ok=False)
    home = Path("/home/team/sqa-round2/defects4j")
    d4j = str(home / "framework/bin/defects4j")
    lock = Path("/home/team/sqa-round2/worktrees")
    trees = lock / REPLAY.name
    build = home / "framework/projects/Cli/Cli.build.xml"
    jar = home / "framework/projects/lib/junit-4.12-hamcrest-1.3.jar"
    with zipfile.ZipFile(jar) as archive:
        if "org/hamcrest/SelfDescribing.class" not in archive.namelist():
            raise ValueError("Pinned replacement dependency lacks required class")
    save(REPLAY / "preexecution-plan.json",{"condition":CONDITION+"-d4j-java11-la-v2-hamcrest",
        "generation_condition":CONDITION,"generation_repeated":False,
        "unchanged_suite_manifest_sha256":digest((MEASURED / "checksums.json").read_bytes()),
        "prepared_manifest_sha256":digest((OUTPUT / "checksums.json").read_bytes()),
        "framework_change":"JUnit dependency URL only, same fixed/buggy; restore in finally under CPU lock",
        "cpu_lock_root":str(lock),"host":"aom-pc1","cpu_slots":1,
        "producer_sha256":digest(Path(__file__).read_bytes()),"primary_results":0,
        "api_requests":0,"queue_mutations":0,"gate_a_approved":False})
    records=[]
    with cpu_slot(lock):
        before=build.read_bytes()
        if before.count(OLD)!=1 or before.count(NEW)!=0:
            raise ValueError("Refuse unreviewed or already modified Cli framework")
        after=before.replace(OLD,NEW,1)
        (REPLAY / "Cli.build.original.xml").write_bytes(before)
        (REPLAY / "Cli.build.scoped.xml").write_bytes(after)
        build.write_bytes(after)
        try:
            trees.mkdir(exist_ok=False)
            paths, classes, fixed = prepare_evaluation(d4j,{"project":"Cli","bug_id":1},trees,REPLAY / "checkout",300)
            binding=load(MEASURED / "production-source-binding.json")
            for rev,tree in paths.items():
                for name,value in binding["actual"][rev].items():
                    if digest((tree/name).read_bytes())!=value:
                        raise ValueError("Production bytes differ from v1; no replay")
            for algorithm in ("cmaes","fscs-art"):
                generation=load(MEASURED / algorithm / "generation/generation.json")
                archive=Path(generation["suite"])
                if digest(archive.read_bytes())!=generation["suite_sha256"]:
                    raise ValueError("Original suite bytes changed")
                result=evaluate_run(EvaluationConfig(project="Cli",bug_id=1,generator=algorithm,seed=101,budget=30,
                    suite=archive,buggy_worktree=paths["b"],fixed_worktree=paths["f"],output=REPLAY/algorithm,
                    d4j=d4j,classes_file=classes,test_count=30,timeout_seconds=300))
                records.append({"approach":algorithm,"status":result["status"],"fault_detected":result["fault_detected"],
                    "record_path":(REPLAY / algorithm / "record.json").relative_to(ROOT).as_posix(),
                    "record_sha256":digest((REPLAY / algorithm / "record.json").read_bytes()),
                    "suite_sha256":result.get("suite_sha256"),"same_v1_suite":result.get("suite_sha256")==generation["suite_sha256"]})
                print(json.dumps(records[-1]),flush=True)
        finally:
            if build.read_bytes()!=after:
                raise ValueError("Unexpected framework mutation; retained original backup requires review")
            build.write_bytes(before)
            save(REPLAY / "framework-restoration.json",{"before_sha256":digest(before),"scoped_sha256":digest(after),
                "restored_sha256":digest(build.read_bytes()),"restored_exactly":build.read_bytes()==before,
                "junit_hamcrest_jar_sha256":digest(jar.read_bytes()),"production_edits":0,"assertion_edits":0})
        if implementation_hashes()!=expected:
            raise ValueError("Frozen runtime changed")
    save(REPLAY / "receipt.json",{"condition":CONDITION+"-d4j-java11-la-v2-hamcrest","generation_condition":CONDITION,
        "records":records,"host":"aom-pc1","cpu_slots":1,"timezone":"America/Los_Angeles","java_major":11,
        "primary_results":0,"api_requests":0,"queue_mutations":0,"gate_a_approved":False,
        "generation_unchanged":True,"framework_restored_exactly":True,
        "ai_status":{"claude-sonnet-5":"pending_not_generated_in_new_condition","gemini-3.5-flash-lite":"pending_not_generated_in_new_condition"}})
    seal(REPLAY)


if __name__ == "__main__":
    main()
