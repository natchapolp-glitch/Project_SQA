## build.xml

```
<?xml version="1.0"?>
<!--
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<project name="commons-collections" default="compile" basedir=".">

<!-- ========== Properties ================================================ -->

  <!-- This can be used to define 'junit.jar' and 'emma.lib.dir' properties if necessary -->
  <property file="build.properties"/>
  
	<!-- Import EMMA coverage tool -->
	<path id="emma.lib">
		<pathelement location="${emma.lib.dir}/emma.jar" />
		<pathelement location="${emma.lib.dir}/emma_ant.jar" />
	</path>
	<taskdef resource="emma_ant.properties" classpathref="emma.lib" />

<!-- ========== Component Declarations ==================================== -->

  <!-- The name of this component -->
  <property name="component.name"          value="commons-collections"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.apache.commons.collections"/>

  <!-- The short title of this component -->
  <property name="component.title"         value="Commons Collections"/>

  <!-- The full title of this component -->
  <property name="component.title.full"    value="Apache Commons Collections"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="4.0-SNAPSHOT"/>

  <!-- The base directory for component configuration files -->
  <property name="source.conf"               value="src/conf"/>

  <!-- The base directory for component sources -->
  <property name="source.java"             value="src/main/java"/>

  <!-- The base directory for unit test sources -->
  <property name="source.test"             value="src/test/java"/>

  <!-- The directories for compilation targets -->
  <property name="build.home"              value="build"/>
  <property name="build.conf"              value="${build.home}/conf"/>
  <property name="build.classes"           value="${build.home}/classes"/>
  <property name="build.tests"             value="${build.home}/tests"/>
  <property name="build.test.reports"      value="${build.home}/test-reports"/>
  <property name="build.docs"              value="${build.home}/docs/apidocs"/>
  <property name="build.src"               value="${build.home}/src-ide" />
  
  <!-- The directories for tests/instrumentation -->
  <property name="build.instrumented"           value="${build.home}/instrumented" />
  <property name="build.reports"           value="${build.home}/reports" />
  <property name="build.reports.coverage"  value="${build.reports}/coverage" />
  
  <!-- The name/location of the jar file to build -->
  <property name="final.name"           value="${component.name}-${component.version}"/>
  <property name="jar.name"             value="${final.name}.jar"/>
  <property name="build.jar.name"       value="${build.home}/${jar.name}"/>
  
  <!-- The name/location of the zip files to build -->
  <property name="build.dist.bin"       value="${build.home}/bin"/>
  <property name="build.dist.bin.work"  value="${build.dist.bin}/${component.name}-${component.version}"/>
  <property name="build.dist.src"       value="${build.home}/src"/>
  <property name="build.dist.src.work"  value="${build.dist.src}/${component.name}-${component.version}-src"/>
  <property name="build.dist"           value="${build.home}/dist"/>
  <property name="build.bin.tar.name"   value="${build.dist}/${component.name}-${component.version}.tar"/>
  <property name="build.bin.gz.name"    value="${build.dist}/${component.name}-${component.version}.tar.gz"/>
  <property name="build.bin.zip.name"   value="${build.dist}/${component.name}-${component.version}.zip"/>
  <property name="build.src.tar.name"   value="${build.dist}/${component.name}-${component.version}-src.tar"/>
  <property name="build.src.gz.name"    value="${build.dist}/${component.name}-${component.version}-src.tar.gz"/>
  <property name="build.src.zip.name"   value="${build.dist}/${component.name}-${component.version}-src.zip"/>
  <property name="dist.home"            value="dist"/>
  <!-- for nightly builds -->

  <!-- Used for generating a support jar for open source packages that require deprecated classes -->
  <property name="collections-3.jar" value="commons-collections-3.2.jar"/>
  
  <!-- Path variables -->
  <path id="test.support.path">
    <!--Combines all jar references needed for running and compiling tests-->
    <pathelement location="${junit.jar}" />
    <pathelement location="${easymock.jar}" />
  </path>

  <!-- ========== Settings ================================================== -->

  <!-- Javac -->
  <property name="compile.debug"           value="true"/>
  <property name="compile.deprecation"     value="true"/>
  <property name="compile.optimize"        value="false"/>

  <!-- Javadoc -->
  <property name="javadoc.access"          value="protected"/>
  <property name="javadoc.links"           value="http://java.sun.com/j2se/1.3/docs/api/"/>

  <!-- JUnit -->
  <property name="test.failonerror"        value="true"/>
  <property name="test.fork"               value="true"/>
  <property name="test.haltonfailure"      value="true"/>

  <!-- Maven -->
  <property name="maven.repo"  value="${user.home}/.maven/repository" />


  <!-- ====================================================================== -->
  <!-- ========== Executable Targets ======================================== -->
  <!-- ====================================================================== -->

  <target name="clean"
          description="Clean build and distribution directories">
    <delete dir="${build.home}"/>
  </target>

  <!-- ====================================================================== -->

  <target name="init"
          description="Initialize and evaluate conditionals">
    <echo message="-------- ${component.name} ${component.version} --------"/>
  </target>

  <!-- ====================================================================== -->

  <target name="prepare" depends="init"
          description="Prepare build directory">
    <mkdir dir="${build.home}"/>
  </target>

  <!-- ====================================================================== -->

  <target name="compile" depends="prepare"
          description="Compile main code">
    <mkdir dir="${build.classes}"/>
    <javac  srcdir="${source.java}" target="1.6" source="1.6"
           destdir="${build.classes}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
          optimize="${compile.optimize}"
 includeantruntime="false">
    </javac>
  </target>

  <!-- ====================================================================== -->

  <target name="jar" depends="compile"
          description="Create jar">
    <mkdir      dir="${build.classes}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${build.classes}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${build.classes}/META-INF/NOTICE.txt"/>

    <tstamp/>
    <mkdir      dir="${build.conf}"/>
    <copy     todir="${build.conf}" filtering="on">
      <filterset>
        <filter token="name"     value="${component.name}"/>
        <filter token="title"    value="${component.title}"/>
        <filter token="package"  value="${component.package}"/>
        <filter token="version"  value="${component.version}"/>
      </filterset>
      <fileset dir="${source.conf}" includes="*.MF"/>
    </copy>

    <!-- NOTE: A jar built using JDK1.4 is incompatible with JDK1.2 -->
    <jar    jarfile="${build.jar.name}"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF"/>
  </target>

  <!-- ====================================================================== -->
  <!-- Targets you might use to get smaller jar files - not recommended -->

  <target name="splitjar" depends="jar"
          description="Create split jar">
    <jar    jarfile="${build.home}/${component.name}-bag-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/BagUtils*.class"/>
      <include name="**/bag/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-bidimap-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/bidimap/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-buffer-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/BufferUtils*.class"/>
      <include name="**/buffer/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-functors-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/ClosureUtils*.class"/>
      <include name="**/FactoryUtils*.class"/>
      <include name="**/PredicateUtils*.class"/>
      <include name="**/TransformerUtils*.class"/>
      <include name="**/functors/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-core-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/*"/>
      <exclude name="**/BagUtils*.class"/>
      <exclude name="**/BufferUtils*.class"/>
      <exclude name="**/ClosureUtils*.class"/>
      <exclude name="**/FactoryUtils*.class"/>
      <exclude name="**/PredicateUtils*.class"/>
      <exclude name="**/TransformerUtils*.class"/>
      <exclude name="**/bag/*.class"/>
      <exclude name="**/bidimap/*.class"/>
      <exclude name="**/buffer/*.class"/>
      <exclude name="**/functors/*.class"/>
      <exclude name="**/iterators/ProxyIterator*.class"/>
      <exclude name="**/iterators/ProxyListIterator*.class"/>
      <exclude name="**/map/*.class"/>
      <exclude name="org/apache/commons/collections/BeanMap*.class"/>
      <exclude name="org/apache/commons/collections/BinaryHeap*.class"/>
      <exclude name="org/apache/commons/collections/BoundedFifoBuffer*.class"/>
      <exclude name="org/apache/commons/collections/CursorableLinkedList*.class"/>
      <exclude name="org/apache/commons/collections/CursorableSubList*.class"/>
      <exclude name="org/apache/commons/collections/DefaultMapBag*.class"/>
      <exclude name="org/apache/commons/collections/DefaultMapEntry*.class"/>
      <exclude name="org/apache/commons/collections/DoubleOrderedMap*.class"/>
      <exclude name="org/apache/commons/collections/HashBag*.class"/>
      <exclude name="org/apache/commons/collections/LRUMap*.class"/>
      <exclude name="org/apache/commons/collections/MultiHashMap*.class"/>
      <exclude name="org/apache/commons/collections/PriorityQueue*.class"/>
      <exclude name="org/apache/commons/collections/ProxyMap*.class"/>
      <exclude name="org/apache/commons/collections/ReferenceMap*.class"/>
      <exclude name="org/apache/commons/collections/SequencedHashMap*.class"/>
      <exclude name="org/apache/commons/collections/StaticBucketMap*.class"/>
      <exclude name="org/apache/commons/collections/SynchronizedPriorityQueue*.class"/>
      <exclude name="org/apache/commons/collections/TreeBag*.class"/>
      <exclude name="org/apache/commons/collections/UnboundedFifoBuffer*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-deprecated-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/iterators/ProxyIterator*.class"/>
      <include name="**/iterators/ProxyListIterator*.class"/>
      <include name="org/apache/commons/collections/BeanMap*.class"/>
      <include name="org/apache/commons/collections/BinaryHeap*.class"/>
      <include name="org/apache/commons/collections/BoundedFifoBuffer*.class"/>
      <include name="org/apache/commons/collections/CursorableLinkedList*.class"/>
      <include name="org/apache/commons/collections/CursorableSubList*.class"/>
      <include name="org/apache/commons/collections/DefaultMapBag*.class"/>
      <include name="org/apache/commons/collections/DefaultMapEntry*.class"/>
      <include name="org/apache/commons/collections/DoubleOrderedMap*.class"/>
      <include name="org/apache/commons/collections/HashBag*.class"/>
      <include name="org/apache/commons/collections/LRUMap*.class"/>
      <include name="org/apache/commons/collections/MultiHashMap*.class"/>
      <include name="org/apache/commons/collections/PriorityQueue*.class"/>
      <include name="org/apache/commons/collections/ProxyMap*.class"/>
      <include name="org/apache/commons/collections/ReferenceMap*.class"/>
      <include name="org/apache/commons/collections/SequencedHashMap*.class"/>
      <include name="org/apache/commons/collections/StaticBucketMap*.class"/>
      <include name="org/apache/commons/collections/SynchronizedPriorityQueue*.class"/>
      <include name="org/apache/commons/collections/TreeBag*.class"/>
      <include name="org/apache/commons/collections/UnboundedFifoBuffer*.class"/>
    </jar>

  </target>

  <!-- ====================================================================== -->

  <target name="compile.tests" depends="compile"
          description="Compile unit test cases">
    <mkdir dir="${build.tests}"/>
    <javac  srcdir="${source.test}" target="1.6" source="1.6"
           destdir="${build.tests}"
             debug="true"
       deprecation="false"
          optimize="false"
 includeantruntime="false">
      <classpath>
        <pathelement location="${build.classes}"/>
        <path refid="test.support.path"/>
      </classpath>
    </javac>
  </target>

  <!-- ====================================================================== -->

  <!-- Tests collections, either running all or one test -->
  <target name="test" depends="-test-all,-test-single"
          description="Run unit tests" />

  <path id="build.path">
    <pathelement location="${build.classes}" />
  </path>

  <target name="instrument"
          depends="compile.tests"
          unless="emma.norun"
          description="Instruments the compiled classes and moves them to the instrumentation directory">
    <emma>
      <instr instrpathref="build.path"
             destdir="${build.instrumented}"
             metadatafile="${build.reports.coverage}/coverage.emma"
             merge="true">
      </instr>
    </emma>
  </target>

  <!-- Runs all tests -->
  <target name="-test-all" depends="compile.tests,instrument" unless="testcase">
	<mkdir dir="${build.test.reports}" />

    <junit printsummary="yes" haltonfailure="${test.haltonfailure}" showoutput="yes">
      <classpath>
        <pathelement location="${build.instrumented}"/>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <path refid="test.support.path"/>
      	<path refid="emma.lib"/>
      </classpath>
      <jvmarg value="-Demma.coverage.out.file=${build.reports.coverage}/coverage.ec" />
      <jvmarg value="-Demma.coverage.out.merge=true" />
      <jvmarg value="-Demma.verbosity.level=quiet" />

      <formatter type="xml" />
      <formatter type="plain" />
      <batchtest fork="${test.fork}" todir="${build.test.reports}">
        <fileset dir="${source.test}">
          <include name="**/Test*.java"/>
          <exclude name="**/TestUtils.java"/>
        </fileset>
      </batchtest>
    </junit>
    <junitreport todir="${build.test.reports}">
      <fileset dir="${build.test.reports}">
        <include name="TEST-*.xml" />
      </fileset>
      <report format="frames" todir="${build.test.reports}/html" />
    </junitreport>
  </target>

  <!-- Runs a single test -->
  <target name="-test-single" depends="compile.tests" if="testcase">
    <junit printsummary="yes" haltonfailure="${test.haltonfailure}" showoutput="yes">
      <formatter type="brief" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
      	<path refid="test.support.path"/>
      </classpath>

      <test name="${testcase}" fork="${test.fork}" todir="${build.test.reports}">
        <formatter type="brief" usefile="false" />
      </test>
    </junit>
  </target>
  
  <target name="coverage.report"
          depends="test"
          description="Generates the coverage report for the code that has been run">
    <emma>
      <report sourcepath="${source.java}">
        <fileset dir="${build.reports.coverage}">
          <include name="*" />
        </fileset>
        <html outfile="${build.reports.coverage}/coverage.html" />
      </report>
    </emma>
  </target>

  <!-- ====================================================================== -->

  <target name="testjar"  depends="compile.tests,jar"
          description="Run all unit test cases">
    <echo message="Running collections tests against built jar ..."/>
    <junit printsummary="yes" haltonfailure="${test.haltonfailure}" dir="${basedir}">
      <classpath>
        <pathelement location="${build.jar.name}"/>
        <pathelement location="${build.tests}"/>
      	<path refid="test.support.path"/>
      </classpath>

      <batchtest fork="${test.fork}">
        <fileset dir="${source.test}">
          <include name="**/TestAllPackages.java"/>
        </fileset>
        <formatter type="brief" usefile="false" />
      </batchtest>
    </junit>
  </target>

  <!-- ====================================================================== -->

  <target name="javadoc" depends="prepare"
          description="Create component Javadoc documentation">
    <tstamp>
      <format property="year" pattern="yyyy"/>
    </tstamp>
    <delete     dir="${build.docs}"/>
    <mkdir      dir="${build.docs}"/>
    <javadoc sourcepath="${source.java}"
             destdir="${build.docs}"
             packagenames="${component.package}.*"
             access="${javadoc.access}"
             author="true"
             version="true"
             use="true"
             link="${javadoc.links}"
             overview="${source.java}/org/apache/commons/collections/overview.html"
             doctitle="${component.title} ${component.version} API;"
             windowtitle="${component.title} ${component.version} API"
             bottom="Copyright &amp;copy; 2001-${year} Apache Software Foundation. All Rights Reserved.">
    </javadoc>
  </target>

  <!-- ====================================================================== -->
  <!-- ========== Test framework ============================================ -->
  <!-- ====================================================================== -->

  <property name="tf.name"                 value="commons-collections-testframework"/>
  <property name="tf.package"              value="org.apache.commons.collections"/>
  <property name="tf.title"                value="Commons Collections Test Framework"/>
  <property name="tf.title.full"           value="Apache Commons Collections Test Framework"/>
  <property name="tf.version"              value="${component.version}"/>

  <property name="tf.build.conf"           value="${build.home}/tfconf"/>
  <property name="tf.build.tf"             value="${build.home}/testframework"/>
  <property name="tf.build.docs"           value="${build.home}/docs/testframework"/>

  <property name="tf.jar.name" value="${tf.name}-${tf.version}.jar"/>
  <property name="tf.build.jar.name" value="${build.home}/${tf.jar.name}"/>


  <!-- ====================================================================== -->

  <!-- patternset describing test framework source not dependent on collections jar -->
  <patternset id="tf.patternset.validate">
    <include name="**/AbstractTestObject.java"/>
    <include name="**/AbstractTestCollection.java"/>
    <include name="**/AbstractTestSet.java"/>
    <include name="**/AbstractTestSortedSet.java"/>
    <include name="**/AbstractTestList.java"/>
    <include name="**/AbstractTestMap.java"/>
    <include name="**/AbstractTestSortedMap.java"/>
    <include name="**/AbstractTestComparator.java"/>
    <include name="**/AbstractTestIterator.java"/>
    <include name="**/AbstractTestListIterator.java"/>
    <include name="**/AbstractTestMapEntry.java"/>
    <include name="**/BulkTest.java"/>
  </patternset>

  <target name="tf.validate" depends="prepare"
          description="Testframework - Validate testframework independence">
    <delete    dir="${tf.build.tf}"/>
    <mkdir     dir="${tf.build.tf}"/>
    <javac  srcdir="${source.test}" target="1.6" source="1.6"
            destdir="${tf.build.tf}"
            debug="true"
            deprecation="false"
            optimize="false"
            includeantruntime="false">
      <patternset refid="tf.patternset.validate" />
      <classpath>
        <path refid="test.support.path"/>
      </classpath>
    </javac>
    <delete dir="${tf.build.tf}"/>
  </target>

  <!-- ====================================================================== -->

  <target name="tf.jar" depends="compile.tests"
          description="Testframework - Create jar">
    <mkdir      dir="${tf.build.tf}"/>
    <copy     todir="${tf.build.tf}">
      <fileset dir="${build.tests}">
        <include name="**/AbstractTest*.class"/>
        <include name="**/BulkTest*.class"/>
      </fileset>
    </copy>

    <!-- NOTE: A jar built using JDK1.4 is incompatible with JDK1.2 -->
    <ant antfile="build-testframework.xml" target="jar">
      <property name="test.classes"         value="${tf.build.tf}"/>
      <property name="dist.home"            value="${build.home}"/>
      <property name="component.version"    value="${tf.version}"/>
      <property name="maven.compile.source" value="1.2"/>
      <property name="maven.compile.target" value="1.2"/>
    </ant>

  </target>

  <!-- ====================================================================== -->

  <target name="tf.javadoc" depends="prepare"
          description="Testframework - Create Javadoc documentation">
    <tstamp>
      <format property="year" pattern="yyyy"/>
    </tstamp>
    <delete     dir="${tf.build.docs}"/>
    <mkdir      dir="${tf.build.docs}"/>
    <javadoc    destdir="${tf.build.docs}"
                 access="protected"
                 author="false"
                version="false"
                   link="${javadoc.links}"
               overview="${source.test}/org/apache/commons/collections/overview.html"
               doctitle="${tf.title} ${tf.version} API;"
            windowtitle="${tf.title} ${tf.version} API"
                 bottom="Copyright &amp;copy; 2001-${year} Apache Software Foundation. All Rights Reserved.">
      <fileset dir="${source.test}">
        <include name="**/AbstractTest*.java"/>
        <include name="**/BulkTest*.java"/>
      </fileset>
    </javadoc>
  </target>


  <!-- ====================================================================== -->
  <!-- ========== Distributions ============================================= -->
  <!-- ====================================================================== -->

  <!-- ====================================================================== -->

  <!-- Target needed for nightly builds -->
  <target name="dist" depends="javadoc,dist.create"
          description="Create distribution folders">
    <delete dir="${dist.home}"/>
    <mkdir dir="${dist.home}" />
    <copy todir="${dist.home}">
      <fileset dir="${build.dist.bin}" />
    </copy>
  </target>

  <target name="dist.create" depends="jar,testjar,tf.validate,tf.jar,dist.bin,dist.src" />

  <target name="dist.bin">
    <copy todir="${build.src}">
      <fileset dir="${basedir}/src/java" includes="**/*.java" />
    </copy>
    <copy todir="${build.src}/META-INF">
      <fileset dir="${basedir}" includes="LICENSE*, NOTICE*" />
    </copy>
    <jar jarfile="${build.home}/${final.name}-src-ide.zip" basedir="${build.src}" />
    <antcall target="internal-md5">
      <param name="path" value="${build.home}/${final.name}.jar"/>
    </antcall>

    <mkdir      dir="${build.dist.bin.work}"/>
    <copy     todir="${build.dist.bin.work}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="README.txt"/>
        <include name="RELEASE-NOTES.html"/>
      </fileset>
    </copy>
    <copy     todir="${build.dist.bin.work}">
      <fileset dir="${build.home}">
        <include name="*.jar"/>
        <include name="docs/**"/>
      </fileset>
    </copy>
  </target>

  <target name="dist.src">
    <mkdir      dir="${build.dist.src.work}"/>
    <copy     todir="${build.dist.src.work}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="README.txt"/>
        <include name="RELEASE-NOTES.html"/>
        <include name="DEVELOPERS-GUIDE.html"/>
        <include name="PROPOSAL.html"/>
        <include name="STATUS.html"/>
        <include name="build.xml"/>
        <include name="build-testframework.xml"/>
        <include name="checkstyle.xml"/>
        <include name="maven.xml"/>
        <include name="project.xml"/>
        <include name="project.properties"/>
        <include name="pom.xml"/>
      </fileset>
    </copy>
    <copy     todir="${build.dist.src.work}">
      <fileset dir="${build.home}">
        <include name="${final.name}.jar"/>
      </fileset>
    </copy>
    <copy     todir="${build.dist.src.work}">
      <fileset dir=".">
        <include name="data/**"/>
        <include name="src/**"/>
        <include name="xdocs/**"/>
      </fileset>
    </copy>
  </target>

  <!-- ====================================================================== -->

  <target name="release" depends="dist.create,zip"
          description="Create release">
    <!-- POM -->
    <copy file="project.xml" tofile="${build.home}/${final.name}.pom" />
    <antcall target="internal-md5">
      <param name="path" value="${build.home}/${final.name}.pom"/>
    </antcall>
  </target>

  <target name="zip" depends="zip.bin,zip.src">
  </target>

  <target name="zip.bin">
    <mkdir dir="${build.dist}"/>
    <fixcrlf srcdir="${build.dist.bin.work}" eol="lf" includes="*.txt" />
    <tar longfile="gnu" tarfile="${build.bin.tar.name}">
      <tarfileset dir="${build.dist.bin}"/>
    </tar>
    <gzip zipfile="${build.bin.gz.name}" src="${build.bin.tar.name}"/>
    <delete file="${build.bin.tar.name}" />
    <antcall target="internal-md5">
      <param name="path" value="${build.bin.gz.name}"/>
    </antcall>

    <fixcrlf srcdir="${build.dist.bin.work}" eol="crlf" includes="*.txt" />
    <zip zipfile="${build.bin.zip.name}" >
      <zipfileset dir="${build.dist.bin}"/>
    </zip>
    <antcall target="internal-md5">
      <param name="path" value="${build.bin.zip.name}"/>
    </antcall>
  </target>

  <target name="zip.src">
    <mkdir dir="${build.dist}"/>
    <fixcrlf srcdir="${build.dist.src.work}" eol="lf" includes="*.txt,*.properties" />
    <tar longfile="gnu" tarfile="${build.src.tar.name}">
      <tarfileset dir="${build.dist.src}"/>
    </tar>
    <gzip zipfile="${build.src.gz.name}" src="${build.src.tar.name}"/>
    <delete file="${build.src.tar.name}" />
    <antcall target="internal-md5">
      <param name="path" value="${build.src.gz.name}"/>
    </antcall>

    <fixcrlf srcdir="${build.dist.src.work}" eol="crlf" includes="*.txt,*.properties" />
    <zip zipfile="${build.src.zip.name}" >
      <zipfileset dir="${build.dist.src}"/>
    </zip>
    <antcall target="internal-md5">
      <param name="path" value="${build.src.zip.name}"/>
    </antcall>
  </target>

  <target name="internal-md5">
    <basename property="_base" file="${path}"/>
    <checksum file="${path}" property="md5"/>
    <echo message="${md5} *${_base}" file="${path}.md5"/>
  </target>

  <!-- ====================================================================== -->
  <target name="clirr">
    <taskdef resource="clirrtask.properties">
      <classpath path="${maven.repo}/clirr/jars/clirr-core-0.6-uber.jar;" />
    </taskdef>
    <clirr>
      <origfiles dir="${maven.repo}/commons-collections/jars" includes="commons-collections-3.1.jar"/>
      <newfiles dir="${build.home}" includes="${final.name}.jar" />
      <formatter type="plain" outfile="${build.home}/clirr.txt" />
    </clirr>
  </target>

  <!-- ====================================================================== -->
  <target name="deprecated.jar" description="Compiles a jar of deprecated classes that existed in 3.* releases, and are necessary for other dependencies">
    <jar jarfile="commons-collections-deprecated.jar">
    	<zipfileset src="${collections-3.jar}">
  			<!-- used by Hibernate -->
    		<include name="**/SequencedHashMap*"/>
  			<include name="**/KeyValue*"/> <!-- used by SequencedHashMap --> 
    		<include name="**/LRUMap*"/>
    		<include name="**/ReferenceMap*"/>
    	</zipfileset>
   	</jar>
  </target>
</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>28</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-collections</groupId>
  <artifactId>commons-collections</artifactId>
  <version>4.0-SNAPSHOT</version>
  <name>Commons Collections</name>

  <inceptionYear>2001</inceptionYear>
  <description>Types that extend and augment the Java Collections Framework.</description>

  <url>http://commons.apache.org/collections/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/COLLECTIONS</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/collections/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/collections/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/collections/trunk</url>
  </scm>

  <developers>
    <developer>
      <name>Matt Benson</name>
      <id>mbenson</id>
    </developer>
    <developer>
      <name>James Carman</name>
      <id>jcarman</id>
    </developer>
    <developer>
      <name>Stephen Colebourne</name>
      <id>scolebourne</id>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
    </developer>
    <developer>
      <name>Morgan Delagrange</name>
      <id>morgand</id>
    </developer>
    <developer>
      <name>Gary D. Gregory</name>
      <id>ggregory</id>
    </developer>
    <developer>
      <name>Matthew Hawthorne</name>
      <id>matth</id>
    </developer>
    <developer>
      <name>Geir Magnusson</name>
      <id>geirm</id>
    </developer>
    <developer>
      <name>Luc Maisonobe</name>
      <id>luc</id>
    </developer>
    <developer>
      <name>Craig McClanahan</name>
      <id>craigmcc</id>
    </developer>
    <developer>
      <name>Adrian Nistor</name>
      <id>adriannistor</id>
    </developer>
    <developer>
      <name>Phil Steitz</name>
      <id>psteitz</id>
    </developer>
    <developer>
      <name>Arun M. Thomas</name>
      <id>amamment</id>
    </developer>
    <developer>
      <name>Rodney Waldhoff</name>
      <id>rwaldhoff</id>
    </developer>
    <developer>
      <name>Henri Yandell</name>
      <id>bayard</id>
    </developer>
    <developer>
      <name>Thomas Neidhart</name>
      <id>tn</id>
    </developer>
  </developers>
  
  <contributors>
    <contributor>
      <name>Rafael U. C. Afonso</name>
    </contributor>
    <contributor>
      <name>Max Rydahl Andersen</name>
    </contributor>
    <contributor>
      <name>Avalon</name>
    </contributor>
    <contributor>
      <name>Federico Barbieri</name>
    </contributor>
    <contributor>
      <name>Arron Bates</name>
    </contributor>
    <contributor>
      <name>Nicola Ken Barozzi</name>
    </contributor>
    <contributor>
      <name>Sebastian Bazley</name>
    </contributor>
    <contributor>
      <name>Benjamin Bentmann</name>
    </contributor>
    <contributor>
      <name>Ola Berg</name>
    </contributor>
    <contributor>
      <name>Sam Berlin</name>
    </contributor>
    <contributor>
      <name>Christopher Berry</name>
    </contributor>
    <contributor>
      <name>Nathan Beyer</name>
    </contributor>
    <contributor>
      <name>Rune Peter Bjørnstad</name>
    </contributor>
    <contributor>
      <name>Janek Bogucki</name>
    </contributor>
    <contributor>
      <name>Maarten Brak</name>
    </contributor>
    <contributor>
      <name>Dave Bryson</name>
    </contributor>
    <contributor>
      <name>Chuck Burdick</name>
    </contributor>
    <contributor>
      <name>Julien Buret</name>
    </contributor>
    <contributor>
      <name>Jonathan Carlson</name>
    </contributor>
    <contributor>
      <name>Ram Chidambaram</name>
    </contributor>
    <contributor>
      <name>Steve Clark</name>
    </contributor>
    <contributor>
      <name>Eric Crampton</name>
    </contributor>
    <contributor>
      <name>Dimiter Dimitrov</name>
    </contributor>
    <contributor>
      <name>Peter Donald</name>
    </contributor>
    <contributor>
      <name>Steve Downey</name>
    </contributor>
    <contributor>
      <name>Rich Dougherty</name>
    </contributor>
    <contributor>
      <name>Tom Dunham</name>
    </contributor>
    <contributor>
      <name>Stefano Fornari</name>
    </contributor>
    <contributor>
      <name>Andrew Freeman</name>
    </contributor>
    <contributor>
      <name>Gerhard Froehlich</name>
    </contributor>
    <contributor>
      <name>Goran Hacek</name>
    </contributor>
    <contributor>
      <name>Mario Ivankovits</name>
    </contributor>
    <contributor>
      <name>Paul Jack</name>
    </contributor>
    <contributor>
      <name>Eric Johnson</name>
    </contributor>
    <contributor>
      <name>Kent Johnson</name>
    </contributor>
    <contributor>
      <name>Marc Johnson</name>
    </contributor>
    <contributor>
      <name>Roger Kapsi</name>
    </contributor>
    <contributor>
      <name>Nissim Karpenstein</name>
    </contributor>
    <contributor>
      <name>Shinobu Kawai</name>
    </contributor>
    <contributor>
      <name>Stephen Kestle</name>
    </contributor>
    <contributor>
      <name>Mohan Kishore</name>
    </contributor>
    <contributor>
      <name>Simon Kitching</name>
    </contributor>
    <contributor>
      <name>Thomas Knych</name>
    </contributor>
    <contributor>
      <name>Serge Knystautas</name>
    </contributor>
    <contributor>
      <name>Peter KoBek</name>
    </contributor>
    <contributor>
      <name>Jordan Krey</name>
    </contributor>
    <contributor>
      <name>Olaf Krische</name>
    </contributor>
    <contributor>
      <name>Guilhem Lavaux</name>
    </contributor>
    <contributor>
      <name>Paul Legato</name>
    </contributor>
    <contributor>
      <name>David Leppik</name>
    </contributor>
    <contributor>
      <name>Berin Loritsch</name>
    </contributor>
    <contributor>
      <name>Hendrik Maryns</name>
    </contributor>
    <contributor>
      <name>Stefano Mazzocchi</name>
    </contributor>
    <contributor>
      <name>Brian McCallister</name>
    </contributor>
    <contributor>
      <name>David Meikle</name>
    </contributor>
    <contributor>
      <name>Steven Melzer</name>
    </contributor>
    <contributor>
      <name>Leon Messerschmidt</name>
    </contributor>
    <contributor>
      <name>Mauricio S. Moura</name>
    </contributor>
    <contributor>
      <name>Kasper Nielsen</name>
    </contributor>
    <contributor>
      <name>Stanislaw Osinski</name>
    </contributor>
    <contributor>
      <name>Alban Peignier</name>
    </contributor>
    <contributor>
      <name>Mike Pettypiece</name>
    </contributor>
    <contributor>
      <name>Steve Phelps</name>
    </contributor>
    <contributor>
      <name>Ilkka Priha</name>
    </contributor>
    <contributor>
      <name>Jonas Van Poucke</name>
    </contributor>
    <contributor>
      <name>Will Pugh</name>
    </contributor>
    <contributor>
      <name>Herve Quiroz</name>
    </contributor>
    <contributor>
      <name>Daniel Rall</name>
    </contributor>
    <contributor>
      <name>Robert Ribnitz</name>
    </contributor>
    <contributor>
      <name>Huw Roberts</name>
    </contributor>
    <contributor>
      <name>Henning P. Schmiedehausen</name>
    </contributor>
    <contributor>
      <name>Joerg Schmuecker</name>
    </contributor>
    <contributor>
      <name>Howard Lewis Ship</name>
    </contributor>
    <contributor>
      <name>Joe Raysa</name>
    </contributor>
    <contributor>
      <name>Ashwin S</name>
    </contributor>
    <contributor>
      <name>Jordane Sarda</name>
    </contributor>
    <contributor>
      <name>Thomas Schapitz</name>
    </contributor>
    <contributor>
      <name>Jon Schewe</name>
    </contributor>
    <contributor>
      <name>Andreas Schlosser</name>
    </contributor>
    <contributor>
      <name>Christian Siefkes</name>
    </contributor>
    <contributor>
      <name>Michael Smith</name>
    </contributor>
    <contributor>
      <name>Stephen Smith</name>
    </contributor>
    <contributor>
      <name>Jan Sorensen</name>
    </contributor>
    <contributor>
      <name>Jon S. Stevens</name>
    </contributor>
    <contributor>
      <name>James Strachan</name>
    </contributor>
    <contributor>
      <name>Leo Sutic</name>
    </contributor>
    <contributor>
      <name>Chris Tilden</name>
    </contributor>
    <contributor>
      <name>Neil O'Toole</name>
    </contributor>
    <contributor>
      <name>Jeff Turner</name>
    </contributor>
    <contributor>
      <name>Kazuya Ujihara</name>
    </contributor>
    <contributor>
      <name>Thomas Vahrst</name>
    </contributor>
    <contributor>
      <name>Jeff Varszegi</name>
    </contributor>
    <contributor>
      <name>Ralph Wagner</name>
    </contributor>
    <contributor>
      <name>David Weinrich</name>
    </contributor>
    <contributor>
      <name>Dieter Wimberger</name>
    </contributor>
    <contributor>
      <name>Serhiy Yevtushenko</name>
    </contributor>
    <contributor>
      <name>Sai Zhang</name>
    </contributor>
    <contributor>
      <name>Jason van Zyl</name>
    </contributor>
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.easymock</groupId>
      <artifactId>easymock</artifactId>
      <version>2.5.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <distributionManagement>
    <site>
      <id>apache.website</id>
      <name>Apache Commons Site</name>
      <url>${commons.deployment.protocol}://people.apache.org/www/commons.apache.org/${commons.componentid}</url>
    </site>
  </distributionManagement>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <maven.compile.source>1.5</maven.compile.source>

    <maven.compile.target>1.5</maven.compile.target>
    <commons.componentid>collections</commons.componentid>
    <commons.release.version>3.2.1</commons.release.version>
    <commons.osgi.symbolicName>org.apache.commons.collections</commons.osgi.symbolicName>
    <commons.jira.id>COLLECTIONS</commons.jira.id>
    <commons.jira.pid>12310465</commons.jira.pid>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC2</commons.rc.version>
    <checkstyle.version>2.9.1</checkstyle.version>
    <commons.scmPubCheckoutDirectory>site-content</commons.scmPubCheckoutDirectory>
  </properties>


  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
          <configuration>
            <includes>
              <include>**/*Test.java</include>
            </includes>
            <excludes>
              <exclude>**/*$*</exclude>
              <exclude>**/TestUtils.java</exclude>
              <exclude>**/Abstract*.java</exclude>
              <exclude>**/BulkTest.java</exclude>
            </excludes>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptors>
            <descriptor>src/main/assembly/bin.xml</descriptor>
            <descriptor>src/main/assembly/src.xml</descriptor>
          </descriptors>
          <tarLongFileMode>gnu</tarLongFileMode>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${checkstyle.version}</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-scm-publish-plugin</artifactId>
        <configuration>
          <ignorePathsToDelete>
            <ignorePathToDelete>javadocs</ignorePathToDelete>
          </ignorePathsToDelete>
        </configuration>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${checkstyle.version}</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
	      <suppressionsLocation>${basedir}/checkstyle-suppressions.xml</suppressionsLocation>
        </configuration>
	    <reportSets>
          <reportSet>
            <reports>
              <report>checkstyle</report>
            </reports>
          </reportSet>
        </reportSets>         
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.4.0</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.5.1</version>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.7.1</version>
        <configuration>
          <targetJdk>${maven.compile.target}</targetJdk>  
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>pmd</report>
              <report>cpd</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
    </plugins>
  </reporting>

  <profiles>
    <profile>
      <id>setup-checkout</id>
      <activation>
        <file>
          <missing>site-content</missing>
        </file>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>1.7</version>
            <executions>
              <execution>
                <id>prepare-checkout</id>
                <phase>pre-site</phase>
                <goals>
                  <goal>run</goal>
                </goals>
                <configuration>
                  <tasks>
                    <exec executable="svn">
                      <arg line="checkout --depth immediates ${commons.scmPubUrl} ${commons.scmPubCheckoutDirectory}" />
                    </exec>

                    <exec executable="svn">
                      <arg line="update --set-depth exclude ${commons.scmPubCheckoutDirectory}/javadocs" />
                    </exec>

                    <pathconvert pathsep=" " property="dirs">
                      <dirset dir="${commons.scmPubCheckoutDirectory}" includes="*" />
                    </pathconvert>
                    <exec executable="svn">
                      <arg line="update --set-depth infinity ${dirs}" />
                    </exec>
                  </tasks>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>

</project>

```

## src/main/java/org/apache/commons/collections/list/SetUniqueList.java

```
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.collections.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections.iterators.AbstractListIteratorDecorator;
import org.apache.commons.collections.set.UnmodifiableSet;

/**
 * Decorates a <code>List</code> to ensure that no duplicates are present much
 * like a <code>Set</code>.
 * <p>
 * The <code>List</code> interface makes certain assumptions/requirements. This
 * implementation breaks these in certain ways, but this is merely the result of
 * rejecting duplicates. Each violation is explained in the method, but it
 * should not affect you. Bear in mind that Sets require immutable objects to
 * function correctly.
 * <p>
 * The {@link org.apache.commons.collections.set.ListOrderedSet ListOrderedSet}
 * class provides an alternative approach, by wrapping an existing Set and
 * retaining insertion order in the iterator.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 * 
 * @since 3.0
 * @version $Id$
 */
public class SetUniqueList<E> extends AbstractSerializableListDecorator<E> {

    /** Serialization version. */
    private static final long serialVersionUID = 7196982186153478694L;

    /** Internal Set to maintain uniqueness. */
    protected final Set<E> set;

    /**
     * Factory method to create a SetList using the supplied list to retain
     * order.
     * <p>
     * If the list contains duplicates, these are removed (first indexed one
     * kept). A <code>HashSet</code> is used for the set behaviour.
     * 
     * @param <E>
     *            the element type
     * @param list
     *            the list to decorate, must not be null
     * @return a new {@link SetUniqueList}
     * @throws IllegalArgumentException
     *             if list is null
     */
    public static <E> SetUniqueList<E> setUniqueList(final List<E> list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        if (list.isEmpty()) {
            return new SetUniqueList<E>(list, new HashSet<E>());
        }
        final List<E> temp = new ArrayList<E>(list);
        list.clear();
        final SetUniqueList<E> sl = new SetUniqueList<E>(list, new HashSet<E>());
        sl.addAll(temp);
        return sl;
    }

    // -----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies) the List and specifies the set to
     * use.
     * <p>
     * The set and list must both be correctly initialised to the same elements.
     * 
     * @param set
     *            the set to decorate, must not be null
     * @param list
     *            the list to decorate, must not be null
     * @throws IllegalArgumentException
     *             if set or list is null
     */
    protected SetUniqueList(final List<E> list, final Set<E> set) {
        super(list);
        if (set == null) {
            throw new IllegalArgumentException("Set must not be null");
        }
        this.set = set;
    }

    // -----------------------------------------------------------------------
    /**
     * Gets an unmodifiable view as a Set.
     * 
     * @return an unmodifiable set view
     */
    public Set<E> asSet() {
        return UnmodifiableSet.unmodifiableSet(set);
    }

    // -----------------------------------------------------------------------
    /**
     * Adds an element to the list if it is not already present.
     * <p>
     * <i>(Violation)</i> The <code>List</code> interface requires that this
     * method returns <code>true</code> always. However this class may return
     * <code>false</code> because of the <code>Set</code> behaviour.
     * 
     * @param object
     *            the object to add
     * @return true if object was added
     */
    @Override
    public boolean add(final E object) {
        // gets initial size
        final int sizeBefore = size();

        // adds element if unique
        add(size(), object);

        // compares sizes to detect if collection changed
        return sizeBefore != size();
    }

    /**
     * Adds an element to a specific index in the list if it is not already
     * present.
     * <p>
     * <i>(Violation)</i> The <code>List</code> interface makes the assumption
     * that the element is always inserted. This may not happen with this
     * implementation.
     * 
     * @param index
     *            the index to insert at
     * @param object
     *            the object to add
     */
    @Override
    public void add(final int index, final E object) {
        // adds element if it is not contained already
        if (set.contains(object) == false) {
            super.add(index, object);
            set.add(object);
        }
    }

    /**
     * Adds a collection of objects to the end of the list avoiding duplicates.
     * <p>
     * Only elements that are not already in this list will be added, and
     * duplicates from the specified collection will be ignored.
     * <p>
     * <i>(Violation)</i> The <code>List</code> interface makes the assumption
     * that the elements are always inserted. This may not happen with this
     * implementation.
     * 
     * @param coll
     *            the collection to add in iterator order
     * @return true if this collection changed
     */
    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return addAll(size(), coll);
    }

    /**
     * Adds a collection of objects a specific index in the list avoiding
     * duplicates.
     * <p>
     * Only elements that are not already in this list will be added, and
     * duplicates from the specified collection will be ignored.
     * <p>
     * <i>(Violation)</i> The <code>List</code> interface makes the assumption
     * that the elements are always inserted. This may not happen with this
     * implementation.
     * 
     * @param index
     *            the index to insert at
     * @param coll
     *            the collection to add in iterator order
     * @return true if this collection changed
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        final List<E> temp = new ArrayList<E>();
        for (final E e : coll) {
            if (set.add(e)) {
                temp.add(e);
            }
        }
        return super.addAll(index, temp);
    }

    // -----------------------------------------------------------------------
    /**
     * Sets the value at the specified index avoiding duplicates.
     * <p>
     * The object is set into the specified index. Afterwards, any previous
     * duplicate is removed If the object is not already in the list then a
     * normal set occurs. If it is present, then the old version is removed.
     * 
     * @param index
     *            the index to insert at
     * @param object
     *            the object to set
     * @return the previous object
     */
    @Override
    public E set(final int index, final E object) {
        final int pos = indexOf(object);
        final E removed = super.set(index, object);

        if (pos != -1 && pos != index) {
            // the object is already in the uniq list
            // (and it hasn't been swapped with itself)
            super.remove(pos); // remove the duplicate by index
            set.remove(removed); // remove the item deleted by the set
        } else if (pos == -1) {
            set.add(object); // add the new item to the unique set
            set.remove(removed); // remove the item deleted by the set
        }
        
        return removed; // return the item deleted by the set
    }

    @Override
    public boolean remove(final Object object) {
        final boolean result = set.remove(object);
        if (result) {
            super.remove(object);
        }
        return result;
    }

    @Override
    public E remove(final int index) {
        final E result = super.remove(index);
        set.remove(result);
        return result;
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean result = false;
        for (final Object name : coll) {
            result |= remove(name);
        }
        return result;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        final Set<Object> setRetainAll = new HashSet<Object>();
        for (final Object next : coll) {
            if (set.contains(next)) {
                setRetainAll.add(next);
            }
        }
        if (setRetainAll.size() == set.size()) {
            return false;
        }
        if (setRetainAll.size() == 0) {
            clear();
        } else {
            for (final Iterator<E> it = iterator(); it.hasNext();) {
                if (!setRetainAll.contains(it.next())) {
                    it.remove();
                }
            }
        }
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        set.clear();
    }

    @Override
    public boolean contains(final Object object) {
        return set.contains(object);
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        return set.containsAll(coll);
    }

    @Override
    public Iterator<E> iterator() {
        return new SetListIterator<E>(super.iterator(), set);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SetListListIterator<E>(super.listIterator(), set);
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return new SetListListIterator<E>(super.listIterator(index), set);
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> superSubList = super.subList(fromIndex, toIndex);
        final Set<E> subSet = createSetBasedOnList(set, superSubList);
        return new SetUniqueList<E>(superSubList, subSet);
    }

    /**
     * Create a new {@link Set} with the same type as the provided {@code set}
     * and populate it with all elements of {@code list}.
     * 
     * @param set
     *            the {@link Set} to be used as return type, must not be null
     * @param list
     *            the {@link List} to populate the {@link Set}
     * @return a new {@link Set} populated with all elements of the provided
     *         {@link List}
     */
    @SuppressWarnings("unchecked")
    protected Set<E> createSetBasedOnList(final Set<E> set, final List<E> list) {
        Set<E> subSet;
        if (set.getClass().equals(HashSet.class)) {
            subSet = new HashSet<E>(list.size());
        } else {
            try {
                subSet = set.getClass().newInstance();
            } catch (final InstantiationException ie) {
                subSet = new HashSet<E>();
            } catch (final IllegalAccessException iae) {
                subSet = new HashSet<E>();
            }
        }
        subSet.addAll(list);
        return subSet;
    }

    // -----------------------------------------------------------------------
    /**
     * Inner class iterator.
     */
    static class SetListIterator<E> extends AbstractIteratorDecorator<E> {

        protected final Set<E> set;
        protected E last = null;

        protected SetListIterator(final Iterator<E> it, final Set<E> set) {
            super(it);
            this.set = set;
        }

        @Override
        public E next() {
            last = super.next();
            return last;
        }

        @Override
        public void remove() {
            super.remove();
            set.remove(last);
            last = null;
        }
    }

    /**
     * Inner class iterator.
     */
    static class SetListListIterator<E> extends
            AbstractListIteratorDecorator<E> {

        protected final Set<E> set;
        protected E last = null;

        protected SetListListIterator(final ListIterator<E> it, final Set<E> set) {
            super(it);
            this.set = set;
        }

        @Override
        public E next() {
            last = super.next();
            return last;
        }

        @Override
        public E previous() {
            last = super.previous();
            return last;
        }

        @Override
        public void remove() {
            super.remove();
            set.remove(last);
            last = null;
        }

        @Override
        public void add(final E object) {
            if (set.contains(object) == false) {
                super.add(object);
                set.add(object);
            }
        }

        @Override
        public void set(final E object) {
            throw new UnsupportedOperationException("ListIterator does not support set");
        }
    }

}

```
