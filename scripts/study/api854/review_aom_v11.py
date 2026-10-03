"""Beam's prospective, offline, pinned v11 consumer and native host review.

Run this file directly in a fresh Python process, so received modules cannot be
confused with the current Beam runtime. No provider, live queue or ledger calls.
"""
import argparse
from datetime import datetime, timezone
import gzip
import hashlib
import io
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import tarfile

AOM = '6c0f6328788f56e3ac2dc52f0e3520b820892625'
PREFIX = 'output/api854-20261003/'
PREP = PREFIX + 'prepare-v11-chronology-development-v3'
CONFIG = PREFIX + 'aom-continuation-v11-development-v3'
READY = PREFIX + 'aom-v11-readiness-v1'
PATHS = ['scripts', 'algorithms', 'experiments/configs', PREP, CONFIG, READY,
         PREFIX + 'prepare-v7-twenty-bug-development',
         PREFIX + 'prepare-v10-joint-development',
         PREFIX + 'prepare-v3', PREFIX + 'aom-chronology-v11-intake-v1',
         PREFIX + 'aom-chronology-v11-integration-v3',
         'docs/api854/evidence/beam-champ-math-field-20261003-attempt2/supplemental-fixed-source',
         'docs/api854/AOM_CHRONOLOGY_V11_HANDOFF_TH.md']


def sha(path):
    h = hashlib.sha256()
    with Path(path).open('rb') as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b''):
            h.update(block)
    return h.hexdigest()


def read(path):
    return json.loads(Path(path).read_bytes())


def write(path, value):
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open('x', encoding='utf-8', newline='\n') as stream:
        json.dump(value, stream, ensure_ascii=False, indent=2)
        stream.write('\n')


def require(value, message):
    if not value:
        raise ValueError(message)


def git(repo, *args):
    return subprocess.check_output(['git', '-c', 'safe.directory=' + str(repo),
                                   '-c', 'core.autocrlf=false', '-C', str(repo), *args], timeout=180)


def intake(repo, snapshot, output):
    require(not snapshot.exists(), 'Choose a new snapshot destination')
    require(git(repo, 'rev-parse', AOM).decode().strip() == AOM, 'Aom pin not available')
    archive = git(repo, 'archive', '--format=tar', AOM, *PATHS)
    snapshot.mkdir(parents=True)
    inventory = {}
    with tarfile.open(fileobj=io.BytesIO(archive)) as tar:
        for entry in tar.getmembers():
            if entry.isdir():
                continue
            require(entry.isfile(), 'Snapshot archive must contain only regular files')
            path = (snapshot / entry.name).resolve()
            require(path.is_relative_to(snapshot), 'Archive path escapes snapshot')
            raw = tar.extractfile(entry).read()
            path.parent.mkdir(parents=True, exist_ok=True)
            with path.open('xb') as stream:
                stream.write(raw)
            inventory[entry.name] = hashlib.sha256(raw).hexdigest()
    # The received implementation uses immutable Git object lookups. This pointer
    # grants read access to those local objects, without copying credentials.
    gitdir = Path(git(repo, 'rev-parse', '--absolute-git-dir').decode().strip())
    (snapshot / '.git').write_text('gitdir: ' + str(gitdir) + '\n', encoding='utf-8')
    write(output / 'snapshot-provenance.json', {
        'source_commit': AOM, 'archive_paths': PATHS,
        'archive_sha256': hashlib.sha256(archive).hexdigest(), 'archive_bytes': len(archive),
        'snapshot_files': len(inventory), 'snapshot_directory': str(snapshot),
        'git_pointer_purpose': 'Read immutable peer Git objects only; not a worker checkout',
        'source_sha256': inventory})
    print(json.dumps({'phase': 'intake', 'source_commit': AOM, 'files': len(inventory)}), flush=True)


def consumer_tests(snapshot, output, env):
    command = [sys.executable, '-B', '-m', 'unittest', '-v',
               'scripts.study.api854.tests.test_chronology_v11',
               'scripts.study.api854.tests.test_preparation_v11_development']
    result = subprocess.run(command, cwd=snapshot, env=env, capture_output=True, timeout=180)
    for name, raw in [('stdout', result.stdout), ('stderr', result.stderr)]:
        (output / ('consumer-tests.' + name + '.log')).write_bytes(raw)
    write(output / 'consumer-tests.command.json', {
        'argv': command, 'cwd': str(snapshot), 'exit_code': result.returncode,
        'stdout_sha256': hashlib.sha256(result.stdout).hexdigest(),
        'stderr_sha256': hashlib.sha256(result.stderr).hexdigest(),
        'test_sources': {name: sha(snapshot / ('scripts/study/api854/tests/' + name))
                         for name in ['test_chronology_v11.py', 'test_preparation_v11_development.py']}})
    log = result.stderr.decode(errors='replace')
    require(result.returncode == 0 and re.search(r'Ran 10 tests in', log)
            and log.strip().endswith('OK') and 'skipped=' not in log,
            'Received consumer/Chronology tests failed; inspect retained logs')
    print(json.dumps({'phase': 'consumers', 'tests_passed': 10,
                      'four_approach_bug_combinations': 80, 'provider_requests': 0}), flush=True)


def native_host(snapshot, output, d4j, worktrees):
    # Delayed imports are intentional; the direct-file entry point must be used.
    sys.path[:0] = [str(snapshot), str(snapshot / 'scripts/study')]
    from scripts.study.api854 import verify_chronology_v11 as chronology
    from scripts.study.api854 import verify_v10_recipe_runtime as retained
    from scripts.study.api854 import verify_v11_suite_packaging as packaging
    from scripts.study.api854.fixture_policy import POLICY_V11
    from scripts.study.api854.common import cpu_slot, implementation_hashes
    from scripts.study.api854.environment import inspect_environment
    from scripts.study.api854.chronology_v11 import load_contract
    from scripts.study.api854.build_prepare_v11_development import checked_integration

    host = output / 'host-review-v1'
    host.mkdir(exist_ok=False)
    completion = read(snapshot / (READY + '/completion-receipt.json'))
    hashes = implementation_hashes()
    require(hashes == completion['runtime_source_sha256'], 'Received runtime differs from final condition')
    contract = load_contract()
    require(contract == checked_integration(), 'Received raw Chronology integration is inconsistent')
    write(host / 'preexecution-seal.json', {
        'sealed_at_utc': datetime.now(timezone.utc).isoformat(), 'source_commit': AOM,
        'condition': completion['condition'], 'runtime_source_sha256': hashes,
        'protocol_sha256': completion['protocol_sha256'],
        'preparation_index_sha256': completion['preparation_index_sha256'],
        'runner_sha256': completion['runner_plan_sha256'],
        'wrapper_sha256': sha(__file__), 'worker_id': 'beam-pc1', 'cpu_slots': 1,
        'worktrees_root': str(worktrees), 'chronology_cases': 13, 'retained_fixed_cases': 64,
        'oracles': contract['independent_expected_observations'],
        'verifiers': {name: sha(snapshot / ('scripts/study/api854/' + name)) for name in
                      ['verify_chronology_v11.py', 'verify_v10_recipe_runtime.py',
                       'verify_v11_suite_packaging.py']},
        'scope': 'Bounded shared component/packaging/host proof; not all 396 semantics or primary results'})

    def retained_run(command, **kwargs):
        index = len(retained.COMMANDS)
        result = subprocess.run(command, capture_output=True, timeout=120, **kwargs)
        prefix = host / 'retained-commands' / f'{index:03d}'
        prefix.parent.mkdir(exist_ok=True)
        raw = result.stdout
        suffix = '.stdout.tar.gz' if command[0] == 'git' and 'archive' in command else '.stdout.log'
        path = prefix.with_suffix(suffix)
        path.write_bytes(gzip.compress(raw, mtime=0) if suffix.endswith('.gz') else raw)
        error = prefix.with_suffix('.stderr.log')
        error.write_bytes(result.stderr)
        record = {'argv': list(map(str, command)), 'exit_code': result.returncode,
                  'stdout_sha256': hashlib.sha256(raw).hexdigest(),
                  'stderr_sha256': hashlib.sha256(result.stderr).hexdigest()}
        retained.COMMANDS.append(record)
        write(prefix.with_suffix('.command.json'), {
            **record, 'retained_stdout': path.relative_to(host).as_posix(),
            'retained_stdout_sha256': sha(path), 'retained_stderr': error.relative_to(host).as_posix()})
        require(result.returncode == 0, 'Retained component command failed: ' + str(command[0]))
        return raw

    retained.run = retained_run
    retained.V6 = POLICY_V11  # Same adapter as Aom's verify_v11_preserved_runtime.
    child = ('from scripts.study.api854.common import cpu_slot\nimport sys\n'
             'try:\n with cpu_slot(sys.argv[1]):pass\nexcept RuntimeError:sys.exit(9)\n')
    with cpu_slot(worktrees):
        environment = inspect_environment(d4j / 'framework/bin/defects4j', host / 'environment')
        require(environment['ready'], 'Beam environment prerequisites failed')
        challenge = subprocess.run([sys.executable, '-B', '-c', child, str(worktrees)],
                                   cwd=snapshot, capture_output=True)
        require(challenge.returncode == 9, 'Held CPU slot did not reject second process')
        print(json.dumps({'phase': 'native_chronology', 'cases': 13}), flush=True)
        chrono = chronology.verify(d4j, host / 'chronology')
        require(chrono['status'] == 'pass' and chrono['buggy_failed_cases'] == ['arrays_bad_order'],
                'Chronology shared oracle or buggy negative control differs')
        print(json.dumps({'phase': 'retained_components', 'cases': 64}), flush=True)
        fixed = retained.verify(d4j)
        write(host / 'retained-fixed-proof.json', fixed)
        require(fixed['status'] == 'pass' and fixed['fixed_source_integration_cases'] == 64,
                'Retained shared components failed')
        print(json.dumps({'phase': 'junit_packaging', 'cases': 13}), flush=True)
        packed = packaging.verify(d4j, host / 'packaging')
        require(packed['status'] == 'pass'
                and packed['stages']['controlled_fixture_failure']['evaluator_rejected_as_fault'],
                'JUnit packaging or fixture/fault separation failed')
        require(implementation_hashes() == hashes, 'Received runtime changed during native review')
    reuse = subprocess.run([sys.executable, '-B', '-c', child, str(worktrees)],
                           cwd=snapshot, capture_output=True)
    require(reuse.returncode == 0, 'Released CPU slot is not reusable')
    write(host / 'host-receipt.json', {
        'status': 'pass', 'worker_id': 'beam-pc1', 'cpu_slots': 1,
        'condition': completion['condition'], 'runtime_source_sha256': hashes,
        'protocol_sha256': completion['protocol_sha256'],
        'runner_sha256': completion['runner_plan_sha256'],
        'preparation_index_sha256': completion['preparation_index_sha256'],
        'worktrees_root': str(worktrees), 'environment_ready': True,
        'environment_sha256': sha(host / 'environment/environment.json'),
        'cpu_lock_exits': [challenge.returncode, reuse.returncode],
        'chronology_cases': 13, 'chronology_exact_declarations': 6,
        'chronology_fixed_observations': 26, 'chronology_buggy_observations': 26,
        'buggy_negative_control': 'arrays_bad_order',
        'chronology_receipt_sha256': sha(host / 'chronology/receipt.json'),
        'retained_fixed_cases': 64, 'retained_fixed_observations': 128,
        'retained_proof_sha256': sha(host / 'retained-fixed-proof.json'),
        'packaged_cases': 13, 'packaging_receipt_sha256': sha(host / 'packaging/receipt.json'),
        'fixture_failure_rejected_as_fault': True, 'line_branch_percentage': None,
        'all_396_semantic_approval': False, 'gate_a_approved': False,
        'kku_requests': 0, 'queue_mutations': 0, 'primary_results_added': 0,
        'new_full_defects4j_evaluations': 0})
    write(host / 'checksums.json', {p.relative_to(host).as_posix(): sha(p)
                                   for p in sorted(host.rglob('*')) if p.is_file()})


def finish(snapshot, output):
    provenance = read(output / 'snapshot-provenance.json')
    for path, expected in provenance['source_sha256'].items():
        require(sha(snapshot / path) == expected, 'Received snapshot bytes changed: ' + path)
    completion = read(snapshot / (READY + '/completion-receipt.json'))
    index = read(snapshot / (PREP + '/index.json'))
    require((len(index['records']), index['target_count'], index['capability_exclusion_count']) == (20, 396, 295),
            'Unexpected actual v11 partition')
    bindings = {
        'protocol_sha256': CONFIG + '/protocol.proposal.json',
        'runner_plan_sha256': CONFIG + '/runner-plan.json',
        'preparation_index_sha256': PREP + '/index.json',
        'worksheet_sha256': READY + '/prompt-reserve-worksheet.json'}
    for key, path in bindings.items():
        if key in completion:
            require(sha(snapshot / path) == completion[key], 'Final condition binding differs')
    # Copy public inputs/runtime as exact received evidence, keeping ignored
    # archives and old preparation snapshots outside the public review packet.
    public = set(completion['runtime_source_sha256'])
    public.update(p for p in provenance['source_sha256'] if p.startswith((PREP + '/', CONFIG + '/', READY + '/')))
    for name in ['chronology_v11.py', 'joint_recipe_v10.py', 'verify_chronology_v11.py',
                 'verify_v10_recipe_runtime.py', 'verify_v11_preserved_runtime.py',
                 'verify_v11_suite_packaging.py', 'build_prepare_v11_development.py',
                 'tests/test_chronology_v11.py', 'tests/test_preparation_v11_development.py']:
        public.add('scripts/study/api854/' + name)
    for path in sorted(public):
        destination = output / 'received-aom' / path
        destination.parent.mkdir(parents=True, exist_ok=True)
        with destination.open('xb') as stream:
            stream.write((snapshot / path).read_bytes())
    host = read(output / 'host-review-v1/host-receipt.json')
    require(host['status'] == 'pass' and host['runtime_source_sha256'] == completion['runtime_source_sha256'],
            'Host uses a different runtime')
    write(output / 'receipt.json', {
        'status': 'accepted_scoped_v11_consumers_chronology_component_oracles_and_beam_host',
        'checked_at_utc': datetime.now(timezone.utc).isoformat(), 'source_commit': AOM,
        'condition': completion['condition'], 'bugs': 20, 'selected': 396, 'exclusions': 295, 'denominator': 691,
        'bindings': {key: {'commit': AOM, 'path': path, 'sha256': sha(snapshot / path)}
                     for key, path in bindings.items()},
        'runtime_source_sha256': completion['runtime_source_sha256'],
        'fresh_tests_passed': 10, 'fresh_tests_skipped': 0, 'four_consumer_combinations': 80,
        'native_host_receipt_sha256': sha(output / 'host-review-v1/host-receipt.json'),
        'semantic_scope': 'Six bounded Chronology identities plus retained 64 setter/JDOM/Math/Buffer/Csv/Lang cases',
        'all_396_semantic_approval': False, 'all_691_requirement_complete': False,
        'empty_enum_targets': 4, 'codec_graphics_integrated': False,
        'gate_a_approved': False, 'primary_protocol_freeze_approved': False, 'final_reserve': None,
        'new_full_defects4j_evaluations': 0, 'kku_requests': 0, 'queue_mutations': 0, 'primary_results_added': 0,
        'producer_sha256': sha(__file__), 'public_received_files': len(public),
        'next_actions': ['Champ v11 actual prompt/settings/limits/framing/quota/reserve',
                         'All 691 fixture/oracle requirement and enum domain joint decision',
                         'Review any prospective Codec/Graphics shared condition separately']})
    write(output / 'checksums.json', {p.relative_to(output).as_posix(): sha(p)
                                     for p in sorted(output.rglob('*')) if p.is_file()})
    print(json.dumps({'status': 'pass', 'consumer_tests': 10, 'selected': 396,
                      'chronology_declarations': 6, 'gate_a_approved': False}), flush=True)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--snapshot', type=Path, required=True)
    parser.add_argument('--output', type=Path, required=True)
    parser.add_argument('--defects4j', type=Path, required=True)
    parser.add_argument('--worktrees', type=Path, required=True)
    args = parser.parse_args()
    repo = Path(__file__).resolve().parents[3]
    output, snapshot = args.output.resolve(), args.snapshot.resolve()
    output.mkdir(parents=True, exist_ok=False)
    try:
        intake(repo, snapshot, output)
        os.environ.update(GIT_CONFIG_COUNT='1', GIT_CONFIG_KEY_0='safe.directory', GIT_CONFIG_VALUE_0=str(snapshot))
        consumer_tests(snapshot, output, os.environ.copy())
        native_host(snapshot, output, args.defects4j.resolve(), args.worktrees.resolve())
        finish(snapshot, output)
    except Exception as error:
        write(output / 'failure.json', {'status': 'failed_attempt_retained', 'reason': str(error),
                                       'gate_a_approved': False, 'kku_requests': 0, 'queue_mutations': 0})
        if not (output / 'checksums.json').exists():
            write(output / 'checksums.json', {p.relative_to(output).as_posix(): sha(p)
                                             for p in sorted(output.rglob('*')) if p.is_file()})
        raise


if __name__ == '__main__':
    main()
