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
<project name="Commons Math" default="jar" basedir=".">


<!--
        "Math" component of the Apache Commons Project
        $Id$
        $Revision$ $Date$
-->


<!-- ========== Initialize Properties ===================================== -->

  <property file="build.properties"/>                <!-- Component local   -->
  <property file="${user.home}/build.properties"/>   <!-- User local        -->


<!-- ========== External Dependencies ===================================== -->


  <!-- Junit -->
  <property name="junit.version"           value="4.4"/>
  <property name="junit.home"              value="/usr/share/junit"/>
  <property name="junit.jar"               value="${junit.home}/junit-${junit.version}.jar"/>


<!-- ========== Component Declarations ==================================== -->


  <!-- The name of this component -->
  <property name="component.name"          value="commons-math"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.apache.commons.math"/>

  <!-- The title of this component -->
  <property name="component.title"         value="Commons MATH"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="2.1-SNAPSHOT"/>

  <!-- The base directory for component sources -->
  <property name="source.home"             value="src/main/java"/>

  <!-- The base directory for unit test sources -->
  <property name="test.home"               value="src/test/java"/>

  <!-- The base directory for unit test resources -->
  <property name="test.resources"          value="src/test/resources"/>

  <!-- Download lib dir -->
  <property name="download.lib.dir"        value="lib"/>

  <!-- The base directory for compilation targets -->
  <property name="build.home"              value="target"/>

  <!-- The base directory for distribution targets -->
  <property name="dist.home"               value="${build.home}/dist"/>

  <!-- Base file name for dist files -->
  <property name="final.name"        value="${component.name}-${component.version}"/>

  <!-- Directory where binary release files are staged -->
  <property name="stage.bin.dir"           value="${dist.home}/stage-bin"/>

  <!-- Directory where source release files are staged -->
  <property name="stage.src.dir"           value="${dist.home}/stage-src"/>

<!-- ========== Compiler Defaults ========================================= -->

  <!-- Should Java compilations set the 'debug' compiler option? -->
  <property name="compile.debug"           value="true"/>

  <!-- Should Java compilations set the 'deprecation' compiler option? -->
  <property name="compile.deprecation"     value="false"/>

  <!-- Should Java compilations set the 'optimize' compiler option? -->
  <property name="compile.optimize"        value="true"/>
    
  <!-- JDK level -->
  <property name="compile.source"          value="1.6"/>
  <property name="compile.target"          value="1.6"/>

  <!-- Base compile classpath -->
  <path id="compile.classpath">
    <pathelement location="${build.home}/classes"/>
  </path>

  <!-- External dependency classpath -->
  <path id="downloaded.lib.classpath">
    <pathelement location="${download.lib.dir}/junit-${junit.version}.jar"/>
  </path>

<!-- ========== Test Execution Defaults =================================== -->


  <!-- Construct unit test classpath -->
  <path id="test.classpath">
    <pathelement location="${build.home}/classes"/>
    <pathelement location="${build.home}/test-classes"/>
    <pathelement location="${junit.jar}"/>
    <path refid="downloaded.lib.classpath"/>
  </path>

  <!-- Should the build fail if there are test failures? -->
  <property name="test.failonerror"        value="true"/>

<!-- ========== Executable Targets ======================================== -->

  <target name="clean" description="Clean build and distribution directories">
    <delete    dir="${build.home}"/>
  </target>


  <target name="init"
   description="Initialize and evaluate conditionals">
    <echo message="-------- ${component.title} ${component.version} --------"/>
    <filter  token="name"                  value="${component.name}"/>
    <filter  token="package"               value="${component.package}"/>
    <filter  token="version"               value="${component.version}"/>
    <filter  token="compilesource"         value="${compile.source}"/>
    <filter  token="compiletarget"         value="${compile.target}"/>
    <tstamp/>
    <mkdir dir="${build.home}"/>
    <mkdir dir="${build.home}/classes"/>
    <mkdir dir="${build.home}/test-classes"/>
  </target>

<!-- ========== Compile Targets =========================================== -->

  <target name="compile" depends="init" description="Compile">

    <javac  srcdir="${source.home}"
           destdir="${build.home}/classes"
             source="${compile.source}"
             target="${compile.target}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
          optimize="${compile.optimize}">
      <classpath refid="compile.classpath"/>
    </javac>
  </target>


<!-- ========== Unit Test Targets ========================================= -->

    <target name="compile.tests" depends="compile, download-dependencies" description="Compile unit tests.">

      <javac srcdir="${test.home}"
             destdir="${build.home}/test-classes"
             debug="${compile.debug}"
             deprecation="${compile.deprecation}"
             optimize="${compile.optimize}">
          <classpath refid="test.classpath"/>
      </javac>
    
      <copy todir="${build.home}/test-classes">
          <fileset dir="${test.resources}">
              <include name="**/*.xml"/>
              <include name="**/*.txt"/>
          </fileset>
      </copy>

    </target>

  <target name="test"  depends="compile.tests"
                       description="Run unit tests">
      <junit printsummary="true"
               errorProperty="test.failed"
               failureProperty="test.failed"
               fork="true"
               showOutput="true">
               <formatter usefile="false" type="plain"/>
               <classpath refid="test.classpath"/>
               <batchtest>
                  <fileset dir="${test.home}" includes="**/*Test.java" 
                  excludes="**/*AbstractTest.java"/>
               </batchtest>
       </junit>
       <fail message="There were test failures.">
           <condition>
               <and>
                   <istrue value="${test.failonerror}"/>
                   <isset property="test.failed"/>
               </and>
           </condition>
     </fail>
  </target>


<!-- ========== Produce JavaDocs ========================================== -->

  <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
    <mkdir dir="${build.home}/apidocs"/>
    <tstamp>
        <format property="current.year" pattern="yyyy"/>
    </tstamp>
    <javadoc sourcepath="${source.home}"
                destdir="${build.home}/apidocs"
           packagenames="org.apache.commons.*"
                 author="true"
                private="true"
                version="true"
               doctitle="&lt;h1&gt;${component.title} ${component.version}&lt;/h1&gt;"
            windowtitle="${component.title} ${component.version}"
                 bottom="Copyright (c) 2003-${current.year}  Apache Software Foundation"
           classpathref="compile.classpath">
        <link href="http://java.sun.com/j2se/1.5.0/docs/api/"/>  
        <link href="http://commons.apache.org/collections/api"/> 
    </javadoc>
  </target>


<!-- ========== Create Jar ================================================ -->

  <target name="jar" depends="test" description="Create jar file">

    <mkdir dir="${build.home}/classes/META-INF"/>
    <copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
    <copy file="NOTICE.txt"  tofile="${build.home}/classes/META-INF/NOTICE.txt"/>

    <manifest file="${build.home}/MANIFEST.MF">
        <attribute name="Specification-Title"      value="${component.title}"/>
        <attribute name="Specification-Version"    value="${component.version}"/>
        <attribute name="Specification-Vendor"     value="Apache Software Foundation"/>
        <attribute name="Implementation-Title"     value="${component.title}"/>
        <attribute name="Implementation-Version"   value="${component.version}"/> 
        <attribute name="Implementation-Vendor"    value="Apache Software Foundation"/>
        <attribute name="Implementation-Vendor-Id" value="org.apache"/>
        <attribute name="X-Compile-Source-JDK"     value="${compile.source}"/>
        <attribute name="X-Compile-Target-JDK"     value="${compile.target}"/>
    </manifest>

    <jar jarfile="${build.home}/${final.name}.jar"
         basedir="${build.home}/classes"
        manifest="${build.home}/MANIFEST.MF"/>
  </target>


<!-- ========== Distribution Target =========================================== -->

  <target name="dist" depends="clean,jar,javadoc" description="Create distribution artifacts">

    <mkdir dir="${dist.home}"/>

    <!-- jar(s) -->
    <copy todir="${dist.home}">
      <fileset dir=".">
        <include name="RELEASE-NOTES.txt"/>
      </fileset>
      <fileset dir="${build.home}">
        <include name="*.jar"/>
      </fileset>
    </copy>

    <!-- Binary Distro -->
    <mkdir dir="${stage.bin.dir}/${final.name}"/>
    <copy todir="${stage.bin.dir}/${final.name}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="RELEASE-NOTES.txt"/>
      </fileset>
      <fileset dir="${build.home}">
        <include name="*.jar"/>
      </fileset>
    </copy>
    <copy todir="${stage.bin.dir}/${final.name}/apidocs">
      <fileset dir="${build.home}/apidocs" />
    </copy>

    <!-- Source Distro -->
    <mkdir dir="${stage.src.dir}/${final.name}-src"/>
    <copy todir="${stage.src.dir}/${final.name}-src">
      <fileset dir=".">
        <include name="*.xml"/>
        <include name="*.txt"/>
        <include name="*.html"/>
      </fileset>
    </copy>
    <copy todir="${stage.src.dir}/${final.name}-src/src">
      <fileset dir="src" excludes="mantissa/**,experimental/**" />
    </copy>
    <zip  zipfile="${dist.home}/${final.name}.zip"     basedir="${stage.bin.dir}"/>
    <zip  zipfile="${dist.home}/${final.name}-src.zip" basedir="${stage.src.dir}"/>
    <tar  tarfile="${dist.home}/${final.name}.tar"     basedir="${stage.bin.dir}" longfile="gnu"/>
    <tar  tarfile="${dist.home}/${final.name}-src.tar" basedir="${stage.src.dir}" longfile="gnu"/>
    <gzip     src="${dist.home}/${final.name}.tar"     zipfile="${dist.home}/${final.name}.tar.gz"/>
    <gzip     src="${dist.home}/${final.name}-src.tar" zipfile="${dist.home}/${final.name}-src.tar.gz"/>

    <!-- clean up staging directories -->
    <delete    dir="${stage.bin.dir}"/>
    <delete    dir="${stage.src.dir}"/>

  </target>


<!-- ========== Gump Target ===================================================== -->

  <target name="gump" depends="clean,test,javadoc,jar" description="Gump Target - clean,test,javadoc,jar"/>


<!-- ========== Download Dependencies =========================================== -->

    <target name="download-dependencies" 
           depends="check-availability" unless="skip.download">
        <echo message="doing download-dependencies..." />
        <antcall target="download-junit" />
    </target>

    <target name="check-availability">
        <echo message="doing check-availability..." />
        <available file="${junit.jar}" property="junit.found"/>
    </target>

    <target name="download-junit" unless="junit.found">
        <echo message="Downloading junit..."/>
        <mkdir dir="${download.lib.dir}" />
        <get dest="${download.lib.dir}/junit-${junit.version}.jar"
            usetimestamp="true" ignoreerrors="true"
            src="http://repo1.maven.org/maven2/junit/junit/${junit.version}/junit-${junit.version}.jar"/>
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
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>12</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-math</artifactId>
  <version>2.1-SNAPSHOT</version>
  <name>Commons Math</name>

  <inceptionYear>2003</inceptionYear>
  <description>The Math project is a library of lightweight, self-contained mathematics and statistics components addressing the most common practical problems not immediately available in the Java programming language or commons-lang.</description>

  <url>http://commons.apache.org/math/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/MATH</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/math/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/math/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/math/trunk</url>
  </scm>
  
  <distributionManagement>
    <site>
      <id>people.apache.org</id>
      <name>Commons Math</name>
      <url>scp://people.apache.org/www/commons.apache.org/math</url>
    </site>
  </distributionManagement>

  <developers>
    <developer>
      <name>Bill Barker</name>
      <id>billbarker</id>
      <email>billbarker at apache dot org</email>
    </developer>
    <developer>
      <name>Albert Davidson Chou</name>
      <id>achou</id>
      <email>achou at apache dot org</email>
    </developer>
    <developer>
      <name>Mark Diggory</name>
      <id>mdiggory</id>
      <email>mdiggory at apache dot org</email>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
      <email>rdonkin at apache dot org</email>
    </developer>
    <developer>
      <name>Tim O'Brien</name>
      <id>tobrien</id>
      <email>tobrien at apache dot org</email>
    </developer>
    <developer>
      <name>Luc Maisonobe</name>
      <id>luc</id>
      <email>luc at apache dot org</email>
    </developer>
    <developer>
      <name>J. Pietschmann</name>
      <id>pietsch</id>
      <email>j3322ptm at yahoo dot de</email>
    </developer>
    <developer>
      <name>Phil Steitz</name>
      <id>psteitz</id>
      <email>psteitz at apache dot org</email>
    </developer>
    <developer>
      <name>Brent Worden</name>
      <id>brentworden</id>
      <email>brentworden at apache dot org</email>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>C. Scott Ananian</name>
    </contributor>
    <contributor>
      <name>Mikkel Meyer Andersen</name>
    </contributor>
    <contributor>
      <name>Mark Anderson</name>
    </contributor>
    <contributor>
      <name>R&#233;mi Arntzen</name>
    </contributor>
    <contributor>
      <name>Michael Bjorkegren</name>
    </contributor>
    <contributor>
      <name>John Bollinger</name>
    </contributor>
    <contributor>
      <name>Cyril Briquet</name>
    </contributor>
    <contributor>
      <name>Paul Cowan</name>
    </contributor>
    <contributor>
      <name>Benjamin Croizet</name>
    </contributor>
    <contributor>
      <name>Rodrigo di Lorenzo Lopes</name>
    </contributor>
    <contributor>
      <name>Hasan Diwan</name>
    </contributor>
    <contributor>
      <name>Ted Dunning</name>
    </contributor>
    <contributor>
      <name>John Gant</name>
    </contributor>
    <contributor>
      <name>Ken Geis</name>
    </contributor>
    <contributor>
      <name>Bernhard Gr&#252;newaldt</name>
    </contributor>
    <contributor>
      <name>Elliotte Rusty Harold</name>
    </contributor>
    <contributor>
      <name>Matthias Hummel</name>
    </contributor>
    <contributor>
      <name>Ismael Juma</name>
    </contributor>
    <contributor>
      <name>Eugene Kirpichov</name>
    </contributor>
    <contributor>
      <name>Piotr Kochanski</name>
    </contributor>
    <contributor>
      <name>Bob MacCallum</name>
    </contributor>
    <contributor>
      <name>Jake Mannix</name>
    </contributor>
    <contributor>
      <name>Benjamin McCann</name>
      <url>http://www.benmccann.com</url>
    </contributor>
    <contributor>
      <name>Fredrik Norin</name>
    </contributor>
    <contributor>
      <name>Sujit Pal</name>
    </contributor>
    <contributor>
      <name>Todd C. Parnell</name>
    </contributor>
    <contributor>
      <name>Dimitri Pourbaix</name>
    </contributor>
    <contributor>
      <name>Andreas Rieger</name>
    </contributor>
    <contributor>
      <name>Matthew Rowles</name>
    </contributor>
    <contributor>
      <name>Gilles Sadowski</name>
    </contributor>
    <contributor>
      <name>Joni Salonen</name>
    </contributor>
    <contributor>
      <name>Christopher Schuck</name>
    </contributor>
    <contributor>
      <name>Christian Semrau</name>
    </contributor>
    <contributor>
      <name>David Stefka</name>
    </contributor>
    <contributor>
      <name>Mauro Talevi</name>
    </contributor>
    <contributor>
      <name>Kim van der Linde</name>
    </contributor>
    <contributor>
      <name>J&#246;rg Weimar</name>
    </contributor>
    <contributor>
      <name>Xiaogang Zhang</name>
    </contributor>
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.4</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <commons.componentid>math</commons.componentid>
    <commons.release.version>2.0</commons.release.version>
    <commons.binary.suffix></commons.binary.suffix>
    <commons.jira.id>MATH</commons.jira.id>
    <commons.jira.pid>12310485</commons.jira.pid>
    <maven.compile.source>1.5</maven.compile.source>
    <maven.compile.target>1.5</maven.compile.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties> 

  <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
                <include>**/*TestBinary.java</include>
                <include>**/*TestPermutations.java</include>
              </includes>
              <excludes>
                <exclude>**/*AbstractTest.java</exclude>
              </excludes>
          </configuration>
        </plugin>
        <plugin>
          <artifactId>maven-assembly-plugin</artifactId>
          <version>2.2-beta-4</version>
          <configuration>
            <descriptors>
              <descriptor>src/main/assembly/src.xml</descriptor>
              <descriptor>src/main/assembly/bin.xml</descriptor>
            </descriptors>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>cobertura-maven-plugin</artifactId>
          <version>2.2</version>
          <executions>
            <execution>
              <goals>
                <goal>clean</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>clirr-maven-plugin</artifactId>
          <version>2.2.2</version>
          <executions>
            <execution>
              <goals>
              </goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
  </build>
  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.1</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.2</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changes-plugin</artifactId>
        <version>2.1</version>
        <configuration>
          <xmlPath>${basedir}/src/site/xdoc/changes.xml</xmlPath>
          <issueLinkTemplate>%URL%/%ISSUE%</issueLinkTemplate>
          <template>math-release-notes.vm</template>
          <templateDirectory>templates</templateDirectory>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>changes-report</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.3</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
          <headerFile>${basedir}/license-header.txt</headerFile>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <rulesets>
            <ruleset>checkstyle.xml</ruleset>
          </rulesets>
          <linkXref>true</linkXref>
          <targetJdk>1.5</targetJdk>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.2.2</version>
        <configuration>
          <minSeverity>info</minSeverity>
         </configuration>
      </plugin>
    </plugins>
  </reporting>
</project>


```

## src/main/java/org/apache/commons/math/stat/Frequency.java

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
package org.apache.commons.math.stat;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.MathRuntimeException;

/**
 * Maintains a frequency distribution.
 * <p>
 * Accepts int, long, char or Comparable values.  New values added must be
 * comparable to those that have been added, otherwise the add method will
 * throw an IllegalArgumentException.</p>
 * <p>
 * Integer values (int, long, Integer, Long) are not distinguished by type --
 * i.e. <code>addValue(Long.valueOf(2)), addValue(2), addValue(2l)</code> all have
 * the same effect (similarly for arguments to <code>getCount,</code> etc.).</p>
 * <p>
 * char values are converted by <code>addValue</code> to Character instances.
 * As such, these values are not comparable to integral values, so attempts
 * to combine integral types with chars in a frequency distribution will fail.
 * </p>
 * <p>
 * The values are ordered using the default (natural order), unless a
 * <code>Comparator</code> is supplied in the constructor.</p>
 *
 * @version $Revision$ $Date$
 */
public class Frequency implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -3845586908418844111L;

    /** underlying collection */
    private final TreeMap<Comparable<?>, Long> freqTable;

    /**
     * Default constructor.
     */
    public Frequency() {
        freqTable = new TreeMap<Comparable<?>, Long>();
    }

    /**
     * Constructor allowing values Comparator to be specified.
     *
     * @param comparator Comparator used to order values
     */
    @SuppressWarnings("unchecked")
    public Frequency(Comparator<?> comparator) {
        freqTable = new TreeMap<Comparable<?>, Long>((Comparator<? super Comparable<?>>) comparator);
    }

    /**
     * Return a string representation of this frequency
     * distribution.
     *
     * @return a string representation.
     */
    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("Value \t Freq. \t Pct. \t Cum Pct. \n");
        Iterator<Comparable<?>> iter = freqTable.keySet().iterator();
        while (iter.hasNext()) {
            Comparable<?> value = iter.next();
            outBuffer.append(value);
            outBuffer.append('\t');
            outBuffer.append(getCount(value));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getPct(value)));
            outBuffer.append('\t');
            outBuffer.append(nf.format(getCumPct(value)));
            outBuffer.append('\n');
        }
        return outBuffer.toString();
    }

    /**
     * Adds 1 to the frequency count for v.
     * <p>
     * If other objects have already been added to this Frequency, v must
     * be comparable to those that have already been added.
     * </p>
     *
     * @param v the value to add.
     * @throws IllegalArgumentException if <code>v</code> is not Comparable,
     *         or is not comparable with previous entries
     * @deprecated use {@link #addValue(Comparable)} instead
     */
    @Deprecated
    public void addValue(Object v) {
        if (v instanceof Comparable<?>){
            addValue((Comparable<?>) v);
        } else {
            throw MathRuntimeException.createIllegalArgumentException(
                  "class ({0}) does not implement Comparable",
                  v.getClass().getName());
        }
    }

    /**
     * Adds 1 to the frequency count for v.
     * <p>
     * If other objects have already been added to this Frequency, v must
     * be comparable to those that have already been added.
     * </p>
     *
     * @param v the value to add.
     * @throws IllegalArgumentException if <code>v</code> is not comparable with previous entries
     */
    public void addValue(Comparable<?> v){
        Comparable<?> obj = v;
        if (v instanceof Integer) {
           obj = Long.valueOf(((Integer) v).longValue());
        }
        try {
            Long count = freqTable.get(obj);
            if (count == null) {
                freqTable.put(obj, Long.valueOf(1));
            } else {
                freqTable.put(obj, Long.valueOf(count.longValue() + 1));
            }
        } catch (ClassCastException ex) {
            //TreeMap will throw ClassCastException if v is not comparable
            throw MathRuntimeException.createIllegalArgumentException(
                  "instance of class {0} not comparable to existing values",
                  v.getClass().getName());
        }
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     */
    public void addValue(int v) {
        addValue(Long.valueOf(v));
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     * @deprecated to be removed in math 3.0
     */
    public void addValue(Integer v) {
        addValue(Long.valueOf(v.longValue()));
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     */
    public void addValue(long v) {
        addValue(Long.valueOf(v));
    }

    /**
     * Adds 1 to the frequency count for v.
     *
     * @param v the value to add.
     */
    public void addValue(char v) {
        addValue(Character.valueOf(v));
    }

    /** Clears the frequency table */
    public void clear() {
        freqTable.clear();
    }

    /**
     * Returns an Iterator over the set of values that have been added.
     * <p>
     * If added values are integral (i.e., integers, longs, Integers, or Longs),
     * they are converted to Longs when they are added, so the objects returned
     * by the Iterator will in this case be Longs.</p>
     *
     * @return values Iterator
     */
    public Iterator<Comparable<?>> valuesIterator() {
        return freqTable.keySet().iterator();
    }

    //-------------------------------------------------------------------------

    /**
     * Returns the sum of all frequencies.
     *
     * @return the total frequency count.
     */
    public long getSumFreq() {
        long result = 0;
        Iterator<Long> iterator = freqTable.values().iterator();
        while (iterator.hasNext())  {
            result += iterator.next().longValue();
        }
        return result;
    }

    /**
     * Returns the number of values = v.
     * Returns 0 if the value is not comparable.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     * @deprecated replaced by {@link #getCount(Comparable)} as of 2.0
     */
    @Deprecated
    public long getCount(Object v) {
        return getCount((Comparable<?>) v);
    }

    /**
     * Returns the number of values = v.
     * Returns 0 if the value is not comparable.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(Comparable<?> v) {
        if (v instanceof Integer) {
            return getCount(((Integer) v).longValue());
        }
        long result = 0;
        try {
            Long count =  freqTable.get(v);
            if (count != null) {
                result = count.longValue();
            }
        } catch (ClassCastException ex) {
            // ignore and return 0 -- ClassCastException will be thrown if value is not comparable
        }
        return result;
    }

    /**
     * Returns the number of values = v.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(int v) {
        return getCount(Long.valueOf(v));
    }

    /**
     * Returns the number of values = v.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(long v) {
        return getCount(Long.valueOf(v));
    }

    /**
     * Returns the number of values = v.
     *
     * @param v the value to lookup.
     * @return the frequency of v.
     */
    public long getCount(char v) {
        return getCount(Character.valueOf(v));
    }

    //-------------------------------------------------------------

    /**
      * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns <code>Double.NaN</code> if no values have been added.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     * @deprecated replaced by {@link #getPct(Comparable)} as of 2.0
     */
    @Deprecated
    public double getPct(Object v) {
        return getPct((Comparable<?>) v);
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns <code>Double.NaN</code> if no values have been added.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(Comparable<?> v) {
        final long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return (double) getCount(v) / (double) sumFreq;
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(int v) {
        return getPct(Long.valueOf(v));
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(long v) {
        return getPct(Long.valueOf(v));
    }

    /**
     * Returns the percentage of values that are equal to v
     * (as a proportion between 0 and 1).
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public double getPct(char v) {
        return getPct(Character.valueOf(v));
    }

    //-----------------------------------------------------------------------------------------

    /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup.
     * @return the proportion of values equal to v
     * @deprecated replaced by {@link #getCumFreq(Comparable)} as of 2.0
     */
    @Deprecated
    public long getCumFreq(Object v) {
        return getCumFreq((Comparable<?>) v);
    }

    /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup.
     * @return the proportion of values equal to v
     */
    @SuppressWarnings("unchecked")
        public long getCumFreq(Comparable<?> v) {
        if (getSumFreq() == 0) {
            return 0;
        }
        if (v instanceof Integer) {
            return getCumFreq(((Integer) v).longValue());
        }
        Comparator<Comparable<?>> c = (Comparator<Comparable<?>>) freqTable.comparator();
        if (c == null) {
            c = new NaturalComparator();
        }
        long result = 0;

        try {
            Long value = freqTable.get(v);
            if (value != null) {
                result = value.longValue();
            }
        } catch (ClassCastException ex) {
            return result;   // v is not comparable
        }

        if (c.compare(v, freqTable.firstKey()) < 0) {
            return 0;  // v is comparable, but less than first value
        }

        if (c.compare(v, freqTable.lastKey()) >= 0) {
            return getSumFreq();    // v is comparable, but greater than the last value
        }

        Iterator<Comparable<?>> values = valuesIterator();
        while (values.hasNext()) {
            Comparable<?> nextValue = values.next();
            if (c.compare(v, nextValue) > 0) {
                result += getCount(nextValue);
            } else {
                return result;
            }
        }
        return result;
    }

     /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public long getCumFreq(int v) {
        return getCumFreq(Long.valueOf(v));
    }

     /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public long getCumFreq(long v) {
        return getCumFreq(Long.valueOf(v));
    }

    /**
     * Returns the cumulative frequency of values less than or equal to v.
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values equal to v
     */
    public long getCumFreq(char v) {
        return getCumFreq(Character.valueOf(v));
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns <code>Double.NaN</code> if no values have been added.
     * Returns 0 if at least one value has been added, but v is not comparable
     * to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     * @deprecated replaced by {@link #getCumPct(Comparable)} as of 2.0
     */
    @Deprecated
    public double getCumPct(Object v) {
        return getCumPct((Comparable<?>) v);

    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns <code>Double.NaN</code> if no values have been added.
     * Returns 0 if at least one value has been added, but v is not comparable
     * to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(Comparable<?> v) {
        final long sumFreq = getSumFreq();
        if (sumFreq == 0) {
            return Double.NaN;
        }
        return (double) getCumFreq(v) / (double) sumFreq;
    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(int v) {
        return getCumPct(Long.valueOf(v));
    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(long v) {
        return getCumPct(Long.valueOf(v));
    }

    /**
     * Returns the cumulative percentage of values less than or equal to v
     * (as a proportion between 0 and 1).
     * <p>
     * Returns 0 if v is not comparable to the values set.</p>
     *
     * @param v the value to lookup
     * @return the proportion of values less than or equal to v
     */
    public double getCumPct(char v) {
        return getCumPct(Character.valueOf(v));
    }

    /**
     * A Comparator that compares comparable objects using the
     * natural order.  Copied from Commons Collections ComparableComparator.
     */
    private static class NaturalComparator<T extends Comparable<T>> implements Comparator<Comparable<T>>, Serializable {

        /** Serializable version identifier */
        private static final long serialVersionUID = -3852193713161395148L;

        /**
         * Compare the two {@link Comparable Comparable} arguments.
         * This method is equivalent to:
         * <pre>(({@link Comparable Comparable})o1).{@link Comparable#compareTo compareTo}(o2)</pre>
         *
         * @param  o1 the first object
         * @param  o2 the second object
         * @return  result of comparison
         * @throws NullPointerException when <i>o1</i> is <code>null</code>,
         *         or when <code>((Comparable)o1).compareTo(o2)</code> does
         * @throws ClassCastException when <i>o1</i> is not a {@link Comparable Comparable},
         *         or when <code>((Comparable)o1).compareTo(o2)</code> does
         */
        @SuppressWarnings("unchecked")
        public int compare(Comparable<T> o1, Comparable<T> o2) {
            return o1.compareTo((T) o2);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result +
                 ((freqTable == null) ? 0 : freqTable.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Frequency))
            return false;
        Frequency other = (Frequency) obj;
        if (freqTable == null) {
            if (other.freqTable != null)
                return false;
        } else if (!freqTable.equals(other.freqTable))
            return false;
        return true;
    }

}

```
