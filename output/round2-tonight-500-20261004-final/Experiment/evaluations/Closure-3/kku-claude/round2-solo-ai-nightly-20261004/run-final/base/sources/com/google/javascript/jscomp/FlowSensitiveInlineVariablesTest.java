package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import org.junit.Test;

public class FlowSensitiveInlineVariablesTest extends CompilerTestCase {

  public FlowSensitiveInlineVariablesTest() {
    super();
  }

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return new FlowSensitiveInlineVariables(compiler);
  }

  @Override
  protected int getNumRepetitions() {
    return 1;
  }

  @Test
  public void testBasicInline() {
    test(
        "function f() { var x = 1; return x; }",
        "function f() { var x = 1; return 1; }");
  }

  @Test
  public void testNoInlineWithMultipleUses() {
    testSame(
        "function f() { var x = foo(); bar(x); baz(x); }");
  }

  @Test
  public void testNoInlineAcrossSideEffectingCall() {
    testSame(
        "function f() { var x = foo(); bar(); use(x); }");
  }

  @Test
  public void testNoInlineWithMultipleReachingDefs() {
    testSame(
        "function f(c) { var x; if (c) { x = 1; } else { x = 2; } return x; }");
  }

  @Test
  public void testNoInlineSelfReferencingAssignment() {
    testSame(
        "function f() { var x = 1; x = x + 1; return x; }");
  }

  @Test
  public void testNoChangeWhenNoCandidates() {
    testSame(
        "function f() { var x = 1; var y = 2; return x + y; }");
  }

  @Test
  public void testGlobalScopeNotInlined() {
    testSame("var x = 1; use(x);");
  }
}
