"""Mechanical source processing; raw provider evidence remains unchanged."""
from pathlib import Path
import hashlib
import re
import subprocess

ROOT = Path(__file__).resolve().parents[2]
JAVA_SOURCE = ROOT / 'scripts/study/java/TestMethodPositions.java'
CLASSES = ROOT / 'tmp/java-provider-normalizer'


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def renderer_suffixes(text):
    """Remove only [integer](undefined) suffixes outside literals and comments."""
    removed, i = [], 0
    while i < len(text):
        if text.startswith('//', i):
            end = text.find('\n', i)
            i = len(text) if end < 0 else end + 1
        elif text.startswith('/*', i):
            end = text.find('*/', i + 2)
            i = len(text) if end < 0 else end + 2
        elif text[i] in ('"', "'"):
            quote, i = text[i], i + 1
            while i < len(text):
                if text[i] == '\\':
                    i += 2
                elif text[i] == quote:
                    i += 1
                    break
                else:
                    i += 1
        elif text.startswith('(undefined)', i) and re.search(r'\[\d+\]$', text[:i]):
            removed.append((i, i + len('(undefined)')))
            i += len('(undefined)')
        else:
            i += 1
    for start, end in reversed(removed):
        text = text[:start] + text[end:]
    return text, removed


def positions(path):
    CLASSES.mkdir(parents=True, exist_ok=True)
    stamp = CLASSES / 'source.sha256'
    expected = sha(JAVA_SOURCE)
    if not stamp.exists() or stamp.read_text() != expected:
        subprocess.run(['javac', '-d', str(CLASSES), str(JAVA_SOURCE)], check=True,
                       capture_output=True, text=True)
        stamp.write_text(expected)
    result = subprocess.run(['java', '-cp', str(CLASSES), 'TestMethodPositions', str(path)],
                            check=True, capture_output=True, text=True)
    rows = []
    for line in result.stdout.splitlines():
        owner, name, start, end = line.split('\t')
        rows.append({'owner': owner, 'name': name, 'start_utf16': int(start), 'end_utf16': int(end)})
    return rows


def utf16_index(text, offset):
    """Javac spans count UTF-16 code units, including supplementary characters."""
    return len(text.encode('utf-16-le')[:offset * 2].decode('utf-16-le'))


def remove_methods(path, rows):
    text = path.read_text(encoding='utf-8')
    spans = [(utf16_index(text, r['start_utf16']), utf16_index(text, r['end_utf16'])) for r in rows]
    for start, end in sorted(spans, reverse=True):
        text = text[:start] + text[end:]
    path.write_text(text, encoding='utf-8')


def normalize(tests_root, tool, maximum=30):
    edits, retained, original_count = [], 0, 0
    for path in sorted(tests_root.rglob('*.java')):
        before = sha(path)
        text = path.read_text(encoding='utf-8')
        suffixes = []
        if tool == 'intellisphere':
            text, suffixes = renderer_suffixes(text)
            path.write_text(text, encoding='utf-8')
        methods = positions(path)
        original_count += len(methods)
        permitted = max(0, maximum - retained)
        excess = methods[permitted:]
        if methods and permitted == 0:
            path.unlink()
        elif excess:
            remove_methods(path, excess)
        retained += min(permitted, len(methods))
        if suffixes or excess:
            edits.append({'path': path.relative_to(tests_root).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path) if path.exists() else None,
                'renderer_suffixes_removed': len(suffixes),
                'budget_removed_methods': [r['owner'] + '::' + r['name'] for r in excess],
                'selection': 'First 30 methods in sorted file/source order; no execution outcomes used.'})
    return retained, original_count, edits


def prune_fixed_failures(tests_root, identifiers):
    removed = []
    for path in sorted(tests_root.rglob('*.java')):
        methods = positions(path)
        bad = [r for r in methods if r['owner'] + '::' + r['name'] in identifiers]
        if bad:
            before = sha(path)
            if len(bad) == len(methods):
                path.unlink()
            else:
                remove_methods(path, bad)
            removed.append({'path': path.relative_to(tests_root).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path) if path.exists() else None,
                'fixed_failed_methods_removed': [r['owner'] + '::' + r['name'] for r in bad],
                'selection': 'Only assertion failures on fixed revision; no buggy outcomes used.'})
    return removed
