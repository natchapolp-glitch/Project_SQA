"""Position one hash-identified XML fixture at the fixed constructor precondition."""
from normalize_provider_source import sha
from provider_compatibility_v38 import repair as repair_v38

def repair(tests, project, tool, seed):
    edits = repair_v38(tests, project, tool, seed)
    if (project, tool, seed) != ('JacksonXml', 'intellisphere', 101):
        return edits
    path = tests / 'com/fasterxml/jackson/dataformat/xml/deser/FromXmlParserTest.java'
    if not path.exists():
        return edits
    text = path.read_text(encoding='utf-8')
    old = 'XMLStreamReader xmlReader = XML_INPUT_FACTORY.createXMLStreamReader(new StringReader(xml));'
    before = sha(path)
    if before != '7ab625decbf4f864aaafd865a81683528e121baa07915555aa3a7a185800a860' or text.count(old) != 1:
        raise ValueError('JacksonXml fixture differs from preserved fixed failure')
    path.write_text(text.replace(old, old + '\n        xmlReader.nextTag();'), encoding='utf-8')
    edits.append({'path': path.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(path), 'fixed_api_compatibility': [{
                      'kind': 'xml_reader_constructor_precondition',
                      'reason': 'Fixed XmlTokenStream constructor requires START_ELEMENT; advance newly created reader from START_DOCUMENT with nextTag. All assertions unchanged.'}],
                  'selection': 'Fixed exception stack and constructor precondition only; failed v38 attempts preserved; no buggy observations.'})
    return edits
