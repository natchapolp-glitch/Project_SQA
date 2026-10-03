"""Reproduce offline Champ v7 intake and conditional reserve evidence."""
import copy
from datetime import datetime, timezone
from pathlib import Path
from scripts.study.api854.common import ROOT, read_json, sha256, contained, write_json, implementation_hashes
from scripts.study.api854.inspect_v7_readiness import identities

OUT = ROOT / 'output/api854-provider-preflight-20261003'
PREP = ROOT / 'output/api854-20261003/champ-prepare-v7-current-v1'
RECEIVED = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development'
PROPOSAL = OUT / 'champ-v7-current-proposal-v1'
index = read_json(PREP / 'index.json')
assert index['runtime_source_sha256'] == implementation_hashes()
checksums = 0
bugs = []
for row in index['records']:
    folder = PREP / f"{row['project']}-{row['bug_id']}"
    for name, expected in read_json(folder / 'checksums.json').items():
        assert sha256(contained(folder, name)) == expected, name
        checksums += 1
    partition = read_json(folder / 'capability-exclusions.json')
    selected = identities(partition['selected'])
    excluded = identities([r['target'] for r in partition['excluded']])
    original = identities(read_json(ROOT / 'output/api854-20261003/prepare-v3' / folder.name / 'targets.json')['targets'])
    assert not selected & excluded and selected | excluded == original
    prompt = (folder / 'prompt.md').read_bytes()
    assert len(prompt) == row['prompt_utf8_bytes']
    assert prompt == (RECEIVED / folder.name / 'prompt.md').read_bytes()
    bugs.append({'project':row['project'], 'bug_id':row['bug_id'], 'owner':row['owner'],
                 'selected':len(selected), 'unsupported':len(excluded),
                 'prompt_utf8_bytes':len(prompt), 'prompt_sha256':sha256(folder / 'prompt.md'),
                 'unsupported_targets':partition['excluded'], 'joint_semantic_approval':False})
assert len(bugs) == 20
assert sum(b['selected'] for b in bugs) == 377 and sum(b['unsupported'] for b in bugs) == 314
now = datetime.now(timezone.utc).isoformat()
write_json(OUT / 'champ-v7-input-audit-v1.json', {
    'checked_at_utc':now, 'preparation_index_sha256':sha256(PREP / 'index.json'),
    'protocol_sha256':sha256(PROPOSAL / 'protocol.proposal.json'), 'runtime_source_sha256':implementation_hashes(),
    'checksum_entries_verified':checksums, 'bugs':bugs,
    'owner_champ_unsupported':sum(b['unsupported'] for b in bugs if b['owner']=='champ'),
    'all_twenty_prompts_equal_received_v7':True, 'real_kku_requests':0, 'queue_mutations':0,
    'generation_authorized':False, 'auditor_sha256':sha256(__file__)})
worksheet = copy.deepcopy(read_json(OUT / 'champ-aomb11-v6-reserve-worksheet-v1.json'))
worksheet.update(checked_at_utc=now, scope='Fresh v7 twenty-bug conditional byte guard; historical quota only',
                 preparation_index_sha256=sha256(PREP / 'index.json'),
                 protocol_sha256=sha256(PROPOSAL / 'protocol.proposal.json'))
for row in worksheet['conditional_budget_rows']:
    folder = PREP / f"{row['project']}-{row['bug_id']}"
    actual = len((folder / 'prompt.md').read_bytes())
    assert actual == row['prompt_utf8_bytes'] and sha256(folder / 'prompt.md') == row['prompt_sha256']
    assert row['request_floor_before_unknown_framing'] == actual + row['proposed_output_cap']
worksheet['largest_prompt']['prompt_path'] = (PREP / 'Math-1/prompt.md').relative_to(ROOT).as_posix()
worksheet['largest_prompt']['cpu_api_prompt_and_recipe_binding_passed'] = False
worksheet['largest_prompt']['binding_note'] = 'Fresh Gate A recipe/input binding passed; CPU/API consumer execution covered separately by integration tests.'
assert len(worksheet['conditional_budget_rows']) == 40
write_json(OUT / 'champ-v7-reserve-worksheet-v1.json', worksheet)
print({'bugs':len(bugs),'checksums':checksums,'champ_unsupported':sum(b['unsupported'] for b in bugs if b['owner']=='champ'),
       'conditional_rows':40,'max_prompt_bytes':max(b['prompt_utf8_bytes'] for b in bugs)})
