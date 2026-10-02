"""Apply the same fixed-diagnosed unboxing to Jsoup's body sibling index."""
from normalize_provider_source import sha
from provider_compatibility_v36 import repair as repair_v36

def repair(tests,project,tool,seed):
    edits=repair_v36(tests,project,tool,seed)
    if (project,tool,seed)!=('Jsoup','intellisphere',102):return edits
    path=tests/'org/jsoup/nodes/DocumentRegressionTest.java'
    if not path.exists():return edits
    text=path.read_text(encoding='utf-8');old='assertEquals(1, doc.body().elementSiblingIndex());'
    if old not in text:return edits
    before=sha(path)
    if before!='fe1a4382b722311e9d041e312ae64597ad3f59a349aceb25602539418bfe7962' or text.count(old)!=1:
        raise ValueError('Jsoup intermediate source changed')
    path.write_text(text.replace(old,'assertEquals(1, doc.body().elementSiblingIndex().intValue());'),encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),
                  'fixed_api_compatibility':[{'kind':'explicit_unboxing','method':'elementSiblingIndex',
                    'reason':'Same Integer return type / JUnit ambiguity on body call. Expected value 1 unchanged; v36 fixed failure preserved.'}],
                  'selection':'Fixed compiler and fixed declarations only; no buggy outcomes.'})
    return edits
