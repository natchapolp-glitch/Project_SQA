"""Read-only immutable intake/native Graphics audit; seal a scoped return packet."""
from pathlib import Path
from datetime import datetime,timezone
import copy,hashlib,io,json,subprocess,sys,tarfile
BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
SNAP=ROOT/'.local/api854/beam-graphics-snapshot-8d9295e6'
PEER=BASE/'received-champ';NATIVE=BASE/'host-review-v1/native-evidence'
sys.path[:0]=[str(SNAP),str(SNAP/'scripts/study')]
from scripts.study.api854 import verify_graphics_development as verifier
PREFIX='output/api854-20261003/'
CHAMP='8d9295e6c238ce6e2f9c2014932207ef2f036663'
AOM='a4880fb2fde574e77705841f62f302273be7dcd9'
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def require(ok,message):
    if not ok:raise ValueError(message)
def write(name,data):
    with (BASE/name).open('x',encoding='utf-8',newline='\n') as f:
        json.dump(data,f,ensure_ascii=False,indent=2);f.write('\n')
def check_manifest(folder):
    rows=read(folder/'checksums.json')
    require('checksums.json' not in rows,'Self-referential checksum manifest')
    for name,value in rows.items():
        path=(folder/name).resolve();require(path.is_relative_to(folder.resolve()),'Checksum escaped evidence root')
        require(sha(path)==value,'Checksum differs: '+str(path))
    return len(rows)
def git_blobs(rows):
    # Preserve binary blobs and exact file sizes rather than parsing by lines.
    queries=[r['commit']+':'+r['source_path'] for r in rows]
    run=subprocess.run(['git','cat-file','--batch'],cwd=ROOT,
                       input=('\n'.join(queries)+'\n').encode(),capture_output=True,check=True)
    stream=io.BytesIO(run.stdout)
    for row in rows:
        header=stream.readline().split()
        require(len(header)==3 and header[1]==b'blob','Missing Git blob: '+row['source_path'])
        raw=stream.read(int(header[2]));require(stream.read(1)==b'\n','Bad Git framing')
        require(hashlib.sha256(raw).hexdigest()==row['sha256'],'Git source pin differs: '+row['source_path'])
        if row.get('received_path'):
            require((ROOT/row['received_path']).read_bytes()==raw,'Received bytes differ')
    require(stream.read()==b'','Unexpected Git batch output')
    return len(rows)
def identity(row):return tuple(row[k] for k in ('class','constructor_types','method','parameter_types'))
def audit():
    provenance=read(BASE/'received-provenance.json')
    require(all(row['source_commit']==CHAMP for row in provenance),'Wrong peer commit')
    git_count=git_blobs([{**r,'commit':r['source_commit']} for r in provenance])
    snap=read(BASE/'snapshot-provenance.json')
    supplement=read(BASE/'snapshot-supplement-provenance.json')
    pins=snap['files']+supplement
    for row in pins:require(sha(SNAP/row['source_path'])==row['sha256'],'Snapshot changed')
    snapshot_count=git_blobs([{**r,'commit':CHAMP} for r in pins])
    count=0
    for version in ('v1','v2','v3'):
        count+=check_manifest(PEER/(PREFIX+'graphics-development-'+version))
    count+=check_manifest(BASE/'host-review-v1')
    count+=check_manifest(NATIVE)
    peer=read(PEER/(PREFIX+'graphics-development-v3/receipt.json'))
    native=read(NATIVE/'receipt.json')
    host=read(BASE/'host-review-v1/host-receipt.json')
    require(native['status']=='pass' and native['cases_per_revision']==24 and
            native['exact_declarations_entered_per_revision']==7,'Native proof not complete')
    require(native['shared_input_sha256']==peer['shared_input_sha256'] and
            native['runtime_source_sha256']==peer['runtime_source_sha256'],'Peer/native input pins differ')
    require(native['compiled_source_sha256']==peer['compiled_source_sha256'] and
            native['dependencies_sha256']==peer['dependencies_sha256'],'Production source/dependencies differ')
    require({v:m['svn_revision'] for v,m in native['source_archives'].items()}==
            {'fixed':'2266','buggy':'2264'},'Wrong original Chart SVN revisions')
    for proof,folder in [(peer,PEER/(PREFIX+'graphics-development-v3')),(native,NATIVE)]:
        require(proof['candidate_fault_detected'] is False and proof['buggy_failed_cases']==[],
                'This candidate has no demonstrated Chart-1 fault')
        require(proof['full_defects4j_evaluation'] is False and proof['shared_integration_approved'] is False
                and proof['gate_a_passed'] is False and proof['live_requests']==proof['queue_mutations']==0,
                'Out-of-scope primary/live/shared approval')
        for version in ('fixed','buggy'):
            validated=[]
            for suffix in ('first','second','method_entry_trace'):
                stage=version+'_'+suffix
                records=verifier.parse_log((folder/(stage+'.stdout.log')).read_bytes())
                rows=verifier.validate_records(records,folder/(stage+'-pixels'),allow_assertion_failures=version=='buggy')
                require(rows==proof['stages'][stage]['cases'],'Receipt/raw cases differ')
                require(proof['stages'][stage]['exit_code']==0,'Production stage failed')
                if suffix=='method_entry_trace':
                    entries=verifier.validate_trace(records,0)
                    require(len(entries)==24 and entries==proof['stages'][stage]['exact_target_entries'],
                            'Trace receipt differs from raw method entry')
                validated.append(rows)
            require(validated[0]==validated[1]==validated[2],'Repeated/traced observations differ')
        records=verifier.parse_log((folder/'temporary-shifted-line.stdout.log').read_bytes())
        mutated=verifier.validate_records(records,folder/'temporary-shifted-line-pixels',allow_assertion_failures=True)
        require([r['case'] for r in mutated if not r['target_check_passed']]==['domain_line_v'] and
                proof['temporary_shifted_line_mutation']['exit_code']==1,'Shift mutation not detected')
        seal=read(folder/'preexecution-seal.json')
        require(sha(folder/'preexecution-seal.json')==proof['preexecution_seal_sha256'],'Seal receipt differs')
        require(seal['selection_used_buggy_outcomes'] is False and seal['owner_approval'] is False,
                'Outcome selection/approval differs')
        for name,value in proof['suite_sha256'].items():require(sha(folder/name)==value,'Executed suite changed')
        for version,metadata in proof['source_archives'].items():
            archive=folder/(version+'-production-source.tar.gz')
            require(sha(archive)==metadata['archive_sha256'],'Production archive changed')
            with tarfile.open(archive) as t:
                sources={m.name:t.extractfile(m).read() for m in t.getmembers() if m.name.endswith('.java')}
            if version=='fixed':
                for name in [verifier.SOURCE,verifier.RECEIVER_SOURCE]:
                    retained=(verifier.PREP/'fixed-source'/name).read_bytes()
                    require(sources[name].replace(b'\r\n',b'\n')==retained.replace(b'\r\n',b'\n'),
                            'Retained production bytes differ')
                    sources[name]=retained
            require(len(sources)==654 and set(sources)==set(proof['compiled_source_sha256'][version]),
                    'Wrong source inventory')
            for name,raw in sources.items():
                require(hashlib.sha256(raw).hexdigest()==proof['compiled_source_sha256'][version][name],
                        'Compiled source pin differs')
            for name,value in proof['dependencies_sha256'][version].items():
                require(sha(folder/'dependencies'/name)==value,'Dependency changed')
    stages=['fixed_first','fixed_second','buggy_first','buggy_second',
            'fixed_method_entry_trace','buggy_method_entry_trace']
    equal={stage:native['stages'][stage]['cases']==peer['stages'][stage]['cases'] for stage in stages}
    require(all(equal.values()) and host['peer_case_observations_equal_by_stage']==equal,
            'No cross-host raster/observation equivalence')
    require(host['worker_id']=='beam-pc1' and host['cpu_slots']==1 and host['cpu_lock_exits']==[9,0],
            'One-slot Beam host not verified')
    require('11.' in host['java_version'] and 'Linux' in host['platform'],'Wrong native Java/AWT host')
    require(host['canonical_native_receipt_sha256']==sha(NATIVE/'receipt.json'),'Native receipt binding differs')
    adapter=read(BASE/'host-review-v1/adapter-preexecution-seal.json')
    require(adapter['wrapper_sha256']==sha(BASE/'run_native_host.py') and
            adapter['canonical_verifier_sha256']==sha(SNAP/'scripts/study/api854/verify_graphics_development.py'),
            'Native adapter or exact verifier changed')
    tests=read(BASE/'received-tests-v2.command.json')
    log=(BASE/'received-tests-v2.stderr.log').read_text(encoding='utf-8')
    require(tests['exit_code']==0 and 'Ran 8 tests' in log and '\nOK\n' in log and 'skipped=' not in log,
            'Focused tests not all passed')
    for stream in ('stdout','stderr'):
        require(tests[stream+'_sha256']==sha(BASE/('received-tests-v2.'+stream+'.log')),'Test log changed')
    old=read(ROOT/'output/api854-20261003/beam-v10-received-review-v1/receipt.json')
    require(old['aom_commit']==AOM and old['selected']==390 and old['exclusions']==301,'Wrong shared condition')
    old_runtime=read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/received-aom/joint-lang-acceptance.template.json')['beam_runtime_source_sha256']
    for path,value in old_runtime.items():require(sha(ROOT/path)==value,'Historical Beam runtime changed')
    shared_targets=json.loads(subprocess.check_output(['git','show',AOM+':'+PREFIX+'prepare-v10-joint-development/Chart-1/targets.json'],cwd=ROOT))
    exclusions=json.loads(subprocess.check_output(['git','show',AOM+':'+PREFIX+'prepare-v10-joint-development/Chart-1/capability-exclusions.json'],cwd=ROOT))
    candidate={identity(r) for r in verifier.POLICY['worklist_identities']}
    selected={identity(r) for r in shared_targets['targets']}
    excluded={identity(r['target']) for r in exclusions['excluded']}
    require(len(candidate)==7 and len(selected)==8 and candidate<=excluded and not candidate&selected,
            'Graphics seven must remain excluded in actual v10')
    return {'status':'accepted_for_bounded_graphics_candidate_oracle_and_native_host_review',
      'champ_commit':CHAMP,'policy_id':verifier.POLICY['policy_id'],
      'received_git_blobs_verified':git_count,'snapshot_git_blobs_verified':snapshot_count,
      'checksum_entries_verified':count,'runtime_source_sha256':native['runtime_source_sha256'],
      'runtime_binding_scope':'Immutable Champ v9 candidate source runtime; not a new shared v10 helper integration',
      'historical_beam_runtime_files_unchanged':len(old_runtime),'fresh_focused_tests_passed':8,
      'fresh_focused_tests_skipped':0,'candidate_signatures':7,'unique_bounded_cases':24,
      'fixed_runs':2,'buggy_runs':2,'fixed_observations':48,'buggy_observations':48,
      'exact_entries_per_revision':24,'exact_declarations_entered_per_revision':7,
      'candidate_fault_detected':False,'oracle_shifted_line_detected':True,
      'peer_raster_equivalence_by_stage':equal,'host_worker_id':'beam-pc1','cpu_slots':1,
      'cpu_lock_exits':[9,0],'shared_preparation_approved':False,'full_legal_domain_approval':False,
      'actual_shared_condition':old['condition'],'actual_shared_aom_commit':AOM,
      'actual_shared_input_bindings':old['input_bindings'],
      'actual_selected':390,'actual_exclusions':301,'denominator':691,
      'existing_chart_selected_targets':8,'graphics_only_possible_next_condition_counts':
      {'selected':397,'exclusions':294,'implemented':False},
      'gate_a_approved':False,'final_reserve':None,'live_requests':0,'queue_mutations':0,
      'primary_results_added':0,'full_defects4j_evaluation':False}
def joint_return(result):
    template=read(PEER/(PREFIX+'champ-graphics-joint-review.template.json'))
    template['review_status']='beam_and_champ_bounded_candidate_agreement_shared_integration_pending'
    template['champ_candidate_commit']=CHAMP
    for row in template['evidence']:row['commit']=CHAMP
    for row in template['candidates']:
        row['beam_verdict']='accepted_for_bounded_candidate_oracle_development'
        row['agreed_preconditions']=row['proposed_preconditions']
        row['agreed_oracle']=row['proposed_oracle']
        row['aom_shared_integration_verdict']=None
        row['accepted_into_shared_inputs']=False
    template['joint_acceptance_complete']=True
    template['joint_acceptance_scope']='Seven bounded standalone candidate oracles only; not shared/runtime/primary integration'
    template['actual_selected']=390;template['actual_unsupported']=301
    template['actual_shared_condition']=result['actual_shared_condition']
    template['actual_shared_aom_commit']=AOM
    template['candidate_source_shared_condition']='Champ v9 380/311/691'
    template['graphics_only_proposed_union']={'selected':397,'unsupported':294,'implemented':False}
    template['current_shared_chart_fixture']['helper']={
        'commit':AOM,'path':'algorithms/java/SqaProbe.java',
        'sha256':read(ROOT/'output/api854-20261003/beam-v10-received-review-v1/receipt.json')['v10_runtime_source_sha256']['algorithms/java/SqaProbe.java']}
    template['beam_evidence']=[{'path':p.relative_to(ROOT).as_posix(),'sha256':sha(p)} for p in [
        BASE/'host-review-v1/host-receipt.json',NATIVE/'receipt.json',NATIVE/'preexecution-seal.json',
        BASE/'host-review-v1/checksums.json',BASE/'received-tests-v2.command.json']]
    template['native_host_binding']={'worker_id':'beam-pc1','cpu_slots':1,'java_major':11,
        'headless':True,'measured_raster_scope_only':True,'peer_raster_equal':True}
    return template
if __name__=='__main__':
    result=audit();result['checked_at_utc']=datetime.now(timezone.utc).isoformat()
    result['producer_sha256']=sha(Path(__file__))
    write('receipt.json',result)
    write('joint-verdict.json',joint_return(result))
    chart_targets=json.loads(subprocess.check_output(['git','show',AOM+':'+PREFIX+'prepare-v10-joint-development/Chart-1/targets.json'],cwd=ROOT))
    write('integration-requirements.json',{
        'status':'shared_integration_pending','base_aom_commit':AOM,
        'base_condition':result['actual_shared_condition'],
        'base_counts':{'selected':390,'exclusions':301,'denominator':691},
        'existing_selected_chart_targets_to_preserve_and_regress':chart_targets['targets'],
        'candidate_source_commit':CHAMP,'candidate_policy_id':verifier.POLICY['policy_id'],
        'bounded_candidates':joint_return(result)['candidates'],
        'requirements':[
            'Use exact default AreaRenderer() receiver and inherited AbstractCategoryItemRenderer descriptors.',
            'Choose per-target lifecycle or explicitly reviewed new Chart condition; preserve/regress all eight selected Chart targets.',
            'initialise target setup must not invoke initialise; start with distinct real non-null plot, info null, real 2x2 or legal null dataset.',
            'Graphics64x64 TYPE_INT_RGB, white background, antialias off/stroke normalize, identity transform/null clip, actual contexts disposed.',
            'Keep analytic coordinates/reference before invocation plus full pixel, graphics, receiver/dataset/return/exception state; do not weaken to object identity or non-null.',
            'Separate fixture_error from target failure; null-dataset is legal here and is not the null-Paint/Stroke boundary.',
            'Give identical sealed production sources, fixture recipes, oracle knowledge/context to all four approaches.',
            'Create a new prospective condition and validate fixed twice, buggy, exact method entry/sensitivity and consumers/host on the shared helper.',
            'Seal final preparation/index/protocol/runner/runtime/prompts and measure reserve for that same new condition before Gate A.',
            'Keep Chart-1 candidate fault=false and method entry separate from line/branch coverage and historical algorithm outcomes.'
        ],
        'graphics_only_possible_next_counts':{'selected':397,'exclusions':294,'implemented':False},
        'new_preparation_authorized_by_receipt':False,'gate_a_approved':False,'primary_added':0,
        'live_requests':0,'queue_mutations':0})
    hashes={p.relative_to(BASE).as_posix():sha(p) for p in sorted(BASE.rglob('*')) if p.is_file()}
    write('checksums.json',hashes)
    print(json.dumps(result,ensure_ascii=False,indent=2))
