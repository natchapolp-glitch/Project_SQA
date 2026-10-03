"""Review pinned Aom Csv receipts without rerunning tests or mutating peer evidence."""
import argparse,csv,hashlib,json,re,subprocess
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3];PREFIX=ROOT/'output/api854-20261004'
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def read(path):return json.loads(Path(path).read_bytes())
def require(ok,message):
    if not ok:raise ValueError(message)
def write(path,value):Path(path).write_text(json.dumps(value,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
def run(output):
    output=output.resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'Fresh contained evidence output required')
    intake=PREFIX/'beam-aom-csv-review-intake-v2';receiver=read(intake/'receipt.json')
    require(receiver['source_commit']=='4334c2ab908f517f99c7046c08f45a25c0b6a64d','Wrong Aom commit')
    for name,digest in read(intake/'checksums.json').items():require(sha(intake/name)==digest,'Aom receipt intake changed')
    peer=intake/'received-champ';base=peer/'output/api854-20261004';strict=base/'aom-ready-messages-d4j-v1';valid=base/'aom-ready-messages-d4j-v2'
    plan=read(valid/'preexecution-plan.json');receipt=read(valid/'receipt.json');failure=read(strict/'failed-attempt.json')
    beam=PREFIX/'beam-champ-csv-messages-d4j-v3';beam_seal=read(beam/'preexecution-seal.json')
    require(plan['baseline_commit']=='63ad195623c2ed3f67f3ae232c00c54d3160ce72' and plan['runtime_source_sha256']==beam_seal['frozen_runtime_source_sha256'],'V12 runtime source hashes differ')
    require(plan['host']=='aom-pc1' and plan['cpu_slots']==1 and plan['java_major']==11 and plan['timezone']=='America/Los_Angeles','Host binding differs')
    require(receipt['condition']==plan['condition'] and receipt['generation_condition']==beam_seal['generation_condition'],'Condition binding differs')
    require(failure['status']=='strict_source_preflight_failed_before_tests' and failure['no_test_outcomes_from_this_attempt'],'Strict source failure was not retained')
    override=plan['buggy_source_override'];require(sha(strict/'d4j-reconstructed-buggy.java')==override['sha256']==failure['benchmark_buggy_sha256'],'Benchmark source SHA differs')
    require(sha(strict/'native-upstream-buggy.java')==failure['native_buggy_sha256'] and failure['native_buggy_sha256']!=override['sha256'],'Native mismatch evidence differs')
    beam_ref=beam/'isolated-bug-reference/expected-buggy'/override['path']
    require(sha(beam_ref)==override['sha256'],'Aom benchmark differs from independently derived Beam official-patch reference')
    require(receipt['runtime_pins_unchanged'] and receipt['api_requests']==receipt['queue_mutations']==receipt['primary_results']==0 and not receipt['gate_a_approved'],'Unexpected request/mutation/promotion')
    output.mkdir();(output/'producer.py').write_bytes(Path(__file__).read_bytes());rows=[]
    beam_rows={r['approach']:r for r in read(beam/'results.json')}
    expected={'cmaes':(30,0,31,37,13,26),'fscs-art':(30,0,31,37,13,26),'kku-claude':(21,1,36,37,22,26),'kku-gemini':(18,1,37,37,23,26)}
    for entry in receipt['observations']:
        approach=entry['approach'];folder=valid/approach/'measurement';record=read(folder/'record.json');number,failures,*coverage=expected[approach]
        require(sha(folder/'record.json')==entry['record_sha256'],'Aom record hash differs')
        require(record['status']=='complete' and record['fixed_validation']=='passed_twice' and record['test_count']==number,'Incomplete fixed-valid outcome')
        suite=folder/f'Csv-1f-{approach}.101.tar.bz2'
        require(sha(suite)==entry['suite_sha256']==record['suite_sha256']==beam_rows[approach]['suite_sha256'],'Aom/Beam suite archive differs')
        version=record['versions'];require(version['defects4j']=='3.0.1' and version['defects4j_commit']=='6d54320e0db5a357f9ab38a8e4d2e5aead7e1c09' and 'version "11.' in version['java'],'Environment evidence differs')
        metrics=[record[k] for k in ['line_covered','line_total','branch_covered','branch_total']];require(metrics==coverage,'Coverage record differs')
        summary=next(csv.DictReader((folder/'coverage/summary.csv').open()))
        require([int(summary[k]) for k in ['LinesCovered','LinesTotal','ConditionsCovered','ConditionsTotal']]==coverage,'Actual coverage summary differs')
        require(record['fault_detected']==entry['fault_detected']==beam_rows[approach]['fault_detected']==bool(failures),'Fault verdict differs')
        stages={}
        for stage in ['fixed-1','fixed-2','buggy','coverage']:
            evidence=folder/stage;command=read(evidence/'command.json');require(command['exit_code']==0 and not command['timed_out'],'Host stage command failed')
            command_text=' '.join(command['command']);require('/home/team/sqa-round2/' in command_text and ('/b' if stage=='buggy' else '/f') in command_text,'Host worktree command differs')
            all_tests=[line for line in (evidence/'all_tests').read_text().splitlines() if line.strip()];require(len(all_tests)==len(set(all_tests))==number,'Actual test-start event count differs')
            failing=(evidence/'failing_tests').read_text();seen=len(re.findall(r'^--- ',failing,re.M));require(seen==(failures if stage=='buggy' else 0),'Raw failing tests differ')
            counter=evidence/'sqa-stage-counts.json'
            if approach.startswith('kku-'):
                require(not counter.exists(),'Unexpected AI aggregate counter')
                skipped=checks=None;source='Actual Defects4J all_tests test-start events; no aggregate skip or target counter'
            else:
                counts=read(counter);require((counts['executed'],counts['skipped'],counts['target_checks'])==(30,0,30),'Algorithm counter differs')
                skipped=counts['skipped'];checks=counts['target_checks'];source='Actual Defects4J test-start events and unchanged embedded suite counter'
            stages[stage]={'test_start_events':len(all_tests),'skipped':skipped,'target_checks':checks,'failed':seen,'source':source,'command_sha256':sha(evidence/'command.json'),'all_tests_sha256':sha(evidence/'all_tests'),'failing_tests_sha256':sha(evidence/'failing_tests')}
        if approach.startswith('kku-'):
            failure_name={'kku-claude':'lineNumberDoesNotDoubleCountCRLF','kku-gemini':'testCarriageReturnLineNumber'}[approach]
            require(failure_name in (folder/'buggy/failing_tests').read_text(),'Expected CR failure missing')
        rows.append({'approach':approach,'status':'received_cross_host_evidence_consistent','tests':number,'fault_detected':bool(failures),'coverage':metrics,'actual_stage_evidence':stages,'aom_record_sha256':entry['record_sha256'],'suite_sha256':entry['suite_sha256'],'environment_sha256':sha(folder/'java-version/command.log'),'evaluator_seconds':record['duration_seconds']})
    require(len(rows)==4,'Four distinct Aom outcomes required')
    write(output/'observations.json',rows)
    result={'status':'aom_csv_v12_receipts_reviewed_without_rerun','checked_at_utc':datetime.now(timezone.utc).isoformat(),'aom_commit':receiver['source_commit'],'intake_receipt_sha256':sha(intake/'receipt.json'),'verified_received_files':len(receiver['source_sha256']),'v12_runtime_pins_verified':len(plan['runtime_source_sha256']),'host':'aom-pc1','host_report_matches_declared':True,'beam_host_not_substituted':True,'strict_attempt_retained':True,'benchmark_buggy_sha256':override['sha256'],'native_buggy_sha256':failure['native_buggy_sha256'],'benchmark_matches_beam_official_patch_reference':True,'same_unchanged_archives_as_beam':True,'completed_outcomes':4,'ai_aggregate_skip_and_target_counters':None,'observations_sha256':sha(output/'observations.json'),'cpu_test_runs_started':0,'kku_requests':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'v13_results_mixed':False,'review_limits':['Read-only review of peer evidence, not physical access to Aom host or an independent rerun.','Host alias/source hashes are checked from sealed plan/raw environment; no new attestation of all live Aom files.','AI test-start events are observed; absent aggregate skip/target counters stay null.','Matching host replay is not a new bug or independent generation repeat.']}
    write(output/'receipt.json',result);write(output/'checksums.json',{p.relative_to(output).as_posix():sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':result['status'],'completed_outcomes':4,'benchmark_matches_beam_official_patch_reference':True,'cpu_test_runs_started':0}))
if __name__=='__main__':
    parser=argparse.ArgumentParser();parser.add_argument('--output',type=Path,required=True);args=parser.parse_args();run(args.output)
