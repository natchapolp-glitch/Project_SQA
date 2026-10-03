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
    <delete dir="${target.dir}/tmp2"/>
    <unzip dest="${target.dir}/tmp2" src="${target.dir}/${zip}" />
    <junit fork="true" forkmode="once" haltonfailure="true">
      <formatter type="plain" usefile="false" />
      <classpath>
        <fileset dir="${target.dir}/tmp2/separate-jars" includes="*.jar" />
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

## src/org/mockito/internal/creation/MockSettingsImpl.java

```
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImpl implements MockSettings {

    private static final long serialVersionUID = 4475297236197939568L;
    private Class<?>[] extraInterfaces;
    private String name;
    private Object spiedInstance;
    private Answer<Object> defaultAnswer;
    private MockName mockName;
    private boolean serializable;

    public MockSettings serializable() {
        this.serializable = true;
        return this;
    }

    public MockSettings extraInterfaces(Class<?>... extraInterfaces) {
        if (extraInterfaces == null || extraInterfaces.length == 0) {
            new Reporter().extraInterfacesRequiresAtLeastOneInterface();
        }
            
        for (Class<?> i : extraInterfaces) {
            if (i == null) {
                new Reporter().extraInterfacesDoesNotAcceptNullParameters();
            } else if (!i.isInterface()) {
                new Reporter().extraInterfacesAcceptsOnlyInterfaces(i);
            }
        }
        this.extraInterfaces = extraInterfaces;
        return this;
    }

    public MockName getMockName() {
        return mockName;
    }

    public Class<?>[] getExtraInterfaces() {
        return extraInterfaces;
    }

    public Object getSpiedInstance() {
        return spiedInstance;
    }

    public MockSettings name(String name) {
        this.name = name;
        return this;
    }

    public MockSettings spiedInstance(Object spiedInstance) {
        this.spiedInstance = spiedInstance;
        return this;
    }

    @SuppressWarnings("unchecked")
    public MockSettings defaultAnswer(Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
        return this;
    }

    public Answer<Object> getDefaultAnswer() {
        return defaultAnswer;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public void initiateMockName(Class classToMock) {
        mockName = new MockName(name, classToMock);
    }
}
```

## src/org/mockito/internal/util/MockUtil.java

```
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

import org.mockito.cglib.proxy.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtil {
    
    private final CreationValidator creationValidator;

    public MockUtil(CreationValidator creationValidator) {
        this.creationValidator = creationValidator;
    }
    
    public MockUtil() {
        this(new CreationValidator());
    }

    public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());

        settings.initiateMockName(classToMock);

        MockHandler<T> mockHandler = new MockHandler<T>(settings);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
        Class<?>[] interfaces = settings.getExtraInterfaces();

        Class<?>[] ancillaryTypes;
        if (settings.isSerializable()) {
            ancillaryTypes = interfaces == null ? new Class<?>[] {Serializable.class} : new ArrayUtils().concat(interfaces, Serializable.class);
        } else {
            ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
        }

        Object spiedInstance = settings.getSpiedInstance();
        
        T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
        
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }
        
        return mock;
    }

    public <T> void resetMock(T mock) {
        MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
        MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, 
                        (MockSettingsImpl) withSettings().defaultAnswer(RETURNS_DEFAULTS));
        ((Factory) mock).setCallback(0, newFilter);
    }

    public <T> MockHandlerInterface<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (MockHandlerInterface) getInterceptor(mock).getHandler();
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    private <T> boolean isMockitoMock(T mock) {
        return Enhancer.isEnhanced(mock.getClass()) && getInterceptor(mock) != null;
    }

    public boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }

    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter) callback;
        }
        return null;
    }

    public MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }
}
```
