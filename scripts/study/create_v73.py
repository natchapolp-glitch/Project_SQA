"""Freeze the existing generic policy for the newly authorized KKU account batch."""
from pathlib import Path
import hashlib
import json
from datetime import datetime, timezone

root = Path(__file__).resolve().parents[2]
target = root / 'scripts/study/evaluate_provider_normalized_v73.py'
policy = root / 'results/study/kku-only-20261001/processing-policy-v73.json'
if target.exists() or policy.exists():
    raise ValueError('Version 73 already exists')
source = root / 'scripts/study/evaluate_provider_normalized_v67.py'
target.write_text(source.read_text(encoding='utf-8').replace('v67', 'v73'), encoding='utf-8')
policy.write_text(json.dumps({
    'version': 73,
    'created_at_utc': datetime.now(timezone.utc).isoformat(),
    'driver': target.relative_to(root).as_posix(),
    'driver_sha256': hashlib.sha256(target.read_bytes()).hexdigest(),
    'parent_driver_sha256': hashlib.sha256(source.read_bytes()).hexdigest(),
    'policy': 'Same generic policy as v67: complete closed Java fences, source-order cap 30, mechanical renderer suffix cleanup, at most two fixed-only pruning passes; no hash-specific repairs. Preserve earlier attempts and raw responses; no evaluation feedback sent to AI.',
    'response_style': 'Normal',
    'account_scope': 'New user-authorized KKU account; Profile observed before collection. Initial Claude quota displayed 0.0%. Account identifier is omitted from public records.',
    'model_choice': 'Explicit Claude agent, exact live model label recorded per capture; no Auto Router.',
    'authorization': 'Owner requested continuation with a new account on 2 October 2026 and confirmed login.'
}, indent=2) + '\n', encoding='utf-8')
print(target)
