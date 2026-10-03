## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="commons-csv" default="package" basedir=".">

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

  <target name="compile" depends="commons-csv-from-maven.compile"> </target>
  <target name="compile.tests" depends="commons-csv-from-maven.compile-tests"> </target>

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
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>43</version>
  </parent>
  <artifactId>commons-csv</artifactId>
  <version>1.6-SNAPSHOT</version>
  <name>Apache Commons CSV</name>
  <url>http://commons.apache.org/proper/commons-csv/</url>
  <description>
The Apache Commons CSV library provides a simple interface for reading and writing
CSV files of various types.
  </description>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-all</artifactId>
      <version>1.10.19</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.6</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.7</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>1.4.196</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <developers>
    <developer>
      <id>bayard</id>
      <name>Henri Yandell</name>
      <email>bayard@apache.org</email>
      <organization>The Apache Software Foundation</organization>
    </developer>
    <developer>
      <name>Martin van den Bemt</name>
      <id>mvdb</id>
      <email>mvdb@apache.org</email>
      <organization>The Apache Software Foundation</organization>
    </developer>
    <developer>
      <name>Yonik Seeley</name>
      <id>yonik</id>
      <email>yonik@apache.org</email>
      <organization>The Apache Software Foundation</organization>
    </developer>
    <developer>
      <name>Emmanuel Bourg</name>
      <id>ebourg</id>
      <email>ebourg@apache.org</email>
      <organization>Apache</organization>
    </developer>
    <developer>
      <name>Gary Gregory</name>
      <id>ggregory</id>
      <email>ggregory@apache.org</email>
      <url>http://www.garygregory.com</url>
    </developer>
    <developer>
      <name>Benedikt Ritter</name>
      <id>britter</id>
      <email>britter@apache.org</email>
      <organization>The Apache Software Foundation</organization>
    </developer>
    <developer>
      <name>Rob Tompkins</name>
      <id>chtompki</id>
      <email>chtompki@apache.org</email>
      <organization>The Apache Software Foundation</organization>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>Bob Smith</name>
    </contributor>
  </contributors>

  <scm>
    <connection>scm:git:http://git-wip-us.apache.org/repos/asf/commons-csv.git</connection>
    <developerConnection>scm:git:https://git-wip-us.apache.org/repos/asf/commons-csv.git</developerConnection>
    <url>https://git-wip-us.apache.org/repos/asf?p=commons-csv.git</url>
  </scm>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/CSV</url>
  </issueManagement>

  <distributionManagement>
    <site>
      <id>apache.website</id>
      <name>Apache Commons Site</name>
      <url>scm:svn:https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-csv/</url>
    </site>
  </distributionManagement>

  <properties>
    <commons.release.version>1.6</commons.release.version>
    <commons.release.desc>(Java 7+)</commons.release.desc>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC1</commons.rc.version>
    <commons.componentid>csv</commons.componentid>
    <commons.module.name>org.apache.commons.csv</commons.module.name>
    <commons.jira.id>CSV</commons.jira.id>
    <commons.jira.pid>12313222</commons.jira.pid>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
    <!-- Ensure copies work OK (can be removed later when this is in parent POM) -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <commons.encoding>UTF-8</commons.encoding>

    <checkstyle.version>2.17</checkstyle.version>
    <checkstyle.header.file>${basedir}/LICENSE-header.txt</checkstyle.header.file>
    <checkstyle.resourceExcludes>LICENSE.txt, NOTICE.txt</checkstyle.resourceExcludes>
  </properties>

  <build>
    <defaultGoal>clean verify apache-rat:check clirr:check javadoc:javadoc</defaultGoal>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-scm-publish-plugin</artifactId>
          <version>1.1</version>
          <configuration>
            <ignorePathsToDelete>
              <ignorePathToDelete>archives/**</ignorePathToDelete>
            </ignorePathsToDelete>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <testExcludes>
            <testExclude>**/*Benchmark*</testExclude>
          </testExcludes>
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
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <excludes>
            <exclude>**/perf/PerformanceTest.java</exclude>
          </excludes>
        </configuration>
      </plugin>
      <!-- Allow checkstyle to be run interactively; keep in sync with report config below -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${checkstyle.version}</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>

      <!-- We need to add our test data files to rat exclusions -->
      <!-- Needed for command-line access, e.g mvn apache-rat:rat and mvn apache-rat:check -->
      <plugin>
        <groupId>org.apache.rat</groupId>
        <artifactId>apache-rat-plugin</artifactId>
        <version>${commons.rat.version}</version>
        <!-- Should agree with config in reporting section -->
        <configuration>
          <excludes>
            <!-- These files are used as test data and test result specifications. -->
            <exclude>src/test/resources/csv-167/sample1.csv</exclude>
            <exclude>src/test/resources/CSV-198/optd_por_public.csv</exclude>
            <exclude>src/test/resources/CSV-213/999751170.patch.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/bom.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/test.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/test_default.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/test_default_comment.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/test_rfc4180.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/test_rfc4180_trim.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/testCSV85.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/testCSV85_default.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/testCSV85_ignoreEmpty.txt</exclude>
            <!-- The ferc.gov files are included discussion in https://issues.apache.org/jira/browse/LEGAL-175. -->
            <exclude>src/test/resources/ferc.gov/contract.txt</exclude>
            <exclude>src/test/resources/ferc.gov/transaction.txt</exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <!-- Keep in sync with build config above -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${checkstyle.version}</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
        <!-- We need to specify reportSets because 2.9.1 creates two reports -->
        <reportSets>
          <reportSet>
            <reports>
              <report>checkstyle</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.8</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <tagListOptions>
            <tagClasses>
              <tagClass>
                <displayName>Needs Work</displayName>
                <tags>
                  <tag>
                    <matchString>TODO</matchString>
                    <matchType>exact</matchType>
                  </tag>
                  <tag>
                    <matchString>FIXME</matchString>
                    <matchType>exact</matchType>
                  </tag>
                  <tag>
                    <matchString>XXX</matchString>
                    <matchType>exact</matchType>
                  </tag>
                </tags>
              </tagClass>
              <tagClass>
                <displayName>Noteable Markers</displayName>
                <tags>
                  <tag>
                    <matchString>NOTE</matchString>
                    <matchType>exact</matchType>
                  </tag>
                  <tag>
                    <matchString>NOPMD</matchString>
                    <matchType>exact</matchType>
                  </tag>
                  <tag>
                    <matchString>NOSONAR</matchString>
                    <matchType>exact</matchType>
                  </tag>
                </tags>
              </tagClass>
            </tagClasses>
          </tagListOptions>
        </configuration>
      </plugin>
      <!--
          NOTE: this requires Maven 3.0.x; when used with Maven 2.2.1, the following error is seen:
          Embedded error: Error rendering Maven report: org.dom4j.DocumentFactory cannot be cast to org.dom4j.DocumentFactory
       -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>javancss-maven-plugin</artifactId>
        <version>2.1</version>
      </plugin>

      <!-- We need to add our test data files to rat exclusions -->
      <plugin>
        <groupId>org.apache.rat</groupId>
        <artifactId>apache-rat-plugin</artifactId>
        <version>${commons.rat.version}</version>
        <!-- Should agree with config in build section -->
        <configuration>
          <excludes>
            <exclude>src/test/resources/csv-167/sample1.csv</exclude>
            <exclude>src/test/resources/CSV-198/optd_por_public.csv</exclude>
            <exclude>src/test/resources/CSV-213/999751170.patch.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/bom.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/test.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/test_default.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/test_default_comment.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/test_rfc4180.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/test_rfc4180_trim.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/testCSV85.csv</exclude>
            <exclude>src/test/resources/CSVFileParser/testCSV85_default.txt</exclude>
            <exclude>src/test/resources/CSVFileParser/testCSV85_ignoreEmpty.txt</exclude>
            <exclude>src/test/resources/ferc.gov/contract.txt</exclude>
            <exclude>src/test/resources/ferc.gov/transaction.txt</exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

  <profiles>
    <profile>
      <id>setup-checkout</id>
      <activation>
        <file>
          <missing>site-content</missing>
        </file>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>1.8</version>
            <executions>
              <execution>
                <id>prepare-checkout</id>
                <phase>pre-site</phase>
                <goals>
                  <goal>run</goal>
                </goals>
                <configuration>
                  <tasks>
                    <exec executable="svn">
                      <arg line="checkout --depth immediates ${commons.scmPubUrl} ${commons.scmPubCheckoutDirectory}" />
                    </exec>

                    <exec executable="svn">
                      <arg line="update --set-depth exclude ${commons.scmPubCheckoutDirectory}/javadocs" />
                    </exec>

                    <pathconvert pathsep=" " property="dirs">
                      <dirset dir="${commons.scmPubCheckoutDirectory}" includes="*" />
                    </pathconvert>
                    <exec executable="svn">
                      <arg line="update --set-depth infinity ${dirs}" />
                    </exec>
                  </tasks>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>


    <!-- Profile to build and run the benchmarks. Use 'mvn test -Pbenchmark', and add '-Dbenchmark=foo' to run only the foo benchmark -->
    <profile>
      <id>benchmark</id>

      <dependencies>
        <dependency>
          <groupId>org.openjdk.jmh</groupId>
          <artifactId>jmh-core</artifactId>
          <version>1.5.2</version>
          <scope>test</scope>
        </dependency>

        <dependency>
          <groupId>org.openjdk.jmh</groupId>
          <artifactId>jmh-generator-annprocess</artifactId>
          <version>1.5.2</version>
          <scope>test</scope>
        </dependency>

        <dependency>
          <groupId>genjava</groupId>
          <artifactId>gj-csv</artifactId>
          <version>1.0</version>
          <scope>test</scope>
        </dependency>

        <dependency>
          <groupId>net.sourceforge.javacsv</groupId>
          <artifactId>javacsv</artifactId>
          <version>2.0</version>
          <scope>test</scope>
        </dependency>

        <dependency>
          <groupId>com.opencsv</groupId>
          <artifactId>opencsv</artifactId>
          <version>3.1</version>
          <scope>test</scope>
        </dependency>

        <dependency>
          <groupId>net.sf.supercsv</groupId>
          <artifactId>super-csv</artifactId>
          <version>2.2.1</version>
        </dependency>

        <!-- Not in Maven Central, download manually from http://kasparov.skife.org/csv/csv-1.0.jar and install with:
             mvn install:install-file -Dfile=E:/Java/skife.org/csv-1.0.jar -DgroupId=org.skife.kasparov -DartifactId=csv -Dversion=1.0 -Dpackaging=jar -->
        <dependency>
          <groupId>org.skife.kasparov</groupId>
          <artifactId>csv</artifactId>
          <version>1.0</version>
        </dependency>

        <dependency>
          <groupId>org.apache.commons</groupId>
          <artifactId>commons-lang3</artifactId>
          <version>3.6</version>
        </dependency>
      </dependencies>

      <properties>
        <skipTests>true</skipTests>
        <benchmark>org.apache</benchmark>
      </properties>

      <build>
        <plugins>
          <!-- Enable the compilation of the benchmarks -->
          <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration combine.self="override">
              <testIncludes>
                <testInclude>**/*</testInclude>
              </testIncludes>
            </configuration>
          </plugin>

          <!-- Hook the benchmarks to the test phase -->
          <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <executions>
              <execution>
                <id>benchmark</id>
                <phase>test</phase>
                <goals>
                  <goal>exec</goal>
                </goals>
                <configuration>
                  <classpathScope>test</classpathScope>
                  <executable>java</executable>
                  <arguments>
                    <argument>-classpath</argument>
                    <classpath/>
                    <argument>org.openjdk.jmh.Main</argument>
                    <argument>-rf</argument>
                    <argument>json</argument>
                    <argument>-rff</argument>
                    <argument>target/jmh-result.json</argument>
                    <argument>${benchmark}</argument>
                  </arguments>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>

     <profile>
      <id>java9</id>
      <activation>
        <jdk>9</jdk>
      </activation>
      <properties>
        <!-- coverall version 4.3.0 does not work with java 9, see https://github.com/trautonen/coveralls-maven-plugin/issues/112 -->
        <coveralls.skip>true</coveralls.skip>
      </properties>
    </profile>
  </profiles>

</project>

```

## src/main/java/org/apache/commons/csv/CSVParser.java

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

package org.apache.commons.csv;

import static org.apache.commons.csv.Token.Type.TOKEN;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * Parses CSV files according to the specified format.
 *
 * Because CSV appears in many different dialects, the parser supports many formats by allowing the
 * specification of a {@link CSVFormat}.
 *
 * The parser works record wise. It is not possible to go back, once a record has been parsed from the input stream.
 *
 * <h2>Creating instances</h2>
 * <p>
 * There are several static factory methods that can be used to create instances for various types of resources:
 * </p>
 * <ul>
 *     <li>{@link #parse(java.io.File, Charset, CSVFormat)}</li>
 *     <li>{@link #parse(String, CSVFormat)}</li>
 *     <li>{@link #parse(java.net.URL, java.nio.charset.Charset, CSVFormat)}</li>
 * </ul>
 * <p>
 * Alternatively parsers can also be created by passing a {@link Reader} directly to the sole constructor.
 *
 * For those who like fluent APIs, parsers can be created using {@link CSVFormat#parse(java.io.Reader)} as a shortcut:
 * </p>
 * <pre>
 * for(CSVRecord record : CSVFormat.EXCEL.parse(in)) {
 *     ...
 * }
 * </pre>
 *
 * <h2>Parsing record wise</h2>
 * <p>
 * To parse a CSV input from a file, you write:
 * </p>
 *
 * <pre>
 * File csvData = new File(&quot;/path/to/csv&quot;);
 * CSVParser parser = CSVParser.parse(csvData, CSVFormat.RFC4180);
 * for (CSVRecord csvRecord : parser) {
 *     ...
 * }
 * </pre>
 *
 * <p>
 * This will read the parse the contents of the file using the
 * <a href="http://tools.ietf.org/html/rfc4180" target="_blank">RFC 4180</a> format.
 * </p>
 *
 * <p>
 * To parse CSV input in a format like Excel, you write:
 * </p>
 *
 * <pre>
 * CSVParser parser = CSVParser.parse(csvData, CSVFormat.EXCEL);
 * for (CSVRecord csvRecord : parser) {
 *     ...
 * }
 * </pre>
 *
 * <p>
 * If the predefined formats don't match the format at hands, custom formats can be defined. More information about
 * customising CSVFormats is available in {@link CSVFormat CSVFormat Javadoc}.
 * </p>
 *
 * <h2>Parsing into memory</h2>
 * <p>
 * If parsing record wise is not desired, the contents of the input can be read completely into memory.
 * </p>
 *
 * <pre>
 * Reader in = new StringReader(&quot;a;b\nc;d&quot;);
 * CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
 * List&lt;CSVRecord&gt; list = parser.getRecords();
 * </pre>
 *
 * <p>
 * There are two constraints that have to be kept in mind:
 * </p>
 *
 * <ol>
 *     <li>Parsing into memory starts at the current position of the parser. If you have already parsed records from
 *     the input, those records will not end up in the in memory representation of your CSV data.</li>
 *     <li>Parsing into memory may consume a lot of system resources depending on the input. For example if you're
 *     parsing a 150MB file of CSV data the contents will be read completely into memory.</li>
 * </ol>
 *
 * <h2>Notes</h2>
 * <p>
 * Internal parser state is completely covered by the format and the reader-state.
 * </p>
 *
 * @see <a href="package-summary.html">package documentation for more details</a>
 */
public final class CSVParser implements Iterable<CSVRecord>, Closeable {

    /**
     * Creates a parser for the given {@link File}.
     *
     * @param file
     *            a CSV file. Must not be null.
     * @param charset
     *            A Charset
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either file or format are null.
     * @throws IOException
     *             If an I/O error occurs
     */
    @SuppressWarnings("resource")
    public static CSVParser parse(final File file, final Charset charset, final CSVFormat format) throws IOException {
        Assertions.notNull(file, "file");
        Assertions.notNull(format, "format");
        return new CSVParser(new InputStreamReader(new FileInputStream(file), charset), format);
    }

    /**
     * Creates a CSV parser using the given {@link CSVFormat}.
     *
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param inputStream
     *            an InputStream containing CSV-formatted input. Must not be null.
     * @param charset
     *            a Charset.
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @return a new CSVParser configured with the given reader and format.
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException
     *             If there is a problem reading the header or skipping the first record
     * @since 1.5
     */
    @SuppressWarnings("resource")
    public static CSVParser parse(final InputStream inputStream, final Charset charset, final CSVFormat format)
            throws IOException {
        Assertions.notNull(inputStream, "inputStream");
        Assertions.notNull(format, "format");
        return parse(new InputStreamReader(inputStream, charset), format);
    }

    /**
     * Creates a parser for the given {@link Path}.
     *
     * @param path
     *            a CSV file. Must not be null.
     * @param charset
     *            A Charset
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either file or format are null.
     * @throws IOException
     *             If an I/O error occurs
     * @since 1.5
     */
    public static CSVParser parse(final Path path, final Charset charset, final CSVFormat format) throws IOException {
        Assertions.notNull(path, "path");
        Assertions.notNull(format, "format");
        return parse(Files.newBufferedReader(path, charset), format);
    }

    /**
     * Creates a CSV parser using the given {@link CSVFormat}
     *
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param reader
     *            a Reader containing CSV-formatted input. Must not be null.
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @return a new CSVParser configured with the given reader and format.
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException
     *             If there is a problem reading the header or skipping the first record
     * @since 1.5
     */
    public static CSVParser parse(final Reader reader, final CSVFormat format) throws IOException {
        return new CSVParser(reader, format);
    }

    /**
     * Creates a parser for the given {@link String}.
     *
     * @param string
     *            a CSV string. Must not be null.
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either string or format are null.
     * @throws IOException
     *             If an I/O error occurs
     */
    public static CSVParser parse(final String string, final CSVFormat format) throws IOException {
        Assertions.notNull(string, "string");
        Assertions.notNull(format, "format");

        return new CSVParser(new StringReader(string), format);
    }

    /**
     * Creates a parser for the given URL.
     *
     * <p>
     * If you do not read all records from the given {@code url}, you should call {@link #close()} on the parser, unless
     * you close the {@code url}.
     * </p>
     *
     * @param url
     *            a URL. Must not be null.
     * @param charset
     *            the charset for the resource. Must not be null.
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @return a new parser
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either url, charset or format are null.
     * @throws IOException
     *             If an I/O error occurs
     */
    public static CSVParser parse(final URL url, final Charset charset, final CSVFormat format) throws IOException {
        Assertions.notNull(url, "url");
        Assertions.notNull(charset, "charset");
        Assertions.notNull(format, "format");

        return new CSVParser(new InputStreamReader(url.openStream(), charset), format);
    }

    // the following objects are shared to reduce garbage

    private final CSVFormat format;

    /** A mapping of column names to column indices */
    private final Map<String, Integer> headerMap;

    private final Lexer lexer;

    private final CSVRecordIterator csvRecordIterator;
    
    /** A record buffer for getRecord(). Grows as necessary and is reused. */
    private final List<String> recordList = new ArrayList<>();

    /**
     * The next record number to assign.
     */
    private long recordNumber;

    /**
     * Lexer offset when the parser does not start parsing at the beginning of the source. Usually used in combination
     * with {@link #recordNumber}.
     */
    private final long characterOffset;

    private final Token reusableToken = new Token();

    /**
     * Customized CSV parser using the given {@link CSVFormat}
     *
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param reader
     *            a Reader containing CSV-formatted input. Must not be null.
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException
     *             If there is a problem reading the header or skipping the first record
     */
    public CSVParser(final Reader reader, final CSVFormat format) throws IOException {
        this(reader, format, 0, 1);
    }

    /**
     * Customized CSV parser using the given {@link CSVFormat}
     *
     * <p>
     * If you do not read all records from the given {@code reader}, you should call {@link #close()} on the parser,
     * unless you close the {@code reader}.
     * </p>
     *
     * @param reader
     *            a Reader containing CSV-formatted input. Must not be null.
     * @param format
     *            the CSVFormat used for CSV parsing. Must not be null.
     * @param characterOffset
     *            Lexer offset when the parser does not start parsing at the beginning of the source.
     * @param recordNumber
     *            The next record number to assign
     * @throws IllegalArgumentException
     *             If the parameters of the format are inconsistent or if either reader or format are null.
     * @throws IOException
     *             If there is a problem reading the header or skipping the first record
     * @since 1.1
     */
    @SuppressWarnings("resource")
    public CSVParser(final Reader reader, final CSVFormat format, final long characterOffset, final long recordNumber)
            throws IOException {
        Assertions.notNull(reader, "reader");
        Assertions.notNull(format, "format");

        this.format = format;
        this.lexer = new Lexer(format, new ExtendedBufferedReader(reader));
        this.csvRecordIterator = new CSVRecordIterator();
        this.headerMap = this.initializeHeader();
        this.characterOffset = characterOffset;
        this.recordNumber = recordNumber - 1;
    }

    private void addRecordValue(final boolean lastRecord) {
        final String input = this.reusableToken.content.toString();
        final String inputClean = this.format.getTrim() ? input.trim() : input;
        if (lastRecord && inputClean.isEmpty() && this.format.getTrailingDelimiter()) {
            return;
        }
        final String nullString = this.format.getNullString();
        this.recordList.add(inputClean.equals(nullString) ? null : inputClean);
    }

    /**
     * Closes resources.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (this.lexer != null) {
            this.lexer.close();
        }
    }

    /**
     * Returns the current line number in the input stream.
     *
     * <p>
     * <strong>ATTENTION:</strong> If your CSV input has multi-line values, the returned number does not correspond to
     * the record number.
     * </p>
     *
     * @return current line number
     */
    public long getCurrentLineNumber() {
        return this.lexer.getCurrentLineNumber();
    }

    /**
     * Gets the first end-of-line string encountered.
     *
     * @return the first end-of-line string
     * @since 1.5
     */
    public String getFirstEndOfLine() {
        return lexer.getFirstEol();
    }

    /**
     * Returns a copy of the header map that iterates in column order.
     * <p>
     * The map keys are column names. The map values are 0-based indices.
     * </p>
     * @return a copy of the header map that iterates in column order.
     */
    public Map<String, Integer> getHeaderMap() {
        return this.headerMap == null ? null : new LinkedHashMap<>(this.headerMap);
    }

    /**
     * Returns the current record number in the input stream.
     *
     * <p>
     * <strong>ATTENTION:</strong> If your CSV input has multi-line values, the returned number does not correspond to
     * the line number.
     * </p>
     *
     * @return current record number
     */
    public long getRecordNumber() {
        return this.recordNumber;
    }

    /**
     * Parses the CSV input according to the given format and returns the content as a list of
     * {@link CSVRecord CSVRecords}.
     *
     * <p>
     * The returned content starts at the current parse-position in the stream.
     * </p>
     *
     * @return list of {@link CSVRecord CSVRecords}, may be empty
     * @throws IOException
     *             on parse error or input read-failure
     */
    public List<CSVRecord> getRecords() throws IOException {
        CSVRecord rec;
        final List<CSVRecord> records = new ArrayList<>();
        while ((rec = this.nextRecord()) != null) {
            records.add(rec);
        }
        return records;
    }

    /**
     * Initializes the name to index mapping if the format defines a header.
     *
     * @return null if the format has no header.
     * @throws IOException if there is a problem reading the header or skipping the first record
     */
    private Map<String, Integer> initializeHeader() throws IOException {
        Map<String, Integer> hdrMap = null;
        final String[] formatHeader = this.format.getHeader();
        if (formatHeader != null) {
            hdrMap = this.format.getIgnoreHeaderCase() ?
                    new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER) :
                    new LinkedHashMap<String, Integer>();

            String[] headerRecord = null;
            if (formatHeader.length == 0) {
                // read the header from the first line of the file
                final CSVRecord nextRecord = this.nextRecord();
                if (nextRecord != null) {
                    headerRecord = nextRecord.values();
                }
            } else {
                if (this.format.getSkipHeaderRecord()) {
                    this.nextRecord();
                }
                headerRecord = formatHeader;
            }

            // build the name to index mappings
            if (headerRecord != null) {
                for (int i = 0; i < headerRecord.length; i++) {
                    final String header = headerRecord[i];
                    final boolean containsHeader = hdrMap.containsKey(header);
                    final boolean emptyHeader = header == null || header.trim().isEmpty();
                    if (containsHeader && (!emptyHeader || !this.format.getAllowMissingColumnNames())) {
                        throw new IllegalArgumentException("The header contains a duplicate name: \"" + header +
                                "\" in " + Arrays.toString(headerRecord));
                    }
                    hdrMap.put(header, Integer.valueOf(i));
                }
            }
        }
        return hdrMap;
    }

    /**
     * Gets whether this parser is closed.
     *
     * @return whether this parser is closed.
     */
    public boolean isClosed() {
        return this.lexer.isClosed();
    }

    /**
     * Returns an iterator on the records.
     *
     * <p>
     * An {@link IOException} caught during the iteration are re-thrown as an
     * {@link IllegalStateException}.
     * </p>
     * <p>
     * If the parser is closed a call to {@link Iterator#next()} will throw a
     * {@link NoSuchElementException}.
     * </p>
     */
    @Override
    public Iterator<CSVRecord> iterator() {
        return csvRecordIterator;
    }
    
    class CSVRecordIterator implements Iterator<CSVRecord> {
        private CSVRecord current;
  
        private CSVRecord getNextRecord() {
            try {
                return CSVParser.this.nextRecord();
            } catch (final IOException e) {
                throw new IllegalStateException(
                        e.getClass().getSimpleName() + " reading next record: " + e.toString(), e);
            }
        }
  
        @Override
        public boolean hasNext() {
            if (CSVParser.this.isClosed()) {
                return false;
            }
            if (this.current == null) {
                this.current = this.getNextRecord();
            }
  
            return this.current != null;
        }
  
        @Override
        public CSVRecord next() {
            if (CSVParser.this.isClosed()) {
                throw new NoSuchElementException("CSVParser has been closed");
            }
            CSVRecord next = this.current;
            this.current = null;
  
            if (next == null) {
                // hasNext() wasn't called before
                next = this.getNextRecord();
                if (next == null) {
                    throw new NoSuchElementException("No more CSV records available");
                }
            }
  
            return next;
        }
  
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * Parses the next record from the current point in the stream.
     *
     * @return the record as an array of values, or {@code null} if the end of the stream has been reached
     * @throws IOException
     *             on parse error or input read-failure
     */
    CSVRecord nextRecord() throws IOException {
        CSVRecord result = null;
        this.recordList.clear();
        StringBuilder sb = null;
        final long startCharPosition = lexer.getCharacterPosition() + this.characterOffset;
        do {
            this.reusableToken.reset();
            this.lexer.nextToken(this.reusableToken);
            switch (this.reusableToken.type) {
            case TOKEN:
                this.addRecordValue(false);
                break;
            case EORECORD:
                this.addRecordValue(true);
                break;
            case EOF:
                if (this.reusableToken.isReady) {
                    this.addRecordValue(true);
                }
                break;
            case INVALID:
                throw new IOException("(line " + this.getCurrentLineNumber() + ") invalid parse sequence");
            case COMMENT: // Ignored currently
                if (sb == null) { // first comment for this record
                    sb = new StringBuilder();
                } else {
                    sb.append(Constants.LF);
                }
                sb.append(this.reusableToken.content);
                this.reusableToken.type = TOKEN; // Read another token
                break;
            default:
                throw new IllegalStateException("Unexpected Token type: " + this.reusableToken.type);
            }
        } while (this.reusableToken.type == TOKEN);

        if (!this.recordList.isEmpty()) {
            this.recordNumber++;
            final String comment = sb == null ? null : sb.toString();
            result = new CSVRecord(this.recordList.toArray(new String[this.recordList.size()]), this.headerMap, comment,
                    this.recordNumber, startCharPosition);
        }
        return result;
    }

}

```
