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

  <target name="jar"
          depends="compile"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" />
    <jar destfile="${jarfile}" update="true">
      <zipfileset src="${lib.dir}/args4j_deploy.jar" />
      <zipfileset src="${lib.dir}/google_common_deploy.jar" />
      <zipfileset src="${lib.dir}/protobuf_deploy.jar" />
      <zipfileset src="${lib.dir}/libtrunk_rhino_parser_jarjared.jar" />
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
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
    <javac debug="true" srcdir="${test.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn">
      <classpath refid="classpath.path" />
    </javac>
  </target>

  <target name="all-classes-jar"
          depends="compile,compile-tests"
          description="package the compiler and its tests into one jar">
    <jar destfile="${jarfile}" update="true">
      <zipfileset src="${lib.dir}/ant_deploy.jar" />
      <zipfileset src="${lib.dir}/args4j_deploy.jar" />
      <zipfileset src="${lib.dir}/junit.jar" />
      <zipfileset src="${lib.dir}/hamcrest-core-1.1.jar" />
      <zipfileset src="${lib.dir}/google_common_deploy.jar" />
      <zipfileset src="${lib.dir}/protobuf_deploy.jar" />
      <zipfileset src="${lib.dir}/libtrunk_rhino_parser_jarjared.jar" />
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

## src/com/google/javascript/jscomp/CheckGlobalThis.java

```
/*
 * Copyright 2007 Google Inc.
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

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

/**
 * Checks for certain uses of the {@code this} keyword that are considered
 * unsafe because they are likely to reference the global {@code this} object
 * unintentionally.
 *
 * <p>A use of {@code this} is considered unsafe if it's on the left side of an
 * assignment or a property access, and not inside one of the following:
 * <ol>
 * <li>a prototype method
 * <li>a function annotated with {@code @constructor}
 * <li>a function annotated with {@code @this}.
 * <li>a function where there's no logical place to put a
 *     {@code this} annotation.
 * </ol>
 *
 * <p>Note that this check does not track assignments of {@code this} to
 * variables or objects. The code
 * <pre>
 * function evil() {
 *   var a = this;
 *   a.useful = undefined;
 * }
 * </pre>
 * will not get flagged, even though it is semantically equivalent to
 * <pre>
 * function evil() {
 *   this.useful = undefined;
 * }
 * </pre>
 * which would get flagged.
 *
*
*
 */
final class CheckGlobalThis implements Callback {

  static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");

  private final AbstractCompiler compiler;
  private final CheckLevel level;

  /**
   * If {@code assignLhsChild != null}, then the node being traversed is
   * a descendant of the first child of an ASSIGN node. assignLhsChild's
   * parent is this ASSIGN node.
   */
  private Node assignLhsChild = null;

  CheckGlobalThis(AbstractCompiler compiler, CheckLevel level) {
    this.compiler = compiler;
    this.level = level;
  }

  /**
   * Since this pass reports errors only when a global {@code this} keyword
   * is encountered, there is no reason to traverse non global contexts.
   */
  public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

    if (n.getType() == Token.FUNCTION) {
      // Don't traverse functions that are constructors or have the @this
      // or @override annotation.
      JSDocInfo jsDoc = getFunctionJsDocInfo(n);
      if (jsDoc != null &&
          (jsDoc.isConstructor() ||
           jsDoc.isInterface() ||
           jsDoc.hasThisType() ||
           jsDoc.isOverride())) {
        return false;
      }

      // Don't traverse functions unless they would normally
      // be able to have a @this annotation associated with them. e.g.,
      // var a = function() { }; // or
      // function a() {} // or
      // a.x = function() {}; // or
      // var a = {x: function() {}};
      int pType = parent.getType();
      if (!(pType == Token.BLOCK ||
            pType == Token.SCRIPT ||
            pType == Token.NAME ||
            pType == Token.ASSIGN ||
            pType == Token.OBJECTLIT)) {
        return false;
      }
    }

    if (parent != null && parent.getType() == Token.ASSIGN) {
      Node lhs = parent.getFirstChild();
      Node rhs = lhs.getNext();

      if (n == lhs) {
        // Always traverse the left side of the assignment. To handle
        // nested assignments properly (e.g., (a = this).property = c;),
        // assignLhsChild should not be overridden.
        if (assignLhsChild == null) {
          assignLhsChild = lhs;
        }
      } else {
        // Only traverse the right side if it's not an assignment to a prototype
        // property or subproperty.
        if (NodeUtil.isGet(lhs)) {
          if (lhs.getType() == Token.GETPROP &&
              lhs.getLastChild().getString().equals("prototype")) {
            return false;
          }
          Node llhs = lhs.getFirstChild();
          if (llhs.getType() == Token.GETPROP &&
              llhs.getLastChild().getString().equals("prototype")) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.THIS && shouldReportThis(n, parent)) {
      compiler.report(t.makeError(n, level, GLOBAL_THIS));
    }
    if (n == assignLhsChild) {
      assignLhsChild = null;
    }
  }

  private boolean shouldReportThis(Node n, Node parent) {
    if (assignLhsChild != null) {
      // Always report a THIS on the left side of an assign.
      return true;
    }

    // Also report a THIS with a property access.
    return parent != null && NodeUtil.isGet(parent);
  }

  /**
   * Gets a function's JSDoc information, if it has any. Checks for a few
   * patterns (ellipses show where JSDoc would be):
   * <pre>
   * ... function() {}
   * ... x = function() {};
   * var ... x = function() {};
   * ... var x = function() {};
   * </pre>
   */
  private JSDocInfo getFunctionJsDocInfo(Node n) {
    JSDocInfo jsDoc = n.getJSDocInfo();
    Node parent = n.getParent();
    if (jsDoc == null) {
      int parentType = parent.getType();
      if (parentType == Token.NAME || parentType == Token.ASSIGN) {
        jsDoc = parent.getJSDocInfo();
        if (jsDoc == null && parentType == Token.NAME) {
          Node gramps = parent.getParent();
          if (gramps.getType() == Token.VAR) {
            jsDoc = gramps.getJSDocInfo();
          }
        }
      }
    }
    return jsDoc;
  }
}

```

## src/com/google/javascript/jscomp/RuntimeTypeCheck.java

```
/*
 * Copyright 2010 Google Inc.
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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.annotation.Nullable;

/**
 * Inserts runtime type assertions.
 *
 * <p>We add markers to user-defined interfaces and classes in order to check if
 * an object conforms to that type.
 *
 * <p>For each function, we insert a runtime type assertion for each parameter
 * and return value for which the compiler has a type.
 *
 * <p>The JavaScript code which implements the type assertions is in
 * js/runtime-type-check.js.
 *
*
*
 */
class RuntimeTypeCheck implements CompilerPass {

  private static final Comparator<JSType> ALPHA = new Comparator<JSType>() {
    public int compare(JSType t1, JSType t2) {
      return getName(t1).compareTo(getName(t2));
    }

    private String getName(JSType type) {
      if (type.isInstanceType()) {
        return ((ObjectType) type).getReferenceName();
      } else if (type.isNullType()
          || type.isBooleanValueType()
          || type.isNumberValueType()
          || type.isStringValueType()
          || type.isVoidType()) {
        return type.toString();
      } else {
        // Type unchecked at runtime, so we don't care about the sorting order.
        return "";
      }
    }
  };

  private final AbstractCompiler compiler;
  private final String logFunction;

  RuntimeTypeCheck(AbstractCompiler compiler, @Nullable String logFunction) {
    this.compiler = compiler;
    this.logFunction = logFunction;
  }

  @Override
  public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new AddMarkers(compiler));
    NodeTraversal.traverse(compiler, root, new AddChecks(compiler));
    addBoilerplateCode();
  }

  /**
   * Inserts marker properties for user-defined interfaces and classes.
   *
   * <p>For example, for a class C, we add
   * {@code C.prototype['instance_of__C']}, and for each interface I it
   * implements , we add {@code C.prototype['implements__I']}.
   *
   * <p>Since interfaces are not a runtime JS concept, we use these markers to
   * recognize an interface implementation at runtime. We also use markers for
   * user-defined classes, so that we can easily recognize them independently of
   * which module they are defined in and whether the module is loaded.
   */
  private static class AddMarkers
      extends NodeTraversal.AbstractPostOrderCallback {

    private final AbstractCompiler compiler;

    private AddMarkers(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (NodeUtil.isFunction(n)) {
        visitFunction(t, n);
      }
    }

    private void visitFunction(NodeTraversal t, Node n) {
      FunctionType funType = (FunctionType) n.getJSType();
      if (!funType.isConstructor()) {
        return;
      }

      Node nodeToInsertAfter = findNodeToInsertAfter(n);

      nodeToInsertAfter = addMarker(funType, nodeToInsertAfter, null);

      TreeSet<ObjectType> stuff = Sets.newTreeSet(ALPHA);
      Iterables.addAll(stuff, funType.getAllImplementedInterfaces());
      for (ObjectType interfaceType : stuff) {
        nodeToInsertAfter =
            addMarker(funType, nodeToInsertAfter, interfaceType);
      }
    }

    private Node addMarker(
            FunctionType funType,
            Node nodeToInsertAfter,
            @Nullable ObjectType interfaceType) {

      if (funType.getSource() == null) {
        return nodeToInsertAfter;
      }

      String className = NodeUtil.getFunctionName(funType.getSource());

      // This can happen with anonymous classes declared with the type
      // {@code Function}.
      if (className == null) {
        return nodeToInsertAfter;
      }

      Node classNode = NodeUtil.newQualifiedNameNode(className, -1, -1);

      Node marker = Node.newString(
              interfaceType == null ?
              "instance_of__" + className :
              "implements__" + interfaceType.getReferenceName());

      Node assign = new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN,
          new Node(Token.GETELEM,
              new Node(Token.GETPROP,
                  classNode,
                  Node.newString("prototype")), marker),
          new Node(Token.TRUE)));

      nodeToInsertAfter.getParent().addChildAfter(assign, nodeToInsertAfter);
      compiler.reportCodeChange();
      nodeToInsertAfter = assign;
      return nodeToInsertAfter;
    }

    /**
     * Find the node to insert the markers after. Typically, this node
     * corresponds to the constructor declaration, but we want to skip any of
     * the white-listed function calls.
     *
     * @param n the constructor function node
     * @return the node to insert after
     */
    private Node findNodeToInsertAfter(Node n) {
      Node nodeToInsertAfter = findEnclosingConstructorDeclaration(n);

      Node next = nodeToInsertAfter.getNext();
      while (next != null && isClassDefiningCall(next)) {
        nodeToInsertAfter = next;
        next = nodeToInsertAfter.getNext();
      }

      return nodeToInsertAfter;
    }

    private Node findEnclosingConstructorDeclaration(Node n) {
      while (n.getParent().getType() != Token.SCRIPT &&
          n.getParent().getType() != Token.BLOCK) {
        n = n.getParent();
      }
      return n;
    }

    private boolean isClassDefiningCall(Node next) {
      return NodeUtil.isExprCall(next) &&
          compiler.getCodingConvention().getClassesDefinedByCall(
              next.getFirstChild()) != null;
    }
  }

  /**
   * Insert calls to the runtime type checking function {@code checkType}, which
   * takes an expression to check and a list of checkers (one of which must
   * match). It returns the expression back to facilitate checking of return
   * values. We have checkers for value types, class types (user-defined and
   * externed), and interface types.
   */
  private static class AddChecks
      extends NodeTraversal.AbstractPostOrderCallback {

    private final AbstractCompiler compiler;

    private AddChecks(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (NodeUtil.isFunction(n)) {
        visitFunction(t, n);
      } else if (n.getType() == Token.RETURN) {
        visitReturn(t, n);
      }
    }

    /**
     * Insert checks for the parameters of the function.
     */
    private void visitFunction(NodeTraversal t, Node n) {
      FunctionType funType = (FunctionType) n.getJSType();
      Node block = n.getLastChild();
      Node paramName = NodeUtil.getFnParameters(n).getFirstChild();
      Node insertionPoint = null;

      // To satisfy normalization constraints, the type checking must be
      // added after any inner function declarations.
      for (Node next = block.getFirstChild();
           next != null && NodeUtil.isFunctionDeclaration(next);
           next = next.getNext()) {
        insertionPoint = next;
      }

      for (Node paramType : funType.getParameters()) {
        // Can this ever happen?
        if (paramName == null) {
          return;
        }

        Node checkNode = createCheckTypeCallNode(
            paramType.getJSType(), paramName.cloneTree());

        if (checkNode == null) {
          // We don't know how to check this parameter type.
          paramName = paramName.getNext();
          continue;
        }

        checkNode = new Node(Token.EXPR_RESULT, checkNode);
        if (insertionPoint == null) {
          block.addChildToFront(checkNode);
        } else {
          block.addChildAfter(checkNode, insertionPoint);
        }

        compiler.reportCodeChange();
        paramName = paramName.getNext();
        insertionPoint = checkNode;
      }
    }

    private void visitReturn(NodeTraversal t, Node n) {
      Node function = t.getEnclosingFunction();
      FunctionType funType = (FunctionType) function.getJSType();

      Node retValue = n.getFirstChild();
      if (retValue == null) {
        return;
      }

      Node checkNode = createCheckTypeCallNode(
          funType.getReturnType(), retValue.cloneTree());

      if (checkNode == null) {
        return;
      }

      n.replaceChild(retValue, checkNode);
      compiler.reportCodeChange();
    }

    /**
     * Creates a function call to check that the given expression matches the
     * given type at runtime.
     *
     * <p>For example, if the type is {@code (string|Foo)}, the function call is
     * {@code checkType(expr, [valueChecker('string'), classChecker('Foo')])}.
     *
     * @return the function call node or {@code null} if the type is not checked
     */
    private Node createCheckTypeCallNode(JSType type, Node expr) {
      Node arrayNode = new Node(Token.ARRAYLIT);
      Collection<JSType> alternates;
      if (type.isUnionType()) {
        alternates = Sets.newTreeSet(ALPHA);
        Iterables.addAll(alternates, ((UnionType)type).getAlternates());
      } else {
        alternates = ImmutableList.of(type);
      }
      for (JSType alternate : alternates) {
        Node checkerNode = createCheckerNode(alternate);
        if (checkerNode == null) {
          return null;
        }
        arrayNode.addChildToBack(checkerNode);
      }
      return new Node(Token.CALL, jsCode("checkType"), expr, arrayNode);
    }

    /**
     * Creates a node which evaluates to a checker for the given type (which
     * must not be a union). We have checkers for value types, classes and
     * interfaces.
     *
     * @return the checker node or {@code null} if the type is not checked
     */
    private Node createCheckerNode(JSType type) {
      if (type.isNullType()) {
        return jsCode("nullChecker");

      } else if (type.isBooleanValueType()
          || type.isNumberValueType()
          || type.isStringValueType()
          || type.isVoidType()) {
        return new Node(Token.CALL,
            jsCode("valueChecker"),
            Node.newString(type.toString()));

      } else if (type.isInstanceType()) {
        ObjectType objType = (ObjectType) type;

        String refName = objType.getReferenceName();

        String sourceName =
            NodeUtil.getSourceName(objType.getConstructor().getSource());
        CompilerInput sourceInput = compiler.getInput(sourceName);
        if (sourceInput == null || sourceInput.isExtern()) {
          return new Node(Token.CALL,
                  jsCode("externClassChecker"),
                  Node.newString(refName));
        }

        return new Node(Token.CALL,
                jsCode(objType.getConstructor().isInterface() ?
                        "interfaceChecker" : "classChecker"),
                Node.newString(refName));

      } else {
        // We don't check this type (e.g. unknown & all types).
        return null;
      }
    }
  }

  private void addBoilerplateCode() {
    Node js = getBoilerplateCode(compiler, logFunction);
    compiler.getNodeForCodeInsertion(null).addChildrenToFront(
        js.removeChildren());
    compiler.reportCodeChange();
  }

  private static Node jsCode(String prop) {
    return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
  }

  @VisibleForTesting
  static Node getBoilerplateCode(
      AbstractCompiler compiler, @Nullable String logFunction) {
    String boilerplateCode;
    try {
      boilerplateCode = CharStreams.toString(new InputStreamReader(
          RuntimeTypeCheck.class.getResourceAsStream(
          "js/runtime_type_check.js"), Charsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    boilerplateCode = boilerplateCode.replace("%%LOG%%",
        logFunction == null ? "function(warning, expr) {}" : logFunction);

    return Normalize.parseAndNormalizeSyntheticCode(
        compiler, boilerplateCode, "jscomp_runtimeTypeCheck_");
  }
}

```
