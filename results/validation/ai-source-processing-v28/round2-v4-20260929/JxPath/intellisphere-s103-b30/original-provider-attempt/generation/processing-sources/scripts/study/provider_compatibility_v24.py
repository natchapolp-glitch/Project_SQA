"""Use fixed annotated-field iteration and exclude compiler-invalid duplicate fixture."""
from normalize_provider_source import sha, positions, remove_methods
from provider_compatibility_v23 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        changes = []
        if (project, seed) == ('JacksonDatabind', 102):
            old = 'ac.findField( new PropertyName("value") )'
            if old in text:
                text = text.replace(old, 'findOriginalField(ac, "value")')
                helper = '''
    private static AnnotatedField findOriginalField(AnnotatedClass owner, String name) {
        for (AnnotatedField field : owner.fields()) {
            if (name.equals(field.getName())) return field;
        }
        throw new IllegalArgumentException("Missing original field " + name);
    }
'''
                text = text.replace('public class BeanPropertyWriterTest {',
                    'public class BeanPropertyWriterTest {\n' + helper, 1)
                path.write_text(text, encoding='utf-8')
                changes.append({'old': old, 'new': 'findOriginalField(ac, "value")',
                    'reason': 'Fixed AnnotatedClass exposes fields() iteration, not findField(PropertyName); select same original named field.'})
        if (project, seed) == ('Mockito', 101):
            bad = [r for r in positions(path) if r['name'] == 'testCaptureArgumentsFrom_NonVarArgs_CapturesFromMatchers']
            if bad:
                remove_methods(path, bad)
                changes.append({'fixed_compile_invalid_methods_removed': [r['owner'] + '::' + r['name'] for r in bad],
                    'reason': 'Fixed compiler identifies duplicate local capturer1 declaration in original provider fixture. Exclude that method; do not rewrite its assertions or fabricate someMethod.'})
        if changes:
            edits.append({'path': path.relative_to(tests).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path),
                'local_compatibility_repairs': changes,
                'selection': 'Fixed compilation diagnostics and fixed API only; raw response and previous attempt preserved.'})
    return edits
