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
  <property name="component.version"       value="2.1"/>
  <property name="previous.version"        value="2.0"/>

  <!-- The current version number of this component -->
  <property name="component.fullname"      value="${component.name}-${component.version}"/>
  
  <!-- The directory of source files -->
  <property name="xdocs"                   value="xdocs"/>

  <!-- Dependencies -->
  <property name="lib"                     value="lib"/>
  <property name="junit.jar"               value="${lib}/junit-3.8.2.jar"/>
  <property name="jodaconvert.jar"         value="${lib}/joda-convert-1.2.jar"/>
  <property name="jodaprevious.jar"        value="${lib}/joda-time-${previous.version}.jar"/>

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
    <available property="jodaprevious.present" file="${jodaprevious.jar}"/>
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
    <echo message="Getting joda-convert from http://repo2.maven.org/maven2/org/joda/joda-convert/1.2"/>
    <setproxy />
    <mkdir dir="${lib}"/>
    <get dest="${jodaconvert.jar}" usetimestamp="true" ignoreerrors="true" src="http://repo2.maven.org/maven2/org/joda/joda-convert/1.2/joda-convert-1.2.jar" />
  </target>
  <target name="getjodaprevious" unless="jodaprevious.present">
    <echo message="Getting joda-convert from http://repo2.maven.org/maven2/joda-time/joda-time/${previous.version}"/>
    <setproxy />
    <mkdir dir="${lib}"/>
    <get dest="${jodaprevious.jar}" usetimestamp="true" ignoreerrors="true" src="http://repo2.maven.org/maven2/joda-time/joda-time/${previous.version}/joda-time-${previous.version}.jar" />
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

  <target name="clirr" depends="getjodaprevious,jar" description="clirr binary compatibility">
  	<echo message="Clirr must be manually downloaded to ${lib}/clirr-core-0.6-uber.jar"></echo>
    <taskdef classpath="${lib}/clirr-core-0.6-uber.jar" resource="clirrtask.properties"/>
    <clirr>
      <origfiles dir="." includes="${jodaprevious.jar}"/>
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
  <version>2.1</version>
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
        <version>2.3.2</version>
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
        <version>1.7</version>
        <executions>
          <execution>
            <phase>compile</phase>
            <configuration>
              <target>
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
              </target>
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
        <version>2.12</version>
        <configuration>
          <includes>
            <include>**/TestAllPackages.java</include>
          </includes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <archive>
            <manifestFile>src/conf/MANIFEST.MF</manifestFile>
          </archive>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.8.1</version>
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
        <version>2.3</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-repository-plugin</artifactId>
        <version>2.3.1</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>2.3</version>
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
          <comparisonVersion>2.0</comparisonVersion>
        </configuration>
      </plugin>
    </plugins>
  </build>
  <dependencies>
    <dependency>
      <groupId>org.joda</groupId>
      <artifactId>joda-convert</artifactId>
      <version>1.2</version>
      <scope>compile</scope>
      <optional>true</optional><!-- mandatory in Scala -->
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
        <version>2.8.1</version>
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
        <version>2.12</version>
        <configuration>
           <showSuccess>true</showSuccess>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
        <version>2.3</version>
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
            <version>1.4</version>
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

## src/main/java/org/joda/time/field/FieldUtils.java

```
/*
 *  Copyright 2001-2005 Stephen Colebourne
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
package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * General utilities that don't fit elsewhere.
 * <p>
 * FieldUtils is thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public class FieldUtils {

    /**
     * Restricted constructor.
     */
    private FieldUtils() {
        super();
    }
    
    //------------------------------------------------------------------------
    /**
     * Negates the input throwing an exception if it can't negate it.
     * 
     * @param value  the value to negate
     * @return the negated value
     * @throws ArithmeticException if the value is Integer.MIN_VALUE
     * @since 1.1
     */
    public static int safeNegate(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("Integer.MIN_VALUE cannot be negated");
        }
        return -value;
    }
    
    /**
     * Add two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static int safeAdd(int val1, int val2) {
        int sum = val1 + val2;
        // If there is a sign change, but the two values have the same sign...
        if ((val1 ^ sum) < 0 && (val1 ^ val2) >= 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " + " + val2);
        }
        return sum;
    }
    
    /**
     * Add two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static long safeAdd(long val1, long val2) {
        long sum = val1 + val2;
        // If there is a sign change, but the two values have the same sign...
        if ((val1 ^ sum) < 0 && (val1 ^ val2) >= 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " + " + val2);
        }
        return sum;
    }
    
    /**
     * Subtracts two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value, to be taken away from
     * @param val2  the second value, the amount to take away
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static long safeSubtract(long val1, long val2) {
        long diff = val1 - val2;
        // If there is a sign change, but the two values have different signs...
        if ((val1 ^ diff) < 0 && (val1 ^ val2) < 0) {
            throw new ArithmeticException
                ("The calculation caused an overflow: " + val1 + " - " + val2);
        }
        return diff;
    }
    
    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     * @since 1.2
     */
    public static int safeMultiply(int val1, int val2) {
        long total = (long) val1 * (long) val2;
        if (total < Integer.MIN_VALUE || total > Integer.MAX_VALUE) {
          throw new ArithmeticException("Multiplication overflows an int: " + val1 + " * " + val2);
        }
        return (int) total;
    }

    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     * @since 1.2
     */
    public static long safeMultiply(long val1, int val2) {
        switch (val2) {
            case -1:
                if (val1 == Long.MIN_VALUE) {
                    throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
                }
                return -val1;
            case 0:
                return 0L;
            case 1:
                return val1;
        }
        long total = val1 * val2;
        if (total / val2 != val1) {
          throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
        }
        return total;
    }

    /**
     * Multiply two values throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static long safeMultiply(long val1, long val2) {
        if (val2 == 1) {
            return val1;
        }
        if (val1 == 1) {
            return val2;
        }
        if (val1 == 0 || val2 == 0) {
            return 0;
        }
        long total = val1 * val2;
        if (total / val2 != val1 || val1 == Long.MIN_VALUE && val2 == -1 || val2 == Long.MIN_VALUE && val1 == -1) {
            throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
        }
        return total;
    }
    
    /**
     * Casts to an int throwing an exception if overflow occurs.
     * 
     * @param value  the value
     * @return the value as an int
     * @throws ArithmeticException if the value is too big or too small
     */
    public static int safeToInt(long value) {
        if (Integer.MIN_VALUE <= value && value <= Integer.MAX_VALUE) {
            return (int) value;
        }
        throw new ArithmeticException("Value cannot fit in an int: " + value);
    }
    
    /**
     * Multiply two values to return an int throwing an exception if overflow occurs.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * @return the new total
     * @throws ArithmeticException if the value is too big or too small
     */
    public static int safeMultiplyToInt(long val1, long val2) {
        long val = FieldUtils.safeMultiply(val1, val2);
        return FieldUtils.safeToInt(val);
    }

    //-----------------------------------------------------------------------
    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalFieldValueException if value is not in the specified bounds
     */
    public static void verifyValueBounds(DateTimeField field, 
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalFieldValueException
                (field.getType(), Integer.valueOf(value),
                 Integer.valueOf(lowerBound), Integer.valueOf(upperBound));
        }
    }

    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalFieldValueException if value is not in the specified bounds
     * @since 1.1
     */
    public static void verifyValueBounds(DateTimeFieldType fieldType, 
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalFieldValueException
                (fieldType, Integer.valueOf(value),
                 Integer.valueOf(lowerBound), Integer.valueOf(upperBound));
        }
    }

    /**
     * Verify that input values are within specified bounds.
     * 
     * @param value  the value to check
     * @param lowerBound  the lower bound allowed for value
     * @param upperBound  the upper bound allowed for value
     * @throws IllegalFieldValueException if value is not in the specified bounds
     */
    public static void verifyValueBounds(String fieldName,
                                         int value, int lowerBound, int upperBound) {
        if ((value < lowerBound) || (value > upperBound)) {
            throw new IllegalFieldValueException
                (fieldName, Integer.valueOf(value),
                 Integer.valueOf(lowerBound), Integer.valueOf(upperBound));
        }
    }

    /**
     * Utility method used by addWrapField implementations to ensure the new
     * value lies within the field's legal value range.
     *
     * @param currentValue the current value of the data, which may lie outside
     * the wrapped value range
     * @param wrapValue  the value to add to current value before
     *  wrapping.  This may be negative.
     * @param minValue the wrap range minimum value.
     * @param maxValue the wrap range maximum value.  This must be
     *  greater than minValue (checked by the method).
     * @return the wrapped value
     * @throws IllegalArgumentException if minValue is greater
     *  than or equal to maxValue
     */
    public static int getWrappedValue(int currentValue, int wrapValue,
                                      int minValue, int maxValue) {
        return getWrappedValue(currentValue + wrapValue, minValue, maxValue);
    }

    /**
     * Utility method that ensures the given value lies within the field's
     * legal value range.
     * 
     * @param value  the value to fit into the wrapped value range
     * @param minValue the wrap range minimum value.
     * @param maxValue the wrap range maximum value.  This must be
     *  greater than minValue (checked by the method).
     * @return the wrapped value
     * @throws IllegalArgumentException if minValue is greater
     *  than or equal to maxValue
     */
    public static int getWrappedValue(int value, int minValue, int maxValue) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("MIN > MAX");
        }

        int wrapRange = maxValue - minValue + 1;
        value -= minValue;

        if (value >= 0) {
            return (value % wrapRange) + minValue;
        }

        int remByRange = (-value) % wrapRange;

        if (remByRange == 0) {
            return 0 + minValue;
        }
        return (wrapRange - remByRange) + minValue;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares two objects as equals handling null.
     * 
     * @param object1  the first object
     * @param object2  the second object
     * @return true if equal
     * @since 1.4
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

}

```
