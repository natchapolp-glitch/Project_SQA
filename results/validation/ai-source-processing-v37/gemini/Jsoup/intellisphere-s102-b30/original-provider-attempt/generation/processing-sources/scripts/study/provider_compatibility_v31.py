"""Fixed-only Time overload disambiguation, preserving v30 prefix recovery."""
from normalize_provider_source import sha
from provider_prefix_v30 import repair as repair_v30


def repair(tests, project, tool, seed):
    edits = repair_v30(tests, project, tool, seed)
    if (project, tool, seed) != ('Time', 'intellisphere', 101):
        return edits
    path = tests / 'org/joda/time/PartialTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    old = 'assertEquals(p.toString(), p.toString(null));'
    if old not in text:
        return edits
    if text.count(old) != 1:
        raise ValueError('Expected one fixed-diagnosed Time overload ambiguity')
    before = sha(path)
    text = text.replace(old, 'assertEquals(p.toString(), p.toString((String) null));')
    path.write_text(text, encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'fixed_api_compatibility': [{
                      'old':old, 'new':'assertEquals(p.toString(), p.toString((String) null));',
                      'reason':'Fixed compiler reports ambiguous DateTimeFormatter/String overloads. String overload matches adjacent formatting calls; null argument and expected value unchanged.'}],
                  'selection':'Fixed compilation diagnostics only; buggy suite not executed.'})
    return edits
