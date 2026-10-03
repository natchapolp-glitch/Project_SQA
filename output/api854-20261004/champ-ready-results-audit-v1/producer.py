"""Seal the actual ready-bug results with conditions, invalids and oracle decisions.

Audits raw logs/archives/coverage; no execution, provider calls, or result repairs.
"""
import argparse
import csv
from datetime import datetime,timezone
import io
import json
from pathlib import Path
import re
import tarfile

from .common import ROOT,read_json,write_json,sha256
from .evaluate_csv_development import counts,coverage_xml
from .review_joint_recipe_intake import require,digest
from .review_v10_readiness import BatchedObjects
from .verify_chronology_development import checkpoint

BASE=ROOT/'output/api854-20261004'
PACKETS=('champ-cli-development-generation-v1','champ-cli-development-generation-v2',
 'champ-cli-native-measurement-v1','champ-cli-order-oracle-audit-v1','champ-cli-order-oracle-audit-v2',
 'champ-provider-messages-calibration-v1','champ-csv-messages-generation-v1','champ-csv-messages-generation-v2',
 'champ-csv-messages-native-measurement-v1')
MEASUREMENTS=(('champ-csv-native-measurement-v4','champ-csv-development-generation-v1'),
 ('champ-cli-native-measurement-v1','champ-cli-development-generation-v2'),
 ('champ-csv-messages-native-measurement-v1','champ-csv-messages-generation-v2'))
BEAM='92a3ee1bbef810aee8b185bf94cce0b1f8718a8b'
BEAM_PACKET='output/api854-20261004/beam-v12-received-review-v2'


def audit_packet(packet):
    manifest=read_json(packet/'checksums.json')
    require({p.relative_to(packet).as_posix() for p in packet.rglob('*') if p.is_file()}==set(manifest)|{'checksums.json'},'Packet inventory changed')
    for relative,h in manifest.items():
        path=(packet/relative).resolve();require(path.is_relative_to(packet) and sha256(path)==h,'Packet bytes changed')
    return {'path':(packet/'checksums.json').relative_to(ROOT).as_posix(),'sha256':sha256(packet/'checksums.json'),'entries':len(manifest)}


def run(output):
    output=Path(output).resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    pins,excluded_identities=checkpoint()
    packets={name:audit_packet(BASE/name) for name in PACKETS};table=[]
    order=read_json(BASE/'champ-cli-order-oracle-audit-v2/receipt.json')
    require(order['status']=='oracle_order_false_positive_quarantined' and order['confirmed_semantic_fault'] is False,'Cli semantic decision differs')
    for measurement,generation in MEASUREMENTS:
        native=BASE/measurement;generated=BASE/generation
        audit_packet(native);audit_packet(generated)
        result=read_json(native/'receipt.json');require(len(result['records'])==4 and result['primary_results_added']==0,'Incomplete or promoted results')
        for row in result['records']:
            folder=native/row['approach'];declared=row.get('declared_test_count')
            for stage,s in row.get('stages',{}).items():
                require(counts((folder/(stage+'.stdout.log')).read_bytes())==s['counts'],'Actual counts differ')
                if s.get('target_counts'):
                    require(read_json(folder/(stage+'.target-counts.json'))==s['target_counts'],'Target count evidence differs')
                    require(s['target_counts']['target_checks']==30,'Algorithm target invocation count differs')
            if declared:
                require(0<declared<=30,'Invalid suite cap')
                source_bytes={p.relative_to(folder/'sources').as_posix():p.read_bytes() for p in (folder/'sources').rglob('*.java')}
                require({n:digest(raw) for n,raw in source_bytes.items()}==row['source_sha256'],'Test bytes changed')
                with tarfile.open(folder/'packaged-suite/suite.tar.bz2') as bundle:
                    members=[m for m in bundle if m.isfile()]
                    require({m.name for m in members}==set(source_bytes),'Archive members differ')
                    require(all(bundle.extractfile(m).read()==source_bytes[m.name] for m in members),'Archive repaired test bytes')
                if row['approach'].startswith('kku-'):
                    raw=(generated/row['approach']/'raw-response.txt').read_text(encoding='utf-8')
                    blocks=re.findall(r'^```(?:java)?\s*\n(.*?)^```\s*$',raw,re.MULTILINE|re.DOTALL)
                    require({b.encode('utf-8') for b in blocks}==set(source_bytes.values()),'AI Java extraction changed')
                else:
                    require((folder/'algorithm-generation/GeneratedStudyTest.java').read_bytes()==source_bytes['GeneratedStudyTest.java'],'Algorithm test repaired')
            coverage=row.get('coverage',{})
            if coverage:
                target='org.apache.commons.csv.ExtendedBufferedReader' if row['project']=='Csv' else 'org.apache.commons.cli.CommandLine'
                require(coverage_xml(folder/'coverage-report/coverage.xml',target)==coverage,'Measured coverage differs')
                require(all(row['stages'][stage]['counts']=={'executed':declared,'skipped':0,'failed':0}
                            for stage in ('fixed_first','fixed_second','coverage')),'Validated fixed stages differ')
                require(not any(marker in (folder/'buggy.stdout.log').read_bytes() for marker in
                    (b'SQA_HARNESS',b'SQA_FIXTURE_FAILURE',b'NoClassDefFoundError',b'LinkageError')),'Environment fault mislabeled')
            usage=row.get('generation',{}).get('usage',{});quota=row.get('generation',{}).get('model_quota',{})
            if usage:
                if usage['total_tokens'] is not None:require(usage['prompt_tokens']+usage['completion_tokens']==usage['total_tokens'],'Usage inconsistent')
                require(quota['daily_usage_tokens']+quota['daily_remaining_tokens']==quota['daily_quota_tokens'],'Quota inconsistent')
            false_positive=row['project']=='Cli' and row['approach']=='fscs-art'
            table.append({'project':row['project'],'bug_id':row['bug_id'],'approach':row['approach'],'condition':result['condition'],
                'raw_status':row['status'],'test_count':declared,
                'fixed_first_failed':row.get('stages',{}).get('fixed_first',{}).get('counts',{}).get('failed'),
                'fixed_second_failed':row.get('stages',{}).get('fixed_second',{}).get('counts',{}).get('failed'),
                'buggy_failed':row.get('stages',{}).get('buggy',{}).get('counts',{}).get('failed'),
                'raw_native_fault_flag':row.get('fault_detected'),
                'fault_interpretation':'oracle_order_false_positive_not_counted' if false_positive else 'native_CR_line_counter_reproduction' if row.get('fault_detected') else 'no_fault_observed' if coverage else 'unavailable_invalid_suite',
                'target_class_covered_lines':coverage.get('covered_lines'),'target_class_instrumented_lines':coverage.get('instrumented_lines'),
                'target_class_line_rate':coverage.get('line_rate'),'target_class_branch_rate':coverage.get('branch_rate'),
                'prompt_tokens':usage.get('prompt_tokens'),'completion_tokens':usage.get('completion_tokens'),'provider_total_tokens':usage.get('total_tokens'),
                'target_checks_available':row.get('target_checks_per_stage') is not None,'primary_result':False,
                'full_defects4j_evaluation':False,'receipt_path':(folder/'receipt.json').relative_to(ROOT).as_posix(),'receipt_sha256':sha256(folder/'receipt.json')})
    require(len(table)==12 and len({(r['project'],r['bug_id']) for r in table})==2,'Outcome accounting differs')
    fresh=[r for r in table if r['condition'].endswith('-v2')]
    require(len(fresh)==4 and all(r['raw_status']=='native_fixed_twice_buggy_coverage_measured' for r in fresh),'Fresh transport condition incomplete')
    require({r['approach']:r['test_count'] for r in fresh}=={'cmaes':30,'fscs-art':30,'kku-claude':21,'kku-gemini':18},'Fresh method counts differ')
    # Confirm reconciliation used the already received Sonnet response without a resend.
    message_gen=BASE/'champ-csv-messages-generation-v2'
    require(read_json(message_gen/'kku-claude/request-not-resent.json')['new_request_sent'] is False,'Sonnet response resent')
    require((message_gen/'kku-claude/response.json').read_bytes()==(BASE/'champ-csv-messages-generation-v1/kku-claude/response.json').read_bytes(),'Received provider response changed')
    body=read_json(message_gen/'kku-claude/response.json')['evidence']['body']
    require(body['usage']['output_tokens_details']['thinking_tokens']==0,'Messages thinking tokens differ')
    # Batch inspect the exact received Beam host scope; no fresh host execution here.
    beam=BatchedObjects(BEAM);manifest=beam.document(BEAM_PACKET+'/checksums.json')
    missing=[BEAM_PACKET+'/'+p for p in manifest if BEAM_PACKET+'/'+p not in beam.cache]
    if missing:beam.preload(missing)
    require(all(digest(beam.blob(BEAM_PACKET+'/'+p))==h for p,h in manifest.items()),'Beam v12 packet changed')
    receipt=beam.document(BEAM_PACKET+'/receipt.json');host=beam.document(BEAM_PACKET+'/host-review-v1/host-receipt.json')
    require(receipt['four_consumer_combinations']==80 and receipt['fresh_tests_passed']==15
            and receipt['fresh_tests_skipped']==0 and not receipt['gate_a_approved'],'Beam scope differs')
    require(host['worker_id']=='beam-pc1' and host['cpu_slots']==1 and host['cpu_lock_exits']==[9,0],'Beam host scope differs')
    csv_seal=read_json(BASE/'champ-csv-messages-native-measurement-v1/preexecution-seal.json')
    require(host['runtime_source_sha256']==csv_seal['runtime_source_sha256'],'Beam/native runtime differs')
    output.mkdir(parents=True);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    (output/'received-beam-host-receipt.json').write_bytes(beam.blob(BEAM_PACKET+'/host-review-v1/host-receipt.json'))
    buffer=io.StringIO(newline='');writer=csv.DictWriter(buffer,fieldnames=list(table[0]),lineterminator='\n')
    writer.writeheader();writer.writerows(table);(output/'results.csv').write_text(buffer.getvalue(),encoding='utf-8',newline='\n')
    write_json(output/'results.json',{'records':table,'conditions_must_not_be_pooled_as_repeats':True,'unavailable_values_are_not_zero':True})
    result={'status':'ready_native_results_audited_with_invalids_and_oracle_quarantine','user_priority_accepted':True,
        'unique_bugs_with_four_baseline_outcomes':2,'condition_bug_approach_outcomes':12,
        'extra_condition_is_same_Csv_bug_not_a_third_bug':True,'fully_native_measured_four_approach_conditions':1,
        'full_defects4j_evaluations':0,'primary_results_added':0,'primary_gate_a_passed':False,'full854_goal_retained':True,
        'packet_manifests':packets,'new_packet_entries_checked':sum(p['entries'] for p in packets.values()),
        'shared_checkpoint_pins_unchanged':len(pins),'unchanged_chronology_exclusion_identities':excluded_identities,
        'earlier_csv_summary_field_note':'shared_checkpoint_audit in summary-v1 contains exclusion identities; checkpoint() also executed actual V9 accounting guard',
        'beam_scoped_v12_receipt_accepted':{'commit':BEAM,'manifest_entries':len(manifest),'receipt_sha256':digest(beam.blob(BEAM_PACKET+'/receipt.json')),
            'manifest_sha256':digest(beam.blob(BEAM_PACKET+'/checksums.json')),'worker_id':'beam-pc1','cpu_slots':1,'scope':'received consumers/components/technical host; no full403 semantic or GateA approval'},
        'cli_fault_quarantined':True,'no_assertion_repairs_or_suite_pruning':True,'messages_existing_response_reconciled_without_resend':True,
        'limitations':['Native Java17/release7/UTC; full Defects4J Java11/Los_Angeles replay pending',
            'AI uses wider legal Reader inputs including CR than bounded algorithm fixtures; domain equivalence not approved',
            'Do not infer overall method superiority or 854-bug throughput from these two bugs',
            'Final 40-pair reserve/limits/reset/expiry not completed; Messages total_tokens remains null when unreported',
            'replay_csv_development_d4j.py is supplied for peer host execution and not executed on Champ Windows'],
        'completed_at_utc':datetime.now(timezone.utc).isoformat()}
    write_json(output/'receipt.json',result)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',required=True,type=Path)
    r=run(parser.parse_args().output);print(json.dumps({'status':r['status'],'outcomes':r['condition_bug_approach_outcomes'],'checked':r['new_packet_entries_checked']}))
