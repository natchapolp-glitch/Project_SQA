"""Hash-identified Claude Cli complete-prefix recovery, before execution."""
import re
from normalize_provider_source import sha
from provider_compatibility_v32 import repair as repair_v32


def repair(tests, project, tool, seed):
    edits = repair_v32(tests, project, tool, seed)
    if (project, tool, seed) != ('Cli', 'intellisphere', 101):
        return edits
    path = tests / 'org/apache/commons/cli/CommandLineGeneratedTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    if not text.rstrip().endswith('Option a1 ='):
        return edits
    before = sha(path)
    if before != '628b2daf5ef9373964d72b0f928a921a5d8355cb701ee182d2f8316b92bc0599':
        raise ValueError('Cli truncated source differs from observed capture')
    marker = re.search(r'\n\s*public\s+void\s+testAddEqualOptionTwiceIsStoredOnce\s*\(', text)
    if not marker:
        raise ValueError('Expected incomplete final JUnit 3 method missing')
    discarded = text[marker.start():]
    path.write_text(text[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'local_syntax_recovery':[{
                      'kind':'drop_incomplete_trailing_method', 'method':'testAddEqualOptionTwiceIsStoredOnce',
                      'discarded_characters':len(discarded),
                      'reason':'UI Java block ends mid-assignment; keep preceding complete methods and close class.'}],
                  'selection':'Exact captured source hash and parser EOF, before fixed/buggy outcomes. Assertions unchanged.'})
    return edits
