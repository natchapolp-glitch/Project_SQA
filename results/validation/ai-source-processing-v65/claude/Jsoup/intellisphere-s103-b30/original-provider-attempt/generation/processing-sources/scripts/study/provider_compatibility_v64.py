"""Exclude Jsoup methods using selectFirst, absent from the fixed API."""
from normalize_provider_source import sha, positions, remove_methods, utf16_index
from provider_compatibility_v62 import repair as inherited_repair

def repair(tests, project, tool, seed):
    edits=inherited_repair(tests,project,tool,seed)
    if (project,tool,seed)!=('Jsoup','intellisphere',103):
        return edits
    path=tests/'org/jsoup/nodes/DocumentTest.java'
    before=sha(path)
    text=path.read_text(encoding='utf-8')
    bad=[r for r in positions(path) if '.selectFirst(' in text[utf16_index(text,r['start_utf16']):utf16_index(text,r['end_utf16'])]]
    assert bad
    remove_methods(path,bad)
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),'fixed_api_compatibility':{'kind':'exclude_unsupported_api_methods','removed_methods':[r['owner']+'::'+r['name'] for r in bad],'reason':'Fixed compilation reports Document.selectFirst unavailable; exclude whole methods. Surviving assertions unchanged.'},'selection':'Fixed compiler only; v62 failed attempt preserved; original hash checked by inherited repair. No buggy or coverage outcomes used.'})
    return edits
