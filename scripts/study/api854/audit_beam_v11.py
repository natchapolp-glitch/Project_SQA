"""Verify public Beam v11 receipts against raw bytes and immutable Aom blobs."""
import argparse
import hashlib
import json
from pathlib import Path
import subprocess


def sha(raw):
    return hashlib.sha256(raw).hexdigest()


def read(path):
    return json.loads(path.read_bytes())


def verify_checksums(folder):
    checks = read(folder / 'checksums.json')
    files = {p.relative_to(folder).as_posix() for p in folder.rglob('*') if p.is_file()}
    assert files == set(checks) | {'checksums.json'}, 'Incomplete checksum inventory'
    for name, expected in checks.items():
        assert sha((folder / name).read_bytes()) == expected, name
    return len(checks)


def audit(repo, packet):
    count = verify_checksums(packet)
    verify_checksums(packet / 'host-review-v1')
    receipt = read(packet / 'receipt.json')
    provenance = read(packet / 'snapshot-provenance.json')
    commit = receipt['source_commit']
    command = ['git', '-c', 'safe.directory=' + str(repo), '-C', str(repo)]
    tree = subprocess.check_output(command + ['ls-tree', '-rz', commit])
    objects = {}
    for entry in tree.split(b'\0'):
        if not entry:
            continue
        header, name = entry.split(b'\t', 1)
        mode, kind, oid = header.split()
        if kind == b'blob':
            objects[name.decode()] = oid
    names = sorted(provenance['source_sha256'])
    data = subprocess.check_output(command + ['cat-file', '--batch'],
                                   input=b'\n'.join(objects[name] for name in names) + b'\n')
    position = 0
    for name in names:
        end = data.index(b'\n', position)
        oid, kind, size = data[position:end].split()
        position = end + 1
        raw = data[position:position + int(size)]
        position += int(size) + 1
        assert oid == objects[name] and kind == b'blob'
        assert sha(raw) == provenance['source_sha256'][name], name
    assert position == len(data)
    public = packet / 'received-aom'
    for path in public.rglob('*'):
        if path.is_file():
            name = path.relative_to(public).as_posix()
            assert sha(path.read_bytes()) == provenance['source_sha256'][name], name
    for binding in receipt['bindings'].values():
        assert binding['commit'] == commit
        assert sha((public / binding['path']).read_bytes()) == binding['sha256']
    assert sha((repo / 'scripts/study/api854/review_aom_v11.py').read_bytes()) == receipt['producer_sha256']
    host = read(packet / 'host-review-v1/host-receipt.json')
    assert sha((packet / 'host-review-v1/host-receipt.json').read_bytes()) == receipt['native_host_receipt_sha256']
    assert host['runtime_source_sha256'] == receipt['runtime_source_sha256']
    assert host['cpu_lock_exits'] == [9, 0]
    assert (host['chronology_cases'], host['chronology_exact_declarations'], host['retained_fixed_cases']) == (13, 6, 64)
    assert host['fixture_failure_rejected_as_fault']
    assert not receipt['all_691_requirement_complete'] and not receipt['gate_a_approved']
    assert receipt['kku_requests'] == receipt['queue_mutations'] == 0
    return {'status': 'pass', 'source_commit': commit, 'packet_checksum_sha256': sha((packet / 'checksums.json').read_bytes()),
            'receipt_sha256': sha((packet / 'receipt.json').read_bytes()),
            'public_checksum_files': count, 'immutable_git_blobs_verified': len(names),
            'all_691_complete': False, 'gate_a_approved': False}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('packet', type=Path)
    parser.add_argument('--output', type=Path, required=True)
    args = parser.parse_args()
    result = audit(Path(__file__).resolve().parents[3], args.packet.resolve())
    with args.output.open('x', encoding='utf-8', newline='\n') as stream:
        json.dump(result, stream, indent=2)
        stream.write('\n')
    print(json.dumps(result))
