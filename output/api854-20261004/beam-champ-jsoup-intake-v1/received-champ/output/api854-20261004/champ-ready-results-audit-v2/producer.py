"""Audit the next actual v12 ready-bug outcomes without repairing any suite.

Joins the immutable earlier 12-outcome audit and new Jsoup-1 four outcomes.
Invalid/partial outcomes stay visible; coverage missing is null, never zero.
"""
import argparse
import csv
import io
import json
from pathlib import Path
import re
import tarfile

from .common import ROOT,read_json,write_json,sha256
from .review_joint_recipe_intake import require,digest
from .review_v10_readiness import BatchedObjects
from .verify_chronology_development import checkpoint
from .start_csv_development import AOM,PAIR
from .evaluate_csv_development import counts,coverage_xml,PROJECTS
from .audit_ready_development_results import audit_packet
from .kku_client import normalize_usage,normalize_quota,utc_now

BASE=ROOT/'output/api854-20261004'
GEN=BASE/'champ-jsoup-development-generation-v1'
NATIVE=BASE/'champ-jsoup-native-measurement-v2'
FAILED=BASE/'champ-jsoup-native-measurement-v1'
PRIOR=BASE/'champ-ready-results-audit-v1'
PREP='output/api854-20261003/prepare-v12-graphics-development-v1/Jsoup-1'


def run(output):
    output=Path(output).resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    pins,excluded=checkpoint();packets={p.name:audit_packet(p) for p in (GEN,FAILED,NATIVE,PRIOR)}
    plan=read_json(GEN/'preexecution-plan.json');seal=read_json(NATIVE/'preexecution-seal.json')
    require(plan['shared_checkpoint_pins']==pins and plan['account_alias']=='a03' and plan['allocation_declared_before_requests'],
            'Checkpoint or prospective account allocation differs')
    require(plan['total_requests_cap']==4 and not plan['automatic_retry'] and not plan['feedback'] and not plan['account_switch_during_attempt'],
            'Request contract differs')
    aom=BatchedObjects(AOM);inputs=list(plan['received_inputs_sha256'])
    aom.preload([PREP+'/'+n for n in inputs]+[PAIR+'/protocol.proposal.json'])
    for name,h in plan['received_inputs_sha256'].items():
        require((GEN/'received'/name).read_bytes()==aom.blob(PREP+'/'+name) and sha256(GEN/'received'/name)==h,'Exact v12 input changed')
    require(seal['runtime_source_sha256']==aom.document(PAIR+'/protocol.proposal.json')['source_sha256'],'Runtime pin differs')
    require(seal['seed']==101 and seal['budget']==30 and seal['test_method_cap']==30,'Suite cap/seed differs')
    require(seal['project']=='Jsoup' and seal['bug_id']==1 and seal['aom_commit']==AOM,'Native source condition differs')
    require(seal['exact_production_revisions']=={'fixed':PROJECTS['Jsoup'][3],'buggy':PROJECTS['Jsoup'][4]},'Source revisions differ')
    require(read_json(GEN/'native-preflight/receipt.json')['exact_targets']==10,'Preflight declarations differ')
    generations=read_json(GEN/'receipt.json');result=read_json(NATIVE/'receipt.json')
    require(generations['requests_attempted']==4 and generations['generation_requests_attempted']==2,'Actual request accounting differs')
    require(result['approaches']==4 and result['primary_results_added']==0 and result['full_defects4j_evaluations']==0,'Outcome scope differs')
    require(read_json(FAILED/'failed-attempt.json')['error']=='Instrumentation failed','Retained failed attempt differs')
    require((FAILED/'cmaes/algorithm-generation/GeneratedStudyTest.java').read_bytes()==
            (NATIVE/'cmaes/algorithm-generation/GeneratedStudyTest.java').read_bytes(),
            'Seeded algorithm suite changed after instrumentation classpath repair')
    prior=read_json(PRIOR/'results.json')['records'];require(len(prior)==12,'Prior audit outcome count differs')
    table=list(prior);fresh=[]
    for row in result['records']:
        approach=row['approach'];folder=NATIVE/approach;declared=row.get('declared_test_count')
        for stage,record in row.get('stages',{}).items():
            require(counts((folder/(stage+'.stdout.log')).read_bytes())==record['counts'],'Actual JUnit counts differ')
            if record.get('target_counts'):
                require(read_json(folder/(stage+'.target-counts.json'))==record['target_counts'],'Actual target counters differ')
        if declared:
            require(0<declared<=30,'Suite exceeds entire-method cap')
            java={p.relative_to(folder/'sources').as_posix():p.read_bytes() for p in (folder/'sources').rglob('*.java')}
            require({n:digest(raw) for n,raw in java.items()}==row['source_sha256'],'Java bytes changed')
            with tarfile.open(folder/'packaged-suite/suite.tar.bz2') as archive:
                files=[m for m in archive if m.isfile()]
                require({m.name for m in files}==set(java) and all(archive.extractfile(m).read()==java[m.name] for m in files),
                        'Packaged suite differs from unchanged generated source')
            if approach.startswith('kku-'):
                raw=(GEN/approach/'raw-response.txt').read_text(encoding='utf-8')
                blocks=re.findall(r'^```(?:java)?\s*\n(.*?)^```\s*$',raw,re.MULTILINE|re.DOTALL)
                require({b.encode() for b in blocks}==set(java.values()),'AI assertions or source were repaired')
            else:require((folder/'algorithm-generation/GeneratedStudyTest.java').read_bytes()==java['GeneratedStudyTest.java'],'Algorithm suite repaired')
        coverage=row.get('coverage',{})
        if coverage:
            require(row['status']=='native_fixed_twice_buggy_coverage_measured','Coverage on invalid suite')
            require(coverage_xml(folder/'coverage-report/coverage.xml',PROJECTS['Jsoup'][2])==coverage,'Exact Document coverage differs')
            require(all(row['stages'][s]['counts']=={'executed':declared,'skipped':0,'failed':0}
                        for s in ('fixed_first','fixed_second','coverage')),'Fixed/coverage counts differ')
            require(not any(marker in (folder/'buggy.stdout.log').read_bytes() for marker in
                            (b'SQA_HARNESS',b'SQA_FIXTURE_FAILURE',b'NoClassDefFoundError',b'LinkageError')),'Environment failure mislabeled')
        generation=row.get('generation',{});usage=generation.get('usage',{});quota=generation.get('model_quota',{})
        if generation:
            response=read_json(GEN/approach/'response.json')['evidence']['body']
            require(normalize_usage(response)==usage and normalize_quota(response)==quota,'Provider usage/quota differs')
            require(quota['daily_usage_tokens']+quota['daily_remaining_tokens']==quota['daily_quota_tokens'],'Quota totals differ')
            if approach=='kku-claude':
                require(response['model']=='anthropic/claude-sonnet-5' and response['usage']['output_tokens_details']['thinking_tokens']==0,'Sonnet contract differs')
                text=''.join(b['text'] for b in response['content'])
            else:text=response['choices'][0]['message']['content']
            require(text.encode()==(GEN/approach/'raw-response.txt').read_bytes(),'Provider text changed')
        entry={'project':row['project'],'bug_id':row['bug_id'],'approach':approach,'condition':result['condition'],
            'raw_status':row['status'],'test_count':declared,
            'fixed_first_failed':row.get('stages',{}).get('fixed_first',{}).get('counts',{}).get('failed'),
            'fixed_second_failed':row.get('stages',{}).get('fixed_second',{}).get('counts',{}).get('failed'),
            'buggy_failed':row.get('stages',{}).get('buggy',{}).get('counts',{}).get('failed'),
            'raw_native_fault_flag':row.get('fault_detected'),
            'fault_interpretation':'native_assertion_failure_pending_semantic_review' if row.get('fault_detected') else 'no_fault_observed' if coverage else 'unavailable_invalid_suite',
            'target_class_covered_lines':coverage.get('covered_lines'),'target_class_instrumented_lines':coverage.get('instrumented_lines'),
            'target_class_line_rate':coverage.get('line_rate'),'target_class_branch_rate':coverage.get('branch_rate'),
            'prompt_tokens':usage.get('prompt_tokens'),'completion_tokens':usage.get('completion_tokens'),'provider_total_tokens':usage.get('total_tokens'),
            'target_checks_available':row.get('target_checks_per_stage') is not None,'primary_result':False,'full_defects4j_evaluation':False,
            'receipt_path':(folder/'receipt.json').relative_to(ROOT).as_posix(),'receipt_sha256':sha256(folder/'receipt.json')}
        fresh.append(entry);table.append(entry)
    require(len(table)==16 and len({(r['project'],r['bug_id']) for r in table})==3,'Unique-bug/outcome accounting differs')
    output.mkdir(parents=True);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    buffer=io.StringIO(newline='');writer=csv.DictWriter(buffer,fieldnames=list(table[0]),lineterminator='\n')
    writer.writeheader();writer.writerows(table);(output/'results.csv').write_text(buffer.getvalue(),encoding='utf-8',newline='\n')
    write_json(output/'results.json',{'records':table,'conditions_must_not_be_pooled_as_repeats':True,'unavailable_values_are_not_zero':True})
    receipt={'status':'next_ready_bug_four_native_outcomes_audited','new_project':'Jsoup','new_bug':1,
        'unique_bugs':3,'condition_bug_approach_outcomes':16,'new_records':fresh,'prior_audit_preserved':True,
        'fully_native_measured_four_approach_conditions':1+int(all(r['target_class_line_rate'] is not None for r in fresh)),
        'new_call_count':4,'new_generation_call_count':2,'cumulative_billable_requests':15,'cumulative_generation_requests':8,
        'native_failed_instrumentation_attempt_retained':True,'same_AI_responses_used_without_resend':True,
        'packet_manifests':packets,'entries_checked':sum(p['entries'] for p in packets.values()),
        'primary_results_added':0,'full_defects4j_evaluations':0,'primary_gate_a_passed':False,
        'shared_checkpoint_pins_unchanged':len(pins),'shared_checkpoint_excluded_identities':excluded,
        'limitations':['Native Java17/release7/UTC; accepted Linux Java11 full Defects4J replay pending',
            'No repaired assertions, suite pruning, feedback, retries, or pooling of distinct conditions',
            'Input domain equivalence not approved; class coverage is not whole-project coverage',
            'Raw buggy assertion failures require semantic review before scientific fault acceptance',
            'V13 peer candidate integration is outside this frozen v12 result condition; final 40-pair reserve is still pending'],
        'completed_at_utc':utc_now()}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',required=True,type=Path)
    r=run(parser.parse_args().output);print(json.dumps({'status':r['status'],'outcomes':r['condition_bug_approach_outcomes'],'checked':r['entries_checked']}))
