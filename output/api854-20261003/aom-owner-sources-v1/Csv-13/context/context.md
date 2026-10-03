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
    <version>39</version>
  </parent>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-csv</artifactId>
  <version>1.3-SNAPSHOT</version>
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
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.4</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>1.4.182</version>
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
      <timezone>-5</timezone>
    </developer>
    <developer>
      <name>Benedikt Ritter</name>
      <id>britter</id>
      <email>britter@apache.org</email>
      <organization>The Apache Software Foundation</organization>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>Bob Smith</name>
    </contributor>
  </contributors>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/csv/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/csv/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/csv/trunk</url>
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
    <commons.release.version>1.3</commons.release.version>
    <commons.release.desc>(Java 6.0+)</commons.release.desc>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC1</commons.rc.version>
    <commons.componentid>csv</commons.componentid>
    <commons.jira.id>CSV</commons.jira.id>
    <commons.jira.pid>12313222</commons.jira.pid>
    <maven.compiler.source>1.6</maven.compiler.source>
    <maven.compiler.target>1.6</maven.compiler.target>
    <!-- Ensure copies work OK (can be removed later when this is in parent POM) -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <commons.encoding>UTF-8</commons.encoding>

    <checkstyle.version>2.17</checkstyle.version>
    <checkstyle.header.file>${basedir}/LICENSE-header.txt</checkstyle.header.file>
    <checkstyle.resourceExcludes>LICENSE.txt, NOTICE.txt</checkstyle.resourceExcludes>
  </properties>

  <build>
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
        <version>3.6</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>3.0.3</version>
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
          <version>3.4</version>
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
  </profiles>

</project>

```

## src/main/java/org/apache/commons/csv/CSVFormat.java

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

import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Specifies the format of a CSV file and parses input.
 *
 * <h2>Using predefined formats</h2>
 *
 * <p>
 * You can use one of the predefined formats:
 * </p>
 *
 * <ul>
 * <li>{@link #DEFAULT}</li>
 * <li>{@link #EXCEL}</li>
 * <li>{@link #MYSQL}</li>
 * <li>{@link #RFC4180}</li>
 * <li>{@link #TDF}</li>
 * </ul>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * CSVParser parser = CSVFormat.EXCEL.parse(reader);
 * </pre>
 *
 * <p>
 * The {@link CSVParser} provides static methods to parse other input types, for example:
 * </p>
 *
 * <pre>
 * CSVParser parser = CSVParser.parse(file, StandardCharsets.US_ASCII, CSVFormat.EXCEL);
 * </pre>
 *
 * <h2>Defining formats</h2>
 *
 * <p>
 * You can extend a format by calling the {@code with} methods. For example:
 * </p>
 *
 * <pre>
 * CSVFormat.EXCEL.withNullString(&quot;N/A&quot;).withIgnoreSurroundingSpaces(true);
 * </pre>
 *
 * <h2>Defining column names</h2>
 *
 * <p>
 * To define the column names you want to use to access records, write:
 * </p>
 *
 * <pre>
 * CSVFormat.EXCEL.withHeader(&quot;Col1&quot;, &quot;Col2&quot;, &quot;Col3&quot;);
 * </pre>
 *
 * <p>
 * Calling {@link #withHeader(String...)} let's you use the given names to address values in a {@link CSVRecord}, and
 * assumes that your CSV source does not contain a first record that also defines column names.
 *
 * If it does, then you are overriding this metadata with your names and you should skip the first record by calling
 * {@link #withSkipHeaderRecord(boolean)} with {@code true}.
 * </p>
 *
 * <h2>Parsing</h2>
 *
 * <p>
 * You can use a format directly to parse a reader. For example, to parse an Excel file with columns header, write:
 * </p>
 *
 * <pre>
 * Reader in = ...;
 * CSVFormat.EXCEL.withHeader(&quot;Col1&quot;, &quot;Col2&quot;, &quot;Col3&quot;).parse(in);
 * </pre>
 *
 * <p>
 * For other input types, like resources, files, and URLs, use the static methods on {@link CSVParser}.
 * </p>
 *
 * <h2>Referencing columns safely</h2>
 *
 * <p>
 * If your source contains a header record, you can simplify your code and safely reference columns, by using
 * {@link #withHeader(String...)} with no arguments:
 * </p>
 *
 * <pre>
 * CSVFormat.EXCEL.withHeader();
 * </pre>
 *
 * <p>
 * This causes the parser to read the first record and use its values as column names.
 *
 * Then, call one of the {@link CSVRecord} get method that takes a String column name argument:
 * </p>
 *
 * <pre>
 * String value = record.get(&quot;Col1&quot;);
 * </pre>
 *
 * <p>
 * This makes your code impervious to changes in column order in the CSV file.
 * </p>
 *
 * <h2>Notes</h2>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 * @version $Id$
 */
public final class CSVFormat implements Serializable {

    /**
     * Predefines formats.
     * 
     * @since 1.2
     */
    public static enum Predefined {

        /**
         * @see CSVFormat#DEFAULT
         */
        Default(CSVFormat.DEFAULT), 

        /**
         * @see CSVFormat#EXCEL
         */
        Excel(CSVFormat.EXCEL), 

        /**
         * @see CSVFormat#MYSQL
         */
        MySQL(CSVFormat.MYSQL), 

        /**
         * @see CSVFormat#RFC4180
         */
        RFC4180(CSVFormat.RFC4180),

        /**
         * @see CSVFormat#TDF
         */
        TDF(CSVFormat.TDF);

        private final CSVFormat format;

        private Predefined(final CSVFormat format) {
            this.format = format;
        }
        
        /**
         * Gets the format.
         * 
         * @return the format.
         */
        public CSVFormat getFormat() {
            return format;
        }
    };
    
    private static final long serialVersionUID = 1L;

    private final char delimiter;
    private final Character quoteCharacter; // null if quoting is disabled
    private final QuoteMode quoteMode;
    private final Character commentMarker; // null if commenting is disabled
    private final Character escapeCharacter; // null if escaping is disabled
    private final boolean ignoreSurroundingSpaces; // Should leading/trailing spaces be ignored around values?
    private final boolean allowMissingColumnNames;
    private final boolean ignoreEmptyLines;
    private final String recordSeparator; // for outputs
    private final String nullString; // the string to be used for null values
    private final String[] header; // array of header column names
    private final String[] headerComments; // array of header comment lines
    private final boolean skipHeaderRecord;
    private final boolean ignoreHeaderCase; // should ignore header names case

    /**
     * Standard comma separated format, as for {@link #RFC4180} but allowing empty lines.
     *
     * <p>
     * Settings are:
     * </p>
     * <ul>
     * <li>withDelimiter(',')</li>
     * <li>withQuote('"')</li>
     * <li>withRecordSeparator("\r\n")</li>
     * <li>withIgnoreEmptyLines(true)</li>
     * </ul>
     * @see Predefined#Default
     */
    public static final CSVFormat DEFAULT = new CSVFormat(COMMA, DOUBLE_QUOTE_CHAR, null, null, null, false, true,
            CRLF, null, null, null, false, false, false);

    /**
     * Comma separated format as defined by <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
     *
     * <p>
     * Settings are:
     * </p>
     * <ul>
     * <li>withDelimiter(',')</li>
     * <li>withQuote('"')</li>
     * <li>withRecordSeparator("\r\n")</li>
     * <li>withIgnoreEmptyLines(false)</li>
     * </ul>
     * @see Predefined#RFC4180
     */
    public static final CSVFormat RFC4180 = DEFAULT.withIgnoreEmptyLines(false);

    /**
     * Excel file format (using a comma as the value delimiter). Note that the actual value delimiter used by Excel is
     * locale dependent, it might be necessary to customize this format to accommodate to your regional settings.
     *
     * <p>
     * For example for parsing or generating a CSV file on a French system the following format will be used:
     * </p>
     *
     * <pre>
     * CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';');
     * </pre>
     *
     * <p>
     * Settings are:
     * </p>
     * <ul>
     * <li>{@link #withDelimiter(char) withDelimiter(',')}</li>
     * <li>{@link #withQuote(char) withQuote('"')}</li>
     * <li>{@link #withRecordSeparator(String) withRecordSeparator("\r\n")}</li>
     * <li>{@link #withIgnoreEmptyLines(boolean) withIgnoreEmptyLines(false)}</li>
     * <li>{@link #withAllowMissingColumnNames(boolean) withAllowMissingColumnNames(true)}</li>
     * </ul>
     * <p>
     * Note: this is currently like {@link #RFC4180} plus {@link #withAllowMissingColumnNames(boolean)
     * withAllowMissingColumnNames(true)}.
     * </p>
     * @see Predefined#Excel
     */
    public static final CSVFormat EXCEL = DEFAULT.withIgnoreEmptyLines(false).withAllowMissingColumnNames();

    /**
     * Tab-delimited format.
     *
     * <p>
     * Settings are:
     * </p>
     * <ul>
     * <li>withDelimiter('\t')</li>
     * <li>withQuote('"')</li>
     * <li>withRecordSeparator("\r\n")</li>
     * <li>withIgnoreSurroundingSpaces(true)</li>
     * </ul>
     * @see Predefined#TDF
     */
    public static final CSVFormat TDF = DEFAULT.withDelimiter(TAB).withIgnoreSurroundingSpaces();

    /**
     * Default MySQL format used by the {@code SELECT INTO OUTFILE} and {@code LOAD DATA INFILE} operations.
     *
     * <p>
     * This is a tab-delimited format with a LF character as the line separator. Values are not quoted and special
     * characters are escaped with {@code '\'}. The default NULL string is {@code "\\N"}.
     * </p>
     *
     * <p>
     * Settings are:
     * </p>
     * <ul>
     * <li>withDelimiter('\t')</li>
     * <li>withQuote(null)</li>
     * <li>withRecordSeparator('\n')</li>
     * <li>withIgnoreEmptyLines(false)</li>
     * <li>withEscape('\\')</li>
     * <li>withNullString("\\N")</li>
     * </ul>
     *
     * @see Predefined#MySQL
     * @see <a href="http://dev.mysql.com/doc/refman/5.1/en/load-data.html">
     *      http://dev.mysql.com/doc/refman/5.1/en/load-data.html</a>
     */
    public static final CSVFormat MYSQL = DEFAULT.withDelimiter(TAB).withEscape(BACKSLASH).withIgnoreEmptyLines(false)
            .withQuote(null).withRecordSeparator(LF).withNullString("\\N");

    /**
     * Returns true if the given character is a line break character.
     *
     * @param c
     *            the character to check
     *
     * @return true if <code>c</code> is a line break character
     */
    private static boolean isLineBreak(final char c) {
        return c == LF || c == CR;
    }

    /**
     * Returns true if the given character is a line break character.
     *
     * @param c
     *            the character to check, may be null
     *
     * @return true if <code>c</code> is a line break character (and not null)
     */
    private static boolean isLineBreak(final Character c) {
        return c != null && isLineBreak(c.charValue());
    }

    /**
     * Creates a new CSV format with the specified delimiter.
     *
     * <p>
     * Use this method if you want to create a CSVFormat from scratch. All fields but the delimiter will be initialized
     * with null/false.
     * </p>
     *
     * @param delimiter
     *            the char used for value separation, must not be a line break character
     * @return a new CSV format.
     * @throws IllegalArgumentException
     *             if the delimiter is a line break character
     *
     * @see #DEFAULT
     * @see #RFC4180
     * @see #MYSQL
     * @see #EXCEL
     * @see #TDF
     */
    public static CSVFormat newFormat(final char delimiter) {
        return new CSVFormat(delimiter, null, null, null, null, false, false, null, null, null, null, false, false, false);
    }

    /**
     * Gets one of the predefined formats from {@link CSVFormat.Predefined}.
     * 
     * @param format
     *            name
     * @return one of the predefined formats
     * @since 1.2
     */
    public static CSVFormat valueOf(final String format) {
        return CSVFormat.Predefined.valueOf(format).getFormat();
    }

    /**
     * Creates a customized CSV format.
     *
     * @param delimiter
     *            the char used for value separation, must not be a line break character
     * @param quoteChar
     *            the Character used as value encapsulation marker, may be {@code null} to disable
     * @param quoteMode
     *            the quote mode
     * @param commentStart
     *            the Character used for comment identification, may be {@code null} to disable
     * @param escape
     *            the Character used to escape special characters in values, may be {@code null} to disable
     * @param ignoreSurroundingSpaces
     *            {@code true} when whitespaces enclosing values should be ignored
     * @param ignoreEmptyLines
     *            {@code true} when the parser should skip empty lines
     * @param recordSeparator
     *            the line separator to use for output
     * @param nullString
     *            the line separator to use for output
     * @param headerComments
     *            the comments to be printed by the Printer before the actual CSV data
     * @param header
     *            the header
     * @param skipHeaderRecord
     *            TODO
     * @param allowMissingColumnNames
     *            TODO
     * @param ignoreHeaderCase
     *            TODO
     * @throws IllegalArgumentException
     *             if the delimiter is a line break character
     */
    private CSVFormat(final char delimiter, final Character quoteChar, final QuoteMode quoteMode,
            final Character commentStart, final Character escape, final boolean ignoreSurroundingSpaces,
            final boolean ignoreEmptyLines, final String recordSeparator, final String nullString,
            final Object[] headerComments, final String[] header, final boolean skipHeaderRecord,
            final boolean allowMissingColumnNames, final boolean ignoreHeaderCase) {
        this.delimiter = delimiter;
        this.quoteCharacter = quoteChar;
        this.quoteMode = quoteMode;
        this.commentMarker = commentStart;
        this.escapeCharacter = escape;
        this.ignoreSurroundingSpaces = ignoreSurroundingSpaces;
        this.allowMissingColumnNames = allowMissingColumnNames;
        this.ignoreEmptyLines = ignoreEmptyLines;
        this.recordSeparator = recordSeparator;
        this.nullString = nullString;
        this.headerComments = toStringArray(headerComments);
        this.header = header == null ? null : header.clone();
        this.skipHeaderRecord = skipHeaderRecord;
        this.ignoreHeaderCase = ignoreHeaderCase;
        validate();
    }

    private String[] toStringArray(final Object[] values) {
        if (values == null) {
            return null;
        }
        final String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            final Object value = values[i];
            strings[i] = value == null ? null : value.toString();
        }
        return strings;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CSVFormat other = (CSVFormat) obj;
        if (delimiter != other.delimiter) {
            return false;
        }
        if (quoteMode != other.quoteMode) {
            return false;
        }
        if (quoteCharacter == null) {
            if (other.quoteCharacter != null) {
                return false;
            }
        } else if (!quoteCharacter.equals(other.quoteCharacter)) {
            return false;
        }
        if (commentMarker == null) {
            if (other.commentMarker != null) {
                return false;
            }
        } else if (!commentMarker.equals(other.commentMarker)) {
            return false;
        }
        if (escapeCharacter == null) {
            if (other.escapeCharacter != null) {
                return false;
            }
        } else if (!escapeCharacter.equals(other.escapeCharacter)) {
            return false;
        }
        if (nullString == null) {
            if (other.nullString != null) {
                return false;
            }
        } else if (!nullString.equals(other.nullString)) {
            return false;
        }
        if (!Arrays.equals(header, other.header)) {
            return false;
        }
        if (ignoreSurroundingSpaces != other.ignoreSurroundingSpaces) {
            return false;
        }
        if (ignoreEmptyLines != other.ignoreEmptyLines) {
            return false;
        }
        if (skipHeaderRecord != other.skipHeaderRecord) {
            return false;
        }
        if (recordSeparator == null) {
            if (other.recordSeparator != null) {
                return false;
            }
        } else if (!recordSeparator.equals(other.recordSeparator)) {
            return false;
        }
        return true;
    }

    /**
     * Formats the specified values.
     *
     * @param values
     *            the values to format
     * @return the formatted values
     */
    public String format(final Object... values) {
        final StringWriter out = new StringWriter();
        try {
            new CSVPrinter(out, this).printRecord(values);
            return out.toString().trim();
        } catch (final IOException e) {
            // should not happen because a StringWriter does not do IO.
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the character marking the start of a line comment.
     *
     * @return the comment start marker, may be {@code null}
     */
    public Character getCommentMarker() {
        return commentMarker;
    }

    /**
     * Returns the character delimiting the values (typically ';', ',' or '\t').
     *
     * @return the delimiter character
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * Returns the escape character.
     *
     * @return the escape character, may be {@code null}
     */
    public Character getEscapeCharacter() {
        return escapeCharacter;
    }

    /**
     * Returns a copy of the header array.
     *
     * @return a copy of the header array; {@code null} if disabled, the empty array if to be read from the file
     */
    public String[] getHeader() {
        return header != null ? header.clone() : null;
    }

    /**
     * Returns a copy of the header comment array.
     *
     * @return a copy of the header comment array; {@code null} if disabled.
     */
    public String[] getHeaderComments() {
        return headerComments != null ? headerComments.clone() : null;
    }

    /**
     * Specifies whether missing column names are allowed when parsing the header line.
     *
     * @return {@code true} if missing column names are allowed when parsing the header line, {@code false} to throw an
     *         {@link IllegalArgumentException}.
     */
    public boolean getAllowMissingColumnNames() {
        return allowMissingColumnNames;
    }

    /**
     * Specifies whether empty lines between records are ignored when parsing input.
     *
     * @return {@code true} if empty lines between records are ignored, {@code false} if they are turned into empty
     *         records.
     */
    public boolean getIgnoreEmptyLines() {
        return ignoreEmptyLines;
    }

    /**
     * Specifies whether spaces around values are ignored when parsing input.
     *
     * @return {@code true} if spaces around values are ignored, {@code false} if they are treated as part of the value.
     */
    public boolean getIgnoreSurroundingSpaces() {
        return ignoreSurroundingSpaces;
    }

    /**
     * Specifies whether header names will be accessed ignoring case.
     *
     * @return {@code true} if header names cases are ignored, {@code false} if they are case sensitive.
     */
    public boolean getIgnoreHeaderCase() {
        return ignoreHeaderCase;
    }

    /**
     * Gets the String to convert to and from {@code null}.
     * <ul>
     * <li>
     * <strong>Reading:</strong> Converts strings equal to the given {@code nullString} to {@code null} when reading
     * records.</li>
     * <li>
     * <strong>Writing:</strong> Writes {@code null} as the given {@code nullString} when writing records.</li>
     * </ul>
     *
     * @return the String to convert to and from {@code null}. No substitution occurs if {@code null}
     */
    public String getNullString() {
        return nullString;
    }

    /**
     * Returns the character used to encapsulate values containing special characters.
     *
     * @return the quoteChar character, may be {@code null}
     */
    public Character getQuoteCharacter() {
        return quoteCharacter;
    }

    /**
     * Returns the quote policy output fields.
     *
     * @return the quote policy
     */
    public QuoteMode getQuoteMode() {
        return quoteMode;
    }

    /**
     * Returns the record separator delimiting output records.
     *
     * @return the record separator
     */
    public String getRecordSeparator() {
        return recordSeparator;
    }

    /**
     * Returns whether to skip the header record.
     *
     * @return whether to skip the header record.
     */
    public boolean getSkipHeaderRecord() {
        return skipHeaderRecord;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + delimiter;
        result = prime * result + ((quoteMode == null) ? 0 : quoteMode.hashCode());
        result = prime * result + ((quoteCharacter == null) ? 0 : quoteCharacter.hashCode());
        result = prime * result + ((commentMarker == null) ? 0 : commentMarker.hashCode());
        result = prime * result + ((escapeCharacter == null) ? 0 : escapeCharacter.hashCode());
        result = prime * result + ((nullString == null) ? 0 : nullString.hashCode());
        result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
        result = prime * result + (ignoreHeaderCase ? 1231 : 1237);
        result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
        result = prime * result + (skipHeaderRecord ? 1231 : 1237);
        result = prime * result + ((recordSeparator == null) ? 0 : recordSeparator.hashCode());
        result = prime * result + Arrays.hashCode(header);
        return result;
    }

    /**
     * Specifies whether comments are supported by this format.
     *
     * Note that the comment introducer character is only recognized at the start of a line.
     *
     * @return {@code true} is comments are supported, {@code false} otherwise
     */
    public boolean isCommentMarkerSet() {
        return commentMarker != null;
    }

    /**
     * Returns whether escape are being processed.
     *
     * @return {@code true} if escapes are processed
     */
    public boolean isEscapeCharacterSet() {
        return escapeCharacter != null;
    }

    /**
     * Returns whether a nullString has been defined.
     *
     * @return {@code true} if a nullString is defined
     */
    public boolean isNullStringSet() {
        return nullString != null;
    }

    /**
     * Returns whether a quoteChar has been defined.
     *
     * @return {@code true} if a quoteChar is defined
     */
    public boolean isQuoteCharacterSet() {
        return quoteCharacter != null;
    }

    /**
     * Parses the specified content.
     *
     * <p>
     * See also the various static parse methods on {@link CSVParser}.
     * </p>
     *
     * @param in
     *            the input stream
     * @return a parser over a stream of {@link CSVRecord}s.
     * @throws IOException
     *             If an I/O error occurs
     */
    public CSVParser parse(final Reader in) throws IOException {
        return new CSVParser(in, this);
    }

    /**
     * Prints to the specified output.
     *
     * <p>
     * See also {@link CSVPrinter}.
     * </p>
     *
     * @param out
     *            the output
     * @return a printer to an output
     * @throws IOException
     *             thrown if the optional header cannot be printed.
     */
    public CSVPrinter print(final Appendable out) throws IOException {
        return new CSVPrinter(out, this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Delimiter=<").append(delimiter).append('>');
        if (isEscapeCharacterSet()) {
            sb.append(' ');
            sb.append("Escape=<").append(escapeCharacter).append('>');
        }
        if (isQuoteCharacterSet()) {
            sb.append(' ');
            sb.append("QuoteChar=<").append(quoteCharacter).append('>');
        }
        if (isCommentMarkerSet()) {
            sb.append(' ');
            sb.append("CommentStart=<").append(commentMarker).append('>');
        }
        if (isNullStringSet()) {
            sb.append(' ');
            sb.append("NullString=<").append(nullString).append('>');
        }
        if (recordSeparator != null) {
            sb.append(' ');
            sb.append("RecordSeparator=<").append(recordSeparator).append('>');
        }
        if (getIgnoreEmptyLines()) {
            sb.append(" EmptyLines:ignored");
        }
        if (getIgnoreSurroundingSpaces()) {
            sb.append(" SurroundingSpaces:ignored");
        }
        if (getIgnoreHeaderCase()) {
            sb.append(" IgnoreHeaderCase:ignored");
        }
        sb.append(" SkipHeaderRecord:").append(skipHeaderRecord);
        if (headerComments != null) {
            sb.append(' ');
            sb.append("HeaderComments:").append(Arrays.toString(headerComments));
        }
        if (header != null) {
            sb.append(' ');
            sb.append("Header:").append(Arrays.toString(header));
        }
        return sb.toString();
    }

    /**
     * Verifies the consistency of the parameters and throws an IllegalArgumentException if necessary.
     *
     * @throws IllegalArgumentException
     */
    private void validate() throws IllegalArgumentException {
        if (isLineBreak(delimiter)) {
            throw new IllegalArgumentException("The delimiter cannot be a line break");
        }
        
        if (quoteCharacter != null && delimiter == quoteCharacter.charValue()) {
            throw new IllegalArgumentException("The quoteChar character and the delimiter cannot be the same ('" +
                    quoteCharacter + "')");
        }

        if (escapeCharacter != null && delimiter == escapeCharacter.charValue()) {
            throw new IllegalArgumentException("The escape character and the delimiter cannot be the same ('" +
                    escapeCharacter + "')");
        }

        if (commentMarker != null && delimiter == commentMarker.charValue()) {
            throw new IllegalArgumentException("The comment start character and the delimiter cannot be the same ('" +
                    commentMarker + "')");
        }

        if (quoteCharacter != null && quoteCharacter.equals(commentMarker)) {
            throw new IllegalArgumentException("The comment start character and the quoteChar cannot be the same ('" +
                    commentMarker + "')");
        }

        if (escapeCharacter != null && escapeCharacter.equals(commentMarker)) {
            throw new IllegalArgumentException("The comment start and the escape character cannot be the same ('" +
                    commentMarker + "')");
        }

        if (escapeCharacter == null && quoteMode == QuoteMode.NONE) {
            throw new IllegalArgumentException("No quotes mode set but no escape character is set");
        }
        
        // validate header
        if (header != null) {
            final Set<String> dupCheck = new HashSet<String>();
            for (final String hdr : header) {
                if (!dupCheck.add(hdr)) {
                    throw new IllegalArgumentException("The header contains a duplicate entry: '" + hdr + "' in " +
                            Arrays.toString(header));
                }
            }
        }
    }

    /**
     * Sets the comment start marker of the format to the specified character.
     *
     * Note that the comment start character is only recognized at the start of a line.
     *
     * @param commentMarker
     *            the comment start marker
     * @return A new CSVFormat that is equal to this one but with the specified character as the comment start marker
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withCommentMarker(final char commentMarker) {
        return withCommentMarker(Character.valueOf(commentMarker));
    }

    /**
     * Sets the comment start marker of the format to the specified character.
     *
     * Note that the comment start character is only recognized at the start of a line.
     *
     * @param commentMarker
     *            the comment start marker, use {@code null} to disable
     * @return A new CSVFormat that is equal to this one but with the specified character as the comment start marker
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withCommentMarker(final Character commentMarker) {
        if (isLineBreak(commentMarker)) {
            throw new IllegalArgumentException("The comment start marker character cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the delimiter of the format to the specified character.
     *
     * @param delimiter
     *            the delimiter character
     * @return A new CSVFormat that is equal to this with the specified character as delimiter
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withDelimiter(final char delimiter) {
        if (isLineBreak(delimiter)) {
            throw new IllegalArgumentException("The delimiter cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the escape character of the format to the specified character.
     *
     * @param escape
     *            the escape character
     * @return A new CSVFormat that is equal to his but with the specified character as the escape character
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withEscape(final char escape) {
        return withEscape(Character.valueOf(escape));
    }

    /**
     * Sets the escape character of the format to the specified character.
     *
     * @param escape
     *            the escape character, use {@code null} to disable
     * @return A new CSVFormat that is equal to this but with the specified character as the escape character
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withEscape(final Character escape) {
        if (isLineBreak(escape)) {
            throw new IllegalArgumentException("The escape character cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the header of the format. The header can either be parsed automatically from the input file with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader();
     * </pre>
     *
     * or specified manually with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader(&quot;name&quot;, &quot;email&quot;, &quot;phone&quot;);
     * </pre>
     * <p>
     * The header is also used by the {@link CSVPrinter}..
     * </p>
     *
     * @param header
     *            the header, {@code null} if disabled, empty if parsed automatically, user specified otherwise.
     *
     * @return A new CSVFormat that is equal to this but with the specified header
     * @see #withSkipHeaderRecord(boolean)
     */
    public CSVFormat withHeader(final String... header) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the header of the format. The header can either be parsed automatically from the input file with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader();
     * </pre>
     *
     * or specified manually with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader(resultSet);
     * </pre>
     * <p>
     * The header is also used by the {@link CSVPrinter}..
     * </p>
     *
     * @param resultSet
     *            the resultSet for the header, {@code null} if disabled, empty if parsed automatically, user specified
     *            otherwise.
     *
     * @return A new CSVFormat that is equal to this but with the specified header
     * @throws SQLException
     *             SQLException if a database access error occurs or this method is called on a closed result set.
     * @since 1.1
     */
    public CSVFormat withHeader(final ResultSet resultSet) throws SQLException {
        return withHeader(resultSet != null ? resultSet.getMetaData() : null);
    }

    /**
     * Sets the header of the format. The header can either be parsed automatically from the input file with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader();
     * </pre>
     *
     * or specified manually with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader(metaData);
     * </pre>
     * <p>
     * The header is also used by the {@link CSVPrinter}..
     * </p>
     *
     * @param metaData
     *            the metaData for the header, {@code null} if disabled, empty if parsed automatically, user specified
     *            otherwise.
     *
     * @return A new CSVFormat that is equal to this but with the specified header
     * @throws SQLException
     *             SQLException if a database access error occurs or this method is called on a closed result set.
     * @since 1.1
     */
    public CSVFormat withHeader(final ResultSetMetaData metaData) throws SQLException {
        String[] labels = null;
        if (metaData != null) {
            final int columnCount = metaData.getColumnCount();
            labels = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                labels[i] = metaData.getColumnLabel(i + 1);
            }
        }
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, labels,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the header comments of the format. The comments will be printed first, before the headers. This setting is
     * ignored by the parser.
     *
     * <pre>
     * CSVFormat format = aformat.withHeaderComments(&quot;Generated by Apache Commons CSV 1.1.&quot;, new Date());
     * </pre>
     *
     * @param headerComments
     *            the headerComments which will be printed by the Printer before the actual CSV data.
     *
     * @return A new CSVFormat that is equal to this but with the specified header
     * @see #withSkipHeaderRecord(boolean)
     * @since 1.1
     */
    public CSVFormat withHeaderComments(final Object... headerComments) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the missing column names behavior of the format to {@code true}
     *
     * @return A new CSVFormat that is equal to this but with the specified missing column names behavior.
     * @see #withAllowMissingColumnNames(boolean)
     * @since 1.1
     */
    public CSVFormat withAllowMissingColumnNames() {
        return this.withAllowMissingColumnNames(true);
    }

    /**
     * Sets the missing column names behavior of the format.
     *
     * @param allowMissingColumnNames
     *            the missing column names behavior, {@code true} to allow missing column names in the header line,
     *            {@code false} to cause an {@link IllegalArgumentException} to be thrown.
     * @return A new CSVFormat that is equal to this but with the specified missing column names behavior.
     */
    public CSVFormat withAllowMissingColumnNames(final boolean allowMissingColumnNames) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the empty line skipping behavior of the format to {@code true}.
     *
     * @return A new CSVFormat that is equal to this but with the specified empty line skipping behavior.
     * @since {@link #withIgnoreEmptyLines(boolean)}
     * @since 1.1
     */
    public CSVFormat withIgnoreEmptyLines() {
        return this.withIgnoreEmptyLines(true);
    }

    /**
     * Sets the empty line skipping behavior of the format.
     *
     * @param ignoreEmptyLines
     *            the empty line skipping behavior, {@code true} to ignore the empty lines between the records,
     *            {@code false} to translate empty lines to empty records.
     * @return A new CSVFormat that is equal to this but with the specified empty line skipping behavior.
     */
    public CSVFormat withIgnoreEmptyLines(final boolean ignoreEmptyLines) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the trimming behavior of the format to {@code true}.
     *
     * @return A new CSVFormat that is equal to this but with the specified trimming behavior.
     * @see #withIgnoreSurroundingSpaces(boolean)
     * @since 1.1
     */
    public CSVFormat withIgnoreSurroundingSpaces() {
        return this.withIgnoreSurroundingSpaces(true);
    }

    /**
     * Sets the trimming behavior of the format.
     *
     * @param ignoreSurroundingSpaces
     *            the trimming behavior, {@code true} to remove the surrounding spaces, {@code false} to leave the
     *            spaces as is.
     * @return A new CSVFormat that is equal to this but with the specified trimming behavior.
     */
    public CSVFormat withIgnoreSurroundingSpaces(final boolean ignoreSurroundingSpaces) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the header ignore case behavior to {@code true}.
     *
     * @return A new CSVFormat that will ignore case header name.
     * @see #withIgnoreHeaderCase(boolean)
     * @since ?
     */
    public CSVFormat withIgnoreHeaderCase() {
        return this.withIgnoreHeaderCase(true);
    }

    /**
     * Sets if header names should be accessed ignoring case.
     *
     * @param ignoreHeaderCase
     *            the case mapping behavior, {@code true} to access name/values, {@code false} to leave the
     *            mapping as is.
     * @return A new CSVFormat that will ignore case header name if specified as {@code true}
     */
    public CSVFormat withIgnoreHeaderCase(final boolean ignoreHeaderCase) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Performs conversions to and from null for strings on input and output.
     * <ul>
     * <li>
     * <strong>Reading:</strong> Converts strings equal to the given {@code nullString} to {@code null} when reading
     * records.</li>
     * <li>
     * <strong>Writing:</strong> Writes {@code null} as the given {@code nullString} when writing records.</li>
     * </ul>
     *
     * @param nullString
     *            the String to convert to and from {@code null}. No substitution occurs if {@code null}
     *
     * @return A new CSVFormat that is equal to this but with the specified null conversion string.
     */
    public CSVFormat withNullString(final String nullString) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the quoteChar of the format to the specified character.
     *
     * @param quoteChar
     *            the quoteChar character
     * @return A new CSVFormat that is equal to this but with the specified character as quoteChar
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withQuote(final char quoteChar) {
        return withQuote(Character.valueOf(quoteChar));
    }

    /**
     * Sets the quoteChar of the format to the specified character.
     *
     * @param quoteChar
     *            the quoteChar character, use {@code null} to disable
     * @return A new CSVFormat that is equal to this but with the specified character as quoteChar
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withQuote(final Character quoteChar) {
        if (isLineBreak(quoteChar)) {
            throw new IllegalArgumentException("The quoteChar cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the output quote policy of the format to the specified value.
     *
     * @param quoteModePolicy
     *            the quote policy to use for output.
     *
     * @return A new CSVFormat that is equal to this but with the specified quote policy
     */
    public CSVFormat withQuoteMode(final QuoteMode quoteModePolicy) {
        return new CSVFormat(delimiter, quoteCharacter, quoteModePolicy, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets the record separator of the format to the specified character.
     *
     * <p>
     * <strong>Note:</strong> This setting is only used during printing and does not affect parsing. Parsing currently
     * only works for inputs with '\n', '\r' and "\r\n"
     * </p>
     *
     * @param recordSeparator
     *            the record separator to use for output.
     *
     * @return A new CSVFormat that is equal to this but with the the specified output record separator
     */
    public CSVFormat withRecordSeparator(final char recordSeparator) {
        return withRecordSeparator(String.valueOf(recordSeparator));
    }

    /**
     * Sets the record separator of the format to the specified String.
     *
     * <p>
     * <strong>Note:</strong> This setting is only used during printing and does not affect parsing. Parsing currently
     * only works for inputs with '\n', '\r' and "\r\n"
     * </p>
     *
     * @param recordSeparator
     *            the record separator to use for output.
     *
     * @return A new CSVFormat that is equal to this but with the the specified output record separator
     * @throws IllegalArgumentException
     *             if recordSeparator is none of CR, LF or CRLF
     */
    public CSVFormat withRecordSeparator(final String recordSeparator) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }

    /**
     * Sets skipping the header record to {@code true}.
     *
     * @return A new CSVFormat that is equal to this but with the the specified skipHeaderRecord setting.
     * @see #withSkipHeaderRecord(boolean)
     * @see #withHeader(String...)
     * @since 1.1
     */
    public CSVFormat withSkipHeaderRecord() {
        return this.withSkipHeaderRecord(true);
    }

    /**
     * Sets whether to skip the header record.
     *
     * @param skipHeaderRecord
     *            whether to skip the header record.
     *
     * @return A new CSVFormat that is equal to this but with the the specified skipHeaderRecord setting.
     * @see #withHeader(String...)
     */
    public CSVFormat withSkipHeaderRecord(final boolean skipHeaderRecord) {
        return new CSVFormat(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase);
    }
}

```

## src/main/java/org/apache/commons/csv/CSVPrinter.java

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

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Prints values in a CSV format.
 *
 * @version $Id$
 */
public final class CSVPrinter implements Flushable, Closeable {

    /** The place that the values get written. */
    private final Appendable out;
    private final CSVFormat format;

    /** True if we just began a new record. */
    private boolean newRecord = true;

    /**
     * Creates a printer that will print values to the given stream following the CSVFormat.
     * <p>
     * Currently, only a pure encapsulation format or a pure escaping format is supported. Hybrid formats (encapsulation
     * and escaping with a different character) are not supported.
     * </p>
     *
     * @param out
     *            stream to which to print. Must not be null.
     * @param format
     *            the CSV format. Must not be null.
     * @throws IOException
     *             thrown if the optional header cannot be printed.
     * @throws IllegalArgumentException
     *             thrown if the parameters of the format are inconsistent or if either out or format are null.
     */
    public CSVPrinter(final Appendable out, final CSVFormat format) throws IOException {
        Assertions.notNull(out, "out");
        Assertions.notNull(format, "format");

        this.out = out;
        this.format = format;
        // TODO: Is it a good idea to do this here instead of on the first call to a print method?
        // It seems a pain to have to track whether the header has already been printed or not.
        if (format.getHeaderComments() != null) {
            for (final String line : format.getHeaderComments()) {
                if (line != null) {
                    this.printComment(line);
                }
            }
        }
        if (format.getHeader() != null && !format.getSkipHeaderRecord()) {
            this.printRecord((Object[]) format.getHeader());
        }
    }

    // ======================================================
    // printing implementation
    // ======================================================

    @Override
    public void close() throws IOException {
        if (out instanceof Closeable) {
            ((Closeable) out).close();
        }
    }

    /**
     * Flushes the underlying stream.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        if (out instanceof Flushable) {
            ((Flushable) out).flush();
        }
    }

    /**
     * Gets the target Appendable.
     *
     * @return the target Appendable.
     */
    public Appendable getOut() {
        return this.out;
    }

    /**
     * Prints the string as the next value on the line. The value will be escaped or encapsulated as needed.
     *
     * @param value
     *            value to be output.
     * @throws IOException
     *             If an I/O error occurs
     */
    public void print(final Object value) throws IOException {
        // null values are considered empty
        String strValue;
        if (value == null) {
            final String nullString = format.getNullString();
            strValue = nullString == null ? Constants.EMPTY : nullString;
        } else {
            strValue = value.toString();
        }
        this.print(value, strValue, 0, strValue.length());
    }

    private void print(final Object object, final CharSequence value, final int offset, final int len)
            throws IOException {
        if (!newRecord) {
            out.append(format.getDelimiter());
        }
        if (object == null) {
            out.append(value);
        } else if (format.isQuoteCharacterSet()) {
            // the original object is needed so can check for Number
            printAndQuote(object, value, offset, len);
        } else if (format.isEscapeCharacterSet()) {
            printAndEscape(value, offset, len);
        } else {
            out.append(value, offset, offset + len);
        }
        newRecord = false;
    }

    /*
     * Note: must only be called if escaping is enabled, otherwise will generate NPE
     */
    private void printAndEscape(final CharSequence value, final int offset, final int len) throws IOException {
        int start = offset;
        int pos = offset;
        final int end = offset + len;

        final char delim = format.getDelimiter();
        final char escape = format.getEscapeCharacter().charValue();

        while (pos < end) {
            char c = value.charAt(pos);
            if (c == CR || c == LF || c == delim || c == escape) {
                // write out segment up until this char
                if (pos > start) {
                    out.append(value, start, pos);
                }
                if (c == LF) {
                    c = 'n';
                } else if (c == CR) {
                    c = 'r';
                }

                out.append(escape);
                out.append(c);

                start = pos + 1; // start on the current char after this one
            }

            pos++;
        }

        // write last segment
        if (pos > start) {
            out.append(value, start, pos);
        }
    }

    /*
     * Note: must only be called if quoting is enabled, otherwise will generate NPE
     */
    // the original object is needed so can check for Number
    private void printAndQuote(final Object object, final CharSequence value, final int offset, final int len)
            throws IOException {
        boolean quote = false;
        int start = offset;
        int pos = offset;
        final int end = offset + len;

        final char delimChar = format.getDelimiter();
        final char quoteChar = format.getQuoteCharacter().charValue();

        QuoteMode quoteModePolicy = format.getQuoteMode();
        if (quoteModePolicy == null) {
            quoteModePolicy = QuoteMode.MINIMAL;
        }
        switch (quoteModePolicy) {
        case ALL:
            quote = true;
            break;
        case NON_NUMERIC:
            quote = !(object instanceof Number);
            break;
        case NONE:
            // Use the existing escaping code
            printAndEscape(value, offset, len);
            return;
        case MINIMAL:
            if (len <= 0) {
                // always quote an empty token that is the first
                // on the line, as it may be the only thing on the
                // line. If it were not quoted in that case,
                // an empty line has no tokens.
                if (newRecord) {
                    quote = true;
                }
            } else {
                char c = value.charAt(pos);

                // TODO where did this rule come from?
                if (newRecord && (c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || (c > 'z'))) {
                    quote = true;
                } else if (c <= COMMENT) {
                    // Some other chars at the start of a value caused the parser to fail, so for now
                    // encapsulate if we start in anything less than '#'. We are being conservative
                    // by including the default comment char too.
                    quote = true;
                } else {
                    while (pos < end) {
                        c = value.charAt(pos);
                        if (c == LF || c == CR || c == quoteChar || c == delimChar) {
                            quote = true;
                            break;
                        }
                        pos++;
                    }

                    if (!quote) {
                        pos = end - 1;
                        c = value.charAt(pos);
                        // Some other chars at the end caused the parser to fail, so for now
                        // encapsulate if we end in anything less than ' '
                        if (c <= SP) {
                            quote = true;
                        }
                    }
                }
            }

            if (!quote) {
                // no encapsulation needed - write out the original value
                out.append(value, start, end);
                return;
            }
            break;
        default:
            throw new IllegalStateException("Unexpected Quote value: " + quoteModePolicy);
        }

        if (!quote) {
            // no encapsulation needed - write out the original value
            out.append(value, start, end);
            return;
        }

        // we hit something that needed encapsulation
        out.append(quoteChar);

        // Pick up where we left off: pos should be positioned on the first character that caused
        // the need for encapsulation.
        while (pos < end) {
            final char c = value.charAt(pos);
            if (c == quoteChar) {
                // write out the chunk up until this point

                // add 1 to the length to write out the encapsulator also
                out.append(value, start, pos + 1);
                // put the next starting position on the encapsulator so we will
                // write it out again with the next string (effectively doubling it)
                start = pos;
            }
            pos++;
        }

        // write the last segment
        out.append(value, start, pos);
        out.append(quoteChar);
    }

    /**
     * Prints a comment on a new line among the delimiter separated values.
     *
     * <p>
     * Comments will always begin on a new line and occupy a least one full line. The character specified to start
     * comments and a space will be inserted at the beginning of each new line in the comment.
     * </p>
     *
     * If comments are disabled in the current CSV format this method does nothing.
     *
     * @param comment
     *            the comment to output
     * @throws IOException
     *             If an I/O error occurs
     */
    public void printComment(final String comment) throws IOException {
        if (!format.isCommentMarkerSet()) {
            return;
        }
        if (!newRecord) {
            println();
        }
        out.append(format.getCommentMarker().charValue());
        out.append(SP);
        for (int i = 0; i < comment.length(); i++) {
            final char c = comment.charAt(i);
            switch (c) {
            case CR:
                if (i + 1 < comment.length() && comment.charAt(i + 1) == LF) {
                    i++;
                }
                //$FALL-THROUGH$ break intentionally excluded.
            case LF:
                println();
                out.append(format.getCommentMarker().charValue());
                out.append(SP);
                break;
            default:
                out.append(c);
                break;
            }
        }
        println();
    }

    /**
     * Outputs the record separator.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    public void println() throws IOException {
        final String recordSeparator = format.getRecordSeparator();
        if (recordSeparator != null) {
            out.append(recordSeparator);
        }
        newRecord = true;
    }

    /**
     * Prints the given values a single record of delimiter separated values followed by the record separator.
     *
     * <p>
     * The values will be quoted if needed. Quotes and newLine characters will be escaped. This method adds the record
     * separator to the output after printing the record, so there is no need to call {@link #println()}.
     * </p>
     *
     * @param values
     *            values to output.
     * @throws IOException
     *             If an I/O error occurs
     */
    public void printRecord(final Iterable<?> values) throws IOException {
        for (final Object value : values) {
            print(value);
        }
        println();
    }

    /**
     * Prints the given values a single record of delimiter separated values followed by the record separator.
     *
     * <p>
     * The values will be quoted if needed. Quotes and newLine characters will be escaped. This method adds the record
     * separator to the output after printing the record, so there is no need to call {@link #println()}.
     * </p>
     *
     * @param values
     *            values to output.
     * @throws IOException
     *             If an I/O error occurs
     */
    public void printRecord(final Object... values) throws IOException {
        for (final Object value : values) {
            print(value);
        }
        println();
    }

    /**
     * Prints all the objects in the given collection handling nested collections/arrays as records.
     *
     * <p>
     * If the given collection only contains simple objects, this method will print a single record like
     * {@link #printRecord(Iterable)}. If the given collections contains nested collections/arrays those nested elements
     * will each be printed as records using {@link #printRecord(Object...)}.
     * </p>
     *
     * <p>
     * Given the following data structure:
     * </p>
     *
     * <pre>
     * <code>
     * List&lt;String[]&gt; data = ...
     * data.add(new String[]{ "A", "B", "C" });
     * data.add(new String[]{ "1", "2", "3" });
     * data.add(new String[]{ "A1", "B2", "C3" });
     * </code>
     * </pre>
     *
     * <p>
     * Calling this method will print:
     * </p>
     *
     * <pre>
     * <code>
     * A, B, C
     * 1, 2, 3
     * A1, B2, C3
     * </code>
     * </pre>
     *
     * @param values
     *            the values to print.
     * @throws IOException
     *             If an I/O error occurs
     */
    public void printRecords(final Iterable<?> values) throws IOException {
        for (final Object value : values) {
            if (value instanceof Object[]) {
                this.printRecord((Object[]) value);
            } else if (value instanceof Iterable) {
                this.printRecord((Iterable<?>) value);
            } else {
                this.printRecord(value);
            }
        }
    }

    /**
     * Prints all the objects in the given array handling nested collections/arrays as records.
     *
     * <p>
     * If the given array only contains simple objects, this method will print a single record like
     * {@link #printRecord(Object...)}. If the given collections contains nested collections/arrays those nested
     * elements will each be printed as records using {@link #printRecord(Object...)}.
     * </p>
     *
     * <p>
     * Given the following data structure:
     * </p>
     *
     * <pre>
     * <code>
     * String[][] data = new String[3][]
     * data[0] = String[]{ "A", "B", "C" };
     * data[1] = new String[]{ "1", "2", "3" };
     * data[2] = new String[]{ "A1", "B2", "C3" };
     * </code>
     * </pre>
     *
     * <p>
     * Calling this method will print:
     * </p>
     *
     * <pre>
     * <code>
     * A, B, C
     * 1, 2, 3
     * A1, B2, C3
     * </code>
     * </pre>
     *
     * @param values
     *            the values to print.
     * @throws IOException
     *             If an I/O error occurs
     */
    public void printRecords(final Object... values) throws IOException {
        for (final Object value : values) {
            if (value instanceof Object[]) {
                this.printRecord((Object[]) value);
            } else if (value instanceof Iterable) {
                this.printRecord((Iterable<?>) value);
            } else {
                this.printRecord(value);
            }
        }
    }

    /**
     * Prints all the objects in the given JDBC result set.
     *
     * @param resultSet
     *            result set the values to print.
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException
     *             if a database access error occurs
     */
    public void printRecords(final ResultSet resultSet) throws SQLException, IOException {
        final int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                print(resultSet.getObject(i));
            }
            println();
        }
    }
}

```
