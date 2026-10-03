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
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>23</version>
  </parent>

  <artifactId>commons-csv</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>Commons CSV</name>
  <url>http://commons.apache.org/proper/csv/</url>
  <description>
The Commons CSV library provides a simple interface for reading and writing
CSV files of various types.
  </description>
  
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.10</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <developers>
    <developer>
      <id>bayard</id>
      <name>Henri Yandell</name>
      <email>bayard@apache.org</email>
      <organization>Apache</organization>
    </developer>
    <developer>
      <name>Martin van den Bemt</name>
      <id>mvdb</id>
      <email>mvdb@apache.org</email>
      <organization>Apache</organization>
    </developer>
    <developer>
      <name>Yonik Seeley</name>
      <id>yonik</id>
      <email>yonik@apache.org</email>
      <organization>Apache</organization>
    </developer>
  </developers>
  <contributors>
  </contributors>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/csv/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/csv/trunk</developerConnection>
    <url>http://svn.apache.org/repos/asf/commons/proper/csv/trunk</url>
  </scm>

  <distributionManagement>
    <site>
      <id>apache.website</id>
      <name>Apache Website</name>
      <url>${commons.deployment.protocol}://people.apache.org/www/commons.apache.org/csv/</url>
    </site>
  </distributionManagement>

  <properties>
    <commons.componentid>csv</commons.componentid>
    <commons.jira.id>CSV</commons.jira.id>
    <commons.jira.pid>12313222</commons.jira.pid>
    <maven.compile.source>1.5</maven.compile.source>
    <maven.compile.target>1.5</maven.compile.target>
  </properties> 

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changes-plugin</artifactId>
        <version>2.0</version>
        <configuration>
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
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.1</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

</project>

```

## src/main/java/org/apache/commons/csv/ExtendedBufferedReader.java

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * ExtendedBufferedReader
 *
 * A special reader decorator which supports more
 * sophisticated access to the underlying reader object.
 *
 * In particular the reader supports a look-ahead option,
 * which allows you to see the next char returned by
 * next().
 */
class ExtendedBufferedReader extends BufferedReader {

    /** The end of stream symbol */
    static final int END_OF_STREAM = -1;

    /** Undefined state for the lookahead char */
    static final int UNDEFINED = -2;

    /** The last char returned */
    private int lastChar = UNDEFINED;

    /** The line counter */
    private int lineCounter = 0;

    /**
     * Created extended buffered reader using default buffer-size
     */
    ExtendedBufferedReader(Reader r) {
        super(r);
    }

    @Override
    public int read() throws IOException {
        int current = super.read();
        if (current == '\r' || (current == '\n' && lastChar != '\r')) {
            lineCounter++;
        }
        lastChar = current;
        return lastChar;
    }

    /**
     * Returns the last character that was read as an integer (0 to 65535). This
     * will be the last character returned by any of the read methods. This will
     * not include a character read using the {@link #peek()} method. If no
     * character has been read then this will return {@link #UNDEFINED}. If the
     * end of the stream was reached on the last read then this will return
     * {@link #END_OF_STREAM}.
     * 
     * @return the last character that was read
     */
    int readAgain() {
        return lastChar;
    }

    @Override
    public int read(char[] buf, int offset, int length) throws IOException {
        if (length == 0) {
            return 0;
        }
        
        int len = super.read(buf, offset, length);
        
        if (len > 0) {

            for (int i = offset; i < offset + len; i++) {
                char ch = buf[i];
                if (ch == '\n') {
                    if ('\r' != (i > 0 ? buf[i-1]: lastChar)) {
                        lineCounter++;                        
                    }
                } else if (ch == '\r') {
                    lineCounter++;
                }
            }

            lastChar = buf[offset + len - 1];

        } else if (len == -1) {
            lastChar = END_OF_STREAM;
        }
        
        return len;
    }

    /**
     * Calls {@link BufferedReader#readLine()} which drops the line terminator(s).
     * This method should only be called when processing a comment, otherwise
     * information can be lost.
     * <p>
     * Increments  {@link #lineCounter}
     * <p>
     * Sets {@link #lastChar} to {@link #END_OF_STREAM} at EOF, 
     * otherwise to last character on the line (won't be CR or LF) 
     * 
     * @return the line that was read, or null if reached EOF.
     */
    @Override
    public String readLine() throws IOException {
        String line = super.readLine();

        if (line != null) {
            if (line.length() > 0) {
                lastChar = line.charAt(line.length() - 1);
            }
            lineCounter++;
        } else {
            lastChar = END_OF_STREAM;
        }

        return line;
    }

    /**
     * Returns the next character in the current reader without consuming it. So
     * the next call to {@link #read()} will still return this value.
     * 
     * @return the next character
     * 
     * @throws IOException if there is an error in reading
     */
    int lookAhead() throws IOException {
        super.mark(1);
        int c = super.read();
        super.reset();

        return c;
    }

    /**
     * Returns the nof line read
     *
     * @return the current-line-number (or -1)
     */
    int getLineNumber() {
        return lineCounter;
    }
}

```
