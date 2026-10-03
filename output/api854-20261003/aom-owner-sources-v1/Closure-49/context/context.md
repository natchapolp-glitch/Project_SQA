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

## src/com/google/javascript/jscomp/MakeDeclaredNamesUnique.java

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

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  Find all Functions, VARs, and Exception names and make them
 *  unique.  Specifically, it will not modify object properties.
 *  @author johnlenz@google.com (John Lenz)
 *  TODO(johnlenz): Try to merge this with the ScopeCreator.
 */
class MakeDeclaredNamesUnique
    implements NodeTraversal.ScopedCallback {

  // Arguments is special cased to handle cases where a local name shadows
  // the arguments declaration.
  public static final String ARGUMENTS = "arguments";

  // The name stack is similiar to how we model scopes but handles some
  // additional cases that are not handled by the current Scope object.
  // Specifically, a Scope currently has only two concepts of scope (global,
  // and function local).  But there are in reality a couple of additional
  // case to worry about:
  //   catch expressions
  //   function expressions names
  // Both belong to a scope by themselves.
  private Deque<Renamer> nameStack = new ArrayDeque<Renamer>();
  private final Renamer rootRenamer;

  MakeDeclaredNamesUnique() {
    this(new ContextualRenamer());
  }

  MakeDeclaredNamesUnique(Renamer renamer) {
    this.rootRenamer = renamer;
  }

  static CompilerPass getContextualRenameInverter(AbstractCompiler compiler) {
    return new ContextualRenameInverter(compiler);
  }

  @Override
  public void enterScope(NodeTraversal t) {
    Node declarationRoot = t.getScopeRoot();
    Renamer renamer;
    if (nameStack.isEmpty()) {
      // If the contextual renamer is being used the starting context can not
      // be a function.
      Preconditions.checkState(
          declarationRoot.getType() != Token.FUNCTION ||
          !(rootRenamer instanceof ContextualRenamer));
      Preconditions.checkState(t.inGlobalScope());
      renamer = rootRenamer;
    } else {
      renamer = nameStack.peek().forChildScope();
    }

    if (declarationRoot.getType() != Token.FUNCTION) {
      // Add the block declarations
      findDeclaredNames(declarationRoot, null, renamer);
    }
    nameStack.push(renamer);
  }

  @Override
  public void exitScope(NodeTraversal t) {
    if (!t.inGlobalScope()) {
      nameStack.pop();
    }
  }

  @Override
  public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

    switch (n.getType()) {
      case Token.FUNCTION:
        {
          // Add recursive function name, if needed.
          // NOTE: "enterScope" is called after we need to pick up this name.
          Renamer renamer = nameStack.peek().forChildScope();

          // If needed, add the function recursive name.
          String name = n.getFirstChild().getString();
          if (name != null && !name.isEmpty() && parent != null
              && !NodeUtil.isFunctionDeclaration(n)) {
            renamer.addDeclaredName(name);
          }

          nameStack.push(renamer);
        }
        break;

      case Token.LP: {
          Renamer renamer = nameStack.peek().forChildScope();

          // Add the function parameters
          for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
            String name = c.getString();
            renamer.addDeclaredName(name);
          }

          // Add the function body declarations
          Node functionBody = n.getNext();
          findDeclaredNames(functionBody, null, renamer);

          nameStack.push(renamer);
        }
        break;

      case Token.CATCH:
        {
          Renamer renamer = nameStack.peek().forChildScope();

          String name = n.getFirstChild().getString();
          renamer.addDeclaredName(name);

          nameStack.push(renamer);
        }
        break;
    }

    return true;
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    switch (n.getType()) {
      case Token.NAME:
        String newName = getReplacementName(n.getString());
        if (newName != null) {
          Renamer renamer = nameStack.peek();
          if (renamer.stripConstIfReplaced()) {
            // TODO(johnlenz): Do we need to do anything about the javadoc?
            n.removeProp(Node.IS_CONSTANT_NAME);
          }
          n.setString(newName);
          t.getCompiler().reportCodeChange();
        }
        break;

      case Token.FUNCTION:
        // Remove the function body scope
        nameStack.pop();
        // Remove function recursive name (if any).
        nameStack.pop();
        break;

      case Token.LP:
        // Note: The parameters and function body variables live in the
        // same scope, we introduce the scope when in the "shouldTraverse"
        // visit of LP, but remove it when when we exit the function above.
        break;

      case Token.CATCH:
        // Remove catch except name from the stack of names.
        nameStack.pop();
        break;
    }
  }

  /**
   * Walks the stack of name maps and finds the replacement name for the
   * current scope.
   */
  private String getReplacementName(String oldName) {
    for (Renamer names : nameStack) {
      String newName = names.getReplacementName(oldName);
      if (newName != null) {
        return newName;
      }
    }
    return null;
  }

  /**
   * Traverses the current scope and collects declared names.  Does not
   * decent into functions or add CATCH exceptions.
   */
  private void findDeclaredNames(Node n, Node parent, Renamer renamer) {
    // Do a shallow traversal, so don't traverse into function declarations,
    // except for the name of the function itself.
    if (parent == null
        || parent.getType() != Token.FUNCTION
        || n == parent.getFirstChild()) {
      if (NodeUtil.isVarDeclaration(n)) {
        renamer.addDeclaredName(n.getString());
      } else if (NodeUtil.isFunctionDeclaration(n)) {
        Node nameNode = n.getFirstChild();
        renamer.addDeclaredName(nameNode.getString());
      }

      for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        findDeclaredNames(c, n, renamer);
      }
    }
  }

  /**
   * Declared names renaming policy interface.
   */
  interface Renamer {

    /**
     * Called when a declared name is found in the local current scope.
     */
    void addDeclaredName(String name);

    /**
     * @return A replacement name, null if oldName is unknown or should not
     * be replaced.
     */
    String getReplacementName(String oldName);

    /**
     * @return Whether the constant-ness of a name should be removed.
     */
    boolean stripConstIfReplaced();

    /**
     * @return A Renamer for a scope within the scope of the current Renamer.
     */
    Renamer forChildScope();
  }

  /**
   * Inverts the transformation by {@link ContextualRenamer}, when possible.
   */
  static class ContextualRenameInverter
      implements ScopedCallback, CompilerPass {
    private final AbstractCompiler compiler;

    // The set of names referenced in the current scope.
    private Set<String> referencedNames = ImmutableSet.of();

    // Stack reference sets.
    private Deque<Set<String>> referenceStack = new ArrayDeque<Set<String>>();

    // Name are globally unique initially, so we don't need a per-scope map.
    private Map<String, List<Node>> nameMap = Maps.newHashMap();

    private ContextualRenameInverter(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void process(Node externs, Node js) {
      NodeTraversal.traverse(compiler, js, this);
    }

    public static String getOrginalName(String name) {
      int index = indexOfSeparator(name);
      return (index == -1) ? name : name.substring(0, index);
    }

    private static int indexOfSeparator(String name) {
      return name.lastIndexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR);
    }

    private boolean containsSeparator(String name) {
      return name.indexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR) != -1;
    }

    /**
     * Prepare a set for the new scope.
     */
    @Override
    public void enterScope(NodeTraversal t) {
      if (t.inGlobalScope()) {
        return;
      }

      referenceStack.push(referencedNames);
      referencedNames = Sets.newHashSet();
    }

    /**
     * Rename vars for the current scope, and merge any referenced
     * names into the parent scope reference set.
     */
    @Override
    public void exitScope(NodeTraversal t) {
      if (t.inGlobalScope()) {
        return;
      }

      for (Iterator<Var> it = t.getScope().getVars(); it.hasNext();) {
        Var v = it.next();
        handleScopeVar(v);
      }

      // Merge any names that were referenced but not declared in the current
      // scope.
      Set<String> current = referencedNames;
      referencedNames = referenceStack.pop();
      // If there isn't anything left in the stack we will be going into the
      // global scope: don't try to build a set of referenced names for the
      // global scope.
      if (!referenceStack.isEmpty()) {
        referencedNames.addAll(current);
      }
    }

    /**
     * For the Var declared in the current scope determine if it is possible
     * to revert the name to its orginal form without conflicting with other
     * values.
     */
    void handleScopeVar(Var v) {
      String name  = v.getName();
      if (containsSeparator(name) && !getOrginalName(name).isEmpty()) {
        String newName = findReplacementName(name);
        referencedNames.remove(name);
        // Adding a reference to the new name to prevent either the parent
        // scopes or the current scope renaming another var to this new name.
        referencedNames.add(newName);
        List<Node> references = nameMap.get(name);
        Preconditions.checkState(references != null);
        for (Node n : references) {
          Preconditions.checkState(n.getType() == Token.NAME);
          n.setString(newName);
        }
        compiler.reportCodeChange();
        nameMap.remove(name);
      }
    }

    /**
     * Find a name usable in the local scope.
     */
    private String findReplacementName(String name) {
      String original = getOrginalName(name);
      String newName = original;
      int i = 0;
      while (!isValidName(newName)) {
        newName = original +
            ContextualRenamer.UNIQUE_ID_SEPARATOR + String.valueOf(i++);
      }
      return newName;
    }

    /**
     * @return Whether the name is valid to use in the local scope.
     */
    private boolean isValidName(String name) {
      if (TokenStream.isJSIdentifier(name) &&
          !referencedNames.contains(name) &&
          !name.equals(ARGUMENTS)) {
        return true;
      }
      return false;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node node, Node parent) {
      if (t.inGlobalScope()) {
        return;
      }

      if (NodeUtil.isReferenceName(node)) {
        String name = node.getString();
        // Add all referenced names to the set so it is possible to check for
        // conflicts.
        referencedNames.add(name);
        // Store only references to candidate names in the node map.
        if (containsSeparator(name)) {
          addCandidateNameReference(name, node);
        }
      }
    }

    private void addCandidateNameReference(String name, Node n) {
      List<Node> nodes = nameMap.get(name);
      if (null == nodes) {
        nodes = Lists.newLinkedList();
        nameMap.put(name, nodes);
      }
      nodes.add(n);
    }
  }

  /**
   * Rename every locally name to be unique, the first encountered declaration
   * (specifically global names) are left in their original form. Those that are
   * renamed are made unique by giving them a unique suffix based on
   * the number of declarations of the name.
   *
   * The root ContextualRenamer is assumed to be in GlobalScope.
   *
   * Used by the Normalize pass.
   * @see Normalize
   */
  static class ContextualRenamer implements Renamer {
    private final Multiset<String> nameUsage;
    private final Map<String, String> declarations = Maps.newHashMap();
    private final boolean global;

    final static String UNIQUE_ID_SEPARATOR = "$$";

    ContextualRenamer() {
      this.global = true;
      nameUsage = HashMultiset.create();
    }

    /**
     * Constructor for child scopes.
     */
    private ContextualRenamer(Multiset<String> nameUsage) {
      this.global = false;
      this.nameUsage = nameUsage;
    }

    /**
     * Create a ContextualRenamer
     */
    @Override
    public Renamer forChildScope() {
      return new ContextualRenamer(nameUsage);
    }

    /**
     * Adds a name to the map of names declared in this scope.
     */
    @Override
    public void addDeclaredName(String name) {
      if (!name.equals(ARGUMENTS)) {
        if (global) {
          reserveName(name);
        } else {
          // It hasn't been declared locally yet, so increment the count.
          if (!declarations.containsKey(name)) {
            int id = incrementNameCount(name);
            String newName = null;
            if (id != 0) {
              newName = getUniqueName(name, id);
            }
            declarations.put(name, newName);
          }
        }
      }
    }

    @Override
    public String getReplacementName(String oldName) {
      return declarations.get(oldName);
    }

    /**
     * Given a name and the associated id, create a new unique name.
     */
    private String getUniqueName(String name, int id) {
      return name + UNIQUE_ID_SEPARATOR + id;
    }

    private void reserveName(String name) {
      nameUsage.setCount(name, 0, 1);
    }

    private int incrementNameCount(String name) {
      return nameUsage.add(name, 1);
    }

    @Override
    public boolean stripConstIfReplaced() {
      return false;
    }
  }


  /**
   * Rename every declared name to be unique. Typically this would be used
   * when injecting code to insure that names do not conflict with existing
   * names.
   *
   * Used by the FunctionInjector
   * @see FunctionInjector
   */
  static class InlineRenamer implements Renamer {
    private final Map<String, String> declarations = Maps.newHashMap();
    private final Supplier<String> uniqueIdSupplier;
    private final String idPrefix;
    private final boolean removeConstness;

    InlineRenamer(
        Supplier<String> uniqueIdSupplier,
        String idPrefix,
        boolean removeConstness) {
      this.uniqueIdSupplier = uniqueIdSupplier;
      // To ensure that the id does not conflict with the id from the
      // ContextualRenamer some prefix is needed.
      Preconditions.checkArgument(!idPrefix.isEmpty());
      this.idPrefix = idPrefix;
      this.removeConstness = removeConstness;
    }

    @Override
    public void addDeclaredName(String name) {
      Preconditions.checkState(!name.equals(ARGUMENTS));
      if (!declarations.containsKey(name)) {
        declarations.put(name, getUniqueName(name));
      }
    }

    private String getUniqueName(String name) {
      if (name.isEmpty()) {
        return name;
      }

      if (name.indexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR) != -1) {
          name = name.substring(
              0, name.lastIndexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR));
      }

      // By using the same separator the id will be stripped if it isn't
      // needed when variable renaming is turned off.
      return name + ContextualRenamer.UNIQUE_ID_SEPARATOR
          + idPrefix + uniqueIdSupplier.get();
    }

    @Override
    public String getReplacementName(String oldName) {
      return declarations.get(oldName);
    }

    @Override
    public Renamer forChildScope() {
      return new InlineRenamer(uniqueIdSupplier, idPrefix, removeConstness);
    }

    @Override
    public boolean stripConstIfReplaced() {
      return removeConstness;
    }
  }

  /**
   * For injecting boilerplate libraries. Leaves global names alone
   * and renames local names like InlineRenamer.
   */
  static class BoilerplateRenamer extends ContextualRenamer {
    private final Supplier<String> uniqueIdSupplier;
    private final String idPrefix;

    BoilerplateRenamer(
        Supplier<String> uniqueIdSupplier,
        String idPrefix) {
      this.uniqueIdSupplier = uniqueIdSupplier;
      this.idPrefix = idPrefix;
    }

    @Override
    public Renamer forChildScope() {
      return new InlineRenamer(uniqueIdSupplier, idPrefix, false);
    }
  }

}

```
