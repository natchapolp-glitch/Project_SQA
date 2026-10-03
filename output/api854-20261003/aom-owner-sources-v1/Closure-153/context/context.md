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
  <property name="stylesheetfile" value="${javadoc.dir}/dev_javadoc.css" />
  <property name="lib.dir" value="${basedir}/lib" />
  <property name="tools.dir" value="${basedir}/tools" />
  <property name="jarfile" value="${build.dir}/${ant.project.name}.jar" />
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

  <!-- Generate pom.xml with the proper svn build number -->
  <target name="pom" depends="svnversion">
    <copy file="closure-compiler.pom" tofile="${build.dir}/pom.xml">
      <filterset>
        <filter token="build.svnVersion"
             value="${build.svnVersion}"/>
      </filterset>
    </copy>

    <property name="jarfile-nodeps" value="${build.dir}/closure-${ant.project.name}-r${build.svnVersion}.jar" />
    <property name="jarfile-javadoc" value="${build.dir}/closure-${ant.project.name}-r${build.svnVersion}-javadoc.jar" />
    <property name="jarfile-sources" value="${build.dir}/closure-${ant.project.name}-r${build.svnVersion}-sources.jar" />

    <artifact:pom id="project" file="${build.dir}/pom.xml" />
  </target>

  <target name="mvn-install" 
          depends="jar-nodeps,jar-javadoc,jar-sources,pom" 
          description="Install closure-compiler artifacts into the local maven repo">
    <artifact:install file="${jarfile-nodeps}">
      <pom refid="project"/>
      <attach file="${jarfile-javadoc}" classifier="javadoc"/>
      <attach file="${jarfile-sources}" classifier="sources"/>
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
      <arg value="-Dfile=${jarfile-nodeps}" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the sources artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${jarfile-sources}" />
      <arg value="-Dclassifier=sources" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the javadoc artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${jarfile-javadoc}" />
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

  <path id="classpath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="*.jar"/>
    </fileset>
  </path>

  <target name="clean" description="delete generated files">
    <delete dir="${build.dir}" />
  </target>

  <target name="compile" description="compile the source code" depends="svnversion">
    <mkdir dir="${classes.dir}" />
    <javac debug="true" srcdir="${gen.dir}"
           destdir="${classes.dir}"
           excludes=".svn"
           >
      <classpath refid="classpath.path" />
    </javac>
    <javac debug="true" srcdir="${src.dir}"
           destdir="${classes.dir}"
           excludes=".svn,/jsonml/"
           >
      <classpath refid="classpath.path" />
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
    <jar destfile="${jarfile-nodeps}" update="true">
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
      <zipfileset src="${lib.dir}/libtrunk_rhino_parser_jarjared.jar" />
    </jar>
  </target>

  <target name="jar"
          depends="compile"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
    <jar destfile="${jarfile}" update="true">
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
      <zipgroupfileset dir="${lib.dir}"/>

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
      <fileset dir="${classes.dir}" />
      <fileset dir="${testClasses.dir}" />
      <zipgroupfileset dir="${lib.dir}"/>
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
      <classpath refid="classpath.path" />
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

  <target name="jar-javadoc" depends="javadoc">
    <jar jarfile="${jarfile-javadoc}">
      <fileset dir="${javadoc.dir}" />
    </jar>
  </target>

  <target name="jar-sources" depends="javadoc">
    <jar jarfile="${jarfile-sources}">
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
           description="package the compiler and JsonML clsses"
           depends="jsonml-compile, compile">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
    <jar destfile="${jsonml-jarfile}" update="true">
      <zipgroupfileset dir="${lib.dir}"/>
      <fileset dir="${classes.dir}" />
      <fileset dir="${jsonml-classes.dir}" />
    </jar>
   </target>
</project>

```

## src/com/google/javascript/jscomp/Normalize.java

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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique.BoilerplateRenamer;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Map;
import java.util.Set;

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
 * 1) Simplifies the AST by splitting var statements, moving initializiers
 *    out of for loops, and converting whiles to fors.
 * 2) Moves hoisted functions to the top of function scopes.
 * 3) Rewrites unhoisted named function declarations to be var declarations.
 * 4) Makes all variable names globally unique (extern or otherwise) so that
 *    no value is ever shadowed (note: "arguments" may require special
 *    handling).
 * 5) Removes duplicate variable declarations.
 * 6) Marks constants with the IS_CONSTANT_NAME annotation.
 *
 * @author johnlenz@google.com (johnlenz)
 */
// public for ReplaceDebugStringsTest
class Normalize implements CompilerPass {

  private final AbstractCompiler compiler;
  private final boolean assertOnChange;
  private static final boolean CONVERT_WHILE_TO_FOR = true;
  static final boolean MAKE_LOCAL_NAMES_UNIQUE = true;

  public static final DiagnosticType CATCH_BLOCK_VAR_ERROR =
    DiagnosticType.error(
        "JSC_CATCH_BLOCK_VAR_ERROR",
        "The use of scope variable {0} is not allowed within a catch block " +
        "with a catch exception of the same name.");


  Normalize(AbstractCompiler compiler, boolean assertOnChange) {
    this.compiler = compiler;
    this.assertOnChange = assertOnChange;

    // TODO(nicksantos): assertOnChange should only be true if the tree
    // is normalized.
  }

  static Node parseAndNormalizeSyntheticCode(
      AbstractCompiler compiler, String code, String prefix) {
    Node js = compiler.parseSyntheticCode(code);
    NodeTraversal.traverse(compiler, js,
        new Normalize.NormalizeStatements(compiler, false));
    NodeTraversal.traverse(
        compiler, js,
        new MakeDeclaredNamesUnique(
            new BoilerplateRenamer(
                compiler.getUniqueNameIdSupplier(),
                prefix)));
    return js;
  }

  static Node parseAndNormalizeTestCode(
      AbstractCompiler compiler, String code, String prefix) {
    Node js = compiler.parseTestCode(code);
    NodeTraversal.traverse(compiler, js,
        new Normalize.NormalizeStatements(compiler, false));
    NodeTraversal.traverse(
        compiler, js,
        new MakeDeclaredNamesUnique());
    return js;
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
    NodeTraversal.traverse(compiler, root,
        new NormalizeStatements(compiler, assertOnChange));
    if (MAKE_LOCAL_NAMES_UNIQUE) {
      MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
      NodeTraversal t = new NodeTraversal(compiler, renamer);
      t.traverseRoots(externs, root);
    }
    // It is important that removeDuplicateDeclarations runs after
    // MakeDeclaredNamesUnique in order for catch block exception names to be
    // handled properly. Specifically, catch block exception names are
    // only valid within the catch block, but our currect Scope logic
    // has no concept of this and includes it in the containing function
    // (or global scope). MakeDeclaredNamesUnique makes the catch exception
    // names unique so that removeDuplicateDeclarations() will properly handle
    // cases where a function scope variable conflict with a exception name:
    //   function f() {
    //      try {throw 0;} catch(e) {e; /* catch scope 'e'*/}
    //      var e = 1; // f scope 'e'
    //   }
    // otherwise 'var e = 1' would be rewritten as 'e = 1'.
    // TODO(johnlenz): Introduce a seperate scope for catch nodes.
    removeDuplicateDeclarations(externs, root);
    new PropagateConstantAnnotationsOverVars(compiler, assertOnChange)
        .process(externs, root);

    compiler.setNormalized();
  }

  /**
   * Propagate constant annotations over the Var graph.
   */
  static class PropagateConstantAnnotationsOverVars
      extends AbstractPostOrderCallback
      implements CompilerPass {
    private final AbstractCompiler compiler;
    private final boolean assertOnChange;

    PropagateConstantAnnotationsOverVars(
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

        boolean shouldBeConstant =
            (info != null && info.isConstant()) ||
            NodeUtil.isConstantByConvention(
                compiler.getCodingConvention(), n, parent);
        boolean isMarkedConstant = n.getBooleanProp(Node.IS_CONSTANT_NAME);
        if (shouldBeConstant && !isMarkedConstant) {
          if (assertOnChange) {
            String name = n.getString();
            throw new IllegalStateException(
                "Unexpected const change.\n" +
                "  name: "+ name + "\n" +
                "  parent:" + n.getParent().toStringTree());
          }
          n.putBooleanProp(Node.IS_CONSTANT_NAME, true);
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

    private Map<String, Boolean> constantMap = Maps.newHashMap();

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
          CodingConvention convention = compiler.getCodingConvention();
          if (NodeUtil.isConstantName(n)
              || NodeUtil.isConstantByConvention(convention, n, parent)) {
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

  /**
   * Simplify the AST:
   *   - VAR declarations split, so they represent exactly one child
   *     declaration.
   *   - WHILEs are converted to FORs
   *   - FOR loop are initializers are moved out of the FOR structure
   *   - LABEL node of children other than LABEL, BLOCK, WHILE, FOR, or DO are
   *     moved into a block.
   *   - Add constant annotations based on coding convention.
   */
  static class NormalizeStatements implements Callback {
    private final AbstractCompiler compiler;
    private final boolean assertOnChange;

    NormalizeStatements(AbstractCompiler compiler, boolean assertOnChange) {
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
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      doStatementNormalizations(t, n, parent);

      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      switch (n.getType()) {
        case Token.WHILE:
          if (CONVERT_WHILE_TO_FOR) {
            Node expr = n.getFirstChild();
            n.setType(Token.FOR);
            Node empty = new Node(Token.EMPTY);
            empty.copyInformationFrom(n);
            n.addChildBefore(empty, expr);
            n.addChildAfter(empty.cloneNode(), expr);
            reportCodeChange("WHILE node");
          }
          break;

        case Token.FUNCTION:
          normalizeFunctionDeclaration(n);
          break;

        case Token.NAME:
        case Token.STRING:
          annotateConstantsByConvention(n, parent);
          break;
      }
    }

    /**
     * Mark names and properties that are constants by convention.
     */
    private void annotateConstantsByConvention(Node n, Node parent) {
      Preconditions.checkState(
          n.getType() == Token.NAME || n.getType() == Token.STRING);

      // There are only two cases where a string token
      // may be a variable reference: The right side of a GETPROP
      // or an OBJECTLIT key.
      boolean isObjLitKey = NodeUtil.isObjectLitKey(n, parent);
      boolean isProperty = isObjLitKey ||
          (parent.getType() == Token.GETPROP &&
           parent.getLastChild() == n);
      if (n.getType() == Token.NAME || isProperty) {
        boolean isMarkedConstant = n.getBooleanProp(Node.IS_CONSTANT_NAME);
        if (!isMarkedConstant &&
            NodeUtil.isConstantByConvention(
                compiler.getCodingConvention(), n, parent)) {
          if (assertOnChange) {
            String name = n.getString();
            throw new IllegalStateException(
                "Unexpected const change.\n" +
                "  name: "+ name + "\n" +
                "  parent:" + n.getParent().toStringTree());
          }
          n.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        }
      }
    }

    /**
     * Rewrite named unhoisted functions declarations to a known
     * consistent behavior so we don't to different logic paths for the same
     * code. From:
     *    function f() {}
     * to:
     *    var f = function () {};
     */
    private void normalizeFunctionDeclaration(Node n) {
      Preconditions.checkState(n.getType() == Token.FUNCTION);
      if (!NodeUtil.isFunctionExpression(n)
          && !NodeUtil.isHoistedFunctionDeclaration(n)) {
        rewriteFunctionDeclaration(n);
      }
    }

    /**
     * Rewrite the function declaration from:
     *   function x() {}
     *   FUNCTION
     *     NAME
     *     LP
     *     BLOCK
     * to:
     *   var x = function() {};
     *   VAR
     *     NAME
     *       FUNCTION
     *         NAME (w/ empty string)
     *         LP
     *         BLOCK
     */
    private void rewriteFunctionDeclaration(Node n) {
      // Prepare a spot for the function.
      Node oldNameNode = n.getFirstChild();
      Node fnNameNode = oldNameNode.cloneNode();
      Node var = new Node(Token.VAR, fnNameNode, n.getLineno(), n.getCharno());
      var.copyInformationFrom(n);

      // Prepare the function
      oldNameNode.setString("");

      // Move the function
      Node parent = n.getParent();
      parent.replaceChild(n, var);
      fnNameNode.addChildToFront(n);

      reportCodeChange("Function declaration");
    }

    /**
     * Do normalizations that introduce new siblings or parents.
     */
    private void doStatementNormalizations(
        NodeTraversal t, Node n, Node parent) {
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
          block.copyInformationFrom(last);
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
            if (NodeUtil.isForIn(c)) {
              Node first = c.getFirstChild();
              if (first.getType() == Token.VAR) {
                // Transform:
                //    for (var a in b) {}
                // to:
                //    var a; for (a in b) {};
                Node newStatement = first.cloneTree();
                Node name = first.removeFirstChild();
                first.getParent().replaceChild(first, name);
                insertBeforeParent.addChildBefore(newStatement, insertBefore);
                reportCodeChange("FOR-IN var declaration");
              }
            } else if (c.getFirstChild().getType() != Token.EMPTY) {
              Node init = c.getFirstChild();
              Node empty = new Node(Token.EMPTY);
              empty.copyInformationFrom(c);
              c.replaceChild(init, empty);

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
            Node newVar = new Node(
                Token.VAR, name, n.getLineno(), n.getCharno());
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
          reportCodeChange("Move function declaration not at top of function");
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
  }

  /**
   * Remove duplicate VAR declarations.
   */
  private void removeDuplicateDeclarations(Node externs, Node root) {
    Callback tickler = new ScopeTicklingCallback();
    ScopeCreator scopeCreator =  new SyntacticScopeCreator(
        compiler, new DuplicateDeclarationHandler());
    NodeTraversal t = new NodeTraversal(compiler, tickler, scopeCreator);
    t.traverseRoots(externs, root);
  }

  /**
   * ScopeCreator duplicate declaration handler.
   */
  private final class DuplicateDeclarationHandler implements
      SyntacticScopeCreator.RedeclarationHandler {

    private Set<Var> hasOkDuplicateDeclaration = Sets.newHashSet();

    /**
     * Remove duplicate VAR declarations encountered discovered during
     * scope creation.
     */
    @Override
    public void onRedeclaration(
        Scope s, String name, Node n, CompilerInput input) {
      Preconditions.checkState(n.getType() == Token.NAME);
      Node parent = n.getParent();
      Var v = s.getVar(name);

      if (v != null && s.isGlobal()) {
        // We allow variables to be duplicate declared if one
        // declaration appears in source and the other in externs.
        // This deals with issues where a browser built-in is declared
        // in one browser but not in another.
        if (v.isExtern() && !input.isExtern()) {
          if (hasOkDuplicateDeclaration.add(v)) {
            return;
          }
        }
      }

      // If name is "arguments", Var maybe null.
      if (v != null && v.getParentNode().getType() == Token.CATCH) {
        // Redeclaration of a catch expression variable is hard to model
        // without support for "with" expressions.
        // The EcmaScript spec (section 12.14), declares that a catch
        // "catch (e) {}" is handled like "with ({'e': e}) {}" so that
        // "var e" would refer to the scope variable, but any following
        // reference would still refer to "e" of the catch expression.
        // Until we have support for this disallow it.
        // Currently the Scope object adds the catch expression to the
        // function scope, which is technically not true but a good
        // approximation for most uses.

        // TODO(johnlenz): Consider improving how scope handles catch
        // expression.

        // Use the name of the var before it was made unique.
        name = MakeDeclaredNamesUnique.ContextualRenameInverter.getOrginalName(
            name);
        compiler.report(
            JSError.make(
                input.getName(), n,
                CATCH_BLOCK_VAR_ERROR, name));
      } else if (v != null && parent.getType() == Token.FUNCTION) {
        if (v.getParentNode().getType() == Token.VAR) {
          s.undeclare(v);
          s.declare(name, n, n.getJSType(), v.input);
          replaceVarWithAssignment(v.getNameNode(), v.getParentNode(),
              v.getParentNode().getParent());
        }
      } else if (parent.getType() == Token.VAR) {
        Preconditions.checkState(parent.hasOneChild());

        replaceVarWithAssignment(n, parent, parent.getParent());
      }
    }

    /**
     * Remove the parent VAR. There are three cases that need to be handled:
     *   1) "var a = b;" which is replaced with "a = b"
     *   2) "label:var a;" which is replaced with "label:;". Ideally, the
     *      label itself would be removed but that is not possible in the
     *      context in which "onRedeclaration" is called.
     *   3) "for (var a in b) ..." which is replaced with "for (a in b)..."
     *      Cases we don't need to handle are VARs with multiple children,
     *      which have already been split into separate declarations, so there
     *      is no need to handle that here, and "for (var a;;);", which has
     *      been moved out of the loop.
     *      The result of this is that in each case the parent node is replaced
     *      which is generally dangerous in a traversal but is fine here with
     *      the scope creator, as the next node of interest is the parent's
     *      next sibling.
     */
    private void replaceVarWithAssignment(Node n, Node parent, Node gramps) {
      if (n.hasChildren()) {
        // The  *  is being initialize, preserve the new value.
        parent.removeChild(n);
        // Convert "var name = value" to "name = value"
        Node value = n.getFirstChild();
        n.removeChild(value);
        Node replacement = new Node(Token.ASSIGN, n, value);
        replacement.copyInformationFrom(parent);
        gramps.replaceChild(parent, NodeUtil.newExpr(replacement));
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
          // We should never get here. LABELs with a single VAR statement should
          // already have been normalized to have a BLOCK.
          throw new IllegalStateException("Unexpected LABEL");
        }
      }
      reportCodeChange("Duplicate VAR declaration");
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

## src/com/google/javascript/jscomp/SyntacticScopeCreator.java

```
/*
 * Copyright 2006 The Closure Compiler Authors.
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
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;


/**
 * <p>The syntactic scope creator scans the parse tree to create a Scope object
 * containing all the variable declarations in that scope.</p>
 *
 * <p>This implementation is not thread-safe.</p>
 *
 */
class SyntacticScopeCreator implements ScopeCreator {
  private final AbstractCompiler compiler;
  private Scope scope;
  private String sourceName;
  private final RedeclarationHandler redeclarationHandler;

  // The arguments variable is special, in that it's declared in every local
  // scope, but not explicitly declared.
  private static final String ARGUMENTS = "arguments";

  public static final DiagnosticType VAR_MULTIPLY_DECLARED_ERROR =
      DiagnosticType.error(
          "JSC_VAR_MULTIPLY_DECLARED_ERROR",
          "Variable {0} first declared in {1}");

  public static final DiagnosticType VAR_ARGUMENTS_SHADOWED_ERROR =
    DiagnosticType.error(
        "JSC_VAR_ARGUMENTS_SHADOWED_ERROR",
        "Shadowing \"arguments\" is not allowed");

  /**
   * Creates a ScopeCreator.
   */
  SyntacticScopeCreator(AbstractCompiler compiler) {
    this.compiler = compiler;
    this.redeclarationHandler = new DefaultRedeclarationHandler();
  }

  SyntacticScopeCreator(
      AbstractCompiler compiler, RedeclarationHandler redeclarationHandler) {
    this.compiler = compiler;
    this.redeclarationHandler = redeclarationHandler;
  }

  public Scope createScope(Node n, Scope parent) {
    sourceName = null;
    if (parent == null) {
      scope = new Scope(n, compiler);
    } else {
      scope = new Scope(parent, n);
    }

    scanRoot(n, parent);

    sourceName = null;
    Scope returnedScope = scope;
    scope = null;
    return returnedScope;
  }

  private void scanRoot(Node n, Scope parent) {
    if (n.getType() == Token.FUNCTION) {
      sourceName = (String) n.getProp(Node.SOURCENAME_PROP);

      final Node fnNameNode = n.getFirstChild();
      final Node args = fnNameNode.getNext();
      final Node body = args.getNext();

      // Bleed the function name into the scope, if it hasn't
      // been declared in the outer scope.
      String fnName = fnNameNode.getString();
      if (!fnName.isEmpty() && NodeUtil.isFunctionExpression(n)) {
        declareVar(fnNameNode);
      }

      // Args: Declare function variables
      Preconditions.checkState(args.getType() == Token.LP);
      for (Node a = args.getFirstChild(); a != null;
           a = a.getNext()) {
        Preconditions.checkState(a.getType() == Token.NAME);
        declareVar(a);
      }

      // Body
      scanVars(body, n);
    } else {
      // It's the global block
      Preconditions.checkState(scope.getParent() == null);
      scanVars(n, null);
    }
  }

  /**
   * Scans and gather variables declarations under a Node
   */
  private void scanVars(Node n, Node parent) {
    switch (n.getType()) {
      case Token.VAR:
        // Declare all variables. e.g. var x = 1, y, z;
        for (Node child = n.getFirstChild();
             child != null;) {
          Node next = child.getNext();
          declareVar(child);
          child = next;
        }
        return;

      case Token.FUNCTION:
        if (NodeUtil.isFunctionExpression(n)) {
          return;
        }

        String fnName = n.getFirstChild().getString();
        if (fnName.isEmpty()) {
          // This is invalid, but allow it so the checks can catch it.
          return;
        }
        declareVar(n.getFirstChild());
        return;   // should not examine function's children

      case Token.CATCH:
        Preconditions.checkState(n.getChildCount() == 2);
        Preconditions.checkState(n.getFirstChild().getType() == Token.NAME);
        // the first child is the catch var and the third child
        // is the code block

        final Node var = n.getFirstChild();
        final Node block = var.getNext();

        declareVar(var);
        scanVars(block, n);
        return;  // only one child to scan

      case Token.SCRIPT:
        sourceName = (String) n.getProp(Node.SOURCENAME_PROP);
        break;
    }

    // Variables can only occur in statement-level nodes, so
    // we only need to traverse children in a couple special cases.
    if (NodeUtil.isControlStructure(n) || NodeUtil.isStatementBlock(n)) {
      for (Node child = n.getFirstChild();
           child != null;) {
        Node next = child.getNext();
        scanVars(child, n);
        child = next;
      }
    }
  }

  /**
   * Interface for injectable duplicate handling.
   */
  interface RedeclarationHandler {
    void onRedeclaration(
        Scope s, String name, Node n, CompilerInput input);
  }

  /**
   * The default handler for duplicate declarations.
   */
  private class DefaultRedeclarationHandler implements RedeclarationHandler {
    public void onRedeclaration(
        Scope s, String name, Node n, CompilerInput input) {
      Node parent = n.getParent();

      // Don't allow multiple variables to be declared at the top level scope
      if (scope.isGlobal()) {
        Scope.Var origVar = scope.getVar(name);
        Node origParent = origVar.getParentNode();
        if (origParent.getType() == Token.CATCH &&
            parent.getType() == Token.CATCH) {
          // Okay, both are 'catch(x)' variables.
          return;
        }

        boolean allowDupe = false;
        JSDocInfo info = n.getJSDocInfo();
        if (info == null) {
          info = parent.getJSDocInfo();
        }
        allowDupe =
            info != null && info.getSuppressions().contains("duplicate");

        if (!allowDupe) {
          compiler.report(
              JSError.make(sourceName, n,
                           VAR_MULTIPLY_DECLARED_ERROR,
                           name,
                           (origVar.input != null
                            ? origVar.input.getName()
                            : "??")));
        }
      } else if (name.equals(ARGUMENTS) && !NodeUtil.isVarDeclaration(n)) {
        // Disallow shadowing "arguments" as we can't handle with our current
        // scope modeling.
        compiler.report(
            JSError.make(sourceName, n,
                VAR_ARGUMENTS_SHADOWED_ERROR));
      }
    }
  }

  /**
   * Declares a variable.
   *
   * @param n The node corresponding to the variable name.
   * @param declaredType The variable's type, according to JSDoc
   */
  private void declareVar(Node n) {
    Preconditions.checkState(n.getType() == Token.NAME);

    CompilerInput input = compiler.getInput(sourceName);
    String name = n.getString();
    if (scope.isDeclared(name, false)
        || (scope.isLocal() && name.equals(ARGUMENTS))) {
      redeclarationHandler.onRedeclaration(
          scope, name, n, input);
    } else {
      scope.declare(name, n, null, input);
    }
  }
}

```
