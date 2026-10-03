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
    <version>37</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-collections4</artifactId>
  <version>4.1-SNAPSHOT</version>
  <name>Apache Commons Collections</name>

  <inceptionYear>2001</inceptionYear>
  <description>The Apache Commons Collections package contains types that extend and augment the Java Collections Framework.</description>

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
      <name>Dipanjan Laha</name>
      <id>dlaha</id>
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
      <name>Thomas Neidhart</name>
      <id>tn</id>
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
      <name>Josh Cain</name>
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
      <name>Radford Tam</name>
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
      <name>Hollis Waite</name>
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
    <contributor>
      <name>Geoff Schoeman</name>
    </contributor>
    <contributor>
      <name>Goncalo Marques</name>
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
      <version>3.2</version>
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
    <maven.compiler.source>1.6</maven.compiler.source>
    <maven.compiler.target>1.6</maven.compiler.target>

    <!--
       This is also  used to generate download_xxx file name.
       To override this when generating the download page:

       mvn commons:download-page -Dcommons.componentid=collections

       The above seems to change the download page name but not any other
       properties that depend on the componentid.
    -->
    <commons.componentid>collections4</commons.componentid>

    <!-- Current 4.x release series -->
    <commons.release.version>4.0</commons.release.version>
    <commons.release.desc>(Java 5.0+)</commons.release.desc>

    <!-- Previous 3.x release series -->
    <commons.release.2.version>3.2.1</commons.release.2.version>
    <commons.release.2.desc>(Requires Java 1.2 or later)</commons.release.2.desc>
    <!-- Override generated name -->
    <commons.release.2.name>commons-collections-${commons.release.2.version}</commons.release.2.name>

    <commons.jira.id>COLLECTIONS</commons.jira.id>
    <commons.jira.pid>12310465</commons.jira.pid>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC5</commons.rc.version>
    <checkstyle.version>2.9.1</checkstyle.version>

    <commons.site.path>collections</commons.site.path>
    <commons.scmPubUrl>https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-collections</commons.scmPubUrl>
    <commons.scmPubCheckoutDirectory>site-content</commons.scmPubCheckoutDirectory>

    <!-- Override javadoc plugin version due to a vulnerability of the generated javadoc
         see http://jira.codehaus.org/browse/MJAVADOC-370, remove after update to CP 32   -->
    <commons.javadoc.version>2.9.1</commons.javadoc.version>
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
        <version>2.5.5</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/src/conf/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.7.1</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>
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
            <exclude>src/test/resources/data/test/*</exclude>
            <exclude>maven-eclipse.xml</exclude>
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

## src/main/java/org/apache/commons/collections4/IteratorUtils.java

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
package org.apache.commons.collections4;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.commons.collections4.iterators.ArrayListIterator;
import org.apache.commons.collections4.iterators.BoundedIterator;
import org.apache.commons.collections4.iterators.CollatingIterator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.EmptyListIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.FilterIterator;
import org.apache.commons.collections4.iterators.FilterListIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.collections4.iterators.IteratorIterable;
import org.apache.commons.collections4.iterators.ListIteratorWrapper;
import org.apache.commons.collections4.iterators.LoopingIterator;
import org.apache.commons.collections4.iterators.LoopingListIterator;
import org.apache.commons.collections4.iterators.NodeListIterator;
import org.apache.commons.collections4.iterators.ObjectArrayIterator;
import org.apache.commons.collections4.iterators.ObjectArrayListIterator;
import org.apache.commons.collections4.iterators.ObjectGraphIterator;
import org.apache.commons.collections4.iterators.PeekingIterator;
import org.apache.commons.collections4.iterators.PushbackIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.apache.commons.collections4.iterators.SingletonListIterator;
import org.apache.commons.collections4.iterators.SkippingIterator;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.iterators.ZippingIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides static utility methods and decorators for {@link Iterator}
 * instances. The implementations are provided in the iterators subpackage.
 * <p>
 * WARNING: Due to human error certain binary incompatibilities were introduced
 * between Commons Collections 2.1 and 3.0. The class remained source and test
 * compatible, so if you can recompile all your classes and dependencies
 * everything is OK. Those methods which are binary incompatible are marked as
 * such, together with alternate solutions that are binary compatible
 * against versions 2.1.1 and 3.1.
 *
 * @since 2.1
 * @version $Id$
 */
public class IteratorUtils {
    // validation is done in this class in certain cases because the
    // public classes allow invalid states

    /**
     * An iterator over no elements.
     * <p>
     * WARNING: This constant is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>EmptyIterator.INSTANCE</code> for compatibility with Commons Collections 2.1.1.
     */
    @SuppressWarnings("rawtypes")
    public static final ResettableIterator EMPTY_ITERATOR = EmptyIterator.RESETTABLE_INSTANCE;

    /**
     * A list iterator over no elements.
     * <p>
     * WARNING: This constant is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>EmptyListIterator.INSTANCE</code> for compatibility with Commons Collections 2.1.1.
     */
    @SuppressWarnings("rawtypes")
    public static final ResettableListIterator EMPTY_LIST_ITERATOR = EmptyListIterator.RESETTABLE_INSTANCE;

    /**
     * An ordered iterator over no elements.
     */
    @SuppressWarnings("rawtypes")
    public static final OrderedIterator EMPTY_ORDERED_ITERATOR = EmptyOrderedIterator.INSTANCE;

    /**
     * A map iterator over no elements.
     */
    @SuppressWarnings("rawtypes")
    public static final MapIterator EMPTY_MAP_ITERATOR = EmptyMapIterator.INSTANCE;

    /**
     * An ordered map iterator over no elements.
     */
    @SuppressWarnings("rawtypes")
    public static final OrderedMapIterator EMPTY_ORDERED_MAP_ITERATOR = EmptyOrderedMapIterator.INSTANCE;

    /**
     * Default prefix used while converting an Iterator to its String representation.
     */
    private static final String DEFAULT_TOSTRING_PREFIX = "[";

    /**
     * Default suffix used while converting an Iterator to its String representation.
     */
    private static final String DEFAULT_TOSTRING_SUFFIX = "]";

    /**
     * Default delimiter used to delimit elements while converting an Iterator
     * to its String representation.
     */
    private static final String DEFAULT_TOSTRING_DELIMITER = ", ";

    /**
     * IteratorUtils is not normally instantiated.
     */
    private IteratorUtils() {}

    // Empty
    //-----------------------------------------------------------------------
    /**
     * Gets an empty iterator.
     * <p>
     * This iterator is a valid iterator object that will iterate over
     * nothing.
     * <p>
     * WARNING: This method is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>EmptyIterator.INSTANCE</code> for compatibility with Commons Collections 2.1.1.
     *
     * @param <E>  the element type
     * @return  an iterator over nothing
     */
    public static <E> ResettableIterator<E> emptyIterator() {
        return EmptyIterator.<E>resettableEmptyIterator();
    }

    /**
     * Gets an empty list iterator.
     * <p>
     * This iterator is a valid list iterator object that will iterate
     * over nothing.
     * <p>
     * WARNING: This method is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>EmptyListIterator.INSTANCE</code> for compatibility with Commons Collections 2.1.1.
     *
     * @param <E>  the element type
     * @return  a list iterator over nothing
     */
    public static <E> ResettableListIterator<E> emptyListIterator() {
        return EmptyListIterator.<E>resettableEmptyListIterator();
    }

    /**
     * Gets an empty ordered iterator.
     * <p>
     * This iterator is a valid iterator object that will iterate
     * over nothing.
     *
     * @param <E>  the element type
     * @return  an ordered iterator over nothing
     */
    public static <E> OrderedIterator<E> emptyOrderedIterator() {
        return EmptyOrderedIterator.<E>emptyOrderedIterator();
    }

    /**
     * Gets an empty map iterator.
     * <p>
     * This iterator is a valid map iterator object that will iterate
     * over nothing.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @return  a map iterator over nothing
     */
    public static <K, V> MapIterator<K, V> emptyMapIterator() {
        return EmptyMapIterator.<K, V>emptyMapIterator();
    }

    /**
     * Gets an empty ordered map iterator.
     * <p>
     * This iterator is a valid map iterator object that will iterate
     * over nothing.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @return  a map iterator over nothing
     */
    public static <K, V> OrderedMapIterator<K, V> emptyOrderedMapIterator() {
        return EmptyOrderedMapIterator.<K, V>emptyOrderedMapIterator();
    }

    // Singleton
    //-----------------------------------------------------------------------
    /**
     * Gets a singleton iterator.
     * <p>
     * This iterator is a valid iterator object that will iterate over
     * the specified object.
     * <p>
     * WARNING: This method is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>new SingletonIterator(object)</code> for compatibility.
     *
     * @param <E>  the element type
     * @param object  the single object over which to iterate
     * @return  a singleton iterator over the object
     */
    public static <E> ResettableIterator<E> singletonIterator(final E object) {
        return new SingletonIterator<E>(object);
    }

    /**
     * Gets a singleton list iterator.
     * <p>
     * This iterator is a valid list iterator object that will iterate over
     * the specified object.
     *
     * @param <E>  the element type
     * @param object  the single object over which to iterate
     * @return  a singleton list iterator over the object
     */
    public static <E> ListIterator<E> singletonListIterator(final E object) {
        return new SingletonListIterator<E>(object);
    }

    // Arrays
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator over an object array.
     * <p>
     * WARNING: This method is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>new ArrayIterator(array)</code> for compatibility.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @return  an iterator over the array
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableIterator<E> arrayIterator(final E... array) {
        return new ObjectArrayIterator<E>(array);
    }

    /**
     * Gets an iterator over an object or primitive array.
     * <p>
     * This method will handle primitive arrays as well as object arrays.
     * The primitives will be wrapped in the appropriate wrapper class.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @return  an iterator over the array
     * @throws IllegalArgumentException if the array is not an array
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableIterator<E> arrayIterator(final Object array) {
        return new ArrayIterator<E>(array);
    }

    /**
     * Gets an iterator over the end part of an object array.
     * <p>
     * WARNING: This method is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>new ArrayIterator(array,start)</code> for compatibility.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @return an iterator over part of the array
     * @throws IndexOutOfBoundsException if start is less than zero or greater
     *   than the length of the array
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableIterator<E> arrayIterator(final E[] array, final int start) {
        return new ObjectArrayIterator<E>(array, start);
    }

    /**
     * Gets an iterator over the end part of an object or primitive array.
     * <p>
     * This method will handle primitive arrays as well as object arrays.
     * The primitives will be wrapped in the appropriate wrapper class.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @return an iterator over part of the array
     * @throws IllegalArgumentException if the array is not an array
     * @throws IndexOutOfBoundsException if start is less than zero or greater
     *   than the length of the array
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableIterator<E> arrayIterator(final Object array, final int start) {
        return new ArrayIterator<E>(array, start);
    }

    /**
     * Gets an iterator over part of an object array.
     * <p>
     * WARNING: This method is binary incompatible with Commons Collections 2.1 and 2.1.1.
     * Use <code>new ArrayIterator(array,start,end)</code> for compatibility.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @param end  the index to finish iterating at
     * @return an iterator over part of the array
     * @throws IndexOutOfBoundsException if array bounds are invalid
     * @throws IllegalArgumentException if end is before start
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableIterator<E> arrayIterator(final E[] array, final int start, final int end) {
        return new ObjectArrayIterator<E>(array, start, end);
    }

    /**
     * Gets an iterator over part of an object or primitive array.
     * <p>
     * This method will handle primitive arrays as well as object arrays.
     * The primitives will be wrapped in the appropriate wrapper class.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @param end  the index to finish iterating at
     * @return an iterator over part of the array
     * @throws IllegalArgumentException if the array is not an array or end is before start
     * @throws IndexOutOfBoundsException if array bounds are invalid
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableIterator<E> arrayIterator(final Object array, final int start, final int end) {
        return new ArrayIterator<E>(array, start, end);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a list iterator over an object array.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @return  a list iterator over the array
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableListIterator<E> arrayListIterator(final E... array) {
        return new ObjectArrayListIterator<E>(array);
    }

    /**
     * Gets a list iterator over an object or primitive array.
     * <p>
     * This method will handle primitive arrays as well as object arrays.
     * The primitives will be wrapped in the appropriate wrapper class.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @return  a list iterator over the array
     * @throws IllegalArgumentException if the array is not an array
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableListIterator<E> arrayListIterator(final Object array) {
        return new ArrayListIterator<E>(array);
    }

    /**
     * Gets a list iterator over the end part of an object array.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @return a list iterator over part of the array
     * @throws IndexOutOfBoundsException if start is less than zero
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableListIterator<E> arrayListIterator(final E[] array, final int start) {
        return new ObjectArrayListIterator<E>(array, start);
    }

    /**
     * Gets a list iterator over the end part of an object or primitive array.
     * <p>
     * This method will handle primitive arrays as well as object arrays.
     * The primitives will be wrapped in the appropriate wrapper class.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @return a list iterator over part of the array
     * @throws IllegalArgumentException if the array is not an array
     * @throws IndexOutOfBoundsException if start is less than zero
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableListIterator<E> arrayListIterator(final Object array, final int start) {
        return new ArrayListIterator<E>(array, start);
    }

    /**
     * Gets a list iterator over part of an object array.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @param end  the index to finish iterating at
     * @return a list iterator over part of the array
     * @throws IndexOutOfBoundsException if array bounds are invalid
     * @throws IllegalArgumentException if end is before start
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableListIterator<E> arrayListIterator(final E[] array, final int start, final int end) {
        return new ObjectArrayListIterator<E>(array, start, end);
    }

    /**
     * Gets a list iterator over part of an object or primitive array.
     * <p>
     * This method will handle primitive arrays as well as object arrays.
     * The primitives will be wrapped in the appropriate wrapper class.
     *
     * @param <E>  the element type
     * @param array  the array over which to iterate
     * @param start  the index to start iterating at
     * @param end  the index to finish iterating at
     * @return a list iterator over part of the array
     * @throws IllegalArgumentException if the array is not an array or end is before start
     * @throws IndexOutOfBoundsException if array bounds are invalid
     * @throws NullPointerException if array is null
     */
    public static <E> ResettableListIterator<E> arrayListIterator(final Object array, final int start, final int end) {
        return new ArrayListIterator<E>(array, start, end);
    }

    // Bounded
    //-----------------------------------------------------------------------
    /**
     * Decorates the specified iterator to return at most the given number
     * of elements.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to decorate
     * @param max  the maximum number of elements returned by this iterator
     * @return a new bounded iterator
     * @throws IllegalArgumentException if the iterator is null or max is negative
     * @since 4.1
     */
    public static <E> BoundedIterator<E> boundedIterator(final Iterator<? extends E> iterator, long max) {
        return boundedIterator(iterator, 0, max);
    }

    /**
     * Decorates the specified iterator to return at most the given number
     * of elements, skipping all elements until the iterator reaches the
     * position at {@code offset}.
     * <p>
     * The iterator is immediately advanced until it reaches the position at
     * {@code offset}, incurring O(n) time.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to decorate
     * @param offset  the index of the first element of the decorated iterator to return
     * @param max  the maximum number of elements returned by this iterator
     * @return a new bounded iterator
     * @throws IllegalArgumentException if the iterator is null or either offset or max is negative
     * @since 4.1
     */
    public static <E> BoundedIterator<E> boundedIterator(final Iterator<? extends E> iterator,
                                                         long offset, long max) {
        return new BoundedIterator<E>(iterator, offset, max);
    }

    // Unmodifiable
    //-----------------------------------------------------------------------
    /**
     * Gets an immutable version of an {@link Iterator}. The returned object
     * will always throw an {@link UnsupportedOperationException} for
     * the {@link Iterator#remove} method.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to make immutable
     * @return an immutable version of the iterator
     */
    public static <E> Iterator<E> unmodifiableIterator(final Iterator<E> iterator) {
        return UnmodifiableIterator.unmodifiableIterator(iterator);
    }

    /**
     * Gets an immutable version of a {@link ListIterator}. The returned object
     * will always throw an {@link UnsupportedOperationException} for
     * the {@link Iterator#remove}, {@link ListIterator#add} and
     * {@link ListIterator#set} methods.
     *
     * @param <E>  the element type
     * @param listIterator  the iterator to make immutable
     * @return an immutable version of the iterator
     */
    public static <E> ListIterator<E> unmodifiableListIterator(final ListIterator<E> listIterator) {
        return UnmodifiableListIterator.umodifiableListIterator(listIterator);
    }

    /**
     * Gets an immutable version of a {@link MapIterator}. The returned object
     * will always throw an {@link UnsupportedOperationException} for
     * the {@link Iterator#remove}, {@link MapIterator#setValue(Object)} methods.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @param mapIterator  the iterator to make immutable
     * @return an immutable version of the iterator
     */
    public static <K, V> MapIterator<K, V> unmodifiableMapIterator(final MapIterator<K, V> mapIterator) {
        return UnmodifiableMapIterator.unmodifiableMapIterator(mapIterator);
    }

    // Chained
    //-----------------------------------------------------------------------

    /**
     * Gets an iterator that iterates through two {@link Iterator}s
     * one after another.
     *
     * @param <E>  the element type
     * @param iterator1  the first iterator to use, not null
     * @param iterator2  the second iterator to use, not null
     * @return a combination iterator over the iterators
     * @throws NullPointerException if either iterator is null
     */
    public static <E> Iterator<E> chainedIterator(final Iterator<? extends E> iterator1,
                                                  final Iterator<? extends E> iterator2) {
        // keep a version with two iterators to avoid the following warning in client code (Java 5 & 6)
        // "A generic array of E is created for a varargs parameter"
        return new IteratorChain<E>(iterator1, iterator2);
    }

    /**
     * Gets an iterator that iterates through an array of {@link Iterator}s
     * one after another.
     *
     * @param <E>  the element type
     * @param iterators  the iterators to use, not null or empty or contain nulls
     * @return a combination iterator over the iterators
     * @throws NullPointerException if iterators array is null or contains a null
     */
    public static <E> Iterator<E> chainedIterator(final Iterator<? extends E>... iterators) {
        return new IteratorChain<E>(iterators);
    }

    /**
     * Gets an iterator that iterates through a collections of {@link Iterator}s
     * one after another.
     *
     * @param <E>  the element type
     * @param iterators  the iterators to use, not null or empty or contain nulls
     * @return a combination iterator over the iterators
     * @throws NullPointerException if iterators collection is null or contains a null
     * @throws ClassCastException if the iterators collection contains the wrong object type
     */
    public static <E> Iterator<E> chainedIterator(final Collection<Iterator<? extends E>> iterators) {
        return new IteratorChain<E>(iterators);
    }

    // Collated
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that provides an ordered iteration over the elements
     * contained in a collection of ordered {@link Iterator}s.
     * <p>
     * Given two ordered {@link Iterator}s <code>A</code> and <code>B</code>,
     * the {@link Iterator#next()} method will return the lesser of
     * <code>A.next()</code> and <code>B.next()</code>.
     * <p>
     * The comparator is optional. If null is specified then natural order is used.
     *
     * @param <E>  the element type
     * @param comparator  the comparator to use, may be null for natural order
     * @param iterator1  the first iterators to use, not null
     * @param iterator2  the first iterators to use, not null
     * @return a combination iterator over the iterators
     * @throws NullPointerException if either iterator is null
     */
    public static <E> Iterator<E> collatedIterator(final Comparator<? super E> comparator,
                                                   final Iterator<? extends E> iterator1,
                                                   final Iterator<? extends E> iterator2) {
        @SuppressWarnings("unchecked")
        final Comparator<E> comp = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : comparator;
        return new CollatingIterator<E>(comp, iterator1, iterator2);
    }

    /**
     * Gets an iterator that provides an ordered iteration over the elements
     * contained in an array of {@link Iterator}s.
     * <p>
     * Given two ordered {@link Iterator}s <code>A</code> and <code>B</code>,
     * the {@link Iterator#next()} method will return the lesser of
     * <code>A.next()</code> and <code>B.next()</code> and so on.
     * <p>
     * The comparator is optional. If null is specified then natural order is used.
     *
     * @param <E>  the element type
     * @param comparator  the comparator to use, may be null for natural order
     * @param iterators  the iterators to use, not null or empty or contain nulls
     * @return a combination iterator over the iterators
     * @throws NullPointerException if iterators array is null or contains a null value
     */
    public static <E> Iterator<E> collatedIterator(final Comparator<? super E> comparator,
                                                   final Iterator<? extends E>... iterators) {
        @SuppressWarnings("unchecked")
        final Comparator<E> comp = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : comparator;
        return new CollatingIterator<E>(comp, iterators);
    }

    /**
     * Gets an iterator that provides an ordered iteration over the elements
     * contained in a collection of {@link Iterator}s.
     * <p>
     * Given two ordered {@link Iterator}s <code>A</code> and <code>B</code>,
     * the {@link Iterator#next()} method will return the lesser of
     * <code>A.next()</code> and <code>B.next()</code> and so on.
     * <p>
     * The comparator is optional. If null is specified then natural order is used.
     *
     * @param <E>  the element type
     * @param comparator  the comparator to use, may be null for natural order
     * @param iterators  the iterators to use, not null or empty or contain nulls
     * @return a combination iterator over the iterators
     * @throws NullPointerException if iterators collection is null or contains a null
     * @throws ClassCastException if the iterators collection contains the wrong object type
     */
    public static <E> Iterator<E> collatedIterator(final Comparator<? super E> comparator,
                                                   final Collection<Iterator<? extends E>> iterators) {
        @SuppressWarnings("unchecked")
        final Comparator<E> comp = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : comparator;
        return new CollatingIterator<E>(comp, iterators);
    }

    // Object Graph
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that operates over an object graph.
     * <p>
     * This iterator can extract multiple objects from a complex tree-like object graph.
     * The iteration starts from a single root object.
     * It uses a <code>Transformer</code> to extract the iterators and elements.
     * Its main benefit is that no intermediate <code>List</code> is created.
     * <p>
     * For example, consider an object graph:
     * <pre>
     *                 |- Branch -- Leaf
     *                 |         \- Leaf
     *         |- Tree |         /- Leaf
     *         |       |- Branch -- Leaf
     *  Forest |                 \- Leaf
     *         |       |- Branch -- Leaf
     *         |       |         \- Leaf
     *         |- Tree |         /- Leaf
     *                 |- Branch -- Leaf
     *                 |- Branch -- Leaf</pre>
     * The following <code>Transformer</code>, used in this class, will extract all
     * the Leaf objects without creating a combined intermediate list:
     * <pre>
     * public Object transform(Object input) {
     *   if (input instanceof Forest) {
     *     return ((Forest) input).treeIterator();
     *   }
     *   if (input instanceof Tree) {
     *     return ((Tree) input).branchIterator();
     *   }
     *   if (input instanceof Branch) {
     *     return ((Branch) input).leafIterator();
     *   }
     *   if (input instanceof Leaf) {
     *     return input;
     *   }
     *   throw new ClassCastException();
     * }</pre>
     * <p>
     * Internally, iteration starts from the root object. When next is called,
     * the transformer is called to examine the object. The transformer will return
     * either an iterator or an object. If the object is an Iterator, the next element
     * from that iterator is obtained and the process repeats. If the element is an object
     * it is returned.
     * <p>
     * Under many circumstances, linking Iterators together in this manner is
     * more efficient (and convenient) than using nested for loops to extract a list.
     *
     * @param <E>  the element type
     * @param root  the root object to start iterating from, null results in an empty iterator
     * @param transformer  the transformer to use, see above, null uses no effect transformer
     * @return a new object graph iterator
     * @since 3.1
     */
    public static <E> Iterator<E> objectGraphIterator(final E root,
            final Transformer<? super E, ? extends E> transformer) {
        return new ObjectGraphIterator<E>(root, transformer);
    }

    // Transformed
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that transforms the elements of another iterator.
     * <p>
     * The transformation occurs during the next() method and the underlying
     * iterator is unaffected by the transformation.
     *
     * @param <I>  the input type
     * @param <O>  the output type
     * @param iterator  the iterator to use, not null
     * @param transform  the transform to use, not null
     * @return a new transforming iterator
     * @throws NullPointerException if either parameter is null
     */
    public static <I, O> Iterator<O> transformedIterator(final Iterator<? extends I> iterator,
            final Transformer<? super I, ? extends O> transform) {

        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        if (transform == null) {
            throw new NullPointerException("Transformer must not be null");
        }
        return new TransformIterator<I, O>(iterator, transform);
    }

    // Filtered
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that filters another iterator.
     * <p>
     * The returned iterator will only return objects that match the specified
     * filtering predicate.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @param predicate  the predicate to use as a filter, not null
     * @return a new filtered iterator
     * @throws NullPointerException if either parameter is null
     */
    public static <E> Iterator<E> filteredIterator(final Iterator<? extends E> iterator,
                                                   final Predicate<? super E> predicate) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        if (predicate == null) {
            throw new NullPointerException("Predicate must not be null");
        }
        return new FilterIterator<E>(iterator, predicate);
    }

    /**
     * Gets a list iterator that filters another list iterator.
     * <p>
     * The returned iterator will only return objects that match the specified
     * filtering predicate.
     *
     * @param <E>  the element type
     * @param listIterator  the list iterator to use, not null
     * @param predicate  the predicate to use as a filter, not null
     * @return a new filtered iterator
     * @throws NullPointerException if either parameter is null
     */
    public static <E> ListIterator<E> filteredListIterator(final ListIterator<? extends E> listIterator,
            final Predicate<? super E> predicate) {

        if (listIterator == null) {
            throw new NullPointerException("ListIterator must not be null");
        }
        if (predicate == null) {
            throw new NullPointerException("Predicate must not be null");
        }
        return new FilterListIterator<E>(listIterator, predicate);
    }

    // Looping
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that loops continuously over the supplied collection.
     * <p>
     * The iterator will only stop looping if the remove method is called
     * enough times to empty the collection, or if the collection is empty
     * to start with.
     *
     * @param <E>  the element type
     * @param coll  the collection to iterate over, not null
     * @return a new looping iterator
     * @throws NullPointerException if the collection is null
     */
    public static <E> ResettableIterator<E> loopingIterator(final Collection<? extends E> coll) {
        if (coll == null) {
            throw new NullPointerException("Collection must not be null");
        }
        return new LoopingIterator<E>(coll);
    }

    /**
     * Gets an iterator that loops continuously over the supplied list.
     * <p>
     * The iterator will only stop looping if the remove method is called
     * enough times to empty the list, or if the list is empty to start with.
     *
     * @param <E>  the element type
     * @param list  the list to iterate over, not null
     * @return a new looping iterator
     * @throws NullPointerException if the list is null
     * @since 3.2
     */
    public static <E> ResettableListIterator<E> loopingListIterator(final List<E> list) {
        if (list == null) {
            throw new NullPointerException("List must not be null");
        }
        return new LoopingListIterator<E>(list);
    }

    // org.w3c.dom.NodeList iterators
    //-----------------------------------------------------------------------
    /**
     * Gets an {@link Iterator} that wraps the specified {@link NodeList}.
     * The returned {@link Iterator} can be used for a single iteration.
     *
     * @param nodeList the node list to use, not null
     * @return a new, single use {@link Iterator}
     * @throws NullPointerException if nodeList is null
     * @since 4.0
     */
    public static NodeListIterator nodeListIterator(final NodeList nodeList) {
        if (nodeList == null) {
            throw new NullPointerException("NodeList must not be null");
        }
        return new NodeListIterator(nodeList);
    }

    /**
     * Gets an {@link Iterator} that wraps the specified node's childNodes.
     * The returned {@link Iterator} can be used for a single iteration.
     * <p>
     * Convenience method, allows easy iteration over NodeLists:
     * <pre>
     *   Iterator&lt;Node&gt; iterator = IteratorUtils.nodeListIterator(node);
     *   for(Node childNode : IteratorUtils.asIterable(iterator)) {
     *     ...
     *   }
     * </pre>
     *
     * @param node the node to use, not null
     * @return a new, single use {@link Iterator}
     * @throws NullPointerException if node is null
     * @since 4.0
     */
    public static NodeListIterator nodeListIterator(final Node node) {
        if (node == null) {
            throw new NullPointerException("Node must not be null");
        }
        return new NodeListIterator(node);
    }

    // Peeking
    //-----------------------------------------------------------------------

    /**
     * Gets an iterator that supports one-element lookahead.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to decorate, not null
     * @return a peeking iterator
     * @throws NullPointerException if the iterator is null
     * @since 4.0
     */
    public static <E> Iterator<E> peekingIterator(final Iterator<? extends E> iterator) {
        return PeekingIterator.peekingIterator(iterator);
    }

    // Pushback
    //-----------------------------------------------------------------------

    /**
     * Gets an iterator that supports pushback of elements.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to decorate, not null
     * @return a pushback iterator
     * @throws NullPointerException if the iterator is null
     * @since 4.0
     */
    public static <E> Iterator<E> pushbackIterator(final Iterator<? extends E> iterator) {
        return PushbackIterator.pushbackIterator(iterator);
    }

    // Skipping
    //-----------------------------------------------------------------------
    /**
     * Decorates the specified iterator to skip the first N elements.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to decorate
     * @param offset  the first number of elements to skip
     * @return a new skipping iterator
     * @throws IllegalArgumentException if the iterator is null or offset is negative
     * @since 4.1
     */
    public static <E> SkippingIterator<E> skippingIterator(final Iterator<E> iterator, long offset) {
        return new SkippingIterator<E>(iterator, offset);
    }

    // Zipping
    //-----------------------------------------------------------------------
    /**
     * Returns an iterator that interleaves elements from the decorated iterators.
     *
     * @param <E>  the element type
     * @param a  the first iterator to interleave
     * @param b  the second iterator to interleave
     * @return an iterator, interleaving the decorated iterators
     * @throws IllegalArgumentException if any iterator is null
     * @since 4.1
     */
    public static <E> ZippingIterator<E> zippingIterator(final Iterator<? extends E> a,
                                                         final Iterator<? extends E> b) {
        return new ZippingIterator<E>(a, b);
    }

    /**
     * Returns an iterator that interleaves elements from the decorated iterators.
     *
     * @param <E>  the element type
     * @param a  the first iterator to interleave
     * @param b  the second iterator to interleave
     * @param c  the third iterator to interleave
     * @return an iterator, interleaving the decorated iterators
     * @throws IllegalArgumentException if any iterator is null
     * @since 4.1
     */
    public static <E> ZippingIterator<E> zippingIterator(final Iterator<? extends E> a,
                                                         final Iterator<? extends E> b,
                                                         final Iterator<? extends E> c) {
        return new ZippingIterator<E>(a, b, c);
    }

    /**
     * Returns an iterator that interleaves elements from the decorated iterators.
     *
     * @param <E>  the element type
     * @param iterators  the array of iterators to interleave
     * @return an iterator, interleaving the decorated iterators
     * @throws IllegalArgumentException if any iterator is null
     * @since 4.1
     */
    public static <E> ZippingIterator<E> zippingIterator(final Iterator<? extends E>... iterators) {
        return new ZippingIterator<E>(iterators);
    }

    // Views
    //-----------------------------------------------------------------------
    /**
     * Gets an iterator that provides an iterator view of the given enumeration.
     *
     * @param <E>  the element type
     * @param enumeration  the enumeration to use
     * @return a new iterator
     */
    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration) {
        if (enumeration == null) {
            throw new NullPointerException("Enumeration must not be null");
        }
        return new EnumerationIterator<E>(enumeration);
    }

    /**
     * Gets an iterator that provides an iterator view of the given enumeration
     * that will remove elements from the specified collection.
     *
     * @param <E>  the element type
     * @param enumeration  the enumeration to use
     * @param removeCollection  the collection to remove elements from
     * @return a new iterator
     */
    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration,
                                             final Collection<? super E> removeCollection) {
        if (enumeration == null) {
            throw new NullPointerException("Enumeration must not be null");
        }
        if (removeCollection == null) {
            throw new NullPointerException("Collection must not be null");
        }
        return new EnumerationIterator<E>(enumeration, removeCollection);
    }

    /**
     * Gets an enumeration that wraps an iterator.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @return a new enumeration
     * @throws NullPointerException if iterator is null
     */
    public static <E> Enumeration<E> asEnumeration(final Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        return new IteratorEnumeration<E>(iterator);
    }

    /**
     * Gets an {@link Iterable} that wraps an iterator.  The returned {@link Iterable} can be
     * used for a single iteration.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @return a new, single use {@link Iterable}
     * @throws NullPointerException if iterator is null
     */
    public static <E> Iterable<E> asIterable(final Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        return new IteratorIterable<E>(iterator, false);
    }

    /**
     * Gets an iterable that wraps an iterator.  The returned iterable can be
     * used for multiple iterations.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @return a new, multiple use iterable
     * @throws NullPointerException if iterator is null
     */
    public static <E> Iterable<E> asMultipleUseIterable(final Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        return new IteratorIterable<E>(iterator, true);
    }

    /**
     * Gets a list iterator based on a simple iterator.
     * <p>
     * As the wrapped Iterator is traversed, a LinkedList of its values is
     * cached, permitting all required operations of ListIterator.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @return a new iterator
     * @throws NullPointerException if iterator parameter is null
     */
    public static <E> ListIterator<E> toListIterator(final Iterator<? extends E> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        return new ListIteratorWrapper<E>(iterator);
    }

    /**
     * Gets an array based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, this is converted to an array.
     *
     * @param iterator  the iterator to use, not null
     * @return an array of the iterator contents
     * @throws NullPointerException if iterator parameter is null
     */
    public static Object[] toArray(final Iterator<?> iterator) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        final List<?> list = toList(iterator, 100);
        return list.toArray();
    }

    /**
     * Gets an array based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, this is converted to an array.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @param arrayClass  the class of array to create
     * @return an array of the iterator contents
     * @throws NullPointerException if iterator parameter or arrayClass is null
     * @throws ClassCastException if the arrayClass is invalid
     */
    public static <E> E[] toArray(final Iterator<? extends E> iterator, final Class<E> arrayClass) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        if (arrayClass == null) {
            throw new NullPointerException("Array class must not be null");
        }
        final List<E> list = toList(iterator, 100);
        @SuppressWarnings("unchecked") // as per Javadoc, will throw CCE if class is wrong
        final E[] array = (E[]) Array.newInstance(arrayClass, list.size());
        return list.toArray(array);
    }

    /**
     * Gets a list based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, the list is returned.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @return a list of the iterator contents
     * @throws NullPointerException if iterator parameter is null
     */
    public static <E> List<E> toList(final Iterator<? extends E> iterator) {
        return toList(iterator, 10);
    }

    /**
     * Gets a list based on an iterator.
     * <p>
     * As the wrapped Iterator is traversed, an ArrayList of its values is
     * created. At the end, the list is returned.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, not null
     * @param estimatedSize  the initial size of the ArrayList
     * @return a list of the iterator contents
     * @throws NullPointerException if iterator parameter is null
     * @throws IllegalArgumentException if the size is less than 1
     */
    public static <E> List<E> toList(final Iterator<? extends E> iterator, final int estimatedSize) {
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        if (estimatedSize < 1) {
            throw new IllegalArgumentException("Estimated size must be greater than 0");
        }
        final List<E> list = new ArrayList<E>(estimatedSize);
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * Gets a suitable Iterator for the given object.
     * <p>
     * This method can handle objects as follows
     * <ul>
     * <li>null - empty iterator
     * <li>Iterator - returned directly
     * <li>Enumeration - wrapped
     * <li>Collection - iterator from collection returned
     * <li>Map - values iterator returned
     * <li>Dictionary - values (elements) enumeration returned as iterator
     * <li>array - iterator over array returned
     * <li>object with iterator() public method accessed by reflection
     * <li>object - singleton iterator
     * <li>NodeList - iterator over the list
     * <li>Node - iterator over the child nodes
     * </ul>
     *
     * @param obj  the object to convert to an iterator
     * @return a suitable iterator, never null
     */
    public static Iterator<?> getIterator(final Object obj) {
        if (obj == null) {
            return emptyIterator();
        }
        if (obj instanceof Iterator) {
            return (Iterator<?>) obj;
        }
        if (obj instanceof Iterable) {
            return ((Iterable<?>) obj).iterator();
        }
        if (obj instanceof Object[]) {
            return new ObjectArrayIterator<Object>((Object[]) obj);
        }
        if (obj instanceof Enumeration) {
            return new EnumerationIterator<Object>((Enumeration<?>) obj);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).values().iterator();
        }
        if (obj instanceof NodeList) {
            return new NodeListIterator((NodeList) obj);
        }
        if (obj instanceof Node) {
            return new NodeListIterator((Node) obj);
        }
        if (obj instanceof Dictionary) {
            return new EnumerationIterator<Object>(((Dictionary<?, ?>) obj).elements());
        } else if (obj.getClass().isArray()) {
            return new ArrayIterator<Object>(obj);
        }
        try {
            final Method method = obj.getClass().getMethod("iterator", (Class[]) null);
            if (Iterator.class.isAssignableFrom(method.getReturnType())) {
                final Iterator<?> it = (Iterator<?>) method.invoke(obj, (Object[]) null);
                if (it != null) {
                    return it;
                }
            }
        } catch (final RuntimeException e) { // NOPMD
            // ignore
        } catch (final NoSuchMethodException e) { // NOPMD
            // ignore
        } catch (final IllegalAccessException e) { // NOPMD
            // ignore
        } catch (final InvocationTargetException e) { // NOPMD
            // ignore
        }
        return singletonIterator(obj);
    }

    // Utility methods
    //-----------------------------------------------------------------------

    /**
     * Applies the closure to each element of the provided iterator.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to use, may be null
     * @param closure  the closure to apply to each element, may not be null
     * @throws NullPointerException if closure is null
     * @since 4.1
     */
    public static <E> void apply(final Iterator<E> iterator, final Closure<? super E> closure) {
        if (closure == null) {
            throw new NullPointerException("Closure must not be null");
        }

        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                closure.execute(element);
            }
        }
    }

    /**
     * Finds the first element in the given iterator which matches the given predicate.
     * <p>
     * A <code>null</code> or empty iterator returns null.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to search, may be null
     * @param predicate  the predicate to use, may not be null
     * @return the first element of the iterator which matches the predicate or null if none could be found
     * @throws NullPointerException if predicate is null
     * @since 4.1
     */
    public static <E> E find(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        if (predicate == null) {
            throw new NullPointerException("Predicate must not be null");
        }

        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                if (predicate.evaluate(element)) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Answers true if a predicate is true for any element of the iterator.
     * <p>
     * A <code>null</code> or empty iterator returns false.
     *
     * @param <E>  the type of object the {@link Iterator} contains
     * @param iterator  the {@link Iterator} to use, may be null
     * @param predicate  the predicate to use, may not be null
     * @return true if any element of the collection matches the predicate, false otherwise
     * @throws NullPointerException if predicate is null
     * @since 4.1
     */
    public static <E> boolean matchesAny(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        if (predicate == null) {
            throw new NullPointerException("Predicate must not be null");
        }

        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                if (predicate.evaluate(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Answers true if a predicate is true for every element of an iterator.
     * <p>
     * A <code>null</code> or empty iterator returns true.
     *
     * @param <E>  the type of object the {@link Iterator} contains
     * @param iterator  the {@link Iterator} to use, may be null
     * @param predicate  the predicate to use, may not be null
     * @return true if every element of the collection matches the predicate or if the
     *   collection is empty, false otherwise
     * @throws NullPointerException if predicate is null
     * @since 4.1
     */
    public static <E> boolean matchesAll(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        if (predicate == null) {
            throw new NullPointerException("Predicate must not be null");
        }

        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                if (!predicate.evaluate(element)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given iterator is empty.
     * <p>
     * A <code>null</code> or empty iterator returns true.
     *
     * @param iterator  the {@link Iterator} to use, may be null
     * @return true if the iterator is exhausted or null, false otherwise
     * @since 4.1
     */
    public static boolean isEmpty(final Iterator<?> iterator) {
        return iterator == null || !iterator.hasNext();
    }

    /**
     * Checks if the object is contained in the given iterator.
     * <p>
     * A <code>null</code> or empty iterator returns false.
     *
     * @param <E>  the type of object the {@link Iterator} contains
     * @param iterator  the iterator to check, may be null
     * @param object  the object to check
     * @return true if the object is contained in the iterator, false otherwise
     * @since 4.1
     */
    public static <E> boolean contains(final Iterator<E> iterator, final Object object) {
        return matchesAny(iterator, EqualPredicate.equalPredicate(object));
    }

    /**
     * Returns the <code>index</code>-th value in {@link Iterator}, throwing
     * <code>IndexOutOfBoundsException</code> if there is no such element.
     * <p>
     * The Iterator is advanced to <code>index</code> (or to the end, if
     * <code>index</code> exceeds the number of entries) as a side effect of this method.
     *
     * @param <E>  the type of object in the {@link Iterator}
     * @param iterator  the iterator to get a value from
     * @param index  the index to get
     * @return the object at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @throws IllegalArgumentException if the object type is invalid
     * @since 4.1
     */
    public static <E> E get(final Iterator<E> iterator, final int index) {
        int i = index;
        CollectionUtils.checkIndexBounds(i);
        while (iterator.hasNext()) {
            i--;
            if (i == -1) {
                return iterator.next();
            }
            iterator.next();
        }
        throw new IndexOutOfBoundsException("Entry does not exist: " + i);
    }

    /**
     * Returns the number of elements contained in the given iterator.
     * <p>
     * A <code>null</code> or empty iterator returns {@code 0}.
     *
     * @param iterator  the iterator to check, may be null
     * @return the number of elements contained in the iterator
     * @since 4.1
     */
    public static int size(final Iterator<?> iterator) {
        int size = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
        }
        return size;
    }

    /**
     * Returns a string representation of the elements of the specified iterator.
     * <p>
     * The string representation consists of a list of the iterator's elements,
     * enclosed in square brackets ({@code "[]"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space). Elements are
     * converted to strings as by {@code String.valueOf(Object)}.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to convert to a string
     * @return a string representation of {@code iterator}
     * @since 4.1
     */
    public static <E> String toString(final Iterator<E> iterator) {
        return toString(iterator, TransformerUtils.stringValueTransformer(),
                        DEFAULT_TOSTRING_DELIMITER, DEFAULT_TOSTRING_PREFIX,
                        DEFAULT_TOSTRING_SUFFIX);
    }

    /**
     * Returns a string representation of the elements of the specified iterator.
     * <p>
     * The string representation consists of a list of the iterable's elements,
     * enclosed in square brackets ({@code "[]"}). Adjacent elements are separated
     * by the characters {@code ", "} (a comma followed by a space). Elements are
     * converted to strings as by using the provided {@code transformer}.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to convert to a string, may be null
     * @param transformer  the transformer used to get a string representation of an element
     * @return a string representation of {@code iterator}
     * @throws NullPointerException if {@code transformer} is null
     * @since 4.1
     */
    public static <E> String toString(final Iterator<E> iterator,
                                      final Transformer<? super E, String> transformer) {
        return toString(iterator, transformer, DEFAULT_TOSTRING_DELIMITER,
                        DEFAULT_TOSTRING_PREFIX, DEFAULT_TOSTRING_SUFFIX);
    }

    /**
     * Returns a string representation of the elements of the specified iterator.
     * <p>
     * The string representation consists of a list of the iterator's elements,
     * enclosed by the provided {@code prefix} and {@code suffix}. Adjacent elements
     * are separated by the provided {@code delimiter}. Elements are converted to
     * strings as by using the provided {@code transformer}.
     *
     * @param <E>  the element type
     * @param iterator  the iterator to convert to a string, may be null
     * @param transformer  the transformer used to get a string representation of an element
     * @param delimiter  the string to delimit elements
     * @param prefix  the prefix, prepended to the string representation
     * @param suffix  the suffix, appended to the string representation
     * @return a string representation of {@code iterator}
     * @throws NullPointerException if either transformer, delimiter, prefix or suffix is null
     * @since 4.1
     */
    public static <E> String toString(final Iterator<E> iterator,
                                      final Transformer<? super E, String> transformer,
                                      final String delimiter,
                                      final String prefix,
                                      final String suffix) {
        if (transformer == null) {
            throw new NullPointerException("transformer may not be null");
        }
        if (delimiter == null) {
            throw new NullPointerException("delimiter may not be null");
        }
        if (prefix == null) {
            throw new NullPointerException("prefix may not be null");
        }
        if (suffix == null) {
            throw new NullPointerException("suffix may not be null");
        }
        final StringBuilder stringBuilder = new StringBuilder(prefix);
        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                stringBuilder.append(transformer.transform(element));
                stringBuilder.append(delimiter);
            }
            if(stringBuilder.length() > prefix.length()) {
                stringBuilder.setLength(stringBuilder.length() - delimiter.length());
            }
        }
        stringBuilder.append(suffix);
        return stringBuilder.toString();
    }

}

```
