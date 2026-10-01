// test/com/google/javascript/jscomp/RemoveUnusedVarsTest.java
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link RemoveUnusedVars}.
 */
public class RemoveUnusedVarsTest {
    private Compiler compiler;
    private RemoveUnusedVars removeUnusedVars;
    private Node root;
    private Node externs;

    @Before
    public void setUp() {
        compiler = new Compiler();
        root = IR.block();
        externs = IR.block();
    }

    @Test
    public void testGetFunctionArgList_withNoArgs() {
        Node function = IR.function(IR.name("f"), IR.paramList(), IR.block());
        Node argList = RemoveUnusedVars.getFunctionArgList(function);
        assertNotNull(argList);
        assertTrue(argList.isParamList());
        assertEquals(0, argList.getChildCount());
    }

    @Test
    public void testGetFunctionArgList_withSingleArg() {
        Node function = IR.function(
            IR.name("f"), IR.paramList(IR.name("a")), IR.block());
        Node argList = RemoveUnusedVars.getFunctionArgList(function);
        assertNotNull(argList);
        assertTrue(argList.isParamList());
        assertEquals(1, argList.getChildCount());
        assertEquals("a", argList.getFirstChild().getString());
    }

    @Test
    public void testGetFunctionArgList_withMultipleArgs() {
        Node function = IR.function(
            IR.name("f"),
            IR.paramList(IR.name("a"), IR.name("b"), IR.name("c")),
            IR.block());
        Node argList = RemoveUnusedVars.getFunctionArgList(function);
        assertNotNull(argList);
        assertEquals(3, argList.getChildCount());
    }

    @Test
    public void testProcess_withNullDefinitionFinder() {
        removeUnusedVars = new RemoveUnusedVars(compiler, false, false, false);
        removeUnusedVars.process(externs, root, null);
    }

    @Test
    public void testProcess_withDefinitionFinder() {
        removeUnusedVars = new RemoveUnusedVars(compiler, false, false, false);
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        defFinder.process(externs, root);
        removeUnusedVars.process(externs, root, defFinder);
    }

    @Test
    public void testProcess_withModifyCallSites() {
        removeUnusedVars = new RemoveUnusedVars(compiler, false, false, true);
        removeUnusedVars.process(externs, root);
    }

    @Test
    public void testCollectMaybeUnreferencedVars_withSimpleVar() {
        String js = "var a = 1;";
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a"), IR.number(1))));
        Scope scope = compiler.getTopScope();

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.collectMaybeUnreferencedVars(scope);
        Scope.Var var = scope.getVar("a");
        assertNotNull(var);
    }

    @Test
    public void testInterpretAssigns_withUnreferencedVar() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a"))));
        Scope scope = compiler.getTopScope();

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.collectMaybeUnreferencedVars(scope);
        removeUnusedVars.interpretAssigns();
    }

    @Test
    public void testMarkReferencedVar_startsWithEmptyReferenced() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a"))));
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        boolean result = removeUnusedVars.markReferencedVar(var);
        assertTrue(result);
    }

    @Test
    public void testMarkReferencedVar_alreadyReferenced() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a"))));
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.markReferencedVar(var);
        boolean result = removeUnusedVars.markReferencedVar(var);
        assertFalse(result);
    }

    @Test
    public void testIsRemovableVar_withUnreferencedVar() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a")), IR.var(IR.name("b"))));
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        boolean removable = removeUnusedVars.isRemovableVar(var);
        assertTrue(removable);
    }

    @Test
    public void testIsRemovableVar_withReferencedVar() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a")),
            IR.var(IR.name("b")),
            IR.exprResult(IR.name("a"))));
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.markReferencedVar(var);
        boolean removable = removeUnusedVars.isRemovableVar(var);
        assertFalse(removable);
    }

    @Test
    public void testIsRemovableVar_withGlobalAndRemoveGlobalsFalse() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a"))));
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, false, false, false);
        boolean removable = removeUnusedVars.isRemovableVar(var);
        assertFalse(removable);
    }

    @Test
    public void testIsRemovableVar_withExportedVar() {
        compiler.compile(IR.externs(IR.script()), IR.script(
            IR.var(IR.name("a"))));
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        compiler.getCodingConvention().addExportedName("a");
        boolean removable = removeUnusedVars.isRemovableVar(var);
        assertFalse(removable);
    }

    @Test
    public void testRemoveUnreferencedVars_removesUnreferencedVar() {
        Node script = IR.script(IR.var(IR.name("a")));
        compiler.compile(IR.externs(IR.script()), script);
        Scope scope = compiler.getTopScope();

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.collectMaybeUnreferencedVars(scope);
        removeUnusedVars.removeUnreferencedVars();
    }

    @Test
    public void testRemoveUnreferencedVars_preservesReferencedVar() {
        Node script = IR.script(
            IR.var(IR.name("a")),
            IR.exprResult(IR.name("a")));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        Var var = compiler.getTopScope().getVar("a");
        removeUnusedVars.markReferencedVar(var);
        removeUnusedVars.removeUnreferencedVars();
    }

    @Test
    public void testRemoveUnreferencedFunctionArgs_withRemoveGlobalsFalse() {
        Node script = IR.script(IR.function(
            IR.name("f"), IR.paramList(IR.name("a")),
            IR.block(IR.returnNode(IR.name("b")))));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, false, false, false);
        removeUnusedVars.traverseAndRemoveUnusedReferences(script);
    }

    @Test
    public void testTraverseFunction_callsCollectMaybeUnreferencedVars() {
        Node script = IR.script(IR.function(
            IR.name("f"), IR.paramList(IR.name("a")),
            IR.block(IR.exprResult(IR.name("a")))));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.traverseAndRemoveUnusedReferences(script);
    }

    @Test
    public void testTraverseNode_withFunctionDeclaration() {
        Node function = IR.function(
            IR.name("f"), IR.paramList(),
            IR.block());
        Node script = IR.script(function);
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        Scope scope = new SyntacticScopeCreator(compiler).createScope(script, null);
        removeUnusedVars.traverseNode(script, null, scope);
    }

    @Test
    public void testTraverseNode_withAssign() {
        Node script = IR.script(
            IR.var(IR.name("a")),
            IR.exprResult(IR.assign(IR.name("a"), IR.number(1))));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        Scope scope = new SyntacticScopeCreator(compiler).createScope(script, null);
        removeUnusedVars.traverseNode(script, null, scope);
    }

    @Test
    public void testTraverseNode_withNameReference() {
        Node script = IR.script(
            IR.var(IR.name("a")),
            IR.var(IR.name("b")),
            IR.exprResult(IR.name("a")));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        Scope scope = new SyntacticScopeCreator(compiler).createScope(script, null);
        removeUnusedVars.traverseNode(script, null, scope);
    }

    @Test
    public void testTraverseNode_withCallExpression() {
        Node script = IR.script(
            IR.exprResult(IR.call(IR.name("foo"))));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        Scope scope = new SyntacticScopeCreator(compiler).createScope(script, null);
        removeUnusedVars.traverseNode(script, null, scope);
    }

    @Test
    public void testRemoveAllAssigns_removesAssignNode() {
        Node script = IR.script(
            IR.var(IR.name("a")),
            IR.exprResult(IR.assign(IR.name("a"), IR.number(1))));
        compiler.compile(IR.externs(IR.script()), script);
        Var var = compiler.getTopScope().getVar("a");

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.traverseAndRemoveUnusedReferences(script);
        removeUnusedVars.removeAllAssigns(var);
    }

    @Test
    public void testTraverseAndRemoveUnusedReferences_removesUnusedGlobals() {
        Node script = IR.script(
            IR.var(IR.name("a")),
            IR.var(IR.name("b")));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.traverseAndRemoveUnusedReferences(script);
    }

    @Test
    public void testTraverseAndRemoveUnusedReferences_withNestedFunction() {
        Node script = IR.script(
            IR.function(
                IR.name("outer"),
                IR.paramList(),
                IR.block(
                    IR.function(
                        IR.name("inner"),
                        IR.paramList(),
                        IR.block())
                )
            )
        );
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.traverseAndRemoveUnusedReferences(script);
    }

    @Test
    public void testProcess_withMultipleTraversals() {
        Node script = IR.script(
            IR.var(IR.name("a")),
            IR.var(IR.name("b")),
            IR.exprResult(IR.assign(IR.name("a"), IR.name("b"))));
        compiler.compile(IR.externs(IR.script()), script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.process(externs, script);
    }

    @Test
    public void testProcess_withExternsAndRoot() {
        Node externsRoot = IR.externs(IR.script());
        Node script = IR.script(IR.var(IR.name("a")));
        compiler.compile(externsRoot, script);

        removeUnusedVars = new RemoveUnusedVars(compiler, true, false, false);
        removeUnusedVars.process(externsRoot, script);
    }
}
