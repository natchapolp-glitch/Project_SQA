"""Receive a pinned Beam v10 host/consumer verdict without execution or API requests."""
import argparse
import base64
from collections import Counter
import copy
from datetime import datetime, timezone
import gzip
import json
from pathlib import Path
import re

from .common import ROOT, read_json, sha256, write_json
from .review_joint_recipe_intake import digest, require, buffer_oracle
from .review_final_recipe_returns import lang_oracle
from .review_v10_readiness import AOM, CONDITION, PREP, PAIR, READY, BatchedObjects, check_received_worksheet
from .verify_chronology_development import checkpoint

BEAM = '0ca73ee6e30f719d23b6b6b89da22fdf953d5160'
BASE = 'output/api854-20261003/beam-v10-received-review-v1'
HOST = BASE + '/host-review-v2'
CHAMP = ROOT / 'output/api854-20261003/champ-v10-readiness-review-v2'


def validate_scope(receipt, host, runtime):
    require(receipt['aom_commit'] == AOM and receipt['condition'] == host['condition'] == CONDITION,
            'Wrong v10 condition/commit')
    require((receipt['bugs'],receipt['selected'],receipt['exclusions'],receipt['denominator']) == (20,390,301,691),
            'Wrong declaration partition')
    require(receipt['v10_runtime_source_sha256'] == host['runtime_source_sha256'] == runtime,
            'Wrong host runtime')
    require(receipt['consumer_bindings_review_passed'] is True and receipt['beam_host_technical_review_passed'] is True
            and host['environment_ready'] is True, 'Missing scoped acceptance')
    require((receipt['fresh_tests_passed'],receipt['fresh_tests_skipped'],
             receipt['four_consumer_bug_approach_combinations_verified']) == (7,0,80), 'Missing consumer evidence')
    require(receipt['worker_id'] == host['worker_id'] == 'beam-pc1'
            and receipt['cpu_slots'] == host['cpu_slots'] == 1
            and host['cross_process_cpu_lock'] == {'held_slot_rejected_exit':9,'released_slot_reusable_exit':0},
            'CPU slot/lock acceptance differs')
    require(receipt['fresh_fixed_component_cases_verified'] == host['fixed_cases_verified'] == 64
            and receipt['fresh_fixed_observations'] == host['repeated_fixed_observations'] == 128,
            'Wrong fixed observation counts')
    for document in (receipt, host):
        require(document['all_390_semantic_approval'] is False and document['gate_a_approved'] is False
                and document['live_kku_requests'] == document['queue_mutations'] == document['primary_results_added'] == 0,
                'Scoped verdict cannot imply full semantic/Gate A/generation approval')
    require(receipt['primary_protocol_freeze_approved'] is False and receipt['final_reserve'] is None
            and receipt['chronology_integrated_into_v10'] is False, 'Unsubstantiated freeze/reserve/Chronology adoption')


def observation_key(target, vector, policy, outcome, invoked):
    return (tuple(target[k] for k in ('class','constructor_types','method','parameter_types')),
            tuple(float(x) for x in vector), policy, outcome, invoked)


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require a new output directory')
    pins, _ = checkpoint()
    beam, aom = BatchedObjects(BEAM), BatchedObjects(AOM)
    manifest = beam.document(BASE+'/checksums.json')
    beam.preload([BASE+'/'+p for p in manifest])
    require(beam.checksums(BASE) == 1026, 'Received packet manifest inventory differs')
    host_counts = {name:beam.checksums(BASE+'/'+name) for name in ('host-review','host-review-v2')}
    receipt = beam.document(BASE+'/receipt.json')
    host = beam.document(HOST+'/host-receipt.json')
    seal = beam.document(HOST+'/preexecution-seal.json')
    current = read_json(CHAMP/'receipt.json')
    runtime = current['runtime_source_sha256']
    validate_scope(receipt,host,runtime)
    bindings = receipt['input_bindings']
    expected_paths = {'protocol_sha256':PAIR+'/protocol.proposal.json', 'runner_plan_sha256':PAIR+'/runner-plan.json',
                      'preparation_index_sha256':PREP+'/index.json', 'worksheet_sha256':READY+'/prompt-reserve-worksheet.json'}
    for name,path in expected_paths.items():
        require(bindings[name] == {'commit':AOM,'path':path,'sha256':digest(aom.blob(path))}, 'Input binding differs: '+name)
        require(aom.blob(path) == (CHAMP/'received/aom'/path).read_bytes(), 'Previous Champ reviewed input differs')
    require(seal['source_commit'] == AOM and seal['condition'] == CONDITION
            and seal['runtime_source_sha256'] == runtime
            and seal['verifier_sha256'] == digest(aom.blob('scripts/study/api854/verify_v10_recipe_runtime.py'))
            and seal['wrapper_sha256'] == digest(beam.blob(BASE+'/run_host_review_v2.py')), 'Host preexecution source binding differs')
    for obj,key in [(host,'preparation_index_sha256'),(seal,'index_sha256')]:
        require(obj[key] == bindings['preparation_index_sha256']['sha256'], 'Host index differs')
    for obj in (host,seal):
        require(obj['protocol_sha256'] == bindings['protocol_sha256']['sha256']
                and obj['runner_sha256'] == bindings['runner_plan_sha256']['sha256'], 'Host pair differs')
    provenance = beam.document(BASE+'/received-aom-provenance.json') + beam.document(BASE+'/reference-provenance.json')
    sources = {}
    for ref in provenance:
        source = sources.setdefault(ref['source_commit'], BatchedObjects(ref['source_commit']))
        require(digest(source.blob(ref['source_path'])) == ref['sha256']
                == digest(beam.blob(ref['received_path'])), 'Original/received Git provenance differs')
    require(len(provenance) == receipt['received_git_blobs_verified'] == 133, 'Received provenance count differs')
    tests = beam.document(BASE+'/consumer-tests.command.json')
    require(tests['exit_code'] == 0 and tests['source_commit'] == AOM, 'Consumer command failed or wrong source')
    for kind in ('stdout','stderr'):
        require(digest(beam.blob(BASE+'/consumer-tests.'+kind+'.log')) == tests[kind+'_sha256'], 'Consumer log hash differs')
    log = beam.blob(BASE+'/consumer-tests.stderr.log').decode('utf-8')
    require(re.search(r'Ran 7 tests in',log) and log.strip().endswith('OK')
            and 'skipped' not in log.lower() and len(re.findall(r'\.\.\. ok',log)) == 7, 'Consumer execution/skip counts differ')
    environment = beam.document(HOST+'/environment/environment.json')
    require(environment['ready'] is True and digest(beam.blob(HOST+'/environment/environment.json')) == host['environment_sha256'],
            'Host environment binding differs')
    proof = beam.document(HOST+'/fixed-runtime-verification.json')
    require(digest(beam.blob(HOST+'/fixed-runtime-verification.json')) == host['fixed_runtime_proof_sha256'], 'Host proof pin differs')
    old = read_json(CHAMP/'native-fixed-runtime-recheck.json')
    for name in ('status','policy','runtime_helper_sha256','verifier_sha256','dependency_fixed_revisions',
                 'math_source_and_factory_sha256','production_source_sha256','dependency_sha256',
                 'observations','buffer_csv_lang_fixed_cases','legacy_policy_behavior_preserved',
                 'temporary_setter_mutation_detected','mutated_observation','fixed_source_integration_cases'):
        require(proof[name] == old[name], 'Bounded Beam/Champ fixed proof differs: '+name)
    for row in proof['buffer_csv_lang_fixed_cases']:
        reference_commit = next(r['source_commit'] for r in seal['peer_reference_bindings'] if r['source_path']==row['reference_path'])
        source = sources.setdefault(reference_commit,BatchedObjects(reference_commit))
        reference = next(r for r in source.document(row['reference_path']) if r['case_id']==row['case_id'])
        expected = lang_oracle(reference) if row['target']['class'].endswith('NumberUtils') else buffer_oracle(reference)
        require(expected == row['expected'] == row['fixed_first']['outcome'] == row['fixed_second']['outcome'],
                'Independent scalar/state/exception oracle differs')
    observed = Counter()
    commands = sorted(p for p in manifest if p.startswith('host-review-v2/commands/') and p.endswith('.command.json'))
    for path in commands:
        command = beam.document(BASE+'/'+path)
        prefix = HOST+'/'
        stdout = beam.blob(prefix+command['retained_stdout'])
        stderr = beam.blob(prefix+command['retained_stderr'])
        require(command['exit_code'] == 0 and digest(stdout) == command['retained_stdout_sha256']
                and digest(stderr) == command['stderr_sha256'], 'Host raw command evidence differs')
        raw = gzip.decompress(stdout) if command['retained_stdout'].endswith('.gz') else stdout
        require(digest(raw) == command['stdout_sha256'], 'Uncompressed archive/stdout pin differs')
        argv = command['argv']
        if 'SqaProbe' in argv and argv[-1] == proof['policy']:
            at = argv.index('SqaProbe')
            require(argv[at+1] == 'observe', 'Wrong Java operation')
            cls,ctor,method,params,vector,policy = argv[at+2:]
            text = raw.decode('utf-8')
            result = re.search(r'SQA_RESULT:([^\r\n]+)',text)
            trace = re.search(r'SQA_TRACE:([^\r\n]+)',text)
            require(result and trace and 'SQA_FIXTURE_FAILURE:' not in text, 'Missing real target observation')
            outcome = base64.b64decode(result[1]).decode('utf-8')
            invoked = json.loads(trace[1])['target_invoked']
            observed[observation_key({'class':cls,'constructor_types':ctor,'method':method,'parameter_types':params},
                                     vector.split(','),policy,outcome,invoked)] += 1
    expected = Counter()
    for row in proof['observations'] + proof['buffer_csv_lang_fixed_cases']:
        for kind in ('fixed_first','fixed_second'):
            expected[observation_key(row['target'],row['vector'],proof['policy'],row[kind]['outcome'],row[kind]['target_invoked'])] += 1
    mutated = proof['mutated_observation']
    expected[observation_key(proof['observations'][0]['target'],proof['observations'][0]['vector'],
                             proof['policy'],mutated['outcome'],mutated['target_invoked'])] += 1
    require(observed == expected, 'Raw Java observation inventory differs from repeated bounded proof')
    rows = read_json(CHAMP/'prompt-model-worksheet.json')['records']
    check_received_worksheet(aom.document(READY+'/prompt-reserve-worksheet.json'), rows,
                             bindings['preparation_index_sha256']['sha256'],bindings['protocol_sha256']['sha256'],runtime)
    negatives = []
    for name in ('wrong_condition','forged_full_semantic_approval','forged_gate_a','invented_token_reserve','lost_cpu_lock'):
        bad, bad_host = copy.deepcopy(receipt),copy.deepcopy(host)
        if name=='wrong_condition':bad['condition']='wrong'
        if name=='forged_full_semantic_approval':bad['all_390_semantic_approval']=True
        if name=='forged_gate_a':bad['gate_a_approved']=True
        if name=='invented_token_reserve':bad['final_reserve']=270033
        if name=='lost_cpu_lock':bad_host['cross_process_cpu_lock']['held_slot_rejected_exit']=0
        try:validate_scope(bad,bad_host,runtime)
        except ValueError:negatives.append(name)
        else:raise ValueError('Negative control accepted: '+name)
    require(checkpoint()[0] == pins, 'Champ shared pins changed')
    output.mkdir(parents=True,exist_ok=False)
    retained = ['receipt.json','consumer-tests.command.json','consumer-tests.stderr.log','consumer-tests.stdout.log',
                'host-review-v2/host-receipt.json','host-review-v2/preexecution-seal.json',
                'host-review-v2/environment/environment.json','host-review/failure.json']
    for path in retained:
        target=output/'received'/path;target.parent.mkdir(parents=True,exist_ok=True);target.write_bytes(beam.blob(BASE+'/'+path))
    (output/'review-producer.py').write_bytes(Path(__file__).read_bytes())
    result = {'schema_version':1,'status':'received_scoped_beam_v10_consumer_bounded_oracle_and_host_acceptance',
              'checked_at_utc':datetime.now(timezone.utc).isoformat(),'beam_commit':BEAM,'aom_commit':AOM,'condition':CONDITION,
              'input_bindings':bindings,'received_manifest_entries_verified':len(manifest),'host_manifest_entries':host_counts,
              'original_received_provenance_entries_verified':len(provenance),'raw_host_commands_verified':len(commands),
              'raw_new_policy_observations_verified_including_separate_mutation':sum(observed.values()),
              'bounded_fixed_cases_received':64,'repeated_fixed_observations_received':128,'independent_oracle_cases_rechecked':54,
              'beam_tests_received':7,'beam_tests_skipped':0,'four_consumer_combinations_received':80,
              'beam_worker':'beam-pc1','beam_cpu_slots':1,'beam_scoped_host_acceptance_received':True,
              'all_390_semantic_approval':False,'aom_champ_hosts_final_v10_acceptance_complete':False,
              'prompt_model_pairs_rebound':40,'max_prompt_utf8_bytes':265937,'conditional_byte_guard_floor':270033,
              'framing_tokens':None,'final_token_reserve':None,'current_provider_readiness_verified':False,
              'negative_controls_rejected':negatives,'shared_champ_pins_unchanged':len(pins),
              'shared_v10_adopted_into_champ_runtime':False,'new_java_or_defects4j_executions_this_review':0,
              'authenticated_kku_requests':0,'live_queue_mutations':0,'quota_ledger_modified':False,
              'gate_a_passed':False,'primary_results_added':0,
              'original_evidence_bindings':{'commit':BEAM,'sha256':beam.bindings},
              'received_host_proof_sha256':host['fixed_runtime_proof_sha256'],
              'existing_champ_worksheet_sha256':sha256(CHAMP/'prompt-model-worksheet.json'),
              'existing_champ_native_fixed_proof_sha256':sha256(CHAMP/'native-fixed-runtime-recheck.json')}
    write_json(output/'receipt.json',result)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,required=True)
    result=run(parser.parse_args().output)
    print(json.dumps({k:result[k] for k in ('status','received_manifest_entries_verified','raw_host_commands_verified',
                                          'prompt_model_pairs_rebound','final_token_reserve')}))
