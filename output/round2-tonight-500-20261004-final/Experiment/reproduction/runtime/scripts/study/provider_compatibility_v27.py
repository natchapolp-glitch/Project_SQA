"""Complete fixed visibility inventory and reflection checked-exception declaration."""
import re
from normalize_provider_source import sha
from provider_compatibility_v26 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if tool != 'intellisphere':
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        changes = []
        if (project, seed) == ('Mockito', 101):
            old = 'public void testCaptureArgumentsFrom_VarArgs_CapturesCorrectly() {'
            if old in text:
                text = text.replace(old, old.replace('() {', '() throws Exception {'))
                changes.append({'reason': 'Declare existing getMethod checked exception on original JUnit test method; body and verification unchanged.'})
        if (project, seed) == ('JxPath', 103) and path.name == 'DOMNodePointerTest.java':
            text, pi_count = re.subn(r'\(\(DOMNodePointer\)\s*(\w+)\)\.getRelativePositionOfPI\(("[^"]*")\)',
                lambda m: 'invokeOriginalPosition((DOMNodePointer) ' + m.group(1) + ', "getRelativePositionOfPI", new Class[] {String.class}, new Object[] {' + m.group(2) + '})', text)
            text, text_count = re.subn(r'\(\(DOMNodePointer\)\s*(\w+)\)\.getRelativePositionOfTextNode\(\)',
                lambda m: 'invokeOriginalPosition((DOMNodePointer) ' + m.group(1) + ', "getRelativePositionOfTextNode", new Class[0], new Object[0])', text)
            if pi_count or text_count:
                helper = '''
    private static int invokeOriginalPosition(DOMNodePointer pointer, String name,
            Class[] parameterTypes, Object[] arguments) {
        try {
            java.lang.reflect.Method method = DOMNodePointer.class
                .getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return ((Integer) method.invoke(pointer, arguments)).intValue();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
'''
                text = text.replace('public class DOMNodePointerTest extends TestCase {',
                    'public class DOMNodePointerTest extends TestCase {\n' + helper, 1)
                changes.append({'pi_occurrences': pi_count, 'text_occurrences': text_count,
                    'reason': 'Fixed private int getRelativePositionOfPI(String)/getRelativePositionOfTextNode() visibility inventory; invoke same methods on original instances with original arguments/expectations.'})
        if changes:
            path.write_text(text, encoding='utf-8')
            edits.append({'path': path.relative_to(tests).as_posix(),
                'before_sha256': before, 'after_sha256': sha(path),
                'local_compatibility_repairs': changes,
                'selection': 'Fixed compile diagnostics and production method signatures; no buggy feedback.'})
    return edits
