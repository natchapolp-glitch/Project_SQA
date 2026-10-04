package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RemoveUnusedVarsTest extends CompilerTestCase {

  private boolean removeGlobal;
  private boolean preserveFunctionExpressionNames;
  private boolean modifyCallSites;

  public RemoveUnusedVarsTest() {
    super("");
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    removeGlobal = true;
    preserveFunctionExpressionNames = false;
    modifyCallSites = false;
    enableNormalize();
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new RemoveUnusedVars(
        compiler, removeGlobal, preserveFunctionExpressionNames, modifyCallSites);
  }

  @Test
  public void testRemoveUnusedGlobalVar() {
    test("var x = 1;", "");
  }

  @Test
  public void testKeepUnusedGlobalVarWhenRemoveGlobalsFalse() {
    removeGlobal = false;
    testSame("var x = 1;");
  }

  @Test
  public void testRemoveUnusedLocalVarInFunction() {
    test(
        "function f() { var x = 1; return 2; }",
        "function f() { return 2; }");
  }

  @Test
  public void testKeepUsedVar() {
    testSame("function f() { var x = 1; return x; }");
  }

  @Test
  public void testSelfAssignIsRemovable() {
    test(
        "function f() { var x = 1; x = 2; }",
        "function f() {}");
  }

  @Test
  public void testRemoveUnreferencedFunctionDeclaration() {
    test(
        "function f() {} function g() { return 1; } g();",
        "function g() { return 1; } g();");
  }

  @Test
  public void testKeepFunctionExpressionNameWhenPreserved() {
    preserveFunctionExpressionNames = true;
    testSame("var f = function g() {};");
  }

  @Test
  public void testNoOpWhenAllVarsUsed() {
    testSame("function f(a, b) { return a + b; } f(1, 2);");
  }

  @Test
  public void testEmptyScopeNoOp() {
    testSame("");
  }

  @Test
  public void testTransitiveRemovalOfVarOnlyUsedInUnusedFunction() {
    test(
        "function f() { var y = 1; return y; }",
        "");
  }

  @Test
  public void testRemoveUnusedVarInNestedBlock() {
    test(
        "function f() { if (true) { var x = 1; } return 2; }",
        "function f() { if (true) {} return 2; }");
  }
}
