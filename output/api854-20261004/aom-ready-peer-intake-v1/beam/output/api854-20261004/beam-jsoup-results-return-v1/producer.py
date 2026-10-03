"""Hash-audit the next ready-bug batch and extend the condition-separated index."""
import argparse,csv,hashlib,json,subprocess,sys,time
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3];PREFIX=ROOT/'output/api854-20261004'
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def read(path):return json.loads(Path(path).read_bytes())
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def require(ok,message):
    if not ok:raise ValueError(message)
def run(output):
    output=output.resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained export required');output.mkdir(parents=True,exist_ok=False)
    (output/'producer.py').write_bytes(Path(__file__).read_bytes())
    manifests={};checked=0
    for name in ['beam-champ-jsoup-intake-v1','beam-champ-jsoup-d4j-v1','beam-ready-results-delivery-v1']:
        base=PREFIX/name;manifest=read(base/'checksums.json')
        actual={p.relative_to(base).as_posix() for p in base.rglob('*') if p.is_file() and p!=base/'checksums.json'}
        require(set(manifest)==actual,'Missing packet checksum entries: '+name)
        for path,digest in manifest.items():require(sha(base/path)==digest,'Changed packet file')
        manifests[name]={'path':(base/'checksums.json').relative_to(ROOT).as_posix(),'sha256':sha(base/'checksums.json'),'entries':len(manifest)};checked+=len(manifest)
    intake=PREFIX/'beam-champ-jsoup-intake-v1';received=read(intake/'receipt.json')
    git=['git','-c','safe.directory='+str(ROOT),'-C',str(ROOT)];batch=subprocess.Popen(git+['cat-file','--batch'],stdin=subprocess.PIPE,stdout=subprocess.PIPE);blobs=0
    for path,digest in received['source_sha256'].items():
        batch.stdin.write((received['source_commit']+':'+path+'\n').encode());batch.stdin.flush();header=batch.stdout.readline().decode().split();require(len(header)==3 and header[1]=='blob','Missing original peer Git blob')
        data=batch.stdout.read(int(header[2]));require(batch.stdout.read(1)==b'\n','Invalid Git batch framing');require(hashlib.sha256(data).hexdigest()==digest and data==(intake/'received-champ'/path).read_bytes(),'Changed original Git bytes');blobs+=1
    batch.stdin.close();require(batch.wait()==0,'Git object audit failed')
    argv=[sys.executable,'-B','-m','unittest','discover','-s','scripts/study/api854/tests','-p','test_ready_jsoup_replay.py','-v'];started=time.monotonic();tests=subprocess.run(argv,cwd=ROOT,capture_output=True,timeout=180)
    (output/'guards.stdout.log').write_bytes(tests.stdout);(output/'guards.stderr.log').write_bytes(tests.stderr)
    write(output/'guards.command.json',{'argv':argv,'cwd':str(ROOT),'exit_code':tests.returncode,'duration_seconds':time.monotonic()-started,'source_sha256':sha(ROOT/'scripts/study/api854/tests/test_ready_jsoup_replay.py')})
    require(tests.returncode==0 and b'Ran 6 tests' in tests.stderr and b'OK' in tests.stderr,'Jsoup replay guards failed')
    rows=read(PREFIX/'beam-ready-results-delivery-v1/condition-separated-results.json');jsoup=read(PREFIX/'beam-champ-jsoup-d4j-v1/receipt.json')
    for record in read(PREFIX/'beam-champ-jsoup-d4j-v1/results.json'):
        coverage=record.get('coverage') or {}
        rows.append({'condition_label':'jsoup','generation_condition':jsoup['generation_condition'],'execution_condition':jsoup['execution_condition'] if record['full_defects4j_status']=='complete' else 'native_invalid_only_no_d4j_execution','project':'Jsoup','bug_id':1,'approach':record['approach'],'status':record['status'],'fault_detected':record['fault_detected'],'test_count':record['test_count'],'generation_seconds':record.get('upstream_generation_seconds'),'worker_seconds_including_setup':record.get('worker_seconds_including_setup'),'line_covered':coverage.get('line_covered'),'line_total':coverage.get('line_total'),'branch_covered':coverage.get('branch_covered'),'branch_total':coverage.get('branch_total'),'suite_sha256':record['suite_sha256'],'outcome_path':(PREFIX/'beam-champ-jsoup-d4j-v1'/record['approach']/'outcome.json').relative_to(ROOT).as_posix(),'outcome_sha256':sha(PREFIX/'beam-champ-jsoup-d4j-v1'/record['approach']/'outcome.json'),'primary':False})
    require(len(rows)==12 and len({(r['project'],r['bug_id']) for r in rows})==2,'Invalid result/unique bug accounting')
    write(output/'condition-separated-results.json',rows)
    with (output/'condition-separated-results.csv').open('x',encoding='utf-8',newline='') as stream:
        writer=csv.DictWriter(stream,fieldnames=list(rows[0]));writer.writeheader();writer.writerows(rows)
    cohort=read(PREFIX/'beam-ready-results-delivery-v1/next-cohort-work.json')
    for bug in cohort['bugs']:
        if bug['bug']=='Jsoup-1':
            bug['status']='two_full_algorithm_outcomes_and_two_retained_native_invalid_ai_outcomes';bug['four_full_defects4j_outcomes_ready']=False;bug['required_next_action']='Joint intake of valid algorithm replay and retained native fixed-invalid AI outcomes; no AI retry or repaired tests admitted';bug['outcome_receipt_sha256']=sha(PREFIX/'beam-champ-jsoup-d4j-v1/receipt.json')
    cohort['pending_other_bugs']=18;cohort['ready_result_bugs_in_delivery']=2;write(output/'next-cohort-work.json',cohort)
    result={'status':'beam_jsoup_ready_results_return_audited','checked_at_utc':datetime.now(timezone.utc).isoformat(),'champ_commit':received['source_commit'],'worker_id':'beam-pc1','cpu_slots':1,'new_jsoup_full_algorithm_evaluations':2,'new_jsoup_native_invalid_ai_outcomes_retained':2,'new_jsoup_ai_d4j_evaluations':0,'combined_csv_jsoup_unique_bugs':2,'combined_condition_approach_outcomes':12,'combined_full_defects4j_completed':8,'unique_bugs_with_four_valid_full_measurements':1,'packet_manifests':manifests,'checksum_entries_verified':checked,'original_peer_git_blobs_verified':blobs,'focused_guard_tests_passed':6,'focused_guard_tests_skipped':0,'new_candidates_paused':True,'kku_requests_from_beam':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'full854_completed':False,'cli_scientific_fault_quarantine_retained':True,'producer_sha256':sha(output/'producer.py')}
    write(output/'receipt.json',result);write(output/'checksums.json',{p.relative_to(output).as_posix():sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':result['status'],'verified_entries':checked,'verified_git_blobs':blobs,'receipt_sha256':sha(output/'receipt.json'),'checksums_sha256':sha(output/'checksums.json')}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args();run(args.output)
