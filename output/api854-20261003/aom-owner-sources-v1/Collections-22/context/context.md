## build.xml

```
<!--
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
 -->
<!--
   "Collections" component of the Apache Commons Subproject
   $Id$
-->
<project name="Collections" default="compile" basedir=".">

    <!-- ========== Initialize Properties ===================================== -->
    <property file="${user.home}/${component.name}.build.properties"/>
    <property file="${user.home}/build.properties"/>
    <property file="${basedir}/build.properties"/>
    <property file="${basedir}/default.properties"/>
    <property name="jdk.javadoc" value="http://download.oracle.com/javase/1.5.0/docs/api/"/>

    <!-- ========== Construct compile classpath =============================== -->
    <path id="compile.classpath">
        <pathelement location="${build.home}/classes"/>
    </path>

    <!-- ========== Construct unit test classpath ============================= -->
    <path id="test.classpath">
        <pathelement location="${build.home}/classes"/>
        <pathelement location="${build.home}/tests"/>
        <pathelement location="${junit.jar}"/>
        <pathelement location="${hamcrest.jar}"/>        
        <pathelement location="${easymock.jar}"/>
    </path>

    <!-- ========== Executable Targets ======================================== -->
    <target name="init" description="Initialize and evaluate conditionals">
        <echo message="-------- ${component.name} ${component.version} --------"/>
        <filter token="name" value="${component.name}"/>
        <filter token="package" value="${component.package}"/>
        <filter token="version" value="${component.version}"/>
        <filter token="compile.source" value="${compile.source}"/>
        <filter token="compile.target" value="${compile.target}"/>
        <mkdir dir="${build.home}"/>
    </target>

    <!-- ========== Compile Targets ========================================= -->
    <target name="compile" depends="init" description="Compile shareable components">
        <mkdir dir="${build.home}/classes"/>
        <javac srcdir="${source.home}" destdir="${build.home}/classes" debug="${compile.debug}" deprecation="${compile.deprecation}" target="${compile.target}" source="${compile.source}" excludes="${compile.excludes}" optimize="${compile.optimize}" includeantruntime="false" encoding="${compile.encoding}">
            <classpath refid="compile.classpath"/>
        </javac>
        <copy todir="${build.home}/classes" filtering="on">
            <fileset dir="${source.home}" excludes="**/*.java,**/*.html"/>
        </copy>
    </target>

    <target name="compile.tests" depends="compile" description="Compile unit test cases">
        <mkdir dir="${build.home}/tests"/>
        <javac srcdir="${test.home}" destdir="${build.home}/tests" debug="${compile.debug}" deprecation="off" target="${compile.target}" source="${compile.source}" optimize="${compile.optimize}" includeantruntime="false" encoding="${compile.encoding}">
            <classpath refid="test.classpath"/>
        </javac>
        <copy todir="${build.home}/tests" filtering="on">
            <fileset dir="${test.home}" excludes="**/*.java"/>
        </copy>
    </target>

    <!-- ========== Unit Tests ========================================= -->
    <target name="test" depends="compile.tests" description="Run all unit test cases">
        <echo message="Running unit tests ..."/>
        <mkdir dir="${build.home}/test-reports"/>
        <junit printsummary="true" showoutput="true" fork="yes" haltonfailure="${test.failonerror}">
            <classpath refid="test.classpath"/>
            <formatter type="plain" usefile="true" />
            <!-- If test.entry is defined, run a single test, otherwise run all valid tests -->
            <test name="${test.entry}" todir="${build.home}/test-reports" if="test.entry"/>
            <batchtest fork="yes" todir="${build.home}/test-reports" unless="test.entry">
                <fileset dir="${test.home}">
                    <include name="**/*Test.java"/>
                    <exclude name="**/*$*"/>
                    <exclude name="**/TestUtils.java"/>
                    <exclude name="**/Abstract*.java"/>
                    <exclude name="**/BulkTest.java"/>
                </fileset>
            </batchtest>
        </junit>
    </target>

    <target name="clean" description="Clean build and distribution directories">
        <delete dir="${build.home}"/>
    </target>

    <target name="all" depends="clean,test,compile" description="Clean and compile all components"/>

    <!-- ========== JavaDocs ========================================= -->
    <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
        <mkdir dir="${build.home}"/>
        <mkdir dir="${build.home}/apidocs"/>
        <tstamp>
            <format property="current.year" pattern="yyyy"/>
        </tstamp>
        <javadoc sourcepath="${source.home}" 
                 destdir="${build.home}/apidocs" 
                 overview="${source.home}/org/apache/commons/collections4/overview.html" 
                 packagenames="org.apache.commons.*" 
                 excludepackagenames="${javadoc.excludepackagenames}" 
                 author="false" 
                 version="true" 
                 doctitle="&lt;h1&gt;Commons Lang ${component.version}&lt;/h1&gt;"
                 windowtitle="Lang ${component.version}" 
                 bottom="Copyright &amp;copy; 2001-${current.year} - Apache Software Foundation" 
                 use="true" 
                 encoding="${compile.encoding}"
                 source="${compile.source}">
            <classpath refid="compile.classpath"/>
            <link href="${jdk.javadoc}"/>
            <link href="${collections.javadoc}"/>
        </javadoc>
    </target>

    <!-- ========== Jar Targets ========================================= -->
    <target name="jar" depends="compile" description="Create jar">
        <mkdir dir="${build.home}/classes/META-INF"/>
        <copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
        <copy file="NOTICE.txt"  tofile="${build.home}/classes/META-INF/NOTICE.txt"/>
        <jar jarfile="${build.home}/${final.name}.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Collections"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Collections"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
                <attribute name="X-Compile-Source-JDK" value="${compile.source}"/>
                <attribute name="X-Compile-Target-JDK" value="${compile.target}"/>
            </manifest>
            <fileset dir="${build.home}/classes">
                <include name="**/*.class"/>
                <include name="**/LICENSE.txt"/>
                <include name="**/NOTICE.txt"/>
            </fileset>
        </jar>
    </target>

    <target name="javadoc-jar" depends="javadoc" description="Create JavaDoc jar">
        <jar jarfile="${build.home}/${final.name}-javadoc.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Collections API"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Collections API"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
            </manifest>
            <fileset dir="${build.home}/apidocs"/>
            <fileset dir="${basedir}">
                <include name="LICENSE.txt"/>
                <include name="NOTICE.txt"/>
            </fileset>
        </jar>
    </target>

    <target name="source-jar" depends="init" description="Create Source jar">
        <jar jarfile="${build.home}/${final.name}-sources.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Collections Source"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Collections Source"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
            </manifest>
            <fileset dir="${source.home}">
                <include name="**/*.java"/>
            </fileset>
            <fileset dir="${basedir}">
                <include name="LICENSE.txt"/>
                <include name="NOTICE.txt"/>
            </fileset>
        </jar>
    </target>

    <target name="test-jar" depends="init" description="Create Testframework jar">
        <jar jarfile="${build.home}/${final.name}-tests.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Collections Testframework"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Collections Testframework"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
            </manifest>
            <fileset dir="${test.home}">
                <include name="**/*.java"/>
            </fileset>
            <fileset dir="${basedir}">
                <include name="LICENSE.txt"/>
                <include name="NOTICE.txt"/>
            </fileset>
        </jar>
    </target>
    
    <!-- ========== Distribution ========================================= -->
    <target name="dist" depends="clean,jar,source-jar,javadoc-jar,test-jar" description="Create binary distribution">

        <!-- binary distro -->
        <zip destfile="${build.home}/${final.name}.zip">
            <zipfileset dir="${basedir}" prefix="${final.name}"
                      includes="LICENSE.txt,
                                NOTICE.txt,
                                RELEASE-NOTES.txt"
             />
            <zipfileset dir="${build.home}" includes="*.jar," prefix="${final.name}"/>
            <zipfileset dir="${build.home}/apidocs" prefix="${final.name}/apidocs"/>
        </zip>
        <tar destfile="${build.home}/${final.name}.tar.gz" compression="gzip">
            <zipfileset src="${build.home}/${final.name}.zip"/>
        </tar>

        <!-- source distro -->
        <zip destfile="${build.home}/${final.name}-src.zip">
            <zipfileset dir="${basedir}" prefix="${final.name}-src"
                      includes="build.xml,
                                default.properties,
                                build.properties.sample,
                                DEVELOPERS-GUIDE.html,
                                PROPOSAL.html,
                                LICENSE.txt,
                                NOTICE.txt,
                                README.txt,
                                pom.xml,
                                RELEASE-NOTES.txt"
             />
            <zipfileset dir="${basedir}/src"   prefix="${final.name}-src/src"/>
        </zip>
        <tar destfile="${build.home}/${final.name}-src.tar.gz" compression="gzip">
            <zipfileset src="${build.home}/${final.name}-src.zip"/>
        </tar>

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
    <version>30</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-collections4</artifactId>
  <version>4.0-SNAPSHOT</version>
  <name>Commons Collections</name>

  <inceptionYear>2001</inceptionYear>
  <description>Types that extend and augment the Java Collections Framework.</description>

  <url>http://commons.apache.org/proper/commons-collections/</url>

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
      <name>Jeffrey Barnes</name>
    </contributor>
    <contributor>
      <name>Nicola Ken Barozzi</name>
    </contributor>
    <contributor>
      <name>Arron Bates</name>
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
      <name>Benoit Corne</name>
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
      <name>David Hay</name>
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
      <name>Jeff Rodriguez</name>
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
      <version>3.1</version>
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
    
    <!-- Need to change to collections4 when releasing -->
    <commons.componentid>collections</commons.componentid>

    <!-- Current 4.x release series: still points to old, set to 4.0 when releasing -->
    <commons.release.version>3.2.1</commons.release.version>
    <commons.release.desc>(Java 5.0+)</commons.release.desc>
    
    <!-- Previous 3.x release series -->
    <commons.release.2.version>3.2.1</commons.release.2.version>
    <commons.release.2.desc>(Requires Java 1.2 or later)</commons.release.2.desc>
    <!-- Override generated name -->
    <commons.release.2.name>commons-collections-${commons.release.2.version}</commons.release.2.name>
    
    <commons.jira.id>COLLECTIONS</commons.jira.id>
    <commons.jira.pid>12310465</commons.jira.pid>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC2</commons.rc.version>
    <checkstyle.version>2.9.1</checkstyle.version>
    
    <commons.site.path>collections</commons.site.path>
    <commons.scmPubUrl>https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-collections</commons.scmPubUrl>
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
          <configLocation>${basedir}/src/conf/checkstyle.xml</configLocation>
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
          <maxEntries>500</maxEntries>
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
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${checkstyle.version}</version>
        <configuration>
          <configLocation>${basedir}/src/conf/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
	      <suppressionsLocation>${basedir}/src/conf/checkstyle-suppressions.xml</suppressionsLocation>
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
        <artifactId>clirr-maven-plugin</artifactId>
        <version>${commons.clirr.version}</version>
        <configuration>
          <minSeverity>${minSeverity}</minSeverity>
         </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.4.0</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/src/conf/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.5.2</version>
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
      <plugin>
        <groupId>org.apache.rat</groupId>
        <artifactId>apache-rat-plugin</artifactId>
        <configuration>
          <excludes>
            <exclude>site-content/**/*</exclude>
          </excludes>
        </configuration>
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

## src/main/java/org/apache/commons/collections4/map/ListOrderedMap.java

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
package org.apache.commons.collections4.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntry;
import org.apache.commons.collections4.list.UnmodifiableList;

/**
 * Decorates a <code>Map</code> to ensure that the order of addition is retained
 * using a <code>List</code> to maintain order.
 * <p>
 * The order will be used via the iterators and toArray methods on the views.
 * The order is also returned by the <code>MapIterator</code>.
 * The <code>orderedMapIterator()</code> method accesses an iterator that can
 * iterate both forwards and backwards through the map.
 * In addition, non-interface methods are provided to access the map by index.
 * <p>
 * If an object is added to the Map for a second time, it will remain in the
 * original position in the iteration.
 * <p>
 * <strong>Note that ListOrderedMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedMap(Map)}. This class may throw
 * exceptions when accessed by concurrent threads without synchronization.
 * <p>
 * <strong>Note that ListOrderedMap doesn't work with
 * {@link java.util.IdentityHashMap IdentityHashMap}, {@link CaseInsensitiveMap},
 * or similar maps that violate the general contract of {@link java.util.Map}.</strong>
 * The <code>ListOrderedMap</code> (or, more precisely, the underlying <code>List</code>)
 * is relying on {@link Object#equals(Object) equals()}. This is fine, as long as the
 * decorated <code>Map</code> is also based on {@link Object#equals(Object) equals()},
 * and {@link Object#hashCode() hashCode()}, which
 * {@link java.util.IdentityHashMap IdentityHashMap}, and
 * {@link CaseInsensitiveMap} don't: The former uses <code>==</code>, and
 * the latter uses {@link Object#equals(Object) equals()} on a lower-cased
 * key.
 * <p>
 * This class is {@link Serializable} starting with Commons Collections 3.1.
 *
 * @since 3.0
 * @version $Id$
 */
public class ListOrderedMap<K, V>
        extends AbstractMapDecorator<K, V>
        implements OrderedMap<K, V>, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2728177751851003750L;

    /** Internal list to hold the sequence of objects */
    private final List<K> insertOrder = new ArrayList<K>();

    /**
     * Factory method to create an ordered map.
     * <p>
     * An <code>ArrayList</code> is used to retain order.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @param map  the map to decorate, must not be null
     * @return a new list ordered map
     * @throws IllegalArgumentException if map is null
     * @since 4.0
     */
    public static <K, V> ListOrderedMap<K, V> listOrderedMap(final Map<K, V> map) {
        return new ListOrderedMap<K, V>(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a new empty <code>ListOrderedMap</code> that decorates
     * a <code>HashMap</code>.
     *
     * @since 3.1
     */
    public ListOrderedMap() {
        this(new HashMap<K, V>());
    }

    /**
     * Constructor that wraps (not copies).
     *
     * @param map  the map to decorate, must not be null
     * @throws IllegalArgumentException if map is null
     */
    protected ListOrderedMap(final Map<K, V> map) {
        super(map);
        insertOrder.addAll(decorated().keySet());
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     *
     * @param out  the output stream
     * @throws IOException
     * @since 3.1
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }

    /**
     * Read the map in using a custom routine.
     *
     * @param in  the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     * @since 3.1
     */
    @SuppressWarnings("unchecked") // (1) should only fail if input stream is incorrect
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject(); // (1)
    }

    // Implement OrderedMap
    //-----------------------------------------------------------------------
    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        return new ListOrderedMapIterator<K, V>(this);
    }

    /**
     * Gets the first key in this map by insert order.
     *
     * @return the first key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    public K firstKey() {
        if (size() == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(0);
    }

    /**
     * Gets the last key in this map by insert order.
     *
     * @return the last key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    public K lastKey() {
        if (size() == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(size() - 1);
    }

    /**
     * Gets the next key to the one specified using insert order.
     * This method performs a list search to find the key and is O(n).
     *
     * @param key  the key to find previous for
     * @return the next key, null if no match or at start
     */
    public K nextKey(final Object key) {
        final int index = insertOrder.indexOf(key);
        if (index >= 0 && index < size() - 1) {
            return insertOrder.get(index + 1);
        }
        return null;
    }

    /**
     * Gets the previous key to the one specified using insert order.
     * This method performs a list search to find the key and is O(n).
     *
     * @param key  the key to find previous for
     * @return the previous key, null if no match or at start
     */
    public K previousKey(final Object key) {
        final int index = insertOrder.indexOf(key);
        if (index > 0) {
            return insertOrder.get(index - 1);
        }
        return null;
    }

    //-----------------------------------------------------------------------
    @Override
    public V put(final K key, final V value) {
        if (decorated().containsKey(key)) {
            // re-adding doesn't change order
            return decorated().put(key, value);
        } else {
            // first add, so add to both map and list
            final V result = decorated().put(key, value);
            insertOrder.add(key);
            return result;
        }
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Puts the values contained in a supplied Map into the Map starting at
     * the specified index.
     *
     * @param index the index in the Map to start at.
     * @param map the Map containing the entries to be added.
     */
    public void putAll(int index, final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            final K key = entry.getKey();
            final boolean contains = containsKey(key);
            // The return value of put is null if the key did not exist OR the value was null
            // so it cannot be used to determine whether the key was added
            put(index, entry.getKey(), entry.getValue());
            if (!contains) {
                // if no key was replaced, increment the index
                index++;
            } else {
                // otherwise put the next item after the currently inserted key
                index = indexOf(entry.getKey()) + 1;
            }
        }
    }

    @Override
    public V remove(final Object key) {
        V result = null;
        if (decorated().containsKey(key)) {
            result = decorated().remove(key);
            insertOrder.remove(key);
        }
        return result;
    }

    @Override
    public void clear() {
        decorated().clear();
        insertOrder.clear();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a view over the keys in the map.
     * <p>
     * The Collection will be ordered by object insertion into the map.
     *
     * @see #keyList()
     * @return the fully modifiable collection view over the keys
     */
    @Override
    public Set<K> keySet() {
        return new KeySetView<K>(this);
    }

    /**
     * Gets a view over the keys in the map as a List.
     * <p>
     * The List will be ordered by object insertion into the map.
     * The List is unmodifiable.
     *
     * @see #keySet()
     * @return the unmodifiable list view over the keys
     * @since 3.2
     */
    public List<K> keyList() {
        return UnmodifiableList.unmodifiableList(insertOrder);
    }

    /**
     * Gets a view over the values in the map.
     * <p>
     * The Collection will be ordered by object insertion into the map.
     * <p>
     * From Commons Collections 3.2, this Collection can be cast
     * to a list, see {@link #valueList()}
     *
     * @see #valueList()
     * @return the fully modifiable collection view over the values
     */
    @Override
    public Collection<V> values() {
        return new ValuesView<V>(this);
    }

    /**
     * Gets a view over the values in the map as a List.
     * <p>
     * The List will be ordered by object insertion into the map.
     * The List supports remove and set, but does not support add.
     *
     * @see #values()
     * @return the partially modifiable list view over the values
     * @since 3.2
     */
    public List<V> valueList() {
        return new ValuesView<V>(this);
    }

    /**
     * Gets a view over the entries in the map.
     * <p>
     * The Set will be ordered by object insertion into the map.
     *
     * @return the fully modifiable set view over the entries
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySetView<K, V>(this, this.insertOrder);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the Map as a string.
     *
     * @return the Map as a String
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        final StringBuilder buf = new StringBuilder();
        buf.append('{');
        boolean first = true;
        for (final Map.Entry<K, V> entry : entrySet()) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(key == this ? "(this Map)" : key);
            buf.append('=');
            buf.append(value == this ? "(this Map)" : value);
        }
        buf.append('}');
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the key at the specified index.
     *
     * @param index  the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public K get(final int index) {
        return insertOrder.get(index);
    }

    /**
     * Gets the value at the specified index.
     *
     * @param index  the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public V getValue(final int index) {
        return get(insertOrder.get(index));
    }

    /**
     * Gets the index of the specified key.
     *
     * @param key  the key to find the index of
     * @return the index, or -1 if not found
     */
    public int indexOf(final Object key) {
        return insertOrder.indexOf(key);
    }

    /**
     * Sets the value at the specified index.
     *
     * @param index  the index of the value to set
     * @param value  the new value to set
     * @return the previous value at that index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @since 3.2
     */
    public V setValue(final int index, final V value) {
        final K key = insertOrder.get(index);
        return put(key, value);
    }

    /**
     * Puts a key-value mapping into the map at the specified index.
     * <p>
     * If the map already contains the key, then the original mapping
     * is removed and the new mapping added at the specified index.
     * The remove may change the effect of the index. The index is
     * always calculated relative to the original state of the map.
     * <p>
     * Thus the steps are: (1) remove the existing key-value mapping,
     * then (2) insert the new key-value mapping at the position it
     * would have been inserted had the remove not occurred.
     *
     * @param index  the index at which the mapping should be inserted
     * @param key  the key
     * @param value  the value
     * @return the value previously mapped to the key
     * @throws IndexOutOfBoundsException if the index is out of range
     * @since 3.2
     */
    public V put(int index, final K key, final V value) {
        final Map<K, V> m = decorated();
        if (m.containsKey(key)) {
            final V result = m.remove(key);
            final int pos = insertOrder.indexOf(key);
            insertOrder.remove(pos);
            if (pos < index) {
                index--;
            }
            insertOrder.add(index, key);
            m.put(key, value);
            return result;
        } else {
            insertOrder.add(index, key);
            m.put(key, value);
            return null;
        }
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index  the index of the object to remove
     * @return the removed value, or <code>null</code> if none existed
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public V remove(final int index) {
        return remove(get(index));
    }

    /**
     * Gets an unmodifiable List view of the keys which changes as the map changes.
     * <p>
     * The returned list is unmodifiable because changes to the values of
     * the list (using {@link java.util.ListIterator#set(Object)}) will
     * effectively remove the value from the list and reinsert that value at
     * the end of the list, which is an unexpected side effect of changing the
     * value of a list.  This occurs because changing the key, changes when the
     * mapping is added to the map and thus where it appears in the list.
     * <p>
     * An alternative to this method is to use the better named
     * {@link #keyList()} or {@link #keySet()}.
     *
     * @see #keyList()
     * @see #keySet()
     * @return The ordered list of keys.
     */
    public List<K> asList() {
        return keyList();
    }

    //-----------------------------------------------------------------------
    static class ValuesView<V> extends AbstractList<V> {
        private final ListOrderedMap<Object, V> parent;

        @SuppressWarnings("unchecked")
        ValuesView(final ListOrderedMap<?, V> parent) {
            super();
            this.parent = (ListOrderedMap<Object, V>) parent;
        }

        @Override
        public int size() {
            return this.parent.size();
        }

        @Override
        public boolean contains(final Object value) {
            return this.parent.containsValue(value);
        }

        @Override
        public void clear() {
            this.parent.clear();
        }

        @Override
        public Iterator<V> iterator() {
            return new AbstractUntypedIteratorDecorator<Map.Entry<Object, V>, V>(parent.entrySet().iterator()) {
                public V next() {
                    return getIterator().next().getValue();
                }
            };
        }

        @Override
        public V get(final int index) {
            return this.parent.getValue(index);
        }

        @Override
        public V set(final int index, final V value) {
            return this.parent.setValue(index, value);
        }

        @Override
        public V remove(final int index) {
            return this.parent.remove(index);
        }
    }

    //-----------------------------------------------------------------------
    static class KeySetView<K> extends AbstractSet<K> {
        private final ListOrderedMap<K, Object> parent;

        @SuppressWarnings("unchecked")
        KeySetView(final ListOrderedMap<K, ?> parent) {
            super();
            this.parent = (ListOrderedMap<K, Object>) parent;
        }

        @Override
        public int size() {
            return this.parent.size();
        }

        @Override
        public boolean contains(final Object value) {
            return this.parent.containsKey(value);
        }

        @Override
        public void clear() {
            this.parent.clear();
        }

        @Override
        public Iterator<K> iterator() {
            return new AbstractUntypedIteratorDecorator<Map.Entry<K, Object>, K>(parent.entrySet().iterator()) {
                public K next() {
                    return getIterator().next().getKey();
                }
            };
        }
    }

    //-----------------------------------------------------------------------
    static class EntrySetView<K, V> extends AbstractSet<Map.Entry<K, V>> {
        private final ListOrderedMap<K, V> parent;
        private final List<K> insertOrder;
        private Set<Map.Entry<K, V>> entrySet;

        public EntrySetView(final ListOrderedMap<K, V> parent, final List<K> insertOrder) {
            super();
            this.parent = parent;
            this.insertOrder = insertOrder;
        }

        private Set<Map.Entry<K, V>> getEntrySet() {
            if (entrySet == null) {
                entrySet = parent.decorated().entrySet();
            }
            return entrySet;
        }

        @Override
        public int size() {
            return this.parent.size();
        }
        @Override
        public boolean isEmpty() {
            return this.parent.isEmpty();
        }

        @Override
        public boolean contains(final Object obj) {
            return getEntrySet().contains(obj);
        }

        @Override
        public boolean containsAll(final Collection<?> coll) {
            return getEntrySet().containsAll(coll);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(final Object obj) {
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            if (getEntrySet().contains(obj)) {
                final Object key = ((Map.Entry<K, V>) obj).getKey();
                parent.remove(key);
                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            this.parent.clear();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            return getEntrySet().equals(obj);
        }

        @Override
        public int hashCode() {
            return getEntrySet().hashCode();
        }

        @Override
        public String toString() {
            return getEntrySet().toString();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new ListOrderedIterator<K, V>(parent, insertOrder);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedIterator<K, V> extends AbstractUntypedIteratorDecorator<K, Map.Entry<K, V>> {
        private final ListOrderedMap<K, V> parent;
        private K last = null;

        ListOrderedIterator(final ListOrderedMap<K, V> parent, final List<K> insertOrder) {
            super(insertOrder.iterator());
            this.parent = parent;
        }

        public Map.Entry<K, V> next() {
            last = getIterator().next();
            return new ListOrderedMapEntry<K, V>(parent, last);
        }

        @Override
        public void remove() {
            super.remove();
            parent.decorated().remove(last);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedMapEntry<K, V> extends AbstractMapEntry<K, V> {
        private final ListOrderedMap<K, V> parent;

        ListOrderedMapEntry(final ListOrderedMap<K, V> parent, final K key) {
            super(key, null);
            this.parent = parent;
        }

        @Override
        public V getValue() {
            return parent.get(getKey());
        }

        @Override
        public V setValue(final V value) {
            return parent.decorated().put(getKey(), value);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {
        private final ListOrderedMap<K, V> parent;
        private ListIterator<K> iterator;
        private K last = null;
        private boolean readable = false;

        ListOrderedMapIterator(final ListOrderedMap<K, V> parent) {
            super();
            this.parent = parent;
            this.iterator = parent.insertOrder.listIterator();
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public K next() {
            last = iterator.next();
            readable = true;
            return last;
        }

        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        public K previous() {
            last = iterator.previous();
            readable = true;
            return last;
        }

        public void remove() {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            iterator.remove();
            parent.map.remove(last);
            readable = false;
        }

        public K getKey() {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            return last;
        }

        public V getValue() {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            return parent.get(last);
        }

        public V setValue(final V value) {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            return parent.map.put(last, value);
        }

        public void reset() {
            iterator = parent.insertOrder.listIterator();
            last = null;
            readable = false;
        }

        @Override
        public String toString() {
            if (readable == true) {
                return "Iterator[" + getKey() + "=" + getValue() + "]";
            }
            return "Iterator[]";
        }
    }

}

```
