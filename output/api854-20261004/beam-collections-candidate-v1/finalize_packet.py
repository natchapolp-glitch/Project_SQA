"""Seal reviewed standalone Collections evidence, including failed attempts."""
from pathlib import Path
import json, subprocess, sys
BASE = Path(__file__).resolve().parent
sys.path.insert(0, str(BASE))
import verify_native as v


def verify(folder):
    manifest = v.read(folder / 'checksums.json')
    files = {p.relative_to(folder).as_posix() for p in folder.rglob('*') if p.is_file()}
    v.require(files == set(manifest) | {'checksums.json'}, 'Missing checksum inventory')
    for name, digest in manifest.items(): v.require(v.sha(folder / name) == digest, 'Changed evidence ' + name)


def main():
    v.require(not (BASE / 'checksums.json').exists(), 'Immutable packet already finalized')
    for name in ['native-v1', 'native-v2', 'native-v3', 'native-v4', 'd4j-v1', 'd4j-v2', 'd4j-v3']:
        verify(BASE / name)
    for attempt in [2, 3]:
        seal = v.read(BASE / ('native-v' + str(attempt)) / 'preexecution-seal.json')
        for name, digest in seal['suite_sha256'].items():
            archived = name.replace('.py', '_attempt' + str(attempt) + '.py') if name == 'verify_native.py' else name.replace('.java', '_attempt' + str(attempt) + '.java') if name == 'CollectionsCandidateProbe.java' else name
            v.require(v.sha(BASE / archived) == digest, 'Original failed attempt producer not preserved ' + archived)
    for attempt in [1, 2]:
        seal = v.read(BASE / ('d4j-v' + str(attempt)) / 'preexecution-seal.json')
        v.require(v.sha(BASE / ('verify_evaluator_attempt' + str(attempt) + '.py')) == seal['producer_sha256'], 'Failed evaluator producer changed')
    command = [sys.executable, '-B', str(BASE / 'test_evidence.py')]
    result = subprocess.run(command, capture_output=True, timeout=180)
    (BASE / 'focused-tests.stdout.log').write_bytes(result.stdout)
    (BASE / 'focused-tests.stderr.log').write_bytes(result.stderr)
    v.write(BASE / 'focused-tests.command.json', {'argv': command, 'exit_code': result.returncode,
          'stdout_sha256': v.sha(BASE / 'focused-tests.stdout.log'), 'stderr_sha256': v.sha(BASE / 'focused-tests.stderr.log'),
          'test_source_sha256': v.sha(BASE / 'test_evidence.py')})
    v.require(result.returncode == 0 and b'Ran 10 tests' in result.stderr and result.stderr.strip().endswith(b'OK'), 'Focused guard tests failed')
    native = v.read(BASE / 'native-v4/receipt.json')
    evaluator = v.read(BASE / 'd4j-v3/measurement/record.json')
    v.require(native['status'] == 'pass' and evaluator['status'] == 'complete', 'Candidate proofs incomplete')
    framework = Path('/home/beam/sqa-beam/defects4j/framework/projects/Collections')
    meta = v.read(BASE / 'native-v4/defects4j-revision-metadata.json')
    v.require(v.sha(framework / 'active-bugs.csv') == meta['metadata_sha256'], 'D4J revision metadata changed')
    (BASE / 'defects4j-active-bugs.csv').write_bytes((framework / 'active-bugs.csv').read_bytes())
    (BASE / 'defects4j-1.src.patch').write_bytes((framework / 'patches/1.src.patch').read_bytes())
    receipt = {'status': 'beam_bounded_candidate_ready_for_joint_review', 'policy_id': v.POLICY['policy_id'],
        'candidate_targets': 10, 'unique_cases': 40, 'fresh_guard_tests_passed': 10, 'fresh_guard_tests_skipped': 0,
        'native_receipt_sha256': v.sha(BASE / 'native-v4/receipt.json'),
        'native_fixed_observations': 80, 'native_buggy_observations': 80,
        'exact_target_method_entry_declarations': 10, 'buggy_failed_cases': native['buggy_failed_cases'],
        'evaluator_receipt_sha256': v.sha(BASE / 'd4j-v3/receipt.json'),
        'measurement_record_sha256': v.sha(BASE / 'd4j-v3/measurement/record.json'),
        'suite_sha256': evaluator['suite_sha256'], 'junit_methods': 10, 'nested_cases': 40,
        'fixed_validation': 'passed_twice', 'manual_component_fault_detected': evaluator['fault_detected'],
        'triggering_tests': evaluator['triggering_tests'],
        'coverage': {k: evaluator[k] for k in ['line_covered', 'line_total', 'branch_covered', 'branch_total']},
        'coverage_engine': evaluator['coverage_engine'], 'coverage_scope': evaluator['coverage_scope'],
        'native_runtime_source_sha256': native['runtime_source_sha256'],
        'fixed_source_sha256': v.read(BASE / 'd4j-v3/receipt.json')['fixed_source_sha256'],
        'worker_id': 'beam-pc1', 'cpu_slots': 1, 'actual_shared_condition': v.POLICY['shared_condition'],
        'actual_shared_selected': 396, 'actual_shared_exclusions': 295, 'denominator': 691,
        'beam_semantic_verdict': 'accepted_only_for_predeclared_bounded_String_null_real_map_and_valid_native_stream_domain',
        'joint_review_complete': False, 'shared_integration_approved': False, 'all_691_complete': False,
        'generation_algorithm_result': False, 'primary_results_added': 0, 'kku_requests': 0, 'queue_mutations': 0,
        'new_completed_full_defects4j_development_evaluations': 1, 'gate_a_approved': False, 'final_reserve': None,
        'retained_failed_attempts': {'native-v1': 'Incorrect legacy commit-db lookup; no target executions',
          'native-v2': 'Probe observed post-independence-mutation storage instead of target-return storage',
          'native-v3': 'Expected mutation failure count incorrectly included delegated hash path',
          'd4j-v1': 'JUnit method hashCode name conflicted with Object method',
          'd4j-v2': 'Top-level helper was discovered as an empty test class; corrected by nesting'},
        'scope_limits': ['Not all legal Java Map/key/value implementations or malformed serialization streams',
                         'Native JDI component proof and Cobertura evaluator proof have distinct bound source/suite hashes',
                         'JUnit suite is explicitly authored bounded development, not a primary algorithm or model output'],
        'next_actions': ['Champ/Aom semantic fixture/oracle review of all ten exact identities and hashes',
                         'Prospective shared helper, four-approach inputs and condition integration by Aom, followed by regression/host review']}
    v.write(BASE / 'receipt.json', receipt)
    v.write(BASE / 'checksums.json', {p.relative_to(BASE).as_posix(): v.sha(p) for p in sorted(BASE.rglob('*')) if p.is_file()})
    print(json.dumps({'status': receipt['status'], 'targets': 10, 'cases': 40, 'guard_tests': 10, 'full_evaluator': 'complete'}))


if __name__ == '__main__': main()
