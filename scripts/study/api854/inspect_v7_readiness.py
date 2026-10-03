"""Verify all capability partitions and retain a handoff worklist, without dispatch."""
import argparse
from collections import Counter
from datetime import datetime, timezone
from pathlib import Path
import re

from .common import ROOT, read_json, sha256, contained, write_json, implementation_hashes
from .preparation import clean_targets


def identities(targets):
    return {tuple(target[key] for key in ('class','constructor_types','method','parameter_types'))
            for target in clean_targets(targets)}


def inspect():
    prep = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development'
    index = read_json(prep / 'index.json')
    capability = ROOT / 'docs/api854/evidence/beam-pilot-v5-capabilities-20261003'
    capability_index = read_json(capability / 'index.json')
    discovery = ROOT / 'output/api854-20261003/prepare-v3'
    rows = {(row['project'],row['bug_id']):row for row in index['records']}
    worklist, reasons = [], Counter()
    for cap in capability_index['bugs']:
        key = (cap['project'],cap['bug_id'])
        row = rows[key]
        folder = prep / f'{key[0]}-{key[1]}'
        original = read_json(discovery / folder.name / 'targets.json')['targets']
        current = read_json(folder / 'capability-exclusions.json')
        path = contained(capability, cap['artifact'])
        if sha256(path) != cap['sha256']:
            raise ValueError('Received capability artifact hash differs')
        declared = read_json(path)
        selected, excluded = identities(current['selected']), identities([r['target'] for r in current['excluded']])
        if (selected & excluded or selected | excluded != identities(original)
                or selected != identities(declared['targets'])
                or excluded != identities([r['target'] for r in declared['excluded_fixture_capabilities']])
                or len(selected) != row['target_count'] or len(excluded) != row['capability_exclusion_count']):
            raise ValueError('Common declaration partition differs')
        for name, expected in read_json(folder / 'checksums.json').items():
            if sha256(contained(folder, name)) != expected:
                raise ValueError('Current preparation checksum differs')
        reasons.update(r['reason'] for r in current['excluded'])
        worklist.append({'project':key[0],'bug_id':key[1],'owner':row['owner'],
            'required_common_declarations':len(identities(original)), 'selected':len(selected), 'unsupported':len(excluded),
            'prompt_utf8_bytes':row['prompt_utf8_bytes'], 'prompt_sha256':sha256(folder / 'prompt.md'),
            'unsupported_targets':current['excluded'], 'shared_semantic_approval':False})
    if len(worklist) != len(rows) != 20:
        raise ValueError('Unexpected pilot inventory')
    if (len(worklist) != 20 or sum(r['selected'] for r in worklist) != 377
            or sum(r['unsupported'] for r in worklist) != 314):
        raise ValueError('Unexpected pilot declaration counts')
    xml = prep / 'JacksonXml-1/fixed-source/src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java'
    source = xml.read_text(encoding='utf-8')
    empty_enum = re.search(r'public enum Feature implements FormatFeature\s*\{\s*;', source)
    if not empty_enum:
        raise ValueError('Enum source evidence changed; require a new decision dossier')
    enum_targets = [r['target'] for row in worklist for r in row['unsupported_targets']
                    if 'FromXmlParser$Feature' in r['target']['parameter_types']]
    if len(enum_targets) != 4:
        raise ValueError('Enum target inventory changed')
    runtime = implementation_hashes()
    if index['runtime_source_sha256'] != runtime:
        raise ValueError('Candidate runtime bindings changed')
    return {'checked_at_utc':datetime.now(timezone.utc).isoformat(),
        'scope':'Current twenty-bug development readiness and immutable unsupported-target handoff; no approvals.',
        'preparation_index_sha256':sha256(prep / 'index.json'), 'capability_index_sha256':sha256(capability / 'index.json'),
        'runtime_source_sha256':runtime, 'inspector_sha256':sha256(__file__),
        'builder_sha256':index['builder_sha256'], 'composer_sha256':sha256(ROOT / 'scripts/study/api854/compose_v7_development.py'),
        'pilot_bugs':20, 'required_common_declarations':691, 'selected':377, 'unsupported':314,
        'unsupported_reason_counts':dict(reasons), 'bugs':worklist,
        'enum_decision':{'project':'JacksonXml','bug_id':1,'source_path':xml.relative_to(ROOT).as_posix(),
            'source_sha256':sha256(xml),'source_line':source[:empty_enum.start()].count('\n') + 1,
            'fact':'The fixed source declares Feature with no enum constants.', 'targets':enum_targets,
            'pending':'Joint prospective decision for an empty non-null parameter domain; retain all four signatures and denominator.',
            'null_exception_oracle_approved':False,'inventory_change_approved':False},
        'max_prompt_utf8_bytes':index['max_prompt_utf8_bytes'],
        'request_reservation_byte_floor_before_unknown_framing':index['max_prompt_utf8_bytes'] + 4096,
        'actual_provider_token_count':None,'final_reserve':None,
        'gate_a_passed':False,'generation_authorized':False,'primary_completed':0,'live_requests':0,'queue_mutations':0}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    result = inspect()
    write_json(args.output, result)
    print({k:result[k] for k in ('pilot_bugs','selected','unsupported','max_prompt_utf8_bytes')})
