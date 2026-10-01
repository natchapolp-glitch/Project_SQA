"""Exclude hash-identified Cli methods containing fixed-compiler unsupported API."""
from normalize_provider_source import sha, positions, remove_methods, utf16_index
from provider_compatibility_v39 import repair as inherited_repair

def repair(tests, project, tool, seed):
    edits = inherited_repair(tests, project, tool, seed)
    if (project, tool, seed) != ('Cli', 'intellisphere', 103):
        return edits
    path = tests / 'org/apache/commons/cli/CommandLineTest.java'
    if not path.exists():
        return edits
    before = sha(path)
    if before != 'cb6843f9232e09a01a1178cd3023ce27012ce47931e20ec2dc3820d996db3506':
        raise ValueError('Cli source differs from preserved fixed compiler failure')
    text = path.read_text(encoding='utf-8')
    bad = [r for r in positions(path) if '.setValue(' in text[utf16_index(text,r['start_utf16']):utf16_index(text,r['end_utf16'])]]
    remove_methods(path, bad)
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'fixed_api_compatibility':{
                      'kind':'exclude_unsupported_api_methods',
                      'removed_methods':[r['owner']+'::'+r['name'] for r in bad],
                      'reason':'Fixed compilation reports Option.setValue unavailable. Exclude entire methods using that API; surviving assertions unchanged.'},
                  'selection':'Fixed compiler only; v54 failed attempt preserved. No buggy or coverage outcomes used.'})
    return edits
