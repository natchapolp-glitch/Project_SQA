"""Require a structural oracle for both real fraction field singleton returns."""
import json
from pathlib import Path
import sys
ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT)); sys.path.insert(0,str(ROOT / 'scripts/study'))
from generate import observe
from scripts.study.api854.common import sha256, write_json

base = Path(__file__).parent
sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
cases = json.loads((sweep / 'Math-1/record.json').read_text())['cases']
cases = [c for c in cases if c['target']['method']=='getField']
assert len(cases) == 2
candidate = '--candidate' in sys.argv
cp = (sweep / 'Math-1/cp.test.txt').read_text().strip() + ':' + str(base / 'candidate-classes' if candidate else sweep / 'probe-classes')
policy = 'beam-champ-fraction-field-v6-development' if candidate else 'beam-explicit-fixtures-v5-proposal'
records = []
for case in cases:
    first = observe(cp,case['target'],case['vector'],20,policy)
    second = observe(cp,case['target'],case['vector'],20,policy)
    passed = first['status']=='ok' and first==second and first.get('target_invoked') is True
    if candidate:
        passed = passed and 'fraction-field:' in first['outcome'] and ':zero=fraction:0/1:one=fraction:1/1' in first['outcome']
    records.append({'target':case['target'],'first':first,'second':second,'passed':passed})
passed = all(row['passed'] for row in records)
write_json(base / ('green.json' if candidate else 'red.json'), {'passed':passed,'candidate':candidate,
    'records':records,'regression_probe_sha256':sha256(__file__),'primary':False,'joint_semantic_approval':False})
print(json.dumps({'passed':passed,'candidate':candidate,'statuses':[r['first']['status'] for r in records]}))
raise SystemExit(0 if passed else 1)
