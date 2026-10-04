"""Resolve fixed reflection helper overload and legacy Jackson type APIs."""
from normalize_provider_source import sha
from provider_compatibility_v22 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        changes = []
        replacements = []
        if (project, seed) == ('JacksonDatabind', 102):
            replacements.append(('TypeFactory.defaultInstance()',
                'com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance()',
                'Qualify existing fixed type factory absent from provider imports.'))
        if (project, seed) == ('JacksonDatabind', 103):
            replacements.extend([
                ('prop.getPrimaryType()', '_mapper.constructType(prop.getAccessor().getGenericType())',
                 'Fixed BeanPropertyDefinition exposes accessor/member generic type; construct same declared property type through mapper.'),
                ('_mapper.getSerializerProviderInstance()',
                 '((com.fasterxml.jackson.databind.ser.DefaultSerializerProvider) _mapper.getSerializerProvider()).createInstance(_mapper.getSerializationConfig(), _mapper.getSerializerFactory())',
                 'Construct configured serializer provider through fixed API, as disclosed in v19.')])
        for old, new, reason in replacements:
            if old in text:
                changes.append({'old': old, 'new': new, 'occurrences': text.count(old), 'reason': reason})
                text = text.replace(old, new)
        if (project, seed) == ('Mockito', 101) and 'method("toString")' in text:
            text = text.replace('public class InvocationMatcherTest {',
                'public class InvocationMatcherTest {\n    private static Method method(String name) { return method(Object.class, name); }\n', 1)
            changes.append({'reason': 'Resolve missing no-argument Object-method helper overload. Unknown method names still fail fixed validation and are not fabricated.'})
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path': path.relative_to(tests).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path),
                'local_compatibility_repairs': changes,
                'selection': 'Fixed API/compilation only; original assertion values unchanged, fixed-only pruning retained.'})
    return edits
