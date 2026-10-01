"""Adapt newer ObjectMapper provider API to the fixed version's equivalent."""
from normalize_provider_source import sha
from provider_compatibility_v46 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('JacksonDatabind', 'intellisphere', 102):
        return edits
    p = tests / 'com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java'
    before = sha(p)
    if before != 'a3a713701474912e08a6da890d4ca9f60802f218db144634d7d93396d458e242':
        raise ValueError('JacksonDatabind102 processed source differs')
    text = p.read_text(encoding='utf-8')
    old = 'mapper.getSerializerProviderInstance()'
    replacement = '((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory())'
    if text.count(old) != 14:
        raise ValueError('JacksonDatabind102 provider call count differs')
    p.write_text(text.replace(old, replacement), encoding='utf-8')
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_api_compatibility': 'Replace unavailable provider-instance accessor with fixed ObjectMapper _serializerProvider implementation via public getters and createInstance; 14 calls; assertions unchanged.',
                  'selection': 'Fixed compiler and fixed ObjectMapper API only; failed v46 preserved; no buggy feedback.'})
    return edits
