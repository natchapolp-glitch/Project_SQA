"""Repair one hash-identified Haiku local variable typo from fixed compiler evidence."""
from normalize_provider_source import sha
from provider_compatibility_v37 import repair as repair_v37

def repair(tests, project, tool, seed):
    edits = repair_v37(tests, project, tool, seed)
    if (project, tool, seed) != ('Codec', 'intellisphere', 102):
        return edits
    path = tests / 'org/apache/commons/codec/language/CaverphoneTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    old = 'String result1 = encoder.caverphone("aeiou");\n        assertTrue(result.length() == 10);'
    if old not in text:
        return edits
    before = sha(path)
    if before != 'd9ae4cdf202bf4696d6fe2fa97812260698730f8c6916cf32b1a77a5211a6952' or text.count(old) != 1:
        raise ValueError('Haiku Codec source differs from preserved failed input')
    path.write_text(text.replace(old, old.replace('assertTrue(result.', 'assertTrue(result1.')), encoding='utf-8')
    edits.append({'path': path.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(path), 'fixed_api_compatibility': [{
                      'kind': 'local_variable_reference_typo',
                      'method': 'testCaverphoneConsecutiveVowels',
                      'reason': 'Fixed compiler reports undeclared result; refer to the result1 declared in the same method. Expected length 10 unchanged.'}],
                  'selection': 'Fixed compiler only; failed v37 attempt preserved; no buggy observations.'})
    return edits
