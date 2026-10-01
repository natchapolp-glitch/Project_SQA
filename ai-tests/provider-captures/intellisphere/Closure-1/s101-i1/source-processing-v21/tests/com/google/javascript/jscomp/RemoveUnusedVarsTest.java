// src/com/google/javascript/jscomp/RemoveUnusedVarsTest.java
package com.google.javascript.jscomp;

import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link RemoveUnusedVars}.
 *
 * <p>This test class verifies the unreferenced variable removal behavior
 * of the RemoveUnusedVars compiler pass. It tests various scenarios including
 * basic variable removal, function removal, property assignment handling,
 * and interaction with exports and coding conventions.
 */
public class RemoveUnusedVarsTest {

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


  private Compiler compiler;
  private RemoveUnusedVars pass;

  // Flags for the RemoveUnusedVars constructor
  private static final boolean REMOVE_GLOBALS = true;
  private static final boolean PRESERVE_FUNCTION_EXPRESSION_NAMES = false;
  private static final boolean MODIFY_CALL_SITES = false;

  @Before
  public void setUp() {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    // Use ECMASCRIPT5 to support modern syntax
    options.setLanguageIn(LanguageMode.ECMASCRIPT5);
    compiler.initOptions(options);
    pass = new RemoveUnusedVars(
        compiler, REMOVE_GLOBALS, PRESERVE_FUNCTION_EXPRESSION_NAMES,
        MODIFY_CALL_SITES);
    // Set lifecycle stage to normalized as required by the pass.
    compiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
  }

  /**
   * Helper method that compiles the provided JavaScript source,
   * runs the RemoveUnusedVars pass, and returns the resulting source.
   */
  private String compile(String js) {
    return compile("", js);
  }

  /**
   * Helper method that compiles the provided externs and JavaScript source,
   * runs the RemoveUnusedVars pass, and returns the resulting source.
   */
  private String compile(String externs, String js) {
    runSelectedPass(compiler,
        SourceFile.fromCode("externs", externs),
        SourceFile.fromCode("input", js),
        pass);
    return compiler.toSource();
  }

  

  @Test
  public void testRemovesUnusedFunction() {
    // An unused function declaration should be removed.
    String result = compile(
        "function unused() { return 1; } function used() { return 2; } used();");
    assertTrue("Used function should remain",
        result.contains("function used"));
    assertFalse("Unused function should be removed",
        result.contains("function unused"));
  }

  

  @Test
  public void testRemovesUnusedVariableInGlobalScope() {
    // Global unused variable should be removed when removeGlobals is true.
    String result = compile(
        "var x = 1; var y = 2; y;");
    assertFalse("Unused global var x should be removed",
        result.contains("var x"));
    assertTrue("Used global var y should remain",
        result.contains("y"));
  }

  

  @Test
  public void testRemovesUnreferencedFunctionArgs() {
    // Unused function parameters should be removed.
    String result = compile(
        "function foo(a, b, c) { return a; } foo(1, 2, 3);");
    assertTrue("Used parameter a should remain",
        result.contains("a"));
    assertFalse("Unused parameter b should be removed",
        result.matches("(?s).*function foo\\(a,.*b.*\\).*"));
    assertFalse("Unused parameter c should be removed",
        result.matches("(?s).*function foo\\(a,.*c.*\\).*"));
  }

  @Test
  public void testKeepsAllUsedFunctionArgs() {
    // All function parameters are used; none should be removed.
    String result = compile(
        "function foo(a, b) { return a + b; } foo(1, 2);");
    assertTrue("Parameter a should remain",
        result.contains("a"));
    assertTrue("Parameter b should remain",
        result.contains("b"));
  }

  

  

  

  @Test
  public void testPreservesFunctionExpressionNameWhenFlagSet() {
    // When preserveFunctionExpressionNames is true, the name of a
    // function expression should be preserved even if unreferenced.
    Compiler localCompiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT5);
    localCompiler.initOptions(options);
    localCompiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    RemoveUnusedVars localPass = new RemoveUnusedVars(
        localCompiler, REMOVE_GLOBALS, true, MODIFY_CALL_SITES);
    runSelectedPass(localCompiler,
        SourceFile.fromCode("externs", ""),
        SourceFile.fromCode("input",
            "(function() { var f = function myFunc() {}; })"),
        localPass);
    String result = localCompiler.toSource();
    // Note: If f is unused, it could be removed entirely.
    // Testing the flag would require a scenario where the name is preserved.
    // Let's test a declared but unused function expression.
    localCompiler = new Compiler();
    localCompiler.initOptions(options);
    localCompiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    localPass = new RemoveUnusedVars(
        localCompiler, REMOVE_GLOBALS, true, MODIFY_CALL_SITES);
    runSelectedPass(localCompiler,
        SourceFile.fromCode("externs", ""),
        SourceFile.fromCode("input",
            "var f = function myFunc() { return 1; }; f();"),
        localPass);
    result = localCompiler.toSource();
    assertTrue("Function expression name myFunc should be preserved",
        result.contains("myFunc"));
  }

  

  

  @Test
  public void testRemovesUnusedFunctionWithRemovableVar() {
    // A function declaration assigned to a variable that is unused
    // should be removed.
    String result = compile(
        "var f = function() { return 1; };");
    assertFalse("Unused function expression should be removed",
        result.contains("function"));
  }

  @Test
  public void testKeepsCallSiteForUsedFunction() {
    // A call to a user function should remain.
    String result = compile(
        "function foo() { return 1; } foo();");
    assertTrue("Function foo should remain",
        result.contains("foo"));
    assertTrue("Call to foo should remain",
        result.contains("foo()"));
  }

  @Test
  public void testRemovesClassDefiningCallsWhenVarUnused() {
    // goog.inherits and similar calls should be removed if the subclass
    // variable is unused.
    String result = compile(
        "var x = function() {}; goog.inherits(x, Object);");
    assertFalse("Unused subclass x and its goog.inherits should be removed",
        result.contains("goog.inherits"));
  }

  @Test
  public void testKeepsClassDefiningCallsWhenVarUsed() {
    // goog.inherits should be kept if the subclass variable is used.
    String result = compile(
        "var x = function() {}; goog.inherits(x, Object); x();");
    assertTrue("Used subclass should remain",
        result.contains("x"));
  }

  @Test
  public void testArgumentsDoesNotCauseParamRemoval() {
    // If 'arguments' is used inside a function, the parameters should not
    // be removed.
    String result = compile(
        "function foo(a, b) { return arguments[0](undefined); } foo(1, 2);");
    assertTrue("Parameters should remain when arguments is used",
        result.contains("a") && result.contains("b"));
  }

  

  

  

  @Test
  public void testGetFunctionArgListReturnsCorrectNode() {
    // Test getFunctionArgList static method.
    Node function = IR.function(
        IR.name("test"),
        IR.paramList(IR.name("a"), IR.name("b")),
        IR.block());
    Node argList = invokeOriginalFunctionArgList(function);
    assertNotNull("Argument list should not be null", argList);
    assertTrue("Argument list should be LP node",
        argList.isParamList());
    assertEquals("Should have two params",
        2, argList.getChildCount());
    assertEquals("First param should be 'a'",
        "a", argList.getFirstChild().getString());
    assertEquals("Second param should be 'b'",
        "b", argList.getLastChild().getString());
  }

  @Test
  public void testGetFunctionArgListWithNoParams() {
    // Test getFunctionArgList with no parameters.
    Node function = IR.function(
        IR.name("empty"),
        IR.paramList(),
        IR.block());
    Node argList = invokeOriginalFunctionArgList(function);
    assertNotNull("Argument list should not be null", argList);
    assertTrue("Argument list should be LP node",
        argList.isParamList());
    assertNull("Should have no children",
        argList.getFirstChild());
  }

  

  @Test
  public void testProcessHandlesEmptyScript() {
    // Process an empty script; should not throw exception.
    String result = compile("");
    assertNotNull("Result should not be null for empty script", result);
  }

  @Test
  public void testProcessHandlesNestedFunctionScopes() {
    // Nested function scopes should be handled correctly.
    String result = compile(
        "function outer() {" +
        "  var a = 1;" +
        "  function inner() {" +
        "    var b = 2;" +
        "    return b;" +
        "  }" +
        "  return inner() + a;" +
        "}" +
        "outer();");
    assertTrue("Outer var a should be preserved",
        result.contains("a"));
    assertTrue("Inner var b should be preserved",
        result.contains("b"));
    assertTrue("Outer function should be preserved",
        result.contains("function outer"));
    assertTrue("Inner function should be preserved",
        result.contains("function inner"));
  }

  @Test
  public void testRemoveUnreferencedVarsHandlesAllDeclTypes() {
    // RemoveUnreferencedVars internally handles var, function,
    // and paramList declarations. This tests covers the var and function
    // removal paths.
    String result = compile(
        "var a = 1; function b() { return 2; } var c = 3; c;");
    // a is unused; b is unused; c is used.
    assertFalse("Unused var a should be removed",
        result.contains("var a"));
    assertFalse("Unused function b should be removed",
        result.contains("function b"));
    assertTrue("Used var c should remain",
        result.contains("c"));
  }

  

  @Test
  public void testNoRemovalWhenRemoveGlobalsFalse() {
    // When removeGlobals is false, global variables should not be removed.
    Compiler localCompiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT5);
    localCompiler.initOptions(options);
    localCompiler.setLifeCycleStage(AbstractCompiler.LifeCycleStage.NORMALIZED);
    RemoveUnusedVars localPass = new RemoveUnusedVars(
        localCompiler, false, PRESERVE_FUNCTION_EXPRESSION_NAMES,
        MODIFY_CALL_SITES);
    runSelectedPass(localCompiler,
        SourceFile.fromCode("externs", ""),
        SourceFile.fromCode("input", "var x = 1;"),
        localPass);
    String result = localCompiler.toSource();
    assertTrue("Global var x should remain when removeGlobals is false",
        result.contains("x"));
  }
}
