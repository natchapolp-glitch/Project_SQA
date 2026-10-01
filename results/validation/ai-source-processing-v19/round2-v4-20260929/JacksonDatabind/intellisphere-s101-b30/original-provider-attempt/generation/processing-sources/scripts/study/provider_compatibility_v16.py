"""Preserve format/name values through the fixed CpioArchiveEntry API."""
import re
from normalize_provider_source import sha
from provider_compatibility_v15 import repair as prior

def repair(tests,project,tool,seed):
    edits = prior(tests,project,tool,seed)
    if (project,tool,seed) != ('Compress','intellisphere',101):
        return edits
    pattern = r'(CpioArchiveEntry\s+(\w+)\s*=\s*)new CpioArchiveEntry\((CpioConstants\.FORMAT_\w+),\s*("[^"]*")\);'
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        changes = []
        def replace(match):
            result = match[1]+'new CpioArchiveEntry('+match[3]+');\n        '+match[2]+'.setName('+match[4]+');'
            changes.append({'old':match[0],'new':result,'reason':'Fixed class provides CpioArchiveEntry(short) and public setName(String), not the combined constructor; values preserved.'})
            return result
        updated = re.sub(pattern,replace,text)
        if changes:
            before = sha(path)
            path.write_text(updated,encoding='utf-8')
            edits.append({'path':path.relative_to(tests).as_posix(),'before_sha256':before,
                'after_sha256':sha(path),'local_compatibility_repairs':changes,
                'selection':'Fixed compiler diagnostics and fixed constructor/setName implementation only; assertions unchanged.'})
    return edits
