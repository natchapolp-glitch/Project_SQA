"""Unbox a fixed-API Integer result to resolve JUnit assertion overload ambiguity."""
from normalize_provider_source import sha
from provider_compatibility_v35 import repair as repair_v35

def repair(tests, project, tool, seed):
    edits=repair_v35(tests,project,tool,seed)
    if (project,tool,seed)!=('Jsoup','intellisphere',102):return edits
    path=tests/'org/jsoup/nodes/DocumentRegressionTest.java'
    if not path.exists():return edits
    text=path.read_text(encoding='utf-8')
    old='assertEquals(0, doc.head().elementSiblingIndex());'
    if old not in text:return edits
    before=sha(path)
    if before!='926b1aab095023d11014614eebea8d791614fba47666dc2197f96a1d80b0bd37' or text.count(old)!=1:
        raise ValueError('Jsoup capture differs from fixed-diagnosed source')
    path.write_text(text.replace(old,'assertEquals(0, doc.head().elementSiblingIndex().intValue());'),encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),
                  'fixed_api_compatibility':[{'kind':'explicit_unboxing','method':'elementSiblingIndex',
                    'reason':'Fixed API returns Integer; JUnit assertEquals(long,long) and assertEquals(Object,Object) are ambiguous. Explicit intValue preserves expected value 0.'}],
                  'selection':'Fixed compiler diagnostics and fixed API return type only; no buggy outcomes.'})
    return edits
