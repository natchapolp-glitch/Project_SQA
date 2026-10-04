import hashlib
from normalize_provider_source import positions, remove_methods

def repair(tests, project, tool, seed):
    if (project, tool, seed) != ('Compress', 'intellisphere', 101): return []
    path = tests/'org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java'
    before = hashlib.sha256(path.read_bytes()).hexdigest()
    if before != 'd873a568dfa6d9428609dbd47a5902914e805a50838253b462d6a16f3f09766e': raise ValueError('Unexpected Compress source')
    selected = [m for m in positions(path) if m['name'] == 'testFormatPropagatedToEntry']
    if len(selected) != 1: raise ValueError('Unexpected incompatible method count')
    remove_methods(path, selected)
    return [{'path': path.relative_to(tests).as_posix(), 'before_sha256': before,
             'after_sha256': hashlib.sha256(path.read_bytes()).hexdigest(),
             'fixed_api_compatibility': 'Exclude whole testFormatPropagatedToEntry: v73 fixed compiler rejects nonexistent CpioArchiveEntry() constructor. Retained assertions unchanged; prior failure preserved.'}]
