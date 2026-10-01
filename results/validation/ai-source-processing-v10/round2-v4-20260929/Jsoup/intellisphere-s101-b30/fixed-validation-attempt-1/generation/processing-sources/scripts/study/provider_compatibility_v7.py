"""Small fixed-compiler follow-ups after v6; no buggy outcome is consulted."""
from normalize_provider_source import positions, remove_methods, sha
from provider_compatibility_v6 import repair as repair_v6


def repair(tests, project, tool, seed):
    edits = repair_v6(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in sorted(tests.rglob('*.java')):
        text, before = path.read_text(encoding='utf-8'), sha(path)
        substitutions = []
        if project == 'Time' and seed in (101, 103) and path.name == 'PartialTest.java':
            for old, new in (
                ('new Partial(null, new int[] {1})', 'new Partial((DateTimeFieldType[]) null, new int[] {1})'),
                ('new Partial(null, new int[]{1})', 'new Partial((DateTimeFieldType[]) null, new int[]{1})'),
            ):
                if old in text:
                    text = text.replace(old, new)
                    substitutions.append({'old':old,'new':new,
                        'reason':'Disambiguate null overload with the array type used by the adjacent value array; null behavior is unchanged.'})
        if project == 'Mockito' and seed == 101 and path.name == 'InvocationMatcherTest.java':
            old, new = 'thenReturn(method("toString")));', 'thenReturn(method("toString"));'
            if text.count(old) != 1:
                raise ValueError('Expected one parser-diagnosed extra parenthesis')
            text = text.replace(old, new)
            substitutions.append({'old':old,'new':new,'reason':'Remove second parser-diagnosed extra parenthesis.'})
        if substitutions:
            path.write_text(text, encoding='utf-8')
            edits.append({'path':path.relative_to(tests).as_posix(),
                'before_sha256':before,'after_sha256':sha(path),
                'local_compiler_repairs':substitutions,
                'selection':'Fixed parser/compiler diagnostics; no buggy outcomes used.'})
        if project == 'JxPath' and seed == 102 and path.name == 'DOMNodePointerTest.java':
            rows = [row for row in positions(path) if row['name'] == 'testTestNode_static']
            if len(rows) != 1:
                raise ValueError('Expected one fixed-compile-incompatible test method')
            before = sha(path)
            remove_methods(path, rows)
            edits.append({'path':path.relative_to(tests).as_posix(),
                'before_sha256':before,'after_sha256':sha(path),
                'fixed_compile_incompatible_methods_removed':[rows[0]['owner']+'::'+rows[0]['name']],
                'selection':'One method references a NodeNameTest constructor absent from fixed API. Drop that method; retain all other methods unchanged.'})
    return edits
