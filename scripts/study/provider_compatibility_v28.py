"""Invoke original private DOM string helpers with unchanged arguments/assertions."""
import re
from normalize_provider_source import sha
from provider_compatibility_v27 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if (project, tool, seed) != ('JxPath', 'intellisphere', 103):
        return edits
    for path in tests.rglob('DOMNodePointerTest.java'):
        text = path.read_text(encoding='utf-8')
        before = sha(path)
        text, escape_count = re.subn(r'\(\(DOMNodePointer\)\s*(\w+)\)\.escape\(("(?:\\.|[^"\\])*")\)',
            lambda m: 'invokeOriginalString((DOMNodePointer) ' + m.group(1) + ', "escape", String.class, ' + m.group(2) + ')', text)
        text, value_count = re.subn(r'\(\(DOMNodePointer\)\s*(\w+)\)\.stringValue\((\w+)\)',
            lambda m: 'invokeOriginalString((DOMNodePointer) ' + m.group(1) + ', "stringValue", org.w3c.dom.Node.class, ' + m.group(2) + ')', text)
        if not escape_count and not value_count:
            continue
        helper = '''
    private static String invokeOriginalString(DOMNodePointer pointer, String name,
            Class parameterType, Object argument) {
        try {
            java.lang.reflect.Method method = DOMNodePointer.class
                .getDeclaredMethod(name, parameterType);
            method.setAccessible(true);
            return (String) method.invoke(pointer, argument);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
'''
        text = text.replace('public class DOMNodePointerTest extends TestCase {',
            'public class DOMNodePointerTest extends TestCase {\n' + helper, 1)
        path.write_text(text, encoding='utf-8')
        edits.append({'path': path.relative_to(tests).as_posix(),
            'before_sha256': before, 'after_sha256': sha(path),
            'local_compatibility_repairs': [{'escape_occurrences': escape_count,
                'string_value_occurrences': value_count,
                'reason': 'Fixed escape(String)/stringValue(org.w3c.dom.Node) are private; reflection calls the same methods on original instances with unchanged inputs and assertion values.'}],
            'selection': 'Fixed production visibility/signatures and compilation diagnostics only.'})
    return edits
