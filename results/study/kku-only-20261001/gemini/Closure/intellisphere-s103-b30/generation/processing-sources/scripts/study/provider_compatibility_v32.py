"""Hash-identified Claude Jsoup complete-prefix recovery, before execution."""
import re
from normalize_provider_source import sha
from provider_compatibility_v31 import repair as repair_v31


def repair(tests, project, tool, seed):
    edits = repair_v31(tests, project, tool, seed)
    if (project, tool, seed) != ('Jsoup', 'intellisphere', 101):
        return edits
    path = tests / 'org/jsoup/nodes/DocumentGeneratedTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    if not text.rstrip().endswith('assertS'):
        return edits
    before = sha(path)
    if before != 'e5b91db6cf3d5838f1226c8b4f37e5c64e03a97e33cac92bce733e4eb2581e61':
        raise ValueError('Jsoup truncated source differs from the observed capture')
    marker = re.search(r'\n\s*@Test\s+public\s+void\s+normaliseIsIdempotentOnStructure\s*\(', text)
    if not marker:
        raise ValueError('Expected incomplete final method missing')
    discarded = text[marker.start():]
    path.write_text(text[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'local_syntax_recovery':[{
                      'kind':'drop_incomplete_trailing_method', 'method':'normaliseIsIdempotentOnStructure',
                      'discarded_characters':len(discarded),
                      'reason':'Visible final response ends mid-assertion. Preserve earlier complete methods and close class.'}],
                  'selection':'Captured source hash and parser EOF; before fixed/buggy evaluation, assertions unchanged.'})
    return edits
