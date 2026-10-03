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
    <property name="collections.javadoc" value="http://commons.apache.org/collections/api-release/"/>

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
    	<pathelement location="${commons-io.jar}"/>
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
<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with 
  this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the 
  "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations 
  under the License. -->
<project
  xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>25</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.2-SNAPSHOT</version>
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
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Steven Caswell</name>
      <id>scaswell</id>
      <email>stevencaswell@apache.org</email>
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
      <timezone>-5</timezone>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
      <email>rdonkin@apache.org</email>
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Gary D. Gregory</name>
      <id>ggregory</id>
      <email>ggregory@apache.org</email>
      <timezone>-5</timezone>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Fredrik Westermarck</name>
      <id>fredrik</id>
      <email />
      <organization />
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
      <name>Morgan Delagrange</name>
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
      <name>Roland Foerther</name>
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
      <name>Chas Honton</name>
    </contributor>
    <contributor>
      <name>Chris Hyzer</name>
    </contributor>
    <contributor>
      <name>Paul Jack</name>
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
      <name>David Leppik</name>
    </contributor>
    <contributor>
      <name>Eli Lindsey</name>
    </contributor>
    <contributor>
      <name>Sven Ludwig</name>
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
      <name>Michael A. Smith</name>
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
      <name>Daniel Trebbien</name>
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
      <version>4.10</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.2</version>
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
    <project.build.sourceEncoding>ISO-8859-1</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <maven.compile.source>1.6</maven.compile.source>
    <maven.compile.target>1.6</maven.compile.target>
    <!--
       This is also  used to generate download_xxx file name.
       To override this when generating the download page:
       
       mvn commons:download-page -Dcommons.componentid=lang
       
       The above seems to change the download page name but not any other
       properties that depend on the componentid.
    -->
    <commons.componentid>lang3</commons.componentid>
    <!-- Current 3.x release series -->
    <commons.release.version>3.1</commons.release.version>
    <commons.release.desc>(Java 5.0+)</commons.release.desc>
    <!-- Previous 2.x release series -->
    <commons.release.2.version>2.6</commons.release.2.version>
    <commons.release.2.desc>(Requires Java 1.2 or later)</commons.release.2.desc>
    <!-- Override generated name -->
    <commons.release.2.name>commons-lang-${commons.release.2.version}</commons.release.2.name>
    <commons.jira.id>LANG</commons.jira.id>
    <commons.jira.pid>12310481</commons.jira.pid>
  </properties>


  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <executions>
          <execution>
            <id>plain</id>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
              </includes>
              <runOrder>random</runOrder>
            </configuration>
          </execution>
          <!-- <execution> <id>security-manager-test</id> <phase>integration-test</phase> <goals> <goal>test</goal> </goals> <configuration> 
            <includes> <include>**/*Test.java</include> </includes> <argLine>-Djava.security.manager -Djava.security.policy=${basedir}/src/test/resources/java.policy</argLine> 
            </configuration> </execution> -->
        </executions>
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
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.9.1</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>
      <!-- Requires setting 'export MAVEN_OPTS="-Xmx512m" ' -->
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
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.5.1</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.3</version>
        <configuration>
          <minSeverity>info</minSeverity>
        </configuration>
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

## src/main/java/org/apache/commons/lang3/RandomStringUtils.java

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
package org.apache.commons.lang3;

import java.util.Random;

/**
 * <p>Operations for random {@code String}s.</p>
 * <p>Currently <em>private high surrogate</em> characters are ignored. 
 * These are Unicode characters that fall between the values 56192 (db80)
 * and 56319 (dbff) as we don't know how to handle them. 
 * High and low surrogates are correctly dealt with - that is if a 
 * high surrogate is randomly chosen, 55296 (d800) to 56191 (db7f) 
 * then it is followed by a low surrogate. If a low surrogate is chosen, 
 * 56320 (dc00) to 57343 (dfff) then it is placed after a randomly 
 * chosen high surrogate. </p>
 *
 * <p>#ThreadSafe#</p>
 * @since 1.0
 * @version $Id$
 */
public class RandomStringUtils {

    /**
     * <p>Random object used by random method. This has to be not local
     * to the random method so as to not return the same value in the 
     * same millisecond.</p>
     */
    private static final Random RANDOM = new Random();

    /**
     * <p>{@code RandomStringUtils} instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * {@code RandomStringUtils.random(5);}.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public RandomStringUtils() {
      super();
    }

    // Random
    //-----------------------------------------------------------------------
    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of all characters.</p>
     *
     * @param count  the length of random string to create
     * @return the random string
     */
    public static String random(int count) {
        return random(count, false, false);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of characters whose
     * ASCII value is between {@code 32} and {@code 126} (inclusive).</p>
     *
     * @param count  the length of random string to create
     * @return the random string
     */
    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }
    
    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alphabetic
     * characters.</p>
     *
     * @param count  the length of random string to create
     * @return the random string
     */
    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }
    
    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters.</p>
     *
     * @param count  the length of random string to create
     * @return the random string
     */
    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }
    
    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of numeric
     * characters.</p>
     *
     * @param count  the length of random string to create
     * @return the random string
     */
    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters as indicated by the arguments.</p>
     *
     * @param count  the length of random string to create
     * @param letters  if {@code true}, generated string will include
     *  alphabetic characters
     * @param numbers  if {@code true}, generated string will include
     *  numeric characters
     * @return the random string
     */
    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }
    
    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of alpha-numeric
     * characters as indicated by the arguments.</p>
     *
     * @param count  the length of random string to create
     * @param start  the position in set of chars to start at
     * @param end  the position in set of chars to end before
     * @param letters  if {@code true}, generated string will include
     *  alphabetic characters
     * @param numbers  if {@code true}, generated string will include
     *  numeric characters
     * @return the random string
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    /**
     * <p>Creates a random string based on a variety of options, using
     * default source of randomness.</p>
     *
     * <p>This method has exactly the same semantics as
     * {@link #random(int,int,int,boolean,boolean,char[],Random)}, but
     * instead of using an externally supplied source of randomness, it uses
     * the internal static {@link Random} instance.</p>
     *
     * @param count  the length of random string to create
     * @param start  the position in set of chars to start at
     * @param end  the position in set of chars to end before
     * @param letters  only allow letters?
     * @param numbers  only allow numbers?
     * @param chars  the set of chars to choose randoms from.
     *  If {@code null}, then it will use the set of all chars.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *  {@code (end - start) + 1} characters in the set array.
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    /**
     * <p>Creates a random string based on a variety of options, using
     * supplied source of randomness.</p>
     *
     * <p>If start and end are both {@code 0}, start and end are set
     * to {@code ' '} and {@code 'z'}, the ASCII printable
     * characters, will be used, unless letters and numbers are both
     * {@code false}, in which case, start and end are set to
     * {@code 0} and {@code Integer.MAX_VALUE}.
     *
     * <p>If set is not {@code null}, characters between start and
     * end are chosen.</p>
     *
     * <p>This method accepts a user-supplied {@link Random}
     * instance to use as a source of randomness. By seeding a single 
     * {@link Random} instance with a fixed seed and using it for each call,
     * the same random sequence of strings can be generated repeatedly
     * and predictably.</p>
     *
     * @param count  the length of random string to create
     * @param start  the position in set of chars to start at
     * @param end  the position in set of chars to end before
     * @param letters  only allow letters?
     * @param numbers  only allow numbers?
     * @param chars  the set of chars to choose randoms from, must not be empty.
     *  If {@code null}, then it will use the set of all chars.
     * @param random  a source of randomness.
     * @return the random string
     * @throws ArrayIndexOutOfBoundsException if there are not
     *  {@code (end - start) + 1} characters in the set array.
     * @throws IllegalArgumentException if {@code count} &lt; 0 or the provided chars array is empty.
     * @since 2.0
     */
    public static String random(int count, int start, int end, boolean letters, boolean numbers,
                                char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            } else {
                if (!letters && !numbers) {
                    end = Integer.MAX_VALUE;
                } else {
                    end = 'z' + 1;
                    start = ' ';                
                }
            }
        } else {
            if (end <= start) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
            }
        }

        char[] buffer = new char[count];
        int gap = end - start;

        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(gap) + start];
            }
            if (letters && Character.isLetter(ch)
                    || numbers && Character.isDigit(ch)
                    || !letters && !numbers) {
                if(ch >= 56320 && ch <= 57343) {
                    if(count == 0) {
                        count++;
                    } else {
                        // low surrogate, insert high surrogate after putting it in
                        buffer[count] = ch;
                        count--;
                        buffer[count] = (char) (55296 + random.nextInt(128));
                    }
                } else if(ch >= 55296 && ch <= 56191) {
                    if(count == 0) {
                        count++;
                    } else {
                        // high surrogate, insert low surrogate before putting it in
                        buffer[count] = (char) (56320 + random.nextInt(128));
                        count--;
                        buffer[count] = ch;
                    }
                } else if(ch >= 56192 && ch <= 56319) {
                    // private high surrogate, no effing clue, so skip it
                    count++;
                } else {
                    buffer[count] = ch;
                }
            } else {
                count++;
            }
        }
        return new String(buffer);
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of characters
     * specified by the string, must not be empty. 
     * If null, the set of all characters is used.</p>
     *
     * @param count  the length of random string to create
     * @param chars  the String containing the set of characters to use,
     *  may be null, but must not be empty
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0 or the string is empty.
     */
    public static String random(int count, String chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, chars.toCharArray());
    }

    /**
     * <p>Creates a random string whose length is the number of characters
     * specified.</p>
     *
     * <p>Characters will be chosen from the set of characters specified.</p>
     *
     * @param count  the length of random string to create
     * @param chars  the character array containing the set of characters to use,
     *  may be null
     * @return the random string
     * @throws IllegalArgumentException if {@code count} &lt; 0.
     */
    public static String random(int count, char... chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RANDOM);
    }
    
}

```
