"""Exclude a Compress method using a constructor absent from fixed revision."""
from normalize_provider_source import sha, positions, remove_methods, utf16_index
from provider_compatibility_v39 import repair as inherited_repair

def repair(tests, project, tool, seed):
    edits = inherited_repair(tests, project, tool, seed)
    if (project, tool, seed) != ('Compress', 'intellisphere', 103):
        return edits
    path = tests/'org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java'
    if not path.exists():
        return edits
    before = sha(path)
    if before != 'a863e9cc5bcbbff4a117fd469da656266c1f674ac3e743c8642aa178d2ff6f86':
        raise ValueError('Compress source differs from preserved fixed compiler failure')
    text = path.read_text(encoding='utf-8')
    bad = [r for r in positions(path) if 'new CpioArchiveEntry()' in text[utf16_index(text,r['start_utf16']):utf16_index(text,r['end_utf16'])]]
    assert 1 <= len(bad) <= 47
    remove_methods(path, bad)
    edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,'after_sha256':sha(path),'fixed_api_compatibility':{'kind':'exclude_unsupported_api_methods','removed_methods':[r['owner']+'::'+r['name'] for r in bad],'reason':'Fixed compiler reports CpioArchiveEntry() absent. Exclude the whole method; surviving assertions unchanged.'},'selection':'Fixed compiler only; v60 failed attempt preserved. No buggy or coverage outcomes used.'})
    return edits
