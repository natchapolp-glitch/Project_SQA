"""Exclude one hash-identified Jsoup unsupported-API method from fixed diagnostics."""
from normalize_provider_source import sha, positions, remove_methods, utf16_index
from provider_compatibility_v39 import repair as inherited_repair

def repair(tests, project, tool, seed):
    edits = inherited_repair(tests, project, tool, seed)
    if (project, tool, seed) != ('Jsoup', 'intellisphere', 102):
        return edits
    path = tests / 'org/jsoup/nodes/DocumentTest.java'
    if not path.exists():
        return edits
    before = sha(path)
    if before != 'dba996653abf2f33938db0932168084d73c897e72050100f88bd3ac446b1411a':
        raise ValueError('Jsoup source differs from preserved fixed compiler failure')
    text = path.read_text(encoding='utf-8')
    bad = [r for r in positions(path) if any(call in text[utf16_index(text,r['start_utf16']):utf16_index(text,r['end_utf16'])] for call in ('.getChildNodes(', 'doc.normalise(element)'))]
    remove_methods(path, bad)
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'fixed_api_compatibility':{
                      'kind':'exclude_unsupported_api_methods',
                      'removed_methods':[r['owner']+'::'+r['name'] for r in bad],
                      'reason':'Fixed compiler reports Document.getChildNodes unavailable and Document.normalise(Element) private. Exclude the entire method; surviving assertions unchanged.'},
                  'selection':'Fixed compiler only; v54 and v58 failed attempts preserved. No buggy or coverage outcomes used.'})
    return edits
