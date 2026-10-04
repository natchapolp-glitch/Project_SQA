from pathlib import Path
import hashlib,json
from datetime import datetime,timezone
root=Path(__file__).resolve().parents[2]
compat=root/'scripts/study/provider_compatibility_v70.py'
driver=root/'scripts/study/evaluate_provider_normalized_v70.py'
if compat.exists() or driver.exists(): raise ValueError('Version already exists')
text=(root/'scripts/study/provider_compatibility_v69.py').read_text()
start=text.index("        methods=[m for m in positions(path)")
end=text.index('    else:',start)
text=text[:start]+'''        encoded=text.encode('utf-16-le')
        methods=[m for m in positions(path) if 'invocation.isVarArgs()' in encoded[m['start_utf16']*2:m['end_utf16']*2].decode('utf-16-le')]
        if len(methods)!=5 or text.count('invocation.isVarArgs()')!=5: raise ValueError('Unexpected unsupported Invocation API')
        for method in sorted(methods,key=lambda m:m['start_utf16'],reverse=True):
            encoded=encoded[:method['start_utf16']*2]+encoded[method['end_utf16']*2:]
        text=encoded.decode('utf-16-le')
        reason='Excluded five whole test methods using Invocation.isVarArgs(), absent from fixed Invocation API. Retained assertions unchanged. v69 guard failed before evaluation because it incorrectly assumed only one occurrence.'
'''+text[end:]
compat.write_text(text,encoding='utf-8')
text=(root/'scripts/study/evaluate_provider_normalized_v69.py').read_text().replace('v69','v70').replace('excludes one unsupported isVarArgs test','excludes five unsupported isVarArgs tests')
driver.write_text(text,encoding='utf-8')
policy={'version':70,'created_at_utc':datetime.now(timezone.utc).isoformat(),'policy':'Exact-hash fixed-compiler compatibility: exclude five unsupported isVarArgs methods for Mockito101; required Hamcrest Class argument for Mockito102. v69 guard failure retained, no buggy outcomes used. Retained assertions unchanged. At most two fixed-only pruning passes.', 'source_sha256':{p.relative_to(root).as_posix():hashlib.sha256(p.read_bytes()).hexdigest() for p in (compat,driver)}}
(root/'results/study/kku-only-20261001/processing-policy-v70.json').write_text(json.dumps(policy,indent=2)+'\n')
