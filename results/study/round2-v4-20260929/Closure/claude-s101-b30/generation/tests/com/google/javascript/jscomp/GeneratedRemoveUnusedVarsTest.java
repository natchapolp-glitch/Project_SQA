package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.lang.reflect.Method;

/**
 * Generated regression tests for {@link RemoveUnusedVars}.
 */
public class GeneratedRemoveUnusedVarsTest extends CompilerTestCase {

  private boolean removeGlobals;
  private boolean preserveFunctionExpressionNames;
  private boolean modifyCallSites;

  public GeneratedRemoveUnusedVarsTest() {
    super("function alert() {}");
    enableNormalize();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    removeGlobals = true;
    preserveFunctionExpressionNames = false;
    modifyCallSites = false;
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new RemoveUnusedVars(
        compiler, removeGlobals, preserveFunctionExpressionNames,
        modifyCallSites);
  }

  // ---------------------------------------------------------------
  // removeGlobals / isRemovableVar / collectMaybeUnreferencedVars
  // ---------------------------------------------------------------

  public void testGlobalVarKeptWhenRemoveGlobalsFalse() {
    removeGlobals = false;
    testSame("var a = 1");
  }

  public void testGlobalVarRemovedWhenRemoveGlobalsTrue() {
    removeGlobals = true;
    test("var a = 1", "");
  }

  public void testGlobalFunctionKeptWhenRemoveGlobalsFalse() {
    removeGlobals = false;
    testSame("function f() {}");
  }

  public void testUnreferencedGlobalFunctionRemoved() {
    test("function f() {}", "");
  }

  public void testReferencedGlobalFunctionKept() {
    testSame("function f() {} f()");
  }

  public void testLocalVarRemovedEvenWhenRemoveGlobalsFalse() {
    removeGlobals = false;
    test("function f() { var x = 1; } f()", "function f() {} f()");
  }

  public void testUnreferencedFunctionCallingOtherFunctionBothRemoved() {
    test("function f() { g(); } function g() {}", "");
  }

  public void testNestedUnusedLocalFunctionRemoved() {
    test("function f() { function g() {} } f()", "function f() {} f()");
  }

  // ---------------------------------------------------------------
  // traverseNode / markReferencedVar / continuations
  // ---------------------------------------------------------------

  public void testChainOfUnreferencedVarsRemoved() {
    test("var a = 1; var b = a;", "");
  }

  public void testChainOfReferencedVarsKept() {
    testSame("var a = 1; var b = a; alert(b)");
  }

  public void testVarWithSideEffectInitializerKeepsExpression() {
    test("var x = alert(1)", "alert(1)");
  }

  public void testForInVariableLeftAlone() {
    testSame("for (var i in {}) {}");
  }

  // ---------------------------------------------------------------
  // interpretAssigns / removeAllAssigns
  // ---------------------------------------------------------------

  public void testAssignOnlyVarRemoved() {
    test("var a; a = 1", "");
  }

  public void testPropertyAssignOnLiteralRemoved() {
    test("var a = {}; a.b = 1", "");
  }

  public void testPropertyAssignOnUnknownValueKept() {
    testSame("var a = alert(); a.b = 1");
  }

  public void testAssignWithSideEffectRhsKeepsRhs() {
    test("var a; a = alert(1)", "alert(1)");
  }

  // ---------------------------------------------------------------
  // removeUnreferencedFunctionArgs / getFunctionArgList
  // ---------------------------------------------------------------

  public void testTrailingUnreferencedArgsRemoved() {
    test("var foo = function(a, b, c) {}; foo",
         "var foo = function() {}; foo");
  }

  public void testMiddleReferencedArgKeepsEarlierArgs() {
    test("var foo = function(a, b, c) { alert(b) }; foo",
         "var foo = function(a, b) { alert(b) }; foo");
  }

  public void testArgsNotRemovedWhenRemoveGlobalsFalse() {
    removeGlobals = false;
    testSame("function f(a, b) {}");
  }

  public void testArgumentsKeywordKeepsAllParams() {
    testSame("var f = function(a, b) { return arguments }; f");
  }

  public void testFunctionExpressionNameRemoved() {
    test("var f = function g() {}; f", "var f = function() {}; f");
  }

  public void testFunctionExpressionNamePreserved() {
    preserveFunctionExpressionNames = true;
    testSame("var f = function g() {}; f");
  }

  public void testGetFunctionArgListReturnsParamList() throws Exception {
    Node param = Node.newString(Token.NAME, "a");
    Node lp = new Node(Token.LP, param);
    Node function = new Node(
        Token.FUNCTION, Node.newString(Token.NAME, "f"), lp,
        new Node(Token.BLOCK));

    Method m = RemoveUnusedVars.class.getDeclaredMethod(
        "getFunctionArgList", Node.class);
    m.setAccessible(true);
    Object result = m.invoke(null, function);

    assertSame(lp, result);
  }

  // ---------------------------------------------------------------
  // process(...) with call-site modification
  // ---------------------------------------------------------------

  public void testCallSiteArgumentRemovedWithSignature() {
    modifyCallSites = true;
    test("function f(a) {} f(1)", "function f() {} f()");
  }

  public void testProcessRequiresNormalizedAst() {
    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    try {
      pass.process(new Node(Token.BLOCK), new Node(Token.BLOCK));
      fail("Expected IllegalStateException for non-normalized compiler");
    } catch (IllegalStateException expected) {
      // expected
    }
  }

  public void testProcessWithNullDefinitionFinderFailsWhenModifyingCallSites() {
    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, true);
    try {
      pass.process(new Node(Token.BLOCK), new Node(Token.BLOCK), null);
      fail("Expected NullPointerException for null SimpleDefinitionFinder");
    } catch (NullPointerException expected) {
      // expected
    }
  }
}
