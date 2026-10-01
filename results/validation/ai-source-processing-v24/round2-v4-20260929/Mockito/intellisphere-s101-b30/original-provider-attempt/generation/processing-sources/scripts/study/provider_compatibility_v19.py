"""Construct the configured serializer provider through the fixed-version API."""
from normalize_provider_source import sha
from provider_compatibility_v18 import repair as prior

def repair(tests,project,tool,seed):
    edits = prior(tests,project,tool,seed)
    if (project,tool,seed) != ('JacksonDatabind','intellisphere',101):
        return edits
    old = 'mapper.getSerializerProviderInstance()'
    new = '((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) mapper.getSerializerProvider()).createInstance(mapper.getSerializationConfig(), mapper.getSerializerFactory())'
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        if old not in text:
            continue
        before = sha(path)
        path.write_text(text.replace(old,new),encoding='utf-8')
        edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
            'after_sha256':sha(path),'local_compatibility_repairs':[{'old':old,'new':new,
            'reason':'Fixed ObjectMapper exposes provider blueprint/config/factory; DefaultSerializerProvider.createInstance produces configured provider.'}],
            'selection':'Fixed compiler diagnostics and fixed ObjectMapper/DefaultSerializerProvider implementations; assertions unchanged.'})
    return edits
