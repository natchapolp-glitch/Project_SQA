"""Recover only the complete prefix of a hash-identified Gemini Codec response."""
import re
from normalize_provider_source import sha
from provider_compatibility_v33 import repair as repair_v33

def repair(tests, project, tool, seed):
    edits = repair_v33(tests, project, tool, seed)
    if (project, tool, seed) != ('Codec', 'intellisphere', 101):
        return edits
    path = tests / 'org/apache/commons/codec/language/PhoneticRegressionTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    if not text.rstrip().endswith('metaphone.metaphone("thick"));'):
        return edits
    before = sha(path)
    if before != '2c450748dbcaa89721cf9d2938190c3232f1e6653616b41945c9ddc71d578b1e':
        raise ValueError('Codec captured source hash changed')
    marker = re.search(r'\n\s*public\s+void\s+testMetaphoneTSpecialCases\s*\(', text)
    if not marker:
        raise ValueError('Expected incomplete final method missing')
    path.write_text(text[:marker.start()].rstrip() + '\n}\n', encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'local_syntax_recovery':[{
                      'kind':'drop_incomplete_trailing_method', 'method':'testMetaphoneTSpecialCases',
                      'discarded_characters':len(text[marker.start():]),
                      'reason':'Final response has no closing method/class braces; retain preceding complete methods.'}],
                  'selection':'Captured EOF and final-method boundary before any execution. Assertions unchanged.'})
    return edits
