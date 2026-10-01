var a = 1;Removed completely.root.getChildCount() == 0`

Case 2: testUsedVar var a = 1; alert(a); Kept. root.getChildCount() == 2

Case 3: testUnusedVarWithSideEffects var a = foo(); a is unreferenced, but foo() has side effects. Declaration removed, but foo() expression result remains. root.getChildCount() == 1 assertTrue(root.getFirstChild().isExprResult()) assertTrue(root.getFirstChild().getFirstChild().isCall())

Case 4: testMultipleVarsInDeclaration var a = 1, b = 2; alert(b); a removed, b retained. root.getChildCount() == 1 (the VAR node) + 1 (the EXPR_RESULT for alert). Total 2. Node varNode = root.getFirstChild(); assertTrue(varNode.isVar()); assertEquals(1, varNode.getChildCount()); assertEquals("b", varNode.getFirstChild().getString());

Case 5: testRemoveGlobalsFalse var a = 1; with removeGlobals = false. root.getChildCount() == 1 assertTrue(root.getFirstChild().isVar()); assertEquals("a", root.getFirstChild().getFirstChild().getString());

Case 6: testRemoveGlobalsFalseInsideFunction function f() { var x = 1; } f(); with removeGlobals = false. f is called, so f is not removed. Inside f, x is