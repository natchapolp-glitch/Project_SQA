# Defects4J unit test generation

You are a Java unit testing engineer. Write deterministic regression tests for the
production classes supplied below, using their documented and fixed-reference
behavior. This experiment measures test generation from fixed production source.

Produce Java test sources with assertions. Exercise normal cases, boundaries,
invalid inputs, exception paths and branches supported by the supplied source.
Inspect the supplied build configuration and use the JUnit version and dependencies
already available to this project. Defects4J projects can have different build
systems and JUnit versions; do not assume Maven or JUnit 5. Use Java 11 compatible
syntax unless the build configuration requires an older source level.

Constraints:

- Do not change production code or build files and do not add dependencies.
- Use the reference behavior to derive assertions; do not invent unsupported APIs.
- Avoid network access, external programs, wall-clock timing, random values without
  a fixed seed, machine-specific paths and environment-dependent assertions.
- Use test class names ending in `Test`, correctly matching Java file and package
  names. Place each source at its package-relative path, such as
  `org/example/GeneratedExampleTest.java`.
- Keep tests independent. Restore global state that a test changes.
- Do not ask for a bug patch, buggy revision, existing detecting test, or hidden
  evaluation results. Only the supplied reference source and build information may
  guide the initial generation.

Return each Java file in a separate fenced Java code block, preceded by its
package-relative path. Include complete imports and test class definitions. If the
context is insufficient to write a compiling test, explicitly state the missing
API or dependency rather than producing a fabricated result.

## Experiment context

Project: Closure
Fixed reference revision: 1f
Target classes:
- com.google.javascript.jscomp.RemoveUnusedVars

## Eligible shared API declarations

Target these declarations, which exist on both evaluation revisions. Only declaration signatures were checked; no buggy behavior was supplied. Generate at most 30 independent test methods per response.

```json
[
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "isRemovableVar",
    "parameter_types": "com.google.javascript.jscomp.Scope$Var",
    "dimensions": 15
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "markReferencedVar",
    "parameter_types": "com.google.javascript.jscomp.Scope$Var",
    "dimensions": 15
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "",
    "method": "getFunctionArgList",
    "parameter_types": "com.google.javascript.rhino.Node",
    "dimensions": 3
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "collectMaybeUnreferencedVars",
    "parameter_types": "com.google.javascript.jscomp.Scope",
    "dimensions": 15
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "interpretAssigns",
    "parameter_types": "",
    "dimensions": 12
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "removeAllAssigns",
    "parameter_types": "com.google.javascript.jscomp.Scope$Var",
    "dimensions": 15
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "removeUnreferencedFunctionArgs",
    "parameter_types": "com.google.javascript.jscomp.Scope",
    "dimensions": 15
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "removeUnreferencedVars",
    "parameter_types": "",
    "dimensions": 12
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "traverseAndRemoveUnusedReferences",
    "parameter_types": "com.google.javascript.rhino.Node",
    "dimensions": 15
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "traverseFunction",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope",
    "dimensions": 18
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "traverseNode",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope",
    "dimensions": 21
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "process",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node",
    "dimensions": 18
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "process",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder",
    "dimensions": 21
  },
  {
    "class": "com.google.javascript.jscomp.RemoveUnusedVars",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 12
  }
]
```

## Production source src/com/google/javascript/jscomp/RemoveUnusedVars.java

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

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CodingConvention.SubclassRelationship;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;

/**
 * Garbage collection for variable and function definitions. Basically performs
 * a mark-and-sweep type algorithm over the JavaScript parse tree.
 *
 * For each scope:
 * (1) Scan the variable/function declarations at that scope.
 * (2) Traverse the scope for references, marking all referenced variables.
 *     Unlike other compiler passes, this is a pre-order traversal, not a
 *     post-order traversal.
 * (3) If the traversal encounters an assign without other side-effects,
 *     create a continuation. Continue the continuation iff the assigned
 *     variable is referenced.
 * (4) When the traversal completes, remove all unreferenced variables.
 *
 * If it makes it easier, you can think of the continuations of the traversal
 * as a reference graph. Each continuation represents a set of edges, where the
 * source node is a known variable, and the destination nodes are lazily
 * evaluated when the continuation is executed.
 *
 * This algorithm is similar to the algorithm used by {@code SmartNameRemoval}.
 * {@code SmartNameRemoval} maintains an explicit graph of dependencies
 * between global symbols. However, {@code SmartNameRemoval} cannot handle
 * non-trivial edges in the reference graph ("A is referenced iff both B and C
 * are referenced"), or local variables. {@code SmartNameRemoval} is also
 * substantially more complicated because it tries to handle namespaces
 * (which is largely unnecessary in the presence of {@code CollapseProperties}.
 *
 * This pass also uses a more complex analysis of assignments, where
 * an assignment to a variable or a property of that variable does not
 * necessarily count as a reference to that variable, unless we can prove
 * that it modifies external state. This is similar to
 * {@code FlowSensitiveInlineVariables}, except that it works for variables
 * used across scopes.
 *
 */
class RemoveUnusedVars
    implements CompilerPass, OptimizeCalls.CallGraphCompilerPass {

  private final AbstractCompiler compiler;

  private final CodingConvention codingConvention;

  private final boolean removeGlobals;

  private boolean preserveFunctionExpressionNames;

  /**
   * Keep track of variables that we've referenced.
   */
  private final Set<Var> referenced = Sets.newHashSet();

  /**
   * Keep track of variables that might be unreferenced.
   */
  private final List<Var> maybeUnreferenced = Lists.newArrayList();

  /**
   * Keep track of scopes that we've traversed.
   */
  private final List<Scope> allFunctionScopes = Lists.newArrayList();

  /**
   * Keep track of assigns to variables that we haven't referenced.
   */
  private final Multimap<Var, Assign> assignsByVar =
      ArrayListMultimap.create();

  /**
   * The assigns, indexed by the NAME node that they assign to.
   */
  private final Map<Node, Assign> assignsByNode = Maps.newHashMap();

  /**
   * Subclass name -> class-defining call EXPR node. (like inherits)
   */
  private final Multimap<Var, Node> classDefiningCalls =
      ArrayListMultimap.create();

  /**
   * Keep track of continuations that are finished iff the variable they're
   * indexed by is referenced.
   */
  private final Multimap<Var, Continuation> continuations =
      ArrayListMultimap.create();

  private boolean modifyCallSites;

  private CallSiteOptimizer callSiteOptimizer;

  RemoveUnusedVars(
      AbstractCompiler compiler,
      boolean removeGlobals,
      boolean preserveFunctionExpressionNames,
      boolean modifyCallSites) {
    this.compiler = compiler;
    this.codingConvention = compiler.getCodingConvention();
    this.removeGlobals = removeGlobals;
    this.preserveFunctionExpressionNames = preserveFunctionExpressionNames;
    this.modifyCallSites = modifyCallSites;
  }

  /**
   * Traverses the root, removing all unused variables. Multiple traversals
   * may occur to ensure all unused variables are removed.
   */
  @Override
  public void process(Node externs, Node root) {
    Preconditions.checkState(compiler.getLifeCycleStage().isNormalized());
    SimpleDefinitionFinder defFinder = null;

    if (modifyCallSites) {
      // For testing, allow the SimpleDefinitionFinder to be build now.
      defFinder = new SimpleDefinitionFinder(compiler);
      defFinder.process(externs, root);
    }
    process(externs, root, defFinder);
  }

  @Override
  public void process(
      Node externs, Node root, SimpleDefinitionFinder defFinder) {
    if (modifyCallSites) {
      Preconditions.checkNotNull(defFinder);
      callSiteOptimizer = new CallSiteOptimizer(compiler, defFinder);
    }
    traverseAndRemoveUnusedReferences(root);
    if (callSiteOptimizer != null) {
      callSiteOptimizer.applyChanges();
    }
  }

  /**
   * Traverses a node recursively. Call this once per pass.
   */
  private void traverseAndRemoveUnusedReferences(Node root) {
    Scope scope = new SyntacticScopeCreator(compiler).createScope(root, null);
    traverseNode(root, null, scope);

    if (removeGlobals) {
      collectMaybeUnreferencedVars(scope);
    }

    interpretAssigns();
    removeUnreferencedVars();
    for (Scope fnScope : allFunctionScopes) {
      removeUnreferencedFunctionArgs(fnScope);
    }
  }

  /**
   * Traverses everything in the current scope and marks variables that
   * are referenced.
   *
   * During traversal, we identify subtrees that will only be
   * referenced if their enclosing variables are referenced. Instead of
   * traversing those subtrees, we create a continuation for them,
   * and traverse them lazily.
   */
  private void traverseNode(Node n, Node parent, Scope scope) {
    int type = n.getType();
    Var var = null;
    switch (type) {
      case Token.FUNCTION:
        // If this function is a removable var, then create a continuation
        // for it instead of traversing immediately.
        if (NodeUtil.isFunctionDeclaration(n)) {
          var = scope.getVar(n.getFirstChild().getString());
        }

        if (var != null && isRemovableVar(var)) {
          continuations.put(var, new Continuation(n, scope));
        } else {
          traverseFunction(n, scope);
        }
        return;

      case Token.ASSIGN:
        Assign maybeAssign = Assign.maybeCreateAssign(n);
        if (maybeAssign != null) {
          // Put this in the assign map. It might count as a reference,
          // but we won't know that until we have an index of all assigns.
          var = scope.getVar(maybeAssign.nameNode.getString());
          if (var != null) {
            assignsByVar.put(var, maybeAssign);
            assignsByNode.put(maybeAssign.nameNode, maybeAssign);

            if (isRemovableVar(var) &&
                !maybeAssign.mayHaveSecondarySideEffects) {
              // If the var is unreferenced and performing this assign has
              // no secondary side effects, then we can create a continuation
              // for it instead of traversing immediately.
              continuations.put(var, new Continuation(n, scope));
              return;
            }
          }
        }
        break;

      case Token.CALL:
        Var modifiedVar = null;

        // Look for calls to inheritance-defining calls (such as goog.inherits).
        SubclassRelationship subclassRelationship =
            codingConvention.getClassesDefinedByCall(n);
        if (subclassRelationship != null) {
          modifiedVar = scope.getVar(subclassRelationship.subclassName);
        } else {
          // Look for calls to addSingletonGetter calls.
          String className = codingConvention.getSingletonGetterClassName(n);
          if (className != null) {
            modifiedVar = scope.getVar(className);
          }
        }

        // Don't try to track the inheritance calls for non-globals. It would
        // be more correct to only not track when the subclass does not
        // reference a constructor, but checking that it is a global is
        // easier and mostly the same.
        if (modifiedVar != null && modifiedVar.isGlobal()
            && !referenced.contains(modifiedVar)) {
          // Save a reference to the EXPR node.
          classDefiningCalls.put(modifiedVar, parent);
          continuations.put(modifiedVar, new Continuation(n, scope));
          return;
        }
        break;

      case Token.NAME:
        var = scope.getVar(n.getString());
        if (parent.isVar()) {
          Node value = n.getFirstChild();
          if (value != null && var != null && isRemovableVar(var) &&
              !NodeUtil.mayHaveSideEffects(value, compiler)) {
            // If the var is unreferenced and creating its value has no side
            // effects, then we can create a continuation for it instead
            // of traversing immediately.
            continuations.put(var, new Continuation(n, scope));
            return;
          }
        } else {

          // If arguments is escaped, we just assume the worst and continue
          // on all the parameters.
          if ("arguments".equals(n.getString()) && scope.isLocal()) {
            Node lp = scope.getRootNode().getFirstChild().getNext();
            for (Node a = lp.getFirstChild(); a != null; a = a.getNext()) {
              markReferencedVar(scope.getVar(a.getString()));
            }
          }

          // All name references that aren't declarations or assigns
          // are references to other vars.
          if (var != null) {
            // If that var hasn't already been marked referenced, then
            // start tracking it.  If this is an assign, do nothing
            // for now.
            if (isRemovableVar(var)) {
              if (!assignsByNode.containsKey(n)) {
                markReferencedVar(var);
              }
            } else {
              markReferencedVar(var);
            }
          }
        }
        break;
    }

    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
      traverseNode(c, n, scope);
    }
  }

  private boolean isRemovableVar(Var var) {
    // Global variables are off-limits if the user might be using them.
    if (!removeGlobals && var.isGlobal()) {
      return false;
    }

    // Referenced variables are off-limits.
    if (referenced.contains(var)) {
      return false;
    }

    // Exported variables are off-limits.
    if (codingConvention.isExported(var.getName())) {
      return false;
    }

    return true;
  }

  /**
   * Traverses a function, which creates a new scope in JavaScript.
   *
   * Note that CATCH blocks also create a new scope, but only for the
   * catch variable. Declarations within the block actually belong to the
   * enclosing scope. Because we don't remove catch variables, there's
   * no need to treat CATCH blocks differently like we do functions.
   */
  private void traverseFunction(Node n, Scope parentScope) {
    Preconditions.checkState(n.getChildCount() == 3);
    Preconditions.checkState(n.isFunction());

    final Node body = n.getLastChild();
    Preconditions.checkState(body.getNext() == null &&
            body.isBlock());

    Scope fnScope =
        new SyntacticScopeCreator(compiler).createScope(n, parentScope);
    traverseNode(body, n, fnScope);

    collectMaybeUnreferencedVars(fnScope);
    allFunctionScopes.add(fnScope);
  }

  /**
   * For each variable in this scope that we haven't found a reference
   * for yet, add it to the list of variables to check later.
   */
  private void collectMaybeUnreferencedVars(Scope scope) {
    for (Iterator<Var> it = scope.getVars(); it.hasNext(); ) {
      Var var = it.next();
      if (isRemovableVar(var)) {
        maybeUnreferenced.add(var);
      }
    }
  }

  /**
   * Removes unreferenced arguments from a function declaration and when
   * possible the function's callSites.
   *
   * @param fnScope The scope inside the function
   */
  private void removeUnreferencedFunctionArgs(Scope fnScope) {
    // Notice that removing unreferenced function args breaks
    // Function.prototype.length. In advanced mode, we don't really care
    // about this: we consider "length" the equivalent of reflecting on
    // the function's lexical source.
    //
    // Rather than create a new option for this, we assume that if the user
    // is removing globals, then it's OK to remove unused function args.
    //
    // See http://code.google.com/p/closure-compiler/issues/detail?id=253
    if (!removeGlobals) {
      return;
    }

    Node function = fnScope.getRootNode();

    Preconditions.checkState(function.isFunction());
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
      // The parameters object literal setters can not be removed.
      return;
    }

    Node argList = getFunctionArgList(function);
    boolean modifyCallers = modifyCallSites
        && callSiteOptimizer.canModifyCallers(function);
    if (!modifyCallers) {
      // Strip unreferenced args off the end of the function declaration.
      Node lastArg;
      while ((lastArg = argList.getLastChild()) != null) {
        Var var = fnScope.getVar(lastArg.getString());
        if (!referenced.contains(var)) {
          argList.removeChild(lastArg);
          compiler.reportCodeChange();
        } else {
          break;
        }
      }
    } else {
      callSiteOptimizer.optimize(fnScope, referenced);
    }
  }


  /**
   * @return the LP node containing the function parameters.
   */
  private static Node getFunctionArgList(Node function) {
    return function.getFirstChild().getNext();
  }

  private static class CallSiteOptimizer {
    private final AbstractCompiler compiler;
    private final SimpleDefinitionFinder defFinder;
    private final List<Node> toRemove = Lists.newArrayList();
    private final List<Node> toReplaceWithZero = Lists.newArrayList();

    CallSiteOptimizer(
        AbstractCompiler compiler,
        SimpleDefinitionFinder defFinder) {
      this.compiler = compiler;
      this.defFinder = defFinder;
    }

    public void optimize(Scope fnScope, Set<Var> referenced) {
      Node function = fnScope.getRootNode();
      Preconditions.checkState(function.isFunction());
      Node argList = getFunctionArgList(function);

      // In this path we try to modify all the call sites to remove unused
      // function parameters.
      boolean changeCallSignature = canChangeSignature(function);
      markUnreferencedFunctionArgs(
          fnScope, function, referenced,
          argList.getFirstChild(), 0, changeCallSignature);
    }

    /**
     * Applies optimizations to all previously marked nodes.
     */
    public void applyChanges() {
      for (Node n : toRemove) {
        n.getParent().removeChild(n);
        compiler.reportCodeChange();
      }
      for (Node n : toReplaceWithZero) {
        n.getParent().replaceChild(n, IR.number(0).srcref(n));
        compiler.reportCodeChange();
      }
    }

    /**
     * For each unused function parameter, determine if it can be removed
     * from all the call sites, if so, remove it from the function signature
     * and the call sites otherwise replace the unused value where possible
     * with a constant (0).
     *
     * @param scope The function scope
     * @param function The function
     * @param param The current parameter node in the parameter list.
     * @param paramIndex The index of the current parameter
     * @param canChangeSignature Whether function signature can be change.
     * @return Whether there is a following function parameter.
     */
    private boolean markUnreferencedFunctionArgs(
        Scope scope, Node function, Set<Var> referenced,
        Node param, int paramIndex,
        boolean canChangeSignature) {
      if (param != null) {
        // Take care of the following siblings first.
        boolean hasFollowing = markUnreferencedFunctionArgs(
            scope, function, referenced, param.getNext(), paramIndex+1,
            canChangeSignature);

        Var var = scope.getVar(param.getString());
        if (!referenced.contains(var)) {
          Preconditions.checkNotNull(var);

          // Remove call parameter if we can generally change the signature
          // or if it is the last parameter in the parameter list.
          boolean modifyAllCallSites = canChangeSignature || !hasFollowing;
          if (modifyAllCallSites) {
            modifyAllCallSites = canRemoveArgFromCallSites(
                function, paramIndex);
          }

          tryRemoveArgFromCallSites(function, paramIndex, modifyAllCallSites);

          // Remove an unused function parameter if all the call sites can
          // be modified to remove it, or if it is the last parameter.
          if (modifyAllCallSites || !hasFollowing) {
            toRemove.add(param);
            return hasFollowing;
          }
        }
        return true;
      } else {
        // Anything past the last formal parameter can be removed from the call
        // sites.
        tryRemoveAllFollowingArgs(function, paramIndex-1);
        return false;
      }
    }

    /**
     * Remove all references to a parameter, otherwise simplify the known
     * references.
     * @return Whether all the references were removed.
     */
    private boolean canRemoveArgFromCallSites(Node function, int argIndex) {
      Definition definition = getFunctionDefinition(function);

      // Check all the call sites.
      for (UseSite site : defFinder.getUseSites(definition)) {
        if (isModifiableCallSite(site)) {
          Node arg = getArgumentForCallOrNewOrDotCall(site, argIndex);
          // TODO(johnlenz): try to remove parameters with side-effects by
          // decomposing the call expression.
          if (arg != null && NodeUtil.mayHaveSideEffects(arg, compiler)) {
            return false;
          }
        } else {
          return false;
        }
      }

      return true;
    }

    /**
     * Remove all references to a parameter if possible otherwise simplify the
     * side-effect free parameters.
     */
    private void tryRemoveArgFromCallSites(
        Node function, int argIndex, boolean canModifyAllSites) {
      Definition definition = getFunctionDefinition(function);

      for (UseSite site : defFinder.getUseSites(definition)) {
        if (isModifiableCallSite(site)) {
          Node arg = getArgumentForCallOrNewOrDotCall(site, argIndex);
          if (arg != null) {
            Node argParent = arg.getParent();
            // Even if we can't change the signature in general we can always
            // remove an unused value off the end of the parameter list.
            if (canModifyAllSites
                || (arg.getNext() == null
                    && !NodeUtil.mayHaveSideEffects(arg, compiler))) {
              toRemove.add(arg);
            } else {
              // replace the node in the arg with 0
              if (!NodeUtil.mayHaveSideEffects(arg, compiler)
                  && (!arg.isNumber() || arg.getDouble() != 0)) {
                toReplaceWithZero.add(arg);
              }
            }
          }
        }
      }
    }

    /**
     * Remove all the following parameters without side-effects
     */
    private void tryRemoveAllFollowingArgs(Node function, final int argIndex) {
      Definition definition = getFunctionDefinition(function);
      for (UseSite site : defFinder.getUseSites(definition)) {
        if (!isModifiableCallSite(site)) {
          continue;
        }
        Node arg = getArgumentForCallOrNewOrDotCall(site, argIndex + 1);
        while (arg != null) {
          if (!NodeUtil.mayHaveSideEffects(arg)) {
            toRemove.add(arg);
          }
          arg = arg.getNext();
        }
      }
    }

    /**
     * Returns the nth argument node given a usage site for a direct function
     * call or for a func.call() node.
     */
    private static Node getArgumentForCallOrNewOrDotCall(UseSite site,
        final int argIndex) {
      int adjustedArgIndex = argIndex;
      Node parent = site.node.getParent();
      if (NodeUtil.isFunctionObjectCall(parent)) {
        adjustedArgIndex++;
      }
      return NodeUtil.getArgumentForCallOrNew(parent, adjustedArgIndex);
    }

    /**
     * @param function
     * @return Whether the callers to this function can be modified in any way.
     */
    boolean canModifyCallers(Node function) {
      if (NodeUtil.isVarArgsFunction(function)) {
        return false;
      }

      DefinitionSite defSite = defFinder.getDefinitionForFunction(function);
      if (defSite == null) {
        return false;
      }

      Definition definition = defSite.definition;

      // Be conservative, don't try to optimize any declaration that isn't as
      // simple function declaration or assignment.
      if (!SimpleDefinitionFinder.isSimpleFunctionDeclaration(function)) {
        return false;
      }

      return defFinder.canModifyDefinition(definition);
    }

    /**
     * @param site The site to inspect
     * @return Whether the call site is suitable for modification
     */
    private static boolean isModifiableCallSite(UseSite site) {
      return SimpleDefinitionFinder.isCallOrNewSite(site)
          && !NodeUtil.isFunctionObjectApply(site.node.getParent());
    }

    /**
     * @return Whether the definitionSite represents a function whose call
     *      signature can be modified.
     */
    private boolean canChangeSignature(Node function) {
      Definition definition = getFunctionDefinition(function);
      CodingConvention convention = compiler.getCodingConvention();

      Preconditions.checkState(!definition.isExtern());

      Collection<UseSite> useSites = defFinder.getUseSites(definition);
      for (UseSite site : useSites) {
        Node parent = site.node.getParent();

        // This was a use site removed by something else before we run.
        // 1. By another pass before us which means the definition graph is
        //    no updated properly.
        // 2. By the continuations algorithm above.
        if (parent == null) {
          continue; // Ignore it.
        }

        // Ignore references within goog.inherits calls.
        if (parent.isCall() &&
            convention.getClassesDefinedByCall(parent) != null) {
          continue;
        }

        // Accessing the property directly prevents rewrite.
        if (!SimpleDefinitionFinder.isCallOrNewSite(site)) {
          if (!(parent.isGetProp() &&
              NodeUtil.isFunctionObjectCall(parent.getParent()))) {
            return false;
          }
        }

        if (NodeUtil.isFunctionObjectApply(parent)) {
          return false;
        }

        // TODO(johnlenz): support specialization

        // Multiple definitions prevent rewrite.
        // Attempt to validate the state of the simple definition finder.
        Node nameNode = site.node;
        Collection<Definition> singleSiteDefinitions =
            defFinder.getDefinitionsReferencedAt(nameNode);
        Preconditions.checkState(singleSiteDefinitions.size() == 1);
        Preconditions.checkState(singleSiteDefinitions.contains(definition));
      }

      return true;
    }

    /**
     * @param function
     * @return the Definition object for the function.
     */
    private Definition getFunctionDefinition(Node function) {
      DefinitionSite definitionSite = defFinder.getDefinitionForFunction(
          function);
      Preconditions.checkNotNull(definitionSite);
      Definition definition = definitionSite.definition;
      Preconditions.checkState(!definitionSite.inExterns);
      Preconditions.checkState(definition.getRValue() == function);
      return definition;
    }
  }


  /**
   * Look at all the property assigns to all variables.
   * These may or may not count as references. For example,
   *
   * <code>
   * var x = {};
   * x.foo = 3; // not a reference.
   * var y = foo();
   * y.foo = 3; // is a reference.
   * </code>
   *
   * Interpreting assignments could mark a variable as referenced that
   * wasn't referenced before, in order to keep it alive. Because we find
   * references by lazily traversing subtrees, marking a variable as
   * referenced could trigger new traversals of new subtrees, which could
   * find new references.
   *
   * Therefore, this interpretation needs to be run to a fixed point.
   */
  private void interpretAssigns() {
    boolean changes = false;
    do {
      changes = false;

      // We can't use traditional iterators and iterables for this list,
      // because our lazily-evaluated continuations will modify it while
      // we traverse it.
      for (int current = 0; current < maybeUnreferenced.size(); current++) {
        Var var = maybeUnreferenced.get(current);
        if (referenced.contains(var)) {
          maybeUnreferenced.remove(current);
          current--;
        } else {
          boolean assignedToUnknownValue = false;
          boolean hasPropertyAssign = false;

          if (var.getParentNode().isVar() &&
              !NodeUtil.isForIn(var.getParentNode().getParent())) {
            Node value = var.getInitialValue();
            assignedToUnknownValue = value != null &&
                !NodeUtil.isLiteralValue(value, true);
          } else {
            // This was initialized to a function arg or a catch param
            // or a for...in variable.
            assignedToUnknownValue = true;
          }

          boolean maybeEscaped = false;
          for (Assign assign : assignsByVar.get(var)) {
            if (assign.isPropertyAssign) {
              hasPropertyAssign = true;
            } else if (!NodeUtil.isLiteralValue(
                assign.assignNode.getLastChild(), true)) {
              assignedToUnknownValue = true;
            }
            if (assign.maybeAliased) {
              maybeEscaped = true;
            }
          }

          if ((assignedToUnknownValue || maybeEscaped) && hasPropertyAssign) {
            changes = markReferencedVar(var) || changes;
            maybeUnreferenced.remove(current);
            current--;
          }
        }
      }
    } while (changes);
  }

  /**
   * Remove all assigns to a var.
   */
  private void removeAllAssigns(Var var) {
    for (Assign assign : assignsByVar.get(var)) {
      assign.remove();
      compiler.reportCodeChange();
    }
  }

  /**
   * Marks a var as referenced, recursing into any values of this var
   * that we skipped.
   * @return True if this variable had not been referenced before.
   */
  private boolean markReferencedVar(Var var) {
    if (referenced.add(var)) {
      for (Continuation c : continuations.get(var)) {
        c.apply();
      }
      return true;
    }
    return false;
  }

  /**
   * Removes any vars in the scope that were not referenced. Removes any
   * assignments to those variables as well.
   */
  private void removeUnreferencedVars() {
    CodingConvention convention = codingConvention;

    for (Iterator<Var> it = maybeUnreferenced.iterator(); it.hasNext(); ) {
      Var var = it.next();

      // Remove calls to inheritance-defining functions where the unreferenced
      // class is the subclass.
      for (Node exprCallNode : classDefiningCalls.get(var)) {
        NodeUtil.removeChild(exprCallNode.getParent(), exprCallNode);
        compiler.reportCodeChange();
      }

      // Regardless of what happens to the original declaration,
      // we need to remove all assigns, because they may contain references
      // to other unreferenced variables.
      removeAllAssigns(var);

      compiler.addToDebugLog("Unreferenced var: " + var.name);
      Node nameNode = var.nameNode;
      Node toRemove = nameNode.getParent();
      Node parent = toRemove.getParent();

      Preconditions.checkState(
          toRemove.isVar() ||
          toRemove.isFunction() ||
          toRemove.isParamList() &&
          parent.isFunction(),
          "We should only declare vars and functions and function args");

      if (toRemove.isParamList() &&
          parent.isFunction()) {
        // Don't remove function arguments here. That's a special case
        // that's taken care of in removeUnreferencedFunctionArgs.
      } else if (NodeUtil.isFunctionExpression(toRemove)) {
        if (!preserveFunctionExpressionNames) {
          toRemove.getFirstChild().setString("");
          compiler.reportCodeChange();
        }
        // Don't remove bleeding functions.
      } else if (parent != null &&
          parent.isFor() &&
          parent.getChildCount() < 4) {
        // foreach iterations have 3 children. Leave them alone.
      } else if (toRemove.isVar() &&
          nameNode.hasChildren() &&
          NodeUtil.mayHaveSideEffects(nameNode.getFirstChild(), compiler)) {
        // If this is a single var declaration, we can at least remove the
        // declaration itself and just leave the value, e.g.,
        // var a = foo(); => foo();
        if (toRemove.getChildCount() == 1) {
          parent.replaceChild(toRemove,
              IR.exprResult(nameNode.removeFirstChild()));
          compiler.reportCodeChange();
        }
      } else if (toRemove.isVar() &&
          toRemove.getChildCount() > 1) {
        // For var declarations with multiple names (i.e. var a, b, c),
        // only remove the unreferenced name
        toRemove.removeChild(nameNode);
        compiler.reportCodeChange();
      } else if (parent != null) {
        NodeUtil.removeChild(parent, toRemove);
        compiler.reportCodeChange();
      }
    }
  }

  /**
   * Our progress in a traversal can be expressed completely as the
   * current node and scope. The continuation lets us save that
   * information so that we can continue the traversal later.
   */
  private class Continuation {
    private final Node node;
    private final Scope scope;

    Continuation(Node node, Scope scope) {
      this.node = node;
      this.scope = scope;
    }

    void apply() {
      if (NodeUtil.isFunctionDeclaration(node)) {
        traverseFunction(node, scope);
      } else {
        for (Node child = node.getFirstChild();
             child != null; child = child.getNext()) {
          traverseNode(child, node, scope);
        }
      }
    }
  }

  private static class Assign {

    final Node assignNode;

    final Node nameNode;

    // If false, then this is an assign to the normal variable. Otherwise,
    // this is an assign to a property of that variable.
    final boolean isPropertyAssign;

    // Secondary side effects are any side effects in this assign statement
    // that aren't caused by the assignment operation itself. For example,
    // a().b = 3;
    // a = b();
    // var foo = (a = b);
    // In the first two cases, the sides of the assignment have side-effects.
    // In the last one, the result of the assignment is used, so we
    // are conservative and assume that it may be used in a side-effecting
    // way.
    final boolean mayHaveSecondarySideEffects;

    // If true, the value may have escaped and any modification is a use.
    final boolean maybeAliased;

    Assign(Node assignNode, Node nameNode, boolean isPropertyAssign) {
      Preconditions.checkState(NodeUtil.isAssignmentOp(assignNode));
      this.assignNode = assignNode;
      this.nameNode = nameNode;
      this.isPropertyAssign = isPropertyAssign;

      this.maybeAliased = NodeUtil.isExpressionResultUsed(assignNode);
      this.mayHaveSecondarySideEffects =
          maybeAliased ||
          NodeUtil.mayHaveSideEffects(assignNode.getFirstChild()) ||
          NodeUtil.mayHaveSideEffects(assignNode.getLastChild());
    }

    /**
     * If this is an assign to a variable or its property, return it.
     * Otherwise, return null.
     */
    static Assign maybeCreateAssign(Node assignNode) {
      Preconditions.checkState(NodeUtil.isAssignmentOp(assignNode));

      // Skip one level of GETPROPs or GETELEMs.
      //
      // Don't skip more than one level, because then we get into
      // situations where assigns to properties of properties will always
      // trigger side-effects, and the variable they're on cannot be removed.
      boolean isPropAssign = false;
      Node current = assignNode.getFirstChild();
      if (NodeUtil.isGet(current)) {
        current = current.getFirstChild();
        isPropAssign = true;

        if (current.isGetProp() &&
            current.getLastChild().getString().equals("prototype")) {
          // Prototype properties sets should be considered like normal
          // property sets.
          current = current.getFirstChild();
        }
      }

      if (current.isName()) {
        return new Assign(assignNode, current, isPropAssign);
      }
      return null;
    }

    /**
     * Replace the current assign with its right hand side.
     */
    void remove() {
      Node parent = assignNode.getParent();
      if (mayHaveSecondarySideEffects) {
        Node replacement = assignNode.getLastChild().detachFromParent();

        // Aggregate any expressions in GETELEMs.
        for (Node current = assignNode.getFirstChild();
             !current.isName();
             current = current.getFirstChild()) {
          if (current.isGetElem()) {
            replacement = IR.comma(
                current.getLastChild().detachFromParent(), replacement);
            replacement.copyInformationFrom(current);
          }
        }

        parent.replaceChild(assignNode, replacement);
      } else {
        Node gramps = parent.getParent();
        if (parent.isExprResult()) {
          gramps.removeChild(parent);
        } else {
          parent.replaceChild(assignNode,
              assignNode.getLastChild().detachFromParent());
        }
      }
    }
  }
}

```

## Build configuration build.xml

```text
<!--
 Copyright 2009 Google Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
<project name="compiler" basedir="." default="jar" xmlns:artifact="antlib:org.apache.maven.artifact.ant">

  <!--
    Use -Dtest.class to change what tests are run on the command-line.
    i.e., -Dtest.class=CommandLineRunnerTest will run just that test class.
  -->
  <property name="test.class" value="*Test"/>

  <!--
    Use -Dtest.fork to specify whether or not to fork the process.
    Some machines run better with forking turned off.
  -->
  <property name="test.fork" value="true"/>

  <!-- Force java 6 -->
  <property name="ant.build.javac.source" value="1.6" />
  <property name="ant.build.javac.target" value="1.6" />

  <!-- define other variables -->
  <property name="javac.debug" value="on" />
  <property name="src.dir" value="${basedir}/src" />
  <property name="gen.dir" value="${basedir}/gen" />
  <property name="test.dir" value="${basedir}/test" />
  <property name="externs.dir" value="${basedir}/externs" />
  <!-- To workaround Ant limitation on overriding properties set on the
       command-line, define a unique property to allow "build.dir" to
       be change without forcing the build of Rhino to the same directory.
  -->
  <property name="closure.build.dir" value="${basedir}/build" />
  <property name="build.dir" value="${closure.build.dir}" />
  <property name="buildlib.dir" value="${build.dir}/lib" />
  <property name="classes.dir" value="${build.dir}/classes" />
  <property name="testClasses.dir" value="${build.dir}/test" />
  <property name="javadoc.dir" value="${build.dir}/javadoc" />
  <property name="stylesheetfile" value="${javadoc.dir}/dev_javadoc.css" />
  <property name="lib.dir" value="${basedir}/lib" />
  <property name="tools.dir" value="${basedir}/tools" />
  <property name="compiler-jarfile"
            value="${build.dir}/${ant.project.name}.jar" />
  <property name="num-fuzz-tests" value="10000"/>

  <property name="jsonml.dir" value="${basedir}/src/com/google/javascript/jscomp/jsonml" />
  <property name="jsonml-classes.dir" value = "${build.dir}/jsonml-classes" />
  <property name="jsonml-jarfile" value="${build.dir}/secure_compiler.jar" />

  <property name="webservice.dir" value="${basedir}/src/com/google/javascript/jscomp/webservice" />
  <property name="webservice-classes.dir" value = "${build.dir}/webservice-classes" />
  <property name="webservice-jarfile" value="${build.dir}/webservice.jar" />

  <!-- The following server is used to deploy releases to maven central via Sonatypes
       publishing service which runs on oss.sonatype.org.

       You will need to have an account on sonatype.org to push releases.  You can
       override these values if you want to deploy to a different repository
  -->
  <property name="maven-repository-url" value="https://oss.sonatype.org/service/local/staging/deploy/maven2/" />
  <property name="maven-repository-id" value="sonatype-nexus-staging" />

  <!-- proto compiler used to generate java classes from .proto files -->
  <property name="protoc.executable" value="protoc"/>

  <property file="build.properties" />

  <!-- maven ant tasks -->
  <path id="maven-ant-tasks.classpath" path="${tools.dir}/maven-ant-tasks-2.1.3.jar" />
  <typedef resource="org/apache/maven/artifact/ant/antlib.xml"
           uri="antlib:org.apache.maven.artifact.ant"
           classpathref="maven-ant-tasks.classpath" />

  <!-- gather svn version -->
  <target name="svnversion">
    <exec outputproperty="build.svnVersion"
      executable="svnversion"
      failonerror="false"
      failifexecutionfails="false"
      dir="."/>
  </target>

  <!-- compile rhino -->
  <target name="rhino">
    <ant antfile="build.xml"
         inheritAll="false"
         dir="lib/rhino/"
         target="jar">
      <property name="build.dir" value="${buildlib.dir}"/>
      <property name="no-e4x" value="true"/>
    </ant>
  </target>

  <target name="rhino-jarjar"
          depends="rhino"
          description="Renamespaces Rhino">
    <taskdef name="jarjar"
             classname="com.tonicsystems.jarjar.JarJarTask"
             classpath="lib/jarjar.jar"/>
    <jarjar destfile="${buildlib.dir}/rhino.jar" update="true">
      <zipfileset src="${buildlib.dir}/rhino1_7R5pre/js.jar"/>
      <rule pattern="org.mozilla.javascript.**"
            result="com.google.javascript.rhino.head.@1"/>
    </jarjar>
  </target>

  <target name="protobuf-gen">
    <fileset dir="${src.dir}" id="proto.classpath">
      <include name="**/*.proto"/>
    </fileset>
    <pathconvert property="protofiles" pathsep=" " refid="proto.classpath"/>
    <echo message="${protoc.executable} -I ${src.dir} --java_out=${gen.dir} ${protofiles}"/>
    <exec executable="${protoc.executable}" searchpath="true">
      <arg line="-I ${src.dir}"/>
      <arg line="--java_out=${gen.dir}"/>
      <arg line="${protofiles}"/>
    </exec>
  </target>

  <!-- Generate pom.xml with the proper svn build number -->
  <target name="pom" depends="svnversion">
    <copy file="closure-compiler.pom" tofile="${build.dir}/pom.xml">
      <filterset>
        <filter token="build.svnVersion"
             value="${build.svnVersion}"/>
      </filterset>
    </copy>

    <property name="compiler-jarfile-nodeps" value="${build.dir}/closure-${ant.project.name}-r${build.svnVersion}.jar" />
    <property name="compiler-jarfile-javadoc" value="${build.dir}/closure-${ant.project.name}-r${build.svnVersion}-javadoc.jar" />
    <property name="compiler-jarfile-sources" value="${build.dir}/closure-${ant.project.name}-r${build.svnVersion}-sources.jar" />

    <artifact:pom id="project" file="${build.dir}/pom.xml" />
  </target>

  <target name="mvn-install"
          depends="jar-nodeps,jar-javadoc,jar-sources,pom"
          description="Install closure-compiler artifacts into the local maven repo">
    <artifact:install file="${compiler-jarfile-nodeps}">
      <pom refid="project"/>
      <attach file="${compiler-jarfile-javadoc}" classifier="javadoc"/>
      <attach file="${compiler-jarfile-sources}" classifier="sources"/>
    </artifact:install>
  </target>

  <!-- The mvn-deploy target takes the generated maven artifacts and pushes
       them to the Sonatype repository.  You will need to have a
       gpg key defined and account set up. More docs on how to do this here:

       https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
  -->

  <target name="mvn-deploy"
    depends="mvn-install"
    description="Signs and Deploys closure-compiler artifacts to the central maven repo">

    <!-- sign and deploy the main artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${compiler-jarfile-nodeps}" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the sources artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${compiler-jarfile-sources}" />
      <arg value="-Dclassifier=sources" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the javadoc artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${compiler-jarfile-javadoc}" />
      <arg value="-Dclassifier=javadoc" />
      <arg value="-Pgpg" />
    </artifact:mvn>
  </target>

  <!-- Sync maven dependencies listed in pom.xml -->
  <target name="mvn-deps-sync" description="sync dependencies/jars in closure-compiler.pom to lib" depends="pom">
    <artifact:dependencies filesetid="dependency.fileset"
                           pathid="dependency.classpath"
                           pomrefid="project"
                           versionsId="dependency.versions"/>
    <mkdir dir="${lib.dir}"/>
    <copy todir="${lib.dir}">
      <fileset refid="dependency.fileset" />
      <!-- This mapper strips off all leading directory information -->
      <mapper classpathref="maven-ant-tasks.classpath"
          classname="org.apache.maven.artifact.ant.VersionMapper"
          from="${dependency.versions}" to="flatten" />
    </copy>
  </target>

  <!-- set the classpath for the project              -->
  <!-- this includes the generated source class files -->
  <!-- and every jar in the /lib directory            -->

  <path id="srcclasspath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="args4j.jar"/>
      <include name="guava.jar"/>
      <include name="json.jar"/>
      <include name="jsr305.jar"/>
      <include name="protobuf-java.jar"/>
    </fileset>
    <fileset dir="${buildlib.dir}">
      <include name="rhino.jar"/>
    </fileset>
  </path>

  <path id="allclasspath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="*.jar"/>
    </fileset>
    <fileset dir="${buildlib.dir}">
      <include name="rhino.jar"/>
    </fileset>
  </path>

  <target name="clean" description="delete generated files">
    <delete dir="${build.dir}" />
  </target>

  <target name="compile"
          description="compile the source code"
          depends="rhino-jarjar,svnversion">
    <mkdir dir="${classes.dir}" />
    <javac debug="true" srcdir="${gen.dir}"
           destdir="${classes.dir}"
           excludes=".svn"
           >
      <classpath refid="srcclasspath.path" />
    </javac>
    <javac debug="true" srcdir="${src.dir}"
           destdir="${classes.dir}"
           excludes=".svn,**/jsonml/**,**/webservice/**,**/testing/**"
           >
      <classpath refid="srcclasspath.path" />
    </javac>

    <!-- Move Messages.properties where ScriptRuntime.java expects it. -->
    <mkdir dir="${classes.dir}/rhino_ast/java/com/google/javascript/rhino/" />
    <copy file="${src.dir}/com/google/javascript/rhino/Messages.properties"
          todir="${classes.dir}/rhino_ast/java/com/google/javascript/rhino/" />

    <!-- Move ParserConfig.properties where ParserRunner.java expects it. -->
    <copy file="${src.dir}/com/google/javascript/jscomp/parsing/ParserConfig.properties"
          todir="${classes.dir}/com/google/javascript/jscomp/parsing" />

    <propertyfile
        file="${classes.dir}/com/google/javascript/jscomp/parsing/ParserConfig.properties"
        comment="Parser properties">
      <entry key="compiler.date" type="date" value="now"/>
      <entry key="compiler.version" value="${build.svnVersion}"/>
    </propertyfile>

    <!-- Move runtime_type_check.js where RuntimeTypeCheck.java expects it. -->
    <mkdir dir="${classes.dir}/com/google/javascript/jscomp/js" />
    <copy todir="${classes.dir}/com/google/javascript/jscomp/js">
      <fileset dir="${src.dir}/com/google/javascript/jscomp/js" />
    </copy>
  </target>

  <target name="jar-nodeps"
          depends="compile,pom"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" />
    <jar destfile="${compiler-jarfile-nodeps}" update="true">
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
      <zipfileset src="${build.dir}/lib/rhino.jar"/>
    </jar>
  </target>


  <target name="jar"
          depends="compile"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
    <jar destfile="${compiler-jarfile}" update="true">
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
      <zipfileset src="${lib.dir}/args4j.jar"/>
      <zipfileset src="${lib.dir}/guava.jar"/>
      <zipfileset src="${lib.dir}/json.jar"/>
      <zipfileset src="${lib.dir}/jsr305.jar"/>
      <zipfileset src="${lib.dir}/protobuf-java.jar"/>

      <zipfileset src="${buildlib.dir}/rhino.jar"/>

      <manifest>
        <attribute name="Main-Class"
                   value="com.google.javascript.jscomp.CommandLineRunner" />
      </manifest>
    </jar>
  </target>

  <target name="compile-tests"
          depends="compile"
          description="compile the JUnit tests">
    <mkdir dir="${testClasses.dir}" />
    <javac debug="true" srcdir="${src.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn"
           >
      <classpath refid="allclasspath.path" />
    </javac>
    <javac debug="true" srcdir="${test.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn"
           >
      <classpath refid="allclasspath.path" />
    </javac>
  </target>

  <target name="all-classes-jar"
          depends="compile,compile-tests"
          description="package the compiler and its tests into one jar">
    <jar destfile="${compiler-jarfile}" update="true">
      <fileset dir="${testClasses.dir}" />
      <zipgroupfileset dir="${lib.dir}" includes="*.jar"/>
      <zipfileset src="${buildlib.dir}/rhino.jar"/>
    </jar>
  </target>

  <target name="test"
          depends="compile-tests"
          description="Compile and execute the JUnit tests.">
    <mkdir dir="build/testoutput"/>
    <junit printsummary="on" fork="${test.fork}"
           forkmode="once" showoutput="true"
           failureproperty="junit.failure">
      <classpath refid="allclasspath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <batchtest todir="build/testoutput">
        <formatter type="brief" usefile="false" />
        <formatter type="xml" />
        <fileset dir="${build.dir}/test">
          <include name="**/${test.class}.class" />
        </fileset>
      </batchtest>
    </junit>
    <junitreport>
       <fileset dir="build/testoutput" includes="*.xml"/>
       <report todir="build/testoutput"/>
    </junitreport>
    <fail if="junit.failure"
          message="Unit tests failed. See build/testoutput/index.html" />
  </target>

  <target name="fuzz-test"
          depends="all-classes-jar"
          description="checks the compiler against a variety of js programs">
      <exec executable="java" failonerror="true">
        <arg value="-cp" />
        <arg value="${compiler-jarfile}" />
        <arg value="com.google.javascript.jscomp.regtests.CompileEachLineOfProgramOutput" />
        <arg value="generatejs"/>
        <arg value="--stdout"/>
        <arg value="${num-fuzz-tests}"/>
      </exec>
  </target>

  <target name="javadoc"
          description="generate Javadoc"
          depends="rhino-jarjar">
    <mkdir dir="${javadoc.dir}" />
    <javadoc
         destdir="${javadoc.dir}"
         author="false"
         protected="true"
         windowtitle="Compiler"
         additionalparam=" -notimestamp "
         stylesheetfile="${stylesheetfile}">
      <sourcepath>
        <pathelement location="${src.dir}" />
        <pathelement location="${gen.dir}" />
      </sourcepath>
      <classpath refid="allclasspath.path" />
      <link href="http://java.sun.com/javase/6/docs/api/" />
      <bottom><![CDATA[
        <div id="footer">
          <div id="footerlogo">
            <img src="http://www.google.com/images/art.gif"
                 alt="Google colored balls">
          </div>

          <div id="copyright">
          <p>&copy; 2009 Google -
            <a href="http://www.google.com/privacy.html">Privacy Policy</a> -
            <a href="http://www.google.com/terms_of_service.html">Terms and Conditions</a> -
            <a href="http://www.google.com/about.html">About Google</a>
          </p>
          </div>
        </div>
      ]]>
      </bottom>
    </javadoc>
  </target>

  <target name="jar-javadoc" depends="javadoc">
    <jar jarfile="${compiler-jarfile-javadoc}">
      <fileset dir="${javadoc.dir}" />
    </jar>
  </target>

  <target name="jar-sources" depends="javadoc">
    <jar jarfile="${compiler-jarfile-sources}">
      <fileset dir="${src.dir}" />
      <fileset dir="${gen.dir}" />
    </jar>
  </target>

  <!-- JsonML package related targets                 -->
  <!-- set the classpath for the project              -->
  <!-- this includes the generated source class files -->
  <!-- and every jar in the /lib directory            -->
  <path id="jsonml-classpath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="*.jar" />
    </fileset>
    <fileset dir="${classes.dir}">
      <include name="*.class" />
    </fileset>
    <fileset dir="${buildlib.dir}">
      <include name="rhino.jar"/>
    </fileset>
  </path>

  <target name="jsonml-compile"
          description="compile the source code of classes from JsonML package"
          depends="compile">
    <mkdir dir="${jsonml-classes.dir}" />
    <javac debug="true" srcdir="${jsonml.dir}"
           destdir="${jsonml-classes.dir}"
           excludes=".svn"
           >
      <classpath refid="jsonml-classpath.path" />
    </javac>
  </target>

  <target name="jsonml-jar"
          description="package the compiler and JsonML classes"
          depends="jsonml-compile, compile">
   <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
   <jar destfile="${jsonml-jarfile}" update="true">
     <zipgroupfileset dir="${lib.dir}" includes="*.jar"/>
     <zipfileset src="${buildlib.dir}/rhino.jar"/>
     <fileset dir="${classes.dir}" />
     <fileset dir="${jsonml-classes.dir}" />
   </jar>
  </target>

  <target name="webservice-compile"
          description="compile the source code of classes from the webservice package"
          depends="compile">
    <mkdir dir="${webservice-classes.dir}" />
    <javac debug="true" srcdir="${webservice.dir}"
           destdir="${webservice-classes.dir}"
           excludes=".svn"
           >
      <classpath refid="jsonml-classpath.path" />
    </javac>
  </target>

  <target name="webservice-jar"
          description="package the compiler and Webservice classes"
          depends="webservice-compile, compile">
   <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
   <jar destfile="${webservice-jarfile}" update="true">
     <zipgroupfileset dir="${lib.dir}" includes="*.jar"/>
     <zipfileset src="${buildlib.dir}/rhino.jar"/>
     <fileset dir="${classes.dir}" />
     <fileset dir="${webservice-classes.dir}" />
   </jar>
  </target>
</project>

```

## Build configuration lib/rhino/build.xml

```text
<?xml version="1.0" encoding="utf-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->


<!--
    Build file for Rhino using Ant (see http://jakarta.apache.org/ant/index.html)
    Requires Ant version 1.2 or later

    Compilation currently requires JDK 1.5 or later. Can cross-compile to
    support JDK 1.4.
-->

<project name="Rhino" default="help" basedir=".">

  <target name="properties">
    <!-- Allow user to override default settings from build.properties -->
    <property file="build.local.properties" />
    <tstamp>
      <!-- Specify date part of Context#getImplementationVersion() -->
      <format property="implementation.date" pattern="yyyy MM dd"/>
    </tstamp>
    <property file="build.properties"/>

    <property name="dist.file" value="rhino${version}.zip"/>
    <property name="dist.source-only-zip" value="rhino${version}-sources.zip"/>

    <property file="apiClasses.properties"/>
    <property name="xmlimplsrc-build-file"
              location="xmlimplsrc/build.xml"/>

    <available property="xmlimplsrc-present?"
               file="${xmlimplsrc-build-file}" />

  </target>

  <target name="init" depends="properties">
    <mkdir dir="${build.dir}"/>
    <mkdir dir="${classes}"/>
    <mkdir dir="${dist.dir}"/>
  </target>

  <target name="compile" depends="init">
    <ant antfile="src/build.xml" target="compile"/>
    <ant antfile="toolsrc/build.xml" target="compile"/>
    <antcall target="xmlimplsrc-compile" />
  </target>

  <target name="compile-all" depends="compile">
    <ant antfile="deprecatedsrc/build.xml" target="compile"/>
  </target>

  <target name="graph" depends="init">
    <ant antfile="src/build.xml" target="graph"/>
  </target> 

  <target name="shell" depends="compile">
    <ant antfile="src/build.xml" target="shell"/>
  </target> 

  <target name="copy-source" depends="init">
    <ant antfile="src/build.xml" target="copy-source"/>
    <ant antfile="toolsrc/build.xml" target="copy-source"/>
    <ant antfile="testsrc/build.xml" target="copy-source"/>
    <antcall target="xmlimplsrc-copy-source" />
    <ant antfile="deprecatedsrc/build.xml" target="copy-source"/>
    <copy todir="${dist.dir}" file="build.xml"/>
    <copy todir="${dist.dir}" file="build.properties"/>
    <copy todir="${dist.dir}" file="apiClasses.properties"/>
    <copy todir="${dist.dir}" file="LICENSE.txt"/>
  </target>

  <target name="xmlimplsrc-compile" if="xmlimplsrc-present?">
    <echo>Calling ${xmlimplsrc-build-file}</echo>
    <!-- Ignore compilation errors under JDK less then 1.4 -->
    <property name="xmlimpl.compile.failonerror" value="no"/>
    <ant antfile="${xmlimplsrc-build-file}" target="compile"/>
  </target>

  <target name="xmlimplsrc-copy-source" if="xmlimplsrc-present?">
    <echo>Calling ${xmlimplsrc-build-file}</echo>
    <ant antfile="${xmlimplsrc-build-file}" target="copy-source"/>
  </target>

  <target name="jar" depends="compile-all">
    <copy todir="${classes}" file="LICENSE.txt"/>
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <jar jarfile="${jarfile}"
         basedir="${classes}"
         manifest="src/manifest"
         compress="${jar-compression}"
     />
  </target>

  <target name="console" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}"
          fork="true">
      <arg line="-version 170"/>
    </java>
  </target>

  <target name="retrotranslator" depends="retrotranslator-check,retrotranslator-download">
    <taskdef name="retrotranslator" classpath="build/download/Retrotranslator-1.2.8-bin/retrotranslator-transformer-1.2.8.jar"
      classname="net.sf.retrotranslator.transformer.RetrotranslatorTask"/>
  </target>


  <target name="retrotranslator-check">
    <condition property="retrotranslator.available">
      <and>
        <available file="build/download/Retrotranslator-1.2.8-bin/retrotranslator-transformer-1.2.8.jar"/>
        <available file="build/download/Retrotranslator-1.2.8-bin/retrotranslator-runtime-1.2.8.jar"/>
        <available file="build/download/Retrotranslator-1.2.8-bin/backport-util-concurrent-3.1.jar"/>
      </and>
    </condition>
  </target>

  <target name="retrotranslator-download" unless="retrotranslator.available">
    <mkdir dir="build/download"/>
    <get src="http://downloads.sourceforge.net/retrotranslator/Retrotranslator-1.2.8-bin.zip" dest="build/download/Retrotranslator-1.2.8-bin.zip" usetimestamp="true"/>
    <unzip src="build/download/Retrotranslator-1.2.8-bin.zip" dest="build/download"/>
  </target>

  <target name="retrojar" depends="jar,retrotranslator">
    <retrotranslator
      srcjar="${jarfile}"
      destjar="${dist.dir}/${rhino-14.jar}"
      embed="org.mozilla.javascript"
    />
  </target>

  <target name="smalljar" depends="compile">
    <property name="smalljarfile" location="${dist.dir}/${small-rhino.jar}"/>
    <jar basedir="${classes}" destfile="${smalljarfile}"
         compress="${jar-compression}">
      <include name="org/mozilla/javascript/*.class"/>

      <include name="org/mozilla/javascript/debug/*.class"/>
      <include name="org/mozilla/javascript/resources/*.properties"/>
      <include name="org/mozilla/javascript/xml/*.class"/>
      <include name="org/mozilla/javascript/continuations/*.class"/>
      <include name="org/mozilla/javascript/jdk13/*.class"/>
      <include name="org/mozilla/javascript/ast/*.class"/>
      <include name="org/mozilla/javascript/json/*.class"/>
      <include name="org/mozilla/javascript/annotations/*.class"/>
      <include name="org/mozilla/javascript/v8dtoa/*.class"/>

      <!-- exclude classes that uses class generation library -->
      <exclude name="org/mozilla/javascript/JavaAdapter*.class"/>

      <include name="org/mozilla/javascript/regexp/*.class"
               unless="no-regexp"/>
    </jar>

  </target>

  <target name="retrosmalljar" depends="smalljar,retrotranslator">
    <retrotranslator
      srcjar="${smalljarfile}"
      destjar="${dist.dir}/${small-rhino-14.jar}"
      embed="org.mozilla.javascript"
    />
  </target>

  <target name="copy-examples" depends="init">
    <mkdir dir="${dist.dir}/examples"/>
    <copy todir="${dist.dir}/examples">
      <fileset dir="examples" includes="**/*.java,**/*.js,**/*.html" />
    </copy>
  </target>

  <target name="copy-misc" depends="init">
    <filter token="datestamp" value="${TODAY}"/>
    <copy todir="${dist.dir}" filtering="yes">
      <fileset dir=".">
        <patternset>
          <include name="build-date"/>
        </patternset>
      </fileset>
    </copy>
  </target>

  <target name="copy-all" depends="copy-source,copy-examples,copy-misc">
  </target>

  <target name="javadoc" depends="init">
    <mkdir dir="${dist.dir}/javadoc"/>
    <javadoc sourcefiles="${apiClasses}"
             sourcepath="src"
             destdir="${dist.dir}/javadoc"
             version="true"
             author="true"
             windowtitle="${Name}" />
  </target>

  <target name="dev-javadoc" depends="init">
    <mkdir dir="${dist.dir}/javadoc"/>
    <javadoc sourcepath="src"
             destdir="${dist.dir}/javadoc"
             version="true"
             package="true"
             author="true"
             windowtitle="${Name}">
       <fileset
         dir="."
         includes="**/*.java"
         excludes="**/deprecatedsrc/**/*.java,**/testsrc/**/*.java"
       />
    </javadoc>
  </target>

  <!--
    Compiles and tests all sources and then creates the distribution file
  -->
  <target name="all" depends="deepclean,compile-all,junit-all,dist">
  </target>

  <target name="dist" depends="deepclean,jar,retrojar,copy-all,javadoc">
    <delete file="${dist.file}" />
    <zip destfile="${dist.file}">
      <fileset dir="${build.dir}" includes="${dist.name}/**"/>
    </zip>
  </target>

  <target name="source-zip" depends="copy-source,copy-examples,javadoc">
    <delete file="${dist.source-only-zip}" />
    <zip destfile="${dist.source-only-zip}">
      <zipfileset prefix="${dist.name}" dir="${dist.dir}">
        <include name="*src/**"/>
        <include name="build.xml"/>
        <include name="*.properties"/>
        <include name="examples/**"/>
      </zipfileset>
    </zip>
  </target>

  <target name="compile-debugger">
    <ant antfile="toolsrc/build.xml" target="compile-debugger"/>
  </target>

  <target name="clean" depends="properties">
    <delete quiet="true" file="${dist.dir}/${rhino.jar}"/>
    <delete quiet="true" file="${dist.dir}/${small-rhino.jar}"/>
    <delete quiet="true" dir="${build.dir}"/>
  </target>

  <target name="deepclean" depends="properties">
    <delete quiet="true" dir="${build.dir}"/>
    <delete quiet="true" file="${dist.file}"/>
    <delete quiet="true" file="${dist.source-only-zip}"/>
  </target>

  <!--
    The next two targets run the JavaScript Test Library tests.  Note that these tests are quite extensive and take a long time
    to run.  They are not documented in the 'help' target for now.
  -->

  <!--
    Run the tests using JUnit.  Beware that if you are using Ant from the command-line, there are some difficulties you may
    encounter setting this up correctly; see http://ant.apache.org/faq.html#delegating-classloader

    IDEs that use Ant as the build system probably handle this fine.
  -->
  <target name="junit-all" depends="compile">
    <ant antfile="testsrc/build.xml" target="junit-coveragereport"/>
  </target>

  <!--
  Run the tests using the Java port of jsdriver.pl.  Note that running this port
  from the command-line may be more useful running this Ant target, as running
  from the command line allows configuration options, such as running with a
  non-default optimization level, or running only a subset of the tests.
  -->
  <target name="jsdriver-run" depends="compile">
      <ant antfile="testsrc/build.xml" target="jsdriver" />
  </target>

  <!--
    Compile the JsDriver test driver.
  -->
  <target name="jsdriver" depends="compile">
      <ant antfile="testsrc/build.xml" target="clean" />
      <ant antfile="testsrc/build.xml" target="compile" />
  </target>

  <target name="benchmark-v8-opt-1" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}" dir="testsrc/benchmarks/v8-benchmarks-v5" fork="true">
      <jvmarg value="-server"/>
      <arg line="-opt -1 run.js"/>
    </java>
  </target>

  <target name="benchmark-v8-opt0" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}" dir="testsrc/benchmarks/v8-benchmarks-v5" fork="true">
      <jvmarg value="-server"/>
      <arg line="-opt 0 run.js"/>
    </java>
  </target>

  <target name="benchmark-v8-opt9" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}" dir="testsrc/benchmarks/v8-benchmarks-v5" fork="true">
      <jvmarg value="-server"/>
      <arg line="-opt 9 run.js"/>
    </java>
  </target>

  <target name="benchmark-sunspider-opt-1" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}" dir="testsrc/benchmarks/sunspider-0.9.1" fork="true">
      <jvmarg value="-server"/>
      <arg line="-opt -1 run.js"/>
    </java>
  </target>

  <target name="benchmark-sunspider-opt0" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}" dir="testsrc/benchmarks/sunspider-0.9.1" fork="true">
      <jvmarg value="-server"/>
      <arg line="-opt 0 run.js"/>
    </java>
  </target>

  <target name="benchmark-sunspider-opt9" depends="jar">
    <property name="jarfile" location="${dist.dir}/${rhino.jar}"/>
    <java jar="${jarfile}" dir="testsrc/benchmarks/sunspider-0.9.1" fork="true">
      <jvmarg value="-server"/>
      <arg line="-opt 9 run.js"/>
    </java>
  </target>

  <target name="help" depends="properties">
<echo>The following targets are available with this build file:

 clean       remove all compiled classes and copied property files

 compile     compile classes and copy all property files
             into ${classes} directory
             excluding deprecated code

 compile-all compile all classes and copy all property files
             into ${classes} directory
             including deprecated code

 deepclean   remove all generated files and directories

 dist        create ${dist.file} with full Rhino distribution

 help        print this help

 jar         create ${rhino.jar} in ${dist.dir}

 smalljar    create ${small-rhino.jar} in ${dist.dir} with
             minimalist set of Rhino classes. See footprint.html
             from the doc directory for details.

 javadoc     generate Rhino API documentation
             in ${dist.dir}/javadoc

 source-zip  create ${dist.source-only-zip} with all Rhino
             source files necessary to recreate ${dist.file}
</echo>
  </target>

</project>

```

## Build configuration lib/rhino/deprecatedsrc/build.xml

```text
<?xml version="1.0" encoding="utf-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->


<project name="src" default="compile" basedir="..">

  <property file="build.properties"/>

  <target name="compile">
    <javac srcdir="deprecatedsrc"
           destdir="${classes}"
           includes="org/mozilla/javascript/*.java"
           deprecation="on"
           debug="${debug}"
           includeAntRuntime="false"
	   target="${target-jvm}"
 	   source="${source-level}"
	   >
    </javac>
  </target>

  <target name="copy-source">
    <mkdir dir="${dist.dir}/deprecatedsrc"/>
    <copy todir="${dist.dir}/deprecatedsrc">
      <fileset dir="deprecatedsrc"
               includes="**/*.java,**/*.properties,**/*.xml,manifest"/>
    </copy>
  </target>

</project>

```

## Build configuration lib/rhino/src/build.xml

```text
<?xml version="1.0" encoding="utf-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->


<!--
Build file for Rhino using Ant (see http://jakarta.apache.org/ant/index.html)
Requires Ant version 1.2
-->
<project name="src" default="compile" basedir="..">

  <property file="build.properties"/>

  <available property="jdk15"
             classname="java.lang.reflect.ParameterizedType" />

  <target name="compile" depends="compile-most,compile-jdk15">
  </target>

  <target name="shell" depends="compile">
    <java classname="org.mozilla.javascript.tools.shell.Main"
          classpath="${classes}"
          fork="true">
      <arg line="-version 170"/>
    </java>
  </target>

  <target name="compile-most">
    <javac srcdir="src"
           destdir="${classes}"
           includes="org/**/*.java"
           excludes="org/**/jdk15/*.java"
           deprecation="on"
           debug="${debug}"
           includeAntRuntime="false"
           target="${target-jvm}"
           source="${source-level}" />
    <copy todir="${classes}">
      <fileset dir="src" includes="org/**/*.properties" />
      <filterset>
      <filter token="IMPLEMENTATION.VERSION"
              value="${implementation.version}"/>
      </filterset>
    </copy>
  </target>

  <target name="compile-jdk15" if="jdk15">
    <javac srcdir="src"
           destdir="${classes}"
           includes="org/**/jdk15/*.java"
           deprecation="on"
           debug="${debug}"
           includeAntRuntime="false"
           target="${target-jvm}"
           source="${source-level}" />
  </target>

  <target name="copy-source">
    <mkdir dir="${dist.dir}/src"/>
    <copy todir="${dist.dir}/src">
      <fileset dir="src"
               includes="**/*.java,**/*.properties,**/*.xml,manifest"/>
    </copy>
  </target>

  <target name="clean">
    <delete includeEmptyDirs="true">
      <fileset dir="${classes}"
               excludes="org/mozilla/javascript/tools/**"/>
    </delete>
  </target>

</project>

```

## Build configuration lib/rhino/testsrc/build.xml

```text
<?xml version="1.0" encoding="UTF-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->

<project name="testsrc" basedir="..">
  <!--
    Location of mozilla/js/tests directory
  -->
  <property name="test.library.dir" location="${build.dir}/tests" />
  <property name="test.jstests.jar" location="${test.library.dir}/src/jstests.jar" />

  <!--
    URL from which junit.jar can be retrieved
  -->
  <property name="test.junit.url" value="http://repo1.maven.org/maven2/junit/junit/4.10/junit-4.10.jar"/>

  <!--
    URL from which emma.jar can be retrieved
  -->
  <property name="test.emma.url" value="http://repo1.maven.org/maven2/emma/emma/2.1.5320/emma-2.1.5320.jar"/>

  <!--
    URL from which emma_ant.jar can be retrieved
  -->
  <property name="test.emma_ant.url" value="http://repo1.maven.org/maven2/emma/emma_ant/2.1.5320/emma_ant-2.1.5320.jar"/>

  <!--
    Destination to which testing classes should be built
  -->
  <property name="test.classes" value="${build.dir}/test/classes" />

  <!--
    Output directory for HTML files generated by jsdriver
  -->
  <property name="test.output" value="${build.dir}/test/output" />

  <!--
    Timeout in milliseconds for tests
  -->
  <property name="test.timeout" value="60000" />

  <!--
    Maximum heap size for VM executing test cases.
  -->
  <property name="test.vm.mx" value="256m" />

  <target name="get-junit">
    <mkdir dir="lib"/>
    <get src="${test.junit.url}" dest="lib/junit.jar" usetimestamp="true"/>
  </target>

  <target name="junit-compile">
    <mkdir dir="${test.classes}" />
    <antcall target="get-junit"/>
    <javac
      destdir="${test.classes}" debug="true"
      target="${target-jvm}"
      source="${source-level}"
    >
      <classpath>
        <pathelement path="lib/junit.jar" />
        <pathelement path="${classes}" />
      </classpath>
        <src path="testsrc"/>
        <src path="examples"/>
    </javac>
    <antcall target="copy-files" />
  </target>

  <target name="compile">
    <mkdir dir="${test.classes}" />
	<antcall target="get-junit"/>
    <javac
      srcdir="testsrc"
      destdir="${test.classes}" debug="true"
      target="${target-jvm}"
      source="${source-level}"
    >
      <classpath>
        <pathelement path="lib/junit.jar" />
        <pathelement path="${classes}" />
      </classpath>
      <sourcepath path="testsrc" />
        <include name="org/mozilla/javascript/drivers/JsDriver.java" />
    </javac>
    <antcall target="copy-files" />
  </target>

  <target name="copy-files">
    <copy todir="${test.classes}">
       <fileset dir="testsrc">
         <exclude name="**/*.java build.xml"/>
       </fileset>
    </copy>
    <gunzip src="testsrc/tests.tar.gz" dest="${build.dir}/tests.tar"/>
    <untar src="${build.dir}/tests.tar" dest="build"/>
  </target>
  	
  <target name="clean">
    <delete dir="${test.classes}" />
  </target>

  <target name="coverage-instrument">
    <get src="${test.emma.url}" dest="lib/emma.jar" usetimestamp="true"/>
    <get src="${test.emma_ant.url}" dest="lib/emma_ant.jar" usetimestamp="true"/>
    <property name="coverage.dir" location="${build.dir}/coverage"/>
    <property name="coverage.classes.dir" location="${build.dir}/coverage/classes"/>
    <mkdir dir="${coverage.classes.dir}"/>
    <path id="emma.lib">
      <pathelement location="lib/emma.jar" />
      <pathelement location="lib/emma_ant.jar" />
    </path>
    <taskdef resource="emma_ant.properties" classpathref="emma.lib" />
    <property name="coverage.instrumentationfile" location="${coverage.dir}/instrumentation"/>
    <emma enabled="true">
      <instr 
        instrpath="${classes}" outdir="${coverage.classes.dir}" 
        outfile="${coverage.instrumentationfile}" mode="copy"/>
    </emma>
    <copy todir="${coverage.classes.dir}">
      <fileset dir="src" excludes="**/*.java"/>
    </copy>
    <copy todir="${coverage.classes.dir}">
      <fileset dir="${classes}"/>
    </copy>
    <property name="coverage.outfile" location="${coverage.dir}/coverage"/>
  </target>
	
  <target name="junit" depends="junit-compile,coverage-instrument">
    <junit printsummary="on" fork="true" forkmode="once" maxmemory="${test.vm.mx}" showoutput="true"
    	failureproperty="junitFailed">
      <sysproperty key="java.awt.headless" value="true" />
      <sysproperty key="mozilla.js.tests" value="${test.library.dir}" />
      <sysproperty key="mozilla.js.tests.timeout" value="${test.timeout}" />
      <sysproperty key="emma.coverage.out.file" value="${coverage.outfile}"/>
      <!-- mozilla test cases are locale dependent! -->
      <sysproperty key="user.language" value="en"/>
      <sysproperty key="user.country" value="US"/>
      <sysproperty key="user.timezone" value="America/Los_Angeles"/>
      <classpath>
        <pathelement location="${xbean.jar}"/>
        <pathelement location="${jsr173.jar}"/>
        <pathelement path="${coverage.classes.dir}" />
        <pathelement path="${classes}" />
        <pathelement path="${test.classes}" />
        <pathelement path="lib/emma.jar"/>
        <pathelement path="lib/junit.jar" />
        <pathelement location="${test.jstests.jar}"/>
      </classpath>
      <batchtest todir="build/test">
        <fileset dir="${test.classes}" includes="**/tests/**/*Test.class"/>
        <fileset dir="${test.classes}" includes="**/StandardTests.class"/>
      </batchtest>
      <formatter type="xml"/>
      <assertions>
        <enable/>
      </assertions>
    </junit>
    <mkdir dir="build/test/report"/>
    <junitreport todir="build/test/report">
       <fileset dir="build/test" includes="*.xml"/>
       <report todir="build/test/report"/>
    </junitreport>
  </target>

  <target name="junit-coveragereport" depends="junit">
    <property name="coverage.report.dir" location="${build.dir}/coverage/report"/>
    <mkdir dir="${coverage.report.dir}"/>
    <delete dir="${coverage.report.dir}"/>
    <mkdir dir="${coverage.report.dir}"/>
    <emma enabled="true">
      <report>
        <fileset dir="${basedir}">
          <include name="build/coverage/instrumentation"/>
          <include name="build/coverage/coverage"/>
        </fileset>
        <sourcepath>
          <dirset dir="${basedir}">
            <include name="src"/>
          </dirset>
        </sourcepath>
        <html outfile="${coverage.report.dir}/index.html"/>
      </report>
    </emma>
  	<fail if="junitFailed" message="JUnit test(s) failed"/>
  </target>

  <target name="jsdriver" depends="compile">
    <tstamp>
      <format property="test.timestamp" pattern="yyyy.MM.dd.HH.mm.ss" />
    </tstamp>
    <mkdir dir="${test.output}" />
    <java
      fork="true"
      classname="org.mozilla.javascript.drivers.JsDriver"
      maxmemory="${test.vm.mx}"
    >
      <classpath>
        <pathelement location="${xbean.jar}"/>
        <pathelement location="${jsr173.jar}"/>
        <pathelement path="${classes}" />
        <pathelement path="${test.classes}" />
        <pathelement location="${test.jstests.jar}"/>
      </classpath>
      <arg value="-p" />
      <arg file="${test.library.dir}" />
      <arg value="-f" />
      <arg value="${test.output}/rhino-test-results.${test.timestamp}.html" />
      <arg value="--timeout" />
      <arg value="${test.timeout}" />
    </java>
  </target>

  <target name="copy-source">
    <mkdir dir="${dist.dir}/testsrc"/>
    <copy todir="${dist.dir}/testsrc">
      <fileset dir="testsrc">
        <include name="**/*.java"/>
        <include name="**/*.js"/>
        <include name="**/*.jstest"/>
        <include name="**/*.doctest"/>
        <include name="**/*.properties"/>
        <include name="**/*.xml"/>
        <include name="**/*.html"/>
        <include name="**/*.jar"/>
        <include name="*.skip"/>
        <include name="*.tests"/>
        <include name="tests.tar.gz"/>
      </fileset>
    </copy>
  </target>
</project>

```

## Build configuration lib/rhino/toolsrc/build.xml

```text
<?xml version="1.0" encoding="utf-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->


<!--
Build file for Rhino using Ant (see http://jakarta.apache.org/ant/index.html)
Requires Ant version 1.2
-->
<project name="toolsrc" default="compile" basedir="..">

  <target name="properties">
    <property file="build.properties"/>
  </target>

  <target name="compile" depends="properties">
    <javac srcdir="toolsrc"
           destdir="${classes}"
           includes="org/**/*.java"
           deprecation="on"
           debug="${debug}"
           includeAntRuntime="false"
           target="${target-jvm}"
           source="${source-level}"
    >
    </javac>
    <copy todir="${classes}">
      <fileset dir="toolsrc" includes="org/**/*.properties" />
    </copy>
  </target>

  <target name="copy-source" depends="properties">
    <mkdir dir="${dist.dir}/toolsrc"/>
    <copy todir="${dist.dir}/toolsrc">
      <fileset dir="toolsrc"
               includes="**/*.java,**/*.properties,**/*.xml" />
    </copy>
  </target>

  <target name="clean" depends="properties">
    <delete includeEmptyDirs="true">
      <fileset dir="${classes}"
               includes="org/mozilla/javascript/tools/**"/>
    </delete>
  </target>

</project>

```

## Build configuration lib/rhino/toolsrc/org/mozilla/javascript/tools/debugger/build.xml

```text
<?xml version="1.0" encoding="utf-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->


<project name="toolsrc" default="help" basedir=".">

  <target name="properties">
    <property name="swing-ex-url" value="http://java.sun.com/products/jfc/tsc/articles/treetable2/downloads/src.zip"/>
    <available file="downloaded/AbstractCellEditor.java"
               property="swing-ex-available"/>
  </target>

  <target name="get-swing-ex" unless="swing-ex-available">
    <!-- Download source from Sun's site, unzip it, remove
         the files we don't need, and change the package
    -->
    <mkdir dir="downloaded"/>
    <get src="${swing-ex-url}" dest="downloaded/swingExSrc.zip"/>
    <unzip src="downloaded/swingExSrc.zip" dest="downloaded/">
      <patternset>
          <include name="AbstractCellEditor.java"/>
          <include name="JTreeTable.java"/>
          <include name="TreeTableModel.java"/>
          <include name="TreeTableModelAdapter.java"/>
      </patternset>
    </unzip>
    <replace file="downloaded/AbstractCellEditor.java">
       <replacetoken>import java.awt.Component;</replacetoken>
       <replacevalue>
package org.mozilla.javascript.tools.debugger.downloaded;
       </replacevalue>
    </replace>
    <replace file="downloaded/AbstractCellEditor.java">
       <replacetoken>import java.awt.event.*;</replacetoken>
       <replacevalue></replacevalue>
    </replace>
    <replace file="downloaded/AbstractCellEditor.java">
       <replacetoken>import java.awt.AWTEvent;</replacetoken>
       <replacevalue></replacevalue>
    </replace>
    <replace file="downloaded/AbstractCellEditor.java">
       <replacetoken>import java.io.Serializable;</replacetoken>
       <replacevalue></replacevalue>
    </replace>
    <replace file="downloaded/JTreeTable.java">
       <replacetoken>import javax.swing.*;</replacetoken>
       <replacevalue>
         package org.mozilla.javascript.tools.debugger.downloaded;
         import javax.swing.*;
       </replacevalue>
    </replace>
    <replace file="downloaded/JTreeTable.java">
       <replacetoken>class ListToTreeSelectionModelWrapper</replacetoken>
       <replacevalue>public class ListToTreeSelectionModelWrapper</replacevalue>
    </replace>
    <replace file="downloaded/JTreeTable.java">
       <replacetoken>ListSelectionModel getListSelectionModel</replacetoken>
       <replacevalue>public ListSelectionModel getListSelectionModel</replacevalue>
    </replace>
    <replace file="downloaded/JTreeTable.java">
       <replacetoken>import java.awt.Rectangle;</replacetoken>
       <replacevalue></replacevalue>
    </replace>
    <replace file="downloaded/TreeTableModel.java">
       <replacetoken>import javax.swing.tree.TreeModel;</replacetoken>
       <replacevalue>
         package org.mozilla.javascript.tools.debugger.downloaded;
         import javax.swing.tree.TreeModel;
       </replacevalue>
    </replace>
    <replace file="downloaded/TreeTableModelAdapter.java">
       <replacetoken>import javax.swing.JTree;</replacetoken>
       <replacevalue>
         package org.mozilla.javascript.tools.debugger.downloaded;
         import javax.swing.JTree;
       </replacevalue>
    </replace>
    <delete file="downloaded/swingExSrc.zip"/>
  </target>

  <target name="download" depends="properties,get-swing-ex"/>

  <target name="help" depends="properties">
<echo>The following targets are available with this build file:

 download    Download ${swing-ex-url}
             and extract the necessary files from it.

 help        Print this help.

</echo>
  </target>

</project>

```

## Build configuration lib/rhino/xmlimplsrc/build.xml

```text
<?xml version="1.0" encoding="utf-8"?>
<!-- This Source Code Form is subject to the terms of the Mozilla Public
   - License, v. 2.0. If a copy of the MPL was not distributed with this
   - file, You can obtain one at http://mozilla.org/MPL/2.0/. -->

<project name="xmlimplsrc" basedir=".." default="compile">
  <!--
    Properties which affect this build file:

    no-e4x: Will cause E4X not to be built
    no-xmlbeans: Will cause the old, XMLBeans-based implementation of E4X not to be built
  -->

  <property file="build.local.properties"/>
  <property file="build.properties"/>

  <!--
    Provide support for the old name for skipping E4X compilation, in case someone is still using it
  -->
  <condition property="no-e4x">
    <isset property="without-xmlimpl" />
  </condition>

  <path id="xmlbeans.classpath">
    <pathelement location="${xbean.jar}"/>
    <pathelement location="${jsr173.jar}"/>
  </path>

  <target name="compile" unless="no-e4x">
    <antcall target="e4x-compile" />
    <antcall target="no-e4x-compile" />

    <antcall target="old-e4x" />
  </target>

  <available property="jdk1.5?" classname="java.lang.ProcessBuilder" />

  <target name="e4x-compile" if="jdk1.5?">
    <javac
      srcdir="xmlimplsrc"
      destdir="${classes}"
      deprecation="on"
      debug="${debug}"
                        includeAntRuntime="false"
      target="${target-jvm}"
      source="${source-level}"
    />
  </target>

  <target name="no-e4x-compile" unless="jdk1.5?">
    <echo>
      Skipping DOM E4X implementation; JDK 1.5+ currently required for compilation.
      <!--
        If the compiler is outfitted with DOM3 using the endorsed standards
        override mechanism, presumably the code could be built under 1.4.
        Not tested.
      -->
    </echo>
  </target>

  <target name="old-e4x" unless="no-xmlbeans">
    <antcall target="old-e4x-compile" />
    <antcall target="no-old-e4x-compile" />
  </target>

  <target name="old-e4x-compile" depends="xmlbeans-unzip">
    <echo>Compiling XMLBeans E4X implementation using ${xbean.jar} and ${jsr173.jar}</echo>
    <javac srcdir="deprecatedsrc"
           destdir="${classes}"
           includes="org/mozilla/javascript/xml/impl/xmlbeans/*.java"
           deprecation="on"
           debug="${debug}"
           classpathref="xmlbeans.classpath"
           failonerror="${xmlimpl.compile.failonerror}"
           includeAntRuntime="false"
           target="${target-jvm}"
           source="${source-level}" />
  </target>

  <target name="no-old-e4x-compile" if="no-xmlbeans">
    <echo>
      Skipping compilation of XMLBeans E4X implementation due to missing XMLBeans files
    </echo>
  </target>

  <target name="copy-source">
    <mkdir dir="${dist.dir}/xmlimplsrc"/>
    <copy todir="${dist.dir}/xmlimplsrc">
      <fileset dir="xmlimplsrc"
               includes="**/*.java,**/*.properties,**/*.xml"
      />
    </copy>
  </target>

  <target name="clean">
    <delete includeEmptyDirs="true">
      <fileset dir="${classes}"
               includes="org/mozilla/javascript/xmlimpl/**"
      />
    </delete>
  </target>

  <property name="xmlbeans.tmp" location="${build.dir}/tmp-xbean" />
  <property name="xmlbeans.zip" location="${xmlbeans.tmp}/xbean.zip" />

  <condition property="xmlbeans-present?">
    <and>
      <available file="${xbean.jar}" />
      <available file="${jsr173.jar}" />
    </and>
  </condition>

  <condition property="xmlbeans-zip-present?">
    <available file="${xmlbeans.zip}" />
  </condition>

  <target name="xmlbeans-get" unless="xmlbeans-zip-present?">
    <property
      name="xmlbeans.url"
      value="http://www.apache.org/dist/xmlbeans/binaries/xmlbeans-2.5.0.zip"
    />

    <mkdir dir="${xmlbeans.tmp}" />
    <get src="${xmlbeans.url}" dest="${xmlbeans.zip}" ignoreerrors="true" />
  </target>

  <target name="xmlbeans-unzip" unless="xmlbeans-present?">
    <antcall target="xmlbeans-get" />
    <unzip src="${xmlbeans.zip}" dest="${xmlbeans.tmp}" />
    <copy tofile="${xbean.jar}" file="${xmlbeans.tmp}/xmlbeans-2.5.0/lib/xbean.jar" />
    <copy tofile="${jsr173.jar}" file="${xmlbeans.tmp}/xmlbeans-2.5.0/lib/jsr173_1.0_api.jar" />
    <delete dir="${xmlbeans.tmp}" />
  </target>
</project>

```
