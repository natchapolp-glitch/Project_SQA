"""Seal and execute an isolated Time-1 Chronology candidate, without shared changes.

Compile both underlying Defects4J revisions from local mirror bytes. JDI proves
exact method entry; this is neither a full Defects4J run nor primary evidence.
"""
import argparse
import csv
from datetime import datetime, timezone
import gzip
import io
import json
import os
from pathlib import Path
import platform
import subprocess
import tarfile
import tempfile
import time

from .common import ROOT, implementation_hashes, read_json, sha256, write_json

PREP = 'output/api854-20261003/prepare-v9-twenty-bug-development'
CONTINUATION = 'output/api854-20261003/aom-champ-v9-continuation-receipt-v2.json'
WORKLIST = 'output/api854-20261003/aom-champ-v9-readiness-worklist-v1.json'
PROTOCOL = 'output/api854-20261003/aom-continuation-v9-integrated/protocol.proposal.json'
RUNNER = 'output/api854-20261003/aom-continuation-v9-integrated/runner-plan.json'
TOOLS = 'scripts/study/api854/development/chronology'
JVM = ['-Duser.timezone=UTC', '-Dorg.joda.time.DateTimeZone.Provider=org.joda.time.tz.UTCProvider']
SOURCE = 'src/main/java/org/joda/time/Partial.java'
CLASS = 'org.joda.time.Partial'
C = 'Lorg/joda/time/Chronology;'
T = 'Lorg/joda/time/DateTimeFieldType;'
TARGETS = {
    'empty': ('<init>', '('+C+')V'),
    'single': ('<init>', '('+T+'I'+C+')V'),
    'arrays': ('<init>', '(['+T+'[I'+C+')V'),
    'internal': ('<init>', '('+C+'['+T+'[I)V'),
    'field': ('getField', '(I'+C+')Lorg/joda/time/DateTimeField;'),
    'retain': ('withChronologyRetainFields', '('+C+')Lorg/joda/time/Partial;'),
}
CASES = {
    'empty_iso_offset':'empty', 'empty_null':'empty', 'single_hour_iso':'single',
    'single_invalid_hour':'single', 'arrays_leap_iso':'arrays',
    'arrays_invalid_date':'arrays', 'arrays_bad_order':'arrays', 'internal_iso':'internal',
    'getfield_buddhist':'field', 'getfield_bad_index':'field',
    'withchrono_buddhist':'retain', 'withchrono_same':'retain', 'withchrono_null':'retain',
}


def require(ok, reason):
    if not ok:
        raise ValueError(reason)


def parse_log(raw):
    return [json.loads(line) for line in raw.decode('utf-8').splitlines() if line.strip()]


def partial(types, values, buddhist=False):
    return {'chronology':'org.joda.time.chrono.'+('BuddhistChronology' if buddhist else 'ISOChronology'),
            'zone':'UTC', 'types':types, 'values':values}


def expected_observations():
    rejected = {'rejected':True, 'exception':'java.lang.IllegalArgumentException'}
    invalid_value = {'rejected':True, 'exception':'org.joda.time.IllegalFieldValueException'}
    date = partial(['year','monthOfYear','dayOfMonth'], [2024,2,29])
    return {
        'empty_iso_offset':partial([],[]), 'empty_null':partial([],[]),
        'single_hour_iso':partial(['hourOfDay'],[10]), 'single_invalid_hour':invalid_value,
        'arrays_leap_iso':date, 'arrays_invalid_date':invalid_value, 'arrays_bad_order':rejected,
        'internal_iso':date,
        'getfield_buddhist':{'field':'year', 'epoch_year':2513, 'receiver':partial(['year'],[2024])},
        'getfield_bad_index':{'rejected':True, 'exception':'java.lang.ArrayIndexOutOfBoundsException'},
        'withchrono_buddhist':partial(['hourOfDay'],[10], True),
        'withchrono_same':partial(['hourOfDay'],[10]), 'withchrono_null':partial(['hourOfDay'],[10]),
    }


def validate_observations(records, fixed=True):
    rows = [r for r in records if 'observation' in r]
    require([r['case'] for r in rows] == list(CASES), 'Case inventory/order differs')
    require(all(r['setup_succeeded'] is True for r in rows), 'Setup failure cannot be target evidence')
    passed = sum(r['target_check_passed'] is True for r in rows)
    require(all(type(r['target_check_passed']) is bool for r in rows), 'Invalid assertion counters')
    summaries = [r for r in records if r.get('summary')]
    require(summaries == [{'summary':True, 'executed':13, 'target_checks':13,
                          'passed':passed, 'failed':13-passed, 'skipped':0, 'fixture_errors':0}],
            'Execution counters differ; skipped/fixture errors cannot be accepted')
    for row in rows:
        if row['target_check_passed']:
            require(row['failure_class'] is None and row['failure_reason'] is None, 'Passed case has failure')
            require(row['observation'] == expected_observations()[row['case']],
                    'Independent value/state oracle differs: '+row['case'])
        else:
            require(row['failure_class'] == 'java.lang.AssertionError', 'Unexpected runtime/fixture failure')
    if fixed:
        require(passed == 13, 'Fixed target assertion failed')
    return rows


def validate_trace(records, exit_code=0):
    rows = [r for r in records if r.get('method_entry')]
    require(all(r['case'] in CASES and r['class'] == CLASS and r['source_line'] > 0 for r in rows),
            'Coverage class/case/source location differs')
    exact = []
    for case, group in CASES.items():
        entries = [r for r in rows if r['case'] == case]
        require(bool(entries) and (entries[0]['method'], entries[0]['descriptor']) == TARGETS[group],
                'Exact target descriptor was not entered first: '+case)
        exact.append(entries[0])
    summary = [r for r in records if r.get('trace_summary')]
    require(summary == [{'trace_summary':True, 'method_entries':len(rows), 'debuggee_exit_code':exit_code}],
            'JDI/debuggee counters differ')
    require(len({(r['method'],r['descriptor']) for r in exact}) == 6, 'Six exact declarations required')
    return exact


def execute(command, packet, name, check=True):
    begin = time.monotonic()
    result = subprocess.run(list(map(str, command)), capture_output=True, timeout=120)
    for suffix, raw in [('stdout.log',result.stdout), ('stderr.log',result.stderr)]:
        with (packet/(name+'.'+suffix)).open('xb') as stream:
            stream.write(raw)
    record = {'command':list(map(str,command)), 'exit_code':result.returncode,
              'duration_seconds':time.monotonic()-begin,
              'stdout_sha256':sha256(packet/(name+'.stdout.log')),
              'stderr_sha256':sha256(packet/(name+'.stderr.log'))}
    write_json(packet/(name+'.command.json'), record)
    if check:
        require(result.returncode == 0, name+' failed: '+result.stderr.decode('utf-8',errors='replace')[-2000:])
    return result, record


def checkpoint():
    from .audit_v9_shared_limits import inspect
    receipt = read_json(ROOT/CONTINUATION)
    pins = {CONTINUATION:sha256(ROOT/CONTINUATION), WORKLIST:sha256(ROOT/WORKLIST),
            PROTOCOL:receipt['current_protocol_sha256'],
            PREP+'/index.json':receipt['current_preparation_index_sha256'], RUNNER:sha256(ROOT/RUNNER)}
    for group in ('source_sha256','evidence_sha256','runtime_source_sha256'):
        pins.update(receipt[group])
    work = read_json(ROOT/WORKLIST)
    pins.update(work['input_sha256'])
    groups = [g for g in work['fixture_development_groups']
              if g['owner'] == 'champ' and g['reason'] == 'No explicit recipe: org.joda.time.Chronology']
    require(len(groups) == 1 and groups[0]['affected_targets'] == 6, 'Chronology worklist group differs')
    identities = [r['target'] for r in groups[0]['targets']]
    for row in work['unsupported_targets']:
        if row['target'] in identities and row['project'] == 'Time' and row['bug_id'] == 1:
            pins[row['raw_case']] = row['raw_case_sha256']
    require(all(sha256(ROOT/path) == value for path,value in pins.items()), 'Checkpoint/source/evidence pin differs')
    require(implementation_hashes() == receipt['runtime_source_sha256'], 'Current v9 runtime differs')
    audit = inspect()
    require(tuple(audit[k] for k in ('shared_prepared_bugs','target_count','capability_exclusion_count',
                                   'max_prompt_utf8_bytes','final_prompt_reserve','gate_a_passed'))
            == (20,380,311,258914,None,False), 'Shared v9 accounting/gate differs')
    return pins, identities


def extract(raw, working):
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive.getmembers():
            if member.isfile() and member.name.endswith('.java'):
                target = (working/member.name).resolve()
                require(target.is_relative_to(working), 'Source archive escaped temporary directory')
                target.parent.mkdir(parents=True,exist_ok=True)
                target.write_bytes(archive.extractfile(member).read())


def verify(defects4j, output):
    defects4j, output = Path(defects4j).resolve(), Path(output).resolve()
    require(output.is_relative_to(ROOT), 'Evidence output must stay inside repository')
    require(not output.exists(), 'Evidence output already exists; choose a new packet path')
    output.mkdir(parents=True,exist_ok=False)
    before = implementation_hashes()
    pins, identities = checkpoint()
    retained = ROOT/PREP/'Time-1/fixed-source'/SOURCE
    with (defects4j/'framework/projects/Time/commit-db').open(encoding='utf-8') as stream:
        row = next(row for row in csv.reader(stream) if row[0] == '1')
    revisions = {'fixed':row[2], 'buggy':row[1]}
    repository = defects4j/'project_repos/joda-time.git'
    jar = defects4j/'framework/projects/Time/lib/joda-convert-1.2.jar'
    require(jar.is_file(), 'Missing local pinned joda-convert dependency')
    dependency_hash = sha256(jar)
    # Candidate policy and complete suite are sealed before compilation/execution.
    suite = {}
    for name in ('policy.json','ChronologyProbe.java','ChronologyEntryTrace.java'):
        with (output/name).open('xb') as stream:
            stream.write((ROOT/TOOLS/name).read_bytes())
        suite[name] = sha256(output/name)
    with (output/'verifier.py').open('xb') as stream:
        stream.write(Path(__file__).read_bytes())
    suite['verifier.py'] = sha256(output/'verifier.py')
    policy = read_json(output/'policy.json')
    require(policy['oracle_approved'] is False and policy['shared_integration_approved'] is False,
            'Candidate cannot imply owner approval')
    require(policy['cases'] == CASES and policy['exact_targets'] == {k:list(v) for k,v in TARGETS.items()},
            'Policy and executable case/descriptor inventory differ')
    write_json(output/'preexecution-seal.json', {
        'sealed_at_utc':datetime.now(timezone.utc).isoformat(), 'policy_id':policy['policy_id'],
        'suite_sha256':suite, 'shared_input_sha256':pins, 'runtime_source_sha256':before,
        'mirror_revisions':revisions, 'fixed_target_source_sha256':sha256(retained),
        'dependency_sha256':{jar.relative_to(defects4j).as_posix():dependency_hash},
        'worklist_identities':identities, 'primary':False, 'owner_approval':False})
    stages, archives, sources, classes_hashes = {}, {}, {}, {}
    with tempfile.TemporaryDirectory(prefix='sqa-chronology-development-') as temporary:
        base = Path(temporary).resolve()
        classpaths = {}
        for version, revision in revisions.items():
            archive_command = ['git','--git-dir='+str(repository),'archive',revision,'src/main/java']
            archive = subprocess.run(archive_command,capture_output=True,check=True,timeout=90).stdout
            compressed = gzip.compress(archive,mtime=0)
            name = version+'-production-source.tar.gz'
            with (output/name).open('xb') as stream:
                stream.write(compressed)
            archives[version] = {'command':archive_command, 'revision':revision,
                                 'archive_sha256':sha256(output/name)}
            working = base/version
            working.mkdir()
            extract(archive,working)
            if version == 'fixed':
                require((working/SOURCE).read_bytes().replace(b'\r\n',b'\n') == retained.read_bytes().replace(b'\r\n',b'\n'),
                        'Mirror fixed source differs from retained v9 source')
                # Archive EOL conversion is checked above; compile exact retained target bytes.
                (working/SOURCE).write_bytes(retained.read_bytes())
            sources[version] = {p.relative_to(working).as_posix():sha256(p)
                                for p in sorted(working.rglob('*.java'))}
            classes = working/'classes'
            classes.mkdir()
            cp = os.pathsep.join(map(str,[classes,jar]))
            classpaths[version] = cp
            execute(['javac','--release','8','-g','-cp',str(jar),'-sourcepath',str(working/'src/main/java'),
                     '-d',str(classes),str(working/SOURCE),str(output/'ChronologyProbe.java')],output,version+'-compile')
            classes_hashes[version] = sha256(classes/'org/joda/time/Partial.class')
            if version == 'fixed':
                execute(['javac','--add-modules','jdk.jdi','-cp',str(classes),'-d',str(classes),
                         str(output/'ChronologyEntryTrace.java')],output,'compile-jdi-trace')
            for repeat in ('first','second'):
                stage = version+'_'+repeat
                result, record = execute(['java',*JVM,'-cp',cp,'org.joda.time.ChronologyProbe'],
                                         output,stage,check=(version == 'fixed'))
                record['cases'] = validate_observations(parse_log(result.stdout),fixed=(version == 'fixed'))
                require(result.returncode == (0 if all(r['target_check_passed'] for r in record['cases']) else 1),
                        'Process status and target assertion counters differ')
                stages[stage] = record
            require(stages[version+'_first']['cases'] == stages[version+'_second']['cases'],
                    'Observations did not repeat: '+version)
        # Trace each original revision using the same sealed suite and fixed tracer.
        for version in revisions:
            cp = classpaths[version]
            result, record = execute(['java','--add-modules','jdk.jdi','-cp',classpaths['fixed'],
                                     'ChronologyEntryTrace',cp],output,version+'_method_entry_trace',check=False)
            records = parse_log(result.stdout)
            record['cases'] = validate_observations(records,fixed=(version == 'fixed'))
            require(record['cases'] == stages[version+'_first']['cases'], 'Tracing changed observed behavior')
            require(result.returncode == stages[version+'_first']['exit_code'], 'Tracing changed exit status')
            record['exact_target_entries'] = validate_trace(records,exit_code=result.returncode)
            record['all_method_entry_count'] = len([r for r in records if r.get('method_entry')])
            stages[version+'_method_entry_trace'] = record
            require(sha256(base/version/'classes/org/joda/time/Partial.class') == classes_hashes[version],
                    'Tracing altered production bytecode')
        # Temporary mutation ignores supplied chronology but returns a valid field.
        # The exact field/value oracle must detect this; it is not a production bug.
        source = base/'fixed'/SOURCE
        text = source.read_text(encoding='utf-8')
        needle = 'return iTypes[index].getField(chrono);'
        require(text.count(needle) == 1, 'Unexpected getField source for sensitivity check')
        source.write_text(text.replace(needle,'return iTypes[index].getField(iChronology);'),encoding='utf-8')
        cp = classpaths['fixed']
        execute(['javac','--release','8','-g','-cp',cp,'-d',str(base/'fixed/classes'),str(source)],
                output,'compile-temporary-ignored-chronology')
        result, mutation = execute(['java',*JVM,'-cp',cp,'org.joda.time.ChronologyProbe'],
                                   output,'temporary-ignored-chronology',check=False)
        rows = validate_observations(parse_log(result.stdout),fixed=False)
        failed = [r['case'] for r in rows if not r['target_check_passed']]
        require(result.returncode == 1 and failed == ['getfield_buddhist'], 'Oracle missed ignored-chronology mutation')
        mutation['failed_cases'] = failed
    require(before == implementation_hashes(), 'Shared runtime changed during development')
    require(all(sha256(ROOT/p) == h for p,h in pins.items()), 'Shared inputs changed during development')
    require(all(sha256(output/p) == h for p,h in suite.items()), 'Sealed suite changed during development')
    require(sha256(jar) == dependency_hash, 'Dependency changed during development')
    fixed = stages['fixed_first']['cases']
    buggy = stages['buggy_first']['cases']
    differences = [a['case'] for a,b in zip(fixed,buggy) if a != b]
    failed = [r['case'] for r in buggy if not r['target_check_passed']]
    receipt = {
        'schema_version':1, 'status':'pass', 'completed_at_utc':datetime.now(timezone.utc).isoformat(),
        'scope':'Standalone prospective Chronology fixture/oracle proof, compiled underlying Time-1 revisions; not full Defects4J or primary results',
        'policy_id':policy['policy_id'], 'suite_sha256':suite,
        'preexecution_seal_sha256':sha256(output/'preexecution-seal.json'),
        'shared_input_sha256':pins, 'runtime_source_sha256':before,
        'source_archives':archives, 'compiled_source_sha256':sources,
        'fixed_target_source_sha256':sha256(retained), 'production_class_sha256':classes_hashes,
        'dependencies_sha256':{jar.relative_to(defects4j).as_posix():dependency_hash},
        'java_version':subprocess.run(['java','-version'],capture_output=True,check=True).stderr.decode('utf-8').strip(),
        'python':platform.python_version(), 'platform':platform.platform(), 'stages':stages,
        'repeated_fixed_equal':True, 'repeated_buggy_equal':True,
        'exact_declarations_entered_per_revision':6, 'cases_per_revision':13,
        'buggy_failed_cases':failed, 'fixed_buggy_different_cases':differences,
        'candidate_fault_detected':bool(failed), 'full_defects4j_evaluation':False,
        'coverage_kind':'JDI exact method-entry on unchanged production bytecode',
        'line_or_branch_coverage_percentage':None,
        'temporary_ignored_chronology_mutation':mutation, 'oracle_sensitivity_verified':True,
        'worklist_identities':identities, 'oracle_approved':False, 'shared_integration_approved':False,
        'runtime_modified':False, 'shared_preparation_modified':False,
        'selected':380, 'unsupported':311, 'denominator':691, 'enum_targets_still_unsupported':4,
        'original_314_closed':False, 'gate_a_passed':False, 'final_prompt_reserve':None,
        'primary_results_added':0, 'live_requests':0, 'queue_mutations':0, 'quota_ledger_imports':0}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.name:sha256(p) for p in sorted(output.iterdir()) if p.is_file()})
    return receipt


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--defects4j',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    # Never append failure files to a previously sealed packet.
    if args.output.exists():
        parser.error('Evidence output already exists; choose a new packet path')
    try:
        result = verify(args.defects4j,args.output)
    except (ValueError,OSError,subprocess.SubprocessError) as error:
        output = args.output.resolve()
        if output.is_relative_to(ROOT) and output.is_dir() and not (output/'checksums.json').exists():
            write_json(output/'failure.json',{'status':'fail','reason':str(error),'primary':False,'approval':False})
            write_json(output/'checksums.json',{p.name:sha256(p) for p in sorted(output.iterdir()) if p.is_file()})
        raise
    print(json.dumps({k:result[k] for k in ('status','cases_per_revision','exact_declarations_entered_per_revision',
                                           'buggy_failed_cases','candidate_fault_detected','gate_a_passed')}))


if __name__ == '__main__':
    main()
