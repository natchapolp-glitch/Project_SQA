"""Disclosed recovery of one hash-identified, truncated Gemini Lang response.

Keep the complete prefix unchanged; discard the incomplete trailing method and
close the class. No assertions or expected values are created or changed.
"""
import re
from normalize_provider_source import sha


def repair(tests, project, tool, seed):
    edits = []
    if (project, tool, seed) != ('Lang', 'intellisphere', 101):
        return edits
    path = tests / 'org/apache/commons/lang3/math/NumberUtilsTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    if not text.rstrip().endswith('assertEquals(100, NumberUtils.min(300, 200,'):
        return edits
    if sha(path) != '74130c370490d89f5a77796fee3c8e0361fc4a2247c4795b29125c56eb910752':
        raise ValueError('Truncated source differs from the observed Gemini Lang capture')
    marker = re.search(r'\n\s*@Test\s+public\s+void\s+testMinMaxInt\s*\(', text)
    if not marker:
        raise ValueError('Expected incomplete annotated final method missing')
    before = sha(path)
    discarded = text[marker.start():]
    path.write_text(text[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path': path.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(path), 'local_syntax_recovery': [{
                      'kind': 'drop_incomplete_trailing_method', 'method': 'testMinMaxInt',
                      'discarded_characters': len(discarded),
                      'reason': 'Visible final Java ends mid-assertion; keep preceding complete methods and close class.'}],
                  'selection': 'Exact observed tail and parser EOF, before any fixed/buggy outcome. No assertions rewritten.'})
    return edits
