"""Exclude fixed-compile diagnosed unsupported calls in every affected test method."""
from normalize_provider_source import positions,remove_methods,sha,renderer_suffixes,utf16_index
from provider_compatibility_v12 import repair as prior
PATTERNS={('Cli',103):('new Option("i", true,',),('Gson',102):('WildcardTypeImpl',),('Gson',103):('.getComponentType()',),('JxPath',103):('.getRelativePositionByName()',)}
def repair(tests,project,tool,seed):
    edits=prior(tests,project,tool,seed)
    if (project,tool,seed)==('Mockito','intellisphere',103):
        p=next(tests.rglob('InvocationMatcherTest.java'));t=p.read_text(encoding='utf-8');b=sha(p)
        old='        @Override\n        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}'
        if t.count(old)!=1:raise ValueError('Expected fixed-diagnosed final method override')
        p.write_text(t.replace(old,''),encoding='utf-8');edits.append({'path':p.relative_to(tests).as_posix(),'before_sha256':b,'after_sha256':sha(p),'fixed_api_repair':'Remove override of BaseMatcher final marker; inherit unchanged marker behavior.'})
    if tool!='intellisphere' or (project,seed) not in PATTERNS:return edits
    for p in tests.rglob('*.java'):
        t=p.read_text(encoding='utf-8');cleaned,suffixes=renderer_suffixes(t)
        if suffixes:
            b=sha(p);p.write_text(cleaned,encoding='utf-8');edits.append({'path':p.relative_to(tests).as_posix(),'before_sha256':b,'after_sha256':sha(p),'renderer_suffixes_removed':len(suffixes)})
        t=p.read_text(encoding='utf-8')
        rows=[x for x in positions(p) if any(q in t[utf16_index(t,x['start_utf16']):utf16_index(t,x['end_utf16'])] for q in PATTERNS[(project,seed)])]
        if rows:
            b=sha(p);remove_methods(p,rows);edits.append({'path':p.relative_to(tests).as_posix(),'before_sha256':b,'after_sha256':sha(p),'fixed_compile_methods_removed':[x['owner']+'::'+x['name'] for x in rows],'selection':'All test methods calling the unsupported API shown in preceding fixed compiler diagnostics; no buggy feedback.'})
    return edits
