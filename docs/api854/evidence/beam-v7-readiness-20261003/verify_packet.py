"""Verify retained diagnostics and the new setter recipe, without granting approval."""
import json
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
from scripts.study.api854.common import sha256, write_json

load = lambda path: json.loads(path.read_text(encoding='utf-8'))
base = Path(__file__).parent
sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
recipe = ROOT / 'docs/api854/evidence/beam-v7-setter-recipe-20261003'
counts = {}
for packet in (sweep, recipe):
    files = load(packet / 'checksums.json')
    for name, expected in files.items():
        path = (packet / name).resolve(strict=True)
        assert path.is_relative_to(packet.resolve()) and sha256(path) == expected, name
    counts[packet.name] = len(files)
index = load(sweep / 'index.json')
summary = load(base / 'sweep-summary.json')
assert len(index['records']) == 20 and summary['complete_sweep']
assert sum(len(row['cases']) for row in index['records']) == 691
assert len(summary['unsupported_worklist']) == 314
assert len({tuple((row['project'],row['bug_id'])) for row in index['records']}) == 20
for row in index['records']:
    assert row['status'] == 'fixed_sweep_complete'
    assert not row['fixed_source_comparison']['mismatches']
    assert row['fixed_source_comparison']['matched']
result = load(recipe / 'evaluation/record.json')
assert result['status'] == 'complete'
stages = {}
for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
    actual = load(recipe / 'evaluation' / stage / 'sqa-stage-counts.json')
    assert actual == {'schema_version':1,'executed':4,'skipped':0,'target_checks':4}, (stage, actual)
    stages[stage] = actual
root = ET.parse(recipe / 'evaluation/coverage/coverage.xml').getroot()
setter_hits = [int(line.get('hits','0')) for cls in root.iter('class')
    if cls.get('name') == 'org.apache.commons.codec.language.Metaphone'
    for method in cls.findall('./methods/method') if method.get('name') == 'setMaxCodeLen'
    for line in method.findall('./lines/line')]
assert setter_hits and any(hit > 0 for hit in setter_hits)
write_json(base / 'verification.json', {'status':'pass', 'packet_files_checked':counts,
    'sweep_declarations':691,'unsupported_worklist':314,'source_comparison':'all_fixed_java_hashes_match',
    'recipe_status':result['status'],'recipe_fault_detected':result['fault_detected'],
    'recipe_stage_counts':stages, 'setter_coverage_line_hits':setter_hits,
    'validation_log_sha256':sha256(base / 'validation.log'), 'verifier_sha256':sha256(__file__),
    'primary':False,'team_semantic_approval':False,'gate_a_passed':False})
print(json.dumps({'status':'pass','sweep_declarations':691,'recipe_status':result['status'],
                  'setter_coverage_line_hits':setter_hits}))
