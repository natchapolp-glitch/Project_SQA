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

  <path id="srcclasspath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="args4j.jar"/>
      <include name="guava.jar"/>
      <include name="json.jar"/>
      <include name="jsr305.jar"/>
      <include name="libtrunk_rhino_parser_jarjared.jar"/>
      <include name="protobuf-java.jar"/>
    </fileset>
  </path>

  <path id="allclasspath.path">
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
      <zipfileset src="${lib.dir}/args4j.jar"/>
      <zipfileset src="${lib.dir}/guava.jar"/>
      <zipfileset src="${lib.dir}/jsr305.jar"/>
      <zipfileset src="${lib.dir}/libtrunk_rhino_parser_jarjared.jar"/>
      <zipfileset src="${lib.dir}/protobuf-java.jar"/>

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
    <jar destfile="${jarfile}" update="true">
      <fileset dir="${testClasses.dir}" />
      <zipgroupfileset dir="${lib.dir}"/>
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
           description="package the compiler and JsonML classes"
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

## src/com/google/javascript/jscomp/PeepholeFoldConstants.java

```
/*
 * Copyright 2004 The Closure Compiler Authors.
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
import com.google.javascript.jscomp.mozilla.rhino.ScriptRuntime;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

import java.util.List;
import java.util.Locale;

/**
 * Peephole optimization to fold constants (e.g. x + 1 + 7 --> x + 8).
 *
 */
class PeepholeFoldConstants extends AbstractPeepholeOptimization {

  static final DiagnosticType INVALID_GETELEM_INDEX_ERROR =
      DiagnosticType.error(
          "JSC_INVALID_GETELEM_INDEX_ERROR",
          "Array index not integer: {0}");

  static final DiagnosticType INDEX_OUT_OF_BOUNDS_ERROR =
      DiagnosticType.error(
          "JSC_INDEX_OUT_OF_BOUNDS_ERROR",
          "Array index out of bounds: {0}");

  static final DiagnosticType NEGATING_A_NON_NUMBER_ERROR =
      DiagnosticType.error(
          "JSC_NEGATING_A_NON_NUMBER_ERROR",
          "Can't negate non-numeric value: {0}");

  static final DiagnosticType BITWISE_OPERAND_OUT_OF_RANGE =
      DiagnosticType.error(
          "JSC_BITWISE_OPERAND_OUT_OF_RANGE",
          "Operand out of range, bitwise operation will lose information: {0}");

  static final DiagnosticType SHIFT_AMOUNT_OUT_OF_BOUNDS = DiagnosticType.error(
      "JSC_SHIFT_AMOUNT_OUT_OF_BOUNDS",
      "Shift amount out of bounds: {0}");

  static final DiagnosticType FRACTIONAL_BITWISE_OPERAND = DiagnosticType.error(
      "JSC_FRACTIONAL_BITWISE_OPERAND",
      "Fractional bitwise operand: {0}");

  private static final double MAX_FOLD_NUMBER = Math.pow(2, 53);

  // The LOCALE independent "locale"
  private static final Locale ROOT_LOCALE = new Locale("");

  @Override
  Node optimizeSubtree(Node subtree) {
    switch(subtree.getType()) {
      case Token.CALL:
        return tryFoldKnownMethods(subtree);

      case Token.NEW:
        return tryFoldCtorCall(subtree);

      case Token.TYPEOF:
        return tryFoldTypeof(subtree);

      case Token.NOT:
      case Token.POS:
      case Token.NEG:
      case Token.BITNOT:
        tryReduceOperandsForOp(subtree);
        return tryFoldUnaryOperator(subtree);

      case Token.VOID:
        return tryReduceVoid(subtree);

      default:
        tryReduceOperandsForOp(subtree);
        return tryFoldBinaryOperator(subtree);
    }
  }

  private Node tryFoldBinaryOperator(Node subtree) {
    Node left = subtree.getFirstChild();

    if (left == null) {
      return subtree;
    }

    Node right = left.getNext();

    if (right == null) {
      return subtree;
    }

    // If we've reached here, node is truly a binary operator.
    switch(subtree.getType()) {
      case Token.GETPROP:
        return tryFoldGetProp(subtree, left, right);

      case Token.GETELEM:
        return tryFoldGetElem(subtree, left, right);

      case Token.INSTANCEOF:
        return tryFoldInstanceof(subtree, left, right);

      case Token.AND:
      case Token.OR:
        return tryFoldAndOr(subtree, left, right);

      case Token.LSH:
      case Token.RSH:
      case Token.URSH:
        return tryFoldShift(subtree, left, right);

      case Token.ASSIGN:
        return tryFoldAssign(subtree, left, right);

      case Token.ADD:
        return tryFoldAdd(subtree, left, right);

      case Token.SUB:
      case Token.DIV:
      case Token.MOD:
        return tryFoldArithmeticOp(subtree, left, right);

      case Token.MUL:
      case Token.BITAND:
      case Token.BITOR:
      case Token.BITXOR:
        Node result = tryFoldArithmeticOp(subtree, left, right);
        if (result != subtree) {
          return result;
        }
        return tryFoldLeftChildOp(subtree, left, right);

      case Token.LT:
      case Token.GT:
      case Token.LE:
      case Token.GE:
      case Token.EQ:
      case Token.NE:
      case Token.SHEQ:
      case Token.SHNE:
        return tryFoldComparison(subtree, left, right);

      default:
        return subtree;
    }
  }

  private Node tryReduceVoid(Node n) {
    Node child = n.getFirstChild();
    if (child.getType() != Token.NUMBER || child.getDouble() != 0.0) {
      if (!mayHaveSideEffects(n)) {
        n.replaceChild(child, Node.newNumber(0));
        reportCodeChange();
      }
    }
    return n;
  }

  private void tryReduceOperandsForOp(Node n) {
    switch (n.getType()) {
      case Token.ADD:
        Node left = n.getFirstChild();
        Node right = n.getLastChild();
        if (!NodeUtil.mayBeString(left) && !NodeUtil.mayBeString(right)) {
          tryConvertOperandsToNumber(n);
        }
        break;
      case Token.ASSIGN_BITOR:
      case Token.ASSIGN_BITXOR:
      case Token.ASSIGN_BITAND:
        // TODO(johnlenz): convert these to integers.
      case Token.ASSIGN_LSH:
      case Token.ASSIGN_RSH:
      case Token.ASSIGN_URSH:
      case Token.ASSIGN_SUB:
      case Token.ASSIGN_MUL:
      case Token.ASSIGN_MOD:
      case Token.ASSIGN_DIV:
        tryConvertToNumber(n.getLastChild());
        break;
      case Token.BITNOT:
      case Token.BITOR:
      case Token.BITXOR:
      case Token.BITAND:
      case Token.LSH:
      case Token.RSH:
      case Token.URSH:
      case Token.SUB:
      case Token.MUL:
      case Token.MOD:
      case Token.DIV:
      case Token.POS:
      case Token.NEG:
        tryConvertOperandsToNumber(n);
        break;
    }
  }

  private void tryConvertOperandsToNumber(Node n) {
    Node next;
    for (Node c = n.getFirstChild(); c != null; c = next) {
      next = c.getNext();
      tryConvertToNumber(c);
    }
  }

  private void tryConvertToNumber(Node n) {
    switch (n.getType()) {
      case Token.NUMBER:
        // Nothing to do
        return;
      case Token.AND:
      case Token.OR:
      case Token.COMMA:
        tryConvertToNumber(n.getLastChild());
        return;
      case Token.HOOK:
        tryConvertToNumber(n.getChildAtIndex(1));
        tryConvertToNumber(n.getLastChild());
        return;
      case Token.NAME:
        if (!NodeUtil.isUndefined(n)) {
          return;
        }
        break;
    }

    Double result = NodeUtil.getNumberValue(n);
    if (result == null) {
      return;
    }

    double value = result;

    Node replacement;
    if (Double.isNaN(value)) {
      replacement = Node.newString(Token.NAME, "NaN");
    } else if (value == Double.POSITIVE_INFINITY) {
      replacement = Node.newString(Token.NAME, "Infinity");
    } else if (value == Double.NEGATIVE_INFINITY) {
      replacement = new Node(Token.NEG, Node.newString(Token.NAME, "Infinity"));
      replacement.copyInformationFromForTree(n);
    } else {
      replacement = Node.newNumber(value);
    }

    n.getParent().replaceChild(n, replacement);
    reportCodeChange();
  }

  /**
   * Folds 'typeof(foo)' if foo is a literal, e.g.
   * typeof("bar") --> "string"
   * typeof(6) --> "number"
   */
  private Node tryFoldTypeof(Node originalTypeofNode) {
    Preconditions.checkArgument(originalTypeofNode.getType() == Token.TYPEOF);

    Node argumentNode = originalTypeofNode.getFirstChild();
    if (argumentNode == null || !NodeUtil.isLiteralValue(argumentNode, true)) {
      return originalTypeofNode;
    }

    String typeNameString = null;

    switch (argumentNode.getType()) {
      case Token.FUNCTION:
        typeNameString = "function";
        break;
      case Token.STRING:
        typeNameString = "string";
        break;
      case Token.NUMBER:
        typeNameString = "number";
        break;
      case Token.TRUE:
      case Token.FALSE:
        typeNameString = "boolean";
        break;
      case Token.NULL:
      case Token.OBJECTLIT:
      case Token.ARRAYLIT:
        typeNameString = "object";
        break;
      case Token.VOID:
        typeNameString = "undefined";
        break;
      case Token.NAME:
        // We assume here that programs don't change the value of the
        // keyword undefined to something other than the value undefined.
        if ("undefined".equals(argumentNode.getString())) {
          typeNameString = "undefined";
        }
        break;
    }

    if (typeNameString != null) {
      Node newNode = Node.newString(typeNameString);
      originalTypeofNode.getParent().replaceChild(originalTypeofNode, newNode);
      reportCodeChange();

      return newNode;
    }

    return originalTypeofNode;
  }

  private Node tryFoldUnaryOperator(Node n) {
    Preconditions.checkState(n.hasOneChild());

    Node left = n.getFirstChild();
    Node parent = n.getParent();

    if (left == null) {
      return n;
    }

    TernaryValue leftVal = NodeUtil.getPureBooleanValue(left);
    if (leftVal == TernaryValue.UNKNOWN) {
      return n;
    }

    switch (n.getType()) {
      case Token.NOT:
        // Don't fold !0 and !1 back to false.
        if (left.getType() == Token.NUMBER) {
          double numValue = left.getDouble();
          if (numValue == 0 || numValue == 1) {
            return n;
          }
        }
        int result = leftVal.toBoolean(true) ? Token.FALSE : Token.TRUE;
        Node replacementNode = new Node(result);
        parent.replaceChild(n, replacementNode);
        reportCodeChange();
        return replacementNode;
      case Token.POS:
        if (NodeUtil.isNumericResult(left)) {
          // POS does nothing to numeric values.
          parent.replaceChild(n, left.detachFromParent());
          reportCodeChange();
          return left;
        }
        return n;
      case Token.NEG:
        try {
          if (left.getType() == Token.NAME) {
            if (left.getString().equals("Infinity")) {
              // "-Infinity" is valid and a literal, don't modify it.
              return n;
            } else if (left.getString().equals("NaN")) {
              // "-NaN" is "NaN".
              n.removeChild(left);
              parent.replaceChild(n, left);
              reportCodeChange();
              return left;
            }
          }

          double negNum = -left.getDouble();

          Node negNumNode = Node.newNumber(negNum);
          parent.replaceChild(n, negNumNode);
          reportCodeChange();
          return negNumNode;
        } catch (UnsupportedOperationException ex) {
          // left is not a number node, so do not replace, but warn the
          // user because they can't be doing anything good
          error(NEGATING_A_NON_NUMBER_ERROR, left);
          return n;
        }
      case Token.BITNOT:
        try {
          double val = left.getDouble();
          if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
            int intVal = (int) val;
            if (intVal == val) {
              Node notIntValNode = Node.newNumber(~intVal);
              parent.replaceChild(n, notIntValNode);
              reportCodeChange();
              return notIntValNode;
            } else {
              error(FRACTIONAL_BITWISE_OPERAND, left);
              return n;
            }
          } else {
            error(BITWISE_OPERAND_OUT_OF_RANGE, left);
            return n;
          }
        } catch (UnsupportedOperationException ex) {
          // left is not a number node, so do not replace, but warn the
          // user because they can't be doing anything good
          error(NEGATING_A_NON_NUMBER_ERROR, left);
          return n;
        }
        default:
          return n;
    }
  }

  /**
   * Try to fold {@code left instanceof right} into {@code true}
   * or {@code false}.
   */
  private Node tryFoldInstanceof(Node n, Node left, Node right) {
    Preconditions.checkArgument(n.getType() == Token.INSTANCEOF);

    // TODO(johnlenz) Use type information if available to fold
    // instanceof.
    if (NodeUtil.isLiteralValue(left, true)
        && !mayHaveSideEffects(right)) {

      Node replacementNode = null;

      if (NodeUtil.isImmutableValue(left)) {
        // Non-object types are never instances.
        replacementNode = new Node(Token.FALSE);
      } else if (right.getType() == Token.NAME
          && "Object".equals(right.getString())) {
        replacementNode = new Node(Token.TRUE);
      }

      if (replacementNode != null) {
        n.getParent().replaceChild(n, replacementNode);
        reportCodeChange();
        return replacementNode;
      }
    }

    return n;
  }

  private Node tryFoldAssign(Node n, Node left, Node right) {
    Preconditions.checkArgument(n.getType() == Token.ASSIGN);

    // Tries to convert x = x + y -> x += y;
    if (!right.hasChildren() ||
        right.getFirstChild().getNext() != right.getLastChild()) {
      // RHS must have two children.
      return n;
    }

    if (mayHaveSideEffects(left)) {
      return n;
    }

    Node newRight;
    if (areNodesEqualForInlining(left, right.getFirstChild())) {
      newRight = right.getLastChild();
    } else if (NodeUtil.isCommutative(right.getType()) &&
          areNodesEqualForInlining(left, right.getLastChild())) {
      newRight = right.getFirstChild();
    } else {
      return n;
    }

    int newType = -1;
    switch (right.getType()) {
      case Token.ADD:
        newType = Token.ASSIGN_ADD;
        break;
      case Token.BITAND:
        newType = Token.ASSIGN_BITAND;
        break;
      case Token.BITOR:
        newType = Token.ASSIGN_BITOR;
        break;
      case Token.BITXOR:
        newType = Token.ASSIGN_BITXOR;
        break;
      case Token.DIV:
        newType = Token.ASSIGN_DIV;
        break;
      case Token.LSH:
        newType = Token.ASSIGN_LSH;
        break;
      case Token.MOD:
        newType = Token.ASSIGN_MOD;
        break;
      case Token.MUL:
        newType = Token.ASSIGN_MUL;
        break;
      case Token.RSH:
        newType = Token.ASSIGN_RSH;
        break;
      case Token.SUB:
        newType = Token.ASSIGN_SUB;
        break;
      case Token.URSH:
        newType = Token.ASSIGN_URSH;
        break;
      default:
        return n;
    }

    Node newNode = new Node(newType,
        left.detachFromParent(), newRight.detachFromParent());
    n.getParent().replaceChild(n, newNode);

    reportCodeChange();

    return newNode;
  }

  /**
   * Try to fold a AND/OR node.
   */
  private Node tryFoldAndOr(Node n, Node left, Node right) {
    Node parent = n.getParent();

    Node result = null;

    int type = n.getType();

    TernaryValue leftVal = NodeUtil.getImpureBooleanValue(left);

    if (leftVal != TernaryValue.UNKNOWN) {
      boolean lval = leftVal.toBoolean(true);

      // (TRUE || x) => TRUE (also, (3 || x) => 3)
      // (FALSE && x) => FALSE
      if (lval && type == Token.OR ||
          !lval && type == Token.AND) {
        result = left;

      } else if (!mayHaveSideEffects(left)) {
        // (FALSE || x) => x
        // (TRUE && x) => x
        result = right;
      }
    }

    // Note: Right hand side folding is handled by
    // PeepholeSubstituteAlternateSyntax#tryMinimizeCondition

    if (result != null) {
      // Fold it!
      n.removeChild(result);
      parent.replaceChild(n, result);
      reportCodeChange();

      return result;
    } else {
      return n;
    }
  }

  /**
   * Expressions such as [foo() + 'a' + 'b'] generate parse trees
   * where no node has two const children ((foo() + 'a') + 'b'), so
   * tryFoldAdd() won't fold it -- tryFoldLeftChildAdd() will (for Strings).
   * Specifically it folds Add exprssions where:
   *  - The left child is also and add expression
   *  - The right child is a constant value
   *  - The left child's right child is a STRING constant.
   */
  private Node tryFoldChildAddString(Node n, Node left, Node right) {

    if (NodeUtil.isLiteralValue(right, false) &&
        left.getType() == Token.ADD) {

      Node ll = left.getFirstChild();
      Node lr = ll.getNext();

      // Left's right child MUST be a string. We would not want to fold
      // foo() + 2 + 'a' because we don't know what foo() will return, and
      // therefore we don't know if left is a string concat, or a numeric add.
      if (lr.getType() == Token.STRING) {
        String leftString = NodeUtil.getStringValue(lr);
        String rightString = NodeUtil.getStringValue(right);
        if (leftString != null && rightString != null) {
          left.removeChild(ll);
          String result = leftString + rightString;
          n.replaceChild(left, ll);
          n.replaceChild(right, Node.newString(result));
          reportCodeChange();
          return n;
        }
      }
    }

    if (NodeUtil.isLiteralValue(left, false) &&
        right.getType() == Token.ADD) {

      Node rl = right.getFirstChild();
      Node rr = right.getLastChild();

      // Left's right child MUST be a string. We would not want to fold
      // foo() + 2 + 'a' because we don't know what foo() will return, and
      // therefore we don't know if left is a string concat, or a numeric add.
      if (rl.getType() == Token.STRING) {
        String leftString = NodeUtil.getStringValue(left);
        String rightString = NodeUtil.getStringValue(rl);
        if (leftString != null && rightString != null) {
          right.removeChild(rr);
          String result = leftString + rightString;
          n.replaceChild(right, rr);
          n.replaceChild(left, Node.newString(result));
          reportCodeChange();
          return n;
        }
      }
    }

    return n;
  }

  /**
   * Try to fold an ADD node with constant operands
   */
  private Node tryFoldAddConstantString(Node n, Node left, Node right) {
    if (left.getType() == Token.STRING ||
        right.getType() == Token.STRING) {
      // Add strings.
      String leftString = NodeUtil.getStringValue(left);
      String rightString = NodeUtil.getStringValue(right);
      if (leftString != null && rightString != null) {
        Node newStringNode = Node.newString(leftString + rightString);
        n.getParent().replaceChild(n, newStringNode);
        reportCodeChange();
        return newStringNode;
      }
    }



    return n;
  }

  /**
   * Try to fold arithmetic binary operators
   */
  private Node tryFoldArithmeticOp(Node n, Node left, Node right) {
    Node result = performArithmeticOp(n.getType(), left, right);
    if (result != null) {
      result.copyInformationFromForTree(n);
      n.getParent().replaceChild(n, result);
      reportCodeChange();
      return result;
    }
    return n;
  }

  /**
   * Try to fold arithmetic binary operators
   */
  private Node performArithmeticOp(int opType, Node left, Node right) {
    // Unlike other operations, ADD operands are not always converted
    // to Number.
    if (opType == Token.ADD
        && (NodeUtil.mayBeString(left, false)
            || NodeUtil.mayBeString(right, false))) {
      return null;
    }

    double result;

    // TODO(johnlenz): Handle NaN with unknown value. BIT ops convert NaN
    // to zero so this is a little akward here.

    Double lValObj = NodeUtil.getNumberValue(left);
    if (lValObj == null) {
      return null;
    }
    Double rValObj = NodeUtil.getNumberValue(right);
    if (rValObj == null) {
      return null;
    }

    double lval = lValObj;
    double rval = rValObj;

    switch (opType) {
      case Token.BITAND:
        result = ScriptRuntime.toInt32(lval) & ScriptRuntime.toInt32(rval);
        break;
      case Token.BITOR:
        result = ScriptRuntime.toInt32(lval) | ScriptRuntime.toInt32(rval);
        break;
      case Token.BITXOR:
        result = ScriptRuntime.toInt32(lval) ^ ScriptRuntime.toInt32(rval);
        break;
      case Token.ADD:
        result = lval + rval;
        break;
      case Token.SUB:
        result = lval - rval;
        break;
      case Token.MUL:
        result = lval * rval;
        break;
      case Token.MOD:
        if (rval == 0) {
          return null;
        }
        result = lval % rval;
        break;
      case Token.DIV:
        if (rval == 0) {
          return null;
        }
        result = lval / rval;
        break;
      default:
        throw new Error("Unexpected arithmetic operator");
    }

    // TODO(johnlenz): consider removing the result length check.
    // length of the left and right value plus 1 byte for the operator.
    if (String.valueOf(result).length() <=
        String.valueOf(lval).length() + String.valueOf(rval).length() + 1 &&

        // Do not try to fold arithmetic for numbers > 2^53. After that
        // point, fixed-point math starts to break down and become inaccurate.
        Math.abs(result) <= MAX_FOLD_NUMBER) {
      Node newNumber = Node.newNumber(result);
      return newNumber;
    } else if (Double.isNaN(result)) {
      return Node.newString(Token.NAME, "NaN");
    } else if (result == Double.POSITIVE_INFINITY) {
      return Node.newString(Token.NAME, "Infinity");
    } else if (result == Double.NEGATIVE_INFINITY) {
      return new Node(Token.NEG, Node.newString(Token.NAME, "Infinity"));
    }

    return null;
  }

  /**
   * Expressions such as [foo() * 10 * 20] generate parse trees
   * where no node has two const children ((foo() * 10) * 20), so
   * performArithmeticOp() won't fold it -- tryFoldLeftChildOp() will.
   * Specifically it folds associative expressions where:
   *  - The left child is also an associative expression of the same time.
   *  - The right child is a constant NUMBER constant.
   *  - The left child's right child is a NUMBER constant.
   */
  private Node tryFoldLeftChildOp(Node n, Node left, Node right) {
    int opType = n.getType();
    Preconditions.checkState(
        (NodeUtil.isAssociative(opType) && NodeUtil.isCommutative(opType))
        || n.getType() == Token.ADD);

    Preconditions.checkState(
        n.getType() != Token.ADD || !NodeUtil.mayBeString(n));

    // Use getNumberValue to handle constants like "NaN" and "Infinity"
    // other values are converted to numbers elsewhere.
    Double rightValObj = NodeUtil.getNumberValue(right);
    if (rightValObj != null && left.getType() == opType) {
      Preconditions.checkState(left.getChildCount() == 2);

      Node ll = left.getFirstChild();
      Node lr = ll.getNext();

      Node valueToCombine = ll;
      Node replacement = performArithmeticOp(opType, valueToCombine, right);
      if (replacement == null) {
        valueToCombine = lr;
        replacement = performArithmeticOp(opType, valueToCombine, right);
      }
      if (replacement != null) {
        // Remove the child that has been combined
        left.removeChild(valueToCombine);
        // Replace the left op with the remaining child.
        n.replaceChild(left, left.removeFirstChild());
        // New "-Infinity" node need location info explicitly
        // added.
        replacement.copyInformationFromForTree(right);
        n.replaceChild(right, replacement);
        reportCodeChange();
      }
    }

    return n;
  }

  private Node tryFoldAdd(Node node, Node left, Node right) {
    Preconditions.checkArgument(node.getType() == Token.ADD);

    if (NodeUtil.mayBeString(node, true)) {
      if (NodeUtil.isLiteralValue(left, false) &&
          NodeUtil.isLiteralValue(right, false)) {
        // '6' + 7
        return tryFoldAddConstantString(node, left, right);
      } else {
        // a + 7 or 6 + a
        return tryFoldChildAddString(node, left, right);
      }
    } else {
      // Try arithmetic add
      Node result = tryFoldArithmeticOp(node, left, right);
      if (result != node) {
        return result;
      }
      return tryFoldLeftChildOp(node, left, right);
    }
  }

  /**
   * Try to fold shift operations
   */
  private Node tryFoldShift(Node n, Node left, Node right) {
    if (left.getType() == Token.NUMBER &&
        right.getType() == Token.NUMBER) {

      double result;
      double lval = left.getDouble();
      double rval = right.getDouble();

      // check ranges.  We do not do anything that would clip the double to
      // a 32-bit range, since the user likely does not intend that.
      if (!(lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE)) {
        error(BITWISE_OPERAND_OUT_OF_RANGE, left);
        return n;
      }

      // only the lower 5 bits are used when shifting, so don't do anything
      // if the shift amount is outside [0,32)
      if (!(rval >= 0 && rval < 32)) {
        error(SHIFT_AMOUNT_OUT_OF_BOUNDS, right);
        return n;
      }

      // Convert the numbers to ints
      int lvalInt = (int) lval;
      if (lvalInt != lval) {
        error(FRACTIONAL_BITWISE_OPERAND, left);
        return n;
      }

      int rvalInt = (int) rval;
      if (rvalInt != rval) {
        error(FRACTIONAL_BITWISE_OPERAND, right);
        return n;
      }

      switch (n.getType()) {
        case Token.LSH:
          result = lvalInt << rvalInt;
          break;
        case Token.RSH:
          result = lvalInt >> rvalInt;
          break;
        case Token.URSH:
          // JavaScript handles zero shifts on signed numbers differently than
          // Java as an Java int can not represent the unsigned 32-bit number
          // where JavaScript can so use a long here.
          long lvalLong = lvalInt & 0xffffffffL;
          result = lvalLong >>> rvalInt;
          break;
        default:
          throw new AssertionError("Unknown shift operator: " +
              Node.tokenToName(n.getType()));
      }

      Node newNumber = Node.newNumber(result);
      n.getParent().replaceChild(n, newNumber);
      reportCodeChange();

      return newNumber;
    }

    return n;
  }

  /**
   * Try to fold comparison nodes, e.g ==
   */
  @SuppressWarnings("fallthrough")
  private Node tryFoldComparison(Node n, Node left, Node right) {
    if (!NodeUtil.isLiteralValue(left, false) ||
        !NodeUtil.isLiteralValue(right, false)) {
      // We only handle non-literal operands for LT and GT.
      if (n.getType() != Token.GT && n.getType() != Token.LT) {
        return n;
      }
    }

    int op = n.getType();
    boolean result;

    // TODO(johnlenz): Use the JSType to compare nodes of different types.

    boolean rightLiteral = NodeUtil.isLiteralValue(right, false);
    boolean undefinedRight = ((Token.NAME == right.getType()
          && right.getString().equals("undefined"))
          || (Token.VOID == right.getType()
              && NodeUtil.isLiteralValue(right.getFirstChild(), false)));
    int lhType = getNormalizedNodeType(left);
    int rhType = getNormalizedNodeType(right);
    switch (lhType) {
      case Token.VOID:
        if (!NodeUtil.isLiteralValue(left.getFirstChild(), false)) {
          return n;
        } else if (!rightLiteral) {
          return n;
        } else {
          result = compareToUndefined(right, op);
        }
        break;

      case Token.NULL:
      case Token.TRUE:
      case Token.FALSE:
        if (undefinedRight) {
          result = compareToUndefined(left, op);
          break;
        }
        if (rhType != Token.TRUE &&
            rhType != Token.FALSE &&
            rhType != Token.NULL) {
          return n;
        }
        switch (op) {
          case Token.SHEQ:
          case Token.EQ:
            result = lhType == rhType;
            break;

          case Token.SHNE:
          case Token.NE:
            result = lhType != rhType;
            break;

          case Token.GE:
          case Token.LE:
          case Token.GT:
          case Token.LT:
            Boolean compareResult = compareAsNumbers(op, left, right);
            if (compareResult != null) {
              result = compareResult;
            } else {
              return n;
            }
            break;

          default:
            return n;  // we only handle == and != here
        }
        break;

      case Token.THIS:
        if (right.getType() != Token.THIS) {
          return n;
        }
        switch (op) {
          case Token.SHEQ:
          case Token.EQ:
            result = true;
            break;

          case Token.SHNE:
          case Token.NE:
            result = false;
            break;

          // We can only handle == and != here.
          // GT, LT, GE, LE depend on the type of "this" and how it will
          // be converted to number.  The results are different depending on
          // whether it is a string, NaN or other number value.
          default:
            return n;
        }
        break;

      case Token.STRING:
        if (undefinedRight) {
          result = compareToUndefined(left, op);
          break;
        }
        if (Token.STRING != right.getType()) {
          return n;  // Only eval if they are the same type
        }
        switch (op) {
          case Token.SHEQ:
          case Token.EQ:
            result = left.getString().equals(right.getString());
            break;

          case Token.SHNE:
          case Token.NE:
            result = !left.getString().equals(right.getString());
            break;

          default:
            return n;  // we only handle == and != here
        }
        break;

      case Token.NUMBER:
        if (undefinedRight) {
          result = compareToUndefined(left, op);
          break;
        }
        if (Token.NUMBER != right.getType()) {
          return n;  // Only eval if they are the same type
        }
        Boolean compareResult = compareAsNumbers(op, left, right);
        if (compareResult != null) {
          result = compareResult;
        } else {
          return null;
        }
        break;

      case Token.NAME:
        if (undefinedRight) {
          result = compareToUndefined(left, op);
          break;
        }

        if (rightLiteral) {
          boolean undefinedLeft = (left.getString().equals("undefined"));
          if (undefinedLeft) {
            result = compareToUndefined(right, op);
            break;
          }
        }

        if (Token.NAME != right.getType()) {
          return n;  // Only eval if they are the same type
        }
        String ln = left.getString();
        String rn = right.getString();
        if (!ln.equals(rn)) {
          return n;  // Not the same value name.
        }

        switch (op) {
          // If we knew the named value wouldn't be NaN, it would be nice
          // to handle EQ,NE,LE,GE,SHEQ, and SHNE.
          case Token.LT:
          case Token.GT:
            result = false;
            break;
          default:
            return n;  // don't handle that op
        }
        break;

      default:
        // assert, this should cover all consts
        return n;
    }

    Node newNode = new Node(result ? Token.TRUE : Token.FALSE);
    n.getParent().replaceChild(n, newNode);
    reportCodeChange();

    return newNode;
  }

  /**
   * @return Translate NOT expressions into TRUE or FALSE when possible.
   */
  private int getNormalizedNodeType(Node n) {
    int type = n.getType();
    if (type == Token.NOT) {
      TernaryValue value = NodeUtil.getPureBooleanValue(n);
      switch (value) {
        case TRUE:
          return Token.TRUE;
        case FALSE:
          return Token.FALSE;
      }
    }
    return type;
  }

  /**
   * The result of the comparison as a Boolean or null if the
   * result could not be determined.
   */
  private Boolean compareAsNumbers(int op, Node left, Node right) {
    Double leftValue = NodeUtil.getNumberValue(left);
    if (leftValue == null) {
      return null;
    }
    Double rightValue = NodeUtil.getNumberValue(right);
    if (rightValue == null) {
      return null;
    }

    double lv = leftValue;
    double rv = rightValue;

    Boolean result;
    switch (op) {
      case Token.SHEQ:
      case Token.EQ:
        Preconditions.checkState(
            left.getType() == Token.NUMBER && right.getType() == Token.NUMBER);
        result = lv == rv;
        break;
      case Token.SHNE:
      case Token.NE:
        Preconditions.checkState(
            left.getType() == Token.NUMBER && right.getType() == Token.NUMBER);
        result = lv != rv;
        break;
      case Token.LE: result = lv <= rv; break;
      case Token.LT: result = lv <  rv; break;
      case Token.GE: result = lv >= rv; break;
      case Token.GT: result = lv >  rv; break;
      default:
        return null;  // don't handle that op
    }
    return result;
  }

  /**
   * @param value The value to compare to "undefined"
   * @param op The boolean op to compare with
   * @return Whether the boolean op is true or false
   */
  private boolean compareToUndefined(Node value, int op) {
    boolean valueUndefined = ((Token.NAME == value.getType()
        && value.getString().equals("undefined"))
        || (Token.VOID == value.getType()
            && NodeUtil.isLiteralValue(value.getFirstChild(), false)));
    boolean valueNull = (Token.NULL == value.getType());
    boolean equivalent = valueUndefined || valueNull;
    switch (op) {
      case Token.EQ:
        // undefined is only equal to null or an undefined value
        return equivalent;
      case Token.NE:
        return !equivalent;
      case Token.SHEQ:
        return valueUndefined;
      case Token.SHNE:
        return !valueUndefined;
      case Token.LT:
      case Token.GT:
      case Token.LE:
      case Token.GE:
        return false;
      default:
        throw new IllegalStateException("unexpected.");
    }
  }

  /**
   * Try to fold away unnecessary object instantiation.
   * e.g. this[new String('eval')] -> this.eval
   */
  private Node tryFoldCtorCall(Node n) {
    Preconditions.checkArgument(n.getType() == Token.NEW);

    // we can remove this for GETELEM calls (anywhere else?)
    if (inForcedStringContext(n)) {
      return tryFoldInForcedStringContext(n);
    }
    return n;
  }

  /** Returns whether this node must be coerced to a string. */
  private boolean inForcedStringContext(Node n) {
    return n.getParent().getType() == Token.GETELEM &&
        n.getParent().getLastChild() == n;
  }

  private Node tryFoldInForcedStringContext(Node n) {
    // For now, we only know how to fold ctors.
    Preconditions.checkArgument(n.getType() == Token.NEW);

    Node objectType = n.getFirstChild();
    if (objectType.getType() != Token.NAME) {
      return n;
    }

    if (objectType.getString().equals("String")) {
      Node value = objectType.getNext();
      String stringValue = null;
      if (value == null) {
        stringValue = "";
      } else {
        if (!NodeUtil.isImmutableValue(value)) {
          return n;
        }

        stringValue = NodeUtil.getStringValue(value);
      }

      if (stringValue == null) {
        return n;
      }

      Node parent = n.getParent();
      Node newString = Node.newString(stringValue);

      parent.replaceChild(n, newString);
      newString.copyInformationFrom(parent);
      reportCodeChange();

      return newString;
    }
    return n;
  }

  private Node tryFoldKnownMethods(Node subtree) {
    // For now we only support .join(),
    // .indexOf(), .substring() and .substr()

    subtree = tryFoldArrayJoin(subtree);

    if (subtree.getType() == Token.CALL) {
      subtree = tryFoldKnownStringMethods(subtree);
    }

    return subtree;
  }

  /**
   * Try to eveluate known String methods
   *    .indexOf(), .substr(), .substring()
   */
  private Node tryFoldKnownStringMethods(Node subtree) {
    Preconditions.checkArgument(subtree.getType() == Token.CALL);

    // check if this is a call on a string method
    // then dispatch to specific folding method.
    Node callTarget = subtree.getFirstChild();
    if (callTarget == null) {
      return subtree;
    }

    if (!NodeUtil.isGet(callTarget)) {
      return subtree;
    }

    Node stringNode = callTarget.getFirstChild();
    Node functionName = stringNode.getNext();

    if ((stringNode.getType() != Token.STRING) || (
        (functionName.getType() != Token.STRING))) {
      return subtree;
    }

    String functionNameString = functionName.getString();
    Node firstArg = callTarget.getNext();
    if (firstArg == null) {
      if (functionNameString.equals("toLowerCase")) {
        subtree = tryFoldStringToLowerCase(subtree, stringNode);
      } else if (functionNameString.equals("toUpperCase")) {
        subtree = tryFoldStringToUpperCase(subtree, stringNode);
      }
      return subtree;
    } else if (NodeUtil.isImmutableValue(firstArg)) {
      if (functionNameString.equals("indexOf") ||
          functionNameString.equals("lastIndexOf")) {
        subtree = tryFoldStringIndexOf(subtree, functionNameString,
            stringNode, firstArg);
      } else if (functionNameString.equals("substr")) {
        subtree = tryFoldStringSubstr(subtree, stringNode, firstArg);
      } else if (functionNameString.equals("substring")) {
        subtree = tryFoldStringSubstring(subtree, stringNode, firstArg);
      }
    }

    return subtree;
  }

  /**
   * @return The lowered string Node.
   */
  private Node tryFoldStringToLowerCase(Node subtree, Node stringNode) {
    // From Rhino, NativeString.java. See ECMA 15.5.4.11
    String lowered = stringNode.getString().toLowerCase(ROOT_LOCALE);
    Node replacement = Node.newString(lowered);
    subtree.getParent().replaceChild(subtree, replacement);
    reportCodeChange();
    return replacement;
  }

  /**
   * @return The uppered string Node.
   */
  private Node tryFoldStringToUpperCase(Node subtree, Node stringNode) {
    // From Rhino, NativeString.java. See ECMA 15.5.4.12
    String uppered = stringNode.getString().toUpperCase(ROOT_LOCALE);
    Node replacement = Node.newString(uppered);
    subtree.getParent().replaceChild(subtree, replacement);
    reportCodeChange();
    return replacement;
  }

  /**
   * Try to evaluate String.indexOf/lastIndexOf:
   *     "abcdef".indexOf("bc") -> 1
   *     "abcdefbc".indexOf("bc", 3) -> 6
   */
  private Node tryFoldStringIndexOf(
      Node n, String functionName, Node lstringNode, Node firstArg) {
    Preconditions.checkArgument(n.getType() == Token.CALL);
    Preconditions.checkArgument(lstringNode.getType() == Token.STRING);

    String lstring = NodeUtil.getStringValue(lstringNode);
    boolean isIndexOf = functionName.equals("indexOf");
    Node secondArg = firstArg.getNext();
    String searchValue = NodeUtil.getStringValue(firstArg);
    // searchValue must be a valid string.
    if (searchValue == null) {
      return n;
    }
    int fromIndex = isIndexOf ? 0 : lstring.length();
    if (secondArg != null) {
      // Third-argument and non-numeric second arg are problematic. Discard.
      if ((secondArg.getNext() != null) ||
          (secondArg.getType() != Token.NUMBER)) {
        return n;
      } else {
        fromIndex = (int) secondArg.getDouble();
      }
    }
    int indexVal = isIndexOf ? lstring.indexOf(searchValue, fromIndex)
                             : lstring.lastIndexOf(searchValue, fromIndex);
    Node newNode = Node.newNumber(indexVal);
    n.getParent().replaceChild(n, newNode);

    reportCodeChange();

    return newNode;
  }

  /**
   * Try to fold an array join: ['a', 'b', 'c'].join('') -> 'abc';
   */
  private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
      return n;
    }

    Node right = callTarget.getNext();
    if (right != null && !NodeUtil.isImmutableValue(right)) {
      return n;
    }

    Node arrayNode = callTarget.getFirstChild();
    Node functionName = arrayNode.getNext();

    if ((arrayNode.getType() != Token.ARRAYLIT) ||
        !functionName.getString().equals("join")) {
      return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = Lists.newLinkedList();
    StringBuilder sb = null;
    int foldedSize = 0;
    Node prev = null;
    Node elem = arrayNode.getFirstChild();
    // Merges adjacent String nodes.
    while (elem != null) {
      if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
        if (sb == null) {
          sb = new StringBuilder();
        } else {
          sb.append(joinString);
        }
        sb.append(NodeUtil.getArrayElementStringValue(elem));
      } else {
        if (sb != null) {
          Preconditions.checkNotNull(prev);
          // + 2 for the quotes.
          foldedSize += sb.length() + 2;
          arrayFoldedChildren.add(
              Node.newString(sb.toString()).copyInformationFrom(prev));
          sb = null;
        }
        foldedSize += InlineCostEstimator.getCost(elem);
        arrayFoldedChildren.add(elem);
      }
      prev = elem;
      elem = elem.getNext();
    }

    if (sb != null) {
      Preconditions.checkNotNull(prev);
      // + 2 for the quotes.
      foldedSize += sb.length() + 2;
      arrayFoldedChildren.add(
          Node.newString(sb.toString()).copyInformationFrom(prev));
    }
    // one for each comma.
    foldedSize += arrayFoldedChildren.size() - 1;

    int originalSize = InlineCostEstimator.getCost(n);
    switch (arrayFoldedChildren.size()) {
      case 0:
        Node emptyStringNode = Node.newString("");
        n.getParent().replaceChild(n, emptyStringNode);
        reportCodeChange();
        return emptyStringNode;
      case 1:
        Node foldedStringNode = arrayFoldedChildren.remove(0);
        if (foldedSize > originalSize) {
          return n;
        }
        arrayNode.detachChildren();
        if (foldedStringNode.getType() != Token.STRING) {
          // If the Node is not a string literal, ensure that
          // it is coerced to a string.
          Node replacement = new Node(Token.ADD,
              Node.newString("").copyInformationFrom(n),
              foldedStringNode);
          foldedStringNode = replacement;
        }
        n.getParent().replaceChild(n, foldedStringNode);
        reportCodeChange();
        return foldedStringNode;
      default:
        // No folding could actually be performed.
        if (arrayFoldedChildren.size() == arrayNode.getChildCount()) {
          return n;
        }
        int kJoinOverhead = "[].join()".length();
        foldedSize += kJoinOverhead;
        foldedSize += (right != null) ? InlineCostEstimator.getCost(right) : 0;
        if (foldedSize > originalSize) {
          return n;
        }
        arrayNode.detachChildren();
        for (Node node : arrayFoldedChildren) {
          arrayNode.addChildToBack(node);
        }
        reportCodeChange();
        break;
    }

    return n;
  }

  /**
   * Try to fold .substr() calls on strings
   */
  private Node tryFoldStringSubstr(Node n, Node stringNode, Node arg1) {
    Preconditions.checkArgument(n.getType() == Token.CALL);
    Preconditions.checkArgument(stringNode.getType() == Token.STRING);

    int start, length;
    String stringAsString = stringNode.getString();

    // TODO(nicksantos): We really need a NodeUtil.getNumberValue
    // function.
    if (arg1 != null && arg1.getType() == Token.NUMBER) {
      start = (int) arg1.getDouble();
    } else {
      return n;
    }

    Node arg2 = arg1.getNext();
    if (arg2 != null) {
      if (arg2.getType() == Token.NUMBER) {
        length = (int) arg2.getDouble();
      } else {
        return n;
      }

      if (arg2.getNext() != null) {
        // If we got more args than we expected, bail out.
        return n;
      }
    } else {
      // parameter 2 not passed
      length = stringAsString.length() - start;
    }

    // Don't handle these cases. The specification actually does
    // specify the behavior in some of these cases, but we haven't
    // done a thorough investigation that it is correctly implemented
    // in all browsers.
    if ((start + length) > stringAsString.length() ||
        (length < 0) ||
        (start < 0)) {
      return n;
    }

    String result = stringAsString.substring(start, start + length);
    Node resultNode = Node.newString(result);

    Node parent = n.getParent();
    parent.replaceChild(n, resultNode);
    reportCodeChange();
    return resultNode;
  }

  /**
   * Try to fold .substring() calls on strings
   */
  private Node tryFoldStringSubstring(Node n, Node stringNode, Node arg1) {
    Preconditions.checkArgument(n.getType() == Token.CALL);
    Preconditions.checkArgument(stringNode.getType() == Token.STRING);

    int start, end;
    String stringAsString = stringNode.getString();

    if (arg1 != null && arg1.getType() == Token.NUMBER) {
      start = (int) arg1.getDouble();
    } else {
      return n;
    }

    Node arg2 = arg1.getNext();
    if (arg2 != null) {
      if (arg2.getType() == Token.NUMBER) {
        end = (int) arg2.getDouble();
      } else {
        return n;
      }

      if (arg2.getNext() != null) {
        // If we got more args than we expected, bail out.
        return n;
      }
    } else {
      // parameter 2 not passed
      end = stringAsString.length();
    }

    // Don't handle these cases. The specification actually does
    // specify the behavior in some of these cases, but we haven't
    // done a thorough investigation that it is correctly implemented
    // in all browsers.
    if ((end > stringAsString.length()) ||
        (start > stringAsString.length()) ||
        (end < 0) ||
        (start < 0)) {
      return n;
    }

    String result = stringAsString.substring(start, end);
    Node resultNode = Node.newString(result);

    Node parent = n.getParent();
    parent.replaceChild(n, resultNode);
    reportCodeChange();
    return resultNode;
  }

  /**
   * Try to fold array-element. e.g [1, 2, 3][10];
   */
  private Node tryFoldGetElem(Node n, Node left, Node right) {
    Preconditions.checkArgument(n.getType() == Token.GETELEM);

    if (left.getType() == Token.OBJECTLIT) {
      return tryFoldObjectPropAccess(n, left, right);
    }

    if (left.getType() == Token.ARRAYLIT) {
      if (right.getType() != Token.NUMBER) {
        // Sometimes people like to use complex expressions to index into
        // arrays, or strings to index into array methods.
        return n;
      }

      double index = right.getDouble();
      int intIndex = (int) index;
      if (intIndex != index) {
        error(INVALID_GETELEM_INDEX_ERROR, right);
        return n;
      }

      if (intIndex < 0) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
      }

      Node elem = left.getFirstChild();
      for (int i = 0; elem != null && i < intIndex; i++) {
        elem = elem.getNext();
      }

      if (elem == null) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
      }

      if (elem.getType() == Token.EMPTY) {
        elem = NodeUtil.newUndefinedNode(elem);
      } else {
        left.removeChild(elem);
      }

      // Replace the entire GETELEM with the value
      n.getParent().replaceChild(n, elem);
      reportCodeChange();
      return elem;
    }
    return n;
  }

  /**
   * Try to fold array-length. e.g [1, 2, 3].length ==> 3, [x, y].length ==> 2
   */
  private Node tryFoldGetProp(Node n, Node left, Node right) {
    Preconditions.checkArgument(n.getType() == Token.GETPROP);

    if (left.getType() == Token.OBJECTLIT) {
      return tryFoldObjectPropAccess(n, left, right);
    }

    if (right.getType() == Token.STRING &&
        right.getString().equals("length")) {
      int knownLength = -1;
      switch (left.getType()) {
        case Token.ARRAYLIT:
          if (mayHaveSideEffects(left)) {
            // Nope, can't fold this, without handling the side-effects.
            return n;
          }
          knownLength = left.getChildCount();
          break;
        case Token.STRING:
          knownLength = left.getString().length();
          break;
        default:
          // Not a foldable case, forget it.
          return n;
      }

      Preconditions.checkState(knownLength != -1);
      Node lengthNode = Node.newNumber(knownLength);
      n.getParent().replaceChild(n, lengthNode);
      reportCodeChange();

      return lengthNode;
    }

    return n;
  }

  private Node tryFoldObjectPropAccess(Node n, Node left, Node right) {
    Preconditions.checkArgument(NodeUtil.isGet(n));

    if (left.getType() != Token.OBJECTLIT || right.getType() != Token.STRING) {
      return n;
    }

    Node parent = n.getParent();
    if ((NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n)
        || parent.getType() == Token.INC
        || parent.getType() == Token.DEC) {
      // If GETPROP/GETELEM is used as assignment target the object literal is
      // acting as a temporary we can't fold it here:
      //    "{a:x}.a += 1" is not "x += 1"
      return n;
    }

    // find the last definition in the object literal
    Node key = null;
    Node value = null;
    for (Node c = left.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getString().equals(right.getString())) {
        switch (c.getType()) {
          case Token.SET:
            continue;
          case Token.GET:
          case Token.STRING:
            if (value != null && mayHaveSideEffects(value)) {
              // The previously found value had side-effects
              return n;
            }
            key = c;
            value = key.getFirstChild();
            break;
          default:
            throw new IllegalStateException();
        }
      } else if (mayHaveSideEffects(c.getFirstChild())) {
        // We don't handle the side-effects here as they might need a temporary
        // or need to be reordered.
        return n;
      }
    }

    // Didn't find a definition of the name in the object literal, it might
    // be coming from the Object prototype
    if (value == null) {
      return n;
    }

    if (value.getType() == Token.FUNCTION && NodeUtil.referencesThis(value)) {
      // 'this' may refer to the object we are trying to remove
      return n;
    }

    Node replacement = value.detachFromParent();
    if (key.getType() == Token.GET){
      replacement = new Node(Token.CALL, replacement);
    }

    n.getParent().replaceChild(n, replacement);
    reportCodeChange();
    return n;
  }
}

```
