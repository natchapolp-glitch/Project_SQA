```java
package com.google.javascript.jscomp;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import junit.framework.TestCase;

/**
 * Deterministic regression tests for {@link RemoveUnusedVars}.
 */
public class RemoveUnusedVarsRegressionTest extends TestCase {

  private Node runPass(String js, boolean removeGlobals,
      boolean preserveFnExprNames, boolean modifyCallSites) {
    Compiler compiler = new Compiler();
    Node root = compiler.parseTestCode(js);
    Node externs = IR.block();
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    RemoveUnusedVars pass = new RemoveUnusedVars(
        compiler, removeGlobals, preserveFnExprNames, modifyCallSites);
    pass.process(externs, root);
    return root;
  }

  public void testLifeCycleNotNormalizedThrows() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseTestCode("var a = 1;");
    Node externs = IR.block();
    // Lifecycle stage not set to NORMALIZED; expect IllegalStateException
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    try {
      pass.process(externs, root);
      fail("Expected IllegalStateException when life cycle is not normalized");
    } catch (IllegalStateException expected) {
      // expected behavior
    }
  }

  public void testModifyCallSitesWithNullDefFinderThrows() {
    Compiler compiler = new Compiler();
    Node root = compiler.parseTestCode("var a = 1;");
    Node externs = IR.block();
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, true);
    try {
      pass.process(externs, root, null);
      fail("Expected NullPointerException when defFinder is null and modifyCallSites is true");
    } catch (NullPointerException expected) {
      // expected behavior
    }
  }

  public void testRemoveUnusedGlobalVar() {
    Node root = runPass("var unused = 10;", true, false, false);
    // The unused variable declaration should be removed completely
    assertEquals(0, root.getChildCount());
  }

  public void testKeepReferencedGlobalVar() {
    Node root = runPass("var used = 10; alert(used);", true, false, false);
    // Both var declaration and alert call should remain
    assertEquals(2, root.getChildCount());
    assertTrue(root.getFirstChild().isVar());
    assertEquals("used", root.getFirstChild().getFirstChild().getString());
  }

  public void testPreserveGlobalVarWhenRemoveGlobalsFalse() {
    Node root = runPass("var unused = 10;", false, false, false);
    // Unused global must not be removed when removeGlobals is false
    assertEquals(1, root.getChildCount());
    assertTrue(root.getFirstChild().isVar());
  }

  public void testRemoveUnusedVarInMultiVarDeclaration() {
    Node root = runPass("var unused = 1, used = 2; alert(used);", true, false, false);
    // Statement 0 is the VAR, statement
```