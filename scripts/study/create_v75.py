from pathlib import Path
import hashlib, json
from datetime import datetime, timezone
root = Path(__file__).resolve().parents[2]
compat = root/'scripts/study/provider_compatibility_v75.py'
driver = root/'scripts/study/evaluate_provider_normalized_v75.py'
policy = root/'results/study/kku-only-20261001/processing-policy-v75.json'
if any(p.exists() for p in (compat,driver,policy)): raise ValueError('Version already exists')
source = root/'ai-tests/provider-captures/kku-only-20261001/claude/Compress-1/s101-i1-newaccount-haiku-20261002/source-processing-v73/input-tests/org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java'
expected = hashlib.sha256(source.read_bytes()).hexdigest()
compat.write_text('''import hashlib
from normalize_provider_source import positions, remove_methods

def repair(tests, project, tool, seed):
    if (project, tool, seed) != ('Compress', 'intellisphere', 101): return []
    path = tests/'org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java'
    before = hashlib.sha256(path.read_bytes()).hexdigest()
    if before != EXPECTED: raise ValueError('Unexpected Compress source')
    selected = [m for m in positions(path) if m['name'] == 'testFormatPropagatedToEntry']
    if len(selected) != 1: raise ValueError('Unexpected incompatible method count')
    remove_methods(path, selected)
    return [{'path': path.relative_to(tests).as_posix(), 'before_sha256': before,
             'after_sha256': hashlib.sha256(path.read_bytes()).hexdigest(),
             'fixed_api_compatibility': 'Exclude whole testFormatPropagatedToEntry: v73 fixed compiler rejects nonexistent CpioArchiveEntry() constructor. Retained assertions unchanged; prior failure preserved.'}]
'''.replace('EXPECTED',repr(expected)),encoding='utf-8')
text=(root/'scripts/study/evaluate_provider_normalized_v73.py').read_text().replace('v73','v75')
text=text.replace("def repair(tests, project, tool, seed):\n    return []  # No historical, hash-specific compatibility repairs applied to fresh captures.", 'from provider_compatibility_v75 import repair')
text=text.replace("PROCESSING_SOURCES = ('scripts/study/evaluate_provider_normalized_v75.py',", "PROCESSING_SOURCES = ('scripts/study/evaluate_provider_normalized_v75.py', 'scripts/study/provider_compatibility_v75.py',")
text=text.replace('No inherited hash-specific compatibility repairs.', 'One disclosed hash-specific whole-method exclusion for Compress101 from v73 fixed compiler diagnostics.')
driver.write_text(text,encoding='utf-8')
policy.write_text(json.dumps({'version':75,'created_at_utc':datetime.now(timezone.utc).isoformat(),
    'policy':'v73 generic policy plus one hash-guarded whole-method exclusion for unsupported CpioArchiveEntry() constructor in Compress101, selected only from fixed compile diagnostics. No replacement constructor or assertion changes; preserve failures; at most two fixed-only pruning passes.',
    'input_source_sha256':expected,'source_sha256':{p.relative_to(root).as_posix():hashlib.sha256(p.read_bytes()).hexdigest() for p in (compat,driver)}},indent=2)+'\n',encoding='utf-8')
