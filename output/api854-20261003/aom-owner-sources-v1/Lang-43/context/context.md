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
	    <tstamp>
	        <format property="current.year" pattern="yyyy"/>
	    </tstamp>
		<javadoc sourcepath="${source.home}" destdir="${dist.home}/docs/api" overview="${source.home}/org/apache/commons/lang/overview.html" packagenames="org.apache.commons.*" excludepackagenames="${javadoc.excludepackagenames}" author="true" version="true" doctitle="&lt;h1&gt;${component.title}&lt;/h1&gt;" windowtitle="${component.title} (Version ${component.version})" bottom="Copyright &amp;copy; 2001-${current.year} - Apache Software Foundation" use="true" link="${jdk.javadoc}" source="${compile.source}">
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
						        test.reflect,
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
	<target name="test.reflect" depends="compile.tests">
		<runTestCase classname="org.apache.commons.lang.reflect.ReflectTestSuite"/>
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
    <version>11</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-lang</groupId>
  <artifactId>commons-lang</artifactId>
  <version>3.0-SNAPSHOT</version>
  <name>Commons Lang</name>

  <inceptionYear>2001</inceptionYear>
    <description>
        Commons Lang, a package of Java utility classes for the
        classes that are in java.lang's hierarchy, or are considered to be so
        standard as to justify existence in java.lang.
    </description>

  <url>http://commons.apache.org/lang/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/LANG</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/lang/trunk</connection>
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
            <email>bayard@apache.org</email>
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
        <developer>
            <name>Niall Pemberton</name>
            <id>niallp</id>
            <roles>
                <role>Java Developer</role>
            </roles>
        </developer>
        <developer>
            <name>Matt Benson</name>
            <id>mbenson</id>
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
            <name>Benjamin Bentmann</name>
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
            <name>Dave Meikle</name>
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
            <name>Robert Scholte</name>
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

  <properties>
    <maven.compile.source>1.3</maven.compile.source>
    <maven.compile.target>1.2</maven.compile.target>
    <commons.componentid>lang</commons.componentid>
    <commons.release.version>2.4</commons.release.version>
    <commons.jira.id>LANG</commons.jira.id>
    <commons.jira.pid>12310481</commons.jira.pid>
  </properties> 


  <build>
    <sourceDirectory>src/java</sourceDirectory>
    <testSourceDirectory>src/test</testSourceDirectory>
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
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.1</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>
      <!--
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>1.2</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.2</version>
      </plugin>
      -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.2.2</version>
        <configuration>
          <comparisonVersion>2.3</comparisonVersion>
          <minSeverity>info</minSeverity>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

</project>

```

## src/java/org/apache/commons/lang/text/ExtendedMessageFormat.java

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
package org.apache.commons.lang.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * Extends <code>java.text.MessageFormat</code> to allow pluggable/additional formatting
 * options for embedded format elements.  Client code should specify a registry
 * of <code>FormatFactory</code> instances associated with <code>String</code>
 * format names.  This registry will be consulted when the format elements are 
 * parsed from the message pattern.  In this way custom patterns can be specified,
 * and the formats supported by <code>java.text.MessageFormat</code> can be overridden
 * at the format and/or format style level (see MessageFormat).  A "format element"
 * embedded in the message pattern is specified (<b>()?</b> signifies optionality):<br />
 * <code>{</code><i>argument-number</i><b>(</b><code>,</code><i>format-name</i><b>(</b><code>,</code><i>format-style</i><b>)?)?</b><code>}</code>
 *
 * <p>
 * <i>format-name</i> and <i>format-style</i> values are trimmed of surrounding whitespace
 * in the manner of <code>java.text.MessageFormat</code>.  If <i>format-name</i> denotes
 * <code>FormatFactory formatFactoryInstance</code> in <code>registry</code>, a <code>Format</code>
 * matching <i>format-name</i> and <i>format-style</i> is requested from
 * <code>formatFactoryInstance</code>.  If this is successful, the <code>Format</code>
 * found is used for this format element.
 * </p>
 *
 * <p>NOTICE: The various subformat mutator methods are considered unnecessary; they exist on the parent
 * class to allow the type of customization which it is the job of this class to provide in
 * a configurable fashion.  These methods have thus been disabled and will throw
 * <code>UnsupportedOperationException</code> if called.
 * </p>
 * 
 * <p>Limitations inherited from <code>java.text.MessageFormat</code>:
 * <ul>
 * <li>When using "choice" subformats, support for nested formatting instructions is limited
 *     to that provided by the base class.</li>
 * <li>Thread-safety of <code>Format</code>s, including <code>MessageFormat</code> and thus
 *     <code>ExtendedMessageFormat</code>, is not guaranteed.</li>
 * </ul>
 * </p>
 * 
 * @author Matt Benson
 * @author Niall Pemberton
 * @since 2.4
 * @version $Id$
 */
public class ExtendedMessageFormat extends MessageFormat {
    private static final long serialVersionUID = -2362048321261811743L;

    private static final String DUMMY_PATTERN = "";
    private static final String ESCAPED_QUOTE = "''";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE = '{';
    private static final char QUOTE = '\'';

    private String toPattern;
    private Map registry;

    /**
     * Create a new ExtendedMessageFormat for the default locale.
     * 
     * @param pattern String
     * @throws IllegalArgumentException in case of a bad pattern.
     */
    public ExtendedMessageFormat(String pattern) {
        this(pattern, Locale.getDefault());
    }

    /**
     * Create a new ExtendedMessageFormat.
     * 
     * @param pattern String
     * @param locale Locale
     * @throws IllegalArgumentException in case of a bad pattern.
     */
    public ExtendedMessageFormat(String pattern, Locale locale) {
        this(pattern, locale, null);
    }

    /**
     * Create a new ExtendedMessageFormat for the default locale.
     * 
     * @param pattern String
     * @param registry Registry of format factories:  Map<String, FormatFactory>
     * @throws IllegalArgumentException in case of a bad pattern.
     */
    public ExtendedMessageFormat(String pattern, Map registry) {
        this(pattern, Locale.getDefault(), registry);
    }

    /**
     * Create a new ExtendedMessageFormat.
     * 
     * @param pattern String
     * @param locale Locale
     * @param registry Registry of format factories:  Map<String, FormatFactory>
     * @throws IllegalArgumentException in case of a bad pattern.
     */
    public ExtendedMessageFormat(String pattern, Locale locale, Map registry) {
        super(DUMMY_PATTERN);
        setLocale(locale);
        this.registry = registry;
        applyPattern(pattern);
    }

    /**
     * {@inheritDoc}
     */
    public String toPattern() {
        return toPattern;
    }

    /**
     * Apply the specified pattern.
     * 
     * @param pattern String
     */
    public final void applyPattern(String pattern) {
        if (registry == null) {
            super.applyPattern(pattern);
            toPattern = super.toPattern();
            return;
        }
        ArrayList foundFormats = new ArrayList();
        ArrayList foundDescriptions = new ArrayList();
        StringBuffer stripCustom = new StringBuffer(pattern.length());

        ParsePosition pos = new ParsePosition(0);
        char[] c = pattern.toCharArray();
        int fmtCount = 0;
        while (pos.getIndex() < pattern.length()) {
            switch (c[pos.getIndex()]) {
            case QUOTE:
                appendQuotedString(pattern, pos, stripCustom, true);
                break;
            case START_FE:
                fmtCount++;
                seekNonWs(pattern, pos);
                int start = pos.getIndex();
                int index = readArgumentIndex(pattern, next(pos));
                stripCustom.append(START_FE).append(index);
                seekNonWs(pattern, pos);
                Format format = null;
                String formatDescription = null;
                if (c[pos.getIndex()] == START_FMT) {
                    formatDescription = parseFormatDescription(pattern,
                            next(pos));
                    format = getFormat(formatDescription);
                    if (format == null) {
                        stripCustom.append(START_FMT).append(formatDescription);
                    }
                }
                foundFormats.add(format);
                foundDescriptions.add(format == null ? null : formatDescription);
                Validate.isTrue(foundFormats.size() == fmtCount);
                Validate.isTrue(foundDescriptions.size() == fmtCount);
                if (c[pos.getIndex()] != END_FE) {
                    throw new IllegalArgumentException(
                            "Unreadable format element at position " + start);
                }
                // fall through
            default:
                stripCustom.append(c[pos.getIndex()]);
                next(pos);
            }
        }
        super.applyPattern(stripCustom.toString());
        toPattern = insertFormats(super.toPattern(), foundDescriptions);
        if (containsElements(foundFormats)) {
            Format[] origFormats = getFormats();
            // only loop over what we know we have, as MessageFormat on Java 1.3 
            // seems to provide an extra format element:
            int i = 0;
            for (Iterator it = foundFormats.iterator(); it.hasNext(); i++) {
                Format f = (Format) it.next();
                if (f != null) {
                    origFormats[i] = f;
                }
            }
            super.setFormats(origFormats);
        }
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException
     */
    public void setFormat(int formatElementIndex, Format newFormat) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException
     */
    public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException
     */
    public void setFormats(Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * @throws UnsupportedOperationException
     */
    public void setFormatsByArgumentIndex(Format[] newFormats) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get a custom format from a format description.
     * 
     * @param desc String
     * @return Format
     */
    private Format getFormat(String desc) {
        if (registry != null) {
            String name = desc;
            String args = null;
            int i = desc.indexOf(START_FMT);
            if (i > 0) {
                name = desc.substring(0, i).trim();
                args = desc.substring(i + 1).trim();
            }
            FormatFactory factory = (FormatFactory) registry.get(name);
            if (factory != null) {
                return factory.getFormat(name, args, getLocale());
            }
        }
        return null;
    }

    /**
     * Read the argument index from the current format element
     * 
     * @param pattern pattern to parse
     * @param pos current parse position
     * @return argument index
     */
    private int readArgumentIndex(String pattern, ParsePosition pos) {
        int start = pos.getIndex();
        seekNonWs(pattern, pos);
        StringBuffer result = new StringBuffer();
        boolean error = false;
        for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
            char c = pattern.charAt(pos.getIndex());
            if (Character.isWhitespace(c)) {
                seekNonWs(pattern, pos);
                c = pattern.charAt(pos.getIndex());
                if (c != START_FMT && c != END_FE) {
                    error = true;
                    continue;
                }
            }
            if ((c == START_FMT || c == END_FE) && result.length() > 0) {
                try {
                    return Integer.parseInt(result.toString());
                } catch (NumberFormatException e) {
                    // we've already ensured only digits, so unless something
                    // outlandishly large was specified we should be okay.
                }
            }
            error = !Character.isDigit(c);
            result.append(c);
        }
        if (error) {
            throw new IllegalArgumentException(
                    "Invalid format argument index at position " + start + ": "
                            + pattern.substring(start, pos.getIndex()));
        }
        throw new IllegalArgumentException(
                "Unterminated format element at position " + start);
    }

    /**
     * Parse the format component of a format element.
     * 
     * @param pattern string to parse
     * @param pos current parse position
     * @return Format description String
     */
    private String parseFormatDescription(String pattern, ParsePosition pos) {
        int start = pos.getIndex();
        seekNonWs(pattern, pos);
        int text = pos.getIndex();
        int depth = 1;
        for (; pos.getIndex() < pattern.length(); next(pos)) {
            switch (pattern.charAt(pos.getIndex())) {
            case START_FE:
                depth++;
                break;
            case END_FE:
                depth--;
                if (depth == 0) {
                    return pattern.substring(text, pos.getIndex());
                }
                break;
            case QUOTE:
                getQuotedString(pattern, pos, false);
                break;
            }
        }
        throw new IllegalArgumentException(
                "Unterminated format element at position " + start);
    }

    /**
     * Insert formats back into the pattern for toPattern() support.
     *
     * @param pattern source
     * @param customPatterns The custom patterns to re-insert, if any
     * @return full pattern
     */
    private String insertFormats(String pattern, ArrayList customPatterns) {
        if (!containsElements(customPatterns)) {
            return pattern;
        }
        StringBuffer sb = new StringBuffer(pattern.length() * 2);
        ParsePosition pos = new ParsePosition(0);
        int fe = -1;
        int depth = 0;
        while (pos.getIndex() < pattern.length()) {
            char c = pattern.charAt(pos.getIndex());
            switch (c) {
            case QUOTE:
                appendQuotedString(pattern, pos, sb, false);
                break;
            case START_FE:
                depth++;
                if (depth == 1) {
                    fe++;
                    sb.append(START_FE).append(
                            readArgumentIndex(pattern, next(pos)));
                    String customPattern = (String) customPatterns.get(fe);
                    if (customPattern != null) {
                        sb.append(START_FMT).append(customPattern);
                    }
                }
                break;
            case END_FE:
                depth--;
                //fall through:
            default:
                sb.append(c);
                next(pos);
            }
        }
        return sb.toString();
    }

    /**
     * Consume whitespace from the current parse position.
     * 
     * @param pattern String to read
     * @param pos current position
     */
    private void seekNonWs(String pattern, ParsePosition pos) {
        int len = 0;
        char[] buffer = pattern.toCharArray();
        do {
            len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
            pos.setIndex(pos.getIndex() + len);
        } while (len > 0 && pos.getIndex() < pattern.length());
    }

    /**
     * Convenience method to advance parse position by 1
     * 
     * @param pos ParsePosition
     * @return <code>pos</code>
     */
    private ParsePosition next(ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }

    /**
     * Consume a quoted string, adding it to <code>appendTo</code> if
     * specified.
     * 
     * @param pattern pattern to parse
     * @param pos current parse position
     * @param appendTo optional StringBuffer to append
     * @param escapingOn whether to process escaped quotes
     * @return <code>appendTo</code>
     */
    private StringBuffer appendQuotedString(String pattern, ParsePosition pos,
            StringBuffer appendTo, boolean escapingOn) {
        int start = pos.getIndex();
        char[] c = pattern.toCharArray();
        if (escapingOn && c[start] == QUOTE) {
            next(pos);
            return appendTo == null ? null : appendTo.append(QUOTE);
        }
        int lastHold = start;
        for (int i = pos.getIndex(); i < pattern.length(); i++) {
            if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
                appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(
                        QUOTE);
                pos.setIndex(i + ESCAPED_QUOTE.length());
                lastHold = pos.getIndex();
                continue;
            }
            switch (c[pos.getIndex()]) {
            case QUOTE:
                next(pos);
                return appendTo == null ? null : appendTo.append(c, lastHold,
                        pos.getIndex() - lastHold);
            default:
                next(pos);
            }
        }
        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + start);
    }

    /**
     * Consume quoted string only
     * 
     * @param pattern pattern to parse
     * @param pos current parse position
     * @param escapingOn whether to process escaped quotes
     */
    private void getQuotedString(String pattern, ParsePosition pos,
            boolean escapingOn) {
        appendQuotedString(pattern, pos, null, escapingOn);
    }

    /**
     * Learn whether the specified Collection contains non-null elements.
     * @param coll to check
     * @return <code>true</code> if some Object was found, <code>false</code> otherwise.
     */
    private boolean containsElements(Collection coll) {
        if (coll == null || coll.size() == 0) {
            return false;
        }
        for (Iterator iter = coll.iterator(); iter.hasNext();) {
            if (iter.next() != null) {
                return true;
            }
        }
        return false;
    }
}

```
