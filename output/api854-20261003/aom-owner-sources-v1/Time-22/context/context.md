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
  <version>2.0</version>
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
    <connection>scm:svn:http://joda-time.svn.sourceforge.net/svnroot/joda-time/trunk/JodaTime/</connection>
    <developerConnection>scm:svn:https://joda-time.svn.sourceforge.net/svnroot/joda-time/trunk/JodaTime/</developerConnection>
    <url>http://joda-time.svn.sourceforge.net/viewvc/joda-time/trunk/JodaTime/</url>
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
        </includes>
      </resource>
      <resource>
        <targetPath>.</targetPath>
        <directory>src/main/java</directory>
        <includes>
          <include>org/joda/time/format/*.properties</include>
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
            <descriptor>src/main/assembly/src.xml</descriptor>
            <descriptor>src/main/assembly/bin.xml</descriptor>
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
      <url>scp://shell.sourceforge.net/home/groups/j/jo/joda-time/htdocs</url>
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

## src/main/java/org/joda/time/base/BasePeriod.java

```
/*
 *  Copyright 2001-2007 Stephen Colebourne
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
package org.joda.time.base;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PeriodConverter;
import org.joda.time.field.FieldUtils;

/**
 * BasePeriod is an abstract implementation of ReadablePeriod that stores
 * data in a <code>PeriodType</code> and an <code>int[]</code>.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadablePeriod} interface should be used when different 
 * kinds of period objects are to be referenced.
 * <p>
 * BasePeriod subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BasePeriod
        extends AbstractPeriod
        implements ReadablePeriod, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -2110953284060001145L;

    /** The type of period */
    private PeriodType iType;
    /** The values */
    private int[] iValues;

    //-----------------------------------------------------------------------
    /**
     * Creates a period from a set of field values.
     *
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @param type  which set of fields this period supports
     * @throws IllegalArgumentException if period type is invalid
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected BasePeriod(int years, int months, int weeks, int days,
                         int hours, int minutes, int seconds, int millis,
                         PeriodType type) {
        super();
        type = checkPeriodType(type);
        iType = type;
        setPeriodInternal(years, months, weeks, days, hours, minutes, seconds, millis); // internal method
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     * @param chrono  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if period type is invalid
     */
    protected BasePeriod(long startInstant, long endInstant, PeriodType type, Chronology chrono) {
        super();
        type = checkPeriodType(type);
        chrono = DateTimeUtils.getChronology(chrono);
        iType = type;
        iValues = chrono.get(this, startInstant, endInstant);
    }

    /**
     * Creates a period from the given interval endpoints.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports, null means standard
     * @throws IllegalArgumentException if period type is invalid
     */
    protected BasePeriod(ReadableInstant startInstant, ReadableInstant endInstant, PeriodType type) {
        super();
        type = checkPeriodType(type);
        if (startInstant == null && endInstant == null) {
            iType = type;
            iValues = new int[size()];
        } else {
            long startMillis = DateTimeUtils.getInstantMillis(startInstant);
            long endMillis = DateTimeUtils.getInstantMillis(endInstant);
            Chronology chrono = DateTimeUtils.getIntervalChronology(startInstant, endInstant);
            iType = type;
            iValues = chrono.get(this, startMillis, endMillis);
        }
    }

    /**
     * Creates a period from the given duration and end point.
     * <p>
     * The two partials must contain the same fields, thus you can
     * specify two <code>LocalDate</code> objects, or two <code>LocalTime</code>
     * objects, but not one of each.
     * As these are Partial objects, time zones have no effect on the result.
     * <p>
     * The two partials must also both be contiguous - see
     * {@link DateTimeUtils#isContiguous(ReadablePartial)} for a
     * definition. Both <code>LocalDate</code> and <code>LocalTime</code> are contiguous.
     *
     * @param start  the start of the period, must not be null
     * @param end  the end of the period, must not be null
     * @param type  which set of fields this period supports, null means standard
     * @throws IllegalArgumentException if the partials are null or invalid
     * @since 1.1
     */
    protected BasePeriod(ReadablePartial start, ReadablePartial end, PeriodType type) {
        super();
        if (start == null || end == null) {
            throw new IllegalArgumentException("ReadablePartial objects must not be null");
        }
        if (start instanceof BaseLocal && end instanceof BaseLocal && start.getClass() == end.getClass()) {
            // for performance
            type = checkPeriodType(type);
            long startMillis = ((BaseLocal) start).getLocalMillis();
            long endMillis = ((BaseLocal) end).getLocalMillis();
            Chronology chrono = start.getChronology();
            chrono = DateTimeUtils.getChronology(chrono);
            iType = type;
            iValues = chrono.get(this, startMillis, endMillis);
        } else {
            if (start.size() != end.size()) {
                throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
            }
            for (int i = 0, isize = start.size(); i < isize; i++) {
                if (start.getFieldType(i) != end.getFieldType(i)) {
                    throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
                }
            }
            if (DateTimeUtils.isContiguous(start) == false) {
                throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
            }
            iType = checkPeriodType(type);
            Chronology chrono = DateTimeUtils.getChronology(start.getChronology()).withUTC();
            iValues = chrono.get(this, chrono.set(start, 0L), chrono.set(end, 0L));
        }
    }

    /**
     * Creates a period from the given start point and duration.
     *
     * @param startInstant  the interval start, null means now
     * @param duration  the duration of the interval, null means zero-length
     * @param type  which set of fields this period supports, null means standard
     */
    protected BasePeriod(ReadableInstant startInstant, ReadableDuration duration, PeriodType type) {
        super();
        type = checkPeriodType(type);
        long startMillis = DateTimeUtils.getInstantMillis(startInstant);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        long endMillis = FieldUtils.safeAdd(startMillis, durationMillis);
        Chronology chrono = DateTimeUtils.getInstantChronology(startInstant);
        iType = type;
        iValues = chrono.get(this, startMillis, endMillis);
    }

    /**
     * Creates a period from the given duration and end point.
     *
     * @param duration  the duration of the interval, null means zero-length
     * @param endInstant  the interval end, null means now
     * @param type  which set of fields this period supports, null means standard
     */
    protected BasePeriod(ReadableDuration duration, ReadableInstant endInstant, PeriodType type) {
        super();
        type = checkPeriodType(type);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        long endMillis = DateTimeUtils.getInstantMillis(endInstant);
        long startMillis = FieldUtils.safeSubtract(endMillis, durationMillis);
        Chronology chrono = DateTimeUtils.getInstantChronology(endInstant);
        iType = type;
        iValues = chrono.get(this, startMillis, endMillis);
    }

    /**
     * Creates a period from the given millisecond duration with the standard period type
     * and ISO rules, ensuring that the calculation is performed with the time-only period type.
     * <p>
     * The calculation uses the hour, minute, second and millisecond fields.
     *
     * @param duration  the duration, in milliseconds
     */
    protected BasePeriod(long duration) {
        super();
        // bug [3264409]
        iType = PeriodType.time();
        int[] values = ISOChronology.getInstanceUTC().get(this, duration);
        iType = PeriodType.standard();
        iValues = new int[8];
        System.arraycopy(values, 0, iValues, 4, 4);
    }

    /**
     * Creates a period from the given millisecond duration, which is only really
     * suitable for durations less than one day.
     * <p>
     * Only fields that are precise will be used.
     * Thus the largest precise field may have a large value.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this period supports, null means standard
     * @param chrono  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if period type is invalid
     */
    protected BasePeriod(long duration, PeriodType type, Chronology chrono) {
        super();
        type = checkPeriodType(type);
        chrono = DateTimeUtils.getChronology(chrono);
        iType = type;
        iValues = chrono.get(this, duration);
    }

    /**
     * Creates a new period based on another using the {@link ConverterManager}.
     *
     * @param period  the period to convert
     * @param type  which set of fields this period supports, null means use type from object
     * @param chrono  the chronology to use, null means ISO default
     * @throws IllegalArgumentException if period is invalid
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected BasePeriod(Object period, PeriodType type, Chronology chrono) {
        super();
        PeriodConverter converter = ConverterManager.getInstance().getPeriodConverter(period);
        type = (type == null ? converter.getPeriodType(period) : type);
        type = checkPeriodType(type);
        iType = type;
        if (this instanceof ReadWritablePeriod) {
            iValues = new int[size()];
            chrono = DateTimeUtils.getChronology(chrono);
            converter.setInto((ReadWritablePeriod) this, period, chrono);
        } else {
            iValues = new MutablePeriod(period, type, chrono).getValues();
        }
    }

    /**
     * Constructor used when we trust ourselves.
     * Do not expose publically.
     *
     * @param values  the values to use, not null, not cloned
     * @param type  which set of fields this period supports, not null
     */
    protected BasePeriod(int[] values, PeriodType type) {
        super();
        iType = type;
        iValues = values;
    }

    //-----------------------------------------------------------------------
    /**
     * Validates a period type, converting nulls to a default value and
     * checking the type is suitable for this instance.
     * 
     * @param type  the type to check, may be null
     * @return the validated type to use, not null
     * @throws IllegalArgumentException if the period type is invalid
     */
    protected PeriodType checkPeriodType(PeriodType type) {
        return DateTimeUtils.getPeriodType(type);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the period type.
     *
     * @return the period type
     */
    public PeriodType getPeriodType() {
        return iType;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields that this period supports.
     *
     * @return the number of fields supported
     */
    public int size() {
        return iType.size();
    }

    /**
     * Gets the field type at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DurationFieldType getFieldType(int index) {
        return iType.getFieldType(index);
    }

    /**
     * Gets the value at the specified index.
     *
     * @param index  the index to retrieve
     * @return the value of the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        return iValues[index];
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the total millisecond duration of this period relative to a start instant.
     * <p>
     * This method adds the period to the specified instant in order to
     * calculate the duration.
     * <p>
     * An instant must be supplied as the duration of a period varies.
     * For example, a period of 1 month could vary between the equivalent of
     * 28 and 31 days in milliseconds due to different length months.
     * Similarly, a day can vary at Daylight Savings cutover, typically between
     * 23 and 25 hours.
     *
     * @param startInstant  the instant to add the period to, thus obtaining the duration
     * @return the total length of the period as a duration relative to the start instant
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public Duration toDurationFrom(ReadableInstant startInstant) {
        long startMillis = DateTimeUtils.getInstantMillis(startInstant);
        Chronology chrono = DateTimeUtils.getInstantChronology(startInstant);
        long endMillis = chrono.add(this, startMillis, 1);
        return new Duration(startMillis, endMillis);
    }

    /**
     * Gets the total millisecond duration of this period relative to an
     * end instant.
     * <p>
     * This method subtracts the period from the specified instant in order
     * to calculate the duration.
     * <p>
     * An instant must be supplied as the duration of a period varies.
     * For example, a period of 1 month could vary between the equivalent of
     * 28 and 31 days in milliseconds due to different length months.
     * Similarly, a day can vary at Daylight Savings cutover, typically between
     * 23 and 25 hours.
     *
     * @param endInstant  the instant to subtract the period from, thus obtaining the duration
     * @return the total length of the period as a duration relative to the end instant
     * @throws ArithmeticException if the millis exceeds the capacity of the duration
     */
    public Duration toDurationTo(ReadableInstant endInstant) {
        long endMillis = DateTimeUtils.getInstantMillis(endInstant);
        Chronology chrono = DateTimeUtils.getInstantChronology(endInstant);
        long startMillis = chrono.add(this, endMillis, -1);
        return new Duration(startMillis, endMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether a field type is supported, and if so adds the new value
     * to the relevent index in the specified array.
     * 
     * @param type  the field type
     * @param values  the array to update
     * @param newValue  the new value to store if successful
     */
    private void checkAndUpdate(DurationFieldType type, int[] values, int newValue) {
        int index = indexOf(type);
        if (index == -1) {
            if (newValue != 0) {
                throw new IllegalArgumentException(
                    "Period does not support field '" + type.getName() + "'");
            }
        } else {
            values[index] = newValue;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets all the fields of this period from another.
     * 
     * @param period  the period to copy from, not null
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setPeriod(ReadablePeriod period) {
        if (period == null) {
            setValues(new int[size()]);
        } else {
            setPeriodInternal(period);
        }
    }

    /**
     * Private method called from constructor.
     */
    private void setPeriodInternal(ReadablePeriod period) {
        int[] newValues = new int[size()];
        for (int i = 0, isize = period.size(); i < isize; i++) {
            DurationFieldType type = period.getFieldType(i);
            int value = period.getValue(i);
            checkAndUpdate(type, newValues, value);
        }
        iValues = newValues;
    }

    /**
     * Sets the eight standard the fields in one go.
     * 
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void setPeriod(int years, int months, int weeks, int days,
                             int hours, int minutes, int seconds, int millis) {
        setPeriodInternal(years, months, weeks, days, hours, minutes, seconds, millis);
    }

    /**
     * Private method called from constructor.
     */
    private void setPeriodInternal(int years, int months, int weeks, int days,
                                   int hours, int minutes, int seconds, int millis) {
        int[] newValues = new int[size()];
        checkAndUpdate(DurationFieldType.years(), newValues, years);
        checkAndUpdate(DurationFieldType.months(), newValues, months);
        checkAndUpdate(DurationFieldType.weeks(), newValues, weeks);
        checkAndUpdate(DurationFieldType.days(), newValues, days);
        checkAndUpdate(DurationFieldType.hours(), newValues, hours);
        checkAndUpdate(DurationFieldType.minutes(), newValues, minutes);
        checkAndUpdate(DurationFieldType.seconds(), newValues, seconds);
        checkAndUpdate(DurationFieldType.millis(), newValues, millis);
        iValues = newValues;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of a field in this period.
     * 
     * @param field  the field to set
     * @param value  the value to set
     * @throws IllegalArgumentException if field is is null or not supported.
     */
    protected void setField(DurationFieldType field, int value) {
        setFieldInto(iValues, field, value);
    }

    /**
     * Sets the value of a field in this period.
     * 
     * @param values  the array of values to update
     * @param field  the field to set
     * @param value  the value to set
     * @throws IllegalArgumentException if field is null or not supported.
     */
    protected void setFieldInto(int[] values, DurationFieldType field, int value) {
        int index = indexOf(field);
        if (index == -1) {
            if (value != 0 || field == null) {
                throw new IllegalArgumentException(
                    "Period does not support field '" + field + "'");
            }
        } else {
            values[index] = value;
        }
    }

    /**
     * Adds the value of a field in this period.
     * 
     * @param field  the field to set
     * @param value  the value to set
     * @throws IllegalArgumentException if field is is null or not supported.
     */
    protected void addField(DurationFieldType field, int value) {
        addFieldInto(iValues, field, value);
    }

    /**
     * Adds the value of a field in this period.
     * 
     * @param values  the array of values to update
     * @param field  the field to set
     * @param value  the value to set
     * @throws IllegalArgumentException if field is is null or not supported.
     */
    protected void addFieldInto(int[] values, DurationFieldType field, int value) {
        int index = indexOf(field);
        if (index == -1) {
            if (value != 0 || field == null) {
                throw new IllegalArgumentException(
                    "Period does not support field '" + field + "'");
            }
        } else {
            values[index] = FieldUtils.safeAdd(values[index], value);
        }
    }

    /**
     * Merges the fields from another period.
     * 
     * @param period  the period to add from, not null
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void mergePeriod(ReadablePeriod period) {
        if (period != null) {
            iValues = mergePeriodInto(getValues(), period);
        }
    }

    /**
     * Merges the fields from another period.
     * 
     * @param values  the array of values to update
     * @param period  the period to add from, not null
     * @return the updated values
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected int[] mergePeriodInto(int[] values, ReadablePeriod period) {
         for (int i = 0, isize = period.size(); i < isize; i++) {
             DurationFieldType type = period.getFieldType(i);
             int value = period.getValue(i);
             checkAndUpdate(type, values, value);
         }
         return values;
    }

    /**
     * Adds the fields from another period.
     * 
     * @param period  the period to add from, not null
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected void addPeriod(ReadablePeriod period) {
        if (period != null) {
            iValues = addPeriodInto(getValues(), period);
        }
    }

    /**
     * Adds the fields from another period.
     * 
     * @param values  the array of values to update
     * @param period  the period to add from, not null
     * @return the updated values
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    protected int[] addPeriodInto(int[] values, ReadablePeriod period) {
         for (int i = 0, isize = period.size(); i < isize; i++) {
             DurationFieldType type = period.getFieldType(i);
             int value = period.getValue(i);
             if (value != 0) {
                 int index = indexOf(type);
                 if (index == -1) {
                     throw new IllegalArgumentException(
                         "Period does not support field '" + type.getName() + "'");
                 } else {
                     values[index] = FieldUtils.safeAdd(getValue(index), value);
                 }
             }
         }
         return values;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @param value  the value to set
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected void setValue(int index, int value) {
        iValues[index] = value;
    }

    /**
     * Sets the values of all fields.
     * 
     * @param values  the array of values
     */
    protected void setValues(int[] values) {
        iValues = values;
    }

}

```
