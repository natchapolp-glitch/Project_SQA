"""Exclude one impossible fixed-API test fixture, retaining raw failed evidence."""
from normalize_provider_source import sha, positions, remove_methods
from provider_compatibility_v45 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('JacksonDatabind', 'intellisphere', 102):
        return edits
    p = tests / 'com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java'
    before = sha(p)
    if before != '13a34e90d549767c99f57e5d9f0b53cba94c6bd553986f42eed61b6512e3c1e2':
        raise ValueError('JacksonDatabind102 source differs')
    rows = [r for r in positions(p) if r['name'] == 'testInvalidMemberTypeThrowsIllegalArgumentException']
    if len(rows) != 1:
        raise ValueError('Expected invalid fixture method missing')
    remove_methods(p, rows)
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_compile_failed_methods_removed': [rows[0]['owner'] + '::' + rows[0]['name']],
                  'reason': 'Fixed compiler: AnnotatedClass cannot be assigned to AnnotatedMember. Remove entire invalid fixture test; retained assertions unchanged.',
                  'selection': 'Fixed compilation only; failed v39 and original 27-method response preserved; no buggy feedback. Processed count excludes this removed method.'})
    return edits
