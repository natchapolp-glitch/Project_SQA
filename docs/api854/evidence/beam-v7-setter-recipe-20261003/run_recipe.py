"""Evaluate a prospective setter oracle in isolation; never changes shared policy."""
import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / 'scripts/study'))
from scripts.study.api854.common import cpu_slot, write_json, sha256
from scripts.study.api854.pack_suite import pack_suite
from run import stage
from evaluate import EvaluationConfig, evaluate_run

base = Path(__file__).parent
d4j = '/home/aomsin/sqa-round2/defects4j/framework/bin/defects4j'
lock_root = Path('/home/aomsin/sqa-round2/worktrees-isolated')
with cpu_slot(lock_root):
    package = pack_suite(base / 'sources', base / 'package', 4, 30)
    trees = lock_root / 'beam-v7-setter-recipe-20261003'
    trees.mkdir()
    for revision in ('f', 'b'):
        stage([d4j, 'checkout', '-p', 'Codec', '-v', '1' + revision, '-w', trees / revision],
              ROOT, base / ('checkout-' + revision), 900)
    source = 'src/java/org/apache/commons/codec/language/Metaphone.java'
    expected = ROOT / 'output/api854-20261003/prepare-v7-twenty-bug-development/Codec-1/fixed-source' / source
    if sha256(trees / 'f' / source) != sha256(expected):
        raise ValueError('Fixed setter source differs from retained v7')
    classes = base / 'target-classes.txt'
    classes.write_text('org.apache.commons.codec.language.Metaphone\n', encoding='utf-8')
    result = evaluate_run(EvaluationConfig(project='Codec', bug_id=1,
        generator='setter-recipe-development', seed=101, budget=4,
        suite=base / 'package/suite.tar.bz2', buggy_worktree=trees / 'b', fixed_worktree=trees / 'f',
        output=base / 'evaluation', classes_file=classes, d4j=d4j, test_count=4, timeout_seconds=300))
    write_json(base / 'receipt.json', {'primary': False, 'shared_policy_changed': False,
        'team_semantic_approval': False, 'real_kku_requests': 0, 'queue_mutations': 0,
        'purpose': 'New handwritten getter/state oracle for one currently unsupported declaration; not a study approach.',
        'fixed_source_sha256': sha256(expected), 'suite_sha256': package['suite_sha256'],
        'result_sha256': sha256(base / 'evaluation/record.json'), 'status': result['status'],
        'fault_detected': result['fault_detected'], 'runner_sha256': sha256(__file__)})
    checksums = {p.relative_to(base).as_posix(): sha256(p) for p in base.rglob('*') if p.is_file()}
    write_json(base / 'checksums.json', checksums)
    print(json.dumps({'status': result['status'], 'fault_detected': result['fault_detected']}))
