"""Repair three fixed-diagnosed metadata and fixture compilation failures."""
from normalize_provider_source import sha
from provider_compatibility_v21 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    replacements = []
    if (project, tool, seed) == ('JacksonDatabind', 'intellisphere', 102):
        replacements = [
            ('ac.findProperty( new PropertyName("name") )',
             'com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition.construct(null, getNameMethod, "name")',
             'Fixed AnnotatedClass has no findProperty; fixed SimpleBeanPropertyDefinition constructs metadata for the same member and property name.'),
            ('ac.findProperty( new PropertyName("value") )',
             'com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition.construct(null, valueField, "value")',
             'Construct fixed metadata for the same field and property name.')]
    if (project, tool, seed) == ('JacksonXml', 'intellisphere', 103):
        replacements = [('new ObjectCodec() {\n            // minimal implementation for test\n        }',
            'new com.fasterxml.jackson.dataformat.xml.XmlMapper()',
            'The empty anonymous abstract ObjectCodec cannot compile; use the existing concrete XML codec to test unchanged set/get identity assertion.')]
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        changes = []
        for old, new, reason in replacements:
            if old in text:
                changes.append({'old': old, 'new': new, 'occurrences': text.count(old), 'reason': reason})
                text = text.replace(old, new)
        if (project, tool, seed) == ('Mockito', 'intellisphere', 101) and 'method(Object.class, "toString")' in text:
            helper = '''
    private static Method method(Class<?> owner, String name) {
        try {
            return owner.getMethod(name);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException(exception);
        }
    }
'''
            text = text.replace('public class InvocationMatcherTest {',
                'public class InvocationMatcherTest {\n' + helper, 1)
            changes.append({'reason': 'Supply missing reflection helper already implied by original method(Object.class,"toString") call; arguments and assertions unchanged.'})
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path': path.relative_to(tests).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path),
                'local_compatibility_repairs': changes,
                'selection': 'Fixed compilation diagnostics and fixed API only; no buggy feedback or expected-value changes.'})
    return edits
