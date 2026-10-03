## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="commons-compress" default="package" basedir=".">

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

  <target name="compile" depends="commons-compress-from-maven.compile"> </target>
  <target name="compile.tests" depends="commons-compress-from-maven.compile-tests"> </target>

</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
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
  <artifactId>commons-compress</artifactId>
  <version>1.12-SNAPSHOT</version>
  <name>Apache Commons Compress</name>
  <url>http://commons.apache.org/proper/commons-compress/</url>
  <!-- The description is not indented to make it look better in the release notes -->
  <description>
Apache Commons Compress software defines an API for working with
compression and archive formats.  These include: bzip2, gzip, pack200,
lzma, xz, Snappy, traditional Unix Compress, DEFLATE and ar, cpio,
jar, tar, zip, dump, 7z, arj.
  </description>

  <properties>
    <maven.compiler.source>1.6</maven.compiler.source>
    <maven.compiler.target>1.6</maven.compiler.target>
    <commons.componentid>compress</commons.componentid>
    <commons.jira.id>COMPRESS</commons.jira.id>
    <commons.jira.pid>12310904</commons.jira.pid>
    <!-- configuration bits for cutting a release candidate -->
    <commons.release.version>${project.version}</commons.release.version>
    <commons.rc.version>RC1</commons.rc.version>
    <powermock.version>1.6.4</powermock.version>
    <commons.pmd-plugin.version>3.6</commons.pmd-plugin.version>
  </properties>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/COMPRESS</url>
  </issueManagement>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.tukaani</groupId>
      <artifactId>xz</artifactId>
      <version>1.5</version>
      <optional>true</optional>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>${powermock.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>${powermock.version}</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <developers>
    <developer>
      <name>Torsten Curdt</name>
      <id>tcurdt</id>
      <email>tcurdt at apache.org</email>
    </developer>
    <developer>
      <name>Stefan Bodewig</name>
      <id>bodewig</id>
      <email>bodewig at apache.org</email>
    </developer>
    <developer>
      <name>Sebastian Bazley</name>
      <id>sebb</id>
      <email>sebb at apache.org</email>
    </developer>
    <developer>
      <name>Christian Grobmeier</name>
      <id>grobmeier</id>
      <email>grobmeier at apache.org</email>
    </developer>
    <developer>
      <name>Julius Davies</name>
      <id>julius</id>
      <email>julius at apache.org</email>
    </developer>
    <developer>
      <name>Damjan Jovanovic</name>
      <id>damjan</id>
      <email>damjan at apache.org</email>
    </developer>
    <developer>
      <name>Emmanuel Bourg</name>
      <id>ebourg</id>
      <email>ebourg at apache.org</email>
    </developer>
    <developer>
      <name>Gary Gregory</name>
      <id>ggregory</id>
      <email>ggregory at apache.org</email>
    </developer>
  </developers>

  <contributors>
    <contributor>
      <name>Wolfgang Glas</name>
      <email>wolfgang.glas at ev-i.at</email>
    </contributor>
    <contributor>
      <name>Christian Kohlschütte</name>
      <email>ck@newsclub.de</email>
    </contributor>
    <contributor>
      <name>Bear Giles</name>
      <email>bgiles@coyotesong.com</email>
    </contributor>
    <contributor>
      <name>Michael Kuss</name>
      <email>mail at michael minus kuss.de</email>
    </contributor>
    <contributor>
      <name>Lasse Collin</name>
      <email>lasse.collin@tukaani.org</email>
    </contributor>
    <contributor>
      <name>John Kodis</name>
    </contributor>
    <contributor>
      <name>BELUGA BEHR</name>
    </contributor>
  </contributors>

  <scm>
    <connection>scm:git:http://git-wip-us.apache.org/repos/asf/commons-compress.git</connection>
    <developerConnection>scm:git:https://git-wip-us.apache.org/repos/asf/commons-compress.git</developerConnection>
    <url>https://git-wip-us.apache.org/repos/asf?p=commons-compress.git</url>
  </scm>

  <build>
    <pluginManagement>
      <plugins>
        <!-- Override Javadoc config in parent pom to add JCIP tags -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
          <configuration> 
            <quiet>true</quiet>
            <source>${maven.compiler.source}</source>
            <encoding>${commons.encoding}</encoding>
            <docEncoding>${commons.docEncoding}</docEncoding>
            <linksource>true</linksource>
            <links>
              <link>${commons.javadoc.java.link}</link>
              <link>${commons.javadoc.javaee.link}</link>
            </links>
            <tags>
              <tag>
                <name>Immutable</name>
                <placement>a</placement>
                <head>This class is immutable</head>
              </tag>
              <tag>
                <name>NotThreadSafe</name>
                <placement>a</placement>
                <head>This class is not thread-safe</head>
              </tag>
              <tag>
                <name>ThreadSafe</name>
                <placement>a</placement>
                <head>This class is thread-safe</head>
              </tag>
            </tags>
          </configuration> 
        </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <!-- create the source and binary assemblies -->
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
        <artifactId>maven-jar-plugin</artifactId>
        <configuration>
          <archive>
            <manifestEntries>
              <Main-Class>org.apache.commons.compress.archivers.Lister</Main-Class>
              <Extension-Name>org.apache.commons.compress</Extension-Name>
            </manifestEntries>
          </archive>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.felix</groupId>
        <artifactId>maven-bundle-plugin</artifactId>
        <configuration>
          <instructions>
            <Import-Package>org.tukaani.xz;resolution:=optional</Import-Package>
          </instructions>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-scm-publish-plugin</artifactId>
        <configuration>
          <ignorePathsToDelete>
            <ignorePathToDelete>javadocs</ignorePathToDelete>
          </ignorePathsToDelete>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>${commons.pmd-plugin.version}</version>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <!-- generate a code coverage report -->
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.7</version>
      </plugin>
      <plugin>
        <!-- generate the PMD reports -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>${commons.pmd-plugin.version}</version>
        <configuration>
          <minimumTokens>200</minimumTokens>
          <targetJdk>${maven.compiler.source}</targetJdk>
          <rulesets>
            <ruleset>${basedir}/pmd-ruleset.xml</ruleset>
          </rulesets>
        </configuration>
      </plugin>
      <!-- Override Javadoc config in parent pom to add JCIP tags -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration> 
          <quiet>true</quiet>
          <source>${maven.compiler.source}</source>
          <encoding>${commons.encoding}</encoding>
          <docEncoding>${commons.docEncoding}</docEncoding>
          <linksource>true</linksource>
          <links>
            <link>${commons.javadoc.java.link}</link>
            <link>${commons.javadoc.javaee.link}</link>
          </links>
          <tags>
            <tag>
              <name>Immutable</name>
              <placement>a</placement>
              <head>This class is immutable</head>
            </tag>
            <tag>
              <name>NotThreadSafe</name>
              <placement>a</placement>
              <head>This class is not thread-safe</head>
            </tag>
            <tag>
              <name>ThreadSafe</name>
              <placement>a</placement>
              <head>This class is thread-safe</head>
            </tag>
          </tags>
        </configuration> 
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>3.0.3</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.rat</groupId>
        <artifactId>apache-rat-plugin</artifactId>
        <version>${commons.rat.version}</version>
        <configuration>
          <excludes>
            <!-- files used during tests -->
            <exclude>src/test/resources/**</exclude>
            <exclude>.pmd</exclude>
            <exclude>.gitignore</exclude>
            <exclude>.gitattributes</exclude>
            <exclude>.projectile</exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

  <profiles>
    <!-- Add long running tests as **/*IT.java -->
    <profile>
      <id>run-zipit</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <executions>
              <execution>
                <phase>process-test-resources</phase>
                <configuration>
                  <target>
                    <untar src="${basedir}/src/test/resources/zip64support.tar.bz2"
                           dest="${project.build.testOutputDirectory}"
                           compression="bzip2"/>
                  </target>
                </configuration>
                <goals>
                  <goal>run</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
          <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/zip/*IT.java</include>
              </includes>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
    <profile>
      <id>run-tarit</id>
      <build>
        <plugins>
          <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/tar/*IT.java</include>
              </includes>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>

</project>

```

## src/main/java/org/apache/commons/compress/utils/ArchiveUtils.java

```
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.commons.compress.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;

/**
 * Generic Archive utilities
 */
public class ArchiveUtils {

    private static final int MAX_SANITIZED_NAME_LENGTH = 255;

    /** Private constructor to prevent instantiation of this utility class. */
    private ArchiveUtils(){
    }

    /**
     * Generates a string containing the name, isDirectory setting and size of an entry.
     * <p>
     * For example:
     * <pre>
     * -    2000 main.c
     * d     100 testfiles
     * </pre>
     * 
     * @param entry the entry
     * @return the representation of the entry
     */
    public static String toString(final ArchiveEntry entry){
        final StringBuilder sb = new StringBuilder();
        sb.append(entry.isDirectory()? 'd' : '-');// c.f. "ls -l" output
        final String size = Long.toString(entry.getSize());
        sb.append(' ');
        // Pad output to 7 places, leading spaces
        for(int i=7; i > size.length(); i--){
            sb.append(' ');
        }
        sb.append(size);
        sb.append(' ').append(entry.getName());
        return sb.toString();
    }

    /**
     * Check if buffer contents matches Ascii String.
     * 
     * @param expected expected string
     * @param buffer the buffer
     * @param offset offset to read from
     * @param length length of the buffer
     * @return {@code true} if buffer is the same as the expected string
     */
    public static boolean matchAsciiBuffer(
            final String expected, final byte[] buffer, final int offset, final int length){
        byte[] buffer1;
        try {
            buffer1 = expected.getBytes(CharsetNames.US_ASCII);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e); // Should not happen
        }
        return isEqual(buffer1, 0, buffer1.length, buffer, offset, length, false);
    }

    /**
     * Check if buffer contents matches Ascii String.
     * 
     * @param expected the expected strin
     * @param buffer the buffer
     * @return {@code true} if buffer is the same as the expected string
     */
    public static boolean matchAsciiBuffer(final String expected, final byte[] buffer){
        return matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    /**
     * Convert a string to Ascii bytes.
     * Used for comparing "magic" strings which need to be independent of the default Locale.
     * 
     * @param inputString string to convert
     * @return the bytes
     */
    public static byte[] toAsciiBytes(final String inputString){
        try {
            return inputString.getBytes(CharsetNames.US_ASCII);
        } catch (final UnsupportedEncodingException e) {
           throw new RuntimeException(e); // Should never happen
        }
    }

    /**
     * Convert an input byte array to a String using the ASCII character set.
     * 
     * @param inputBytes bytes to convert
     * @return the bytes, interpreted as an Ascii string
     */
    public static String toAsciiString(final byte[] inputBytes){
        try {
            return new String(inputBytes, CharsetNames.US_ASCII);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    /**
     * Convert an input byte array to a String using the ASCII character set.
     * 
     * @param inputBytes input byte array
     * @param offset offset within array
     * @param length length of array
     * @return the bytes, interpreted as an Ascii string
     */
    public static String toAsciiString(final byte[] inputBytes, final int offset, final int length){
        try {
            return new String(inputBytes, offset, length, CharsetNames.US_ASCII);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e); // Should never happen
        }
    }

    /**
     * Compare byte buffers, optionally ignoring trailing nulls
     * 
     * @param buffer1 first buffer
     * @param offset1 first offset
     * @param length1 first length
     * @param buffer2 second buffer
     * @param offset2 second offset
     * @param length2 second length
     * @param ignoreTrailingNulls whether to ignore trailing nulls
     * @return {@code true} if buffer1 and buffer2 have same contents, having regard to trailing nulls
     */
    public static boolean isEqual(
            final byte[] buffer1, final int offset1, final int length1,
            final byte[] buffer2, final int offset2, final int length2,
            final boolean ignoreTrailingNulls){
        final int minLen=length1 < length2 ? length1 : length2;
        for (int i=0; i < minLen; i++){
            if (buffer1[offset1+i] != buffer2[offset2+i]){
                return false;
            }
        }
        if (length1 == length2){
            return true;
        }
        if (ignoreTrailingNulls){
            if (length1 > length2){
                for(int i = length2; i < length1; i++){
                    if (buffer1[offset1+i] != 0){
                        return false;
                    }
                }
            } else {
                for(int i = length1; i < length2; i++){
                    if (buffer2[offset2+i] != 0){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Compare byte buffers
     * 
     * @param buffer1 the first buffer
     * @param offset1 the first offset
     * @param length1 the first length
     * @param buffer2 the second buffer
     * @param offset2 the second offset
     * @param length2 the second length
     * @return {@code true} if buffer1 and buffer2 have same contents
     */
    public static boolean isEqual(
            final byte[] buffer1, final int offset1, final int length1,
            final byte[] buffer2, final int offset2, final int length2){
        return isEqual(buffer1, offset1, length1, buffer2, offset2, length2, false);
    }

    /**
     * Compare byte buffers
     * 
     * @param buffer1 the first buffer
     * @param buffer2 the second buffer
     * @return {@code true} if buffer1 and buffer2 have same contents
     */
    public static boolean isEqual(final byte[] buffer1, final byte[] buffer2 ){
        return isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
    }

    /**
     * Compare byte buffers, optionally ignoring trailing nulls
     * 
     * @param buffer1 the first buffer
     * @param buffer2 the second buffer 
     * @param ignoreTrailingNulls whether to ignore tariling nulls
     * @return {@code true} if buffer1 and buffer2 have same contents
     */
    public static boolean isEqual(final byte[] buffer1, final byte[] buffer2, final boolean ignoreTrailingNulls){
        return isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, ignoreTrailingNulls);
    }

    /**
     * Compare byte buffers, ignoring trailing nulls
     * 
     * @param buffer1 the first buffer
     * @param offset1 the first offset
     * @param length1 the first length
     * @param buffer2 the second buffer
     * @param offset2 the second offset
     * @param length2 the second length
     * @return {@code true} if buffer1 and buffer2 have same contents, having regard to trailing nulls
     */
    public static boolean isEqualWithNull(
            final byte[] buffer1, final int offset1, final int length1,
            final byte[] buffer2, final int offset2, final int length2){
        return isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true);
    }
    
    /**
     * Returns true if the first N bytes of an array are all zero
     * 
     * @param a
     *            The array to check
     * @param size
     *            The number of characters to check (not the size of the array)
     * @return true if the first N bytes are zero
     */
    public static boolean isArrayZero(final byte[] a, final int size) {
        for (int i = 0; i < size; i++) {
            if (a[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a "sanitized" version of the string given as arguments,
     * where sanitized means non-printable characters have been
     * replaced with a question mark and the outcome is not longer
     * than 255 chars.
     *
     * <p>This method is used to clean up file names when they are
     * used in exception messages as they may end up in log files or
     * as console output and may have been read from a corrupted
     * input.</p>
     *
     * @param s the string to sanitize
     * @return a sanitized version of the argument
     * @since Compress 1.12
     */
    public static String sanitize(String s) {
        final char[] cs = s.toCharArray();
        final char[] chars = cs.length <= MAX_SANITIZED_NAME_LENGTH ? cs : Arrays.copyOf(cs, MAX_SANITIZED_NAME_LENGTH);
        if (cs.length > MAX_SANITIZED_NAME_LENGTH) {
            for (int i = MAX_SANITIZED_NAME_LENGTH - 3; i < MAX_SANITIZED_NAME_LENGTH; i++) {
                chars[i] = '.';
            }
        }
        final int len = chars.length;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            final char c = chars[i];
            if (!Character.isISOControl(c)) {
                Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
                if (block != null && block != Character.UnicodeBlock.SPECIALS) {
                    sb.append(c);
                    continue;
                }
            }
            sb.append('?');
        }
        return sb.toString();
    }

}

```
