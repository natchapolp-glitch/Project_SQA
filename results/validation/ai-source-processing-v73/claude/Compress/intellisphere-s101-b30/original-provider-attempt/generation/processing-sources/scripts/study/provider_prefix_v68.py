"""Recover only the complete prefix of the identified truncated Lang capture."""
import hashlib
def repair(tests, project, tool, seed):
    if (project,tool,seed)!=('Lang','intellisphere',101): return []
    path=tests/'org/apache/commons/lang3/math/NumberUtilsTest.java'
    if not path.exists(): return []
    original=path.read_bytes()
    before=hashlib.sha256(original).hexdigest()
    if before!='285cc40fa17fa91a7463acd5785ecd9f1d6f4957c44ffb908eb4570a85a8475b':
        raise ValueError('Capture differs from the identified Lang truncation')
    text=original.decode('utf-8')
    tail='    @Test(expected = IllegalArgumentException.class)\n    public void testMaxIntArrayEmpty()\n'
    if not text.endswith(tail): raise ValueError('Unexpected incomplete tail')
    repaired=text[:-len(tail)].rstrip()+'\n}\n'
    path.write_text(repaired,encoding='utf-8')
    return [{'path':path.relative_to(tests).as_posix(),'before_sha256':before,
             'after_sha256':hashlib.sha256(path.read_bytes()).hexdigest(),
             'complete_prefix_recovery':'Dropped final annotation and method signature with no body; closed outer class. Earlier complete methods and assertions unchanged.',
             'selection':'Syntactic truncation only, before any fixed or buggy execution.'}]
