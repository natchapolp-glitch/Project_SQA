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
  <property name="junit.version"           value="4.8.2"/>
  <property name="junit.home"              value="/usr/share/junit"/>
  <property name="junit.jar"               value="${junit.home}/junit-${junit.version}.jar"/>


<!-- ========== Component Declarations ==================================== -->


  <!-- The name of this component -->
  <property name="component.name"          value="commons-math"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.apache.commons.math3"/>

  <!-- The title of this component -->
  <property name="component.title"         value="Commons MATH"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="3.0-SNAPSHOT"/>

  <!-- The base directory for component sources -->
  <property name="source.home"             value="src/main/java"/>

  <!-- The base directory for component resources -->
  <property name="source.resources"        value="src/main/resources"/>

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

  <!-- The base directory for test reports -->
  <property name="test.reports"            value="${build.home}/test-reports"/>

  <!-- Base file name for dist files -->
  <property name="final.name"              value="${component.name}-${component.version}"/>

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
    <copy todir="${build.home}/classes/">
       <fileset dir="${source.resources}" />
    </copy>

  </target>

<!-- ========== Compile Targets =========================================== -->

  <target name="compile" depends="init" description="Compile">

    <javac  srcdir="${source.home}"
           destdir="${build.home}/classes"
             source="${compile.source}"
             target="${compile.target}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
 includeantruntime="false"
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
             includeantruntime="false"
             optimize="${compile.optimize}">
          <classpath refid="test.classpath"/>
      </javac>
    
      <copy todir="${build.home}/test-classes">
          <fileset dir="${test.resources}">
          </fileset>
      </copy>

    </target>

  <target name="test"  depends="compile.tests"
                       description="Run unit tests">
      <mkdir dir="${test.reports}"/>
      <junit printsummary="true"
               errorProperty="test.failed"
               failureProperty="test.failed"
               fork="true"
               showOutput="true">
               <formatter type="brief"/>
               <classpath refid="test.classpath"/>
               <!-- If test.entry is defined, run a single test, otherwise run all valid tests -->
               <!-- N.B. test.entry must be the full path to the test class, for example:
               ant test -Dtest.entry=org.apache.commons.math3.util.FastMathTestPerformance
               -->
               <test name="${test.entry}" todir="${test.reports}" if="test.entry"/>
               <batchtest todir="${test.reports}" unless="test.entry">
                   <fileset dir="${test.home}">
                      <include name="**/*Test.java"/> 
                      <include name="**/*TestBinary.java"/> 
                      <include name="**/*TestPermutations.java"/> 
                      <exclude name="**/*AbstractTest.java"/>
                   </fileset>
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
    <version>24</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-math3</artifactId>
  <version>3.1-SNAPSHOT</version>
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
      <name>Mikkel Meyer Andersen</name>
      <id>mikl</id>
      <email>mikl at apache dot org</email>
    </developer>
    <developer>
      <name>Bill Barker</name>
      <id>billbarker</id>
      <email>billbarker at apache dot org</email>
    </developer>
    <developer>
      <name>S&#233;bastien Brisard</name>
      <id>celestin</id>
      <email>celestin at apache dot org</email>
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
      <name>Luc Maisonobe</name>
      <id>luc</id>
      <email>luc at apache dot org</email>
    </developer>
    <developer>
      <name>Tim O'Brien</name>
      <id>tobrien</id>
      <email>tobrien at apache dot org</email>
    </developer>
    <developer>
      <name>J. Pietschmann</name>
      <id>pietsch</id>
      <email>j3322ptm at yahoo dot de</email>
    </developer>
    <developer>
      <name>Dimitri Pourbaix</name>
      <id>dimpbx</id>
      <email>dimpbx at apache dot org</email>
    </developer>
    <developer>
      <name>Gilles Sadowski</name>
      <id>erans</id>
      <email>erans at apache dot org</email>
    </developer>
    <developer>
      <name>Phil Steitz</name>
      <id>psteitz</id>
      <email>psteitz at apache dot org</email>
    </developer>
    <developer>
      <name>Greg Sterijevski</name>
      <id>gregs</id>
      <email>gregs at apache dot org</email>
    </developer>
    <developer>
      <name>Brent Worden</name>
      <id>brentworden</id>
      <email>brentworden at apache dot org</email>
    </developer>
    <developer>
      <name>Thomas Neidhart</name>
      <id>tn</id>
      <email>tn at apache dot org</email>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>Eldar Agalarov</name>
    </contributor>
    <contributor>
      <name>C. Scott Ananian</name>
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
      <name>Dave Brosius</name>
    </contributor>
    <contributor>
      <name>Paul Cowan</name>
    </contributor>
    <contributor>
      <name>Benjamin Croizet</name>
    </contributor>
    <contributor>
      <name>Larry Diamond</name>
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
      <name>Dennis Hendriks</name>
    </contributor>
    <contributor>
      <name>Reid Hochstedler</name>
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
    </contributor>
    <contributor>
      <name>Patrick Meyer</name>
    </contributor>
    <contributor>
      <name>J. Lewis Muir</name>
    </contributor>
    <contributor>
      <name>Christopher Nix</name>
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
      <name>Andreas Rieger</name>
    </contributor>
    <contributor>
      <name>Bill Rossi</name>
    </contributor>
    <contributor>
      <name>Matthew Rowles</name>
    </contributor>
    <contributor>
      <name>Pavel Ryzhov</name>
    </contributor>
    <contributor>
      <name>Joni Salonen</name>
    </contributor>
    <contributor>
      <name>Michael Saunders</name>
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
      <name>Christian Winter</name>
    </contributor>
    <contributor>
      <name>Xiaogang Zhang</name>
    </contributor>
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.10</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <commons.componentid>math</commons.componentid>
    <!-- do not use snapshot suffix here -->
    <commons.release.version>3.0</commons.release.version>
    <commons.release.desc>(requires Java 1.5+; not compatible with 2.2)</commons.release.desc>
    <!-- <commons.rc.version>RC1</commons.rc.version> -->
    <commons.binary.suffix>-bin</commons.binary.suffix>
 
    <commons.release.2.version>2.2</commons.release.2.version>
    <!-- override parent name, because 2.2 uses different artifactId -->
    <commons.release.2.name>commons-math-${commons.release.2.version}</commons.release.2.name>
    <commons.release.2.desc>(requires Java 1.5+)</commons.release.2.desc>
    <commons.release.2.binary.suffix></commons.release.2.binary.suffix>
 
    <commons.jira.id>MATH</commons.jira.id>
    <commons.jira.pid>12310485</commons.jira.pid>
    <maven.compile.source>1.5</maven.compile.source>
    <maven.compile.target>1.5</maven.compile.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <math.pmd.version>2.7.1</math.pmd.version>
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
          <configuration>
            <descriptors>
              <descriptor>src/main/assembly/src.xml</descriptor>
              <descriptor>src/main/assembly/bin.xml</descriptor>
            </descriptors>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>clirr-maven-plugin</artifactId>
          <version>${commons.clirr.version}</version>
          <executions>
            <execution>
              <goals>
              </goals>
            </execution>
          </executions>
        </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>${math.pmd.version}</version>
        <configuration>
          <targetJdk>${maven.compile.target}</targetJdk>  
        </configuration>
        </plugin>
      </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changes-plugin</artifactId>
        <version>${commons.changes.version}</version>
        <configuration>
          <issueLinkTemplatePerSystem>
            <default>%URL%/%ISSUE%</default>
          </issueLinkTemplatePerSystem>
          <!--  Add sample JIRA report - 'mvn changes:jira-report' or 'mvn site' -->
          <onlyCurrentVersion>false</onlyCurrentVersion>
          <columnNames>Fix Version,Key,Summary,Type,Resolution,Status</columnNames>
          <!-- Sort cols have to be reversed in JIRA 4 -->
          <sortColumnNames>Key DESC,Type,Fix Version DESC</sortColumnNames>
          <resolutionIds>Fixed</resolutionIds>
          <statusIds>Resolved,Closed</statusIds>
          <!-- Don't include sub-task -->
          <typeIds>Bug,New Feature,Task,Improvement,Wish,Test</typeIds>
          <fixVersionIds>${commons.release.version}</fixVersionIds>
          <!-- The default is 100 -->
          <maxEntries>100</maxEntries>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>changes-report</report>
              <report>jira-report</report>
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
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.7</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
          <headerLocation>${basedir}/license-header.txt</headerLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>${commons.clirr.version}</version>
        <configuration>
          <minSeverity>${minSeverity}</minSeverity>
         </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>${math.pmd.version}</version>
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
</project>


```

## src/main/java/org/apache/commons/math3/optimization/univariate/BrentOptimizer.java

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
package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

/**
 * Implements Richard Brent's algorithm (from his book "Algorithms for
 * Minimization without Derivatives", p. 79) for finding minima of real
 * univariate functions. This implementation is an adaptation partly
 * based on the Python code from SciPy (module "optimize.py" v0.5).
 * If the function is defined on some interval {@code (lo, hi)}, then
 * this method finds an approximation {@code x} to the point at which
 * the function attains its minimum.
 *
 * @version $Id$
 * @since 2.0
 */
public class BrentOptimizer extends BaseAbstractUnivariateOptimizer {
    /**
     * Golden section.
     */
    private static final double GOLDEN_SECTION = 0.5 * (3 - FastMath.sqrt(5));
    /**
     * Minimum relative tolerance.
     */
    private static final double MIN_RELATIVE_TOLERANCE = 2 * FastMath.ulp(1d);
    /**
     * Relative threshold.
     */
    private final double relativeThreshold;
    /**
     * Absolute threshold.
     */
    private final double absoluteThreshold;

    /**
     * The arguments are used implement the original stopping criterion
     * of Brent's algorithm.
     * {@code abs} and {@code rel} define a tolerance
     * {@code tol = rel |x| + abs}. {@code rel} should be no smaller than
     * <em>2 macheps</em> and preferably not much less than <em>sqrt(macheps)</em>,
     * where <em>macheps</em> is the relative machine precision. {@code abs} must
     * be positive.
     *
     * @param rel Relative threshold.
     * @param abs Absolute threshold.
     * @param checker Additional, user-defined, convergence checking
     * procedure.
     * @throws NotStrictlyPositiveException if {@code abs <= 0}.
     * @throws NumberIsTooSmallException if {@code rel < 2 * Math.ulp(1d)}.
     */
    public BrentOptimizer(double rel,
                          double abs,
                          ConvergenceChecker<UnivariatePointValuePair> checker) {
        super(checker);

        if (rel < MIN_RELATIVE_TOLERANCE) {
            throw new NumberIsTooSmallException(rel, MIN_RELATIVE_TOLERANCE, true);
        }
        if (abs <= 0) {
            throw new NotStrictlyPositiveException(abs);
        }

        relativeThreshold = rel;
        absoluteThreshold = abs;
    }

    /**
     * The arguments are used for implementing the original stopping criterion
     * of Brent's algorithm.
     * {@code abs} and {@code rel} define a tolerance
     * {@code tol = rel |x| + abs}. {@code rel} should be no smaller than
     * <em>2 macheps</em> and preferably not much less than <em>sqrt(macheps)</em>,
     * where <em>macheps</em> is the relative machine precision. {@code abs} must
     * be positive.
     *
     * @param rel Relative threshold.
     * @param abs Absolute threshold.
     * @throws NotStrictlyPositiveException if {@code abs <= 0}.
     * @throws NumberIsTooSmallException if {@code rel < 2 * Math.ulp(1d)}.
     */
    public BrentOptimizer(double rel,
                          double abs) {
        this(rel, abs, null);
    }

    /** {@inheritDoc} */
    @Override
    protected UnivariatePointValuePair doOptimize() {
        final boolean isMinim = getGoalType() == GoalType.MINIMIZE;
        final double lo = getMin();
        final double mid = getStartValue();
        final double hi = getMax();

        // Optional additional convergence criteria.
        final ConvergenceChecker<UnivariatePointValuePair> checker
            = getConvergenceChecker();

        double a;
        double b;
        if (lo < hi) {
            a = lo;
            b = hi;
        } else {
            a = hi;
            b = lo;
        }

        double x = mid;
        double v = x;
        double w = x;
        double d = 0;
        double e = 0;
        double fx = computeObjectiveValue(x);
        if (!isMinim) {
            fx = -fx;
        }
        double fv = fx;
        double fw = fx;

        UnivariatePointValuePair previous = null;
        UnivariatePointValuePair current
            = new UnivariatePointValuePair(x, isMinim ? fx : -fx);

        int iter = 0;
        while (true) {
            final double m = 0.5 * (a + b);
            final double tol1 = relativeThreshold * FastMath.abs(x) + absoluteThreshold;
            final double tol2 = 2 * tol1;

            // Default stopping criterion.
            final boolean stop = FastMath.abs(x - m) <= tol2 - 0.5 * (b - a);
            if (!stop) {
                double p = 0;
                double q = 0;
                double r = 0;
                double u = 0;

                if (FastMath.abs(e) > tol1) { // Fit parabola.
                    r = (x - w) * (fx - fv);
                    q = (x - v) * (fx - fw);
                    p = (x - v) * q - (x - w) * r;
                    q = 2 * (q - r);

                    if (q > 0) {
                        p = -p;
                    } else {
                        q = -q;
                    }

                    r = e;
                    e = d;

                    if (p > q * (a - x) &&
                        p < q * (b - x) &&
                        FastMath.abs(p) < FastMath.abs(0.5 * q * r)) {
                        // Parabolic interpolation step.
                        d = p / q;
                        u = x + d;

                        // f must not be evaluated too close to a or b.
                        if (u - a < tol2 || b - u < tol2) {
                            if (x <= m) {
                                d = tol1;
                            } else {
                                d = -tol1;
                            }
                        }
                    } else {
                        // Golden section step.
                        if (x < m) {
                            e = b - x;
                        } else {
                            e = a - x;
                        }
                        d = GOLDEN_SECTION * e;
                    }
                } else {
                    // Golden section step.
                    if (x < m) {
                        e = b - x;
                    } else {
                        e = a - x;
                    }
                    d = GOLDEN_SECTION * e;
                }

                // Update by at least "tol1".
                if (FastMath.abs(d) < tol1) {
                    if (d >= 0) {
                        u = x + tol1;
                    } else {
                        u = x - tol1;
                    }
                } else {
                    u = x + d;
                }

                double fu = computeObjectiveValue(u);
                if (!isMinim) {
                    fu = -fu;
                }

                // User-defined convergence checker.
                previous = current;
                current = new UnivariatePointValuePair(u, isMinim ? fu : -fu);

                if (checker != null) {
                    if (checker.converged(iter, previous, current)) {
                        return best(current, previous, isMinim);
                    }
                }

                // Update a, b, v, w and x.
                if (fu <= fx) {
                    if (u < x) {
                        b = x;
                    } else {
                        a = x;
                    }
                    v = w;
                    fv = fw;
                    w = x;
                    fw = fx;
                    x = u;
                    fx = fu;
                } else {
                    if (u < x) {
                        a = u;
                    } else {
                        b = u;
                    }
                    if (fu <= fw ||
                        Precision.equals(w, x)) {
                        v = w;
                        fv = fw;
                        w = u;
                        fw = fu;
                    } else if (fu <= fv ||
                               Precision.equals(v, x) ||
                               Precision.equals(v, w)) {
                        v = u;
                        fv = fu;
                    }
                }
            } else { // Default termination (Brent's criterion).
                return best(current, previous, isMinim);
            }
            ++iter;
        }
    }

    /**
     * Selects the best of two points.
     *
     * @param a Point and value.
     * @param b Point and value.
     * @param isMinim {@code true} if the selected point must be the one with
     * the lowest value.
     * @return the best point, or {@code null} if {@code a} and {@code b} are
     * both {@code null}.
     */
    private UnivariatePointValuePair best(UnivariatePointValuePair a,
                                          UnivariatePointValuePair b,
                                          boolean isMinim) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        if (isMinim) {
            return a.getValue() < b.getValue() ? a : b;
        } else {
            return a.getValue() > b.getValue() ? a : b;
        }
    }
}

```
