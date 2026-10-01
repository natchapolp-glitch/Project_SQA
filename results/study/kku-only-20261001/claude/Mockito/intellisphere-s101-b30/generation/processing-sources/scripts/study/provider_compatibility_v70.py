"""Fixed-compiler-only compatibility recovery for two identified Mockito captures."""
import hashlib
from normalize_provider_source import positions
from provider_prefix_v68 import repair as previous
def repair(tests,project,tool,seed):
    edits=previous(tests,project,tool,seed)
    if project!='Mockito' or tool!='intellisphere' or seed not in (101,102): return edits
    path=tests/'org/mockito/internal/invocation/InvocationMatcherTest.java'
    text=path.read_text(encoding='utf-8')
    before=hashlib.sha256(path.read_bytes()).hexdigest()
    expected={101:'9070b5e9bb626c1976838c1cccf8b3069de29be7d5c0474e4f81260dbf7e413c',
              102:'b6dd81bb72e38695ab3bc81dc4abf53b899bd3edc78c5852a790799236ca6acc'}
    if before!=expected[seed]: raise ValueError('Mockito capture differs from fixed compiler evidence')
    if seed==101:
        encoded=text.encode('utf-16-le')
        methods=[m for m in positions(path) if 'invocation.isVarArgs()' in encoded[m['start_utf16']*2:m['end_utf16']*2].decode('utf-16-le')]
        if len(methods)!=5 or text.count('invocation.isVarArgs()')!=5: raise ValueError('Unexpected unsupported Invocation API')
        for method in sorted(methods,key=lambda m:m['start_utf16'],reverse=True):
            encoded=encoded[:method['start_utf16']*2]+encoded[method['end_utf16']*2:]
        text=encoded.decode('utf-16-le')
        reason='Excluded five whole test methods using Invocation.isVarArgs(), absent from fixed Invocation API. Retained assertions unchanged. v69 guard failed before evaluation because it incorrectly assumed only one occurrence.'
    else:
        if text.count('Matchers.any()')!=4: raise ValueError('Unexpected Hamcrest calls')
        text=text.replace('Matchers.any()','Matchers.any(Object.class)')
        reason='Supplied required Object.class argument to four Hamcrest Matchers.any calls. The fixed compiler requires Class<T>; assertion expectations unchanged.'
    path.write_text(text,encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
                  'after_sha256':hashlib.sha256(path.read_bytes()).hexdigest(),
                  'fixed_api_compatibility':reason,
                  'selection':'Fixed-1 compile diagnostics only; v67 failures preserved; no buggy outcomes observed before this repair.'})
    return edits
