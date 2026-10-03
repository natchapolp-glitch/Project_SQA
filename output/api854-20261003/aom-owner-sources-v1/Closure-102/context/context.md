## build.xml

```
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
<project name="compiler" basedir="." default="jar">
<property name="ant.build.javac.target" value="1.6"/>
<property name="ant.build.javac.source" value="1.6"/>

  <!-- define other variables -->
  <property name="src.dir" value="${basedir}/src" />
  <property name="gen.dir" value="${basedir}/gen" />
  <property name="test.dir" value="${basedir}/test" />
  <property name="externs.dir" value="${basedir}/externs" />
  <property name="build.dir" value="${basedir}/build" />
  <property name="classes.dir" value="${build.dir}/classes" />
  <property name="testClasses.dir" value="${build.dir}/test" />
  <property name="flags.xml" value="${build.dir}/flags.xml" />
  <property name="javadoc.dir" value="${build.dir}/javadoc" />
  <property name="stylesheetfile" value="" />
  <property name="lib.dir" value="${basedir}/lib" />
  <property name="tools.dir" value="${basedir}/tools" />
  <property name="jarfile" value="${build.dir}/${ant.project.name}.jar" />
  <property name="num-fuzz-tests" value="10000"/>

  <!-- set the classpath for the project              -->
  <!-- this includes the generated source class files -->
  <!-- and every jar in the /lib directory            -->
  <path id="classpath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="*.jar" />
    </fileset>
  </path>

  <target name="clean" description="delete generated files">
    <delete dir="${build.dir}" />
  </target>

  <target name="compile" description="compile the source code">
    <mkdir dir="${classes.dir}" />
    <javac debug="true" srcdir="${gen.dir}"
           destdir="${classes.dir}"
           excludes=".svn">
      <classpath refid="classpath.path" />
    </javac>
    <javac debug="true" srcdir="${src.dir}"
           destdir="${classes.dir}"
           excludes=".svn">
      <classpath refid="classpath.path" />
    </javac>

    <!-- Move Messages.properties where ScriptRuntime.java expects it. -->
    <mkdir dir="${classes.dir}/rhino_ast/java/com/google/javascript/rhino/" />
    <copy file="${src.dir}/com/google/javascript/rhino/Messages.properties"
          todir="${classes.dir}/rhino_ast/java/com/google/javascript/rhino/" />

    <!-- Move ParserConfig.properties where ParserRunner.java expects it. -->
    <copy file="${src.dir}/com/google/javascript/jscomp/parsing/ParserConfig.properties"
          todir="${classes.dir}/com/google/javascript/jscomp/parsing" />

    <!-- Move runtime_type_check.js where RuntimeTypeCheck.java expects it. -->
    <mkdir dir="${classes.dir}/com/google/javascript/jscomp/js" />
    <copy file="${src.dir}/com/google/javascript/jscomp/js/runtime_type_check.js"
          todir="${classes.dir}/com/google/javascript/jscomp/js" />
  </target>

  <target name="apt">
    <mkdir dir="${build.dir}" />
    <apt srcdir="${src.dir}"
         compile="false"
         factory="com.google.common.flags.FlagProcessorFactory"
         >
      <classpath>
        <pathelement location="${lib.dir}/google_common_deploy.jar" />
        <pathelement location="${tools.dir}/FlagProcessorFactory_deploy.jar" />
      </classpath>
      <compilerarg value="-Aflags.path=${flags.xml}" />
    </apt>
  </target>

  <target name="jar"
          depends="compile,apt"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" />
    <jar destfile="${jarfile}" update="true">
      <zipfileset src="${lib.dir}/google_common_deploy.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/protobuf_deploy.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/libtrunk_rhino_parser_jarjared.jar"
                  excludes="flags.xml" />
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="flags.xml,externs.zip" />
      <manifest>
        <attribute name="Main-Class"
                   value="com.google.javascript.jscomp.CompilerRunner" />
      </manifest>
    </jar>
  </target>

  <target name="compile-tests"
          depends="compile"
          description="compile the JUnit tests">
    <mkdir dir="${testClasses.dir}" />
    <javac debug="true" srcdir="${test.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn">
      <classpath refid="classpath.path" />
    </javac>
  </target>

  <target name="all-classes-jar"
          depends="compile,compile-tests,apt"
          description="package the compiler and its tests into one jar">
    <jar destfile="${jarfile}" update="true">
      <zipfileset src="${lib.dir}/ant_deploy.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/junit4-core.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/junit4-legacy.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/hamcrest-core-1.1.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/google_common_deploy.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/protobuf_deploy.jar"
                  excludes="flags.xml" />
      <zipfileset src="${lib.dir}/libtrunk_rhino_parser_jarjared.jar"
                  excludes="flags.xml" />
      <fileset dir="${classes.dir}" />
      <fileset dir="${testClasses.dir}" />
    </jar>
  </target>

  <target name="test-forkless"
      depends="compile-tests"
      description="execute JUnit tests without forking the process">
    <junit>
      <classpath refid="classpath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <formatter type="plain" usefile="false" />
      <batchtest>
        <fileset dir="${build.dir}/test">
          <include name="**/*Test.class" />
        </fileset>
      </batchtest>
    </junit>
  </target>

  <target name="test"
          depends="compile-tests"
          description="compile and execute JUnit tests">
    <junit printsummary="on" fork="true" forkmode="once" showoutput="true">
      <classpath refid="classpath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <formatter type="plain" usefile="false" />
      <batchtest>
        <fileset dir="${build.dir}/test">
          <include name="**/*Test.class" />
        </fileset>
      </batchtest>
    </junit>
  </target>

  <target name="fuzz-test"
          depends="all-classes-jar"
          description="checks the compiler against a variety of js programs">
      <exec executable="java" failonerror="true">
        <arg value="-cp" />
        <arg value="${jarfile}" />
        <arg value="com.google.javascript.jscomp.regtests.CompileEachLineOfProgramOutput" />
        <arg value="generatejs"/>
        <arg value="--stdout"/>
        <arg value="${num-fuzz-tests}"/>
      </exec>
  </target>

  <target name="javadoc"
          description="generate Javadoc">
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
      <classpath refid="classpath.path" />
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

</project>

```

## src/com/google/javascript/jscomp/Normalize.java

```
/*
 * Copyright 2008 Google Inc.
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Map;


/**
 * The goal with this pass is to simplify the other passes,
 * by making less complex statements.
 *
 * Starting with statements like:
 *   var a = 0, b = foo();
 *
 * Which become:
 *   var a = 0;
 *   var b = foo();
 *
 * The key here is only to break down things that help the other passes
 * and can be put back together in a form that is at least as small when
 * all is said and done.
 *
 * This pass currently does the following:
 * 1) Splits var statements contains multiple declarations into individual
 * statements.
 * 2) Splits chained assign statements such as "a = b = c = 0" into individual
 * statements.  These are split as follows "c = 0; b = c; a = b". Unfortunately,
 * not all such statements can be broken up, for instance:
 *   "a[next()] = a[next()] = 0"
 * can not be made into
 *   "a[next()] = 0; a[next()] = a[next()];
 * 3) init expressions in FOR statements are extracted and placed before the
 * statement. For example: "for(var a=0;;);" becomes "var a=0;for(;;);"
 * 4) WHILE statements are converted to FOR statements. For example:
 * "while(true);" becomes "for(;true;);"
 * 5) Renames constant variables, as marked with an IS_CONSTANT_NAME annotation,
 *   to include a suffix of NodeUtil.CONSTANT_MARKER which is used by constant
 *   inlining.
 *
*
 */
// public for ReplaceDebugStringsTest
class Normalize implements CompilerPass, Callback {

  private final AbstractCompiler compiler;
  private final boolean assertOnChange;
  private static final boolean CONVERT_WHILE_TO_FOR = true;
  static final boolean MAKE_LOCAL_NAMES_UNIQUE = true;

  Normalize(AbstractCompiler compiler, boolean assertOnChange) {
    this.compiler = compiler;
    this.assertOnChange = assertOnChange;
  }

  private void reportCodeChange(String changeDescription) {
    if (assertOnChange) {
      throw new IllegalStateException(
          "Normalize constraints violated:\n" + changeDescription);
    }
    compiler.reportCodeChange();
  }

  @Override
  public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, this);
    removeDuplicateDeclarations(root);
    if (MAKE_LOCAL_NAMES_UNIQUE) {
      MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
      NodeTraversal t = new NodeTraversal(compiler, renamer);
      t.traverseRoots(externs, root);
    }
    new PropogateConstantAnnotations(compiler, assertOnChange)
        .process(externs, root);
  }

  @Override
  public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    doStatementNormalizations(t, n, parent);

    return true;
  }

  public static class PropogateConstantAnnotations
      extends AbstractPostOrderCallback
      implements CompilerPass {
    private final AbstractCompiler compiler;
    private final boolean assertOnChange;

    public PropogateConstantAnnotations(
        AbstractCompiler compiler, boolean forbidChanges) {
      this.compiler = compiler;
      this.assertOnChange = forbidChanges;
    }

    @Override
    public void process(Node externs, Node root) {
      new NodeTraversal(compiler, this).traverseRoots(externs, root);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Note: Constant properties annotations are not propagated.
      if (n.getType() == Token.NAME) {
        if (n.getString().isEmpty()) {
          return;
        }

        JSDocInfo info = null;
        // Find the JSDocInfo for a top level variable.
        Var var = t.getScope().getVar(n.getString());
        if (var != null) {
          info = var.getJSDocInfo();
        }

        if ((info != null && info.isConstant()) &&
            !n.getBooleanProp(Node.IS_CONSTANT_NAME)) {
          n.putBooleanProp(Node.IS_CONSTANT_NAME, true);
          if (assertOnChange) {
            String name = n.getString();
            throw new IllegalStateException(
                "Unexpected const change.\n" +
                "  name: "+ name + "\n" +
                "  gramps:" + n.getParent().getParent().toStringTree());
          }
          // Even though the AST has changed (an annotation was added),
          // the annotations are not compared so don't report the change.
          // reportCodeChange("constant annotation");
        }
      }
    }
  }

  /**
   * Walk the AST tree and verify that constant names are used consistently.
   */
  static class VerifyConstants extends AbstractPostOrderCallback
      implements CompilerPass {

    final private AbstractCompiler compiler;
    final private boolean checkUserDeclarations;

    VerifyConstants(AbstractCompiler compiler, boolean checkUserDeclarations) {
      this.compiler = compiler;
      this.checkUserDeclarations = checkUserDeclarations;
    }

    @Override
    public void process(Node externs, Node root) {
      Node externsAndJs = root.getParent();
      Preconditions.checkState(externsAndJs != null);
      Preconditions.checkState(externsAndJs.hasChild(externs));

      NodeTraversal.traverseRoots(
          compiler, Lists.newArrayList(externs, root), this);
    }

    private Map<String,Boolean> constantMap = Maps.newHashMap();

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.NAME) {
        String name = n.getString();
        if (n.getString().isEmpty()) {
          return;
        }

        boolean isConst = n.getBooleanProp(Node.IS_CONSTANT_NAME);
        if (checkUserDeclarations) {
          boolean expectedConst = false;
          if (NodeUtil.isConstantName(n)
              || compiler.getCodingConvention().isConstant(n.getString())) {
            expectedConst = true;
          } else {
            expectedConst = false;

            JSDocInfo info = null;
            Var var = t.getScope().getVar(n.getString());
            if (var != null) {
              info = var.getJSDocInfo();
            }

            if (info != null && info.isConstant()) {
              expectedConst = true;
            } else {
              expectedConst = false;
            }
          }

          if (expectedConst) {
            Preconditions.checkState(expectedConst == isConst,
                "The name " + name + " is not annotated as constant.");
          } else {
            Preconditions.checkState(expectedConst == isConst,
                "The name " + name + " should not be annotated as constant.");
          }
        }

        Boolean value = constantMap.get(name);
        if (value == null) {
          constantMap.put(name, isConst);
        } else {
          Preconditions.checkState(value.booleanValue() == isConst,
              "The name " + name + " is not consistently annotated as " +
              "constant.");
        }
      }
    }
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    switch (n.getType()) {
      case Token.WHILE:
        if (CONVERT_WHILE_TO_FOR) {
          Node expr = n.getFirstChild();
          n.setType(Token.FOR);
          n.addChildBefore(new Node(Token.EMPTY), expr);
          n.addChildAfter(new Node(Token.EMPTY), expr);
          reportCodeChange("WHILE node");
        }
        break;
    }
  }

  /**
   * Do normalizations that introduce new siblings or parents.
   */
  private void doStatementNormalizations(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.LABEL) {
      normalizeLabels(n);
    }

    // Only inspect the children of SCRIPTs, BLOCKs and LABELs, as all these
    // are the only legal place for VARs and FOR statements.
    if (NodeUtil.isStatementBlock(n) || n.getType() == Token.LABEL) {
      extractForInitializer(n, null, null);
    }

    // Only inspect the children of SCRIPTs, BLOCKs, as all these
    // are the only legal place for VARs.
    if (NodeUtil.isStatementBlock(n)) {
      splitVarDeclarations(n);
    }

    if (n.getType() == Token.FUNCTION) {
      moveNamedFunctions(n.getLastChild());
    }
  }

  // TODO(johnlenz): Move this to NodeTypeNormalizer once the unit tests are
  // fixed.
  /**
   * Limit the number of special cases where LABELs need to be handled. Only
   * BLOCK and loops are allowed to be labeled.  Loop labels must remain in
   * place as the named continues are not allowed for labeled blocks.
   */
  private void normalizeLabels(Node n) {
    Preconditions.checkArgument(n.getType() == Token.LABEL);

    Node last = n.getLastChild();
    switch (last.getType()) {
      case Token.LABEL:
      case Token.BLOCK:
      case Token.FOR:
      case Token.WHILE:
      case Token.DO:
        return;
      default:
        Node block = new Node(Token.BLOCK);
        n.replaceChild(last, block);
        block.addChildToFront(last);
        reportCodeChange("LABEL normalization");
        return;
    }
  }

  /**
   * Bring the initializers out of FOR loops.  These need to be placed
   * before any associated LABEL nodes. This needs to be done from the top
   * level label first so this is called as a pre-order callback (from
   * shouldTraverse).
   *
   * @param n The node to inspect.
   * @param before The node to insert the initializer before.
   * @param beforeParent The parent of the node before which the initializer
   *     will be inserted.
   */
  private void extractForInitializer(
      Node n, Node before, Node beforeParent) {

    for (Node next, c = n.getFirstChild(); c != null; c = next) {
      next = c.getNext();
      Node insertBefore = (before == null) ? c : before;
      Node insertBeforeParent = (before == null) ? n : beforeParent;
      switch (c.getType()) {
        case Token.LABEL:
          extractForInitializer(c, insertBefore, insertBeforeParent);
          break;
        case Token.FOR:
          if (!NodeUtil.isForIn(c)
              && c.getFirstChild().getType() != Token.EMPTY) {
            Node init = c.getFirstChild();
            c.replaceChild(init, new Node(Token.EMPTY));

            Node newStatement;
            // Only VAR statements, and expressions are allowed,
            // but are handled differently.
            if (init.getType() == Token.VAR) {
              newStatement = init;
            } else {
              newStatement = NodeUtil.newExpr(init);
            }

            insertBeforeParent.addChildBefore(newStatement, insertBefore);
            reportCodeChange("FOR initializer");
          }
          break;
      }
    }
  }


  /**
   * Split a var node such as:
   *   var a, b;
   * into individual statements:
   *   var a;
   *   var b;
   * @param n The whose children we should inspect.
   */
  private void splitVarDeclarations(Node n) {
    for (Node next, c = n.getFirstChild(); c != null; c = next) {
      next = c.getNext();
      if (c.getType() == Token.VAR) {
        if (assertOnChange && !c.hasChildren()) {
          throw new IllegalStateException("Empty VAR node.");
        }

        while (c.getFirstChild() != c.getLastChild()) {
          Node name = c.getFirstChild();
          c.removeChild(name);
          Node newVar = new Node(Token.VAR, name, n.getLineno(), n.getCharno());
          n.addChildBefore(newVar, c);
          reportCodeChange("VAR with multiple children");
        }
      }
    }
  }

  /**
   * Move all the functions that are valid at the execution of the first
   * statement of the function to the beginning of the function definition.
   */
  private void moveNamedFunctions(Node functionBody) {
    Preconditions.checkState(
        functionBody.getParent().getType() == Token.FUNCTION);
    Node previous = null;
    Node current = functionBody.getFirstChild();
    // Skip any declarations at the beginning of the function body, they
    // are already in the right place.
    while (current != null && NodeUtil.isFunctionDeclaration(current)) {
      previous = current;
      current = current.getNext();
    }

    // Find any remaining declarations and move them.
    Node insertAfter = previous;
    while (current != null) {
      // Save off the next node as the current node maybe removed.
      Node next = current.getNext();
      if (NodeUtil.isFunctionDeclaration(current)) {
        // Remove the declaration from the body.
        Preconditions.checkNotNull(previous);
        functionBody.removeChildAfter(previous);

        // Readd the function at the top of the function body (after any
        // previous declarations).
        insertAfter = addToFront(functionBody, current, insertAfter);
        compiler.reportCodeChange();
      } else {
        // Update the previous only if the current node hasn't been moved.
        previous = current;
      }
      current = next;
    }
  }

  /**
   * @param after The child node to insert the newChild after, or null if
   *     newChild should be added to the front of parent's child list.
   * @return The inserted child node.
   */
  private Node addToFront(Node parent, Node newChild, Node after) {
    if (after == null) {
      parent.addChildToFront(newChild);
    } else {
      parent.addChildAfter(newChild, after);
    }
    return newChild;
  }

  /**
   * Remove duplicate VAR declarations.
   */
  private void removeDuplicateDeclarations(Node root) {
    Callback tickler = new ScopeTicklingCallback();
    ScopeCreator scopeCreator =  new SyntacticScopeCreator(
        compiler, new DuplicateDeclarationHandler());
    NodeTraversal t = new NodeTraversal(compiler, tickler, scopeCreator);
    t.traverse(root);
  }

  /**
   * ScopeCreator duplicate declaration handler.
   */
  private final class DuplicateDeclarationHandler implements
      SyntacticScopeCreator.RedeclarationHandler {

    /**
     * Remove duplicate VAR declarations encountered discovered during
     * scope creation.
     */
    @Override
    public void onRedeclaration(
        Scope s, String name, Node n, Node parent, Node gramps,
        Node nodeWithLineNumber) {
      Preconditions.checkState(n.getType() == Token.NAME);
      if (parent.getType() == Token.VAR) {
        Preconditions.checkState(parent.hasOneChild());

        //
        // Remove the parent VAR. There are three cases that need to be handled:
        //  1) "var a = b;" which is replaced with "a = b"
        //  2) "label:var a;" which is replaced with "label:;".  Ideally, the
        //     label itself would be removed but that is not possible in the
        //     context in which "onRedeclaration" is called.
        //  3) "for (var a in b) ..." which is replaced with "for (a in b)..."
        // Cases we don't need to handle are VARs with multiple children,
        // which have already been split into separate declarations, so there
        // is no need to handle that here, and "for (var a;;);", which has
        // been moved out of the loop.
        //
        // The result of this is that in each case the parent node is replaced
        // which is generally dangerous in a traversal but is fine here with
        // the scope creator, as the next node of interest is the parent's
        // next sibling.
        //
        if (n.hasChildren()) {
          // The var is being initialize, preserve the new value.
          parent.removeChild(n);
          // Convert "var name = value" to "name = value"
          Node value = n.getFirstChild();
          n.removeChild(value);
          Node replacement = new Node(Token.ASSIGN, n, value);
          gramps.replaceChild(parent, new Node(Token.EXPR_RESULT, replacement));
        } else {
          // It is an empty reference remove it.
          if (NodeUtil.isStatementBlock(gramps)) {
            gramps.removeChild(parent);
          } else if (gramps.getType() == Token.FOR) {
            // This is the "for (var a in b)..." case.  We don't need to worry
            // about initializers in "for (var a;;)..." as those are moved out
            // as part of the other normalizations.
            parent.removeChild(n);
            gramps.replaceChild(parent, n);
          } else {
            Preconditions.checkState(gramps.getType() == Token.LABEL);
            gramps.replaceChild(parent, new Node(Token.EMPTY));
          }
        }
        reportCodeChange("Duplicate VAR declaration");
      }
    }

  }

  /**
   * A simple class that causes scope to be created.
   */
  private final class ScopeTicklingCallback
      implements NodeTraversal.ScopedCallback {
    @Override
    public void enterScope(NodeTraversal t) {
      // Cause the scope to be created, which will cause duplicate
      // to be found.
      t.getScope();
    }

    @Override
    public void exitScope(NodeTraversal t) {
      // Nothing to do.
    }

    @Override
    public boolean shouldTraverse(
        NodeTraversal nodeTraversal, Node n, Node parent) {
      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Nothing to do.
    }
  }

}

```
