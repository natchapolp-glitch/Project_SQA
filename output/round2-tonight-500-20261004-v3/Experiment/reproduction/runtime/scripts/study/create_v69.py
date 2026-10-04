from pathlib import Path
import json,hashlib
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
target=root/'scripts/study/evaluate_provider_normalized_v69.py'
if target.exists(): raise ValueError('Version 69 already exists')
text=(root/'scripts/study/evaluate_provider_normalized_v68.py').read_text(encoding='utf-8')
for old,new in [('evaluate_provider_normalized_v68.py','evaluate_provider_normalized_v69.py'),
                ('ai-source-processing-v68','ai-source-processing-v69'),('source-processing-v68','source-processing-v69'),
                ('Local processing v68','Local processing v69')]: text=text.replace(old,new)
text=text.replace('from provider_prefix_v68 import repair','from provider_compatibility_v69 import repair')
text=text.replace("'scripts/study/provider_prefix_v68.py',", "'scripts/study/provider_prefix_v68.py', 'scripts/study/provider_compatibility_v69.py',")
text=text.replace('Raw provider output remains unchanged.', 'Adds fixed-compiler-only Mockito API recovery: excludes one unsupported isVarArgs test on index 101 and supplies Hamcrest any(Object.class) on index 102; assertions in retained methods unchanged. Raw provider output remains unchanged.')
target.write_text(text,encoding='utf-8')
policy={'version':69,'created_at_utc':datetime.now(timezone.utc).isoformat(),
        'policy':'Version 68 plus exact-hash fixed-compiler API recovery for Mockito/101 and /102. Preserve v67 failed evaluations; at most two fixed-only pruning passes; no evaluation feedback transmitted to providers.',
        'source_sha256':{p.relative_to(root).as_posix():hashlib.sha256(p.read_bytes()).hexdigest() for p in [target,root/'scripts/study/provider_compatibility_v69.py']}}
(root/'results/study/kku-only-20261001/processing-policy-v69.json').write_text(json.dumps(policy,indent=2)+'\n',encoding='utf-8')
print(target)
