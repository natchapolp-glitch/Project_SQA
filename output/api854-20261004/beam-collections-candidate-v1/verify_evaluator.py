"""Evaluate one bounded manual Collections suite with real Defects4J, offline."""
from pathlib import Path
import json, shutil, sys
BASE = Path(__file__).resolve().parent; ROOT = BASE.parents[2]
sys.path[:0] = [str(ROOT), str(ROOT / 'scripts/study')]
import verify_native as native
from scripts.study.api854.common import cpu_slot, implementation_hashes
from scripts.study.api854.worker import prepare_evaluation
from scripts.study.api854.pack_suite import pack_suite
from scripts.study.evaluate import EvaluationConfig, evaluate_run


def java_literal(text): return json.dumps(text, ensure_ascii=True)


def main():
    output = BASE / 'd4j-v3'; output.mkdir(exist_ok=False)
    source = output / 'suite-source/sqa/development'; source.mkdir(parents=True)
    helper = (BASE / 'CollectionsCandidateProbe.java').read_text()
    # D4J's historical build accepts Java7 syntax. Keep the exact algorithms and
    # target calls, replacing only Java8 collection/lambda syntax in this copy.
    helper = helper.replace('entries.sort((a, b) -> compare(a.get(0), b.get(0)));',
        'Collections.sort(entries, new Comparator<List>() { public int compare(List a, List b) { return CollectionsCandidateProbe.compare(a.get(0), b.get(0)); } });')
    helper = helper.replace('items.sort((a, b) -> compare(((List)a).get(0), ((List)b).get(0)));',
        'Collections.sort(items, new Comparator() { public int compare(Object a, Object b) { return CollectionsCandidateProbe.compare(((List)a).get(0), ((List)b).get(0)); } });')
    helper = helper.replace('items.sort((a, b) -> compare(a, b));',
        'Collections.sort(items, new Comparator() { public int compare(Object a, Object b) { return CollectionsCandidateProbe.compare(a, b); } });')
    native.require(' -> ' not in helper, 'Unadapted lambda')
    imports = '\n'.join(line for line in helper.splitlines() if line.startswith('import '))
    nested = helper[helper.index('public final class CollectionsCandidateProbe {'):]
    nested = nested.replace('public final class CollectionsCandidateProbe {',
                            'public static final class CollectionsCandidateProbe {', 1)
    tests = []
    for target in native.POLICY['targets']:
        method = target['target']['method']
        cases = [c for c in native.CASES if c['method'] == method]
        rows = []
        for case in cases:
            row = [case['case'], case['method'], case['fixture'], case['descriptor'],
                   json.dumps(native.expected(case), separators=(',', ':'))]
            rows.append('new String[]{' + ','.join(java_literal(v) for v in row) + '}')
        tests.append('@org.junit.Test public void bounded_' + ('constructor' if method == '<init>' else method) +
                     '() throws Exception { group(new String[][]{' + ','.join(rows) + '}); }')
    wrapper = 'package sqa.development;\n' + imports + '\n' + '''
public class CollectionsCandidateTest {
    static int executed, checks;
    static void group(String[][] cases) throws Exception {
        executed++; String failures = "";
        for (String[] c : cases) {
            java.util.Map observation;
            try { observation = CollectionsCandidateProbe.observation(c); }
            catch (Throwable error) { throw new AssertionError("SQA_HARNESS Collections setup/projection failed: " + c[0], error); }
            if (!CollectionsCandidateProbe.json(observation).equals(c[4])) failures += c[0] + " ";
        }
        checks++; org.junit.Assert.assertEquals("Full independent bounded observations differ: " + failures, "", failures);
    }
    @org.junit.AfterClass public static void counts() throws Exception {
        String json = "{\\"schema_version\\":1,\\"executed\\":" + executed + ",\\"skipped\\":0,\\"target_checks\\":" + checks + "}";
        java.nio.file.Files.write(java.nio.file.Paths.get("sqa-stage-counts.json"), json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
''' + nested + '\n' + '\n'.join(tests) + '\n}\n'
    (source / 'CollectionsCandidateTest.java').write_text(wrapper, encoding='utf-8', newline='\n')
    manifest = pack_suite(output / 'suite-source', output / 'packaged', test_count=10, test_method_cap=30)
    runtime = implementation_hashes()
    native.write(output / 'preexecution-seal.json', {'suite_sha256': manifest['suite_sha256'],
        'source_sha256': manifest['source_sha256'], 'native_preexecution_seal_sha256': native.sha(BASE / 'native-v4/preexecution-seal.json'),
        'producer_sha256': native.sha(__file__), 'runtime_source_sha256': runtime,
        'policy_id': native.POLICY['policy_id'], 'worker_id': 'beam-pc1', 'cpu_slots': 1,
        'case_count': 40, 'grouped_junit_methods': 10, 'selection_used_buggy_outcomes': False,
        'scope': 'Manual bounded component/evaluator development, not any primary algorithm generation',
        'primary': False, 'kku_requests': 0, 'queue_mutations': 0})
    d4j = '/home/beam/sqa-beam/defects4j/framework/bin/defects4j'
    worktrees = Path('/home/beam/sqa-beam/worktrees')
    trees = worktrees / 'beam-collections-evaluator-v3'; trees.mkdir(exist_ok=False)
    with cpu_slot(worktrees):
        paths, classes, sources = prepare_evaluation(d4j, {'project': 'Collections', 'bug_id': 1}, trees, output / 'setup', 900)
        retained = BASE / 'received-aom/output/api854-20261003/prepare-v11-chronology-development-v3/Collections-1/fixed-source'
        for name, digest in sources.items():
            native.require(native.sha(retained / name) == digest, 'Actual D4J fixed source differs from received preparation')
        record = evaluate_run(EvaluationConfig(project='Collections', bug_id=1, generator='BeamBoundedDevelopment',
            seed=20261003, budget=30, suite=output / 'packaged/suite.tar.bz2', buggy_worktree=paths['b'], fixed_worktree=paths['f'],
            output=output / 'measurement', d4j=d4j, classes_file=classes, test_count=10, timeout_seconds=900))
        native.require(implementation_hashes() == runtime, 'Shared runtime changed')
        native.write(output / 'receipt.json', {'status': record['status'], 'suite_sha256': manifest['suite_sha256'],
            'record_sha256': native.sha(output / 'measurement/record.json'), 'fixed_source_sha256': sources,
            'manual_component_fault_detected': record.get('fault_detected'), 'primary_results_added': 0,
            'generation_algorithm_result': False, 'shared_integration_approved': False,
            'semantic_validity': 'pending_joint_review', 'gate_a_approved': False, 'kku_requests': 0, 'queue_mutations': 0})
        native.require(record['status'] == 'complete', 'Full evaluator did not complete; see retained measurement')
    print(json.dumps({'status': record['status'], 'manual_component_fault_detected': record['fault_detected'],
                      'coverage': [record['line_covered'], record['line_total'], record['branch_covered'], record['branch_total']]}))


if __name__ == '__main__':
    output = BASE / 'd4j-v3'
    try: main()
    except BaseException as error:
        if output.exists(): native.write(output / 'failure.json', {'status': 'failed_attempt_retained', 'reason': type(error).__name__ + ': ' + str(error)})
        raise
    finally:
        if output.exists(): native.write(output / 'checksums.json', {p.relative_to(output).as_posix(): native.sha(p) for p in sorted(output.rglob('*')) if p.is_file()})
