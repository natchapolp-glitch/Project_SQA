"""Export public development receipts without changing any historical record."""
import hashlib
import json
from pathlib import Path
import shutil

def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def write(path, obj):
    with path.open('x', encoding='utf-8') as file:
        json.dump(obj, file, ensure_ascii=False, indent=2)
        file.write('\n')

base = Path('docs/api854/evidence')
out = base / 'beam-pilot-v5-repair-handoff-20261003'
out.mkdir(exist_ok=False)
refs = []
selected = {}
for name in ['beam-pilot-v5-review-20261003', 'beam-pilot-v5-repair-review-20261003']:
    packet = base / name
    checks = json.loads((packet / 'checksums.json').read_text())
    for relative, expected in checks.items():
        assert digest(packet / relative) == expected, relative
    index = json.loads((packet / 'index.json').read_text())
    reviews = json.loads((packet / 'semantic-review-index.json').read_text())
    records = {(row['project'], row['bug_id'], row['approach']): row for row in index['records']}
    refs.append({'packet': name, 'index_sha256': digest(packet / 'index.json'),
                 'review_index_sha256': digest(packet / 'semantic-review-index.json'),
                 'checksums_sha256': digest(packet / 'checksums.json')})
    for review in reviews['reviews']:
        key = (review['project'], review['bug_id'], review['approach'])
        if review['verdict'] != 'valid' or not review['local_development_usable']:
            continue
        row = records[key]
        assert row['evaluation_outcome'] == 'complete'
        assert row['declared_test_count'] == 30
        for stage in ['fixed-1', 'fixed-2', 'buggy', 'coverage']:
            counts = row['stage_counts'][stage]
            assert counts['executed'] == counts['target_checks'] == 30 and counts['skipped'] == 0
        selected[key] = {'project':key[0], 'bug_id':key[1], 'approach':key[2],
                         'packet':name, 'suite_sha256':row['suite_sha256'],
                         'protocol_sha256':index['protocol_sha256'],
                         'local_development_verdict':'valid', 'team_or_primary_approval':False,
                         'fixed_repetitions':2, 'buggy_and_coverage_complete':True,
                         'test_count':30, 'fault_detected':row['fault_detected']}
assert len(selected) == 30
assert len({key[:2] for key in selected}) == 15
for directory, names in [
    ('environment-repair-cli-v5', ['repair.json', 'Cli.build.original.xml', 'Cli.build.patched.xml']),
    ('pilot-fixtures-v5-1-cpu-slot', ['held-slot-receipt.json', 'released-slot-receipt.json'])]:
    source = Path('output/api854-beam') / directory
    (out / directory).mkdir()
    for name in names:
        shutil.copyfile(source / name, out / directory / name)
checker = Path('tmp/check-real-beam-cpu-slot-v5.py')
shutil.copyfile(checker, out / 'cpu-slot-checker.py')
for name in ['held-slot-receipt.json', 'released-slot-receipt.json']:
    receipt = json.loads((out / 'pilot-fixtures-v5-1-cpu-slot' / name).read_text())
    assert receipt['passed'] and receipt['physical_hosts'] == receipt['cpu_slots'] == 1
    assert receipt['checker_sha256'] == digest(out / 'cpu-slot-checker.py')
repair = json.loads((out / 'environment-repair-cli-v5/repair.json').read_text())
assert repair['before_sha256'] == digest(out / 'environment-repair-cli-v5/Cli.build.original.xml')
assert repair['after_sha256'] == digest(out / 'environment-repair-cli-v5/Cli.build.patched.xml')
write(out / 'index.json', {
    'scope':'15 bugs / 30 development suites; multiple runtime/protocol pins retained honestly',
    'primary':False, 'gate_a_approved':False, 'team_or_primary_approval':False,
    'worker_id':'beam-pc1', 'physical_hosts':1, 'cpu_slots':1,
    'real_kku_requests':0, 'live_queue_mutations':0,
    'bug_count':15, 'suite_count':30, 'declaration_scope_complete':False,
    'required_declarations':691, 'sampler_recipe_scope':377,
    'packet_refs':refs, 'records':[selected[key] for key in sorted(selected)],
    'retention':'All earlier failures, measurements and source pins remain in their original packets.',
    'limitations':['This consolidates sampled suite evidence, not all 691 declarations.',
                   'It does not relabel shared preparation v5 or freeze a new shared protocol.',
                   'Other team hosts and joint semantic review require their own acceptance.'],
    'producer_sha256':digest(Path(__file__))})
shutil.copyfile(__file__, out / 'export-repair-handoff.py')
write(out / 'checksums.json', {str(p.relative_to(out)):digest(p) for p in sorted(out.rglob('*')) if p.is_file()})
print('Exported 15 bugs / 30 valid development suites and one-host/environment evidence')
