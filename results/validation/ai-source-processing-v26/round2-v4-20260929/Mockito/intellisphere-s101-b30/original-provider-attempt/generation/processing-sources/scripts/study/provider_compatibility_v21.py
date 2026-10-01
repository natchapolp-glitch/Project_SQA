"""Access the same fixed private method through reflection; preserve assertions."""
from normalize_provider_source import sha
from provider_compatibility_v20 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if (project, tool, seed) != ('Closure', 'intellisphere', 101):
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        old = 'RemoveUnusedVars.getFunctionArgList(function)'
        if old not in text:
            continue
        before = sha(path)
        count = text.count(old)
        text = text.replace(old, 'invokeOriginalFunctionArgList(function)')
        helper = '''
  private static Node invokeOriginalFunctionArgList(Node function) {
    try {
      java.lang.reflect.Method method = RemoveUnusedVars.class
          .getDeclaredMethod("getFunctionArgList", Node.class);
      method.setAccessible(true);
      return (Node) method.invoke(null, function);
    } catch (Exception exception) {
      throw new IllegalStateException(exception);
    }
  }
'''
        text = text.replace('public class RemoveUnusedVarsTest {',
            'public class RemoveUnusedVarsTest {\n' + helper, 1)
        path.write_text(text, encoding='utf-8')
        edits.append({'path': path.relative_to(tests).as_posix(),
            'before_sha256': before, 'after_sha256': sha(path),
            'local_compatibility_repairs': [{'old': old,
                'new': 'invokeOriginalFunctionArgList(function)', 'occurrences': count,
                'reason': 'Fixed getFunctionArgList is private static; reflection invokes the same method with the same input and preserves all assertions.'}],
            'selection': 'Fixed compiler diagnostic and fixed production visibility only; no buggy feedback.'})
    return edits
