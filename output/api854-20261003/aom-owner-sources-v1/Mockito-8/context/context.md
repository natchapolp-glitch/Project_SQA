## build.gradle

```
buildscript {
    repositories {
        maven { url "/home/team/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
 maven { url "https://jcenter.bintray.com/" }

    }

    dependencies {
        classpath 'net.saliman:gradle-cobertura-plugin:2.0.0' // coveralls plugin depends on cobertura plugin
        classpath 'org.kt3k.gradle.plugin:coveralls-gradle-plugin:0.6.1'
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.0' //publishing to bintray
        classpath 'org.codehaus.groovy.modules.http-builder:http-builder:0.5.2' //rest calls to bintray api
    }
}

apply plugin: 'maven-publish'

apply from: 'gradle/version.gradle'
apply from: "gradle/ide.gradle"
apply from: 'gradle/coverage.gradle'

allprojects {
    repositories {
        maven { url "/home/team/sqa-round2/defects4j/framework/lib/build_systems/gradle/deps" }
 maven { url "https://jcenter.bintray.com/" }

    }
}

group = 'org.mockito'
description = 'Core API and implementation.'
sourceCompatibility=1.6
targetCompatibility=1.6

configurations {
    provided
    testUtil //TODO move to separate project
}

sourceSets {
    main {
        java.srcDir 'src'
        compileClasspath = compileClasspath + configurations.provided
    }
    test {
        java.srcDir 'test'
        compileClasspath = compileClasspath + configurations.provided
    }
}

test.include "**/*Test.class"

tasks.withType(JavaCompile) {
    options.warnings = false
}

//TODO we should remove all dependencies to checked-in jars and move the 'mockito-all' project out
//we should consider getting rid of mockito-all completely (or moving out into a completely separate project.
dependencies {
    provided "junit:junit:4.10"
    compile "org.hamcrest:hamcrest-core:1.1", "org.objenesis:objenesis:2.1"
    compile fileTree('lib/repackaged') { exclude '*.txt'}

    testCompile fileTree("lib/test")
    testRuntime configurations.provided

    testUtil sourceSets.test.output
}

def licenseFiles = copySpec {
    //mockito license
    from(".") { include 'LICENSE', 'NOTICE' }
    //repackaged license
    from("lib/repackaged") { include '*.txt' }
}

def repackagedClasses = copySpec {
    from(zipTree("lib/repackaged/cglib-and-asm-1.0.jar")) {
        exclude 'META-INF/MANIFEST.MF'
    }
}

jar {
    baseName = 'mockito-core'
    from(sourceSets.main.allSource)
    with repackagedClasses
    with licenseFiles
}

task sourcesJar(type: Jar) {
    baseName = 'mockito-core'
    from(sourceSets.main.allSource)
    classifier = "sources"
    from(zipTree("lib/sources/cglib-and-asm-1.0-sources.jar"))
    with licenseFiles
}

apply from: 'gradle/javadoc.gradle'

task javadocJar(type: Jar) {
    baseName = 'mockito-core'
    classifier = "javadoc"
    with licenseFiles
    from mockitoJavadoc
}

//TODO SF mockito-all jar should automatically appear as downloadable in bintray and in Mockito 2.0, replaced with zip distro
task allJar(type: Jar) {
    baseName = 'mockito-all'

    with repackagedClasses
    with licenseFiles

    //source files
    from(sourceSets.main.allSource)

    //classes
    from(sourceSets.main.output)

    //3rd party library classes
    from(zipTree("lib/run/objenesis-2.1.jar")) { exclude "META-INF/maven/**" }
    from(zipTree("lib/run/com.springsource.org.hamcrest.core-1.1.0.jar")) { exclude "LICENSE.txt" }

    //3rd party license files
    from("lib/run") { exclude '**/*.jar' }
}

def antCommand = "ant"

if (System.getProperty("os.name").startsWith("Windows")) {
    antCommand += ".bat"
}

configure([jar, allJar]) { task ->
    task.rootSpec.exclude "MANIFEST.MF" //hack to avoid problems with bnd
    doLast {
        project.exec {
            commandLine antCommand, '-f', 'build-ant.xml', "osgify.$task.baseName", "-Dversion=$project.version"
        }
    }
}

artifacts {
    archives allJar, sourcesJar, javadocJar
}

publishing {
    publications {
        mockitoCore(MavenPublication) {
            from components.java
            artifactId 'mockito-core'
            artifact sourcesJar
            artifact javadocJar
        }
        mockitoAll(MavenPublication) {
            artifactId 'mockito-all'
            artifact allJar
            artifact sourcesJar
            artifact javadocJar
        }
    }
}

apply from: 'gradle/release.gradle'
apply from: "gradle/pom.gradle"

task wrapper(type: Wrapper) {
    gradleVersion = '2.0'
}

task ciBuild {
    //validate the state of the project
    dependsOn build, publishToMavenLocal, tasks.idea, tasks.eclipse
}
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
  <property name="snapshots" value="${target.dir}/snapshots" />
  <property name="src.classes.dir" value="${target.dir}/classes" />
  <property name="test.classes.dir" value="${target.dir}/test-classes" />
  <property name="test.reports.dir" value="${target.dir}/reports/junit" />
  <property name="test.reports.dir.html" value="${target.dir}/reports/junit.html" />

  <property name="pmd.reports.dir" value="${target.dir}/reports/pmd" />

  <property name="javadoc.dir" value="${target.dir}/javadoc" />

  <property name="lib.dir" value="lib" />
  <property name="lib.dir.build" value="lib/build" />
  <property name="lib.dir.run" value="lib/run" />
  <property name="lib.dir.compile" value="lib/compile" />
  <property name="lib.dir.test" value="lib/test" />
  <property name="lib.dir.repackaged" value="lib/repackaged" />
  <property name="lib.dir.sources" value="lib/sources" />
    
  <property name="jar.core" value="mockito-core-${version}.jar" />
  <property name="jar.core.javadoc" value="mockito-core-${version}-javadoc.jar" />
  <property name="jar.core.sources" value="mockito-core-${version}-sources.jar" />
	
  <property name="jar.all" value="mockito-all-${version}.jar" />
  <property name="jar.all.javadoc" value="mockito-all-${version}-javadoc.jar" />
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
    <javac srcdir="src" destdir="${src.classes.dir}" source="1.6" target="1.6" debug="true" deprecation="true" nowarn="true">
      <classpath refid="compile.classpath" />
    </javac>
  </target>

  <target name="compile.test" depends="compile">
    <javac srcdir="test" destdir="${test.classes.dir}" source="1.6" target="1.6" debug="true" deprecation="true" nowarn="true">
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
	
	<antcall target="junit.report" />
   
    <fail if="mockito.tests.failed" message="Tests failed."/>
  </target>
  
  <target name="junit.report">
    <mkdir dir="${test.reports.dir.html}" />
	<junitreport todir="${test.reports.dir.html}">
	  <fileset dir="${test.reports.dir}">
		<include name="*.xml"/>
	  </fileset>
	  <report format="frames" todir="${test.reports.dir.html}"/>
	</junitreport>
  </target>

  <target name="pmd" depends="prepare">

    <taskdef name="pmd" classname="net.sourceforge.pmd.ant.PMDTask" classpathref="ant.classpath" />
    <mkdir dir="${pmd.reports.dir}" />

    <pmd shortFilenames="true" failonruleviolation="true" rulesetfiles="conf/pmd-rules.xml">
      <ruleset>Mockito</ruleset>
      <formatter type="text" toConsole="true" />
      <formatter type="xml" tofile="${pmd.reports.dir}/pmd_report.xml" />

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
	  <fileset dir="${lib.dir.run}" excludes="*.jar" />
      <fileset dir="${lib.dir.repackaged}" excludes="*.jar" />
      <zipfileset src="${lib.dir.run}/objenesis-2.1.jar">
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
	
  <target name="javadoc.jar" depends="javadoc">
    <jar jarfile="${target.dir}/${jar.core.javadoc}">
      <manifest>
        <attribute name="Built-By" value="${user.name}"/>
        <attribute name="Implementation-Version" value="${version}"/>
      </manifest>
      <fileset dir="${javadoc.dir}" />
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
      <zipfileset src="${lib.dir.sources}/objenesis-2.1-sources.jar" />
    </jar>
  </target>

  <target name="javadoc-all.jar" depends="javadoc">
    <jar jarfile="${target.dir}/${jar.all.javadoc}">
      <manifest>
        <attribute name="Built-By" value="${user.name}"/>
        <attribute name="Implementation-Version" value="${version}"/>
      </manifest>
      <fileset dir="${javadoc.dir}" />
    </jar>
  </target>
	
  <target name="prepare.package" depends="jar, javadoc, javadoc.jar, javadoc-all.jar, sources-all, osgify.manifests">
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
    <javadoc
            sourcepath="${src.dir}"
            destdir="${javadoc.dir}"
            stylesheetfile="javadoc/stylesheet.css"
            header=""
            author="true"
            version="true"
            use="true"
            windowtitle="Mockito ${version} API">
      <doctitle>
        <![CDATA[
          <h1><a href="org/mockito/Mockito.html">Click to see examples</a>. Mockito ${version} API.</h1>
        ]]>
      </doctitle>
      <header>
        <![CDATA[
          <!-- Note there is a weird javadoc task bug if using the double quote char \" that causes an 'illegal package name' error -->

          <!-- using the beautify plugin for jQuery from https://bitbucket.org/larscorneliussen/beautyofcode/ -->
          <script type="text/javascript">
              var shBaseURL = '{@docRoot}/js/sh-2.1.382/';
          </script>
          <script type="text/javascript" src="{@docRoot}/js/jquery-1.7.min.js"></script>
          <script type="text/javascript" src="{@docRoot}/js/jquery.beautyOfCode-min.js"></script>

          <script type="text/javascript">
              /* Apply beautification of code */
              var usingOldIE = false;
              if($.browser.msie && parseInt($.browser.version) < 9) usingOldIE = true;

              if(!usingOldIE) {
                  $.beautyOfCode.init({
                    theme : 'Eclipse',
                    brushes: ['Java']
                  });

                  var version = ${version};

                  /* Add name & version to header */
                  $(function() {
                    $('td.NavBarCell1[colspan=2]').each(function(index, element) {
                      var jqueryTD = $(element);
                      jqueryTD.after(
                        $('<td><em><strong>Mockito ${version} API</strong></em></td>').attr('class','NavBarCell1').attr('id','mockito-version-header')
                      );
                      jqueryTD.removeAttr('colspan');
                    })
                  })
              }
          </script>
        ]]>
      </header>
      <group title="Main package" packages="org.mockito"/>
      <classpath refid="compile.classpath" />
    </javadoc>
  	<copy todir="${javadoc.dir}/org/mockito" file="javadoc/img/logo.jpg" />
  	<copy todir="${javadoc.dir}/js" file="javadoc/js/jquery-1.7.min.js" />
  	<copy todir="${javadoc.dir}/js" file="javadoc/js/jquery.beautyOfCode-min.js" />
    <copy todir="${javadoc.dir}/js/sh-2.1.382">
      <fileset dir="javadoc/js/sh-2.1.382"/>
    </copy>
  </target>

  <target name="repackage-tests" depends="compile.test">
    <jar jarfile="${target.dir}/${jar.tests}">
      <fileset dir="${test.classes.dir}"/>
    </jar>
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
        <available file="${target.dir}/${jar.all.javadoc}"/>
        <available file="${target.dir}/${jar.all.sources}"/>
        <available file="${target.dir}/${jar.core}"/>
        <available file="${target.dir}/${jar.core.javadoc}"/>
        <available file="${target.dir}/${jar.core.sources}"/>
      </and>
    </condition>

    <fail unless="mockito.jars.exist" message="Mockito binaries don't exist. Issue ant at the command line to build."/>     
  </target>

  <target name="copy.maven.metadata" depends="check.binaries" >
  	<delete dir="${maven.repository.dir}"/>
    <mkdir dir="${maven.repository.dir}/org/mockito/mockito-all" />

    <get src="http://search.maven.org/remotecontent?filepath=org/mockito/mockito-all/maven-metadata.xml"
         dest="${maven.repository.dir}/org/mockito/mockito-all/maven-metadata.xml"/>

    <mkdir dir="${maven.repository.dir}/org/mockito/mockito-core" />

    <get src="http://search.maven.org/remotecontent?filepath=org/mockito/mockito-core/maven-metadata.xml"
         dest="${maven.repository.dir}/org/mockito/mockito-core/maven-metadata.xml"/>
  </target>

  <taskdef resource="org/apache/maven/artifact/ant/antlib.xml" classpathref="ant.classpath"/>

    <target name="prepare.poms" >
        <filter token="version" value="${version}"/>
        <copy todir="${target.dir}" filtering="true">
            <fileset dir="maven" includes="mockito-*.pom"/>
        </copy>
    </target>

    <target name="install.artifact" depends="check.binaries, prepare.poms" description="Installs maven artifact in local repo. Helpful for testing">
      <install file="${target.dir}/${jar.all}">
        <pom file="${target.dir}/mockito-all.pom" />
    </install>
  </target>

  <target name="release.maven" depends="copy.maven.metadata, prepare.poms" description="deploy binaries to sync with maven repository" > 	
    <remoteRepository id="repo.local" url="file:///${maven.repository.dir}" />
  	
    <deploy file="${target.dir}/${jar.all}">
      <pom file="${target.dir}/mockito-all.pom" />
      <attach file="${target.dir}/${jar.all.javadoc}" classifier="javadoc" />
      <attach file="${target.dir}/${jar.all.sources}" classifier="sources" />
      <remoterepository refid="repo.local" />
    </deploy>
  	
    <deploy file="${target.dir}/${jar.core}" >
      <pom file="${target.dir}/mockito-core.pom" />
      <attach file="${target.dir}/${jar.core.javadoc}" classifier="javadoc" />
      <attach file="${target.dir}/${jar.core.sources}" classifier="sources" />
      <remoterepository refid="repo.local" />
    </deploy>
  </target>
  
  <target name="deploy.snapshot" depends="release.maven, deploy.snapshot.hg" /> 	        

  <target name="deploy.snapshot.hg" >
    <taskdef resource="net/sourceforge/ant4hg/taskdefs/antlib.xml" classpathref="ant.classpath" />

    <loadproperties srcfile="../../mockito.hudson.pwd" />

    <mkdir dir="${snapshots}" />

    <hg cmd="clone" source="https://mockito.hudson:${mockito.hudson.pwd}@snapshots.mockito.googlecode.com/hg/"
      destination="${snapshots}" />

    <hg cmd="remove" dir="${snapshots}/org/mockito/mockito-core/${version}" force="true"  />
    <hg cmd="remove" dir="${snapshots}/org/mockito/mockito-all/${version}" force="true"  />

    <copy todir="${snapshots}">
		  <fileset dir="maven/repository" includes="**/*" />
    </copy>

    <hg cmd="add" dir="${snapshots}" />
    <hg cmd="commit" dir="${snapshots}" message="${user.name} deployed new snapshot to please maven fan-boys and give new features to the community!" />
    <hg cmd="push" dir="${snapshots}" />

  </target>
  
  <target name="test.release" depends="test.zip.release, test.zip.release.separate.jars" description="builds zip, unzips it and runs tests against jars" />
  
  <target name="all" depends="zip" description="cleans, builds, tests, jars, zips, bundles maven" />
  
  <target name="build.and.deploy.snapshot" depends="test, prepare.package, deploy.snapshot" />
  
  <target name="hudson.build" depends="test, prepare.package" />

</project>

```

## gradle.properties

```
org.gradle.daemon=false
org.gradle.parallel=true
```

## settings.gradle

```
include 'testng'
include 'extTest'

rootProject.name = 'mockito'

rootProject.children.each { project ->
    String projectDirName = "subprojects/$project.name"
    project.projectDir = new File(settingsDir, projectDirName)
    project.buildFileName = "${project.name}.gradle"
    assert project.projectDir.isDirectory()
    assert project.buildFile.isFile()
}
```

## src/org/mockito/internal/util/reflection/GenericMetadataSupport.java

```
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;


import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Checks;

import java.lang.reflect.*;
import java.util.*;


/**
 * This class can retrieve generic meta-data that the compiler stores on classes
 * and accessible members.
 *
 * <p>
 *     The main idea of this code is to create a Map that will help to resolve return types.
 *     In order to actually work with nested generics, this map will have to be passed along new instances
 *     as a type context.
 * </p>
 *
 * <p>
 *     Hence :
 *     <ul>
 *         <li>A new instance representing the metadata is created using the {@link #inferFrom(Type)} method from a real
 *         <code>Class</code> or from a <code>ParameterizedType</code>, other types are not yet supported.</li>
 *
 *         <li>Then from this metadata, we can extract meta-data for a generic return type of a method, using
 *         {@link #resolveGenericReturnType(Method)}.</li>
 *     </ul>
 * </p>
 *
 * <p>
 * For now this code support the following kind of generic declarations :
 * <pre class="code"><code class="java">
 * interface GenericsNest&lt;K extends Comparable&lt;K&gt; & Cloneable&gt; extends Map&lt;K, Set&lt;Number&gt;&gt; {
 *     Set&lt;Number&gt; remove(Object key); // override with fixed ParameterizedType
 *     List&lt;? super Integer&gt; returning_wildcard_with_class_lower_bound();
 *     List&lt;? super K&gt; returning_wildcard_with_typeVar_lower_bound();
 *     List&lt;? extends K&gt; returning_wildcard_with_typeVar_upper_bound();
 *     K returningK();
 *     &lt;O extends K&gt; List&lt;O&gt; paramType_with_type_params();
 *     &lt;S, T extends S&gt; T two_type_params();
 *     &lt;O extends K&gt; O typeVar_with_type_params();
 *     Number returningNonGeneric();
 * }
 * </code></pre>
 *
 * @see #inferFrom(Type)
 * @see #resolveGenericReturnType(Method)
 * @see org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs
 */
public abstract class GenericMetadataSupport {

    // public static MockitoLogger logger = new ConsoleMockitoLogger();

    /**
     * Represents actual type variables resolved for current class.
     */
    protected Map<TypeVariable, Type> contextualActualTypeParameters = new HashMap<TypeVariable, Type>();


    protected void registerTypeVariablesOn(Type classType) {
        if (!(classType instanceof ParameterizedType)) {
            return;
        }
        ParameterizedType parameterizedType = (ParameterizedType) classType;
        TypeVariable[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            TypeVariable typeParameter = typeParameters[i];
            Type actualTypeArgument = actualTypeArguments[i];

            if (actualTypeArgument instanceof WildcardType) {
                contextualActualTypeParameters.put(typeParameter, boundsOf((WildcardType) actualTypeArgument));
            } else if (typeParameter != actualTypeArgument) {
                contextualActualTypeParameters.put(typeParameter, actualTypeArgument);
            }
            // logger.log("For '" + parameterizedType + "' found type variable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualTypeArgument + "(" + System.identityHashCode(typeParameter) + ")" + "' }");
        }
    }

    protected void registerTypeParametersOn(TypeVariable[] typeParameters) {
        for (TypeVariable typeVariable : typeParameters) {
            registerTypeVariableIfNotPresent(typeVariable);
        }
    }

    private void registerTypeVariableIfNotPresent(TypeVariable typeVariable) {
        if (!contextualActualTypeParameters.containsKey(typeVariable)) {
            contextualActualTypeParameters.put(typeVariable, boundsOf(typeVariable));
            // logger.log("For '" + typeVariable.getGenericDeclaration() + "' found type variable : { '" + typeVariable + "(" + System.identityHashCode(typeVariable) + ")" + "' : '" + boundsOf(typeVariable) + "' }");
        }
    }

    /**
     * @param typeParameter The TypeVariable parameter
     * @return A {@link BoundedType} for easy bound information, if first bound is a TypeVariable
     *         then retrieve BoundedType of this TypeVariable
     */
    private BoundedType boundsOf(TypeVariable typeParameter) {
        if (typeParameter.getBounds()[0] instanceof TypeVariable) {
            return boundsOf((TypeVariable) typeParameter.getBounds()[0]);
        }
        return new TypeVarBoundedType(typeParameter);
    }

    /**
     * @param wildCard The WildCard type
     * @return A {@link BoundedType} for easy bound information, if first bound is a TypeVariable
     *         then retrieve BoundedType of this TypeVariable
     */
    private BoundedType boundsOf(WildcardType wildCard) {
        /*
         *  According to JLS(http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.5.1):
         *  - Lower and upper can't coexist: (for instance, this is not allowed: <? extends List<String> & super MyInterface>)
         *  - Multiple bounds are not supported (for instance, this is not allowed: <? extends List<String> & MyInterface>)
         */

        WildCardBoundedType wildCardBoundedType = new WildCardBoundedType(wildCard);
        if (wildCardBoundedType.firstBound() instanceof TypeVariable) {
            return boundsOf((TypeVariable) wildCardBoundedType.firstBound());
        }

        return wildCardBoundedType;
    }



    /**
     * @return Raw type of the current instance.
     */
    public abstract Class<?> rawType();



    /**
     * @return Returns extra interfaces <strong>if relevant</strong>, otherwise empty List.
     */
    public List<Type> extraInterfaces() {
        return Collections.emptyList();
    }

    /**
     * @return Returns an array with the raw types of {@link #extraInterfaces()} <strong>if relevant</strong>.
     */
    public Class<?>[] rawExtraInterfaces() {
        return new Class[0];
    }

    /**
     * @return Returns true if metadata knows about extra-interfaces {@link #extraInterfaces()} <strong>if relevant</strong>.
     */
    public boolean hasRawExtraInterfaces() {
        return rawExtraInterfaces().length > 0;
    }



    /**
     * @return Actual type arguments matching the type variables of the raw type represented by this {@link GenericMetadataSupport} instance.
     */
    public Map<TypeVariable, Type> actualTypeArguments() {
        TypeVariable[] typeParameters = rawType().getTypeParameters();
        LinkedHashMap<TypeVariable, Type> actualTypeArguments = new LinkedHashMap<TypeVariable, Type>();

        for (TypeVariable typeParameter : typeParameters) {

            Type actualType = getActualTypeArgumentFor(typeParameter);

            actualTypeArguments.put(typeParameter, actualType);
            // logger.log("For '" + rawType().getCanonicalName() + "' returning explicit TypeVariable : { '" + typeParameter + "(" + System.identityHashCode(typeParameter) + ")" + "' : '" + actualType +"' }");
        }

        return actualTypeArguments;
    }

    protected Type getActualTypeArgumentFor(TypeVariable typeParameter) {
        Type type = this.contextualActualTypeParameters.get(typeParameter);
        if (type instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type;
            return getActualTypeArgumentFor(typeVariable);
        }

        return type;
    }



    /**
     * Resolve current method generic return type to a {@link GenericMetadataSupport}.
     *
     * @param method Method to resolve the return type.
     * @return {@link GenericMetadataSupport} representing this generic return type.
     */
    public GenericMetadataSupport resolveGenericReturnType(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        // logger.log("Method '" + method.toGenericString() + "' has return type : " + genericReturnType.getClass().getInterfaces()[0].getSimpleName() + " : " + genericReturnType);

        if (genericReturnType instanceof Class) {
            return new NotGenericReturnTypeSupport(genericReturnType);
        }
        if (genericReturnType instanceof ParameterizedType) {
            return new ParameterizedReturnType(this, method.getTypeParameters(), (ParameterizedType) method.getGenericReturnType());
        }
        if (genericReturnType instanceof TypeVariable) {
            return new TypeVariableReturnType(this, method.getTypeParameters(), (TypeVariable) genericReturnType);
        }

        throw new MockitoException("Ouch, it shouldn't happen, type '" + genericReturnType.getClass().getCanonicalName() + "' on method : '" + method.toGenericString() + "' is not supported : " + genericReturnType);
    }

    /**
     * Create an new instance of {@link GenericMetadataSupport} inferred from a {@link Type}.
     *
     * <p>
     *     At the moment <code>type</code> can only be a {@link Class} or a {@link ParameterizedType}, otherwise
     *     it'll throw a {@link MockitoException}.
     * </p>
     *
     * @param type The class from which the {@link GenericMetadataSupport} should be built.
     * @return The new {@link GenericMetadataSupport}.
     * @throws MockitoException Raised if type is not a {@link Class} or a {@link ParameterizedType}.
     */
    public static GenericMetadataSupport inferFrom(Type type) {
        Checks.checkNotNull(type, "type");
        if (type instanceof Class) {
            return new FromClassGenericMetadataSupport((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return new FromParameterizedTypeGenericMetadataSupport((ParameterizedType) type);
        }

        throw new MockitoException("Type meta-data for this Type (" + type.getClass().getCanonicalName() + ") is not supported : " + type);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Below are specializations of GenericMetadataSupport that could handle retrieval of possible Types
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generic metadata implementation for {@link Class}.
     *
     * Offer support to retrieve generic metadata on a {@link Class} by reading type parameters and type variables on
     * the class and its ancestors and interfaces.
     */
    private static class FromClassGenericMetadataSupport extends GenericMetadataSupport {
        private final Class<?> clazz;

        public FromClassGenericMetadataSupport(Class<?> clazz) {
            this.clazz = clazz;

            for (Class currentExploredClass = clazz;
                 currentExploredClass != null && currentExploredClass != Object.class;
                 currentExploredClass = superClassOf(currentExploredClass)
                ) {
                readActualTypeParametersOnDeclaringClass(currentExploredClass);
            }
        }

        private Class superClassOf(Class currentExploredClass) {
            Type genericSuperclass = currentExploredClass.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) genericSuperclass).getRawType();
                return (Class) rawType;
            }
            return (Class) genericSuperclass;
        }

        private void readActualTypeParametersOnDeclaringClass(Class<?> clazz) {
            registerTypeParametersOn(clazz.getTypeParameters());
            registerTypeVariablesOn(clazz.getGenericSuperclass());
            for (Type genericInterface : clazz.getGenericInterfaces()) {
                registerTypeVariablesOn(genericInterface);
            }
        }

        @Override
        public Class<?> rawType() {
            return clazz;
        }
    }


    /**
     * Generic metadata implementation for "standalone" {@link ParameterizedType}.
     *
     * Offer support to retrieve generic metadata on a {@link ParameterizedType} by reading type variables of
     * the related raw type and declared type variable of this parameterized type.
     *
     * This class is not designed to work on ParameterizedType returned by {@link Method#getGenericReturnType()}, as
     * the ParameterizedType instance return in these cases could have Type Variables that refer to type declaration(s).
     * That's what meant the "standalone" word at the beginning of the Javadoc.
     * Instead use {@link ParameterizedReturnType}.
     */
    private static class FromParameterizedTypeGenericMetadataSupport extends GenericMetadataSupport {
        private final ParameterizedType parameterizedType;

        public FromParameterizedTypeGenericMetadataSupport(ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
            readActualTypeParameters();
        }

        private void readActualTypeParameters() {
            registerTypeVariablesOn(parameterizedType.getRawType());
            registerTypeVariablesOn(parameterizedType);
        }

        @Override
        public Class<?> rawType() {
            return (Class<?>) parameterizedType.getRawType();
        }
    }


    /**
     * Generic metadata specific to {@link ParameterizedType} returned via {@link Method#getGenericReturnType()}.
     */
    private static class ParameterizedReturnType extends GenericMetadataSupport {
        private final ParameterizedType parameterizedType;
        private final TypeVariable[] typeParameters;

        public ParameterizedReturnType(GenericMetadataSupport source, TypeVariable[] typeParameters, ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
            this.typeParameters = typeParameters;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;

            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(typeParameters);
        }

        private void readTypeVariables() {
            registerTypeVariablesOn(parameterizedType);
        }

        @Override
        public Class<?> rawType() {
            return (Class<?>) parameterizedType.getRawType();
        }

    }


    /**
     * Generic metadata for {@link TypeVariable} returned via {@link Method#getGenericReturnType()}.
     */
    private static class TypeVariableReturnType extends GenericMetadataSupport {
        private final TypeVariable typeVariable;
        private final TypeVariable[] typeParameters;
        private Class<?> rawType;



        public TypeVariableReturnType(GenericMetadataSupport source, TypeVariable[] typeParameters, TypeVariable typeVariable) {
            this.typeParameters = typeParameters;
            this.typeVariable = typeVariable;
            this.contextualActualTypeParameters = source.contextualActualTypeParameters;

            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(typeParameters);
        }

        private void readTypeVariables() {
            for (Type type : typeVariable.getBounds()) {
                registerTypeVariablesOn(type);
            }
            registerTypeVariablesOn(getActualTypeArgumentFor(typeVariable));
        }

        @Override
        public Class<?> rawType() {
            if (rawType == null) {
                rawType = extractRawTypeOf(typeVariable);
            }
            return rawType;
        }

        private Class<?> extractRawTypeOf(Type type) {
            if (type instanceof Class) {
                return (Class<?>) type;
            }
            if (type instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) type).getRawType();
            }
            if (type instanceof BoundedType) {
                return extractRawTypeOf(((BoundedType) type).firstBound());
            }
            if (type instanceof TypeVariable) {
                /*
                 * If type is a TypeVariable, then it is needed to gather data elsewhere. Usually TypeVariables are declared
                 * on the class definition, such as such as List<E>.
                 */
                return extractRawTypeOf(contextualActualTypeParameters.get(type));
            }
            throw new MockitoException("Raw extraction not supported for : '" + type + "'");
        }

        @Override
        public List<Type> extraInterfaces() {
            Type type = extractActualBoundedTypeOf(typeVariable);
            if (type instanceof BoundedType) {
                return Arrays.asList(((BoundedType) type).interfaceBounds());
            }
            if (type instanceof ParameterizedType) {
                return Collections.singletonList(type);
            }
            if (type instanceof Class) {
                return Collections.emptyList();
            }
            throw new MockitoException("Cannot extract extra-interfaces from '" + typeVariable + "' : '" + type + "'");
        }

        /**
         * @return Returns an array with the extracted raw types of {@link #extraInterfaces()}.
         * @see #extractRawTypeOf(java.lang.reflect.Type)
         */
        public Class<?>[] rawExtraInterfaces() {
            List<Type> extraInterfaces = extraInterfaces();
            List<Class<?>> rawExtraInterfaces = new ArrayList<Class<?>>();
            for (Type extraInterface : extraInterfaces) {
                Class<?> rawInterface = extractRawTypeOf(extraInterface);
                // avoid interface collision with actual raw type (with typevariables, resolution ca be quite aggressive)
                if(!rawType().equals(rawInterface)) {
                    rawExtraInterfaces.add(rawInterface);
                }
            }
            return rawExtraInterfaces.toArray(new Class[rawExtraInterfaces.size()]);
        }

        private Type extractActualBoundedTypeOf(Type type) {
            if (type instanceof TypeVariable) {
                /*
                If type is a TypeVariable, then it is needed to gather data elsewhere. Usually TypeVariables are declared
                on the class definition, such as such as List<E>.
                */
                return extractActualBoundedTypeOf(contextualActualTypeParameters.get(type));
            }
            if (type instanceof BoundedType) {
                Type actualFirstBound = extractActualBoundedTypeOf(((BoundedType) type).firstBound());
                if (!(actualFirstBound instanceof BoundedType)) {
                    return type; // avoid going one step further, ie avoid : O(TypeVar) -> K(TypeVar) -> Some ParamType
                }
                return actualFirstBound;
            }
            return type; // irrelevant, we don't manage other types as they are not bounded.
        }
    }



    /**
     * Non-Generic metadata for {@link Class} returned via {@link Method#getGenericReturnType()}.
     */
    private static class NotGenericReturnTypeSupport extends GenericMetadataSupport {
        private final Class<?> returnType;

        public NotGenericReturnTypeSupport(Type genericReturnType) {
            returnType = (Class<?>) genericReturnType;
        }

        @Override
        public Class<?> rawType() {
            return returnType;
        }
    }



    /**
     * Type representing bounds of a type
     *
     * @see TypeVarBoundedType
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.4">http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.4</a>
     * @see WildCardBoundedType
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.5.1">http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.5.1</a>
     */
    public interface BoundedType extends Type {
        Type firstBound();

        Type[] interfaceBounds();
    }

    /**
     * Type representing bounds of a type variable, allows to keep all bounds information.
     *
     * <p>It uses the first bound in the array, as this array is never null and always contains at least
     * one element (Object is always here if no bounds are declared).</p>
     *
     * <p>If upper bounds are declared with SomeClass and additional interfaces, then firstBound will be SomeClass and
     * interfacesBound will be an array of the additional interfaces.
     *
     * i.e. <code>SomeClass</code>.
     * <pre class="code"><code class="java">
     *     interface UpperBoundedTypeWithClass<E extends Comparable<E> & Cloneable> {
     *         E get();
     *     }
     *     // will return Comparable type
     * </code></pre>
     * </p>
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.4">http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.4</a>
     */
    public static class TypeVarBoundedType implements BoundedType {
        private final TypeVariable typeVariable;


        public TypeVarBoundedType(TypeVariable typeVariable) {
            this.typeVariable = typeVariable;
        }

        /**
         * @return either a class or an interface (parameterized or not), if no bounds declared Object is returned.
         */
        public Type firstBound() {
            return typeVariable.getBounds()[0]; //
        }

        /**
         * On a Type Variable (typeVar extends C_0 & I_1 & I_2 & etc), will return an array
         * containing I_1 and I_2.
         *
         * @return other bounds for this type, these bounds can only be only interfaces as the JLS says,
         * empty array if no other bound declared.
         */
        public Type[] interfaceBounds() {
            Type[] interfaceBounds = new Type[typeVariable.getBounds().length - 1];
            System.arraycopy(typeVariable.getBounds(), 1, interfaceBounds, 0, typeVariable.getBounds().length - 1);
            return interfaceBounds;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return typeVariable.equals(((TypeVarBoundedType) o).typeVariable);

        }

        @Override
        public int hashCode() {
            return typeVariable.hashCode();
        }

        @Override
        public String toString() {
            return "{firstBound=" + firstBound() + ", interfaceBounds=" + Arrays.deepToString(interfaceBounds()) + '}';
        }

        public TypeVariable typeVariable() {
            return typeVariable;
        }
    }

    /**
     * Type representing bounds of a wildcard, allows to keep all bounds information.
     *
     * <p>The JLS says that lower bound and upper bound are mutually exclusive, and that multiple bounds
     * are not allowed.
     *
     * @see <a href="http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.4">http://docs.oracle.com/javase/specs/jls/se5.0/html/typesValues.html#4.4</a>
     */
    public static class WildCardBoundedType implements BoundedType {
        private final WildcardType wildcard;


        public WildCardBoundedType(WildcardType wildcard) {
            this.wildcard = wildcard;
        }

        /**
         * @return The first bound, either a type or a reference to a TypeVariable
         */
        public Type firstBound() {
            Type[] lowerBounds = wildcard.getLowerBounds();
            Type[] upperBounds = wildcard.getUpperBounds();

            return lowerBounds.length != 0 ? lowerBounds[0] : upperBounds[0];
        }

        /**
         * @return An empty array as, wildcard don't support multiple bounds.
         */
        public Type[] interfaceBounds() {
            return new Type[0];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            return wildcard.equals(((TypeVarBoundedType) o).typeVariable);

        }

        @Override
        public int hashCode() {
            return wildcard.hashCode();
        }

        @Override
        public String toString() {
            return "{firstBound=" + firstBound() + ", interfaceBounds=[]}";
        }

        public WildcardType wildCard() {
            return wildcard;
        }
    }

}



```
