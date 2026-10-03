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
<project name="Commons Math" default="jar" basedir=".">


<!--
        "Math" component of the Apache Commons Project
        $Id$
        $Revision$ $Date$
-->


<!-- ========== Initialize Properties ===================================== -->

  <property file="build.properties"/>                <!-- Component local   -->
  <property file="${user.home}/build.properties"/>   <!-- User local        -->


<!-- ========== External Dependencies ===================================== -->


  <!-- Junit -->
  <property name="junit.version"           value="4.4"/>
  <property name="junit.home"              value="/usr/share/junit"/>
  <property name="junit.jar"               value="${junit.home}/junit-${junit.version}.jar"/>


<!-- ========== Component Declarations ==================================== -->


  <!-- The name of this component -->
  <property name="component.name"          value="commons-math"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.apache.commons.math"/>

  <!-- The title of this component -->
  <property name="component.title"         value="Commons MATH"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="2.1-SNAPSHOT"/>

  <!-- The base directory for component sources -->
  <property name="source.home"             value="src/main/java"/>

  <!-- The base directory for unit test sources -->
  <property name="test.home"               value="src/test/java"/>

  <!-- The base directory for unit test resources -->
  <property name="test.resources"          value="src/test/resources"/>

  <!-- Download lib dir -->
  <property name="download.lib.dir"        value="lib"/>

  <!-- The base directory for compilation targets -->
  <property name="build.home"              value="target"/>

  <!-- The base directory for distribution targets -->
  <property name="dist.home"               value="${build.home}/dist"/>

  <!-- Base file name for dist files -->
  <property name="final.name"        value="${component.name}-${component.version}"/>

  <!-- Directory where binary release files are staged -->
  <property name="stage.bin.dir"           value="${dist.home}/stage-bin"/>

  <!-- Directory where source release files are staged -->
  <property name="stage.src.dir"           value="${dist.home}/stage-src"/>

<!-- ========== Compiler Defaults ========================================= -->

  <!-- Should Java compilations set the 'debug' compiler option? -->
  <property name="compile.debug"           value="true"/>

  <!-- Should Java compilations set the 'deprecation' compiler option? -->
  <property name="compile.deprecation"     value="false"/>

  <!-- Should Java compilations set the 'optimize' compiler option? -->
  <property name="compile.optimize"        value="true"/>
    
  <!-- JDK level -->
  <property name="compile.source"          value="1.6"/>
  <property name="compile.target"          value="1.6"/>

  <!-- Base compile classpath -->
  <path id="compile.classpath">
    <pathelement location="${build.home}/classes"/>
  </path>

  <!-- External dependency classpath -->
  <path id="downloaded.lib.classpath">
    <pathelement location="${download.lib.dir}/junit-${junit.version}.jar"/>
  </path>

<!-- ========== Test Execution Defaults =================================== -->


  <!-- Construct unit test classpath -->
  <path id="test.classpath">
    <pathelement location="${build.home}/classes"/>
    <pathelement location="${build.home}/test-classes"/>
    <pathelement location="${junit.jar}"/>
    <path refid="downloaded.lib.classpath"/>
  </path>

  <!-- Should the build fail if there are test failures? -->
  <property name="test.failonerror"        value="true"/>

<!-- ========== Executable Targets ======================================== -->

  <target name="clean" description="Clean build and distribution directories">
    <delete    dir="${build.home}"/>
  </target>


  <target name="init"
   description="Initialize and evaluate conditionals">
    <echo message="-------- ${component.title} ${component.version} --------"/>
    <filter  token="name"                  value="${component.name}"/>
    <filter  token="package"               value="${component.package}"/>
    <filter  token="version"               value="${component.version}"/>
    <filter  token="compilesource"         value="${compile.source}"/>
    <filter  token="compiletarget"         value="${compile.target}"/>
    <tstamp/>
    <mkdir dir="${build.home}"/>
    <mkdir dir="${build.home}/classes"/>
    <mkdir dir="${build.home}/test-classes"/>
  </target>

<!-- ========== Compile Targets =========================================== -->

  <target name="compile" depends="init" description="Compile">

    <javac  srcdir="${source.home}"
           destdir="${build.home}/classes"
             source="${compile.source}"
             target="${compile.target}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
          optimize="${compile.optimize}">
      <classpath refid="compile.classpath"/>
    </javac>
  </target>


<!-- ========== Unit Test Targets ========================================= -->

    <target name="compile.tests" depends="compile, download-dependencies" description="Compile unit tests.">

      <javac srcdir="${test.home}"
             destdir="${build.home}/test-classes"
             debug="${compile.debug}"
             deprecation="${compile.deprecation}"
             optimize="${compile.optimize}">
          <classpath refid="test.classpath"/>
      </javac>
    
      <copy todir="${build.home}/test-classes">
          <fileset dir="${test.resources}">
              <include name="**/*.xml"/>
              <include name="**/*.txt"/>
          </fileset>
      </copy>

    </target>

  <target name="test"  depends="compile.tests"
                       description="Run unit tests">
      <junit printsummary="true"
               errorProperty="test.failed"
               failureProperty="test.failed"
               fork="true"
               showOutput="true">
               <formatter usefile="false" type="plain"/>
               <classpath refid="test.classpath"/>
               <batchtest>
                   <fileset dir="${test.home}">
                      <include name="**/*Test.java"/> 
                      <include name="**/*TestBinary.java"/> 
                      <include name="**/*TestPermutations.java"/> 
                      <exclude name="**/*AbstractTest.java"/>
                   </fileset>
               </batchtest>
       </junit>
       <fail message="There were test failures.">
           <condition>
               <and>
                   <istrue value="${test.failonerror}"/>
                   <isset property="test.failed"/>
               </and>
           </condition>
     </fail>
  </target>


<!-- ========== Produce JavaDocs ========================================== -->

  <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
    <mkdir dir="${build.home}/apidocs"/>
    <tstamp>
        <format property="current.year" pattern="yyyy"/>
    </tstamp>
    <javadoc sourcepath="${source.home}"
                destdir="${build.home}/apidocs"
           packagenames="org.apache.commons.*"
                 author="true"
                private="true"
                version="true"
               doctitle="&lt;h1&gt;${component.title} ${component.version}&lt;/h1&gt;"
            windowtitle="${component.title} ${component.version}"
                 bottom="Copyright (c) 2003-${current.year}  Apache Software Foundation"
           classpathref="compile.classpath">
        <link href="http://java.sun.com/j2se/1.5.0/docs/api/"/>  
        <link href="http://commons.apache.org/collections/api"/> 
    </javadoc>
  </target>


<!-- ========== Create Jar ================================================ -->

  <target name="jar" depends="test" description="Create jar file">

    <mkdir dir="${build.home}/classes/META-INF"/>
    <copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
    <copy file="NOTICE.txt"  tofile="${build.home}/classes/META-INF/NOTICE.txt"/>

    <manifest file="${build.home}/MANIFEST.MF">
        <attribute name="Specification-Title"      value="${component.title}"/>
        <attribute name="Specification-Version"    value="${component.version}"/>
        <attribute name="Specification-Vendor"     value="Apache Software Foundation"/>
        <attribute name="Implementation-Title"     value="${component.title}"/>
        <attribute name="Implementation-Version"   value="${component.version}"/> 
        <attribute name="Implementation-Vendor"    value="Apache Software Foundation"/>
        <attribute name="Implementation-Vendor-Id" value="org.apache"/>
        <attribute name="X-Compile-Source-JDK"     value="${compile.source}"/>
        <attribute name="X-Compile-Target-JDK"     value="${compile.target}"/>
    </manifest>

    <jar jarfile="${build.home}/${final.name}.jar"
         basedir="${build.home}/classes"
        manifest="${build.home}/MANIFEST.MF"/>
  </target>


<!-- ========== Distribution Target =========================================== -->

  <target name="dist" depends="clean,jar,javadoc" description="Create distribution artifacts">

    <mkdir dir="${dist.home}"/>

    <!-- jar(s) -->
    <copy todir="${dist.home}">
      <fileset dir=".">
        <include name="RELEASE-NOTES.txt"/>
      </fileset>
      <fileset dir="${build.home}">
        <include name="*.jar"/>
      </fileset>
    </copy>

    <!-- Binary Distro -->
    <mkdir dir="${stage.bin.dir}/${final.name}"/>
    <copy todir="${stage.bin.dir}/${final.name}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="RELEASE-NOTES.txt"/>
      </fileset>
      <fileset dir="${build.home}">
        <include name="*.jar"/>
      </fileset>
    </copy>
    <copy todir="${stage.bin.dir}/${final.name}/apidocs">
      <fileset dir="${build.home}/apidocs" />
    </copy>

    <!-- Source Distro -->
    <mkdir dir="${stage.src.dir}/${final.name}-src"/>
    <copy todir="${stage.src.dir}/${final.name}-src">
      <fileset dir=".">
        <include name="*.xml"/>
        <include name="*.txt"/>
        <include name="*.html"/>
      </fileset>
    </copy>
    <copy todir="${stage.src.dir}/${final.name}-src/src">
      <fileset dir="src" excludes="mantissa/**,experimental/**" />
    </copy>
    <zip  zipfile="${dist.home}/${final.name}.zip"     basedir="${stage.bin.dir}"/>
    <zip  zipfile="${dist.home}/${final.name}-src.zip" basedir="${stage.src.dir}"/>
    <tar  tarfile="${dist.home}/${final.name}.tar"     basedir="${stage.bin.dir}" longfile="gnu"/>
    <tar  tarfile="${dist.home}/${final.name}-src.tar" basedir="${stage.src.dir}" longfile="gnu"/>
    <gzip     src="${dist.home}/${final.name}.tar"     zipfile="${dist.home}/${final.name}.tar.gz"/>
    <gzip     src="${dist.home}/${final.name}-src.tar" zipfile="${dist.home}/${final.name}-src.tar.gz"/>

    <!-- clean up staging directories -->
    <delete    dir="${stage.bin.dir}"/>
    <delete    dir="${stage.src.dir}"/>

  </target>


<!-- ========== Gump Target ===================================================== -->

  <target name="gump" depends="clean,test,javadoc,jar" description="Gump Target - clean,test,javadoc,jar"/>


<!-- ========== Download Dependencies =========================================== -->

    <target name="download-dependencies" 
           depends="check-availability" unless="skip.download">
        <echo message="doing download-dependencies..." />
        <antcall target="download-junit" />
    </target>

    <target name="check-availability">
        <echo message="doing check-availability..." />
        <available file="${junit.jar}" property="junit.found"/>
    </target>

    <target name="download-junit" unless="junit.found">
        <echo message="Downloading junit..."/>
        <mkdir dir="${download.lib.dir}" />
        <get dest="${download.lib.dir}/junit-${junit.version}.jar"
            usetimestamp="true" ignoreerrors="true"
            src="http://repo1.maven.org/maven2/junit/junit/${junit.version}/junit-${junit.version}.jar"/>
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
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>12</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-math</artifactId>
  <version>2.1-SNAPSHOT</version>
  <name>Commons Math</name>

  <inceptionYear>2003</inceptionYear>
  <description>The Math project is a library of lightweight, self-contained mathematics and statistics components addressing the most common practical problems not immediately available in the Java programming language or commons-lang.</description>

  <url>http://commons.apache.org/math/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/MATH</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/math/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/math/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/math/trunk</url>
  </scm>
  
  <distributionManagement>
    <site>
      <id>people.apache.org</id>
      <name>Commons Math</name>
      <url>scp://people.apache.org/www/commons.apache.org/math</url>
    </site>
  </distributionManagement>

  <developers>
    <developer>
      <name>Bill Barker</name>
      <id>billbarker</id>
      <email>billbarker at apache dot org</email>
    </developer>
    <developer>
      <name>Albert Davidson Chou</name>
      <id>achou</id>
      <email>achou at apache dot org</email>
    </developer>
    <developer>
      <name>Mark Diggory</name>
      <id>mdiggory</id>
      <email>mdiggory at apache dot org</email>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
      <email>rdonkin at apache dot org</email>
    </developer>
    <developer>
      <name>Tim O'Brien</name>
      <id>tobrien</id>
      <email>tobrien at apache dot org</email>
    </developer>
    <developer>
      <name>Luc Maisonobe</name>
      <id>luc</id>
      <email>luc at apache dot org</email>
    </developer>
    <developer>
      <name>J. Pietschmann</name>
      <id>pietsch</id>
      <email>j3322ptm at yahoo dot de</email>
    </developer>
    <developer>
      <name>Dimitri Pourbaix</name>
      <id>dimpbx</id>
      <email>dimpbx at apache dot org</email>
    </developer>
    <developer>
      <name>Phil Steitz</name>
      <id>psteitz</id>
      <email>psteitz at apache dot org</email>
    </developer>
    <developer>
      <name>Brent Worden</name>
      <id>brentworden</id>
      <email>brentworden at apache dot org</email>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>C. Scott Ananian</name>
    </contributor>
    <contributor>
      <name>Mikkel Meyer Andersen</name>
    </contributor>
    <contributor>
      <name>Mark Anderson</name>
    </contributor>
    <contributor>
      <name>R&#233;mi Arntzen</name>
    </contributor>
    <contributor>
      <name>Michael Bjorkegren</name>
    </contributor>
    <contributor>
      <name>John Bollinger</name>
    </contributor>
    <contributor>
      <name>Cyril Briquet</name>
    </contributor>
    <contributor>
      <name>Paul Cowan</name>
    </contributor>
    <contributor>
      <name>Benjamin Croizet</name>
    </contributor>
    <contributor>
      <name>Larry Diamond</name>
    </contributor>
    <contributor>
      <name>Rodrigo di Lorenzo Lopes</name>
    </contributor>
    <contributor>
      <name>Hasan Diwan</name>
    </contributor>
    <contributor>
      <name>Ted Dunning</name>
    </contributor>
    <contributor>
      <name>John Gant</name>
    </contributor>
    <contributor>
      <name>Ken Geis</name>
    </contributor>
    <contributor>
      <name>Bernhard Gr&#252;newaldt</name>
    </contributor>
    <contributor>
      <name>Elliotte Rusty Harold</name>
    </contributor>
    <contributor>
      <name>Matthias Hummel</name>
    </contributor>
    <contributor>
      <name>Ismael Juma</name>
    </contributor>
    <contributor>
      <name>Eugene Kirpichov</name>
    </contributor>
    <contributor>
      <name>Piotr Kochanski</name>
    </contributor>
    <contributor>
      <name>Bob MacCallum</name>
    </contributor>
    <contributor>
      <name>Jake Mannix</name>
    </contributor>
    <contributor>
      <name>Benjamin McCann</name>
      <url>http://www.benmccann.com</url>
    </contributor>
    <contributor>
      <name>Fredrik Norin</name>
    </contributor>
    <contributor>
      <name>Sujit Pal</name>
    </contributor>
    <contributor>
      <name>Todd C. Parnell</name>
    </contributor>
    <contributor>
      <name>Andreas Rieger</name>
    </contributor>
    <contributor>
      <name>Matthew Rowles</name>
    </contributor>
    <contributor>
      <name>Gilles Sadowski</name>
    </contributor>
    <contributor>
      <name>Joni Salonen</name>
    </contributor>
    <contributor>
      <name>Christopher Schuck</name>
    </contributor>
    <contributor>
      <name>Christian Semrau</name>
    </contributor>
    <contributor>
      <name>David Stefka</name>
    </contributor>
    <contributor>
      <name>Mauro Talevi</name>
    </contributor>
    <contributor>
      <name>Kim van der Linde</name>
    </contributor>
    <contributor>
      <name>J&#246;rg Weimar</name>
    </contributor>
    <contributor>
      <name>Xiaogang Zhang</name>
    </contributor>
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.4</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <commons.componentid>math</commons.componentid>
    <commons.release.version>2.0</commons.release.version>
    <commons.binary.suffix></commons.binary.suffix>
    <commons.jira.id>MATH</commons.jira.id>
    <commons.jira.pid>12310485</commons.jira.pid>
    <maven.compile.source>1.5</maven.compile.source>
    <maven.compile.target>1.5</maven.compile.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties> 

  <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
                <include>**/*TestBinary.java</include>
                <include>**/*TestPermutations.java</include>
              </includes>
              <excludes>
                <exclude>**/*AbstractTest.java</exclude>
              </excludes>
          </configuration>
        </plugin>
        <plugin>
          <artifactId>maven-assembly-plugin</artifactId>
          <version>2.2-beta-4</version>
          <configuration>
            <descriptors>
              <descriptor>src/main/assembly/src.xml</descriptor>
              <descriptor>src/main/assembly/bin.xml</descriptor>
            </descriptors>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>cobertura-maven-plugin</artifactId>
          <version>2.2</version>
          <executions>
            <execution>
              <goals>
                <goal>clean</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>clirr-maven-plugin</artifactId>
          <version>2.2.2</version>
          <executions>
            <execution>
              <goals>
              </goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
  </build>
  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.1</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.2</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changes-plugin</artifactId>
        <version>2.1</version>
        <configuration>
          <xmlPath>${basedir}/src/site/xdoc/changes.xml</xmlPath>
          <issueLinkTemplate>%URL%/%ISSUE%</issueLinkTemplate>
          <template>math-release-notes.vm</template>
          <templateDirectory>templates</templateDirectory>
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
        <version>2.3</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
          <headerFile>${basedir}/license-header.txt</headerFile>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <rulesets>
            <ruleset>checkstyle.xml</ruleset>
          </rulesets>
          <linkXref>true</linkXref>
          <targetJdk>1.5</targetJdk>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.2.2</version>
        <configuration>
          <minSeverity>info</minSeverity>
         </configuration>
      </plugin>
    </plugins>
  </reporting>
</project>


```

## src/main/java/org/apache/commons/math/analysis/solvers/BrentSolver.java

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
package org.apache.commons.math.analysis.solvers;


import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Implements the <a href="http://mathworld.wolfram.com/BrentsMethod.html">
 * Brent algorithm</a> for  finding zeros of real univariate functions.
 * <p>
 * The function should be continuous but not necessarily smooth.</p>
 *
 * @version $Revision:670469 $ $Date:2008-06-23 10:01:38 +0200 (lun., 23 juin 2008) $
 */
public class BrentSolver extends UnivariateRealSolverImpl {

    /** Error message for non-bracketing interval. */
    private static final String NON_BRACKETING_MESSAGE =
        "function values at endpoints do not have different signs.  " +
        "Endpoints: [{0}, {1}], Values: [{2}, {3}]";

    /** Serializable version identifier */
    private static final long serialVersionUID = 7694577816772532779L;

    /**
     * Construct a solver for the given function.
     *
     * @param f function to solve.
     * @deprecated as of 2.0 the function to solve is passed as an argument
     * to the {@link #solve(UnivariateRealFunction, double, double)} or
     * {@link UnivariateRealSolverImpl#solve(UnivariateRealFunction, double, double, double)}
     * method.
     */
    @Deprecated
    public BrentSolver(UnivariateRealFunction f) {
        super(f, 100, 1E-6);
    }

    /**
     * Construct a solver.
     */
    public BrentSolver() {
        super(100, 1E-6);
    }

    /** {@inheritDoc} */
    @Deprecated
    public double solve(double min, double max)
        throws MaxIterationsExceededException, FunctionEvaluationException {
        return solve(f, min, max);
    }

    /** {@inheritDoc} */
    @Deprecated
    public double solve(double min, double max, double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {
        return solve(f, min, max, initial);
    }

    /**
     * Find a zero in the given interval with an initial guess.
     * <p>Throws <code>IllegalArgumentException</code> if the values of the
     * function at the three points have the same sign (note that it is
     * allowed to have endpoints with the same sign if the initial point has
     * opposite sign function-wise).</p>
     *
     * @param f function to solve.
     * @param min the lower bound for the interval.
     * @param max the upper bound for the interval.
     * @param initial the start value to use (must be set to min if no
     * initial point is known).
     * @return the value where the function is zero
     * @throws MaxIterationsExceededException the maximum iteration count
     * is exceeded
     * @throws FunctionEvaluationException if an error occurs evaluating
     *  the function
     * @throws IllegalArgumentException if initial is not between min and max
     * (even if it <em>is</em> a root)
     */
    public double solve(final UnivariateRealFunction f,
                        final double min, final double max, final double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {

        clearResult();
        verifySequence(min, initial, max);

        // return the initial guess if it is good enough
        double yInitial = f.value(initial);
        if (Math.abs(yInitial) <= functionValueAccuracy) {
            setResult(initial, 0);
            return result;
        }

        // return the first endpoint if it is good enough
        double yMin = f.value(min);
        if (Math.abs(yMin) <= functionValueAccuracy) {
            setResult(min, 0);
            return result;
        }

        // reduce interval if min and initial bracket the root
        if (yInitial * yMin < 0) {
            return solve(f, min, yMin, initial, yInitial, min, yMin);
        }

        // return the second endpoint if it is good enough
        double yMax = f.value(max);
        if (Math.abs(yMax) <= functionValueAccuracy) {
            setResult(max, 0);
            return result;
        }

        // reduce interval if initial and max bracket the root
        if (yInitial * yMax < 0) {
            return solve(f, initial, yInitial, max, yMax, initial, yInitial);
        }

        if (yMin * yMax > 0) {
            throw MathRuntimeException.createIllegalArgumentException(
                  NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
        }

        // full Brent algorithm starting with provided initial guess
        return solve(f, min, yMin, max, yMax, initial, yInitial);

    }

    /**
     * Find a zero in the given interval.
     * <p>
     * Requires that the values of the function at the endpoints have opposite
     * signs. An <code>IllegalArgumentException</code> is thrown if this is not
     * the case.</p>
     *
     * @param f the function to solve
     * @param min the lower bound for the interval.
     * @param max the upper bound for the interval.
     * @return the value where the function is zero
     * @throws MaxIterationsExceededException if the maximum iteration count is exceeded
     * @throws FunctionEvaluationException if an error occurs evaluating the
     * function
     * @throws IllegalArgumentException if min is not less than max or the
     * signs of the values of the function at the endpoints are not opposites
     */
    public double solve(final UnivariateRealFunction f,
                        final double min, final double max)
        throws MaxIterationsExceededException,
        FunctionEvaluationException {

        clearResult();
        verifyInterval(min, max);

        double ret = Double.NaN;

        double yMin = f.value(min);
        double yMax = f.value(max);

        // Verify bracketing
        double sign = yMin * yMax;
        if (sign > 0) {
            // check if either value is close to a zero
            if (Math.abs(yMin) <= functionValueAccuracy) {
                setResult(min, 0);
                ret = min;
            } else if (Math.abs(yMax) <= functionValueAccuracy) {
                setResult(max, 0);
                ret = max;
            } else {
                // neither value is close to zero and min and max do not bracket root.
                throw MathRuntimeException.createIllegalArgumentException(
                        NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
            }
        } else if (sign < 0){
            // solve using only the first endpoint as initial guess
            ret = solve(f, min, yMin, max, yMax, min, yMin);
        } else {
            // either min or max is a root
            if (yMin == 0.0) {
                ret = min;
            } else {
                ret = max;
            }
        }

        return ret;
    }

    /**
     * Find a zero starting search according to the three provided points.
     * @param f the function to solve
     * @param x0 old approximation for the root
     * @param y0 function value at the approximation for the root
     * @param x1 last calculated approximation for the root
     * @param y1 function value at the last calculated approximation
     * for the root
     * @param x2 bracket point (must be set to x0 if no bracket point is
     * known, this will force starting with linear interpolation)
     * @param y2 function value at the bracket point.
     * @return the value where the function is zero
     * @throws MaxIterationsExceededException if the maximum iteration count
     * is exceeded
     * @throws FunctionEvaluationException if an error occurs evaluating
     * the function
     */
    private double solve(final UnivariateRealFunction f,
                         double x0, double y0,
                         double x1, double y1,
                         double x2, double y2)
    throws MaxIterationsExceededException, FunctionEvaluationException {

        double delta = x1 - x0;
        double oldDelta = delta;

        int i = 0;
        while (i < maximalIterationCount) {
            if (Math.abs(y2) < Math.abs(y1)) {
                // use the bracket point if is better than last approximation
                x0 = x1;
                x1 = x2;
                x2 = x0;
                y0 = y1;
                y1 = y2;
                y2 = y0;
            }
            if (Math.abs(y1) <= functionValueAccuracy) {
                // Avoid division by very small values. Assume
                // the iteration has converged (the problem may
                // still be ill conditioned)
                setResult(x1, i);
                return result;
            }
            double dx = x2 - x1;
            double tolerance =
                Math.max(relativeAccuracy * Math.abs(x1), absoluteAccuracy);
            if (Math.abs(dx) <= tolerance) {
                setResult(x1, i);
                return result;
            }
            if ((Math.abs(oldDelta) < tolerance) ||
                    (Math.abs(y0) <= Math.abs(y1))) {
                // Force bisection.
                delta = 0.5 * dx;
                oldDelta = delta;
            } else {
                double r3 = y1 / y0;
                double p;
                double p1;
                // the equality test (x0 == x2) is intentional,
                // it is part of the original Brent's method,
                // it should NOT be replaced by proximity test
                if (x0 == x2) {
                    // Linear interpolation.
                    p = dx * r3;
                    p1 = 1.0 - r3;
                } else {
                    // Inverse quadratic interpolation.
                    double r1 = y0 / y2;
                    double r2 = y1 / y2;
                    p = r3 * (dx * r1 * (r1 - r2) - (x1 - x0) * (r2 - 1.0));
                    p1 = (r1 - 1.0) * (r2 - 1.0) * (r3 - 1.0);
                }
                if (p > 0.0) {
                    p1 = -p1;
                } else {
                    p = -p;
                }
                if (2.0 * p >= 1.5 * dx * p1 - Math.abs(tolerance * p1) ||
                        p >= Math.abs(0.5 * oldDelta * p1)) {
                    // Inverse quadratic interpolation gives a value
                    // in the wrong direction, or progress is slow.
                    // Fall back to bisection.
                    delta = 0.5 * dx;
                    oldDelta = delta;
                } else {
                    oldDelta = delta;
                    delta = p / p1;
                }
            }
            // Save old X1, Y1
            x0 = x1;
            y0 = y1;
            // Compute new X1, Y1
            if (Math.abs(delta) > tolerance) {
                x1 = x1 + delta;
            } else if (dx > 0.0) {
                x1 = x1 + 0.5 * tolerance;
            } else if (dx <= 0.0) {
                x1 = x1 - 0.5 * tolerance;
            }
            y1 = f.value(x1);
            if ((y1 > 0) == (y2 > 0)) {
                x2 = x0;
                y2 = y0;
                delta = x1 - x0;
                oldDelta = delta;
            }
            i++;
        }
        throw new MaxIterationsExceededException(maximalIterationCount);
    }
}

```
