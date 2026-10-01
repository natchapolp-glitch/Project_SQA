"""Exact EOF prefix recovery for Gemini Cli/102; retained assertions unchanged."""
from provider_prefix_v52 import repair as previous_repair
from normalize_provider_source import sha
import re

def repair(tests, project, tool, seed):
    edits = previous_repair(tests, project, tool, seed)
    if (project, tool, seed) != ('Cli', 'intellisphere', 102):
        return edits
    paths = list(tests.rglob('CommandLineRegressionTest.java'))
    if len(paths) != 1 or sha(paths[0]) != 'fe6b62e70b42806d71bf64376a2fb56540193d5230d1e023c1b7411731d495b6':
        raise ValueError('Observed Cli/102 source mismatch')
    path = paths[0]
    source = path.read_text(encoding='utf-8')
    marker = re.search(r'\n\s*public\s+void\s+testGetOptionValueString\s*\(', source)
    if not marker or not source.rstrip().endswith('cmd.'):
        raise ValueError('Observed EOF method missing')
    before = sha(path)
    path.write_text(source[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path': path.relative_to(tests).as_posix(), 'before_sha256': before,
        'after_sha256': sha(path), 'local_syntax_recovery': [{
            'kind': 'drop_incomplete_trailing_method', 'method': 'testGetOptionValueString',
            'discarded_characters': len(source[marker.start():]),
            'reason': 'Response stops mid-method; keep complete prefix and close class.'}],
        'selection': 'Exact observed hash and EOF before execution; retained assertions unchanged.'})
    return edits
