"""Produce sealed, offline null-boundary diagnostics without changing shared recipes.

Uses exact retained fixed source and local dependency bytes. JDI method-entry
events demonstrate actual entry and both configure delegations without modifying
production bytecode. This is prospective development evidence, not team approval.
"""
import argparse
from collections import Counter
from datetime import datetime, timezone
import csv
import hashlib
import io
import json
import os
from pathlib import Path
import platform
import subprocess
import tarfile
import tempfile
import time

from .common import ROOT, implementation_hashes, read_json, sha256

SOURCE = 'src/main/java/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java'
PREP = 'output/api854-20261003/prepare-v9-twenty-bug-development'
TOOLS = 'scripts/study/api854/development/enum_boundary'
CASES = ('configure_true', 'configure_false', 'enable', 'disable', 'isEnabled')
CLASS = 'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser'
FEATURE = 'Lcom/fasterxml/jackson/dataformat/xml/deser/FromXmlParser$Feature;'
DESCRIPTORS = {'configure':'('+FEATURE+'Z)L'+CLASS.replace('.', '/')+';',
    'enable':'('+FEATURE+')L'+CLASS.replace('.', '/')+';',
    'disable':'('+FEATURE+')L'+CLASS.replace('.', '/')+';',
    'isEnabled':'('+FEATURE+')Z'}


def require(condition, reason):
    if not condition:
        raise ValueError(reason)


def parse_log(raw):
    return [json.loads(line) for line in raw.decode('utf-8').splitlines() if line.strip()]


def validate_observations(records):
    cases = [r for r in records if 'case' in r and not r.get('method_entry')]
    require([r['case'] for r in cases] == list(CASES), 'Boundary case inventory/order differs')
    summaries = [r for r in records if r.get('summary')]
    require(len(summaries) == 1, 'Require one execution summary')
    expected = {'summary':True,'executed':5,'skipped':0,'fixture_errors':0,'target_checks':5,'enum_constants':0}
    require(summaries[0] == expected, 'Execution counters or production enum domain differ')
    before = {'format_features':0,'closed':False,'token':'VALUE_STRING','text':'45'}
    for case in cases:
        origin = ('enable' if case['case'].endswith('true') else 'disable') if case['case'].startswith('configure') else case['case']
        require(case['setup_succeeded'] is True and case['target_check_passed'] is True, 'Setup/target assertion failed')
        require(case['exception'] == 'java.lang.NullPointerException' and case['exception_origin_method'] == origin,
                'Exception class/origin differs')
        require(case['before'] == case['after'] == before, 'Parser state changed or fixture differs')
        require(case['continued_value'] == 'beta', 'Parser continuation failed')
    return cases


def validate_trace(records):
    entries = [r for r in records if r.get('method_entry')]
    expected = [('configure_true','configure'),('configure_true','enable'),
        ('configure_false','configure'),('configure_false','disable'),
        ('enable','enable'),('disable','disable'),('isEnabled','isEnabled')]
    require([(r['case'],r['method']) for r in entries] == expected, 'Actual target-entry/delegation trace differs')
    for entry in entries:
        require(entry['class'] == CLASS and entry['descriptor'] == DESCRIPTORS[entry['method']],
                'Coverage class or exact method descriptor differs')
        require(entry['source_line'] > 0, 'Entry has no source location')
    summary = [r for r in records if r.get('trace_summary')]
    require(len(summary) == 1 and summary[0]['method_entries'] == 7 and summary[0]['debuggee_exit_code'] == 0,
            'JDI/debuggee counters differ')
    return entries


def execute(command, packet, name, check=True):
    begin = time.monotonic()
    result = subprocess.run(command, capture_output=True, timeout=90)
    for suffix, raw in [('stdout.log',result.stdout),('stderr.log',result.stderr)]:
        with (packet/(name+'.'+suffix)).open('xb') as stream:
            stream.write(raw)
    record = {'command':list(map(str,command)), 'exit_code':result.returncode,
        'duration_seconds':time.monotonic()-begin,
        'stdout_sha256':sha256(packet/(name+'.stdout.log')),
        'stderr_sha256':sha256(packet/(name+'.stderr.log'))}
    with (packet/(name+'.command.json')).open('x',encoding='utf-8') as stream:
        json.dump(record,stream,indent=2)
        stream.write('\n')
    if check:
        require(result.returncode == 0, name+' failed: '+result.stderr.decode('utf-8',errors='replace')[-2500:])
    return result, record


def verify(defects4j, output):
    defects4j, output = Path(defects4j).resolve(), Path(output).resolve()
    require(output.is_relative_to(ROOT), 'Evidence output must stay inside this repository')
    output.mkdir(parents=True,exist_ok=False)
    before = implementation_hashes()
    index = read_json(ROOT/PREP/'index.json')
    require(index['runtime_source_sha256'] == before, 'Require exact current v9 runtime bindings')
    exclusions = read_json(ROOT/PREP/'JacksonXml-1/capability-exclusions.json')['excluded']
    enums = [r['target'] for r in exclusions if CLASS+'$Feature' in r['target']['parameter_types']]
    require(len(enums) == 4 and {t['method'] for t in enums} == set(DESCRIPTORS), 'Four enum targets must remain unsupported')
    pins = {PREP+'/index.json':sha256(ROOT/PREP/'index.json'),
        PREP+'/JacksonXml-1/capability-exclusions.json':sha256(ROOT/PREP/'JacksonXml-1/capability-exclusions.json')}
    # Seal the candidate policy and suite before executing any stage.
    for name in ('policy.json','EnumBoundaryProbe.java','EnumEntryTrace.java'):
        raw = (ROOT/TOOLS/name).read_bytes()
        with (output/name).open('xb') as stream:
            stream.write(raw)
    with (output/'verifier.py').open('xb') as stream:
        stream.write(Path(__file__).read_bytes())
    policy = read_json(output/'policy.json')
    suite_hashes = {n:sha256(output/n) for n in ('policy.json','EnumBoundaryProbe.java','EnumEntryTrace.java','verifier.py')}
    require(policy['boundary_oracle_approved'] is False and policy['enum_joint_decision_approved'] is False,
            'Diagnostic packet cannot imply semantic approval')
    sealed = {'sealed_at_utc':datetime.now(timezone.utc).isoformat(), 'source_sha256':suite_hashes,
              'policy_id':policy['policy_id'], 'runtime_source_sha256':before, 'input_sha256':pins,
              'primary':False,'team_approval':False}
    with (output/'preexecution-seal.json').open('x',encoding='utf-8') as stream:
        json.dump(sealed,stream,indent=2)
        stream.write('\n')
    with (defects4j/'framework/projects/JacksonXml/commit-db').open(encoding='utf-8') as stream:
        fixed = next(row[2] for row in csv.reader(stream) if row[0] == '1')
    repo = defects4j/'project_repos/jackson-dataformat-xml.git'
    raw = subprocess.run(['git','--git-dir='+str(repo),'archive',fixed,'src/main/java'],capture_output=True,check=True,timeout=90).stdout
    with (output/'fixed-production-source.tar').open('xb') as stream:
        stream.write(raw)
    dependency_names = ('jackson-annotations-2.7.0-rc3.jar','jackson-core-2.7.0-rc3.jar',
        'jackson-databind-2.7.0-rc3.jar','jackson-module-jaxb-annotations-2.7.0-rc3.jar',
        'stax2-api-3.1.4.jar','woodstox-core-5.0.1.jar','jaxb-api-2.3.0.jar')
    library = defects4j/'framework/projects/JacksonXml/lib'
    jars = []
    for name in dependency_names:
        matches = list(library.rglob(name))
        require(len(matches) == 1, 'Missing/ambiguous pinned dependency: '+name)
        jars.append(matches[0])
    generated = defects4j/'framework/projects/JacksonXml/generated_sources/2.7.0/PackageVersion.java'
    with (output/'PackageVersion.java').open('xb') as stream:
        stream.write(generated.read_bytes())
    stages = {}
    with tempfile.TemporaryDirectory(prefix='sqa-enum-development-') as temporary:
        working = Path(temporary).resolve()
        with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
            for member in archive.getmembers():
                if member.isfile() and member.name.endswith('.java'):
                    target = (working/member.name).resolve()
                    require(target.is_relative_to(working), 'Archive path escaped temporary checkout')
                    target.parent.mkdir(parents=True,exist_ok=True)
                    target.write_bytes(archive.extractfile(member).read())
        source = working/SOURCE
        retained = ROOT/PREP/'JacksonXml-1/fixed-source'/SOURCE
        require(source.read_bytes().replace(b'\r\n',b'\n') == retained.read_bytes().replace(b'\r\n',b'\n'),
                'Mirror fixed source content differs from retained preparation')
        require(sha256(retained) == policy['fixed_source_sha256'], 'Fixed target source pin differs')
        source.write_bytes(retained.read_bytes())
        version_source = working/'src/main/java/com/fasterxml/jackson/dataformat/xml/PackageVersion.java'
        version_source.write_bytes(generated.read_bytes())
        classes = working/'classes'
        classes.mkdir()
        cp = os.pathsep.join(map(str,[classes,*jars]))
        execute(['javac','--release','8','-g','-cp',os.pathsep.join(map(str,jars)),
                 '-sourcepath',str(working/'src/main/java'),'-d',str(classes),str(source),
                 str(output/'EnumBoundaryProbe.java')],output,'compile-production-and-suite')
        execute(['javac','--add-modules','jdk.jdi','-cp',str(classes),'-d',str(classes),
                 str(output/'EnumEntryTrace.java')],output,'compile-jdi-trace')
        class_file = classes/'com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.class'
        fixed_class_hash = sha256(class_file)
        for stage in ('fixed_first','fixed_second'):
            result, record = execute(['java','-cp',cp,'EnumBoundaryProbe'],output,stage)
            record['cases'] = validate_observations(parse_log(result.stdout))
            record.update(executed=5,skipped=0,fixture_errors=0,target_checks=5)
            stages[stage] = record
        require(stages['fixed_first']['cases'] == stages['fixed_second']['cases'], 'Fixed observations did not repeat')
        result, record = execute(['java','--add-modules','jdk.jdi','-cp',str(classes),'EnumEntryTrace',cp],output,'method_entry_trace')
        records = parse_log(result.stdout)
        record['cases'] = validate_observations(records)
        require(record['cases'] == stages['fixed_first']['cases'], 'Tracing changed observed behavior')
        record['entries'] = validate_trace(records)
        record.update(executed=5,skipped=0,fixture_errors=0,target_checks=5)
        stages['method_entry_trace'] = record
        require(sha256(class_file) == fixed_class_hash, 'JDI altered production bytecode')
        # A temporary mutation retains NPE but changes state before throwing.
        # The candidate state assertion must detect it, rather than accept NPE alone.
        text = source.read_text(encoding='utf-8')
        needle = '_formatFeatures |= f.getMask();'
        require(text.count(needle) == 1, 'Unexpected fixed enable implementation')
        source.write_text(text.replace(needle,'_formatFeatures |= 1;\n        '+needle),encoding='utf-8')
        execute(['javac','--release','8','-g','-cp',cp,'-d',str(classes),str(source)],output,'compile-temporary-state-mutation')
        mutant, mutation_record = execute(['java','-cp',cp,'EnumBoundaryProbe'],output,'temporary-state-mutation',check=False)
        require(mutant.returncode != 0 and b'Parser state changed: configure_true' in mutant.stderr,
                'Candidate oracle did not detect NPE-with-state-change mutation')
    require(before == implementation_hashes(), 'Shared runtime changed during diagnostic proof')
    require(all(sha256(ROOT/p) == h for p,h in pins.items()), 'Shared preparation changed during diagnostic proof')
    require(all(sha256(output/p) == h for p,h in suite_hashes.items()), 'Sealed candidate suite/policy changed')
    receipt = {'schema_version':1,'status':'pass','completed_at_utc':datetime.now(timezone.utc).isoformat(),
        'scope':'Offline prospective null-boundary development proof on exact retained fixed source; not Defects4J evaluation or primary results',
        'verifier_sha256':sha256(__file__), 'policy_id':policy['policy_id'], 'policy_and_suite_sha256':suite_hashes,
        'preexecution_seal_sha256':sha256(output/'preexecution-seal.json'),
        'fixed_mirror_revision':fixed,'fixed_source_sha256':sha256(retained),
        'fixed_production_class_sha256':fixed_class_hash,'generated_source_sha256':sha256(output/'PackageVersion.java'),
        'dependencies_sha256':{p.relative_to(defects4j).as_posix():sha256(p) for p in jars},
        'java_version':subprocess.run(['java','-version'],capture_output=True,check=True).stderr.decode('utf-8').strip(),
        'python':platform.python_version(), 'platform':platform.platform(),
        'stages':stages,'temporary_state_mutation':mutation_record,'temporary_state_mutation_detected':True,
        'method_entry_trace_counts':dict(Counter(e['method'] for e in stages['method_entry_trace']['entries'])),
        'configure_true_and_false_delegations_verified':True,'state_and_parser_continuation_verified':True,
        'line_or_branch_coverage_percentage':None,'production_bug_detection_evaluated':False,
        'normal_non_null_domain_coverage':False,'boundary_oracle_approved':False,'enum_joint_decision_approved':False,
        'shared_preparation_modified':False,'runtime_modified':False,'shared_input_sha256':pins,
        'denominator':691,'shared_selected':380,'shared_unsupported':311,'enum_targets_still_unsupported':4,
        'original_314_closed':False,'gate_a_passed':False,'primary_results_added':0,'live_requests':0,'queue_mutations':0}
    with (output/'receipt.json').open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(receipt,stream,indent=2)
        stream.write('\n')
    checksums = {p.name:sha256(p) for p in output.iterdir() if p.is_file()}
    with (output/'checksums.json').open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(checksums,stream,indent=2)
        stream.write('\n')
    return receipt


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--defects4j',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    try:
        result = verify(args.defects4j,args.output)
    except (ValueError,OSError,subprocess.SubprocessError) as error:
        if args.output.is_dir() and not (args.output/'failure.json').exists():
            with (args.output/'failure.json').open('x',encoding='utf-8') as stream:
                json.dump({'status':'fail','reason':str(error),'primary':False,'approval':False},stream,indent=2)
                stream.write('\n')
            checksums = {p.name:sha256(p) for p in args.output.iterdir() if p.is_file()}
            with (args.output/'checksums.json').open('x',encoding='utf-8') as stream:
                json.dump(checksums,stream,indent=2)
                stream.write('\n')
        raise
    print(json.dumps({k:result[k] for k in ('status','method_entry_trace_counts','temporary_state_mutation_detected','enum_joint_decision_approved')}))
