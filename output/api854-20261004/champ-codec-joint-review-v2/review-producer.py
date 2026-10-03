"""Review five pinned Codec candidates and rerun production Java in isolation."""
import argparse
import base64
import copy
import csv
from datetime import datetime, timezone
import gzip
import io
import json
from pathlib import Path
import re
import subprocess
import sys
import tarfile
import tempfile
import time

from .common import ROOT, sha256, write_json
from .review_joint_recipe_intake import digest, require
from .review_v10_readiness import AOM, CONDITION, PREP, BatchedObjects, target_key
from .verify_chronology_development import checkpoint

BEAM = 'a44f796f6b72281b0bd39d3f26bfc80519e00710'
BASE = 'output/api854-20261004/beam-codec-candidate-v1'
PREFIX = 'src/java/org/apache/commons/codec/language/'
METAPHONE = 'org.apache.commons.codec.language.Metaphone'
SOUNDEX = 'org.apache.commons.codec.language.SoundexUtils'
ENCODED = {None:'', '':'', 'A':'A', 'a':'A', 'E':'E', 'B':'B', 'AB':'AB'}
DESCRIPTORS = {'isNextChar':'(Ljava/lang/StringBuffer;IC)Z', 'isPreviousChar':'(Ljava/lang/StringBuffer;IC)Z',
               'isVowel':'(Ljava/lang/StringBuffer;I)Z', 'regionMatch':'(Ljava/lang/StringBuffer;ILjava/lang/String;)Z',
               'difference':'(Lorg/apache/commons/codec/StringEncoder;Ljava/lang/String;Ljava/lang/String;)I'}
STAGES = tuple(revision + '_' + suffix for revision in ('fixed','buggy') for suffix in ('first','second','method_entry'))
MUTATIONS = {'next_char':('Metaphone.java', 'matches = string.charAt(index + 1) == c;',
                         'matches = string.charAt(index + 1) != c;'),
             'difference_score':('SoundexUtils.java', 'return differenceEncoded(encoder.encode(s1), encoder.encode(s2));',
                                 'return differenceEncoded(encoder.encode(s1), encoder.encode(s2)) + 1;')}


def oracle(case):
    """Scalar and state contract derived independently from the bounded domain."""
    method, text, at = case['method'], case['buffer'], case['index']
    error, codes = None, None
    if method == 'difference':
        require(case['s1'] in ENCODED and case['s2'] in ENCODED, 'Unreviewed encoder input')
        codes = [ENCODED[case['s1']], ENCODED[case['s2']]]
        value = sum(codes[0][i] == codes[1][i] for i in range(min(map(len, codes))))
    else:
        require(type(text) is str and text.isascii() and type(at) is int and -1 <= at <= 5, 'Unreviewed buffer/index')
        if method == 'isNextChar':
            value = 0 <= at < len(text)-1 and text[at+1] == case['char']
        elif method == 'isPreviousChar':
            value = 0 < at < len(text) and text[at-1] == case['char']
        elif method == 'isVowel':
            if not 0 <= at < len(text):
                value, error = None, 'java.lang.StringIndexOutOfBoundsException'
            else:
                value = text[at] in 'AEIOU'
        elif method == 'regionMatch':
            needle = case['needle']
            require(type(needle) is str and needle.isascii(), 'Unreviewed needle')
            value = 0 <= at <= len(text) and len(needle) <= len(text)-at and text[at:at+len(needle)] == needle
        else:
            raise ValueError('Unknown Codec helper')
    require(type(value) is type(case['expected_value']) and value == case['expected_value']
            and error == case['expected_exception_class'], 'Predeclared Codec expectation differs')
    return {'value':value,'exception_class':error,'buffer_contents':text,
            'buffer_length':None if text is None else len(text),'buffer_unchanged':True,
            'receiver_max_code_len':None if method == 'difference' else 4,
            'encoder_max_code_len':4 if method == 'difference' else None,'encoder_encoded_inputs':codes}


def records(raw):
    return [json.loads(line) for line in raw.decode('utf-8').splitlines() if line.strip()]


def validate(raw, cases, allow_failures=False, traced=False, exit_code=0):
    data = records(raw)
    rows = [r for r in data if 'case' in r and not r.get('method_entry')]
    require([r['case'] for r in rows] == [c['case'] for c in cases], 'Codec case inventory/order differs')
    for case, row in zip(cases, rows):
        method = case['method']
        owner = SOUNDEX if method == 'difference' else METAPHONE
        require(row['setup_succeeded'] is True and row['target_invoked'] is True, 'Codec fixture is not target evidence')
        require(row['method'] == method and row['receiver_class'] == row['declaring_class'] == owner
                and row['descriptor'] == DESCRIPTORS[method], 'Wrong exact Codec target/receiver/descriptor')
        expected = oracle(case)
        require(row['expected_observation'] == expected
                and type(row['expected_observation']['value']) is type(expected['value']), 'Independent Codec oracle differs')
        text = case['buffer']
        state = {'buffer_contents':text,'buffer_length':None if text is None else len(text),
                 'buffer_capacity':None if text is None else len(text)+16}
        require(row['pre_state'] == row['post_state'] == state, 'Codec buffer contents/length/capacity changed')
        passed = row['observation'] == expected and type(row['observation']['value']) is type(expected['value'])
        require(type(row['target_check_passed']) is bool and row['target_check_passed'] is passed
                and row['failure_class'] == (None if passed else 'java.lang.AssertionError'), 'Codec assertion/failure differs')
        require(allow_failures or passed, 'Codec production oracle failed')
    count = sum(r['target_check_passed'] for r in rows)
    require([r for r in data if r.get('summary')] == [{'summary':True,'executed':43,'target_checks':43,
            'passed':count,'failed':43-count,'skipped':0,'fixture_errors':0}], 'Codec summary/skips/fixture errors differ')
    entries = [r for r in data if r.get('method_entry')]
    if traced:
        first = {}
        for entry in entries:
            require(entry['source_line'] > 0 and entry['case'] in {c['case'] for c in cases}
                    and entry['class'] in (METAPHONE,SOUNDEX), 'Invalid production entry')
            first.setdefault(entry['case'], entry)
        require(set(first) == {c['case'] for c in cases}, 'Missing exact Codec method entry')
        for case in cases:
            entry, method = first[case['case']], case['method']
            require((entry['class'],entry['method'],entry['descriptor'])
                    == (SOUNDEX if method == 'difference' else METAPHONE,method,DESCRIPTORS[method]),
                    'Wrong first Codec method entry')
        require(len({(e['class'],e['method'],e['descriptor']) for e in first.values()}) == 5
                and [r for r in data if r.get('trace_summary')] == [{'trace_summary':True,
                    'method_entries':len(entries),'debuggee_exit_code':exit_code}], 'Codec trace counters differ')
    return rows


def execute(command, output, name, expected_exit=0, cwd=None):
    begin = time.monotonic()
    result = subprocess.run(list(map(str, command)), capture_output=True, timeout=180, cwd=cwd)
    for kind, raw in (('stdout',result.stdout),('stderr',result.stderr)):
        (output / (name + '.' + kind + '.log')).write_bytes(raw)
    meta = {'argv':list(map(str,command)),'exit_code':result.returncode,'duration_seconds':time.monotonic()-begin,
            'stdout_sha256':digest(result.stdout),'stderr_sha256':digest(result.stderr)}
    if cwd is not None:
        meta['cwd'] = str(cwd)
    write_json(output / (name + '.command.json'), meta)
    require(result.returncode == expected_exit, 'Command failed: ' + name + ' / ' + result.stderr.decode(errors='replace')[-1200:])
    return result, meta


def extract_sources(raw, destination):
    sources = {}
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive.getmembers():
            if not member.isfile():
                continue
            target = (destination / member.name).resolve()
            require(target.is_relative_to(destination), 'Production archive escaped temporary workspace')
            data = archive.extractfile(member).read()
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(data)
            if member.name.endswith('.java'):
                sources[member.name] = data
    return sources


def run(defects4j, output):
    output, defects4j = Path(output).resolve(), Path(defects4j).resolve()
    require(output.is_relative_to(ROOT / 'output') and not output.exists(), 'Require a new output directory')
    pins, _ = checkpoint()
    beam, aom = BatchedObjects(BEAM), BatchedObjects(AOM)
    manifest = beam.document(BASE + '/checksums.json')
    beam.preload([BASE + '/' + path for path in manifest])
    require(beam.checksums(BASE) == 88 and beam.checksums(BASE + '/native-v1') == 68, 'Received Codec manifests differ')
    policy, template, receipt, native, seal = [beam.document(BASE + '/' + path) for path in (
        'policy.json','joint-review.template.json','receipt.json','native-v1/receipt.json','native-v1/preexecution-seal.json')]
    cases = policy['cases']
    require(len(cases) == policy['cases_predeclared'] == 43 and len({c['case'] for c in cases}) == 43
            and {m:sum(c['method']==m for c in cases) for m in DESCRIPTORS}
            == {'isNextChar':8,'isPreviousChar':8,'isVowel':9,'regionMatch':9,'difference':9}, 'Codec scope/case partition differs')
    require(policy['metaphone_encoded_reference_table'] == {('<null>' if k is None else k):v for k,v in ENCODED.items()},
            'Encoded literal references differ from reviewed production contract')
    for case in cases:
        oracle(case)
    provenance = beam.document(BASE + '/received-provenance.json')
    require(len(provenance) == 7 and seal['input_provenance'] == provenance, 'Codec input provenance differs')
    aom.preload([r['source_path'] for r in provenance])
    for row in provenance:
        require(row['commit'] == AOM and digest(aom.blob(row['source_path']))
                == row['sha256'] == digest(beam.blob(row['received_path'])), 'Codec original/received source differs')
    selected = aom.document(PREP + '/Codec-1/targets.json')['targets']
    excluded = aom.document(PREP + '/Codec-1/capability-exclusions.json')['excluded']
    require(len(selected) == 13 and len(excluded) == 5
            and [target_key(r['target']) for r in template['candidates']] == [target_key(r['target']) for r in excluded],
            'Codec candidates must be exactly the five excluded v10 identities')
    require(template['base_aom_commit'] == policy['aom_base_commit'] == AOM
            and template['base_condition'] == policy['shared_base_condition'] == CONDITION
            and template['actual_selected'] == receipt['actual_shared_selected'] == 390
            and template['actual_exclusions'] == receipt['actual_shared_exclusions'] == 301
            and template['possible_codec_only_next_condition'] == {'selected':395,'exclusions':296,'implemented':False},
            'Codec baseline/proposed partition differs')
    for candidate, declared in zip(template['candidates'], policy['targets']):
        method = candidate['target']['method']
        require(all(candidate[k] == declared[k] for k in ('target','descriptor','declaring_class'))
                and candidate['descriptor'] == DESCRIPTORS[method]
                and candidate['declaring_class'] == (SOUNDEX if method=='difference' else METAPHONE)
                and candidate['beam_verdict'] == 'accepted_for_bounded_candidate_oracle_development'
                and candidate['champ_verdict'] is None and candidate['agreed_preconditions'] is None
                and candidate['agreed_oracle'] is None and candidate['accepted_into_shared_inputs'] is False,
                'Codec template target/verdict differs')
    for ref in template['evidence']:
        require(digest(beam.blob(ref['path'])) == ref['sha256'], 'Joint evidence hash differs')
    require(digest(beam.blob(BASE + '/native-v1/preexecution-seal.json')) == native['preexecution_seal_sha256']
            == receipt['native_seal_sha256'] and seal['selection_used_buggy_outcomes'] is False and seal['primary'] is False,
            'Native selection/seal differs')
    for name, value in seal['suite_sha256'].items():
        require(digest(beam.blob(BASE + '/' + name)) == value, 'Codec sealed suite differs')
    declared_tsv = []
    def encode(value):
        return '-' if value is None else base64.b64encode(value.encode('utf-8')).decode()
    for case in cases:
        refs = oracle(case)['encoder_encoded_inputs'] or [None,None]
        fields = [case['case'],case['method'],encode(case['buffer']),str(case['index']),str(ord(case['char'])),
                  encode(case['needle']),encode(case['s1']),encode(case['s2']),json.dumps(case['expected_value']),
                  case['expected_exception_class'] or '-',encode(refs[0]),encode(refs[1])]
        declared_tsv.append('\t'.join(fields))
    tsv = ('\n'.join(declared_tsv) + '\n').encode('utf-8')
    require(beam.blob(BASE + '/native-v1/cases.tsv') == tsv
            and digest(tsv) == seal['cases_tsv_sha256'] == native['cases_tsv_sha256'], 'Actual Java case arguments differ')
    require(native['suite_sha256'] == seal['suite_sha256'] and native['source_sha256'] == seal['source_sha256']
            and native['revisions'] == seal['revisions'] and native['runtime_source_sha256'] == seal['runtime_source_sha256'],
            'Native receipt/source seal differs')
    beam.preload([p for p in seal['runtime_source_sha256'] if p not in beam.cache])
    for path, value in seal['runtime_source_sha256'].items():
        require(digest(beam.blob(path)) == value, 'Historical Beam runtime pin differs')
    commands = [p for p in beam.document(BASE + '/native-v1/checksums.json') if p.endswith('.command.json')]
    require(len(commands) == 21, 'Native command inventory differs')
    for name in commands:
        command = beam.document(BASE + '/native-v1/' + name)
        stem = name.removesuffix('.command.json')
        raw = beam.blob(BASE + '/native-v1/' + command['retained_stdout'])
        require(digest(raw) == command['retained_stdout_sha256']
                and digest(gzip.decompress(raw) if command['retained_stdout'].endswith('.gz') else raw) == command['stdout_sha256']
                and digest(beam.blob(BASE + '/native-v1/' + stem + '.stderr.log')) == command['stderr_sha256'],
                'Native raw command/log/archive differs')
        wanted = 9 if stem == 'held-cpu-lock' else 1 if stem.endswith('-mutation') else 0
        require(command['exit_code'] == wanted, 'Native command exit differs')
    for stage in STAGES:
        rows = validate(beam.blob(BASE + '/native-v1/' + stage + '.stdout.log'), cases, traced=stage.endswith('method_entry'))
        require(rows == native['stages'][stage]['cases'] and native['stages'][stage]['exit_code'] == 0,
                'Native production rows differ from receipt')
    for name in MUTATIONS:
        rows = validate(beam.blob(BASE + '/native-v1/' + name + '-mutation.stdout.log'), cases, allow_failures=True)
        failed = [r['case'] for r in rows if not r['target_check_passed']]
        require(failed == native['mutations'][name]['failed_cases'] and len(failed) == (3 if name=='next_char' else 9),
                'Native mutation sensitivity differs')
    require(native['status'] == 'pass' and native['candidate_fault_detected'] is False and native['buggy_failed_cases'] == []
            and native['worker_id'] == 'beam-pc1' and native['cpu_slots'] == 1 and native['cpu_lock_exits'] == [9,0]
            and '11.' in native['java_version'] and native['shared_integration_approved'] is False
            and native['full_legal_domain_approval'] is False and native['line_or_branch_coverage_percentage'] is None
            and native['gate_a_approved'] is False and native['final_reserve'] is None
            and native['kku_requests'] == native['queue_mutations'] == native['primary_added'] == 0,
            'Native candidate scope/host differs')
    tests = beam.document(BASE + '/focused-tests.command.json')
    test_log = beam.blob(BASE + '/focused-tests.stderr.log').decode('utf-8')
    require(tests['exit_code'] == 0 and tests['test_source_sha256'] == digest(beam.blob(BASE + '/test_evidence.py'))
            and re.search(r'Ran 7 tests in', test_log) and test_log.strip().endswith('OK')
            and len(re.findall(r'\.\.\. ok\s*$',test_log,re.MULTILINE)) == 7, 'Received focused tests differ')
    for stream in ('stdout','stderr'):
        require(digest(beam.blob(BASE + '/focused-tests.' + stream + '.log')) == tests[stream+'_sha256'], 'Test log pin differs')
    with (defects4j / 'framework/projects/Codec/commit-db').open() as stream:
        row = next(r for r in csv.reader(stream) if r[0]=='1')
    require(native['revisions'] == {'fixed':row[2],'buggy':row[1]}, 'Local Defects4J original revisions differ')
    output.mkdir(parents=True, exist_ok=False)
    (output / 'review-producer.py').write_bytes(Path(__file__).read_bytes())
    for name in ('policy.json','joint-review.template.json','receipt.json','CodecCandidateProbe.java','CodecEntryTrace.java'):
        (output / ('received-' + name)).write_bytes(beam.blob(BASE + '/' + name))
    snapshots, native_comparisons = {}, {}
    with tempfile.TemporaryDirectory(dir=ROOT / 'output', prefix='.champ-codec-review-') as temporary:
        temporary = Path(temporary).resolve()
        require(temporary.is_relative_to(ROOT / 'output'), 'Temporary Codec review escaped workspace')
        snap_base = temporary / BASE
        for path in [BASE + '/checksums.json', *[BASE+'/'+p for p in manifest], *seal['runtime_source_sha256']]:
            target = temporary / path
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(beam.blob(path))
        for version, revision in native['revisions'].items():
            working = temporary / ('native-' + version)
            working.mkdir()
            raw = subprocess.check_output(['git','-c','core.autocrlf=false','-C',str(defects4j / 'project_repos/commons-codec.git'),
                                           'archive',revision,'src/java'])
            sources = extract_sources(raw, working)
            peer_archive = gzip.decompress(beam.blob(BASE + '/native-v1/' + version + '-production-archive.stdout.tar.gz'))
            require(digest(raw) == digest(peer_archive), 'Local/Beam production archive differs')
            if version == 'fixed':
                for name in ('Metaphone.java','SoundexUtils.java'):
                    path = PREFIX + name
                    retained = aom.blob(PREP + '/Codec-1/fixed-source/' + path)
                    require(sources[path].replace(b'\r\n',b'\n') == retained.replace(b'\r\n',b'\n'), 'Fixed retained source differs')
                    (working / path).write_bytes(retained)
                    sources[path] = retained
            inventory = {p:digest(raw) for p,raw in sources.items()}
            require(len(inventory) == 25 and inventory == seal['source_sha256'][version], 'Compiled Codec source inventory differs')
            snapshots[version] = {'revision':revision,'archive_sha256':digest(raw),'export_core_autocrlf':False,
                                  'compiled_source_sha256':inventory}
        write_json(output / 'preexecution-seal.json', {'sealed_at_utc':datetime.now(timezone.utc).isoformat(),
                   'beam_commit':BEAM,'aom_commit':AOM,'policy_sha256':digest(beam.blob(BASE + '/policy.json')),
                   'suite_sha256':seal['suite_sha256'],'cases_tsv_sha256':seal['cases_tsv_sha256'],
                   'original_production_sources':snapshots,'champ_checkpoint_pins':pins,
                   'selection_used_buggy_outcomes':False,'primary':False})
        tested, _ = execute([sys.executable,'-B','-X','utf8',snap_base / 'test_evidence.py'], output,
                            'received-focused-tests', cwd=temporary)
        fresh_log = tested.stderr.decode('utf-8')
        require(re.search(r'Ran 7 tests in',fresh_log) and fresh_log.strip().endswith('OK')
                and len(re.findall(r'\.\.\. ok\s*$',fresh_log,re.MULTILINE)) == 7, 'Fresh focused test count/status differs')
        classes = {}
        originals = {}
        for revision in ('fixed','buggy'):
            working = temporary / ('native-' + revision)
            cp = working / 'classes'
            cp.mkdir()
            classes[revision] = cp
            execute(['javac','--release','8','-g','-encoding','UTF-8','-sourcepath',working / 'src/java','-d',cp,
                     working / (PREFIX+'Metaphone.java'),working / (PREFIX+'SoundexUtils.java'),
                     snap_base / 'CodecCandidateProbe.java'], output, revision + '-compile')
            originals[revision] = {owner:sha256(cp / (owner.replace('.','/')+'.class')) for owner in (METAPHONE,SOUNDEX)}
            if revision == 'fixed':
                execute(['javac','--add-modules','jdk.jdi','-d',cp,snap_base / 'CodecEntryTrace.java'], output, 'compile-entry-trace')
            for suffix in ('first','second','method_entry'):
                stage = revision + '_' + suffix
                argv = (['java','--add-modules','jdk.jdi','-cp',classes['fixed'],'CodecEntryTrace',cp,
                         snap_base / 'native-v1/cases.tsv'] if suffix == 'method_entry' else
                        ['java','-Duser.timezone=UTC','-Duser.language=en','-Duser.country=US','-cp',cp,
                         'sqa.development.CodecCandidateProbe',snap_base / 'native-v1/cases.tsv'])
                observed, command = execute(argv, output, stage)
                checked = validate(observed.stdout, cases, traced=suffix=='method_entry')
                require(checked == native['stages'][stage]['cases'], 'Fresh native/Beam bounded outcomes differ')
                native_comparisons[stage] = {'command':command,'bounded_rows_equal_to_beam':True,'cases':43}
            require(originals[revision] == {owner:sha256(cp / (owner.replace('.','/')+'.class')) for owner in (METAPHONE,SOUNDEX)},
                    'Tracing changed Codec production bytecodes')
        mutations = {}
        for label, (name, old, new) in MUTATIONS.items():
            path = temporary / 'native-fixed' / (PREFIX + name)
            original = path.read_bytes()
            require(original.count(old.encode()) == 1, 'Unexpected Codec sensitivity expression')
            changed = original.replace(old.encode(), new.encode())
            path.write_bytes(changed)
            execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',classes['fixed'],'-d',classes['fixed'],path],
                    output, label + '-compile')
            observed, command = execute(['java','-Duser.timezone=UTC','-Duser.language=en','-Duser.country=US','-cp',classes['fixed'],
                'sqa.development.CodecCandidateProbe',snap_base / 'native-v1/cases.tsv'], output, label + '-mutation', expected_exit=1)
            checked = validate(observed.stdout, cases, allow_failures=True)
            failed = [r['case'] for r in checked if not r['target_check_passed']]
            require(failed == native['mutations'][label]['failed_cases'], 'Fresh Codec sensitivity differs')
            mutations[label] = {'command':command,'failed_cases':failed,'original_source_sha256':digest(original),
                                'mutated_source_sha256':digest(changed),'old_expression':old,'new_expression':new}
            path.write_bytes(original)
            execute(['javac','--release','8','-g','-encoding','UTF-8','-cp',classes['fixed'],'-d',classes['fixed'],path],
                    output, label + '-restore-compile')
        require(originals['fixed'] == {owner:sha256(classes['fixed'] / (owner.replace('.','/')+'.class')) for owner in (METAPHONE,SOUNDEX)},
                'Sensitivity failed to restore Codec production bytecodes')
        java, _ = execute(['java','-version'], output, 'java-version')
    require(checkpoint()[0] == pins, 'Champ checkpoint changed during Codec review')
    verdict = copy.deepcopy(template)
    for candidate in verdict['candidates']:
        candidate['champ_verdict'] = 'accepted_for_bounded_candidate_oracle_development'
        candidate['agreed_preconditions'] = candidate['proposed_preconditions']
        candidate['agreed_oracle'] = candidate['proposed_oracle']
    verdict.update({'review_status':'beam_champ_bounded_codec_joint_accepted_shared_integration_pending',
                    'joint_acceptance_complete':True,'beam_commit':BEAM,'prospective_shared_implementation_and_testing_can_proceed':True})
    write_json(output / 'joint-verdict.json', verdict)
    write_json(output / 'native-rerun-comparisons.json', {'stages':native_comparisons,'mutations':mutations,
               'original_production_class_sha256':originals,'java_version':java.stderr.decode().strip(),
               'entry_kind':'Exact production JDI method entry, not line/branch coverage'})
    result = {'schema_version':1,'status':'five_codec_bounded_candidates_joint_accepted_shared_integration_pending',
              'checked_at_utc':datetime.now(timezone.utc).isoformat(),'beam_commit':BEAM,'base_aom_commit':AOM,'base_condition':CONDITION,
              'policy_id':policy['policy_id'],'received_manifest_entries_verified':88,'native_manifest_entries_verified':68,
              'original_received_provenance_entries_verified':7,'historical_beam_runtime_pins_verified':41,
              'received_native_commands_verified':21,'candidate_signatures':5,'unique_bounded_cases':43,
              'independent_oracle_cases_rechecked':43,'fixed_observations_rerun':86,'buggy_observations_rerun':86,
              'traced_observations_rerun':86,'exact_declaring_methods_entered_per_revision':5,
              'exact_first_target_entries_per_revision':43,'fresh_received_guard_tests':7,'fresh_received_guard_tests_skipped':0,
              'production_source_files_per_revision':25,'new_oracle_sensitivity_failures':{'next_char':3,'difference_score':9},
              'candidate_fault_detected':False,'shared_integration_approved':False,'joint_acceptance_complete':True,
              'full_legal_domain_approval':False,'line_or_branch_coverage_percentage':None,'full_defects4j_evaluation':False,
              'actual_v10_selected':390,'actual_v10_exclusions':301,'denominator':691,'existing_codec_targets_to_preserve':selected,
              'possible_codec_only_next_condition':{'selected':395,'exclusions':296,'implemented':False},
              'new_conditions_require_new_inputs_runtime_protocol_prompts_and_reserve':True,
              'final_reserve':None,'gate_a_approved':False,'kku_requests':0,'queue_mutations':0,'primary_results_added':0,
              'champ_checkpoint_pins_unchanged':len(pins)}
    write_json(output / 'receipt.json', result)
    write_json(output / 'source-bindings.json', {obj.commit:obj.bindings for obj in (beam,aom)})
    return result


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--defects4j', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    try:
        result = run(args.defects4j, args.output)
    except Exception as error:
        output = args.output.resolve()
        if output.is_relative_to(ROOT / 'output') and output.is_dir() and not (output / 'checksums.json').exists():
            write_json(output / 'failure.json', {'status':'fail','reason':str(error),'primary':False})
        raise
    finally:
        output = args.output.resolve()
        if output.is_relative_to(ROOT / 'output') and output.is_dir() and not (output / 'checksums.json').exists():
            write_json(output / 'checksums.json', {p.relative_to(output).as_posix():sha256(p)
                                                  for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({k:result[k] for k in ('status','candidate_signatures','unique_bounded_cases',
                                         'fresh_received_guard_tests','candidate_fault_detected')}))
