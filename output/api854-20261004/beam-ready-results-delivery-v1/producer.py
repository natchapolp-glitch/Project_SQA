"""Seal a hash-linked, condition-separated delivery index and remaining cohort work."""
import argparse,csv,hashlib,json,subprocess,sys,time
from datetime import datetime,timezone
from pathlib import Path
ROOT=Path(__file__).resolve().parents[3]
PREFIX=ROOT/'output/api854-20261004'
PACKETS=['beam-champ-csv-a4-intake-v1','beam-champ-csv-a4-d4j-v1','beam-champ-d98-ready-intake-v1','beam-champ-csv-messages-d4j-v1','beam-champ-csv-messages-d4j-v2','beam-champ-csv-messages-d4j-v3','beam-champ-cli-order-review-v1']
def sha(path):return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def read(path):return json.loads(Path(path).read_bytes())
def write(path,value):
    with Path(path).open('x',encoding='utf-8',newline='\n') as stream:json.dump(value,stream,ensure_ascii=False,indent=2);stream.write('\n')
def require(ok,message):
    if not ok:raise ValueError(message)
def run(output):
    output=output.resolve();require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained delivery output required')
    output.mkdir(parents=True,exist_ok=False);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    manifests={};verified=0
    for name in PACKETS:
        base=PREFIX/name;manifest=read(base/'checksums.json')
        actual={p.relative_to(base).as_posix() for p in base.rglob('*') if p.is_file() and p!=base/'checksums.json'}
        require(set(manifest)==actual,'Incomplete public evidence packet: '+name)
        for relative,digest in manifest.items():require(sha(base/relative)==digest,'Changed packet file: '+name+'/'+relative)
        manifests[name]={'path':(base/'checksums.json').relative_to(ROOT).as_posix(),'sha256':sha(base/'checksums.json'),'files':len(manifest)};verified+=len(manifest)
    # Independently bind both read-only intakes back to the original Git blobs.
    git=['git','-c','safe.directory='+str(ROOT),'-C',str(ROOT)]
    batch=subprocess.Popen(git+['cat-file','--batch'],stdin=subprocess.PIPE,stdout=subprocess.PIPE)
    blobs=0
    for name in ['beam-champ-csv-a4-intake-v1','beam-champ-d98-ready-intake-v1']:
        intake=PREFIX/name;receipt=read(intake/'receipt.json')
        for relative,digest in receipt['source_sha256'].items():
            batch.stdin.write((receipt['source_commit']+':'+relative+'\n').encode());batch.stdin.flush()
            header=batch.stdout.readline().decode().split();require(len(header)==3 and header[1]=='blob','Git blob not found')
            size=int(header[2]);data=batch.stdout.read(size);require(batch.stdout.read(1)==b'\n','Invalid batch response')
            require(hashlib.sha256(data).hexdigest()==digest and data==(intake/'received-champ'/relative).read_bytes(),'Original Git bytes differ');blobs+=1
    batch.stdin.close();require(batch.wait()==0,'Git object audit failed')
    started=time.monotonic();argv=[sys.executable,'-B','-m','unittest','discover','-s','scripts/study/api854/tests','-p','test_ready_defects4j_replay.py','-v']
    guards=subprocess.run(argv,cwd=ROOT,capture_output=True,timeout=180)
    (output/'guards.stdout.log').write_bytes(guards.stdout);(output/'guards.stderr.log').write_bytes(guards.stderr)
    write(output/'guards.command.json',{'argv':argv,'cwd':str(ROOT),'exit_code':guards.returncode,'duration_seconds':time.monotonic()-started,'source_sha256':sha(ROOT/'scripts/study/api854/tests/test_ready_defects4j_replay.py')})
    require(guards.returncode==0 and b'Ran 9 tests' in guards.stderr and b'OK' in guards.stderr,'Replay/count/quarantine guard failure')
    old=read(PREFIX/'beam-champ-csv-a4-d4j-v1/results.json');new=read(PREFIX/'beam-champ-csv-messages-d4j-v3/results.json')
    require(len(new)==4 and all(r['status']=='complete' for r in new),'New condition not complete for all four')
    rows=[]
    for label,packet,records in [('baseline','beam-champ-csv-a4-d4j-v1',old),('messages','beam-champ-csv-messages-d4j-v3',new)]:
        condition=read(PREFIX/packet/'receipt.json')
        for record in records:
            rows.append({'condition_label':label,'generation_condition':condition['generation_condition'],'execution_condition':condition['execution_condition'],'project':record['project'],'bug_id':record['bug_id'],'approach':record['approach'],'status':record['status'],'fault_detected':record['fault_detected'],'test_count':record['test_count'],'generation_seconds':record.get('generation_seconds'),'worker_seconds_including_setup':record.get('evaluation_seconds'),'line_covered':(record.get('coverage') or {}).get('line_covered'),'line_total':(record.get('coverage') or {}).get('line_total'),'branch_covered':(record.get('coverage') or {}).get('branch_covered'),'branch_total':(record.get('coverage') or {}).get('branch_total'),'suite_sha256':record['suite_sha256'],'outcome_path':(PREFIX/packet/record['approach']/'outcome.json').relative_to(ROOT).as_posix(),'outcome_sha256':sha(PREFIX/packet/record['approach']/'outcome.json'),'primary':False})
    write(output/'condition-separated-results.json',rows)
    with (output/'condition-separated-results.csv').open('x',encoding='utf-8',newline='') as stream:
        writer=csv.DictWriter(stream,fieldnames=list(rows[0]));writer.writeheader();writer.writerows(rows)
    snapshot=ROOT/'.local/api854/beam-v12-snapshot-63ad1956-v2';prep=snapshot/'output/api854-20261003/prepare-v12-graphics-development-v1'
    index=read(prep/'index.json');next_rows=[]
    for record in index['records']:
        bug=record['project']+'-'+str(record['bug_id']);folder=prep/bug
        status='four_full_development_outcomes_ready' if bug=='Csv-1' else 'quarantined_unordered_option_oracle_requires_prospective_condition' if bug=='Cli-1' else 'prepared_inputs_only_next_four_method_suite_packet_pending'
        next_rows.append({'bug':bug,'owner':record['owner'],'shared_selected':record['target_count'],'status':status,'four_full_defects4j_outcomes_ready':bug=='Csv-1','required_next_action':'Joint result intake; counted Java11/isolated-bug execution profile reviewed' if bug=='Csv-1' else 'Aom/Champ prospective options-only unordered oracle and Cli16 regression, then new condition' if bug=='Cli-1' else 'Team selects scoped-ready batch under pinned inputs; Champ coordinates two model suites, owner supplies algorithm suites; Beam replays received archives on CPU1','frozen_input_sha256':{n:sha(folder/n) for n in ['targets.json','fixture-recipes.json','prepare-policy.json','prompt.md']}})
    write(output/'next-cohort-work.json',{'source_commit':'63ad195623c2ed3f67f3ae232c00c54d3160ce72','index_sha256':sha(prep/'index.json'),'bugs':next_rows,'pending_other_bugs':19,'new_candidates_paused':True,'all_691_semantic_completion_backlog':True,'enum_four_still_pending_team_decision':True,'api_requests_scheduled_by_this_export':0})
    cli=read(PREFIX/'beam-champ-cli-order-review-v1/receipt.json')
    require(cli['confirmed_semantic_fault'] is False and cli['scientific_fault_count_added']==0,'Cli not quarantined')
    receipt={'status':'beam_ready_results_delivery_audited','checked_at_utc':datetime.now(timezone.utc).isoformat(),'worker_id':'beam-pc1','cpu_slots':1,'unique_bugs_with_four_full_development_outcomes':1,'new_messages_full_defects4j_completed':4,'baseline_full_defects4j_completed':2,'baseline_invalid_outcomes_retained':2,'total_condition_approach_outcomes':8,'scientific_fault_positive_methods_on_csv':['kku-claude','kku-gemini'],'cli_order_false_positive_quarantined':True,'packet_manifests':manifests,'checksum_entries_verified':verified,'original_peer_git_blobs_verified':blobs,'focused_guard_tests_passed':9,'focused_guard_tests_skipped':0,'new_candidates_paused':True,'kku_requests_from_beam':0,'queue_mutations':0,'primary_results_added':0,'gate_a_approved':False,'full854_completed':False,'received_v13_not_adopted_for_frozen_v12_batch':True,'provider_generation_handled_by_champ':True,'producer_sha256':sha(output/'producer.py')}
    write(output/'receipt.json',receipt)
    write(output/'checksums.json',{p.relative_to(output).as_posix():sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':receipt['status'],'checksum_entries_verified':verified,'original_peer_git_blobs_verified':blobs,'receipt_sha256':sha(output/'receipt.json'),'checksum_sha256':sha(output/'checksums.json')}))
if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,required=True);args=parser.parse_args();run(args.output)
