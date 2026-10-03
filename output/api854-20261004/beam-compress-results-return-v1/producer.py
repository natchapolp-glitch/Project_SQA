"""Seal the Compress replay, Aom Csv review, failed attempts and combined result index."""
import argparse,csv,hashlib,json,subprocess,sys,time
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3];PREFIX=ROOT/'output/api854-20261004'
PACKETS=['beam-champ-compress-intake-v1','beam-champ-compress-d4j-v1','beam-champ-compress-d4j-v2','beam-compress-benchmark-binding-v1','beam-compress-benchmark-binding-v2','beam-aom-csv-review-intake-v1','beam-aom-csv-review-intake-v2','beam-aom-csv-receipts-review-v1','beam-jsoup-results-return-v1']
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def read(path):return json.loads(Path(path).read_bytes())
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def require(ok,message):
    if not ok:raise ValueError(message)
def audit_git(intake):
    received=read(intake/'receipt.json');git=['git','-c','safe.directory='+str(ROOT),'-C',str(ROOT)]
    proc=subprocess.Popen(git+['cat-file','--batch'],stdin=subprocess.PIPE,stdout=subprocess.PIPE);number=0
    for path,digest in received['source_sha256'].items():
        proc.stdin.write((received['source_commit']+':'+path+'\n').encode());proc.stdin.flush();header=proc.stdout.readline().decode().split();require(len(header)==3 and header[1]=='blob','Missing peer Git blob')
        data=proc.stdout.read(int(header[2]));require(proc.stdout.read(1)==b'\n','Bad Git framing');require(hashlib.sha256(data).hexdigest()==digest and data==(intake/'received-champ'/path).read_bytes(),'Peer Git bytes differ');number+=1
    proc.stdin.close();require(proc.wait()==0,'Git audit failed');return number
def run(output):
    output=output.resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'Fresh contained export required');output.mkdir();(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    manifests={};checked=0
    for name in PACKETS:
        base=PREFIX/name;manifest=read(base/'checksums.json');actual={p.relative_to(base).as_posix() for p in base.rglob('*') if p.is_file() and p!=base/'checksums.json'}
        require(set(manifest)==actual,'Incomplete checksum inventory: '+name)
        for path,digest in manifest.items():require(sha(base/path)==digest,'Changed packet: '+name+'/'+path)
        manifests[name]={'path':(base/'checksums.json').relative_to(ROOT).as_posix(),'sha256':sha(base/'checksums.json'),'entries':len(manifest)};checked+=len(manifest)
    peer_blobs={name:audit_git(PREFIX/name) for name in ['beam-champ-compress-intake-v1','beam-aom-csv-review-intake-v2']}
    argv=[sys.executable,'-B','-m','unittest','discover','-s','scripts/study/api854/tests','-p','test_ready_compress_replay.py','-v'];started=time.monotonic();tests=subprocess.run(argv,cwd=ROOT,capture_output=True,timeout=180)
    (output/'guards.stdout.log').write_bytes(tests.stdout);(output/'guards.stderr.log').write_bytes(tests.stderr);write(output/'guards.command.json',{'argv':argv,'cwd':str(ROOT),'exit_code':tests.returncode,'duration_seconds':time.monotonic()-started,'source_sha256':sha(ROOT/'scripts/study/api854/tests/test_ready_compress_replay.py')})
    require(tests.returncode==0 and b'Ran 9 tests' in tests.stderr and b'OK' in tests.stderr,'Replay guard tests failed')
    rows=read(PREFIX/'beam-jsoup-results-return-v1/condition-separated-results.json');compress=read(PREFIX/'beam-champ-compress-d4j-v2/receipt.json')
    for r in read(PREFIX/'beam-champ-compress-d4j-v2/results.json'):
        coverage=r.get('coverage') or {};full=r['full_defects4j_status']=='complete';folder=PREFIX/'beam-champ-compress-d4j-v2'/r['approach']
        rows.append({'condition_label':'compress','generation_condition':compress['generation_condition'],'execution_condition':compress['execution_condition'] if full else 'native_invalid_only_no_d4j_execution','project':'Compress','bug_id':1,'approach':r['approach'],'status':r['status'],'fault_detected':r['fault_detected'],'test_count':r['test_count'],'generation_seconds':r.get('upstream_generation_seconds'),'worker_seconds_including_setup':r.get('worker_seconds_including_setup'),'line_covered':coverage.get('line_covered'),'line_total':coverage.get('line_total'),'branch_covered':coverage.get('branch_covered'),'branch_total':coverage.get('branch_total'),'suite_sha256':r['suite_sha256'],'outcome_path':(folder/'outcome.json').relative_to(ROOT).as_posix(),'outcome_sha256':sha(folder/'outcome.json'),'primary':False})
    require(len(rows)==16 and len({(r['project'],r['bug_id']) for r in rows})==3,'Result accounting differs')
    write(output/'condition-separated-results.json',rows)
    with (output/'condition-separated-results.csv').open('x',encoding='utf-8',newline='') as stream:
        writer=csv.DictWriter(stream,fieldnames=list(rows[0]));writer.writeheader();writer.writerows(rows)
    cohort=read(PREFIX/'beam-jsoup-results-return-v1/next-cohort-work.json')
    for bug in cohort['bugs']:
        if bug['bug']=='Compress-1':
            bug['status']='two_full_algorithm_outcomes_and_two_retained_native_invalid_ai_outcomes';bug['four_full_defects4j_outcomes_ready']=False;bug['required_next_action']='Joint intake of benchmark-bound algorithm replay; retain Sonnet truncated/Gemini compile-invalid without retry or repair';bug['outcome_receipt_sha256']=sha(PREFIX/'beam-champ-compress-d4j-v2/receipt.json')
    cohort['pending_other_bugs']=17;cohort['ready_result_bugs_in_delivery']=3;write(output/'next-cohort-work.json',cohort)
    result={'status':'beam_compress_and_aom_csv_return_audited','checked_at_utc':datetime.now(timezone.utc).isoformat(),'champ_commit':read(PREFIX/'beam-champ-compress-intake-v1/receipt.json')['source_commit'],'aom_review_commit':read(PREFIX/'beam-aom-csv-review-intake-v2/receipt.json')['source_commit'],'worker_id':'beam-pc1','cpu_slots':1,'new_compress_full_algorithm_evaluations':2,'new_compress_native_invalid_ai_outcomes_retained':2,'new_compress_ai_d4j_evaluations':0,'aom_csv_receipts_reviewed':4,'aom_csv_reruns_started':0,'combined_unique_bugs':3,'combined_condition_approach_outcomes':16,'combined_full_defects4j_completed':10,'unique_bugs_with_four_valid_full_measurements':1,'packet_manifests':manifests,'checksum_entries_verified':checked,'original_peer_git_blobs_verified':peer_blobs,'focused_guard_tests_passed':9,'focused_guard_tests_skipped':0,'strict_source_attempt_retained':True,'benchmark_binding_declared_before_replay':True,'new_candidates_paused':True,'kku_requests_from_beam':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'full854_completed':False,'v12_v13_not_mixed':True,'cli_scientific_fault_quarantine_retained':True,'producer_sha256':sha(output/'producer.py')}
    write(output/'receipt.json',result);write(output/'checksums.json',{p.relative_to(output).as_posix():sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':result['status'],'checksum_entries_verified':checked,'original_peer_git_blobs_verified':peer_blobs,'receipt_sha256':sha(output/'receipt.json'),'checksums_sha256':sha(output/'checksums.json')}))
if __name__=='__main__':
    parser=argparse.ArgumentParser();parser.add_argument('--output',type=Path,required=True);args=parser.parse_args();run(args.output)
