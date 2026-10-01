"""Refresh delivery notes only after the audited manifest is available."""
import json
from pathlib import Path

root = Path(__file__).resolve().parents[2]
out = root / 'output/kku-only-20261001'
summary = json.loads((out / 'summary.json').read_text(encoding='utf-8'))
assert summary['completed_runs'] == 162
path = out / 'delivery-status.json'
data = json.loads(path.read_text(encoding='utf-8'))
data['experiment'] = 'incomplete: 162/204; KKU Claude 9/51 (9/17 projects), KKU Gemini 51/51'
data['remaining_work'] = '42 Claude planned identities; historical provenance gaps; update final report/slides/package and submit Classroom.'
data['historical_gemini_quota'] = data['gemini_quota']
data['gemini_quota'] = {'status': 'available_at_last_observation',
    'account': 'supakron.k@kkumail.com', 'model': 'gemini-pro', 'used_percent': 30.9,
    'evidence': 'ai-tests/provider-captures/kku-only-20261001/gemini/Compress-1/s102-i1-supakron-20261001/provider-screen.png'}
data['latest_local_test_update'].update(completed=162, pending=42,
    gemini_completed_runs=51, new_gemini_runs=6, new_methods=85,
    receipt='docs/GEMINI_SUPAKRON_CHECKPOINT_20261001.md', published=False)
data['local_continuation_published'] = False
path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
print('Updated delivery status: 162/204; Gemini 51/51; local/unpushed')
