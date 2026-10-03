"""Regression probe for an actually traversed JDOM attribute, fixed revision only."""
import json
from pathlib import Path
import sys
ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / 'scripts/study'))
from generate import observe
from scripts.study.api854.common import write_json, sha256

base = Path(__file__).parent
sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
cases = json.loads((sweep / 'JxPath-1/record.json').read_text())['cases']
case = next(c for c in cases if c['v7_capability'] == 'selected' and c['first']['status'] != 'ok')
cp = (sweep / 'JxPath-1/cp.test.txt').read_text().strip()
candidate = '--candidate' in sys.argv
cp += ':' + str(base / 'candidate-classes' if candidate else sweep / 'probe-classes')
policy = 'beam-jdom-attribute-oracle-v6-development' if candidate else 'beam-explicit-fixtures-v5-proposal'
first = observe(cp, case['target'], case['vector'], 20, policy)
second = observe(cp, case['target'], case['vector'], 20, policy)
passed = first['status'] == 'ok' and first == second and first.get('target_invoked') is True
if candidate:
    passed = passed and 'jdom-attribute:' in first.get('outcome','')
write_json(base / ('green.json' if candidate else 'red.json'), {
    'passed':passed,'candidate':candidate,'target':case['target'],'first':first,'second':second,
    'regression_probe_sha256':sha256(__file__),'primary':False,'shared_approval':False})
print(json.dumps({'passed':passed,'candidate':candidate,'first':first}))
raise SystemExit(0 if passed else 1)
