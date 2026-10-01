"""Use the fixed legacy JsonParser accessor for newly captured XML suites."""
from normalize_provider_source import sha
from provider_compatibility_v16 import repair as prior

def repair(tests,project,tool,seed):
    edits = prior(tests,project,tool,seed)
    if project != 'JacksonXml' or tool != 'intellisphere' or seed not in (102,103):
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        old = '.currentToken()'
        if old not in text:
            continue
        before = sha(path)
        path.write_text(text.replace(old,'.getCurrentToken()'),encoding='utf-8')
        edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
            'after_sha256':sha(path),'local_compatibility_repairs':[{'old':old,'new':'.getCurrentToken()',
            'reason':'Legacy fixed JsonParser dependency exposes getCurrentToken; preserved token assertions.'}],
            'selection':'Fixed compiler error and fixed API compatibility already established in v4/v5.'})
    return edits
