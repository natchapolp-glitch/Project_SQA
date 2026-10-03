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
    <version>40</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-collections4</artifactId>
  <version>4.2-SNAPSHOT</version>
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
      <name>Gary Gregory</name>
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
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.easymock</groupId>
      <artifactId>easymock</artifactId>
      <version>3.4</version>
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
    <commons.release.version>4.1</commons.release.version>
    <commons.release.desc>(Java 6.0+)</commons.release.desc>

    <!-- Previous 3.x release series -->
    <commons.release.2.version>3.2.2</commons.release.2.version>
    <commons.release.2.desc>(Requires Java 1.3 or later)</commons.release.2.desc>
    <!-- Override generated name -->
    <commons.release.2.name>commons-collections-${commons.release.2.version}</commons.release.2.name>

    <commons.jira.id>COLLECTIONS</commons.jira.id>
    <commons.jira.pid>12310465</commons.jira.pid>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC1</commons.rc.version>
    <checkstyle.version>2.9.1</checkstyle.version>

    <commons.site.path>collections</commons.site.path>
    <commons.scmPubUrl>https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-collections</commons.scmPubUrl>
    <commons.scmPubCheckoutDirectory>site-content</commons.scmPubCheckoutDirectory>

    <!-- Override javadoc plugin version due to a vulnerability of the generated javadoc
         see http://jira.codehaus.org/browse/MJAVADOC-370, remove after update to CP 32   -->
    <commons.javadoc.version>2.9.1</commons.javadoc.version>

    <!-- Override default buildNumber timestamp format, needed for coveralls plugin -->
    <maven.buildNumber.timestampFormat>{0,date,yyyy-MM-dd HH:mm:ssZ}</maven.buildNumber.timestampFormat>
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
            <descriptor>src/assembly/bin.xml</descriptor>
            <descriptor>src/assembly/src.xml</descriptor>
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
            <exclude>.travis.yml</exclude>
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

    <profile>
      <id>travis</id>
      <activation>
        <property>
          <name>env.TRAVIS</name>
          <value>true</value>
        </property>
      </activation>
      <properties>
        <buildnumber.skip>true</buildnumber.skip>
      </properties>
      <build>
        <plugins>
          <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>${commons.jacoco.version}</version>
            <executions>
              <execution>
                <id>prepare-agent</id>
                <goals>
                  <goal>prepare-agent</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
          <plugin>
            <groupId>org.eluder.coveralls</groupId>
            <artifactId>coveralls-maven-plugin</artifactId>
            <version>3.1.0</version>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>

</project>

```

## src/main/java/org/apache/commons/collections4/trie/AbstractPatriciaTrie.java

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
package org.apache.commons.collections4.trie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.collections4.OrderedMapIterator;

/**
 * This class implements the base PATRICIA algorithm and everything that
 * is related to the {@link Map} interface.
 *
 * @since 4.0
 * @version $Id$
 */
abstract class AbstractPatriciaTrie<K, V> extends AbstractBitwiseTrie<K, V> {

    private static final long serialVersionUID = 5155253417231339498L;

    /** The root node of the {@link Trie}. */
    private transient TrieEntry<K, V> root = new TrieEntry<K, V>(null, null, -1);

    /**
     * Each of these fields are initialized to contain an instance of the
     * appropriate view the first time this view is requested. The views are
     * stateless, so there's no reason to create more than one of each.
     */
    private transient volatile Set<K> keySet;
    private transient volatile Collection<V> values;
    private transient volatile Set<Map.Entry<K,V>> entrySet;

    /** The current size of the {@link Trie}. */
    private transient int size = 0;

    /**
     * The number of times this {@link Trie} has been modified.
     * It's used to detect concurrent modifications and fail-fast the {@link Iterator}s.
     */
    protected transient int modCount = 0;

    protected AbstractPatriciaTrie(final KeyAnalyzer<? super K> keyAnalyzer) {
        super(keyAnalyzer);
    }

    /**
     * Constructs a new {@link org.apache.commons.collections4.Trie Trie} using the given
     * {@link KeyAnalyzer} and initializes the {@link org.apache.commons.collections4.Trie Trie}
     * with the values from the provided {@link Map}.
     */
    protected AbstractPatriciaTrie(final KeyAnalyzer<? super K> keyAnalyzer,
                                   final Map<? extends K, ? extends V> map) {
        super(keyAnalyzer);
        putAll(map);
    }

    //-----------------------------------------------------------------------
    @Override
    public void clear() {
        root.key = null;
        root.bitIndex = -1;
        root.value = null;

        root.parent = null;
        root.left = root;
        root.right = null;
        root.predecessor = root;

        size = 0;
        incrementModCount();
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * A helper method to increment the {@link Trie} size and the modification counter.
     */
    void incrementSize() {
        size++;
        incrementModCount();
    }

    /**
     * A helper method to decrement the {@link Trie} size and increment the modification counter.
     */
    void decrementSize() {
        size--;
        incrementModCount();
    }

    /**
     * A helper method to increment the modification counter.
     */
    private void incrementModCount() {
        ++modCount;
    }

    @Override
    public V put(final K key, final V value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        final int lengthInBits = lengthInBits(key);

        // The only place to store a key with a length
        // of zero bits is the root node
        if (lengthInBits == 0) {
            if (root.isEmpty()) {
                incrementSize();
            } else {
                incrementModCount();
            }
            return root.setKeyValue(key, value);
        }

        final TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
        if (compareKeys(key, found.key)) {
            if (found.isEmpty()) { // <- must be the root
                incrementSize();
            } else {
                incrementModCount();
            }
            return found.setKeyValue(key, value);
        }

        final int bitIndex = bitIndex(key, found.key);
        if (!KeyAnalyzer.isOutOfBoundsIndex(bitIndex)) {
            if (KeyAnalyzer.isValidBitIndex(bitIndex)) { // in 99.999...9% the case
                /* NEW KEY+VALUE TUPLE */
                final TrieEntry<K, V> t = new TrieEntry<K, V>(key, value, bitIndex);
                addEntry(t, lengthInBits);
                incrementSize();
                return null;
            } else if (KeyAnalyzer.isNullBitKey(bitIndex)) {
                // A bits of the Key are zero. The only place to
                // store such a Key is the root Node!

                /* NULL BIT KEY */
                if (root.isEmpty()) {
                    incrementSize();
                } else {
                    incrementModCount();
                }
                return root.setKeyValue(key, value);

            } else if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
                // This is a very special and rare case.

                /* REPLACE OLD KEY+VALUE */
                if (found != root) {
                    incrementModCount();
                    return found.setKeyValue(key, value);
                }
            }
        }

        throw new IllegalArgumentException("Failed to put: " + key + " -> " + value + ", " + bitIndex);
    }

    /**
     * Adds the given {@link TrieEntry} to the {@link Trie}.
     */
    TrieEntry<K, V> addEntry(final TrieEntry<K, V> entry, final int lengthInBits) {
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while(true) {
            if (current.bitIndex >= entry.bitIndex
                    || current.bitIndex <= path.bitIndex) {
                entry.predecessor = entry;

                if (!isBitSet(entry.key, entry.bitIndex, lengthInBits)) {
                    entry.left = entry;
                    entry.right = current;
                } else {
                    entry.left = current;
                    entry.right = entry;
                }

                entry.parent = path;
                if (current.bitIndex >= entry.bitIndex) {
                    current.parent = entry;
                }

                // if we inserted an uplink, set the predecessor on it
                if (current.bitIndex <= path.bitIndex) {
                    current.predecessor = entry;
                }

                if (path == root || !isBitSet(entry.key, path.bitIndex, lengthInBits)) {
                    path.left = entry;
                } else {
                    path.right = entry;
                }

                return entry;
            }

            path = current;

            if (!isBitSet(entry.key, current.bitIndex, lengthInBits)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    @Override
    public V get(final Object k) {
        final TrieEntry<K, V> entry = getEntry(k);
        return entry != null ? entry.getValue() : null;
    }

    /**
     * Returns the entry associated with the specified key in the
     * PatriciaTrieBase.  Returns null if the map contains no mapping
     * for this key.
     * <p>
     * This may throw ClassCastException if the object is not of type K.
     */
    TrieEntry<K,V> getEntry(final Object k) {
        final K key = castKey(k);
        if (key == null) {
            return null;
        }

        final int lengthInBits = lengthInBits(key);
        final TrieEntry<K,V> entry = getNearestEntryForKey(key, lengthInBits);
        return !entry.isEmpty() && compareKeys(key, entry.key) ? entry : null;
    }

    /**
     * Returns the {@link Entry} whose key is closest in a bitwise XOR
     * metric to the given key. This is NOT lexicographic closeness.
     * For example, given the keys:
     *
     * <ol>
     * <li>D = 1000100
     * <li>H = 1001000
     * <li>L = 1001100
     * </ol>
     *
     * If the {@link Trie} contained 'H' and 'L', a lookup of 'D' would
     * return 'L', because the XOR distance between D &amp; L is smaller
     * than the XOR distance between D &amp; H.
     *
     * @param key  the key to use in the search
     * @return the {@link Entry} whose key is closest in a bitwise XOR metric
     *   to the provided key
     */
    public Map.Entry<K, V> select(final K key) {
        final int lengthInBits = lengthInBits(key);
        final Reference<Map.Entry<K, V>> reference = new Reference<Map.Entry<K,V>>();
        if (!selectR(root.left, -1, key, lengthInBits, reference)) {
            return reference.get();
        }
        return null;
    }

    /**
     * Returns the key that is closest in a bitwise XOR metric to the
     * provided key. This is NOT lexicographic closeness!
     *
     * For example, given the keys:
     *
     * <ol>
     * <li>D = 1000100
     * <li>H = 1001000
     * <li>L = 1001100
     * </ol>
     *
     * If the {@link Trie} contained 'H' and 'L', a lookup of 'D' would
     * return 'L', because the XOR distance between D &amp; L is smaller
     * than the XOR distance between D &amp; H.
     *
     * @param key  the key to use in the search
     * @return the key that is closest in a bitwise XOR metric to the provided key
     */
    public K selectKey(final K key) {
        final Map.Entry<K, V> entry = select(key);
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    /**
     * Returns the value whose key is closest in a bitwise XOR metric to
     * the provided key. This is NOT lexicographic closeness!
     *
     * For example, given the keys:
     *
     * <ol>
     * <li>D = 1000100
     * <li>H = 1001000
     * <li>L = 1001100
     * </ol>
     *
     * If the {@link Trie} contained 'H' and 'L', a lookup of 'D' would
     * return 'L', because the XOR distance between D &amp; L is smaller
     * than the XOR distance between D &amp; H.
     *
     * @param key  the key to use in the search
     * @return the value whose key is closest in a bitwise XOR metric
     * to the provided key
     */
    public V selectValue(final K key) {
        final Map.Entry<K, V> entry = select(key);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    /**
     * This is equivalent to the other {@link #selectR(TrieEntry, int, Object, int, Cursor, Reference)}
     * method but without its overhead because we're selecting only one best matching Entry from the {@link Trie}.
     */
    private boolean selectR(final TrieEntry<K, V> h, final int bitIndex,
                            final K key, final int lengthInBits,
                            final Reference<Map.Entry<K, V>> reference) {

        if (h.bitIndex <= bitIndex) {
            // If we hit the root Node and it is empty
            // we have to look for an alternative best
            // matching node.
            if (!h.isEmpty()) {
                reference.set(h);
                return false;
            }
            return true;
        }

        if (!isBitSet(key, h.bitIndex, lengthInBits)) {
            if (selectR(h.left, h.bitIndex, key, lengthInBits, reference)) {
                return selectR(h.right, h.bitIndex, key, lengthInBits, reference);
            }
        } else {
            if (selectR(h.right, h.bitIndex, key, lengthInBits, reference)) {
                return selectR(h.left, h.bitIndex, key, lengthInBits, reference);
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(final Object k) {
        if (k == null) {
            return false;
        }

        final K key = castKey(k);
        final int lengthInBits = lengthInBits(key);
        final TrieEntry<K, V> entry = getNearestEntryForKey(key, lengthInBits);
        return !entry.isEmpty() && compareKeys(key, entry.key);
    }

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new Values();
        }
        return values;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ClassCastException if provided key is of an incompatible type
     */
    @Override
    public V remove(final Object k) {
        if (k == null) {
            return null;
        }

        final K key = castKey(k);
        final int lengthInBits = lengthInBits(key);
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while (true) {
            if (current.bitIndex <= path.bitIndex) {
                if (!current.isEmpty() && compareKeys(key, current.key)) {
                    return removeEntry(current);
                }
                return null;
            }

            path = current;

            if (!isBitSet(key, current.bitIndex, lengthInBits)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    /**
     * Returns the nearest entry for a given key.  This is useful
     * for finding knowing if a given key exists (and finding the value
     * for it), or for inserting the key.
     *
     * The actual get implementation. This is very similar to
     * selectR but with the exception that it might return the
     * root Entry even if it's empty.
     */
    TrieEntry<K, V> getNearestEntryForKey(final K key, final int lengthInBits) {
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while(true) {
            if (current.bitIndex <= path.bitIndex) {
                return current;
            }

            path = current;
            if (!isBitSet(key, current.bitIndex, lengthInBits)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    /**
     * Removes a single entry from the {@link Trie}.
     *
     * If we found a Key (Entry h) then figure out if it's
     * an internal (hard to remove) or external Entry (easy
     * to remove)
     */
    V removeEntry(final TrieEntry<K, V> h) {
        if (h != root) {
            if (h.isInternalNode()) {
                removeInternalEntry(h);
            } else {
                removeExternalEntry(h);
            }
        }

        decrementSize();
        return h.setKeyValue(null, null);
    }

    /**
     * Removes an external entry from the {@link Trie}.
     *
     * If it's an external Entry then just remove it.
     * This is very easy and straight forward.
     */
    private void removeExternalEntry(final TrieEntry<K, V> h) {
        if (h == root) {
            throw new IllegalArgumentException("Cannot delete root Entry!");
        } else if (!h.isExternalNode()) {
            throw new IllegalArgumentException(h + " is not an external Entry!");
        }

        final TrieEntry<K, V> parent = h.parent;
        final TrieEntry<K, V> child = h.left == h ? h.right : h.left;

        if (parent.left == h) {
            parent.left = child;
        } else {
            parent.right = child;
        }

        // either the parent is changing, or the predecessor is changing.
        if (child.bitIndex > parent.bitIndex) {
            child.parent = parent;
        } else {
            child.predecessor = parent;
        }

    }

    /**
     * Removes an internal entry from the {@link Trie}.
     *
     * If it's an internal Entry then "good luck" with understanding
     * this code. The Idea is essentially that Entry p takes Entry h's
     * place in the trie which requires some re-wiring.
     */
    private void removeInternalEntry(final TrieEntry<K, V> h) {
        if (h == root) {
            throw new IllegalArgumentException("Cannot delete root Entry!");
        } else if (!h.isInternalNode()) {
            throw new IllegalArgumentException(h + " is not an internal Entry!");
        }

        final TrieEntry<K, V> p = h.predecessor;

        // Set P's bitIndex
        p.bitIndex = h.bitIndex;

        // Fix P's parent, predecessor and child Nodes
        {
            final TrieEntry<K, V> parent = p.parent;
            final TrieEntry<K, V> child = p.left == h ? p.right : p.left;

            // if it was looping to itself previously,
            // it will now be pointed from it's parent
            // (if we aren't removing it's parent --
            //  in that case, it remains looping to itself).
            // otherwise, it will continue to have the same
            // predecessor.
            if (p.predecessor == p && p.parent != h) {
                p.predecessor = p.parent;
            }

            if (parent.left == p) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            if (child.bitIndex > parent.bitIndex) {
                child.parent = parent;
            }
        }

        // Fix H's parent and child Nodes
        {
            // If H is a parent of its left and right child
            // then change them to P
            if (h.left.parent == h) {
                h.left.parent = p;
            }

            if (h.right.parent == h) {
                h.right.parent = p;
            }

            // Change H's parent
            if (h.parent.left == h) {
                h.parent.left = p;
            } else {
                h.parent.right = p;
            }
        }

        // Copy the remaining fields from H to P
        //p.bitIndex = h.bitIndex;
        p.parent = h.parent;
        p.left = h.left;
        p.right = h.right;

        // Make sure that if h was pointing to any uplinks,
        // p now points to them.
        if (isValidUplink(p.left, p)) {
            p.left.predecessor = p;
        }

        if (isValidUplink(p.right, p)) {
            p.right.predecessor = p;
        }
    }

    /**
     * Returns the entry lexicographically after the given entry.
     * If the given entry is null, returns the first node.
     */
    TrieEntry<K, V> nextEntry(final TrieEntry<K, V> node) {
        if (node == null) {
            return firstEntry();
        }
        return nextEntryImpl(node.predecessor, node, null);
    }

    /**
     * Scans for the next node, starting at the specified point, and using 'previous'
     * as a hint that the last node we returned was 'previous' (so we know not to return
     * it again).  If 'tree' is non-null, this will limit the search to the given tree.
     *
     * The basic premise is that each iteration can follow the following steps:
     *
     * 1) Scan all the way to the left.
     *   a) If we already started from this node last time, proceed to Step 2.
     *   b) If a valid uplink is found, use it.
     *   c) If the result is an empty node (root not set), break the scan.
     *   d) If we already returned the left node, break the scan.
     *
     * 2) Check the right.
     *   a) If we already returned the right node, proceed to Step 3.
     *   b) If it is a valid uplink, use it.
     *   c) Do Step 1 from the right node.
     *
     * 3) Back up through the parents until we encounter find a parent
     *    that we're not the right child of.
     *
     * 4) If there's no right child of that parent, the iteration is finished.
     *    Otherwise continue to Step 5.
     *
     * 5) Check to see if the right child is a valid uplink.
     *    a) If we already returned that child, proceed to Step 6.
     *       Otherwise, use it.
     *
     * 6) If the right child of the parent is the parent itself, we've
     *    already found & returned the end of the Trie, so exit.
     *
     * 7) Do Step 1 on the parent's right child.
     */
    TrieEntry<K, V> nextEntryImpl(final TrieEntry<K, V> start,
            final TrieEntry<K, V> previous, final TrieEntry<K, V> tree) {

        TrieEntry<K, V> current = start;

        // Only look at the left if this was a recursive or
        // the first check, otherwise we know we've already looked
        // at the left.
        if (previous == null || start != previous.predecessor) {
            while (!current.left.isEmpty()) {
                // stop traversing if we've already
                // returned the left of this node.
                if (previous == current.left) {
                    break;
                }

                if (isValidUplink(current.left, current)) {
                    return current.left;
                }

                current = current.left;
            }
        }

        // If there's no data at all, exit.
        if (current.isEmpty()) {
            return null;
        }

        // If we've already returned the left,
        // and the immediate right is null,
        // there's only one entry in the Trie
        // which is stored at the root.
        //
        //  / ("")   <-- root
        //  \_/  \
        //       null <-- 'current'
        //
        if (current.right == null) {
            return null;
        }

        // If nothing valid on the left, try the right.
        if (previous != current.right) {
            // See if it immediately is valid.
            if (isValidUplink(current.right, current)) {
                return current.right;
            }

            // Must search on the right's side if it wasn't initially valid.
            return nextEntryImpl(current.right, previous, tree);
        }

        // Neither left nor right are valid, find the first parent
        // whose child did not come from the right & traverse it.
        while (current == current.parent.right) {
            // If we're going to traverse to above the subtree, stop.
            if (current == tree) {
                return null;
            }

            current = current.parent;
        }

        // If we're on the top of the subtree, we can't go any higher.
        if (current == tree) {
            return null;
        }

        // If there's no right, the parent must be root, so we're done.
        if (current.parent.right == null) {
            return null;
        }

        // If the parent's right points to itself, we've found one.
        if (previous != current.parent.right
                && isValidUplink(current.parent.right, current.parent)) {
            return current.parent.right;
        }

        // If the parent's right is itself, there can't be any more nodes.
        if (current.parent.right == current.parent) {
            return null;
        }

        // We need to traverse down the parent's right's path.
        return nextEntryImpl(current.parent.right, previous, tree);
    }

    /**
     * Returns the first entry the {@link Trie} is storing.
     * <p>
     * This is implemented by going always to the left until
     * we encounter a valid uplink. That uplink is the first key.
     */
    TrieEntry<K, V> firstEntry() {
        // if Trie is empty, no first node.
        if (isEmpty()) {
            return null;
        }

        return followLeft(root);
    }

    /**
     * Goes left through the tree until it finds a valid node.
     */
    TrieEntry<K, V> followLeft(TrieEntry<K, V> node) {
        while(true) {
            TrieEntry<K, V> child = node.left;
            // if we hit root and it didn't have a node, go right instead.
            if (child.isEmpty()) {
                child = node.right;
            }

            if (child.bitIndex <= node.bitIndex) {
                return child;
            }

            node = child;
        }
    }

    //-----------------------------------------------------------------------

    @Override
    public Comparator<? super K> comparator() {
        return getKeyAnalyzer();
    }

    @Override
    public K firstKey() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return firstEntry().getKey();
    }

    @Override
    public K lastKey() {
        final TrieEntry<K, V> entry = lastEntry();
        if (entry != null) {
            return entry.getKey();
        }
        throw new NoSuchElementException();
    }

    @Override
    public K nextKey(final K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        final TrieEntry<K, V> entry = getEntry(key);
        if (entry != null) {
            final TrieEntry<K, V> nextEntry = nextEntry(entry);
            return nextEntry != null ? nextEntry.getKey() : null;
        }
        return null;
    }

    @Override
    public K previousKey(final K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        final TrieEntry<K, V> entry = getEntry(key);
        if (entry != null) {
            final TrieEntry<K, V> prevEntry = previousEntry(entry);
            return prevEntry != null ? prevEntry.getKey() : null;
        }
        return null;
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        return new TrieMapIterator();
    }

    @Override
    public SortedMap<K, V> prefixMap(final K key) {
        return getPrefixMapByBits(key, 0, lengthInBits(key));
    }

    /**
     * Returns a view of this {@link Trie} of all elements that are prefixed
     * by the number of bits in the given Key.
     * <p>
     * The view that this returns is optimized to have a very efficient
     * {@link Iterator}. The {@link SortedMap#firstKey()},
     * {@link SortedMap#lastKey()} &amp; {@link Map#size()} methods must
     * iterate over all possible values in order to determine the results.
     * This information is cached until the PATRICIA {@link Trie} changes.
     * All other methods (except {@link Iterator}) must compare the given
     * key to the prefix to ensure that it is within the range of the view.
     * The {@link Iterator}'s remove method must also relocate the subtree
     * that contains the prefixes if the entry holding the subtree is
     * removed or changes. Changing the subtree takes O(K) time.
     *
     * @param key  the key to use in the search
     * @param offsetInBits  the prefix offset
     * @param lengthInBits  the number of significant prefix bits
     * @return a {@link SortedMap} view of this {@link Trie} with all elements whose
     *   key is prefixed by the search key
     */
    private SortedMap<K, V> getPrefixMapByBits(final K key, final int offsetInBits, final int lengthInBits) {

        final int offsetLength = offsetInBits + lengthInBits;
        if (offsetLength > lengthInBits(key)) {
            throw new IllegalArgumentException(offsetInBits + " + "
                    + lengthInBits + " > " + lengthInBits(key));
        }

        if (offsetLength == 0) {
            return this;
        }

        return new PrefixRangeMap(key, offsetInBits, lengthInBits);
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        return new RangeEntryMap(null, toKey);
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return new RangeEntryMap(fromKey, toKey);
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        return new RangeEntryMap(fromKey, null);
    }

    /**
     * Returns an entry strictly higher than the given key,
     * or null if no such entry exists.
     */
    TrieEntry<K,V> higherEntry(final K key) {
        // TODO: Cleanup so that we don't actually have to add/remove from the
        //       tree.  (We do it here because there are other well-defined
        //       functions to perform the search.)
        final int lengthInBits = lengthInBits(key);

        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                // If data in root, and more after -- return it.
                if (size() > 1) {
                    return nextEntry(root);
                }
                // If no more after, no higher entry.
                return null;
            }
            // Root is empty & we want something after empty, return first.
            return firstEntry();
        }

        final TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
        if (compareKeys(key, found.key)) {
            return nextEntry(found);
        }

        final int bitIndex = bitIndex(key, found.key);
        if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
            final TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
            addEntry(added, lengthInBits);
            incrementSize(); // must increment because remove will decrement
            final TrieEntry<K, V> ceil = nextEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return ceil;
        } else if (KeyAnalyzer.isNullBitKey(bitIndex)) {
            if (!root.isEmpty()) {
                return firstEntry();
            } else if (size() > 1) {
                return nextEntry(firstEntry());
            } else {
                return null;
            }
        } else if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
            return nextEntry(found);
        }

        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Returns a key-value mapping associated with the least key greater
     * than or equal to the given key, or null if there is no such key.
     */
    TrieEntry<K,V> ceilingEntry(final K key) {
        // Basically:
        // Follow the steps of adding an entry, but instead...
        //
        // - If we ever encounter a situation where we found an equal
        //   key, we return it immediately.
        //
        // - If we hit an empty root, return the first iterable item.
        //
        // - If we have to add a new item, we temporarily add it,
        //   find the successor to it, then remove the added item.
        //
        // These steps ensure that the returned value is either the
        // entry for the key itself, or the first entry directly after
        // the key.

        // TODO: Cleanup so that we don't actually have to add/remove from the
        //       tree.  (We do it here because there are other well-defined
        //       functions to perform the search.)
        final int lengthInBits = lengthInBits(key);

        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                return root;
            }
            return firstEntry();
        }

        final TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
        if (compareKeys(key, found.key)) {
            return found;
        }

        final int bitIndex = bitIndex(key, found.key);
        if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
            final TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
            addEntry(added, lengthInBits);
            incrementSize(); // must increment because remove will decrement
            final TrieEntry<K, V> ceil = nextEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return ceil;
        } else if (KeyAnalyzer.isNullBitKey(bitIndex)) {
            if (!root.isEmpty()) {
                return root;
            }
            return firstEntry();
        } else if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
            return found;
        }

        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Returns a key-value mapping associated with the greatest key
     * strictly less than the given key, or null if there is no such key.
     */
    TrieEntry<K,V> lowerEntry(final K key) {
        // Basically:
        // Follow the steps of adding an entry, but instead...
        //
        // - If we ever encounter a situation where we found an equal
        //   key, we return it's previousEntry immediately.
        //
        // - If we hit root (empty or not), return null.
        //
        // - If we have to add a new item, we temporarily add it,
        //   find the previousEntry to it, then remove the added item.
        //
        // These steps ensure that the returned value is always just before
        // the key or null (if there was nothing before it).

        // TODO: Cleanup so that we don't actually have to add/remove from the
        //       tree.  (We do it here because there are other well-defined
        //       functions to perform the search.)
        final int lengthInBits = lengthInBits(key);

        if (lengthInBits == 0) {
            return null; // there can never be anything before root.
        }

        final TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
        if (compareKeys(key, found.key)) {
            return previousEntry(found);
        }

        final int bitIndex = bitIndex(key, found.key);
        if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
            final TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
            addEntry(added, lengthInBits);
            incrementSize(); // must increment because remove will decrement
            final TrieEntry<K, V> prior = previousEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return prior;
        } else if (KeyAnalyzer.isNullBitKey(bitIndex)) {
            return null;
        } else if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
            return previousEntry(found);
        }

        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Returns a key-value mapping associated with the greatest key
     * less than or equal to the given key, or null if there is no such key.
     */
    TrieEntry<K,V> floorEntry(final K key) {
        // TODO: Cleanup so that we don't actually have to add/remove from the
        //       tree.  (We do it here because there are other well-defined
        //       functions to perform the search.)
        final int lengthInBits = lengthInBits(key);

        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                return root;
            }
            return null;
        }

        final TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
        if (compareKeys(key, found.key)) {
            return found;
        }

        final int bitIndex = bitIndex(key, found.key);
        if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
            final TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
            addEntry(added, lengthInBits);
            incrementSize(); // must increment because remove will decrement
            final TrieEntry<K, V> floor = previousEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return floor;
        } else if (KeyAnalyzer.isNullBitKey(bitIndex)) {
            if (!root.isEmpty()) {
                return root;
            }
            return null;
        } else if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
            return found;
        }

        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Finds the subtree that contains the prefix.
     *
     * This is very similar to getR but with the difference that
     * we stop the lookup if h.bitIndex > lengthInBits.
     */
    TrieEntry<K, V> subtree(final K prefix, final int offsetInBits, final int lengthInBits) {
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while(true) {
            if (current.bitIndex <= path.bitIndex || lengthInBits <= current.bitIndex) {
                break;
            }

            path = current;
            if (!isBitSet(prefix, offsetInBits + current.bitIndex, offsetInBits + lengthInBits)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // Make sure the entry is valid for a subtree.
        final TrieEntry<K, V> entry = current.isEmpty() ? path : current;

        // If entry is root, it can't be empty.
        if (entry.isEmpty()) {
            return null;
        }

        final int endIndexInBits = offsetInBits + lengthInBits;

        // if root && length of root is less than length of lookup,
        // there's nothing.
        // (this prevents returning the whole subtree if root has an empty
        //  string and we want to lookup things with "\0")
        if (entry == root && lengthInBits(entry.getKey()) < endIndexInBits) {
            return null;
        }

        // Found key's length-th bit differs from our key
        // which means it cannot be the prefix...
        if (isBitSet(prefix, endIndexInBits - 1, endIndexInBits)
                != isBitSet(entry.key, lengthInBits - 1, lengthInBits(entry.key))) {
            return null;
        }

        // ... or there are less than 'length' equal bits
        final int bitIndex = getKeyAnalyzer().bitIndex(prefix, offsetInBits, lengthInBits,
                                                       entry.key, 0, lengthInBits(entry.getKey()));

        if (bitIndex >= 0 && bitIndex < lengthInBits) {
            return null;
        }

        return entry;
    }

    /**
     * Returns the last entry the {@link Trie} is storing.
     *
     * <p>This is implemented by going always to the right until
     * we encounter a valid uplink. That uplink is the last key.
     */
    TrieEntry<K, V> lastEntry() {
        return followRight(root.left);
    }

    /**
     * Traverses down the right path until it finds an uplink.
     */
    TrieEntry<K, V> followRight(TrieEntry<K, V> node) {
        // if Trie is empty, no last entry.
        if (node.right == null) {
            return null;
        }

        // Go as far right as possible, until we encounter an uplink.
        while (node.right.bitIndex > node.bitIndex) {
            node = node.right;
        }

        return node.right;
    }

    /**
     * Returns the node lexicographically before the given node (or null if none).
     *
     * This follows four simple branches:
     *  - If the uplink that returned us was a right uplink:
     *      - If predecessor's left is a valid uplink from predecessor, return it.
     *      - Else, follow the right path from the predecessor's left.
     *  - If the uplink that returned us was a left uplink:
     *      - Loop back through parents until we encounter a node where
     *        node != node.parent.left.
     *          - If node.parent.left is uplink from node.parent:
     *              - If node.parent.left is not root, return it.
     *              - If it is root & root isEmpty, return null.
     *              - If it is root & root !isEmpty, return root.
     *          - If node.parent.left is not uplink from node.parent:
     *              - Follow right path for first right child from node.parent.left
     *
     * @param start  the start entry
     */
    TrieEntry<K, V> previousEntry(final TrieEntry<K, V> start) {
        if (start.predecessor == null) {
            throw new IllegalArgumentException("must have come from somewhere!");
        }

        if (start.predecessor.right == start) {
            if (isValidUplink(start.predecessor.left, start.predecessor)) {
                return start.predecessor.left;
            }
            return followRight(start.predecessor.left);
        }
        TrieEntry<K, V> node = start.predecessor;
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        if (node.parent == null) { // can be null if we're looking up root.
            return null;
        }

        if (isValidUplink(node.parent.left, node.parent)) {
            if (node.parent.left == root) {
                if (root.isEmpty()) {
                    return null;
                }
                return root;

            }
            return node.parent.left;
        }
        return followRight(node.parent.left);
    }

    /**
     * Returns the entry lexicographically after the given entry.
     * If the given entry is null, returns the first node.
     *
     * This will traverse only within the subtree.  If the given node
     * is not within the subtree, this will have undefined results.
     */
    TrieEntry<K, V> nextEntryInSubtree(final TrieEntry<K, V> node,
            final TrieEntry<K, V> parentOfSubtree) {
        if (node == null) {
            return firstEntry();
        }
        return nextEntryImpl(node.predecessor, node, parentOfSubtree);
    }

    /**
     * Returns true if 'next' is a valid uplink coming from 'from'.
     */
    static boolean isValidUplink(final TrieEntry<?, ?> next, final TrieEntry<?, ?> from) {
        return next != null && next.bitIndex <= from.bitIndex && !next.isEmpty();
    }

    /**
     * A {@link Reference} allows us to return something through a Method's
     * argument list. An alternative would be to an Array with a length of
     * one (1) but that leads to compiler warnings. Computationally and memory
     * wise there's no difference (except for the need to load the
     * {@link Reference} Class but that happens only once).
     */
    private static class Reference<E> {

        private E item;

        public void set(final E item) {
            this.item = item;
        }

        public E get() {
            return item;
        }
    }

    /**
     *  A {@link Trie} is a set of {@link TrieEntry} nodes.
     */
    protected static class TrieEntry<K,V> extends BasicEntry<K, V> {

        private static final long serialVersionUID = 4596023148184140013L;

        /** The index this entry is comparing. */
        protected int bitIndex;

        /** The parent of this entry. */
        protected TrieEntry<K,V> parent;

        /** The left child of this entry. */
        protected TrieEntry<K,V> left;

        /** The right child of this entry. */
        protected TrieEntry<K,V> right;

        /** The entry who uplinks to this entry. */
        protected TrieEntry<K,V> predecessor;

        public TrieEntry(final K key, final V value, final int bitIndex) {
            super(key, value);

            this.bitIndex = bitIndex;

            this.parent = null;
            this.left = this;
            this.right = null;
            this.predecessor = this;
        }

        /**
         * Whether or not the entry is storing a key.
         * Only the root can potentially be empty, all other
         * nodes must have a key.
         */
        public boolean isEmpty() {
            return key == null;
        }

        /**
         * Neither the left nor right child is a loopback.
         */
        public boolean isInternalNode() {
            return left != this && right != this;
        }

        /**
         * Either the left or right child is a loopback.
         */
        public boolean isExternalNode() {
            return !isInternalNode();
        }

        @Override
        public String toString() {
            final StringBuilder buffer = new StringBuilder();

            if (bitIndex == -1) {
                buffer.append("RootEntry(");
            } else {
                buffer.append("Entry(");
            }

            buffer.append("key=").append(getKey()).append(" [").append(bitIndex).append("], ");
            buffer.append("value=").append(getValue()).append(", ");
            //buffer.append("bitIndex=").append(bitIndex).append(", ");

            if (parent != null) {
                if (parent.bitIndex == -1) {
                    buffer.append("parent=").append("ROOT");
                } else {
                    buffer.append("parent=").append(parent.getKey()).append(" [").append(parent.bitIndex).append("]");
                }
            } else {
                buffer.append("parent=").append("null");
            }
            buffer.append(", ");

            if (left != null) {
                if (left.bitIndex == -1) {
                    buffer.append("left=").append("ROOT");
                } else {
                    buffer.append("left=").append(left.getKey()).append(" [").append(left.bitIndex).append("]");
                }
            } else {
                buffer.append("left=").append("null");
            }
            buffer.append(", ");

            if (right != null) {
                if (right.bitIndex == -1) {
                    buffer.append("right=").append("ROOT");
                } else {
                    buffer.append("right=").append(right.getKey()).append(" [").append(right.bitIndex).append("]");
                }
            } else {
                buffer.append("right=").append("null");
            }
            buffer.append(", ");

            if (predecessor != null) {
                if(predecessor.bitIndex == -1) {
                    buffer.append("predecessor=").append("ROOT");
                } else {
                    buffer.append("predecessor=").append(predecessor.getKey()).append(" [").
                           append(predecessor.bitIndex).append("]");
                }
            }

            buffer.append(")");
            return buffer.toString();
        }
    }


    /**
     * This is a entry set view of the {@link Trie} as returned by {@link Map#entrySet()}.
     */
    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {

        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            final TrieEntry<K,V> candidate = getEntry(((Map.Entry<?, ?>)o).getKey());
            return candidate != null && candidate.equals(o);
        }

        @Override
        public boolean remove(final Object obj) {
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            if (contains(obj) == false) {
                return false;
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            AbstractPatriciaTrie.this.remove(entry.getKey());
            return true;
        }

        @Override
        public int size() {
            return AbstractPatriciaTrie.this.size();
        }

        @Override
        public void clear() {
            AbstractPatriciaTrie.this.clear();
        }

        /**
         * An {@link Iterator} that returns {@link Entry} Objects.
         */
        private class EntryIterator extends TrieIterator<Map.Entry<K,V>> {
            @Override
            public Map.Entry<K,V> next() {
                return nextEntry();
            }
        }
    }

    /**
     * This is a key set view of the {@link Trie} as returned by {@link Map#keySet()}.
     */
    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return AbstractPatriciaTrie.this.size();
        }

        @Override
        public boolean contains(final Object o) {
            return containsKey(o);
        }

        @Override
        public boolean remove(final Object o) {
            final int size = size();
            AbstractPatriciaTrie.this.remove(o);
            return size != size();
        }

        @Override
        public void clear() {
            AbstractPatriciaTrie.this.clear();
        }

        /**
         * An {@link Iterator} that returns Key Objects.
         */
        private class KeyIterator extends TrieIterator<K> {
            @Override
            public K next() {
                return nextEntry().getKey();
            }
        }
    }

    /**
     * This is a value view of the {@link Trie} as returned by {@link Map#values()}.
     */
    private class Values extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return AbstractPatriciaTrie.this.size();
        }

        @Override
        public boolean contains(final Object o) {
            return containsValue(o);
        }

        @Override
        public void clear() {
            AbstractPatriciaTrie.this.clear();
        }

        @Override
        public boolean remove(final Object o) {
            for (final Iterator<V> it = iterator(); it.hasNext(); ) {
                final V value = it.next();
                if (compare(value, o)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        /**
         * An {@link Iterator} that returns Value Objects.
         */
        private class ValueIterator extends TrieIterator<V> {
            @Override
            public V next() {
                return nextEntry().getValue();
            }
        }
    }

    /**
     * An iterator for the entries.
     */
    abstract class TrieIterator<E> implements Iterator<E> {

        /** For fast-fail. */
        protected int expectedModCount = AbstractPatriciaTrie.this.modCount;

        protected TrieEntry<K, V> next; // the next node to return
        protected TrieEntry<K, V> current; // the current entry we're on

        /**
         * Starts iteration from the root.
         */
        protected TrieIterator() {
            next = AbstractPatriciaTrie.this.nextEntry(null);
        }

        /**
         * Starts iteration at the given entry.
         */
        protected TrieIterator(final TrieEntry<K, V> firstEntry) {
            next = firstEntry;
        }

        /**
         * Returns the next {@link TrieEntry}.
         */
        protected TrieEntry<K,V> nextEntry() {
            if (expectedModCount != AbstractPatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }

            final TrieEntry<K,V> e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }

            next = findNext(e);
            current = e;
            return e;
        }

        /**
         * @see PatriciaTrie#nextEntry(TrieEntry)
         */
        protected TrieEntry<K, V> findNext(final TrieEntry<K, V> prior) {
            return AbstractPatriciaTrie.this.nextEntry(prior);
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            if (expectedModCount != AbstractPatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }

            final TrieEntry<K, V> node = current;
            current = null;
            AbstractPatriciaTrie.this.removeEntry(node);

            expectedModCount = AbstractPatriciaTrie.this.modCount;
        }
    }

    /**
     * An {@link OrderedMapIterator} for a {@link Trie}.
     */
    private class TrieMapIterator extends TrieIterator<K> implements OrderedMapIterator<K, V> {

        protected TrieEntry<K, V> previous; // the previous node to return

        @Override
        public K next() {
            return nextEntry().getKey();
        }

        @Override
        public K getKey() {
            if (current == null) {
                throw new IllegalStateException();
            }
            return current.getKey();
        }

        @Override
        public V getValue() {
            if (current == null) {
                throw new IllegalStateException();
            }
            return current.getValue();
        }

        @Override
        public V setValue(final V value) {
            if (current == null) {
                throw new IllegalStateException();
            }
            return current.setValue(value);
        }

        @Override
        public boolean hasPrevious() {
            return previous != null;
        }

        @Override
        public K previous() {
            return previousEntry().getKey();
        }

        @Override
        protected TrieEntry<K, V> nextEntry() {
            final TrieEntry<K, V> nextEntry = super.nextEntry();
            previous = nextEntry;
            return nextEntry;
        }

        protected TrieEntry<K,V> previousEntry() {
            if (expectedModCount != AbstractPatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }

            final TrieEntry<K,V> e = previous;
            if (e == null) {
                throw new NoSuchElementException();
            }

            previous = AbstractPatriciaTrie.this.previousEntry(e);
            next = current;
            current = e;
            return current;
        }

    }

    /**
     * A range view of the {@link Trie}.
     */
    private abstract class RangeMap extends AbstractMap<K, V>
            implements SortedMap<K, V> {

        /** The {@link #entrySet()} view. */
        private transient volatile Set<Map.Entry<K, V>> entrySet;

        /**
         * Creates and returns an {@link #entrySet()} view of the {@link RangeMap}.
         */
        protected abstract Set<Map.Entry<K, V>> createEntrySet();

        /**
         * Returns the FROM Key.
         */
        protected abstract K getFromKey();

        /**
         * Whether or not the {@link #getFromKey()} is in the range.
         */
        protected abstract boolean isFromInclusive();

        /**
         * Returns the TO Key.
         */
        protected abstract K getToKey();

        /**
         * Whether or not the {@link #getToKey()} is in the range.
         */
        protected abstract boolean isToInclusive();

        @Override
        public Comparator<? super K> comparator() {
            return AbstractPatriciaTrie.this.comparator();
        }

        @Override
        public boolean containsKey(final Object key) {
            if (!inRange(castKey(key))) {
                return false;
            }

            return AbstractPatriciaTrie.this.containsKey(key);
        }

        @Override
        public V remove(final Object key) {
            if (!inRange(castKey(key))) {
                return null;
            }

            return AbstractPatriciaTrie.this.remove(key);
        }

        @Override
        public V get(final Object key) {
            if (!inRange(castKey(key))) {
                return null;
            }

            return AbstractPatriciaTrie.this.get(key);
        }

        @Override
        public V put(final K key, final V value) {
            if (!inRange(key)) {
                throw new IllegalArgumentException("Key is out of range: " + key);
            }
            return AbstractPatriciaTrie.this.put(key, value);
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            if (entrySet == null) {
                entrySet = createEntrySet();
            }
            return entrySet;
        }

        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            if (!inRange2(fromKey)) {
                throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
            }

            if (!inRange2(toKey)) {
                throw new IllegalArgumentException("ToKey is out of range: " + toKey);
            }

            return createRangeMap(fromKey, isFromInclusive(), toKey, isToInclusive());
        }

        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            if (!inRange2(toKey)) {
                throw new IllegalArgumentException("ToKey is out of range: " + toKey);
            }
            return createRangeMap(getFromKey(), isFromInclusive(), toKey, isToInclusive());
        }

        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            if (!inRange2(fromKey)) {
                throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
            }
            return createRangeMap(fromKey, isFromInclusive(), getToKey(), isToInclusive());
        }

        /**
         * Returns true if the provided key is greater than TO and less than FROM.
         */
        protected boolean inRange(final K key) {
            final K fromKey = getFromKey();
            final K toKey = getToKey();

            return (fromKey == null || inFromRange(key, false)) && (toKey == null || inToRange(key, false));
        }

        /**
         * This form allows the high endpoint (as well as all legit keys).
         */
        protected boolean inRange2(final K key) {
            final K fromKey = getFromKey();
            final K toKey = getToKey();

            return (fromKey == null || inFromRange(key, false)) && (toKey == null || inToRange(key, true));
        }

        /**
         * Returns true if the provided key is in the FROM range of the {@link RangeMap}.
         */
        protected boolean inFromRange(final K key, final boolean forceInclusive) {
            final K fromKey = getFromKey();
            final boolean fromInclusive = isFromInclusive();

            final int ret = getKeyAnalyzer().compare(key, fromKey);
            if (fromInclusive || forceInclusive) {
                return ret >= 0;
            }
            return ret > 0;
        }

        /**
         * Returns true if the provided key is in the TO range of the {@link RangeMap}.
         */
        protected boolean inToRange(final K key, final boolean forceInclusive) {
            final K toKey = getToKey();
            final boolean toInclusive = isToInclusive();

            final int ret = getKeyAnalyzer().compare(key, toKey);
            if (toInclusive || forceInclusive) {
                return ret <= 0;
            }
            return ret < 0;
        }

        /**
         * Creates and returns a sub-range view of the current {@link RangeMap}.
         */
        protected abstract SortedMap<K, V> createRangeMap(K fromKey, boolean fromInclusive,
                                                          K toKey, boolean toInclusive);
    }

   /**
    * A {@link RangeMap} that deals with {@link Entry}s.
    */
   private class RangeEntryMap extends RangeMap {

       /** The key to start from, null if the beginning. */
       private final K fromKey;

       /** The key to end at, null if till the end. */
       private final K toKey;

       /** Whether or not the 'from' is inclusive. */
       private final boolean fromInclusive;

       /** Whether or not the 'to' is inclusive. */
       private final boolean toInclusive;

       /**
        * Creates a {@link RangeEntryMap} with the fromKey included and
        * the toKey excluded from the range.
        */
       protected RangeEntryMap(final K fromKey, final K toKey) {
           this(fromKey, true, toKey, false);
       }

       /**
        * Creates a {@link RangeEntryMap}.
        */
       protected RangeEntryMap(final K fromKey, final boolean fromInclusive,
                               final K toKey, final boolean toInclusive) {

           if (fromKey == null && toKey == null) {
               throw new IllegalArgumentException("must have a from or to!");
           }

           if (fromKey != null && toKey != null && getKeyAnalyzer().compare(fromKey, toKey) > 0) {
               throw new IllegalArgumentException("fromKey > toKey");
           }

           this.fromKey = fromKey;
           this.fromInclusive = fromInclusive;
           this.toKey = toKey;
           this.toInclusive = toInclusive;
       }

       @Override
    public K firstKey() {
           Map.Entry<K,V> e = null;
           if (fromKey == null) {
               e = firstEntry();
           } else {
               if (fromInclusive) {
                   e = ceilingEntry(fromKey);
               } else {
                   e = higherEntry(fromKey);
               }
           }

           final K first = e != null ? e.getKey() : null;
           if (e == null || toKey != null && !inToRange(first, false)) {
               throw new NoSuchElementException();
           }
           return first;
       }

       @Override
    public K lastKey() {
           Map.Entry<K,V> e;
           if (toKey == null) {
               e = lastEntry();
           } else {
               if (toInclusive) {
                   e = floorEntry(toKey);
               } else {
                   e = lowerEntry(toKey);
               }
           }

           final K last = e != null ? e.getKey() : null;
           if (e == null || fromKey != null && !inFromRange(last, false)) {
               throw new NoSuchElementException();
           }
           return last;
       }

       @Override
       protected Set<Entry<K, V>> createEntrySet() {
           return new RangeEntrySet(this);
       }

       @Override
       public K getFromKey() {
           return fromKey;
       }

       @Override
       public K getToKey() {
           return toKey;
       }

       @Override
       public boolean isFromInclusive() {
           return fromInclusive;
       }

       @Override
       public boolean isToInclusive() {
           return toInclusive;
       }

       @Override
       protected SortedMap<K, V> createRangeMap(final K fromKey, final boolean fromInclusive,
                                                final K toKey, final boolean toInclusive) {
           return new RangeEntryMap(fromKey, fromInclusive, toKey, toInclusive);
       }
   }

    /**
     * A {@link Set} view of a {@link RangeMap}.
     */
    private class RangeEntrySet extends AbstractSet<Map.Entry<K, V>> {

        private final RangeMap delegate;

        private transient int size = -1;

        private transient int expectedModCount;

        /**
         * Creates a {@link RangeEntrySet}.
         */
        public RangeEntrySet(final RangeMap delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }

            this.delegate = delegate;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final K fromKey = delegate.getFromKey();
            final K toKey = delegate.getToKey();

            TrieEntry<K, V> first = null;
            if (fromKey == null) {
                first = firstEntry();
            } else {
                first = ceilingEntry(fromKey);
            }

            TrieEntry<K, V> last = null;
            if (toKey != null) {
                last = ceilingEntry(toKey);
            }

            return new EntryIterator(first, last);
        }

        @Override
        public int size() {
            if (size == -1 || expectedModCount != AbstractPatriciaTrie.this.modCount) {
                size = 0;

                for (final Iterator<?> it = iterator(); it.hasNext(); it.next()) {
                    ++size;
                }

                expectedModCount = AbstractPatriciaTrie.this.modCount;
            }
            return size;
        }

        @Override
        public boolean isEmpty() {
            return !iterator().hasNext();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            final K key = entry.getKey();
            if (!delegate.inRange(key)) {
                return false;
            }

            final TrieEntry<K, V> node = getEntry(key);
            return node != null && compare(node.getValue(), entry.getValue());
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            final K key = entry.getKey();
            if (!delegate.inRange(key)) {
                return false;
            }

            final TrieEntry<K, V> node = getEntry(key);
            if (node != null && compare(node.getValue(), entry.getValue())) {
                removeEntry(node);
                return true;
            }
            return false;
        }

        /**
         * An {@link Iterator} for {@link RangeEntrySet}s.
         */
        private final class EntryIterator extends TrieIterator<Map.Entry<K,V>> {

            private final K excludedKey;

            /**
             * Creates a {@link EntryIterator}.
             */
            private EntryIterator(final TrieEntry<K,V> first, final TrieEntry<K,V> last) {
                super(first);
                this.excludedKey = last != null ? last.getKey() : null;
            }

            @Override
            public boolean hasNext() {
                return next != null && !compare(next.key, excludedKey);
            }

            @Override
            public Map.Entry<K,V> next() {
                if (next == null || compare(next.key, excludedKey)) {
                    throw new NoSuchElementException();
                }
                return nextEntry();
            }
        }
    }

    /**
     * A submap used for prefix views over the {@link Trie}.
     */
    private class PrefixRangeMap extends RangeMap {

        private final K prefix;

        private final int offsetInBits;

        private final int lengthInBits;

        private K fromKey = null;

        private K toKey = null;

        private transient int expectedModCount = 0;

        private int size = -1;

        /**
         * Creates a {@link PrefixRangeMap}.
         */
        private PrefixRangeMap(final K prefix, final int offsetInBits, final int lengthInBits) {
            this.prefix = prefix;
            this.offsetInBits = offsetInBits;
            this.lengthInBits = lengthInBits;
        }

        /**
         * This method does two things. It determines the FROM
         * and TO range of the {@link PrefixRangeMap} and the number
         * of elements in the range. This method must be called every
         * time the {@link Trie} has changed.
         */
        private int fixup() {
            // The trie has changed since we last found our toKey / fromKey
            if (size == - 1 || AbstractPatriciaTrie.this.modCount != expectedModCount) {
                final Iterator<Map.Entry<K, V>> it = super.entrySet().iterator();
                size = 0;

                Map.Entry<K, V> entry = null;
                if (it.hasNext()) {
                    entry = it.next();
                    size = 1;
                }

                fromKey = entry == null ? null : entry.getKey();
                if (fromKey != null) {
                    final TrieEntry<K, V> prior = previousEntry((TrieEntry<K, V>)entry);
                    fromKey = prior == null ? null : prior.getKey();
                }

                toKey = fromKey;

                while (it.hasNext()) {
                    ++size;
                    entry = it.next();
                }

                toKey = entry == null ? null : entry.getKey();

                if (toKey != null) {
                    entry = nextEntry((TrieEntry<K, V>)entry);
                    toKey = entry == null ? null : entry.getKey();
                }

                expectedModCount = AbstractPatriciaTrie.this.modCount;
            }

            return size;
        }

        @Override
        public K firstKey() {
            fixup();

            Map.Entry<K,V> e = null;
            if (fromKey == null) {
                e = firstEntry();
            } else {
                e = higherEntry(fromKey);
            }

            final K first = e != null ? e.getKey() : null;
            if (e == null || !getKeyAnalyzer().isPrefix(prefix, offsetInBits, lengthInBits, first)) {
                throw new NoSuchElementException();
            }

            return first;
        }

        @Override
        public K lastKey() {
            fixup();

            Map.Entry<K,V> e = null;
            if (toKey == null) {
                e = lastEntry();
            } else {
                e = lowerEntry(toKey);
            }

            final K last = e != null ? e.getKey() : null;
            if (e == null || !getKeyAnalyzer().isPrefix(prefix, offsetInBits, lengthInBits, last)) {
                throw new NoSuchElementException();
            }

            return last;
        }

        /**
         * Returns true if this {@link PrefixRangeMap}'s key is a prefix of the provided key.
         */
        @Override
        protected boolean inRange(final K key) {
            return getKeyAnalyzer().isPrefix(prefix, offsetInBits, lengthInBits, key);
        }

        /**
         * Same as {@link #inRange(Object)}.
         */
        @Override
        protected boolean inRange2(final K key) {
            return inRange(key);
        }

        /**
         * Returns true if the provided Key is in the FROM range of the {@link PrefixRangeMap}.
         */
        @Override
        protected boolean inFromRange(final K key, final boolean forceInclusive) {
            return getKeyAnalyzer().isPrefix(prefix, offsetInBits, lengthInBits, key);
        }

        /**
         * Returns true if the provided Key is in the TO range of the {@link PrefixRangeMap}.
         */
        @Override
        protected boolean inToRange(final K key, final boolean forceInclusive) {
            return getKeyAnalyzer().isPrefix(prefix, offsetInBits, lengthInBits, key);
        }

        @Override
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new PrefixRangeEntrySet(this);
        }

        @Override
        public K getFromKey() {
            return fromKey;
        }

        @Override
        public K getToKey() {
            return toKey;
        }

        @Override
        public boolean isFromInclusive() {
            return false;
        }

        @Override
        public boolean isToInclusive() {
            return false;
        }

        @Override
        protected SortedMap<K, V> createRangeMap(final K fromKey, final boolean fromInclusive,
                                                 final K toKey, final boolean toInclusive) {
            return new RangeEntryMap(fromKey, fromInclusive, toKey, toInclusive);
        }

        @Override
        public void clear() {
            Iterator<Map.Entry<K, V>> it = AbstractPatriciaTrie.this.entrySet().iterator();
            Set<K> currentKeys = keySet();
            while (it.hasNext()) {
                if (currentKeys.contains(it.next().getKey())) {
                    it.remove();
                }
            }
        }
    }

    /**
     * A prefix {@link RangeEntrySet} view of the {@link Trie}.
     */
    private final class PrefixRangeEntrySet extends RangeEntrySet {

        private final PrefixRangeMap delegate;

        private TrieEntry<K, V> prefixStart;

        private int expectedModCount = 0;

        /**
         * Creates a {@link PrefixRangeEntrySet}.
         */
        public PrefixRangeEntrySet(final PrefixRangeMap delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        @Override
        public int size() {
            return delegate.fixup();
        }

        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            if (AbstractPatriciaTrie.this.modCount != expectedModCount) {
                prefixStart = subtree(delegate.prefix, delegate.offsetInBits, delegate.lengthInBits);
                expectedModCount = AbstractPatriciaTrie.this.modCount;
            }

            if (prefixStart == null) {
                final Set<Map.Entry<K,V>> empty = Collections.emptySet();
                return empty.iterator();
            } else if (delegate.lengthInBits > prefixStart.bitIndex) {
                return new SingletonIterator(prefixStart);
            } else {
                return new EntryIterator(prefixStart, delegate.prefix, delegate.offsetInBits, delegate.lengthInBits);
            }
        }

        /**
         * An {@link Iterator} that holds a single {@link TrieEntry}.
         */
        private final class SingletonIterator implements Iterator<Map.Entry<K, V>> {

            private final TrieEntry<K, V> entry;

            private int hit = 0;

            public SingletonIterator(final TrieEntry<K, V> entry) {
                this.entry = entry;
            }

            @Override
            public boolean hasNext() {
                return hit == 0;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (hit != 0) {
                    throw new NoSuchElementException();
                }

                ++hit;
                return entry;
            }

            @Override
            public void remove() {
                if (hit != 1) {
                    throw new IllegalStateException();
                }

                ++hit;
                AbstractPatriciaTrie.this.removeEntry(entry);
            }
        }

        /**
         * An {@link Iterator} for iterating over a prefix search.
         */
        private final class EntryIterator extends TrieIterator<Map.Entry<K, V>> {

            // values to reset the subtree if we remove it.
            private final K prefix;
            private final int offset;
            private final int lengthInBits;
            private boolean lastOne;

            private TrieEntry<K, V> subtree; // the subtree to search within

            /**
             * Starts iteration at the given entry & search only
             * within the given subtree.
             */
            EntryIterator(final TrieEntry<K, V> startScan, final K prefix,
                    final int offset, final int lengthInBits) {
                subtree = startScan;
                next = AbstractPatriciaTrie.this.followLeft(startScan);
                this.prefix = prefix;
                this.offset = offset;
                this.lengthInBits = lengthInBits;
            }

            @Override
            public Map.Entry<K,V> next() {
                final Map.Entry<K, V> entry = nextEntry();
                if (lastOne) {
                    next = null;
                }
                return entry;
            }

            @Override
            protected TrieEntry<K, V> findNext(final TrieEntry<K, V> prior) {
                return AbstractPatriciaTrie.this.nextEntryInSubtree(prior, subtree);
            }

            @Override
            public void remove() {
                // If the current entry we're removing is the subtree
                // then we need to find a new subtree parent.
                boolean needsFixing = false;
                final int bitIdx = subtree.bitIndex;
                if (current == subtree) {
                    needsFixing = true;
                }

                super.remove();

                // If the subtree changed its bitIndex or we
                // removed the old subtree, get a new one.
                if (bitIdx != subtree.bitIndex || needsFixing) {
                    subtree = subtree(prefix, offset, lengthInBits);
                }

                // If the subtree's bitIndex is less than the
                // length of our prefix, it's the last item
                // in the prefix tree.
                if (lengthInBits >= subtree.bitIndex) {
                    lastOne = true;
                }
            }
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Reads the content of the stream.
     */
    @SuppressWarnings("unchecked") // This will fail at runtime if the stream is incorrect
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException{
        stream.defaultReadObject();
        root = new TrieEntry<K, V>(null, null, -1);
        int size = stream.readInt();
        for(int i = 0; i < size; i++){
            K k = (K) stream.readObject();
            V v = (V) stream.readObject();
            put(k, v);
        }
    }

    /**
     * Writes the content to the stream for serialization.
     */
    private void writeObject(final ObjectOutputStream stream) throws IOException{
        stream.defaultWriteObject();
        stream.writeInt(this.size());
        for (final Entry<K, V> entry : entrySet()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }

}

```
