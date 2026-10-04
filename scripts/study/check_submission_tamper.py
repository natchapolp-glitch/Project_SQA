#!/usr/bin/env python3
"""Exercise submission tamper rejection in memory; preserve original evidence."""
import argparse
from copy import deepcopy
from io import StringIO
from pathlib import Path
from unittest.mock import patch
import solo_batch as batch
from verify_tonight_snapshot import verify


def check(root):
    root = root.resolve()
    manifest_path = root / 'Experiment/snapshot.json'
    original_read, original_open, original_bytes = batch.read, Path.open, Path.read_bytes
    manifest = deepcopy(batch.read(manifest_path))
    next(r for r in manifest['results'] if r['state'] == 'DONE').update(state='PENDING', archive=None)
    csv_path = root / 'Report/data/final_comparison.csv'
    import csv
    with csv_path.open(encoding='utf-8', newline='') as stream:
        reader = csv.DictReader(stream)
        fields, rows = reader.fieldnames, list(reader)
    next(r for r in rows if r['state'] == 'DONE')['line_covered'] = '999999'
    text = StringIO()
    writer = csv.DictWriter(text, fieldnames=fields)
    writer.writeheader()
    writer.writerows(rows)
    protocol_path = root / 'Experiment/evidence/offline/Experiment/protocol/offline.json'
    attacks = {
        'manifest_state_downgrade': patch.object(batch, 'read', side_effect=lambda p:
                deepcopy(manifest) if Path(p) == manifest_path else original_read(p)),
        'csv_metric_inflation': patch.object(Path, 'open', lambda self, *a, **kw:
                StringIO(text.getvalue()) if self == csv_path else original_open(self, *a, **kw)),
        'protocol_byte_change': patch.object(Path, 'read_bytes', lambda self:
                b'{}' if self == protocol_path else original_bytes(self))}
    result = {}
    for name, mutation in attacks.items():
        with mutation:
            try:
                verify(root, require_artifacts=False)
            except (ValueError, batch.StageError):
                result[name] = 'REJECTED'
            else:
                raise AssertionError('Verifier accepted tampering: ' + name)
    return {'checks': result, 'original_evidence_changed': False, 'ai_requests': 0}


if __name__ == '__main__':
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--snapshot', type=Path, required=True)
    cli.add_argument('--receipt', type=Path)
    args = cli.parse_args()
    result = check(args.snapshot)
    if args.receipt:
        batch.write(args.receipt, result)
    print(result)
