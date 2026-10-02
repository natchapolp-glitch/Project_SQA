Generate a JUnit 4 Java test suite using only the supplied fixed source and build context.
Project: Time; fixed revision: 1f.
Candidate target classes:
org.joda.time.Partial
org.joda.time.field.UnsupportedDurationField

Return Java source only, with explicit package declarations and JUnit 4 imports. Create at most 30 @Test methods in source order, with deterministic inputs and meaningful assertions derived from the fixed API behavior. Avoid external services, clock dependence, randomness, empty tests, and assertions that merely check non-null. Do not modify production code or create shadow production classes. Preserve all assertions; no feedback or repair loop.

Use the provided candidate classes; target eligibility and fixtures are specified by the shared adapter contract before generation.

## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="joda-time" default="package" basedir=".">

  <!-- ====================================================================== -->
  <!-- Import maven-build.xml into the current project                        -->
  <!-- ====================================================================== -->

  <import file="maven-build.xml"/>
  
  <!-- ====================================================================== -->
  <!-- Help target                                                            -->
  <!-- ====================================================================== -->

  <target name="help">
    <echo message="Please run: $ant -projecthelp"/>
  </target>

  <target name="compile" depends="joda-time-from-maven.compile"> </target>
  <target name="compile.tests" depends="joda-time-from-maven.compile-tests"> </target>

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
  <name>Joda-Time</name>
  <version>2.4-SNAPSHOT</version>
  <description>Date and time library to replace JDK date handling</description>
  <url>http://www.joda.org/joda-time/</url>

  <!-- ==================================================================== -->
  <issueManagement>
    <system>GitHub</system>
    <url>https://github.com/JodaOrg/joda-time/issues</url>
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

  <!-- ==================================================================== -->
  <developers>
    <developer>
      <id>scolebourne</id>
      <name>Stephen Colebourne</name>
      <email></email>
      <roles>
        <role>Project Lead</role>
      </roles>
      <timezone>0</timezone>
      <url>https://github.com/jodastephen</url>
    </developer>
    <developer>
      <id>broneill</id>
      <name>Brian S O'Neill</name>
      <email></email>
      <roles>
        <role>Senior Developer</role>
      </roles>
      <url>https://github.com/broneill</url>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>Guy Allard</name>
    </contributor>
    <contributor>
      <name>Oren Benjamin</name>
      <url>https://github.com/oby1</url>
    </contributor>
    <contributor>
      <name>Fredrik Borgh</name>
    </contributor>
    <contributor>
      <name>Dave Brosius</name>
      <url>https://github.com/mebigfatguy</url>
    </contributor>
    <contributor>
      <name>Luc Claes</name>
      <url>https://github.com/lucclaes</url>
    </contributor>
    <contributor>
      <name>Dan Cojocar</name>
      <url>https://github.com/dancojocar</url>
    </contributor>
    <contributor>
      <name>Christopher Elkins</name>
      <url>https://github.com/celkins</url>
    </contributor>
    <contributor>
      <name>Jeroen van Erp</name>
    </contributor>
    <contributor>
      <name>Gwyn Evans</name>
    </contributor>
    <contributor>
      <name>John Fletcher</name>
    </contributor>
    <contributor>
      <name>Sean Geoghegan</name>
    </contributor>
    <contributor>
      <name>haguenau</name>
      <url>https://github.com/haguenau</url>
    </contributor>
    <contributor>
      <name>Vsevolod Ivanov</name>
      <url>https://github.com/seva-ask</url>
    </contributor>
    <contributor>
      <name>Ashish Katyal</name>
    </contributor>
    <contributor>
      <name>Martin Kneissl</name>
      <url>https://github.com/mkneissl</url>
    </contributor>
    <contributor>
      <name>Vidar Larsen</name>
      <url>https://github.com/vlarsen</url>
    </contributor>
    <contributor>
      <name>Kasper Laudrup</name>
    </contributor>
    <contributor>
      <name>Jeff Lavallee</name>
      <url>https://github.com/jlavallee</url>
    </contributor>
    <contributor>
      <name>Antonio Leitao</name>
    </contributor>
    <contributor>
      <name>Kostas Maistrelis</name>
    </contributor>
    <contributor>
      <name>mjunginger</name>
      <url>https://github.com/mjunginger</url>
    </contributor>
    <contributor>
      <name>Al Major</name>
    </contributor>
    <contributor>
      <name>Pete Marsh</name>
      <url>https://github.com/petedmarsh</url>
    </contributor>
    <contributor>
      <name>Blair Martin</name>
    </contributor>
    <contributor>
      <name>Julen Parra</name>
    </contributor>
    <contributor>
      <name>Michael Plump</name>
    </contributor>
    <contributor>
      <name>Bjorn Pollex</name>
      <url>https://github.com/bjoernpollex</url>
    </contributor>
    <contributor>
      <name>Ryan Propper</name>
    </contributor>
    <contributor>
      <name>Mike Schrag</name>
    </contributor>
    <contributor>
      <name>Hajime Senuma</name>
      <url>https://github.com/hajimes</url>
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
      <name>Bram Van Dam</name>
      <url>https://github.com/codematters</url>
    </contributor>
    <contributor>
      <name>Maxim Zhao</name>
    </contributor>
  </contributors>

  <!-- ==================================================================== -->
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:https://github.com/JodaOrg/joda-time.git</connection>
    <developerConnection>scm:git:git@github.com:JodaOrg/joda-time.git</developerConnection>
    <url>https://github.com/JodaOrg/joda-time</url>
  </scm>
  <organization>
    <name>Joda.org</name>
    <url>http://www.joda.org</url>
  </organization>

  <!-- ==================================================================== -->
  <build>
    <resources>
      <resource>
        <targetPath>META-INF</targetPath>
        <directory>${project.basedir}</directory>
        <includes>
          <include>LICENSE.txt</include>
          <include>NOTICE.txt</include>
        </includes>
      </resource>
      <resource>
        <directory>${project.basedir}/src/main/java</directory>
        <includes>
          <include>**/*.properties</include>
        </includes>
      </resource>
    </resources>
    <!-- define build -->
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>      
        <artifactId>exec-maven-plugin</artifactId>
        <version>1.2.1</version>
        <executions>
          <execution>
            <phase>compile</phase>
            <goals>
              <goal>java</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <mainClass>org.joda.time.tz.ZoneInfoCompiler</mainClass>
          <classpathScope>compile</classpathScope>
          <verbose>true</verbose>
          <systemProperties>
            <systemProperty>
              <key>org.joda.time.DateTimeZone.Provider</key>
              <value>org.joda.time.tz.UTCProvider</value>
            </systemProperty>
          </systemProperties>
          <arguments>
            <argument>-src</argument>
            <argument>${project.build.sourceDirectory}/org/joda/time/tz/src</argument>
            <argument>-dst</argument>
            <argument>${project.build.outputDirectory}/org/joda/time/tz/data</argument>
            <argument>africa</argument>
            <argument>antarctica</argument>
            <argument>asia</argument>
            <argument>australasia</argument>
            <argument>europe</argument>
            <argument>northamerica</argument>
            <argument>southamerica</argument>
            <argument>pacificnew</argument>
            <argument>etcetera</argument>
            <argument>backward</argument>
            <argument>systemv</argument>
          </arguments>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <includes>
            <include>**/TestAllPackages.java</include>
          </includes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <configuration>
          <archive>
            <manifestFile>src/conf/MANIFEST.MF</manifestFile>
            <manifest>
              <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
              <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
            </manifest>
          </archive>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
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
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <attach>false</attach>
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
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-site-plugin</artifactId>
        <configuration>
          <skipDeploy>true</skipDeploy>
        </configuration>
      </plugin>
      <plugin>
        <groupId>com.github.github</groupId>
        <artifactId>site-maven-plugin</artifactId>
        <version>0.8</version>
        <executions>
          <execution>
            <id>github-site</id>
            <goals>
              <goal>site</goal>
            </goals>
            <phase>site-deploy</phase>
          </execution>
        </executions>
        <configuration>
          <message>Create website for ${project.artifactId} v${project.version}</message>
          <path>${project.artifactId}</path>
          <merge>true</merge>
          <server>github</server>
          <repositoryOwner>JodaOrg</repositoryOwner>
          <repositoryName>jodaorg.github.io</repositoryName>
          <branch>refs/heads/master</branch>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.3</version>
        <configuration>
          <comparisonVersion>2.2</comparisonVersion>
          <minSeverity>info</minSeverity>
          <logResults>true</logResults>
        </configuration>
      </plugin>
    </plugins>
    <!-- Manage plugin versions -->
    <pluginManagement>
      <plugins>
        <!-- Maven build and reporting plugins (alphabetical) -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-assembly-plugin</artifactId>
          <version>${maven-assembly-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-checkstyle-plugin</artifactId>
          <version>${maven-checkstyle-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-changes-plugin</artifactId>
          <version>${maven-changes-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-clean-plugin</artifactId>
          <version>${maven-clean-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>${maven-compiler-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>${maven-deploy-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-dependency-plugin</artifactId>
          <version>${maven-dependency-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-gpg-plugin</artifactId>
          <version>${maven-gpg-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-install-plugin</artifactId>
          <version>${maven-install-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-jar-plugin</artifactId>
          <version>${maven-jar-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
          <version>${maven-javadoc-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-jxr-plugin</artifactId>
          <version>${maven-jxr-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-plugin-plugin</artifactId>
          <version>${maven-plugin-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-pmd-plugin</artifactId>
          <version>${maven-pmd-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>${maven-project-info-reports-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-repository-plugin</artifactId>
          <version>${maven-repository-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-resources-plugin</artifactId>
          <version>${maven-resources-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-site-plugin</artifactId>
          <version>${maven-site-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-source-plugin</artifactId>
          <version>${maven-source-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>${maven-surefire-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-report-plugin</artifactId>
          <version>${maven-surefire-report-plugin.version}</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-toolchains-plugin</artifactId>
          <version>${maven-toolchains-plugin.version}</version>
        </plugin>
        <!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself.-->
        <plugin>
        	<groupId>org.eclipse.m2e</groupId>
        	<artifactId>lifecycle-mapping</artifactId>
        	<version>1.0.0</version>
        	<configuration>
        		<lifecycleMappingMetadata>
        			<pluginExecutions>
        				<pluginExecution>
        					<pluginExecutionFilter>
        						<groupId>org.codehaus.mojo</groupId>
        						<artifactId>
        							exec-maven-plugin
        						</artifactId>
        						<versionRange>[1.2.1,)</versionRange>
        						<goals>
        							<goal>java</goal>
        						</goals>
        					</pluginExecutionFilter>
        					<action>
        						<ignore></ignore>
        					</action>
        				</pluginExecution>
        			</pluginExecutions>
        		</lifecycleMappingMetadata>
        	</configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>

  <!-- ==================================================================== -->
  <prerequisites>
    <maven>3.0.4</maven>
  </prerequisites>
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

  <!-- ==================================================================== -->
  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-project-info-reports-plugin</artifactId>
        <version>${maven-project-info-plugin.version}</version>
        <reportSets>
          <reportSet>
            <reports>
              <report>dependencies</report>
              <report>dependency-info</report>
              <report>issue-tracking</report>
              <report>license</report>
              <report>mailing-list</report>
              <report>project-team</report>
              <report>scm</report>
              <report>summary</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>${maven-javadoc-plugin.version}</version>
        <reportSets>
          <reportSet>
            <reports>
              <report>javadoc</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-report-plugin</artifactId>
        <version>${maven-surefire-report-plugin.version}</version>
        <configuration>
           <showSuccess>true</showSuccess>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jxr-plugin</artifactId>
        <version>${maven-jxr-plugin.version}</version>
        <reportSets>
          <reportSet>
            <reports>
              <report>jxr</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
    </plugins>
  </reporting>

  <!-- ==================================================================== -->
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
    <downloadUrl>http://oss.sonatype.org/content/repositories/joda-releases</downloadUrl>
  </distributionManagement>

  <!-- ==================================================================== -->
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

  <!-- ==================================================================== -->
  <properties>
    <!-- Plugin version numbers -->
    <maven-assembly-plugin.version>2.4</maven-assembly-plugin.version>
    <maven-changes-plugin.version>2.9</maven-changes-plugin.version>
    <maven-checkstyle-plugin.version>2.10</maven-checkstyle-plugin.version>
    <maven-clean-plugin.version>2.5</maven-clean-plugin.version>
    <maven-compiler-plugin.version>3.1</maven-compiler-plugin.version>
    <maven-deploy-plugin.version>2.7</maven-deploy-plugin.version>
    <maven-dependency-plugin.version>2.8</maven-dependency-plugin.version>
    <maven-gpg-plugin.version>1.4</maven-gpg-plugin.version>
    <maven-install-plugin.version>2.4</maven-install-plugin.version>
    <maven-jar-plugin.version>2.4</maven-jar-plugin.version>
    <maven-javadoc-plugin.version>2.9.1</maven-javadoc-plugin.version>
    <maven-jxr-plugin.version>2.3</maven-jxr-plugin.version>
    <maven-plugin-plugin.version>3.2</maven-plugin-plugin.version>
    <maven-pmd-plugin.version>3.0.1</maven-pmd-plugin.version>
    <maven-project-info-reports-plugin.version>2.7</maven-project-info-reports-plugin.version>
    <maven-repository-plugin.version>2.3.1</maven-repository-plugin.version>
    <maven-resources-plugin.version>2.6</maven-resources-plugin.version>
    <maven-site-plugin.version>3.3</maven-site-plugin.version>
    <maven-source-plugin.version>2.2.1</maven-source-plugin.version>
    <maven-surefire-plugin.version>2.15</maven-surefire-plugin.version>
    <maven-surefire-report-plugin.version>2.15</maven-surefire-report-plugin.version>
    <maven-toolchains-plugin.version>1.0</maven-toolchains-plugin.version>
    <!-- Properties for maven-compiler-plugin -->
    <maven.compiler.compilerVersion>1.5</maven.compiler.compilerVersion>
    <maven.compiler.source>1.5</maven.compiler.source>
    <maven.compiler.target>1.5</maven.compiler.target>
    <maven.compiler.fork>true</maven.compiler.fork>
    <maven.compiler.verbose>true</maven.compiler.verbose>
    <maven.compiler.optimize>true</maven.compiler.optimize>
    <maven.compiler.debug>true</maven.compiler.debug>
    <maven.compiler.debuglevel>lines,source</maven.compiler.debuglevel>
    <!-- Properties for maven-javadoc-plugin -->
    <author>false</author>
    <notimestamp>true</notimestamp>
    <!-- Properties for maven-checkstyle-plugin -->
    <checkstyle.config.location>${project.basedir}/src/main/checkstyle/checkstyle.xml</checkstyle.config.location>
    <!-- Other properties -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties>
</project>

```

## src/main/java/org/joda/time/Partial.java

```
/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.joda.time.base.AbstractPartial;
import org.joda.time.field.AbstractPartialFieldProperty;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Partial is an immutable partial datetime supporting any set of datetime fields.
 * <p>
 * A Partial instance can be used to hold any combination of fields.
 * The instance does not contain a time zone, so any datetime is local.
 * <p>
 * A Partial can be matched against an instant using {@link #isMatch(ReadableInstant)}.
 * This method compares each field on this partial with those of the instant
 * and determines if the partial matches the instant.
 * Given this definition, an empty Partial instance represents any datetime
 * and always matches.
 * <p>
 * Calculations on Partial are performed using a {@link Chronology}.
 * This chronology is set to be in the UTC time zone for all calculations.
 * <p>
 * Each individual field can be queried in two ways:
 * <ul>
 * <li><code>get(DateTimeFieldType.monthOfYear())</code>
 * <li><code>property(DateTimeFieldType.monthOfYear()).get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>numeric value - <code>monthOfYear().get()</code>
 * <li>text value - <code>monthOfYear().getAsText()</code>
 * <li>short text value - <code>monthOfYear().getAsShortText()</code>
 * <li>maximum/minimum values - <code>monthOfYear().getMaximumValue()</code>
 * <li>add/subtract - <code>monthOfYear().addToCopy()</code>
 * <li>set - <code>monthOfYear().setCopy()</code>
 * </ul>
 * <p>
 * Partial is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.1
 */
public final class Partial
        extends AbstractPartial
        implements ReadablePartial, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 12324121189002L;

    /** The chronology in use. */
    private final Chronology iChronology;
    /** The set of field types. */
    private final DateTimeFieldType[] iTypes;
    /** The values of each field in this partial. */
    private final int[] iValues;
    /** The formatter to use, [0] may miss some fields, [1] doesn't miss any fields. */
    private transient DateTimeFormatter[] iFormatter;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs a Partial with no fields or values, which can be considered
     * to represent any date.
     * <p>
     * This is most useful when constructing partials, for example:
     * <pre>
     * Partial p = new Partial()
     *     .with(DateTimeFieldType.dayOfWeek(), 5)
     *     .with(DateTimeFieldType.hourOfDay(), 12)
     *     .with(DateTimeFieldType.minuteOfHour(), 20);
     * </pre>
     * Note that, although this is a clean way to write code, it is fairly
     * inefficient internally.
     * <p>
     * The constructor uses the default ISO chronology.
     */
    public Partial() {
        this((Chronology) null);
    }

    /**
     * Constructs a Partial with no fields or values, which can be considered
     * to represent any date.
     * <p>
     * This is most useful when constructing partials, for example:
     * <pre>
     * Partial p = new Partial(chrono)
     *     .with(DateTimeFieldType.dayOfWeek(), 5)
     *     .with(DateTimeFieldType.hourOfDay(), 12)
     *     .with(DateTimeFieldType.minuteOfHour(), 20);
     * </pre>
     * Note that, although this is a clean way to write code, it is fairly
     * inefficient internally.
     *
     * @param chrono  the chronology, null means ISO
     */
    public Partial(Chronology chrono) {
        super();
        iChronology = DateTimeUtils.getChronology(chrono).withUTC();
        iTypes = new DateTimeFieldType[0];
        iValues = new int[0];
    }

    /**
     * Constructs a Partial with the specified field and value.
     * <p>
     * The constructor uses the default ISO chronology.
     * 
     * @param type  the single type to create the partial from, not null
     * @param value  the value to store
     * @throws IllegalArgumentException if the type or value is invalid
     */
    public Partial(DateTimeFieldType type, int value) {
        this(type, value, null);
    }

    /**
     * Constructs a Partial with the specified field and value.
     * <p>
     * The constructor uses the specified chronology.
     * 
     * @param type  the single type to create the partial from, not null
     * @param value  the value to store
     * @param chronology  the chronology, null means ISO
     * @throws IllegalArgumentException if the type or value is invalid
     */
    public Partial(DateTimeFieldType type, int value, Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology).withUTC();
        iChronology = chronology;
        if (type == null) {
            throw new IllegalArgumentException("The field type must not be null");
        }
        iTypes = new DateTimeFieldType[] {type};
        iValues = new int[] {value};
        chronology.validate(this, iValues);
    }

    /**
     * Constructs a Partial with the specified fields and values.
     * The fields must be specified in the order largest to smallest.
     * <p>
     * The constructor uses the specified chronology.
     * 
     * @param types  the types to create the partial from, not null
     * @param values  the values to store, not null
     * @throws IllegalArgumentException if the types or values are invalid
     */
    public Partial(DateTimeFieldType[] types, int[] values) {
        this(types, values, null);
    }

    /**
     * Constructs a Partial with the specified fields and values.
     * The fields must be specified in the order largest to smallest.
     * <p>
     * The constructor uses the specified chronology.
     * 
     * @param types  the types to create the partial from, not null
     * @param values  the values to store, not null
     * @param chronology  the chronology, null means ISO
     * @throws IllegalArgumentException if the types or values are invalid
     */
    public Partial(DateTimeFieldType[] types, int[] values, Chronology chronology) {
        super();
        chronology = DateTimeUtils.getChronology(chronology).withUTC();
        iChronology = chronology;
        if (types == null) {
            throw new IllegalArgumentException("Types array must not be null");
        }
        if (values == null) {
            throw new IllegalArgumentException("Values array must not be null");
        }
        if (values.length != types.length) {
            throw new IllegalArgumentException("Values array must be the same length as the types array");
        }
        if (types.length == 0) {
            iTypes = types;
            iValues = values;
            return;
        }
        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) {
                throw new IllegalArgumentException("Types array must not contain null: index " + i);
            }
        }
        DurationField lastUnitField = null;
        for (int i = 0; i < types.length; i++) {
            DateTimeFieldType loopType = types[i];
            DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
            if (i > 0) {
                if (loopUnitField.isSupported() == false) {
                    if (lastUnitField.isSupported()) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " +
                                        types[i - 1].getName() + " < " + loopType.getName());
                    } else {
                        throw new IllegalArgumentException("Types array must not contain duplicate unsupported: " +
                                        types[i - 1].getName() + " and " + loopType.getName());
                    }
                }
                int compare = lastUnitField.compareTo(loopUnitField);
                if (compare < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " +
                            types[i - 1].getName() + " < " + loopType.getName());
                } else if (compare == 0 && lastUnitField.equals(loopUnitField)) {
                    if (types[i - 1].getRangeDurationType() == null) {
                        if (loopType.getRangeDurationType() == null) {
                            throw new IllegalArgumentException("Types array must not contain duplicate: " +
                                            types[i - 1].getName() + " and " + loopType.getName());
                        }
                    } else {
                        if (loopType.getRangeDurationType() == null) {
                            throw new IllegalArgumentException("Types array must be in order largest-smallest: " +
                                    types[i - 1].getName() + " < " + loopType.getName());
                        }
                        DurationField lastRangeField = types[i - 1].getRangeDurationType().getField(iChronology);
                        DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                        if (lastRangeField.compareTo(loopRangeField) < 0) {
                            throw new IllegalArgumentException("Types array must be in order largest-smallest: " +
                                    types[i - 1].getName() + " < " + loopType.getName());
                        }
                        if (lastRangeField.compareTo(loopRangeField) == 0) {
                            throw new IllegalArgumentException("Types array must not contain duplicate: " +
                                            types[i - 1].getName() + " and " + loopType.getName());
                        }
                    }
                }
            }
            lastUnitField = loopUnitField;
        }
        
        iTypes = (DateTimeFieldType[]) types.clone();
        chronology.validate(this, values);
        iValues = (int[]) values.clone();
    }

    /**
     * Constructs a Partial by copying all the fields and types from
     * another partial.
     * <p>
     * This is most useful when copying from a YearMonthDay or TimeOfDay.
     */
    public Partial(ReadablePartial partial) {
        super();
        if (partial == null) {
            throw new IllegalArgumentException("The partial must not be null");
        }
        iChronology = DateTimeUtils.getChronology(partial.getChronology()).withUTC();
        iTypes = new DateTimeFieldType[partial.size()];
        iValues = new int[partial.size()];
        for (int i = 0; i < partial.size(); i++) {
            iTypes[i] = partial.getFieldType(i);
            iValues[i] = partial.getValue(i);
        }
    }

    /**
     * Constructs a Partial with the specified values.
     * This constructor assigns and performs no validation.
     * 
     * @param partial  the partial to copy
     * @param values  the values to store
     * @throws IllegalArgumentException if the types or values are invalid
     */
    Partial(Partial partial, int[] values) {
        super();
        iChronology = partial.iChronology;
        iTypes = partial.iTypes;
        iValues = values;
    }

    /**
     * Constructs a Partial with the specified chronology, fields and values.
     * This constructor assigns and performs no validation.
     * 
     * @param chronology  the chronology
     * @param types  the types to create the partial from
     * @param values  the values to store
     * @throws IllegalArgumentException if the types or values are invalid
     */
    Partial(Chronology chronology, DateTimeFieldType[] types, int[] values) {
        super();
        iChronology = chronology;
        iTypes = types;
        iValues = values;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the number of fields in this partial.
     * 
     * @return the field count
     */
    public int size() {
        return iTypes.length;
    }

    /**
     * Gets the chronology of the partial which is never null.
     * <p>
     * The {@link Chronology} is the calculation engine behind the partial and
     * provides conversion and validation of the fields in a particular calendar system.
     * 
     * @return the chronology, never null
     */
    public Chronology getChronology() {
        return iChronology;
    }

    /**
     * Gets the field for a specific index in the chronology specified.
     * 
     * @param index  the index to retrieve
     * @param chrono  the chronology to use
     * @return the field
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    protected DateTimeField getField(int index, Chronology chrono) {
        return iTypes[index].getField(chrono);
    }

    /**
     * Gets the field type at the specified index.
     *
     * @param index  the index to retrieve
     * @return the field at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public DateTimeFieldType getFieldType(int index) {
        return iTypes[index];
    }

    /**
     * Gets an array of the field type of each of the fields that
     * this partial supports.
     * <p>
     * The fields are returned largest to smallest.
     *
     * @return the array of field types (cloned), largest to smallest
     */
    public DateTimeFieldType[] getFieldTypes() {
        return (DateTimeFieldType[]) iTypes.clone();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value of the field at the specifed index.
     * 
     * @param index  the index
     * @return the value
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public int getValue(int index) {
        return iValues[index];
    }

    /**
     * Gets an array of the value of each of the fields that
     * this partial supports.
     * <p>
     * The fields are returned largest to smallest.
     * Each value corresponds to the same array index as <code>getFieldTypes()</code>
     *
     * @return the current values of each field (cloned), largest to smallest
     */
    public int[] getValues() {
        return (int[]) iValues.clone();
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new Partial instance with the specified chronology.
     * This instance is immutable and unaffected by this method call.
     * <p>
     * This method retains the values of the fields, thus the result will
     * typically refer to a different instant.
     * <p>
     * The time zone of the specified chronology is ignored, as Partial
     * operates without a time zone.
     *
     * @param newChronology  the new chronology, null means ISO
     * @return a copy of this datetime with a different chronology
     * @throws IllegalArgumentException if the values are invalid for the new chronology
     */
    public Partial withChronologyRetainFields(Chronology newChronology) {
        newChronology = DateTimeUtils.getChronology(newChronology);
        newChronology = newChronology.withUTC();
        if (newChronology == getChronology()) {
            return this;
        } else {
            Partial newPartial = new Partial(newChronology, iTypes, iValues);
            newChronology.validate(newPartial, iValues);
            return newPartial;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this date with the specified field set to a new value.
     * <p>
     * If this partial did not previously support the field, the new one will.
     * Contrast this behaviour with {@link #withField(DateTimeFieldType, int)}.
     * <p>
     * For example, if the field type is <code>dayOfMonth</code> then the day
     * would be changed/added in the returned instance.
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this instance with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public Partial with(DateTimeFieldType fieldType, int value) {
        if (fieldType == null) {
            throw new IllegalArgumentException("The field type must not be null");
        }
        int index = indexOf(fieldType);
        if (index == -1) {
            DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
            int[] newValues = new int[newTypes.length];
            
            // find correct insertion point to keep largest-smallest order
            int i = 0;
            DurationField unitField = fieldType.getDurationType().getField(iChronology);
            if (unitField.isSupported()) {
                for (; i < iTypes.length; i++) {
                    DateTimeFieldType loopType = iTypes[i];
                    DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
                    if (loopUnitField.isSupported()) {
                        int compare = unitField.compareTo(loopUnitField);
                        if (compare > 0) {
                            break;
                        } else if (compare == 0) {
                            if (fieldType.getRangeDurationType() == null) {
                                break;
                            }
                            DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);
                            DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                            if (rangeField.compareTo(loopRangeField) > 0) {
                                break;
                            }
                        }
                    }
                }
            }
            System.arraycopy(iTypes, 0, newTypes, 0, i);
            System.arraycopy(iValues, 0, newValues, 0, i);
            newTypes[i] = fieldType;
            newValues[i] = value;
            System.arraycopy(iTypes, i, newTypes, i + 1, newTypes.length - i - 1);
            System.arraycopy(iValues, i, newValues, i + 1, newValues.length - i - 1);
            // use public constructor to ensure full validation
            // this isn't overly efficient, but is safe
            Partial newPartial = new Partial(newTypes, newValues, iChronology);
            iChronology.validate(newPartial, newValues);
            return newPartial;
        }
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).set(this, index, newValues, value);
        return new Partial(this, newValues);
    }

    /**
     * Gets a copy of this date with the specified field removed.
     * <p>
     * If this partial did not previously support the field, no error occurs.
     *
     * @param fieldType  the field type to remove, may be null
     * @return a copy of this instance with the field removed
     */
    public Partial without(DateTimeFieldType fieldType) {
        int index = indexOf(fieldType);
        if (index != -1) {
            DateTimeFieldType[] newTypes = new DateTimeFieldType[size() - 1];
            int[] newValues = new int[size() - 1];
            System.arraycopy(iTypes, 0, newTypes, 0, index);
            System.arraycopy(iTypes, index + 1, newTypes, index, newTypes.length - index);
            System.arraycopy(iValues, 0, newValues, 0, index);
            System.arraycopy(iValues, index + 1, newValues, index, newValues.length - index);
            Partial newPartial = new Partial(iChronology, newTypes, newValues);
            iChronology.validate(newPartial, newValues);
            return newPartial;
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this Partial with the specified field set to a new value.
     * <p>
     * If this partial does not support the field, an exception is thrown.
     * Contrast this behaviour with {@link #with(DateTimeFieldType, int)}.
     * <p>
     * For example, if the field type is <code>dayOfMonth</code> then the day
     * would be changed in the returned instance if supported.
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this instance with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public Partial withField(DateTimeFieldType fieldType, int value) {
        int index = indexOfSupported(fieldType);
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).set(this, index, newValues, value);
        return new Partial(this, newValues);
    }

    /**
     * Gets a copy of this Partial with the value of the specified field increased.
     * If this partial does not support the field, an exception is thrown.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * The addition will overflow into larger fields (eg. minute to hour).
     * However, it will not wrap around if the top maximum is reached.
     *
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this instance with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new datetime exceeds the capacity
     */
    public Partial withFieldAdded(DurationFieldType fieldType, int amount) {
        int index = indexOfSupported(fieldType);
        if (amount == 0) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).add(this, index, newValues, amount);
        return new Partial(this, newValues);
    }

    /**
     * Gets a copy of this Partial with the value of the specified field increased.
     * If this partial does not support the field, an exception is thrown.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * The addition will overflow into larger fields (eg. minute to hour).
     * If the maximum is reached, the addition will wra.
     *
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this instance with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new datetime exceeds the capacity
     */
    public Partial withFieldAddWrapped(DurationFieldType fieldType, int amount) {
        int index = indexOfSupported(fieldType);
        if (amount == 0) {
            return this;
        }
        int[] newValues = getValues();
        newValues = getField(index).addWrapPartial(this, index, newValues, amount);
        return new Partial(this, newValues);
    }

    /**
     * Gets a copy of this Partial with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * Fields in the period that aren't present in the partial are ignored.
     * <p>
     * This method is typically used to add multiple copies of complex
     * period instances. Adding one field is best achieved using the method
     * {@link #withFieldAdded(DurationFieldType, int)}.
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this instance with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity
     */
    public Partial withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        int[] newValues = getValues();
        for (int i = 0; i < period.size(); i++) {
            DurationFieldType fieldType = period.getFieldType(i);
            int index = indexOf(fieldType);
            if (index >= 0) {
                newValues = getField(index).add(this, index, newValues,
                        FieldUtils.safeMultiply(period.getValue(i), scalar));
            }
        }
        return new Partial(this, newValues);
    }

    /**
     * Gets a copy of this instance with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     *
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this instance with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public Partial plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    /**
     * Gets a copy of this instance with the specified period take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     *
     * @param period  the period to reduce this instant by
     * @return a copy of this instance with the period taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public Partial minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains
     * many useful methods for getting and manipulating the partial.
     * <p>
     * See also {@link ReadablePartial#get(DateTimeFieldType)}.
     *
     * @param type  the field type to get the property for, not null
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public Property property(DateTimeFieldType type) {
        return new Property(this, indexOfSupported(type));
    }

    //-----------------------------------------------------------------------
    /**
     * Does this partial match the specified instant.
     * <p>
     * A match occurs when all the fields of this partial are the same as the
     * corresponding fields on the specified instant.
     *
     * @param instant  an instant to check against, null means now in default zone
     * @return true if this partial matches the specified instant
     */
    public boolean isMatch(ReadableInstant instant) {
        long millis = DateTimeUtils.getInstantMillis(instant);
        Chronology chrono = DateTimeUtils.getInstantChronology(instant);
        for (int i = 0; i < iTypes.length; i++) {
            int value = iTypes[i].getField(chrono).get(millis);
            if (value != iValues[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Does this partial match the specified partial.
     * <p>
     * A match occurs when all the fields of this partial are the same as the
     * corresponding fields on the specified partial.
     *
     * @param partial  a partial to check against, must not be null
     * @return true if this partial matches the specified partial
     * @throws IllegalArgumentException if the partial is null
     * @throws IllegalArgumentException if the fields of the two partials do not match
     * @since 1.5
     */
    public boolean isMatch(ReadablePartial partial) {
        if (partial == null) {
            throw new IllegalArgumentException("The partial must not be null");
        }
        for (int i = 0; i < iTypes.length; i++) {
            int value = partial.get(iTypes[i]);
            if (value != iValues[i]) {
                return false;
            }
        }
        return true;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a formatter suitable for the fields in this partial.
     * <p>
     * If there is no appropriate ISO format, null is returned.
     * This method may return a formatter that does not display all the
     * fields of the partial. This might occur when you have overlapping
     * fields, such as dayOfWeek and dayOfMonth.
     *
     * @return a formatter suitable for the fields in this partial, null
     *  if none is suitable
     */
    public DateTimeFormatter getFormatter() {
        DateTimeFormatter[] f = iFormatter;
        if (f == null) {
            if (size() == 0) {
                return null;
            }
            f = new DateTimeFormatter[2];
            try {
                List<DateTimeFieldType> list = new ArrayList<DateTimeFieldType>(Arrays.asList(iTypes));
                f[0] = ISODateTimeFormat.forFields(list, true, false);
                if (list.size() == 0) {
                    f[1] = f[0];
                }
            } catch (IllegalArgumentException ex) {
                // ignore
            }
            iFormatter = f;
        }
        return f[0];
    }

    //-----------------------------------------------------------------------
    /**
     * Output the date in an appropriate ISO8601 format.
     * <p>
     * This method will output the partial in one of two ways.
     * If {@link #getFormatter()}
     * <p>
     * If there is no appropriate ISO format a dump of the fields is output
     * via {@link #toStringList()}.
     * 
     * @return ISO8601 formatted string
     */
    public String toString() {
        DateTimeFormatter[] f = iFormatter;
        if (f == null) {
            getFormatter();
            f = iFormatter;
            if (f == null) {
                return toStringList();
            }
        }
        DateTimeFormatter f1 = f[1];
        if (f1 == null) {
            return toStringList();
        }
        return f1.print(this);
    }

    /**
     * Gets a string version of the partial that lists all the fields.
     * <p>
     * This method exists to provide a better debugging toString than
     * the standard toString. This method lists all the fields and their
     * values in a style similar to the collections framework.
     *
     * @return a toString format that lists all the fields
     */
    public String toStringList() {
        int size = size();
        StringBuilder buf = new StringBuilder(20 * size);
        buf.append('[');
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                buf.append(',').append(' ');
            }
            buf.append(iTypes[i].getName());
            buf.append('=');
            buf.append(iValues[i]);
        }
        buf.append(']');
        return buf.toString();
    }

    /**
     * Output the date using the specified format pattern.
     * Unsupported fields will appear as special unicode characters.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @see org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).print(this);
    }

    /**
     * Output the date using the specified format pattern.
     * Unsupported fields will appear as special unicode characters.
     *
     * @param pattern  the pattern specification, null means use <code>toString</code>
     * @param locale  Locale to use, null means default
     * @see org.joda.time.format.DateTimeFormat
     */
    public String toString(String pattern, Locale locale) {
        if (pattern == null) {
            return toString();
        }
        return DateTimeFormat.forPattern(pattern).withLocale(locale).print(this);
    }

    //-----------------------------------------------------------------------
    /**
     * The property class for <code>Partial</code>.
     * <p>
     * This class binds a <code>Partial</code> to a <code>DateTimeField</code>.
     * 
     * @author Stephen Colebourne
     * @since 1.1
     */
    public static class Property extends AbstractPartialFieldProperty implements Serializable {

        /** Serialization version */
        private static final long serialVersionUID = 53278362873888L;

        /** The partial */
        private final Partial iPartial;
        /** The field index */
        private final int iFieldIndex;

        /**
         * Constructs a property.
         * 
         * @param partial  the partial instance
         * @param fieldIndex  the index in the partial
         */
        Property(Partial partial, int fieldIndex) {
            super();
            iPartial = partial;
            iFieldIndex = fieldIndex;
        }

        /**
         * Gets the field that this property uses.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iPartial.getField(iFieldIndex);
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        protected ReadablePartial getReadablePartial() {
            return iPartial;
        }

        /**
         * Gets the partial that this property belongs to.
         * 
         * @return the partial
         */
        public Partial getPartial() {
            return iPartial;
        }

        /**
         * Gets the value of this field.
         * 
         * @return the field value
         */
        public int get() {
            return iPartial.getValue(iFieldIndex);
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to the value of this field in a copy of this Partial.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it will affect larger fields.
         * Smaller fields are unaffected.
         * <p>
         * If the result would be too large, beyond the maximum year, then an
         * IllegalArgumentException is thrown.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the Partial with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public Partial addToCopy(int valueToAdd) {
            int[] newValues = iPartial.getValues();
            newValues = getField().add(iPartial, iFieldIndex, newValues, valueToAdd);
            return new Partial(iPartial, newValues);
        }

        /**
         * Adds to the value of this field in a copy of this Partial wrapping
         * within this field if the maximum value is reached.
         * <p>
         * The value will be added to this field. If the value is too large to be
         * added solely to this field then it wraps within this field.
         * Other fields are unaffected.
         * <p>
         * For example,
         * <code>2004-12-20</code> addWrapField one month returns <code>2004-01-20</code>.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param valueToAdd  the value to add to the field in the copy
         * @return a copy of the Partial with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public Partial addWrapFieldToCopy(int valueToAdd) {
            int[] newValues = iPartial.getValues();
            newValues = getField().addWrapField(iPartial, iFieldIndex, newValues, valueToAdd);
            return new Partial(iPartial, newValues);
        }

        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the Partial.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the Partial with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public Partial setCopy(int value) {
            int[] newValues = iPartial.getValues();
            newValues = getField().set(iPartial, iFieldIndex, newValues, value);
            return new Partial(iPartial, newValues);
        }

        /**
         * Sets this field in a copy of the Partial to a parsed text value.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the Partial with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public Partial setCopy(String text, Locale locale) {
            int[] newValues = iPartial.getValues();
            newValues = getField().set(iPartial, iFieldIndex, newValues, text, locale);
            return new Partial(iPartial, newValues);
        }

        /**
         * Sets this field in a copy of the Partial to a parsed text value.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         * Instead, a new instance is returned.
         * 
         * @param text  the text value to set
         * @return a copy of the Partial with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public Partial setCopy(String text) {
            return setCopy(text, null);
        }

        //-----------------------------------------------------------------------
        /**
         * Returns a new Partial with this field set to the maximum value
         * for this field.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         *
         * @return a copy of the Partial with this field set to its maximum
         * @since 1.2
         */
        public Partial withMaximumValue() {
            return setCopy(getMaximumValue());
        }

        /**
         * Returns a new Partial with this field set to the minimum value
         * for this field.
         * <p>
         * The Partial attached to this property is unchanged by this call.
         *
         * @return a copy of the Partial with this field set to its minimum
         * @since 1.2
         */
        public Partial withMinimumValue() {
            return setCopy(getMinimumValue());
        }
    }

}

```

## src/main/java/org/joda/time/field/UnsupportedDurationField.java

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
package org.joda.time.field;

import java.io.Serializable;
import java.util.HashMap;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * A placeholder implementation to use when a duration field is not supported.
 * <p>
 * UnsupportedDurationField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class UnsupportedDurationField extends DurationField implements Serializable {

    /** Serialization lock. */
    private static final long serialVersionUID = -6390301302770925357L;

    /** The cache of unsupported duration field instances */
    private static HashMap<DurationFieldType, UnsupportedDurationField> cCache;

    /**
     * Gets an instance of UnsupportedDurationField for a specific named field.
     * The returned instance is cached.
     * 
     * @param type  the type to obtain
     * @return the instance
     */
    public static synchronized UnsupportedDurationField getInstance(DurationFieldType type) {
        UnsupportedDurationField field;
        if (cCache == null) {
            cCache = new HashMap<DurationFieldType, UnsupportedDurationField>(7);
            field = null;
        } else {
            field = cCache.get(type);
        }
        if (field == null) {
            field = new UnsupportedDurationField(type);
            cCache.put(type, field);
        }
        return field;
    }

    /** The name of the field */
    private final DurationFieldType iType;

    /**
     * Constructor.
     * 
     * @param type  the type to use
     */
    private UnsupportedDurationField(DurationFieldType type) {
        iType = type;
    }

    //-----------------------------------------------------------------------
    // Design note: Simple Accessors return a suitable value, but methods
    // intended to perform calculations throw an UnsupportedOperationException.

    public final DurationFieldType getType() {
        return iType;
    }

    public String getName() {
        return iType.getName();
    }

    /**
     * This field is not supported.
     *
     * @return false always
     */
    public boolean isSupported() {
        return false;
    }

    /**
     * This field is precise.
     * 
     * @return true always
     */
    public boolean isPrecise() {
        return true;
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getValue(long duration) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getValueAsLong(long duration) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getValue(long duration, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getValueAsLong(long duration, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(long value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(int value, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getMillis(long value, long instant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long add(long instant, int value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long add(long instant, long value) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        throw unsupported();
    }

    /**
     * Always throws UnsupportedOperationException
     *
     * @throws UnsupportedOperationException
     */
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        throw unsupported();
    }

    /**
     * Always returns zero.
     *
     * @return zero always
     */
    public long getUnitMillis() {
        return 0;
    }

    /**
     * Always returns zero, indicating that sort order is not relevent.
     *
     * @return zero always
     */
    public int compareTo(DurationField durationField) {
        return 0;
    }

    //------------------------------------------------------------------------
    /**
     * Compares this duration field to another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof UnsupportedDurationField) {
            UnsupportedDurationField other = (UnsupportedDurationField) obj;
            if (other.getName() == null) {
                return (getName() == null);
            }
            return (other.getName().equals(getName()));
        }
        return false;
    }

    /**
     * Gets a suitable hashcode.
     * 
     * @return the hashcode
     */
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return "UnsupportedDurationField[" + getName() + ']';
    }

    /**
     * Ensure proper singleton serialization
     */
    private Object readResolve() {
        return getInstance(iType);
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(iType + " field is unsupported");
    }

}

```
