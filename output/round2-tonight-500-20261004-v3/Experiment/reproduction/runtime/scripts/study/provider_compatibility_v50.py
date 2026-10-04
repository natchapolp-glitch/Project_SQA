"""Disambiguate a typed null fixed API constructor, Time Gemini103."""
from normalize_provider_source import sha
from provider_compatibility_v49 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('Time', 'intellisphere', 103):
        return edits
    p = tests / 'org/joda/time/PartialTest.java'
    before = sha(p)
    if before != '131a41daecde586cf92f65b4b803303677ec09fc2dedad35b547d8ad7d997fc9':
        raise ValueError('Time103 source differs')
    text = p.read_text(encoding='utf-8')
    old = 'new Partial(null, new int[] { 2024 });'
    if text.count(old) != 1:
        raise ValueError('Time103 typed null anchor differs')
    p.write_text(text.replace(old, 'new Partial((DateTimeFieldType[]) null, new int[] { 2024 });'), encoding='utf-8')
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_api_compatibility': 'Cast null to DateTimeFieldType[] for the stated null-types-array test; same null input and assertions.',
                  'selection': 'Fixed compiler ambiguity only; failed v39 preserved; no buggy feedback.'})
    return edits
