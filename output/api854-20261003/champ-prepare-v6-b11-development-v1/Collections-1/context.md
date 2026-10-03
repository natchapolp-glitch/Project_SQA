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

## src/java/org/apache/commons/collections/map/Flat3Map.java

```
/*
 *  Copyright 2003-2004 The Apache Software Foundation
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.EmptyMapIterator;

/**
 * A <code>Map</code> implementation that stores data in simple fields until
 * the size is greater than 3.
 * <p>
 * This map is designed for performance and can outstrip HashMap.
 * It also has good garbage collection characteristics.
 * <ul>
 * <li>Optimised for operation at size 3 or less.
 * <li>Still works well once size 3 exceeded.
 * <li>Gets at size 3 or less are about 0-10% faster than HashMap,
 * <li>Puts at size 3 or less are over 4 times faster than HashMap.
 * <li>Performance 5% slower than HashMap once size 3 exceeded once.
 * </ul>
 * The design uses two distinct modes of operation - flat and delegate.
 * While the map is size 3 or less, operations map straight onto fields using
 * switch statements. Once size 4 is reached, the map switches to delegate mode
 * and only switches back when cleared. In delegate mode, all operations are
 * forwarded straight to a HashMap resulting in the 5% performance loss.
 * <p>
 * The performance gains on puts are due to not needing to create a Map Entry
 * object. This is a large saving not only in performance but in garbage collection.
 * <p>
 * Whilst in flat mode this map is also easy for the garbage collector to dispatch.
 * This is because it contains no complex objects or arrays which slow the progress.
 * <p>
 * Do not use <code>Flat3Map</code> if the size is likely to grow beyond 3.
 * <p>
 * <strong>Note that Flat3Map is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedMap(Map)}. This class may throw 
 * exceptions when accessed by concurrent threads without synchronization.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class Flat3Map implements IterableMap, Serializable, Cloneable {

    /** Serialization version */
    private static final long serialVersionUID = -6701087419741928296L;

    /** The size of the map, used while in flat mode */
    private transient int size;
    /** Hash, used while in flat mode */
    private transient int hash1;
    /** Hash, used while in flat mode */
    private transient int hash2;
    /** Hash, used while in flat mode */
    private transient int hash3;
    /** Key, used while in flat mode */
    private transient Object key1;
    /** Key, used while in flat mode */
    private transient Object key2;
    /** Key, used while in flat mode */
    private transient Object key3;
    /** Value, used while in flat mode */
    private transient Object value1;
    /** Value, used while in flat mode */
    private transient Object value2;
    /** Value, used while in flat mode */
    private transient Object value3;
    /** Map, used while in delegate mode */
    private transient AbstractHashedMap delegateMap;

    /**
     * Constructor.
     */
    public Flat3Map() {
        super();
    }

    /**
     * Constructor copying elements from another map.
     *
     * @param map  the map to copy
     * @throws NullPointerException if the map is null
     */
    public Flat3Map(Map map) {
        super();
        putAll(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the key specified.
     * 
     * @param key  the key
     * @return the mapped value, null if no match
     */
    public Object get(Object key) {
        if (delegateMap != null) {
            return delegateMap.get(key);
        }
        if (key == null) {
            switch (size) {
                // drop through
                case 3:
                    if (key3 == null) return value3;
                case 2:
                    if (key2 == null) return value2;
                case 1:
                    if (key1 == null) return value1;
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {
                    // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) return value3;
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) return value2;
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) return value1;
                }
            }
        }
        return null;
    }

    /**
     * Gets the size of the map.
     * 
     * @return the size
     */
    public int size() {
        if (delegateMap != null) {
            return delegateMap.size();
        }
        return size;
    }

    /**
     * Checks whether the map is currently empty.
     * 
     * @return true if the map is currently size zero
     */
    public boolean isEmpty() {
        return (size() == 0);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the map contains the specified key.
     * 
     * @param key  the key to search for
     * @return true if the map contains the key
     */
    public boolean containsKey(Object key) {
        if (delegateMap != null) {
            return delegateMap.containsKey(key);
        }
        if (key == null) {
            switch (size) {  // drop through
                case 3:
                    if (key3 == null) return true;
                case 2:
                    if (key2 == null) return true;
                case 1:
                    if (key1 == null) return true;
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {  // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) return true;
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) return true;
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the map contains the specified value.
     * 
     * @param value  the value to search for
     * @return true if the map contains the key
     */
    public boolean containsValue(Object value) {
        if (delegateMap != null) {
            return delegateMap.containsValue(value);
        }
        if (value == null) {  // drop through
            switch (size) {
                case 3:
                    if (value3 == null) return true;
                case 2:
                    if (value2 == null) return true;
                case 1:
                    if (value1 == null) return true;
            }
        } else {
            switch (size) {  // drop through
                case 3:
                    if (value.equals(value3)) return true;
                case 2:
                    if (value.equals(value2)) return true;
                case 1:
                    if (value.equals(value1)) return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Puts a key-value mapping into this map.
     * 
     * @param key  the key to add
     * @param value  the value to add
     * @return the value previously mapped to this key, null if none
     */
    public Object put(Object key, Object value) {
        if (delegateMap != null) {
            return delegateMap.put(key, value);
        }
        // change existing mapping
        if (key == null) {
            switch (size) {  // drop through
                case 3:
                    if (key3 == null) {
                        Object old = value3;
                        value3 = value;
                        return old;
                    }
                case 2:
                    if (key2 == null) {
                        Object old = value2;
                        value2 = value;
                        return old;
                    }
                case 1:
                    if (key1 == null) {
                        Object old = value1;
                        value1 = value;
                        return old;
                    }
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {  // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) {
                            Object old = value3;
                            value3 = value;
                            return old;
                        }
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) {
                            Object old = value2;
                            value2 = value;
                            return old;
                        }
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value1;
                            value1 = value;
                            return old;
                        }
                }
            }
        }
        
        // add new mapping
        switch (size) {
            default:
                convertToMap();
                delegateMap.put(key, value);
                return null;
            case 2:
                hash3 = (key == null ? 0 : key.hashCode());
                key3 = key;
                value3 = value;
                break;
            case 1:
                hash2 = (key == null ? 0 : key.hashCode());
                key2 = key;
                value2 = value;
                break;
            case 0:
                hash1 = (key == null ? 0 : key.hashCode());
                key1 = key;
                value1 = value;
                break;
        }
        size++;
        return null;
    }

    /**
     * Puts all the values from the specified map into this map.
     * 
     * @param map  the map to add
     * @throws NullPointerException if the map is null
     */
    public void putAll(Map map) {
        int size = map.size();
        if (size == 0) {
            return;
        }
        if (delegateMap != null) {
            delegateMap.putAll(map);
            return;
        }
        if (size < 4) {
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                put(entry.getKey(), entry.getValue());
            }
        } else {
            convertToMap();
            delegateMap.putAll(map);
        }
    }

    /**
     * Converts the flat map data to a map.
     */
    private void convertToMap() {
        delegateMap = createDelegateMap();
        switch (size) {  // drop through
            case 3:
                delegateMap.put(key3, value3);
            case 2:
                delegateMap.put(key2, value2);
            case 1:
                delegateMap.put(key1, value1);
        }
        
        size = 0;
        hash1 = hash2 = hash3 = 0;
        key1 = key2 = key3 = null;
        value1 = value2 = value3 = null;
    }

    /**
     * Create an instance of the map used for storage when in delegation mode.
     * <p>
     * This can be overridden by subclasses to provide a different map implementation.
     * Not every AbstractHashedMap is suitable, identity and reference based maps
     * would be poor choices.
     *
     * @return a new AbstractHashedMap or subclass
     * @since Commons Collections 3.1
     */
    protected AbstractHashedMap createDelegateMap() {
        return new HashedMap();
    }

    /**
     * Removes the specified mapping from this map.
     * 
     * @param key  the mapping to remove
     * @return the value mapped to the removed key, null if key not in map
     */
    public Object remove(Object key) {
        if (delegateMap != null) {
            return delegateMap.remove(key);
        }
        if (size == 0) {
            return null;
        }
        if (key == null) {
            switch (size) {  // drop through
                case 3:
                    if (key3 == null) {
                        Object old = value3;
                        hash3 = 0;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (key2 == null) {
                        Object old = value3;
                        hash2 = hash3;
                        key2 = key3;
                        value2 = value3;
                        hash3 = 0;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (key1 == null) {
                        Object old = value3;
                        hash1 = hash3;
                        key1 = key3;
                        value1 = value3;
                        hash3 = 0;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    return null;
                case 2:
                    if (key2 == null) {
                        Object old = value2;
                        hash2 = 0;
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    if (key1 == null) {
                        Object old = value2;
                        hash1 = hash2;
                        key1 = key2;
                        value1 = value2;
                        hash2 = 0;
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    return null;
                case 1:
                    if (key1 == null) {
                        Object old = value1;
                        hash1 = 0;
                        key1 = null;
                        value1 = null;
                        size = 0;
                        return old;
                    }
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {  // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) {
                            Object old = value3;
                            hash3 = 0;
                            key3 = null;
                            value3 = null;
                            size = 2;
                            return old;
                        }
                        if (hash2 == hashCode && key.equals(key2)) {
                            Object old = value3;
                            hash2 = hash3;
                            key2 = key3;
                            value2 = value3;
                            hash3 = 0;
                            key3 = null;
                            value3 = null;
                            size = 2;
                            return old;
                        }
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value3;
                            hash1 = hash3;
                            key1 = key3;
                            value1 = value3;
                            hash3 = 0;
                            key3 = null;
                            value3 = null;
                            size = 2;
                            return old;
                        }
                        return null;
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) {
                            Object old = value2;
                            hash2 = 0;
                            key2 = null;
                            value2 = null;
                            size = 1;
                            return old;
                        }
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value2;
                            hash1 = hash2;
                            key1 = key2;
                            value1 = value2;
                            hash2 = 0;
                            key2 = null;
                            value2 = null;
                            size = 1;
                            return old;
                        }
                        return null;
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value1;
                            hash1 = 0;
                            key1 = null;
                            value1 = null;
                            size = 0;
                            return old;
                        }
                }
            }
        }
        return null;
    }

    /**
     * Clears the map, resetting the size to zero and nullifying references
     * to avoid garbage collection issues.
     */
    public void clear() {
        if (delegateMap != null) {
            delegateMap.clear();  // should aid gc
            delegateMap = null;  // switch back to flat mode
        } else {
            size = 0;
            hash1 = hash2 = hash3 = 0;
            key1 = key2 = key3 = null;
            value1 = value2 = value3 = null;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an iterator over the map.
     * Changes made to the iterator affect this map.
     * <p>
     * A MapIterator returns the keys in the map. It also provides convenient
     * methods to get the key and value, and set the value.
     * It avoids the need to create an entrySet/keySet/values object.
     * It also avoids creating the Map Entry object.
     * 
     * @return the map iterator
     */
    public MapIterator mapIterator() {
        if (delegateMap != null) {
            return delegateMap.mapIterator();
        }
        if (size == 0) {
            return EmptyMapIterator.INSTANCE;
        }
        return new FlatMapIterator(this);
    }

    /**
     * FlatMapIterator
     */
    static class FlatMapIterator implements MapIterator, ResettableIterator {
        private final Flat3Map parent;
        private int nextIndex = 0;
        private boolean canRemove = false;
        
        FlatMapIterator(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public boolean hasNext() {
            return (nextIndex < parent.size);
        }

        public Object next() {
            if (hasNext() == false) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            canRemove = true;
            nextIndex++;
            return getKey();
        }

        public void remove() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            parent.remove(getKey());
            nextIndex--;
            canRemove = false;
        }

        public Object getKey() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.key3;
                case 2:
                    return parent.key2;
                case 1:
                    return parent.key1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object getValue() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.value3;
                case 2:
                    return parent.value2;
                case 1:
                    return parent.value1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object setValue(Object value) {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            Object old = getValue();
            switch (nextIndex) {
                case 3: 
                    parent.value3 = value;
                    break;
                case 2:
                    parent.value2 = value;
                    break;
                case 1:
                    parent.value1 = value;
                    break;
            }
            return old;
        }
        
        public void reset() {
            nextIndex = 0;
            canRemove = false;
        }
        
        public String toString() {
            if (canRemove) {
                return "Iterator[" + getKey() + "=" + getValue() + "]";
            } else {
                return "Iterator[]";
            }
        }
    }
    
    /**
     * Gets the entrySet view of the map.
     * Changes made to the view affect this map.
     * The Map Entry is not an independent object and changes as the 
     * iterator progresses.
     * To simply iterate through the entries, use {@link #mapIterator()}.
     * 
     * @return the entrySet view
     */
    public Set entrySet() {
        if (delegateMap != null) {
            return delegateMap.entrySet();
        }
        return new EntrySet(this);
    }
    
    /**
     * EntrySet
     */
    static class EntrySet extends AbstractSet {
        private final Flat3Map parent;
        
        EntrySet(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }
        
        public void clear() {
            parent.clear();
        }
        
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            boolean result = parent.containsKey(key);
            parent.remove(key);
            return result;
        }

        public Iterator iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.entrySet().iterator();
            }
            if (parent.size() == 0) {
                return EmptyIterator.INSTANCE;
            }
            return new EntrySetIterator(parent);
        }
    }

    /**
     * EntrySetIterator and MapEntry
     */
    static class EntrySetIterator implements Iterator, Map.Entry {
        private final Flat3Map parent;
        private int nextIndex = 0;
        private boolean canRemove = false;
        
        EntrySetIterator(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public boolean hasNext() {
            return (nextIndex < parent.size);
        }

        public Object next() {
            if (hasNext() == false) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            canRemove = true;
            nextIndex++;
            return this;
        }

        public void remove() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            parent.remove(getKey());
            nextIndex--;
            canRemove = false;
        }

        public Object getKey() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.key3;
                case 2:
                    return parent.key2;
                case 1:
                    return parent.key1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object getValue() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.value3;
                case 2:
                    return parent.value2;
                case 1:
                    return parent.value1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object setValue(Object value) {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            Object old = getValue();
            switch (nextIndex) {
                case 3: 
                    parent.value3 = value;
                    break;
                case 2:
                    parent.value2 = value;
                    break;
                case 1:
                    parent.value1 = value;
                    break;
            }
            return old;
        }
        
        public boolean equals(Object obj) {
            if (canRemove == false) {
                return false;
            }
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            Map.Entry other = (Map.Entry) obj;
            Object key = getKey();
            Object value = getValue();
            return (key == null ? other.getKey() == null : key.equals(other.getKey())) &&
                   (value == null ? other.getValue() == null : value.equals(other.getValue()));
        }
        
        public int hashCode() {
            if (canRemove == false) {
                return 0;
            }
            Object key = getKey();
            Object value = getValue();
            return (key == null ? 0 : key.hashCode()) ^
                   (value == null ? 0 : value.hashCode());
        }
        
        public String toString() {
            if (canRemove) {
                return getKey() + "=" + getValue();
            } else {
                return "";
            }
        }
    }
    
    /**
     * Gets the keySet view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the keys, use {@link #mapIterator()}.
     * 
     * @return the keySet view
     */
    public Set keySet() {
        if (delegateMap != null) {
            return delegateMap.keySet();
        }
        return new KeySet(this);
    }

    /**
     * KeySet
     */
    static class KeySet extends AbstractSet {
        private final Flat3Map parent;
        
        KeySet(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }
        
        public void clear() {
            parent.clear();
        }
        
        public boolean contains(Object key) {
            return parent.containsKey(key);
        }

        public boolean remove(Object key) {
            boolean result = parent.containsKey(key);
            parent.remove(key);
            return result;
        }

        public Iterator iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.keySet().iterator();
            }
            if (parent.size() == 0) {
                return EmptyIterator.INSTANCE;
            }
            return new KeySetIterator(parent);
        }
    }

    /**
     * KeySetIterator
     */
    static class KeySetIterator extends EntrySetIterator {
        
        KeySetIterator(Flat3Map parent) {
            super(parent);
        }

        public Object next() {
            super.next();
            return getKey();
        }
    }
    
    /**
     * Gets the values view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the values, use {@link #mapIterator()}.
     * 
     * @return the values view
     */
    public Collection values() {
        if (delegateMap != null) {
            return delegateMap.values();
        }
        return new Values(this);
    }

    /**
     * Values
     */
    static class Values extends AbstractCollection {
        private final Flat3Map parent;
        
        Values(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }
        
        public void clear() {
            parent.clear();
        }
        
        public boolean contains(Object value) {
            return parent.containsValue(value);
        }

        public Iterator iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.values().iterator();
            }
            if (parent.size() == 0) {
                return EmptyIterator.INSTANCE;
            }
            return new ValuesIterator(parent);
        }
    }

    /**
     * ValuesIterator
     */
    static class ValuesIterator extends EntrySetIterator {
        
        ValuesIterator(Flat3Map parent) {
            super(parent);
        }

        public Object next() {
            super.next();
            return getValue();
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size());
        for (MapIterator it = mapIterator(); it.hasNext();) {
            out.writeObject(it.next());  // key
            out.writeObject(it.getValue());  // value
        }
    }

    /**
     * Read the map in using a custom routine.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int count = in.readInt();
        if (count > 3) {
            delegateMap = createDelegateMap();
        }
        for (int i = count; i > 0; i--) {
            put(in.readObject(), in.readObject());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Clones the map without cloning the keys or values.
     *
     * @return a shallow clone
     * @since Commons Collections 3.1
     */
    public Object clone() {
        try {
            Flat3Map cloned = (Flat3Map) super.clone();
            if (cloned.delegateMap != null) {
                cloned.delegateMap = (HashedMap) cloned.delegateMap.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }

    /**
     * Compares this map with another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (delegateMap != null) {
            return delegateMap.equals(obj);
        }
        if (obj instanceof Map == false) {
            return false;
        }
        Map other = (Map) obj;
        if (size != other.size()) {
            return false;
        }
        if (size > 0) {
            Object otherValue = null;
            switch (size) {  // drop through
                case 3:
                    if (other.containsKey(key3) == false) {
                        return false;
                    }
                    otherValue = other.get(key3);
                    if (value3 == null ? otherValue != null : !value3.equals(otherValue)) {
                        return false;
                    }
                case 2:
                    if (other.containsKey(key2) == false) {
                        return false;
                    }
                    otherValue = other.get(key2);
                    if (value2 == null ? otherValue != null : !value2.equals(otherValue)) {
                        return false;
                    }
                case 1:
                    if (other.containsKey(key1) == false) {
                        return false;
                    }
                    otherValue = other.get(key1);
                    if (value1 == null ? otherValue != null : !value1.equals(otherValue)) {
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * Gets the standard Map hashCode.
     * 
     * @return the hash code defined in the Map interface
     */
    public int hashCode() {
        if (delegateMap != null) {
            return delegateMap.hashCode();
        }
        int total = 0;
        switch (size) {  // drop through
            case 3:
                total += (hash3 ^ (value3 == null ? 0 : value3.hashCode()));
            case 2:
                total += (hash2 ^ (value2 == null ? 0 : value2.hashCode()));
            case 1:
                total += (hash1 ^ (value1 == null ? 0 : value1.hashCode()));
        }
        return total;
    }

    /**
     * Gets the map as a String.
     * 
     * @return a string version of the map
     */
    public String toString() {
        if (delegateMap != null) {
            return delegateMap.toString();
        }
        if (size == 0) {
            return "{}";
        }
        StringBuffer buf = new StringBuffer(128);
        buf.append('{');
        switch (size) {  // drop through
            case 3:
                buf.append((key3 == this ? "(this Map)" : key3));
                buf.append('=');
                buf.append((value3 == this ? "(this Map)" : value3));
                buf.append(',');
            case 2:
                buf.append((key2 == this ? "(this Map)" : key2));
                buf.append('=');
                buf.append((value2 == this ? "(this Map)" : value2));
                buf.append(',');
            case 1:
                buf.append((key1 == this ? "(this Map)" : key1));
                buf.append('=');
                buf.append((value1 == this ? "(this Map)" : value1));
        }
        buf.append('}');
        return buf.toString();
    }

}

```
