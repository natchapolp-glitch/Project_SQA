"""Replace unsupported Closure compile-with-pass fixture using fixed APIs."""
import re
from normalize_provider_source import sha
from provider_compatibility_v19 import repair as prior


def repair(tests, project, tool, seed):
    edits = prior(tests, project, tool, seed)
    if (project, tool, seed) != ('Closure', 'intellisphere', 101):
        return edits
    for path in tests.rglob('*.java'):
        text = path.read_text(encoding='utf-8')
        if 'public class RemoveUnusedVarsTest' not in text:
            continue
        before = sha(path)
        # The response has four compile calls, each passing RemoveUnusedVars
        # where this fixed Compiler API requires CompilerOptions.
        text, count = re.subn(r'\b(compiler|localCompiler)\.compile\(\s*',
            lambda m: 'runSelectedPass(' + m.group(1) + ',\n        ', text)
        if count != 4:
            raise ValueError('Unexpected original Closure fixture: %s compile calls' % count)
        helper = '''
  // Local compatibility fixture: parse and normalize, then run the original
  // selected pass. Source inputs, pass flags, and assertions are unchanged.
  private static void runSelectedPass(Compiler target, SourceFile externs,
      SourceFile input, RemoveUnusedVars selectedPass) {
    CompilerOptions options = target.getOptions();
    target.init(java.util.Collections.singletonList(externs),
        java.util.Collections.singletonList(input), options);
    target.setLifeCycleStage(AbstractCompiler.LifeCycleStage.RAW);
    Node combinedRoot = target.parseInputs();
    if (combinedRoot == null) {
      throw new IllegalStateException("Could not parse original test fixture");
    }
    Node externRoot = combinedRoot.getFirstChild();
    Node inputRoot = combinedRoot.getLastChild();
    new Normalize(target, false).process(externRoot, inputRoot);
    target.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    selectedPass.process(externRoot, inputRoot);
  }
'''
        text = text.replace('public class RemoveUnusedVarsTest {',
            'public class RemoveUnusedVarsTest {\n' + helper, 1)
        path.write_text(text, encoding='utf-8')
        edits.append({'path': path.relative_to(tests).as_posix(),
            'before_sha256': before, 'after_sha256': sha(path),
            'local_compatibility_repairs': [{'occurrences': count,
                'reason': 'Fixed Compiler.compile accepts CompilerOptions, not a CompilerPass. Use fixed init/parseInputs/Normalize/process APIs to run the original selected pass.'}],
            'selection': 'Fixed production API and prior fixed compiler diagnostics only; original inputs, pass constructor flags, assertions and expected values unchanged.'})
    return edits
