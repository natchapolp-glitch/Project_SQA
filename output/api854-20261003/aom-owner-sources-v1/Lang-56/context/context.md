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
   "Lang" component of the Jakarta Commons Subproject
   $Id$
-->
<project name="Lang" default="compile" basedir=".">
	<!-- ========== Initialize Properties ===================================== -->
	<property file="${user.home}/${component.name}.build.properties"/>
	<property file="${user.home}/build.properties"/>
	<property file="${basedir}/build.properties"/>
	<property file="${basedir}/default.properties"/>
	<!-- The Sun Javadoc for versions prior to 1.3 are off-line. -->
	<property name="jdk.javadoc" value="http://java.sun.com/j2se/1.3/docs/api/"/>
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
		<filter token="compile.source" value="${compile.source}"/>
		<filter token="compile.target" value="${compile.target}"/>
	</target>
	<target name="prepare" depends="init" description="Prepare build directory">
		<mkdir dir="${build.home}"/>
		<mkdir dir="${build.home}/classes"/>
		<mkdir dir="${build.home}/conf"/>
		<mkdir dir="${build.home}/tests"/>
	</target>
	<target name="static" depends="prepare" description="Copy static files to build directory">
		<tstamp/>
		<copy todir="${build.home}/conf" filtering="on">
			<fileset dir="${conf.home}" includes="*.MF"/>
		</copy>
	</target>
	<target name="compile" depends="static" description="Compile shareable components">
		<javac srcdir="${source.home}" destdir="${build.home}/classes" debug="${compile.debug}" deprecation="${compile.deprecation}" target="${compile.target}" source="${compile.source}" excludes="${compile.excludes}" optimize="${compile.optimize}">
			<classpath refid="compile.classpath"/>
		</javac>
		<copy todir="${build.home}/classes" filtering="on">
			<fileset dir="${source.home}" excludes="**/*.java"/>
		</copy>
	</target>
	<target name="compile.tests" depends="compile" description="Compile unit test cases">
		<javac srcdir="${test.home}" destdir="${build.home}/tests" debug="${compile.debug}" deprecation="off" target="${compile.target}" source="${compile.source}" optimize="${compile.optimize}">
			<classpath refid="test.classpath"/>
		</javac>
		<copy todir="${build.home}/tests" filtering="on">
			<fileset dir="${test.home}" excludes="**/*.java"/>
		</copy>
	</target>
	<target name="clean" description="Clean build and distribution directories">
		<delete dir="${build.home}"/>
		<delete dir="${dist.home}"/>
	</target>
	<target name="all" depends="clean,compile" description="Clean and compile all components"/>
	<target name="javadoc" depends="compile" description="Create component Javadoc documentation">
		<mkdir dir="${dist.home}"/>
		<mkdir dir="${dist.home}/docs"/>
		<mkdir dir="${dist.home}/docs/api"/>
		<javadoc sourcepath="${source.home}" destdir="${dist.home}/docs/api" overview="${source.home}/org/apache/commons/lang/overview.html" packagenames="org.apache.commons.*" excludepackagenames="${javadoc.excludepackagenames}" author="true" version="true" doctitle="&lt;h1&gt;${component.title}&lt;/h1&gt;" windowtitle="${component.title} (Version ${component.version})" bottom="Copyright &amp;copy; 2001-${copyright.end} - Apache Software Foundation" use="true" link="${jdk.javadoc}" source="${compile.source}">
			<classpath refid="compile.classpath"/>
		</javadoc>
	</target>
	<target name="dist" depends="compile,javadoc" description="Create binary distribution">
		<mkdir dir="${dist.home}"/>
		<copy file="LICENSE.txt" todir="${dist.home}"/>
		<copy file="NOTICE.txt" todir="${dist.home}"/>
		<copy file="RELEASE-NOTES.txt" todir="${dist.home}"/>
		<antcall target="jar"/>
	</target>
	<target name="dist-build" depends="clean,dist" description="Build binary distribution files">
        <copy file="${dist.home}/${final.name}.jar" todir="."/>
		<jar jarfile="${final.name}-sources.jar" basedir="${source.home}" manifest="${build.home}/conf/MANIFEST.MF"/>
		<jar jarfile="${final.name}-javadoc.jar" basedir="${dist.home}/docs/api" manifest="${build.home}/conf/MANIFEST.MF"/>
		<mkdir dir="${final.name}"/>
		<move todir="${final.name}/${final.name}">
			<fileset dir="${dist.home}"/>
		</move>
		<tar destfile="${final.name}.tar" basedir="${final.name}"/>
		<gzip zipfile="${final.name}.tar.gz" src="${final.name}.tar"/>
		<delete file="${final.name}.tar"/>
		<fixcrlf srcdir="${final.name}" eol="crlf" includes="**/*.txt"/>
		<zip destfile="${final.name}.zip" basedir="${final.name}"/>
		<delete dir="${final.name}"/>
	</target>
	<target name="jar" depends="compile" description="Create jar">
		<mkdir dir="${dist.home}"/>
		<mkdir dir="${build.home}/classes/META-INF"/>
		<copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
		<copy file="NOTICE.txt" tofile="${build.home}/classes/META-INF/NOTICE.txt"/>
		<jar jarfile="${dist.home}/${final.name}.jar" basedir="${build.home}/classes" manifest="${build.home}/conf/MANIFEST.MF" excludes="**/*.html"/>
	</target>
	<target name="install-jar" depends="jar" description="--> Installs jar file in ${lib.repo}">
		<copy todir="${lib.repo}" filtering="no">
			<fileset dir="${dist.home}">
				<include name="${final.name}.jar"/>
			</fileset>
		</copy>
	</target>
	<!-- ========== Unit Test Targets ========================================= -->
	<target name="test" depends="
                                compile.tests, 
                                test.lang,
                                test.builder,
						        test.enum,
						        test.enums,
                                test.exception,
                                test.math,
						        test.mutable,
        						test.text,
						        test.time" description="Run all unit test cases">
		<echo message="Running tests ..."/>
	</target>
	<macrodef name="runTestCase">
		<attribute name="classname"/>
		<sequential>
			<junit printsummary="true" showoutput="true" fork="${junit.fork}" haltonerror="${test.failonerror}">
				<classpath refid="test.classpath"/>
				<test name="@{classname}"/>
			</junit>
		</sequential>
	</macrodef>
	<target name="test.lang" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.LangTestSuite"/>
	</target>
	<target name="test.builder" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.builder.BuilderTestSuite"/>
	</target>
	<target name="test.enum" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.enum.EnumTestSuite"/>
	</target>
	<target name="test.enums" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.enums.EnumTestSuite"/>
	</target>
	<target name="test.exception" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.exception.ExceptionTestSuite"/>
	</target>
	<target name="test.math" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.math.MathTestSuite"/>
	</target>
	<target name="test.mutable" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.mutable.MutableTestSuite"/>
	</target>
	<target name="test.text" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.text.TextTestSuite"/>
	</target>
	<target name="test.time" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.time.TimeTestSuite"/>
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
    <version>1</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-lang</groupId>
  <artifactId>commons-lang</artifactId>
  <version>2.3-SNAPSHOT</version>
  <name>Lang</name>

  <inceptionYear>2001</inceptionYear>
    <description>
        Commons.Lang, a package of Java utility classes for the
        classes that are in java.lang's hierarchy, or are considered to be so
        standard as to justify existence in java.lang.
    </description>

  <url>http://jakarta.apache.org/commons/lang/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/LANG</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:scm:svn:https://svn.apache.org/repos/asf/jakarta/commons/proper/lang/trunk</connection>
    <developerConnection>scm:svn:scm:svn:https://svn.apache.org/repos/asf/jakarta/commons/proper/lang/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/jakarta/commons/proper/lang/trunk</url>
  </scm>

    <developers>
        <developer>
            <name>Daniel Rall</name>
            <id>dlr</id>
            <email>dlr@finemaltcoding.com</email>
            <organization>CollabNet, Inc.</organization>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Stephen Colebourne</name>
            <id>scolebourne</id>
            <email>scolebourne@joda.org</email>
            <organization>SITA ATS Ltd</organization>
            <timezone>0</timezone>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Henri Yandell</name>
            <id>bayard</id>
            <email>bayard@generationjava.com</email>
            <organization/>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Steven Caswell</name>
            <id>scaswell</id>
            <email>stevencaswell@apache.org</email>
            <organization/>
            <roles>
                <role>Java Developer</role>
            </roles>
            <timezone>-5</timezone>
        </developer>
        <developer>
            <name>Robert Burrell Donkin</name>
            <id>rdonkin</id>
            <email>rdonkin@apache.org</email>
            <organization/>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Gary D. Gregory</name>
            <id>ggregory</id>
            <email>ggregory@seagullsw.com</email>
            <organization>Seagull Software</organization>
            <timezone>-8</timezone>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Phil Steitz</name>
            <id>psteitz</id>
            <email>phil@steitz.com</email>
            <organization/>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Fredrik Westermarck</name>
            <id>fredrik</id>
            <email/>
            <organization/>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>James Carman</name>
            <id>jcarman</id>
            <email>jcarman@apache.org</email>
            <organization>Carman Consulting, Inc.</organization>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
    </developers>
    <contributors>
        <contributor>
            <name>C. Scott Ananian</name>
        </contributor>
        <contributor>
            <name>Chris Audley</name>
        </contributor>
        <contributor>
            <name>Stephane Bailliez</name>
        </contributor>
        <contributor>
            <name>Michael Becke</name>
        </contributor>
        <contributor>
            <name>Ola Berg</name>
        </contributor>
        <contributor>
            <name>Nathan Beyer</name>
        </contributor>
        <contributor>
            <name>Stefan Bodewig</name>
        </contributor>
        <contributor>
            <name>Janek Bogucki</name>
        </contributor>
        <contributor>
            <name>Mike Bowler</name>
        </contributor>
        <contributor>
            <name>Sean Brown</name>
        </contributor>
        <contributor>
            <name>Alexander Day Chaffee</name>
        </contributor>
        <contributor>
            <name>Al Chou</name>
        </contributor>
        <contributor>
            <name>Greg Coladonato</name>
        </contributor>
        <contributor>
            <name>Maarten Coene</name>
        </contributor>
        <contributor>
            <name>Justin Couch</name>
        </contributor>
        <contributor>
            <name>Michael Davey</name>
        </contributor>
        <contributor>
            <name>Norm Deane</name>
        </contributor>
        <contributor>
            <name>Ringo De Smet</name>
        </contributor>
        <contributor>
            <name>Russel Dittmar</name>
        </contributor>
        <contributor>
            <name>Steve Downey</name>
        </contributor>
        <contributor>
            <name>Matthias Eichel</name>
        </contributor>
        <contributor>
            <name>Christopher Elkins</name>
        </contributor>
        <contributor>
            <name>Chris Feldhacker</name>
        </contributor>
        <contributor>
            <name>Pete Gieser</name>
        </contributor>
        <contributor>
            <name>Jason Gritman</name>
        </contributor>
        <contributor>
            <name>Matthew Hawthorne</name>
        </contributor>
        <contributor>
            <name>Michael Heuer</name>
        </contributor>
        <contributor>
            <name>Oliver Heger</name>
        </contributor>
        <contributor>
            <name>Chris Hyzer</name>
        </contributor>
        <contributor>
            <name>Marc Johnson</name>
        </contributor>
        <contributor>
            <name>Shaun Kalley</name>
        </contributor>
        <contributor>
            <name>Tetsuya Kaneuchi</name>
        </contributor>
        <contributor>
            <name>Nissim Karpenstein</name>
        </contributor>
        <contributor>
            <name>Ed Korthof</name>
        </contributor>
        <contributor>
            <name>Holger Krauth</name>
        </contributor>
        <contributor>
            <name>Rafal Krupinski</name>
        </contributor>
        <contributor>
            <name>Rafal Krzewski</name>
        </contributor>
        <contributor>
            <name>Craig R. McClanahan</name>
        </contributor>
        <contributor>
            <name>Rand McNeely</name>
        </contributor>
        <contributor>
            <name>Nikolay Metchev</name>
        </contributor>
        <contributor>
            <name>Kasper Nielsen</name>
        </contributor>
        <contributor>
            <name>Tim O'Brien</name>
        </contributor>
        <contributor>
            <name>Brian S O'Neill</name>
        </contributor>
        <contributor>
            <name>Andrew C. Oliver</name>
        </contributor>
        <contributor>
            <name>Alban Peignier</name>
        </contributor>
        <contributor>
            <name>Moritz Petersen</name>
        </contributor>
        <contributor>
            <name>Dmitri Plotnikov</name>
        </contributor>
        <contributor>
            <name>Neeme Praks</name>
        </contributor>
        <contributor>
            <name>Eric Pugh</name>
        </contributor>
        <contributor>
            <name>Stephen Putman</name>
        </contributor>
        <contributor>
            <name>Travis Reeder</name>
        </contributor>
        <contributor>
            <name>Antony Riley</name>
        </contributor>
        <contributor>
            <name>Scott Sanders</name>
        </contributor>
        <contributor>
            <name>Ralph Schaer</name>
        </contributor>
        <contributor>
            <name>Henning P. Schmiedehausen</name>
        </contributor>
        <contributor>
            <name>Sean Schofield</name>
        </contributor>
        <contributor>
            <name>Reuben Sivan</name>
        </contributor>
        <contributor>
            <name>Ville Skytta</name>
        </contributor>
        <contributor>
            <name>Jan Sorensen</name>
        </contributor>
        <contributor>
            <name>Glen Stampoultzis</name>
        </contributor>
        <contributor>
            <name>Scott Stanchfield</name>
        </contributor>
        <contributor>
            <name>Jon S. Stevens</name>
        </contributor>
        <contributor>
            <name>Sean C. Sullivan</name>
        </contributor>
        <contributor>
            <name>Ashwin Suresh</name>
        </contributor>
        <contributor>
            <name>Helge Tesgaard</name>
        </contributor>
        <contributor>
            <name>Arun Mammen Thomas</name>
        </contributor>
        <contributor>
            <name>Masato Tezuka</name>
        </contributor>
        <contributor>
            <name>Jeff Varszegi</name>
        </contributor>
        <contributor>
            <name>Chris Webb</name>
        </contributor>
        <contributor>
            <name>Mario Winterer</name>
        </contributor>
        <contributor>
            <name>Stepan Koltsov</name>
        </contributor>
        <contributor>
            <name>Holger Hoffstatte</name>
        </contributor>
    </contributors>

  <!-- Lang should depend on very little -->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <sourceDirectory>src/java</sourceDirectory>
    <testSourceDirectory>src/test</testSourceDirectory>
    <resources>
      <resource>
        <directory>.</directory>
        <targetPath>META-INF</targetPath>
          <includes>
            <include>NOTICE.txt</include>
            <include>LICENSE.txt</include>
          </includes>
        </resource>
      </resources>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/*TestSuite.java</include>
              </includes>
              <excludes>
                <exclude>**/AllLangTestSuite.java</exclude>
              </excludes>
          </configuration>
        </plugin>
      </plugins>
    </build>

</project>

```

## project.properties

```
#
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
#

# $Id$

maven.repo.remote=http://repo1.maven.org/maven

maven.changelog.factory=org.apache.maven.svnlib.SvnChangeLogFactory

maven.checkstyle.properties=checkstyle.xml
maven.junit.fork=true

maven.xdoc.date=left
maven.xdoc.version=${pom.currentVersion}
maven.xdoc.developmentProcessUrl=http://jakarta.apache.org/commons/charter.html
maven.xdoc.poweredby.image=maven-feather.png

# Jar Manifest Additional Attributes
maven.jar.manifest.attributes.list=Implementation-Vendor-Id,X-Compile-Source-JDK,X-Compile-Target-JDK
maven.jar.manifest.attribute.Implementation-Vendor-Id=org.apache
maven.jar.manifest.attribute.X-Compile-Source-JDK=${maven.compile.source}
maven.jar.manifest.attribute.X-Compile-Target-JDK=${maven.compile.target}

# Make the source distro unzip to a different directory
maven.dist.src.assembly.dir=${maven.dist.assembly.dir}/src/${maven.final.name}-src

# The Sun Javadoc for versions prior to 1.3 are off-line.
maven.javadoc.links=http://java.sun.com/j2se/1.3/docs/api/
maven.javadoc.source=1.3
maven.javadoc.overview=./src/java/org/apache/commons/lang/overview.html

# This links to the current stable version. We could have LANG_CURRENT, but this 
# is simpler to manage at the moment.
maven.jdiff.new.tag=CURRENT
maven.jdiff.old.tag=LANG_2_1

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

```

## src/java/org/apache/commons/lang/time/FastDateFormat.java

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
package org.apache.commons.lang.time;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.Validate;

/**
 * <p>FastDateFormat is a fast and thread-safe version of
 * {@link java.text.SimpleDateFormat}.</p>
 * 
 * <p>This class can be used as a direct replacement to
 * <code>SimpleDateFormat</code> in most formatting situations.
 * This class is especially useful in multi-threaded server environments.
 * <code>SimpleDateFormat</code> is not thread-safe in any JDK version,
 * nor will it be as Sun have closed the bug/RFE.
 * </p>
 *
 * <p>Only formatting is supported, but all patterns are compatible with
 * SimpleDateFormat (except time zones - see below).</p>
 *
 * <p>Java 1.4 introduced a new pattern letter, <code>'Z'</code>, to represent
 * time zones in RFC822 format (eg. <code>+0800</code> or <code>-1100</code>).
 * This pattern letter can be used here (on all JDK versions).</p>
 *
 * <p>In addition, the pattern <code>'ZZ'</code> has been made to represent
 * ISO8601 full format time zones (eg. <code>+08:00</code> or <code>-11:00</code>).
 * This introduces a minor incompatibility with Java 1.4, but at a gain of
 * useful functionality.</p>
 *
 * @author TeaTrove project
 * @author Brian S O'Neill
 * @author Sean Schofield
 * @author Gary Gregory
 * @author Stephen Colebourne
 * @author Nikolay Metchev
 * @since 2.0
 * @version $Id$
 */
public class FastDateFormat extends Format {
    // A lot of the speed in this class comes from caching, but some comes
    // from the special int to StringBuffer conversion.
    //
    // The following produces a padded 2 digit number:
    //   buffer.append((char)(value / 10 + '0'));
    //   buffer.append((char)(value % 10 + '0'));
    //
    // Note that the fastest append to StringBuffer is a single char (used here).
    // Note that Integer.toString() is not called, the conversion is simply
    // taking the value and adding (mathematically) the ASCII value for '0'.
    // So, don't change this code! It works and is very fast.
    
    /**
     * Required for serialization support.
     * 
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * FULL locale dependent date or time style.
     */
    public static final int FULL = DateFormat.FULL;
    /**
     * LONG locale dependent date or time style.
     */
    public static final int LONG = DateFormat.LONG;
    /**
     * MEDIUM locale dependent date or time style.
     */
    public static final int MEDIUM = DateFormat.MEDIUM;
    /**
     * SHORT locale dependent date or time style.
     */
    public static final int SHORT = DateFormat.SHORT;
    
    private static String cDefaultPattern;

    private static Map cInstanceCache = new HashMap(7);
    private static Map cDateInstanceCache = new HashMap(7);
    private static Map cTimeInstanceCache = new HashMap(7);
    private static Map cDateTimeInstanceCache = new HashMap(7);
    private static Map cTimeZoneDisplayCache = new HashMap(7);

    /**
     * The pattern.
     */
    private final String mPattern;
    /**
     * The time zone.
     */
    private final TimeZone mTimeZone;
    /**
     * Whether the time zone overrides any on Calendars.
     */
    private final boolean mTimeZoneForced;
    /**
     * The locale.
     */
    private final Locale mLocale;
    /**
     * Whether the locale overrides the default.
     */
    private final boolean mLocaleForced;
    /**
     * The parsed rules.
     */
    private transient Rule[] mRules;
    /**
     * The estimated maximum length.
     */
    private transient int mMaxLengthEstimate;

    //-----------------------------------------------------------------------
    /**
     * <p>Gets a formatter instance using the default pattern in the
     * default locale.</p>
     * 
     * @return a date/time formatter
     */
    public static FastDateFormat getInstance() {
        return getInstance(getDefaultPattern(), null, null);
    }

    /**
     * <p>Gets a formatter instance using the specified pattern in the
     * default locale.</p>
     * 
     * @param pattern  {@link java.text.SimpleDateFormat} compatible
     *  pattern
     * @return a pattern based date/time formatter
     * @throws IllegalArgumentException if pattern is invalid
     */
    public static FastDateFormat getInstance(String pattern) {
        return getInstance(pattern, null, null);
    }

    /**
     * <p>Gets a formatter instance using the specified pattern and
     * time zone.</p>
     * 
     * @param pattern  {@link java.text.SimpleDateFormat} compatible
     *  pattern
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted date
     * @return a pattern based date/time formatter
     * @throws IllegalArgumentException if pattern is invalid
     */
    public static FastDateFormat getInstance(String pattern, TimeZone timeZone) {
        return getInstance(pattern, timeZone, null);
    }

    /**
     * <p>Gets a formatter instance using the specified pattern and
     * locale.</p>
     * 
     * @param pattern  {@link java.text.SimpleDateFormat} compatible
     *  pattern
     * @param locale  optional locale, overrides system locale
     * @return a pattern based date/time formatter
     * @throws IllegalArgumentException if pattern is invalid
     */
    public static FastDateFormat getInstance(String pattern, Locale locale) {
        return getInstance(pattern, null, locale);
    }

    /**
     * <p>Gets a formatter instance using the specified pattern, time zone
     * and locale.</p>
     * 
     * @param pattern  {@link java.text.SimpleDateFormat} compatible
     *  pattern
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted date
     * @param locale  optional locale, overrides system locale
     * @return a pattern based date/time formatter
     * @throws IllegalArgumentException if pattern is invalid
     *  or <code>null</code>
     */
    public static synchronized FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
        FastDateFormat emptyFormat = new FastDateFormat(pattern, timeZone, locale);
        FastDateFormat format = (FastDateFormat) cInstanceCache.get(emptyFormat);
        if (format == null) {
            format = emptyFormat;
            format.init();  // convert shell format into usable one
            cInstanceCache.put(format, format);  // this is OK!
        }
        return format;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Gets a date formatter instance using the specified style in the
     * default time zone and locale.</p>
     * 
     * @param style  date style: FULL, LONG, MEDIUM, or SHORT
     * @return a localized standard date formatter
     * @throws IllegalArgumentException if the Locale has no date
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getDateInstance(int style) {
        return getDateInstance(style, null, null);
    }

    /**
     * <p>Gets a date formatter instance using the specified style and
     * locale in the default time zone.</p>
     * 
     * @param style  date style: FULL, LONG, MEDIUM, or SHORT
     * @param locale  optional locale, overrides system locale
     * @return a localized standard date formatter
     * @throws IllegalArgumentException if the Locale has no date
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getDateInstance(int style, Locale locale) {
        return getDateInstance(style, null, locale);
    }

    /**
     * <p>Gets a date formatter instance using the specified style and
     * time zone in the default locale.</p>
     * 
     * @param style  date style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted date
     * @return a localized standard date formatter
     * @throws IllegalArgumentException if the Locale has no date
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getDateInstance(int style, TimeZone timeZone) {
        return getDateInstance(style, timeZone, null);
    }
    /**
     * <p>Gets a date formatter instance using the specified style, time
     * zone and locale.</p>
     * 
     * @param style  date style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted date
     * @param locale  optional locale, overrides system locale
     * @return a localized standard date formatter
     * @throws IllegalArgumentException if the Locale has no date
     *  pattern defined
     */
    public static synchronized FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
        Object key = new Integer(style);
        if (timeZone != null) {
            key = new Pair(key, timeZone);
        }
        if (locale != null) {
            key = new Pair(key, locale);
        }

        FastDateFormat format = (FastDateFormat) cDateInstanceCache.get(key);
        if (format == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            try {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(style, locale);
                String pattern = formatter.toPattern();
                format = getInstance(pattern, timeZone, locale);
                cDateInstanceCache.put(key, format);
                
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date pattern for locale: " + locale);
            }
        }
        return format;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Gets a time formatter instance using the specified style in the
     * default time zone and locale.</p>
     * 
     * @param style  time style: FULL, LONG, MEDIUM, or SHORT
     * @return a localized standard time formatter
     * @throws IllegalArgumentException if the Locale has no time
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getTimeInstance(int style) {
        return getTimeInstance(style, null, null);
    }

    /**
     * <p>Gets a time formatter instance using the specified style and
     * locale in the default time zone.</p>
     * 
     * @param style  time style: FULL, LONG, MEDIUM, or SHORT
     * @param locale  optional locale, overrides system locale
     * @return a localized standard time formatter
     * @throws IllegalArgumentException if the Locale has no time
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getTimeInstance(int style, Locale locale) {
        return getTimeInstance(style, null, locale);
    }
    
    /**
     * <p>Gets a time formatter instance using the specified style and
     * time zone in the default locale.</p>
     * 
     * @param style  time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted time
     * @return a localized standard time formatter
     * @throws IllegalArgumentException if the Locale has no time
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getTimeInstance(int style, TimeZone timeZone) {
        return getTimeInstance(style, timeZone, null);
    }
    
    /**
     * <p>Gets a time formatter instance using the specified style, time
     * zone and locale.</p>
     * 
     * @param style  time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted time
     * @param locale  optional locale, overrides system locale
     * @return a localized standard time formatter
     * @throws IllegalArgumentException if the Locale has no time
     *  pattern defined
     */
    public static synchronized FastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale) {
        Object key = new Integer(style);
        if (timeZone != null) {
            key = new Pair(key, timeZone);
        }
        if (locale != null) {
            key = new Pair(key, locale);
        }

        FastDateFormat format = (FastDateFormat) cTimeInstanceCache.get(key);
        if (format == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            try {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getTimeInstance(style, locale);
                String pattern = formatter.toPattern();
                format = getInstance(pattern, timeZone, locale);
                cTimeInstanceCache.put(key, format);
            
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date pattern for locale: " + locale);
            }
        }
        return format;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Gets a date/time formatter instance using the specified style
     * in the default time zone and locale.</p>
     * 
     * @param dateStyle  date style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle  time style: FULL, LONG, MEDIUM, or SHORT
     * @return a localized standard date/time formatter
     * @throws IllegalArgumentException if the Locale has no date/time
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getDateTimeInstance(
            int dateStyle, int timeStyle) {
        return getDateTimeInstance(dateStyle, timeStyle, null, null);
    }
    
    /**
     * <p>Gets a date/time formatter instance using the specified style and
     * locale in the default time zone.</p>
     * 
     * @param dateStyle  date style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle  time style: FULL, LONG, MEDIUM, or SHORT
     * @param locale  optional locale, overrides system locale
     * @return a localized standard date/time formatter
     * @throws IllegalArgumentException if the Locale has no date/time
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getDateTimeInstance(
            int dateStyle, int timeStyle, Locale locale) {
        return getDateTimeInstance(dateStyle, timeStyle, null, locale);
    }
    
    /**
     * <p>Gets a date/time formatter instance using the specified style and
     * time zone in the default locale.</p>
     * 
     * @param dateStyle  date style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle  time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted date
     * @return a localized standard date/time formatter
     * @throws IllegalArgumentException if the Locale has no date/time
     *  pattern defined
     * @since 2.1
     */
    public static FastDateFormat getDateTimeInstance(
            int dateStyle, int timeStyle, TimeZone timeZone) {
        return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
    }    
    /**
     * <p>Gets a date/time formatter instance using the specified style,
     * time zone and locale.</p>
     * 
     * @param dateStyle  date style: FULL, LONG, MEDIUM, or SHORT
     * @param timeStyle  time style: FULL, LONG, MEDIUM, or SHORT
     * @param timeZone  optional time zone, overrides time zone of
     *  formatted date
     * @param locale  optional locale, overrides system locale
     * @return a localized standard date/time formatter
     * @throws IllegalArgumentException if the Locale has no date/time
     *  pattern defined
     */
    public static synchronized FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone,
            Locale locale) {

        Object key = new Pair(new Integer(dateStyle), new Integer(timeStyle));
        if (timeZone != null) {
            key = new Pair(key, timeZone);
        }
        if (locale != null) {
            key = new Pair(key, locale);
        }

        FastDateFormat format = (FastDateFormat) cDateTimeInstanceCache.get(key);
        if (format == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            try {
                SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(dateStyle, timeStyle,
                        locale);
                String pattern = formatter.toPattern();
                format = getInstance(pattern, timeZone, locale);
                cDateTimeInstanceCache.put(key, format);

            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }
        return format;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Gets the time zone display name, using a cache for performance.</p>
     * 
     * @param tz  the zone to query
     * @param daylight  true if daylight savings
     * @param style  the style to use <code>TimeZone.LONG</code>
     *  or <code>TimeZone.SHORT</code>
     * @param locale  the locale to use
     * @return the textual name of the time zone
     */
    static synchronized String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        Object key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = (String) cTimeZoneDisplayCache.get(key);
        if (value == null) {
            // This is a very slow call, so cache the results.
            value = tz.getDisplayName(daylight, style, locale);
            cTimeZoneDisplayCache.put(key, value);
        }
        return value;
    }

    /**
     * <p>Gets the default pattern.</p>
     * 
     * @return the default pattern
     */
    private static synchronized String getDefaultPattern() {
        if (cDefaultPattern == null) {
            cDefaultPattern = new SimpleDateFormat().toPattern();
        }
        return cDefaultPattern;
    }

    // Constructor
    //-----------------------------------------------------------------------
    /**
     * <p>Constructs a new FastDateFormat.</p>
     * 
     * @param pattern  {@link java.text.SimpleDateFormat} compatible
     *  pattern
     * @param timeZone  time zone to use, <code>null</code> means use
     *  default for <code>Date</code> and value within for
     *  <code>Calendar</code>
     * @param locale  locale, <code>null</code> means use system
     *  default
     * @throws IllegalArgumentException if pattern is invalid or
     *  <code>null</code>
     */
    protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale) {
        super();
        if (pattern == null) {
            throw new IllegalArgumentException("The pattern must not be null");
        }
        mPattern = pattern;
        
        mTimeZoneForced = (timeZone != null);
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        mTimeZone = timeZone;
        
        mLocaleForced = (locale != null);
        if (locale == null) {
            locale = Locale.getDefault();
        }
        mLocale = locale;
    }

    /**
     * <p>Initializes the instance for first use.</p>
     */
    protected void init() {
        List rulesList = parsePattern();
        mRules = (Rule[]) rulesList.toArray(new Rule[rulesList.size()]);

        int len = 0;
        for (int i=mRules.length; --i >= 0; ) {
            len += mRules[i].estimateLength();
        }

        mMaxLengthEstimate = len;
    }

    // Parse the pattern
    //-----------------------------------------------------------------------
    /**
     * <p>Returns a list of Rules given a pattern.</p>
     * 
     * @return a <code>List</code> of Rule objects
     * @throws IllegalArgumentException if pattern is invalid
     */
    protected List parsePattern() {
        DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
        List rules = new ArrayList();

        String[] ERAs = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] AmPmStrings = symbols.getAmPmStrings();

        int length = mPattern.length();
        int[] indexRef = new int[1];

        for (int i = 0; i < length; i++) {
            indexRef[0] = i;
            String token = parseToken(mPattern, indexRef);
            i = indexRef[0];

            int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }

            Rule rule;
            char c = token.charAt(0);

            switch (c) {
            case 'G': // era designator (text)
                rule = new TextField(Calendar.ERA, ERAs);
                break;
            case 'y': // year (number)
                if (tokenLen >= 4) {
                    rule = selectNumberRule(Calendar.YEAR, tokenLen);
                } else {
                    rule = TwoDigitYearField.INSTANCE;
                }
                break;
            case 'M': // month in year (text and number)
                if (tokenLen >= 4) {
                    rule = new TextField(Calendar.MONTH, months);
                } else if (tokenLen == 3) {
                    rule = new TextField(Calendar.MONTH, shortMonths);
                } else if (tokenLen == 2) {
                    rule = TwoDigitMonthField.INSTANCE;
                } else {
                    rule = UnpaddedMonthField.INSTANCE;
                }
                break;
            case 'd': // day in month (number)
                rule = selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
                break;
            case 'h': // hour in am/pm (number, 1..12)
                rule = new TwelveHourField(selectNumberRule(Calendar.HOUR, tokenLen));
                break;
            case 'H': // hour in day (number, 0..23)
                rule = selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen);
                break;
            case 'm': // minute in hour (number)
                rule = selectNumberRule(Calendar.MINUTE, tokenLen);
                break;
            case 's': // second in minute (number)
                rule = selectNumberRule(Calendar.SECOND, tokenLen);
                break;
            case 'S': // millisecond (number)
                rule = selectNumberRule(Calendar.MILLISECOND, tokenLen);
                break;
            case 'E': // day in week (text)
                rule = new TextField(Calendar.DAY_OF_WEEK, tokenLen < 4 ? shortWeekdays : weekdays);
                break;
            case 'D': // day in year (number)
                rule = selectNumberRule(Calendar.DAY_OF_YEAR, tokenLen);
                break;
            case 'F': // day of week in month (number)
                rule = selectNumberRule(Calendar.DAY_OF_WEEK_IN_MONTH, tokenLen);
                break;
            case 'w': // week in year (number)
                rule = selectNumberRule(Calendar.WEEK_OF_YEAR, tokenLen);
                break;
            case 'W': // week in month (number)
                rule = selectNumberRule(Calendar.WEEK_OF_MONTH, tokenLen);
                break;
            case 'a': // am/pm marker (text)
                rule = new TextField(Calendar.AM_PM, AmPmStrings);
                break;
            case 'k': // hour in day (1..24)
                rule = new TwentyFourHourField(selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen));
                break;
            case 'K': // hour in am/pm (0..11)
                rule = selectNumberRule(Calendar.HOUR, tokenLen);
                break;
            case 'z': // time zone (text)
                if (tokenLen >= 4) {
                    rule = new TimeZoneNameRule(mTimeZone, mTimeZoneForced, mLocale, TimeZone.LONG);
                } else {
                    rule = new TimeZoneNameRule(mTimeZone, mTimeZoneForced, mLocale, TimeZone.SHORT);
                }
                break;
            case 'Z': // time zone (value)
                if (tokenLen == 1) {
                    rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                } else {
                    rule = TimeZoneNumberRule.INSTANCE_COLON;
                }
                break;
            case '\'': // literal text
                String sub = token.substring(1);
                if (sub.length() == 1) {
                    rule = new CharacterLiteral(sub.charAt(0));
                } else {
                    rule = new StringLiteral(sub);
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal pattern component: " + token);
            }

            rules.add(rule);
        }

        return rules;
    }

    /**
     * <p>Performs the parsing of tokens.</p>
     * 
     * @param pattern  the pattern
     * @param indexRef  index references
     * @return parsed token
     */
    protected String parseToken(String pattern, int[] indexRef) {
        StringBuffer buf = new StringBuffer();

        int i = indexRef[0];
        int length = pattern.length();

        char c = pattern.charAt(i);
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            // Scan a run of the same character, which indicates a time
            // pattern.
            buf.append(c);

            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek == c) {
                    buf.append(c);
                    i++;
                } else {
                    break;
                }
            }
        } else {
            // This will identify token as text.
            buf.append('\'');

            boolean inLiteral = false;

            for (; i < length; i++) {
                c = pattern.charAt(i);

                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        // '' is treated as escaped '
                        i++;
                        buf.append(c);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else if (!inLiteral &&
                         (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
                    i--;
                    break;
                } else {
                    buf.append(c);
                }
            }
        }

        indexRef[0] = i;
        return buf.toString();
    }

    /**
     * <p>Gets an appropriate rule for the padding required.</p>
     * 
     * @param field  the field to get a rule for
     * @param padding  the padding required
     * @return a new rule with the correct padding
     */
    protected NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
        case 1:
            return new UnpaddedNumberField(field);
        case 2:
            return new TwoDigitNumberField(field);
        default:
            return new PaddedNumberField(field, padding);
        }
    }

    // Format methods
    //-----------------------------------------------------------------------
    /**
     * <p>Formats a <code>Date</code>, <code>Calendar</code> or
     * <code>Long</code> (milliseconds) object.</p>
     * 
     * @param obj  the object to format
     * @param toAppendTo  the buffer to append to
     * @param pos  the position - ignored
     * @return the buffer passed in
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof Date) {
            return format((Date) obj, toAppendTo);
        } else if (obj instanceof Calendar) {
            return format((Calendar) obj, toAppendTo);
        } else if (obj instanceof Long) {
            return format(((Long) obj).longValue(), toAppendTo);
        } else {
            throw new IllegalArgumentException("Unknown class: " +
                (obj == null ? "<null>" : obj.getClass().getName()));
        }
    }

    /**
     * <p>Formats a millisecond <code>long</code> value.</p>
     * 
     * @param millis  the millisecond value to format
     * @return the formatted string
     * @since 2.1
     */
    public String format(long millis) {
        return format(new Date(millis));
    }

    /**
     * <p>Formats a <code>Date</code> object.</p>
     * 
     * @param date  the date to format
     * @return the formatted string
     */
    public String format(Date date) {
        Calendar c = new GregorianCalendar(mTimeZone);
        c.setTime(date);
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }

    /**
     * <p>Formats a <code>Calendar</code> object.</p>
     * 
     * @param calendar  the calendar to format
     * @return the formatted string
     */
    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(mMaxLengthEstimate)).toString();
    }

    /**
     * <p>Formats a milliseond <code>long</code> value into the
     * supplied <code>StringBuffer</code>.</p>
     * 
     * @param millis  the millisecond value to format
     * @param buf  the buffer to format into
     * @return the specified string buffer
     * @since 2.1
     */
    public StringBuffer format(long millis, StringBuffer buf) {
        return format(new Date(millis), buf);
    }

    /**
     * <p>Formats a <code>Date</code> object into the
     * supplied <code>StringBuffer</code>.</p>
     * 
     * @param date  the date to format
     * @param buf  the buffer to format into
     * @return the specified string buffer
     */
    public StringBuffer format(Date date, StringBuffer buf) {
        Calendar c = new GregorianCalendar(mTimeZone);
        c.setTime(date);
        return applyRules(c, buf);
    }

    /**
     * <p>Formats a <code>Calendar</code> object into the
     * supplied <code>StringBuffer</code>.</p>
     * 
     * @param calendar  the calendar to format
     * @param buf  the buffer to format into
     * @return the specified string buffer
     */
    public StringBuffer format(Calendar calendar, StringBuffer buf) {
        if (mTimeZoneForced) {
            calendar = (Calendar) calendar.clone();
            calendar.setTimeZone(mTimeZone);
        }
        return applyRules(calendar, buf);
    }

    /**
     * <p>Performs the formatting by applying the rules to the
     * specified calendar.</p>
     * 
     * @param calendar  the calendar to format
     * @param buf  the buffer to format into
     * @return the specified string buffer
     */
    protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
        Rule[] rules = mRules;
        int len = mRules.length;
        for (int i = 0; i < len; i++) {
            rules[i].appendTo(buf, calendar);
        }
        return buf;
    }

    // Parsing
    //-----------------------------------------------------------------------
    /**
     * <p>Parsing is not supported.</p>
     * 
     * @param source  the string to parse
     * @param pos  the parsing position
     * @return <code>null</code> as not supported
     */
    public Object parseObject(String source, ParsePosition pos) {
        pos.setIndex(0);
        pos.setErrorIndex(0);
        return null;
    }
    
    // Accessors
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the pattern used by this formatter.</p>
     * 
     * @return the pattern, {@link java.text.SimpleDateFormat} compatible
     */
    public String getPattern() {
        return mPattern;
    }

    /**
     * <p>Gets the time zone used by this formatter.</p>
     *
     * <p>This zone is always used for <code>Date</code> formatting.
     * If a <code>Calendar</code> is passed in to be formatted, the
     * time zone on that may be used depending on
     * {@link #getTimeZoneOverridesCalendar()}.</p>
     * 
     * @return the time zone
     */
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    /**
     * <p>Returns <code>true</code> if the time zone of the
     * calendar overrides the time zone of the formatter.</p>
     * 
     * @return <code>true</code> if time zone of formatter
     *  overridden for calendars
     */
    public boolean getTimeZoneOverridesCalendar() {
        return mTimeZoneForced;
    }

    /**
     * <p>Gets the locale used by this formatter.</p>
     * 
     * @return the locale
     */
    public Locale getLocale() {
        return mLocale;
    }

    /**
     * <p>Gets an estimate for the maximum string length that the
     * formatter will produce.</p>
     *
     * <p>The actual formatted length will almost always be less than or
     * equal to this amount.</p>
     * 
     * @return the maximum formatted length
     */
    public int getMaxLengthEstimate() {
        return mMaxLengthEstimate;
    }

    // Basics
    //-----------------------------------------------------------------------
    /**
     * <p>Compares two objects for equality.</p>
     * 
     * @param obj  the object to compare to
     * @return <code>true</code> if equal
     */
    public boolean equals(Object obj) {
        if (obj instanceof FastDateFormat == false) {
            return false;
        }
        FastDateFormat other = (FastDateFormat) obj;
        if (
            (mPattern == other.mPattern || mPattern.equals(other.mPattern)) &&
            (mTimeZone == other.mTimeZone || mTimeZone.equals(other.mTimeZone)) &&
            (mLocale == other.mLocale || mLocale.equals(other.mLocale)) &&
            (mTimeZoneForced == other.mTimeZoneForced) &&
            (mLocaleForced == other.mLocaleForced)
            ) {
            return true;
        }
        return false;
    }

    /**
     * <p>Returns a hashcode compatible with equals.</p>
     * 
     * @return a hashcode compatible with equals
     */
    public int hashCode() {
        int total = 0;
        total += mPattern.hashCode();
        total += mTimeZone.hashCode();
        total += (mTimeZoneForced ? 1 : 0);
        total += mLocale.hashCode();
        total += (mLocaleForced ? 1 : 0);
        return total;
    }

    /**
     * <p>Gets a debugging string version of this formatter.</p>
     * 
     * @return a debugging string
     */
    public String toString() {
        return "FastDateFormat[" + mPattern + "]";
    }

    // Serializing
    //-----------------------------------------------------------------------
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }
    
    // Rules
    //-----------------------------------------------------------------------
    /**
     * <p>Inner class defining a rule.</p>
     */
    private interface Rule {
        /**
         * Returns the estimated lentgh of the result.
         * 
         * @return the estimated length
         */
        int estimateLength();
        
        /**
         * Appends the value of the specified calendar to the output buffer based on the rule implementation.
         * 
         * @param buffer the output buffer
         * @param calendar calendar to be appended
         */
        void appendTo(StringBuffer buffer, Calendar calendar);
    }

    /**
     * <p>Inner class defining a numeric rule.</p>
     */
    private interface NumberRule extends Rule {
        /**
         * Appends the specified value to the output buffer based on the rule implementation.
         * 
         * @param buffer the output buffer
         * @param value the value to be appended
         */
        void appendTo(StringBuffer buffer, int value);
    }

    /**
     * <p>Inner class to output a constant single character.</p>
     */
    private static class CharacterLiteral implements Rule {
        private final char mValue;

        /**
         * Constructs a new instance of <code>CharacterLiteral</code>
         * to hold the specified value.
         * 
         * @param value the character literal
         */
        CharacterLiteral(char value) {
            mValue = value;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValue);
        }
    }

    /**
     * <p>Inner class to output a constant string.</p>
     */
    private static class StringLiteral implements Rule {
        private final String mValue;

        /**
         * Constructs a new instance of <code>StringLiteral</code>
         * to hold the specified value.
         * 
         * @param value the string literal
         */
        StringLiteral(String value) {
            mValue = value;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return mValue.length();
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValue);
        }
    }

    /**
     * <p>Inner class to output one of a set of values.</p>
     */
    private static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        /**
         * Constructs an instance of <code>TextField</code>
         * with the specified field and values.
         * 
         * @param field the field
         * @param values the field values
         */
        TextField(int field, String[] values) {
            mField = field;
            mValues = values;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            int max = 0;
            for (int i=mValues.length; --i >= 0; ) {
                int len = mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
            return max;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValues[calendar.get(mField)]);
        }
    }

    /**
     * <p>Inner class to output an unpadded number.</p>
     */
    private static class UnpaddedNumberField implements NumberRule {
        static final UnpaddedNumberField INSTANCE_YEAR = new UnpaddedNumberField(Calendar.YEAR);
        
        private final int mField;

        /**
         * Constructs an instance of <code>UnpadedNumberField</code> with the specified field.
         * 
         * @param field the field
         */
        UnpaddedNumberField(int field) {
            mField = field;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 4;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char)(value + '0'));
            } else if (value < 100) {
                buffer.append((char)(value / 10 + '0'));
                buffer.append((char)(value % 10 + '0'));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    /**
     * <p>Inner class to output an unpadded month.</p>
     */
    private static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        /**
         * Constructs an instance of <code>UnpaddedMonthField</code>.
         *
         */
        UnpaddedMonthField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        /**
         * {@inheritDoc}
         */
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char)(value + '0'));
            } else {
                buffer.append((char)(value / 10 + '0'));
                buffer.append((char)(value % 10 + '0'));
            }
        }
    }

    /**
     * <p>Inner class to output a padded number.</p>
     */
    private static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        /**
         * Constructs an instance of <code>PaddedNumberField</code>.
         * 
         * @param field the field
         * @param size size of the output field
         */
        PaddedNumberField(int field, int size) {
            if (size < 3) {
                // Should use UnpaddedNumberField or TwoDigitNumberField.
                throw new IllegalArgumentException();
            }
            mField = field;
            mSize = size;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 4;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                for (int i = mSize; --i >= 2; ) {
                    buffer.append('0');
                }
                buffer.append((char)(value / 10 + '0'));
                buffer.append((char)(value % 10 + '0'));
            } else {
                int digits;
                if (value < 1000) {
                    digits = 3;
                } else {
                    Validate.isTrue(value > -1, "Negative values should not be possible", value);
                    digits = Integer.toString(value).length();
                }
                for (int i = mSize; --i >= digits; ) {
                    buffer.append('0');
                }
                buffer.append(Integer.toString(value));
            }
        }
    }

    /**
     * <p>Inner class to output a two digit number.</p>
     */
    private static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        /**
         * Constructs an instance of <code>TwoDigitNumberField</code> with the specified field.
         * 
         * @param field the field
         */
        TwoDigitNumberField(int field) {
            mField = field;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                buffer.append((char)(value / 10 + '0'));
                buffer.append((char)(value % 10 + '0'));
            } else {
                buffer.append(Integer.toString(value));
            }
        }
    }

    /**
     * <p>Inner class to output a two digit year.</p>
     */
    private static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        /**
         * Constructs an instance of <code>TwoDigitYearField</code>.
         */
        TwoDigitYearField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.YEAR) % 100);
        }

        /**
         * {@inheritDoc}
         */
        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char)(value / 10 + '0'));
            buffer.append((char)(value % 10 + '0'));
        }
    }

    /**
     * <p>Inner class to output a two digit month.</p>
     */
    private static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        /**
         * Constructs an instance of <code>TwoDigitMonthField</code>.
         */
        TwoDigitMonthField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        /**
         * {@inheritDoc}
         */
        public final void appendTo(StringBuffer buffer, int value) {
            buffer.append((char)(value / 10 + '0'));
            buffer.append((char)(value % 10 + '0'));
        }
    }

    /**
     * <p>Inner class to output the twelve hour field.</p>
     */
    private static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        /**
         * Constructs an instance of <code>TwelveHourField</code> with the specified
         * <code>NumberRule</code>.
         * 
         * @param rule the rule
         */
        TwelveHourField(NumberRule rule) {
            mRule = rule;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return mRule.estimateLength();
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(Calendar.HOUR);
            if (value == 0) {
                value = calendar.getLeastMaximum(Calendar.HOUR) + 1;
            }
            mRule.appendTo(buffer, value);
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, int value) {
            mRule.appendTo(buffer, value);
        }
    }

    /**
     * <p>Inner class to output the twenty four hour field.</p>
     */
    private static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        /**
         * Constructs an instance of <code>TwentyFourHourField</code> with the specified
         * <code>NumberRule</code>.
         * 
         * @param rule the rule
         */
        TwentyFourHourField(NumberRule rule) {
            mRule = rule;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return mRule.estimateLength();
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(Calendar.HOUR_OF_DAY);
            if (value == 0) {
                value = calendar.getMaximum(Calendar.HOUR_OF_DAY) + 1;
            }
            mRule.appendTo(buffer, value);
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, int value) {
            mRule.appendTo(buffer, value);
        }
    }

    /**
     * <p>Inner class to output a time zone name.</p>
     */
    private static class TimeZoneNameRule implements Rule {
        private final TimeZone mTimeZone;
        private final boolean mTimeZoneForced;
        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;

        /**
         * Constructs an instance of <code>TimeZoneNameRule</code> with the specified properties.
         * 
         * @param timeZone the time zone
         * @param timeZoneForced if <code>true</code> the time zone is forced into standard and daylight
         * @param locale the locale
         * @param style the style
         */
        TimeZoneNameRule(TimeZone timeZone, boolean timeZoneForced, Locale locale, int style) {
            mTimeZone = timeZone;
            mTimeZoneForced = timeZoneForced;
            mLocale = locale;
            mStyle = style;

            if (timeZoneForced) {
                mStandard = getTimeZoneDisplay(timeZone, false, style, locale);
                mDaylight = getTimeZoneDisplay(timeZone, true, style, locale);
            } else {
                mStandard = null;
                mDaylight = null;
            }
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            if (mTimeZoneForced) {
                return Math.max(mStandard.length(), mDaylight.length());
            } else if (mStyle == TimeZone.SHORT) {
                return 4;
            } else {
                return 40;
            }
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            if (mTimeZoneForced) {
                if (mTimeZone.useDaylightTime() && calendar.get(Calendar.DST_OFFSET) != 0) {
                    buffer.append(mDaylight);
                } else {
                    buffer.append(mStandard);
                }
            } else {
                TimeZone timeZone = calendar.getTimeZone();
                if (timeZone.useDaylightTime() && calendar.get(Calendar.DST_OFFSET) != 0) {
                    buffer.append(getTimeZoneDisplay(timeZone, true, mStyle, mLocale));
                } else {
                    buffer.append(getTimeZoneDisplay(timeZone, false, mStyle, mLocale));
                }
            }
        }
    }

    /**
     * <p>Inner class to output a time zone as a number <code>+/-HHMM</code>
     * or <code>+/-HH:MM</code>.</p>
     */
    private static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        
        final boolean mColon;
        
        /**
         * Constructs an instance of <code>TimeZoneNumberRule</code> with the specified properties.
         * 
         * @param colon add colon between HH and MM in the output if <code>true</code>
         */
        TimeZoneNumberRule(boolean colon) {
            mColon = colon;
        }

        /**
         * {@inheritDoc}
         */
        public int estimateLength() {
            return 5;
        }

        /**
         * {@inheritDoc}
         */
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
            
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }
            
            int hours = offset / (60 * 60 * 1000);
            buffer.append((char)(hours / 10 + '0'));
            buffer.append((char)(hours % 10 + '0'));
            
            if (mColon) {
                buffer.append(':');
            }
            
            int minutes = offset / (60 * 1000) - 60 * hours;
            buffer.append((char)(minutes / 10 + '0'));
            buffer.append((char)(minutes % 10 + '0'));
        }            
    }

    // ----------------------------------------------------------------------
    /**
     * <p>Inner class that acts as a compound key for time zone names.</p>
     */
    private static class TimeZoneDisplayKey {
        private final TimeZone mTimeZone;
        private final int mStyle;
        private final Locale mLocale;

        /**
         * Constructs an instance of <code>TimeZoneDisplayKey</code> with the specified properties.
         *  
         * @param timeZone the time zone
         * @param daylight adjust the style for daylight saving time if <code>true</code>
         * @param style the timezone style
         * @param locale the timezone locale
         */
        TimeZoneDisplayKey(TimeZone timeZone,
                           boolean daylight, int style, Locale locale) {
            mTimeZone = timeZone;
            if (daylight) {
                style |= 0x80000000;
            }
            mStyle = style;
            mLocale = locale;
        }

        /**
         * {@inheritDoc}
         */
        public int hashCode() {
            return mStyle * 31 + mLocale.hashCode();
        }

        /**
         * {@inheritDoc}
         */
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
                return
                    mTimeZone.equals(other.mTimeZone) &&
                    mStyle == other.mStyle &&
                    mLocale.equals(other.mLocale);
            }
            return false;
        }
    }

    // ----------------------------------------------------------------------
    /**
     * <p>Helper class for creating compound objects.</p>
     *
     * <p>One use for this class is to create a hashtable key
     * out of multiple objects.</p>
     */
    private static class Pair {
        private final Object mObj1;
        private final Object mObj2;

        /**
         * Constructs an instance of <code>Pair</code> to hold the specified objects.
         * @param obj1 one object in the pair
         * @param obj2 second object in the pair
         */
        public Pair(Object obj1, Object obj2) {
            mObj1 = obj1;
            mObj2 = obj2;
        }

        /**
         * {@inheritDoc}
         */
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Pair)) {
                return false;
            }

            Pair key = (Pair)obj;

            return
                (mObj1 == null ?
                 key.mObj1 == null : mObj1.equals(key.mObj1)) &&
                (mObj2 == null ?
                 key.mObj2 == null : mObj2.equals(key.mObj2));
        }

        /**
         * {@inheritDoc}
         */
        public int hashCode() {
            return
                (mObj1 == null ? 0 : mObj1.hashCode()) +
                (mObj2 == null ? 0 : mObj2.hashCode());
        }

        /**
         * {@inheritDoc}
         */
        public String toString() {
            return "[" + mObj1 + ':' + mObj2 + ']';
        }
    }

}

```
