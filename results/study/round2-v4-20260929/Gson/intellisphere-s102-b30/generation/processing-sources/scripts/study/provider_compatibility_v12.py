"""Post-hoc fixed-only repairs; preserve raw evidence and preceding attempts."""
from normalize_provider_source import positions,remove_methods,sha
from provider_compatibility_v9 import repair as prior

def repair(tests,project,tool,seed):
    edits=prior(tests,project,tool,seed)
    if (project,tool,seed)==('Mockito','intellisphere',103):
        path=next(tests.rglob('InvocationMatcherTest.java'))
        text=path.read_text(encoding='utf-8');before=sha(path)
        old='class CapturingMatcher implements Matcher<Object>, CapturesArguments'
        if text.count(old)!=1: raise ValueError('Expected diagnosed Matcher helper')
        path.write_text(text.replace(old,'class CapturingMatcher extends org.hamcrest.BaseMatcher<Object> implements CapturesArguments'),encoding='utf-8')
        edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),'fixed_api_repair':'Use Hamcrest BaseMatcher defaults as required by the fixed dependency interface; custom matches and capture behavior unchanged.'})
    if (project,tool,seed)==('Math','intellisphere',103):
        wanted={'testConstructorDoubleEpsilonMaxIterationsFail','testGetReducedFractionZeroDenominator'}
        found=set()
        for path in tests.rglob('*.java'):
            rows=[x for x in positions(path) if x['name'] in wanted]
            if rows:
                before=sha(path);remove_methods(path,rows);found.update(x['name'] for x in rows)
                edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),'fixed_diagnosed_methods_removed':[x['owner']+'::'+x['name'] for x in rows],'selection':'Both are assertion failures in preserved fixed log; exclude without changing expected values. No buggy feedback.'})
        if found!=wanted:raise ValueError('Missing fixed-diagnosed methods')
    return edits
