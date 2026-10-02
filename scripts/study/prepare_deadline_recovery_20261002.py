"""Declare exact-source, fixed-only compatibility repairs before execution."""
from pathlib import Path
import hashlib, json, re

root = Path(__file__).resolve().parents[2]
baseline = (root/'scripts/study/evaluate_provider_normalized_v74.py').read_text()
repairs = {
84: '''def repair(tests, project, tool, seed):
    if (project, tool, seed) != ('JacksonXml', 'intellisphere', 102):
        return []
    path = tests / 'com/fasterxml/jackson/dataformat/xml/deser/FromXmlParserTest.java'
    before = hashlib.sha256(path.read_bytes()).hexdigest()
    if before != '4ccae4d2241b2a22bf2e36da79afaa6b0dd2d3410a62c66ba1b09a75ea502e83':
        raise ValueError('Unexpected original source')
    source = path.read_text()
    source = source.replace('import com.fasterxml.woodstox.core.WstxInputFactory;', 'import com.ctc.wstx.stax.WstxInputFactory;')
    for method in ('enable', 'disable'):
        source = source.replace('parser.'+method+'(null)', 'parser.'+method+'((FromXmlParser.Feature) null)')
    source = source.replace('parser.configure(null,', 'parser.configure((FromXmlParser.Feature) null,')
    source, removed = re.subn(r'\\} catch \\(IOException e\\) \\{\\s*throw e;\\s*', '', source)
    if removed < 1: raise ValueError('Expected unreachable catch')
    if source.count('codec = null; // Use null codec for basic tests') != 1:
        raise ValueError('Expected fixture initialization')
    source = source.replace('codec = null; // Use null codec for basic tests', 'xmlReader.nextTag(); // Position fixture at START_ELEMENT required by constructor\\n        codec = null; // Use null codec for basic tests')
    path.write_text(source)
    content = path.read_text()
    bad = [r for r in positions(path) if 'builder2.size()' in content[utf16_index(content,r['start_utf16']):utf16_index(content,r['end_utf16'])]]
    if len(bad) != 1: raise ValueError('Expected one incompatible method')
    remove_methods(path, bad)
    return [{'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
             'after_sha256':hashlib.sha256(path.read_bytes()).hexdigest(),
             'compatibility_repair':'Correct Woodstox import, null overload casts, unreachable helper catch; exclude one unsupported builder.size method; advance XML fixture to START_ELEMENT based on fixed-only setup error. Retained assertions unchanged.'}]
''',
85: '''def repair(tests, project, tool, seed):
    if (project, tool, seed) != ('JacksonDatabind', 'intellisphere', 101):
        return []
    path = tests / 'com/fasterxml/jackson/databind/ser/BeanPropertyWriterTest.java'
    before = hashlib.sha256(path.read_bytes()).hexdigest()
    if before != 'SOURCE_HASH': raise ValueError('Unexpected original source')
    source = path.read_text()
    anchor = 'import com.fasterxml.jackson.databind.util.NameTransformer;'
    if source.count(anchor) != 1: raise ValueError('Import anchor missing')
    path.write_text(source.replace(anchor, anchor+'\\nimport com.fasterxml.jackson.databind.util.Annotations;'))
    return [{'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
             'after_sha256':hashlib.sha256(path.read_bytes()).hexdigest(),
             'compatibility_repair':'Add missing Annotations import based on fixed compile error. Assertions unchanged.'}]
'''
}
response=(root/'ai-tests/provider-captures/kku-only-20261001/claude/JacksonDatabind-1/s101-i1-newaccount-haiku-20261002/response.md').read_text(encoding='utf-8')
code=re.findall(r'```java[^\n]*\n(.*?)\n```',response,re.S|re.I)[0]
repairs[85]=repairs[85].replace('SOURCE_HASH',hashlib.sha256((code.strip()+'\n').encode()).hexdigest())
for version, repair in repairs.items():
    target=root/f'scripts/study/evaluate_provider_normalized_v{version}.py'
    if target.exists(): raise ValueError('Version already exists')
    text=baseline.replace('v74',f'v{version}')
    start=text.index('def repair(')
    end=text.index('PROCESSING_SOURCES',start)
    text=text[:start]+repair+'\n'+text[end:]
    text=text.replace('normalize, positions, prune_fixed_failures', 'normalize, positions, prune_fixed_failures, remove_methods, utf16_index')
    target.write_text(text,encoding='utf-8')
    policy=root/f'results/study/kku-only-20261001/processing-policy-v{version}.json'
    policy.write_text(json.dumps({'version':version,'basis':'fixed compile/runtime diagnostics only; no provider feedback; retained assertions unchanged','driver_sha256':hashlib.sha256(target.read_bytes()).hexdigest(),'repair_definition':repair},indent=2)+'\n')
    print(target)
