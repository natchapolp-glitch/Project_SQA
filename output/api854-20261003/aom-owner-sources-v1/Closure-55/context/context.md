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
<project name="compiler" basedir="." default="jar" xmlns:artifact="antlib:org.apache.maven.artifact.ant">

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
  <property name="test" value="*Test"/>
  <property name="jsonml.dir" value="${basedir}/src/com/google/javascript/jscomp/jsonml" />
  <property name="jsonml-classes.dir" value = "${build.dir}/jsonml-classes" />
  <property name="jsonml-jarfile" value="${build.dir}/secure_compiler.jar" />

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
  <path id="maven-ant-tasks.classpath" path="${tools.dir}/maven-ant-tasks-2.1.1.jar" />
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
         dir="lib/rhino/src/mozilla/js/rhino/"
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
      <zipfileset src="${buildlib.dir}/rhino1_7R3pre/js.jar"/>
      <rule pattern="org.mozilla.javascript.**"
            result="com.google.javascript.jscomp.mozilla.rhino.@1"/>
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
           excludes=".svn,**/jsonml/**,**/testing/**"
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
    <copy file="${src.dir}/com/google/javascript/jscomp/js/runtime_type_check.js"
          todir="${classes.dir}/com/google/javascript/jscomp/js" />
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
           excludes=".svn">
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

  <target name="test-forkless"
      depends="compile-tests"
      description="execute JUnit tests without forking the process">
    <junit showoutput="true">
      <classpath refid="allclasspath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <formatter type="plain" usefile="false" />
      <batchtest haltonfailure="true">
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
      <classpath refid="allclasspath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <formatter type="plain" usefile="false" />
      <batchtest haltonfailure="true">
        <fileset dir="${build.dir}/test">
          <include name="**/*Test.class" />
        </fileset>
      </batchtest>
    </junit>
  </target>

  <target name="testclass"
          depends="compile-tests"
          description="compile and execute one JUnit class specified with the -Dtest flag.">
    <junit printsummary="on" fork="true" forkmode="once" showoutput="true">
      <classpath refid="allclasspath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <formatter type="plain" usefile="false" />
      <batchtest>
        <fileset dir="${build.dir}/test">
          <include name="**/${test}.class" />
        </fileset>
      </batchtest>
    </junit>
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
</project>

```

## src/com/google/javascript/jscomp/FunctionRewriter.java

```
/*
 * Copyright 2009 The Closure Compiler Authors.
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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

/**
 * Reduces the size of common function expressions.
 *
 * This pass will rewrite:
 *
 * C.prototype.getA = function() { return this.a_ };
 * C.prototype.setA = function(newValue) { this.a_ = newValue };
 *
 * as:
 *
 * C.prototype.getA = JSCompiler_get("a_);
 * C.prototype.setA = JSCompiler_set("a_);
 *
 * if by doing so we will save bytes, after the helper functions are
 * added and renaming is done.
 *
 */
class FunctionRewriter implements CompilerPass {
  private final AbstractCompiler compiler;
  // Safety margin used to avoid growing simple programs by a few bytes.
  // Selected arbitrarily.
  private static final int SAVINGS_THRESHOLD = 16;

  FunctionRewriter(AbstractCompiler compiler) {
    this.compiler = compiler;
  }

  @Override
  public void process(Node externs, Node root) {
    List<Reducer> reducers = ImmutableList.of(new ReturnConstantReducer(),
                                              new GetterReducer(),
                                              new SetterReducer(),
                                              new EmptyFunctionReducer(),
                                              new IdentityReducer());

    Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

    // Accumulate possible reductions in the reduction multi map.  They
    // will be applied in the loop below.
    NodeTraversal.traverse(compiler, root,
                           new ReductionGatherer(reducers, reductionMap));

    // Apply reductions iff they will provide some savings.
    for (Reducer reducer : reducers) {
      Collection<Reduction> reductions = reductionMap.get(reducer);
      if (reductions.isEmpty()) {
        continue;
      }

      Node helperCode = parseHelperCode(reducer);
      if (helperCode == null) {
        continue;
      }

      int helperCodeCost = InlineCostEstimator.getCost(helperCode);

      // Estimate savings
      int savings = 0;
      for (Reduction reduction : reductions) {
        savings += reduction.estimateSavings();
      }

      // Compare estimated savings against the helper cost.  Apply
      // reductions if doing so will result in some savings.
      if (savings > (helperCodeCost + SAVINGS_THRESHOLD)) {
        for (Reduction reduction : reductions) {
          reduction.apply();
        }

        Node addingRoot = compiler.getNodeForCodeInsertion(null);
        addingRoot.addChildrenToFront(helperCode);
        compiler.reportCodeChange();
      }
    }
  }

  /**
   * Parse helper code needed by a reducer.
   *
   * @return Helper code root.  If parse fails, return null.
   */
  public Node parseHelperCode(Reducer reducer) {
    Node root = compiler.parseSyntheticCode(
        reducer.getClass().toString() + ":helper", reducer.getHelperSource());
    return (root != null) ? root.removeFirstChild() : null;
  }

  private static boolean isReduceableFunctionExpression(Node n) {
    return NodeUtil.isFunctionExpression(n)
        && !NodeUtil.isGetOrSetKey(n.getParent());
  }

  /**
   * Information needed to apply a reduction.
   */
  private class Reduction {
    private final Node parent;
    private final Node oldChild;
    private final Node newChild;

    Reduction(Node parent, Node oldChild, Node newChild) {
      this.parent = parent;
      this.oldChild = oldChild;
      this.newChild = newChild;
    }

    /**
     * Apply the reduction by replacing the old child with the new child.
     */
    void apply() {
      parent.replaceChild(oldChild, newChild);
      compiler.reportCodeChange();
    }

    /**
     * Estimate number of bytes saved by applying this reduction.
     */
    int estimateSavings() {
      return InlineCostEstimator.getCost(oldChild) -
          InlineCostEstimator.getCost(newChild);
    }
  }

  /**
   * Gathers a list of reductions to apply later by doing an in-order
   * AST traversal.  If a suitable reduction is found, stop traversal
   * in that branch.
   */
  private class ReductionGatherer implements Callback {
    private final List<Reducer> reducers;
    private final Multimap<Reducer, Reduction> reductions;

    /**
     * @param reducers List of reducers to apply during traversal.
     * @param reductions Reducer -> Reduction multimap,
     *                   populated during traversal.
     */
    ReductionGatherer(List<Reducer> reducers,
                      Multimap<Reducer, Reduction> reductions) {
      this.reducers = reducers;
      this.reductions = reductions;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal raversal,
                                  Node node,
                                  Node parent) {
      for (Reducer reducer : reducers) {
        Node replacement = reducer.reduce(node);
        if (replacement != node) {
          reductions.put(reducer, new Reduction(parent, node, replacement));
          return false;
        }
      }
      return true;
    }


    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
    }
  }

  /**
   * Interface implemented by the strength-reduction optimizers below.
   */
  abstract static class Reducer {
    /**
     * @return js source for helper methods used by this reduction.
     */
    abstract String getHelperSource();

    /**
     * @return root of the reduced subtree if a reduction was applied;
     *         otherwise returns the node argument.
     */
    abstract Node reduce(Node node);

    /**
     * Builds a method call based on the the given method name,
     * argument and history.
     *
     * @param methodName Method to call.
     * @param argumentNode Method argument.
     * @param lineno line number in original source.
     * @param charno character offset in original line.
     */
    protected final Node buildCallNode(String methodName, Node argumentNode,
                                       int lineno, int charno) {
      Node call = new Node(Token.CALL, lineno, charno);
      call.putBooleanProp(Node.FREE_CALL, true);
      call.addChildToBack(Node.newString(Token.NAME, methodName));
      if (argumentNode != null) {
        call.addChildToBack(argumentNode.cloneTree());
      }
      return call;
    }
  }

  /**
   * Reduces return immutable constant literal methods declarations
   * with calls to a constant return method factory.
   *
   * Example:
   *   a.prototype.b = function() {}
   * is reduced to:
   *   a.prototype.b = emptyFn();
   */
  private static class EmptyFunctionReducer extends Reducer {
    static final String FACTORY_METHOD_NAME = "JSCompiler_emptyFn";
    static final String HELPER_SOURCE =
        "function " + FACTORY_METHOD_NAME + "() {" +
        "  return function() {}" +
        "}";

    @Override
    public String getHelperSource() {
      return HELPER_SOURCE;
    }

    @Override
    public Node reduce(Node node) {
      if (NodeUtil.isEmptyFunctionExpression(node)) {
        return buildCallNode(FACTORY_METHOD_NAME, null,
                             node.getLineno(), node.getCharno());
      } else {
        return node;
      }
    }
  }

  /**
   * Base class for reducers that match functions that contain a
   * single return statement.
   */
  abstract static class SingleReturnStatementReducer extends Reducer {

    /**
     * @return function return value node if function body contains a
     * single return statement.  Otherwise, null.
     */
    protected final Node maybeGetSingleReturnRValue(Node functionNode) {
      Node body = functionNode.getLastChild();
      if (!body.hasOneChild()) {
        return null;
      }

      Node statement = body.getFirstChild();
      if (statement.getType() == Token.RETURN) {
        return statement.getFirstChild();
      }
      return null;
    }
  }

  /**
   * Reduces property getter method declarations with calls to a
   * getter method factory.
   *
   * Example:
   *   a.prototype.b = function(a) {return a}
   * is reduced to:
   *   a.prototype.b = getter(a);
   */
  private static class IdentityReducer extends SingleReturnStatementReducer {
    static final String FACTORY_METHOD_NAME = "JSCompiler_identityFn";
    static final String HELPER_SOURCE =
        "function " + FACTORY_METHOD_NAME + "() {" +
        "  return function(" + FACTORY_METHOD_NAME + "_value) {" +
             "return " + FACTORY_METHOD_NAME + "_value}" +
        "}";

    @Override
    public String getHelperSource() {
      return HELPER_SOURCE;
    }

    @Override
    public Node reduce(Node node) {
      if (!isReduceableFunctionExpression(node)) {
        return node;
      }

      if (isIdentityFunction(node)) {
        return buildCallNode(FACTORY_METHOD_NAME, null,
                             node.getLineno(), node.getCharno());
      } else {
        return node;
      }
    }

    /**
     * Checks if the function matches the pattern:
     *   function(<value>, <rest>) {return <value>}
     *
     * @return Whether the function matches the pattern.
     */
    private boolean isIdentityFunction(Node functionNode) {
      Node argList = functionNode.getFirstChild().getNext();
      Node paramNode = argList.getFirstChild();
      if (paramNode == null) {
        return false;
      }

      Node value = maybeGetSingleReturnRValue(functionNode);
      if (value != null &&
          NodeUtil.isName(value) &&
          value.getString().equals(paramNode.getString())) {
        return true;
      }
      return false;
    }
  }

  /**
   * Reduces return immutable constant literal methods declarations
   * with calls to a constant return method factory.
   *
   * Example:
   *   a.prototype.b = function() {return 10}
   * is reduced to:
   *   a.prototype.b = returnconst(10);
   */
  private static class ReturnConstantReducer
      extends SingleReturnStatementReducer {
    static final String FACTORY_METHOD_NAME = "JSCompiler_returnArg";
    static final String HELPER_SOURCE =
        "function " + FACTORY_METHOD_NAME +
        "(" + FACTORY_METHOD_NAME + "_value) {" +
        "  return function() {return " + FACTORY_METHOD_NAME + "_value}" +
        "}";

    @Override
    public String getHelperSource() {
      return HELPER_SOURCE;
    }

    @Override
    public Node reduce(Node node) {
      if (!isReduceableFunctionExpression(node)) {
        return node;
      }

      Node valueNode = getValueNode(node);
      if (valueNode != null) {
        return buildCallNode(FACTORY_METHOD_NAME, valueNode,
                             node.getLineno(), node.getCharno());
      } else {
        return node;
      }
    }

    /**
     * Checks if the function matches the pattern:
     *   function(<args>) {return <immutable value>}
     * and returns <immutable value> if a match is found.
     *
     * @return the immutable value node; or null.
     */
    private Node getValueNode(Node functionNode) {
      Node value = maybeGetSingleReturnRValue(functionNode);
      if (value != null &&
          NodeUtil.isImmutableValue(value)) {
        return value;
      }
      return null;
    }
  }

  /**
   * Reduces property getter method declarations with calls to a
   * getter method factory.
   *
   * Example:
   *   a.prototype.b = function() {return this.b_}
   * is reduced to:
   *   a.prototype.b = getter("b_");
   */
  private static class GetterReducer extends SingleReturnStatementReducer {
    static final String FACTORY_METHOD_NAME = "JSCompiler_get";
    static final String HELPER_SOURCE =
        "function " + FACTORY_METHOD_NAME + "(" +
        FACTORY_METHOD_NAME + "_name) {" +
        "  return function() {return this[" + FACTORY_METHOD_NAME + "_name]}" +
        "}";

    @Override
    public String getHelperSource() {
      return HELPER_SOURCE;
    }

    @Override
    public Node reduce(Node node) {
      if (!isReduceableFunctionExpression(node)) {
        return node;
      }

      Node propName = getGetPropertyName(node);
      if (propName != null) {
        if (propName.getType() != Token.STRING) {
          throw new IllegalStateException(
              "Expected STRING, got " + Token.name(propName.getType()));
        }

        return buildCallNode(FACTORY_METHOD_NAME, propName,
                             node.getLineno(), node.getCharno());
      } else {
        return node;
      }
    }

    /**
     * Checks if the function matches the pattern:
     *   function(<args>) {return this.<name>}
     * and returns <name> if a match is found.
     *
     * @return STRING node that is the rhs of a this property get; or null.
     */
    private Node getGetPropertyName(Node functionNode) {
      Node value = maybeGetSingleReturnRValue(functionNode);
      if (value != null &&
          NodeUtil.isGetProp(value) &&
          NodeUtil.isThis(value.getFirstChild())) {
        return value.getLastChild();
      }
      return null;
    }
  }

  /**
   * Reduces property setter method declarations with calls to a
   * setter method factory.
   *
   * Example:
   *   a.prototype.setB = function(value) {this.b_ = value}
   * reduces to:
   *   a.prototype.setB = getter("b_");
   */
  private static class SetterReducer extends Reducer {
    static final String FACTORY_METHOD_NAME = "JSCompiler_set";
    static final String HELPER_SOURCE =
        "function " + FACTORY_METHOD_NAME + "(" +
        FACTORY_METHOD_NAME + "_name) {" +
        "  return function(" + FACTORY_METHOD_NAME + "_value) {" +
        "this[" + FACTORY_METHOD_NAME + "_name] = " +
        FACTORY_METHOD_NAME + "_value}" +
        "}";

    @Override
    public String getHelperSource() {
      return HELPER_SOURCE;
    }

    @Override
    public Node reduce(Node node) {
      if (!isReduceableFunctionExpression(node)) {
        return node;
      }

      Node propName = getSetPropertyName(node);
      if (propName != null) {
        if (propName.getType() != Token.STRING) {
          throw new IllegalStateException(
              "Expected STRING, got " + Token.name(propName.getType()));
        }

        return buildCallNode(FACTORY_METHOD_NAME, propName,
                             node.getLineno(), node.getCharno());
      } else {
        return node;
      }
    }

    /**
     * Checks if the function matches the pattern:
     *   function(<value>, <rest>) {this.<name> = <value>}
     * and returns <name> if a match is found.
     *
     * @return STRING node that is the rhs of a this property get; or null.
     */
    private Node getSetPropertyName(Node functionNode) {
      Node body = functionNode.getLastChild();
      if (!body.hasOneChild()) {
        return null;
      }

      Node argList = functionNode.getFirstChild().getNext();
      Node paramNode = argList.getFirstChild();
      if (paramNode == null) {
        return null;
      }

      Node statement = body.getFirstChild();
      if (!NodeUtil.isExprAssign(statement)) {
        return null;
      }

      Node assign = statement.getFirstChild();
      Node lhs = assign.getFirstChild();
      if (NodeUtil.isGetProp(lhs) && NodeUtil.isThis(lhs.getFirstChild())) {
        Node rhs = assign.getLastChild();
        if (NodeUtil.isName(rhs) &&
            rhs.getString().equals(paramNode.getString())) {
          Node propertyName = lhs.getLastChild();
          return propertyName;
        }
      }
      return null;
    }
  }
}

```
