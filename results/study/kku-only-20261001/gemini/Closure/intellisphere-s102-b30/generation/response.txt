An extensive suite of regression tests for `RemoveUnusedVars` in the Google Closure Compiler has been created.

### Test Suite Design and Coverage

The regression tests target the behavior of `RemoveUnusedVars` as documented and implemented in the reference revision:
- **Normalization Precondition**: Asserts that `process(Node, Node)` enforces `LifeCycleStage.NORMALIZED` on the compiler.
- **DefFinder Precondition**: Verifies `Preconditions.checkNotNull(defFinder)` when `modifyCallSites` is set to `true`.
- **Global vs. Local Variable Removal**: Checks behavior when `removeGlobals` is `true` versus `false` for global and local scopes.
- **Side-Effect Preservation**: Ensures that removing variable declarations with side-effecting initial values (e.g., `var a = sideEffect()`) converts them into expression statements (e.g., `sideEffect()`).
- **Function Declaration and Arguments**: Verifies removal of unreferenced functions, removal of trailing unused arguments, and argument retention when `arguments` is accessed.
- **Property Assignments and Escaping Analysis**: Tests removal of properties on literal objects (`var x = {}; x.y = 1;`) versus retention when the variable is initialized to an unknown/side-effecting value (`var x = external(); x.y = 1;`).
- **Function Expression Naming**: Exercises `preserveFunctionExpressionNames` behavior.
- **Self-referential (Recursive) Unused Functions**: Verifies that cycles of unreferenced functions/vars are pruned correctly.

`com/google/javascript/jscomp/RemoveUnusedVarsTest.java`
```java
package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import junit.framework.TestCase;

public class RemoveUnusedVarsTest extends TestCase {

  private Compiler compiler;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
  }

  private Node parseAndNormalize(String js) {
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.RAW);
    Node root = compiler.parseTestCode(js);
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    return root;
  }

  public void testProcessFailsWhenNotNormalized() {
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.RAW);
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode("var a = 1;");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    try {
      pass.process(externs, root);
      fail("Expected IllegalStateException when life cycle stage is not normalized");
    } catch (IllegalStateException expected) {
      // Success
    }
  }

  public void testProcessWithNullDefFinderWhenModifyCallSitesTrue() {
    Node externs = compiler.parseTestCode("");
    Node root = parseAndNormalize("function f() {}");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, true);
    try {
      pass.process(externs, root, null);
      fail("Expected NullPointerException when defFinder is null and modifyCallSites is true");
    } catch (NullPointerException expected) {
      // Success
    }
  }

  public void testRemoveUnusedGlobalVarWhenRemoveGlobalsTrue() {
    Node root = parseAndNormalize("var unused = 10;");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(0, root.getChildCount());
  }

  public void testKeepUnusedGlobalVarWhenRemoveGlobalsFalse() {
    Node root = parseAndNormalize("var unused = 10;");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, false, false, false);
    pass.process(externs, root);

    assertEquals(1, root.getChildCount());
    assertTrue(root.getFirstChild().isVar());
  }

  public void testRemoveUnusedLocalVarEvenWhenRemoveGlobalsFalse() {
    Node root = parseAndNormalize("function foo() { var localUnused = 10; } foo();");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, false, false, false);
    pass.process(externs, root);

    Node functionNode = root.getFirstChild();
    assertTrue(functionNode.isFunction());
    Node functionBody = functionNode.getLastChild();
    assertEquals(0, functionBody.getChildCount());
  }

  public void testKeepReferencedGlobalVar() {
    Node root = parseAndNormalize("var used = 10; alert(used);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(2, root.getChildCount());
    assertTrue(root.getFirstChild().isVar());
    assertEquals("used", root.getFirstChild().getFirstChild().getString());
  }

  public void testRemoveOneOfMultipleVarsInDeclaration() {
    Node root = parseAndNormalize("var used = 1, unused = 2; alert(used);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(2, root.getChildCount());
    Node varNode = root.getFirstChild();
    assertTrue(varNode.isVar());
    assertEquals(1, varNode.getChildCount());
    assertEquals("used", varNode.getFirstChild().getString());
  }

  public void testReplaceUnusedVarWithSideEffectsByExpression() {
    Node root = parseAndNormalize("var unused = externalCall();");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(1, root.getChildCount());
    Node expr = root.getFirstChild();
    assertTrue(expr.isExprResult());
    assertTrue(expr.getFirstChild().isCall());
    assertEquals("externalCall", expr.getFirstChild().getFirstChild().getString());
  }

  public void testRemoveUnusedFunctionDeclaration() {
    Node root = parseAndNormalize("function unusedFn() { return 42; }");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(0, root.getChildCount());
  }

  public void testKeepReferencedFunctionDeclaration() {
    Node root = parseAndNormalize("function usedFn() { return 42; } usedFn();");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(2, root.getChildCount());
    assertTrue(root.getFirstChild().isFunction());
    assertEquals("usedFn", root.getFirstChild().getFirstChild().getString());
  }

  public void testRemoveUnusedFunctionTrailingArgs() {
    Node root = parseAndNormalize("function f(a, b, c) { alert(a); } f(1);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    Node fnNode = root.getFirstChild();
    assertTrue(fnNode.isFunction());
    Node paramList = fnNode.getFirstChild().getNext();
    assertEquals(1, paramList.getChildCount());
    assertEquals("a", paramList.getFirstChild().getString());
  }

  public void testKeepFunctionArgsWhenNotRemovingGlobals() {
    Node root = parseAndNormalize("function f(a, b) { alert(a); } f(1);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, false, false, false);
    pass.process(externs, root);

    Node fnNode = root.getFirstChild();
    assertTrue(fnNode.isFunction());
    Node paramList = fnNode.getFirstChild().getNext();
    assertEquals(2, paramList.getChildCount());
  }

  public void testArgumentsEscapeRetainsAllParameters() {
    Node root = parseAndNormalize("function f(a, b) { return arguments[0]; } f(1, 2);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    Node fnNode = root.getFirstChild();
    assertTrue(fnNode.isFunction());
    Node paramList = fnNode.getFirstChild().getNext();
    assertEquals(2, paramList.getChildCount());
    assertEquals("a", paramList.getFirstChild().getString());
    assertEquals("b", paramList.getFirstChild().getNext().getString());
  }

  public void testPreserveFunctionExpressionNamesTrue() {
    Node root = parseAndNormalize("var f = function myName() {}; alert(f);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, true, false);
    pass.process(externs, root);

    Node varNode = root.getFirstChild();
    assertTrue(varNode.isVar());
    Node fn = varNode.getFirstChild().getFirstChild();
    assertTrue(fn.isFunction());
    assertEquals("myName", fn.getFirstChild().getString());
  }

  public void testPreserveFunctionExpressionNamesFalse() {
    Node root = parseAndNormalize("var f = function myName() {}; alert(f);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    Node varNode = root.getFirstChild();
    assertTrue(varNode.isVar());
    Node fn = varNode.getFirstChild().getFirstChild();
    assertTrue(fn.isFunction());
    assertEquals("", fn.getFirstChild().getString());
  }

  public void testRemoveUnusedAssignWithoutSideEffects() {
    Node root = parseAndNormalize("var a = 1; a = 2;");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(0, root.getChildCount());
  }

  public void testRemoveUnusedAssignWithSideEffectsKeepsRhs() {
    Node root = parseAndNormalize("var a = 1; a = external();");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(1, root.getChildCount());
    Node expr = root.getFirstChild();
    assertTrue(expr.isExprResult());
    assertTrue(expr.getFirstChild().isCall());
    assertEquals("external", expr.getFirstChild().getFirstChild().getString());
  }

  public void testRemoveUnusedObjectLiteralPropertyAssigns() {
    Node root = parseAndNormalize("var obj = {}; obj.foo = 1; obj.bar = 2;");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(0, root.getChildCount());
  }

  public void testKeepPropertyAssignOnUnknownInitialValue() {
    Node root = parseAndNormalize("var obj = external(); obj.foo = 1;");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(2, root.getChildCount());
    assertTrue(root.getFirstChild().isVar());
    assertEquals("obj", root.getFirstChild().getFirstChild().getString());
    assertTrue(root.getLastChild().isExprResult());
  }

  public void testRemoveSelfReferentialRecursiveFunction() {
    Node root = parseAndNormalize("function rec() { rec(); }");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(0, root.getChildCount());
  }

  public void testRemoveMutuallyRecursiveUnreferencedFunctions() {
    Node root = parseAndNormalize("function f() { g(); } function g() { f(); }");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(0, root.getChildCount());
  }

  public void testKeepTransitivelyUsedFunction() {
    Node root = parseAndNormalize("function helper() { return 1; } function main() { return helper(); } main();");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(3, root.getChildCount());
  }

  public void testPreserveForInLoopVariable() {
    Node root = parseAndNormalize("var obj = {a: 1}; for (var prop in obj) {}");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    pass.process(externs, root);

    assertEquals(2, root.getChildCount());
    Node forNode = root.getLastChild();
    assertTrue(forNode.isFor());
    Node varInFor = forNode.getFirstChild();
    assertTrue(varInFor.isVar());
    assertEquals("prop", varInFor.getFirstChild().getString());
  }

  public void testModifyCallSitesRemovesUnusedParameter() {
    Node root = parseAndNormalize("function f(x) {} f(1);");
    Node externs = parseAndNormalize("");
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, true);
    pass.process(externs, root);

    Node fnNode = root.getFirstChild();
    assertTrue(fnNode.isFunction());
    Node paramList = fnNode.getFirstChild().getNext();
    assertEquals(0, paramList.getChildCount());

    Node callExpr = root.getLastChild();
    assertTrue(callExpr.isExprResult());
    Node callNode = callExpr.getFirstChild();
    assertTrue(callNode.isCall());
    assertEquals(1, callNode.getChildCount());
  }
}
```

---

### References
1. Google Inc., *Closure Compiler: JavaScript optimizer and minifier source documentation*, `com.google.javascript.jscomp.RemoveUnusedVars`.
2. Apache Ant Project, *JUnit Task Documentation*, [https://ant.apache.org/manual/Tasks/junit.html](https://ant.apache.org/manual/Tasks/junit.html).
3. JUnit 4 Framework Documentation, *JUnit TestCase and Assertion APIs*, [https://junit.org/junit4/javadoc/latest/](https://junit.org/junit4/javadoc/latest/).