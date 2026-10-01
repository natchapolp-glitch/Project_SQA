"""Fixed compiler diagnosed missing FastMath import, Math Gemini 103."""
from normalize_provider_source import sha
from provider_compatibility_v43 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('Math', 'intellisphere', 103):
        return edits
    p = tests / 'org/apache/commons/math3/fraction/BigFractionRegressionTest.java'
    before = sha(p)
    if before != '485b534193942cb50e726cd992e35e8105103985669009398a11a43c00be113b':
        raise ValueError('Math103 source differs')
    old = 'import org.junit.Assert;'
    text = p.read_text(encoding='utf-8')
    if text.count(old) != 1:
        raise ValueError('Math103 import anchor differs')
    p.write_text(text.replace(old, 'import org.apache.commons.math3.util.FastMath;\n' + old), encoding='utf-8')
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_api_compatibility': 'Add missing FastMath import identified by fixed compilation; assertions unchanged.',
                  'selection': 'Fixed compiler only; failed v39 preserved; no buggy feedback.'})
    return edits
