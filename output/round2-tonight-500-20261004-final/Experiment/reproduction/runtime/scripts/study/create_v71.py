from pathlib import Path
import hashlib,json
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
compat=root/'scripts/study/provider_compatibility_v71.py';driver=root/'scripts/study/evaluate_provider_normalized_v71.py'
if compat.exists() or driver.exists():raise ValueError('Version already exists')
compat.write_text('''import hashlib
from normalize_provider_source import positions,remove_methods
from provider_compatibility_v70 import repair as previous
def repair(tests,project,tool,seed):
    edits=previous(tests,project,tool,seed)
    if (project,tool,seed)!=('Mockito','intellisphere',101):return edits
    path=tests/'org/mockito/internal/invocation/InvocationMatcherTest.java'
    before=hashlib.sha256(path.read_bytes()).hexdigest()
    if before!='fa15663e5d95b62d888c2ffbc7b2224bb64ec3ff6915f2e807cc1dd7dcd1f0a2':raise ValueError('Unexpected post-v70 source')
    methods=[m for m in positions(path) if m['name']=='testIsVarargMatcherWithDecorator']
    if len(methods)!=1:raise ValueError('Unexpected decorator method')
    remove_methods(path,methods)
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':hashlib.sha256(path.read_bytes()).hexdigest(),'fixed_api_compatibility':'Exclude whole testIsVarargMatcherWithDecorator: fixed compiler rejects MatcherDecorator as Matcher. Retained assertions unchanged. v70 fixed compile evidence preserved.'})
    return edits
''',encoding='utf-8')
text=(root/'scripts/study/evaluate_provider_normalized_v70.py').read_text()
for old,new in [('evaluate_provider_normalized_v70.py','evaluate_provider_normalized_v71.py'),('ai-source-processing-v70','ai-source-processing-v71'),('source-processing-v70','source-processing-v71'),('Local processing v70','Local processing v71'),('from provider_compatibility_v70 import repair','from provider_compatibility_v71 import repair')]:text=text.replace(old,new)
text=text.replace("'scripts/study/provider_compatibility_v70.py',","'scripts/study/provider_compatibility_v70.py', 'scripts/study/provider_compatibility_v71.py',")
text=text.replace('Raw provider output remains unchanged.','Also excludes the whole incompatible MatcherDecorator method using only v70 fixed compiler diagnostics. Raw provider output remains unchanged.')
driver.write_text(text,encoding='utf-8')
(root/'results/study/kku-only-20261001/processing-policy-v71.json').write_text(json.dumps({'version':71,'created_at_utc':datetime.now(timezone.utc).isoformat(),'policy':'v70 plus one whole-method exclusion using fixed compiler incompatibility evidence only; retained assertions unchanged; no buggy feedback; preserve v70 failed attempt. At most two fixed-only pruning passes.','source_sha256':{p.relative_to(root).as_posix():hashlib.sha256(p.read_bytes()).hexdigest() for p in (compat,driver)}},indent=2)+'\n')
