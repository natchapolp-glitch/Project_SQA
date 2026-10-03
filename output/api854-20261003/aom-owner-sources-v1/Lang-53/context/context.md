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
   "Lang" component of the Apache Commons Subproject
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
	<target name="dist" depends="clean,compile,javadoc" description="Create binary distribution">
		<mkdir dir="${dist.home}"/>
		<copy file="LICENSE.txt" todir="${dist.home}"/>
		<copy file="NOTICE.txt" todir="${dist.home}"/>
		<copy file="RELEASE-NOTES.txt" todir="${dist.home}"/>
		<antcall target="jar"/>
        <copy file="${dist.home}/${final.name}.jar" todir="."/>
        <copy todir="${source.home}/META-INF">
          <fileset dir="${basedir}" includes="LICENSE*, NOTICE*" />
        </copy>
		<jar jarfile="${final.name}-sources.jar" basedir="${source.home}" manifest="${build.home}/conf/MANIFEST.MF"/>
        <delete dir="${source.home}/META-INF"/>
		<copy file="${final.name}-sources.jar" todir="${dist.home}"/>
        <copy todir="${dist.home}/docs/api/META-INF">
          <fileset dir="${basedir}" includes="LICENSE*, NOTICE*" />
        </copy>
		<jar jarfile="${final.name}-javadoc.jar" basedir="${dist.home}/docs/api" manifest="${build.home}/conf/MANIFEST.MF"/>
        <delete dir="${dist.home}/docs/api/META-INF"/>
		<copy file="${final.name}-javadoc.jar" todir="${dist.home}"/>
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
    <version>3</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-lang</groupId>
  <artifactId>commons-lang</artifactId>
  <version>3.0-SNAPSHOT</version>
  <name>Lang</name>

  <inceptionYear>2001</inceptionYear>
    <description>
        Commons.Lang, a package of Java utility classes for the
        classes that are in java.lang's hierarchy, or are considered to be so
        standard as to justify existence in java.lang.
    </description>

  <url>http://commons.apache.org/lang/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/LANG</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/lang/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/lang/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/lang/trunk</url>
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
maven.xdoc.developmentProcessUrl=http://commons.apache.org/charter.html
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

## src/java/org/apache/commons/lang/time/DateUtils.java

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

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TimeZone;

/**
 * <p>A suite of utilities surrounding the use of the
 * {@link java.util.Calendar} and {@link java.util.Date} object.</p>
 *
 * @author <a href="mailto:sergek@lokitech.com">Serge Knystautas</a>
 * @author Stephen Colebourne
 * @author Janek Bogucki
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @author Phil Steitz
 * @since 2.0
 * @version $Id$
 */
public class DateUtils {
    
    /**
     * The UTC time zone  (often referred to as GMT).
     */
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    /**
     * Number of milliseconds in a standard second.
     * @since 2.1
     */
    public static final long MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     * @since 2.1
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     * @since 2.1
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     * @since 2.1
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    /**
     * This is half a month, so this represents whether a date is in the top
     * or bottom half of the month.
     */
    public final static int SEMI_MONTH = 1001;

    private static final int[][] fields = {
            {Calendar.MILLISECOND},
            {Calendar.SECOND},
            {Calendar.MINUTE},
            {Calendar.HOUR_OF_DAY, Calendar.HOUR},
            {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM 
                /* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
            },
            {Calendar.MONTH, DateUtils.SEMI_MONTH},
            {Calendar.YEAR},
            {Calendar.ERA}};

    /**
     * A week range, starting on Sunday.
     */
    public final static int RANGE_WEEK_SUNDAY = 1;

    /**
     * A week range, starting on Monday.
     */
    public final static int RANGE_WEEK_MONDAY = 2;

    /**
     * A week range, starting on the day focused.
     */
    public final static int RANGE_WEEK_RELATIVE = 3;

    /**
     * A week range, centered around the day focused.
     */
    public final static int RANGE_WEEK_CENTER = 4;

    /**
     * A month range, the week starting on Sunday.
     */
    public final static int RANGE_MONTH_SUNDAY = 5;

    /**
     * A month range, the week starting on Monday.
     */
    public final static int RANGE_MONTH_MONDAY = 6;

    /**
     * <p><code>DateUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>DateUtils.parse(str);</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public DateUtils() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Checks if two date objects are on the same day ignoring time.</p>
     *
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     * 
     * @param date1  the first date, not altered, not null
     * @param date2  the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendar objects are on the same day ignoring time.</p>
     *
     * <p>28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     * </p>
     * 
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Checks if two date objects represent the same instant in time.</p>
     *
     * <p>This method compares the long millisecond time of the two objects.</p>
     * 
     * @param date1  the first date, not altered, not null
     * @param date2  the second date, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameInstant(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return date1.getTime() == date2.getTime();
    }

    /**
     * <p>Checks if two calendar objects represent the same instant in time.</p>
     *
     * <p>This method compares the long millisecond time of the two objects.</p>
     * 
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return cal1.getTime().getTime() == cal2.getTime().getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Checks if two calendar objects represent the same local time.</p>
     *
     * <p>This method compares the values of the fields of the two objects.
     * In addition, both calendars must be the same of the same type.</p>
     * 
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is <code>null</code>
     * @since 2.1
     */
    public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
                cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
                cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
                cal1.get(Calendar.HOUR) == cal2.get(Calendar.HOUR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.getClass() == cal2.getClass());
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Parses a string representing a date by trying a variety of different parsers.</p>
     * 
     * <p>The parse will try each parse pattern in turn.
     * A parse is only deemed sucessful if it parses the whole of the input string.
     * If no parse patterns match, a ParseException is thrown.</p>
     * 
     * @param str  the date to parse, not null
     * @param parsePatterns  the date format patterns to use, see SimpleDateFormat, not null
     * @return the parsed date
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws ParseException if none of the date patterns were suitable
     */
    public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        
        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parsePatterns[0]);
            } else {
                parser.applyPattern(parsePatterns[i]);
            }
            pos.setIndex(0);
            Date date = parser.parse(str, pos);
            if (date != null && pos.getIndex() == str.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of years to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of months to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of weeks to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of days to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of hours to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of minutes to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of seconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of milliseconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new date object with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Round this date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the datetime of 28 Mar 2002
     * 13:45:01.231, if this was passed with HOUR, it would return
     * 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it
     * would return 1 April 2002 0:00:00.000.</p>
     * 
     * <p>For a date in a timezone that handles the change to daylight
     * saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows.
     * Suppose daylight saving time begins at 02:00 on March 30. Rounding a 
     * date that crosses this time would produce the following values:
     * <ul>
     * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
     * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
     * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
     * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
     * </ul>
     * </p>
     * 
     * @param date  the date to work with
     * @param field  the field from <code>Calendar</code>
     *  or <code>SEMI_MONTH</code>
     * @return the rounded date
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date round(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, true);
        return gval.getTime();
    }

    /**
     * <p>Round this date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the datetime of 28 Mar 2002
     * 13:45:01.231, if this was passed with HOUR, it would return
     * 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it
     * would return 1 April 2002 0:00:00.000.</p>
     * 
     * <p>For a date in a timezone that handles the change to daylight
     * saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows.
     * Suppose daylight saving time begins at 02:00 on March 30. Rounding a 
     * date that crosses this time would produce the following values:
     * <ul>
     * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
     * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
     * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
     * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
     * </ul>
     * </p>
     * 
     * @param date  the date to work with
     * @param field  the field from <code>Calendar</code>
     *  or <code>SEMI_MONTH</code>
     * @return the rounded date (a different object)
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Calendar round(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar rounded = (Calendar) date.clone();
        modify(rounded, field, true);
        return rounded;
    }

    /**
     * <p>Round this date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the datetime of 28 Mar 2002
     * 13:45:01.231, if this was passed with HOUR, it would return
     * 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it
     * would return 1 April 2002 0:00:00.000.</p>
     * 
     * <p>For a date in a timezone that handles the change to daylight
     * saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows.
     * Suppose daylight saving time begins at 02:00 on March 30. Rounding a 
     * date that crosses this time would produce the following values:
     * <ul>
     * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
     * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
     * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
     * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
     * </ul>
     * </p>
     * 
     * @param date  the date to work with, either Date or Calendar
     * @param field  the field from <code>Calendar</code>
     *  or <code>SEMI_MONTH</code>
     * @return the rounded date
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ClassCastException if the object type is not a <code>Date</code>
     *  or <code>Calendar</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date round(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (date instanceof Date) {
            return round((Date) date, field);
        } else if (date instanceof Calendar) {
            return round((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not round " + date);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Truncate this date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the datetime of 28 Mar 2002
     * 13:45:01.231, if you passed with HOUR, it would return 28 Mar
     * 2002 13:00:00.000.  If this was passed with MONTH, it would
     * return 1 Mar 2002 0:00:00.000.</p>
     * 
     * @param date  the date to work with
     * @param field  the field from <code>Calendar</code>
     *  or <code>SEMI_MONTH</code>
     * @return the rounded date
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date truncate(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, false);
        return gval.getTime();
    }

    /**
     * <p>Truncate this date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the datetime of 28 Mar 2002
     * 13:45:01.231, if you passed with HOUR, it would return 28 Mar
     * 2002 13:00:00.000.  If this was passed with MONTH, it would
     * return 1 Mar 2002 0:00:00.000.</p>
     * 
     * @param date  the date to work with
     * @param field  the field from <code>Calendar</code>
     *  or <code>SEMI_MONTH</code>
     * @return the rounded date (a different object)
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Calendar truncate(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar truncated = (Calendar) date.clone();
        modify(truncated, field, false);
        return truncated;
    }

    /**
     * <p>Truncate this date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the datetime of 28 Mar 2002
     * 13:45:01.231, if you passed with HOUR, it would return 28 Mar
     * 2002 13:00:00.000.  If this was passed with MONTH, it would
     * return 1 Mar 2002 0:00:00.000.</p>
     * 
     * @param date  the date to work with, either <code>Date</code>
     *  or <code>Calendar</code>
     * @param field  the field from <code>Calendar</code>
     *  or <code>SEMI_MONTH</code>
     * @return the rounded date
     * @throws IllegalArgumentException if the date
     *  is <code>null</code>
     * @throws ClassCastException if the object type is not a
     *  <code>Date</code> or <code>Calendar</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date truncate(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (date instanceof Date) {
            return truncate((Date) date, field);
        } else if (date instanceof Calendar) {
            return truncate((Calendar) date, field).getTime();
        } else {
            throw new ClassCastException("Could not truncate " + date);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Internal calculation method.</p>
     * 
     * @param val  the calendar
     * @param field  the field constant
     * @param round  true to round, false to truncate
     * @throws ArithmeticException if the year is over 280 million
     */
    private static void modify(Calendar val, int field, boolean round) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }
        
        if (field == Calendar.MILLISECOND) {
            return;
        }

        // ----------------- Fix for LANG-59 ---------------------- START ---------------
        // see http://issues.apache.org/jira/browse/LANG-59
        //
        // Manually truncate milliseconds, seconds and minutes, rather than using
        // Calendar methods.

        Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;

        // truncate milliseconds
        int millisecs = val.get(Calendar.MILLISECOND);
        if (!round || millisecs < 500) {
            time = time - millisecs;
        }
        if (field == Calendar.SECOND) {
            done = true;
        }

        // truncate seconds
        int seconds = val.get(Calendar.SECOND);
        if (!done && (!round || seconds < 30)) {
            time = time - (seconds * 1000L);
        }
        if (field == Calendar.MINUTE) {
            done = true;
        }

        // truncate minutes
        int minutes = val.get(Calendar.MINUTE);
        if (!done && (!round || minutes < 30)) {
            time = time - (minutes * 60000L);
        }

        // reset time
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }
        // ----------------- Fix for LANG-59 ----------------------- END ----------------

        boolean roundUp = false;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] == field) {
                    //This is our field... we stop looping
                    if (round && roundUp) {
                        if (field == DateUtils.SEMI_MONTH) {
                            //This is a special case that's hard to generalize
                            //If the date is 1, we round up to 16, otherwise
                            //  we subtract 15 days and add 1 month
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                        } else {
                            //We need at add one to this field since the
                            //  last number causes us to round up
                            val.add(fields[i][0], 1);
                        }
                    }
                    return;
                }
            }
            //We have various fields that are not easy roundings
            int offset = 0;
            boolean offsetSet = false;
            //These are special types of fields that require different rounding rules
            switch (field) {
                case DateUtils.SEMI_MONTH:
                    if (fields[i][0] == Calendar.DATE) {
                        //If we're going to drop the DATE field's value,
                        //  we want to do this our own way.
                        //We need to subtrace 1 since the date has a minimum of 1
                        offset = val.get(Calendar.DATE) - 1;
                        //If we're above 15 days adjustment, that means we're in the
                        //  bottom half of the month and should stay accordingly.
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        //Record whether we're in the top or bottom half of that range
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (fields[i][0] == Calendar.HOUR_OF_DAY) {
                        //If we're going to drop the HOUR field's value,
                        //  we want to do this our own way.
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset > 6;
                        offsetSet = true;
                    }
                    break;
            }
            if (!offsetSet) {
                int min = val.getActualMinimum(fields[i][0]);
                int max = val.getActualMaximum(fields[i][0]);
                //Calculate the offset from the minimum allowed value
                offset = val.get(fields[i][0]) - min;
                //Set roundUp if this is more than half way between the minimum and maximum
                roundUp = offset > ((max - min) / 2);
            }
            //We need to remove this field
            if (offset != 0) {
                val.set(fields[i][0], val.get(fields[i][0]) - offset);
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");

    }

    //-----------------------------------------------------------------------
    /**
     * <p>This constructs an <code>Iterator</code> over each day in a date
     * range defined by a focus date and range style.</p>
     *
     * <p>For instance, passing Thursday, July 4, 2002 and a
     * <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code>
     * that starts with Sunday, June 30, 2002 and ends with Saturday, August 3,
     * 2002, returning a Calendar instance for each intermediate day.</p>
     *
     * <p>This method provides an iterator that returns Calendar objects.
     * The days are progressed using {@link Calendar#add(int, int)}.</p>
     *
     * @param focus  the date to work with, not null
     * @param rangeStyle  the style constant to use. Must be one of
     * {@link DateUtils#RANGE_MONTH_SUNDAY}, 
     * {@link DateUtils#RANGE_MONTH_MONDAY},
     * {@link DateUtils#RANGE_WEEK_SUNDAY},
     * {@link DateUtils#RANGE_WEEK_MONDAY},
     * {@link DateUtils#RANGE_WEEK_RELATIVE},
     * {@link DateUtils#RANGE_WEEK_CENTER}
     * @return the date iterator, which always returns Calendar instances
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws IllegalArgumentException if the rangeStyle is invalid
     */
    public static Iterator iterator(Date focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(focus);
        return iterator(gval, rangeStyle);
    }

    /**
     * <p>This constructs an <code>Iterator</code> over each day in a date
     * range defined by a focus date and range style.</p>
     *
     * <p>For instance, passing Thursday, July 4, 2002 and a
     * <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code>
     * that starts with Sunday, June 30, 2002 and ends with Saturday, August 3,
     * 2002, returning a Calendar instance for each intermediate day.</p>
     *
     * <p>This method provides an iterator that returns Calendar objects.
     * The days are progressed using {@link Calendar#add(int, int)}.</p>
     *
     * @param focus  the date to work with
     * @param rangeStyle  the style constant to use. Must be one of
     * {@link DateUtils#RANGE_MONTH_SUNDAY}, 
     * {@link DateUtils#RANGE_MONTH_MONDAY},
     * {@link DateUtils#RANGE_WEEK_SUNDAY},
     * {@link DateUtils#RANGE_WEEK_MONDAY},
     * {@link DateUtils#RANGE_WEEK_RELATIVE},
     * {@link DateUtils#RANGE_WEEK_CENTER}
     * @return the date iterator
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws IllegalArgumentException if the rangeStyle is invalid
     */
    public static Iterator iterator(Calendar focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar start = null;
        Calendar end = null;
        int startCutoff = Calendar.SUNDAY;
        int endCutoff = Calendar.SATURDAY;
        switch (rangeStyle) {
            case RANGE_MONTH_SUNDAY:
            case RANGE_MONTH_MONDAY:
                //Set start to the first of the month
                start = truncate(focus, Calendar.MONTH);
                //Set end to the last of the month
                end = (Calendar) start.clone();
                end.add(Calendar.MONTH, 1);
                end.add(Calendar.DATE, -1);
                //Loop start back to the previous sunday or monday
                if (rangeStyle == RANGE_MONTH_MONDAY) {
                    startCutoff = Calendar.MONDAY;
                    endCutoff = Calendar.SUNDAY;
                }
                break;
            case RANGE_WEEK_SUNDAY:
            case RANGE_WEEK_MONDAY:
            case RANGE_WEEK_RELATIVE:
            case RANGE_WEEK_CENTER:
                //Set start and end to the current date
                start = truncate(focus, Calendar.DATE);
                end = truncate(focus, Calendar.DATE);
                switch (rangeStyle) {
                    case RANGE_WEEK_SUNDAY:
                        //already set by default
                        break;
                    case RANGE_WEEK_MONDAY:
                        startCutoff = Calendar.MONDAY;
                        endCutoff = Calendar.SUNDAY;
                        break;
                    case RANGE_WEEK_RELATIVE:
                        startCutoff = focus.get(Calendar.DAY_OF_WEEK);
                        endCutoff = startCutoff - 1;
                        break;
                    case RANGE_WEEK_CENTER:
                        startCutoff = focus.get(Calendar.DAY_OF_WEEK) - 3;
                        endCutoff = focus.get(Calendar.DAY_OF_WEEK) + 3;
                        break;
                }
                break;
            default:
                throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
        }
        if (startCutoff < Calendar.SUNDAY) {
            startCutoff += 7;
        }
        if (startCutoff > Calendar.SATURDAY) {
            startCutoff -= 7;
        }
        if (endCutoff < Calendar.SUNDAY) {
            endCutoff += 7;
        }
        if (endCutoff > Calendar.SATURDAY) {
            endCutoff -= 7;
        }
        while (start.get(Calendar.DAY_OF_WEEK) != startCutoff) {
            start.add(Calendar.DATE, -1);
        }
        while (end.get(Calendar.DAY_OF_WEEK) != endCutoff) {
            end.add(Calendar.DATE, 1);
        }
        return new DateIterator(start, end);
    }

    /**
     * <p>This constructs an <code>Iterator</code> over each day in a date
     * range defined by a focus date and range style.</p>
     *
     * <p>For instance, passing Thursday, July 4, 2002 and a
     * <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code>
     * that starts with Sunday, June 30, 2002 and ends with Saturday, August 3,
     * 2002, returning a Calendar instance for each intermediate day.</p>
     *
     * @param focus  the date to work with, either
     *  <code>Date</code> or <code>Calendar</code>
     * @param rangeStyle  the style constant to use. Must be one of the range
     * styles listed for the {@link #iterator(Calendar, int)} method.
     * @return the date iterator
     * @throws IllegalArgumentException if the date
     *  is <code>null</code>
     * @throws ClassCastException if the object type is
     *  not a <code>Date</code> or <code>Calendar</code>
     */
    public static Iterator iterator(Object focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        if (focus instanceof Date) {
            return iterator((Date) focus, rangeStyle);
        } else if (focus instanceof Calendar) {
            return iterator((Calendar) focus, rangeStyle);
        } else {
            throw new ClassCastException("Could not iterate based on " + focus);
        }
    }

    /**
     * <p>Date iterator.</p>
     */
    static class DateIterator implements Iterator {
        private final Calendar endFinal;
        private final Calendar spot;
        
        /**
         * Constructs a DateIterator that ranges from one date to another. 
         *
         * @param startFinal start date (inclusive)
         * @param endFinal end date (not inclusive)
         */
        DateIterator(Calendar startFinal, Calendar endFinal) {
            super();
            this.endFinal = endFinal;
            spot = startFinal;
            spot.add(Calendar.DATE, -1);
        }

        /**
         * Has the iterator not reached the end date yet?
         *
         * @return <code>true</code> if the iterator has yet to reach the end date
         */
        public boolean hasNext() {
            return spot.before(endFinal);
        }

        /**
         * Return the next calendar in the iteration
         *
         * @return Object calendar for the next date
         */
        public Object next() {
            if (spot.equals(endFinal)) {
                throw new NoSuchElementException();
            }
            spot.add(Calendar.DATE, 1);
            return spot.clone();
        }

        /**
         * Always throws UnsupportedOperationException.
         * 
         * @throws UnsupportedOperationException
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    //------------------------------------------------------------------------- 
    // Deprecated int constants
    // TODO: Remove in 3.0
    
    /**
     * Number of milliseconds in a standard second.
     * 
     * @deprecated Use MILLIS_PER_SECOND. This will be removed in Commons Lang 3.0.
     */
    public static final int MILLIS_IN_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     * 
     * @deprecated Use MILLIS_PER_MINUTE. This will be removed in Commons Lang 3.0.
     */
    public static final int MILLIS_IN_MINUTE = 60 * 1000;
    /**
     * Number of milliseconds in a standard hour.
     * 
     * @deprecated Use MILLIS_PER_HOUR. This will be removed in Commons Lang 3.0.
     */
    public static final int MILLIS_IN_HOUR = 60 * 60 * 1000;
    /**
     * Number of milliseconds in a standard day.
     * 
     * @deprecated Use MILLIS_PER_DAY. This will be removed in Commons Lang 3.0.
     */
    public static final int MILLIS_IN_DAY = 24 * 60 * 60 * 1000;
    
}

```
