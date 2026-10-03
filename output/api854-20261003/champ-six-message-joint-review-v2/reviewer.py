"""Review six peer messages using immutable Git objects, without shared composition.

Rerun the received Aom inspectors and focused Beam tests on an isolated archive.
Independently recompute Buffer/Csv oracles; return scoped Champ verdicts only.
"""
import argparse
import base64
from collections import Counter
import copy
from datetime import datetime, timezone
import hashlib
import io
import json
import math
from pathlib import Path
import subprocess
import sys
import tarfile
import tempfile
import xml.etree.ElementTree as ET

from .common import ROOT, implementation_hashes, read_json, sha256, write_json

BEAM = '2e11c7d9'
AOM = '60cc1a6e'
V9 = 'output/api854-20261003/prepare-v9-twenty-bug-development'
BUFFER = 'output/api854-20261003/beam-buffer-joint-review-v1'
MATH = 'output/api854-20261003/beam-v8-received-v1'
LANG = 'docs/api854/evidence/beam-lang-reference-20261003-v2'
FIELDS = ('project','bug_id','class','constructor_types','method','parameter_types')
INSPECTORS = ('aom-beam-c125-intake-v1','aom-beam-lang-intake-v1','aom-beam-v8-received-intake-v1')


def require(ok, reason):
    if not ok:
        raise ValueError(reason)


def digest(raw):
    return hashlib.sha256(raw).hexdigest()


class GitObjects:
    def __init__(self, revision):
        self.commit = subprocess.check_output(['git','rev-parse',revision],cwd=ROOT,text=True).strip()
        self.cache, self.bindings = {}, {}

    def blob(self, path):
        require(not Path(path).is_absolute() and '..' not in Path(path).parts, 'Unsafe Git member')
        if path not in self.cache:
            self.cache[path] = subprocess.check_output(['git','show',self.commit+':'+path],cwd=ROOT)
            self.bindings[path] = digest(self.cache[path])
        return self.cache[path]

    def document(self, path):
        return json.loads(self.blob(path))

    def checksums(self, base):
        hashes = self.document(base+'/checksums.json')
        for path, value in hashes.items():
            require(digest(self.blob(base+'/'+path)) == value, 'Checksum differs: '+base+'/'+path)
        return len(hashes)


def scalar(kind, value):
    return kind+':'+base64.b64encode(str(value).encode()).decode()


def bucket(value, count):
    return max(0,min(count-1,math.floor((value+1)*count/2)))


def buffer_oracle(row):
    """Arithmetic/string/stream expectations, independent of recorded outcomes."""
    target, (a,b) = row['target'], row['vector'][:2]
    name = target['method']
    if target['class'].endswith('NumberInput'):
        texts = {'parseInt':['0','7','12345','999999999'],
                 'parseLong':['1000000000','1234567890123','123456789012345678'],
                 'parseBigDecimal':['0','12.50','-0.125'],
                 'inLongRange':['0','9223372036854775807','9223372036854775808','9223372036854775809']}[name]
        text = texts[bucket(a,len(texts))]
        kind = {'parseInt':'java.lang.Integer','parseLong':'java.lang.Long',
                'parseBigDecimal':'java.math.BigDecimal','inLongRange':'java.lang.Boolean'}[name]
        value = str(int(text)) if name in ('parseInt','parseLong') else text
        if name == 'inLongRange':
            value = str(int(text) <= (2**63 if b < 0 else 2**63-1)).lower()
        return 'value:'+scalar(kind,value)+'|state=stateless-scalars'
    if target['class'].endswith('TextBuffer'):
        initial, source, offset = ('123','xABCDy',1) if a < 0 else ('45.5','p12345q',2)
        count = 1+bucket(b,len(source)-offset-1)
        text = initial+source[offset:offset+count]
        return 'void|state=text:'+text+':size='+str(len(text))
    stream = 'A\nBC\nDE' if a < 0 else '12\n345\n'
    if target['parameter_types']:
        offset = 1 if a < 0 else 2
        count = min(len(stream),1+bucket(b,8-offset-1))
        read = stream[:count]
        chars = ['~']*8
        chars[offset:offset+count] = read
        state = ':buffer=[C['+''.join(scalar('java.lang.Character',c)+';' for c in chars)+']'
        return ('value:'+scalar('java.lang.Integer',count)+'|state=reader:line='
                +str(read.count('\n'))+':last='+str(ord(read[-1]))+state)
    value = {'getLineNumber':0,'lookAhead':ord(stream[0]),'read':ord(stream[0]),
             'readAgain':-2,'readLine':stream.split('\n')[0]}[name]
    line = 1 if name == 'readLine' else 0
    last = ord(value[-1]) if name == 'readLine' else ord(stream[0]) if name == 'read' else -2
    return ('value:'+scalar('java.lang.String' if name == 'readLine' else 'java.lang.Integer',value)
            +'|state=reader:line='+str(line)+':last='+str(last))


def validate_buffer_observations(rows, count):
    require(len(rows) == count and [r['case_id'] for r in rows] == list(range(count)), 'Reference inventory differs')
    for row in rows:
        first = row['fixed_first']
        require(first == row['fixed_second'] and first['status'] == 'ok'
                and first['target_invoked'] is True and row['independent_reference_passed'] is True,
                'Reference setup/invocation/repeat failed')
        require(first['outcome'] == row['expected'] == buffer_oracle(row), 'Independent Buffer/Csv oracle differs')


def check_stages(objects, base, count):
    records = {}
    for stage in ('fixed-1','fixed-2','buggy','coverage'):
        counters = objects.document(base+'/'+stage+'/sqa-stage-counts.json')
        require((counters['executed'],counters['skipped'],counters['target_checks']) == (count,0,count),
                'Execution counters differ')
        command = objects.document(base+'/'+stage+'/command.json')
        require(command['exit_code'] == 0 and command['timed_out'] is False, 'Received command failed')
        require(not objects.blob(base+'/'+stage+'/failing_tests').strip(), 'Received assertion failure')
        require(len(objects.blob(base+'/'+stage+'/all_tests').splitlines()) == count, 'Raw test enumeration differs')
        records[stage] = counters
    return records


def method_entry(objects, path, target, descriptor):
    xml = ET.fromstring(objects.blob(path))
    matches = [m for c in xml.findall('.//class') if c.attrib['name'] == target['class']
               for m in c.findall('./methods/method')
               if m.attrib['name'] == target['method'] and m.attrib['signature'] == descriptor]
    require(len(matches) == 1, 'Exact coverage descriptor missing')
    line = min(matches[0].findall('./lines/line'),key=lambda x:int(x.attrib['number']))
    return {'descriptor':descriptor,'entry_line':int(line.attrib['number']),'entry_hits':int(line.attrib['hits'])}


def extract_archive(revision, paths, destination):
    raw = subprocess.check_output(['git','archive',revision,*paths],cwd=ROOT)
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive.getmembers():
            if member.isfile():
                path = (destination/member.name).resolve()
                require(path.is_relative_to(destination), 'Snapshot archive escaped temporary root')
                path.parent.mkdir(parents=True,exist_ok=True)
                path.write_bytes(archive.extractfile(member).read())
    return digest(raw)


def execute(command, cwd, output, name):
    result = subprocess.run(list(map(str,command)),cwd=cwd,capture_output=True,timeout=180)
    for suffix, raw in [('stdout.log',result.stdout),('stderr.log',result.stderr)]:
        with (output/(name+'.'+suffix)).open('xb') as stream:
            stream.write(raw)
    record = {'command':list(map(str,command)),'exit_code':result.returncode,
              'stdout_sha256':sha256(output/(name+'.stdout.log')),'stderr_sha256':sha256(output/(name+'.stderr.log'))}
    write_json(output/(name+'.command.json'),record)
    require(result.returncode == 0, name+' failed: '+result.stderr.decode(errors='replace')[-1800:])
    return record


def run(output):
    output = Path(output).resolve()
    require(output.is_relative_to(ROOT) and not output.exists(), 'Require a new repository output path')
    output.mkdir(parents=True,exist_ok=False)
    before = implementation_hashes()
    beam, aom = GitObjects(BEAM), GitObjects(AOM)
    received = output/'received'
    received.mkdir()
    inspectors = []
    for name in INSPECTORS:
        relative = 'output/api854-20261003/'+name+'/inspect_received.py'
        raw = aom.blob(relative)
        path = received/name/'inspect_received.py'
        path.parent.mkdir()
        path.write_bytes(raw)
        inspectors.append(path)
    with (output/'reviewer.py').open('xb') as stream:
        stream.write(Path(__file__).read_bytes())
    # The received inspectors assume Git is invoked at repo root. Their local
    # historical-file checks need the snapshot cwd, so adapt only Git's cwd.
    runner = output/'received-inspector-runner.py'
    runner.write_text('''import runpy, subprocess, sys
script, repository = sys.argv[1:3]
original = subprocess.check_output
def at_repository(command, *args, **kwargs):
    if isinstance(command, list) and command[0] == "git":
        command = ["git", "-C", repository, *command[1:]]
    return original(command, *args, **kwargs)
subprocess.check_output = at_repository
sys.argv = [script, *sys.argv[3:]]
runpy.run_path(script, run_name="__main__")
''',encoding='utf-8',newline='\n')
    # Archive is temporary, not a branch/worktree or replacement for current v9.
    with tempfile.TemporaryDirectory(dir=ROOT/'output',prefix='.champ-peer-review-') as temporary:
        snapshot = Path(temporary).resolve()
        require(snapshot.is_relative_to(ROOT/'output'), 'Temporary snapshot escaped workspace')
        beam_archive = extract_archive(beam.commit,['scripts','algorithms','experiments/configs/api854-20261003',
            'docs/api854/evidence/beam-aom-review-20261003',MATH,
            'docs/api854/evidence/beam-v8-math-development-20261003-v1'],snapshot)
        aom_archive = extract_archive(aom.commit,['output/api854-20261003/prepare-v7-twenty-bug-development',
            'output/api854-20261003/prepare-v8-fraction-field-development',
            'output/api854-20261003/aom-continuation-v8-development',
            'output/api854-20261003/aom-v8-composition-audit-v1'],snapshot)
        audits = {}
        for name,path in zip(('buffer','lang','math-host'),inspectors):
            command = [sys.executable,str(runner),str(path),str(ROOT),'--output',str(output/(name+'-audit'))]
            if name == 'math-host':
                command += ['--snapshot',str(snapshot)]
            execute(command,snapshot,output,name+'-received-inspector')
            audits[name] = read_json(output/(name+'-audit')/'receipt.json')
        tests = execute([sys.executable,'-m','unittest','-v',
                         'scripts.study.api854.tests.test_lang_fixture_policy',
                         'scripts.study.api854.tests.test_buffer_fixture_policy',
                         'scripts.study.tests.test_java_probe'],snapshot,output,'beam-focused-rerun')
        raw = (output/'beam-focused-rerun.stderr.log').read_text(encoding='utf-8')
        require('Ran 14 tests' in raw and '\nOK\n' in raw and 'skipped=' not in raw, 'Focused rerun count/skips differ')
    reference_checks = beam.checksums(BUFFER+'/reference')
    joint_checks = beam.checksums(BUFFER)
    verdict = beam.document(BUFFER+'/beam-buffer-verdict.json')
    seal = beam.document(BUFFER+'/reference/preexecution-seal.json')
    require(seal['declared_before_observation'] is True and seal['use_buggy_outcomes_for_selection'] is False,
            'Reference must be declared before observations')
    require(seal['producer_sha256'] == digest(beam.blob(BUFFER+'/run_reference.py')), 'Reference producer differs')
    require(len(seal['runtime_source_sha256']) == 41, 'Reference runtime inventory differs')
    for path,value in seal['runtime_source_sha256'].items():
        require(digest(beam.blob(path)) == value == digest(beam.blob(BUFFER+'/reference/implementation/'+path)),
                'Reference runtime differs')
    refs, coverage_rows = {}, []
    for project,count in [('JacksonCore',28),('Csv',14)]:
        base = BUFFER+'/reference/'+project
        rows = beam.document(base+'/observations.json')
        validate_buffer_observations(rows,count)
        declared = beam.document(seal['cases'][project]['path'])
        require(digest(beam.blob(seal['cases'][project]['path'])) == seal['cases'][project]['sha256'], 'Case seal differs')
        require([{k:r[k] for k in declared[i]} for i,r in enumerate(rows)] == declared, 'Observed cases differ from seal')
        require(digest(beam.blob(seal['suites'][project]['path'])) == seal['suites'][project]['sha256'], 'Suite seal differs')
        check_stages(beam,base+'/evaluation',count)
        result = beam.document(base+'/evaluation/record.json')
        require(result['status'] == 'complete' and result['fixed_validation'] == 'passed_twice'
                and result['fault_detected'] is False, 'Reference measurement differs')
        for path,value in seal['fixed_source_sha256'][project].items():
            require(sha256(ROOT/V9/(project+'-1')/'fixed-source'/path) == value, 'Reference fixed source differs from v9')
        refs[project] = rows
    candidates = verdict['candidates']
    require(len(candidates) == 8 and len({tuple(c['target'][k] for k in FIELDS) for c in candidates}) == 8,
            'Buffer identity inventory differs')
    sampled_xml = aom.document('output/api854-20261003/aom-beam-c125-intake-v1/target-method-coverage.json')
    for candidate in candidates:
        target = candidate['target']
        require(candidate['beam_verdict'] == 'accepted_for_prospective_bounded_shared_recipe_composition'
                and candidate['champ_verdict'] is None, 'Preserve actual reviewer attribution')
        for item in candidate['evidence']:
            require(digest(beam.blob(item['path'])) == item['sha256'], 'Candidate evidence binding differs')
        matches = [r for r in refs[target['project']] if all(r['target'][k] == target[k] for k in FIELDS[2:])]
        require(len(matches) == 4 and candidate['fixed_repeated_observations'] == matches, 'Candidate reference differs')
        # Match exact parameter descriptor, including both append/BigDecimal overloads.
        old = next(r for r in sampled_xml['rows'] if r['class']==target['class'] and r['method']==target['method']
                   and r['evidence'] == candidate['sampled_approach_target_coverage'])
        exact = method_entry(beam,BUFFER+'/reference/'+target['project']+'/evaluation/coverage/coverage.xml',target,old['descriptor'])
        require(exact['entry_hits'] > 0, 'Reference target not covered')
        for item in candidate['sampled_approach_target_coverage']:
            observed = method_entry(beam,item['xml'],target,old['descriptor'])
            require(digest(beam.blob(item['xml'])) == item['xml_sha256'] and observed['entry_line']==item['entry_line']
                    and observed['entry_hits']==item['hits'], 'Historical coverage evidence differs')
        coverage_rows.append({'target':target,'reference':exact,'historical':candidate['sampled_approach_target_coverage']})
        candidate['champ_verdict'] = 'accepted_for_prospective_bounded_shared_recipe_composition'
        candidate['champ_review_evidence'] = {'path':BUFFER+'/beam-buffer-verdict.json',
                                            'sha256':digest(beam.blob(BUFFER+'/beam-buffer-verdict.json')),'commit':beam.commit}
    string_append = next(r for r in coverage_rows if r['target']['class'].endswith('TextBuffer')
                         and r['target']['parameter_types']=='java.lang.String,int,int')
    require({r['approach']:r['hits'] for r in string_append['historical']} == {'fscs-art':2,'cmaes':0},
            'Preserve zero historical CMA-ES String append coverage')
    csv = verdict['csv_stream_condition_change']
    require(csv['beam_verdict'] == 'accepted_as_explicit_prospective_condition_change' and csv['champ_verdict'] is None,
            'Csv reviewer attribution differs')
    csv['champ_verdict'] = 'accepted_as_explicit_prospective_condition_change'
    csv['agreed_condition'] = {**csv['beam_proposed_condition'],
                              'scope':'prospective component agreement; Aom must create a new combined policy/condition ID'}
    verdict.update(review_status='beam_and_champ_scoped_buffer_component_acceptance_complete',reviewer='champ',
        checked_at_utc=datetime.now(timezone.utc).isoformat(),champ_review_commit=subprocess.check_output(
            ['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip(),joint_acceptance_complete=True,
        current_shared_preparation_changed=False,team_or_primary_approval=False,gate_a_approved=False)
    write_json(output/'champ-buffer-verdict.json',verdict)
    lang_template = aom.document('output/api854-20261003/aom-beam-lang-intake-v1/joint-lang-acceptance.template.json')
    for row in lang_template['candidates']:
        row.update(champ_joint_verdict='accepted_for_prospective_bounded_development_composition',
                   beam_joint_verdict=None, accepted_into_shared_inputs=False,
                   agreed_preconditions=('Private static helper invoked by reflection; bounded null/empty/zero/nonzero String cases'
                    if row['target']['method']=='isAllZeros' else
                    'Private static helper invoked by reflection; null or int[] only, including empty/[0]/[-1,0,7]; no non-array Object'),
                   agreed_exception_oracle=('Exact Boolean, null=true, empty=false, nonempty all-zero=true; otherwise false'
                    if row['target']['method']=='isAllZeros' else
                    'null/empty: IllegalArgumentException with exact documented message and unchanged array state; success: void plus unchanged contents'),
                   evidence=[{'commit':audits['lang']['beam_commit'],'path':LANG+'/reference-observations.json',
                              'sha256':digest(beam.blob(LANG+'/reference-observations.json'))},
                             {'commit':audits['lang']['beam_commit'],'path':LANG+'/package/suite.tar.bz2',
                              'sha256':digest(beam.blob(LANG+'/package/suite.tar.bz2'))}])
    lang_template.update(example_only=False,review_status='champ_scoped_acceptance_complete_explicit_beam_joint_verdict_pending',
        checked_at_utc=datetime.now(timezone.utc).isoformat(),reviewer='champ',joint_acceptance_complete=False)
    write_json(output/'champ-lang-verdict.json',lang_template)
    # Setter/JDOM are already composed in shared v9. Confirm scope, never add them twice.
    joint = beam.document(MATH+'/joint-candidate-review.json')
    setter_jdom_checks = sum(beam.checksums('docs/api854/evidence/'+name)
                            for name in ('beam-v7-setter-recipe-20261003','beam-v7-jdom-oracle-20261003'))
    for candidate in joint['reviewed_candidates']:
        require(digest(beam.blob(candidate['fixture_recipe_path'])) == candidate['fixture_recipe_sha256'],
                'Setter/JDOM recipe source differs')
        for field in ('fixed_twice_suite_result_hashes','buggy_result_hash','target_coverage_result_hash'):
            items = candidate[field] if isinstance(candidate[field],list) else [candidate[field]]
            for item in items:
                require(digest(beam.blob(item['path'])) == item['sha256'], 'Setter/JDOM evidence differs')
        require(sha256(ROOT/V9/(candidate['project']+'-1')/'fixed-source'/
            ('src/java/'+candidate['class'].replace('.','/')+'.java')) == candidate['fixed_source_sha256'],
            'Setter/JDOM fixed source differs')
        candidate['champ_verdict'] = 'accepted_for_bounded_development_composition_preserve_current_v9'
        candidate['new_addition_relative_to_current_v9'] = False
    joint.update(review_status='beam_and_champ_scoped_setter_jdom_component_acceptance_complete',
        accepted_candidates=joint['reviewed_candidates'],joint_semantic_approval=False,
        champ_review_commit=verdict['champ_review_commit'])
    joint['reviewers']['champ'] = 'bounded_candidate_recipe_review_completed'
    write_json(output/'champ-setter-jdom-verdict.json',joint)
    accounting = {'status':'blocked_final_condition_and_provider_evidence_pending','final_prompt_reserve':None,
        'historical_conditions':[
            {'condition':'aom_math_only_v8','selected':379,'unsupported':312,'max_prompt_utf8_bytes':257515,'request_floor_before_framing':261611},
            {'condition':'beam_recomposed_math_only_v8','selected':379,'unsupported':312,'max_prompt_utf8_bytes':264899,'request_floor_before_framing':268995},
            {'condition':'champ_integrated_v9','selected':380,'unsupported':311,'max_prompt_utf8_bytes':258914,'request_floor_before_framing':263010}],
        'prospective_counts':{'v9_plus_buffer':{'selected':388,'unsupported':303},
                              'v9_plus_lang':{'selected':382,'unsupported':309},
                              'v9_plus_buffer_and_lang':{'selected':390,'unsupported':301}},
        'final_40_pair_worksheet_created':False,'final_preparation_received':False,
        'actual_model_ids':None,'effective_settings':None,'provider_prompt_tokens':None,
        'context_output_limits':None,'framing':None,'current_quota':None,'quota_reset_expiry':None,
        'gate_a_approved':False,'live_ai_test_authorized':False,
        'next_action':'Aom compose accepted scoped recipes preserving setter/JDOM/Math; Beam explicitly confirm Lang scope; then audit final 20 bugs x 2 models and current provider evidence.'}
    write_json(output/'reserve-readiness.json',accounting)
    require(before == implementation_hashes(), 'Shared runtime changed during intake')
    receipt = {'schema_version':1,'status':'champ_scoped_verdicts_recorded_no_shared_composition',
        'checked_at_utc':datetime.now(timezone.utc).isoformat(),'base_champ_commit':verdict['champ_review_commit'],
        'beam_commit':beam.commit,'aom_commit':aom.commit,'runtime_source_sha256':before,
        'received_inspectors_sha256':{p.relative_to(output).as_posix():sha256(p) for p in inspectors},
        'git_cwd_adapter_sha256':sha256(runner),
        'snapshot_archive_sha256':{'beam':beam_archive,'aom_historical_inputs':aom_archive},
        'received_audits':{k:{'path':k+'-audit/receipt.json','sha256':sha256(output/(k+'-audit')/'receipt.json'),
                            'checksum_entries_verified':v['checksum_entries_verified']} for k,v in audits.items()},
        'new_buffer_reference_checksum_entries':reference_checks,'buffer_joint_bundle_checksum_entries':joint_checks,
        'setter_jdom_checksum_entries':setter_jdom_checks,
        'independent_buffer_reference_cases':42,'fixed_reference_observations_received':84,
        'buffer_exact_declarations':8,'csv_existing_selected_methods_reviewed':5,
        'buffer_reference_target_coverage':coverage_rows,'focused_tests_rerun':14,'focused_test_command':tests,
        'lang_explicit_beam_joint_verdict_pending':True,'new_defects4j_runs':0,
        'shared_selected':380,'shared_unsupported':311,'denominator':691,'empty_enum_targets_still_unsupported':4,
        'runtime_modified':False,'shared_preparation_modified':False,'full_351_tests_rerun':False,
        'gate_a_approved':False,'final_prompt_reserve':None,'primary_results_added':0,
        'live_kku_requests':0,'live_queue_mutations':0,'quota_ledger_imports':0,
        'reviewer_source_sha256':sha256(__file__),
        'limits':['Bounded prospective acceptance only; received experiments were not rerun.',
                  'Host Math-only evidence is historical and does not certify final merged condition.',
                  'CMA-ES historical String append entry coverage remains zero; reference evidence stays separate.',
                  'Chronology candidate and four empty-enum targets remain unsupported pending their decisions.']}
    write_json(output/'receipt.json',receipt)
    write_json(output/'received-object-sha256.json',{o.commit:o.bindings for o in (beam,aom)})
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status':receipt['status'],'buffer_accepted':8,'champ_lang_accepted':2,
                      'focused_tests':14,'gate_a':False}))


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    if args.output.exists():
        parser.error('Evidence output already exists; choose a new packet path')
    try:
        run(args.output)
    except (ValueError,OSError,subprocess.SubprocessError) as error:
        output = args.output.resolve()
        if output.is_relative_to(ROOT) and output.is_dir() and not (output/'checksums.json').exists():
            write_json(output/'failure.json',{'status':'fail','reason':str(error),'primary':False,'approval':False})
            write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
        raise


if __name__ == '__main__':
    main()
