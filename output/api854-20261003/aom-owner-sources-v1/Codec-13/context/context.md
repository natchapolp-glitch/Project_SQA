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
        <pathelement location="${resources.home}"/>
        <pathelement location="${build.home}/tests"/>
        <pathelement location="${junit.jar}"/>
        <pathelement location="${hamcrest.jar}"/>
<pathelement location="src/main/resources/commons-lang3-3.8.1.jar"/>
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
            debug="${compile.debug}" deprecation="${compile.deprecation}" optimize="${compile.optimize}" 
			includeantruntime="${compile.includeantruntime}"
			encoding="${compile.encoding}">
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
    <target name="all" depends="clean, compile" description="Clean and compile all components"/>
    <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
        <mkdir dir="${dist.home}"/>
        <mkdir dir="${dist.home}/docs"/>
        <mkdir dir="${dist.home}/docs/api"/>
        <mkdir dir="${build.home}/apidocs"/>
        <tstamp>
            <format property="current.year" pattern="yyyy"/>
        </tstamp>
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
            link="http://download.oracle.com/javase/1.5.0/docs/api/"
			encoding="${compile.encoding}">
            <classpath refid="compile.classpath"/>
        </javadoc>
    </target>
    <target name="dist" depends="compile, javadoc" description="Create binary distribution">
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
            <fileset dir="${resources.home}"/>
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
            debug="${compile.debug}" deprecation="${compile.deprecation}" optimize="${compile.optimize}" 
			includeantruntime="${compile.includeantruntime}"
			encoding="${compile.encoding}">
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
<!--
 To produce reports, use the reporting profile, for example: mvn -Preporting clean site
 You may need to use the -U option to update your environment if you get an error.  
 -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>33</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <!-- Remember to update the version in default.properties as well -->
  <version>1.10-SNAPSHOT</version>
  <name>Apache Commons Codec</name>
  <inceptionYear>2002</inceptionYear>
  <description>
     The Apache Commons Codec package contains simple encoder and decoders for
     various formats such as Base64 and Hexadecimal.  In addition to these
     widely used encoders and decoders, the codec package also maintains a
     collection of phonetic encoding utilities.
  </description>
  <prerequisites>
    <maven>3.0.0</maven>
  </prerequisites>  
  <url>http://commons.apache.org/proper/commons-codec/</url>
  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/CODEC</url>
  </issueManagement>
  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/codec/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/codec/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/codec/trunk</url>
  </scm>
  <distributionManagement>
    <site>
      <id>stagingSite</id>
      <name>Apache Staging Website</name>
      <url>${commons.deployment.protocol}://people.apache.org/www/commons.apache.org/${commons.componentid}/</url>
    </site>
  </distributionManagement>
  <developers>
    <developer>
      <name>Henri Yandell</name>
      <id>bayard</id>
      <email>bayard@apache.org</email>
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
      <name>Gary Gregory</name>
      <id>ggregory</id>
      <email>ggregory@apache.org</email>
      <url>http://www.garygregory.com</url>
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
    <developer>
      <name>Thomas Neidhart</name>
      <id>tn</id>
      <email>tn@apache.org</email>
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
      <roles>
        <role>Representing xml-rpc</role>
      </roles>
    </contributor>
    <contributor>
      <name>Jeffery Dever</name>
      <roles>
        <role>Representing http-client</role>
      </roles>
    </contributor>
    <contributor>
      <name>Steve Zimmermann</name>
      <email>steve.zimmermann@heii.com</email>
      <roles>
        <role>Documentation</role>
      </roles>
    </contributor>
    <contributor>
      <name>Benjamin Walstrum</name>
      <email>ben@walstrum.com</email>
    </contributor>
    <contributor>
      <name>Oleg Kalnichevski</name>
      <email>oleg@ural.ru</email>
      <roles>
        <role>Representing http-client</role>
      </roles>
    </contributor>
    <contributor>
      <name>Dave Dribin</name>
      <email>apache@dave.dribin.org</email>
      <roles>
        <role>DigestUtil</role>
      </roles>
    </contributor>
    <contributor>
      <name>Alex Karasulu</name>
      <email>aok123 at bellsouth.net</email>
      <roles>
        <role>Submitted Binary class and test</role>
      </roles>
    </contributor>
    <contributor>
      <name>Matthew Inger</name>
      <email>mattinger at yahoo.com</email>
      <roles>
        <role>Submitted DIFFERENCE algorithm for Soundex and RefinedSoundex</role>
      </roles>
    </contributor>
    <contributor>
      <name>Jochen Wiedmann</name>
      <email>jochen@apache.org</email>
      <roles>
        <role>Base64 code [CODEC-69]</role>
      </roles>
    </contributor>
    <contributor>
      <name>Sebastian Bazley</name>
      <email>sebb@apache.org</email>
      <roles>
        <role>Streaming Base64</role>
      </roles>
    </contributor>
    <contributor>
      <name>Matthew Pocock</name>
      <email>turingatemyhamster@gmail.com</email>
      <roles>
        <role>Beider-Morse phonetic matching</role>
      </roles>
    </contributor>
    <contributor>
      <name>Colm Rice</name>
      <email>colm_rice at hotmail dot com</email>
      <roles>
        <role>Submitted Match Rating Approach (MRA) phonetic encoder and tests [CODEC-161]</role>
      </roles>
    </contributor>
  </contributors>
  <!-- Codec should depend on very little -->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <properties>
    <maven.compiler.source>1.6</maven.compiler.source>
    <maven.compiler.target>1.6</maven.compiler.target>
    <commons.componentid>codec</commons.componentid>
    <commons.release.version>1.10</commons.release.version>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC1</commons.rc.version>
    <commons.jira.id>CODEC</commons.jira.id>
    <commons.jira.pid>12310464</commons.jira.pid>
    <!-- Ensure copies work OK (can be removed later when this is in parent POM) -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <commons.encoding>UTF-8</commons.encoding>
    <checkstyle.header.file>${basedir}/LICENSE-header.txt</checkstyle.header.file>
  </properties>
  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-scm-publish-plugin</artifactId>
          <configuration>
            <ignorePathsToDelete>
              <ignorePathToDelete>archive**</ignorePathToDelete>
            </ignorePathsToDelete>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <excludes>
            <exclude>**/*AbstractTest.java</exclude>
            <exclude>**/*PerformanceTest.java</exclude>
          </excludes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.4</version>
        <executions>
          <execution>
            <goals>
              <goal>test-jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <descriptors>
            <descriptor>src/main/assembly/bin.xml</descriptor>
            <descriptor>src/main/assembly/src.xml</descriptor>
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
        <artifactId>maven-checkstyle-plugin</artifactId>
        <!-- Use 2.9.1 instead of 2.10; 2.10 seems to scan the 'target' 
        dir and wants license headers in .properties to be the header for 
        Java files. -->
        <version>2.9.1</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
          <headerFile>${basedir}/LICENSE-header.txt</headerFile>
        </configuration>
        <!-- We need to specify reportSets because 2.9.1 creates two reports -->
        <reportSets>
          <reportSet>
            <reports>
              <report>checkstyle</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.1</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>
          <linkXref>true</linkXref>
          <rulesets>
            <ruleset>${basedir}/pmd.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.5.3</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>NOPMD</tag>
            <tag>NOTE</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>javancss-maven-plugin</artifactId>
        <version>2.0</version>
      </plugin>
    </plugins>
  </reporting>
</project>

```

## src/main/java/org/apache/commons/codec/binary/CharSequenceUtils.java

```
package org.apache.commons.codec.binary;

/**
 * <p>
 * Operations on {@link CharSequence} that are {@code null} safe.
 * </p>
 * <p>
 * Copied from Apache Commons Lang r1586295 on April 10, 2014 (day of 3.3.2 release).
 * </p>
 * 
 * @see CharSequence
 * @since 1.10
 */
public class CharSequenceUtils {

    /**
     * Green implementation of regionMatches.
     * 
     * @param cs
     *            the {@code CharSequence} to be processed
     * @param ignoreCase
     *            whether or not to be case insensitive
     * @param thisStart
     *            the index to start on the {@code cs} CharSequence
     * @param substring
     *            the {@code CharSequence} to be looked for
     * @param start
     *            the index to start on the {@code substring} CharSequence
     * @param length
     *            character length of the region
     * @return whether the region matched
     */
    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
            final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        while (tmpLen-- > 0) {
            char c1 = cs.charAt(index1++);
            char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The same check as in String.regionMatches():
            if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
                    && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }

        return true;
    }
}

```

## src/main/java/org/apache/commons/codec/binary/StringUtils.java

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

package org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

/**
 * Converts String to and from bytes using the encodings required by the Java specification. These encodings are
 * specified in <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">
 * Standard charsets</a>.
 *
 * <p>This class is immutable and thread-safe.</p>
 *
 * @see CharEncoding
 * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
 * @version $Id$
 * @since 1.4
 */
public class StringUtils {

    /**
     * <p>
     * Compares two CharSequences, returning {@code true} if they represent equal sequences of characters.
     * </p>
     * 
     * <p>
     * {@code null}s are handled without exceptions. Two {@code null} references are considered to be equal. The
     * comparison is case sensitive.
     * </p>
     * 
     * <pre>
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * </pre>
     * 
     * <p>
     * Copied from Apache Commons Lang r1583482 on April 10, 2014 (day of 3.3.2 release).
     * </p>
     * 
     * @see Object#equals(Object)
     * @param cs1
     *            the first CharSequence, may be {@code null}
     * @param cs2
     *            the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-sensitive), or both {@code null}
     * @since 1.10
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, Math.max(cs1.length(), cs2.length()));
    }

    /**
     * Calls {@link String#getBytes(Charset)}
     *
     * @param string
     *            The string to encode (if null, return null).
     * @param charset
     *            The {@link Charset} to encode the {@code String}
     * @return the encoded bytes
     */
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    /**
     * Encodes the given string into a sequence of bytes using the ISO-8859-1 charset, storing the result into a new
     * byte array.
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException
     *             Thrown if {@link Charsets#ISO_8859_1} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesIso8859_1(final String string) {
        return getBytes(string, Charsets.ISO_8859_1);
    }


    /**
     * Encodes the given string into a sequence of bytes using the named charset, storing the result into a new byte
     * array.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and rethrows it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#getBytes(String)
     */
    public static byte[] getBytesUnchecked(final String string, final String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (final UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    /**
     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
     * array.
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException
     *             Thrown if {@link Charsets#US_ASCII} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUsAscii(final String string) {
        return getBytes(string, Charsets.US_ASCII);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16 charset, storing the result into a new byte
     * array.
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_16} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16(final String string) {
        return getBytes(string, Charsets.UTF_16);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16BE charset, storing the result into a new byte
     * array.
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_16BE} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Be(final String string) {
        return getBytes(string, Charsets.UTF_16BE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-16LE charset, storing the result into a new byte
     * array.
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_16LE} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf16Le(final String string) {
        return getBytes(string, Charsets.UTF_16LE);
    }

    /**
     * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result into a new byte
     * array.
     *
     * @param string
     *            the String to encode, may be {@code null}
     * @return encoded bytes, or {@code null} if the input string was {@code null}
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_8} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @see #getBytesUnchecked(String, String)
     */
    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, Charsets.UTF_8);
    }

    private static IllegalStateException newIllegalStateException(final String charsetName,
                                                                  final UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @param charset
     *            The {@link Charset} to encode the {@code String}
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_8} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     */
    private static String newString(final byte[] bytes, final Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
     * <p>
     * This method catches {@link UnsupportedEncodingException} and re-throws it as {@link IllegalStateException}, which
     * should never happen for a required charset name. Use this method when the encoding is required to be in the JRE.
     * </p>
     *
     * @param bytes
     *            The bytes to be decoded into characters, may be {@code null}
     * @param charsetName
     *            The name of a required {@link java.nio.charset.Charset}
     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws IllegalStateException
     *             Thrown when a {@link UnsupportedEncodingException} is caught, which should never happen for a
     *             required charset name.
     * @see CharEncoding
     * @see String#String(byte[], String)
     */
    public static String newString(final byte[] bytes, final String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (final UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the ISO-8859-1 charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters, may be {@code null}
     * @return A new <code>String</code> decoded from the specified array of bytes using the ISO-8859-1 charset, or
     *         {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#ISO_8859_1} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringIso8859_1(final byte[] bytes) {
        return new String(bytes, Charsets.ISO_8859_1);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the US-ASCII charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#US_ASCII} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUsAscii(final byte[] bytes) {
        return new String(bytes, Charsets.US_ASCII);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16 charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the UTF-16 charset
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_16} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUtf16(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_16);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16BE charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the UTF-16BE charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_16BE} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUtf16Be(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_16BE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-16LE charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the UTF-16LE charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_16LE} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUtf16Le(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_16LE);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8 charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the UTF-8 charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if {@link Charsets#UTF_8} is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUtf8(final byte[] bytes) {
        return newString(bytes, Charsets.UTF_8);
    }

}

```

## src/main/java/org/apache/commons/codec/language/DoubleMetaphone.java

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
import org.apache.commons.codec.binary.StringUtils;

/**
 * Encodes a string into a double metaphone value. This Implementation is based on the algorithm by <CITE>Lawrence
 * Philips</CITE>.
 * <p>
 * This class is conditionally thread-safe. The instance field {@link #maxCodeLen} is mutable
 * {@link #setMaxCodeLen(int)} but is not volatile, and accesses are not synchronized. If an instance of the class is
 * shared between threads, the caller needs to ensure that suitable synchronization is used to ensure safe publication
 * of the value between threads, and must not invoke {@link #setMaxCodeLen(int)} after initial setup.
 *
 * @see <a href="http://drdobbs.com/184401251?pgno=2">Original Article</a>
 * @see <a href="http://en.wikipedia.org/wiki/Metaphone">http://en.wikipedia.org/wiki/Metaphone</a>
 *
 * @version $Id$
 */
public class DoubleMetaphone implements StringEncoder {

    /**
     * "Vowels" to test for
     */
    private static final String VOWELS = "AEIOUY";

    /**
     * Prefixes when present which are not pronounced
     */
    private static final String[] SILENT_START =
        { "GN", "KN", "PN", "WR", "PS" };
    private static final String[] L_R_N_M_B_H_F_V_W_SPACE =
        { "L", "R", "N", "M", "B", "H", "F", "V", "W", " " };
    private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER =
        { "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER" };
    private static final String[] L_T_K_S_N_M_B_Z =
        { "L", "T", "K", "S", "N", "M", "B", "Z" };

    /**
     * Maximum length of an encoding, default is 4
     */
    private int maxCodeLen = 4;

    /**
     * Creates an instance of this DoubleMetaphone encoder
     */
    public DoubleMetaphone() {
        super();
    }

    /**
     * Encode a value with Double Metaphone.
     *
     * @param value String to encode
     * @return an encoded string
     */
    public String doubleMetaphone(final String value) {
        return doubleMetaphone(value, false);
    }

    /**
     * Encode a value with Double Metaphone, optionally using the alternate encoding.
     *
     * @param value String to encode
     * @param alternate use alternate encode
     * @return an encoded string
     */
    public String doubleMetaphone(String value, final boolean alternate) {
        value = cleanInput(value);
        if (value == null) {
            return null;
        }

        final boolean slavoGermanic = isSlavoGermanic(value);
        int index = isSilentStart(value) ? 1 : 0;

        final DoubleMetaphoneResult result = new DoubleMetaphoneResult(this.getMaxCodeLen());

        while (!result.isComplete() && index <= value.length() - 1) {
            switch (value.charAt(index)) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'Y':
                index = handleAEIOUY(result, index);
                break;
            case 'B':
                result.append('P');
                index = charAt(value, index + 1) == 'B' ? index + 2 : index + 1;
                break;
            case '\u00C7':
                // A C with a Cedilla
                result.append('S');
                index++;
                break;
            case 'C':
                index = handleC(value, result, index);
                break;
            case 'D':
                index = handleD(value, result, index);
                break;
            case 'F':
                result.append('F');
                index = charAt(value, index + 1) == 'F' ? index + 2 : index + 1;
                break;
            case 'G':
                index = handleG(value, result, index, slavoGermanic);
                break;
            case 'H':
                index = handleH(value, result, index);
                break;
            case 'J':
                index = handleJ(value, result, index, slavoGermanic);
                break;
            case 'K':
                result.append('K');
                index = charAt(value, index + 1) == 'K' ? index + 2 : index + 1;
                break;
            case 'L':
                index = handleL(value, result, index);
                break;
            case 'M':
                result.append('M');
                index = conditionM0(value, index) ? index + 2 : index + 1;
                break;
            case 'N':
                result.append('N');
                index = charAt(value, index + 1) == 'N' ? index + 2 : index + 1;
                break;
            case '\u00D1':
                // N with a tilde (spanish ene)
                result.append('N');
                index++;
                break;
            case 'P':
                index = handleP(value, result, index);
                break;
            case 'Q':
                result.append('K');
                index = charAt(value, index + 1) == 'Q' ? index + 2 : index + 1;
                break;
            case 'R':
                index = handleR(value, result, index, slavoGermanic);
                break;
            case 'S':
                index = handleS(value, result, index, slavoGermanic);
                break;
            case 'T':
                index = handleT(value, result, index);
                break;
            case 'V':
                result.append('F');
                index = charAt(value, index + 1) == 'V' ? index + 2 : index + 1;
                break;
            case 'W':
                index = handleW(value, result, index);
                break;
            case 'X':
                index = handleX(value, result, index);
                break;
            case 'Z':
                index = handleZ(value, result, index, slavoGermanic);
                break;
            default:
                index++;
                break;
            }
        }

        return alternate ? result.getAlternate() : result.getPrimary();
    }

    /**
     * Encode the value using DoubleMetaphone.  It will only work if
     * <code>obj</code> is a <code>String</code> (like <code>Metaphone</code>).
     *
     * @param obj Object to encode (should be of type String)
     * @return An encoded Object (will be of type String)
     * @throws EncoderException encode parameter is not of type String
     */
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
        }
        return doubleMetaphone((String) obj);
    }

    /**
     * Encode the value using DoubleMetaphone.
     *
     * @param value String to encode
     * @return An encoded String
     */
    @Override
    public String encode(final String value) {
        return doubleMetaphone(value);
    }

    /**
     * Check if the Double Metaphone values of two <code>String</code> values
     * are equal.
     *
     * @param value1 The left-hand side of the encoded {@link String#equals(Object)}.
     * @param value2 The right-hand side of the encoded {@link String#equals(Object)}.
     * @return {@code true} if the encoded <code>String</code>s are equal;
     *          {@code false} otherwise.
     * @see #isDoubleMetaphoneEqual(String,String,boolean)
     */
    public boolean isDoubleMetaphoneEqual(final String value1, final String value2) {
        return isDoubleMetaphoneEqual(value1, value2, false);
    }

    /**
     * Check if the Double Metaphone values of two <code>String</code> values
     * are equal, optionally using the alternate value.
     *
     * @param value1 The left-hand side of the encoded {@link String#equals(Object)}.
     * @param value2 The right-hand side of the encoded {@link String#equals(Object)}.
     * @param alternate use the alternate value if {@code true}.
     * @return {@code true} if the encoded <code>String</code>s are equal;
     *          {@code false} otherwise.
     */
    public boolean isDoubleMetaphoneEqual(final String value1, final String value2, final boolean alternate) {
        return StringUtils.equals(doubleMetaphone(value1, alternate), doubleMetaphone(value2, alternate));
    }

    /**
     * Returns the maxCodeLen.
     * @return int
     */
    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    /**
     * Sets the maxCodeLen.
     * @param maxCodeLen The maxCodeLen to set
     */
    public void setMaxCodeLen(final int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }

    //-- BEGIN HANDLERS --//

    /**
     * Handles 'A', 'E', 'I', 'O', 'U', and 'Y' cases.
     */
    private int handleAEIOUY(final DoubleMetaphoneResult result, final int index) {
        if (index == 0) {
            result.append('A');
        }
        return index + 1;
    }

    /**
     * Handles 'C' cases.
     */
    private int handleC(final String value, final DoubleMetaphoneResult result, int index) {
        if (conditionC0(value, index)) {  // very confusing, moved out
            result.append('K');
            index += 2;
        } else if (index == 0 && contains(value, index, 6, "CAESAR")) {
            result.append('S');
            index += 2;
        } else if (contains(value, index, 2, "CH")) {
            index = handleCH(value, result, index);
        } else if (contains(value, index, 2, "CZ") &&
                   !contains(value, index - 2, 4, "WICZ")) {
            //-- "Czerny" --//
            result.append('S', 'X');
            index += 2;
        } else if (contains(value, index + 1, 3, "CIA")) {
            //-- "focaccia" --//
            result.append('X');
            index += 3;
        } else if (contains(value, index, 2, "CC") &&
                   !(index == 1 && charAt(value, 0) == 'M')) {
            //-- double "cc" but not "McClelland" --//
            return handleCC(value, result, index);
        } else if (contains(value, index, 2, "CK", "CG", "CQ")) {
            result.append('K');
            index += 2;
        } else if (contains(value, index, 2, "CI", "CE", "CY")) {
            //-- Italian vs. English --//
            if (contains(value, index, 3, "CIO", "CIE", "CIA")) {
                result.append('S', 'X');
            } else {
                result.append('S');
            }
            index += 2;
        } else {
            result.append('K');
            if (contains(value, index + 1, 2, " C", " Q", " G")) {
                //-- Mac Caffrey, Mac Gregor --//
                index += 3;
            } else if (contains(value, index + 1, 1, "C", "K", "Q") &&
                       !contains(value, index + 1, 2, "CE", "CI")) {
                index += 2;
            } else {
                index++;
            }
        }

        return index;
    }

    /**
     * Handles 'CC' cases.
     */
    private int handleCC(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index + 2, 1, "I", "E", "H") &&
            !contains(value, index + 2, 2, "HU")) {
            //-- "bellocchio" but not "bacchus" --//
            if ((index == 1 && charAt(value, index - 1) == 'A') ||
                contains(value, index - 1, 5, "UCCEE", "UCCES")) {
                //-- "accident", "accede", "succeed" --//
                result.append("KS");
            } else {
                //-- "bacci", "bertucci", other Italian --//
                result.append('X');
            }
            index += 3;
        } else {    // Pierce's rule
            result.append('K');
            index += 2;
        }

        return index;
    }

    /**
     * Handles 'CH' cases.
     */
    private int handleCH(final String value, final DoubleMetaphoneResult result, final int index) {
        if (index > 0 && contains(value, index, 4, "CHAE")) {   // Michael
            result.append('K', 'X');
            return index + 2;
        } else if (conditionCH0(value, index)) {
            //-- Greek roots ("chemistry", "chorus", etc.) --//
            result.append('K');
            return index + 2;
        } else if (conditionCH1(value, index)) {
            //-- Germanic, Greek, or otherwise 'ch' for 'kh' sound --//
            result.append('K');
            return index + 2;
        } else {
            if (index > 0) {
                if (contains(value, 0, 2, "MC")) {
                    result.append('K');
                } else {
                    result.append('X', 'K');
                }
            } else {
                result.append('X');
            }
            return index + 2;
        }
    }

    /**
     * Handles 'D' cases.
     */
    private int handleD(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 2, "DG")) {
            //-- "Edge" --//
            if (contains(value, index + 2, 1, "I", "E", "Y")) {
                result.append('J');
                index += 3;
                //-- "Edgar" --//
            } else {
                result.append("TK");
                index += 2;
            }
        } else if (contains(value, index, 2, "DT", "DD")) {
            result.append('T');
            index += 2;
        } else {
            result.append('T');
            index++;
        }
        return index;
    }

    /**
     * Handles 'G' cases.
     */
    private int handleG(final String value, final DoubleMetaphoneResult result, int index,
                        final boolean slavoGermanic) {
        if (charAt(value, index + 1) == 'H') {
            index = handleGH(value, result, index);
        } else if (charAt(value, index + 1) == 'N') {
            if (index == 1 && isVowel(charAt(value, 0)) && !slavoGermanic) {
                result.append("KN", "N");
            } else if (!contains(value, index + 2, 2, "EY") &&
                       charAt(value, index + 1) != 'Y' && !slavoGermanic) {
                result.append("N", "KN");
            } else {
                result.append("KN");
            }
            index = index + 2;
        } else if (contains(value, index + 1, 2, "LI") && !slavoGermanic) {
            result.append("KL", "L");
            index += 2;
        } else if (index == 0 &&
                   (charAt(value, index + 1) == 'Y' ||
                    contains(value, index + 1, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
            //-- -ges-, -gep-, -gel-, -gie- at beginning --//
            result.append('K', 'J');
            index += 2;
        } else if ((contains(value, index + 1, 2, "ER") ||
                    charAt(value, index + 1) == 'Y') &&
                   !contains(value, 0, 6, "DANGER", "RANGER", "MANGER") &&
                   !contains(value, index - 1, 1, "E", "I") &&
                   !contains(value, index - 1, 3, "RGY", "OGY")) {
            //-- -ger-, -gy- --//
            result.append('K', 'J');
            index += 2;
        } else if (contains(value, index + 1, 1, "E", "I", "Y") ||
                   contains(value, index - 1, 4, "AGGI", "OGGI")) {
            //-- Italian "biaggi" --//
            if (contains(value, 0 ,4, "VAN ", "VON ") ||
                contains(value, 0, 3, "SCH") ||
                contains(value, index + 1, 2, "ET")) {
                //-- obvious germanic --//
                result.append('K');
            } else if (contains(value, index + 1, 3, "IER")) {
                result.append('J');
            } else {
                result.append('J', 'K');
            }
            index += 2;
        } else if (charAt(value, index + 1) == 'G') {
            index += 2;
            result.append('K');
        } else {
            index++;
            result.append('K');
        }
        return index;
    }

    /**
     * Handles 'GH' cases.
     */
    private int handleGH(final String value, final DoubleMetaphoneResult result, int index) {
        if (index > 0 && !isVowel(charAt(value, index - 1))) {
            result.append('K');
            index += 2;
        } else if (index == 0) {
            if (charAt(value, index + 2) == 'I') {
                result.append('J');
            } else {
                result.append('K');
            }
            index += 2;
        } else if ((index > 1 && contains(value, index - 2, 1, "B", "H", "D")) ||
                   (index > 2 && contains(value, index - 3, 1, "B", "H", "D")) ||
                   (index > 3 && contains(value, index - 4, 1, "B", "H"))) {
            //-- Parker's rule (with some further refinements) - "hugh"
            index += 2;
        } else {
            if (index > 2 && charAt(value, index - 1) == 'U' &&
                contains(value, index - 3, 1, "C", "G", "L", "R", "T")) {
                //-- "laugh", "McLaughlin", "cough", "gough", "rough", "tough"
                result.append('F');
            } else if (index > 0 && charAt(value, index - 1) != 'I') {
                result.append('K');
            }
            index += 2;
        }
        return index;
    }

    /**
     * Handles 'H' cases.
     */
    private int handleH(final String value, final DoubleMetaphoneResult result, int index) {
        //-- only keep if first & before vowel or between 2 vowels --//
        if ((index == 0 || isVowel(charAt(value, index - 1))) &&
            isVowel(charAt(value, index + 1))) {
            result.append('H');
            index += 2;
            //-- also takes car of "HH" --//
        } else {
            index++;
        }
        return index;
    }

    /**
     * Handles 'J' cases.
     */
    private int handleJ(final String value, final DoubleMetaphoneResult result, int index,
                        final boolean slavoGermanic) {
        if (contains(value, index, 4, "JOSE") || contains(value, 0, 4, "SAN ")) {
                //-- obvious Spanish, "Jose", "San Jacinto" --//
                if ((index == 0 && (charAt(value, index + 4) == ' ') ||
                     value.length() == 4) || contains(value, 0, 4, "SAN ")) {
                    result.append('H');
                } else {
                    result.append('J', 'H');
                }
                index++;
            } else {
                if (index == 0 && !contains(value, index, 4, "JOSE")) {
                    result.append('J', 'A');
                } else if (isVowel(charAt(value, index - 1)) && !slavoGermanic &&
                           (charAt(value, index + 1) == 'A' || charAt(value, index + 1) == 'O')) {
                    result.append('J', 'H');
                } else if (index == value.length() - 1) {
                    result.append('J', ' ');
                } else if (!contains(value, index + 1, 1, L_T_K_S_N_M_B_Z) &&
                           !contains(value, index - 1, 1, "S", "K", "L")) {
                    result.append('J');
                }

                if (charAt(value, index + 1) == 'J') {
                    index += 2;
                } else {
                    index++;
                }
            }
        return index;
    }

    /**
     * Handles 'L' cases.
     */
    private int handleL(final String value, final DoubleMetaphoneResult result, int index) {
        if (charAt(value, index + 1) == 'L') {
            if (conditionL0(value, index)) {
                result.appendPrimary('L');
            } else {
                result.append('L');
            }
            index += 2;
        } else {
            index++;
            result.append('L');
        }
        return index;
    }

    /**
     * Handles 'P' cases.
     */
    private int handleP(final String value, final DoubleMetaphoneResult result, int index) {
        if (charAt(value, index + 1) == 'H') {
            result.append('F');
            index += 2;
        } else {
            result.append('P');
            index = contains(value, index + 1, 1, "P", "B") ? index + 2 : index + 1;
        }
        return index;
    }

    /**
     * Handles 'R' cases.
     */
    private int handleR(final String value, final DoubleMetaphoneResult result, final int index,
                        final boolean slavoGermanic) {
        if (index == value.length() - 1 && !slavoGermanic &&
            contains(value, index - 2, 2, "IE") &&
            !contains(value, index - 4, 2, "ME", "MA")) {
            result.appendAlternate('R');
        } else {
            result.append('R');
        }
        return charAt(value, index + 1) == 'R' ? index + 2 : index + 1;
    }

    /**
     * Handles 'S' cases.
     */
    private int handleS(final String value, final DoubleMetaphoneResult result, int index,
                        final boolean slavoGermanic) {
        if (contains(value, index - 1, 3, "ISL", "YSL")) {
            //-- special cases "island", "isle", "carlisle", "carlysle" --//
            index++;
        } else if (index == 0 && contains(value, index, 5, "SUGAR")) {
            //-- special case "sugar-" --//
            result.append('X', 'S');
            index++;
        } else if (contains(value, index, 2, "SH")) {
            if (contains(value, index + 1, 4, "HEIM", "HOEK", "HOLM", "HOLZ")) {
                //-- germanic --//
                result.append('S');
            } else {
                result.append('X');
            }
            index += 2;
        } else if (contains(value, index, 3, "SIO", "SIA") || contains(value, index, 4, "SIAN")) {
            //-- Italian and Armenian --//
            if (slavoGermanic) {
                result.append('S');
            } else {
                result.append('S', 'X');
            }
            index += 3;
        } else if ((index == 0 && contains(value, index + 1, 1, "M", "N", "L", "W")) ||
                   contains(value, index + 1, 1, "Z")) {
            //-- german & anglicisations, e.g. "smith" match "schmidt" //
            // "snider" match "schneider" --//
            //-- also, -sz- in slavic language although in hungarian it //
            //   is pronounced "s" --//
            result.append('S', 'X');
            index = contains(value, index + 1, 1, "Z") ? index + 2 : index + 1;
        } else if (contains(value, index, 2, "SC")) {
            index = handleSC(value, result, index);
        } else {
            if (index == value.length() - 1 && contains(value, index - 2, 2, "AI", "OI")) {
                //-- french e.g. "resnais", "artois" --//
                result.appendAlternate('S');
            } else {
                result.append('S');
            }
            index = contains(value, index + 1, 1, "S", "Z") ? index + 2 : index + 1;
        }
        return index;
    }

    /**
     * Handles 'SC' cases.
     */
    private int handleSC(final String value, final DoubleMetaphoneResult result, final int index) {
        if (charAt(value, index + 2) == 'H') {
            //-- Schlesinger's rule --//
            if (contains(value, index + 3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
                //-- Dutch origin, e.g. "school", "schooner" --//
                if (contains(value, index + 3, 2, "ER", "EN")) {
                    //-- "schermerhorn", "schenker" --//
                    result.append("X", "SK");
                } else {
                    result.append("SK");
                }
            } else {
                if (index == 0 && !isVowel(charAt(value, 3)) && charAt(value, 3) != 'W') {
                    result.append('X', 'S');
                } else {
                    result.append('X');
                }
            }
        } else if (contains(value, index + 2, 1, "I", "E", "Y")) {
            result.append('S');
        } else {
            result.append("SK");
        }
        return index + 3;
    }

    /**
     * Handles 'T' cases.
     */
    private int handleT(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 4, "TION")) {
            result.append('X');
            index += 3;
        } else if (contains(value, index, 3, "TIA", "TCH")) {
            result.append('X');
            index += 3;
        } else if (contains(value, index, 2, "TH") || contains(value, index, 3, "TTH")) {
            if (contains(value, index + 2, 2, "OM", "AM") ||
                //-- special case "thomas", "thames" or germanic --//
                contains(value, 0, 4, "VAN ", "VON ") ||
                contains(value, 0, 3, "SCH")) {
                result.append('T');
            } else {
                result.append('0', 'T');
            }
            index += 2;
        } else {
            result.append('T');
            index = contains(value, index + 1, 1, "T", "D") ? index + 2 : index + 1;
        }
        return index;
    }

    /**
     * Handles 'W' cases.
     */
    private int handleW(final String value, final DoubleMetaphoneResult result, int index) {
        if (contains(value, index, 2, "WR")) {
            //-- can also be in middle of word --//
            result.append('R');
            index += 2;
        } else {
            if (index == 0 && (isVowel(charAt(value, index + 1)) ||
                               contains(value, index, 2, "WH"))) {
                if (isVowel(charAt(value, index + 1))) {
                    //-- Wasserman should match Vasserman --//
                    result.append('A', 'F');
                } else {
                    //-- need Uomo to match Womo --//
                    result.append('A');
                }
                index++;
            } else if ((index == value.length() - 1 && isVowel(charAt(value, index - 1))) ||
                       contains(value, index - 1, 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY") ||
                       contains(value, 0, 3, "SCH")) {
                //-- Arnow should match Arnoff --//
                result.appendAlternate('F');
                index++;
            } else if (contains(value, index, 4, "WICZ", "WITZ")) {
                //-- Polish e.g. "filipowicz" --//
                result.append("TS", "FX");
                index += 4;
            } else {
                index++;
            }
        }
        return index;
    }

    /**
     * Handles 'X' cases.
     */
    private int handleX(final String value, final DoubleMetaphoneResult result, int index) {
        if (index == 0) {
            result.append('S');
            index++;
        } else {
            if (!((index == value.length() - 1) &&
                  (contains(value, index - 3, 3, "IAU", "EAU") ||
                   contains(value, index - 2, 2, "AU", "OU")))) {
                //-- French e.g. breaux --//
                result.append("KS");
            }
            index = contains(value, index + 1, 1, "C", "X") ? index + 2 : index + 1;
        }
        return index;
    }

    /**
     * Handles 'Z' cases.
     */
    private int handleZ(final String value, final DoubleMetaphoneResult result, int index,
                        final boolean slavoGermanic) {
        if (charAt(value, index + 1) == 'H') {
            //-- Chinese pinyin e.g. "zhao" or Angelina "Zhang" --//
            result.append('J');
            index += 2;
        } else {
            if (contains(value, index + 1, 2, "ZO", "ZI", "ZA") ||
                (slavoGermanic && (index > 0 && charAt(value, index - 1) != 'T'))) {
                result.append("S", "TS");
            } else {
                result.append('S');
            }
            index = charAt(value, index + 1) == 'Z' ? index + 2 : index + 1;
        }
        return index;
    }

    //-- BEGIN CONDITIONS --//

    /**
     * Complex condition 0 for 'C'.
     */
    private boolean conditionC0(final String value, final int index) {
        if (contains(value, index, 4, "CHIA")) {
            return true;
        } else if (index <= 1) {
            return false;
        } else if (isVowel(charAt(value, index - 2))) {
            return false;
        } else if (!contains(value, index - 1, 3, "ACH")) {
            return false;
        } else {
            final char c = charAt(value, index + 2);
            return (c != 'I' && c != 'E') ||
                    contains(value, index - 2, 6, "BACHER", "MACHER");
        }
    }

    /**
     * Complex condition 0 for 'CH'.
     */
    private boolean conditionCH0(final String value, final int index) {
        if (index != 0) {
            return false;
        } else if (!contains(value, index + 1, 5, "HARAC", "HARIS") &&
                   !contains(value, index + 1, 3, "HOR", "HYM", "HIA", "HEM")) {
            return false;
        } else if (contains(value, 0, 5, "CHORE")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Complex condition 1 for 'CH'.
     */
    private boolean conditionCH1(final String value, final int index) {
        return ((contains(value, 0, 4, "VAN ", "VON ") || contains(value, 0, 3, "SCH")) ||
                contains(value, index - 2, 6, "ORCHES", "ARCHIT", "ORCHID") ||
                contains(value, index + 2, 1, "T", "S") ||
                ((contains(value, index - 1, 1, "A", "O", "U", "E") || index == 0) &&
                 (contains(value, index + 2, 1, L_R_N_M_B_H_F_V_W_SPACE) || index + 1 == value.length() - 1)));
    }

    /**
     * Complex condition 0 for 'L'.
     */
    private boolean conditionL0(final String value, final int index) {
        if (index == value.length() - 3 &&
            contains(value, index - 1, 4, "ILLO", "ILLA", "ALLE")) {
            return true;
        } else if ((contains(value, value.length() - 2, 2, "AS", "OS") ||
                    contains(value, value.length() - 1, 1, "A", "O")) &&
                   contains(value, index - 1, 4, "ALLE")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Complex condition 0 for 'M'.
     */
    private boolean conditionM0(final String value, final int index) {
        if (charAt(value, index + 1) == 'M') {
            return true;
        }
        return contains(value, index - 1, 3, "UMB") &&
               ((index + 1) == value.length() - 1 || contains(value, index + 2, 2, "ER"));
    }

    //-- BEGIN HELPER FUNCTIONS --//

    /**
     * Determines whether or not a value is of slavo-germanic origin. A value is
     * of slavo-germanic origin if it contians any of 'W', 'K', 'CZ', or 'WITZ'.
     */
    private boolean isSlavoGermanic(final String value) {
        return value.indexOf('W') > -1 || value.indexOf('K') > -1 ||
            value.indexOf("CZ") > -1 || value.indexOf("WITZ") > -1;
    }

    /**
     * Determines whether or not a character is a vowel or not
     */
    private boolean isVowel(final char ch) {
        return VOWELS.indexOf(ch) != -1;
    }

    /**
     * Determines whether or not the value starts with a silent letter.  It will
     * return {@code true} if the value starts with any of 'GN', 'KN',
     * 'PN', 'WR' or 'PS'.
     */
    private boolean isSilentStart(final String value) {
        boolean result = false;
        for (final String element : SILENT_START) {
            if (value.startsWith(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Cleans the input.
     */
    private String cleanInput(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if (input.length() == 0) {
            return null;
        }
        return input.toUpperCase(java.util.Locale.ENGLISH);
    }

    /**
     * Gets the character at index <code>index</code> if available, otherwise
     * it returns <code>Character.MIN_VALUE</code> so that there is some sort
     * of a default.
     */
    protected char charAt(final String value, final int index) {
        if (index < 0 || index >= value.length()) {
            return Character.MIN_VALUE;
        }
        return value.charAt(index);
    }

    /**
     * Determines whether <code>value</code> contains any of the criteria starting at index <code>start</code> and
     * matching up to length <code>length</code>.
     */
    protected static boolean contains(final String value, final int start, final int length,
                                      final String... criteria) {
        boolean result = false;
        if (start >= 0 && start + length <= value.length()) {
            final String target = value.substring(start, start + length);

            for (final String element : criteria) {
                if (target.equals(element)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    //-- BEGIN INNER CLASSES --//

    /**
     * Inner class for storing results, since there is the optional alternate encoding.
     */
    public class DoubleMetaphoneResult {

        private final StringBuilder primary = new StringBuilder(getMaxCodeLen());
        private final StringBuilder alternate = new StringBuilder(getMaxCodeLen());
        private final int maxLength;

        public DoubleMetaphoneResult(final int maxLength) {
            this.maxLength = maxLength;
        }

        public void append(final char value) {
            appendPrimary(value);
            appendAlternate(value);
        }

        public void append(final char primary, final char alternate) {
            appendPrimary(primary);
            appendAlternate(alternate);
        }

        public void appendPrimary(final char value) {
            if (this.primary.length() < this.maxLength) {
                this.primary.append(value);
            }
        }

        public void appendAlternate(final char value) {
            if (this.alternate.length() < this.maxLength) {
                this.alternate.append(value);
            }
        }

        public void append(final String value) {
            appendPrimary(value);
            appendAlternate(value);
        }

        public void append(final String primary, final String alternate) {
            appendPrimary(primary);
            appendAlternate(alternate);
        }

        public void appendPrimary(final String value) {
            final int addChars = this.maxLength - this.primary.length();
            if (value.length() <= addChars) {
                this.primary.append(value);
            } else {
                this.primary.append(value.substring(0, addChars));
            }
        }

        public void appendAlternate(final String value) {
            final int addChars = this.maxLength - this.alternate.length();
            if (value.length() <= addChars) {
                this.alternate.append(value);
            } else {
                this.alternate.append(value.substring(0, addChars));
            }
        }

        public String getPrimary() {
            return this.primary.toString();
        }

        public String getAlternate() {
            return this.alternate.toString();
        }

        public boolean isComplete() {
            return this.primary.length() >= this.maxLength &&
                   this.alternate.length() >= this.maxLength;
        }
    }
}

```
