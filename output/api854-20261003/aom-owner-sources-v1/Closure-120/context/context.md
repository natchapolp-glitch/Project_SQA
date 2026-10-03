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

  <!-- gather release version -->
  <target name="relversion">
    <exec outputproperty="build.relVersion"
      executable="git"
      failonerror="false"
      failifexecutionfails="false"
      dir=".">
      <arg value="describe"/>
      <arg value="--tag"/>
      <arg value="--always"/>
    </exec>
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

  <!-- Generate pom.xml with the proper rel build number -->
  <target name="pom" depends="relversion">
    <copy file="closure-compiler.pom" tofile="${build.dir}/pom.xml">
      <filterset>
        <filter token="build.relVersion"
             value="${build.relVersion}"/>
      </filterset>
    </copy>

    <property name="compiler-jarfile-nodeps" value="${build.dir}/closure-${ant.project.name}-${build.relVersion}.jar" />
    <property name="compiler-jarfile-javadoc" value="${build.dir}/closure-${ant.project.name}-${build.relVersion}-javadoc.jar" />
    <property name="compiler-jarfile-sources" value="${build.dir}/closure-${ant.project.name}-${build.relVersion}-sources.jar" />

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
          depends="rhino-jarjar,relversion">
    <mkdir dir="${classes.dir}" />
    <javac debug="true" srcdir="${gen.dir}"
           destdir="${classes.dir}"
           excludes=".svn,.git"
           >
      <classpath refid="srcclasspath.path" />
    </javac>
    <javac debug="true" srcdir="${src.dir}"
           destdir="${classes.dir}"
           excludes=".svn,.git,**/jsonml/**,**/webservice/**,**/testing/**"
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
      <entry key="compiler.version" value="${build.relVersion}"/>
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
           excludes=".svn,.git"
           >
      <classpath refid="allclasspath.path" />
    </javac>
    <javac debug="true" srcdir="${test.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn,.git"
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
         windowtitle="Closure Compiler"
         additionalparam=" -notimestamp ">
      <sourcepath>
        <pathelement location="${src.dir}" />
        <pathelement location="${gen.dir}" />
      </sourcepath>
      <classpath refid="allclasspath.path" />
      <link href="http://java.sun.com/javase/6/docs/api/" />
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
           excludes=".svn,.git"
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
           excludes=".svn,.git"
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

## src/com/google/javascript/jscomp/ReferenceCollectingCallback.java

```
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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.StaticReference;
import com.google.javascript.rhino.jstype.StaticSourceFile;
import com.google.javascript.rhino.jstype.StaticSymbolTable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A helper class for passes that want to access all information about where a
 * variable is referenced and declared at once and then make a decision as to
 * how it should be handled, possibly inlining, reordering, or generating
 * warnings. Callers do this by providing {@link Behavior} and then
 * calling {@link #process(Node, Node)}.
 *
 * @author kushal@google.com (Kushal Dave)
 */
class ReferenceCollectingCallback implements ScopedCallback,
    HotSwapCompilerPass,
    StaticSymbolTable<Var, ReferenceCollectingCallback.Reference> {

  /**
   * Maps a given variable to a collection of references to that name. Note that
   * Var objects are not stable across multiple traversals (unlike scope root or
   * name).
   */
  private final Map<Var, ReferenceCollection> referenceMap =
      Maps.newHashMap();

  /**
   * The stack of basic blocks and scopes the current traversal is in.
   */
  private final Deque<BasicBlock> blockStack = new ArrayDeque<BasicBlock>();

  /**
   * Source of behavior at various points in the traversal.
   */
  private final Behavior behavior;

  /**
   * JavaScript compiler to use in traversing.
   */
  private final AbstractCompiler compiler;

  /**
   * Only collect references for filtered variables.
   */
  private final Predicate<Var> varFilter;

  /**
   * Constructor initializes block stack.
   */
  ReferenceCollectingCallback(AbstractCompiler compiler, Behavior behavior) {
    this(compiler, behavior, Predicates.<Var>alwaysTrue());
  }

  /**
   * Constructor only collects references that match the given variable.
   *
   * The test for Var equality uses reference equality, so it's necessary to
   * inject a scope when you traverse.
   */
  ReferenceCollectingCallback(AbstractCompiler compiler, Behavior behavior,
      Predicate<Var> varFilter) {
    this.compiler = compiler;
    this.behavior = behavior;
    this.varFilter = varFilter;
  }

  /**
   * Convenience method for running this pass over a tree with this
   * class as a callback.
   */
  @Override
  public void process(Node externs, Node root) {
    NodeTraversal.traverseRoots(
        compiler, Lists.newArrayList(externs, root), this);
  }

  /**
   * Same as process but only runs on a part of AST associated to one script.
   */
  @Override
  public void hotSwapScript(Node scriptRoot, Node originalRoot) {
    NodeTraversal.traverse(compiler, scriptRoot, this);
  }

  /**
   * Gets the variables that were referenced in this callback.
   */
  @Override
  public Iterable<Var> getAllSymbols() {
    return referenceMap.keySet();
  }

  @Override
  public Scope getScope(Var var) {
    return var.scope;
  }

  /**
   * Gets the reference collection for the given variable.
   */
  @Override
  public ReferenceCollection getReferences(Var v) {
    return referenceMap.get(v);
  }

  /**
   * For each node, update the block stack and reference collection
   * as appropriate.
   */
  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isName()) {
      Var v;
      if (n.getString().equals("arguments")) {
        v = t.getScope().getArgumentsVar();
      } else {
        v = t.getScope().getVar(n.getString());
      }
      if (v != null && varFilter.apply(v)) {
        addReference(v, new Reference(n, t, blockStack.peek()));
      }
    }

    if (isBlockBoundary(n, parent)) {
      blockStack.pop();
    }
  }

  /**
   * Updates block stack and invokes any additional behavior.
   */
  @Override
  public void enterScope(NodeTraversal t) {
    Node n = t.getScope().getRootNode();
    BasicBlock parent = blockStack.isEmpty() ? null : blockStack.peek();
    blockStack.push(new BasicBlock(parent, n));
  }

  /**
   * Updates block stack and invokes any additional behavior.
   */
  @Override
  public void exitScope(NodeTraversal t) {
    blockStack.pop();
    if (t.getScope().isGlobal()) {
      // Update global scope reference lists when we are done with it.
      compiler.updateGlobalVarReferences(referenceMap, t.getScopeRoot());
      behavior.afterExitScope(t, compiler.getGlobalVarReferences());
    } else {
      behavior.afterExitScope(t, new ReferenceMapWrapper(referenceMap));
    }
  }

  /**
   * Updates block stack.
   */
  @Override
  public boolean shouldTraverse(NodeTraversal nodeTraversal, Node n,
      Node parent) {
    // If node is a new basic block, put on basic block stack
    if (isBlockBoundary(n, parent)) {
      blockStack.push(new BasicBlock(blockStack.peek(), n));
    }
    return true;
  }

  /**
   * @return true if this node marks the start of a new basic block
   */
  private static boolean isBlockBoundary(Node n, Node parent) {
    if (parent != null) {
      switch (parent.getType()) {
        case Token.DO:
        case Token.FOR:
        case Token.TRY:
        case Token.WHILE:
        case Token.WITH:
          // NOTE: TRY has up to 3 child blocks:
          // TRY
          //   BLOCK
          //   BLOCK
          //     CATCH
          //   BLOCK
          // Note that there is an explicit CATCH token but no explicit
          // FINALLY token. For simplicity, we consider each BLOCK
          // a separate basic BLOCK.
          return true;
        case Token.AND:
        case Token.HOOK:
        case Token.IF:
        case Token.OR:
          // The first child of a conditional is not a boundary,
          // but all the rest of the children are.
          return n != parent.getFirstChild();

      }
    }

    return n.isCase();
  }

  private void addReference(Var v, Reference reference) {
    // Create collection if none already
    ReferenceCollection referenceInfo = referenceMap.get(v);
    if (referenceInfo == null) {
      referenceInfo = new ReferenceCollection();
      referenceMap.put(v, referenceInfo);
    }

    // Add this particular reference
    referenceInfo.add(reference);
  }

  interface ReferenceMap {
    ReferenceCollection getReferences(Var var);
  }

  private static class ReferenceMapWrapper implements ReferenceMap {
    private final Map<Var, ReferenceCollection> referenceMap;

    public ReferenceMapWrapper(Map<Var, ReferenceCollection> referenceMap) {
      this.referenceMap = referenceMap;
    }

    @Override
    public ReferenceCollection getReferences(Var var) {
      return referenceMap.get(var);
    }
  }

  /**
   * Way for callers to add specific behavior during traversal that
   * utilizes the built-up reference information.
   */
  interface Behavior {
    /**
     * Called after we finish with a scope.
     */
    void afterExitScope(NodeTraversal t, ReferenceMap referenceMap);
  }

  static final Behavior DO_NOTHING_BEHAVIOR = new Behavior() {
    @Override
    public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {}
  };

  /**
   * A collection of references. Can be subclassed to apply checks or
   * store additional state when adding.
   */
  static class ReferenceCollection implements Iterable<Reference> {

    List<Reference> references = Lists.newArrayList();

    @Override
    public Iterator<Reference> iterator() {
      return references.iterator();
    }

    void add(Reference reference) {
      references.add(reference);
    }

    /**
     * Determines if the variable for this reference collection is
     * "well-defined." A variable is well-defined if we can prove at
     * compile-time that it's assigned a value before it's used.
     *
     * Notice that if this function returns false, this doesn't imply that the
     * variable is used before it's assigned. It just means that we don't
     * have enough information to make a definitive judgment.
     */
    protected boolean isWellDefined() {
      int size = references.size();
      if (size == 0) {
        return false;
      }

      // If this is a declaration that does not instantiate the variable,
      // it's not well-defined.
      Reference init = getInitializingReference();
      if (init == null) {
        return false;
      }

      Preconditions.checkState(references.get(0).isDeclaration());
      BasicBlock initBlock = init.getBasicBlock();
      for (int i = 1; i < size; i++) {
        if (!initBlock.provablyExecutesBefore(
                references.get(i).getBasicBlock())) {
          return false;
        }
      }

      return true;
    }

    /**
     * Whether the variable is escaped into an inner scope.
     */
    boolean isEscaped() {
      Scope scope = null;
      for (Reference ref : references) {
        if (scope == null) {
          scope = ref.scope;
        } else if (scope != ref.scope) {
          return true;
        }
      }
      return false;
    }

    /**
     * @param index The index into the references array to look for an
     * assigning declaration.
     *
     * This is either the declaration if a value is assigned (such as
     * "var a = 2", "function a()...", "... catch (a)...").
     */
    private boolean isInitializingDeclarationAt(int index) {
      Reference maybeInit = references.get(index);
      if (maybeInit.isInitializingDeclaration()) {
        // This is a declaration that represents the initial value.
        // Specifically, var declarations without assignments such as "var a;"
        // are not.
        return true;
      }
      return false;
    }

    /**
     * @param index The index into the references array to look for an
     * initialized assignment reference. That is, an assignment immediately
     * follow a variable declaration that itself does not initialize the
     * variable.
     */
    private boolean isInitializingAssignmentAt(int index) {
      if (index < references.size() && index > 0) {
        Reference maybeDecl = references.get(index - 1);
        if (maybeDecl.isVarDeclaration()) {
          Preconditions.checkState(!maybeDecl.isInitializingDeclaration());
          Reference maybeInit = references.get(index);
          if (maybeInit.isSimpleAssignmentToName()) {
            return true;
          }
        }
      }
      return false;
    }

    /**
     * @return The reference that provides the value for the variable at the
     * time of the first read, if known, otherwise null.
     *
     * This is either the variable declaration ("var a = ...") or first
     * reference following the declaration if it is an assignment.
     */
    Reference getInitializingReference() {
      if (isInitializingDeclarationAt(0)) {
        return references.get(0);
      } else if (isInitializingAssignmentAt(1)) {
        return references.get(1);
      }
      return null;
    }

    /**
     * Constants are allowed to be defined after their first use.
     */
    Reference getInitializingReferenceForConstants() {
      int size = references.size();
      for (int i = 0; i < size; i++) {
        if (isInitializingDeclarationAt(i) || isInitializingAssignmentAt(i)) {
          return references.get(i);
        }
      }
      return null;
    }

    /**
     * @return Whether the variable is only assigned a value once for its
     *     lifetime.
     */
    boolean isAssignedOnceInLifetime() {
      Reference ref = getOneAndOnlyAssignment();
      if (ref == null) {
        return false;
      }

      // Make sure this assignment is not in a loop.
      for (BasicBlock block = ref.getBasicBlock();
           block != null; block = block.getParent()) {
        if (block.isFunction) {
          if (ref.getSymbol().getScope() != ref.scope) {
            return false;
          }
          break;
        } else if (block.isLoop) {
          return false;
        }
      }

      return true;
    }

    /**
     * @return The one and only assignment. Returns if there are 0 or 2+
     *    assignments.
     */
    private Reference getOneAndOnlyAssignment() {
      Reference assignment = null;
      int size = references.size();
      for (int i = 0; i < size; i++) {
        Reference ref = references.get(i);
        if (ref.isLvalue() || ref.isInitializingDeclaration()) {
          if (assignment == null) {
            assignment = ref;
          } else {
            return null;
          }
        }
      }
      return assignment;
    }

    /**
     * @return Whether the variable is never assigned a value.
     */
    boolean isNeverAssigned() {
      int size = references.size();
      for (int i = 0; i < size; i++) {
        Reference ref = references.get(i);
        if (ref.isLvalue() || ref.isInitializingDeclaration()) {
          return false;
        }
      }
      return true;
    }

    boolean firstReferenceIsAssigningDeclaration() {
      int size = references.size();
      if (size > 0 && references.get(0).isInitializingDeclaration()) {
        return true;
      }
      return false;
    }
  }

  /**
   * Represents a single declaration or reference to a variable.
   */
  static final class Reference implements StaticReference<JSType> {

    private static final Set<Integer> DECLARATION_PARENTS =
        ImmutableSet.of(Token.VAR, Token.FUNCTION, Token.CATCH);

    private final Node nameNode;
    private final BasicBlock basicBlock;
    private final Scope scope;
    private final InputId inputId;
    private final StaticSourceFile sourceFile;

    Reference(Node nameNode, NodeTraversal t,
        BasicBlock basicBlock) {
      this(nameNode, basicBlock, t.getScope(), t.getInput().getInputId());
    }

    // Bleeding functions are weird, because the declaration does
    // not appear inside their scope. So they need their own constructor.
    static Reference newBleedingFunction(NodeTraversal t,
        BasicBlock basicBlock, Node func) {
      return new Reference(func.getFirstChild(),
          basicBlock, t.getScope(), t.getInput().getInputId());
    }

    /**
     * Creates a variable reference in a given script file name, used in tests.
     *
     * @return The created reference.
     */
    @VisibleForTesting
    static Reference createRefForTest(CompilerInput input) {
      return new Reference(new Node(Token.NAME), null, null,
          input.getInputId());
    }

    private Reference(Node nameNode,
        BasicBlock basicBlock, Scope scope, InputId inputId) {
      this.nameNode = nameNode;
      this.basicBlock = basicBlock;
      this.scope = scope;
      this.inputId = inputId;
      this.sourceFile = nameNode.getStaticSourceFile();
    }

    /**
     * Makes a copy of the current reference using a new Scope instance.
     */
    Reference cloneWithNewScope(Scope newScope) {
      return new Reference(nameNode, basicBlock, newScope, inputId);
    }

    @Override
    public Var getSymbol() {
      return scope.getVar(nameNode.getString());
    }

    @Override
    public Node getNode() {
      return nameNode;
    }

    public InputId getInputId() {
      return inputId;
    }

    @Override
    public StaticSourceFile getSourceFile() {
      return sourceFile;
    }

    boolean isDeclaration() {
      Node parent = getParent();
      Node grandparent = parent.getParent();
      return DECLARATION_PARENTS.contains(parent.getType()) ||
          parent.isParamList() &&
          grandparent.isFunction();
    }

    boolean isVarDeclaration() {
      return getParent().isVar();
    }

    boolean isHoistedFunction() {
      return NodeUtil.isHoistedFunctionDeclaration(getParent());
    }

    /**
     * Determines whether the variable is initialized at the declaration.
     */
    boolean isInitializingDeclaration() {
      // VAR is the only type of variable declaration that may not initialize
      // its variable. Catch blocks, named functions, and parameters all do.
      return isDeclaration() &&
          !getParent().isVar() ||
          nameNode.getFirstChild() != null;
    }

   /**
    * @return For an assignment, variable declaration, or function declaration
    * return the assigned value, otherwise null.
    */
    Node getAssignedValue() {
      Node parent = getParent();
      return (parent.isFunction())
          ? parent : NodeUtil.getAssignedValue(nameNode);
    }

    BasicBlock getBasicBlock() {
      return basicBlock;
    }

    Node getParent() {
      return getNode().getParent();
    }

    Node getGrandparent() {
      Node parent = getParent();
      return parent == null ? null : parent.getParent();
    }

    private static boolean isLhsOfForInExpression(Node n) {
      Node parent = n.getParent();
      if (parent.isVar()) {
        return isLhsOfForInExpression(parent);
      }
      return NodeUtil.isForIn(parent) && parent.getFirstChild() == n;
    }

    boolean isSimpleAssignmentToName() {
      Node parent = getParent();
      return parent.isAssign()
          && parent.getFirstChild() == nameNode;
    }

    boolean isLvalue() {
      Node parent = getParent();
      int parentType = parent.getType();
      return (parentType == Token.VAR && nameNode.getFirstChild() != null)
          || parentType == Token.INC
          || parentType == Token.DEC
          || (NodeUtil.isAssignmentOp(parent)
              && parent.getFirstChild() == nameNode)
          || isLhsOfForInExpression(nameNode);
    }

    Scope getScope() {
      return scope;
    }
  }

  /**
   * Represents a section of code that is uninterrupted by control structures
   * (conditional or iterative logic).
   */
  static final class BasicBlock {

    private final BasicBlock parent;

    /**
     * Determines whether the block may not be part of the normal control flow,
     * but instead "hoisted" to the top of the scope.
     */
    private final boolean isHoisted;

    /**
     * Whether this block denotes a function scope.
     */
    private final boolean isFunction;

    /**
     * Whether this block denotes a loop.
     */
    private final boolean isLoop;

    /**
     * Creates a new block.
     * @param parent The containing block.
     * @param root The root node of the block.
     */
    BasicBlock(BasicBlock parent, Node root) {
      this.parent = parent;

      // only named functions may be hoisted.
      this.isHoisted = NodeUtil.isHoistedFunctionDeclaration(root);

      this.isFunction = root.isFunction();

      if (root.getParent() != null) {
        int pType = root.getParent().getType();
        this.isLoop = pType == Token.DO ||
            pType == Token.WHILE ||
            pType == Token.FOR;
      } else {
        this.isLoop = false;
      }
    }

    BasicBlock getParent() {
      return parent;
    }

    /**
     * Determines whether this block is equivalent to the very first block that
     * is created when reference collection traversal enters global scope. Note
     * that when traversing a single script in a hot-swap fashion a new instance
     * of {@code BasicBlock} is created.
     *
     * @return true if this is global scope block.
     */
    boolean isGlobalScopeBlock() {
      return getParent() == null;
    }

    /**
     * Determines whether this block is guaranteed to begin executing before
     * the given block does.
     */
    boolean provablyExecutesBefore(BasicBlock thatBlock) {
      // If thatBlock is a descendant of this block, and there are no hoisted
      // blocks between them, then this block must start before thatBlock.
      BasicBlock currentBlock;
      for (currentBlock = thatBlock;
           currentBlock != null && currentBlock != this;
           currentBlock = currentBlock.getParent()) {
        if (currentBlock.isHoisted) {
          return false;
        }
      }

      if (currentBlock == this) {
        return true;
      }
      if (isGlobalScopeBlock() && thatBlock.isGlobalScopeBlock()) {
        return true;
      }
      return false;
    }
  }
}

```
