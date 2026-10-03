## build.xml

```
<project name="joda-time" default="jar" basedir=".">

<!-- Joda-time ANT script -->
<!-- Based on scripts from Apache Jakarta Commons and elsewhere -->

<!-- This is the recommended way to build Joda-Time. -->
<!-- Maven is only intended for building the website. -->

<!-- This ant file will download junit-3.8.1.jar to the lib subdirectory -->
<!-- automatically if it does not find it there already. To change this -->
<!-- behaviour, override the junit.jar property in build.properties. -->

<!-- ========== Properties ================================================ -->

  <property file="build.properties"/>

<!-- ========== Component Declarations ==================================== -->


  <!-- The name of this component -->
  <property name="component.name"          value="joda-time"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.joda.time"/>

  <!-- The title of this component -->
  <property name="component.title"         value="Joda date and time"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="2.0"/>
  <property name="previous.version"        value="1.6"/>

  <!-- The current version number of this component -->
  <property name="component.fullname"      value="${component.name}-${component.version}"/>
  
  <!-- The directory of source files -->
  <property name="xdocs"                   value="xdocs"/>

  <!-- Dependencies -->
  <property name="lib"                     value="lib"/>
  <property name="junit.jar"               value="${lib}/junit-3.8.2.jar"/>
  <property name="jodaconvert.jar"         value="${lib}/joda-convert-1.1.jar"/>

  <!-- The directory of source files -->
  <property name="source"                  value="src"/>
  <property name="source.home"             value="${source}/main/java"/>
  <property name="source.tz"               value="${source.home}/org/joda/time/tz/src"/>
  <property name="conf.home"               value="${source}/conf"/>
  <property name="test.home"               value="${source}/test/java"/>

  <!-- The base directory for example sources -->
  <property name="example.home"            value="src/example"/>

  <!-- The base directory for compilation targets -->
  <property name="build"                   value="build"/>
  <property name="build.conf"              value="${build}/conf"/>
  <property name="build.classes"           value="${build}/classes"/>
  <property name="build.tz"                value="${build.classes}/org/joda/time/tz/data"/>
  <property name="build.tests"             value="${build}/tests"/>
  <property name="build.docs"              value="${build}/docs"/>
  <property name="build.sources"           value="${build}/sources"/>
  <property name="build.javadoc"           value="${build}/javadoc"/>
  <property name="build.dist"              value="${build}/dist"/>
  <property name="build.fullname"          value="${build}/${component.fullname}"/>
  <property name="build.dist.fullname"     value="${build.dist}/${component.fullname}"/>
  <property name="build.dist.src.fullname" value="${build.dist.fullname}-src"/>
  <property name="build.dist.bundle"       value="${build.dist.fullname}-bundle"/>

  <property name="repo" value="${user.home}/.maven/repository" />

<!-- ========== Compiler Defaults ========================================= -->

  <!-- Should Java compilations set the 'debug' compiler option? -->
  <property name="compile.debug"           value="true"/>
  <property name="compile.debuglevel"      value="lines,source"/>

  <!-- Should Java compilations set the 'deprecation' compiler option? -->
  <property name="compile.deprecation"     value="false"/>

  <!-- Should Java compilations set the 'optimize' compiler option? -->
  <property name="compile.optimize"        value="true"/>

  <!-- Construct compile classpath -->
  <path id="compile.classpath">
    <pathelement location="${build.classes}"/>
    <pathelement location="${jodaconvert.jar}"/>
  </path>


<!-- ========== Test Execution Defaults =================================== -->

  <!-- Construct unit test classpath -->
  <path id="test.classpath">
    <pathelement location="${build.classes}"/>
    <pathelement location="${build.tests}"/>
    <pathelement location="${junit.jar}"/>
    <pathelement location="${jodaconvert.jar}"/>
  </path>

  <!-- Should all tests fail if one does? -->
  <property name="test.failonerror"        value="true"/>

  <!-- The test runner to execute -->
  <property name="test.runner"             value="junit.textui.TestRunner"/>


<!-- ====================================================================== -->
<!-- ========== Executable Targets ======================================== -->
<!-- ====================================================================== -->

  <target name="clean"
          description="Clean build and distribution directories">
    <delete dir="${build}"/>
  </target>

<!-- ====================================================================== -->

  <target name="init"
          description="Initialize and evaluate conditionals">
    <echo message="-------- ${component.name} ${component.version} --------"/>
    <filter token="name"                  value="${component.name}"/>
    <filter token="package"               value="${component.package}"/>
    <filter token="version"               value="${component.version}"/>
    <available property="junit.ant" classname="junit.framework.Test"/>
    <available property="junit.present" file="${junit.jar}"/>
    <available property="jodaconvert.present" file="${jodaconvert.jar}"/>
    <uptodate property="tz.build.notneeded" targetfile="${build.tz}/ZoneInfoMap" >
      <srcfiles dir= "${source.tz}" includes="**/*.*"/>
    </uptodate>
  </target>

<!-- ====================================================================== -->

  <target name="getjunit" unless="junit.present">
    <echo message="Getting junit from http://repo2.maven.org/maven2/junit/junit/3.8.2"/>
    <setproxy />
    <mkdir dir="${lib}"/>
    <get dest="${junit.jar}" usetimestamp="true" ignoreerrors="true" src="http://repo2.maven.org/maven2/junit/junit/3.8.2/junit-3.8.2.jar" />
  </target>
  <target name="getjodaconvert" unless="jodaconvert.present">
    <echo message="Getting joda-convert from http://repo1.maven.org/maven2/org/joda/joda-convert/1.1"/>
    <setproxy />
    <mkdir dir="${lib}"/>
    <get dest="${jodaconvert.jar}" usetimestamp="true" ignoreerrors="true" src="http://repo1.maven.org/maven2/org/joda/joda-convert/1.1/joda-convert-1.1.jar" />
  </target>

<!-- ====================================================================== -->

  <target name="installjunit" unless="junit.ant">
    <echo message="Installing junit in ${ant.home}/lib"/>
  	<copy file="${junit.jar}" todir="${ant.home}/lib" />
    <echo message="***************************************************************"/>
    <echo message="*  A copy of junit has been installed in your ant directory   *"/>
    <echo message="*                                                             *"/>
    <echo message="* You will need to restart the ant build to pickup the change *"/>
    <echo message="***************************************************************"/>
  	<fail message="Please restart ant"/>
  </target>

<!-- ====================================================================== -->

  <target name="prepare" depends="init,getjunit,installjunit,getjodaconvert"
          description="Prepare build directory">
    <mkdir dir="${build}"/>
    <mkdir dir="${build.classes}"/>
    <mkdir dir="${build.conf}"/>
  </target>

<!-- ====================================================================== -->

  <target name="static" depends="prepare"
          description="Copy static files to build directory">
    <tstamp/>
    <copy todir="${build.conf}" filtering="on">
      <fileset dir="${conf.home}" includes="*.MF"/>
    </copy>
  </target>

<!-- ====================================================================== -->

  <target name="compile" depends="compile.main,compile.zoneinfo"
          description="Compile shareable components">
  </target>
          
          
  <target name="compile.main" depends="static"
          description="Compile main datetime classes">
    <javac  srcdir="${source.home}"
           destdir="${build.classes}"
             debug="${compile.debug}"
        debuglevel="${compile.debuglevel}"
       deprecation="${compile.deprecation}"
          optimize="${compile.optimize}"
    	    source="1.6" target="1.6" includeantruntime="false">
      <classpath refid="compile.classpath"/>
    </javac>
    <copy todir="${build.classes}">
      <fileset dir="${source.home}" includes="**/*.properties"/>
    </copy>
  </target>

<!-- ====================================================================== -->

  <target name="compile.zoneinfo"
          depends="compile.main"
          description="Compile timezone data files"
          unless="tz.build.notneeded">
    <!-- Invoke the newly built ZoneInfoCompiler to compile the zoneinfo data files -->
    <mkdir dir="${build.tz}" />
    <java classname="org.joda.time.tz.ZoneInfoCompiler"
          fork="true"
          failonerror="true">
      <classpath path="${build.classes}" />
      <!-- Override default provider since data directory doesn't exist yet -->
      <sysproperty key="org.joda.time.DateTimeZone.Provider"
                   value="org.joda.time.tz.UTCProvider" />
      <!-- Specify source and destination directories -->
      <arg line="-src ${source.tz} -dst ${build.tz}" />
      <!-- Specify all the data files to compile -->
      <arg value="africa" />
      <arg value="antarctica" />
      <arg value="asia" />
      <arg value="australasia" />
      <arg value="europe" />
      <arg value="northamerica" />
      <arg value="southamerica" />
      <arg value="pacificnew" />
      <arg value="etcetera" />
      <arg value="backward" />
      <arg value="systemv" />
    </java>
  </target>
  
<!-- ====================================================================== -->

  <target name="compile.tests" depends="compile"
          description="Compile unit test cases">
    <mkdir dir="${build.tests}"/>
    <javac  srcdir="${test.home}"
           destdir="${build.tests}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
          optimize="${compile.optimize}" includeantruntime="false">
      <classpath refid="test.classpath"/>
    </javac>
    <copy    todir="${build.tests}" filtering="on">
      <fileset dir="${test.home}" excludes="**/*.java"/>
    </copy>
  </target>

<!-- ====================================================================== -->

  <target name="all" depends="clean,compile"
          description="Clean and compile all components"/>

<!-- ====================================================================== -->

  <target name="javadoc" depends="compile"
          description="Create component Javadoc documentation">
    <mkdir      dir="${build.docs}"/>
    <javadoc sourcepath="${source.home}"
                destdir="${build.docs}"
           packagenames="org.joda.time.*"
                 author="true"
                private="false"
                package="false"
                version="true"
                    use="yes"
             splitindex="yes"
               doctitle="&lt;h1&gt;${component.title}&lt;/h1&gt;"
            windowtitle="${component.title} (Version ${component.version})"
                 bottom="Copyright (c) 2001-2006 - Joda.org"
               Overview="${source.home}/org/joda/time/overview.html">
      <classpath refid="compile.classpath"/>
      <group title="User Packages" packages="org.joda.time:org.joda.time.format:org.joda.time.chrono">
      </group>
      <group title="Implementation Packages" packages="org.joda.time.base:org.joda.time.convert:org.joda.time.field:org.joda.time.tz">
      </group>
    </javadoc>
  </target>

<!-- ====================================================================== -->

  <target name="jar" depends="compile"
          description="Create jar">
    <mkdir      dir="${build.classes}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${build.classes}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${build.classes}/META-INF/NOTICE.txt"/>
    <jar    jarfile="${build.fullname}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF"/>
  </target>

<!-- ====================================================================== -->

  <target name="javadoc.jar">
    <mkdir      dir="${build.javadoc}"/>
    <copy     todir="${build.javadoc}">
  	  <fileset dir="${build.docs}" includes="**/*" />
  	</copy>
    <mkdir      dir="${build.javadoc}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${build.javadoc}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${build.javadoc}/META-INF/NOTICE.txt"/>
    <jar    jarfile="${build.fullname}-javadoc.jar"
            basedir="${build.javadoc}" />
  </target>

<!-- ====================================================================== -->

  <target name="sources.jar">
    <mkdir      dir="${build.sources}"/>
    <copy     todir="${build.sources}">
  	  <fileset dir="${source.home}" includes="**/*.java" />
  	</copy>
    <mkdir      dir="${build.sources}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${build.sources}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${build.sources}/META-INF/NOTICE.txt"/>
    <jar    jarfile="${build.fullname}-sources.jar"
            basedir="${build.sources}" />
  </target>

<!-- ====================================================================== -->

  <target name="dist" depends="compile,jar,test.jar,javadoc,javadoc.jar,sources.jar"
          description="Create binary distribution">
    
	<!-- binary -->
    <delete     dir="${build.dist.fullname}"/>
    <mkdir      dir="${build.dist.fullname}"/>
    <copy      file="LICENSE.txt" todir="${build.dist.fullname}"/>
    <copy      file="NOTICE.txt" todir="${build.dist.fullname}"/>
    <copy      file="RELEASE-NOTES.txt" todir="${build.dist.fullname}"/>
    <copy      file="${build.fullname}.jar"
              todir="${build.dist.fullname}"/>
    <copy      file="${build.fullname}-sources.jar"
              todir="${build.dist.fullname}"/>
    <copy      file="${build.fullname}-javadoc.jar"
              todir="${build.dist.fullname}"/>
    
	<fixcrlf srcdir="${build.dist.fullname}" eol="lf" includes="*.txt"/>
	<tar   destfile="${build.fullname}.tar" basedir="${build.dist}"/>
	<gzip   zipfile="${build.fullname}.tar.gz" src="${build.fullname}.tar"/>
	<delete    file="${build.fullname}.tar"/>
	<fixcrlf srcdir="${build.dist.fullname}" eol="crlf" includes="*.txt"/>
	<zip   destfile="${build.fullname}.zip" basedir="${build.dist}"/>
    <delete     dir="${build.dist.fullname}"/>
	
	<!-- source -->
    <delete     dir="${build.dist.src.fullname}"/>
    <mkdir      dir="${build.dist.src.fullname}"/>
    <copy      file="LICENSE.txt" todir="${build.dist.src.fullname}"/>
    <copy      file="NOTICE.txt" todir="${build.dist.src.fullname}"/>
    <copy      file="RELEASE-NOTES.txt" todir="${build.dist.src.fullname}"/>
    <copy      file="${build.fullname}.jar"
              todir="${build.dist.src.fullname}"/>
    <copy     todir="${build.dist.src.fullname}">
      <fileset  dir="." includes="${source}/**/*" excludes="CVS/**/*"/>
    </copy>
    <copy     todir="${build.dist.src.fullname}">
      <fileset  dir="." includes="${xdocs}/**/*" excludes="CVS/**/*"/>
    </copy>
	<delete     dir="${build.dist.src.fullname}/src/tzdata"/>
    <copy      file="build.xml" todir="${build.dist.src.fullname}"/>
    <copy      file="pom.xml" todir="${build.dist.src.fullname}"/>
    <copy      file="checkstyle.xml" todir="${build.dist.src.fullname}"/>
    <copy      file="ToDo.txt" todir="${build.dist.src.fullname}"/>
	
	<fixcrlf srcdir="${build.dist.src.fullname}" eol="lf" includes="*.txt,*.properties,*.xml"/>
	<tar   destfile="${build.fullname}-src.tar" basedir="${build.dist}"/>
	<gzip   zipfile="${build.fullname}-src.tar.gz" src="${build.fullname}-src.tar"/>
	<delete    file="${build.fullname}-src.tar"/>
	<fixcrlf srcdir="${build.dist.src.fullname}" eol="crlf" includes="*.txt,*.properties,*.xml"/>
	<zip   destfile="${build.fullname}-src.zip" basedir="${build.dist}"/>
    <delete     dir="${build.dist.src.fullname}"/>
  	
	<!-- bundle -->
    <delete     dir="${build.dist.bundle}"/>
    <mkdir      dir="${build.dist.bundle}"/>
    <copy      file="LICENSE.txt" todir="${build.dist.bundle}"/>
    <copy      file="NOTICE.txt" todir="${build.dist.bundle}"/>
    <copy      file="pom.xml" todir="${build.dist.bundle}"/>
    <copy      file="${build.fullname}.jar"
              todir="${build.dist.bundle}"/>
    <copy      file="${build.fullname}-sources.jar"
              todir="${build.dist.bundle}"/>
	<copy      file="${build.fullname}-javadoc.jar"
	          todir="${build.dist.bundle}"/>
    
	<fixcrlf srcdir="${build.dist.bundle}" eol="crlf" includes="*.txt"/>
	<jar    jarfile="${build.fullname}-bundle.jar" basedir="${build.dist.bundle}"/>
    <delete     dir="${build.dist.bundle}"/>
	
  </target>

<!-- ====================================================================== -->

  <target name="test"  depends="compile.tests, test.time"
          description="Run all unit test cases">
  </target>

  <target name="test.time" depends="compile.tests,compile.zoneinfo">
    <echo message="Running time tests ..."/>
    <junit printsummary="yes" haltonfailure="yes">
      <formatter type="plain" usefile="false" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${build.tz}"/>
        <pathelement location="${jodaconvert.jar}"/>
        <pathelement path="${java.class.path}"/>
      </classpath>

      <batchtest fork="yes">
        <fileset dir="${test.home}">
          <include name="**/TestAll.java"/>
        </fileset>
      </batchtest>
    </junit>
  </target>

  <!-- don't depend on jar, so we can test jar built on another JDK version -->
  <target name="test.jar" depends="compile.tests">
    <echo message="Running time tests from jar ..."/>
    <junit printsummary="yes" haltonfailure="yes">
      <formatter type="plain" usefile="false" />
      <classpath>
        <pathelement location="${build.fullname}.jar"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
        <pathelement location="${jodaconvert.jar}"/>
        <pathelement path="${java.class.path}"/>
      </classpath>

      <batchtest fork="yes">
        <fileset dir="${test.home}">
          <include name="**/TestAll.java"/>
        </fileset>
      </batchtest>
    </junit>
  </target>

  <target name="clirr" depends="jar" description="clirr binary compatibility">
  	<echo message="${repo}/clirr/jars/clirr-core-0.6-uber.jar"></echo>
    <taskdef classpath="${repo}/clirr/jars/clirr-core-0.6-uber.jar"
        resource="clirrtask.properties"/>

    <clirr>
      <origfiles dir="${repo}/${component.name}/jars" includes="${component.name}-${previous.version}.jar"/>
      <newfiles dir="." includes="${build.fullname}.jar"/>
      <formatter type="xml" outfile="build/clirr.xml" />
    </clirr>
  </target>

  <!--property name="emma.dir" value="${lib}" />
  <path id="emma.lib" >
    <pathelement location="${emma.dir}/emma.jar" />
    <pathelement location="${emma.dir}/emma_ant.jar" />
  </path>
  <target name="emma" description="turns on EMMA's on-the-fly instrumentation mode" >
  	<taskdef resource="emma_ant.properties" classpathref="emma.lib" />
    <property name="emma.enabled" value="true" />
  </target-->
</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>joda-time</groupId>
  <artifactId>joda-time</artifactId>
  <packaging>jar</packaging>
  <name>Joda time</name>
  <version>2.1.alpha</version>
  <description>Date and time library to replace JDK date handling</description>
  <url>http://joda-time.sourceforge.net</url>
  <issueManagement>
  	<system>Sourceforge</system>
    <url>https://sourceforge.net/tracker/?group_id=97367&amp;atid=617889</url>
  </issueManagement>
  <inceptionYear>2002</inceptionYear>
  <mailingLists>
    <mailingList>
      <name>Joda Interest list</name>
      <subscribe>https://lists.sourceforge.net/lists/listinfo/joda-interest</subscribe>
      <unsubscribe>https://lists.sourceforge.net/lists/listinfo/joda-interest</unsubscribe>
      <archive>http://sourceforge.net/mailarchive/forum.php?forum_name=joda-interest</archive>
    </mailingList>
  </mailingLists>
  <developers>
    <developer>
      <id>scolebourne</id>
      <name>Stephen Colebourne</name>
      <email></email>
      <roles>
        <role>Project Lead</role>
      </roles>
      <timezone>0</timezone>
    </developer>
    <developer>
      <id>broneill</id>
      <name>Brian S O'Neill</name>
      <email></email>
      <roles>
        <role>Senior Developer</role>
      </roles>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>Guy Allard</name>
    </contributor>
    <contributor>
      <name>Fredrik Borgh</name>
    </contributor>
    <contributor>
      <name>Dave Brosius</name>
    </contributor>
    <contributor>
      <name>Jeroen van Erp</name>
    </contributor>
    <contributor>
      <name>Gwyn Evans</name>
    </contributor>
    <contributor>
      <name>Sean Geoghegan</name>
    </contributor>
    <contributor>
      <name>Ashish Katyal</name>
    </contributor>
    <contributor>
      <name>Antonio Leitao</name>
    </contributor>
    <contributor>
      <name>Kostas Maistrelis</name>
    </contributor>
    <contributor>
      <name>Al Major</name>
    </contributor>
    <contributor>
      <name>Blair Martin</name>
    </contributor>
    <contributor>
      <name>Julen Parra</name>
    </contributor>
    <contributor>
      <name>Ryan Propper</name>
    </contributor>
    <contributor>
      <name>Mike Schrag</name>
    </contributor>
    <contributor>
      <name>Kandarp Shah</name>
    </contributor>
    <contributor>
      <name>Francois Staes</name>
    </contributor>
    <contributor>
      <name>Ricardo Trindade</name>
    </contributor>
    <contributor>
      <name>Maxim Zhao</name>
    </contributor>
  </contributors>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:git@github.com:JodaOrg/joda-time.git</connection>
    <developerConnection>scm:git:git@github.com:JodaOrg/joda-time.git</developerConnection>
    <url>https://github.com/JodaOrg/joda-time</url>
  </scm>
  <organization>
    <name>Joda.org</name>
    <url>http://www.joda.org</url>
  </organization>
  <build>
    <resources>
      <resource>
        <targetPath>META-INF</targetPath>
        <directory>.</directory>
        <includes>
          <include>LICENSE.txt</include>
          <include>NOTICE.txt</include>
        </includes>
      </resource>
      <resource>
        <targetPath>.</targetPath>
        <directory>src/main/java</directory>
        <includes>
          <include>org/joda/time/tz/data/**</include>
          <include>**/*.properties</include>
        </includes>
      </resource>
    </resources>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-clean-plugin</artifactId>
        <version>2.4.1</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.3.1</version>
        <configuration>
          <verbose>true</verbose>
          <fork>true</fork>
          <compilerVersion>1.5</compilerVersion>
          <source>1.5</source>
          <target>1.5</target>
          <debug>true</debug>
          <debuglevel>lines,source</debuglevel>
          <optimize>true</optimize>
          <showDeprecation>false</showDeprecation>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-antrun-plugin</artifactId>
        <version>1.4</version>
        <executions>
          <execution>
            <phase>compile</phase>
            <configuration>
              <tasks>
                <property name="tz.src" value="${pom.build.sourceDirectory}/org/joda/time/tz/src" />
                <property name="tz.dst" value="${pom.build.outputDirectory}/org/joda/time/tz/data" />
                <!--uptodate property="tz.build.notneeded" targetfile="${tz.dst}/ZoneInfoMap" >
                  <srcfiles dir="${tz.src}" includes="**/*.*"/>
                </uptodate-->
                <mkdir dir="${tz.dst}" />
                <java classname="org.joda.time.tz.ZoneInfoCompiler" fork="true" failonerror="true">
                  <classpath refid="maven.compile.classpath" />
                  <sysproperty key="org.joda.time.DateTimeZone.Provider" value="org.joda.time.tz.UTCProvider" />
                  <arg line="-src ${tz.src} -dst ${tz.dst}" />
                  <arg value="africa" />
                  <arg value="antarctica" />
                  <arg value="asia" />
                  <arg value="australasia" />
                  <arg value="europe" />
                  <arg value="northamerica" />
                  <arg value="southamerica" />
                  <arg value="pacificnew" />
                  <arg value="etcetera" />
                  <arg value="backward" />
                  <arg value="systemv" />
                </java>
              </tasks>
            </configuration>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>2.5</version>
        <configuration>
          <includes>
            <include>**/TestAllPackages.java</include>
          </includes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.3.1</version>
        <configuration>
          <archive>
            <manifestFile>src/conf/MANIFEST.MF</manifestFile>
          </archive>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.7</version>
        <configuration>
          <linksource>false</linksource>
          <links>
            <link>http://download.oracle.com/javase/1.5.0/docs/api/</link>
          </links>
          <encoding>UTF-8</encoding>
          <groups>
            <group>
              <title>User packages</title>
              <packages>org.joda.time:org.joda.time.format:org.joda.time.chrono</packages>
            </group>
            <group>
              <title>Implementation packages</title>
              <packages>org.joda.time.base:org.joda.time.convert:org.joda.time.field:org.joda.time.tz</packages>
            </group>
          </groups>
        </configuration>
        <executions>
          <execution>
            <id>attach-javadocs</id>
            <phase>package</phase>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <version>2.1.2</version>
        <executions>
          <execution>
            <id>attach-sources</id>
            <phase>package</phase>
            <goals>
              <goal>jar-no-fork</goal>
            </goals>
          </execution>
        </executions>
        <!-- work around maven bug where properties files added twice -->
        <configuration>
          <excludes>
            <exclude>**/*.properties</exclude>
          </excludes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-site-plugin</artifactId>
        <version>2.1.1</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-repository-plugin</artifactId>
        <version>2.3.1</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptors>
            <descriptor>src/main/assembly/dist.xml</descriptor>
          </descriptors>
          <tarLongFileMode>gnu</tarLongFileMode>
        </configuration>
        <executions>
          <execution>
            <id>make-assembly</id>
            <phase>deploy</phase>
            <goals>
              <goal>single</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.3</version>
        <configuration>
          <comparisonVersion>1.6.2</comparisonVersion>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <dependencies>
    <dependency>
      <groupId>org.joda</groupId>
      <artifactId>joda-convert</artifactId>
      <version>1.1</version>
      <scope>compile</scope>
      <optional>true</optional>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <reporting>
  	<plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-project-info-reports-plugin</artifactId>
        <reportSets>
          <reportSet>
            <reports>
              <report>index</report>
              <report>dependencies</report>
              <report>project-team</report>
              <report>mailing-list</report>
              <report>issue-tracking</report>
              <report>license</report>
              <report>scm</report>
              <report>summary</report>
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
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.7</version>
        <configuration>
          <linksource>true</linksource>
          <links>
            <link>http://download.oracle.com/javase/1.5.0/docs/api/</link>
          </links>
          <encoding>UTF-8</encoding>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-report-plugin</artifactId>
        <version>2.5</version>
        <configuration>
           <showSuccess>true</showSuccess>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
        <version>2.2</version>
      </plugin>
      <!--plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.5</version>
        <configuration>
          <linkXref>true</linkXref>
          <sourceEncoding>utf-8</sourceEncoding>
          <minimumTokens>100</minimumTokens>
          <targetJdk>1.5</targetJdk>
        </configuration>
      </plugin-->
  	</plugins>
  </reporting>
  <distributionManagement>
    <repository>
      <id>sonatype-joda-staging</id>
      <name>Sonatype OSS staging repository</name>
      <url>http://oss.sonatype.org/service/local/staging/deploy/maven2/</url>
      <layout>default</layout>
    </repository>
    <snapshotRepository>
      <uniqueVersion>false</uniqueVersion>
      <id>sonatype-joda-snapshot</id>
      <name>Sonatype OSS snapshot repository</name>
      <url>http://oss.sonatype.org/content/repositories/joda-snapshots</url>
      <layout>default</layout>
    </snapshotRepository>
    <site>
      <id>sf-web-joda-time</id>
      <name>Sourceforge Site</name>
      <url>scpexe://shell.sourceforge.net/home/project-web/joda-time/htdocs</url>
    </site>
    <downloadUrl>http://oss.sonatype.org/content/repositories/joda-releases</downloadUrl>
  </distributionManagement>
  <profiles>
    <profile>
      <id>repo-sign-artifacts</id>
      <activation>
        <property>
          <name>oss.repo</name>
          <value>true</value>
        </property>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-toolchains-plugin</artifactId>
            <version>1.0</version>
            <executions>
              <execution>
                <phase>validate</phase>
                <goals>
                  <goal>toolchain</goal>
                </goals>
              </execution>
            </executions>
            <configuration>
              <toolchains>
                <jdk>
                  <version>1.5</version>
                  <vendor>sun</vendor>
                </jdk>
              </toolchains>
            </configuration>
          </plugin>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-gpg-plugin</artifactId>
            <version>1.1</version>
            <executions>
              <execution>
                <id>sign-artifacts</id>
                <phase>verify</phase>
                <goals>
                  <goal>sign</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
</project>

```

## src/main/java/org/joda/time/chrono/GJChronology.java

```
/*
 *  Copyright 2001-2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.chrono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.DecoratedDurationField;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Implements the Gregorian/Julian calendar system which is the calendar system
 * used in most of the world. Wherever possible, it is recommended to use the
 * {@link ISOChronology} instead.
 * <p>
 * The Gregorian calendar replaced the Julian calendar, and the point in time
 * when this chronology switches can be controlled using the second parameter
 * of the getInstance method. By default this cutover is set to the date the
 * Gregorian calendar was first instituted, October 15, 1582.
 * <p>
 * Before this date, this chronology uses the proleptic Julian calendar
 * (proleptic means extending indefinitely). The Julian calendar has leap years
 * every four years, whereas the Gregorian has special rules for 100 and 400
 * years. A meaningful result will thus be obtained for all input values.
 * However before 8 CE, Julian leap years were irregular, and before 45 BCE
 * there was no Julian calendar.
 * <p>
 * This chronology differs from
 * {@link java.util.GregorianCalendar GregorianCalendar} in that years
 * in BCE are returned correctly. Thus year 1 BCE is returned as -1 instead of 1.
 * The yearOfEra field produces results compatible with GregorianCalendar.
 * <p>
 * The Julian calendar does not have a year zero, and so year -1 is followed by
 * year 1. If the Gregorian cutover date is specified at or before year -1
 * (Julian), year zero is defined. In other words, the proleptic Gregorian
 * chronology used by this class has a year zero.
 * <p>
 * To create a pure proleptic Julian chronology, use {@link JulianChronology},
 * and to create a pure proleptic Gregorian chronology, use
 * {@link GregorianChronology}.
 * <p>
 * GJChronology is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class GJChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -2545574827706931671L;

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convertByYear(long instant, Chronology from, Chronology to) {
        return to.getDateTimeMillis
            (from.year().get(instant),
             from.monthOfYear().get(instant),
             from.dayOfMonth().get(instant),
             from.millisOfDay().get(instant));
    }

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convertByWeekyear(final long instant, Chronology from, Chronology to) {
        long newInstant;
        newInstant = to.weekyear().set(0, from.weekyear().get(instant));
        newInstant = to.weekOfWeekyear().set(newInstant, from.weekOfWeekyear().get(instant));
        newInstant = to.dayOfWeek().set(newInstant, from.dayOfWeek().get(instant));
        newInstant = to.millisOfDay().set(newInstant, from.millisOfDay().get(instant));
        return newInstant;
    }

    /**
     * The default GregorianJulian cutover point.
     */
    static final Instant DEFAULT_CUTOVER = new Instant(-12219292800000L);

    /** Cache of zone to chronology list */
    private static final Map<DateTimeZone, ArrayList<GJChronology>> cCache = new HashMap<DateTimeZone, ArrayList<GJChronology>>();

    /**
     * Factory method returns instances of the default GJ cutover
     * chronology. This uses a cutover date of October 15, 1582 (Gregorian)
     * 00:00:00 UTC. For this value, October 4, 1582 (Julian) is followed by
     * October 15, 1582 (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * <p>The time zone of the returned instance is UTC.
     */
    public static GJChronology getInstanceUTC() {
        return getInstance(DateTimeZone.UTC, DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the default GJ cutover
     * chronology. This uses a cutover date of October 15, 1582 (Gregorian)
     * 00:00:00 UTC. For this value, October 4, 1582 (Julian) is followed by
     * October 15, 1582 (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * <p>The returned chronology is in the default time zone.
     */
    public static GJChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. This uses
     * a cutover date of October 15, 1582 (Gregorian) 00:00:00 UTC. For this
     * value, October 4, 1582 (Julian) is followed by October 15, 1582
     * (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     */
    public static GJChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * <p>The first day of the week is designated to be
     * {@link org.joda.time.DateTimeConstants#MONDAY Monday},
     * and the minimum days in the first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use, null means default
     */
    public static GJChronology getInstance(
            DateTimeZone zone,
            ReadableInstant gregorianCutover) {
        
        return getInstance(zone, gregorianCutover, 4);
    }
    
    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use, null means default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     */
    public static synchronized GJChronology getInstance(
            DateTimeZone zone,
            ReadableInstant gregorianCutover,
            int minDaysInFirstWeek) {
        
        zone = DateTimeUtils.getZone(zone);
        Instant cutoverInstant;
        if (gregorianCutover == null) {
            cutoverInstant = DEFAULT_CUTOVER;
        } else {
            cutoverInstant = gregorianCutover.toInstant();
        }

        GJChronology chrono;

        ArrayList<GJChronology> chronos = cCache.get(zone);
        if (chronos == null) {
            chronos = new ArrayList<GJChronology>(2);
            cCache.put(zone, chronos);
        } else {
            for (int i=chronos.size(); --i>=0; ) {
                chrono = chronos.get(i);
                if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() &&
                    cutoverInstant.equals(chrono.getGregorianCutover())) {
                    
                    return chrono;
                }
            }
        }

        if (zone == DateTimeZone.UTC) {
            chrono = new GJChronology
                (JulianChronology.getInstance(zone, minDaysInFirstWeek),
                 GregorianChronology.getInstance(zone, minDaysInFirstWeek),
                 cutoverInstant);
        } else {
            chrono = getInstance(DateTimeZone.UTC, cutoverInstant, minDaysInFirstWeek);
            chrono = new GJChronology
                (ZonedChronology.getInstance(chrono, zone),
                 chrono.iJulianChronology,
                 chrono.iGregorianChronology,
                 chrono.iCutoverInstant);
        }

        chronos.add(chrono);

        return chrono;
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     */
    public static GJChronology getInstance(
            DateTimeZone zone,
            long gregorianCutover,
            int minDaysInFirstWeek) {
        
        Instant cutoverInstant;
        if (gregorianCutover == DEFAULT_CUTOVER.getMillis()) {
            cutoverInstant = null;
        } else {
            cutoverInstant = new Instant(gregorianCutover);
        }
        return getInstance(zone, cutoverInstant, minDaysInFirstWeek);
    }

    //-----------------------------------------------------------------------
    private JulianChronology iJulianChronology;
    private GregorianChronology iGregorianChronology;
    private Instant iCutoverInstant;

    private long iCutoverMillis;
    private long iGapDuration;

    /**
     * @param julian chronology used before the cutover instant
     * @param gregorian chronology used at and after the cutover instant
     * @param cutoverInstant instant when the gregorian chronology began
     */
    private GJChronology(JulianChronology julian,
                         GregorianChronology gregorian,
                         Instant cutoverInstant) {
        super(null, new Object[] {julian, gregorian, cutoverInstant});
    }

    /**
     * Called when applying a time zone.
     */
    private GJChronology(Chronology base,
                         JulianChronology julian,
                         GregorianChronology gregorian,
                         Instant cutoverInstant) {
        super(base, new Object[] {julian, gregorian, cutoverInstant});
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return getInstance(getZone(), iCutoverInstant, getMinimumDaysInFirstWeek());
    }

    public DateTimeZone getZone() {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getZone();
        }
        return DateTimeZone.UTC;
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return withZone(DateTimeZone.UTC);
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone, iCutoverInstant, getMinimumDaysInFirstWeek());
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        }

        // Assume date is Gregorian.
        long instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth, millisOfDay);
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth, millisOfDay);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        // Assume date is Gregorian.
        long instant;
        try {
            instant = iGregorianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        } catch (IllegalFieldValueException ex) {
            if (monthOfYear != 2 || dayOfMonth != 29) {
                throw ex;
            }
            instant = iGregorianChronology.getDateTimeMillis
                (year, monthOfYear, 28,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                throw ex;
            }
        }
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }

    /**
     * Gets the cutover instant between Gregorian and Julian chronologies.
     * @return the cutover instant
     */
    public Instant getGregorianCutover() {
        return iCutoverInstant;
    }

    /**
     * Gets the minimum days needed for a week to be the first week in a year.
     * 
     * @return the minimum days
     */
    public int getMinimumDaysInFirstWeek() {
        return iGregorianChronology.getMinimumDaysInFirstWeek();
    }

    /**
     * Checks if this chronology instance equals another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     * @since 1.6
     */
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * A suitable hash code for the chronology.
     * 
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return "GJ".hashCode() * 11 + iJulianChronology.hashCode() +
            iGregorianChronology.hashCode() + iCutoverInstant.hashCode();
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(60);
        sb.append("GJChronology");
        sb.append('[');
        sb.append(getZone().getID());
        
        if (iCutoverMillis != DEFAULT_CUTOVER.getMillis()) {
            sb.append(",cutover=");
            DateTimeFormatter printer;
            if (withUTC().dayOfYear().remainder(iCutoverMillis) == 0) {
                printer = ISODateTimeFormat.date();
            } else {
                printer = ISODateTimeFormat.dateTime();
            }
            printer.withChronology(withUTC()).printTo(sb, iCutoverMillis);
        }
        
        if (getMinimumDaysInFirstWeek() != 4) {
            sb.append(",mdfw=");
            sb.append(getMinimumDaysInFirstWeek());
        }
        sb.append(']');
        
        return sb.toString();
    }

    protected void assemble(Fields fields) {
        Object[] params = (Object[])getParam();

        JulianChronology julian = (JulianChronology)params[0];
        GregorianChronology gregorian = (GregorianChronology)params[1];
        Instant cutoverInstant = (Instant)params[2];
        iCutoverMillis = cutoverInstant.getMillis();

        iJulianChronology = julian;
        iGregorianChronology = gregorian;
        iCutoverInstant = cutoverInstant;

        if (getBase() != null) {
            return;
        }

        if (julian.getMinimumDaysInFirstWeek() != gregorian.getMinimumDaysInFirstWeek()) {
            throw new IllegalArgumentException();
        }

        // Compute difference between the chronologies at the cutover instant
        iGapDuration = iCutoverMillis - julianToGregorianByYear(iCutoverMillis);

        // Begin field definitions.

        // First just copy all the Gregorian fields and then override those
        // that need special attention.
        fields.copyFieldsFrom(gregorian);
        
        // Assuming cutover is at midnight, all time of day fields can be
        // gregorian since they are unaffected by cutover.

        // Verify assumption.
        if (gregorian.millisOfDay().get(iCutoverMillis) == 0) {
            // Cutover is sometime in the day, so cutover fields are required
            // for time of day.

            fields.millisOfSecond = new CutoverField(julian.millisOfSecond(), fields.millisOfSecond, iCutoverMillis);
            fields.millisOfDay = new CutoverField(julian.millisOfDay(), fields.millisOfDay, iCutoverMillis);
            fields.secondOfMinute = new CutoverField(julian.secondOfMinute(), fields.secondOfMinute, iCutoverMillis);
            fields.secondOfDay = new CutoverField(julian.secondOfDay(), fields.secondOfDay, iCutoverMillis);
            fields.minuteOfHour = new CutoverField(julian.minuteOfHour(), fields.minuteOfHour, iCutoverMillis);
            fields.minuteOfDay = new CutoverField(julian.minuteOfDay(), fields.minuteOfDay, iCutoverMillis);
            fields.hourOfDay = new CutoverField(julian.hourOfDay(), fields.hourOfDay, iCutoverMillis);
            fields.hourOfHalfday = new CutoverField(julian.hourOfHalfday(), fields.hourOfHalfday, iCutoverMillis);
            fields.clockhourOfDay = new CutoverField(julian.clockhourOfDay(), fields.clockhourOfDay, iCutoverMillis);
            fields.clockhourOfHalfday = new CutoverField(julian.clockhourOfHalfday(),
                                                         fields.clockhourOfHalfday, iCutoverMillis);
            fields.halfdayOfDay = new CutoverField(julian.halfdayOfDay(), fields.halfdayOfDay, iCutoverMillis);
        }

        // These fields just require basic cutover support.
        {
            fields.era = new CutoverField(julian.era(), fields.era, iCutoverMillis);
        }

        // DayOfYear and weekOfWeekyear require special handling since cutover
        // year has fewer days and weeks. Extend the cutover to the start of
        // the next year or weekyear. This keeps the sequence unbroken during
        // the cutover year.

        {
            long cutover = gregorian.year().roundCeiling(iCutoverMillis);
            fields.dayOfYear = new CutoverField(
                julian.dayOfYear(), fields.dayOfYear, cutover);
        }

        {
            long cutover = gregorian.weekyear().roundCeiling(iCutoverMillis);
            fields.weekOfWeekyear = new CutoverField(
                julian.weekOfWeekyear(), fields.weekOfWeekyear, cutover, true);
        }

        // These fields are special because they have imprecise durations. The
        // family of addition methods need special attention. Override affected
        // duration fields as well.
        {
            fields.year = new ImpreciseCutoverField(
                julian.year(), fields.year, iCutoverMillis);
            fields.years = fields.year.getDurationField();
            fields.yearOfEra = new ImpreciseCutoverField(
                julian.yearOfEra(), fields.yearOfEra, fields.years, iCutoverMillis);
            fields.yearOfCentury = new ImpreciseCutoverField(
                julian.yearOfCentury(), fields.yearOfCentury, fields.years, iCutoverMillis);
            
            fields.centuryOfEra = new ImpreciseCutoverField(
                julian.centuryOfEra(), fields.centuryOfEra, iCutoverMillis);
            fields.centuries = fields.centuryOfEra.getDurationField();
            
            fields.monthOfYear = new ImpreciseCutoverField(
                julian.monthOfYear(), fields.monthOfYear, iCutoverMillis);
            fields.months = fields.monthOfYear.getDurationField();
            
            fields.weekyear = new ImpreciseCutoverField(
                julian.weekyear(), fields.weekyear, null, iCutoverMillis, true);
            fields.weekyearOfCentury = new ImpreciseCutoverField(
                julian.weekyearOfCentury(), fields.weekyearOfCentury, fields.weekyears, iCutoverMillis);
            fields.weekyears = fields.weekyear.getDurationField();
        }

        // These fields require basic cutover support, except they must link to
        // imprecise durations.
        {
            CutoverField cf = new CutoverField
                (julian.dayOfMonth(), fields.dayOfMonth, iCutoverMillis);
            cf.iRangeDurationField = fields.months;
            fields.dayOfMonth = cf;
        }
    }

    long julianToGregorianByYear(long instant) {
        return convertByYear(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulianByYear(long instant) {
        return convertByYear(instant, iGregorianChronology, iJulianChronology);
    }

    long julianToGregorianByWeekyear(long instant) {
        return convertByWeekyear(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulianByWeekyear(long instant) {
        return convertByWeekyear(instant, iGregorianChronology, iJulianChronology);
    }

    //-----------------------------------------------------------------------
    /**
     * This basic cutover field adjusts calls to 'get' and 'set' methods, and
     * assumes that calls to add and addWrapField are unaffected by the cutover.
     */
    private class CutoverField extends BaseDateTimeField {
        private static final long serialVersionUID = 3528501219481026402L;

        final DateTimeField iJulianField;
        final DateTimeField iGregorianField;
        final long iCutover;
        final boolean iConvertByWeekyear;

        protected DurationField iDurationField;
        protected DurationField iRangeDurationField;

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         * @param cutoverMillis  the millis of the cutover
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverMillis) {
            this(julianField, gregorianField, cutoverMillis, false);
        }

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         * @param cutoverMillis  the millis of the cutover
         * @param convertByWeekyear
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField,
                     long cutoverMillis, boolean convertByWeekyear) {
            super(gregorianField.getType());
            iJulianField = julianField;
            iGregorianField = gregorianField;
            iCutover = cutoverMillis;
            iConvertByWeekyear = convertByWeekyear;
            // Although average length of Julian and Gregorian years differ,
            // use the Gregorian duration field because it is more accurate.
            iDurationField = gregorianField.getDurationField();

            DurationField rangeField = gregorianField.getRangeDurationField();
            if (rangeField == null) {
                rangeField = julianField.getRangeDurationField();
            }
            iRangeDurationField = rangeField;
        }

        public boolean isLenient() {
            return false;
        }

        public int get(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.get(instant);
            } else {
                return iJulianField.get(instant);
            }
        }

        public String getAsText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsText(instant, locale);
            } else {
                return iJulianField.getAsText(instant, locale);
            }
        }

        public String getAsText(int fieldValue, Locale locale) {
            return iGregorianField.getAsText(fieldValue, locale);
        }

        public String getAsShortText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsShortText(instant, locale);
            } else {
                return iJulianField.getAsShortText(instant, locale);
            }
        }

        public String getAsShortText(int fieldValue, Locale locale) {
            return iGregorianField.getAsShortText(fieldValue, locale);
        }

        public long add(long instant, int value) {
            return iGregorianField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iGregorianField.add(instant, value);
        }

        public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
            // overridden as superclass algorithm can't handle
            // 2004-02-29 + 48 months -> 2008-02-29 type dates
            if (valueToAdd == 0) {
                return values;
            }
            if (DateTimeUtils.isContiguous(partial)) {
                long instant = 0L;
                for (int i = 0, isize = partial.size(); i < isize; i++) {
                    instant = partial.getFieldType(i).getField(GJChronology.this).set(instant, values[i]);
                }
                instant = add(instant, valueToAdd);
                return GJChronology.this.get(partial, instant);
            } else {
                return super.add(partial, fieldIndex, values, valueToAdd);
            }
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }

        public long set(long instant, int value) {
            if (instant >= iCutover) {
                instant = iGregorianField.set(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                    // Verify that new value stuck.
                    if (get(instant) != value) {
                        throw new IllegalFieldValueException
                            (iGregorianField.getType(), Integer.valueOf(value), null, null);
                    }
                }
            } else {
                instant = iJulianField.set(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                    // Verify that new value stuck.
                    if (get(instant) != value) {
                       throw new IllegalFieldValueException
                            (iJulianField.getType(), Integer.valueOf(value), null, null);
                    }
                }
            }
            return instant;
        }

        public long set(long instant, String text, Locale locale) {
            if (instant >= iCutover) {
                instant = iGregorianField.set(instant, text, locale);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            } else {
                instant = iJulianField.set(instant, text, locale);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            }
            return instant;
        }

        public DurationField getDurationField() {
            return iDurationField;
        }

        public DurationField getRangeDurationField() {
            return iRangeDurationField;
        }

        public boolean isLeap(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.isLeap(instant);
            } else {
                return iJulianField.isLeap(instant);
            }
        }

        public int getLeapAmount(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getLeapAmount(instant);
            } else {
                return iJulianField.getLeapAmount(instant);
            }
        }

        public DurationField getLeapDurationField() {
            return iGregorianField.getLeapDurationField();
        }


        public int getMinimumValue() {
            // For all precise fields, the Julian and Gregorian limits are
            // identical. Choose Julian to tighten up the year limits.
            return iJulianField.getMinimumValue();
        }

        public int getMinimumValue(ReadablePartial partial) {
            return iJulianField.getMinimumValue(partial);
        }

        public int getMinimumValue(ReadablePartial partial, int[] values) {
            return iJulianField.getMinimumValue(partial, values);
        }

        public int getMinimumValue(long instant) {
            if (instant < iCutover) {
                return iJulianField.getMinimumValue(instant);
            }

            int min = iGregorianField.getMinimumValue(instant);

            // Because the cutover may reduce the length of this field, verify
            // the minimum by setting it.
            instant = iGregorianField.set(instant, min);
            if (instant < iCutover) {
                min = iGregorianField.get(iCutover);
            }

            return min;
        }

        public int getMaximumValue() {
            // For all precise fields, the Julian and Gregorian limits are
            // identical.
            return iGregorianField.getMaximumValue();
        }

        public int getMaximumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMaximumValue(instant);
            }

            int max = iJulianField.getMaximumValue(instant);

            // Because the cutover may reduce the length of this field, verify
            // the maximum by setting it.
            instant = iJulianField.set(instant, max);
            if (instant >= iCutover) {
                max = iJulianField.get(iJulianField.add(iCutover, -1));
            }

            return max;
        }

        public int getMaximumValue(ReadablePartial partial) {
            long instant = GJChronology.getInstanceUTC().set(partial, 0L);
            return getMaximumValue(instant);
        }

        public int getMaximumValue(ReadablePartial partial, int[] values) {
            Chronology chrono = GJChronology.getInstanceUTC();
            long instant = 0L;
            for (int i = 0, isize = partial.size(); i < isize; i++) {
                DateTimeField field = partial.getFieldType(i).getField(chrono);
                if (values[i] <= field.getMaximumValue(instant)) {
                    instant = field.set(instant, values[i]);
                }
            }
            return getMaximumValue(instant);
        }

        public long roundFloor(long instant) {
            if (instant >= iCutover) {
                instant = iGregorianField.roundFloor(instant);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.roundFloor(instant);
            }
            return instant;
        }

        public long roundCeiling(long instant) {
            if (instant >= iCutover) {
                instant = iGregorianField.roundCeiling(instant);
            } else {
                instant = iJulianField.roundCeiling(instant);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }

        public int getMaximumTextLength(Locale locale) {
            return Math.max(iJulianField.getMaximumTextLength(locale),
                            iGregorianField.getMaximumTextLength(locale));
        }

        public int getMaximumShortTextLength(Locale locale) {
            return Math.max(iJulianField.getMaximumShortTextLength(locale),
                            iGregorianField.getMaximumShortTextLength(locale));
        }

        protected long julianToGregorian(long instant) {
            if (iConvertByWeekyear) {
                return julianToGregorianByWeekyear(instant);
            } else {
                return julianToGregorianByYear(instant);
            }
        }

        protected long gregorianToJulian(long instant) {
            if (iConvertByWeekyear) {
                return gregorianToJulianByWeekyear(instant);
            } else {
                return gregorianToJulianByYear(instant);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Cutover field for variable length fields. These fields internally call
     * set whenever add is called. As a result, the same correction applied to
     * set must be applied to add and addWrapField. Knowing when to use this
     * field requires specific knowledge of how the GJ fields are implemented.
     */
    private final class ImpreciseCutoverField extends CutoverField {
        private static final long serialVersionUID = 3410248757173576441L;

        /**
         * Creates a duration field that links back to this.
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverMillis) {
            this(julianField, gregorianField, null, cutoverMillis, false);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField, long cutoverMillis)
        {
            this(julianField, gregorianField, durationField, cutoverMillis, false);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField,
                              long cutoverMillis, boolean convertByWeekyear)
        {
            super(julianField, gregorianField, cutoverMillis, convertByWeekyear);
            if (durationField == null) {
                durationField = new LinkedDurationField(iDurationField, this);
            }
            iDurationField = durationField;
        }

        public long add(long instant, int value) {
            if (instant >= iCutover) {
                instant = iGregorianField.add(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }
        
        public long add(long instant, long value) {
            if (instant >= iCutover) {
                instant = iGregorianField.add(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            if (minuendInstant >= iCutover) {
                if (subtrahendInstant >= iCutover) {
                    return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendInstant = gregorianToJulian(minuendInstant);
                return iJulianField.getDifference(minuendInstant, subtrahendInstant);
            } else {
                if (subtrahendInstant < iCutover) {
                    return iJulianField.getDifference(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendInstant = julianToGregorian(minuendInstant);
                return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
            }
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            if (minuendInstant >= iCutover) {
                if (subtrahendInstant >= iCutover) {
                    return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendInstant = gregorianToJulian(minuendInstant);
                return iJulianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            } else {
                if (subtrahendInstant < iCutover) {
                    return iJulianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendInstant = julianToGregorian(minuendInstant);
                return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            }
        }

        // Since the imprecise fields have durations longer than the gap
        // duration, keep these methods simple. The inherited implementations
        // produce incorrect results.
        //
        // Degenerate case: If this field is a month, and the cutover is set
        // far into the future, then the gap duration may be so large as to
        // reduce the number of months in a year. If the missing month(s) are
        // at the beginning or end of the year, then the minimum and maximum
        // values are not 1 and 12. I don't expect this case to ever occur.

        public int getMinimumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMinimumValue(instant);
            } else {
                return iJulianField.getMinimumValue(instant);
            }
        }

        public int getMaximumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMaximumValue(instant);
            } else {
                return iJulianField.getMaximumValue(instant);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Links the duration back to a ImpreciseCutoverField.
     */
    private static class LinkedDurationField extends DecoratedDurationField {
        private static final long serialVersionUID = 4097975388007713084L;

        private final ImpreciseCutoverField iField;

        LinkedDurationField(DurationField durationField, ImpreciseCutoverField dateTimeField) {
            super(durationField, durationField.getType());
            iField = dateTimeField;
        }

        public long add(long instant, int value) {
            return iField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iField.add(instant, value);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return iField.getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }
    }

}

```
