"""Replay four immutable Csv suites in fresh Defects4J worktrees; no AI requests."""
import argparse
from datetime import datetime,timezone
import hashlib
import json
import os
from pathlib import Path
import subprocess
import sys

def digest(p):return hashlib.sha256(Path(p).read_bytes()).hexdigest()
def save(p,d):Path(p).write_text(json.dumps(d,indent=2,allow_nan=False)+'\n',encoding='utf-8')

def main():
    parser=argparse.ArgumentParser()
    parser.add_argument('--d4j',required=True)
    parser.add_argument('--worktrees',required=True)
    parser.add_argument('--output',required=True)
    args=parser.parse_args()
    if sys.platform!='linux':raise RuntimeError('Use Linux/WSL and Java11')
    bundle=Path(__file__).resolve().parents[1]
    if not (bundle/'Experiment/results.json').exists():raise RuntimeError('Run the copy inside the submission bundle')
    data=json.loads((bundle/'Experiment/results.json').read_text(encoding='utf-8'))
    for p,h in data['runtime_source_hashes'].items():
        if digest(bundle/'Shared/frozen-runtime'/p)!=h:raise RuntimeError('Frozen runtime differs: '+p)
    sys.path.insert(0,str(bundle/'Shared/frozen-runtime/scripts/study'))
    from evaluate import EvaluationConfig,evaluate_run,run_command,validate_worktree,validate_archive
    from api854.common import cpu_slot
    out=Path(args.output).resolve();out.mkdir(parents=True,exist_ok=False)
    roots=Path(args.worktrees).resolve()
    trees=roots/('csv-single-bug-demo-'+datetime.now(timezone.utc).strftime('%Y%m%dT%H%M%S%f'))
    os.environ['TZ']='America/Los_Angeles'
    java=subprocess.run(['java','-version'],text=True,capture_output=True,check=True).stderr
    if 'version "11.' not in java:raise RuntimeError('Java11 is required')
    framework=Path(args.d4j).resolve().parents[2]
    commit=subprocess.run(['git','-C',str(framework),'rev-parse','HEAD'],text=True,capture_output=True,check=True).stdout.strip()
    if commit!=data['framework_commit']:raise RuntimeError('Defects4J commit differs')
    save(out/'preexecution.json',{'scope':'Replay of Csv-1 only, four existing suites','java':java,
         'defects4j_commit':commit,'host':'aom-pc1','cpu_slots':1,'api_requests':0,'generation_repeats_added':0,
         'worktrees':str(trees),'canonical_summary_sha256':digest(bundle/'Experiment/summary.csv')})
    (out/'instrument-classes.txt').write_text(data['target_class']+'\n')
    observations=[]
    try:
        with cpu_slot(roots):
            trees.mkdir(exist_ok=False)
            for row in data['methods']:
                method=row['approach'];folder=out/method;folder.mkdir()
                fixed=trees/method/'f';buggy=trees/method/'b'
                for rev,tree,key in [('f',fixed,'fixed_source_hashes'),('b',buggy,'buggy_source_hashes')]:
                    tree.parent.mkdir(parents=True,exist_ok=True)
                    cmd=run_command([args.d4j,'checkout','-p','Csv','-v','1'+rev,'-w',str(tree)],bundle,folder/('checkout-'+rev),300)
                    if cmd['exit_code']!=0 or cmd['timed_out']:raise RuntimeError('Checkout failed')
                    validate_worktree(tree,'Csv','1'+rev)
                    for path,h in data[key].items():
                        if digest(tree/path)!=h:raise RuntimeError('Benchmark source differs: '+path)
                suite=bundle/row['suite_path'];validate_archive(suite)
                if digest(suite)!=row['suite_sha256']:raise RuntimeError('Suite differs')
                result=evaluate_run(EvaluationConfig(project='Csv',bug_id=1,generator=method,
                    seed=101,budget=30,suite=suite,buggy_worktree=buggy,fixed_worktree=fixed,
                    output=folder/'evaluation',d4j=args.d4j,classes_file=out/'instrument-classes.txt',
                    test_count=row['declared_tests'],timeout_seconds=300))
                comparisons={
                    'status':result['status']=='complete',
                    'fixed-1':result['stages']['fixed-1'].get('failure_count')==0,
                    'fixed-2':result['stages']['fixed-2'].get('failure_count')==0,
                    'buggy':result['stages']['buggy'].get('failure_count')==row['buggy_failures'],
                    'fault':result['fault_detected']==(row['fault_detected']=='True'),
                    'line_covered':result['line_covered']==row['line_covered'],
                    'line_total':result['line_total']==row['line_total'],
                    'branch_covered':result['branch_covered']==row['branch_covered'],
                    'branch_total':result['branch_total']==row['branch_total']}
                observations.append({'approach':method,'checks':comparisons,'verified':all(comparisons.values()),
                    'record_path':str((folder/'evaluation/record.json').relative_to(out)),
                    'record_sha256':digest(folder/'evaluation/record.json'),'suite_sha256':digest(suite)})
                print(json.dumps({'approach':method,'status':result['status'],'verified':all(comparisons.values()),'fault':result['fault_detected']}),flush=True)
                if not all(comparisons.values()):raise RuntimeError('Replay differs from canonical record; preserve attempt')
        save(out/'receipt.json',{'all_four_methods_verified':len(observations)==4 and all(x['verified'] for x in observations),
            'observations':observations,'unique_bugs':1,'new_generation_repeats':0,'api_requests':0,
            'meaning':'Fresh host replay / demo rehearsal; not a new experimental generation run'})
    except Exception as e:
        save(out/'failed-attempt.json',{'error_type':type(e).__name__,'error':str(e),'observations':observations})
        raise

if __name__=='__main__':main()
