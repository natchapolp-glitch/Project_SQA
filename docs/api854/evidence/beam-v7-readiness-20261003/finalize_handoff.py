"""Bind the reviewed development handoff without altering measured receipts."""
import json
from pathlib import Path
import sys
ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0,str(ROOT))
from scripts.study.api854.common import sha256, write_json
base = Path(__file__).parent
summary = json.loads((base / 'sweep-summary.json').read_text(encoding='utf-8'))
assert summary['summarizer_sha256'] == sha256(base / 'summarize_sweep.v1.py')
docs = ('BEAM_V7_READINESS_WORK_TH.md','BEAM_V7_ORACLE_REVIEW_TH.md','BEAM_V7_ENUM_DECISION_TH.md')
write_json(base / 'handoff.json', {'source_base':'c25faa5e880583012156cd4d60fa0743beead4e4',
    'local_branch':'codex/beam-readiness','scope':'Offline Beam development evidence and joint-review handoff',
    'fixed_probe_attempts':1382,'declarations_probed':691,'fixture_errors':127,
    'normal_stable_observations':514,'target_exceptions_needing_review':50,
    'unsupported_shared_declarations':314,'shared_target_support_approved':False,
    'historical_summarizer_path':'summarize_sweep.v1.py',
    'report_wording_correction':'Probed all declarations twice; fixture_error does not certify target invocation.',
    'existing_validation_tests_passed':18,
    'development_suites':[{'target':'Metaphone.setMaxCodeLen(int)','tests':4,'fixed_runs':2,'buggy':True,'target_coverage':True},
                          {'target':'JDOMNodePointer.attributeIterator(QName)','tests':2,'fixed_runs':2,'buggy':True,'target_coverage':True}],
    'code_review':'Independent reviewer found only the probe/invocation wording issue; corrected in current report/template.',
    'docs_sha256':{name:sha256(ROOT / 'docs/api854' / name) for name in docs},
    'verification_sha256':sha256(base / 'verification.json'),
    'final_verification_sha256':sha256(base / 'verification-v2.json'),
    'primary_completed':0,'gate_a_passed':False,'team_semantic_approval':False,
    'real_kku_requests':0,'queue_mutations':0})
files = {p.relative_to(base).as_posix():sha256(p) for p in base.rglob('*')
         if p.is_file() and '__pycache__' not in p.parts and p.name != 'checksums.json'}
write_json(base / 'checksums.json',files)
print(json.dumps({'readiness_handoff_files':len(files),'status':'bound'}))
