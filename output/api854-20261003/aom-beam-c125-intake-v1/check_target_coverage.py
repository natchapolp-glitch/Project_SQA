"""Check exact buffer overload entry lines from received coverage XML, offline."""
import json
from pathlib import Path
import xml.etree.ElementTree as ET
from inspect_received import Objects, BEAM, PACKET, digest, require

SIGNATURES = [
    ('JacksonCore', 'com.fasterxml.jackson.core.io.NumberInput', 'inLongRange', '([CIIZ)Z'),
    ('JacksonCore', 'com.fasterxml.jackson.core.io.NumberInput', 'parseBigDecimal', '([C)Ljava/math/BigDecimal;'),
    ('JacksonCore', 'com.fasterxml.jackson.core.io.NumberInput', 'parseBigDecimal', '([CII)Ljava/math/BigDecimal;'),
    ('JacksonCore', 'com.fasterxml.jackson.core.io.NumberInput', 'parseInt', '([CII)I'),
    ('JacksonCore', 'com.fasterxml.jackson.core.io.NumberInput', 'parseLong', '([CII)J'),
    ('JacksonCore', 'com.fasterxml.jackson.core.util.TextBuffer', 'append', '([CII)V'),
    ('JacksonCore', 'com.fasterxml.jackson.core.util.TextBuffer', 'append', '(Ljava/lang/String;II)V'),
    ('Csv', 'org.apache.commons.csv.ExtendedBufferedReader', 'read', '([CII)I'),
]

if __name__ == '__main__':
    beam = Objects(BEAM)
    rows = []
    for project, class_name, method_name, descriptor in SIGNATURES:
        evidence = []
        for approach in ('fscs-art', 'cmaes'):
            path = PACKET + f'/{project}-1-{approach}/coverage/coverage.xml'
            root = ET.fromstring(beam.blob(path))
            methods = [method for cls in root.findall('.//class')
                       if cls.attrib['name'] == class_name
                       for method in cls.findall('./methods/method')
                       if method.attrib['name'] == method_name and method.attrib['signature'] == descriptor]
            require(len(methods) == 1, 'Missing or ambiguous exact overload')
            lines = methods[0].findall('./lines/line')
            require(lines, 'Missing method entry line')
            entry = min(lines, key=lambda line: int(line.attrib['number']))
            evidence.append({'approach': approach, 'xml': path, 'xml_sha256': digest(beam.blob(path)),
                             'entry_line': int(entry.attrib['number']), 'hits': int(entry.attrib['hits'])})
        require(any(item['hits'] > 0 for item in evidence), 'Exact target absent across both sampled suites')
        rows.append({'project': project, 'bug_id': 1, 'class': class_name,
                     'method': method_name, 'descriptor': descriptor, 'evidence': evidence})
    target = Path(__file__).with_name('target-method-coverage.json')
    require(not target.exists(), 'Refuse to overwrite coverage intake')
    target.write_text(json.dumps({'beam_commit': beam.revision, 'exact_declarations_checked': 8,
        'source_sha256': digest(Path(__file__).read_bytes()), 'rows': rows,
        'received_evidence_only': True, 'team_or_primary_approval': False,
        'limitations': ['Entry coverage proves invocation, not complete domain or meaningful oracle.',
          'JacksonCore CMA-ES did not cover TextBuffer.append(String,int,int); FSCS-ART did.']},
        indent=2)+'\n', encoding='utf8')
    print('Exact target method entry coverage: 8 declarations covered across the two approaches; one per-approach gap retained')
