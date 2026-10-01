"""Two exact parser/compiler repairs remaining after v7."""
from normalize_provider_source import sha
from provider_compatibility_v7 import repair as repair_v7


def repair(tests, project, tool, seed):
    edits = repair_v7(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in sorted(tests.rglob('*.java')):
        text, before = path.read_text(encoding='utf-8'), sha(path)
        changes = []
        if project == 'Mockito' and seed == 101 and path.name == 'InvocationMatcherTest.java':
            old, new = 'assertSame(inv, matcher.getInvocation()));', 'assertSame(inv, matcher.getInvocation());'
            if text.count(old) != 1:
                raise ValueError('Expected one parser-diagnosed parenthesis')
            text = text.replace(old, new)
            changes.append({'old':old,'new':new,'reason':'Remove extra closing parenthesis in assertion.'})
        if project == 'Time' and seed == 103 and path.name == 'PartialTest.java':
            old, new = 'package org.joda.time;\n', ('package org.joda.time;\n\n'
                'import org.joda.time.chrono.ISOChronology;\n')
            if text.count(old) != 1 or 'import org.joda.time.chrono.ISOChronology;' in text:
                raise ValueError('Expected missing ISOChronology import')
            text = text.replace(old, new, 1)
            changes.append({'old':old,'new':new,
                'reason':'Import the fixed source class referenced by the generated test.'})
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path':path.relative_to(tests).as_posix(),
                'before_sha256':before,'after_sha256':sha(path),
                'local_compiler_repairs':changes,
                'selection':'Exact parser/compiler diagnostics on source or fixed revision; no buggy outcomes used.'})
    return edits
