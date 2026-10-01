"""Remove the exact filename header from hash-identified Jsoup source."""
from normalize_provider_source import sha
from provider_compatibility_v39 import repair as inherited_repair

def repair(tests, project, tool, seed):
    edits = inherited_repair(tests, project, tool, seed)
    if (project, tool, seed) != ('Jsoup', 'intellisphere', 103):
        return edits
    path=tests/'org/jsoup/nodes/DocumentTest.java'
    before=sha(path)
    if before!='10ad404092ed06264367f951fff292135875a951f82b3eaba2a18a2a70963f4b':
        raise ValueError('Jsoup source differs from preserved AST parsing failure')
    text=path.read_text(encoding='utf-8')
    assert text.startswith('org/jsoup/nodes/DocumentTest.java\n')
    path.write_text(text.split('\n',1)[1],encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),'fixed_api_compatibility':{'kind':'remove_filename_header','reason':'Exact filename line before package is not Java syntax. All test bodies and assertions unchanged.'},'selection':'Source syntax only; raw export and v60 input preserved. No execution outcomes used.'})
    return edits
