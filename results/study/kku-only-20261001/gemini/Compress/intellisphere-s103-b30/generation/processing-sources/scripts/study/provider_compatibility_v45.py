"""Fixed API constructor adaptation, Compress Gemini103; assertions preserved."""
import re
from normalize_provider_source import sha
from provider_compatibility_v44 import repair as previous

def repair(tests, project, tool, seed):
    edits = previous(tests, project, tool, seed)
    if (project, tool, seed) != ('Compress', 'intellisphere', 103):
        return edits
    p = tests / 'org/apache/commons/compress/archivers/cpio/CpioArchiveOutputStreamTest.java'
    before = sha(p)
    if before != '6814cf529f7f166e4aa05159493ae4b3d7d2d2e436141d23bf9b4db25c0ba34b':
        raise ValueError('Compress103 source differs')
    text = p.read_text(encoding='utf-8')
    pattern = r'(CpioArchiveEntry\s+(\w+)\s*=\s*)new CpioArchiveEntry\((CpioConstants\.FORMAT_NEW(?:_CRC)?),\s*("[^"]+"),\s*([^;]+)\);'
    def replacement(m):
        return m[1] + 'new CpioArchiveEntry(' + m[3] + ');\n        ' + m[2] + '.setName(' + m[4] + ');\n        ' + m[2] + '.setSize(' + m[5] + ');'
    updated, count = re.subn(pattern, replacement, text)
    if count != 11:
        raise ValueError('Compress103 constructor count differs')
    p.write_text(updated, encoding='utf-8')
    edits.append({'path': p.relative_to(tests).as_posix(), 'before_sha256': before,
                  'after_sha256': sha(p), 'fixed_api_compatibility': 'Replace 11 unavailable (format,name,size) constructors with format constructor plus setName/setSize; same inputs and assertions.',
                  'selection': 'Fixed compiler and fixed CpioArchiveEntry API only; failed v39 preserved; no buggy feedback.'})
    return edits
