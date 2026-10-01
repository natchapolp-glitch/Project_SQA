"""Remove a hash-identified filename header emitted inside a Java fence."""
from normalize_provider_source import sha
from provider_compatibility_v39 import repair as inherited_repair

def repair(tests, project, tool, seed):
    edits = inherited_repair(tests, project, tool, seed)
    if (project, tool, seed) != ('JacksonCore', 'intellisphere', 101):
        return edits
    path = tests / 'com/fasterxml/jackson/core/io/NumberInputTest.java'
    if not path.exists():
        return edits
    before = sha(path)
    header = 'com/fasterxml/jackson/core/io/NumberInputTest.java\n'
    text = path.read_text(encoding='utf-8')
    if before != '2e1ac79767a7bc58911c0498e6bfff358b980f5d60d939bce0b8ada19aafa437' or not text.startswith(header):
        raise ValueError('JacksonCore filename header source differs from preserved raw input')
    path.write_text(text[len(header):], encoding='utf-8')
    edits.append({'path':path.relative_to(tests).as_posix(), 'before_sha256':before,
                  'after_sha256':sha(path), 'local_syntax_recovery':{
                      'kind':'non_java_filename_header', 'removed':header.rstrip(),
                      'reason':'Java AST parser rejects the standalone filename before the package declaration. Remove only this header; all Java statements and assertions unchanged.'},
                  'selection':'Syntax diagnosis only; no fixed or buggy outcomes used.'})
    return edits
