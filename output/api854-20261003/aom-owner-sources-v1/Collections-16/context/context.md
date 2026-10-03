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

## src/java/org/apache/commons/collections/list/SetUniqueList.java

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
 * Decorates a <code>List</code> to ensure that no duplicates are present
 * much like a <code>Set</code>.
 * <p>
 * The <code>List</code> interface makes certain assumptions/requirements.
 * This implementation breaks these in certain ways, but this is merely the
 * result of rejecting duplicates.
 * Each violation is explained in the method, but it should not affect you.
 * Bear in mind that Sets require immutable objects to function correctly.
 * <p>
 * The {@link org.apache.commons.collections.set.ListOrderedSet ListOrderedSet}
 * class provides an alternative approach, by wrapping an existing Set and
 * retaining insertion order in the iterator.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 * 
 * @author Matthew Hawthorne
 * @author Stephen Colebourne
 * @author Tom Dunham
 */
public class SetUniqueList extends AbstractSerializableListDecorator {

    /** Serialization version */
    private static final long serialVersionUID = 7196982186153478694L;

    /**
     * Internal Set to maintain uniqueness.
     */
    protected final Set set;

    /**
     * Factory method to create a SetList using the supplied list to retain order.
     * <p>
     * If the list contains duplicates, these are removed (first indexed one kept).
     * A <code>HashSet</code> is used for the set behaviour.
     * 
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    public static SetUniqueList decorate(List list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        if (list.isEmpty()) {
            return new SetUniqueList(list, new HashSet());
        } else {
            List temp = new ArrayList(list);
            list.clear();
            SetUniqueList sl = new SetUniqueList(list, new HashSet());
            sl.addAll(temp);
            return sl;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies) the List and specifies the set to use.
     * <p>
     * The set and list must both be correctly initialised to the same elements.
     * 
     * @param set  the set to decorate, must not be null
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if set or list is null
     */
    protected SetUniqueList(List list, Set set) {
        super(list);
        if (set == null) {
            throw new IllegalArgumentException("Set must not be null");
        }
        this.set = set;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an unmodifiable view as a Set.
     * 
     * @return an unmodifiable set view
     */
    public Set asSet() {
        return UnmodifiableSet.decorate(set);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds an element to the list if it is not already present.
     * <p>
     * <i>(Violation)</i>
     * The <code>List</code> interface requires that this method returns
     * <code>true</code> always. However this class may return <code>false</code>
     * because of the <code>Set</code> behaviour.
     * 
     * @param object the object to add
     * @return true if object was added
     */
    public boolean add(Object object) {
        // gets initial size
        final int sizeBefore = size();

        // adds element if unique
        add(size(), object);

        // compares sizes to detect if collection changed
        return (sizeBefore != size());
    }

    /**
     * Adds an element to a specific index in the list if it is not already present.
     * <p>
     * <i>(Violation)</i>
     * The <code>List</code> interface makes the assumption that the element is
     * always inserted. This may not happen with this implementation.
     * 
     * @param index  the index to insert at
     * @param object  the object to add
     */
    public void add(int index, Object object) {
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
     * <i>(Violation)</i>
     * The <code>List</code> interface makes the assumption that the elements
     * are always inserted. This may not happen with this implementation.
     * 
     * @param coll  the collection to add in iterator order
     * @return true if this collection changed
     */
    public boolean addAll(Collection coll) {
        return addAll(size(), coll);
    }

    /**
     * Adds a collection of objects a specific index in the list avoiding 
     * duplicates.
     * <p>
     * Only elements that are not already in this list will be added, and
     * duplicates from the specified collection will be ignored.
     * <p>
     * <i>(Violation)</i>
     * The <code>List</code> interface makes the assumption that the elements
     * are always inserted. This may not happen with this implementation.
     * 
     * @param index  the index to insert at
     * @param coll  the collection to add in iterator order
     * @return true if this collection changed
     */
    public boolean addAll(int index, Collection coll) {
        // gets initial size
        final int sizeBefore = size();

        // adds all elements
        for (final Iterator it = coll.iterator(); it.hasNext();) {
            int sizeBeforeAddNext = size();
            add(index, it.next());
            // if it was inserted, then increase the target index
            if (sizeBeforeAddNext != size()) {
              index++;
            }
        }

        // compares sizes to detect if collection changed
        return sizeBefore != size();
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value at the specified index avoiding duplicates.
     * <p>
     * The object is set into the specified index.
     * Afterwards, any previous duplicate is removed
     * If the object is not already in the list then a normal set occurs.
     * If it is present, then the old version is removed.
     * 
     * @param index  the index to insert at
     * @param object  the object to set
     * @return the previous object
     */
    public Object set(int index, Object object) {
        int pos = indexOf(object);
        Object removed = super.set(index, object);

        if (pos != -1 && pos != index) {
            // the object is already in the uniq list
            // (and it hasn't been swapped with itself)
            super.remove(pos);  // remove the duplicate by index
        }

        set.add(object);      // add the new item to the unique set
        set.remove(removed);  // remove the item deleted by the set

        return removed;  // return the item deleted by the set
    }

    public boolean remove(Object object) {
        boolean result = super.remove(object);
        set.remove(object);
        return result;
    }

    public Object remove(int index) {
        Object result = super.remove(index);
        set.remove(result);
        return result;
    }

    public boolean removeAll(Collection coll) {
        boolean result = super.removeAll(coll);
        set.removeAll(coll);
        return result;
    }

    public boolean retainAll(Collection coll) {
        boolean result = super.retainAll(coll);
        set.retainAll(coll);
        return result;
    }

    public void clear() {
        super.clear();
        set.clear();
    }

    public boolean contains(Object object) {
        return set.contains(object);
    }

    public boolean containsAll(Collection coll) {
        return set.containsAll(coll);
    }

    public Iterator iterator() {
        return new SetListIterator(super.iterator(), set);
    }

    public ListIterator listIterator() {
        return new SetListListIterator(super.listIterator(), set);
    }

    public ListIterator listIterator(int index) {
        return new SetListListIterator(super.listIterator(index), set);
    }

    public List subList(int fromIndex, int toIndex) {
        List superSubList = super.subList(fromIndex, toIndex);
        Set subSet = createSetBasedOnList(set, superSubList);
        return new SetUniqueList(superSubList, subSet);
    }

    protected Set createSetBasedOnList(Set set, List list) {
        Set subSet = null;
        if(set.getClass().equals(HashSet.class)) {
            subSet = new HashSet();
        } else {
            try {
                subSet = (Set) set.getClass().newInstance();
            } catch(InstantiationException ie) {
                subSet = new HashSet();
            } catch(IllegalAccessException iae) {
                subSet = new HashSet();
            }
        }
        subSet.addAll(list);
        return subSet;
    }

    //-----------------------------------------------------------------------
    /**
     * Inner class iterator.
     */
    static class SetListIterator extends AbstractIteratorDecorator {
        
        protected final Set set;
        protected Object last = null;
        
        protected SetListIterator(Iterator it, Set set) {
            super(it);
            this.set = set;
        }
        
        public Object next() {
            last = super.next();
            return last;
        }

        public void remove() {
            super.remove();
            set.remove(last);
            last = null;
        }
    }
    
    /**
     * Inner class iterator.
     */
    static class SetListListIterator extends AbstractListIteratorDecorator {
        
        protected final Set set;
        protected Object last = null;
        
        protected SetListListIterator(ListIterator it, Set set) {
            super(it);
            this.set = set;
        }
        
        public Object next() {
            last = super.next();
            return last;
        }

        public Object previous() {
            last = super.previous();
            return last;
        }

        public void remove() {
            super.remove();
            set.remove(last);
            last = null;
        }

        public void add(Object object) {
            if (set.contains(object) == false) {
                super.add(object);
                set.add(object);
            }
        }
        
        public void set(Object object) {
            throw new UnsupportedOperationException("ListIterator does not support set");
        }
    }
    
}

```
