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
      <zipfileset src="${buildlib.dir}/rhino1_7R4pre/js.jar"/>
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

## src/com/google/javascript/jscomp/type/ChainableReverseAbstractInterpreter.java

```
/*
 * Copyright 2007 The Closure Compiler Authors.
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

package com.google.javascript.jscomp.type;

import static com.google.javascript.rhino.jstype.JSTypeNative.ALL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.BOOLEAN_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.CHECKED_UNKNOWN_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.STRING_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.U2U_CONSTRUCTOR_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID_TYPE;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.EnumElementType;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ParameterizedType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TemplateType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.Visitor;

/**
 * Chainable reverse abstract interpreter providing basic functionality.
 *
 */
public abstract class ChainableReverseAbstractInterpreter
    implements ReverseAbstractInterpreter {
  protected final CodingConvention convention;
  final JSTypeRegistry typeRegistry;
  private ChainableReverseAbstractInterpreter firstLink;
  private ChainableReverseAbstractInterpreter nextLink;

  /**
   * Constructs an interpreter, which is the only link in a chain. Interpreters
   * can be appended using {@link #append}.
   */
  public ChainableReverseAbstractInterpreter(CodingConvention convention,
      JSTypeRegistry typeRegistry) {
    Preconditions.checkNotNull(convention);
    this.convention = convention;
    this.typeRegistry = typeRegistry;
    firstLink = this;
    nextLink = null;
  }

  /**
   * Appends a link to {@code this}, returning the updated last link.
   * <p>
   * The pattern {@code new X().append(new Y())...append(new Z())} forms a
   * chain starting with X, then Y, then ... Z.
   * @param lastLink a chainable interpreter, with no next link
   * @return the updated last link
   */
  public ChainableReverseAbstractInterpreter append(
      ChainableReverseAbstractInterpreter lastLink) {
    Preconditions.checkArgument(lastLink.nextLink == null);
    this.nextLink = lastLink;
    lastLink.firstLink = this.firstLink;
    return lastLink;
  }

  /**
   * Gets the first link of this chain.
   */
  public ChainableReverseAbstractInterpreter getFirst() {
    return firstLink;
  }

  /**
   * Calculates the preciser scope starting with the first link.
   */
  protected FlowScope firstPreciserScopeKnowingConditionOutcome(Node condition,
      FlowScope blindScope, boolean outcome) {
    return firstLink.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);
  }

  /**
   * Delegates the calculation of the preciser scope to the next link.
   * If there is no next link, returns the blind scope.
   */
  protected FlowScope nextPreciserScopeKnowingConditionOutcome(Node condition,
      FlowScope blindScope, boolean outcome) {
    return nextLink != null ? nextLink.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome) : blindScope;
  }

  /**
   * Returns the type of a node in the given scope if the node corresponds to a
   * name whose type is capable of being refined.
   * @return The current type of the node if it can be refined, null otherwise.
   */
  protected JSType getTypeIfRefinable(Node node, FlowScope scope) {
    switch (node.getType()) {
      case Token.NAME:
        StaticSlot<JSType> nameVar = scope.getSlot(node.getString());
        if (nameVar != null) {
          JSType nameVarType = nameVar.getType();
          if (nameVarType == null) {
            nameVarType = node.getJSType();
          }
          return nameVarType;
        }
        return null;

      case Token.GETPROP:
        String qualifiedName = node.getQualifiedName();
        if (qualifiedName == null) {
          return null;
        }
        StaticSlot<JSType> propVar = scope.getSlot(qualifiedName);
        JSType propVarType = null;
        if (propVar != null) {
          propVarType = propVar.getType();
        }
        if (propVarType == null) {
          propVarType = node.getJSType();
        }
        if (propVarType == null) {
          propVarType = getNativeType(UNKNOWN_TYPE);
        }
        return propVarType;
    }
    return null;
  }

  /**
   * Declares a refined type in {@code scope} for the name represented by
   * {@code node}. It must be possible to refine the type of the given node in
   * the given scope, as determined by {@link #getTypeIfRefinable}.
   */
  protected void declareNameInScope(FlowScope scope, Node node, JSType type) {
    switch (node.getType()) {
      case Token.NAME:
        scope.inferSlotType(node.getString(), type);
        break;

      case Token.GETPROP:
        String qualifiedName = node.getQualifiedName();
        Preconditions.checkNotNull(qualifiedName);

        JSType origType = node.getJSType();
        origType = origType == null ? getNativeType(UNKNOWN_TYPE) : origType;
        scope.inferQualifiedSlot(node, qualifiedName, origType, type);
        break;

      case Token.THIS:
        // "this" references aren't currently modeled in the CFG.
        break;

      default:
        throw new IllegalArgumentException("Node cannot be refined. \n" +
            node.toStringTree());
    }
  }

  /**
   * @see #getRestrictedWithoutUndefined(JSType)
   */
  private final Visitor<JSType> restrictUndefinedVisitor =
    new Visitor<JSType>() {
      @Override
      public JSType caseEnumElementType(EnumElementType enumElementType) {
        JSType type = enumElementType.getPrimitiveType().visit(this);
        if (type != null && enumElementType.getPrimitiveType().isEquivalentTo(type)) {
          return enumElementType;
        } else {
          return type;
        }
      }

      @Override
      public JSType caseAllType() {
        return typeRegistry.createUnionType(OBJECT_TYPE, NUMBER_TYPE,
            STRING_TYPE, BOOLEAN_TYPE, NULL_TYPE);
      }

      @Override
      public JSType caseNoObjectType() {
        return getNativeType(NO_OBJECT_TYPE);
      }

      @Override
      public JSType caseNoType() {
        return getNativeType(NO_TYPE);
      }

      @Override
      public JSType caseBooleanType() {
        return getNativeType(BOOLEAN_TYPE);
      }

      @Override
      public JSType caseFunctionType(FunctionType type) {
        return type;
      }

      @Override
      public JSType caseNullType() {
        return getNativeType(NULL_TYPE);
      }

      @Override
      public JSType caseNumberType() {
        return getNativeType(NUMBER_TYPE);
      }

      @Override
      public JSType caseObjectType(ObjectType type) {
        return type;
      }

      @Override
      public JSType caseStringType() {
        return getNativeType(STRING_TYPE);
      }

      @Override
      public JSType caseUnionType(UnionType type) {
        return type.getRestrictedUnion(getNativeType(VOID_TYPE));
      }

      @Override
      public JSType caseUnknownType() {
        return getNativeType(UNKNOWN_TYPE);
      }

      @Override
      public JSType caseVoidType() {
        return null;
      }

      @Override
      public JSType caseParameterizedType(ParameterizedType type) {
        return caseObjectType(type);
      }

      @Override
      public JSType caseTemplateType(TemplateType templateType) {
        return caseObjectType(templateType);
      }
    };


  /**
   * @see #getRestrictedWithoutNull(JSType)
   */
  private final Visitor<JSType> restrictNullVisitor =
    new Visitor<JSType>() {
      @Override
      public JSType caseEnumElementType(EnumElementType enumElementType) {
        JSType type = enumElementType.getPrimitiveType().visit(this);
        if (type != null &&
            enumElementType.getPrimitiveType().isEquivalentTo(type)) {
          return enumElementType;
        } else {
          return type;
        }
      }

      @Override
      public JSType caseAllType() {
        return typeRegistry.createUnionType(OBJECT_TYPE, NUMBER_TYPE,
            STRING_TYPE, BOOLEAN_TYPE, VOID_TYPE);
      }

      @Override
      public JSType caseNoObjectType() {
        return getNativeType(NO_OBJECT_TYPE);
      }

      @Override
      public JSType caseNoType() {
        return getNativeType(NO_TYPE);
      }

      @Override
      public JSType caseBooleanType() {
        return getNativeType(BOOLEAN_TYPE);
      }

      @Override
      public JSType caseFunctionType(FunctionType type) {
        return type;
      }

      @Override
      public JSType caseNullType() {
        return null;
      }

      @Override
      public JSType caseNumberType() {
        return getNativeType(NUMBER_TYPE);
      }

      @Override
      public JSType caseObjectType(ObjectType type) {
        return type;
      }

      @Override
      public JSType caseStringType() {
        return getNativeType(STRING_TYPE);
      }

      @Override
      public JSType caseUnionType(UnionType type) {
        return type.getRestrictedUnion(getNativeType(NULL_TYPE));
      }

      @Override
      public JSType caseUnknownType() {
        return getNativeType(UNKNOWN_TYPE);
      }

      @Override
      public JSType caseVoidType() {
        return getNativeType(VOID_TYPE);
      }

      @Override
      public JSType caseParameterizedType(ParameterizedType type) {
        return caseObjectType(type);
      }

      @Override
      public JSType caseTemplateType(TemplateType templateType) {
        return caseObjectType(templateType);
      }
    };

  /**
   * A class common to all visitors that need to restrict the type based on
   * {@code typeof}-like conditions.
   */
  abstract class RestrictByTypeOfResultVisitor
      implements Visitor<JSType> {

    /**
     * Abstracts away the similarities between visiting the unknown type and the
     * all type.
     * @param topType {@code UNKNOWN_TYPE} or {@code ALL_TYPE}
     * @return the restricted type
     * @see #caseAllType
     * @see #caseUnknownType
     */
    protected abstract JSType caseTopType(JSType topType);

    @Override
    public JSType caseAllType() {
      return caseTopType(getNativeType(ALL_TYPE));
    }

    @Override
    public JSType caseUnknownType() {
      return caseTopType(getNativeType(CHECKED_UNKNOWN_TYPE));
    }

    @Override
    public JSType caseUnionType(UnionType type) {
      JSType restricted = null;
      for (JSType alternate : type.getAlternates()) {
        JSType restrictedAlternate = alternate.visit(this);
        if (restrictedAlternate != null) {
          if (restricted == null) {
            restricted = restrictedAlternate;
          } else {
            restricted = restrictedAlternate.getLeastSupertype(restricted);
          }
        }
      }
      return restricted;
    }

    @Override
    public JSType caseNoType() {
      return getNativeType(NO_TYPE);
    }

    @Override
    public JSType caseEnumElementType(EnumElementType enumElementType) {
      // NOTE(nicksantos): This is a white lie. Suppose we have:
      // /** @enum {string|number} */ var MyEnum = ...;
      // if (goog.isNumber(myEnumInstance)) {
      //   /* what is myEnumInstance here? */
      // }
      // There is no type that represents {MyEnum - string}. What we really
      // need is a notion of "enum subtyping", so that we could dynamically
      // create a subtype of MyEnum restricted by string. In any case,
      // this should catch the common case.
      JSType type = enumElementType.getPrimitiveType().visit(this);
      if (type != null &&
          enumElementType.getPrimitiveType().isEquivalentTo(type)) {
        return enumElementType;
      } else {
        return type;
      }
    }

    @Override
    public JSType caseParameterizedType(ParameterizedType type) {
      return caseObjectType(type);
    }

    @Override
    public JSType caseTemplateType(TemplateType templateType) {
      return caseObjectType(templateType);
    }
  }

  /**
   * A class common to all visitors that need to restrict the type based on
   * some {@code typeof}-like condition being true. All base cases return
   * {@code null}. It is up to the subclasses to override the appropriate ones.
   */
  abstract class RestrictByTrueTypeOfResultVisitor
      extends RestrictByTypeOfResultVisitor {
    @Override
    public JSType caseNoObjectType() {
      return null;
    }

    @Override
    public JSType caseBooleanType() {
      return null;
    }

    @Override
    public JSType caseFunctionType(FunctionType type) {
      return null;
    }

    @Override
    public JSType caseNullType() {
      return null;
    }

    @Override
    public JSType caseNumberType() {
      return null;
    }

    @Override
    public JSType caseObjectType(ObjectType type) {
      return null;
    }

    @Override
    public JSType caseStringType() {
      return null;
    }

    @Override
    public JSType caseVoidType() {
      return null;
    }
  }

  /**
   * A class common to all visitors that need to restrict the type based on
   * some {@code typeof}-like condition being false. All base cases return
   * their type. It is up to the subclasses to override the appropriate ones.
   */
  abstract class RestrictByFalseTypeOfResultVisitor
      extends RestrictByTypeOfResultVisitor {
    @Override
    protected JSType caseTopType(JSType topType) {
      return topType;
    }

    @Override
    public JSType caseNoObjectType() {
      return getNativeType(NO_OBJECT_TYPE);
    }

    @Override
    public JSType caseBooleanType() {
      return getNativeType(BOOLEAN_TYPE);
    }

    @Override
    public JSType caseFunctionType(FunctionType type) {
      return type;
    }

    @Override
    public JSType caseNullType() {
      return getNativeType(NULL_TYPE);
    }

    @Override
    public JSType caseNumberType() {
      return getNativeType(NUMBER_TYPE);
    }

    @Override
    public JSType caseObjectType(ObjectType type) {
      return type;
    }

    @Override
    public JSType caseStringType() {
      return getNativeType(STRING_TYPE);
    }

    @Override
    public JSType caseVoidType() {
      return getNativeType(VOID_TYPE);
    }
  }

  /**
   * @see ChainableReverseAbstractInterpreter#getRestrictedByTypeOfResult
   */
  private class RestrictByOneTypeOfResultVisitor
      extends RestrictByTypeOfResultVisitor {
    /**
     * A value known to be equal or not equal to the result of the
     * {@code typeOf} operation.
     */
    private final String value;

    /**
     * {@code true} if the {@code typeOf} result is known to equal
     * {@code value}; {@code false} if it is known <em>not</em> to equal
     * {@code value}.
     */
    private final boolean resultEqualsValue;

    RestrictByOneTypeOfResultVisitor(String value, boolean resultEqualsValue) {
      this.value = value;
      this.resultEqualsValue = resultEqualsValue;
    }

    /**
     * Computes whether the given result of a {@code typeof} operator matches
     * expectations, i.e. whether a type that gives such a result should be
     * kept.
     */
    private boolean matchesExpectation(String result) {
      return result.equals(value) == resultEqualsValue;
    }

    @Override
    protected JSType caseTopType(JSType topType) {
      JSType result = topType;
      if (resultEqualsValue) {
        JSType typeByName = getNativeTypeForTypeOf(value);
        if (typeByName != null) {
          result = typeByName;
        }
      }
      return result;
    }

    @Override
    public JSType caseNoObjectType() {
      return (value.equals("object") || value.equals("function")) ==
          resultEqualsValue ? getNativeType(NO_OBJECT_TYPE) : null;
    }

    @Override
    public JSType caseBooleanType() {
      return matchesExpectation("boolean") ? getNativeType(BOOLEAN_TYPE) : null;
    }

    @Override
    public JSType caseFunctionType(FunctionType type) {
      return matchesExpectation("function") ? type : null;
    }

    @Override
    public JSType caseNullType() {
      return matchesExpectation("object") ? getNativeType(NULL_TYPE) : null;
    }

    @Override
    public JSType caseNumberType() {
      return matchesExpectation("number") ? getNativeType(NUMBER_TYPE) : null;
    }

    @Override
    public JSType caseObjectType(ObjectType type) {
      if (value.equals("function")) {
        JSType ctorType = getNativeType(U2U_CONSTRUCTOR_TYPE);
        if (resultEqualsValue) {
          // Objects are restricted to "Function", subtypes are left
          return ctorType.getGreatestSubtype(type);
        } else {
          // Only filter out subtypes of "function"
          return type.isSubtype(ctorType) ? null : type;
        }
      }
      return matchesExpectation("object") ? type : null;
    }

    @Override
    public JSType caseStringType() {
      return matchesExpectation("string") ? getNativeType(STRING_TYPE) : null;
    }

    @Override
    public JSType caseVoidType() {
      return matchesExpectation("undefined") ? getNativeType(VOID_TYPE) : null;
    }
  }

  /**
   * Returns a version of type where undefined is not present.
   */
  protected final JSType getRestrictedWithoutUndefined(JSType type) {
    return type == null ? null : type.visit(restrictUndefinedVisitor);
  }

  /**
   * Returns a version of type where null is not present.
   */
  protected final JSType getRestrictedWithoutNull(JSType type) {
    return type == null ? null : type.visit(restrictNullVisitor);
  }

  /**
   * Returns a version of {@code type} that is restricted by some knowledge
   * about the result of the {@code typeof} operation.
   * <p>
   * The behavior of the {@code typeof} operator can be summarized by the
   * following table:
   * <table>
   * <tr><th>type</th><th>result</th></tr>
   * <tr><td>{@code undefined}</td><td>"undefined"</td></tr>
   * <tr><td>{@code null}</td><td>"object"</td></tr>
   * <tr><td>{@code boolean}</td><td>"boolean"</td></tr>
   * <tr><td>{@code number}</td><td>"number"</td></tr>
   * <tr><td>{@code string}</td><td>"string"</td></tr>
   * <tr><td>{@code Object} (which doesn't implement [[Call]])</td>
   *     <td>"object"</td></tr>
   * <tr><td>{@code Object} (which implements [[Call]])</td>
   *     <td>"function"</td></tr>
   * </table>
   * @param type the type to restrict
   * @param value A value known to be equal or not equal to the result of the
   *        {@code typeof} operation
   * @param resultEqualsValue {@code true} if the {@code typeOf} result is known
   *        to equal {@code value}; {@code false} if it is known <em>not</em> to
   *        equal {@code value}
   * @return the restricted type or null if no version of the type matches the
   *         restriction
   */
  JSType getRestrictedByTypeOfResult(JSType type, String value,
                                     boolean resultEqualsValue) {
    if (type == null) {
      if (resultEqualsValue) {
        JSType result = getNativeTypeForTypeOf(value);
        return result == null ? getNativeType(CHECKED_UNKNOWN_TYPE) : result;
      } else {
        return null;
      }
    }
    return type.visit(
        new RestrictByOneTypeOfResultVisitor(value, resultEqualsValue));
  }

  JSType getNativeType(JSTypeNative typeId) {
    return typeRegistry.getNativeType(typeId);
  }

  /**
   * If we definitely know what a type is based on the typeof result,
   * return it.  Otherwise, return null.
   *
   * The typeof operation in JS is poorly defined, and this function works
   * for both the native typeof and goog.typeOf. It should not be made public,
   * because its semantics are informally defined, and would be wrong in
   * the general case.
   */
  private JSType getNativeTypeForTypeOf(String value) {
    if (value.equals("number")) {
      return getNativeType(NUMBER_TYPE);
    } else if (value.equals("boolean")) {
      return getNativeType(BOOLEAN_TYPE);
    } else if (value.equals("string")) {
      return getNativeType(STRING_TYPE);
    } else if (value.equals("undefined")) {
      return getNativeType(VOID_TYPE);
    } else if (value.equals("function")) {
      return getNativeType(U2U_CONSTRUCTOR_TYPE);
    } else {
      return null;
    }
  }
}

```
