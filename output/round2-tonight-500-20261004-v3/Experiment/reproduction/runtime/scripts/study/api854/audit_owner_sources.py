"""Verify saved source preparation bytes and owner denominators, never promote results."""
import argparse
from collections import Counter
import json
from pathlib import Path

from .common import ROOT, contained, read_json, sha256
from .context_export import BUILD_FILES
from .kku_client import digest
from .prepare_owner_sources import POLICY, preflight, save


def audit(folder, ownership, installed, *, allow_pending=False):
    config=read_json(folder/'config.json')
    index=read_json(folder/'index.json')
    expected=preflight(ownership,installed,config['owner'])
    versions={POLICY:('prepare_owner_sources.py','prepare_pilot.py'),
        'aom-owner-fixed-modified-source-build-v2-dollar':('prepare_owner_sources_v2.py','owner_source_selection_v2.py'),
        'aom-owner-fixed-modified-source-build-v3-source-root':('prepare_owner_sources_v3.py','owner_source_selection_v3.py')}
    driver,selection=versions[config['policy_id']]
    if config['policy_id']=='aom-owner-fixed-modified-source-build-v3-source-root':
        assert config['dollar_selection_sha256']==sha256(ROOT/'scripts/study/api854/owner_source_selection_v2.py')
    assert config['ownership_sha256']==sha256(ownership)
    assert config['installed_sha256']==sha256(installed)
    for field, path in [('script_sha256',driver),
                        ('context_export_sha256','context_export.py'),
                        ('selection_sha256',selection)]:
        assert config[field]==sha256(ROOT/'scripts/study/api854'/path), field
    identity=lambda r:(r['project'],r['bug_id'],r['owner'])
    assert len(index['records'])==len(expected)==config['expected_bugs']==index['expected_bugs']
    assert len(set(map(identity,index['records'])))==len(expected)
    assert set(map(identity,index['records']))==set(map(identity,expected))
    assert index['owner']==config['owner']
    for key in ('primary_completed','test_methods_generated','real_kku_requests','live_queue_mutations'):
        assert index[key]==0,key
    checked_files=0
    pending=[]
    for record in index['records']:
        name=f"{record['project']}-{record['bug_id']}"
        evidence=folder/name
        if record['status'] in {'not_attempted','preparing'}:
            pending.append(name)
            continue
        assert read_json(evidence/'record.json')==record,name
        assert record['policy_id']==config['policy_id'] and record['primary_usable'] is False,name
        assert record['finished_at_utc']>=record['started_at_utc'],name
        hashes=read_json(evidence/'checksums.json')
        actual={p.relative_to(evidence).as_posix() for p in evidence.rglob('*') if p.is_file()}
        assert actual==set(hashes)|{'checksums.json'},name+' unexpected/missing file'
        for relative,expected_hash in hashes.items():
            assert sha256(contained(evidence,relative))==expected_hash,name+'/'+relative
        checked_files+=len(hashes)
        if record['source_prepared']:
            if config['policy_id']=='aom-owner-fixed-modified-source-build-v3-source-root':
                assert (evidence/'source-root/command.log').read_text().strip().splitlines()[-1].strip()==record['source_root']
            context=evidence/'context'
            manifest=read_json(context/'context-manifest.json')
            assert (manifest['project'],manifest['bug_id'],manifest['revision'])==(record['project'],record['bug_id'],f"{record['bug_id']}f")
            assert manifest['selection_policy_id']==config['policy_id'] and manifest['contains_execution_logs'] is False
            assert sha256(context/'context-manifest.json')==record['context_manifest_sha256']
            canonical=json.dumps(manifest['source_files'],sort_keys=True,separators=(',',':')).encode()
            assert digest(canonical)==manifest['source_hash']==record['source_hash']
            source_names=set()
            for item in manifest['source_files']:
                path=contained(context/'fixed-source',item['path'])
                assert path.suffix=='.java' or path.name in BUILD_FILES
                assert path.stat().st_size==item['bytes'] and sha256(path)==item['sha256']
                if config['policy_id']=='aom-owner-fixed-modified-source-build-v3-source-root' and path.suffix=='.java':
                    assert Path(item['path']).is_relative_to(Path(record['source_root']))
                source_names.add(item['path'])
            assert source_names=={p.relative_to(context/'fixed-source').as_posix() for p in (context/'fixed-source').rglob('*') if p.is_file()}
            proof=read_json(evidence/'revision-proof.json')
            assert proof['verified'] is True and proof['head']==proof['fixed_tag_commit']
            assert proof['selected_source_changes']==''
            assert (evidence/'post-compile-source-diff/command.log').read_text().strip()==''
            compile_record=read_json(evidence/'compile/command.json')
            wanted='timeout' if compile_record['timed_out'] else 'passed' if compile_record['exit_code']==0 else 'failed'
            assert record['compile_status']==wanted
        else:
            assert record.get('error'),name+' missing failure reason'
    assert dict(Counter(r['status'] for r in index['records']))==index['statuses']
    assert dict(Counter(r['compile_status'] for r in index['records']))==index['compile_statuses']
    assert sum(r['source_prepared'] for r in index['records'])==index['source_prepared']
    if not allow_pending and pending:
        raise ValueError(f'{len(pending)} records still pending; final audit refused')
    return {'scope':'Fixed-source/build prerequisite bytes only; no primary acceptance.',
        'all_owner_bugs_attempted':not pending,'expected_bugs':len(expected),
        'pending_bugs':len(pending),'source_prepared':index['source_prepared'],
        'statuses':index['statuses'],'compile_statuses':index['compile_statuses'],
        'checked_artifact_files':checked_files,'index_sha256':sha256(folder/'index.json'),
        'config_sha256':sha256(folder/'config.json'),'ownership_sha256':sha256(ownership),
        'audit_implementation_sha256':sha256(__file__),
        'primary_completed':0,'real_kku_requests':0,'live_queue_mutations':0}


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--folder',type=Path,required=True)
    parser.add_argument('--ownership',type=Path,default=ROOT/'experiments/configs/api854-20261003/ownership.json')
    parser.add_argument('--installed',type=Path,default=ROOT/'output/api854-20261003/aom-installed-current.json')
    parser.add_argument('--allow-pending',action='store_true')
    parser.add_argument('--receipt',type=Path,required=True)
    args=parser.parse_args()
    result=audit(args.folder,args.ownership,args.installed,allow_pending=args.allow_pending)
    if args.receipt.exists():raise ValueError('Receipt exists; retain it and choose a new path')
    save(args.receipt,result)
    print(json.dumps(result))
