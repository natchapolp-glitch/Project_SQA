"""Apply the diagnosed legacy Option API compatibility to all call sites."""
from normalize_provider_source import sha
from provider_compatibility_v14 import repair as prior

def repair(tests,project,tool,seed):
    edits = prior(tests,project,tool,seed)
    if (project,tool,seed) != ('Cli','claude',102):
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        old = '.addValueForProcessing('
        if old not in text:
            continue
        before = sha(path)
        count = text.count(old)
        path.write_text(text.replace(old,'.addValue('),encoding='utf-8')
        edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
            'after_sha256':sha(path),'local_compatibility_repairs':[{'old':old,'new':'.addValue(',
            'occurrences':count,'reason':'All Option receiver calls use the package-visible fixed-version API; arguments and assertions unchanged.'}],
            'selection':'Both helper and direct call sites identified by fixed compiler diagnostics and original test source.'})
    return edits
