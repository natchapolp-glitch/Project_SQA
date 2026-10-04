"""Remove KKU rendering suffixes before the v24 AST-based exclusion."""
from normalize_provider_source import sha, renderer_suffixes
from provider_compatibility_v24 import repair as prior


def repair(tests, project, tool, seed):
    initial = []
    if (project, tool, seed) == ('Mockito', 'intellisphere', 101):
        for path in tests.rglob('*.java'):
            text = path.read_text(encoding='utf-8')
            cleaned, suffixes = renderer_suffixes(text)
            if suffixes:
                before = sha(path)
                path.write_text(cleaned, encoding='utf-8')
                initial.append({'path': path.relative_to(tests).as_posix(),
                    'before_sha256': before, 'after_sha256': sha(path),
                    'renderer_suffixes_removed': len(suffixes),
                    'selection': 'Mechanical rendered suffix cleanup before AST lookup; no outcomes or literals changed.'})
    return initial + prior(tests, project, tool, seed)
