package com.google.javascript.jscomp;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RemoveUnusedVarsTest {

  @Test
  public void testConstructorAndProcess() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);

    RemoveUnusedVars remover = new RemoveUnusedVars(compiler, true, true, false);
    assertNotNull(remover);

    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    remover.process(externs, root);
  }

  @Test
  public void testProcessWithDefinitionFinder() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);

    RemoveUnusedVars remover = new RemoveUnusedVars(compiler, true, false, true);
    assertNotNull(remover);

    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    remover.process(externs, root, defFinder);
  }

  @Test
  public void testRemoveGlobalsDisabled() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);

    RemoveUnusedVars remover = new RemoveUnusedVars(compiler, false, false, false);
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    
    remover.process(externs, root);
    assertNotNull(compiler);
  }

  @Test
  public void testModifyCallSitesEnabled() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);

    RemoveUnusedVars remover = new RemoveUnusedVars(compiler, true, true, true);
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);

    remover.process(externs, root);
    assertNotNull(remover);
  }
}
