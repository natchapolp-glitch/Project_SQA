"""Fixed-diagnosed XML fixture precondition repair for JacksonXml/103."""
from normalize_provider_source import sha
from provider_compatibility_v42 import repair as previous

def repair(tests,project,tool,seed):
    edits=previous(tests,project,tool,seed)
    if (project,tool,seed)!=('JacksonXml','intellisphere',103): return edits
    p=tests/'com/fasterxml/jackson/dataformat/xml/deser/FromXmlParserTest.java'
    before=sha(p)
    if before!='ed1e724a0de169b1f185d0e8f93084a9d2144c5598d6ce7588076148fbf7a97d': raise ValueError('JacksonXml103 source differs')
    text=p.read_text(encoding='utf-8'); old='XMLStreamReader sr = _xmlInputFactory.createXMLStreamReader(new StringReader(xml));'
    if text.count(old)!=1: raise ValueError('XML fixture differs')
    p.write_text(text.replace(old,old+'\n        sr.nextTag();'),encoding='utf-8')
    edits.append({'path':p.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(p),'fixed_api_compatibility':'Advance fixture XMLStreamReader to START_ELEMENT required by fixed constructor, assertions unchanged.','selection':'Fixed exception stack only; original fully-pruned v39 preserved. No buggy feedback.'})
    return edits
