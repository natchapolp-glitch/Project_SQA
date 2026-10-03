"""Seal an incremental composition audit/worklist and prompt worksheet, without approvals."""
import argparse
import copy
from collections import Counter
from pathlib import Path

from .common import ROOT, read_json, sha256, contained, write_json
from .gate_a import inspect
from .preparation import clean_targets
from .build_prepare_v8_development import receive, ACCEPTANCE, ACCEPTANCE_SHA256


def audit(protocol_path,runner_path,output):
    protocol_path,runner_path,output = map(Path,(protocol_path,runner_path,output))
    accepted = receive()
    gate = inspect(protocol_path=protocol_path,runner_path=runner_path)
    checks = {r['id']:r for r in gate['checklist']}
    required = ('prepare_contract','protocol_runner_binding','preparation_index_binding','runtime_source_binding',
                'shared_policy','fixture_recipe_binding','runner_coverage','eligible_targets_20')
    if any(checks[name]['status'] != 'pass' for name in required) or gate['gate_a_passed']:
        raise ValueError('Require valid incremental input bindings with final Gate A still closed')
    protocol = read_json(protocol_path)
    preparation = contained(ROOT,protocol['preparation_artifacts'])
    index = read_json(preparation/'index.json')
    old = ROOT/'output/api854-20261003/prepare-v7-twenty-bug-development'
    original = read_json(old/'index.json')
    for row in original['records']:
        folder = old/f"{row['project']}-{row['bug_id']}"
        for name,expected in read_json(folder/'checksums.json').items():
            if sha256(contained(folder,name)) != expected:
                raise ValueError('Historical v7 evidence changed')
    output.mkdir(parents=True,exist_ok=False)
    additions,bugs,reasons,prompts = [],[],Counter(),[]
    for row in index['records']:
        name = f"{row['project']}-{row['bug_id']}"
        folder = preparation/name
        before = clean_targets(read_json(old/name/'targets.json')['targets'])
        after = clean_targets(read_json(folder/'targets.json')['targets'])
        if any(t not in after for t in before):
            raise ValueError('Historical selected declaration was removed')
        delta = [t for t in after if t not in before]
        if delta and name != 'Math-1':
            raise ValueError('Unexpected addition outside Math-1')
        additions.extend(delta)
        capability = read_json(folder/'capability-exclusions.json')
        previous_exclusions = read_json(old/name/'capability-exclusions.json')['excluded']
        if capability['excluded'] != [r for r in previous_exclusions if r['target'] not in delta]:
            raise ValueError('Unaccepted exclusions or their reasons changed')
        reasons.update(r['reason'] for r in capability['excluded'])
        bugs.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
            'required_common_declarations':len(after)+len(capability['excluded']),
            'selected':len(after),'unsupported':len(capability['excluded']),
            'unsupported_targets':capability['excluded'],'accepted_additions':delta,'shared_semantic_approval':False,
            'prompt_utf8_bytes':row['prompt_utf8_bytes'],'prompt_sha256':row['prompt_sha256']})
        prompts.append({'project':row['project'],'bug_id':row['bug_id'],'owner':row['owner'],
            'prompt_path':(folder/'prompt.md').relative_to(ROOT).as_posix(),'prompt_sha256':sha256(folder/'prompt.md'),
            'prompt_utf8_bytes':(folder/'prompt.md').stat().st_size,
            'request_byte_floor_before_unknown_framing':row['prompt_utf8_bytes']+4096})
    if (clean_targets(additions) != clean_targets([r['target'] for r in accepted['accepted_candidates']])
            or sum(r['selected'] for r in bugs)!=379 or sum(r['unsupported'] for r in bugs)!=312):
        raise ValueError('Audit differs from exact accepted increment')
    enum = copy.deepcopy(read_json(ROOT/'output/api854-20261003/aom-v7-readiness-worklist-v1.json')['enum_decision'])
    enum['source_path'] = enum['source_path'].replace(old.relative_to(ROOT).as_posix(),preparation.relative_to(ROOT).as_posix())
    if sha256(ROOT/enum['source_path']) != enum['source_sha256']:
        raise ValueError('Historical empty enum source changed')
    write_json(output/'worklist.json',{'scope':'Remaining 312 common signatures; not the Champ-owner-only 167 subset',
        'accepted_count':2,'champ_owner_review_remaining':167,'selected':379,'unsupported':312,
        'required_common_declarations':691,'bugs':bugs,'unsupported_reason_counts':dict(reasons),
        'enum_decision':enum,'preparation_index_sha256':sha256(preparation/'index.json'),
        'gate_a_passed':False,'primary_completed':0,'live_requests':0,'queue_mutations':0})
    write_json(output/'prompt-reserve-worksheet.json',{'scope':'New composed prompt bytes only; Champ must add framing and actual provider accounting',
        'protocol_sha256':sha256(protocol_path),'preparation_index_sha256':sha256(preparation/'index.json'),
        'records':prompts,'max_prompt_utf8_bytes':max(r['prompt_utf8_bytes'] for r in prompts),
        'max_request_byte_floor_before_unknown_framing':max(r['request_byte_floor_before_unknown_framing'] for r in prompts),
        'requested_max_output_tokens':4096,'requested_temperature':0,
        'framing_reserve':None,'actual_provider_token_count':None,'final_reserve':None,
        'limits_bucket_reset_expiry_review_received':False,'live_requests':0})
    write_json(output/'gate-a-checklist.json',gate)
    write_json(output/'receipt.json',{'scope':'Aom composition acceptance of two candidates only; final protocol not frozen',
        'accepted_additions':clean_targets(additions),'acceptance':{'path':ACCEPTANCE,'sha256':ACCEPTANCE_SHA256},
        'beam_source_commit':'0b560f058b811c8d9891b93f7606654106691766',
        'champ_source_commit':'4c9ccf7ed5fcaee1fb0c70a2b7249da61c17e8f6',
        'historical_v7_artifact_checksums_verified':True,'old_index_sha256':sha256(old/'index.json'),
        'protocol_path':protocol_path.relative_to(ROOT).as_posix(),'protocol_sha256':sha256(protocol_path),
        'runner_path':runner_path.relative_to(ROOT).as_posix(),'runner_sha256':sha256(runner_path),
        'new_index_sha256':sha256(preparation/'index.json'),'inspector_sha256':sha256(__file__),
        'builder_sha256':sha256(ROOT/'scripts/study/api854/build_prepare_v8_development.py'),
        'composer_sha256':sha256(ROOT/'scripts/study/api854/compose_v8_development.py'),
        'bugs':20,'selected':379,'unsupported':312,'required_common_declarations':691,
        'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],'gate_a_passed':False,
        'generation_authorized':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0})
    write_json(output/'checksums.json',{p.name:sha256(p) for p in output.iterdir() if p.is_file()})
    return {'selected':379,'unsupported':312,'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],'gate_a_passed':False}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--protocol',type=Path,required=True)
    parser.add_argument('--runner',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    print(audit(args.protocol.resolve(),args.runner.resolve(),args.output.resolve()))
