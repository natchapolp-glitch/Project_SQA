"""Condition-separated ready results, independently checked against raw peer evidence."""
from __future__ import annotations

import csv
import copy
import json
from pathlib import Path
import shutil
import tarfile
import xml.etree.ElementTree as ET

from aom_ready_csv import ROOT, DAY, INTAKE, PAIR, BASE, APPROACHES, digest, load, save, seal, verify_manifest
from aom_ready_messages import RECEIVED
from aom_ready_report import read_started, flatten, write_csv
from aom_ready_peer_report import INTAKE_NEW, REPORT, OLD, PACKETS, CHAMP_JSOUP, BEAM, CHAMP_LATEST


def received_path(relative,expected):
    roots=[INTAKE_NEW/label for label in PACKETS]+[RECEIVED/"peer",INTAKE/"peer"]
    for parent in roots:
        candidate=parent/relative
        if candidate.is_file() and digest(candidate.read_bytes())==expected:
            return candidate
    raise ValueError("Cannot resolve hash-bound original evidence: "+relative)


def junit_counts(folder,declared,expected,algorithm):
    files=sorted((folder / "junit-reports").glob("TEST-*.xml"))
    if not files: raise ValueError("Missing actual JUnit XML")
    identities=[]
    skipped=failed=errors=0
    for path in files:
        suite=ET.parse(path).getroot()
        cases=suite.findall("testcase")
        if suite.tag!="testsuite" or int(suite.get("tests","-1"))!=len(cases):
            raise ValueError("XML declared count does not match cases")
        totals={"skipped":0,"failures":0,"errors":0}
        for case in cases:
            identity=(case.get("classname"),case.get("name"))
            if None in identity or identity in identities:
                raise ValueError("Duplicate or missing JUnit test identity")
            identities.append(identity)
            totals["skipped"]+=len(case.findall("skipped"))
            totals["failures"]+=len(case.findall("failure"))
            totals["errors"]+=len(case.findall("error"))
        for field,value in totals.items():
            if int(suite.get(field,"0"))!=value: raise ValueError("XML summary differs from cases")
        skipped+=totals["skipped"]; failed+=totals["failures"]; errors+=totals["errors"]
    counts={"executed":len(identities)-skipped,"skipped":skipped,"failed":failed,"errors":errors,"target_checks":None}
    if len(identities)!=declared or read_started(folder)!=declared:
        raise ValueError("Declared methods/XML/Defects4J starts disagree")
    counter=folder/"sqa-stage-counts.json"
    if counter.is_file():
        embedded=load(counter)
        for key in ("executed","skipped","target_checks"):
            if type(embedded.get(key)) is not int or not 0<=embedded[key]<=declared:
                raise ValueError("Invalid embedded suite count")
        if (embedded["executed"],embedded["skipped"])!=(counts["executed"],skipped):
            raise ValueError("Suite counter/XML disagree")
        counts["target_checks"]=embedded["target_checks"]
    for key in counts:
        if counts[key]!=expected.get(key): raise ValueError("Peer reported counts disagree with raw XML/counter: "+key)
    if algorithm and (counts["executed"],skipped,counts["target_checks"])!=(declared,0,declared):
        raise ValueError("Algorithm did not check every declared case")
    counts.update(source="Aom independently parsed peer Ant JUnit XML, Formatter starts and unchanged embedded counters",
        xml_reports=[{"path":p.relative_to(ROOT).as_posix(),"sha256":digest(p.read_bytes())} for p in files])
    return counts


def suite_sources(archive):
    result={}
    with tarfile.open(archive) as bundle:
        for member in bundle:
            if not member.isfile() or not member.name.endswith(".java") or member.name.startswith("/") or ".." in Path(member.name).parts:
                raise ValueError("Suite archive contains unexpected entries")
            result[member.name]=digest(bundle.extractfile(member).read())
    return result


def generation_key(row):
    return (row["project"],row["bug_id"],row.get("generation_condition",row["condition"]),row["approach"])


def beam_records():
    baseline=load(INTAKE / "baseline" / PAIR / "protocol.proposal.json")["source_sha256"]
    records=[]
    checks=[]
    for project,packet in (("Csv","beam-champ-csv-messages-d4j-v3"),("Jsoup","beam-champ-jsoup-d4j-v1")):
        root=INTAKE_NEW/"beam"/DAY/packet
        receipt=load(root/"receipt.json")
        preseal=load(root/"preexecution-seal.json")
        if preseal["frozen_runtime_source_sha256"]!=baseline or preseal["worker_id"]!="beam-pc1" or preseal["cpu_slots"]!=1:
            raise ValueError("Peer host/runtime binding mismatch")
        if digest((root/"preexecution-seal.json").read_bytes())!=receipt["preexecution_seal_sha256"]:
            raise ValueError("Peer preexecution seal differs")
        if digest((root/"results.json").read_bytes())!=receipt["results_sha256"]:
            raise ValueError("Peer results receipt differs")
        if not receipt["framework_restored"]["restored_exact_bytes"]:
            raise ValueError("Peer framework not restored")
        framework=root/"count-observer-framework/defects4j.build.original.xml"
        if digest(framework.read_bytes())!=receipt["framework_restored"]["restored_sha256"]:
            raise ValueError("Restored framework hash differs from retained original")
        derivation=root/"isolated-bug-reference/derivation.json"
        derive=load(derivation)
        if not derive["official_patch_git_blob_verified"] or derive["patch_exit_code"]!=0 or derive["actual_production_source_replacement"]:
            raise ValueError("Peer source derivation was not accepted")
        for row in load(root/"results.json"):
            if row["status"]!="complete": continue  # native-only invalid AI stays in native table
            approach=row["approach"]
            record_path=root/approach/"evaluation/record.json"
            raw=load(record_path)
            if digest(record_path.read_bytes())!=row["record_sha256"] or raw["status"]!="complete":
                raise ValueError("Peer raw completed record differs")
            if raw["versions"]["defects4j"]!="3.0.1" or raw["timezone"]!="America/Los_Angeles":
                raise ValueError("Unexpected peer environment")
            archive=root/approach/"suite.tar.bz2"
            if digest(archive.read_bytes())!=row["suite_sha256"] or raw["suite_sha256"]!=row["suite_sha256"]:
                raise ValueError("Peer replay archive differs")
            expected_suite=preseal["suites"][approach]
            if expected_suite["suite_sha256"]!=row["suite_sha256"] or suite_sources(archive)!=expected_suite["source_sha256"]:
                raise ValueError("Peer Java/archive differs from preexecution source pins")
            generation_packet="champ-csv-messages-native-measurement-v1" if project=="Csv" else "champ-jsoup-native-measurement-v2"
            received_suite=received_path(f"{DAY}/{generation_packet}/{approach}/packaged-suite/suite.tar.bz2",row["suite_sha256"])
            coverage=raw["stages"]["coverage"]
            with (record_path.parent/"coverage/summary.csv").open(encoding="utf-8",newline="") as f:
                summary=list(csv.DictReader(f))
            metrics={"line_covered":sum(int(v["LinesCovered"]) for v in summary),
                "line_total":sum(int(v["LinesTotal"]) for v in summary),
                "branch_covered":sum(int(v["ConditionsCovered"]) for v in summary),
                "branch_total":sum(int(v["ConditionsTotal"]) for v in summary)}
            if metrics!=row["coverage"] or any(raw[k]!=v for k,v in metrics.items()):
                raise ValueError("Raw class coverage differs from peer table")
            counts={}
            for stage in ("fixed-1","fixed-2","buggy","coverage"):
                expected=row.get("counts",row.get("stages"))[stage]
                counts[stage]=junit_counts(record_path.parent/stage,row["test_count"],expected,approach in ("cmaes","fscs-art"))
                if counts[stage]["errors"] or counts[stage]["skipped"]:
                    raise ValueError("Peer completed result has JUnit error/skip")
                if stage!="buggy" and counts[stage]["failed"]:
                    raise ValueError("Fixed/coverage stage did not pass")
                if stage!="coverage" and counts[stage]["failed"]!=raw["stages"][stage]["failure_count"]:
                    raise ValueError("JUnit and evaluator failure counts disagree")
            if raw["fault_detected"]!=(counts["buggy"]["failed"]>0):
                raise ValueError("Fault flag contradicts raw buggy failures")
            records.append({"project":project,"bug_id":1,"approach":approach,"status":"complete",
                "condition":receipt["execution_condition"],"generation_condition":receipt["generation_condition"],
                "evaluation_host":"beam-pc1","baseline_commit":BASE,"declared_tests":row["test_count"],
                "compile_status":raw["compile_status"],"fault_detected":raw["fault_detected"],**metrics,
                "duration_seconds":raw["duration_seconds"],"worker_seconds_including_setup":row.get("worker_seconds_including_setup",row.get("evaluation_seconds")),
                "suite_sha256":row["suite_sha256"],"received_suite_path":received_suite.relative_to(ROOT).as_posix(),
                "record_path":record_path.relative_to(ROOT).as_posix(),"record_sha256":row["record_sha256"],
                "stage_counts":counts,"primary_result":False,"full_defects4j_completed":True,
                "input_domain_equivalence":"not_approved","native_and_d4j_sources_separate":True})
            checks.append({"project":project,"approach":approach,"raw_record_sha256":row["record_sha256"],
                "raw_xml_reports_checked":sum(len(v["xml_reports"]) for v in counts.values()),
                "source_derivation_sha256":digest(derivation.read_bytes()),"unchanged_suite":True,
                "framework_restored_exactly":True,"source_runtime_host_bound":True})
    return records,checks


def main():
    verify_manifest(INTAKE_NEW)
    verify_manifest(OLD)
    for label,(_,packets,_) in PACKETS.items():
        for packet in packets: verify_manifest(INTAKE_NEW/label/DAY/packet)
    REPORT.mkdir(exist_ok=False)
    historical=load(OLD/"full-d4j-results.json")
    new,checks=beam_records()
    all_rows=historical["records"]+new
    native_a4=load(INTAKE_NEW/"champ-jsoup"/DAY/"champ-ready-results-audit-v2/results.json")["records"]
    native=load(INTAKE_NEW/"champ-latest"/DAY/"champ-ready-progress-audit-v1/native-results.json")["records"]
    if native[:len(native_a4)]!=native_a4 or len(native_a4)!=16 or len(native)!=24:
        raise ValueError("New native audit did not retain the original 16 outcomes")
    received=[]
    for row in native:
        path=received_path(row["receipt_path"],row["receipt_sha256"])
        received.append({**row,"original_receipt_path":row["receipt_path"],"receipt_path":path.relative_to(ROOT).as_posix(),
            "baseline_commit":BASE,"execution_family":"native_java17_utc"})
    save(REPORT/"native-results.json",{"records":received,"historical_champ_a4_16_rows_preserved":True,
        "native_unique_bugs":5,"native_condition_outcomes":24,"v12_baseline_commit":BASE})
    write_csv(REPORT/"native-results.csv",received)
    save(REPORT/"full-d4j-results.json",{"records":all_rows,"pending_new_condition":historical["pending_new_condition"],
        "original_report_manifest_sha256":digest((OLD/"checksums.json").read_bytes()),
        "duplicate_host_replays_not_independent_generations":True})
    flat=[]
    for row in all_rows:
        flat.append({"evaluation_host":row.get("evaluation_host","aom-pc1"),**flatten(row)})
    write_csv(REPORT/"full-d4j-results.csv",flat)
    save(REPORT/"peer-acceptance.json",{"checks":checks,"accepted_beam_full_measurements":6,
        "actual_junit_xml_reports_checked":sum(r["raw_xml_reports_checked"] for r in checks),
        "champ_acknowledgement_path":(INTAKE_NEW/"champ-latest"/DAY/"champ-beam-ready-results-review-v5/receipt.json").relative_to(ROOT).as_posix(),
        "review_is_not_a_local_rerun":True})
    latest=[]
    for project in ("Csv","Cli","Jsoup","Compress","Gson"):
        for approach in APPROACHES:
            chosen=next((r for r in new if r["project"]==project and r["approach"]==approach),None)
            if project=="Cli":
                candidates=load(OLD/"cli-latest-four-methods.json")["records"]
                chosen=next(r for r in candidates if r["approach"]==approach)
            if chosen:
                item={**flatten(chosen),"evaluation_host":chosen.get("evaluation_host","aom-pc1"),
                    "execution_family":"defects4j_java11_la" if chosen["status"]=="complete" else "pending_new_condition",
                    "full_defects4j_completed":chosen["status"]=="complete","baseline_commit":BASE}
            else:
                source=next(r for r in received if r["project"]==project and r["approach"]==approach)
                complete=source["raw_status"]=="native_fixed_twice_buggy_coverage_measured"
                item={**flatten({}),"project":project,"bug_id":1,"approach":approach,"condition":source["condition"],
                    "status":source["raw_status"],"declared_tests":source["test_count"],"primary_result":False,
                    "evaluation_host":"champ-pc1","execution_family":"native_java17_utc",
                    "full_defects4j_completed":False,"baseline_commit":BASE,
                    "fault_detected":source["raw_native_fault_flag"],"line_covered":source["target_class_covered_lines"],
                    "line_total":source["target_class_instrumented_lines"],"record_path":source["receipt_path"],
                    "record_sha256":source["receipt_sha256"],"prompt_tokens":source["prompt_tokens"],
                    "completion_tokens":source["completion_tokens"],"total_tokens":source["provider_total_tokens"],
                    "next":"pending_full_defects4j_from_beam" if complete else "invalid_outcome_retained_no_buggy_or_coverage"}
            latest.append(item)
    save(REPORT/"latest-four-methods.json",{"records":latest,"select_one_current_condition_per_bug":True,
        "native_and_full_d4j_not_pooled":True})
    write_csv(REPORT/"latest-four-methods.csv",latest)
    with (OLD/"cohort20-worklist.csv").open(encoding="utf-8",newline="") as f: work=list(csv.DictReader(f))
    for row in work:
        item=next((r for r in latest if r["project"]==row["project"] and r["approach"]==row["approach"]),None)
        if item:
            row.update(latest_received_status=item["status"],condition=item["condition"],
                evaluation_host=item["evaluation_host"],full_d4j_evidence=item["record_path"] if item["full_defects4j_completed"] else "",
                limitation=item.get("next","conditions/hosts separate; full domain equivalence not approved; not primary"))
    write_csv(REPORT/"cohort20-worklist.csv",work)
    shutil.copyfile(OLD/"cli-quarantine.json",REPORT/"cli-quarantine.json")
    save(REPORT/"condition-catalog.json",{"v12":{"baseline_commit":BASE,"native_condition_outcomes":24,
        "native_unique_bugs":5,"scoped_cli_oracle_separate_from_shared_v12":True},
        "v13":{"status":"parked_candidate_history","measured_method_outcomes_in_this_report":0,
            "handoff":"docs/api854/AOM_CODEC_V13_HANDOFF_TH.md","included_in_v12_results":False},
        "host_selection":"Beam Csv/Jsoup counted receipts selected for overview; Aom Csv replays retained as separate host rows; Cli new condition retained",
        "native_invalid_not_d4j_measurements":True,"class_coverage_not_whole_project_or_domain_equivalence":True})
    completed=[r for r in all_rows if r["status"]=="complete"]
    save(REPORT/"receipt.json",{"champ_jsoup_commit":CHAMP_JSOUP,"beam_commit":BEAM,"champ_latest_commit":CHAMP_LATEST,
        "native_condition_outcomes":24,"native_unique_bugs":5,"original_native16_preserved":True,
        "d4j_report_rows":len(all_rows),"completed_host_evaluations":len(completed),
        "completed_unique_generation_method_trials":len({generation_key(r) for r in completed}),
        "unique_bugs_with_full_d4j_results":len({(r["project"],r["bug_id"]) for r in completed}),
        "latest_valid_full_four_method_bugs":["Csv-1"],"latest_cli_new_ai_pending":2,
        "beam_latest_completed_replays_received":6,"aom_new_cpu_evaluations":0,"actual_xml_reports_reviewed":24,
        "invalids_missing_metrics_and_cli_quarantine_preserved":True,"historical_v3_rows_unchanged":True,
        "v13_method_results_mixed":False,"api_requests":0,"queue_mutations":0,"primary_results":0,
        "gate_a_approved":False,"final_reserve":None,"producer_sha256":digest(Path(__file__).read_bytes())})
    seal(REPORT)
    print(json.dumps(load(REPORT/"receipt.json")))


if __name__=="__main__": main()
