"""Versioned selective source-only recovery; literal dollar class filenames supported."""
import argparse
from collections import Counter
import datetime
import json
import os
from pathlib import Path
import shutil

from .common import ROOT, contained, cpu_slot, sha256, read_json
from .inventory import validate_inventory, compare_installed
from .context_export import export_context
from .owner_source_selection_v2 import select_files
from evaluate import run_command, validate_worktree

POLICY='aom-owner-fixed-modified-source-build-v2-dollar'


def now():return datetime.datetime.now(datetime.timezone.utc).isoformat()


def save(path, data):
    path=Path(path)
    path.parent.mkdir(parents=True,exist_ok=True)
    temporary=path.with_suffix('.writing.json')
    temporary.write_text(json.dumps(data,indent=2,ensure_ascii=False)+'\n',encoding='utf-8')
    temporary.replace(path)


def command(args, output, timeout, cwd=ROOT):
    stage=run_command([str(a) for a in args],cwd,output,timeout)
    if stage['exit_code'] != 0 or stage['timed_out']:
        raise RuntimeError(f'Command failed/timeout: {output.relative_to(ROOT)}')
    return (output/'command.log').read_text(encoding='utf-8',errors='replace').strip()


def verify_artifacts(folder):
    for name, expected in read_json(folder/'checksums.json').items():
        if sha256(contained(folder,name)) != expected:
            raise ValueError('Existing evidence changed: '+name)


def preflight(ownership, installed, owner):
    rows=validate_inventory(read_json(ownership)['bugs'])
    compare_installed(rows,read_json(installed)['projects'])
    return [r for r in rows if r['owner']==owner]


def summarize(output, rows):
    records=[]
    for row in rows:
        record=output/f"{row['project']}-{row['bug_id']}"/'record.json'
        records.append(read_json(record) if record.exists() else {**row,'status':'not_attempted',
            'source_prepared':False,'compile_status':'not_attempted'})
    report={'updated_at_utc':now(),'owner':rows[0]['owner'],'expected_bugs':len(rows),
        'primary_completed':0,'test_methods_generated':0,'real_kku_requests':0,'live_queue_mutations':0,
        'source_prepared':sum(r['source_prepared'] for r in records),
        'statuses':dict(Counter(r['status'] for r in records)),
        'compile_statuses':dict(Counter(r['compile_status'] for r in records)),
        'scope':'Fixed-source/build prerequisite evidence only; declarations/fixtures/oracles and primary evaluation not approved.',
        'records':records}
    save(output/'index.json',report)
    return report


def prepare_one(row, *, d4j, worktrees, output, timeout, compile_timeout):
    folder=output/f"{row['project']}-{row['bug_id']}"
    folder.mkdir(exist_ok=False)
    tree=worktrees/row['project']/str(row['bug_id'])/'f'
    record={**row,'started_at_utc':now(),'status':'preparing','source_prepared':False,
        'compile_status':'not_attempted','policy_id':POLICY,'primary_usable':False}
    save(folder/'record.json',record)
    try:
        if tree.exists():
            raise ValueError('Fresh attempt refuses an existing worktree')
        tree.parent.mkdir(parents=True,exist_ok=True)
        command([d4j,'checkout','-p',row['project'],'-v',f"{row['bug_id']}f",'-w',tree],folder/'checkout',timeout)
        validate_worktree(tree,row['project'],f"{row['bug_id']}f")
        head=command(['git','rev-parse','HEAD'],folder/'revision-head',30,cwd=tree)
        tag=f"D4J_{row['project']}_{row['bug_id']}_FIXED_VERSION"
        fixed=command(['git','rev-parse',tag+'^{commit}'],folder/'revision-tag',30,cwd=tree)
        if head!=fixed:raise ValueError('HEAD differs from installed fixed tag')
        install=Path(d4j).resolve().parents[2]
        modified=install/'framework/projects'/row['project']/'modified_classes'/f"{row['bug_id']}.src"
        classes=sorted(set(s.strip() for s in modified.read_text().splitlines() if s.strip()))
        if not classes:raise ValueError('No declared modified classes')
        paths=select_files(tree,classes)
        diff=command(['git','diff',head,'--',*paths],folder/'selected-source-diff',30,cwd=tree)
        if diff:raise ValueError('Selected source/build files differ from fixed revision')
        manifest=export_context(tree,row['project'],row['bug_id'],paths,folder/'context',policy_id=POLICY)
        save(folder/'revision-proof.json',{'head':head,'fixed_tag':tag,'fixed_tag_commit':fixed,
            'selected_source_changes':diff,'verified':True,'modified_classes_metadata_sha256':sha256(modified)})
        record.update(source_prepared=True,source_hash=manifest['source_hash'],context_manifest_sha256=None)
    except Exception as error:
        record.update(status='source_failed',error=str(error))
    if record['source_prepared']:
        record['context_manifest_sha256']=sha256(folder/'context/context-manifest.json')
        try:
            command([d4j,'compile','-w',tree],folder/'compile',compile_timeout)
            record.update(status='source_prepared',compile_status='passed')
        except Exception as error:
            state=read_json(folder/'compile/command.json')
            record.update(status='source_prepared_compile_failed',compile_status='timeout' if state['timed_out'] else 'failed',compile_error=str(error))
        try:
            diff=command(['git','diff',head,'--',*paths],folder/'post-compile-source-diff',30,cwd=tree)
            if diff:raise ValueError('Selected source changed during compile')
            for item in manifest['source_files']:
                if sha256(tree/item['path'])!=item['sha256']:raise ValueError('Selected bytes changed during compile')
        except Exception as error:
            record.update(status='source_changed_after_compile',source_prepared=False,error=str(error))
    record['finished_at_utc']=now()
    save(folder/'record.json',record)
    save(folder/'checksums.json',{p.relative_to(folder).as_posix():sha256(p) for p in sorted(folder.rglob('*')) if p.is_file()})
    print(json.dumps({k:record[k] for k in ('project','bug_id','status','compile_status')}) ,flush=True)
    return record


def main():
    cli=argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--owner',choices=['aom','beam','champ'],default='aom')
    cli.add_argument('--ownership',type=Path,default=ROOT/'experiments/configs/api854-20261003/ownership.json')
    cli.add_argument('--installed',type=Path,default=ROOT/'output/api854-20261003/aom-installed-current.json')
    cli.add_argument('--d4j',type=Path,required=True)
    cli.add_argument('--worktrees',type=Path,required=True)
    cli.add_argument('--output',type=Path,required=True)
    cli.add_argument('--limit',type=int)
    cli.add_argument('--only',nargs='+',required=True,help='Explicit Project-ID source recovery subset')
    cli.add_argument('--resume',action='store_true')
    cli.add_argument('--timeout',type=int,default=300)
    cli.add_argument('--compile-timeout',type=int,default=120)
    args=cli.parse_args()
    if os.name=='nt':cli.error('Use Linux/WSL for Defects4J')
    rows=preflight(args.ownership,args.installed,args.owner)
    selected=set(args.only)
    if not selected.issubset({f"{r['project']}-{r['bug_id']}" for r in rows}):
        cli.error('Recovery IDs must belong to this owner inventory')
    output=args.output.resolve()
    if not output.is_relative_to(ROOT/'output'):cli.error('Output must be under repository output')
    config={'owner':args.owner,'policy_id':POLICY,'selected_bug_ids':sorted(selected),
        'ownership_sha256':sha256(args.ownership),
        'installed_sha256':sha256(args.installed),'script_sha256':sha256(__file__),
        'context_export_sha256':sha256(ROOT/'scripts/study/api854/context_export.py'),
        'selection_sha256':sha256(ROOT/'scripts/study/api854/owner_source_selection_v2.py'),
        'd4j':str(args.d4j.resolve()),'worktrees':str(args.worktrees.resolve()),
        'checkout_timeout':args.timeout,'compile_timeout':args.compile_timeout,
        'expected_bugs':len(rows),'created_conditions':'Source/build prerequisites only; no AI prompt or live queue mutation.'}
    if output.exists():
        if not args.resume:cli.error('Existing output needs --resume')
        if read_json(output/'config.json')!=config:raise ValueError('Resume configuration/source hashes differ')
    else:
        output.mkdir(parents=True)
        save(output/'config.json',config)
        summarize(output,rows)
    attempts=0
    with cpu_slot(args.worktrees):
        for row in rows:
            if f"{row['project']}-{row['bug_id']}" not in selected:continue
            folder=output/f"{row['project']}-{row['bug_id']}"
            if folder.exists():
                if (folder/'checksums.json').exists():
                    verify_artifacts(folder)
                    continue
                raise ValueError('Interrupted attempt retained; reconcile before retry: '+str(folder))
            if args.limit is not None and attempts>=args.limit:break
            if min(shutil.disk_usage(output).free,shutil.disk_usage('/mnt/c').free)<20*1024**3:
                raise RuntimeError('Disk free below 20 GiB; stop before checkout')
            print(f"START {row['project']}-{row['bug_id']}",flush=True)
            prepare_one(row,d4j=args.d4j.resolve(),worktrees=args.worktrees.resolve(),output=output,
                timeout=args.timeout,compile_timeout=args.compile_timeout)
            attempts+=1
            summarize(output,rows)
    result=summarize(output,rows)
    print(json.dumps({k:result[k] for k in ('expected_bugs','source_prepared','statuses','compile_statuses')}),flush=True)


if __name__=='__main__':main()
