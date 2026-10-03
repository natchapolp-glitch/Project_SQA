"""Independently accept pinned Beam Csv/Jsoup development observations.

Checks raw XML counters, unchanged archives, coverage, fixed+official-patch
source derivation, framework restoration and invalids; does not rerun tests/API.
"""
import argparse
import json
from pathlib import Path
import tempfile
import xml.etree.ElementTree as ET

from .common import ROOT,read_json,sha256,write_json
from .review_v10_readiness import BatchedObjects
from .review_joint_recipe_intake import require,digest
from .verify_chronology_development import checkpoint
from .benchmark_sources import derive
from .evaluate_csv_development import PROJECTS
from .kku_client import utc_now

BEAM='2c0e92fc'
BASE='output/api854-20261004/'
DELIVERY=BASE+'beam-ready-results-delivery-v1'
JSOUP=BASE+'beam-jsoup-results-return-v1'
MEASUREMENTS={'Csv':('beam-champ-csv-messages-d4j-v3','champ-csv-messages-native-measurement-v1'),
              'Jsoup':('beam-champ-jsoup-d4j-v1','champ-jsoup-native-measurement-v2')}


def run(output,defects4j):
    output=Path(output).resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained evidence required')
    pins,_=checkpoint();beam=BatchedObjects(BEAM)
    full_commit=beam.revision if hasattr(beam,'revision') else BEAM
    deliveries={p:beam.document(p+'/receipt.json') for p in (DELIVERY,JSOUP)}
    manifests={}
    for root,r in deliveries.items():
        manifests[root]=beam.document(root+'/checksums.json')
        for info in r['packet_manifests'].values():
            path=info['path'];require(digest(beam.blob(path))==info['sha256'],'Published manifest binding differs')
            manifests[path.rsplit('/',1)[0]]=beam.document(path)
    wanted={root+'/'+p for root,m in manifests.items() for p in m}
    missing=sorted(wanted-set(beam.cache))
    if missing:beam.preload(missing)
    for root,m in manifests.items():
        for relative,h in m.items():require(digest(beam.blob(root+'/'+relative))==h,'Sealed Beam bytes changed')
    require(deliveries[JSOUP]['combined_full_defects4j_completed']==8 and deliveries[JSOUP]['combined_csv_jsoup_unique_bugs']==2,
            'Beam execution accounting differs')
    output.mkdir(parents=True);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    rows=[];source_reviews={};xml_reports=0
    for project,(packet,native_name) in MEASUREMENTS.items():
        root=BASE+packet;native=ROOT/BASE/native_name;seal=beam.document(root+'/preexecution-seal.json')
        original=read_json(native/'preexecution-seal.json')
        require(seal['worker_id']=='beam-pc1' and seal['cpu_slots']==1 and seal['aom_commit']==original['aom_commit'],'Host/frozen source differs')
        runtime=seal['frozen_runtime_source_sha256']
        require(runtime==original['runtime_source_sha256'] and len(runtime)==41,'Received runtime differs')
        for approach,data in seal['suites'].items():
            require(data['suite_sha256']==sha256(native/approach/'packaged-suite/suite.tar.bz2'),'Archive differs from original Champ suite')
            require(data['source_sha256']==read_json(native/approach/'receipt.json')['source_sha256'],'Original Java binding differs')
        proof=beam.document(root+'/isolated-bug-reference/derivation.json')
        repo,source_root,_,fixed,buggy,_=PROJECTS[project]
        with tempfile.TemporaryDirectory(prefix='.champ-benchmark-review-',dir=ROOT/'output') as folder:
            scratch=Path(folder).resolve();require(scratch.is_relative_to(ROOT/'output'),'Unsafe temporary cleanup')
            check=derive(Path(defects4j),project,1,repo,fixed,buggy,scratch,source_root,output/(project.lower()+'-source-derivation'))
            require(check['official_patch_sha256']==proof['official_patch_sha256'],'Peer official patch differs from local verified framework')
            require(check['fixed_source_sha256']==original['production_source_sha256']['fixed']['sources'],'Exact fixed source inventory differs')
            require(check['expected_isolated_buggy_source_sha256']==proof['expected_isolated_buggy_source_sha256'],
                    'Independent official-patch source derivation differs')
        source_reviews[project]={'derivation_sha256':sha256(output/(project.lower()+'-source-derivation/derivation.json')),
            'peer_derivation_sha256':digest(beam.blob(root+'/isolated-bug-reference/derivation.json')),
            'native_parent_source_differs':proof['native_and_isolated_buggy_sources_identical'] is False,
            'official_patch_git_blob_verified':True,'actual_production_source_replacement':False}
        restoration=beam.document(root+'/count-observer-framework/restoration.json')
        require(restoration['restored_exact_bytes'] and restoration['observed_bytes_unchanged'],'Framework was not restored')
        result=beam.document(root+'/results.json')
        for row in result:
            approach=row['approach'];record_root=root+'/'+approach+'/evaluation'
            if row['status']!='complete':
                require(project=='Jsoup' and approach.startswith('kku-') and row['full_defects4j_status']=='not_replayed_by_design'
                        and row['fault_detected'] is None and row['coverage'] is None,'Invalid native AI outcome was promoted')
                require(row['suite_sha256']==sha256(native/approach/'packaged-suite/suite.tar.bz2'),'Invalid AI archive changed')
                continue
            require(digest(beam.blob(record_root+'/record.json'))==row['record_sha256'],'Canonical record binding differs')
            canonical=beam.document(record_root+'/record.json')
            require(canonical['status']=='complete' and canonical['fault_detected']==row['fault_detected'],'Canonical measurement differs')
            stage_counts=row.get('stages',row.get('counts'))
            require(set(stage_counts)=={'fixed-1','fixed-2','buggy','coverage'},'Actual stage inventory differs')
            failures={}
            for stage,record in stage_counts.items():
                require(beam.document(record_root+'/'+stage+'/actual-junit-counts.json')==record,'Actual counters differ')
                totals={'tests':0,'skipped':0,'failures':0,'errors':0};identities=[]
                for report in record['suites']:
                    path=record_root+'/'+stage+'/junit-reports/TEST-'+report['class']+'.xml'
                    raw=beam.blob(path);require(digest(raw)==report['report_sha256'],'Raw XML hash differs')
                    tree=ET.fromstring(raw)
                    for field in totals:totals[field]+=int(tree.get(field,'0'))
                    identities.extend((case.get('classname'),case.get('name')) for case in tree.findall('testcase'))
                    xml_reports+=1
                require(totals=={'tests':row['test_count'],'skipped':0,'failures':record['failed'],'errors':0},'Raw XML/count mismatch')
                require(sorted(map(tuple,record['test_identities']))==sorted(identities),'Raw executed test identities differ')
                require(record['executed']==row['test_count'] and record['skipped']==0 and record['errors']==0,'Actual counts differ')
                if not approach.startswith('kku-'):require(record['target_checks']==30,'Algorithm target counts differ')
                else:require(record['target_checks'] is None,'AI target counts were inferred')
                failures[stage]=record['failed']
            require(all(failures[s]==0 for s in ('fixed-1','fixed-2','coverage')),'Accepted fixed suite has failures')
            require(row['fault_detected']==(failures['buggy']>0),'Raw fault flag differs')
            coverage=ET.fromstring(beam.blob(record_root+'/coverage/coverage.xml'))
            target=[c for c in coverage.findall('.//class') if c.get('name')==PROJECTS[project][2]]
            require(len(target)==1,'Exact coverage class missing');lines=target[0].findall('./lines/line')
            require(row['coverage']['line_total']==len(lines) and row['coverage']['line_covered']==sum(int(l.get('hits'))>0 for l in lines),
                    'Raw target-class line counts differ')
            require(abs(float(target[0].get('branch-rate'))-row['coverage']['branch_covered']/row['coverage']['branch_total'])<1e-10,
                    'Raw target-class branch coverage differs')
            if project=='Csv' and approach.startswith('kku-'):
                failure=beam.blob(record_root+'/buggy/failing_tests')
                method='lineNumberDoesNotDoubleCountCRLF' if approach=='kku-claude' else 'testCarriageReturnLineNumber'
                require(method.encode() in failure and b'AssertionError' in failure,'Expected CR failure missing')
            rows.append({'project':project,'bug_id':1,'approach':approach,'test_count':row['test_count'],
                'generation_condition':seal['generation_condition'],'execution_condition':seal['execution_condition'],
                'worker_id':'beam-pc1','fault_detected':row['fault_detected'],'coverage':row['coverage'],
                'actual_stage_counts':{s:{k:v[k] for k in ('executed','skipped','failed','errors','target_checks')} for s,v in stage_counts.items()},
                'suite_sha256':row['suite_sha256'],'canonical_record_sha256':row['record_sha256'],
                'peer_record_path':record_root+'/record.json','primary_result':False})
        (output/(project.lower()+'-received-results.json')).write_bytes(beam.blob(root+'/results.json'))
    require(len(rows)==6,'Latest condition completed-suite count differs')
    receipt={'status':'beam_csv_four_and_jsoup_two_full_development_measurements_accepted','beam_commit':BEAM,
        'accepted_latest_condition_full_evaluations':6,'beam_total_including_baseline_full_evaluations':8,
        'unique_bugs_with_valid_full_four_approach_measurements':1,'unique_bugs_in_this_peer_full_measurement_receipt':2,
        'native_invalid_jsoup_ai_outcomes_retained':2,'primary_results_added':0,'gate_a_approved':False,
        'packet_manifests':{root:{'entries':len(m),'sha256':digest(beam.blob(root+'/checksums.json'))} for root,m in manifests.items()},
        'verified_manifest_entries':sum(len(m) for m in manifests.values()),'actual_junit_XML_reports_checked':xml_reports,
        'source_reviews':source_reviews,'records':rows,'cli_quarantine_retained':True,'kku_requests':0,'queue_mutations':0,
        'shared_checkpoint_pins_unchanged':len(pins),'completed_at_utc':utc_now(),
        'limitations':['Native parent vs benchmark isolated bug sources are separate conditions',
            'No new unique bugs or independent generation repetitions inferred from host replays',
            'Class coverage only; AI/algorithm input-domain equivalence and full854/GateA not approved']}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',required=True,type=Path)
    parser.add_argument('--defects4j',required=True,type=Path);args=parser.parse_args()
    new=not args.output.resolve().exists()
    try:r=run(args.output,args.defects4j)
    except Exception as error:
        out=args.output.resolve()
        if new and out.is_relative_to(ROOT/'output') and out.exists():
            write_json(out/'failed-attempt.json',{'error':str(error),'error_type':type(error).__name__,'primary_results_added':0})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':r['status'],'entries':r['verified_manifest_entries'],'XML_reports':r['actual_junit_XML_reports_checked']}))
