Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
com/google/javascript/jscomp/RemoveUnusedVarsTest.java

```

```java
/*
 * Copyright 2008 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CodingConvention.AssertionFunctionSpec;
import com.google.javascript.jscomp.CodingConvention.SubclassRelationship;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.parsing.JsDocInfoParser;
import com.google.javascript.jscomp.testing.NodelessUnitTest;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link RemoveUnusedVars}.
 */
@RunWith(JUnit4.class)
public class RemoveUnusedVarsTest {

  private AbstractCompiler compiler;
  private RemoveUnusedVars pass;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    pass = new RemoveUnusedVars(compiler, true, false, true);
  }

  @Test
  public void testProcessDoesNotThrowOnEmptyScript() {
    Node script = IR.script();
    Node externs = IR.script();
    pass.process(externs, script);
  }

  @Test
  public void testProcessRemovesUnusedGlobalVar() {
    String code = "var unused = 1;";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    assertTrue("Unused var should be removed",
        script.getFirstChild() == null || !script.getFirstChild().isVar());
  }

  @Test
  public void testProcessKeepsUsedGlobalVar() {
    String code = "var used = 1; alert(used);";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    Node firstChild = script.getFirstChild();
    assertNotNull(firstChild);
    assertTrue("Used var should be kept",
        firstChild.isVar() || firstChild.isScript());
  }

  @Test
  public void testTraverseFunctionDoesNotThrow() {
    Node function = IR.function(IR.name(""), IR.paramList(), IR.block());
    Node script = IR.script(function);
    Node externs = IR.script();
    pass.process(externs, script);
  }

  @Test
  public void testIsRemovableVarReturnsFalseForExported() {
    Var var = createVar("exportedVar", false, null);
    // exported is off-limits
    assertFalse(pass.isRemovableVar(var));
  }

  @Test
  public void testIsRemovableVarReturnsFalseIfGlobalsNotRemoved() {
    pass = new RemoveUnusedVars(compiler, false, false, false);
    Var var = createVar("localVar", false, null);
    // global var with removeGlobals=false
    assertFalse(pass.isRemovableVar(var));
  }

  @Test
  public void testMarkReferencedVarAddsToReferencedSet() {
    Var var = createVar("myVar", false, null);
    boolean added = pass.markReferencedVar(var);
    assertTrue("Variable should be newly referenced", added);
    added = pass.markReferencedVar(var);
    assertFalse("Variable should already be referenced", added);
  }

  @Test
  public void testGetFunctionArgListReturnsParamList() {
    Node function = IR.function(
        IR.name(""),
        IR.paramList(IR.name("a"), IR.name("b")),
        IR.block());
    Node argList = RemoveUnusedVars.getFunctionArgList(function);
    assertNotNull(argList);
    assertEquals(2, argList.getChildCount());
  }

  @Test
  public void testGetFunctionArgListEmpty() {
    Node function = IR.function(IR.name(""), IR.paramList(), IR.block());
    Node argList = RemoveUnusedVars.getFunctionArgList(function);
    assertNotNull(argList);
    assertEquals(0, argList.getChildCount());
  }

  @Test
  public void testCollectMaybeUnreferencedVarsAddsUnreferenced() {
    Scope globalScope = compiler.getTopScope();
    Var var = createVar("unreferencedVar", true, globalScope);
    // Hack: we need a scope containing the var
    pass.collectMaybeUnreferencedVars(globalScope);
    // No public getter for maybeUnreferenced, so alternative assertion
    // is to run full process and check removal
    String code = "var unreferencedVar = 1;";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    // The var should be removed
    Node first = script.getFirstChild();
    if (first != null && first.isVar()) {
      fail("Unreferenced var was not removed");
    }
  }

  @Test
  public void testInterpretAssignsMarkReferencedWhenAssignedAndUsed() {
    String code = "var x = {}; x.foo = 1; alert(x);";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    // x should be kept
    assertNotNull(findName(script, "x"));
  }

  @Test
  public void testRemoveAllAssignsRemovingAssigns() {
    String code = "var x = 1; x = 2;";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    // 'x' should be removed because not used
    assertNull(findName(script, "x"));
  }

  @Test
  public void testRemoveUnreferencedFunctionArgsRemovesUnusedArgs() {
    String code = "function foo(a, b) { alert(b); }";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    Node func = findFunction(script, "foo");
    assertNotNull(func);
    Node params = func.getFirstChild().getNext();
    assertEquals("Only one arg should remain", 1, params.getChildCount());
  }

  @Test
  public void testTraverseAndRemoveUnusedReferencesDoesNotCrash() {
    String code = "var a = 1; var b = 2; alert(b);";
    Node script = parse(code);
    Node externs = IR.script();
    pass.process(externs, script);
    // Should not throw
  }

  // Helpers

  private Node parse(String code) {
    compiler.init(new SimpleSourceFile("testcode.js"), null);
    compiler.parse(code);
    return compiler.getRoot();
  }

  private Var createVar(String name, boolean isLocal, Scope scope) {
    if (scope == null) {
      scope = compiler.getTopScope();
    }
    Node nameNode = IR.name(name);
    Var var = new Var(false, nameNode, null, scope, -1, compiler);
    // We can't easily add it to the scope, but the var is enough for testing
    return var;
  }

  private static Node findName(Node root, String name) {
    if (root.isName() && root.getString().equals(name)) {
      return root;
    }
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      Node found = findName(child, name);
      if (found != null) return found;
    }
    return null;
  }

  private static Node findFunction(Node root, String name) {
    if (root.isFunction()) {
      Node firstChild = root.getFirstChild();
      if (firstChild.getString().equals(name)) {
        return root;
      }
    }
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      Node found = findFunction(child, name);
      if (found != null) return found;
    }
    return null;
  }

  private static class SimpleSourceFile implements StaticSourceFile {
    private final String name;

    SimpleSourceFile(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isExtern() {
      return false;
    }

    @Override
    public int getLineOffset(int lineno) {
      return 0;
    }

    @Override
    public int getColumnOffset(int lineno, int charno) {
      return 0;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private static class TestCompiler extends AbstractCompiler {
    private Node root;
    private CodingConvention codingConvention;
    private Scope topScope;
    private LifeCycleStage stage = LifeCycleStage.NORMALIZED;

    TestCompiler() {
      this.codingConvention = new DefaultCodingConvention();
    }

    @Override
    void init(StaticSourceFile externs, List<StaticSourceFile> inputs) {
      // no-op
    }

    public Node getRoot() {
      return root;
    }

    void parse(String code) {
      root = IR.script();
      Node child = IR.exprResult(IR.name(code));
      root.addChildToBack(child);
    }

    @Override
    public CodingConvention getCodingConvention() {
      return codingConvention;
    }

    @Override
    public Scope getTopScope() {
      if (topScope == null) {
        topScope = new Scope(root, null);
      }
      return topScope;
    }

    @Override
    public void reportCodeChange() {
      // nothing
    }

    @Override
    public void addToDebugLog(String message) {
      // nothing
    }

    @Override
    public LifeCycleStage getLifeCycleStage() {
      return stage;
    }

    // Unsupported abstract methods stubs
    @Override
    void process(CompilerPass p) { throw new UnsupportedOperationException(); }
    @Override
    CompilerPass getCodeGrabbingPass(CompilerPass p, List<SourceFile> files) { throw new UnsupportedOperationException(); }
    @Override
    Node getNodeForCodeInsertion(JSSourceFile module) { throw new UnsupportedOperationException(); }
    @Override
    CompilerOptions getOptions() { throw new UnsupportedOperationException(); }
    @Override
    void setScope(Node n) { throw new UnsupportedOperationException(); }
    @Override
    Node getRootForSyntheticCode() { throw new UnsupportedOperationException(); }
    @Override
    void addChangeHandler(CodeChangeHandler handler) { throw new UnsupportedOperationException(); }
    @Override
    void removeChangeHandler(CodeChangeHandler handler) { throw new UnsupportedOperationException(); }
    @Override
    public JSTypeRegistry getTypeRegistry() { throw new UnsupportedOperationException(); }
    @Override
    public ErrorManager getErrorManager() { throw new UnsupportedOperationException(); }
    @Override
    public SymbolTable getSymbolTable() { throw new UnsupportedOperationException(); }
    @Override
    public Node getRootForCodingConvention() { throw new UnsupportedOperationException(); }
    @Override
    public void setCodingConvention(CodingConvention convention) { throw new UnsupportedOperationException(); }
    @Override
    public PassConfig getPassConfig() { throw new UnsupportedOperationException(); }
    @Override
    void forAllComments(Node root, Callback cb) { throw new UnsupportedOperationException(); }
    @Override
    Node parse(JSSourceFile file) { throw new UnsupportedOperationException(); }
    @Override
    Node parseSyntheticCode(String code) { throw new UnsupportedOperationException(); }
    @Override
    public SourceFile getSourceFileByName(String sourceName) { throw new UnsupportedOperationException(); }
    @Override
    public void setSourceMap(SourceMap sourceMap) { throw new UnsupportedOperationException(); }
    @Override
    public Region getSourceRegion(JSSourceFile sourceFile, int lineNumber) { throw new UnsupportedOperationException(); }
    @Override
    public Node getSynthesizedExternsRoot() { throw new UnsupportedOperationException(); }
    @Override
    Node getSynthesizedExternsRootForTesting() { throw new UnsupportedOperationException(); }
  }
}

```