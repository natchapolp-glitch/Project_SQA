"""Review Champ's joint recipe return and the real shared-v9 fixture diagnostics."""
from pathlib import Path
from datetime import datetime, timezone
import copy
import hashlib
import importlib.util
import io
import json
import subprocess

BASE=Path(__file__).resolve().parent;ROOT=BASE.parents[2]
PEER=BASE/'received-champ'
RETURN=PEER/'output/api854-20261003/champ-final-recipe-joint-return-v1'
TIME=PEER/'output/api854-20261003/prepare-v9-twenty-bug-development/Time-1'
DIAG=BASE/'shared-v9-diagnostic-v2'
COMMIT='7de14726c-placeholder'
def require(condition,message):
    if not condition:raise ValueError(message)
def sha(path):return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path):return json.loads(path.read_text(encoding='utf-8'))
def key(row):return tuple(row[k] for k in ['class','constructor_types','method','parameter_types'])
def write(name,data):
    with (BASE/name).open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(data,stream,ensure_ascii=False,indent=2);stream.write('\n')


def validate_boundary(joint,policy,diagnostic):
    require(joint['joint_bounded_candidate_acceptance_complete'] is True,'Joint candidate receipt missing')
    require(joint['worklist_identities']==read(PEER/'output/api854-20261003/chronology-development-v2/receipt.json')['worklist_identities'],
            'Receiver identities differ')
    require(joint['exact_targets']==policy['exact_targets'],'Exact JVM overload differs')
    require(joint['agreed_preconditions']==policy['preconditions'],'Domain or setup contract differs')
    require(joint['agreed_assertions']==policy['assertions'],'State/identity/exception assertions weakened')
    require(joint['shared_integration_approved'] is False and joint['accepted_into_shared_inputs'] is False,
            'Candidate cannot certify shared integration')
    require(joint['gate_a_approved'] is False,'Candidate cannot approve Gate A')
    require(diagnostic['target_invocations']==0 and diagnostic['fixture_failures']==12 and
            diagnostic['candidate_fault_counted'] is False,'Fixture failures counted as algorithm bugs')
    require(diagnostic['final_condition_host_approval'] is False,'Old helper diagnostics cannot approve a final host')


def audit():
    provenance=read(BASE/'provenance.json');commit=provenance[0]['source_commit']
    paths=list(dict.fromkeys(item['source_path'] for item in provenance))
    stream=io.BytesIO(subprocess.check_output(['git','cat-file','--batch'],cwd=ROOT,
        input=''.join(commit+':'+p+'\n' for p in paths).encode()))
    blobs={}
    for path in paths:
        header=stream.readline().split();require(len(header)==3 and header[1]==b'blob','Source Git blob missing')
        blobs[path]=stream.read(int(header[2]));require(stream.read(1)==b'\n','Bad Git response')
    require(stream.read()==b'','Extra Git response')
    for item in provenance:
        require(item['source_commit']==commit and sha(ROOT/item['received_path'])==item['sha256']==hashlib.sha256(blobs[item['source_path']]).hexdigest(),
                'Received provenance mismatch')
    checksum_entries=0
    for folder in [RETURN,BASE/'shared-v9-diagnostic',DIAG]:
        for name,value in read(folder/'checksums.json').items():
            require(sha(folder/name)==value,'Packet checksum mismatch: '+name);checksum_entries+=1
    index=read(RETURN/'return-index.json')
    for item in index['verdicts']:
        require(sha(PEER/item['path'])==item['sha256'],'Joint return-index hash differs')
    receipt=read(RETURN/'receipt.json')
    for path,value in receipt['champ_runtime_source_sha256'].items():
        require(sha(PEER/path)==value,'Shared runtime pin mismatch')
    require(len(receipt['champ_runtime_source_sha256'])==41,'Shared runtime inventory changed')
    joint=read(RETURN/'champ-chronology-joint-verdict.json')
    policy=read(PEER/'output/api854-20261003/chronology-development-v2/policy.json')
    diag=read(DIAG/'receipt.json')
    validate_boundary(joint,policy,diag)
    prior=read(ROOT/'output/api854-20261003/beam-chronology-review-v1/beam-chronology-verdict.json')
    require(sha(ROOT/joint['received_beam_verdict']['path'])==joint['received_beam_verdict']['sha256'], 'Champ received Beam verdict binding differs')
    require(joint['exact_targets']==prior['exact_targets'] and joint['policy_sha256']==prior['policy_sha256'], 'Joint Chronology scope differs from Beam')
    selected=read(TIME/'targets.json')['targets']
    exclusions=read(TIME/'capability-exclusions.json')['excluded']
    require(all(key(target) not in {key(t) for t in selected} for target in joint['worklist_identities']), 'Unexpected Chronology target selection')
    reasons=[]
    for identity in joint['worklist_identities']:
        row=next(row for row in exclusions if key(row['target'])==key(identity))
        reasons.append({'target':identity,'current_exclusion_reason':row['reason']})
    metadata=read(TIME/'prepare-metadata.json')
    require(sha(TIME/'targets.json')==metadata['targets_sha256'],'Time target pin mismatch')
    require(diag['helper_sha256']==sha(PEER/'algorithms/java/SqaProbe.java'),'Compiled helper differs from shared snapshot')
    seal=read(DIAG/'preexecution-seal.json')
    require(sha(DIAG/'preexecution-seal.json')==diag['preexecution_seal_sha256'],'Diagnostic seal changed')
    require(seal['policy_id']==metadata['fixture_policy_id'] and seal['helper_sha256']==diag['helper_sha256'], 'Wrong diagnostic policy/helper')
    require(seal['driver_sha256']==sha(BASE/'SharedChronologyDiagnosticV2.java') and seal['producer_sha256']==sha(BASE/'run_shared_diagnostic_v2.py'),'Diagnostic sources changed')
    require((DIAG/'first.stdout.log').read_bytes()==(DIAG/'second.stdout.log').read_bytes(),'Diagnostic repeat mismatch')
    rows=[json.loads(line) for line in (DIAG/'first.stdout.log').read_text().splitlines()]
    require(rows==diag['observations'] and len(rows)==6,'Raw diagnostic observations differ')
    for identity,row in zip(joint['worklist_identities'],rows):
        require(all(row[k]==identity[k] for k in ['constructor_types','method','parameter_types']),'Wrong diagnostic target')
        require(row['target_invoked'] is False and row['error_class']=='SqaProbe$FixtureFailure','Unexpected target failure')
    source=(PEER/'algorithms/java/SqaProbe.java').read_text()
    require('org.joda.time.Chronology' not in source,'Current source gap changed; re-review actual implementation')
    require('return "partial:" + call(result, "toStringList", new Class<?>[]{});' in source,'Current Partial projection changed')
    require('No explicit recipe: ' in source,'Expected fixture separation changed')
    for name in ['compile','first','second','java-version']:
        command=read(DIAG/(name+'.command.json'))
        require(command['exit_code']==0,'Diagnostic command failed')
        for kind in ['stdout','stderr']:require(sha(DIAG/(name+'.'+kind+'.log'))==command[kind+'_sha256'],'Command log differs')
    negatives=[]
    controls=[('wrong_descriptor',lambda j,d:j['exact_targets']['field'].__setitem__(1,'()V')),
              ('changed_receiver_identity',lambda j,d:j['worklist_identities'][0].update(constructor_types='org.joda.time.Chronology')),
              ('weakened_oracle',lambda j,d:j.update(agreed_assertions=['type-only'])),
              ('premature_shared_approval',lambda j,d:j.update(shared_integration_approved=True)),
              ('fixture_counted_as_bug',lambda j,d:d.update(candidate_fault_counted=True)),
              ('premature_final_host_approval',lambda j,d:d.update(final_condition_host_approval=True))]
    for label,mutate in controls:
        j,d=copy.deepcopy(joint),copy.deepcopy(diag);mutate(j,d)
        try:validate_boundary(j,policy,d)
        except ValueError:negatives.append(label)
        else:raise ValueError('Invalid integration approval accepted: '+label)
    old=read(ROOT/'output/api854-20261003/beam-final-recipe-return-v1/beam-lang-verdict.json')
    for path,value in old['current_runtime_source_sha256'].items():require(sha(ROOT/path)==value,'Beam runtime changed')
    return {'status':'review_complete_shared_invocation_blocked_final_condition_not_received',
            'champ_commit':commit,'aom_snapshot_commit':receipt['aom_commit'],
            'received_git_blobs_verified':len(provenance),'checksum_entries_verified':checksum_entries,
            'joint_verdict_groups_verified':4,'shared_runtime_pins_verified':41,'beam_runtime_pins_unchanged':41,
            'chronology_joint_candidate_declarations':6,'chronology_current_selected':0,
            'current_exclusion_reasons':reasons,'shared_helper_sha256':diag['helper_sha256'],
            'shared_policy_id':metadata['fixture_policy_id'],'fixed_diagnostic_attempts':12,
            'target_invocations':0,'fixture_failures':12,'candidate_fault_counted':False,
            'negative_controls_rejected':negatives,'final_condition_received':False,
            'final_four_consumer_checks_run':0,'final_condition_host_approval':False,
            'new_defects4j_evaluations':0,'live_kku_requests':0,'queue_mutations':0,'primary_results_added':0,
            'gate_a_approved':False,'final_reserve':None,'shared_integration_approved':False,
            'shared_preparation_modified':False,'producer_sha256':sha(Path(__file__))}


if __name__=='__main__':
    result=audit()
    spec=importlib.util.spec_from_file_location('beam_independent_chronology_oracle',ROOT/'output/api854-20261003/beam-chronology-review-v1/review_received.py')
    oracle=importlib.util.module_from_spec(spec);spec.loader.exec_module(oracle)
    joint=read(RETURN/'champ-chronology-joint-verdict.json')
    write('integration-requirements.json',{'schema_version':1,'scope':'Beam review contract for prospective shared Chronology integration; no implementation approval',
        'joint_verdict_source':{'commit':result['champ_commit'],'path':'output/api854-20261003/champ-final-recipe-joint-return-v1/champ-chronology-joint-verdict.json','sha256':sha(RETURN/'champ-chronology-joint-verdict.json')},
        'worklist_identities':joint['worklist_identities'],'exact_targets':joint['exact_targets'],
        'agreed_preconditions':joint['agreed_preconditions'],'agreed_assertions':joint['agreed_assertions'],
        'required_integration_checks':joint['required_integration_checks'],
        'independent_expected_observations':oracle.EXPECTED,'independent_oracle_source_sha256':sha(ROOT/'output/api854-20261003/beam-chronology-review-v1/review_received.py'),
        'new_policy_condition_required':True,'required_future_evidence':['exact invocation/receiver identities','source/runtime/recipe pins',
            'preexecution seal','fixed twice / buggy / exact target-entry checks','semantic projections with assertion sensitivity',
            'all four consumer bindings on final 20-bug preparation','beam-pc1 one-CPU host bindings for final runner'],
        'current_shared_support':False,'final_approval':False})
    result['checked_at_utc']=datetime.now(timezone.utc).isoformat()
    result['integration_requirements_sha256']=sha(BASE/'integration-requirements.json')
    write('receipt.json',result)
    print(json.dumps({k:result[k] for k in ['status','received_git_blobs_verified','checksum_entries_verified',
                                          'fixed_diagnostic_attempts','target_invocations','fixture_failures','negative_controls_rejected']}))
