from pathlib import Path
import hashlib,json
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
compat=root/'scripts/study/provider_compatibility_v72.py';driver=root/'scripts/study/evaluate_provider_normalized_v72.py'
if compat.exists() or driver.exists():raise ValueError('Version already exists')
raw=root/'ai-tests/provider-captures/kku-only-20261001/claude/Mockito-1/s102-i1-team-haiku-20261002/source-processing-v70/input-tests/org/mockito/internal/invocation/InvocationMatcherTest.java'
expected=hashlib.sha256(raw.read_text().replace('Matchers.any()','Matchers.any(Object.class)').encode()).hexdigest()
text=(root/'scripts/study/provider_compatibility_v71.py').read_text().replace("101):return edits","102):return edits").replace('fa15663e5d95b62d888c2ffbc7b2224bb64ec3ff6915f2e807cc1dd7dcd1f0a2',expected).replace('testIsVarargMatcherWithDecorator','testIsVarargMatcherWithDecoratedVarargMatcher').replace('fixed compiler rejects MatcherDecorator as Matcher','fixed compiler rejects VarargMatcher as return value of MatcherDecorator.getActualMatcher()')
compat.write_text(text,encoding='utf-8')
text=(root/'scripts/study/evaluate_provider_normalized_v71.py').read_text()
for old,new in [('evaluate_provider_normalized_v71.py','evaluate_provider_normalized_v72.py'),('ai-source-processing-v71','ai-source-processing-v72'),('source-processing-v71','source-processing-v72'),('Local processing v71','Local processing v72'),('from provider_compatibility_v71 import repair','from provider_compatibility_v72 import repair'),("'scripts/study/provider_compatibility_v71.py'","'scripts/study/provider_compatibility_v72.py'")]:text=text.replace(old,new)
driver.write_text(text,encoding='utf-8')
(root/'results/study/kku-only-20261001/processing-policy-v72.json').write_text(json.dumps({'version':72,'created_at_utc':datetime.now(timezone.utc).isoformat(),'policy':'v70 plus exclude whole incompatible decorated-vararg method for Mockito102 using only v70 fixed compiler diagnostics; retained assertions unchanged, preserve failed attempt. At most two fixed-only pruning passes; no buggy feedback.','source_sha256':{p.relative_to(root).as_posix():hashlib.sha256(p.read_bytes()).hexdigest() for p in (compat,driver)}},indent=2)+'\n')
