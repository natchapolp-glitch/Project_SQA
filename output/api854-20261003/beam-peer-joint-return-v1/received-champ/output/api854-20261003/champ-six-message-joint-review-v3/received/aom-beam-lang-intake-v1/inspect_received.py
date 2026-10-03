"""Offline scoped Lang intake from immutable commits; does not compose or approve.

Use a new --output directory for each run. Reuses the retained buffer intake's
Git-object reader, whose source hash is recorded in this receipt.
"""
import argparse
import base64
import hashlib
import io
import json
from pathlib import Path
import subprocess
import sys
import tarfile
import types
import xml.etree.ElementTree as ET

PREVIOUS = Path(__file__).resolve().parent.parent / 'aom-beam-c125-intake-v1/inspect_received.py'
import importlib.util
spec = importlib.util.spec_from_file_location('buffer_intake_reader', PREVIOUS)
reader = importlib.util.module_from_spec(spec)
spec.loader.exec_module(reader)
Objects, require, digest, identity = reader.Objects, reader.require, reader.digest, reader.identity
BEAM = '22982e8c3b63380830cdbbe8d282c6593314cadf'
CHAMP = 'e742095d'
AOM = 'ec26350fc919d1be2b4a559c3f91b0072ba04f75'
PACKET = 'docs/api854/evidence/beam-lang-development-20261003-v1'
REF = 'docs/api854/evidence/beam-lang-reference-20261003-v'
V9 = 'output/api854-20261003/prepare-v9-twenty-bug-development'
JAVA = 'src/main/java/org/apache/commons/lang3/math/NumberUtils.java'
HELPERS = [('isAllZeros', '(Ljava/lang/String;)Z'), ('validateArray', '(Ljava/lang/Object;)V')]


def scalar(kind, value):
    return kind + ':' + base64.b64encode(str(value).encode()).decode()


def expected(row):
    value = row['declared_input']['input']
    if row['target']['method'] == 'isAllZeros':
        answer = value is None or (len(value) > 0 and set(value) == {'0'})
        require(row['declared_input']['expected_boolean'] == answer, 'Declared Boolean contract')
        return 'value:' + scalar('java.lang.Boolean', str(answer).lower()) + '|state=stateless-scalars'
    require(value is None or isinstance(value, list), 'Non-array fixture')
    state = 'null' if value is None else '[I[' + ''.join(scalar('java.lang.Integer', n)+';' for n in value) + ']'
    message = 'The Array must not be null' if value is None else 'Array cannot be empty.' if not value else None
    require(row['declared_input']['expected_message'] == message, 'Declared exception contract')
    return ('void' if message is None else 'exception:java.lang.IllegalArgumentException|message='
            + scalar('java.lang.String', message)) + '|state=validation-input:' + state


def check_stages(beam, directory, count):
    for stage in ('fixed-1', 'fixed-2', 'buggy', 'coverage'):
        counters = beam.json(directory+'/'+stage+'/sqa-stage-counts.json')
        require((counters['executed'], counters['skipped'], counters['target_checks']) == (count, 0, count), 'Counters')
        command = beam.json(directory+'/'+stage+'/command.json')
        require(command['exit_code'] == 0 and command['timed_out'] is False, 'Failed stage')
        require(not beam.blob(directory+'/'+stage+'/failing_tests').strip(), 'Failing tests')
        require(len(beam.blob(directory+'/'+stage+'/all_tests').splitlines()) == count, 'Raw test count')
    xml = ET.fromstring(beam.blob(directory+'/coverage/coverage.xml'))
    coverage = {key: int(xml.attrib[attr]) for key, attr in
                [('line_covered','lines-covered'),('line_total','lines-valid'),
                 ('branch_covered','branches-covered'),('branch_total','branches-valid')]}
    entries = []
    for name, descriptor in HELPERS:
        methods = [m for c in xml.findall('.//class') if c.attrib['name'] == 'org.apache.commons.lang3.math.NumberUtils'
                   for m in c.findall('./methods/method') if m.attrib['name'] == name and m.attrib['signature'] == descriptor]
        require(len(methods) == 1, 'Exact helper overload missing')
        line = min(methods[0].findall('./lines/line'), key=lambda l:int(l.attrib['number']))
        entries.append({'method':name,'descriptor':descriptor,'entry_line':int(line.attrib['number']),
                        'entry_hits':int(line.attrib['hits'])})
    return coverage, entries


def run(output):
    output = Path(output)
    require(not output.exists(), 'Refuse to overwrite sealed receipt')
    beam, champ, aom = Objects(BEAM), Objects(CHAMP), Objects(AOM)
    sizes = [beam.checksums(path) for path in (PACKET, REF+'1', REF+'2')]
    require(sum(sizes) == 281, 'Expected 281 received checksums')
    index = beam.json(PACKET+'/index.json')
    protocol = beam.json(PACKET+'/protocol-proposal.json')
    pins = protocol['source_sha256']
    require(len(pins) == 41 and index['source_sha256'] == pins, 'Runtime inventory')
    for path, value in pins.items():
        require(digest(beam.blob(path)) == value == digest(beam.blob(PACKET+'/runtime-implementation/'+path)), 'Runtime drift')
    require(index['protocol_sha256'] == digest(beam.blob(PACKET+'/protocol-proposal.json'))
            and index['team_or_primary_approval'] is False and len(index['records']) == 2, 'Scope/protocol')
    reference_rows = []
    for attempt in (1, 2):
        directory = REF+str(attempt)
        result = beam.json(directory+'/receipt.json')
        require(result['source_sha256'] == pins and result['team_or_primary_approval'] is False, 'Reference condition')
        require(result['reviewer_sha256'] == digest(beam.blob(directory+'/lang_fixture_review.py')), 'Reference producer')
        require(result['fixed_source_sha256'][JAVA] == digest(beam.blob(directory+'/fixed-source/'+JAVA)), 'Fixed source')
        require(result['suite_sha256'] == digest(beam.blob(directory+'/package/suite.tar.bz2')), 'Reference suite')
        observations = beam.json(directory+'/reference-observations.json')
        require(len(observations) == 12, 'Reference count')
        for row in observations:
            require(row['target']['constructor_types'] == '' and row['reference_check_passed'] is True
                    and row['fixed_first'] == row['fixed_second'] and row['fixed_first']['status'] == 'ok'
                    and row['fixed_first']['target_invoked'] is True
                    and row['fixed_first']['outcome'] == row['expected'] == expected(row), 'Reference oracle')
        require(sum(row['target']['method']=='isAllZeros' for row in observations) == 8, 'Helper partition')
        require(sum(row['target']['method']=='validateArray' for row in observations) == 4, 'Helper partition')
        coverage, entries = check_stages(beam, directory+'/evaluation', 12)
        measured = beam.json(directory+'/evaluation/record.json')
        require(measured['fixed_validation'] == 'passed_twice' and measured['fault_detected'] is False
                and measured['status'] == 'complete' and all(row['entry_hits']>0 for row in entries), 'Reference measurement')
        require(all(measured[k] == v for k,v in coverage.items()), 'Reference XML')
        reference_rows.append({'attempt':attempt,'suite_sha256':result['suite_sha256'],
            'local_development_valid':result['local_development_valid'],'actual_fixed_validation':measured['fixed_validation'],
            'stage_executed':12,'stage_skipped':0,'stage_target_checks':12,'fault_detected':False,'entries':entries,**coverage})
    require(reference_rows[0]['suite_sha256'] == reference_rows[1]['suite_sha256']
            and reference_rows[0]['local_development_valid'] is False
            and reference_rows[1]['local_development_valid'] is True, 'Preserved attempt history')
    sampled = []
    for record in index['records']:
        directory = PACKET+'/Lang-1-'+record['approach']
        result = beam.json(directory+'/original-evaluation-result.json')
        review = beam.json(directory+'/semantic-review-result.json')
        require(result['usable'] is False and result['measurement']['fault_detected'] is False, 'Original results')
        require(review['evaluation_result_sha256'] == digest(beam.blob(directory+'/original-evaluation-result.json'))
                and review['status'] == 'valid' and review['review']['fixture_oracle_review']['team_or_primary_approval'] is False,
                'Review supplement')
        suite = beam.blob(directory+'/suite.tar.bz2')
        require(digest(suite) == result['suite_sha256'] == record['suite_sha256'], 'Sample suite')
        with tarfile.open(fileobj=io.BytesIO(suite)) as archive:
            java = [archive.extractfile(m).read() for m in archive.getmembers() if m.isfile() and m.name.endswith('GeneratedStudyTest.java')]
        require(java == [beam.blob(directory+'/generation/GeneratedStudyTest.java')], 'Suite source')
        coverage, entries = check_stages(beam, directory, 30)
        require(all(result['measurement'][k] == record[k] == v for k,v in coverage.items()), 'Sample XML')
        require(all(row['entry_hits']>0 for row in entries), 'Sample helper coverage')
        sampled.append({'approach':record['approach'],'stage_executed':30,'stage_skipped':0,'stage_target_checks':30,
                        'original_usable':False,'local_semantic_review':'valid','team_approval':False,
                        'fault_detected':False,'entries':entries,**coverage})
    policy = types.ModuleType('received_lang_policy')
    exec(compile(beam.blob('scripts/study/api854/fixture_policy.py'),'received fixture policy','exec'),policy.__dict__)
    inventory = []
    for bug in beam.json('docs/api854/evidence/beam-aom-review-20261003/index.json')['bugs']:
        inventory.extend(dict(t,project=bug['project'],bug_id=bug['bug_id']) for t in beam.json(
            'docs/api854/evidence/beam-aom-review-20261003/declarations/'+f"{bug['project']}-{bug['bug_id']}/targets.json")['targets'])
    prior = set()
    for name in (policy.POLICY_V6, policy.POLICY_V6_BUFFER):
        prior.update(identity(t) for t in policy.select(inventory,name)[0])
    selected, excluded = policy.select(inventory, policy.POLICY_V9_LANG)
    combined = {identity(t) for t in selected}
    added = combined-prior
    require((len(inventory),len(prior),len(selected),len(excluded),len(added)) == (691,387,389,302,2), 'Selection delta')
    require({tuple(t[2:]) for t in added} == policy.LANG_SIGNATURES and all(t[:2]==('Lang',1) for t in added), 'Exact Lang scope')
    v9_ids = set()
    for bug in champ.json(V9+'/index.json')['records']:
        v9_ids.update(identity(t,bug['project'],bug['bug_id']) for t in champ.json(V9+f"/{bug['project']}-{bug['bug_id']}/targets.json")['targets'])
    require(len(v9_ids)==380 and not(v9_ids&added), 'Shared v9 delta')
    fixed_hash = beam.json(REF+'2/receipt.json')['fixed_source_sha256'][JAVA]
    require(digest(champ.blob(V9+'/Lang-1/fixed-source/'+JAVA)) == fixed_hash, 'Shared fixed source differs')
    historical = subprocess.check_output(['git','ls-tree','-r','--name-only',aom.revision],text=True).splitlines()
    retained = [p for p in historical if '/prepare-v8-fraction-field-development/' in p]
    require(retained and all(digest(aom.blob(p))==digest(Path(p).read_bytes()) for p in retained), 'Historical v8 changed')
    tests = beam.json('output/api854-20261003/beam-lang-test-receipt-v1.json')
    for command in tests['commands']:
        require(digest(beam.blob('output/api854-20261003/'+command['log'])) == command['log_sha256'], 'Received test log')
    receipt = {'schema_version':1,'beam_commit':beam.revision,'champ_commit':champ.revision,'aom_base':aom.revision,
        'status':'aom_scoped_review_complete_champ_verdict_pending','checksum_entries_verified':sum(sizes),
        'packet_checksum_counts':sizes,'runtime_pins_verified':41,'sampled_suites':sampled,'references':reference_rows,
        'reference_cases_in_current_attempt':12,'reference_observations_in_current_attempt':24,
        'new_lang_targets':[dict(zip(reader.FIELDS,t)) for t in sorted(added)],'fixed_source_sha256':{JAVA:fixed_hash},
        'beam_condition':{'selected':389,'unsupported':302},'shared_v9':{'selected':380,'unsupported':311},
        'if_lang_only_accepted':{'selected':len(v9_ids|added),'unsupported':691-len(v9_ids|added)},
        'if_lang_and_buffer_accepted':{'selected':len(v9_ids|combined),'unsupported':691-len(v9_ids|combined)},
        'historical_v8_files_unchanged':len(retained),'runtime_source_sha256':pins,
        'aom_scoped_verdict':'suitable_for_bounded_development_composition_subject_to_champ_review',
        'joint_acceptance':False,'new_shared_preparation':False,'new_prompt_measurement':False,
        'gate_a_approved':False,'primary_added':0,'real_kku_requests':0,'live_queue_mutations':0,
        'aom_defects4j_reruns':0,'inspector_sha256':digest(Path(__file__).read_bytes()),
        'git_reader_sha256':digest(PREVIOUS.read_bytes()),
        'limitations':['Bounded array recipe uses int[] only, not every array type or legal input.',
          'New Beam policy implicitly enables buffer and changes Csv streams; do not adopt it wholesale for Lang-only acceptance.',
          'Champ v9 setter/JDOM repairs must be retained; count 389 is not the final shared selection.',
          'Fixed private static helper checks do not certify callers, complete domain coverage or 389 semantic approvals.']}
    output.mkdir(parents=True,exist_ok=False)
    for name,data in [('receipt.json',receipt),('received-object-sha256.json',{o.revision:o.bindings for o in (beam,champ,aom)})]:
        (output/name).write_bytes((json.dumps(data,indent=2)+'\n').encode())
    print(json.dumps({k:receipt[k] for k in ['status','checksum_entries_verified','historical_v8_files_unchanged',
                      'if_lang_only_accepted','if_lang_and_buffer_accepted']},indent=2))
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',required=True)
    run(parser.parse_args().output)
