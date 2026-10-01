"""Fixed-source diagnosed Option helper compatibility for new Claude Cli runs."""
from normalize_provider_source import sha
from provider_compatibility_v13 import repair as prior

def repair(tests,project,tool,seed):
    edits = prior(tests,project,tool,seed)
    if project != 'Cli' or tool != 'claude' or seed not in (102,103):
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        old = 'o.addValueForProcessing(value);'
        if old not in text:
            continue
        before = sha(path)
        path.write_text(text.replace(old,'o.addValue(value);'),encoding='utf-8')
        edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
            'after_sha256':sha(path),'local_compatibility_repairs':[{'old':old,'new':'o.addValue(value);',
            'reason':'Fixed Option.java declares package-visible addValue(String), routing to processValue; same-package test helper.'}],
            'selection':'Preceding fixed compiler error and fixed API source only; assertions unchanged.'})
    return edits
