## build.xml

```
 <!--
   Copyright 2001-2006 The Apache Software Foundation

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
  <property name="component.title.full"    value="Apache Jakarta Commons Collections"/>

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
    <junit printsummary="yes" haltonfailure="yes" showoutput="yes">
      <formatter type="brief" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <batchtest fork="yes">
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

      <test name="${testcase}" fork="yes">
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
  <property name="tf.title.full"           value="Apache Jakarta Commons Collections Test Framework"/>
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
    
    <mkdir      dir="${tf.build.tf}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${tf.build.tf}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${tf.build.tf}/META-INF/NOTICE.txt"/>
             
    <tstamp/>
    <mkdir      dir="${tf.build.conf}"/>
    <copy     todir="${tf.build.conf}" filtering="on">
      <filterset>
        <filter token="name"     value="${tf.name}"/>
        <filter token="title"    value="${tf.title}"/>
        <filter token="package"  value="${tf.package}"/>
        <filter token="version"  value="${tf.version}"/>
      </filterset>
      <fileset dir="${source.conf}" includes="*.MF"/>
    </copy>
             
    <!-- NOTE: A jar built using JDK1.4 is incompatible with JDK1.2 -->
    <jar    jarfile="${tf.build.jar.name}"
            basedir="${tf.build.tf}"
           manifest="${tf.build.conf}/MANIFEST.MF"/>
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
        <include name="maven.xml"/>
        <include name="project.xml"/>
        <include name="project.properties"/>
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

## project.properties

```
#   Copyright 2003-2005 The Apache Software Foundation
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.

maven.changelog.factory=org.apache.maven.svnlib.SvnChangeLogFactory

maven.xdoc.date=left
maven.xdoc.version=${pom.currentVersion}
maven.xdoc.developmentProcessUrl=http://jakarta.apache.org/commons/charter.html
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

## src/java/org/apache/commons/collections/map/MultiValueMap.java

```
/*
 *  Copyright 2001-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections.map;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.FunctorException;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.IteratorChain;

/**
 * A MultiValueMap decorates another map, allowing it to have
 * more than one value for a key.
 * <p>
 * A <code>MultiMap</code> is a Map with slightly different semantics.
 * Putting a value into the map will add the value to a Collection at that key.
 * Getting a value will return a Collection, holding all the values put to that key.
 * <p>
 * This implementation is a decorator, allowing any Map implementation
 * to be used as the base.
 * <p>
 * In addition, this implementation allows the type of collection used
 * for the values to be controlled. By default, an <code>ArrayList</code>
 * is used, however a <code>Class</code> to instantiate may be specified,
 * or a factory that returns a <code>Collection</code> instance.
 * <p>
 * <strong>Note that MultiValueMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. This class may throw exceptions when accessed
 * by concurrent threads without synchronization.
 *
 * @author James Carman
 * @author Christopher Berry
 * @author James Strachan
 * @author Steve Downey
 * @author Stephen Colebourne
 * @author Julien Buret
 * @author Serhiy Yevtushenko
 * @version $Revision$ $Date$
 * @since Commons Collections 3.2
 */
public class MultiValueMap extends AbstractMapDecorator implements MultiMap {

    /** The factory for creating value collections. */
    private final Factory collectionFactory;
    /** The cached values. */
    private transient Collection values;

    /**
     * Creates a map which wraps the given map and
     * maps keys to ArrayLists.
     *
     * @param map  the map to wrap
     */
    public static MultiValueMap decorate(Map map) {
        return new MultiValueMap(map, new ReflectionFactory(ArrayList.class));
    }

    /**
     * Creates a map which decorates the given <code>map</code> and
     * maps keys to collections of type <code>collectionClass</code>.
     *
     * @param map  the map to wrap
     * @param collectionClass  the type of the collection class
     */
    public static MultiValueMap decorate(Map map, Class collectionClass) {
        return new MultiValueMap(map, new ReflectionFactory(collectionClass));
    }

    /**
     * Creates a map which decorates the given <code>map</code> and
     * creates the value collections using the supplied <code>collectionFactory</code>.
     *
     * @param map  the map to decorate
     * @param collectionFactory  the collection factory (must return a Collection object).
     */
    public static MultiValueMap decorate(Map map, Factory collectionFactory) {
        return new MultiValueMap(map, collectionFactory);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a MultiValueMap based on a <code>HashMap</code> and
     * storing the multiple values in an <code>ArrayList</code>.
     */
    public MultiValueMap() {
        this(new HashMap(), new ReflectionFactory(ArrayList.class));
    }

    /**
     * Creates a MultiValueMap which decorates the given <code>map</code> and
     * creates the value collections using the supplied <code>collectionFactory</code>.
     *
     * @param map  the map to decorate
     * @param collectionFactory  the collection factory which must return a Collection instance
     */
    protected MultiValueMap(Map map, Factory collectionFactory) {
        super(map);
        if (collectionFactory == null) {
            throw new IllegalArgumentException("The factory must not be null");
        }
        this.collectionFactory = collectionFactory;
    }

    //-----------------------------------------------------------------------
    /**
     * Clear the map.
     */
    public void clear() {
        // If you believe that you have GC issues here, try uncommenting this code
//        Set pairs = getMap().entrySet();
//        Iterator pairsIterator = pairs.iterator();
//        while (pairsIterator.hasNext()) {
//            Map.Entry keyValuePair = (Map.Entry) pairsIterator.next();
//            Collection coll = (Collection) keyValuePair.getValue();
//            coll.clear();
//        }
        getMap().clear();
    }

    /**
     * Removes a specific value from map.
     * <p>
     * The item is removed from the collection mapped to the specified key.
     * Other values attached to that key are unaffected.
     * <p>
     * If the last value for a key is removed, <code>null</code> will be returned
     * from a subsequant <code>get(key)</code>.
     *
     * @param key  the key to remove from
     * @param value the value to remove
     * @return the value removed (which was passed in), null if nothing removed
     */
    public Object removeMapping(Object key, Object value) {
        Collection valuesForKey = getCollection(key);
        if (valuesForKey == null) {
            return null;
        }
        boolean removed = valuesForKey.remove(value);
        if (removed == false) {
            return null;
        }
        if (valuesForKey.isEmpty()) {
            remove(key);
        }
        return value;
    }

    /**
     * Checks whether the map contains the value specified.
     * <p>
     * This checks all collections against all keys for the value, and thus could be slow.
     *
     * @param value  the value to search for
     * @return true if the map contains the value
     */
    public boolean containsValue(Object value) {
        Set pairs = getMap().entrySet();
        if (pairs == null) {
            return false;
        }
        Iterator pairsIterator = pairs.iterator();
        while (pairsIterator.hasNext()) {
            Map.Entry keyValuePair = (Map.Entry) pairsIterator.next();
            Collection coll = (Collection) keyValuePair.getValue();
            if (coll.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the value to the collection associated with the specified key.
     * <p>
     * Unlike a normal <code>Map</code> the previous value is not replaced.
     * Instead the new value is added to the collection stored against the key.
     *
     * @param key  the key to store against
     * @param value  the value to add to the collection at the key
     * @return the value added if the map changed and null if the map did not change
     */
    public Object put(Object key, Object value) {
        boolean result = false;
        Collection coll = getCollection(key);
        if (coll == null) {
            coll = createCollection(1);  // might produce a non-empty collection
            coll.add(value);
            if (coll.size() > 0) {
                // only add if non-zero size to maintain class state
                getMap().put(key, coll);
                result = true;  // map definitely changed
            }
        } else {
            result = coll.add(value);
        }
        return (result ? value : null);
    }

    /**
     * Override superclass to ensure that MultiMap instances are
     * correctly handled.
     * <p>
     * If you call this method with a normal map, each entry is
     * added using <code>put(Object,Object)</code>.
     * If you call this method with a multi map, each entry is
     * added using <code>putAll(Object,Collection)</code>.
     *
     * @param map  the map to copy (either a normal or multi map)
     */
    public void putAll(Map map) {
        if (map instanceof MultiMap) {
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                Collection coll = (Collection) entry.getValue();
                putAll(entry.getKey(), coll);
            }
        } else {
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Gets a collection containing all the values in the map.
     * <p>
     * This returns a collection containing the combination of values from all keys.
     *
     * @return a collection view of the values contained in this map
     */
    public Collection values() {
        Collection vs = values;
        return (vs != null ? vs : (values = new Values()));
    }

    /**
     * Checks whether the collection at the specified key contains the value.
     *
     * @param value  the value to search for
     * @return true if the map contains the value
     */
    public boolean containsValue(Object key, Object value) {
        Collection coll = getCollection(key);
        if (coll == null) {
            return false;
        }
        return coll.contains(value);
    }

    /**
     * Gets the collection mapped to the specified key.
     * This method is a convenience method to typecast the result of <code>get(key)</code>.
     *
     * @param key  the key to retrieve
     * @return the collection mapped to the key, null if no mapping
     */
    public Collection getCollection(Object key) {
        return (Collection) getMap().get(key);
    }

    /**
     * Gets the size of the collection mapped to the specified key.
     *
     * @param key  the key to get size for
     * @return the size of the collection at the key, zero if key not in map
     */
    public int size(Object key) {
        Collection coll = getCollection(key);
        if (coll == null) {
            return 0;
        }
        return coll.size();
    }

    /**
     * Adds a collection of values to the collection associated with
     * the specified key.
     *
     * @param key  the key to store against
     * @param values  the values to add to the collection at the key, null ignored
     * @return true if this map changed
     */
    public boolean putAll(Object key, Collection values) {
        if (values == null || values.size() == 0) {
            return false;
        }
        boolean result = false;
        Collection coll = getCollection(key);
        if (coll == null) {
            coll = createCollection(values.size());  // might produce a non-empty collection
            coll.addAll(values);
            if (coll.size() > 0) {
                // only add if non-zero size to maintain class state
                getMap().put(key, coll);
                result = true;  // map definitely changed
            }
        } else {
            result = coll.addAll(values);
        }
        return result;
    }

    /**
     * Gets an iterator for the collection mapped to the specified key.
     *
     * @param key  the key to get an iterator for
     * @return the iterator of the collection at the key, empty iterator if key not in map
     */
    public Iterator iterator(Object key) {
        if (!containsKey(key)) {
            return EmptyIterator.INSTANCE;
        } else {
            return new ValuesIterator(key);
        }
    }

    /**
     * Gets the total size of the map by counting all the values.
     *
     * @return the total size of the map counting all values
     */
    public int totalSize() {
        int total = 0;
        Collection values = getMap().values();
        for (Iterator it = values.iterator(); it.hasNext();) {
            Collection coll = (Collection) it.next();
            total += coll.size();
        }
        return total;
    }

    /**
     * Creates a new instance of the map value Collection container
     * using the factory.
     * <p>
     * This method can be overridden to perform your own processing
     * instead of using the factory.
     *
     * @param size  the collection size that is about to be added
     * @return the new collection
     */
    protected Collection createCollection(int size) {
        return (Collection) collectionFactory.create();
    }

    //-----------------------------------------------------------------------
    /**
     * Inner class that provides the values view.
     */
    private class Values extends AbstractCollection {
        public Iterator iterator() {
            final IteratorChain chain = new IteratorChain();
            for (Iterator it = keySet().iterator(); it.hasNext();) {
                chain.addIterator(new ValuesIterator(it.next()));
            }
            return chain;
        }

        public int size() {
            return totalSize();
        }

        public void clear() {
            MultiValueMap.this.clear();
        }
    }

    /**
     * Inner class that provides the values iterator.
     */
    private class ValuesIterator implements Iterator {
        private final Object key;
        private final Collection values;
        private final Iterator iterator;

        public ValuesIterator(Object key) {
            this.key = key;
            this.values = getCollection(key);
            this.iterator = values.iterator();
        }

        public void remove() {
            iterator.remove();
            if (values.isEmpty()) {
                MultiValueMap.this.remove(key);
            }
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            return iterator.next();
        }
    }

    /**
     * Inner class that provides a simple reflection factory.
     */
    private static class ReflectionFactory implements Factory {
        private final Class clazz;

        public ReflectionFactory(Class clazz) {
            this.clazz = clazz;
        }

        public Object create() {
            try {
                return clazz.newInstance();
            } catch (Exception ex) {
                throw new FunctorException("Cannot instantiate class: " + clazz, ex);
            }
        }
    }

}

```
