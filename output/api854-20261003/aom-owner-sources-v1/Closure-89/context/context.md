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
  <property name="version" value="1.0-SNAPSHOT"/>
  <property name="jarfile-nodeps" value="${build.dir}/closure-${ant.project.name}-${version}.jar" />
  <property name="jarfile-javadoc" value="${build.dir}/closure-${ant.project.name}-${version}-javadoc.jar" />
  <property name="jarfile-sources" value="${build.dir}/closure-${ant.project.name}-${version}-sources.jar" />
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

  <artifact:pom id="project" file="closure-compiler.pom" />

  <target name="mvn-install" depends="jar-nodeps,jar-javadoc,jar-sources" description="Install closure-compiler artifacts into the local maven repo">
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
      <arg value="-DpomFile=closure-compiler.pom" />
      <arg value="-Dfile=${jarfile-nodeps}" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the sources artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=closure-compiler.pom" />
      <arg value="-Dfile=${jarfile-sources}" />
      <arg value="-Dclassifier=sources" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the javadoc artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=closure-compiler.pom" />
      <arg value="-Dfile=${jarfile-javadoc}" />
      <arg value="-Dclassifier=javadoc" />
      <arg value="-Pgpg" />
    </artifact:mvn>
  </target>

  <!-- Sync maven dependencies listed in pom.xml -->
  <target name="mvn-deps-sync" description="sync dependencies/jars in closure-compiler.pom to lib">
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

  <target name="compile" description="compile the source code">
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
      <entry key="compiler.version" value="${version}"/>
    </propertyfile>

    <!-- Move runtime_type_check.js where RuntimeTypeCheck.java expects it. -->
    <mkdir dir="${classes.dir}/com/google/javascript/jscomp/js" />
    <copy file="${src.dir}/com/google/javascript/jscomp/js/runtime_type_check.js"
          todir="${classes.dir}/com/google/javascript/jscomp/js" />
  </target>

  <target name="jar-nodeps"
          depends="compile"
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

## src/com/google/javascript/jscomp/CollapseProperties.java

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
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.GlobalNamespace.Name;
import com.google.javascript.jscomp.GlobalNamespace.Ref;
import com.google.javascript.jscomp.GlobalNamespace.Ref.Type;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Flattens global objects/namespaces by replacing each '.' with '$' in
 * their names. This reduces the number of property lookups the browser has
 * to do and allows the {@link RenameVars} pass to shorten namespaced names.
 * For example, goog.events.handleEvent() -> goog$events$handleEvent() -> Za().
 *
 * <p>If a global object's name is assigned to more than once, or if a property
 * is added to the global object in a complex expression, then none of its
 * properties will be collapsed (for safety/correctness).
 *
 * <p>If, after a global object is declared, it is never referenced except when
 * its properties are read or set, then the object will be removed after its
 * properties have been collapsed.
 *
 * <p>Uninitialized variable stubs are created at a global object's declaration
 * site for any of its properties that are added late in a local scope.
 *
 * <p>If, after an object is declared, it is referenced directly in a way that
 * might create an alias for it, then none of its properties will be collapsed.
 * This behavior is a safeguard to prevent the values associated with the
 * flattened names from getting out of sync with the object's actual property
 * values. For example, in the following case, an alias a$b, if created, could
 * easily keep the value 0 even after a.b became 5:
 * <code> a = {b: 0}; c = a; c.b = 5; </code>.
 *
 * <p>This pass doesn't flatten property accesses of the form: a[b].
 *
 * <p>For lots of examples, see the unit test.
 *
 */
class CollapseProperties implements CompilerPass {

  // Warnings
  static final DiagnosticType UNSAFE_NAMESPACE_WARNING =
      DiagnosticType.warning(
          "JSC_UNSAFE_NAMESPACE",
          "incomplete alias created for namespace {0}");

  static final DiagnosticType NAMESPACE_REDEFINED_WARNING =
      DiagnosticType.warning(
          "JSC_NAMESPACE_REDEFINED",
          "namespace {0} should not be redefined");

  static final DiagnosticType UNSAFE_THIS = DiagnosticType.warning(
      "JSC_UNSAFE_THIS",
      "dangerous use of 'this' in static method {0}");

  private AbstractCompiler compiler;

  /** Global namespace tree */
  private List<Name> globalNames;

  /** Maps names (e.g. "a.b.c") to nodes in the global namespace tree */
  private Map<String, Name> nameMap;

  private final boolean collapsePropertiesOnExternTypes;
  private final boolean inlineAliases;

  /**
   * Creates an instance.
   *
   * @param compiler The JSCompiler, for reporting code changes
   * @param collapsePropertiesOnExternTypes if true, will rename user-defined
   *     static properties on externed typed. E.g. String.foo.
   * @param inlineAliases Whether we're allowed to inline local aliases of
   *     namespaces, etc.
   */
  CollapseProperties(AbstractCompiler compiler,
      boolean collapsePropertiesOnExternTypes, boolean inlineAliases) {
    this.compiler = compiler;
    this.collapsePropertiesOnExternTypes = collapsePropertiesOnExternTypes;
    this.inlineAliases = inlineAliases;
  }

  @Override
  public void process(Node externs, Node root) {
    GlobalNamespace namespace;
    if (collapsePropertiesOnExternTypes) {
      namespace = new GlobalNamespace(compiler, externs, root);
    } else {
      namespace = new GlobalNamespace(compiler, root);
    }

    if (inlineAliases) {
      inlineAliases(namespace);
    }
    nameMap = namespace.getNameIndex();
    globalNames = namespace.getNameForest();
    checkNamespaces();

    for (Name n : globalNames) {
      flattenReferencesToCollapsibleDescendantNames(n, n.name);
    }

    // We collapse property definitions after collapsing property references
    // because this step can alter the parse tree above property references,
    // invalidating the node ancestry stored with each reference.
    for (Name n : globalNames) {
      collapseDeclarationOfNameAndDescendants(n, n.name);
    }
  }

  /**
   * For each qualified name N in the global scope, we check if:
   * (a) No ancestor of N is ever aliased or assigned an unknown value type.
   *     (If N = "a.b.c", "a" and "a.b" are never aliased).
   * (b) N has exactly one write, and it lives in the global scope.
   * (c) N is aliased in a local scope.
   *
   * If (a) is true, then GlobalNamespace must know all the writes to N.
   * If (a) and (b) are true, then N cannot change during the execution of
   *    a local scope.
   * If (a) and (b) and (c) are true, then the alias can be inlined if the
   *    alias obeys the usual rules for how we decide whether a variable is
   *    inlineable.
   * @see InlineVariables
   */
  private void inlineAliases(GlobalNamespace namespace) {
    // Invariant: All the names in the worklist meet condition (a).
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
      Name name = workList.pop();

      if (name.globalSets == 1 && name.localSets == 0 &&
          name.aliasingGets > 0) {
        // {@code name} meets condition (b). Find all of its local aliases
        // and try to inline them.
        List<Ref> refs = Lists.newArrayList(name.refs);
        for (Ref ref : refs) {
          if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
            // {@code name} meets condition (c). Try to inline it.
            if (inlineAliasIfPossible(ref, namespace)) {
              name.removeRef(ref);
            }
          }
        }
      }

      // Check if {@code name} has any aliases left after the
      // local-alias-inlining above.
      if ((name.type == Name.Type.OBJECTLIT ||
           name.type == Name.Type.FUNCTION) &&
          name.aliasingGets == 0 && name.props != null) {
        // All of {@code name}'s children meet condition (a), so they can be
        // added to the worklist.
        workList.addAll(name.props);
      }
    }
  }

  private boolean inlineAliasIfPossible(Ref alias, GlobalNamespace namespace) {
    // Ensure that the alias is assigned to a local variable at that
    // variable's declaration. If the alias's parent is a NAME,
    // then the NAME must be the child of a VAR node, and we must
    // be in a VAR assignment.
    Node aliasParent = alias.node.getParent();
    if (aliasParent.getType() == Token.NAME) {
      // Ensure that the local variable is well defined and never reassigned.
      Scope scope = alias.scope;
      Var aliasVar = scope.getVar(aliasParent.getString());
      ReferenceCollectingCallback collector =
          new ReferenceCollectingCallback(compiler,
              ReferenceCollectingCallback.DO_NOTHING_BEHAVIOR,
              Predicates.<Var>equalTo(aliasVar));
      (new NodeTraversal(compiler, collector)).traverseAtScope(scope);

      ReferenceCollection aliasRefs =
          collector.getReferenceCollection(aliasVar);
      if (aliasRefs.isWellDefined()
          && aliasRefs.firstReferenceIsAssigningDeclaration()
          && aliasRefs.isAssignedOnceInLifetime()) {
        // The alias is well-formed, so do the inlining now.
        int size = aliasRefs.references.size();
        Set<Node> newNodes = Sets.newHashSetWithExpectedSize(size - 1);
        for (int i = 1; i < size; i++) {
          ReferenceCollectingCallback.Reference aliasRef =
              aliasRefs.references.get(i);

          Node newNode = alias.node.cloneTree();
          aliasRef.getParent().replaceChild(aliasRef.getNameNode(), newNode);
          newNodes.add(newNode);
        }

        // just set the original alias to null.
        aliasParent.replaceChild(alias.node, new Node(Token.NULL));
        compiler.reportCodeChange();

        // Inlining the variable may have introduced new references
        // to descendents of {@code name}. So those need to be collected now.
        namespace.scanNewNodes(alias.scope, newNodes);
        return true;
      }
    }

    return false;
  }

  /**
   * Runs through all namespaces (prefixes of classes and enums), and checks if
   * any of them have been used in an unsafe way.
   */
  private void checkNamespaces() {
    for (Name name : nameMap.values()) {
      if (name.isNamespace() && name.refs != null &&
          (name.aliasingGets > 0 || name.localSets + name.globalSets > 1)) {
        boolean initialized = name.declaration != null;
        for (Ref ref : name.refs) {
          if (ref.type == Ref.Type.SET_FROM_GLOBAL ||
              ref.type == Ref.Type.SET_FROM_LOCAL) {
            if (initialized) {
              warnAboutNamespaceRedefinition(name, ref);
            }

            initialized = true;
          } else if (ref.type == Ref.Type.ALIASING_GET) {
            warnAboutNamespaceAliasing(name, ref);
          }
        }
      }
    }
  }

  /**
   * Reports a warning because a namespace was aliased.
   *
   * @param nameObj A namespace that is being aliased
   * @param ref The reference that forced the alias
   */
  private void warnAboutNamespaceAliasing(Name nameObj, Ref ref) {
    compiler.report(
        JSError.make(ref.sourceName, ref.node,
                     UNSAFE_NAMESPACE_WARNING, nameObj.fullName()));
  }

  /**
   * Reports a warning because a namespace was redefined.
   *
   * @param nameObj A namespace that is being redefined
   * @param ref The reference that set the namespace
   */
  private void warnAboutNamespaceRedefinition(Name nameObj, Ref ref) {
    compiler.report(
        JSError.make(ref.sourceName, ref.node,
                     NAMESPACE_REDEFINED_WARNING, nameObj.fullName()));
  }

  /**
   * Flattens all references to collapsible properties of a global name except
   * their initial definitions. Recurses on subnames.
   *
   * @param n An object representing a global name
   * @param alias The flattened name for {@code n}
   */
  private void flattenReferencesToCollapsibleDescendantNames(
      Name n, String alias) {
    if (n.props == null) return;

    for (Name p : n.props) {
      String propAlias = appendPropForAlias(alias, p.name);

      if (p.canCollapse()) {
        flattenReferencesTo(p, propAlias);
      }

      flattenReferencesToCollapsibleDescendantNames(p, propAlias);
    }
  }

  /**
   * Flattens all references to a collapsible property of a global name except
   * its initial definition.
   *
   * @param n A global property name (e.g. "a.b" or "a.b.c.d")
   * @param alias The flattened name (e.g. "a$b" or "a$b$c$d")
   */
  private void flattenReferencesTo(Name n, String alias) {
    if (n.refs != null) {
      String originalName = n.fullName();
      for (Ref r : n.refs) {
        Node rParent = r.node.getParent();

        // There are two cases when we shouldn't flatten a reference:
        // 1) Object literal keys, because duplicate keys show up as refs.
        // 2) References inside a complex assign. (a = x.y = 0). These are
        //    called TWIN references, because they show up twice in the
        //    reference list. Only collapse the set, not the alias.
        if (!NodeUtil.isObjectLitKey(r.node, rParent) &&
            (r.getTwin() == null || r.isSet())) {
          flattenNameRef(alias, r.node, rParent, originalName);
        }
      }
    }

    // Flatten all occurrences of a name as a prefix of its subnames. For
    // example, if {@code n} corresponds to the name "a.b", then "a.b" will be
    // replaced with "a$b" in all occurrences of "a.b.c", "a.b.c.d", etc.
    if (n.props != null) {
      for (Name p : n.props) {
        flattenPrefixes(alias, p, 1);
      }
    }
  }

  /**
   * Flattens all occurrences of a name as a prefix of subnames beginning
   * with a particular subname.
   *
   * @param n A global property name (e.g. "a.b.c.d")
   * @param alias A flattened prefix name (e.g. "a$b")
   * @param depth The difference in depth between the property name and
   *    the prefix name (e.g. 2)
   */
  private void flattenPrefixes(String alias, Name n, int depth) {
    // Only flatten the prefix of a name declaration if the name being
    // initialized is fully qualified (i.e. not an object literal key).
    String originalName = n.fullName();
    if (n.declaration != null && n.declaration.node != null &&
        n.declaration.node.getType() == Token.GETPROP) {
      flattenNameRefAtDepth(alias, n.declaration.node, depth, originalName);
    }

    if (n.refs != null) {
      for (Ref r : n.refs) {

        // References inside a complex assign (a = x.y = 0)
        // have twins. We should only flatten one of the twins.
        if (r.getTwin() == null || r.isSet()) {
          flattenNameRefAtDepth(alias, r.node, depth, originalName);
        }
      }
    }

    if (n.props != null) {
      for (Name p : n.props) {
        flattenPrefixes(alias, p, depth + 1);
      }
    }
  }

  /**
   * Flattens a particular prefix of a single name reference.
   *
   * @param alias A flattened prefix name (e.g. "a$b")
   * @param n The node corresponding to a subproperty name (e.g. "a.b.c.d")
   * @param depth The difference in depth between the property name and
   *    the prefix name (e.g. 2)
   * @param originalName String version of the property name.
   */
  private void flattenNameRefAtDepth(String alias, Node n, int depth,
      String originalName) {
    // This method has to work for both GETPROP chains and, in rare cases,
    // OBJLIT keys, possibly nested. That's why we check for children before
    // proceeding. In the OBJLIT case, we don't need to do anything.
    int nType = n.getType();
    boolean isQName = nType == Token.NAME || nType == Token.GETPROP;
    boolean isObjKey = nType == Token.STRING || nType == Token.NUMBER;
    Preconditions.checkState(isObjKey || isQName);
    if (isQName) {
      for (int i = 1; i < depth && n.hasChildren(); i++) {
        n = n.getFirstChild();
      }
      if (n.hasChildren()) {
        flattenNameRef(alias, n.getFirstChild(), n, originalName);
      }
    }
  }

  /**
   * Replaces a GETPROP a.b.c with a NAME a$b$c.
   *
   * @param alias A flattened prefix name (e.g. "a$b")
   * @param n The GETPROP node corresponding to the original name (e.g. "a.b")
   * @param parent {@code n}'s parent
   * @param originalName String version of the property name.
   */
  private void flattenNameRef(String alias, Node n, Node parent,
      String originalName) {
    // BEFORE:
    //   getprop
    //     getprop
    //       name a
    //       string b
    //     string c
    // AFTER:
    //   name a$b$c
    Node ref = NodeUtil.newName(
        compiler.getCodingConvention(), alias, n, originalName);
    NodeUtil.copyNameAnnotations(n.getLastChild(), ref);
    parent.replaceChild(n, ref);
    compiler.reportCodeChange();
  }

  /**
   * Collapses definitions of the collapsible properties of a global name.
   * Recurses on subnames that also represent JavaScript objects with
   * collapsible properties.
   *
   * @param n A node representing a global name
   * @param alias The flattened name for {@code n}
   */
  private void collapseDeclarationOfNameAndDescendants(Name n, String alias) {
    boolean canCollapseChildNames = n.canCollapseUnannotatedChildNames();

    // Handle this name first so that nested object literals get unrolled.
    if (n.canCollapse() && canCollapseChildNames) {
      updateObjLitOrFunctionDeclaration(n, alias);
    }

    if (n.props != null) {
      for (Name p : n.props) {
        // Recurse first so that saved node ancestries are intact when needed.
        collapseDeclarationOfNameAndDescendants(
            p, appendPropForAlias(alias, p.name));

        if (!p.inExterns && canCollapseChildNames && p.declaration != null &&
            p.declaration.node != null &&
            p.declaration.node.getParent() != null &&
            p.declaration.node.getParent().getType() == Token.ASSIGN) {
          updateSimpleDeclaration(
              appendPropForAlias(alias, p.name), p, p.declaration);
        }
      }
    }
  }

  /**
   * Updates the initial assignment to a collapsible property at global scope
   * by changing it to a variable declaration (e.g. a.b = 1 -> var a$b = 1).
   * The property's value may either be a primitive or an object literal or
   * function whose properties aren't collapsible.
   *
   * @param alias The flattened property name (e.g. "a$b")
   * @param refName The name for the reference being updated.
   * @param ref An object containing information about the assignment getting
   *     updated
   */
  private void updateSimpleDeclaration(String alias, Name refName, Ref ref) {
    Node rvalue = ref.node.getNext();
    Node parent = ref.node.getParent();
    Node gramps = parent.getParent();
    Node greatGramps = gramps.getParent();
    Node greatGreatGramps = greatGramps.getParent();

    if (rvalue != null && rvalue.getType() == Token.FUNCTION) {
      checkForHosedThisReferences(rvalue, refName.docInfo, refName);
    }

    // Create the new alias node.
    Node nameNode = NodeUtil.newName(
        compiler.getCodingConvention(), alias, gramps.getFirstChild(),
        refName.fullName());
    NodeUtil.copyNameAnnotations(ref.node.getLastChild(), nameNode);

    if (gramps.getType() == Token.EXPR_RESULT) {
      // BEFORE: a.b.c = ...;
      //   exprstmt
      //     assign
      //       getprop
      //         getprop
      //           name a
      //           string b
      //         string c
      //       NODE
      // AFTER: var a$b$c = ...;
      //   var
      //     name a$b$c
      //       NODE

      // Remove the rvalue (NODE).
      parent.removeChild(rvalue);
      nameNode.addChildToFront(rvalue);

      Node varNode = new Node(Token.VAR, nameNode);
      greatGramps.replaceChild(gramps, varNode);
    } else {
      // This must be a complex assignment.
      Preconditions.checkNotNull(ref.getTwin());

      // BEFORE:
      // ... (x.y = 3);
      //
      // AFTER:
      // var x$y;
      // ... (x$y = 3);

      Node current = gramps;
      Node currentParent = gramps.getParent();
      for (; currentParent.getType() != Token.SCRIPT &&
             currentParent.getType() != Token.BLOCK;
           current = currentParent,
           currentParent = currentParent.getParent()) {}

      // Create a stub variable declaration right
      // before the current statement.
      Node stubVar = new Node(Token.VAR, nameNode.cloneTree())
          .copyInformationFrom(nameNode);
      currentParent.addChildBefore(stubVar, current);

      parent.replaceChild(ref.node, nameNode);
    }

    compiler.reportCodeChange();
  }

  /**
   * Updates the first initialization (a.k.a "declaration") of a global name.
   * This involves flattening the global name (if it's not just a global
   * variable name already), collapsing object literal keys into global
   * variables, declaring stub global variables for properties added later
   * in a local scope, and eliminating the global name entirely (if possible).
   *
   * @param n An object representing a global name (e.g. "a", "a.b.c")
   * @param alias The flattened name for {@code n} (e.g. "a", "a$b$c")
   */
  private void updateObjLitOrFunctionDeclaration(Name n, String alias) {
    switch (n.declaration.node.getParent().getType()) {
      case Token.ASSIGN:
        updateObjLitOrFunctionDeclarationAtAssignNode(n, alias);
        break;
      case Token.VAR:
        updateObjLitOrFunctionDeclarationAtVarNode(n);
        break;
      case Token.FUNCTION:
        updateFunctionDeclarationAtFunctionNode(n);
        break;
    }
  }

  /**
   * Updates the first initialization (a.k.a "declaration") of a global name
   * that occurs at an ASSIGN node. See comment for
   * {@link #updateObjLitOrFunctionDeclaration}.
   *
   * @param n An object representing a global name (e.g. "a", "a.b.c")
   * @param alias The flattened name for {@code n} (e.g. "a", "a$b$c")
   */
  private void updateObjLitOrFunctionDeclarationAtAssignNode(
      Name n, String alias) {
    // NOTE: It's important that we don't add additional nodes
    // (e.g. a var node before the exprstmt) because the exprstmt might be
    // the child of an if statement that's not inside a block).

    Ref ref = n.declaration;
    Node rvalue = ref.node.getNext();
    Node varNode = new Node(Token.VAR);
    Node varParent = ref.node.getAncestor(3);
    Node gramps = ref.node.getAncestor(2);
    boolean isObjLit = rvalue.getType() == Token.OBJECTLIT;

    if (isObjLit && n.canEliminate()) {
      // Eliminate the object literal altogether.
      varParent.replaceChild(gramps, varNode);
      ref.node = null;

    } else {
      if (rvalue.getType() == Token.FUNCTION) {
        checkForHosedThisReferences(rvalue, n.docInfo, n);
      }

      ref.node.getParent().removeChild(rvalue);

      Node nameNode = NodeUtil.newName(
          compiler.getCodingConvention(),
          alias, ref.node.getAncestor(2), n.fullName());

      if (ref.node.getLastChild().getBooleanProp(Node.IS_CONSTANT_NAME)) {
        nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
      }

      varNode.addChildToBack(nameNode);
      nameNode.addChildToFront(rvalue);
      varParent.replaceChild(gramps, varNode);

      // Update the node ancestry stored in the reference.
      ref.node = nameNode;
    }

    if (isObjLit) {
      declareVarsForObjLitValues(
          n, alias, rvalue,
          varNode, varParent.getChildBefore(varNode), varParent);
    }

    addStubsForUndeclaredProperties(n, alias, varParent, varNode);

    if (!varNode.hasChildren()) {
      varParent.removeChild(varNode);
    }

    compiler.reportCodeChange();
  }

  /**
   * Warns about any references to "this" in the given FUNCTION. The function
   * is getting collapsed, so the references will change.
   */
  private void checkForHosedThisReferences(Node function, JSDocInfo docInfo,
      final Name name) {
    // A function is getting collapsed. Make sure that if it refers to
    // "this", it must be a constructor or documented with @this.
    if (docInfo == null ||
        (!docInfo.isConstructor() && !docInfo.hasThisType())) {
      NodeTraversal.traverse(compiler, function.getLastChild(),
          new NodeTraversal.AbstractShallowCallback() {
            public void visit(NodeTraversal t, Node n, Node parent) {
              if (n.getType() == Token.THIS) {
                compiler.report(
                    JSError.make(name.declaration.sourceName, n,
                        UNSAFE_THIS, name.fullName()));
              }
            }
          });
    }
  }

  /**
   * Updates the first initialization (a.k.a "declaration") of a global name
   * that occurs at a VAR node. See comment for
   * {@link #updateObjLitOrFunctionDeclaration}.
   *
   * @param n An object representing a global name (e.g. "a")
   */
  private void updateObjLitOrFunctionDeclarationAtVarNode(Name n) {
    Ref ref = n.declaration;
    String name = ref.node.getString();
    Node rvalue = ref.node.getFirstChild();
    Node varNode = ref.node.getParent();
    Node gramps = varNode.getParent();

    boolean isObjLit = rvalue.getType() == Token.OBJECTLIT;
    int numChanges = 0;

    if (isObjLit) {
      numChanges += declareVarsForObjLitValues(
          n, name, rvalue, varNode, gramps.getChildBefore(varNode),
          gramps);
    }

    numChanges += addStubsForUndeclaredProperties(n, name, gramps, varNode);

    if (isObjLit && n.canEliminate()) {
      varNode.removeChild(ref.node);
      if (!varNode.hasChildren()) {
        gramps.removeChild(varNode);
      }
      numChanges++;

      // Clear out the object reference, since we've eliminated it from the
      // parse tree.
      ref.node = null;
    }

    if (numChanges > 0) {
      compiler.reportCodeChange();
    }
  }

  /**
   * Updates the first initialization (a.k.a "declaration") of a global name
   * that occurs at a FUNCTION node. See comment for
   * {@link #updateObjLitOrFunctionDeclaration}.
   *
   * @param n An object representing a global name (e.g. "a")
   */
  private void updateFunctionDeclarationAtFunctionNode(Name n) {
    Ref ref = n.declaration;
    String fnName = ref.node.getString();
    addStubsForUndeclaredProperties(
        n, fnName, ref.node.getAncestor(2), ref.node.getParent());
  }

  /**
   * Declares global variables to serve as aliases for the values in an object
   * literal, optionally removing all of the object literal's keys and values.
   *
   * @param alias The object literal's flattened name (e.g. "a$b$c")
   * @param objlit The OBJLIT node
   * @param varNode The VAR node to which new global variables should be added
   *     as children
   * @param nameToAddAfter The child of {@code varNode} after which new
   *     variables should be added (may be null)
   * @param varParent {@code varNode}'s parent
   * @return The number of variables added
   */
  private int declareVarsForObjLitValues(
      Name objlitName, String alias, Node objlit, Node varNode,
      Node nameToAddAfter, Node varParent) {
    int numVars = 0;
    int arbitraryNameCounter = 0;
    boolean discardKeys = !objlitName.shouldKeepKeys();

    for (Node key = objlit.getFirstChild(), nextKey; key != null;
         key = nextKey) {
      Node value = key.getFirstChild();
      nextKey = key.getNext();

      // We generate arbitrary names for keys that aren't valid JavaScript
      // identifiers, since those keys are never referenced. (If they were,
      // this object literal's child names wouldn't be collapsible.) The only
      // reason that we don't eliminate them entirely is the off chance that
      // their values are expressions that have side effects.
      boolean isJsIdentifier = key.getType() != Token.NUMBER &&
                               TokenStream.isJSIdentifier(key.getString());
      String propName = isJsIdentifier ?
          key.getString() : String.valueOf(++arbitraryNameCounter);
      String propAlias = appendPropForAlias(alias, propName);
      String qName = objlitName.fullName() + '.' + propName;

      Node refNode = null;
      if (discardKeys) {
        objlit.removeChild(key);
        value.detachFromParent();
      } else {
        // Substitute a reference for the value.
        refNode = Node.newString(Token.NAME, propAlias);
        if (key.getBooleanProp(Node.IS_CONSTANT_NAME)) {
          refNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        }

        key.replaceChild(value, refNode);
      }

      // Declare the collapsed name as a variable with the original value.
      Node nameNode = Node.newString(Token.NAME, propAlias);
      nameNode.addChildToFront(value);
      if (key.getBooleanProp(Node.IS_CONSTANT_NAME)) {
        nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
      }
      Node newVar = new Node(Token.VAR, nameNode)
          .copyInformationFromForTree(key);
      if (nameToAddAfter != null) {
        varParent.addChildAfter(newVar, nameToAddAfter);
      } else {
        varParent.addChildBefore(newVar, varNode);
      }
      compiler.reportCodeChange();
      nameToAddAfter = newVar;

      if (isJsIdentifier) {
        // Update the global name's node ancestry if it hasn't already been
        // done. (Duplicate keys in an object literal can bring us here twice
        // for the same global name.)
        Name p = nameMap.get(qName);
        if (p != null) {
          if (!discardKeys) {
            Ref newAlias =
                p.declaration.cloneAndReclassify(Ref.Type.ALIASING_GET);
            newAlias.node = refNode;
            p.addRef(newAlias);
          }

          p.declaration.node = nameNode;

          if (value.getType() == Token.FUNCTION) {
            checkForHosedThisReferences(value, value.getJSDocInfo(), p);
          }
        }
      }

      numVars++;
    }
    return numVars;
  }

  /**
   * Adds global variable "stubs" for any properties of a global name that are
   * only set in a local scope or read but never set.
   *
   * @param n An object representing a global name (e.g. "a", "a.b.c")
   * @param alias The flattened name of the object whose properties we are
   *     adding stubs for (e.g. "a$b$c")
   * @param parent The node to which new global variables should be added
   *     as children
   * @param addAfter The child of after which new
   *     variables should be added (may be null)
   * @return The number of variables added
   */
  private int addStubsForUndeclaredProperties(
      Name n, String alias, Node parent, Node addAfter) {
    Preconditions.checkArgument(NodeUtil.isStatementBlock(parent));
    Preconditions.checkNotNull(addAfter);
    int numStubs = 0;
    if (n.props != null) {
      for (Name p : n.props) {
        if (p.needsToBeStubbed()) {
          String propAlias = appendPropForAlias(alias, p.name);
          Node nameNode = Node.newString(Token.NAME, propAlias);
          Node newVar = new Node(Token.VAR, nameNode)
              .copyInformationFromForTree(addAfter);
          parent.addChildAfter(newVar, addAfter);
          addAfter = newVar;
          numStubs++;
          compiler.reportCodeChange();

          // Determine if this is a constant var by checking the first
          // reference to it. Don't check the declaration, as it might be null.
          if (p.refs.get(0).node.getLastChild().getBooleanProp(
                Node.IS_CONSTANT_NAME)) {
            nameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
          }
        }
      }
    }
    return numStubs;
  }

  private static String appendPropForAlias(String root, String prop) {
    if (prop.indexOf('$') != -1) {
      // Encode '$' in a property as '$0'. Because '0' cannot be the
      // start of an identifier, this will never conflict with our
      // encoding from '.' -> '$'.
      prop = prop.replaceAll("\\$", "\\$0");
    }
    return root + '$' + prop;
  }
}

```

## src/com/google/javascript/jscomp/GlobalNamespace.java

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
import com.google.common.base.Predicate;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Builds a global namespace of all the objects and their properties in
 * the global scope. Also builds an index of all the references to those names.
 *
 */
class GlobalNamespace {

  private AbstractCompiler compiler;
  private final Node root;
  private final Node externsRoot;
  private boolean inExterns;
  private Scope externsScope;
  private boolean generated = false;

  /** Global namespace tree */
  private List<Name> globalNames = new ArrayList<Name>();

  /** Maps names (e.g. "a.b.c") to nodes in the global namespace tree */
  private Map<String, Name> nameMap = new HashMap<String, Name>();

  /**
   * Creates an instance that may emit warnings when building the namespace.
   *
   * @param compiler The AbstractCompiler, for reporting code changes
   * @param root The root of the rest of the code to build a namespace for.
   */
  GlobalNamespace(AbstractCompiler compiler, Node root) {
    this(compiler, null, root);
  }

  /**
   * Creates an instance that may emit warnings when building the namespace.
   *
   * @param compiler The AbstractCompiler, for reporting code changes
   * @param externsRoot The root of the externs to build a namespace for. If
   *     this is null, externs and properties defined on extern types will not
   *     be included in the global namespace.  If non-null, it allows
   *     user-defined function on extern types to be included in the global
   *     namespace.  E.g. String.foo.
   * @param root The root of the rest of the code to build a namespace for.
   */
  GlobalNamespace(AbstractCompiler compiler, Node externsRoot, Node root) {
    this.compiler = compiler;
    this.externsRoot = externsRoot;
    this.root = root;
  }

  /**
   * Gets a list of the roots of the forest of the global names, where the
   * roots are the top-level names.
   */
  List<Name> getNameForest() {
    if (!generated) {
      process();
    }
    return globalNames;
  }

  /**
   * Gets an index of all the global names, indexed by full qualified name
   * (as in "a", "a.b.c", etc.).
   */
  Map<String, Name> getNameIndex() {
    if (!generated) {
      process();
    }
    return nameMap;
  }

  /**
   * If the client adds new nodes to the AST, scan these new nodes
   * to see if they've added any references to the global namespace.
   * @param scope The scope to scan.
   * @param newNodes New nodes to check.
   */
  void scanNewNodes(Scope scope, Set<Node> newNodes) {
    NodeTraversal t = new NodeTraversal(compiler,
        new BuildGlobalNamespace(new NodeFilter(newNodes)));
    t.traverseAtScope(scope);
  }

  /**
   * A filter that looks for qualified names that contain one of the nodes
   * in the given set.
   */
  private static class NodeFilter implements Predicate<Node> {
    private final Set<Node> newNodes;

    NodeFilter(Set<Node> newNodes) {
      this.newNodes = newNodes;
    }

    public boolean apply(Node n) {
      if (!n.isQualifiedName()) {
        return false;
      }

      Node current;
      for (current = n;
           current.getType() == Token.GETPROP;
           current = current.getFirstChild()) {
        if (newNodes.contains(current)) {
          return true;
        }
      }

      return current.getType() == Token.NAME && newNodes.contains(current);
    }
  }

  /**
   * Builds the namespace lazily.
   */
  private void process() {
    if (externsRoot != null) {
      inExterns = true;
      NodeTraversal.traverse(compiler, externsRoot, new BuildGlobalNamespace());
    }
    inExterns = false;

    NodeTraversal.traverse(compiler, root, new BuildGlobalNamespace());
    generated = true;
  }

  /**
   * Determines whether a name reference in a particular scope is a global name
   * reference.
   *
   * @param name A variable or property name (e.g. "a" or "a.b.c.d")
   * @param s The scope in which the name is referenced
   * @return Whether the name reference is a global name reference
   */
  private boolean isGlobalNameReference(String name, Scope s) {
    String topVarName = getTopVarName(name);
    return isGlobalVarReference(topVarName, s);
  }

  /**
   * Gets the top variable name from a possibly namespaced name.
   *
   * @param name A variable or qualified property name (e.g. "a" or "a.b.c.d")
   * @return The top variable name (e.g. "a")
   */
  private String getTopVarName(String name) {
    int firstDotIndex = name.indexOf('.');
    return firstDotIndex == -1 ? name : name.substring(0, firstDotIndex);
  }

  /**
   * Determines whether a variable name reference in a particular scope is a
   * global variable reference.
   *
   * @param name A variable name (e.g. "a")
   * @param s The scope in which the name is referenced
   * @return Whether the name reference is a global variable reference
   */
  private boolean isGlobalVarReference(String name, Scope s) {
    Scope.Var v = s.getVar(name);
    if (v == null && externsScope != null) {
      v = externsScope.getVar(name);
    }
    return v != null && !v.isLocal();
  }

  /**
   * Gets whether a scope is the global scope.
   *
   * @param s A scope
   * @return Whether the scope is the global scope
   */
  private boolean isGlobalScope(Scope s) {
    return s.getParent() == null;
  }

  // -------------------------------------------------------------------------

  /**
   * Builds a tree representation of the global namespace. Omits prototypes.
   */
  private class BuildGlobalNamespace extends AbstractPostOrderCallback {

    private final Predicate<Node> nodeFilter;

    BuildGlobalNamespace() {
      this(null);
    }

    /**
     * Builds a global namepsace, but only visits nodes that match the
     * given filter.
     */
    BuildGlobalNamespace(Predicate<Node> nodeFilter) {
      this.nodeFilter = nodeFilter;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (nodeFilter != null && !nodeFilter.apply(n)) {
        return;
      }

      // If we are traversing the externs, then we save a pointer to the scope
      // generated by them, so that we can do lookups in it later.
      if (externsRoot != null && n == externsRoot) {
        externsScope = t.getScope();
      }

      String name;
      boolean isSet = false;
      Name.Type type = Name.Type.OTHER;
      boolean isPropAssign = false;

      switch (n.getType()) {
        case Token.STRING:
          // This may be a key in an object literal declaration.
          name = null;
          if (parent != null && parent.getType() == Token.OBJECTLIT) {
            name = getNameForObjLitKey(n);
          }
          if (name == null) return;
          isSet = true;
          type = getValueType(n.getFirstChild());
          break;
        case Token.NAME:
          // This may be a variable get or set.
          if (parent != null) {
            switch (parent.getType()) {
              case Token.VAR:
                isSet = true;
                Node rvalue = n.getFirstChild();
                type = rvalue == null ? Name.Type.OTHER : getValueType(rvalue);
                break;
              case Token.ASSIGN:
                if (parent.getFirstChild() == n) {
                  isSet = true;
                  type = getValueType(n.getNext());
                }
                break;
              case Token.GETPROP:
                return;
              case Token.FUNCTION:
                Node gramps = parent.getParent();
                if (gramps == null ||
                    NodeUtil.isFunctionExpression(parent)) return;
                isSet = true;
                type = Name.Type.FUNCTION;
                break;
            }
          }
          name = n.getString();
          break;
        case Token.GETPROP:
          // This may be a namespaced name get or set.
          if (parent != null) {
            switch (parent.getType()) {
              case Token.ASSIGN:
                if (parent.getFirstChild() == n) {
                  isSet = true;
                  type = getValueType(n.getNext());
                  isPropAssign = true;
                }
                break;
              case Token.GETPROP:
                return;
            }
          }
          name = n.getQualifiedName();
          if (name == null) return;
          break;
        default:
          return;
      }

      // We are only interested in global names.
      Scope scope = t.getScope();
      if (!isGlobalNameReference(name, scope)) {
        return;
      }

      if (isSet) {
        if (isGlobalScope(scope)) {
          handleSetFromGlobal(t, n, parent, name, isPropAssign, type);
        } else {
          handleSetFromLocal(t, n, parent, name);
        }
      } else {
        handleGet(t, n, parent, name);
      }
    }

    /**
     * Gets the fully qualified name corresponding to an object literal key,
     * as long as it and its prefix property names are valid JavaScript
     * identifiers. The object literal may be nested inside of other object
     * literals.
     *
     * For example, if called with node {@code n} representing "z" in any of
     * the following expressions, the result would be "w.x.y.z":
     * <code> var w = {x: {y: {z: 0}}}; </code>
     * <code> w.x = {y: {z: 0}}; </code>
     * <code> w.x.y = {'a': 0, 'z': 0}; </code>
     *
     * @param n A child of an OBJLIT node
     * @return The global name, or null if {@code n} doesn't correspond to the
     *   key of an object literal that can be named
     */
    String getNameForObjLitKey(Node n) {
      Node parent = n.getParent();
      Preconditions.checkState(parent.getType() == Token.OBJECTLIT);

      Node gramps = parent.getParent();
      if (gramps == null) {
        return null;
      }

      Node greatGramps = gramps.getParent();
      String name;
      switch (gramps.getType()) {
        case Token.NAME:
          // VAR
          //   NAME (gramps)
          //     OBJLIT (parent)
          //       STRING (n)
          if (greatGramps == null ||
              greatGramps.getType() != Token.VAR) {
            return null;
          }
          name = gramps.getString();
          break;
        case Token.ASSIGN:
          // ASSIGN (gramps)
          //   NAME|GETPROP
          //   OBJLIT (parent)
          //     STRING (n)
          Node lvalue = gramps.getFirstChild();
          name = lvalue.getQualifiedName();
          break;
        case Token.STRING:
          // OBJLIT
          //   STRING (gramps)
          //     OBJLIT (parent)
          //       STRING (n)
          if (greatGramps != null &&
              greatGramps.getType() == Token.OBJECTLIT) {
            name = getNameForObjLitKey(gramps);
          } else {
            return null;
          }
          break;
        default:
          return null;
      }
      if (name != null) {
        String key = n.getString();
        if (TokenStream.isJSIdentifier(key)) {
          return name + '.' + key;
        }
      }
      return null;
    }

    /**
     * Gets the type of a value or simple expression.
     *
     * @param n An rvalue in an assignment or variable declaration (not null)
     * @return A {@link Name.Type}
     */
    Name.Type getValueType(Node n) {
      switch (n.getType()) {
        case Token.OBJECTLIT:
          return Name.Type.OBJECTLIT;
        case Token.FUNCTION:
          return Name.Type.FUNCTION;
        case Token.OR:
          // Recurse on the second value. If the first value were an object
          // literal or function, then the OR would be meaningless and the
          // second value would be dead code. Assume that if the second value
          // is an object literal or function, then the first value will also
          // evaluate to one when it doesn't evaluate to false.
          return getValueType(n.getLastChild());
        case Token.HOOK:
          // The same line of reasoning used for the OR case applies here.
          Node second = n.getFirstChild().getNext();
          Name.Type t = getValueType(second);
          if (t != Name.Type.OTHER) return t;
          Node third = second.getNext();
          return getValueType(third);
      }
      return Name.Type.OTHER;
    }

    /**
     * Updates our respresentation of the global namespace to reflect an
     * assignment to a global name in global scope.
     *
     * @param t The traversal
     * @param n The node currently being visited
     * @param parent {@code n}'s parent
     * @param name The global name (e.g. "a" or "a.b.c.d")
     * @param isPropAssign Whether this set corresponds to a property
     *     assignment of the form <code>a.b.c = ...;</code>
     * @param type The type of the value that the name is being assigned
     */
    void handleSetFromGlobal(NodeTraversal t, Node n, Node parent, String name,
                             boolean isPropAssign, Name.Type type) {
      if (maybeHandlePrototypePrefix(t, n, parent, name)) return;

      Name nameObj = getOrCreateName(name);
      nameObj.type = type;

      Ref set = new Ref(t, n, Ref.Type.SET_FROM_GLOBAL);
      nameObj.addRef(set);

      if (isNestedAssign(parent)) {
        // This assignment is both a set and a get that creates an alias.
        Ref get = new Ref(t, n, Ref.Type.ALIASING_GET);
        nameObj.addRef(get);
        Ref.markTwins(set, get);
      } else if (isConstructorOrEnumDeclaration(n, parent)) {
        // Names with a @constructor or @enum annotation are always collapsed
        nameObj.setIsClassOrEnum();
      }
    }

    /**
     * Determines whether a set operation is a constructor or enumeration
     * declaration. The set operation may either be an assignment to a name,
     * a variable declaration, or an object literal key mapping.
     *
     * @param n The node that represents the name being set
     * @param parent Parent node of {@code n} (an ASSIGN, VAR, or OBJLIT node)
     * @return Whether the set operation is either a constructor or enum
     *     declaration
     */
    private boolean isConstructorOrEnumDeclaration(Node n, Node parent) {
      // NOTE(nicksantos): This does not handle named constructors
      // function a() {}
      // For legacy reasons, we should not fix this, because we do not
      // know who's depending on the current behavior.

      JSDocInfo info;
      int valueNodeType;
      switch (parent.getType()) {
        case Token.ASSIGN:
          info = parent.getJSDocInfo();
          valueNodeType = n.getNext().getType();
          break;
        case Token.VAR:
          info = n.getJSDocInfo();
          if (info == null) {
            info = parent.getJSDocInfo();
          }
          Node valueNode = n.getFirstChild();
          valueNodeType = valueNode != null ? valueNode.getType() : Token.VOID;
          break;
        default:
          return false;
      }
      // Heed the annotations only if they're sensibly used.
      return info != null &&
             (info.isConstructor() && valueNodeType == Token.FUNCTION ||
              info.hasEnumParameterType() && valueNodeType == Token.OBJECTLIT);
    }

    /**
     * Updates our respresentation of the global namespace to reflect an
     * assignment to a global name in a local scope.
     *
     * @param t The traversal
     * @param n The node currently being visited
     * @param parent {@code n}'s parent
     * @param name The global name (e.g. "a" or "a.b.c.d")
     */
    void handleSetFromLocal(NodeTraversal t, Node n, Node parent,
                            String name) {
      if (maybeHandlePrototypePrefix(t, n, parent, name)) return;

      Name node = getOrCreateName(name);
      Ref set = new Ref(t, n, Ref.Type.SET_FROM_LOCAL);
      node.addRef(set);

      if (isNestedAssign(parent)) {
        // This assignment is both a set and a get that creates an alias.
        Ref get = new Ref(t, n, Ref.Type.ALIASING_GET);
        node.addRef(get);
        Ref.markTwins(set, get);
      }
    }

    /**
     * Updates our respresentation of the global namespace to reflect a read
     * of a global name.
     *
     * @param t The traversal
     * @param n The node currently being visited
     * @param parent {@code n}'s parent
     * @param name The global name (e.g. "a" or "a.b.c.d")
     */
    void handleGet(NodeTraversal t, Node n, Node parent, String name) {
      if (maybeHandlePrototypePrefix(t, n, parent, name)) return;

      Ref.Type type = Ref.Type.DIRECT_GET;
      if (parent != null) {
        switch (parent.getType()) {
          case Token.IF:
          case Token.TYPEOF:
          case Token.VOID:
          case Token.NOT:
          case Token.BITNOT:
          case Token.POS:
          case Token.NEG:
            break;
          case Token.CALL:
            type = n == parent.getFirstChild()
                   ? Ref.Type.CALL_GET
                   : Ref.Type.ALIASING_GET;
            break;
          case Token.NEW:
            type = n == parent.getFirstChild()
                   ? Ref.Type.DIRECT_GET
                   : Ref.Type.ALIASING_GET;
            break;
          case Token.OR:
          case Token.AND:
            // This node is x or y in (x||y) or (x&&y). We only know that an
            // alias is not getting created for this name if the result is used
            // in a boolean context or assigned to the same name
            // (e.g. var a = a || {}).
            type = determineGetTypeForHookOrBooleanExpr(t, parent, name);
            break;
          case Token.HOOK:
            if (n != parent.getFirstChild()) {
              // This node is y or z in (x?y:z). We only know that an alias is
              // not getting created for this name if the result is assigned to
              // the same name (e.g. var a = a ? a : {}).
              type = determineGetTypeForHookOrBooleanExpr(t, parent, name);
            }
            break;
          default:
            type = Ref.Type.ALIASING_GET;
            break;
        }
      }

      handleGet(t, n, parent, name, type);
    }

    /**
     * Determines whether the result of a hook (x?y:z) or boolean expression
     * (x||y) or (x&&y) is assigned to a specific global name.
     *
     * @param t The traversal
     * @param parent The parent of the current node in the traversal. This node
     *     should already be known to be a HOOK, AND, or OR node.
     * @param name A name that is already known to be global in the current
     *     scope (e.g. "a" or "a.b.c.d")
     * @return The expression's get type, either {@link Ref.Type#DIRECT_GET} or
     *     {@link Ref.Type#ALIASING_GET}
     */
    Ref.Type determineGetTypeForHookOrBooleanExpr(
        NodeTraversal t, Node parent, String name) {
      Node prev = parent;
      for (Node anc : parent.getAncestors()) {
        switch (anc.getType()) {
          case Token.EXPR_RESULT:
          case Token.VAR:
          case Token.IF:
          case Token.WHILE:
          case Token.FOR:
          case Token.TYPEOF:
          case Token.VOID:
          case Token.NOT:
          case Token.BITNOT:
          case Token.POS:
          case Token.NEG:
            return Ref.Type.DIRECT_GET;
          case Token.HOOK:
            if (anc.getFirstChild() == prev) {
              return Ref.Type.DIRECT_GET;
            }
            break;
          case Token.ASSIGN:
            if (!name.equals(anc.getFirstChild().getQualifiedName())) {
              return Ref.Type.ALIASING_GET;
            }
            break;
          case Token.NAME:  // a variable declaration
            if (!name.equals(anc.getString())) {
              return Ref.Type.ALIASING_GET;
            }
            break;
          case Token.CALL:
            if (anc.getFirstChild() != prev) {
              return Ref.Type.ALIASING_GET;
            }
            break;
        }
        prev = anc;
      }
      return Ref.Type.ALIASING_GET;
    }

    /**
     * Updates our respresentation of the global namespace to reflect a read
     * of a global name.
     *
     * @param t The current node traversal
     * @param n The node currently being visited
     * @param parent {@code n}'s parent
     * @param name The global name (e.g. "a" or "a.b.c.d")
     * @param type The reference type
     */
    void handleGet(NodeTraversal t, Node n, Node parent,
        String name, Ref.Type type) {
      Name node = getOrCreateName(name);

      // No need to look up additional ancestors, since they won't be used.
      node.addRef(new Ref(t, n, type));
    }

    /**
     * Updates our respresentation of the global namespace to reflect a read
     * of a global name's longest prefix before the "prototype" property if the
     * name includes the "prototype" property. Does nothing otherwise.
     *
     * @param t The current node traversal
     * @param n The node currently being visited
     * @param parent {@code n}'s parent
     * @param name The global name (e.g. "a" or "a.b.c.d")
     * @return Whether the name was handled
     */
    boolean maybeHandlePrototypePrefix(NodeTraversal t, Node n, Node parent,
        String name) {
      // We use a string-based approach instead of inspecting the parse tree
      // to avoid complexities with object literals, possibly nested, beneath
      // assignments.

      int numLevelsToRemove;
      String prefix;
      if (name.endsWith(".prototype")) {
        numLevelsToRemove = 1;
        prefix = name.substring(0, name.length() - 10);
      } else {
        int i = name.indexOf(".prototype.");
        if (i == -1) {
          return false;
        }
        prefix = name.substring(0, i);
        numLevelsToRemove = 2;
        i = name.indexOf('.', i + 11);
        while (i >= 0) {
          numLevelsToRemove++;
          i = name.indexOf('.', i + 1);
        }
      }

      if (parent != null && NodeUtil.isObjectLitKey(n, parent)) {
        // Object literal keys have no prefix that's referenced directly per
        // key, so we're done.
        return true;
      }

      for (int i = 0; i < numLevelsToRemove; i++) {
        parent = n;
        n = n.getFirstChild();
      }

      handleGet(t, n, parent, prefix, Ref.Type.PROTOTYPE_GET);
      return true;
    }

    /**
     * Determines whether an assignment is nested (i.e. whether its return
     * value is used).
     *
     * @param parent The parent of the current traversal node (not null)
     * @return Whether it appears that the return value of the assignment is
     *     used
     */
    boolean isNestedAssign(Node parent) {
      return parent.getType() == Token.ASSIGN &&
             !NodeUtil.isExpressionNode(parent.getParent());
    }

    /**
     * Gets a {@link Name} instance for a global name. Creates it if necessary,
     * as well as instances for any of its prefixes that are not yet defined.
     *
     * @param name A global name (e.g. "a", "a.b.c.d")
     * @return The {@link Name} instance for {@code name}
     */
    Name getOrCreateName(String name) {
      Name node = nameMap.get(name);
      if (node == null) {
        int i = name.lastIndexOf('.');
        if (i >= 0) {
          String parentName = name.substring(0, i);
          Name parent = getOrCreateName(parentName);
          node = parent.addProperty(name.substring(i + 1), inExterns);
        } else {
          node = new Name(name, null, inExterns);
          globalNames.add(node);
        }
        nameMap.put(name, node);
      }
      return node;
    }
  }

  // -------------------------------------------------------------------------

  /**
   * A name defined in global scope (e.g. "a" or "a.b.c.d"). These form a tree.
   * As the parse tree traversal proceeds, we'll discover that some names
   * correspond to JavaScript objects whose properties we should consider
   * collapsing.
   */
  static class Name {
    enum Type {
      OBJECTLIT,
      FUNCTION,
      OTHER,
    }

    final String name;
    final Name parent;
    List<Name> props;
    Ref declaration;
    List<Ref> refs;
    Type type;
    private boolean isClassOrEnum = false;
    private boolean hasClassOrEnumDescendant = false;
    int globalSets = 0;
    int localSets = 0;
    int aliasingGets = 0;
    int totalGets = 0;
    int callGets = 0;
    boolean inExterns;

    JSDocInfo docInfo = null;

    Name(String name, Name parent, boolean inExterns) {
      this.name = name;
      this.parent = parent;
      this.type = Type.OTHER;
      this.inExterns = inExterns;
    }

    Name addProperty(String name, boolean inExterns) {
      if (props == null) {
        props = new ArrayList<Name>();
      }
      Name node = new Name(name, this, inExterns);
      props.add(node);
      return node;
    }

    void addRef(Ref ref) {
      switch (ref.type) {
        case SET_FROM_GLOBAL:
          if (declaration == null) {
            declaration = ref;
            docInfo = getDocInfoForDeclaration(ref);
          } else {
            addRefInternal(ref);
          }
          globalSets++;
          break;
        case SET_FROM_LOCAL:
          addRefInternal(ref);
          localSets++;
          break;
        case PROTOTYPE_GET:
        case DIRECT_GET:
          addRefInternal(ref);
          totalGets++;
          break;
        case ALIASING_GET:
          addRefInternal(ref);
          aliasingGets++;
          totalGets++;
          break;
        case CALL_GET:
          addRefInternal(ref);
          callGets++;
          totalGets++;
          break;
        default:
          throw new IllegalStateException();
      }
    }

    void removeRef(Ref ref) {
      if (ref == declaration ||
          (refs != null && refs.remove(ref))) {
        if (ref == declaration) {
          declaration = null;
          if (refs != null) {
            for (Ref maybeNewDecl : refs) {
              if (maybeNewDecl.type == Ref.Type.SET_FROM_GLOBAL) {
                declaration = maybeNewDecl;
                refs.remove(declaration);
                break;
              }
            }
          }
        }

        switch (ref.type) {
          case SET_FROM_GLOBAL:
            globalSets--;
            break;
          case SET_FROM_LOCAL:
            localSets--;
            break;
          case PROTOTYPE_GET:
          case DIRECT_GET:
            totalGets--;
            break;
          case ALIASING_GET:
            aliasingGets--;
            totalGets--;
            break;
          case CALL_GET:
            callGets--;
            totalGets--;
            break;
          default:
            throw new IllegalStateException();
        }
      }
    }

    void addRefInternal(Ref ref) {
      if (refs == null) {
        refs = new LinkedList<Ref>();
      }
      refs.add(ref);
    }

    boolean canEliminate() {
      if (!canCollapseUnannotatedChildNames() || totalGets > 0) {
        return false;
      }

      if (props != null) {
        for (Name n : props) {
          if (!n.canCollapse()) {
            return false;
          }
        }
      }
      return true;
    }

    boolean canCollapse() {
      return !inExterns && (isClassOrEnum ||
          (parent == null || parent.canCollapseUnannotatedChildNames()) &&
          (globalSets > 0 || localSets > 0));
    }

    boolean canCollapseUnannotatedChildNames() {
      if (type == Type.OTHER || globalSets != 1 || localSets != 0) {
        return false;
      }

      // Don't try to collapse if the one global set is a twin reference.
      // We could theoretically handle this case in CollapseProperties, but
      // it's probably not worth the effort.
      Preconditions.checkNotNull(declaration);
      if (declaration.getTwin() != null) {
        return false;
      }

      if (isClassOrEnum) {
        return true;
      }

      // If this is a key of an aliased object literal, then it will be aliased
      // later. So we won't be able to collapse its properties.
      if (parent != null && parent.shouldKeepKeys()) {
        return false;
      }

      // If this is aliased, then its properties can't be collapsed either.
      if (aliasingGets > 0) {
        return false;
      }

      return (parent == null || parent.canCollapseUnannotatedChildNames());
    }

    /** Whether this is an object literal that needs to keep its keys. */
    boolean shouldKeepKeys() {
      return type == Type.OBJECTLIT && aliasingGets > 0;
    }

    boolean needsToBeStubbed() {
      return globalSets == 0 && localSets > 0;
    }

    void setIsClassOrEnum() {
      isClassOrEnum = true;
      for (Name ancestor = parent; ancestor != null;
           ancestor = ancestor.parent) {
        ancestor.hasClassOrEnumDescendant = true;
      }
    }

    /**
     * Determines whether this name is a prefix of at least one class or enum
     * name. Because classes and enums are always collapsed, the namespace will
     * have different properties in compiled code than in uncompiled code.
     *
     * For example, if foo.bar.DomHelper is a class, then foo and foo.bar are
     * considered namespaces.
     */
    boolean isNamespace() {
      return hasClassOrEnumDescendant && type == Type.OBJECTLIT;
    }

    /**
     * Determines whether this is a simple name (as opposed to a qualified
     * name).
     */
    boolean isSimpleName() {
      return parent == null;
    }

    @Override public String toString() {
      return fullName() + " (" + type + "): globalSets=" + globalSets +
          ", localSets=" + localSets + ", totalGets=" + totalGets +
          ", aliasingGets=" + aliasingGets + ", callGets=" + callGets;
    }

    String fullName() {
      return parent == null ? name : parent.fullName() + '.' + name;
    }

    /**
     * Tries to get the doc info for a given declaration ref.
     */
    private static JSDocInfo getDocInfoForDeclaration(Ref ref) {
      if (ref.node != null) {
        Node refParent = ref.node.getParent();
        switch (refParent.getType()) {
          case Token.FUNCTION:
          case Token.ASSIGN:
            return refParent.getJSDocInfo();
          case Token.VAR:
            return ref.node == refParent.getFirstChild() ?
                refParent.getJSDocInfo() : ref.node.getJSDocInfo();
        }
      }

      return null;
    }
  }

  // -------------------------------------------------------------------------

  /**
   * A global name reference. Contains references to the relevant parse tree
   * node and its ancestors that may be affected.
   */
  static class Ref {
    enum Type {
      SET_FROM_GLOBAL,
      SET_FROM_LOCAL,
      PROTOTYPE_GET,
      ALIASING_GET,     // Prevents a name's properties from being collapsed
      DIRECT_GET,       // Prevents a name from being completely eliminated
      CALL_GET,         // Prevents a name from being collapsed if never set
    }

    Node node;
    final Type type;
    final String sourceName;
    final Scope scope;
    final JSModule module;

    /**
     * Certain types of references are actually double-refs. For example,
     * var a = b = 0;
     * counts as both a "set" of b and an "alias" of b.
     *
     * We create two Refs for this node, and mark them as twins of each other.
     */
    private Ref twin = null;

    /**
     * Creates a reference at the current node.
     */
    Ref(NodeTraversal t, Node name, Type type) {
      this.node = name;
      this.sourceName = t.getSourceName();
      this.type = type;
      this.scope = t.getScope();
      this.module = t.getModule();
    }

    private Ref(Ref original, Type type) {
      this.node = original.node;
      this.sourceName = original.sourceName;
      this.type = type;
      this.scope = original.scope;
      this.module = original.module;
    }

    private Ref(Type type) {
      this.type = type;
      this.sourceName = "source";
      this.scope = null;
      this.module = null;
    }

    Ref getTwin() {
      return twin;
    }

    boolean isSet() {
      return type == Type.SET_FROM_GLOBAL || type == Type.SET_FROM_LOCAL;
    }

    static void markTwins(Ref a, Ref b) {
      Preconditions.checkArgument(
          (a.type == Type.ALIASING_GET || b.type == Type.ALIASING_GET) &&
          (a.type == Type.SET_FROM_GLOBAL || a.type == Type.SET_FROM_LOCAL ||
           b.type == Type.SET_FROM_GLOBAL || b.type == Type.SET_FROM_LOCAL));
      a.twin = b;
      b.twin = a;
    }

    /**
     * Create a new ref that is the same as this one, but of
     * a different class.
     */
    Ref cloneAndReclassify(Type type) {
      return new Ref(this, type);
    }

    static Ref createRefForTesting(Type type) {
      return new Ref(type);
    }
  }
}

```
