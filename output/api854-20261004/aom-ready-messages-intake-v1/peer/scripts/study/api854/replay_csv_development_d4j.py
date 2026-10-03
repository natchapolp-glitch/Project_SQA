"""Replay the four unchanged Csv Messages-condition archives on a Linux D4J host.

Uses the received Aom evaluator/runtime and the host's existing single CPU lock.
Separate development replay; no provider requests, queue, or primary promotion.
"""
import argparse
from datetime import datetime, timezone
import importlib.util
import json
from pathlib import Path
import sys

from .common import ROOT,read_json,write_json,sha256,cpu_slot
from .review_joint_recipe_intake import require,extract_archive
from .start_csv_development import AOM

PACKET=ROOT/'output/api854-20261004/champ-csv-messages-native-measurement-v1'


def run(output,worktrees,d4j,worker_id):
    require(sys.platform=='linux','Full replay requires the accepted Linux/Java11 host')
    output=Path(output).resolve();worktrees=Path(worktrees).resolve()
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained evidence output required')
    require(worker_id in {'beam-pc1','aom-pc1'},'Declare the actual accepted host alias')
    for relative,h in read_json(PACKET/'checksums.json').items():require(sha256(PACKET/relative)==h,'Native packet changed')
    received=read_json(PACKET/'receipt.json');seal=read_json(PACKET/'preexecution-seal.json')
    require(len(received['records'])==4 and all(r['status']=='native_fixed_twice_buggy_coverage_measured' for r in received['records']), 'Only complete unchanged native suites may enter this replay')
    # A new isolated checkout for every approach; retain all checkouts for peer inspection.
    checkout_base=(worktrees/output.name).resolve()
    require(checkout_base.is_relative_to(worktrees) and not checkout_base.exists(),'Fresh contained host checkout root required')
    output.mkdir(parents=True);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    snapshot=output/'received-evaluator';snapshot.mkdir()
    archive=extract_archive(AOM,['scripts/study','algorithms','experiments/configs/api854-20261003'],snapshot)
    for relative,h in seal['runtime_source_sha256'].items():require(sha256(snapshot/relative)==h,'Received evaluator/runtime changed')
    evaluator_path=snapshot/'scripts/study/evaluate.py'
    spec=importlib.util.spec_from_file_location('ready_csv_received_evaluate',evaluator_path)
    evaluator=importlib.util.module_from_spec(spec);sys.modules[spec.name]=evaluator;spec.loader.exec_module(evaluator)
    classes_file=output/'instrument-classes.txt';classes_file.write_text('org.apache.commons.csv.ExtendedBufferedReader\n',encoding='utf-8',newline='\n')
    tasks=[{'approach':r['approach'],'test_count':r['declared_test_count'],
            'suite':(PACKET/r['approach']/'packaged-suite/suite.tar.bz2').relative_to(ROOT).as_posix(),
            'suite_sha256':sha256(PACKET/r['approach']/'packaged-suite/suite.tar.bz2')} for r in received['records']]
    write_json(output/'preexecution-plan.json',{'schema_version':1,'condition':received['condition'],
        'replay_condition':received['condition']+'-d4j-java11-los-angeles-replay-v1','native_environment_results_do_not_transfer':True,
        'purpose':'User-authorized full Defects4J development replay of first ready four valid suites',
        'worker_id':worker_id,'cpu_slots':1,'worktrees_root':str(worktrees),'checkout_root':str(checkout_base),
        'received_native_manifest_sha256':sha256(PACKET/'checksums.json'),'aom_commit':AOM,
        'received_runtime_source_sha256':seal['runtime_source_sha256'],'snapshot_archive_sha256':archive,
        'evaluated_classes':['org.apache.commons.csv.ExtendedBufferedReader'],'java_required':11,'timezone':'America/Los_Angeles',
        'tasks':tasks,'primary_results_allowed':False,'kku_requests_cap':0,'queue_mutations':0})
    records=[]
    with cpu_slot(worktrees):
        checkout_base.mkdir(parents=True)
        for task in tasks:
            approach=task['approach'];base=(checkout_base/approach).resolve()
            require(base.is_relative_to(worktrees),'Unsafe checkout directory');base.mkdir()
            out=output/approach;out.mkdir();trees={}
            for version in ('f','b'):
                tree=base/version
                command=evaluator.run_command([d4j,'checkout','-p','Csv','-v','1'+version,'-w',str(tree)],ROOT,out/('checkout-'+version),300)
                require(command['exit_code']==0 and not command['timed_out'],'Defects4J checkout failed')
                evaluator.validate_worktree(tree,'Csv','1'+version);trees[version]=tree
                expected=seal['production_source_sha256']['fixed' if version=='f' else 'buggy']['sources']
                for relative,h in expected.items():require(sha256(tree/relative)==h,'Actual D4J production source differs from native production revision')
            record=evaluator.evaluate_run(evaluator.EvaluationConfig(project='Csv',bug_id=1,generator=approach,seed=101,budget=30,
                suite=ROOT/task['suite'],buggy_worktree=trees['b'],fixed_worktree=trees['f'],output=out/'measurement',d4j=d4j,
                classes_file=classes_file,test_count=task['test_count'],timeout_seconds=300))
            records.append({'approach':approach,'status':record['status'],'record_path':(out/'measurement/record.json').relative_to(ROOT).as_posix(),
                            'record_sha256':sha256(out/'measurement/record.json'),'raw_fault_detected':record['fault_detected']})
    result={'status':'full_development_replay_observations_collected','condition':received['condition'],
            'records':records,'worker_id':worker_id,'primary_results_added':0,'kku_requests':0,'queue_mutations':0,
            'completed_at_utc':datetime.now(timezone.utc).isoformat(),
            'limits':'Host owner must review raw actual executed/skipped counters, coverage and semantic failure before accepting measurements'}
    write_json(output/'receipt.json',result)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return result


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('output','worktrees'):parser.add_argument('--'+name,type=Path,required=True)
    parser.add_argument('--d4j',required=True);parser.add_argument('--worker-id',required=True);args=parser.parse_args()
    try:r=run(args.output,args.worktrees,args.d4j,args.worker_id)
    except Exception as error:
        out=args.output.resolve()
        if out.is_relative_to(ROOT/'output') and out.exists() and not (out/'checksums.json').exists():
            write_json(out/'failed-attempt.json',{'status':'replay_attempt_failed','error_type':type(error).__name__,'error':str(error),'primary_results_added':0})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':r['status'],'records':r['records']}))
