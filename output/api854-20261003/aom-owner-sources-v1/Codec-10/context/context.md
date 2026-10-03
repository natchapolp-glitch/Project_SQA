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
<project name="Codec" default="compile" basedir=".">
    <!--
        "Codec" component of the Apache Commons Subproject
        $Id$
    -->
    <!-- ========== Initialize Properties ===================================== -->
    <property file="${user.home}/${component.name}.build.properties"/>
    <property file="${user.home}/build.properties"/>
    <property file="${basedir}/build.properties"/>
    <property file="${basedir}/default.properties"/>
    <!-- ========== Construct compile classpath =============================== -->
    <path id="compile.classpath">
        <pathelement location="${build.home}/classes"/>
    </path>
    <!-- ========== Construct unit test classpath ============================= -->
    <path id="test.classpath">
        <pathelement location="${build.home}/classes"/>
        <pathelement location="${build.home}/tests"/>
        <pathelement location="${junit.jar}"/>
    </path>
    <!-- ========== Executable Targets ======================================== -->
    <target name="init" description="Initialize and evaluate conditionals">
        <echo message="-------- ${component.name} ${component.version} --------"/>
        <filter token="name" value="${component.name}"/>
        <filter token="package" value="${component.package}"/>
        <filter token="version" value="${component.version}"/>
    </target>
    <target name="prepare" depends="init" description="Prepare build directory">
        <mkdir dir="${build.home}"/>
        <mkdir dir="${build.home}/classes"/>
        <mkdir dir="${build.home}/tests"/>
        <mkdir dir="${build.home}/test-reports"/>
    </target>
    <target name="static" depends="prepare" description="Copy static files to build directory">
        <tstamp/>
    </target>
    <target name="compile" depends="static" description="Compile shareable components">
        <javac srcdir="${source.home}" destdir="${build.home}/classes"
            source="${compile.source}" target="${compile.target}"
            debug="${compile.debug}" deprecation="${compile.deprecation}" optimize="${compile.optimize}">
            <classpath refid="compile.classpath"/>
        </javac>
        <copy todir="${build.home}/classes" filtering="on">
            <fileset dir="${source.home}" excludes="**/*.java"/>
        </copy>
    </target>
    <target name="clean" description="Clean build and distribution directories">
        <delete dir="${build.home}"/>
        <delete dir="${dist.home}"/>
        <delete dir="${pub.home}"/>
    </target>
    <target name="all" depends="clean,compile" description="Clean and compile all components"/>
    <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
        <mkdir dir="${dist.home}"/>
        <mkdir dir="${dist.home}/docs"/>
        <mkdir dir="${dist.home}/docs/api"/>
        <mkdir dir="${build.home}/apidocs"/>
        <tstamp>
            <format property="current.year" pattern="yyyy"/>
        </tstamp>
         <!-- The Sun 1.2 docs are no-longer on-line, point to 1.3. -->
        <javadoc
            sourcepath="${source.home}"
            destdir="${dist.home}/docs/api"
            packagenames="org.apache.commons.*"
            overview="${source.home}/org/apache/commons/codec/overview.html"
            author="true"
            private="true"
            version="true"
            doctitle="&lt;h1&gt;${component.title}&lt;/h1&gt;"
            windowtitle="${component.title} (Version ${component.version})"
            bottom="${component.name} version ${component.version} - Copyright &amp;copy; 2002-${current.year} - Apache Software Foundation"
            use="true"
            link="http://java.sun.com/products/jdk/1.3/docs/api/">
            <classpath refid="compile.classpath"/>
        </javadoc>
    </target>
    <target name="dist" depends="compile,javadoc" description="Create binary distribution">
        <mkdir dir="${dist.home}"/>
        <copy file="${basedir}/LICENSE.txt" todir="${dist.home}"/>
        <copy file="${basedir}/NOTICE.txt" todir="${dist.home}"/>
        <copy file="${basedir}/RELEASE-NOTES.txt" todir="${dist.home}"/>
        <antcall target="jar"/>
    </target>
    <target name="jar" depends="compile" description="Create jar">
        <mkdir dir="${dist.home}"/>
        <mkdir dir="${build.home}/classes/META-INF"/>
        <copy file="${basedir}/LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
        <copy file="${basedir}/LICENSE.txt" tofile="${build.home}/classes/META-INF/NOTICE.txt"/>
        <jar jarfile="${dist.home}/${final.name}.jar" basedir="${build.home}/classes">
            <manifest>
                <attribute name="Specification-Title" value="Commons Codec"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Codec"/>
                <attribute name="Implementation-Version" value="${component.version}"/>
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
                <attribute name="X-Compile-Source-JDK" value="${compile.source}"/>
                <attribute name="X-Compile-Target-JDK" value="${compile.target}"/>
            </manifest>
        </jar>
    </target>
    <target name="install-jar" depends="jar" description="--> Installs jar file in ${lib.repo}">
        <copy todir="${lib.repo}" filtering="no">
            <fileset dir="${dist.home}">
                <include name="${final.name}.jar"/>
            </fileset>
        </copy>
    </target>
    <target name="pub-bin" depends="dist" description="Create binary distribution (compressed) ready for publication">
        <mkdir dir="${pub.home}"/>
        <!-- Binary properties -->
        <property name="final.path" value="${pub.home}/${final.name}"/>
        <property name="zip.path" value="${final.path}.zip"/>
        <property name="tar.path" value="${final.path}.tar"/>
        <property name="gz.path" value="${tar.path}.gz"/>
        <!-- Zip binary dist -->
        <zip destfile="${zip.path}">
           <zipfileset dir="${dist.home}" prefix="${final.name}/"/>
        </zip>
        <checksum algorithm="md5" file="${zip.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${zip.path}" fileext=".sha"/>
        <!-- Tar & gzip binary dist -->
        <tar tarfile="${tar.path}" basedir="${dist.home}"/>
        <gzip zipfile="${gz.path}" src="${tar.path}"/>
        <checksum algorithm="md5" file="${gz.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${gz.path}" fileext=".sha"/>
        <delete file="${tar.path}"/>
        <!-- Delete old signatures -->
        <delete file="${zip.path}.asc"/>
        <delete file="${gz.path}.asc"/>
    </target>
    <target name="pub-src" depends="dist" description="Create source distribution (compressed) ready for publication based on your LOCAL CVS sources">
        <mkdir dir="${pub.home}"/>
        <echo>Warning: The source files used to create this source distribution come from your local copy of the source files.</echo>
        <!-- Source properties -->
        <property name="final-src.path" value="${pub.home}/${final.name}-src"/>
        <property name="zip-src.path" value="${final-src.path}.zip"/>
        <property name="tar-src.path" value="${final-src.path}.tar"/>
        <property name="gz-src.path" value="${tar-src.path}.gz"/>
        <property name="excludes" value="${pub.home}/**, ${dist.home}/**, target/**"/>
        <!-- Zip source dist -->
        <zip destfile="${zip-src.path}">
           <zipfileset dir="src" prefix="${final.name}/src/"/>
           <zipfileset dir="xdocs" prefix="${final.name}/xdocs/"/>
           <zipfileset dir="." includes="build.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="checkstyle.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="default.properties" prefix="${final.name}/"/>
           <zipfileset dir="." includes="LICENSE*.txt" prefix="${final.name}/"/>
           <zipfileset dir="." includes="NOTICE.txt" prefix="${final.name}/"/>
           <zipfileset dir="." includes="pom.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="RELEASE-NOTES*.txt" prefix="${final.name}/"/>
        </zip>
        <checksum algorithm="md5" file="${zip-src.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${zip-src.path}" fileext=".sha"/>
        <!-- Tar & gzip source dist -->
        <tar tarfile="${tar-src.path}" basedir="." excludes="${excludes}"/>
        <gzip zipfile="${gz-src.path}" src="${tar-src.path}"/>
        <checksum algorithm="md5" file="${gz-src.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${gz-src.path}" fileext=".sha"/>
        <delete file="${tar-src.path}"/>
        <!-- Delete old signatures -->
        <delete file="${zip-src.path}.asc"/>
        <delete file="${gz-src.path}.asc"/>
    </target>
    <target name="pub" depends="pub-bin, pub-src" description="Create binary and source distribution (compressed) ready for publication">
    </target>
    <!-- ========== Unit Test Targets ========================================= -->
    <target name="compile.tests" depends="compile" description="Compile unit test cases">
        <javac srcdir="${test.home}" destdir="${build.home}/tests"
            source="${compile.source}" target="${compile.target}"
            debug="${compile.debug}" deprecation="${compile.deprecation}" optimize="${compile.optimize}">
            <classpath refid="test.classpath"/>
        </javac>
        <copy todir="${build.home}/tests" filtering="on">
            <fileset dir="${test.home}" excludes="**/*.java"/>
        </copy>
    </target>
    <!-- Run all the JUnit Tests -->
    <target name="test" depends="compile.tests" description="Compiles and runs unit test cases">
        <record name="${build.home}/test-output.txt" append="no" action="start"/>
        <junit printsummary="yes" haltonfailure="yes">
            <classpath refid="test.classpath"/>
            <formatter type="plain" usefile="true" />
            <!-- If test.entry is defined, run a single test, otherwise run all valid tests -->
            <test name="${test.entry}" todir="${build.home}/test-reports" if="test.entry"/>
            <batchtest fork="yes" todir="${build.home}/test-reports" unless="test.entry">
                <fileset dir="${test.home}">
                    <include name="**/*Test.java"/>
                    <exclude name="**/*AbstractTest.java"/>
                </fileset>
            </batchtest>
        </junit>
        <record name="${build.home}/test-output.txt" action="stop"/>
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
<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>18</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <version>1.5-SNAPSHOT</version>
  <name>Commons Codec</name>

  <inceptionYear>2002</inceptionYear>
    <description>
     The codec package contains simple encoder and decoders for
     various formats such as Base64 and Hexadecimal.  In addition to these
     widely used encoders and decoders, the codec package also maintains a
     collection of phonetic encoding utilities.
    </description>

  <url>http://commons.apache.org/codec/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/CODEC</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/codec/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/codec/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/codec/trunk</url>
  </scm>

  <profiles>
	<!-- 
	  Use maven-site-plugin 2.x with Maven 2.x and maven-site-plugin 3.x with Maven 3.x  
	  See http://maven.apache.org/plugins/maven-site-plugin-3.0-beta-3/maven-3.html 
	-->
    <profile>
      <id>maven-3</id>
      <activation>
        <file>
          <!--  The basedir expression is only recognized by Maven 3.x (see MNG-2363) -->
          <exists>${basedir}</exists>
        </file>
      </activation>
      <build>
        <pluginManagement>
          <plugins>
            <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-site-plugin</artifactId>
              <version>3.0-beta-3</version>
            </plugin>
          </plugins>
        </pluginManagement>
        <plugins>
          <plugin>
            <artifactId>maven-site-plugin</artifactId>
            <executions>
              <execution>
                <id>attach-descriptor</id>
                <goals>
                  <goal>attach-descriptor</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>    
  </profiles>
  
    <developers>
        <developer>
            <name>Henri Yandell</name>
            <id>bayard</id>
            <email>bayard@generationjava.com</email>
        </developer>
        <developer>
            <name>Tim OBrien</name>
            <id>tobrien</id>
            <email>tobrien@apache.org</email>
            <timezone>-6</timezone>
        </developer>
        <developer>
            <name>Scott Sanders</name>
            <id>sanders</id>
            <email>sanders@totalsync.com</email>
        </developer>
        <developer>
            <name>Rodney Waldhoff</name>
            <id>rwaldhoff</id>
            <email>rwaldhoff@apache.org</email>
        </developer>
        <developer>
            <name>Daniel Rall</name>
            <id>dlr</id>
            <email>dlr@finemaltcoding.com</email>
        </developer>
        <developer>
            <name>Jon S. Stevens</name>
            <id>jon</id>
            <email>jon@collab.net</email>
        </developer>
        <developer>
            <name>Gary D. Gregory</name>
            <id>ggregory</id>
            <email>ggregory@apache.org</email>
            <url>http://www.garygregory.com</url>
            <organization>Seagull Software</organization>
            <organizationUrl>http://www.seagullsoftware.com</organizationUrl>
            <timezone>-5</timezone>
        </developer>
        <developer>
            <name>David Graham</name>
            <id>dgraham</id>
            <email>dgraham@apache.org</email>
        </developer>
        <developer>
            <name>Julius Davies</name>
            <id>julius</id>
            <email>julius@apache.org</email>
            <organizationUrl>http://juliusdavies.ca/</organizationUrl>
            <timezone>-8</timezone>
        </developer>
    </developers>
    <contributors>
        <contributor>
            <name>Christopher O'Brien</name>
            <email>siege@preoccupied.net</email>
            <roles>
                <role>hex</role>
                <role>md5</role>
                <role>architecture</role>
            </roles>
        </contributor>
        <contributor>
            <name>Martin Redington</name>
            <roles><role>Representing xml-rpc</role></roles>
        </contributor>
        <contributor>
            <name>Jeffery Dever</name>
            <roles><role>Representing http-client</role></roles>
        </contributor>
        <contributor>
            <name>Steve Zimmermann</name>
            <email>steve.zimmermann@heii.com</email>
            <roles><role>Documentation</role></roles>
        </contributor>
        <contributor>
            <name>Benjamin Walstrum</name>
            <email>ben@walstrum.com</email>
        </contributor>
        <contributor>
            <name>Oleg Kalnichevski</name>
            <email>oleg@ural.ru</email>
            <roles><role>Representing http-client</role></roles>
        </contributor>
        <contributor>
            <name>Dave Dribin</name>
            <email>apache@dave.dribin.org</email>
            <roles><role>DigestUtil</role></roles>
        </contributor>
        <contributor>
            <name>Alex Karasulu</name>
            <email>aok123 at bellsouth.net</email>
            <roles><role>Submitted Binary class and test</role></roles>
        </contributor>
        <contributor>
            <name>Matthew Inger</name>
            <email>mattinger at yahoo.com</email>
            <roles><role>Submitted DIFFERENCE algorithm for Soundex and RefinedSoundex</role></roles>
        </contributor>
        <contributor>
            <name>Jochen Wiedmann</name>
            <email>jochen@apache.org</email>
            <roles><role>Base64 code [CODEC-69]</role></roles>
        </contributor>
        <contributor>
            <name>Sebastian Bazley</name>
            <email>sebb@apache.org</email>
            <roles><role>Streaming Base64</role></roles>
        </contributor>
    </contributors>    

  <!-- Codec should depend on very little -->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <maven.compile.source>1.4</maven.compile.source>
    <maven.compile.target>1.4</maven.compile.target>
    <commons.componentid>codec</commons.componentid>
    <commons.release.version>1.5</commons.release.version>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC1</commons.rc.version>
    <commons.jira.id>CODEC</commons.jira.id>
    <commons.jira.pid>12310464</commons.jira.pid>
    <!-- Ensure copies work OK (can be removed later when this is in parent POM) -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties> 

  <build>
    <sourceDirectory>src/java</sourceDirectory>
    <testSourceDirectory>src/test</testSourceDirectory>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-site-plugin</artifactId>
          <version>2.2</version>
        </plugin>        
      </plugins>
    </pluginManagement>    
    <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
                <include>**/Test*.java</include>
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
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>2.3.1</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-changes-plugin</artifactId>
          <version>2.4</version>
          <configuration>
            <xmlPath>${basedir}/src/changes/changes.xml</xmlPath>
            <issueLinkTemplate>%URL%/%ISSUE%</issueLinkTemplate>
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
          <version>2.6</version>
          <configuration>
            <configLocation>${basedir}/checkstyle.xml</configLocation>
            <enableRulesSummary>false</enableRulesSummary>
            <headerFile>${basedir}/LICENSE-header.txt</headerFile>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>cobertura-maven-plugin</artifactId>
          <version>2.4</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-pmd-plugin</artifactId>
          <version>2.5</version>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>findbugs-maven-plugin</artifactId>
          <version>2.3.1</version>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>clirr-maven-plugin</artifactId>
          <version>2.3</version>
          <configuration>
            <comparisonVersion>1.4</comparisonVersion>
            <minSeverity>info</minSeverity>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
          <version>2.7</version>
          <configuration>
            <linksource>true</linksource>
            <links>
              <link>http://java.sun.com/j2se/1.4.2/docs/api/</link>
            </links>
          </configuration>
        </plugin>
      </plugins>
    </reporting>

</project>

```

## src/java/org/apache/commons/codec/language/Caverphone.java

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

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/**
 * Encodes a string into a Caverphone value.
 * 
 * This is an algorithm created by the Caversham Project at the University of Otago. It implements the Caverphone 2.0
 * algorithm:
 * 
 * @author Apache Software Foundation
 * @version $Id$
 * @see <a href="http://en.wikipedia.org/wiki/Caverphone">Wikipedia - Caverphone</a>
 * @see <a href="http://caversham.otago.ac.nz/files/working/ctp150804.pdf">Caverphone 2.0 specification</a>
 * @since 1.4
 */
public class Caverphone implements StringEncoder {

    /**
     * Creates an instance of the Caverphone encoder
     */
    public Caverphone() {
        super();
    }

    /**
     * Encodes the given String into a Caverphone value.
     *
     * @param txt String the source string
     * @return A caverphone code for the given String
     */
    public String caverphone(String txt) {
        // NOTE: Version 1.0 of Caverphone is easily derivable from this code 
        // by commenting out the 2.0 lines and adding in the 1.0 lines

        if( txt == null || txt.length() == 0 ) {
            return "1111111111";
        }

        // 1. Convert to lowercase
        txt = txt.toLowerCase(java.util.Locale.ENGLISH);

        // 2. Remove anything not A-Z
        txt = txt.replaceAll("[^a-z]", "");

        // 2.5. Remove final e
        txt = txt.replaceAll("e$", "");             // 2.0 only

        // 3. Handle various start options
        txt = txt.replaceAll("^cough", "cou2f");
        txt = txt.replaceAll("^rough", "rou2f");
        txt = txt.replaceAll("^tough", "tou2f");
        txt = txt.replaceAll("^enough", "enou2f");  // 2.0 only
        txt = txt.replaceAll("^trough", "trou2f");  // 2.0 only - note the spec says ^enough here again, c+p error I assume
        txt = txt.replaceAll("^gn", "2n");

        // End 
        txt = txt.replaceAll("mb$", "m2");

        // 4. Handle replacements
        txt = txt.replaceAll("cq", "2q");
        txt = txt.replaceAll("ci", "si");
        txt = txt.replaceAll("ce", "se");
        txt = txt.replaceAll("cy", "sy");
        txt = txt.replaceAll("tch", "2ch");
        txt = txt.replaceAll("c", "k");
        txt = txt.replaceAll("q", "k");
        txt = txt.replaceAll("x", "k");
        txt = txt.replaceAll("v", "f");
        txt = txt.replaceAll("dg", "2g");
        txt = txt.replaceAll("tio", "sio");
        txt = txt.replaceAll("tia", "sia");
        txt = txt.replaceAll("d", "t");
        txt = txt.replaceAll("ph", "fh");
        txt = txt.replaceAll("b", "p");
        txt = txt.replaceAll("sh", "s2");
        txt = txt.replaceAll("z", "s");
        txt = txt.replaceAll("^[aeiou]", "A");
        txt = txt.replaceAll("[aeiou]", "3");
        txt = txt.replaceAll("j", "y");        // 2.0 only
        txt = txt.replaceAll("^y3", "Y3");     // 2.0 only
        txt = txt.replaceAll("^y", "A");       // 2.0 only
        txt = txt.replaceAll("y", "3");        // 2.0 only
        txt = txt.replaceAll("3gh3", "3kh3");
        txt = txt.replaceAll("gh", "22");
        txt = txt.replaceAll("g", "k");
        txt = txt.replaceAll("s+", "S");
        txt = txt.replaceAll("t+", "T");
        txt = txt.replaceAll("p+", "P");
        txt = txt.replaceAll("k+", "K");
        txt = txt.replaceAll("f+", "F");
        txt = txt.replaceAll("m+", "M");
        txt = txt.replaceAll("n+", "N");
        txt = txt.replaceAll("w3", "W3");
        //txt = txt.replaceAll("wy", "Wy");    // 1.0 only
        txt = txt.replaceAll("wh3", "Wh3");
        txt = txt.replaceAll("w$", "3");       // 2.0 only
        //txt = txt.replaceAll("why", "Why");  // 1.0 only
        txt = txt.replaceAll("w", "2");
        txt = txt.replaceAll("^h", "A");
        txt = txt.replaceAll("h", "2");
        txt = txt.replaceAll("r3", "R3");
        txt = txt.replaceAll("r$", "3");       // 2.0 only
        //txt = txt.replaceAll("ry", "Ry");    // 1.0 only
        txt = txt.replaceAll("r", "2");
        txt = txt.replaceAll("l3", "L3");
        txt = txt.replaceAll("l$", "3");       // 2.0 only
        //txt = txt.replaceAll("ly", "Ly");    // 1.0 only
        txt = txt.replaceAll("l", "2");
        //txt = txt.replaceAll("j", "y");      // 1.0 only
        //txt = txt.replaceAll("y3", "Y3");    // 1.0 only
        //txt = txt.replaceAll("y", "2");      // 1.0 only

        // 5. Handle removals
        txt = txt.replaceAll("2", "");
        txt = txt.replaceAll("3$", "A");       // 2.0 only
        txt = txt.replaceAll("3", "");

        // 6. put ten 1s on the end
        txt = txt + "111111" + "1111";        // 1.0 only has 6 1s

        // 7. take the first six characters as the code
        return txt.substring(0, 10);          // 1.0 truncates to 6
    }

    /**
     * Encodes an Object using the caverphone algorithm.  This method
     * is provided in order to satisfy the requirements of the
     * Encoder interface, and will throw an EncoderException if the
     * supplied object is not of type java.lang.String.
     *
     * @param pObject Object to encode
     * @return An object (or type java.lang.String) containing the 
     *         caverphone code which corresponds to the String supplied.
     * @throws EncoderException if the parameter supplied is not
     *                          of type java.lang.String
     */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String"); 
        }
        return caverphone((String) pObject);
    }

    /**
     * Encodes a String using the Caverphone algorithm. 
     *
     * @param pString String object to encode
     * @return The caverphone code corresponding to the String supplied
     */
    public String encode(String pString) {
        return caverphone(pString);   
    }

    /**
     * Tests if the caverphones of two strings are identical.
     *
     * @param str1 First of two strings to compare
     * @param str2 Second of two strings to compare
     * @return <code>true</code> if the caverphones of these strings are identical, 
     *        <code>false</code> otherwise.
     */
    public boolean isCaverphoneEqual(String str1, String str2) {
        return caverphone(str1).equals(caverphone(str2));
    }

}

```
