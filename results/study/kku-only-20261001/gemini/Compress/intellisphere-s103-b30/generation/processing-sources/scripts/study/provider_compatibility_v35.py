"""Explicit fixed-API interface cast for a hash-identified Collections response."""
from normalize_provider_source import sha
from provider_compatibility_v34 import repair as repair_v34

def repair(tests, project, tool, seed):
    edits = repair_v34(tests, project, tool, seed)
    if (project, tool, seed) != ('Collections', 'intellisphere', 101):
        return edits
    path = tests / 'org/apache/commons/collections/map/Flat3MapTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    if 'it.reset();' not in text:
        return edits
    before = sha(path)
    if before != 'cd88d90314f58373b4fd65bccade9b15ef7b3e9dfc51edd4a0d9f3ab35fef00e':
        raise ValueError('Collections captured source hash changed')
    if text.count('it.reset();') != 1:
        raise ValueError('Expected exactly one interface call')
    text = text.replace('it.reset();','((org.apache.commons.collections.ResettableIterator) it).reset();')
    path.write_text(text,encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),
                  'fixed_api_compatibility':[{'kind':'interface_cast','from':'MapIterator.reset()',
                    'to':'((ResettableIterator) it).reset()',
                    'reason':'Fixed Flat3Map.FlatMapIterator implements MapIterator and ResettableIterator; MapIterator lacks reset.'}],
                  'selection':'Fixed compilation and fixed source declarations only. Assertions unchanged.'})
    return edits
