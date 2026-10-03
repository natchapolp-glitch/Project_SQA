"""Prospective bounded Collections proof on one actual Beam Java11 CPU slot."""
from pathlib import Path
from datetime import datetime, timezone
import csv, gzip, hashlib, io, json, platform, subprocess, sys, tarfile, tempfile
BASE = Path(__file__).resolve().parent
ROOT = BASE.parents[2]
sys.path[:0] = [str(ROOT), str(ROOT / 'scripts/study')]
from scripts.study.api854.common import cpu_slot, implementation_hashes
POLICY = json.loads((BASE / 'policy.json').read_bytes())
CASES = POLICY['cases']
OWNER = 'org.apache.commons.collections.map.Flat3Map'
SOURCE = 'src/java/org/apache/commons/collections/map/Flat3Map.java'
OUT = BASE / 'native-v4'
JVM = ['-Duser.timezone=UTC', '-Duser.language=en', '-Duser.country=US']


def sha(path): return hashlib.sha256(Path(path).read_bytes()).hexdigest()
def read(path): return json.loads(Path(path).read_bytes())
def require(ok, message):
    if not ok: raise ValueError(message)
def write(path, value):
    with Path(path).open('x', encoding='utf-8', newline='\n') as stream:
        json.dump(value, stream, ensure_ascii=False, indent=2); stream.write('\n')
def records(raw): return [json.loads(line) for line in raw.decode().splitlines() if line.strip()]
def key(value): return (value is not None, value or '')
def pairs(value): return sorted(value, key=lambda pair: key(pair[0]))


def expected(case):
    before = POLICY['fixtures'][case['fixture']]
    before = None if before is None else pairs(before)
    method = case['method']
    absent = method in {'<init>', 'readObject'}
    storage = 'absent' if before is None else 'delegate' if len(before) > 3 else 'flat'
    value, after, distinct, independent, cleared = None, before, None, None, None
    exception = None
    if method == '<init>' and before is None:
        exception = 'java.lang.NullPointerException'
    elif method in {'<init>', 'readObject', 'writeObject'}:
        value, distinct, independent = before, True, True
    elif method == 'convertToMap':
        cleared = True
    elif method == 'createDelegateMap':
        value, distinct, independent = [], True, True
    elif method == 'hashCode':
        def string_hash(text):
            result = 0
            for char in text or '': result = (31 * result + ord(char)) & 0xffffffff
            return result
        total = sum(string_hash(k) ^ string_hash(v) for k, v in before) & 0xffffffff
        value = total if total < 2**31 else total - 2**32
    elif method == 'mapIterator':
        changed_key = {'one': 'k0', 'three': 'k2', 'four': 'k3'}.get(case['fixture'])
        value = before; after = [[k, 'changed' if k == changed_key else v] for k, v in before]
    elif method == 'entrySet':
        value, after = before, []
    elif method == 'keySet':
        value, after = sorted([k for k, v in before], key=key), []
    elif method == 'values':
        value, after = sorted([v for k, v in before], key=key), []
    else: raise ValueError('Unknown bounded target')
    return {'value': value, 'exception_class': exception, 'pre_map': before, 'post_map': after,
            'source_unchanged': after == before, 'distinct_result': distinct,
            'independent_result': independent, 'storage_before': 'absent' if absent else storage,
            'storage_after': 'delegate' if method == 'convertToMap' else storage,
            'flat_slots_cleared': cleared}


def validate(rows, cases=CASES, allow_failures=False):
    observations = [r for r in rows if 'case' in r and not r.get('method_entry')]
    require([r['case'] for r in observations] == [c['case'] for c in cases], 'Case inventory/order differs')
    for case, row in zip(cases, observations):
        require(row['setup_succeeded'] and row['fixture_error'] is None, 'Fixture failure cannot prove target')
        require((row['method'], row['descriptor'], row['declaring_class']) ==
                (case['method'], case['descriptor'], OWNER), 'Wrong exact target identity')
        passed = json.dumps(row['observation'], sort_keys=True) == json.dumps(expected(case), sort_keys=True)
        require(type(row['target_check_passed']) is bool and row['target_check_passed'] is passed,
                'Independent oracle/assertion differs ' + case['case'])
        require(row['failure_class'] == (None if passed else 'java.lang.AssertionError'), 'Wrong failure class')
        require(passed or allow_failures, 'Fixed independent oracle failed ' + case['case'])
    passed = sum(r['target_check_passed'] for r in observations)
    require([r for r in rows if r.get('summary')] == [{'summary': True, 'executed': len(cases),
            'target_checks': len(cases), 'passed': passed, 'failed': len(cases)-passed,
            'skipped': 0, 'fixture_errors': 0}], 'Counters/skips differ')
    return observations


def validate_trace(rows, exit_code=0):
    entries = [r for r in rows if r.get('method_entry')]; first = {}
    for row in entries:
        require(row['case'] in {c['case'] for c in CASES} and row['class'] == OWNER and row['source_line'] > 0,
                'Wrong production entry')
        first.setdefault(row['case'], row)
    require(set(first) == {c['case'] for c in CASES}, 'Missing exact entry')
    for case in CASES:
        row = first[case['case']]
        require((row['class'], row['method'], row['descriptor']) == (OWNER, case['method'], case['descriptor']),
                'Wrong first exact entry descriptor')
    require(len({(r['method'], r['descriptor']) for r in first.values()}) == 10, 'Ten declarations required')
    require([r for r in rows if r.get('trace_summary')] == [{'trace_summary': True,
            'method_entries': len(entries), 'debuggee_exit_code': exit_code}], 'Trace counters differ')
    return list(first.values())


def execute(argv, name, check=True, binary=False):
    result = subprocess.run(list(map(str, argv)), capture_output=True, timeout=180)
    stdout = OUT / (name + ('.stdout.tar.gz' if binary else '.stdout.log'))
    stdout.write_bytes(gzip.compress(result.stdout, mtime=0) if binary else result.stdout)
    stderr = OUT / (name + '.stderr.log'); stderr.write_bytes(result.stderr)
    record = {'argv': list(map(str, argv)), 'exit_code': result.returncode,
              'stdout_sha256': hashlib.sha256(result.stdout).hexdigest(), 'stderr_sha256': sha(stderr),
              'retained_stdout': stdout.name, 'retained_stdout_sha256': sha(stdout)}
    write(OUT / (name + '.command.json'), record)
    require(not check or result.returncode == 0, name + ' command failed: ' + result.stderr.decode(errors='replace')[-1400:])
    return result, record


def main():
    OUT.mkdir(exist_ok=False)
    before = implementation_hashes()
    require(len(CASES) == 40 and len(POLICY['targets']) == 10, 'Wrong prospective scope')
    for case in CASES: expected(case)
    def write_cases(path, cases):
        with path.open('x', encoding='utf-8', newline='\n') as stream:
            for c in cases:
                stream.write('\t'.join([c['case'], c['method'], c['fixture'], c['descriptor'],
                                       json.dumps(expected(c), separators=(',', ':'))]) + '\n')
    write_cases(OUT / 'cases.tsv', CASES)
    mutation_cases = {'hash': [c for c in CASES if c['method'] == 'hashCode'],
                      'conversion': [c for c in CASES if c['method'] == 'convertToMap']}
    for label, cases in mutation_cases.items(): write_cases(OUT / (label + '-cases.tsv'), cases)
    d4j = Path('/home/beam/sqa-beam/defects4j'); worktrees = '/home/beam/sqa-beam/worktrees'
    child = 'from scripts.study.api854.common import cpu_slot\nimport sys\ntry:\n with cpu_slot(sys.argv[1]):pass\nexcept RuntimeError:sys.exit(9)\n'
    with cpu_slot(worktrees):
        held, _ = execute([sys.executable, '-B', '-c', child, worktrees], 'held-cpu-lock', check=False)
        require(held.returncode == 9, 'CPU lock not held')
        java, _ = execute(['java', '-version'], 'java-version'); execute(['javac', '-version'], 'javac-version')
        require('11.' in java.stderr.decode(), 'Actual Beam Java11 required')
        metadata_path = d4j / 'framework/projects/Collections/active-bugs.csv'
        with metadata_path.open() as stream:
            commit = next(r for r in csv.DictReader(stream) if r['bug.id'] == '1')
        revisions = {'fixed': commit['revision.id.fixed'], 'buggy': commit['revision.id.buggy']}
        write(OUT / 'defects4j-revision-metadata.json', {'metadata_path': str(metadata_path),
              'metadata_sha256': sha(metadata_path), 'record': commit,
              'framework_sha': subprocess.check_output(['git', '-C', str(d4j), 'rev-parse', 'HEAD']).decode().strip(),
              'note': 'Public native production Git revisions. Retained fixed modified source is compared below; no cross-host synthetic checkout commit identity claimed.'})
        with tempfile.TemporaryDirectory(dir=ROOT / 'output', prefix='.beam-collections-') as temporary:
            temp = Path(temporary).resolve(); require(temp.is_relative_to((ROOT / 'output').resolve()), 'Unsafe build root')
            folders, inventories, classes, archives = {}, {}, {}, {}
            for version, revision in revisions.items():
                folder = temp / version; folder.mkdir(); folders[version] = folder
                archive, record = execute(['git', '-C', d4j / 'project_repos/commons-collections.git',
                                           'archive', revision, 'src/java'], version + '-production-archive', binary=True)
                archives[version] = record
                with tarfile.open(fileobj=io.BytesIO(archive.stdout)) as tar:
                    for entry in tar.getmembers():
                        if not entry.isfile(): continue
                        path = (folder / entry.name).resolve(); require(path.is_relative_to(folder), 'Unsafe archive path')
                        path.parent.mkdir(parents=True, exist_ok=True); path.write_bytes(tar.extractfile(entry).read())
                if version == 'fixed':
                    raw = (BASE / 'received-aom/output/api854-20261003/prepare-v11-chronology-development-v3/Collections-1/fixed-source' / SOURCE).read_bytes()
                    require((folder / SOURCE).read_bytes().replace(b'\r\n', b'\n') == raw.replace(b'\r\n', b'\n'), 'Fixed retained source differs')
                    (folder / SOURCE).write_bytes(raw)
                inventories[version] = {p.relative_to(folder).as_posix(): sha(p) for p in sorted((folder / 'src/java').rglob('*.java'))}
                classes[version] = folder / 'classes'; classes[version].mkdir()
            sealed_names = ['policy.json', 'CollectionsCandidateProbe.java', 'CollectionsEntryTrace.java', 'verify_native.py']
            suite = {name: sha(BASE / name) for name in sealed_names}
            write(OUT / 'preexecution-seal.json', {'sealed_at_utc': datetime.now(timezone.utc).isoformat(),
                  'policy_id': POLICY['policy_id'], 'suite_sha256': suite, 'runtime_source_sha256': before,
                  'input_provenance': read(BASE / 'received-provenance.json'), 'source_sha256': inventories,
                  'revisions': revisions, 'production_archives': archives, 'worker_id': 'beam-pc1',
                  'cpu_slots': 1, 'worktrees_root': worktrees, 'selection_used_buggy_outcomes': False,
                  'cases_tsv_sha256': {p.name: sha(p) for p in OUT.glob('*cases.tsv')},
                  'expected_observations': {c['case']: expected(c) for c in CASES}, 'primary': False})
            stages, class_hashes = {}, {}
            for version in revisions:
                cp = classes[version]; folder = folders[version]
                execute(['javac', '--release', '8', '-g', '-encoding', 'UTF-8', '-sourcepath', folder / 'src/java',
                         '-d', cp, folder / SOURCE, BASE / 'CollectionsCandidateProbe.java'], version + '-compile')
                if version == 'fixed': execute(['javac', '--add-modules', 'jdk.jdi', '-d', cp, BASE / 'CollectionsEntryTrace.java'], 'compile-trace')
                class_hashes[version] = sha(cp / (OWNER.replace('.', '/') + '.class'))
                for repeat in ('first', 'second'):
                    label = version + '_' + repeat
                    result, record = execute(['java', *JVM, '-cp', cp, 'sqa.development.CollectionsCandidateProbe', OUT / 'cases.tsv'], label, check=False)
                    record['cases'] = validate(records(result.stdout), allow_failures=version == 'buggy')
                    require(result.returncode == int(any(not r['target_check_passed'] for r in record['cases'])), 'Stage exit differs')
                    stages[label] = record
                require(stages[version + '_first']['cases'] == stages[version + '_second']['cases'], 'Repeat instability')
            for version in revisions:
                result, record = execute(['java', '--add-modules', 'jdk.jdi', '-cp', classes['fixed'],
                        'CollectionsEntryTrace', classes[version], OUT / 'cases.tsv'], version + '_method_entry', check=False)
                record['cases'] = validate(records(result.stdout), allow_failures=version == 'buggy')
                require(record['cases'] == stages[version + '_first']['cases'], 'Tracing changed observations')
                require(result.returncode == stages[version + '_first']['exit_code'], 'Trace exit differs')
                record['exact_entries'] = validate_trace(records(result.stdout), result.returncode)
                stages[version + '_method_entry'] = record
                require(sha(classes[version] / (OWNER.replace('.', '/') + '.class')) == class_hashes[version], 'Production bytecode changed')
            mutations = {}
            for label, old, new in [('hash', 'return total;', 'return total + 1;'),
                                     ('conversion', 'delegateMap.put(key1, value1);', '// controlled mutation: omit first mapping')]:
                source = folders['fixed'] / SOURCE; original = source.read_bytes(); text = original.decode()
                require(text.count(old) == 1, 'Mutation identity ambiguous')
                source.write_text(text.replace(old, new), encoding='utf-8')
                (OUT / (label + '-mutated.java')).write_bytes(source.read_bytes())
                execute(['javac', '--release', '8', '-g', '-encoding', 'UTF-8', '-cp', classes['fixed'], '-d', classes['fixed'], source], label + '-mutation-compile')
                result, record = execute(['java', *JVM, '-cp', classes['fixed'], 'sqa.development.CollectionsCandidateProbe', OUT / (label + '-cases.tsv')], label + '-mutation', check=False)
                checked = validate(records(result.stdout), cases=mutation_cases[label], allow_failures=True)
                record['failed_cases'] = [r['case'] for r in checked if not r['target_check_passed']]
                expected_failed = [c['case'] for c in mutation_cases[label]
                                   if (c['fixture'] != 'four' if label == 'hash' else c['fixture'] != 'empty')]
                require(result.returncode == 1 and record['failed_cases'] == expected_failed, 'Mutation missed or unexpected fixture contamination')
                mutations[label] = record
                source.write_bytes(original)
                execute(['javac', '--release', '8', '-g', '-encoding', 'UTF-8', '-cp', classes['fixed'], '-d', classes['fixed'], source], label + '-restore-compile')
                require(sha(classes['fixed'] / (OWNER.replace('.', '/') + '.class')) == class_hashes['fixed'], 'Restore differs')
            require(suite == {name: sha(BASE / name) for name in sealed_names}, 'Sealed suite changed')
            require(before == implementation_hashes(), 'Shared runtime changed')
    reuse, _ = execute([sys.executable, '-B', '-c', child, worktrees], 'released-cpu-lock')
    require(reuse.returncode == 0, 'CPU slot not reusable')
    failed = [r['case'] for r in stages['buggy_first']['cases'] if not r['target_check_passed']]
    result = {'status': 'pass', 'policy_id': POLICY['policy_id'], 'unique_cases': 40, 'declarations': 10,
              'fixed_observations': 80, 'buggy_observations': 80, 'stages': stages, 'mutations': mutations,
              'revisions': revisions, 'source_sha256': inventories, 'production_class_sha256': class_hashes,
              'suite_sha256': suite, 'preexecution_seal_sha256': sha(OUT / 'preexecution-seal.json'),
              'runtime_source_sha256': before, 'java_version': java.stderr.decode().strip(), 'platform': platform.platform(),
              'worker_id': 'beam-pc1', 'cpu_slots': 1, 'cpu_lock_exits': [held.returncode, reuse.returncode],
              'buggy_failed_cases': failed, 'candidate_fault_detected': bool(failed),
              'method_entry_only': True, 'line_branch_coverage_percentage': None,
              'revision_metadata_sha256': sha(OUT / 'defects4j-revision-metadata.json'),
              'actual_shared_selected': 396, 'actual_shared_exclusions': 295, 'denominator': 691,
              'shared_integration_approved': False, 'full_legal_domain_approved': False,
              'new_full_defects4j_evaluations': 0, 'kku_requests': 0, 'queue_mutations': 0, 'primary_added': 0,
              'gate_a_approved': False, 'final_reserve': None}
    write(OUT / 'receipt.json', result); return result


if __name__ == '__main__':
    try:
        result = main()
        print(json.dumps({k: result[k] for k in ['status', 'unique_cases', 'declarations', 'candidate_fault_detected']}))
    except BaseException as error:
        if OUT.exists() and not (OUT / 'failure.json').exists(): write(OUT / 'failure.json', {'status': 'failed_attempt_retained', 'reason': str(error)})
        raise
    finally:
        if OUT.exists() and not (OUT / 'checksums.json').exists():
            write(OUT / 'checksums.json', {p.relative_to(OUT).as_posix(): sha(p) for p in sorted(OUT.rglob('*')) if p.is_file()})
