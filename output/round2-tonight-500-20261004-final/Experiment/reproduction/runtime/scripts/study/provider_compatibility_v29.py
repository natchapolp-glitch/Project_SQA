"""Invoke equivalent original private positioning methods for retained JDOM tests."""
import re
from normalize_provider_source import sha
from provider_compatibility_v28 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if (project, tool, seed) != ('JxPath', 'intellisphere', 103):
        return edits
    for path in tests.rglob('JDOMNodePointerTest.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        text, count = re.subn(r'\(\(JDOMNodePointer\)\s*(\w+)\)\.(getRelativePositionOfElement|getRelativePositionOfTextNode)\(\)',
            lambda m: 'invokeOriginalJdomPosition((JDOMNodePointer) ' + m.group(1) + ', "' + m.group(2) + '", new Class[0], new Object[0])', text)
        text, pi_count = re.subn(r'\(\(JDOMNodePointer\)\s*(\w+)\)\.getRelativePositionOfPI\(("[^"]*")\)',
            lambda m: 'invokeOriginalJdomPosition((JDOMNodePointer) ' + m.group(1) + ', "getRelativePositionOfPI", new Class[] {String.class}, new Object[] {' + m.group(2) + '})', text)
        if not count and not pi_count:
            continue
        helper = '''
    private static int invokeOriginalJdomPosition(JDOMNodePointer pointer, String name,
            Class[] parameterTypes, Object[] arguments) {
        try {
            java.lang.reflect.Method method = JDOMNodePointer.class
                .getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return ((Integer) method.invoke(pointer, arguments)).intValue();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
'''
        text = text.replace('public class JDOMNodePointerTest extends TestCase {',
            'public class JDOMNodePointerTest extends TestCase {\n' + helper, 1)
        path.write_text(text, encoding='utf-8')
        edits.append({'path': path.relative_to(tests).as_posix(),
            'before_sha256': before, 'after_sha256': sha(path),
            'local_compatibility_repairs': [{'zero_arg_occurrences': count, 'pi_occurrences': pi_count,
                'reason': 'Retained JDOM method invokes private fixed positioning API; reflection keeps the same method, instance, arguments and expected positions.'}],
            'selection': 'Fixed compiler and fixed JDOM method visibility inventory; no buggy feedback.'})
    return edits
