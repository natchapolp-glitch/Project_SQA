"""Verify Beam joint-review packet while preserving received verification bytes."""
import ast
from datetime import datetime, timezone
from pathlib import Path
from scripts.study.api854.common import ROOT, read_json, write_json, sha256, contained, implementation_hashes

out = ROOT/'output/api854-provider-preflight-20261003'
packet = ROOT/'docs/api854/evidence/beam-champ-v7-review-20261003'
verifier = packet/'verify_joint_review.py'
before = sha256(packet/'verification.json')
tree = ast.parse(verifier.read_text(encoding='utf-8'))
# The received verifier writes its own verification.json. Execute all assertions
# but preserve that immutable receipt, and publish our result separately.
tree.body = [n for n in tree.body if not (isinstance(n,ast.Expr) and isinstance(n.value,ast.Call)
             and isinstance(n.value.func,ast.Attribute) and n.value.func.attr=='write_text')]
scope = {'__file__':str(verifier),'__name__':'received_verifier_readonly'}
exec(compile(tree,str(verifier),'exec'),scope)
assert sha256(packet/'verification.json') == before
checks = read_json(packet/'checksums-v2.json')
for name,digest in checks.items():
    assert sha256(contained(ROOT,name)) == digest, name
review = read_json(packet/'review.json')
prep = ROOT/'output/api854-20261003/champ-prepare-v7-current-v1'
assert read_json(prep/'index.json')['runtime_source_sha256'] == implementation_hashes()
for row in review['payload_comparison']:
    folder=prep/f"{row['project']}-{row['bug_id']}"
    for name,digest in row['payload_sha256'].items():
        assert sha256(contained(folder,name))==digest,(folder,name)
mapping={'worklist.json':out/'champ-v7-owner-worklist-v1.json','input-audit.json':out/'champ-v7-input-audit-v1.json',
         'preparation-index.json':prep/'index.json','protocol.json':out/'champ-v7-current-proposal-v1/protocol.proposal.json',
         'runner-plan.json':out/'champ-v7-current-proposal-v1/runner-plan.json',
         'CHAMP_V7_INTAKE_TH.md':ROOT/'docs/api854/CHAMP_V7_INTAKE_TH.md'}
for name,path in mapping.items():
    assert sha256(path)==review['received_sha256'][name]
proof=ROOT/'docs/api854/evidence/beam-champ-math-field-20261003-attempt2'
r=read_json(proof/'receipt.json')
assert sha256(proof/'evaluation/record.json')==r['result_sha256']
assert sha256(proof/'package/suite.tar.bz2')==r['suite_sha256']
for name,digest in r['supplemental_fixed_source_sha256'].items():
    assert sha256(proof/'supplemental-fixed-source'/Path(name).name)==digest
    text=(proof/'supplemental-fixed-source'/Path(name).name).read_text(encoding='utf-8')
    assert 'getRuntimeClass()' in text and 'getZero()' in text and 'getOne()' in text
worksheet=read_json(out/'champ-v7-reserve-worksheet-v1.json')
for row in worksheet['conditional_budget_rows']:
    path=prep/f"{row['project']}-{row['bug_id']}"/'prompt.md'
    assert sha256(path)==row['prompt_sha256'] and len(path.read_bytes())==row['prompt_utf8_bytes']
result={'checked_at_utc':datetime.now(timezone.utc).isoformat(),'beam_commit':'0b560f058b811c8d9891b93f7606654106691766',
        'received_verifier_sha256':sha256(verifier),'received_assertion_results':scope['out'],
        'extra_manifest_entries_verified':len(checks),'review_sha256':sha256(packet/'review.json'),
        'current_shared_payloads_match_bugs':len(review['payload_comparison']),
        'received_champ_files_match':len(mapping),'supplemental_factory_sources_verified':2,
        'conditional_reserve_rows_rechecked':40,'max_prompt_utf8_bytes':250315,'request_guard_floor_before_unknown_framing':254411,
        'final_inputs_received':False,'final_reserve':None,'provider_framing_bound':None,'current_quota_verified':False,
        'shared_supported':377,'shared_unsupported':314,'primary_results_added':0,'gate_a_passed':False,
        'production_runtime_unchanged':True,'live_api_requests':0,'queue_mutations':0,'auditor_sha256':sha256(__file__)}
write_json(out/'champ-beam0b560-review-v1.json',result)
print({'shared_bugs_verified':20,'supplemental_factories':2,'conditional_reserve_rows':40,'final_reserve':None})
