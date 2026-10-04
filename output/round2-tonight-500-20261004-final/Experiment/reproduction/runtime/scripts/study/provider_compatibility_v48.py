"""Exclude fixed compiler diagnosed unavailable API test, retaining raw evidence."""
from normalize_provider_source import sha, positions, remove_methods
from provider_compatibility_v47 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('JacksonDatabind', 'intellisphere', 102):
        return edits
    p = tests / 'com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java'
    before = sha(p)
    if before != '24957988e2c40bfe9016ab4a6c7fc59669389f2867793e22da35fd5fecfff5bc':
        raise ValueError('JacksonDatabind102 processed source differs')
    rows = [r for r in positions(p) if r['name'] == 'testUnwrappingWriter']
    if len(rows) != 1:
        raise ValueError('Expected unavailable API test missing')
    remove_methods(p, rows)
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_compile_failed_methods_removed': [rows[0]['owner'] + '::' + rows[0]['name']],
                  'reason': 'Fixed compiler: BeanPropertyWriter.isUnwrapping does not exist. Exclude whole test; retained assertions unchanged.',
                  'selection': 'Fixed compilation only; failed v47 and raw 27-method response preserved; no buggy feedback. Processed raw count excludes two compiler-invalid methods.'})
    return edits
