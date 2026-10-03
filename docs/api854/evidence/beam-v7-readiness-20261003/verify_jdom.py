"""Verify the isolated oracle candidate, its red/green regression and real coverage."""
import json
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
from scripts.study.api854.common import sha256, write_json
load = lambda p: json.loads(p.read_text(encoding='utf-8'))
base = Path(__file__).parent
packet = ROOT / 'docs/api854/evidence/beam-v7-jdom-oracle-20261003'
files = load(packet / 'checksums.json')
for name, expected in files.items():
    path = (packet / name).resolve(strict=True)
    assert path.is_relative_to(packet.resolve()) and sha256(path) == expected, name
assert load(packet / 'red.json')['passed'] is False
assert load(packet / 'red.json')['first']['reason'] == 'No structural oracle: org.jdom.Attribute'
assert load(packet / 'green.json')['passed'] is True
receipt = load(packet / 'receipt.json')
assert sha256(ROOT / 'algorithms/java/SqaProbe.java') == receipt['original_probe_sha256']
assert sha256(packet / 'helper-src/algorithms/java/SqaProbe.java') == receipt['candidate_probe_sha256']
observations = load(packet / 'observations.json')
assert len(observations) == 2 and observations[0]['fixed_first']['outcome'] != observations[1]['fixed_first']['outcome']
for row in observations:
    assert row['fixed_first'] == row['fixed_second'] and row['fixed_first']['target_invoked'] is True
    assert 'jdom-attribute:' in row['fixed_first']['outcome']
result = load(packet / 'evaluation/record.json')
assert result['status'] == 'complete'
counts = {}
for stage in ('fixed-1','fixed-2','buggy','coverage'):
    counts[stage] = load(packet / 'evaluation' / stage / 'sqa-stage-counts.json')
    assert counts[stage] == {'schema_version':1,'executed':2,'skipped':0,'target_checks':2}
xml = ET.parse(packet / 'evaluation/coverage/coverage.xml').getroot()
hits = [int(line.get('hits','0')) for cls in xml.iter('class')
    if cls.get('name') == 'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'
    for method in cls.findall('./methods/method') if method.get('name') == 'attributeIterator'
    for line in method.findall('./lines/line')]
assert hits and any(hit > 0 for hit in hits)
write_json(base / 'verification-v2.json', {'status':'pass','prior_verification_sha256':sha256(base / 'verification.json'),
    'jdom_packet_files_checked':len(files),'red_failed_for_missing_attribute_oracle':True,'green_passed':True,
    'actual_attribute_observations_differ':True,'stage_counts':counts,'attribute_iterator_coverage_hits':hits,
    'runtime_probe_unchanged':True,'fault_detected':result['fault_detected'], 'verifier_sha256':sha256(__file__),
    'primary':False,'shared_approval':False,'gate_a_passed':False})
print(json.dumps({'status':'pass','attribute_iterator_coverage_hits':hits,'jdom_files_checked':len(files)}))
