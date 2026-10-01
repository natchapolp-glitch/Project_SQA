"""Repair fixed-diagnosed XML fixture and serializer generic compatibility."""
from normalize_provider_source import sha
from provider_compatibility_v17 import repair as prior

def repair(tests,project,tool,seed):
    edits = prior(tests,project,tool,seed)
    replacements = []
    if (project,tool,seed) == ('JacksonXml','intellisphere',102):
        replacements.append(('return new FromXmlParser(ctxt, genericFeatures, xmlFeatures, codec, sr);',
            'sr.nextTag();\n        return new FromXmlParser(ctxt, genericFeatures, xmlFeatures, codec, sr);',
            'Fixed XmlTokenStream constructor requires START_ELEMENT; fresh StAX reader starts on START_DOCUMENT. Advance fixture to first tag before constructing parser.'))
    if (project,tool,seed) == ('JacksonDatabind','intellisphere',101):
        replacements.append(('new StringSerializer()',
            '(JsonSerializer<Object>) (JsonSerializer<?>) new StringSerializer()',
            'Fixed StringSerializer is JsonSerializer<String>; explicit generic bridge retains serializer instance and assertions for Object-typed writer API.'))
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        changes = []
        for old,new,reason in replacements:
            if old in text:
                changes.append({'old':old,'new':new,'occurrences':text.count(old),'reason':reason})
                text = text.replace(old,new)
        if changes:
            before = sha(path)
            path.write_text(text,encoding='utf-8')
            edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
                'after_sha256':sha(path),'local_compatibility_repairs':changes,
                'selection':'Preserved fixed logs and fixed API source only; no buggy outcome, production or assertion changes.'})
    return edits
