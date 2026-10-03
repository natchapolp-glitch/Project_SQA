"""Replay four unchanged, fixed-validated Csv/Jsoup archives on a Linux D4J host.

Uses the received Aom evaluator/runtime and the host's existing single CPU lock.
Separate development replay; no provider requests, queue, or primary promotion.
"""
import argparse
from contextlib import nullcontext
from datetime import datetime, timezone
import importlib.util
import json
from pathlib import Path
import sys

from .common import ROOT,read_json,write_json,sha256,cpu_slot
from .review_joint_recipe_intake import require,extract_archive
from .start_csv_development import AOM

PACKET=ROOT/'output/api854-20261004/champ-csv-messages-native-measurement-v1'
CLASSES={'Csv':'org.apache.commons.csv.ExtendedBufferedReader','Jsoup':'org.jsoup.nodes.Document'}
CLASSES.update({'Gson':'com.google.gson.TypeInfoFactory','Compress':'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream'})
CONDITIONS={'Csv':'api854-20261004-csv-messages-disabled-thinking-native-development-v2',
            'Jsoup':'api854-20261004-jsoup-ten-target-messages-disabled-thinking-native-development-v1'}
CONDITIONS.update({'Gson':'api854-20261004-gson-five-target-messages-disabled-thinking-isolated-bug-native-development-v1',
                   'Compress':'api854-20261004-compress-eight-target-messages-disabled-thinking-isolated-bug-native-development-v1'})


def run(output,worktrees,d4j,worker_id,packet=PACKET,approaches=None,counted=False):
    require(sys.platform=='linux','Full replay requires the accepted Linux/Java11 host')
    output=Path(output).resolve();worktrees=Path(worktrees).resolve()
    packet=Path(packet).resolve();require(packet.is_relative_to(ROOT/'output'),'Contained native packet required')
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained evidence output required')
    require(worker_id in {'beam-pc1','aom-pc1'},'Declare the actual accepted host alias')
    observer=None
    if counted:
        observer_path=ROOT/'scripts/study/api854/received/beam_ready_d4j_counts_2c0e92fc.py'
        require(sha256(observer_path)=='b74ec13932e29220481758604a61b97794cc427720f75ff5dcc60c677437a965','Pinned Beam XML observer changed')
        observer_spec=importlib.util.spec_from_file_location('received_beam_ready_xml_observer',observer_path)
        observer=importlib.util.module_from_spec(observer_spec);observer_spec.loader.exec_module(observer)
        require(Path(d4j).is_absolute() and Path(d4j).is_file(),'Actual Defects4J CLI file required for counted host replay')
    for relative,h in read_json(packet/'checksums.json').items():require(sha256(packet/relative)==h,'Native packet changed')
    received=read_json(packet/'receipt.json');seal=read_json(packet/'preexecution-seal.json')
    require(len(received['records'])==4,'All four native outcomes must be retained, including invalid suites')
    if approaches is None:approaches=['cmaes','fscs-art','kku-claude','kku-gemini']
    require(approaches and len(set(approaches))==len(approaches),'Unique nonempty explicit approaches required')
    selected=[r for r in received['records'] if r['approach'] in approaches]
    require(len(selected)==len(approaches) and all(r['status']=='native_fixed_twice_buggy_coverage_measured' for r in selected),
            'Only unchanged, native fixed-validated selected suites may enter this replay')
    project=seal['project'];bug=seal['bug_id']
    require(project in CLASSES and bug==1 and seal['aom_commit']==AOM and received['condition']==CONDITIONS[project],
            'Exact accepted native condition/project differs')
    target_class=CLASSES[project]
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
    classes_file=output/'instrument-classes.txt';classes_file.write_text(target_class+'\n',encoding='utf-8',newline='\n')
    tasks=[{'approach':r['approach'],'test_count':r['declared_test_count'],
            'suite':(packet/r['approach']/'packaged-suite/suite.tar.bz2').relative_to(ROOT).as_posix(),
            'suite_sha256':sha256(packet/r['approach']/'packaged-suite/suite.tar.bz2')} for r in selected]
    write_json(output/'preexecution-plan.json',{'schema_version':1,'condition':received['condition'],
        'replay_condition':received['condition']+('-d4j-java11-los-angeles-counted-replay-v1' if counted else '-d4j-java11-los-angeles-replay-v1'),
        'native_environment_results_do_not_transfer':True,'actual_junit_xml_observer_enabled':counted,
        'received_observer_sha256':sha256(observer_path) if counted else None,
        'purpose':'User-authorized full Defects4J development replay of explicitly selected fixed-valid ready-bug suites',
        'project':project,'bug_id':bug,'native_packet':packet.relative_to(ROOT).as_posix(),
        'buggy_source_mode':seal['production_source_sha256']['buggy'].get('source_mode','exact_git_parent_revision'),
        'source_guard_requires_exact_bytes':True,
        'selected_approaches':approaches,'native_outcomes_not_replayed':[{'approach':r['approach'],'status':r['status']}
            for r in received['records'] if r['approach'] not in approaches],
        'unreplayed_or_invalid_AI_outcomes_are_not_zero_coverage_or_four_measured_methods':True,
        'worker_id':worker_id,'cpu_slots':1,'worktrees_root':str(worktrees),'checkout_root':str(checkout_base),
        'received_native_manifest_sha256':sha256(packet/'checksums.json'),'aom_commit':AOM,
        'received_runtime_source_sha256':seal['runtime_source_sha256'],'snapshot_archive_sha256':archive,
        'evaluated_classes':[target_class],'java_required':11,'timezone':'America/Los_Angeles',
        'tasks':tasks,'primary_results_allowed':False,'kku_requests_cap':0,'queue_mutations':0})
    records=[]
    with cpu_slot(worktrees):
        checkout_base.mkdir(parents=True)
        original_command=observer.install_collector(evaluator,worktrees) if counted else evaluator.run_command
        try:
            observing=observer.observe_framework(Path(d4j).resolve().parents[2],output/'count-observer-framework') if counted else nullcontext()
            with observing:
                for task in tasks:
                    approach=task['approach'];base=(checkout_base/approach).resolve()
                    require(base.is_relative_to(worktrees),'Unsafe checkout directory');base.mkdir()
                    out=output/approach;out.mkdir();trees={}
                    for version in ('f','b'):
                        tree=base/version
                        command=evaluator.run_command([d4j,'checkout','-p',project,'-v',str(bug)+version,'-w',str(tree)],ROOT,out/('checkout-'+version),300)
                        require(command['exit_code']==0 and not command['timed_out'],'Defects4J checkout failed')
                        evaluator.validate_worktree(tree,project,str(bug)+version);trees[version]=tree
                        expected=seal['production_source_sha256']['fixed' if version=='f' else 'buggy']['sources']
                        for relative,h in expected.items():require(sha256(tree/relative)==h,'Actual D4J production source differs from native production revision')
                    record=evaluator.evaluate_run(evaluator.EvaluationConfig(project=project,bug_id=bug,generator=approach,seed=101,budget=30,
                        suite=ROOT/task['suite'],buggy_worktree=trees['b'],fixed_worktree=trees['f'],output=out/'measurement',d4j=d4j,
                        classes_file=classes_file,test_count=task['test_count'],timeout_seconds=300))
                    actual_counts={}
                    if counted and record['status']=='complete':
                        for stage in ('fixed-1','fixed-2','buggy','coverage'):
                            counts=read_json(out/('measurement/'+stage+'/actual-junit-counts.json'))
                            require(counts['executed']==task['test_count'] and counts['skipped']==0 and counts['errors']==0,'Actual replay counts differ')
                            if not approach.startswith('kku-'):require(counts['target_checks']==30,'Actual algorithm target checks differ')
                            if stage!='buggy':require(counts['failed']==0,'Accepted fixed/coverage stage has failures')
                            actual_counts[stage]=counts
                    records.append({'approach':approach,'status':record['status'],'record_path':(out/'measurement/record.json').relative_to(ROOT).as_posix(),
                                    'record_sha256':sha256(out/'measurement/record.json'),'raw_fault_detected':record['fault_detected'],
                                    'actual_junit_stage_counts':actual_counts if counted else None})
        finally:evaluator.run_command=original_command
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
    parser.add_argument('--packet',type=Path,default=PACKET)
    parser.add_argument('--approaches',nargs='+',choices=['cmaes','fscs-art','kku-claude','kku-gemini'])
    parser.add_argument('--counted',action='store_true',help='Use received Beam formatter-only observer under the same CPU lock, restoring exact framework bytes')
    parser.add_argument('--d4j',required=True);parser.add_argument('--worker-id',required=True);args=parser.parse_args()
    new=not args.output.resolve().exists()
    try:r=run(args.output,args.worktrees,args.d4j,args.worker_id,args.packet,args.approaches,args.counted)
    except Exception as error:
        out=args.output.resolve()
        if new and out.is_relative_to(ROOT/'output') and out.exists() and not (out/'checksums.json').exists():
            write_json(out/'failed-attempt.json',{'status':'replay_attempt_failed','error_type':type(error).__name__,'error':str(error),'primary_results_added':0})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':r['status'],'records':r['records']}))
