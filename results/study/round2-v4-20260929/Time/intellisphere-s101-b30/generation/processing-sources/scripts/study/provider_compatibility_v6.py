"""Disclosed fixed-syntax and complete-prefix salvage for truncated UI exports."""
import re
from normalize_provider_source import sha
from provider_compatibility_v5 import repair as repair_v5


TRUNCATED_METHOD = {
    ('Chart', 101, 'AbstractCategoryItemRendererTest.java'): 'testGetItemLabelGeneratorFallback',
    ('Collections', 101, 'Flat3MapTest.java'): 'testMapIteratorNextThrowsWhenExhausted',
    ('JxPath', 102, 'JDOMNodePointerTest.java'): 'testRemove',
    ('Time', 103, 'PartialTest.java'): 'testPropertyAddWrapFieldToCopy',
}


def repair(tests, project, tool, seed):
    edits = repair_v5(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in sorted(tests.rglob('*.java')):
        text, before = path.read_text(encoding='utf-8'), sha(path)
        changes = []
        method = TRUNCATED_METHOD.get((project, seed, path.name))
        if method:
            # A parser-diagnosed EOF in this precise last method is salvaged by
            # retaining the original prefix through the preceding complete method.
            marker = re.search(r'^\s*public\s+void\s+' + re.escape(method) + r'\s*\(', text, re.M)
            if not marker:
                raise ValueError('Expected truncated method missing: ' + method)
            discarded = text[marker.start():]
            if not discarded or text.rstrip().endswith('\n}'):
                raise ValueError('Source does not have the expected truncated tail: ' + str(path))
            text = text[:marker.start()].rstrip() + '\n}\n'
            changes.append({'kind':'drop_incomplete_trailing_method','method':method,
                'discarded_characters':len(discarded),
                'reason':'Original Java block ended during this method; earlier complete methods are retained unchanged.'})
        if project == 'Time' and seed == 101 and path.name == 'PartialTest.java':
            old, new = 'types[0));', 'types[0]);'
            if text.count(old) != 1:
                raise ValueError('Expected one fixed syntax typo in Time 101')
            text = text.replace(old, new)
            changes.append({'kind':'syntax_bracket','old':old,'new':new,
                'reason':'Close array index before assertion parentheses; assertion expression unchanged.'})
        if project == 'Mockito' and seed == 101 and path.name == 'InvocationMatcherTest.java':
            old, new = 'new Object[]{mockObject}));', 'new Object[]{mockObject});'
            if text.count(old) != 1:
                raise ValueError('Expected one extra parenthesis in Mockito 101')
            text = text.replace(old, new)
            changes.append({'kind':'syntax_parenthesis','old':old,'new':new,
                'reason':'Remove the extra closing parenthesis; mock return value unchanged.'})
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path':path.relative_to(tests).as_posix(),
                'before_sha256':before,'after_sha256':sha(path),
                'local_syntax_recovery':changes,
                'selection':'Parser diagnostics and known source tail; no fixed/buggy test outcomes used.'})
    return edits
