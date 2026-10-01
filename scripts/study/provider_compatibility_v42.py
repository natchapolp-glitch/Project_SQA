"""Remove one fixed-compiler malformed unused local declaration; assertions unchanged."""
from normalize_provider_source import sha
from provider_compatibility_v41 import repair as previous

def repair(tests, project, tool, seed):
    edits=previous(tests,project,tool,seed)
    if (project,tool,seed)!=('Collections','intellisphere',103): return edits
    p=tests/'org/apache/commons/collections/map/Flat3MapRegressionTest.java'
    before=sha(p)
    if before!='7c56141863f4cea1b027d425045c76e0f12afff5073a5a003752e0caa31f7381': raise ValueError('Intermediate Collections source differs')
    text=p.read_text(encoding='utf-8')
    line='        Map.Entry dummyEntry = (Map.Entry) new HashMap().entrySet().iterator().hasNext() ? null : null;'
    if text.count(line)!=1 or text.count('dummyEntry')!=1: raise ValueError('Unused local differs')
    p.write_text(text.replace(line,''),encoding='utf-8')
    edits.append({'path':p.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(p),'fixed_api_compatibility':'Remove malformed unused dummyEntry local; no assertions or expected values changed.','selection':'Fixed compiler error, no buggy outcome. Previous v41 failed attempt retained.'})
    return edits
