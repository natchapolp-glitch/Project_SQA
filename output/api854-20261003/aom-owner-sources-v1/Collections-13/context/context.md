## build.xml

```
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

  <!-- This can be used to define 'junit.jar' property if necessary -->
  <property file="build.properties"/>

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
  <property name="component.version"       value="3.3-SNAPSHOT"/>

  <!-- The base directory for component configuration files -->
  <property name="source.conf"               value="src/conf"/>

  <!-- The base directory for component sources -->
  <property name="source.java"             value="src/java"/>

  <!-- The base directory for unit test sources -->
  <property name="source.test"             value="src/test"/>

  <!-- The directories for compilation targets -->
  <property name="build.home"              value="build"/>
  <property name="build.conf"              value="${build.home}/conf"/>
  <property name="build.classes"           value="${build.home}/classes"/>
  <property name="build.tests"             value="${build.home}/tests"/>
  <property name="build.test.reports"      value="${build.home}/test-reports"/>
  <property name="build.docs"              value="${build.home}/docs/apidocs"/>
  <property name="build.src"               value="${build.home}/src-ide" />
  
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
  <property name="dist.home"            value="dist"/> <!-- for nightly builds -->


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
          optimize="${compile.optimize}">
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
          optimize="false">
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>
    </javac>
  </target>

<!-- ====================================================================== -->

  <!-- Tests collections, either running all or one test -->
  <target name="test" depends="-test-all,-test-single"
          description="Run unit tests" />

  <!-- Runs all tests -->
  <target name="-test-all" depends="compile.tests" unless="testcase">
    <mkdir dir="${build.test.reports}"/>
    <junit printsummary="yes" haltonfailure="yes" showoutput="yes">
      <formatter type="brief" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <batchtest fork="yes" todir="${build.test.reports}">
        <fileset dir="${source.test}">
          <include name="**/Test*.java"/>
          <exclude name="**/TestAll*.java"/>
          <exclude name="**/TestAbstract*"/>
          <exclude name="**/TestArrayList.java"/>
          <exclude name="**/TestLinkedList.java"/>
          <exclude name="**/TestHashMap.java"/>
          <exclude name="**/TestTreeMap.java"/>
          <exclude name="**/TestTypedCollection.java"/>
        </fileset>
        <formatter type="brief" usefile="false" />
      </batchtest>
    </junit>
  </target>

  <!-- Runs a single test -->
  <target name="-test-single" depends="compile.tests" if="testcase">
    <junit printsummary="yes" haltonfailure="yes" showoutput="yes">
      <formatter type="brief" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <test name="${testcase}" fork="yes" todir="${build.test.reports}">
        <formatter type="brief" usefile="false" />
      </test>
    </junit>
  </target>

<!-- ====================================================================== -->

  <target name="testjar"  depends="compile.tests,jar"
          description="Run all unit test cases">
    <echo message="Running collections tests against built jar ..."/>
    <junit printsummary="yes" haltonfailure="yes">
      <classpath>
        <pathelement location="${build.jar.name}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <batchtest fork="yes">
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
    <tstamp><format property="year" pattern="yyyy"/></tstamp>
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
          optimize="false">
      <patternset refid="tf.patternset.validate" />
      <classpath>
        <pathelement location="${junit.jar}"/>
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
    <tstamp><format property="year" pattern="yyyy"/></tstamp>
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

  <target name="dist.create" depends="jar,testjar,tf.validate,tf.jar,dist.bin,dist.src">
  </target>

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

</project>

```

## pom.xml

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
   See the License for the specific collectionsuage governing permissions and
   limitations under the License.
-->
<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>11</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-collections</groupId>
  <artifactId>commons-collections</artifactId>
  <version>3.3-SNAPSHOT</version>
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
      <name>Stephen Colebourne</name>
      <id>scolebourne</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Morgan Delagrange</name>
      <id>morgand</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Matthew Hawthorne</name>
      <id>matth</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Geir Magnusson</name>
      <id>geirm</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Craig McClanahan</name>
      <id>craigmcc</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Phil Steitz</name>
      <id>psteitz</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Arun M. Thomas</name>
      <id>amamment</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Rodney Waldhoff</name>
      <id>rwaldhoff</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Henri Yandell</name>
      <id>bayard</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>James Carman</name>
      <id>jcarman</id>
      <email></email>
      <organization></organization>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
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
      <name>Matt Benson</name>
    </contributor>
    <contributor>
      <name>Ola Berg</name>
    </contributor>
    <contributor>
      <name>Christopher Berry</name>
    </contributor>
    <contributor>
      <name>Nathan Beyer</name>
    </contributor>
    <contributor>
      <name>Janek Bogucki</name>
    </contributor>
    <contributor>
      <name>Chuck Burdick</name>
    </contributor>
    <contributor>
      <name>Dave Bryson</name>
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
      <name>Nissim Karpenstein</name>
    </contributor>
    <contributor>
      <name>Shinobu Kawai</name>
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
      <name>Howard Lewis Ship</name>
    </contributor>
    <contributor>
      <name>Joe Raysa</name>
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
      <name>Jon S. Stevens </name>
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
      <name>Jason van Zyl</name>
    </contributor>
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <commons.componentid>collections</commons.componentid>
    <commons.release.version>3.2.1</commons.release.version>
    <commons.jira.id>COLLECTIONS</commons.jira.id>
    <commons.jira.pid>12310465</commons.jira.pid>
  </properties> 

  <build>
    <sourceDirectory>src/java</sourceDirectory>
    <testSourceDirectory>src/test</testSourceDirectory>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>org/apache/commons/collections/TestAllPackages.java</include>
              </includes>
          </configuration>
        </plugin>
        <plugin>
          <artifactId>maven-antrun-plugin</artifactId>
          <executions>
            <execution>
              <phase>package</phase>
              <configuration>
                <tasks>
                  <ant antfile="build-testframework.xml" target="jar">
                    <property name="component.version"    value="${project.version}"/>
                    <property name="test.classes"         value="${project.build.directory}/test-classes"/>
                    <property name="dist.home"            value="${project.build.directory}"/>
                    <property name="maven.compile.source" value="${maven.compile.source}"/>
                    <property name="maven.compile.target" value="${maven.compile.target}"/>
                  </ant>
                </tasks>
              </configuration>
              <goals>
                <goal>run</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
        <plugin>
          <artifactId>maven-assembly-plugin</artifactId>
          <configuration>
            <descriptors>
              <descriptor>src/assembly/bin.xml</descriptor>
              <descriptor>src/assembly/src.xml</descriptor>
            </descriptors>
            <tarLongFileMode>gnu</tarLongFileMode>
          </configuration>
        </plugin>
      </plugins>
    </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <linksource>true</linksource>
          <links>
            <link>http://java.sun.com/javase/6/docs/api</link>
          </links>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

</project>

```

## project.properties

```
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

maven.repo.remote=http://repo1.maven.org/maven

maven.changelog.factory=org.apache.maven.svnlib.SvnChangeLogFactory

maven.xdoc.date=left
maven.xdoc.version=${pom.currentVersion}
maven.xdoc.developmentProcessUrl=http://commons.apache.org/charter.html
maven.xdoc.poweredby.image=maven-feather.png
maven.xdoc.copy.excludes=images/file.gif,images/folder-closed.gif,images/folder-open.gif,images/icon_alert.gif,images/icon_alertsml.gif,images/icon_arrowfolder1_sml.gif,images/icon_arrowfolder2_sml.gif,images/icon_arrowmembers1_sml.gif,images/icon_arrowmembers2_sml.gif,images/icon_arrowusergroups1_sml.gif,images/icon_arrowusergroups2_sml.gif,images/icon_confirmsml.gif,images/icon_help_lrg.gif,images/icon_infosml.gif,images/icon_members_sml.gif,images/icon_sortleft.gif,images/icon_sortright.gif,images/icon_usergroups_sml.gif,images/icon_waste_lrg.gif,images/icon_waste_sml.gif,images/none.png,images/nw_maj.gif,images/nw_maj_hi.gif,images/nw_med.gif,images/nw_med_hi.gif,images/nw_med_rond.gif,images/nw_min.gif,images/nw_min_036.gif,images/nw_min_hi.gif,images/poweredby_036.gif,images/product_logo.gif,images/se_maj_rond.gif,images/sw_min.gif,images/logos/**
maven.xdoc.copy.excludes.classic=images/external-classic.png,images/help_logo.gif,images/icon_arrowfolderclosed1_sml.gif,images/icon_arrowwaste1_sml.gif,images/icon_arrowwaste2_sml.gif,images/icon_doc_lrg.gif,images/icon_doc_sml.gif,images/icon_error_lrg.gif,images/icon_folder_lrg.gif,images/icon_folder_sml.gif,images/icon_help_sml.gif,images/icon_info_lrg.gif,images/icon_members_lrg.gif,images/icon_sortdown.gif,images/icon_sortup.gif,images/icon_success_lrg.gif,images/icon_usergroups_lrg.gif,images/icon_arrowfolderopen2_sml.gif,images/icon_warning_lrg.gif,images/newwindow-classic.png,images/nw_maj_rond.gif,images/strich.gif,images/sw_maj_rond.gif,images/sw_med_rond.gif

# Jar Manifest Additional Attributes
maven.jar.manifest.attributes.list=Implementation-Vendor-Id,X-Compile-Source-JDK,X-Compile-Target-JDK
maven.jar.manifest.attribute.Implementation-Vendor-Id=org.apache
maven.jar.manifest.attribute.X-Compile-Source-JDK=${maven.compile.source}
maven.jar.manifest.attribute.X-Compile-Target-JDK=${maven.compile.target}

maven.javadoc.author=false
maven.javadoc.links=http://java.sun.com/j2se/1.4/docs/api/
maven.javadoc.source=1.3
#maven.javadoc.additionalparam=-tag todo:a:"To Do:"
maven.javadoc.overview=src/java/org/apache/commons/collections/overview.html
#maven.javadoc.public=true
#maven.javadoc.package=false
#maven.javadoc.private=false

maven.checkstyle.properties=checkstyle.xml

maven.jdiff.new.tag=CURRENT
maven.jdiff.old.tag=COLLECTIONS_3_1

# Generate class files for specific VM version (e.g., 1.1 or 1.2). 
# Note that the default value depends on the JVM that is running Ant. 
# In particular, if you use JDK 1.4+ the generated classes will not be usable
# for a 1.1 Java VM unless you explicitly set this attribute to the value 1.1 
# (which is the default value for JDK 1.1 to 1.3).
maven.compile.target = 1.6

# Specifies the source version for the Java compiler.
# Corresponds to the source attribute for the ant javac task. 
# Valid values are 1.3, 1.4, 1.5. 
maven.compile.source = 1.6

maven.compile.debug=on
maven.compile.deprecation=off
maven.compile.optimize=off

maven.jarResources.basedir=src/java
maven.jar.excludes=**/package.html
maven.junit.fork=true

clover.excludes=**/Test*.java

```

## src/java/org/apache/commons/collections/ExtendedProperties.java

```
/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class extends normal Java properties by adding the possibility
 * to use the same key many times concatenating the value strings
 * instead of overwriting them.
 * <p>
 * <b>Please consider using the
 * {@link org.apache.commons.configuration.PropertiesConfiguration} class in
 * <a href="http://commons.apache.org/configuration">Commons Configuration</a>.
 * It's an evolution of <code>ExtendedProperties</code> supporting more
 * features like automatic reloading, advanced interpolation, more type
 * conversions, changes notifications and file layout preservation.</b>
 * <p>
 * The Extended Properties syntax is explained here:
 *
 * <ul>
 *  <li>
 *   Each property has the syntax <code>key = value</code>
 *  </li>
 *  <li>
 *   The <i>key</i> may use any character but the equal sign '='.
 *  </li>
 *  <li>
 *   <i>value</i> may be separated on different lines if a backslash
 *   is placed at the end of the line that continues below.
 *  </li>
 *  <li>
 *   If <i>value</i> is a list of strings, each token is separated
 *   by a comma ','.
 *  </li>
 *  <li>
 *   Commas in each token are escaped placing a backslash right before
 *   the comma.
 *  </li>
 *  <li>
 *   Backslashes are escaped by using two consecutive backslashes i.e. \\
 *  </li>
 *  <li>
 *   If a <i>key</i> is used more than once, the values are appended
 *   as if they were on the same line separated with commas.
 *  </li>
 *  <li>
 *   Blank lines and lines starting with character '#' are skipped.
 *  </li>
 *  <li>
 *   If a property is named "include" (or whatever is defined by
 *   setInclude() and getInclude() and the value of that property is
 *   the full path to a file on disk, that file will be included into
 *   the ConfigurationsRepository. You can also pull in files relative
 *   to the parent configuration file. So if you have something
 *   like the following:
 *
 *   include = additional.properties
 *
 *   Then "additional.properties" is expected to be in the same
 *   directory as the parent configuration file.
 * 
 *   Duplicate name values will be replaced, so be careful.
 *
 *  </li>
 * </ul>
 *
 * <p>Here is an example of a valid extended properties file:
 *
 * <p><pre>
 *      # lines starting with # are comments
 *
 *      # This is the simplest property
 *      key = value
 *
 *      # A long property may be separated on multiple lines
 *      longvalue = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa \
 *                  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
 *
 *      # This is a property with many tokens
 *      tokens_on_a_line = first token, second token
 *
 *      # This sequence generates exactly the same result
 *      tokens_on_multiple_lines = first token
 *      tokens_on_multiple_lines = second token
 *
 *      # commas may be escaped in tokens
 *      commas.escaped = Hi\, what'up?
 * </pre>
 *
 * <p><b>NOTE</b>: this class has <b>not</b> been written for
 * performance nor low memory usage.  In fact, it's way slower than it
 * could be and generates too much memory garbage.  But since
 * performance is not an issue during intialization (and there is not
 * much time to improve it), I wrote it this way.  If you don't like
 * it, go ahead and tune it up!
 *
 * @since Commons Collections 1.0
 * @version $Revision$ $Date$
 * 
 * @author <a href="mailto:stefano@apache.org">Stefano Mazzocchi</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:daveb@miceda-data">Dave Bryson</a>
 * @author <a href="mailto:jvanzyl@periapt.com">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @author <a href="mailto:kjohnson@transparent.com">Kent Johnson</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:ipriha@surfeu.fi">Ilkka Priha</a>
 * @author Janek Bogucki
 * @author Mohan Kishore
 * @author Stephen Colebourne
 * @author Shinobu Kawai
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 */
public class ExtendedProperties extends Hashtable {
    
    /**
     * Default configurations repository.
     */
    private ExtendedProperties defaults;

    /**
     * The file connected to this repository (holding comments and
     * such).
     *
     * @serial
     */
    protected String file;

    /**
     * Base path of the configuration file used to create
     * this ExtendedProperties object.
     */
    protected String basePath;

    /**
     * File separator.
     */
    protected String fileSeparator = System.getProperty("file.separator");

    /**
     * Has this configuration been intialized.
     */
    protected boolean isInitialized = false;

    /**
     * This is the name of the property that can point to other
     * properties file for including other properties files.
     */
    private String includePropertyName = null;

    /**
     * This is the default name of the property that can point to other
     * properties file for including other properties files.
     * 
     * @deprecated Use getInclude() and setInclude() methods which operate
     * on an instance variable from v3.3. Due to be removed in v4.0.
     */
    protected static String include = "include";

    /**
     * These are the keys in the order they listed
     * in the configuration file. This is useful when
     * you wish to perform operations with configuration
     * information in a particular order.
     */
    protected ArrayList keysAsListed = new ArrayList();

    protected final static String START_TOKEN="${";
    protected final static String END_TOKEN="}";


    /**
     * Interpolate key names to handle ${key} stuff
     *
     * @param base string to interpolate
     * @return returns the key name with the ${key} substituted
     */
    protected String interpolate(String base) {
        // COPIED from [configuration] 2003-12-29
        return (interpolateHelper(base, null));
    }

    /**
     * Recursive handler for multiple levels of interpolation.
     *
     * When called the first time, priorVariables should be null.
     *
     * @param base string with the ${key} variables
     * @param priorVariables serves two purposes: to allow checking for
     * loops, and creating a meaningful exception message should a loop
     * occur.  It's 0'th element will be set to the value of base from
     * the first call.  All subsequent interpolated variables are added
     * afterward.
     *
     * @return the string with the interpolation taken care of
     */
    protected String interpolateHelper(String base, List priorVariables) {
        // COPIED from [configuration] 2003-12-29
        if (base == null) {
            return null;
        }

        // on the first call initialize priorVariables
        // and add base as the first element
        if (priorVariables == null) {
            priorVariables = new ArrayList();
            priorVariables.add(base);
        }

        int begin = -1;
        int end = -1;
        int prec = 0 - END_TOKEN.length();
        String variable = null;
        StringBuffer result = new StringBuffer();

        // FIXME: we should probably allow the escaping of the start token
        while (((begin = base.indexOf(START_TOKEN, prec + END_TOKEN.length())) > -1)
            && ((end = base.indexOf(END_TOKEN, begin)) > -1)) {
            result.append(base.substring(prec + END_TOKEN.length(), begin));
            variable = base.substring(begin + START_TOKEN.length(), end);

            // if we've got a loop, create a useful exception message and throw
            if (priorVariables.contains(variable)) {
                String initialBase = priorVariables.remove(0).toString();
                priorVariables.add(variable);
                StringBuffer priorVariableSb = new StringBuffer();

                // create a nice trace of interpolated variables like so:
                // var1->var2->var3
                for (Iterator it = priorVariables.iterator(); it.hasNext();) {
                    priorVariableSb.append(it.next());
                    if (it.hasNext()) {
                        priorVariableSb.append("->");
                    }
                }

                throw new IllegalStateException(
                    "infinite loop in property interpolation of " + initialBase + ": " + priorVariableSb.toString());
            }
            // otherwise, add this variable to the interpolation list.
            else {
                priorVariables.add(variable);
            }

            //QUESTION: getProperty or getPropertyDirect
            Object value = getProperty(variable);
            if (value != null) {
                result.append(interpolateHelper(value.toString(), priorVariables));

                // pop the interpolated variable off the stack
                // this maintains priorVariables correctness for
                // properties with multiple interpolations, e.g.
                // prop.name=${some.other.prop1}/blahblah/${some.other.prop2}
                priorVariables.remove(priorVariables.size() - 1);
            } else if (defaults != null && defaults.getString(variable, null) != null) {
                result.append(defaults.getString(variable));
            } else {
                //variable not defined - so put it back in the value
                result.append(START_TOKEN).append(variable).append(END_TOKEN);
            }
            prec = end;
        }
        result.append(base.substring(prec + END_TOKEN.length(), base.length()));

        return result.toString();
    }
    
    /**
     * Inserts a backslash before every comma and backslash. 
     */
    private static String escape(String s) {
        StringBuffer buf = new StringBuffer(s);
        for (int i = 0; i < buf.length(); i++) {
            char c = buf.charAt(i);
            if (c == ',' || c == '\\') {
                buf.insert(i, '\\');
                i++;
            }
        }
        return buf.toString();
    }
    
    /**
     * Removes a backslash from every pair of backslashes. 
     */
    private static String unescape(String s) {
        StringBuffer buf = new StringBuffer(s);
        for (int i = 0; i < buf.length() - 1; i++) {
            char c1 = buf.charAt(i);
            char c2 = buf.charAt(i + 1);
            if (c1 == '\\' && c2 == '\\') {
                buf.deleteCharAt(i);
            }
        }
        return buf.toString();
    }

    /**
     * Counts the number of successive times 'ch' appears in the
     * 'line' before the position indicated by the 'index'.
     */
    private static int countPreceding(String line, int index, char ch) {
        int i;
        for (i = index - 1; i >= 0; i--) {
            if (line.charAt(i) != ch) {
                break;
            }
        }
        return index - 1 - i;
    }

    /**
     * Checks if the line ends with odd number of backslashes 
     */
    private static boolean endsWithSlash(String line) {
        if (!line.endsWith("\\")) {
            return false;
        }
        return (countPreceding(line, line.length() - 1, '\\') % 2 == 0);
    }

    /**
     * This class is used to read properties lines.  These lines do
     * not terminate with new-line chars but rather when there is no
     * backslash sign a the end of the line.  This is used to
     * concatenate multiple lines for readability.
     */
    static class PropertiesReader extends LineNumberReader {
        /**
         * Constructor.
         *
         * @param reader A Reader.
         */
        public PropertiesReader(Reader reader) {
            super(reader);
        }

        /**
         * Read a property.
         *
         * @return a String property
         * @throws IOException if there is difficulty reading the source.
         */
        public String readProperty() throws IOException {
            StringBuffer buffer = new StringBuffer();
            String line = readLine();
            while (line != null) {
                line = line.trim();
                if ((line.length() != 0) && (line.charAt(0) != '#')) {
                    if (endsWithSlash(line)) {
                        line = line.substring(0, line.length() - 1);
                        buffer.append(line);
                    } else {
                        buffer.append(line);
                        return buffer.toString();  // normal method end
                    }
                }
                line = readLine();
            }
            return null;  // EOF reached
        }
    }

    /**
     * This class divides into tokens a property value.  Token
     * separator is "," but commas into the property value are escaped
     * using the backslash in front.
     */
    static class PropertiesTokenizer extends StringTokenizer {
        /**
         * The property delimiter used while parsing (a comma).
         */
        static final String DELIMITER = ",";

        /**
         * Constructor.
         *
         * @param string A String.
         */
        public PropertiesTokenizer(String string) {
            super(string, DELIMITER);
        }

        /**
         * Check whether the object has more tokens.
         *
         * @return True if the object has more tokens.
         */
        public boolean hasMoreTokens() {
            return super.hasMoreTokens();
        }

        /**
         * Get next token.
         *
         * @return A String.
         */
        public String nextToken() {
            StringBuffer buffer = new StringBuffer();

            while (hasMoreTokens()) {
                String token = super.nextToken();
                if (endsWithSlash(token)) {
                    buffer.append(token.substring(0, token.length() - 1));
                    buffer.append(DELIMITER);
                } else {
                    buffer.append(token);
                    break;
                }
            }

            return buffer.toString().trim();
        }
    }

    /**
     * Creates an empty extended properties object.
     */
    public ExtendedProperties() {
        super();
    }

    /**
     * Creates and loads the extended properties from the specified file.
     *
     * @param file  the filename to load
     * @throws IOException if a file error occurs
     */
    public ExtendedProperties(String file) throws IOException {
        this(file, null);
    }

    /**
     * Creates and loads the extended properties from the specified file.
     *
     * @param file  the filename to load
     * @param defaultFile  a second filename to load default values from
     * @throws IOException if a file error occurs
     */
    public ExtendedProperties(String file, String defaultFile) throws IOException {
        this.file = file;

        basePath = new File(file).getAbsolutePath();
        basePath = basePath.substring(0, basePath.lastIndexOf(fileSeparator) + 1);

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            this.load(in);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {}
        }

        if (defaultFile != null) {
            defaults = new ExtendedProperties(defaultFile);
        }
    }

    /**
     * Indicate to client code whether property
     * resources have been initialized or not.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Gets the property value for including other properties files.
     * By default it is "include".
     * <p>
     * NOTE: Prior to v3.3 this method accessed a static variable.
     * It now accesses an instance variable. For compatability, if the
     * instance variable has not been set then the previous static
     * variable is then accessed. However, the protected static variable
     * can now only be set by subclasses.
     * In v4.0, the static variable will be removed.
     *
     * @return the property name which includes another property
     */
    public String getInclude() {
        if (includePropertyName == null) {
            return include;  // backwards compatability
        }
        if ("".equals(includePropertyName)) {
            return null;  // hack to allow backwards compatability
        }
        return includePropertyName;
    }

    /**
     * Sets the property value for including other properties files.
     * By default it is "include".
     * <p>
     * NOTE: Prior to v3.3 this method set a static variable and affected all
     * users of the class. It now sets an instance variable.
     * An empty string is also now converted to null internally.
     * In v4.0, the static variable will be removed.
     *
     * @param inc  the property name which includes another property, empty converted to null
     */
    public void setInclude(String inc) {
        if (inc == null) {
            inc = "";  // hack to allow backwards compatability
        }
        includePropertyName = inc;
    }

    /**
     * Load the properties from the given input stream.
     *
     * @param input  the InputStream to load from
     * @throws IOException if an IO error occurs
     */
    public void load(InputStream input) throws IOException {
        load(input, null);
    }

    /**
     * Load the properties from the given input stream
     * and using the specified encoding.
     *
     * @param input  the InputStream to load from
     * @param enc  the encoding to use
     * @throws IOException if an IO error occurs
     */
    public synchronized void load(InputStream input, String enc) throws IOException {
        PropertiesReader reader = null;
        if (enc != null) {
            try {
                reader = new PropertiesReader(new InputStreamReader(input, enc));
                
            } catch (UnsupportedEncodingException ex) {
                // Another try coming up....
            }
        }
        
        if (reader == null) {
            try {
                reader = new PropertiesReader(new InputStreamReader(input, "8859_1"));
                
            } catch (UnsupportedEncodingException ex) {
                // ISO8859-1 support is required on java platforms but....
                // If it's not supported, use the system default encoding
                reader = new PropertiesReader(new InputStreamReader(input));
            }
        }

        try {
            String includeProperty = getInclude();
            while (true) {
                String line = reader.readProperty();
                if (line == null) {
                    return;  // EOF
                }
                int equalSign = line.indexOf('=');

                if (equalSign > 0) {
                    String key = line.substring(0, equalSign).trim();
                    String value = line.substring(equalSign + 1).trim();

                    /* COLLECTIONS-238 allows empty properties to exist by commenting this out
                    // Configure produces lines like this ... just ignore them
                    if ("".equals(value)) {
                        continue;
                    }
                    */

                    if (includeProperty != null && key.equalsIgnoreCase(includeProperty)) {
                        // Recursively load properties files.
                        File file = null;

                        if (value.startsWith(fileSeparator)) {
                            // We have an absolute path so we'll use this
                            file = new File(value);
                            
                        } else {
                            // We have a relative path, and we have two 
                            // possible forms here. If we have the "./" form
                            // then just strip that off first before continuing.
                            if (value.startsWith("." + fileSeparator)) {
                                value = value.substring(2);
                            }

                            file = new File(basePath + value);
                        }

                        if (file != null && file.exists() && file.canRead()) {
                            load(new FileInputStream(file));
                        }
                    } else {
                        addProperty(key, value);
                    }
                }
            }
        } finally {
            // Loading is initializing
            isInitialized = true;
        }
    }

    /**
     * Gets a property from the configuration.
     *
     * @param key property to retrieve
     * @return value as object. Will return user value if exists,
     *        if not then default value if exists, otherwise null
     */
    public Object getProperty(String key) {
        // first, try to get from the 'user value' store
        Object obj = super.get(key);

        if (obj == null) {
            // if there isn't a value there, get it from the
            // defaults if we have them
            if (defaults != null) {
                obj = defaults.get(key);
            }
        }

        return obj;
    }
    
    /**
     * Add a property to the configuration. If it already
     * exists then the value stated here will be added
     * to the configuration entry. For example, if
     *
     * <code>resource.loader = file</code>
     *
     * is already present in the configuration and you
     *
     * <code>addProperty("resource.loader", "classpath")</code>
     *
     * Then you will end up with a Vector like the
     * following:
     *
     * <code>["file", "classpath"]</code>
     *
     * @param key  the key to add
     * @param value  the value to add
     */
    public void addProperty(String key, Object value) {
        if (value instanceof String) {
            String str = (String) value;
            if (str.indexOf(PropertiesTokenizer.DELIMITER) > 0) {
                // token contains commas, so must be split apart then added
                PropertiesTokenizer tokenizer = new PropertiesTokenizer(str);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    addPropertyInternal(key, unescape(token));
                }
            } else {
                // token contains no commas, so can be simply added
                addPropertyInternal(key, unescape(str));
            }
        } else {
            addPropertyInternal(key, value);
        }

        // Adding a property connotes initialization
        isInitialized = true;
    }

    /**
     * Adds a key/value pair to the map.  This routine does
     * no magic morphing.  It ensures the keylist is maintained
     *
     * @param key  the key to store at
     * @param value  the decoded object to store
     */
    private void addPropertyDirect(String key, Object value) {
        // safety check
        if (!containsKey(key)) {
            keysAsListed.add(key);
        }
        super.put(key, value);
    }

    /**
     * Adds a decoded property to the map w/o checking for commas - used
     * internally when a property has been broken up into
     * strings that could contain escaped commas to prevent
     * the inadvertent vectorization.
     * <p>
     * Thanks to Leon Messerschmidt for this one.
     *
     * @param key  the key to store at
     * @param value  the decoded object to store
     */
    private void addPropertyInternal(String key, Object value) {
        Object current = this.get(key);

        if (current instanceof String) {
            // one object already in map - convert it to a vector
            List values = new Vector(2);
            values.add(current);
            values.add(value);
            super.put(key, values);
            
        } else if (current instanceof List) {
            // already a list - just add the new token
            ((List) current).add(value);
            
        } else {
            // brand new key - store in keysAsListed to retain order
            if (!containsKey(key)) {
                keysAsListed.add(key);
            }
            super.put(key, value);
        }
    }

    /**
     * Set a property, this will replace any previously
     * set values. Set values is implicitly a call
     * to clearProperty(key), addProperty(key,value).
     *
     * @param key  the key to set
     * @param value  the value to set
     */
    public void setProperty(String key, Object value) {
        clearProperty(key);
        addProperty(key, value);
    }
    
    /**
     * Save the properties to the given output stream.
     * <p>
     * The stream is not closed, but it is flushed.
     *
     * @param output  an OutputStream, may be null
     * @param header  a textual comment to act as a file header
     * @throws IOException if an IO error occurs
     */
    public synchronized void save(OutputStream output, String header) throws IOException {
        if (output == null) {
            return;
        }
        PrintWriter theWrtr = new PrintWriter(output);
        if (header != null) {
            theWrtr.println(header);
        }
        
        Enumeration theKeys = keys();
        while (theKeys.hasMoreElements()) {
            String key = (String) theKeys.nextElement();
            Object value = get(key);
            if (value != null) {
                if (value instanceof String) {
                    StringBuffer currentOutput = new StringBuffer();
                    currentOutput.append(key);
                    currentOutput.append("=");
                    currentOutput.append(escape((String) value));
                    theWrtr.println(currentOutput.toString());
                    
                } else if (value instanceof List) {
                    List values = (List) value;
                    for (Iterator it = values.iterator(); it.hasNext(); ) {
                        String currentElement = (String) it.next();
                        StringBuffer currentOutput = new StringBuffer();
                        currentOutput.append(key);
                        currentOutput.append("=");
                        currentOutput.append(escape(currentElement));
                        theWrtr.println(currentOutput.toString());
                    }
                }
            }
            theWrtr.println();
            theWrtr.flush();
        }
    }

    /**
     * Combines an existing Hashtable with this Hashtable.
     * <p>
     * Warning: It will overwrite previous entries without warning.
     *
     * @param props  the properties to combine
     */
    public void combine(ExtendedProperties props) {
        for (Iterator it = props.getKeys(); it.hasNext();) {
            String key = (String) it.next();
            clearProperty(key);
            addPropertyDirect(key, props.get(key));
        }
    }
    
    /**
     * Clear a property in the configuration.
     *
     * @param key  the property key to remove along with corresponding value
     */
    public void clearProperty(String key) {
        if (containsKey(key)) {
            // we also need to rebuild the keysAsListed or else
            // things get *very* confusing
            for (int i = 0; i < keysAsListed.size(); i++) {
                if (( keysAsListed.get(i)).equals(key)) {
                    keysAsListed.remove(i);
                    break;
                }
            }
            super.remove(key);
        }
    }

    /**
     * Get the list of the keys contained in the configuration
     * repository.
     *
     * @return an Iterator over the keys
     */
    public Iterator getKeys() {
        return keysAsListed.iterator();
    }

    /**
     * Get the list of the keys contained in the configuration
     * repository that match the specified prefix.
     *
     * @param prefix  the prefix to match
     * @return an Iterator of keys that match the prefix
     */
    public Iterator getKeys(String prefix) {
        Iterator keys = getKeys();
        ArrayList matchingKeys = new ArrayList();

        while (keys.hasNext()) {
            Object key = keys.next();

            if (key instanceof String && ((String) key).startsWith(prefix)) {
                matchingKeys.add(key);
            }
        }
        return matchingKeys.iterator();
    }

    /**
     * Create an ExtendedProperties object that is a subset
     * of this one. Take into account duplicate keys
     * by using the setProperty() in ExtendedProperties.
     *
     * @param prefix  the prefix to get a subset for
     * @return a new independent ExtendedProperties
     */
    public ExtendedProperties subset(String prefix) {
        ExtendedProperties c = new ExtendedProperties();
        Iterator keys = getKeys();
        boolean validSubset = false;

        while (keys.hasNext()) {
            Object key = keys.next();

            if (key instanceof String && ((String) key).startsWith(prefix)) {
                if (!validSubset) {
                    validSubset = true;
                }

                /*
                 * Check to make sure that c.subset(prefix) doesn't
                 * blow up when there is only a single property
                 * with the key prefix. This is not a useful
                 * subset but it is a valid subset.
                 */
                String newKey = null;
                if (((String) key).length() == prefix.length()) {
                    newKey = prefix;
                } else {
                    newKey = ((String) key).substring(prefix.length() + 1);
                }

                /*
                 *  use addPropertyDirect() - this will plug the data as 
                 *  is into the Map, but will also do the right thing
                 *  re key accounting
                 */
                c.addPropertyDirect(newKey, get(key));
            }
        }

        if (validSubset) {
            return c;
        } else {
            return null;
        }
    }

    /**
     * Display the configuration for debugging purposes to System.out.
     */
    public void display() {
        Iterator i = getKeys();

        while (i.hasNext()) {
            String key = (String) i.next();
            Object value = get(key);
            System.out.println(key + " => " + value);
        }
    }

    /**
     * Get a string associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated string.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String.
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Get a string associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated string if key is found,
     * default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String.
     */
    public String getString(String key, String defaultValue) {
        Object value = get(key);

        if (value instanceof String) {
            return interpolate((String) value);
            
        } else if (value == null) {
            if (defaults != null) {
                return interpolate(defaults.getString(key, defaultValue));
            } else {
                return interpolate(defaultValue);
            }
        } else if (value instanceof List) {
            return interpolate((String) ((List) value).get(0));
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a String object");
        }
    }

    /**
     * Get a list of properties associated with the given
     * configuration key.
     *
     * @param key The configuration key.
     * @return The associated properties if key is found.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String/List.
     * @throws IllegalArgumentException if one of the tokens is
     * malformed (does not contain an equals sign).
     */
    public Properties getProperties(String key) {
        return getProperties(key, new Properties());
    }

    /**
     * Get a list of properties associated with the given
     * configuration key.
     *
     * @param key The configuration key.
     * @return The associated properties if key is found.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String/List.
     * @throws IllegalArgumentException if one of the tokens is
     * malformed (does not contain an equals sign).
     */
    public Properties getProperties(String key, Properties defaults) {
        /*
         * Grab an array of the tokens for this key.
         */
        String[] tokens = getStringArray(key);

        // Each token is of the form 'key=value'.
        Properties props = new Properties(defaults);
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            int equalSign = token.indexOf('=');
            if (equalSign > 0) {
                String pkey = token.substring(0, equalSign).trim();
                String pvalue = token.substring(equalSign + 1).trim();
                props.put(pkey, pvalue);
            } else {
                throw new IllegalArgumentException('\'' + token + "' does not contain " + "an equals sign");
            }
        }
        return props;
    }

    /**
     * Get an array of strings associated with the given configuration
     * key.
     *
     * @param key The configuration key.
     * @return The associated string array if key is found.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a String/List.
     */
    public String[] getStringArray(String key) {
        Object value = get(key);

        List values;
        if (value instanceof String) {
            values = new Vector(1);
            values.add(value);
            
        } else if (value instanceof List) {
            values = (List) value;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getStringArray(key);
            } else {
                return new String[0];
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a String/List object");
        }

        String[] tokens = new String[values.size()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = (String) values.get(i);
        }

        return tokens;
    }

    /**
     * Get a Vector of strings associated with the given configuration
     * key.
     *
     * @param key The configuration key.
     * @return The associated Vector.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Vector.
     */
    public Vector getVector(String key) {
        return getVector(key, null);
    }

    /**
     * Get a Vector of strings associated with the given configuration key.
     * <p>
     * The list is a copy of the internal data of this object, and as
     * such you may alter it freely.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated Vector.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Vector.
     */
    public Vector getVector(String key, Vector defaultValue) {
        Object value = get(key);

        if (value instanceof List) {
            return new Vector((List) value);
            
        } else if (value instanceof String) {
            Vector values = new Vector(1);
            values.add(value);
            super.put(key, values);
            return values;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getVector(key, defaultValue);
            } else {
                return ((defaultValue == null) ? new Vector() : defaultValue);
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Vector object");
        }
    }

    /**
     * Get a List of strings associated with the given configuration key.
     * <p>
     * The list is a copy of the internal data of this object, and as
     * such you may alter it freely.
     *
     * @param key The configuration key.
     * @return The associated List object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a List.
     * @since Commons Collections 3.2
     */
    public List getList(String key) {
        return getList(key, null);
    }

    /**
     * Get a List of strings associated with the given configuration key.
     * <p>
     * The list is a copy of the internal data of this object, and as
     * such you may alter it freely.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated List.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a List.
     * @since Commons Collections 3.2
     */
    public List getList(String key, List defaultValue) {
        Object value = get(key);

        if (value instanceof List) {
            return new ArrayList((List) value);
            
        } else if (value instanceof String) {
            List values = new ArrayList(1);
            values.add(value);
            super.put(key, values);
            return values;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getList(key, defaultValue);
            } else {
                return ((defaultValue == null) ? new ArrayList() : defaultValue);
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a List object");
        }
    }

    /**
     * Get a boolean associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated boolean.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Boolean.
     */
    public boolean getBoolean(String key) {
        Boolean b = getBoolean(key, null);
        if (b != null) {
            return b.booleanValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    /**
     * Get a boolean associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated boolean.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Boolean.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key, new Boolean(defaultValue)).booleanValue();
    }

    /**
     * Get a boolean associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated boolean if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Boolean.
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {

        Object value = get(key);

        if (value instanceof Boolean) {
            return (Boolean) value;
            
        } else if (value instanceof String) {
            String s = testBoolean((String) value);
            Boolean b = new Boolean(s);
            super.put(key, b);
            return b;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getBoolean(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Boolean object");
        }
    }

    /**
     * Test whether the string represent by value maps to a boolean
     * value or not. We will allow <code>true</code>, <code>on</code>,
     * and <code>yes</code> for a <code>true</code> boolean value, and
     * <code>false</code>, <code>off</code>, and <code>no</code> for
     * <code>false</code> boolean values.  Case of value to test for
     * boolean status is ignored.
     *
     * @param value  the value to test for boolean state
     * @return <code>true</code> or <code>false</code> if the supplied
     * text maps to a boolean value, or <code>null</code> otherwise.
     */
    public String testBoolean(String value) {
        String s = value.toLowerCase(Locale.ENGLISH);

        if (s.equals("true") || s.equals("on") || s.equals("yes")) {
            return "true";
        } else if (s.equals("false") || s.equals("off") || s.equals("no")) {
            return "false";
        } else {
            return null;
        }
    }

    /**
     * Get a byte associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated byte.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Byte.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public byte getByte(String key) {
        Byte b = getByte(key, null);
        if (b != null) {
            return b.byteValue();
        } else {
            throw new NoSuchElementException('\'' + key + " doesn't map to an existing object");
        }
    }

    /**
     * Get a byte associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated byte.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Byte.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public byte getByte(String key, byte defaultValue) {
        return getByte(key, new Byte(defaultValue)).byteValue();
    }

    /**
     * Get a byte associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated byte if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Byte.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Byte getByte(String key, Byte defaultValue) {
        Object value = get(key);

        if (value instanceof Byte) {
            return (Byte) value;
            
        } else if (value instanceof String) {
            Byte b = new Byte((String) value);
            super.put(key, b);
            return b;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getByte(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Byte object");
        }
    }

    /**
     * Get a short associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated short.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Short.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public short getShort(String key) {
        Short s = getShort(key, null);
        if (s != null) {
            return s.shortValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    /**
     * Get a short associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated short.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Short.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public short getShort(String key, short defaultValue) {
        return getShort(key, new Short(defaultValue)).shortValue();
    }

    /**
     * Get a short associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated short if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Short.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Short getShort(String key, Short defaultValue) {
        Object value = get(key);

        if (value instanceof Short) {
            return (Short) value;
            
        } else if (value instanceof String) {
            Short s = new Short((String) value);
            super.put(key, s);
            return s;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getShort(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Short object");
        }
    }

    /**
     * The purpose of this method is to get the configuration resource
     * with the given name as an integer.
     *
     * @param name The resource name.
     * @return The value of the resource as an integer.
     */
    public int getInt(String name) {
        return getInteger(name);
    }

    /**
     * The purpose of this method is to get the configuration resource
     * with the given name as an integer, or a default value.
     *
     * @param name The resource name
     * @param def The default value of the resource.
     * @return The value of the resource as an integer.
     */
    public int getInt(String name, int def) {
        return getInteger(name, def);
    }

    /**
     * Get a int associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated int.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Integer.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public int getInteger(String key) {
        Integer i = getInteger(key, null);
        if (i != null) {
            return i.intValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    /**
     * Get a int associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated int.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Integer.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public int getInteger(String key, int defaultValue) {
        Integer i = getInteger(key, null);

        if (i == null) {
            return defaultValue;
        }
        return i.intValue();
    }

    /**
     * Get a int associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated int if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Integer.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Integer getInteger(String key, Integer defaultValue) {
        Object value = get(key);

        if (value instanceof Integer) {
            return (Integer) value;
            
        } else if (value instanceof String) {
            Integer i = new Integer((String) value);
            super.put(key, i);
            return i;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getInteger(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Integer object");
        }
    }

    /**
     * Get a long associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated long.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Long.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public long getLong(String key) {
        Long l = getLong(key, null);
        if (l != null) {
            return l.longValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    /**
     * Get a long associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated long.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Long.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public long getLong(String key, long defaultValue) {
        return getLong(key, new Long(defaultValue)).longValue();
    }

    /**
     * Get a long associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated long if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Long.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Long getLong(String key, Long defaultValue) {
        Object value = get(key);

        if (value instanceof Long) {
            return (Long) value;
            
        } else if (value instanceof String) {
            Long l = new Long((String) value);
            super.put(key, l);
            return l;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getLong(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Long object");
        }
    }

    /**
     * Get a float associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated float.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Float.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public float getFloat(String key) {
        Float f = getFloat(key, null);
        if (f != null) {
            return f.floatValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    /**
     * Get a float associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated float.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Float.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public float getFloat(String key, float defaultValue) {
        return getFloat(key, new Float(defaultValue)).floatValue();
    }

    /**
     * Get a float associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated float if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Float.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Float getFloat(String key, Float defaultValue) {
        Object value = get(key);

        if (value instanceof Float) {
            return (Float) value;
            
        } else if (value instanceof String) {
            Float f = new Float((String) value);
            super.put(key, f);
            return f;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getFloat(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Float object");
        }
    }

    /**
     * Get a double associated with the given configuration key.
     *
     * @param key The configuration key.
     * @return The associated double.
     * @throws NoSuchElementException is thrown if the key doesn't
     * map to an existing object.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Double.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public double getDouble(String key) {
        Double d = getDouble(key, null);
        if (d != null) {
            return d.doubleValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    /**
     * Get a double associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated double.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Double.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public double getDouble(String key, double defaultValue) {
        return getDouble(key, new Double(defaultValue)).doubleValue();
    }

    /**
     * Get a double associated with the given configuration key.
     *
     * @param key The configuration key.
     * @param defaultValue The default value.
     * @return The associated double if key is found and has valid
     * format, default value otherwise.
     * @throws ClassCastException is thrown if the key maps to an
     * object that is not a Double.
     * @throws NumberFormatException is thrown if the value mapped
     * by the key has not a valid number format.
     */
    public Double getDouble(String key, Double defaultValue) {
        Object value = get(key);

        if (value instanceof Double) {
            return (Double) value;
            
        } else if (value instanceof String) {
            Double d = new Double((String) value);
            super.put(key, d);
            return d;
            
        } else if (value == null) {
            if (defaults != null) {
                return defaults.getDouble(key, defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a Double object");
        }
    }

    /**
     * Convert a standard properties class into a configuration class.
     * <p>
     * NOTE: From Commons Collections 3.2 this method will pick up
     * any default parent Properties of the specified input object.
     * <p>
     * As with java.util.Properties(Properties), any non-String 
     * values will not be passed on in the new ExtendedProperties 
     * object.
     *
     * @param props  the properties object to convert
     * @return new ExtendedProperties created from props
     */
    public static ExtendedProperties convertProperties(Properties props) {
        ExtendedProperties c = new ExtendedProperties();

        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
            String s = (String) e.nextElement();
            String value = props.getProperty(s);
            if(value != null) {
                c.setProperty(s, value);
            }
        }

        return c;
    }

    /**
     * Add a new property specified by the key to the 
     * ExtendedProperties.
     *
     * @param key specifying the property
     * @param value for the property
     * @return old value of the property
     */
    public Object put(Object key, Object value) {
        String strKey = String.valueOf(key);
        Object ret = getProperty(strKey);
        addProperty(strKey, value);
        return ret;
    }

    /**
     * Add a map full of key/value pairs to the ExtendedProperties. 
     * If the added map is an ExtendedProperties class, then the 
     * order of the added properties is maintained. 
     *
     * @param map full of key/value pair data
     */
    public void putAll(Map map) {
        if (map instanceof ExtendedProperties) {
            for (Iterator it = ((ExtendedProperties) map).getKeys(); it.hasNext(); ) {
                Object key = it.next();
                put(key, map.get(key));
            }
        } else {
            for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry entry = (Map.Entry) it.next();
                put(entry.getKey(), entry.getValue());
            }
        }
    }


    /**
     * Remove the property specified by the key from the 
     * ExtendedProperties.
     *
     * @param key specifying the property
     * @return old value of the property
     */
    public Object remove(Object key) {
        String strKey = String.valueOf(key);
        Object ret = getProperty(strKey);
        clearProperty(strKey);
        return ret;
    }

}

```
