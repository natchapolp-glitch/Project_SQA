"""Read-only candidate audit and scoped Beam verdict; preserves sealed native proof."""
from pathlib import Path
from datetime import datetime,timezone
import hashlib,importlib.util,io,json,re,subprocess
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
spec=importlib.util.spec_from_file_location('beam_codec_verifier',BASE/'verify_native.py')
v=importlib.util.module_from_spec(spec);spec.loader.exec_module(v)
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def write(name,data):
    with (BASE/name).open('x',encoding='utf-8',newline='\n') as f:json.dump(data,f,ensure_ascii=False,indent=2);f.write('\n')
def audit():
    provenance=read(BASE/'received-provenance.json')
    specs=[r['commit']+':'+r['source_path'] for r in provenance]
    run=subprocess.run(['git','cat-file','--batch'],cwd=ROOT,input=('\n'.join(specs)+'\n').encode(),capture_output=True,check=True)
    stream=io.BytesIO(run.stdout)
    for r in provenance:
        header=stream.readline().split();assert len(header)==3 and header[1]==b'blob'
        raw=stream.read(int(header[2]));assert stream.read(1)==b'\n'
        assert hashlib.sha256(raw).hexdigest()==r['sha256'] and (ROOT/r['received_path']).read_bytes()==raw
    assert stream.read()==b''
    root=BASE/'received-aom/output/api854-20261003'
    excluded=read(root/'prepare-v10-joint-development/Codec-1/capability-exclusions.json')['excluded']
    candidates=[r['target'] for r in v.POLICY['targets']]
    assert candidates==[r['target'] for r in excluded] and len(candidates)==5
    index=read(root/'prepare-v10-joint-development/index.json')
    assert (index['target_count'],index['capability_exclusion_count'])==(390,301)
    proof=read(v.OUT/'receipt.json');seal=read(v.OUT/'preexecution-seal.json')
    assert proof['status']=='pass' and proof['unique_cases']==43 and proof['declarations']==5
    assert proof['preexecution_seal_sha256']==sha(v.OUT/'preexecution-seal.json')
    for name,value in read(v.OUT/'checksums.json').items():assert sha(v.OUT/name)==value,name
    for name,value in seal['suite_sha256'].items():assert sha(BASE/name)==value,name
    assert sha(v.OUT/'cases.tsv')==seal['cases_tsv_sha256']==proof['cases_tsv_sha256']
    for name,value in seal['runtime_source_sha256'].items():assert sha(ROOT/name)==value,name
    old=read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')['beam_runtime_source_sha256']
    assert old==seal['runtime_source_sha256']
    assert seal['selection_used_buggy_outcomes'] is False
    for version in ['fixed','buggy']:
        checked=[]
        for repeat in ['first','second','method_entry']:
            stage=version+'_'+repeat;raw=(v.OUT/(stage+'.stdout.log')).read_bytes()
            rows=v.validate(v.records(raw),allow_failures=version=='buggy')
            assert rows==proof['stages'][stage]['cases'] and proof['stages'][stage]['exit_code']==0
            assert hashlib.sha256(raw).hexdigest()==proof['stages'][stage]['stdout_sha256']
            if repeat=='method_entry':assert v.validate_trace(v.records(raw))==proof['stages'][stage]['exact_entries']
            checked.append(rows)
        assert checked[0]==checked[1]==checked[2]
    for name,count in [('next_char',3),('difference_score',9)]:
        rows=v.validate(v.records((v.OUT/(name+'-mutation.stdout.log')).read_bytes()),allow_failures=True)
        failed=[r['case'] for r in rows if not r['target_check_passed']]
        assert len(failed)==count and failed==proof['mutations'][name]['failed_cases']
    assert proof['candidate_fault_detected'] is False and proof['buggy_failed_cases']==[]
    assert proof['cpu_lock_exits']==[9,0] and proof['cpu_slots']==1 and proof['worker_id']=='beam-pc1'
    tests=read(BASE/'focused-tests.command.json')
    assert tests['exit_code']==0 and tests['test_source_sha256']==sha(BASE/'test_evidence.py')
    for stream_name in ['stdout','stderr']:assert tests[stream_name+'_sha256']==sha(BASE/('focused-tests.'+stream_name+'.log'))
    log=(BASE/'focused-tests.stderr.log').read_text(encoding='utf-8')
    assert re.search(r'Ran 7 tests in',log) and log.strip().endswith('OK') and 'skipped=' not in log
    return {'status':'beam_bounded_codec_candidate_oracles_accepted_joint_review_pending',
      'policy_id':v.POLICY['policy_id'],'aom_base_commit':v.POLICY['aom_base_commit'],
      'base_condition':v.POLICY['shared_base_condition'],'source_bindings_verified':len(provenance),
      'native_receipt':{'path':(v.OUT/'receipt.json').relative_to(ROOT).as_posix(),'sha256':sha(v.OUT/'receipt.json')},
      'native_seal_sha256':sha(v.OUT/'preexecution-seal.json'),'native_manifest_sha256':sha(v.OUT/'checksums.json'),
      'policy_sha256':sha(BASE/'policy.json'),'cases_predeclared':43,'candidate_signatures':5,
      'fixed_observations':86,'buggy_observations':86,'exact_declarations_entered_per_revision':5,
      'exact_target_entries_per_revision':43,'native_stage_counters':{'executed':43,'target_checks':43,'passed':43,'failed':0,'skipped':0,'fixture_errors':0},
      'candidate_fault_detected':False,'oracle_mutations_detected':['next_char_boolean','difference_score_plus_one'],
      'focused_tests_passed':7,'focused_tests_skipped':0,'unchanged_shared_runtime_files':41,
      'actual_shared_selected':390,'actual_shared_exclusions':301,'denominator':691,
      'possible_codec_only_next_condition':{'selected':395,'exclusions':296,'implemented':False},
      'joint_acceptance_complete':False,'shared_integration_approved':False,'full_legal_domain_approval':False,
      'method_entry_only':True,'line_or_branch_coverage_percentage':None,'full_defects4j_evaluation':False,
      'new_primary_algorithm_or_ai_results':0,'kku_requests':0,'queue_mutations':0,'gate_a_approved':False,'final_reserve':None}
if __name__=='__main__':
    result=audit();result['checked_at_utc']=datetime.now(timezone.utc).isoformat();result['producer_sha256']=sha(Path(__file__))
    write('receipt.json',result)
    methods={
      'isNextChar':('Fresh non-null declared uppercase ASCII StringBuffer; indices-1..length, char A/B/C, default Metaphone().',
          'Predeclared adjacent-next Boolean, false boundary results, unchanged full buffer contents/length/capacity and maxCodeLen4.'),
      'isPreviousChar':('Fresh non-null declared uppercase ASCII StringBuffer; indices-1..length, char A/B/C, default Metaphone().',
          'Predeclared adjacent-previous Boolean, false boundary results, unchanged full buffer contents/length/capacity and maxCodeLen4.'),
      'isVowel':('Fresh non-null AEIOUB buffer with indices0..5; separate empty/negative/at-length exception cases.',
          'True for A/E/I/O/U, false for B; exact StringIndexOutOfBoundsException class at invalid index, unchanged buffer/maxCodeLen state.'),
      'regionMatch':('Fresh non-null ABCA/empty StringBuffer, bounded indices-1..5, non-null declared AB/BC/A/BA/empty needle.',
          'Exact bounded region Boolean including empty needle at end/empty buffer and false beyond-end/too-long/negative cases; unchanged buffer/maxCodeLen state.'),
      'difference':('Reflective production SoundexUtils() identity for static method; real fresh default Metaphone StringEncoder/maxCodeLen4. Strings only null/empty/A/a/E/B/AB.',
          'Exact integer positional similarity from sealed literal encoded references before invocation; actual real encoder encodings match references and maxCodeLen stays4. No null encoder/arbitrary encoder/EncoderException propagation approval.')}
    rows=[]
    for target in v.POLICY['targets']:
        row=dict(target);method=row['target']['method']
        row.update({'candidate_cases':sum(c['method']==method for c in v.CASES),
          'beam_verdict':'accepted_for_bounded_candidate_oracle_development','champ_verdict':None,
          'proposed_preconditions':methods[method][0],'proposed_oracle':methods[method][1],
          'agreed_preconditions':None,'agreed_oracle':None,'accepted_into_shared_inputs':False})
        rows.append(row)
    write('joint-review.template.json',{'base_aom_commit':v.POLICY['aom_base_commit'],'base_condition':v.POLICY['shared_base_condition'],
      'candidate_policy_id':v.POLICY['policy_id'],'candidates':rows,
      'evidence':[{'path':p.relative_to(ROOT).as_posix(),'sha256':sha(p)} for p in [BASE/'receipt.json',BASE/'policy.json',v.OUT/'receipt.json',v.OUT/'checksums.json']],
      'joint_acceptance_complete':False,'shared_integration_approved':False,'actual_selected':390,'actual_exclusions':301,
      'possible_codec_only_next_condition':{'selected':395,'exclusions':296,'implemented':False},
      'prospective_integration_requirements':[
       'Provide real fresh StringBuffer arguments and a real Metaphone StringEncoder; preserve exact default receiver/signatures.',
       'Bind legal-domain mapping and meaningful scalar/exception/buffer/encoder-state oracles in shared helper.',
       'Review impact on existing13 selected Codec targets, including setter/getter state; preserve accepted setter/JDOM/Math/Buffer/Lang recipes.',
       'Give identical source/context/fixtures/oracle knowledge to all four approaches; seal new condition/preparation/protocol/runner/runtime/prompts.',
       'Rerun shared integration/fixed twice/buggy/exact entry and consumer/host checks, then remeasure provider reserve for the chosen condition.',
       'No automatic union with Graphics/Chronology or historical coverage/fault relabeling.'],
      'full_legal_domain_approval':False,'candidate_fault_detected':False,'gate_a_approved':False,'primary_added':0,'kku_requests':0})
    hashes={p.relative_to(BASE).as_posix():sha(p) for p in sorted(BASE.rglob('*')) if p.is_file()}
    write('checksums.json',hashes)
    print(json.dumps({k:result[k] for k in ['status','cases_predeclared','candidate_signatures','focused_tests_passed','candidate_fault_detected']}))
