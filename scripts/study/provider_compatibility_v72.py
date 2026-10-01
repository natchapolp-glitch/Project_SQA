import hashlib
from normalize_provider_source import positions,remove_methods
from provider_compatibility_v70 import repair as previous
def repair(tests,project,tool,seed):
    edits=previous(tests,project,tool,seed)
    if (project,tool,seed)!=('Mockito','intellisphere',102):return edits
    path=tests/'org/mockito/internal/invocation/InvocationMatcherTest.java'
    before=hashlib.sha256(path.read_bytes()).hexdigest()
    if before!='9855797d694f67b33ea66b1a47923b053b1b5b1d80e9cf7edfcea71d173a9e78':raise ValueError('Unexpected post-v70 source')
    methods=[m for m in positions(path) if m['name']=='testIsVarargMatcherWithDecoratedVarargMatcher']
    if len(methods)!=1:raise ValueError('Unexpected decorator method')
    remove_methods(path,methods)
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':hashlib.sha256(path.read_bytes()).hexdigest(),'fixed_api_compatibility':'Exclude whole testIsVarargMatcherWithDecoratedVarargMatcher: fixed compiler rejects VarargMatcher as return value of MatcherDecorator.getActualMatcher(). Retained assertions unchanged. v70 fixed compile evidence preserved.'})
    return edits
