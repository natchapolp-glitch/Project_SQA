"""Declare fixed API checked exceptions without changing test bodies."""
from normalize_provider_source import sha
from provider_compatibility_v48 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('JacksonDatabind', 'intellisphere', 102):
        return edits
    p = tests / 'com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java'
    before = sha(p)
    if before != '1ac6ab879d437cfa6a7189cf2527af30c44a1e41a649ecefaf1c7385665dcdd8':
        raise ValueError('JacksonDatabind102 processed source differs')
    text = p.read_text(encoding='utf-8')
    for name in ('testAssignSerializerSuccessAndFailure', 'testAssignNullSerializerSuccessAndFailure'):
        old = 'public void ' + name + '() {'
        if text.count(old) != 1:
            raise ValueError('Exception declaration anchor differs')
        text = text.replace(old, 'public void ' + name + '() throws Exception {')
    p.write_text(text, encoding='utf-8')
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_api_compatibility': 'Declare checked exceptions on two tests using fixed findNullValueSerializer; bodies and assertions unchanged.',
                  'selection': 'Fixed compiler only; failed v48 preserved; no buggy feedback.'})
    return edits
