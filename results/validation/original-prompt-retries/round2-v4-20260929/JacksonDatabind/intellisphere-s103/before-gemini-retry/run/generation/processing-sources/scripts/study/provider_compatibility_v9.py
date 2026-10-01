"""Keep complete Mockito tests before a parser-diagnosed truncated final method."""
import re
from normalize_provider_source import sha
from provider_compatibility_v8 import repair as repair_v8


def repair(tests, project, tool, seed):
    edits = repair_v8(tests, project, tool, seed)
    if (project, tool, seed) != ('Mockito', 'intellisphere', 101):
        return edits
    paths = list(tests.rglob('InvocationMatcherTest.java'))
    if len(paths) != 1:
        raise ValueError('Expected one Mockito test source')
    path = paths[0]
    text, before = path.read_text(encoding='utf-8'), sha(path)
    method = 'testIsVarargMatcher_ThroughMatcherDecorator_ReturnsTrue'
    marker = re.search(r'^[ \t]*@Test[ \t]*\n[ \t]*public void ' + method + r'\s*\(', text, re.M)
    if not marker or text.rstrip().endswith('\n}'):
        raise ValueError('Expected truncated final Mockito method')
    discarded = text[marker.start():]
    path.write_text(text[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(),
        'before_sha256':before,'after_sha256':sha(path),
        'local_syntax_recovery':[{'kind':'drop_incomplete_trailing_method',
            'method':method,'discarded_characters':len(discarded),
            'reason':'Provider Java block ends during this method; retain preceding complete tests.'}],
        'selection':'Parser-diagnosed truncated tail only; no fixed or buggy outcomes used.'})
    return edits
