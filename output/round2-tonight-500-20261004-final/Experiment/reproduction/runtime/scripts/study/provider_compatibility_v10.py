"""Fixed-compile diagnosed method exclusions and one null-overload cast.

This is a post-hoc AI-assisted cohort. Raw responses and previous results stay
preserved; selection uses fixed compilation diagnostics, never buggy outcomes.
"""
from normalize_provider_source import positions, remove_methods, sha
from provider_compatibility_v9 import repair as repair_v9

EXCLUSIONS = {
    ('Cli', 103): ('testGetOptionObjectStringInteger',),
    ('Compress', 101): ('testWriteZeroLengthDoesNothing',),
    ('Compress', 102): ('testCloseAfterFinish',),
    ('Gson', 102): ('testExtractRealTypes_WithWildcard',),
    ('Gson', 103): ('testGetTypeInfoForArrayWithStringArray',),
    ('Jsoup', 101): ('testNormalisePreservesTextOrder',),
    ('Jsoup', 102): ('testNormaliseMovesTextNodesToBody',),
    ('JxPath', 103): ('testGetRelativePositionByNameFirstChild',),
}

def repair(tests, project, tool, seed):
    edits = repair_v9(tests, project, tool, seed)
    if (project, tool, seed) == ('JxPath', 'claude', 101):
        path = next(tests.rglob('GeneratedDOMNodePointerTest.java'))
        text, before = path.read_text(encoding='utf-8'), sha(path)
        old = 'ptr.getNamespaceURI(null)'
        if text.count(old) != 1:
            raise ValueError('Expected one fixed-diagnosed ambiguous null call')
        path.write_text(text.replace(old, 'ptr.getNamespaceURI((String) null)'), encoding='utf-8')
        edits.append({'path':path.relative_to(tests).as_posix(),
            'before_sha256':before,'after_sha256':sha(path),
            'fixed_api_repair':'Disambiguate String namespace prefix overload with a null cast.',
            'selection':'Fixed compilation only; no assertion or buggy outcome changed.'})
    if tool != 'intellisphere' or (project, seed) not in EXCLUSIONS:
        return edits
    wanted = set(EXCLUSIONS[(project, seed)])
    found = set()
    for path in sorted(tests.rglob('*.java')):
        rows = [r for r in positions(path) if r['name'] in wanted]
        if not rows:
            continue
        before = sha(path)
        remove_methods(path, rows)
        found.update(r['name'] for r in rows)
        edits.append({'path':path.relative_to(tests).as_posix(),
            'before_sha256':before,'after_sha256':sha(path),
            'fixed_compile_methods_removed':[r['owner']+'::'+r['name'] for r in rows],
            'selection':'Only methods containing errors in the preserved fixed-1 compilation log; no buggy outcomes used.'})
    if found != wanted:
        raise ValueError('Missing diagnosed methods: '+str(wanted-found))
    return edits
