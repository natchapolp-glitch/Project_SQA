"""Disclosed fixed-compiler interface cast for Collections/103; expectations unchanged."""
from normalize_provider_source import sha
from provider_compatibility_v39 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('Collections','intellisphere',103): return edits
    p = tests/'org/apache/commons/collections/map/Flat3MapRegressionTest.java'
    before = sha(p)
    if before != 'f61b9cfe6b5447b7b6a950182c84701244ed7a87fbf12f5654b330db0696be0b': raise ValueError('Collections raw source differs')
    text=p.read_text(encoding='utf-8')
    if text.count('it.reset();') != 1: raise ValueError('Reset call differs')
    p.write_text(text.replace('it.reset();','((org.apache.commons.collections.ResettableIterator) it).reset();'),encoding='utf-8')
    edits.append({'path':p.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(p),'fixed_api_compatibility':'MapIterator does not declare reset; fixed FlatMapIterator implements ResettableIterator. Cast receiver only; assertion values unchanged.','selection':'Fixed compiler and fixed production declarations; original failed v39 retained. No buggy outcomes or provider feedback.'})
    return edits
