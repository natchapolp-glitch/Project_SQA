from pathlib import Path
import json,hashlib
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
target=root/'scripts/study/evaluate_provider_normalized_v68.py'
if target.exists(): raise ValueError('Version 68 already exists')
text=(root/'scripts/study/evaluate_provider_normalized_v67.py').read_text(encoding='utf-8').replace('v67','v68')
text=text.replace('def repair(tests, project, tool, seed):\n    return []  # No historical, hash-specific compatibility repairs applied to fresh captures.','from provider_prefix_v68 import repair')
text=text.replace("'scripts/study/evaluate_provider_normalized_v68.py',", "'scripts/study/evaluate_provider_normalized_v68.py', 'scripts/study/provider_prefix_v68.py',")
text=text.replace('No inherited hash-specific compatibility repairs. Raw provider output remains unchanged.', 'Adds exact-hash Lang/101 complete-prefix recovery before evaluation; all retained assertions unchanged. Raw provider output remains unchanged.')
target.write_text(text,encoding='utf-8')
policy={'version':68,'created_at_utc':datetime.now(timezone.utc).isoformat(),
        'policy':'Version 67 general processing plus exact-hash recovery of Lang/101 truncated method header; cap 30 in source order; fixed-only pruning at most twice; no feedback sent to AI.',
        'source_sha256':{p.relative_to(root).as_posix():hashlib.sha256(p.read_bytes()).hexdigest() for p in [target,root/'scripts/study/provider_prefix_v68.py']}}
(root/'results/study/kku-only-20261001/processing-policy-v68.json').write_text(json.dumps(policy,indent=2)+'\n',encoding='utf-8')
print(target)
