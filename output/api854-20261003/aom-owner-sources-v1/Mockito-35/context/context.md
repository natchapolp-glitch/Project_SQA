## build.gradle

```
group = 'org.mockito'
version = 'SNAPSHOT'
archivesBaseName = 'mockito'

usePlugin('java')
  
sourceCompatibility=1.6
targetCompatibility=1.6
srcRootName = '.'
srcDirNames = ['src']
testSrcDirNames = ['test']

dependencies {
    addConfiguration('build')
    addFlatDirResolver("lib", "$rootDir/lib/run", "$rootDir/lib/ant", "$rootDir/lib/build")
    compile ':asm:3.1', ':cglib:2.2', ':hamcrest-core:1.1', ':junit:4.5', ':objenesis:1.0'
    testCompile ':junit:4.5'
    build ':jarjar:1.0rc7', ':pmd:4.1'
}

createTask('package', dependsOn:'libs')

createTask('jarjar', dependsOn: 'compile') {
    ant {
        taskdef(name: 'jarjar', classname: 'com.tonicsystems.jarjar.JarJarTask', classpath: dependencies.antpath('build'))
        
        jarjar(jarfile: "$buildDir/mockito-all.jar") {
            manifest() {
                attribute(name: 'Build-By', value: 'Someone')
                attribute(name: 'Implementation-Version', value: version)
            }
            fileset(dir: '.', file: 'LICENSE')
            fileset(dir: '.', file: 'NOTICE')
            fileset(dir: classesDir)
            fileset(dir: srcDirs[0])
            runDir = "$rootDir/lib/run"
            fileset(dir: runDir, excludes: "*.jar")
            zipfileset(src: "$runDir/asm-3.1.jar")
            zipfileset(src: "$runDir/objenesis-1.0.jar") {
                exclude(name: "META-INF/maven/**")
            }
            zipfileset(src: "$runDir/hamcrest-core-1.1.jar") {
                exclude(name: "LICENSE.txt")
            }
            zipfileset(src: "$runDir/cglib-2.2.jar") {
                exclude(name: "LICENSE") 
                exclude(name: "NOTICE")
            }
            rule(pattern: "net.sf.**", result: "org.mockito.@1")
            rule(pattern: "org.objectweb.**", result: "org.mockito.@1")
        }
    }
}

createTask('pmd') {
    println 'Running PMD static code analysis'
    ant {
        taskdef(name:'pmd', classname:'net.sourceforge.pmd.ant.PMDTask', classpath: dependencies.antpath('build'))

        pmd(shortFilenames:'true', failonruleviolation:'true', rulesetfiles:'conf/pmd-rules.xml') {
            ruleset() { 'Mockito' }
            formatter(type:'text', toConsole:'true')
            fileset(dir: srcDirs[0]) {
                include(name: '**/*.java')
            }
            fileset(dir: testSrcDirNames[0]) {
                include(name: '**/*.java')
            }
        }
    }
}    

createTask('release.javadoc', dependsOn: 'javadoc') {
    println 'Releasing javadoc html to the javadoc release folder'
    ant.copy(todir: 'javadoc') {
        fileset(dir: 'build/docs')
    }
}
 
libs.dependsOn jarjar
compile.dependsOn pmd
```

## build.xml

```
<!--
 Copyright (c) 2007 Mockito contributors 
 This program is made available under the terms of the MIT License.
-->
<project name="mockito" basedir="." default="all">

  <loadproperties srcfile="version.properties" />
    
  <property name="src.dir" value="src" />
  <property name="test.src.dir" value="test" />
  <property name="target.dir" value="target" />
  <property name="src.classes.dir" value="${target.dir}/classes" />
  <property name="test.classes.dir" value="${target.dir}/test-classes" />
  <property name="test.reports.dir" value="${target.dir}/reports/junit" />
	
  <property name="javadoc.dir" value="${target.dir}/javadoc" />

  <property name="lib.dir" value="lib" />
  <property name="lib.dir.build" value="${lib.dir}/build" />
  <property name="lib.dir.run" value="${lib.dir}/run" />
  <property name="lib.dir.compile" value="${lib.dir}/compile" />
  <property name="lib.dir.test" value="${lib.dir}/test" />
  <property name="lib.dir.repackaged" value="${lib.dir}/repackaged" />
  <property name="lib.dir.sources" value="${lib.dir}/sources" />
    
  <property name="jar.core" value="mockito-core-${version}.jar" />
  <property name="jar.core.sources" value="mockito-core-${version}-sources.jar" />
	
  <property name="jar.all" value="mockito-all-${version}.jar" />
  <property name="jar.all.sources" value="mockito-all-${version}-sources.jar" />
	
  <property name="jar.core.osgified" value="${jar.core}-osgified" />
  <property name="jar.all.osgified" value="${jar.all}-osgified" />
	
  <property name="jar.tests" value="mockito-tests.jar" />
  <property name="zip" value="mockito-${version}.zip" />
	
  <property name="maven.repository.dir" value="${basedir}/maven/repository" />
  
  <path id="compile.classpath">
    <fileset dir="${lib.dir.run}" includes="*.jar" />
    <fileset dir="${lib.dir.compile}" includes="*.jar" />
    <fileset dir="${lib.dir.repackaged}" includes="*.jar" />
  </path>

  <path id="test.compile.classpath">
  	<fileset dir="${lib.dir.test}" includes="*.jar" />
    <path refid="compile.classpath" />
    <pathelement location="${src.classes.dir}" />
  </path>

  <path id="test.classpath">
    <path refid="test.compile.classpath" />
    <pathelement location="${test.classes.dir}" />
  </path>

  <path id="ant.classpath">
    <fileset dir="${lib.dir.build}" includes="*.jar" />
  </path>

  <target name="prepare">
    <mkdir dir="${src.classes.dir}" />
    <mkdir dir="${test.classes.dir}" />
    <mkdir dir="${test.reports.dir}" />
  </target>

  <target name="clean" description="Cleans out the build directories">
    <delete includeemptydirs="true" quiet="true">
      <fileset dir="${target.dir}">
        <exclude name="eclipse-*/**" />
      </fileset>
    </delete>
  </target>

  <target name="compile" depends="clean, prepare">
    <javac srcdir="src" destdir="${src.classes.dir}" source="1.6" target="1.6" debug="true" deprecation="true">
      <classpath refid="compile.classpath" />
    </javac>
  </target>

  <target name="compile.test" depends="compile">
    <javac srcdir="test" destdir="${test.classes.dir}" source="1.6" target="1.6" debug="true" deprecation="true">
      <classpath refid="test.compile.classpath" />
    </javac>
  </target>

  <target name="test" depends="compile.test, pmd" description="Executes the unit tests">
    <junit fork="true" forkmode="once" printsummary="on" haltonfailure="false" failureproperty="mockito.tests.failed">
      <formatter type="xml"/>
      <classpath refid="test.classpath" />
      <jvmarg value="-ea" />

      <batchtest todir="${test.reports.dir}">
        <fileset dir="${test.classes.dir}" includes="**/*Test.class" />
      </batchtest>
    </junit>
    
    <fail if="mockito.tests.failed" message="Tests failed."/>
  </target>

  <target name="pmd" depends="prepare">

    <taskdef name="pmd" classname="net.sourceforge.pmd.ant.PMDTask" classpathref="ant.classpath" />

    <pmd shortFilenames="true" failonruleviolation="true" rulesetfiles="conf/pmd-rules.xml">
      <ruleset>Mockito</ruleset>
      <formatter type="text" toConsole="true" />

      <fileset dir="${src.dir}">
        <include name="**/*.java" />
      </fileset>

      <fileset dir="${test.src.dir}">
        <include name="**/*.java" />
      </fileset>
    </pmd>

  </target>

  <target name="jar.all" depends="test">
    <taskdef name="jarjar" classname="com.tonicsystems.jarjar.JarJarTask" classpathref="ant.classpath" />
    <jarjar jarfile="${target.dir}/${jar.all}">
        <manifest>
          <attribute name="Built-By" value="${user.name}"/>
          <attribute name="Implementation-Version" value="${version}"/>
        </manifest>
      <fileset dir="." file="LICENSE" />
      <fileset dir="." file="NOTICE" />
      <fileset dir="${src.classes.dir}" />
      <zipfileset dir="${src.dir}" />
	  <fileset dir="${lib.dir.run}" excludes="*.jar" />
      <fileset dir="${lib.dir.repackaged}" excludes="*.jar" />
      <zipfileset src="${lib.dir.run}/com.springsource.org.objenesis-1.0.0.jar">
        <exclude name="META-INF/maven/**"/>
      </zipfileset>
      <zipfileset src="${lib.dir.run}/com.springsource.org.hamcrest.core-1.1.0.jar">
        <exclude name="LICENSE.txt" />
      </zipfileset>
      <zipfileset src="${lib.dir.repackaged}/cglib-and-asm-1.0.jar" />
    </jarjar>
  </target>
	
<taskdef resource="aQute/bnd/ant/taskdef.properties" classpathref="ant.classpath"/> 
  <target name="bnd-core" depends="jar" > 
    <bnd
         classpath="${target.dir}/${jar.core}"
         eclipse="false"
         failok="false"
         exceptions="true"
         sourcepath="${src.dir}"
         destfile="${target.dir}/${jar.core.osgified}"
         files="conf/mockito-core.bnd"/>
  </target>
	
  <target name="bnd-all" depends="jar" > 
    <bnd
         classpath="${target.dir}/${jar.all}"
         eclipse="false"
         failok="false"
         exceptions="true"
         sourcepath="${src.dir}"
         destfile="${target.dir}/${jar.all.osgified}"
         files="conf/mockito-all.bnd"/>
  </target>	
	
    <target name="osgify.manifests" depends="bnd-core, bnd-all"> 
        <zip update="true" destfile="${target.dir}/${jar.core}" >
          <zipfileset src="${target.dir}/${jar.core.osgified}" >
            <include name="META-INF/MANIFEST.MF"/>
          </zipfileset>
    	</zip>
        <zip update="true" destfile="${target.dir}/${jar.all}">
          <zipfileset src="${target.dir}/${jar.all.osgified}" >
            <include name="META-INF/MANIFEST.MF"/>
          </zipfileset>
    	</zip>
        <zip update="true" destfile="${target.dir}/${jar.core.sources}">
          <zipfileset src="${target.dir}/${jar.core.osgified}" >
            <include name="META-INF/MANIFEST.MF"/>
          </zipfileset>
    	</zip>
        <zip update="true" destfile="${target.dir}/${jar.all.sources}">
          <zipfileset src="${target.dir}/${jar.all.osgified}" >
            <include name="META-INF/MANIFEST.MF"/>
          </zipfileset>
    	</zip>
    	<delete file="${target.dir}/${jar.all.osgified}" />
    	<delete file="${target.dir}/${jar.core.osgified}" />
  </target>
	
  <target name="jar" depends="jar.all">
    <jarjar jarfile="${target.dir}/${jar.core}">
      <manifest>
        <attribute name="Built-By" value="${user.name}"/>
        <attribute name="Implementation-Version" value="${version}"/>
      </manifest>
      <fileset dir="${lib.dir.repackaged}" includes="*.txt" />
      <fileset dir="." file="LICENSE" />
      <fileset dir="." file="NOTICE" />
      <fileset dir="${src.classes.dir}" />
      <fileset dir="${src.dir}" />
      <zipfileset src="${lib.dir.repackaged}/cglib-and-asm-1.0.jar"/>
    </jarjar>
  </target>
	
  <target name="sources" >
    <jar jarfile="${target.dir}/${jar.core.sources}">
        <manifest>
          <attribute name="Built-By" value="${user.name}"/>
          <attribute name="Implementation-Version" value="${version}"/>
        </manifest>
      <fileset dir="." file="LICENSE" />
      <fileset dir="." file="NOTICE" />
      <fileset dir="${src.dir}" />
      <zipfileset src="${lib.dir.sources}/cglib-and-asm-1.0-sources.jar" />
    </jar>
  </target>
	
  <target name="sources-all" depends="sources" >
    <jar jarfile="${target.dir}/${jar.all.sources}">
        <manifest>
          <attribute name="Built-By" value="${user.name}"/>
          <attribute name="Implementation-Version" value="${version}"/>
        </manifest>
      <fileset dir="." file="LICENSE" />
      <fileset dir="." file="NOTICE" />
        <fileset dir="${lib.dir.run}" >
          <include name="hamcrest-license*"/>
          <include name="objenesis-license*"/>
        </fileset>
      <fileset dir="${src.dir}" />
      <zipfileset src="${lib.dir.sources}/cglib-and-asm-1.0-sources.jar" />
      <zipfileset src="${lib.dir.sources}/com.springsource.org.hamcrest.core-1.1.0-sources.jar" >
      	<exclude name="LICENSE.txt" />
      </zipfileset>
      <zipfileset src="${lib.dir.sources}/com.springsource.org.objenesis-1.0.0-sources.jar" />
    </jar>
  </target>
	
  <target name="prepare.package" depends="jar, javadoc, sources-all, osgify.manifests">
    <mkdir dir="${target.dir}/mockito.zip.tmp/separate-jars" />
    <copy todir="${target.dir}/mockito.zip.tmp/separate-jars">
      <fileset dir="${lib.dir.run}" />
      <fileset dir="${lib.dir.repackaged}" includes="*.txt" />
      <fileset dir="${target.dir}" includes="${jar.core}" />
    </copy>
    <mkdir dir="${target.dir}/mockito.zip.tmp/javadoc" />
    <copy todir="${target.dir}/mockito.zip.tmp/javadoc">
      <fileset dir="${javadoc.dir}" />
    </copy>
    <copy todir="${target.dir}/mockito.zip.tmp">
      <fileset dir="." includes="LICENSE" />
      <fileset dir="." includes="NOTICE" />
      <fileset dir="doc" includes="jars-info.txt" />
      <fileset dir="${target.dir}" includes="${jar.all}" />
    </copy>
  	<mkdir dir="${target.dir}/mockito.zip.tmp/sources" />
    <copy todir="${target.dir}/mockito.zip.tmp/sources">
      <fileset dir="${target.dir}" includes="*-sources.jar" />
    </copy>
  </target>

  <target name="zip" depends="prepare.package">
    <zip destfile="${target.dir}/${zip}">
      <fileset dir="${target.dir}/mockito.zip.tmp"/>
    </zip>
  </target>

  <target name="javadoc">
    <javadoc sourcepath="${src.dir}" destdir="${javadoc.dir}" 
      author="true" version="true" use="true" windowtitle="Mockito API">
      <doctitle>
        <![CDATA[
          <h1><a href="org/mockito/Mockito.html">Click to see examples</a>. Mockito API.</h1>
        ]]>
      </doctitle>
      <group title="Main package" packages="org.mockito"/>
      <classpath refid="compile.classpath" />
    </javadoc>
  	<copy todir="${javadoc.dir}/org/mockito" file="img/logo.jpg" />
  </target>

  <target name="repackage-tests">
    <jarjar jarfile="${target.dir}/${jar.tests}">
      <fileset dir="${test.classes.dir}"/>
      <rule pattern="net.sf.**" result="org.mockito.@1" />
      <rule pattern="org.objectweb.**" result="org.mockito.@1" />
    </jarjar>
  </target>  
  
  <target name="test.zip.release" depends="zip, repackage-tests" >
    <delete dir="${target.dir}/tmp"/>
    <unzip dest="${target.dir}/tmp" src="${target.dir}/${zip}" />
      <junit fork="true" forkmode="once" haltonfailure="true">
        <formatter type="plain" usefile="false"/>      	
        <classpath>
          <fileset dir="${target.dir}/tmp" includes="${jar.all}" />
          <fileset dir="${lib.dir.test}" includes="*.jar" />
          <fileset dir="${lib.dir.compile}" includes="*junit*.jar" />
          <pathelement location="${target.dir}/${jar.tests}"/>
        </classpath>
        <jvmarg value="-ea" />
        <batchtest>
          <fileset dir="${test.classes.dir}" includes="**/*Test.class" />
        </batchtest>
      </junit>
  </target>
  
  <target name="test.zip.release.separate.jars" depends="zip, repackage-tests" >
    <delete dir="${target.dir}/tmp"/>
    <unzip dest="${target.dir}/tmp" src="${target.dir}/${zip}" />
    <junit fork="true" forkmode="once" haltonfailure="true">
      <formatter type="plain" usefile="false" />
      <classpath>
        <fileset dir="${target.dir}/tmp/separate-jars" includes="*.jar" />
      	<fileset dir="${lib.dir.test}" includes="*.jar" />
      	<fileset dir="${lib.dir.compile}" includes="*junit*.jar" />
        <pathelement location="${target.dir}/${jar.tests}"/>
      </classpath>
      <jvmarg value="-ea" />

      <batchtest>
        <fileset dir="${test.classes.dir}" includes="**/*Test.class" />
      </batchtest>
    </junit>
  </target>
  
  <target name="release.javadoc" depends="javadoc" description="releases javadoc to 'javadoc' folder" >
    <copy todir="javadoc">
      <fileset dir="${target.dir}/javadoc" />
    </copy>
  </target>
  
  <target name="check.binaries">
    <condition property="mockito.jars.exist">
      <and>
        <available file="${target.dir}/${jar.all}"/>
        <available file="${target.dir}/${jar.all.sources}"/>
        <available file="${target.dir}/${jar.core}"/>
        <available file="${target.dir}/${jar.core.sources}"/>
      </and>
    </condition>

    <fail unless="mockito.jars.exist" message="Mockito binaries don't exist. Issue ant at the command line to build."/>     
  </target>

  <target name="copy.maven.metadata" depends="check.binaries" >
  	<delete dir="${maven.repository.dir}"/>
    <mkdir dir="${maven.repository.dir}/org/mockito/mockito-all" />
    <copy file="maven/mockito-all-metadata.xml" tofile="${maven.repository.dir}/org/mockito/mockito-all/maven-metadata.xml" />
   
    <mkdir dir="${maven.repository.dir}/org/mockito/mockito-core" />
    <copy file="maven/mockito-core-metadata.xml" tofile="${maven.repository.dir}/org/mockito/mockito-core/maven-metadata.xml" />
  </target>

  <taskdef resource="org/apache/maven/artifact/ant/antlib.xml" classpathref="ant.classpath"/>

    <target name="prepare.poms" >
        <filter token="version" value="${version}"/>
        <copy todir="${target.dir}" filtering="true">
            <fileset dir="maven" includes="mockito-*.pom"/>
        </copy>
    </target>

    <target  name="install.artifact" depends="check.binaries, prepare.poms" description="Installs maven artifact in local repo. Helpful for testing">
      <install file="${target.dir}/${jar.all}">
        <pom file="${target.dir}/mockito-all.pom" />
    </install>
  </target>

  <target name="release.maven" depends="copy.maven.metadata, prepare.poms" description="deploy binaries to sync with maven repository" > 	
    <remoteRepository id="repo.local" url="file:///${maven.repository.dir}" />
  	
    <deploy file="${target.dir}/${jar.all}">
      <pom file="${target.dir}/mockito-all.pom" />
      <attach file="${target.dir}/${jar.all.sources}" classifier="sources" />
      <remoterepository refid="repo.local" />
    </deploy>
  	
    <deploy file="${target.dir}/${jar.core}" >
      <pom file="${target.dir}/mockito-core.pom" />
      <attach file="${target.dir}/${jar.core.sources}" classifier="sources" />
      <remoterepository refid="repo.local" />
    </deploy>
  </target>
  
  <target name="test.release" depends="test.zip.release, test.zip.release.separate.jars" description="builds zip, unzips it and runs tests against jars" />
  
  <target name="all" depends="zip" description="cleans, builds, tests, jars, zips, bundles maven" />
  
  <!-- to make Hudson happy -->
  <target name="build" depends="test"/>

    <!-- yet another test of bzr -->
    <!-- yet another test of bzr -->
    <!-- yet another test of bzr -->

</project>
```

## src/org/mockito/Matchers.java

```
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.*;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

/**
 * Allow flexible verification or stubbing. See also {@link AdditionalMatchers}.
 * <p>
 * {@link Mockito} extends Matchers so to get access to all matchers just import Mockito class statically.
 * <pre>
 *  //stubbing using anyInt() argument matcher
 *  when(mockedList.get(anyInt())).thenReturn("element");
 *  
 *  //following prints "element"
 *  System.out.println(mockedList.get(999));
 *  
 *  //you can also verify using argument matcher
 *  verify(mockedList).get(anyInt());
 * </pre>
 * Scroll down to see all methods - full list of matchers.
 * <p>
 * <b>Warning:</b>
 * <p>
 * If you are using argument matchers, <b>all arguments</b> have to be provided by matchers.
 * <p>
 * E.g: (example shows verification but the same applies to stubbing):
 * <pre>
 *   verify(mock).someMethod(anyInt(), anyString(), <b>eq("third argument")</b>);
 *   //above is correct - eq() is also an argument matcher
 *   
 *   verify(mock).someMethod(anyInt(), anyString(), <b>"third argument"</b>);
 *   //above is incorrect - exception will be thrown because third argument is given without argument matcher.
 * </pre>
 * 
 * <h1>Custom Argument Matchers</h1>
 * 
 * Use {@link Matchers#argThat} method and pass an instance of hamcrest {@link Matcher}.
 * <p>
 * Before you start implementing your own custom argument matcher, make sure you check out {@link ArgumentCaptor} api.
 * <p>
 * So, how to implement your own argument matcher?
 * First, you might want to subclass {@link ArgumentMatcher} which is an hamcrest matcher with predefined describeTo() method.
 * Default description generated by describeTo() uses <b>decamelized class name</b> - to promote meaningful class names.
 * <p>
 * Example:
 * 
 * <pre>
 *   class IsListOfTwoElements extends ArgumentMatcher&lt;List&gt; {
 *      public boolean matches(Object list) {
 *          return ((List) list).size() == 2;
 *      }
 *   }
 *   
 *   List mock = mock(List.class);
 *   
 *   when(mock.addAll(argThat(new IsListOfTwoElements()))).thenReturn(true);
 *   
 *   mock.addAll(Arrays.asList("one", "two"));
 *   
 *   verify(mock).addAll(argThat(new IsListOfTwoElements()));
 * </pre>
 * 
 * To keep it readable you may want to extract method, e.g:
 * <pre>
 *   verify(mock).addAll(<b>argThat(new IsListOfTwoElements())</b>);
 *   //becomes
 *   verify(mock).addAll(<b>listOfTwoElements()</b>);
 * </pre>
 *
 * <b>Warning:</b> Be reasonable with using complicated argument matching, especially custom argument matchers, as it can make the test less readable. 
 * Sometimes it's better to implement equals() for arguments that are passed to mocks 
 * (Mockito naturally uses equals() for argument matching). 
 * This can make the test cleaner. 
 * <p>
 * Also, <b>sometimes {@link ArgumentCaptor} may be a better fit</b> than custom matcher. 
 * For example, if custom argument matcher is not likely to be reused
 * or you just need it to assert on argument values to complete verification of behavior.
 */
@SuppressWarnings("unchecked")
public class Matchers {
    
    private static MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    /**
     * any boolean, Boolean or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>false</code>.
     */
    public static boolean anyBoolean() {
        return reportMatcher(Any.ANY).returnFalse();
    }

    /**
     * any byte, Byte or null
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static byte anyByte() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any char, Character or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static char anyChar() {
        return reportMatcher(Any.ANY).returnChar();
    }

    /**
     * any int, Integer or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static int anyInt() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any long, Long or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static long anyLong() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any float, Float or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static float anyFloat() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any double, Double or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static double anyDouble() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any short, Short or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>0</code>.
     */
    public static short anyShort() {
        return reportMatcher(Any.ANY).returnZero();
    }

    /**
     * any Object or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static <T> T anyObject() {
        return (T) reportMatcher(Any.ANY).returnNull();
    }
    //TODO: after 1.8 check out Jay Fields' idea on any() matcher

    /**
     * Any vararg, meaning any number and values of arguments.
     * <p>
     * Example:
     * <pre>
     *   //verification:
     *   mock.foo(1, 2);
     *   mock.foo(1, 2, 3, 4);
     *
     *   verify(mock, times(2)).foo(anyVararg());
     *
     *   //stubbing:
     *   when(mock.foo(anyVararg()).thenReturn(100);
     *
     *   //prints 100
     *   System.out.println(mock.foo(1, 2));
     *   //also prints 100
     *   System.out.println(mock.foo(1, 2, 3, 4));
     * </pre>
     * See examples in javadoc for {@link Matchers} class
     *
     * @return <code>null</code>.
     */
    public static <T> T anyVararg() {
        return (T) reportMatcher(AnyVararg.ANY_VARARG).returnNull();
    }
    
    /**
     * any kind object, not necessary of the given class.
     * The class argument is provided only to avoid casting.
     * <p>
     * Sometimes looks better than anyObject() - especially when explicit casting is required
     * <p>
     * Alias to {@link Matchers#anyObject()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static <T> T any(Class<T> clazz) {
        return (T) anyObject();
    }
    
    /**
     * any object or null 
     * <p>
     * Shorter alias to {@link Matchers#anyObject()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static <T> T any() {
        return (T) anyObject();
    }

    /**
     * any String or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty String ("")
     */
    public static String anyString() {
        return reportMatcher(Any.ANY).returnString();
    }
    
    /**
     * any List or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty List.
     */
    public static List anyList() {
        return reportMatcher(Any.ANY).returnList();
    }    
    
    /**
     * generic friendly alias to {@link Matchers#anyList()}.
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.
     * <p>
     * any List or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty List.
     */
    public static <T> List<T> anyListOf(Class<T> clazz) {
        return (List) reportMatcher(Any.ANY).returnList();
    }    
    
    /**
     * any Set or null
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @return empty Set
     */
    public static Set anySet() {
        return reportMatcher(Any.ANY).returnSet();
    }
    
    /**
     * generic friendly alias to {@link Matchers#anySet()}.
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.
     * <p>
     * any Set or null
     * <p>
     * See examples in javadoc for {@link Matchers} class
     *
     * @return empty Set
     */
    public static <T> Set<T> anySetOf(Class<T> clazz) {
        return (Set) reportMatcher(Any.ANY).returnSet();
    }

    /**
     * any Map or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty Map.
     */
    public static Map anyMap() {
        return reportMatcher(Any.ANY).returnMap();
    }    
    
    /**
     * any Collection or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty Collection.
     */
    public static Collection anyCollection() {
        return reportMatcher(Any.ANY).returnList();
    }    
    
    /**
     * generic friendly alias to {@link Matchers#anyCollection()}. 
     * It's an alternative to &#064;SuppressWarnings("unchecked") to keep code clean of compiler warnings.     
     * <p>
     * any Collection or null.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return empty Collection.
     */
    public static <T> Collection<T> anyCollectionOf(Class<T> clazz) {
        return (Collection) reportMatcher(Any.ANY).returnList();
    }    

    /**
     * Object argument that implements the given class. 
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param <T>
     *            the accepted type.
     * @param clazz
     *            the class of the accepted type.
     * @return <code>null</code>.
     */
    public static <T> T isA(Class<T> clazz) {
        return reportMatcher(new InstanceOf(clazz)).<T>returnFor(clazz);
    }

    /**
     * boolean argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static boolean eq(boolean value) {
        return reportMatcher(new Equals(value)).returnFalse();
    }

    /**
     * byte argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte eq(byte value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * char argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static char eq(char value) {
        return reportMatcher(new Equals(value)).returnChar();
    }

    /**
     * double argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double eq(double value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * float argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float eq(float value) {
        return reportMatcher(new Equals(value)).returnZero();
    }
    
    /**
     * int argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int eq(int value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * long argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long eq(long value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * short argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short eq(short value) {
        return reportMatcher(new Equals(value)).returnZero();
    }

    /**
     * Object argument that is equal to the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T eq(T value) {
        return (T) reportMatcher(new Equals(value)).<T>returnFor((Class) value.getClass());
    }  

    /**
     * Object argument that is reflection-equal to the given value with support for excluding
     * selected fields from a class.
     * <p>
     * This matcher can be used when equals() is not implemented on compared objects.
     * Matcher uses java reflection API to compare fields of wanted and actual object.
     * <p>
     * Works similarly to EqualsBuilder.reflectionEquals(this, other, exlucdeFields) from
     * apache commons library.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param value
     *            the given value.
     * @param excludeFields
     *            fields to exclude, if field does not exist it is ignored.
     * @return <code>null</code>.
     */
    public static <T> T refEq(T value, String... excludeFields) {
        return reportMatcher(new ReflectionEquals(value, excludeFields)).<T>returnNull();
    }
    
    /**
     * Object argument that is the same as the given value.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T> T same(T value) {
        return (T) reportMatcher(new Same(value)).<T>returnFor((Class) value.getClass());
    }

    /**
     * null argument.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object isNull() {
        return reportMatcher(Null.NULL).returnNull();
    }

    /**
     * not null argument.
     * <p>
     * alias to {@link Matchers#isNotNull()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object notNull() {
        return reportMatcher(NotNull.NOT_NULL).returnNull();
    }
    
    /**
     * not null argument.
     * <p>
     * alias to {@link Matchers#notNull()}
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @return <code>null</code>.
     */
    public static Object isNotNull() {
        return notNull();
    }

    /**
     * String argument that contains the given substring.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param substring
     *            the substring.
     * @return empty String ("").
     */
    public static String contains(String substring) {
        return reportMatcher(new Contains(substring)).returnString();
    }

    /**
     * String argument that matches the given regular expression.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param regex
     *            the regular expression.
     * @return empty String ("").
     */
    public static String matches(String regex) {
        return reportMatcher(new Matches(regex)).returnString();
    }

    /**
     * String argument that ends with the given suffix.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param suffix
     *            the suffix.
     * @return empty String ("").
     */
    public static String endsWith(String suffix) {
        return reportMatcher(new EndsWith(suffix)).returnString();
    }

    /**
     * String argument that starts with the given prefix.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param prefix
     *            the prefix.
     * @return empty String ("").
     */
    public static String startsWith(String prefix) {
        return reportMatcher(new StartsWith(prefix)).returnString();
    }

    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link ArgumentMatcher} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>null</code>.
     */
    public static <T> T argThat(Matcher<T> matcher) {
        return reportMatcher(matcher).<T>returnNull();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static char charThat(Matcher<Character> matcher) {
        return reportMatcher(matcher).returnChar();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>false</code>.
     */
    public static boolean booleanThat(Matcher<Boolean> matcher) {
        return reportMatcher(matcher).returnFalse();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static byte byteThat(Matcher<Byte> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static short shortThat(Matcher<Short> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static int intThat(Matcher<Integer> matcher) {
        return reportMatcher(matcher).returnZero();
    }

    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static long longThat(Matcher<Long> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static float floatThat(Matcher<Float> matcher) {
        return reportMatcher(matcher).returnZero();
    }
    
    /**
     * Allows creating custom argument matchers.
     * <p>
     * See examples in javadoc for {@link Matchers} class
     * 
     * @param matcher decides whether argument matches
     * @return <code>0</code>.
     */
    public static double doubleThat(Matcher<Double> matcher) {
        return reportMatcher(matcher).returnZero();
    }

    private static HandyReturnValues reportMatcher(Matcher<?> matcher) {
        return mockingProgress.getArgumentMatcherStorage().reportMatcher(matcher);
    }
}
```
