"""Seal Gson/Compress actual outcomes, including invalids and received Beam scope."""
import argparse
import csv
import io
import json
from pathlib import Path
import re
import tarfile

from .common import ROOT,read_json,write_json,sha256
from .audit_ready_development_results import audit_packet
from .evaluate_csv_development import counts,coverage_xml,PROJECTS
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import require,digest
from .start_csv_development import AOM,PAIR
from .verify_chronology_development import checkpoint
from .kku_client import utc_now,normalize_usage,normalize_quota

BASE=ROOT/'output/api854-20261004'


def run(output):
    output=Path(output).resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'Fresh contained audit output required')
    pins,_=checkpoint();aom=BatchedObjects(AOM);packets={}
    names=['champ-ready-results-audit-v2','champ-gson-development-generation-v1','champ-gson-native-measurement-v1',
        'champ-compress-development-generation-v1','champ-compress-native-measurement-v1','champ-compress-native-measurement-v2']
    names+=['champ-beam-ready-results-review-v'+str(i) for i in range(1,6)]
    for name in names:packets[name]=audit_packet(BASE/name)
    table=list(read_json(BASE/'champ-ready-results-audit-v2/results.json')['records']);fresh=[]
    for project,version,account in [('Gson',1,'a04'),('Compress',2,'a05')]:
        generation=BASE/('champ-'+project.lower()+'-development-generation-v1')
        native=BASE/('champ-'+project.lower()+'-native-measurement-v'+str(version))
        plan=read_json(generation/'preexecution-plan.json');seal=read_json(native/'preexecution-seal.json')
        require(plan['account_alias']==account and plan['condition']==PROJECTS[project][-1] and plan['shared_checkpoint_pins']==pins,
                'New bug allocation/condition/checkpoint differs')
        require(plan['native_buggy_source_mode']=='fixed_git_plus_official_isolated_bug_patch','Benchmark source mode differs')
        require(read_json(generation/'receipt.json')['requests_attempted']==4,'Actual generation/calibration request accounting differs')
        prep='output/api854-20261003/prepare-v12-graphics-development-v1/'+project+'-1'
        missing=[prep+'/'+n for n in plan['received_inputs_sha256'] if prep+'/'+n not in aom.cache]
        if missing:aom.preload(missing)
        for n,h in plan['received_inputs_sha256'].items():
            require((generation/'received'/n).read_bytes()==aom.blob(prep+'/'+n) and sha256(generation/'received'/n)==h,'Original v12 inputs changed')
        require(seal['aom_commit']==AOM and seal['runtime_source_sha256']==plan['source_v12_runtime_sha256'],'Native runtime pin differs')
        derivation=read_json(native/'isolated-bug-reference/derivation.json')
        preflight=read_json(generation/'native-preflight/isolated-bug-reference/derivation.json')
        require(derivation['expected_isolated_buggy_source_sha256']==seal['production_source_sha256']['buggy']['sources']
                ==preflight['expected_isolated_buggy_source_sha256'],'Exact isolated benchmark source hashes differ')
        result=read_json(native/'receipt.json');require(len(result['records'])==4 and result['primary_results_added']==0,'Actual outcome scope differs')
        for row in result['records']:
            approach=row['approach'];folder=native/approach;declared=row.get('declared_test_count')
            for stage,record in row.get('stages',{}).items():
                require(counts((folder/(stage+'.stdout.log')).read_bytes())==record['counts'],'Raw actual JUnit counts differ')
                if record.get('target_counts'):require(read_json(folder/(stage+'.target-counts.json'))==record['target_counts'],'Raw target counts differ')
            if declared:
                source={p.relative_to(folder/'sources').as_posix():p.read_bytes() for p in (folder/'sources').rglob('*.java')}
                require({n:digest(raw) for n,raw in source.items()}==row['source_sha256'] and 0<declared<=30,'Suite bytes/cap differ')
                with tarfile.open(folder/'packaged-suite/suite.tar.bz2') as archive:
                    files=[m for m in archive if m.isfile()]
                    require({m.name for m in files}==set(source) and all(archive.extractfile(m).read()==source[m.name] for m in files),'Suite archive repaired')
                if approach.startswith('kku-'):
                    blocks=re.findall(r'^```(?:java)?\s*\n(.*?)^```\s*$',(generation/approach/'raw-response.txt').read_text(encoding='utf-8'),re.MULTILINE|re.DOTALL)
                    require({b.encode() for b in blocks}==set(source.values()),'AI source repaired or pruned')
                else:require((folder/'algorithm-generation/GeneratedStudyTest.java').read_bytes()==source['GeneratedStudyTest.java'],'Algorithm Java repaired')
            cov=row.get('coverage',{});usage=row.get('generation',{}).get('usage',{});quota=row.get('generation',{}).get('model_quota',{})
            if cov:
                require(coverage_xml(folder/'coverage-report/coverage.xml',PROJECTS[project][2])==cov,'Exact class coverage differs')
                require(all(row['stages'][s]['counts']=={'executed':declared,'skipped':0,'failed':0} for s in ('fixed_first','fixed_second','coverage')),'Accepted fixed results differ')
            if usage:
                body=read_json(generation/approach/'response.json')['evidence']['body']
                require(normalize_usage(body)==usage and normalize_quota(body)==quota,'Provider counters changed')
            status=row['status']
            interpretation='native_close_missing_finish_failure_pending_full_D4J' if row.get('fault_detected') else 'unavailable_invalid_suite'
            entry={'project':project,'bug_id':1,'approach':approach,'condition':result['condition'],'raw_status':status,
                'test_count':declared,'fixed_first_failed':row.get('stages',{}).get('fixed_first',{}).get('counts',{}).get('failed'),
                'fixed_second_failed':row.get('stages',{}).get('fixed_second',{}).get('counts',{}).get('failed'),
                'buggy_failed':row.get('stages',{}).get('buggy',{}).get('counts',{}).get('failed'),'raw_native_fault_flag':row.get('fault_detected'),
                'fault_interpretation':interpretation,'target_class_covered_lines':cov.get('covered_lines'),'target_class_instrumented_lines':cov.get('instrumented_lines'),
                'target_class_line_rate':cov.get('line_rate'),'target_class_branch_rate':cov.get('branch_rate'),
                'prompt_tokens':usage.get('prompt_tokens'),'completion_tokens':usage.get('completion_tokens'),'provider_total_tokens':usage.get('total_tokens'),
                'target_checks_available':row.get('target_checks_per_stage') is not None,'primary_result':False,'full_defects4j_evaluation':False,
                'receipt_path':(folder/'receipt.json').relative_to(ROOT).as_posix(),'receipt_sha256':sha256(folder/'receipt.json')}
            fresh.append(entry);table.append(entry)
    require(len(table)==24 and len({(r['project'],r['bug_id']) for r in table})==5,'Unique-bug/outcome accounting differs')
    beam=read_json(BASE/'champ-beam-ready-results-review-v5/receipt.json')
    require(beam['accepted_latest_condition_full_evaluations']==6 and beam['actual_junit_XML_reports_checked']==24,'Received Beam acceptance differs')
    failed=BASE/'champ-compress-native-measurement-v1';successful=BASE/'champ-compress-native-measurement-v2'
    require((failed/'cmaes/algorithm-generation/GeneratedStudyTest.java').read_bytes()==(successful/'cmaes/algorithm-generation/GeneratedStudyTest.java').read_bytes(),
            'Suite bytes changed after dependency-only coverage classpath repair')
    output.mkdir(parents=True);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    buf=io.StringIO(newline='');writer=csv.DictWriter(buf,fieldnames=list(table[0]),lineterminator='\n');writer.writeheader();writer.writerows(table)
    (output/'native-results.csv').write_text(buf.getvalue(),encoding='utf-8',newline='\n')
    write_json(output/'native-results.json',{'records':table,'distinct_conditions_must_not_be_pooled_as_repeats':True,'missing_values_are_not_zero':True})
    receipt={'status':'beam_full_development_results_accepted_and_next_native_batches_audited','native_unique_bugs':5,'native_condition_bug_approach_outcomes':24,
        'fully_native_measured_four_approach_conditions':1,'new_native_records':fresh,'beam_latest_condition_full_evaluations_accepted':6,
        'beam_total_including_baseline_full_evaluations_reported':8,'unique_bugs_with_four_valid_full_D4J_methods':1,
        'new_billable_requests':8,'cumulative_billable_requests':23,'cumulative_generation_requests':12,
        'new_ready_full_D4J_packet':'champ-compress-native-measurement-v2','new_ready_approaches':['cmaes','fscs-art'],
        'new_nonready_bug':'Gson-1','gson_cause':'Fixed projection serializes ParameterizedTypeImpl object identity; prospective structural Type oracle required',
        'provider_total_tokens_missing_stays_null':True,'no_feedback_or_assertion_repairs':True,'packet_manifests':packets,
        'manifest_entries_verified':sum(p['entries'] for p in packets.values()),'primary_results_added':0,'gate_a_approved':False,
        'shared_checkpoint_pins_unchanged':len(pins),'completed_at_utc':utc_now()}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',required=True,type=Path)
    r=run(parser.parse_args().output);print(json.dumps({'status':r['status'],'entries':r['manifest_entries_verified'],'outcomes':r['native_condition_bug_approach_outcomes']}))
