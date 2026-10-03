"""Check received Beam evidence without running remote commands or changing recipes."""
from collections import Counter
from datetime import datetime, timezone
from pathlib import Path
import xml.etree.ElementTree as ET
from scripts.study.api854.common import ROOT, read_json, write_json, sha256, contained, implementation_hashes
from scripts.study.api854.inspect_v7_readiness import identities

E = ROOT / 'docs/api854/evidence'
OUT = ROOT / 'output/api854-provider-preflight-20261003'
packets = ['beam-v7-fixed-sweep-20261003','beam-v7-readiness-20261003',
           'beam-v7-setter-recipe-20261003','beam-v7-jdom-oracle-20261003']
verified = {}
for name in packets:
    folder = E / name
    checks = read_json(folder / 'checksums.json')
    for path, expected in checks.items():
        assert sha256(contained(folder, path)) == expected, (name,path)
    verified[name] = {'checksum_entries':len(checks),'checksum_manifest_sha256':sha256(folder/'checksums.json')}
sweep = read_json(E / packets[0] / 'index.json')
received = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development'
current = ROOT / 'output/api854-20261003/champ-prepare-v7-current-v1'
assert sweep['preparation_index_sha256'] == sha256(received / 'index.json')
assert read_json(current/'index.json')['runtime_source_sha256'] == implementation_hashes()
categories, unsupported, champ = Counter(), Counter(), Counter()
selected_errors, enums = [], []
assert len(sweep['records']) == 20
for record in sweep['records']:
    name = f"{record['project']}-{record['bug_id']}"
    assert record['status'] == 'fixed_sweep_complete'
    cases = record['cases']
    expected = read_json(ROOT/'output/api854-20261003/prepare-v3'/name/'targets.json')['targets']
    assert len(cases) == len(identities(expected))
    assert identities([c['target'] for c in cases]) == identities(expected)
    manifest = read_json(current/name/'context-manifest.json')
    source_hashes = {s['path']:s['sha256'] for s in manifest['source_files'] if s['path'].endswith('.java')}
    compared = record['fixed_source_comparison']
    assert not compared['mismatches']
    assert {s['path']:s['actual'] for s in compared['matched']} == source_hashes
    partition = read_json(current/name/'capability-exclusions.json')
    selected = identities(partition['selected'])
    for n,case in enumerate(cases):
        assert read_json(E/packets[0]/name/f'case-{n:03d}.json') == case
        ident = next(iter(identities([case['target']])))
        assert (ident in selected) == (case['v7_capability']=='selected')
        first = case['first']
        category = (first['status'] if first['status'] != 'ok' else
                    'repeat_unstable' if not case['repeat_equal'] else
                    'invocation_unverified' if first.get('target_invoked') is not True else
                    'target_exception_review_needed' if first.get('outcome','').startswith('exception:') else
                    'stable_normal_observation_oracle_review_needed')
        categories[category] += 1
        if case['v7_capability'] == 'unsupported':
            unsupported[category] += 1
            if record['owner']=='champ': champ[category] += 1
        elif category == 'fixture_error':
            selected_errors.append({'bug':name,'target':case['target'],'reason':first['reason']})
        if 'FromXmlParser$Feature' in case['target']['parameter_types']:
            assert first.get('target_invoked') is True and first.get('outcome')=='exception:java.lang.NullPointerException'
            enums.append(case['target'])
assert sum(categories.values()) == 691 and sum(unsupported.values()) == 314 and sum(champ.values()) == 169
assert len(enums) == 4 and len(selected_errors) == 1
assert categories == read_json(E/packets[1]/'sweep-summary.json')['category_counts']
proofs=[]
for foldername, count, method in [(packets[2],4,'setMaxCodeLen'),(packets[3],2,'attributeIterator')]:
    folder=E/foldername
    receipt=read_json(folder/'receipt.json')
    result=read_json(folder/'evaluation/record.json')
    assert receipt['primary'] is False and receipt['team_semantic_approval'] is False
    assert sha256(folder/'evaluation/record.json')==receipt['result_sha256']
    assert sha256(folder/'package/suite.tar.bz2')==receipt['suite_sha256']==result['suite_sha256']
    assert result['status']=='complete' and result['fixed_validation']=='passed_twice'
    for stage in ['fixed-1','fixed-2','buggy','coverage']:
        counts=read_json(folder/'evaluation'/stage/'sqa-stage-counts.json')
        assert counts['executed']==count and counts['skipped']==0 and counts['target_checks']==count
        assert result['stages'][stage]['exit_code']==0
    xml=ET.parse(folder/'evaluation/coverage/coverage.xml')
    methods=[m for m in xml.findall('.//method') if m.get('name')==method]
    assert methods and any(int(line.get('hits','0'))>0 for m in methods for line in m.findall('.//line'))
    proofs.append({'packet':foldername,'tests_per_stage':count,'target_method':method,'target_method_covered':True,
                   'fault_detected':result['fault_detected'],'receipt_sha256':sha256(folder/'receipt.json'),
                   'scope':'Received development proof; not rerun or final shared approval'})
assert read_json(E/packets[3]/'red.json')['passed'] is False
assert read_json(E/packets[3]/'green.json')['passed'] is True
result={'checked_at_utc':datetime.now(timezone.utc).isoformat(),'beam_commit':'532baa317cd0c6895a0f1d7a3b1ca9f5544132f3',
        'packets':verified,'diagnostic_declarations':691,'categories':dict(categories),'unsupported_categories':dict(unsupported),
        'champ_unsupported_categories':dict(champ),'selected_fixture_errors':selected_errors,'development_proofs':proofs,
        'enum_targets':enums,'enum_joint_decision':None,'current_runtime_binding_unchanged':True,
        'current_preparation_index_sha256':sha256(current/'index.json'),'source_comparisons_verified':True,
        'recipe_policy_changes':0,'closed_unsupported_declarations':0,'primary_results_added':0,
        'gate_a_passed':False,'generation_authorized':False,'live_api_requests':0,'queue_mutations':0,
        'final_preparation_received':False,'auditor_sha256':sha256(__file__)}
write_json(OUT/'champ-beam532-intake-v1.json',result)
print({'verified_checksum_entries':sum(v['checksum_entries'] for v in verified.values()),'diagnostics':691,
       'unsupported_categories':dict(unsupported),'champ_categories':dict(champ),'proofs':len(proofs)})
