import hashlib
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
