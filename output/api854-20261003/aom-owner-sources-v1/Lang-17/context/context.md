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
        <javac srcdir="${source.home}" destdir="${build.home}/classes" debug="${compile.debug}" deprecation="${compile.deprecation}" target="${compile.target}" source="${compile.source}" excludes="${compile.excludes}" optimize="${compile.optimize}" includeantruntime="false">
            <classpath refid="compile.classpath"/>
        </javac>
        <copy todir="${build.home}/classes" filtering="on">
            <fileset dir="${source.home}" excludes="**/*.java"/>
        </copy>
    </target>

    <target name="compile.tests" depends="compile" description="Compile unit test cases">
        <mkdir dir="${build.home}/tests"/>
        <javac srcdir="${test.home}" destdir="${build.home}/tests" debug="${compile.debug}" deprecation="off" target="${compile.target}" source="${compile.source}" optimize="${compile.optimize}" includeantruntime="false">
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
                    <exclude name="**/Abstract*Test.java"/>
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
                 overview="${source.home}/org/apache/commons/lang3/overview.html" 
                 packagenames="org.apache.commons.*" 
                 excludepackagenames="${javadoc.excludepackagenames}" 
                 author="false" 
                 version="true" 
                 doctitle="&lt;h1&gt;Commons Lang ${component.version}&lt;/h1&gt;"
                 windowtitle="Lang ${component.version}" 
                 bottom="Copyright &amp;copy; 2001-${current.year} - Apache Software Foundation" 
                 use="true" 
                 link="${jdk.javadoc}" 
                 source="${compile.source}">
            <classpath refid="compile.classpath"/>
        </javadoc>
    </target>

    <!-- ========== Jar Targets ========================================= -->
    <target name="jar" depends="compile" description="Create jar">
        <mkdir dir="${build.home}/classes/META-INF"/>
        <copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
        <copy file="NOTICE.txt"  tofile="${build.home}/classes/META-INF/NOTICE.txt"/>
        <jar jarfile="${build.home}/${final.name}.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Lang"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Lang"/>
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
                <attribute name="Specification-Title" value="Commons Lang API"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Lang API"/>
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

    <target name="source-jar" depends="init" description="Create JavaDoc jar">
        <jar jarfile="${build.home}/${final.name}-sources.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Lang Source"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Lang Source"/>
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

    <!-- ========== Distribution ========================================= -->
    <target name="dist" depends="clean,jar,source-jar,javadoc-jar" description="Create binary distribution">

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
                                build.xml,
                                checkstyle.xml,
                                default.properties,
                                LICENSE.txt,
                                NOTICE.txt,
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
<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>21</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.0</version>
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
        <developer>
            <name>Joerg Schaible</name>
            <id>joehni</id>
            <email>joerg.schaible@gmx.de</email>
            <roles>
                <role>Java Developer</role>
            </roles>
            <timezone>+1</timezone>
        </developer>
        <developer>
          <name>Oliver Heger</name>
          <id>oheger</id>
          <email>oheger@apache.org</email>
          <timezone>+1</timezone>
          <roles>
            <role>Java Developer</role>
          </roles>
        </developer>
        <developer>
          <name>Paul Benedict</name>
          <id>pbenedict</id>
          <email>pbenedict@apache.org</email>
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
            <name>Eli Lindsey</name>
        </contributor>
        <contributor>
            <name>Craig R. McClanahan</name>
        </contributor>
        <contributor>
            <name>Rand McNeely</name>
        </contributor>
        <contributor>
            <name>Hendrik Maryns</name>
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
            <name>Valentin Rocher</name>
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
            <name>David M. Sledge</name>
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
        <contributor>
            <name>Derek C. Ashmore</name>
        </contributor>
    </contributors>

  <!-- Lang should depend on very little -->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.7</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>org.easymock</groupId>
      <artifactId>easymock</artifactId>
      <version>2.5.2</version>
      <scope>test</scope>
    </dependency>

  </dependencies>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <maven.compile.source>1.5</maven.compile.source>
    <maven.compile.target>1.5</maven.compile.target>
    <commons.componentid>lang</commons.componentid>
    <commons.release.version>3.0</commons.release.version>
    <commons.release.desc>(Java 5.0+)</commons.release.desc>
    <commons.release.2.version>2.6</commons.release.2.version>
    <commons.release.2.desc>(Java 1.3+)</commons.release.2.desc>
    <commons.jira.id>LANG</commons.jira.id>
    <commons.jira.pid>12310481</commons.jira.pid>
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
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>test-jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-changes-plugin</artifactId>
          <version>2.3</version>
          <configuration>
            <xmlPath>${basedir}/src/site/changes/changes.xml</xmlPath>
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
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.6</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>
      <!-- Requires setting 'export MAVEN_OPTS="-Xmx512m" ' -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.3.1</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.4</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.2.2</version>
        <configuration>
          <comparisonArtifacts>
            <comparisonArtifact>
              <groupId>commons-lang</groupId>
              <artifactId>commons-lang</artifactId>
              <version>2.6</version>
            </comparisonArtifact>
          </comparisonArtifacts>
          <minSeverity>info</minSeverity>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.3</version>
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

## src/main/java/org/apache/commons/lang3/text/translate/CharSequenceTranslator.java

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
package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * An API for translating text. 
 * Its core use is to escape and unescape text. Because escaping and unescaping 
 * is completely contextual, the API does not present two separate signatures.
 * 
 * @since 3.0
 * @version $Id$
 */
public abstract class CharSequenceTranslator {

    /**
     * Translate a set of codepoints, represented by an int index into a CharSequence, 
     * into another set of codepoints. The number of codepoints consumed must be returned, 
     * and the only IOExceptions thrown must be from interacting with the Writer so that 
     * the top level API may reliable ignore StringWriter IOExceptions. 
     *
     * @param input CharSequence that is being translated
     * @param index int representing the current point of translation
     * @param out Writer to translate the text to
     * @return int count of codepoints consumed
     * @throws IOException if and only if the Writer produces an IOException
     */
    public abstract int translate(CharSequence input, int index, Writer out) throws IOException;

    /**
     * Helper for non-Writer usage. 
     * @param input CharSequence to be translated
     * @return String output of translation
     */
    public final String translate(CharSequence input) {
        if (input == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(input.length() * 2);
            translate(input, writer);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Translate an input onto a Writer. This is intentionally final as its algorithm is 
     * tightly coupled with the abstract method of this class. 
     *
     * @param input CharSequence that is being translated
     * @param out Writer to translate the text to
     * @throws IOException if and only if the Writer produces an IOException
     */
    public final void translate(CharSequence input, Writer out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (input == null) {
            return;
        }
        int pos = 0;
        int len = input.length();
        while (pos < len) {
            int consumed = translate(input, pos, out);
            if (consumed == 0) {
                char[] c = Character.toChars(Character.codePointAt(input, pos));
                out.write(c);
                pos+= c.length;
                continue;
            }
//          // contract with translators is that they have to understand codepoints 
//          // and they just took care of a surrogate pair
            for (int pt = 0; pt < consumed; pt++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }

    /**
     * Helper method to create a merger of this translator with another set of 
     * translators. Useful in customizing the standard functionality.
     *
     * @param translators CharSequenceTranslator array of translators to merge with this one
     * @return CharSequenceTranslator merging this translator with the others
     */
    public final CharSequenceTranslator with(CharSequenceTranslator... translators) {
        CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
        newArray[0] = this;
        System.arraycopy(translators, 0, newArray, 1, translators.length);
        return new AggregateTranslator(newArray);
    }

    /**
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     *
     * @param codepoint The codepoint to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    public static String hex(int codepoint) {
        return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
    }

}

```
