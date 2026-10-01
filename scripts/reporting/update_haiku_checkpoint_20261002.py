import json
from pathlib import Path
from PIL import Image

root = Path(__file__).resolve().parents[2]
for jpg in (root / 'ai-tests/provider-captures/kku-only-20261001/claude').rglob('provider-screen.jpg'):
    if not jpg.with_suffix('.png').exists():
        Image.open(jpg).save(jpg.with_suffix('.png'))
p = root / 'output/kku-only-20261001/delivery-status.json'
s = json.loads(p.read_text(encoding='utf-8'))
s['experiment'] = 'incomplete: 163/204; KKU Claude 10/51, KKU Gemini 51/51'
s['remaining_work'] = '41 Claude planned identities; historical provenance gaps; update final report/slides/package and submit Classroom.'
s['latest_local_test_update'].update(completed=163, pending=41, new_claude_runs=1,
    new_claude_methods=27, claude_completed_runs=10,
    claude_receipt='docs/CLAUDE_HAIKU_CHECKPOINT_20261002.md')
s['claude_quota'] = {'status':'available_at_last_observation', 'account':'supakron.k@kkumail.com',
    'model':'claude-haiku-latest', 'used_percent':85.1,
    'evidence':'ai-tests/provider-captures/kku-only-20261001/claude/JacksonXml-1/s101-i1-supakron-haiku-20261001/provider-screen.png'}
s['local_continuation_published'] = False
p.write_text(json.dumps(s, ensure_ascii=False, indent=2)+'\n', encoding='utf-8')
p = root / 'README.md'
s = p.read_text(encoding='utf-8')
head = '''## Latest test update: 163/204 — Claude Haiku continuation

KKU Gemini is complete at 51/51; KKU Claude is 10/51, with 41 planned identities incomplete. New Haiku JacksonCore/101 adds 27 retained methods after disclosed syntax cleanup and fixed-only pruning; it passes fixed twice but does not detect the selected bug. JacksonXml/101 fails fixed compilation because Mockito is absent. Refusals and context requests remain unsuccessful attempts. Claude quota last observed 85.1% used. See `docs/CLAUDE_HAIKU_CHECKPOINT_20261002.md`. The report PDF/slides are earlier checkpoints; latest changes are local and unpushed.

'''
s = head + s[s.index('# SQA Project 2.2'):]
s = s.replace('audit_kku_only_v3.py --results', 'audit_kku_only_v4.py --results')
p.write_text(s, encoding='utf-8')
