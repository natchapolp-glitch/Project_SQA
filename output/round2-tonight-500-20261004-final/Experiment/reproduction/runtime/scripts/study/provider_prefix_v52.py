"""Recover complete prefixes of two exact observed truncated provider sources.

No new assertions or expected values are introduced. Selection precedes execution.
"""
import re
from normalize_provider_source import sha
from provider_compatibility_v39 import repair as previous_repair

CASES = {
    ('Cli', 'intellisphere', 101): ('CommandLineRegressionTest.java',
        'ccc1a87001cf48c820c259b16a8f13e7fef8c37d12b63f0dda25b0fe63c66540',
        'testGetOptionValueString'),
    ('Closure', 'intellisphere', 103): ('RemoveUnusedVarsRegressionTest.java',
        'd511531d6a3c89d11e760285441c0ad9b6375d2800103bbc6812546ad448494d',
        'testProcessThrowsNullPointerWhenDefFinderNullAndModifyCallSites'),
}

def repair(tests, project, tool, seed):
    edits = previous_repair(tests, project, tool, seed)
    case = CASES.get((project, tool, seed))
    if not case:
        return edits
    filename, expected, method = case
    paths = list(tests.rglob(filename))
    if len(paths) != 1 or sha(paths[0]) != expected:
        raise ValueError('Observed truncated source hash does not match v52 policy')
    path = paths[0]
    source = path.read_text(encoding='utf-8')
    marker = re.search(r'\n\s*public\s+void\s+' + re.escape(method) + r'\s*\(', source)
    if not marker:
        raise ValueError('Incomplete trailing method marker missing')
    before = sha(path)
    path.write_text(source[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path': path.relative_to(tests).as_posix(),
        'before_sha256': before, 'after_sha256': sha(path),
        'local_syntax_recovery': [{'kind': 'drop_incomplete_trailing_method',
            'method': method, 'discarded_characters': len(source[marker.start():]),
            'reason': 'Provider response stops mid-method; preserve complete prefix and close class.'}],
        'selection': 'Exact observed source hash and EOF tail before any execution; retained assertions unchanged.'})
    return edits
