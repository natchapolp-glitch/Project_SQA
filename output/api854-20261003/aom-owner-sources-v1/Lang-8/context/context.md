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
    <version>26</version>
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
      <name>Duncan Jones</name>
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
      <version>2.4</version>
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
        <version>2.5.2</version>
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
        <version>2.4</version>
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

## src/main/java/org/apache/commons/lang3/time/FastDatePrinter.java

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
package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.Validate;

/**
 * <p>FastDatePrinter is a fast and thread-safe version of
 * {@link java.text.SimpleDateFormat}.</p>
 *
 * <p>This class can be used as a direct replacement to
 * {@code SimpleDateFormat} in most formatting situations.
 * This class is especially useful in multi-threaded server environments.
 * {@code SimpleDateFormat} is not thread-safe in any JDK version,
 * nor will it be as Sun have closed the bug/RFE.
 * </p>
 *
 * <p>Only formatting is supported, but all patterns are compatible with
 * SimpleDateFormat (except time zones and some year patterns - see below).</p>
 *
 * <p>Java 1.4 introduced a new pattern letter, {@code 'Z'}, to represent
 * time zones in RFC822 format (eg. {@code +0800} or {@code -1100}).
 * This pattern letter can be used here (on all JDK versions).</p>
 *
 * <p>In addition, the pattern {@code 'ZZ'} has been made to represent
 * ISO8601 full format time zones (eg. {@code +08:00} or {@code -11:00}).
 * This introduces a minor incompatibility with Java 1.4, but at a gain of
 * useful functionality.</p>
 *
 * <p>Javadoc cites for the year pattern: <i>For formatting, if the number of
 * pattern letters is 2, the year is truncated to 2 digits; otherwise it is
 * interpreted as a number.</i> Starting with Java 1.7 a pattern of 'Y' or
 * 'YYY' will be formatted as '2003', while it was '03' in former Java
 * versions. FastDatePrinter implements the behavior of Java 7.</p>
 *
 * @since 3.2
 */
public class FastDatePrinter implements DatePrinter, Serializable {
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

    /**
     * The pattern.
     */
    private final String mPattern;
    /**
     * The time zone.
     */
    private final TimeZone mTimeZone;
    /**
     * The locale.
     */
    private final Locale mLocale;
    /**
     * The parsed rules.
     */
    private transient Rule[] mRules;
    /**
     * The estimated maximum length.
     */
    private transient int mMaxLengthEstimate;

    // Constructor
    //-----------------------------------------------------------------------
    /**
     * <p>Constructs a new FastDatePrinter.</p>
     *
     * @param pattern  {@link java.text.SimpleDateFormat} compatible pattern
     * @param timeZone  non-null time zone to use
     * @param locale  non-null locale to use
     * @throws NullPointerException if pattern, timeZone, or locale is null.
     */
    protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
        mPattern = pattern;
        mTimeZone = timeZone;
        mLocale = locale;

        init();
    }

    /**
     * <p>Initializes the instance for first use.</p>
     */
    private void init() {
        List<Rule> rulesList = parsePattern();
        mRules = rulesList.toArray(new Rule[rulesList.size()]);

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
     * @return a {@code List} of Rule objects
     * @throws IllegalArgumentException if pattern is invalid
     */
    protected List<Rule> parsePattern() {
        DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
        List<Rule> rules = new ArrayList<Rule>();

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
                if (tokenLen == 2) {
                    rule = TwoDigitYearField.INSTANCE;
                } else {
                    rule = selectNumberRule(Calendar.YEAR, tokenLen < 4 ? 4 : tokenLen);
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
                    rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.LONG);
                } else {
                    rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.SHORT);
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
        StringBuilder buf = new StringBuilder();

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
     * <p>Formats a {@code Date}, {@code Calendar} or
     * {@code Long} (milliseconds) object.</p>
     *
     * @param obj  the object to format
     * @param toAppendTo  the buffer to append to
     * @param pos  the position - ignored
     * @return the buffer passed in
     */
    @Override
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

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#format(long)
     */
    @Override
    public String format(long millis) {
        return format(new Date(millis));
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#format(java.util.Date)
     */
    @Override
    public String format(Date date) {
        Calendar c = new GregorianCalendar(mTimeZone, mLocale);  // hard code GregorianCalendar
        c.setTime(date);
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#format(java.util.Calendar)
     */
    @Override
    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(mMaxLengthEstimate)).toString();
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#format(long, java.lang.StringBuffer)
     */
    @Override
    public StringBuffer format(long millis, StringBuffer buf) {
        return format(new Date(millis), buf);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#format(java.util.Date, java.lang.StringBuffer)
     */
    @Override
    public StringBuffer format(Date date, StringBuffer buf) {
        Calendar c = new GregorianCalendar(mTimeZone, mLocale);  // hard code GregorianCalendar
        c.setTime(date);
        return applyRules(c, buf);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#format(java.util.Calendar, java.lang.StringBuffer)
     */
    @Override
    public StringBuffer format(Calendar calendar, StringBuffer buf) {
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
        for (Rule rule : mRules) {
            rule.appendTo(buf, calendar);
        }
        return buf;
    }

    // Accessors
    //-----------------------------------------------------------------------
    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#getPattern()
     */
    @Override
    public String getPattern() {
        return mPattern;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#getTimeZone()
     */
    @Override
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.lang3.time.DatePrinter#getLocale()
     */
    @Override
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
     * @return {@code true} if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FastDatePrinter == false) {
            return false;
        }
        FastDatePrinter other = (FastDatePrinter) obj;
        return mPattern.equals(other.mPattern)
            && mTimeZone.equals(other.mTimeZone) 
            && mLocale.equals(other.mLocale);
    }

    /**
     * <p>Returns a hashcode compatible with equals.</p>
     *
     * @return a hashcode compatible with equals
     */
    @Override
    public int hashCode() {
        return mPattern.hashCode() + 13 * (mTimeZone.hashCode() + 13 * mLocale.hashCode());
    }

    /**
     * <p>Gets a debugging string version of this formatter.</p>
     *
     * @return a debugging string
     */
    @Override
    public String toString() {
        return "FastDatePrinter[" + mPattern + "," + mLocale + "," + mTimeZone.getID() + "]";
    }

    // Serializing
    //-----------------------------------------------------------------------
    /**
     * Create the object after serialization. This implementation reinitializes the
     * transient properties.
     *
     * @param in ObjectInputStream from which the object is being deserialized.
     * @throws IOException if there is an IO issue.
     * @throws ClassNotFoundException if a class cannot be found.
     */
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
         * Constructs a new instance of {@code CharacterLiteral}
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
        @Override
        public int estimateLength() {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs a new instance of {@code StringLiteral}
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
        @Override
        public int estimateLength() {
            return mValue.length();
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code TextField}
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
        @Override
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
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(mValues[calendar.get(mField)]);
        }
    }

    /**
     * <p>Inner class to output an unpadded number.</p>
     */
    private static class UnpaddedNumberField implements NumberRule {
        private final int mField;

        /**
         * Constructs an instance of {@code UnpadedNumberField} with the specified field.
         *
         * @param field the field
         */
        UnpaddedNumberField(int field) {
            mField = field;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 4;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code UnpaddedMonthField}.
         *
         */
        UnpaddedMonthField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code PaddedNumberField}.
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
        @Override
        public int estimateLength() {
            return 4;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code TwoDigitNumberField} with the specified field.
         *
         * @param field the field
         */
        TwoDigitNumberField(int field) {
            mField = field;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(mField));
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code TwoDigitYearField}.
         */
        TwoDigitYearField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.YEAR) % 100);
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code TwoDigitMonthField}.
         */
        TwoDigitMonthField() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code TwelveHourField} with the specified
         * {@code NumberRule}.
         *
         * @param rule the rule
         */
        TwelveHourField(NumberRule rule) {
            mRule = rule;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return mRule.estimateLength();
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
        @Override
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
         * Constructs an instance of {@code TwentyFourHourField} with the specified
         * {@code NumberRule}.
         *
         * @param rule the rule
         */
        TwentyFourHourField(NumberRule rule) {
            mRule = rule;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return mRule.estimateLength();
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
        @Override
        public void appendTo(StringBuffer buffer, int value) {
            mRule.appendTo(buffer, value);
        }
    }

    //-----------------------------------------------------------------------

    private static ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache =
        new ConcurrentHashMap<TimeZoneDisplayKey, String>(7);
    /**
     * <p>Gets the time zone display name, using a cache for performance.</p>
     *
     * @param tz  the zone to query
     * @param daylight  true if daylight savings
     * @param style  the style to use {@code TimeZone.LONG} or {@code TimeZone.SHORT}
     * @param locale  the locale to use
     * @return the textual name of the time zone
     */
    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = cTimeZoneDisplayCache.get(key);
        if (value == null) {
            // This is a very slow call, so cache the results.
            value = tz.getDisplayName(daylight, style, locale);
            String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
            if (prior != null) {
                value= prior;
            }
        }
        return value;
    }

    /**
     * <p>Inner class to output a time zone name.</p>
     */
    private static class TimeZoneNameRule implements Rule {
        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;

        /**
         * Constructs an instance of {@code TimeZoneNameRule} with the specified properties.
         *
         * @param timeZone the time zone
         * @param locale the locale
         * @param style the style
         */
        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            mLocale = locale;
            mStyle = style;
            
            mStandard = getTimeZoneDisplay(timeZone, false, style, locale);
            mDaylight = getTimeZoneDisplay(timeZone, true, style, locale);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            // We have no access to the Calendar object that will be passed to
            // appendTo so base estimate on the TimeZone passed to the
            // constructor
            return Math.max(mStandard.length(), mDaylight.length());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            TimeZone zone = calendar.getTimeZone();
            if (zone.useDaylightTime()
                    && calendar.get(Calendar.DST_OFFSET) != 0) {
                buffer.append(getTimeZoneDisplay(zone, true, mStyle, mLocale));
            } else {
                buffer.append(getTimeZoneDisplay(zone, false, mStyle, mLocale));
            }
        }
    }

    /**
     * <p>Inner class to output a time zone as a number {@code +/-HHMM}
     * or {@code +/-HH:MM}.</p>
     */
    private static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);

        final boolean mColon;

        /**
         * Constructs an instance of {@code TimeZoneNumberRule} with the specified properties.
         *
         * @param colon add colon between HH and MM in the output if {@code true}
         */
        TimeZoneNumberRule(boolean colon) {
            mColon = colon;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int estimateLength() {
            return 5;
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
         * Constructs an instance of {@code TimeZoneDisplayKey} with the specified properties.
         *
         * @param timeZone the time zone
         * @param daylight adjust the style for daylight saving time if {@code true}
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
        @Override
        public int hashCode() {
            return (mStyle * 31 + mLocale.hashCode() ) * 31 + mTimeZone.hashCode();
        }

        /**
         * {@inheritDoc}
         */
        @Override
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
}

```
