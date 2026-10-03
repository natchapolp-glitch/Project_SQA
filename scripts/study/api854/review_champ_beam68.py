"""Audit received first-round Beam evidence, preserving every failed outcome."""
import argparse
from collections import Counter
from datetime import datetime, timezone
import json
import tarfile
from pathlib import Path

from .common import ROOT, read_json, sha256, contained, write_json, implementation_hashes
from .preparation import clean_targets, POLICY_V6, encoded, digest


def inspect():
    packet = ROOT/'docs/api854/evidence/beam-pilot-v5-review-20261003'
    index = read_json(packet/'index.json')
    hashes = read_json(packet/'checksums.json')
    for name, expected in hashes.items():
        assert sha256(contained(packet,name)) == expected, name
    assert len(hashes) == 1040 and len(index['records']) == 30
    protocol_path = packet/'protocol-proposal.json'
    assert sha256(protocol_path) == index['protocol_sha256']
    old_protocol = read_json(protocol_path)
    for name, expected in old_protocol['source_sha256'].items():
        assert sha256(contained(packet/'runtime-implementation',name)) == expected
    assert len(old_protocol['source_sha256']) == 40
    outcomes, reviews, rows = Counter(), Counter(), []
    for row in index['records']:
        folder = packet/f"{row['project']}-{row['bug_id']}-{row['approach']}"
        original = read_json(folder/'original-evaluation-result.json')
        generated = read_json(folder/'original-generation-result.json')
        assert original['job']['protocol_hash'] == generated['job']['protocol_hash'] == index['protocol_sha256']
        assert original['observed_outcome'] == row['evaluation_outcome'] and original['usable'] is False
        assert sha256(folder/'suite.tar.bz2') == row['suite_sha256'] == original['suite_sha256']
        with tarfile.open(folder/'suite.tar.bz2','r:bz2') as archive:
            sources = [archive.extractfile(member).read() for member in archive if member.isfile() and member.name.endswith('.java')]
        assert sources == [(folder/'generation/GeneratedStudyTest.java').read_bytes()]
        outcome, verdict = row['evaluation_outcome'], row['semantic_review']['verdict']
        outcomes[outcome] += 1
        reviews[verdict] += 1
        if outcome == 'complete':
            semantic = read_json(folder/'semantic-review-result.json')
            assert semantic['evaluation_result_sha256'] == sha256(folder/'original-evaluation-result.json')
            assert semantic['status'] == semantic['review']['verdict'] == verdict
            assert semantic['review']['suite_sha256'] == row['suite_sha256']
            assert semantic['review']['fixture_oracle_review']['team_or_primary_approval'] is False
            for stage in ('fixed-1','fixed-2','buggy','coverage'):
                counts = read_json(folder/stage/'sqa-stage-counts.json')
                assert all(counts[k] == v for k,v in {'executed':30,'skipped':0,'target_checks':30}.items())
        rows.append({'project':row['project'],'bug_id':row['bug_id'],'approach':row['approach'],
                     'evaluation_outcome':outcome,'local_review':verdict,'original_usable':False,
                     'suite_sha256':row['suite_sha256'],'evaluation_result_sha256':sha256(folder/'original-evaluation-result.json')})
    assert outcomes == {'complete':26,'environment_failed':2,'fixed_failed':2}
    assert reviews == {'valid':24,'invalid':2,'incomplete_evaluation':4}
    capability = ROOT/'docs/api854/evidence/beam-pilot-v5-capabilities-20261003'
    caps = read_json(capability/'index.json')
    original_index = read_json(ROOT/'output/api854-20261003/prepare-v3/index.json')
    originals = {(r['project'],r['bug_id']):r for r in original_index['records']}
    ids = lambda targets: {tuple(t[k] for k in ('class','constructor_types','method','parameter_types')) for t in clean_targets(targets)}
    count, kept, excluded = 0,0,0
    for row in caps['bugs']:
        artifact = contained(capability,row['artifact'])
        assert sha256(artifact) == row['sha256']
        document = read_json(artifact)
        old = ROOT/'output/api854-20261003/prepare-v3'/f"{row['project']}-{row['bug_id']}"
        all_ids = ids(read_json(old/'targets.json')['targets'])
        chosen = ids(document['targets'])
        # Capability export uses an explicit exclusion list; retain its identities.
        exclusion_rows = document['excluded_fixture_capabilities']
        missing = ids([r['target'] for r in exclusion_rows])
        assert chosen.isdisjoint(missing) and chosen|missing == all_ids
        assert len(all_ids) == row['original_shared_declarations'] == originals[(row['project'],row['bug_id'])]['target_count']
        candidate_folder = ROOT/'output/api854-20261003/prepare-v6-twenty-bug-development'/f"{row['project']}-{row['bug_id']}"
        candidate_capability = read_json(candidate_folder/'capability-exclusions.json')
        assert ids(candidate_capability['selected']) == chosen
        assert ids([r['target'] for r in candidate_capability['excluded']]) == missing
        count += len(all_ids); kept += len(chosen); excluded += len(missing)
    assert len(caps['bugs']) == 20 and (count,kept,excluded) == (691,377,314)
    current = implementation_hashes()
    diffs = {name:{'received_execution_sha256':expected,'current_sha256':current.get(name)}
             for name,expected in old_protocol['source_sha256'].items() if current.get(name) != expected}
    prep = ROOT/'output/api854-20261003/prepare-v6-twenty-bug-development/index.json'
    candidate = read_json(prep)
    assert candidate['runtime_source_sha256'] == current and candidate['policy_sha256'] == digest(encoded(POLICY_V6))
    assert candidate['target_count'] == kept and candidate['capability_exclusion_count'] == excluded
    return {'checked_at_utc':datetime.now(timezone.utc).isoformat(),
        'received_champ_commit':'8e350c6853eed252c4c0642319b043da50d64269',
        'received_packet_checksum_entries_verified':len(hashes),'received_runtime_files_verified':40,
        'packet_index_sha256':sha256(packet/'index.json'),'packet_checksum_sha256':sha256(packet/'checksums.json'),
        'capability_index_sha256':sha256(capability/'index.json'),'received_bugs':15,'received_suites':30,
        'measurement_outcomes':dict(outcomes),'local_review_outcomes':dict(reviews),'suites':rows,
        'common_declarations':691,'candidate_selected_declarations':377,'candidate_unsupported_declarations':314,
        'candidate_preparation_index_sha256':sha256(prep),'candidate_bugs':20,
        'candidate_max_prompt_utf8_bytes':candidate['max_prompt_utf8_bytes'],
        'candidate_reservation_byte_floor_before_unknown_framing':candidate['max_prompt_utf8_bytes']+4096,
        'actual_provider_token_count':None,'final_prompt_reserve':None,
        'received_execution_runtime_differences':diffs,'current_runtime_additions':sorted(set(current)-set(old_protocol['source_sha256'])),
        'review_implementation_sha256':sha256(__file__),
        'scope':'Received first-round development evidence audit plus current development candidate; no experiment rerun.',
        'gate_a_passed':False,'generation_authorized':False,'primary_completed':0,'live_requests':0,'queue_mutations':0}


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    args=parser.parse_args()
    result=inspect()
    write_json(args.output,result)
    print(json.dumps({'received_bugs':15,'received_suites':30,'candidate_bugs':20,'selected':377,'unsupported':314}))
