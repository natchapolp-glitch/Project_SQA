package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import junit.framework.TestCase;

public class RemoveUnusedVarsRegressionTest extends TestCase {

  private Compiler createCompiler() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setCodingConvention(new ClosureCodingConvention());
    compiler.initOptions(options);
    return compiler;
  }

  private Node parseAndNormalize(Compiler compiler, String js) {
    Node root = compiler.parseTestCode(js);
    assertEquals(0, compiler.getErrorCount());
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    return root;
  }

  public void testLifeCycleStageCheckThrowsWhenNotNormalized() {
    Compiler compiler = createCompiler();
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode("var a = 1;");
    // Do not set LifeCycleStage to NORMALIZED (stage remains RAW)
    RemoveUnusedVars pass = new RemoveUnusedVars(compiler, true, false, false);
    try {
      pass.process(externs, root);
      fail("Expected IllegalStateException when lifeCycleStage is not normalized");
    } catch (IllegalStateException expected) {
      // Success
    }
  }

  public void testProcessThrowsNullPointerWhenDefFinderNullAndModifyCallSites() {
    Compiler
