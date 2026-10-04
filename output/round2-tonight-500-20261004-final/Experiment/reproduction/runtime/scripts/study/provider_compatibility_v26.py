"""Correct a fixed-diagnosed typo and invoke the same private DOM positioning API."""
import re
from normalize_provider_source import sha
from provider_compatibility_v25 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        changes = []
        if (project, seed) == ('Mockito', 101) and 'veriffy(' in text:
            changes.append({'old': 'veriffy(', 'new': 'verify(',
                'occurrences': text.count('veriffy('),
                'reason': 'Fixed compiler identifies misspelled imported Mockito.verify API; original capture arguments unchanged.'})
            text = text.replace('veriffy(', 'verify(')
        if (project, seed) == ('JxPath', 103):
            text, count = re.subn(r'\(\(DOMNodePointer\)\s*(\w+)\)\.getRelativePositionOfElement\(\)',
                lambda m: 'invokeOriginalElementPosition((DOMNodePointer) ' + m.group(1) + ')', text)
            if count:
                helper = '''
    private static int invokeOriginalElementPosition(DOMNodePointer pointer) {
        try {
            java.lang.reflect.Method method = DOMNodePointer.class
                .getDeclaredMethod("getRelativePositionOfElement");
            method.setAccessible(true);
            return ((Integer) method.invoke(pointer)).intValue();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
'''
                text = text.replace('public class DOMNodePointerTest extends TestCase {',
                    'public class DOMNodePointerTest extends TestCase {\n' + helper, 1)
                changes.append({'occurrences': count,
                    'reason': 'Fixed getRelativePositionOfElement is private; invoke same method/instance through reflection without changing expected positions.'})
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path': path.relative_to(tests).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path),
                'local_compatibility_repairs': changes,
                'selection': 'Fixed compile diagnostics and fixed API visibility; no buggy outcomes used.'})
    return edits
