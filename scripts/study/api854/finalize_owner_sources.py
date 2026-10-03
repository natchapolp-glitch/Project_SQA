"""Combine audited prerequisite versions without overwriting original failures."""
import argparse
from collections import Counter
from pathlib import Path

from .audit_owner_sources import audit
from .common import ROOT, read_json, sha256
from .prepare_owner_sources import now, save


def finalize(original, recovery, destination, ownership, installed, interruptions=None):
    destination.mkdir(exist_ok=False)
    original_audit=audit(original,ownership,installed)
    initial=read_json(original/'index.json')
    recoveries=[recovery] if isinstance(recovery,Path) else list(recovery)
    recovery_audits=[]
    selected={}
    for version in recoveries:
        recovery_audits.append(audit(version,ownership,installed,allow_pending=True))
        newer=read_json(version/'index.json')
        by_id={f"{r['project']}-{r['bug_id']}":r for r in newer['records']}
        for name in read_json(version/'config.json')['selected_bug_ids']:
            assert name not in selected,'Overlapping recovery versions need explicit reconciliation'
            selected[name]=(version,by_id[name])
    interruption_history=[]
    if interruptions is not None and interruptions.exists():
        for receipt in sorted(interruptions.glob('*/interruption.json')):
            retained=read_json(receipt)
            assert retained['status']=='interrupted_exit_unknown'
            interruption_history.append({'path':receipt.relative_to(destination.parent).as_posix(),
                'sha256':sha256(receipt),'project':retained['project'],'bug_id':retained['bug_id']})
    records=[]
    history=[]
    for prior in initial['records']:
        name=f"{prior['project']}-{prior['bug_id']}"
        version=original
        effective=prior
        if name in selected:
            assert not prior['source_prepared'],'Recovery must address an original source failure'
            version,effective=selected[name]
            assert effective['status'] not in {'not_attempted','preparing'},name
            history.append({'project':prior['project'],'bug_id':prior['bug_id'],
                'original_record':(original/name/'record.json').relative_to(destination.parent).as_posix(),
                'original_record_sha256':sha256(original/name/'record.json'),
                'recovery_record':(version/name/'record.json').relative_to(destination.parent).as_posix(),
                'recovery_record_sha256':sha256(version/name/'record.json'),
                'source_recovered':effective['source_prepared'],
                'compile_status':effective['compile_status']})
        records.append({**effective,'evidence_folder':(version/name).relative_to(destination.parent).as_posix()})
    result={**{k:v for k,v in initial.items() if k not in {'records','statuses','compile_statuses','source_prepared','updated_at_utc','scope'}},
        'updated_at_utc':now(),'records':records,'source_prepared':sum(r['source_prepared'] for r in records),
        'statuses':dict(Counter(r['status'] for r in records)),
        'compile_statuses':dict(Counter(r['compile_status'] for r in records)),
        'source_recovery_count':sum(h['source_recovered'] for h in history),
        'completed_original_records':len(initial['records']),'source_recovery_attempts':len(selected),
        'interrupted_source_attempts':len(interruption_history),
        'source_preparation_attempts':len(initial['records'])+len(selected)+len(interruption_history),
        'retained_interruption_history':interruption_history,
        'scope':'Combined source/build prerequisites only, preserving original failures and versioned recovery. Not shared generation acceptance or primary results.',
        'source_indices':[
            {'path':(original/'index.json').relative_to(destination.parent).as_posix(),'sha256':sha256(original/'index.json')},
            *[{'path':(v/'index.json').relative_to(destination.parent).as_posix(),'sha256':sha256(v/'index.json')} for v in recoveries]],
        'retained_failure_history':history}
    assert len(records)==284 and len({(r['project'],r['bug_id']) for r in records})==284
    save(destination/'index.json',result)
    save(destination/'audit.json',{'original':original_audit,'recoveries':recovery_audits,
        'index_sha256':sha256(destination/'index.json'),
        'finalizer_sha256':sha256(__file__),'source_recovery_count':result['source_recovery_count']})
    save(destination/'checksums.json',{p.name:sha256(p) for p in sorted(destination.iterdir()) if p.is_file()})
    return {k:result[k] for k in ('expected_bugs','source_prepared','statuses','compile_statuses','source_recovery_count','primary_completed')}


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--original',type=Path,required=True)
    parser.add_argument('--recovery',type=Path,nargs='+',required=True)
    parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--interruptions',type=Path)
    args=parser.parse_args()
    import json
    print(json.dumps(finalize(args.original.resolve(),[v.resolve() for v in args.recovery],args.output.resolve(),
        ROOT/'experiments/configs/api854-20261003/ownership.json',ROOT/'output/api854-20261003/aom-installed-current.json',
        args.interruptions.resolve() if args.interruptions else None)))
