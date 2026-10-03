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

## src/com/google/javascript/jscomp/DevirtualizePrototypeMethods.java

```
/*
 * Copyright 2009 Google Inc.
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
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;

/**
 * Rewrites prototyped methods calls as static calls that take "this"
 * as their first argument.  This tranformation simplifies the call
 * graph so smart name removal, cross module code motion and other
 * passes can do more.
 *
 * <p>This pass should only be used in production code if property
 * and variable renaming are turned on.  Resulting code may also
 * benefit from --collapse_anonymous_functions and
 * --collapse_variable_declarations
 *
 * <p>This pass only rewrites functions that are part of an objects
 * prototype.  Functions that access the "arguments" variable
 * arguments object are not eligible for this optimization.
 *
 * <p>For example:
 * <pre>
 *     A.prototype.accumulate = function(value) {
 *       this.total += value; return this.total
 *     }
 *     var total = a.accumulate(2)
 * </pre>
 *
 * <p>will be rewritten as:
 *
 * <pre>
 *     var accumulate = function(self, value) {
 *       self.total += value; return self.total
 *     }
 *     var total = accumulate(a, 2)
 * </pre>
 *
*
 */
class DevirtualizePrototypeMethods implements CompilerPass {
  private final AbstractCompiler compiler;

  DevirtualizePrototypeMethods(AbstractCompiler compiler) {
    this.compiler = compiler;
  }

  @Override
  public void process(Node externs, Node root) {
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    for (DefinitionSite defSite : defFinder.getDefinitionSites()) {
      rewriteDefinitionIfEligible(defSite, defFinder);
    }
  }

  /**
   * Determines if the name node acts as the function name in a call expression.
   */
  private static boolean isCall(UseSite site) {
    Node node = site.node;
    Node parent = node.getParent();
    return (parent.getFirstChild() == node) && NodeUtil.isCall(parent);
  }

  /**
   * Determines if the current node is a function prototype definition.
   */
  private static boolean isPrototypeMethodDefinition(Node node) {
    Node parent = node.getParent();
    if (parent == null) {
      return false;
    }

    Node gramp = parent.getParent();
    if ((gramp == null) ||
        (parent.getFirstChild() != node) ||
        !NodeUtil.isExprAssign(gramp)) {
      return false;
    }

    Node functionNode = parent.getLastChild();
    if ((functionNode == null) || !NodeUtil.isFunction(functionNode)) {
      return false;
    }

    if (!NodeUtil.isGetProp(node)) {
      return false;
    }

    Node nameNode = node.getFirstChild();
    return NodeUtil.isGetProp(nameNode) &&
        nameNode.getLastChild().getString().equals("prototype");

  }

  /**
   * @returns The new name for a rewritten method.
   */
  private String getRewrittenMethodName(String orginalMethodName) {
    return "JSCompiler_StaticMethods_" + orginalMethodName;
  }

  /**
   * Rewrites method definition and call sites if the method is
   * defined in the global scope exactly once.
   *
   * Definition and use site information is provided by the
   * {@link SimpleDefinitionFinder} passed in as an argument.
   *
   * @param defSite definition site to process.
   * @param defFinder structure that hold Node -> Definition and
   * Definition -> [UseSite] maps.
   */
  private void rewriteDefinitionIfEligible(DefinitionSite defSite,
                                           SimpleDefinitionFinder defFinder) {

    if (defSite.inExterns ||
        !defSite.inGlobalScope ||
        !isEligibleDefinition(defFinder, defSite)) {
      return;
    }

    Node node = defSite.node;

    // TODO(user) support rewritting methods defined as part of
    // object literals.
    if (!isPrototypeMethodDefinition(node)) {
      return;
    }

    for (Node ancestor = node.getParent();
         ancestor != null;
         ancestor = ancestor.getParent()) {
      if (NodeUtil.isControlStructure(ancestor)) {
        return;
      }
    }

    // TODO(user) The code only works if there is a single definition
    // associated with a property name.  Once this pass starts using
    // the NameReferenceGraph to disambiguate call sites, it will be
    // necessary to consider type information when generating static
    // method names and/or append unique ids to duplicate static
    // method names.
    // Whatever scheme we use should not break stable renaming.
    String newMethodName = getRewrittenMethodName(
        node.getLastChild().getString());
    rewriteDefinition(node, newMethodName);
    rewriteCallSites(defFinder, defSite.definition, newMethodName);
  }

  /**
   * Determines if a method definition is eligible for rewrite as a
   * global function.  In order to be eligible for rewrite, the
   * definition must:
   *
   * - Refer to a function that takes a fixed number of arguments.
   * - Function must not be exported.
   * - Function must be used at least once.
   * - Property is never accesed outside a function call context.
   * - The definition under consideration must be the only possible
   *   choice at each call site.
   * - Definition must happen in a module loaded before the first use.
   */
  private boolean isEligibleDefinition(SimpleDefinitionFinder defFinder,
                                       DefinitionSite definitionSite) {

    Definition definition = definitionSite.definition;
    JSModule definitionModule = definitionSite.module;

    // Only functions may be rewritten.
    // Functions that access "arguments" are not eligible since
    // rewrite changes the structure of this object.
    Node rValue = definition.getRValue();
    if (rValue == null ||
        !NodeUtil.isFunction(rValue) ||
        NodeUtil.isVarArgsFunction(rValue)) {
      return false;
    }

    // Exporting a method prevents rewrite.
    Node lValue = definition.getLValue();
    if ((lValue == null) ||
        !NodeUtil.isGetProp(lValue)) {
      return false;
    }
    CodingConvention codingConvention = compiler.getCodingConvention();
    if (codingConvention.isExported(lValue.getLastChild().getString())) {
      return false;
    }

    Collection<UseSite> useSites = defFinder.getUseSites(definition);

    // Rewriting unused methods is not sound.
    if (useSites.isEmpty()) {
      return false;
    }

    JSModuleGraph moduleGraph = compiler.getModuleGraph();

    for (UseSite site : useSites) {

      // Accessing the property directly prevents rewrite.
      if (!isCall(site)) {
        return false;
      }

      // Multiple definitions prevent rewrite.
      Node nameNode = site.node;
      Collection<Definition> singleSiteDefinitions =
          defFinder.getDefinitionsReferencedAt(nameNode);
      if (singleSiteDefinitions.size() > 1) {
        return false;
      }
      Preconditions.checkState(!singleSiteDefinitions.isEmpty());
      Preconditions.checkState(singleSiteDefinitions.contains(definition));

      // Accessing the property in a module loaded before the
      // definition module prevents rewrite; accessing a variable
      // before definition results in a parse error.
      JSModule callModule = site.module;
      if ((definitionModule != callModule) &&
          ((callModule == null) ||
          !moduleGraph.dependsOn(callModule, definitionModule))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Rewrites object method call sites as calls to global functions
   * that take "this" as their first argument.
   *
   * Before:
   *   o.foo(a, b, c)
   *
   * After:
   *   foo(o, a, b, c)
   */
  private void rewriteCallSites(SimpleDefinitionFinder defFinder,
                                Definition definition,
                                String newMethodName) {
    Collection<UseSite> useSites = defFinder.getUseSites(definition);
    for (UseSite site : useSites) {
      Node node = site.node;
      Node parent = node.getParent();

      Node objectNode = node.getFirstChild();
      node.removeChild(objectNode);
      parent.replaceChild(node, objectNode);
      parent.addChildToFront(Node.newString(Token.NAME, newMethodName));
      compiler.reportCodeChange();
    }
  }

  /**
   * Rewrites method definitions as global functions that take "this"
   * as their first argument.
   *
   * Before:
   *   a.prototype.b = function(a, b, c) {...}
   *
   * After:
   *   var b = function(self, a, b, c) {...}
   */
  private void rewriteDefinition(Node node, String newMethodName) {
    Node parent = node.getParent();
    Node functionNode = parent.getLastChild();
    Node expr = parent.getParent();
    Node block = expr.getParent();

    Node newNameNode = Node.newString(Token.NAME, newMethodName);
    parent.removeChild(functionNode);
    newNameNode.addChildToFront(functionNode);
    block.replaceChild(expr, new Node(Token.VAR, newNameNode));

    // add extra argument
    String self = newMethodName + "$self";
    Node argList = functionNode.getFirstChild().getNext();
    argList.addChildToFront(Node.newString(Token.NAME, self));

    // rewrite body
    Node body = functionNode.getLastChild();
    replaceReferencesToThis(body, self);

    // fix type
    fixFunctionType(functionNode);

    compiler.reportCodeChange();
  }

  /**
   * Creates a new JSType based on the original function type by
   * adding the original this pointer type to the beginning of the
   * argument type list and replacing the this pointer type with
   * NO_TYPE.
   */
  private void fixFunctionType(Node functionNode) {
    FunctionType type = (FunctionType) functionNode.getJSType();
    if (type != null) {
      JSTypeRegistry typeRegistry = compiler.getTypeRegistry();

      List<JSType> parameterTypes = Lists.newArrayList();
      parameterTypes.add(type.getTypeOfThis());

      for (Node param : type.getParameters()) {
        parameterTypes.add(param.getJSType());
      }

      ObjectType thisType =
          typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
      JSType returnType = type.getReturnType();

      JSType newType = typeRegistry.createFunctionType(
          thisType, returnType, parameterTypes);
      functionNode.setJSType(newType);
    }
  }

  /**
   * Replaces references to "this" with references to name.  Do not
   * traverse function boundaries.
   */
  private void replaceReferencesToThis(Node node, String name) {
    if (NodeUtil.isFunction(node)) {
      return;
    }

    for (Node child : node.children()) {
      if (NodeUtil.isThis(child)) {
        Node newName = Node.newString(Token.NAME, name);
        newName.setJSType(child.getJSType());
        node.replaceChild(child, newName);
      } else {
        replaceReferencesToThis(child, name);
      }
    }
  }
}

```

## src/com/google/javascript/rhino/jstype/FunctionType.java

```
/*
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Bob Jervis
 *   Google Inc.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package com.google.javascript.rhino.jstype;

import static com.google.javascript.rhino.jstype.JSTypeNative.U2U_CONSTRUCTOR_TYPE;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This derived type provides extended information about a function, including
 * its return type and argument types.<p>
 *
 * Note: the parameters list is the LP node that is the parent of the
 * actual NAME node containing the parsed argument list (annotated with
 * JSDOC_TYPE_PROP's for the compile-time type of each argument.
*
*
 */
public class FunctionType extends PrototypeObjectType {
  private static final long serialVersionUID = 1L;

  private enum Kind {
    ORDINARY,
    CONSTRUCTOR,
    INTERFACE
  }

  /**
   * {@code [[Call]]} property.
   */
  private ArrowType call;

  /**
   * The {@code prototype} property. This field is lazily initialized by
   * {@code #getPrototype()}. The most important reason for lazily
   * initializing this field is that there are cycles in the native types
   * graph, so some prototypes must temporarily be {@code null} during
   * the construction of the graph.
   */
  private FunctionPrototypeType prototype;

  /**
   * Whether a function is a constructor, an interface, or just an ordinary
   * function.
   */
  private final Kind kind;

  /**
   * The type of {@code this} in the scope of this function.
   */
  private ObjectType typeOfThis;

  /**
   * The function node which this type represents. It may be {@code null}.
   */
  private Node source;

  /**
   * The interfaces directly implemented by this function.
   * It is only relevant for constructors. May not be {@code null}.
   */
  private List<ObjectType> implementedInterfaces = ImmutableList.of();

  /**
   * The types which are subtypes of this function. It is only relevant for
   * constructors and may be {@code null}.
   */
  private List<FunctionType> subTypes;

  /**
   * The template type name. May be {@code null}.
   */
  private String templateTypeName;

  /**
   * Creates a function type.
   * @param registry the owner registry for this type
   * @param name the function's name or {@code null} to indicate that the
   *        function is anonymous.
   * @param source the node defining this function. Its type
   *        ({@link Node#getType()}) must be {@link Token#FUNCTION}.
   * @param parameters the function's parameters or {@code null}
   *        to indicate that the parameter types are unknown.
   * @param returnType the function's return type or {@code null} to indicate
   *        that the return type is unknown.
   */
  @VisibleForTesting
  public FunctionType(JSTypeRegistry registry, String name, Node source,
      Node parameters, JSType returnType) {
    this(registry, name, source, parameters, returnType, null, null, false,
         false);
  }

  /**
   * Creates a function type.
   * @param registry the owner registry for this type
   * @param name the function's name or {@code null} to indicate that the
   *        function is anonymous.
   * @param source the node defining this function. Its type
   *        ({@link Node#getType()}) must be {@link Token#FUNCTION}.
   * @param parameters the function's parameters or {@code null}
   *        to indicate that the parameter types are unknown.
   * @param returnType the function's return type or {@code null} to indicate
   *        that the return type is unknown.
   * @param typeOfThis The type of {@code this} in non-constructors.  May be
   *        {@code null} to indicate that the type of {@code this} is unknown.
   */
  public FunctionType(JSTypeRegistry registry, String name, Node source,
      Node parameters, JSType returnType, ObjectType typeOfThis) {
    this(registry, name, source, parameters, returnType, typeOfThis,
        null, false, false);
  }

  /**
   * Creates a function type.
   * @param registry the owner registry for this type
   * @param name the function's name or {@code null} to indicate that the
   *        function is anonymous.
   * @param source the node defining this function. Its type
   *        ({@link Node#getType()}) must be {@link Token#FUNCTION}.
   * @param parameters the function's parameters or {@code null}
   *        to indicate that the parameter types are unknown.
   * @param returnType the function's return type or {@code null} to indicate
   *        that the return type is unknown.
   * @param typeOfThis The type of {@code this} in non-constructors.  May be
   *        {@code null} to indicate that the type of {@code this} is unknown.
   * @param templateTypeName The template type name or {@code null}.
   */
  public FunctionType(JSTypeRegistry registry, String name, Node source,
      Node parameters, JSType returnType, ObjectType typeOfThis,
      String templateTypeName) {
    this(registry, name, source, parameters, returnType, typeOfThis,
        templateTypeName, false, false);
  }

  /** Creates an instance for a function that might be a constructor. */
  FunctionType(JSTypeRegistry registry, String name, Node source,
      Node parameters, JSType returnType, ObjectType typeOfThis,
      String templateTypeName,  boolean isConstructor, boolean nativeType) {
    super(registry, name,
        registry.getNativeObjectType(JSTypeNative.FUNCTION_INSTANCE_TYPE),
        nativeType);
    Preconditions.checkArgument(source == null ||
        Token.FUNCTION == source.getType());
    this.source = source;
    this.kind = isConstructor ? Kind.CONSTRUCTOR : Kind.ORDINARY;
    if (isConstructor) {
      this.typeOfThis = typeOfThis != null && typeOfThis.isNoObjectType() ?
          typeOfThis : new InstanceObjectType(registry, this, nativeType);
    } else {
      this.typeOfThis = typeOfThis != null ?
          typeOfThis :
          registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
    }
    // The call type should be set up last because we are calling getReturnType,
    // which may be overloaded and depend on other properties being set.
    this.call = new ArrowType(registry, parameters,
        (returnType == null ? getReturnType() : returnType));
    this.templateTypeName = templateTypeName;
  }

  /** Creates an instance for a function that is an interface. */
  FunctionType(JSTypeRegistry registry, String name, Node source) {
    super(registry, name,
        registry.getNativeObjectType(JSTypeNative.FUNCTION_INSTANCE_TYPE));
    Preconditions.checkArgument(source == null ||
        Token.FUNCTION == source.getType());
    Preconditions.checkArgument(name != null);
    this.source = source;
    this.call = null;
    this.kind = Kind.INTERFACE;
    this.typeOfThis = new InstanceObjectType(registry, this);
  }

  @Override
  public boolean isInstanceType() {
    // The universal constructor is its own instance, bizarrely.
    return equals(registry.getNativeType(U2U_CONSTRUCTOR_TYPE));
  }

  @Override
  public boolean isConstructor() {
    return kind == Kind.CONSTRUCTOR;
  }

  @Override
  public boolean isInterface() {
    return kind == Kind.INTERFACE;
  }

  @Override
  public boolean isOrdinaryFunction() {
    return kind == Kind.ORDINARY;
  }

  @Override
  public boolean isFunctionType() {
    return true;
  }

  @Override
  public boolean canBeCalled() {
    return true;
  }

  public Iterable<Node> getParameters() {
    Node n = getParametersNode();
    if (n != null) {
      return n.children();
    } else {
      return Collections.emptySet();
    }
  }

  /** Gets an LP node that contains all params. May be null. */
  public Node getParametersNode() {
    return call == null ? null : call.parameters;
  }

  /** Gets the minimum number of arguments that this function requires. */
  public int getMinArguments() {
    // NOTE(nicksantos): There are some native functions that have optional
    // parameters before required parameters. This algorithm finds the position
    // of the last required parameter.
    int i = 0;
    int min = 0;
    for (Node n : getParameters()) {
      i++;
      if (!n.isOptionalArg() && !n.isVarArgs()) {
        min = i;
      }
    }
    return min;
  }

  /**
   * Gets the maximum number of arguments that this function requires,
   * or Integer.MAX_VALUE if this is a variable argument function.
   */
  public int getMaxArguments() {
    Node params = getParametersNode();
    if (params != null) {
      Node lastParam = params.getLastChild();
      if (lastParam == null || !lastParam.isVarArgs()) {
        return params.getChildCount();
      }
    }

    return Integer.MAX_VALUE;
  }

  public JSType getReturnType() {
    return call == null ? null : call.returnType;
  }

  /**
   * Gets the {@code prototype} property of this function type. This is
   * equivalent to {@code (ObjectType) getPropertyType("prototype")}.
   */
  public FunctionPrototypeType getPrototype() {
    // lazy initialization of the prototype field
    if (prototype == null) {
      setPrototype(new FunctionPrototypeType(registry, this, null));
    }
    return prototype;
  }

  /**
   * Sets the prototype, creating the prototype object from the given
   * base type.
   * @param baseType The base type.
   */
  public void setPrototypeBasedOn(ObjectType baseType) {
    if (prototype == null) {
      setPrototype(
          new FunctionPrototypeType(
              registry, this, baseType, isNativeObjectType()));
    } else {
      prototype.setImplicitPrototype(baseType);
    }
  }

  /**
   * Sets the prototype.
   * @param prototype the prototype. If this value is {@code null} it will
   *        silently be discarded.
   */
  public boolean setPrototype(FunctionPrototypeType prototype) {
    if (prototype == null) {
      return false;
    }
    // getInstanceType fails if the function is not a constructor
    if (isConstructor() && prototype == getInstanceType()) {
      return false;
    }

    this.prototype = prototype;

    if (isConstructor() || isInterface()) {
      FunctionType superClass = getSuperClassConstructor();
      if (superClass != null) {
        superClass.addSubType(this);
      }
    }
    return true;
  }

  /**
   * Returns all interfaces implemented by a class or its superclass and any
   * superclasses for any of those interfaces. If this is called before all
   * types are resolved, it may return an incomplete set.
   */
  public Iterable<ObjectType> getAllImplementedInterfaces() {
    Set<ObjectType> interfaces = Sets.newHashSet();

    for (ObjectType type : getImplementedInterfaces()) {
      addRelatedInterfaces(type, interfaces);
    }
    return interfaces;
  }

  private void addRelatedInterfaces(ObjectType instance, Set<ObjectType> set) {
    FunctionType constructor = instance.getConstructor();
    if (constructor != null) {
      if (!constructor.isInterface()) {
        return;
      }

      set.add(instance);

      if (constructor.getSuperClassConstructor() != null) {
        addRelatedInterfaces(
            constructor.getSuperClassConstructor().getInstanceType(), set);
      }
    }
  }

  /** Returns interfaces implemented directly by a class or its superclass. */
  public Iterable<ObjectType> getImplementedInterfaces() {
    FunctionType superCtor = isConstructor() ?
        getSuperClassConstructor() : null;
    if (superCtor == null) {
      return implementedInterfaces;
    } else {
      return Iterables.concat(
          implementedInterfaces, superCtor.getImplementedInterfaces());
    }
  }

  public void setImplementedInterfaces(List<ObjectType> implementedInterfaces) {
    // Records this type for each implemented interface.
    for (ObjectType type : implementedInterfaces) {
      registry.registerTypeImplementingInterface(this, type);
    }
    this.implementedInterfaces = ImmutableList.copyOf(implementedInterfaces);
  }

  @Override
  public boolean hasProperty(String name) {
    return super.hasProperty(name) || "prototype".equals(name);
  }

  @Override
  public boolean hasOwnProperty(String name) {
    return super.hasOwnProperty(name) || "prototype".equals(name);
  }

  @Override
  public JSType getPropertyType(String name) {
    if ("prototype".equals(name)) {
      return getPrototype();
    } else {
      if (!hasOwnProperty(name)) {
        if ("call".equals(name)) {
          // Define the "call" function lazily.
          Node params = getParametersNode();
          if (params == null) {
            // If there's no params array, don't do any type-checking
            // in this CALL function.
            defineDeclaredProperty(name,
                new FunctionType(registry, null, null,
                    null, getReturnType()),
                false);
          } else {
            params = params.cloneTree();
            Node thisTypeNode = Node.newString(Token.NAME, "thisType");
            thisTypeNode.setJSType(
                registry.createOptionalNullableType(getTypeOfThis()));
            params.addChildToFront(thisTypeNode);
            thisTypeNode.setOptionalArg(true);

            defineDeclaredProperty(name,
                new FunctionType(registry, null, null,
                    params, getReturnType()),
                false);
          }
        } else if ("apply".equals(name)) {
          // Define the "apply" function lazily.
          FunctionParamBuilder builder = new FunctionParamBuilder(registry);

          // Ecma-262 says that apply's second argument must be an Array
          // or an arguments object. We don't model the arguments object,
          // so let's just be forgiving for now.
          // TODO(nicksantos): Model the Arguments object.
          builder.addOptionalParams(
              registry.createNullableType(getTypeOfThis()),
              registry.createNullableType(
                  registry.getNativeType(JSTypeNative.OBJECT_TYPE)));

          defineDeclaredProperty(name,
              new FunctionType(registry, null, null,
                  builder.build(), getReturnType()),
              false);
        }
      }

      return super.getPropertyType(name);
    }
  }

  @Override
  boolean defineProperty(String name, JSType type,
      boolean inferred, boolean inExterns) {
    if ("prototype".equals(name)) {
      ObjectType objType = type.toObjectType();
      if (objType != null) {
        if (objType.equals(prototype)) {
          return true;
        }
        return setPrototype(
            new FunctionPrototypeType(
                registry, this, objType, isNativeObjectType()));
      } else {
        return false;
      }
    }
    return super.defineProperty(name, type, inferred, inExterns);
  }

  @Override
  public boolean isPropertyTypeInferred(String property) {
    return "prototype".equals(property) ||
        super.isPropertyTypeInferred(property);
  }

  @Override
  public JSType getLeastSupertype(JSType that) {
    // NOTE(nicksantos): When we remove the unknown type, the function types
    // form a lattice with the universal constructor at the top of the lattice,
    // and the NoObject type at the bottom of the lattice.
    //
    // When we introduce the unknown type, it's much more difficult to make
    // heads or tails of the partial ordering of types, because there's no
    // clear hierarchy between the different components (parameter types and
    // return types) in the ArrowType.
    //
    // Rather than make the situation more complicated by introducing new
    // types (like unions of functions), we just fallback on the simpler
    // approach of using the universal constructor and the AnyObject as
    // the supremum and infinum of all function types.
    if (isFunctionType() && that.isFunctionType()) {
      if (equals(that)) {
        return this;
      }

      JSType functionInstance = registry.getNativeType(
          JSTypeNative.FUNCTION_INSTANCE_TYPE);
      if (functionInstance.equals(that)) {
        return that;
      } else if (functionInstance.equals(this)) {
        return this;
      }

      return registry.getNativeType(JSTypeNative.U2U_CONSTRUCTOR_TYPE);
    }

    return super.getLeastSupertype(that);
  }

  @Override
  public JSType getGreatestSubtype(JSType that) {
    if (isFunctionType() && that.isFunctionType()) {
      if (equals(that)) {
        return this;
      }

      JSType functionInstance = registry.getNativeType(
          JSTypeNative.FUNCTION_INSTANCE_TYPE);
      if (functionInstance.equals(that)) {
        return this;
      } else if (functionInstance.equals(this)) {
        return that;
      }

      return registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    }

    return super.getGreatestSubtype(that);
  }

  /**
   * Given a constructor or an interface type, get its superclass constructor
   * or {@code null} if none exists.
   */
  public FunctionType getSuperClassConstructor() {
    Preconditions.checkArgument(isConstructor() || isInterface());
    ObjectType maybeSuperInstanceType = getPrototype().getImplicitPrototype();
    if (maybeSuperInstanceType == null) {
      return null;
    }
    return maybeSuperInstanceType.getConstructor();
  }

  /**
   * Given a constructor or an interface type, find out whether the unknown
   * type is a supertype of the current type.
   */
  public boolean hasUnknownSupertype() {
    Preconditions.checkArgument(isConstructor() || isInterface());
    Preconditions.checkArgument(!this.isUnknownType());
    // Potential infinite loop if our type system messes up or someone defines
    // a bad type. Otherwise the loop should always end.
    FunctionType ctor = this;
    while (true) {
      ObjectType maybeSuperInstanceType =
          ctor.getPrototype().getImplicitPrototype();
      if (maybeSuperInstanceType == null) {
        return false;
      }
      if (maybeSuperInstanceType.isUnknownType()) {
        return true;
      }
      ctor = maybeSuperInstanceType.getConstructor();
      if (ctor == null) {
        return false;
      }
      Preconditions.checkState(ctor.isConstructor() || ctor.isInterface());
    }
  }

  /**
   * Given a constructor or an interface type and a property, finds the
   * top-most superclass that has the property defined (including this
   * constructor).
   */
  public JSType getTopMostDefiningType(String propertyName) {
    Preconditions.checkState(isConstructor() || isInterface());
    Preconditions.checkArgument(getPrototype().hasProperty(propertyName));
    FunctionType ctor = this;
    JSType topInstanceType;
    do {
      topInstanceType = ctor.getInstanceType();
      ctor = ctor.getSuperClassConstructor();
    } while (ctor != null && ctor.getPrototype().hasProperty(propertyName));
    return topInstanceType;
  }

  /**
   * Two function types are equal if their signatures match. Since they don't
   * have signatures, two interfaces are equal if their names match.
   */
  @Override
  public boolean equals(Object otherType) {
    if (!(otherType instanceof FunctionType)) {
      return false;
    }
    FunctionType that = (FunctionType) otherType;
    if (!that.isFunctionType()) {
      return false;
    }
    if (this.isConstructor()) {
      if (that.isConstructor()) {
        return this == that;
      }
      return false;
    }
    if (this.isInterface()) {
      if (that.isInterface()) {
        return this.getReferenceName().equals(that.getReferenceName());
      }
      return false;
    }
    if (that.isInterface()) {
      return false;
    }
    return this.typeOfThis.equals(that.typeOfThis) &&
        this.call.equals(that.call);
  }

  @Override
  public int hashCode() {
    return isInterface() ? getReferenceName().hashCode() : call.hashCode();
  }

  public boolean hasEqualCallType(FunctionType otherType) {
    return this.call.equals(otherType.call);
  }

  /**
   * Informally, a function is represented by
   * {@code function (params): returnType} where the {@code params} is a comma
   * separated list of types, the first one being a special
   * {@code this:T} if the function expects a known type for {@code this}.
   */
  @Override
  public String toString() {
    if (this == registry.getNativeType(JSTypeNative.FUNCTION_INSTANCE_TYPE)) {
      return "Function";
    }

    StringBuilder b = new StringBuilder(32);
    b.append("function (");
    int paramNum = (call == null || call.parameters == null) ?
        0 : call.parameters.getChildCount();
    boolean hasKnownTypeOfThis = !typeOfThis.isUnknownType();
    if (hasKnownTypeOfThis) {
      b.append("this:");
      b.append(typeOfThis.toString());
    }
    if (paramNum > 0) {
      if (hasKnownTypeOfThis) {
        b.append(", ");
      }
      Node p = call.parameters.getFirstChild();
      if (p.isVarArgs()) {
        appendVarArgsString(b, p.getJSType());
      } else {
        b.append(p.getJSType().toString());
      }
      p = p.getNext();
      while (p != null) {
        b.append(", ");
        if (p.isVarArgs()) {
          appendVarArgsString(b, p.getJSType());
        } else {
          b.append(p.getJSType().toString());
        }
        p = p.getNext();
      }
    }
    b.append(")");
    if (call != null && call.returnType != null) {
      b.append(": ");
      b.append(call.returnType);
    }
    return b.toString();
  }

  /** Gets the string representation of a var args param. */
  private void appendVarArgsString(StringBuilder builder, JSType paramType) {
    if (paramType.isUnionType()) {
      // Remove the optionalness from the var arg.
      paramType = ((UnionType) paramType).getRestrictedUnion(
          registry.getNativeType(JSTypeNative.VOID_TYPE));
    }
    builder.append("...[").append(paramType.toString()).append("]");
  }

  /**
   * A function is a subtype of another if their call methods are related via
   * subtyping and {@code this} is a subtype of {@code that} with regard to
   * the prototype chain.
   */
  @Override
  public boolean isSubtype(JSType that) {
    if (this.equals(that)) {
      return true;
    }
    if (that.isFunctionType()) {
      if (((FunctionType) that).isInterface()) {
        // Any function can be assigned to an interface function.
        return true;
      }
      if (this.isInterface()) {
        // An interface function cannot be assigned to anything.
        return false;
      }
      // If functionA is a subtype of functionB, then their "this" types
      // should be contravariant. However, this causes problems because
      // of the way we enforce overrides. Because function(this:SubFoo)
      // is not a subtype of function(this:Foo), our override check treats
      // this as an error. It also screws up out standard method
      // for aliasing constructors. Let's punt on all this for now.
      // TODO(nicksantos): fix this.
      FunctionType other = (FunctionType) that;
      return (this.isConstructor() || other.isConstructor() ||
              other.typeOfThis.isSubtype(this.typeOfThis) ||
              this.typeOfThis.isSubtype(other.typeOfThis)) &&
          this.call.isSubtype(other.call);
    }
    if (that instanceof UnionType) {
      UnionType union = (UnionType) that;
      for (JSType element : union.alternates) {
        if (this.isSubtype(element)) {
          return true;
        }
      }
    }
    return getNativeType(JSTypeNative.FUNCTION_PROTOTYPE).isSubtype(that);
  }

  @Override
  public <T> T visit(Visitor<T> visitor) {
    return visitor.caseFunctionType(this);
  }

  /**
   * Gets the type of instance of this function.
   * @throws IllegalStateException if this function is not a constructor
   *         (see {@link #isConstructor()}).
   */
  public ObjectType getInstanceType() {
    Preconditions.checkState(hasInstanceType());
    return typeOfThis;
  }

  /** Sets the instance type. This should only be used for special native types. */
  void setInstanceType(ObjectType instanceType) {
    typeOfThis = instanceType;
  }

  /**
   * Returns whether this function type has an instance type.
   */
  public boolean hasInstanceType() {
    return isConstructor() || isInterface();
  }

  /**
   * Gets the type of {@code this} in this function.
   */
  public ObjectType getTypeOfThis() {
    return typeOfThis.isNoObjectType() ?
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE) : typeOfThis;
  }

  /**
   * Gets the source node.
   */
  public Node getSource() {
    return source;
  }

  /**
   * Sets the source node.
   */
  public void setSource(Node source) {
    this.source = source;
  }

  /** Adds a type to the list of subtypes for this type. */
  private void addSubType(FunctionType subType) {
    if (subTypes == null) {
      subTypes = Lists.newArrayList();
    }
    subTypes.add(subType);
  }

  /**
   * Returns a list of types that are subtypes of this type. This is only valid
   * for constructor functions, and may be null. This allows a downward
   * traversal of the subtype graph.
   */
  public List<FunctionType> getSubTypes() {
    return subTypes;
  }

  @Override
  public boolean hasCachedValues() {
    return prototype != null || super.hasCachedValues();
  }

  /**
   * Gets the template type name.
   */
  public String getTemplateTypeName() {
    return templateTypeName;
  }

  @Override
  JSType resolveInternal(ErrorReporter t, StaticScope<JSType> scope) {
    call = (ArrowType) safeResolve(call, t, scope);
    prototype = (FunctionPrototypeType) safeResolve(prototype, t, scope);
    typeOfThis = (ObjectType) safeResolve(typeOfThis, t, scope);

    boolean changed = false;
    ImmutableList.Builder<ObjectType> resolvedInterfaces =
        ImmutableList.builder();
    for (ObjectType iface : implementedInterfaces) {
      ObjectType resolvedIface = (ObjectType) iface.resolve(t, scope);
      resolvedInterfaces.add(resolvedIface);
      changed |= (resolvedIface != iface);
    }
    if (changed) {
      implementedInterfaces = resolvedInterfaces.build();
    }

    if (subTypes != null) {
      for (int i = 0; i < subTypes.size(); i++) {
        subTypes.set(i, (FunctionType) subTypes.get(i).resolve(t, scope));
      }
    }

    return super.resolveInternal(t, scope);
  }
}

```
