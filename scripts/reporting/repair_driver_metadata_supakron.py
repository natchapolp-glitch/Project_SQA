"""Correct three metadata pointers; preserve original records and actual driver bytes."""
import json, hashlib, shutil
from pathlib import Path
root = Path(__file__).resolve().parents[2]
receipt = root / 'results/validation/supakron-driver-metadata-20261001'
receipt.mkdir(exist_ok=False)
changes = []
for project, seed, version in [('Cli', 101, 52), ('Cli', 102, 53), ('Closure', 103, 52)]:
    run = root / f'results/study/kku-only-20261001/gemini/{project}/intellisphere-s{seed}-b30'
    path = run / 'evaluation/record.json'
    record = json.loads(path.read_text())
    driver = f'scripts/study/evaluate_provider_normalized_v{version}.py'
    digest = hashlib.sha256((root / driver).read_bytes()).hexdigest()
    assert hashlib.sha256((run / 'generation/execution-driver.py').read_bytes()).hexdigest() == digest
    assert record['ai_processing_source_sha256'][driver] == digest
    old = record['ai_execution_driver']
    before = hashlib.sha256(path.read_bytes()).hexdigest()
    shutil.copy2(path, receipt / f'{project}-{seed}-record-before.json')
    record['ai_execution_driver'] = driver
    record['ai_execution_driver_sha256'] = digest
    record['metadata_correction_receipt'] = receipt.relative_to(root).as_posix() + '/receipt.json'
    path.write_text(json.dumps(record, indent=2) + '\n')
    changes.append({'project': project, 'seed': seed, 'old_pointer': old,
        'actual_driver': driver, 'driver_sha256': digest,
        'before_sha256': before, 'after_sha256': hashlib.sha256(path.read_bytes()).hexdigest()})
(receipt / 'receipt.json').write_text(json.dumps({'reason': 'Processing dependency prepended before driver; index-zero metadata pointer was wrong. Actual snapshotted driver and all execution bytes unchanged.', 'changes': changes}, indent=2) + '\n')
