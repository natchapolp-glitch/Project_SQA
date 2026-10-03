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
    <version>27</version>
  </parent>

  <groupId>org.apache.commons</groupId>
  <artifactId>commons-compress</artifactId>
  <version>1.5-SNAPSHOT</version>
  <name>Commons Compress</name>
  <url>http://commons.apache.org/compress/</url>
  <!-- The description is not indented to make it look better in the release notes -->
  <description>
Apache Commons Compress software defines an API for working with compression and archive formats.
These include: bzip2, gzip, pack200, xz and ar, cpio, jar, tar, zip, dump.
  </description>

  <properties>
    <maven.compile.source>1.5</maven.compile.source>
    <maven.compile.target>1.5</maven.compile.target>
    <commons.componentid>compress</commons.componentid>
    <commons.jira.id>COMPRESS</commons.jira.id>
    <commons.jira.pid>12310904</commons.jira.pid>
    <!-- configuration bits for cutting a release candidate -->
    <commons.release.version>1.4.1</commons.release.version>
    <commons.rc.version>RC1</commons.rc.version>
  </properties>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/COMPRESS</url>
  </issueManagement>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.10</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.tukaani</groupId>
      <artifactId>xz</artifactId>
      <version>1.0</version>
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
  </contributors>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/compress/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/compress/trunk</developerConnection>
    <url>http://svn.apache.org/repos/asf/commons/proper/compress/trunk</url>
  </scm>

  <distributionManagement>
    <site>
      <id>website</id>
      <name>Apache Website</name>
      <url>scp://people.apache.org/www/commons.apache.org/compress/</url>
    </site>
  </distributionManagement>

  <build>
    <plugins>
      <plugin>
        <!-- create the source and binary assemblies -->
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptors>
            <descriptor>src/main/assembly/bin.xml</descriptor>
            <descriptor>src/main/assembly/src.xml</descriptor>
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
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <!-- generate the changes report from changes.xml and link to JIRA -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changes-plugin</artifactId>
        <version>${commons.changes.version}</version>
        <configuration>
           <issueLinkTemplatePerSystem>
               <default>%URL%/%ISSUE%</default>
           </issueLinkTemplatePerSystem>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>changes-report</report>
              <report>jira-report</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <!-- generate a code coverage report -->
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.5.1</version>
      </plugin>
      <plugin>
        <!-- generate the PMD reports -->
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.5</version>
        <configuration>
          <minimumTokens>200</minimumTokens>
          <targetJdk>${maven.compile.source}</targetJdk>
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
          <source>${maven.compile.source}</source>
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
        <version>2.3.2</version>
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
            <!-- proposal text without license -->
            <exclude>PROPOSAL.txt</exclude>
            <exclude>.pmd</exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

  <profiles>
    <profile>
      <id>rc</id>
      <distributionManagement>
        <!-- Cannot define in parent ATM, see COMMONSSITE-26 -->
        <site>
          <id>apache.website</id>
          <name>Apache Commons Release Candidate Staging Site</name>
          <url>${commons.deployment.protocol}://people.apache.org/www/people.apache.org/builds/commons/${commons.componentid}/${commons.release.version}/${commons.rc.version}/site</url>
        </site>
      </distributionManagement>
    </profile>
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

## src/main/java/org/apache/commons/compress/archivers/tar/TarArchiveOutputStream.java

```
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
package org.apache.commons.compress.archivers.tar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.compress.utils.CountingOutputStream;

/**
 * The TarOutputStream writes a UNIX tar archive as an OutputStream.
 * Methods are provided to put entries, and then write their contents
 * by writing to this stream using write().
 * @NotThreadSafe
 */
public class TarArchiveOutputStream extends ArchiveOutputStream {
    /** Fail if a long file name is required in the archive. */
    public static final int LONGFILE_ERROR = 0;

    /** Long paths will be truncated in the archive. */
    public static final int LONGFILE_TRUNCATE = 1;

    /** GNU tar extensions are used to store long file names in the archive. */
    public static final int LONGFILE_GNU = 2;

    /** POSIX/PAX extensions are used to store long file names in the archive. */
    public static final int LONGFILE_POSIX = 3;

    /** Fail if a big number (e.g. size &gt; 8GiB) is required in the archive. */
    public static final int BIGNUMBER_ERROR = 0;

    /** star/GNU tar/BSD tar extensions are used to store big number in the archive. */
    public static final int BIGNUMBER_STAR = 1;

    /** POSIX/PAX extensions are used to store big numbers in the archive. */
    public static final int BIGNUMBER_POSIX = 2;

    private long      currSize;
    private String    currName;
    private long      currBytes;
    private final byte[]    recordBuf;
    private int       assemLen;
    private final byte[]    assemBuf;
    protected final TarBuffer buffer;
    private int       longFileMode = LONGFILE_ERROR;
    private int       bigNumberMode = BIGNUMBER_ERROR;

    private boolean closed = false;

    /** Indicates if putArchiveEntry has been called without closeArchiveEntry */
    private boolean haveUnclosedEntry = false;

    /** indicates if this archive is finished */
    private boolean finished = false;

    private final OutputStream out;

    private final ZipEncoding encoding;

    private boolean addPaxHeadersForNonAsciiNames = false;
    private static final ZipEncoding ASCII =
        ZipEncodingHelper.getZipEncoding("ASCII");

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     */
    public TarArchiveOutputStream(OutputStream os) {
        this(os, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param encoding name of the encoding to use for file names
     * @since Commons Compress 1.4
     */
    public TarArchiveOutputStream(OutputStream os, String encoding) {
        this(os, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE, encoding);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param blockSize the block size to use
     */
    public TarArchiveOutputStream(OutputStream os, int blockSize) {
        this(os, blockSize, TarBuffer.DEFAULT_RCDSIZE);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param blockSize the block size to use
     * @param encoding name of the encoding to use for file names
     * @since Commons Compress 1.4
     */
    public TarArchiveOutputStream(OutputStream os, int blockSize,
                                  String encoding) {
        this(os, blockSize, TarBuffer.DEFAULT_RCDSIZE, encoding);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param blockSize the block size to use
     * @param recordSize the record size to use
     */
    public TarArchiveOutputStream(OutputStream os, int blockSize, int recordSize) {
        this(os, blockSize, recordSize, null);
    }

    /**
     * Constructor for TarInputStream.
     * @param os the output stream to use
     * @param blockSize the block size to use
     * @param recordSize the record size to use
     * @param encoding name of the encoding to use for file names
     * @since Commons Compress 1.4
     */
    public TarArchiveOutputStream(OutputStream os, int blockSize,
                                  int recordSize, String encoding) {
        out = new CountingOutputStream(os);
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);

        this.buffer = new TarBuffer(out, blockSize, recordSize);
        this.assemLen = 0;
        this.assemBuf = new byte[recordSize];
        this.recordBuf = new byte[recordSize];
    }

    /**
     * Set the long file mode.
     * This can be LONGFILE_ERROR(0), LONGFILE_TRUNCATE(1) or LONGFILE_GNU(2).
     * This specifies the treatment of long file names (names >= TarConstants.NAMELEN).
     * Default is LONGFILE_ERROR.
     * @param longFileMode the mode to use
     */
    public void setLongFileMode(int longFileMode) {
        this.longFileMode = longFileMode;
    }

    /**
     * Set the big number mode.
     * This can be BIGNUMBER_ERROR(0), BIGNUMBER_POSIX(1) or BIGNUMBER_STAR(2).
     * This specifies the treatment of big files (sizes &gt; TarConstants.MAXSIZE) and other numeric values to big to fit into a traditional tar header.
     * Default is BIGNUMBER_ERROR.
     * @param bigNumberMode the mode to use
     * @since 1.4
     */
    public void setBigNumberMode(int bigNumberMode) {
        this.bigNumberMode = bigNumberMode;
    }

    /**
     * Whether to add a PAX extension header for non-ASCII file names.
     * @since 1.4
     */
    public void setAddPaxHeadersForNonAsciiNames(boolean b) {
        addPaxHeadersForNonAsciiNames = b;
    }

    @Deprecated
    @Override
    public int getCount() {
        return (int) getBytesWritten();
    }

    @Override
    public long getBytesWritten() {
        return ((CountingOutputStream) out).getBytesWritten();
    }

    /**
     * Ends the TAR archive without closing the underlying OutputStream.
     * 
     * An archive consists of a series of file entries terminated by an
     * end-of-archive entry, which consists of two 512 blocks of zero bytes. 
     * POSIX.1 requires two EOF records, like some other implementations.
     * 
     * @throws IOException on error
     */
    @Override
    public void finish() throws IOException {
        if (finished) {
            throw new IOException("This archive has already been finished");
        }

        if (haveUnclosedEntry) {
            throw new IOException("This archives contains unclosed entries.");
        }
        writeEOFRecord();
        writeEOFRecord();
        buffer.flushBlock();
        finished = true;
    }

    /**
     * Closes the underlying OutputStream.
     * @throws IOException on error
     */
    @Override
    public void close() throws IOException {
        if(!finished) {
            finish();
        }

        if (!closed) {
            buffer.close();
            out.close();
            closed = true;
        }
    }

    /**
     * Get the record size being used by this stream's TarBuffer.
     *
     * @return The TarBuffer record size.
     */
    public int getRecordSize() {
        return buffer.getRecordSize();
    }

    /**
     * Put an entry on the output stream. This writes the entry's
     * header record and positions the output stream for writing
     * the contents of the entry. Once this method is called, the
     * stream is ready for calls to write() to write the entry's
     * contents. Once the contents are written, closeArchiveEntry()
     * <B>MUST</B> be called to ensure that all buffered data
     * is completely written to the output stream.
     *
     * @param archiveEntry The TarEntry to be written to the archive.
     * @throws IOException on error
     * @throws ClassCastException if archiveEntry is not an instance of TarArchiveEntry
     */
    @Override
    public void putArchiveEntry(ArchiveEntry archiveEntry) throws IOException {
        if(finished) {
            throw new IOException("Stream has already been finished");
        }
        TarArchiveEntry entry = (TarArchiveEntry) archiveEntry;
        Map<String, String> paxHeaders = new HashMap<String, String>();
        final String entryName = entry.getName();
        final ByteBuffer encodedName = encoding.encode(entryName);
        final int nameLen = encodedName.limit() - encodedName.position();
        boolean paxHeaderContainsPath = false;
        if (nameLen >= TarConstants.NAMELEN) {

            if (longFileMode == LONGFILE_POSIX) {
                paxHeaders.put("path", entryName);
                paxHeaderContainsPath = true;
            } else if (longFileMode == LONGFILE_GNU) {
                // create a TarEntry for the LongLink, the contents
                // of which are the entry's name
                TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK,
                                                                    TarConstants.LF_GNUTYPE_LONGNAME);

                longLinkEntry.setSize(nameLen + 1); // +1 for NUL
                putArchiveEntry(longLinkEntry);
                write(encodedName.array(), encodedName.arrayOffset(), nameLen);
                write(0); // NUL terminator
                closeArchiveEntry();
            } else if (longFileMode != LONGFILE_TRUNCATE) {
                throw new RuntimeException("file name '" + entryName
                                           + "' is too long ( > "
                                           + TarConstants.NAMELEN + " bytes)");
            }
        }

        if (bigNumberMode == BIGNUMBER_POSIX) {
            addPaxHeadersForBigNumbers(paxHeaders, entry);
        } else if (bigNumberMode != BIGNUMBER_STAR) {
            failForBigNumbers(entry);
        }

        if (addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath
            && !ASCII.canEncode(entryName)) {
            paxHeaders.put("path", entryName);
        }

        if (addPaxHeadersForNonAsciiNames
            && (entry.isLink() || entry.isSymbolicLink())
            && !ASCII.canEncode(entry.getLinkName())) {
            paxHeaders.put("linkpath", entry.getLinkName());
        }

        if (paxHeaders.size() > 0) {
            writePaxHeaders(entryName, paxHeaders);
        }

        entry.writeEntryHeader(recordBuf, encoding,
                               bigNumberMode == BIGNUMBER_STAR);
        buffer.writeRecord(recordBuf);

        currBytes = 0;

        if (entry.isDirectory()) {
            currSize = 0;
        } else {
            currSize = entry.getSize();
        }
        currName = entryName;
        haveUnclosedEntry = true;
    }

    /**
     * Close an entry. This method MUST be called for all file
     * entries that contain data. The reason is that we must
     * buffer data written to the stream in order to satisfy
     * the buffer's record based writes. Thus, there may be
     * data fragments still being assembled that must be written
     * to the output stream before this entry is closed and the
     * next entry written.
     * @throws IOException on error
     */
    @Override
    public void closeArchiveEntry() throws IOException {
        if (finished) {
            throw new IOException("Stream has already been finished");
        }
        if (!haveUnclosedEntry){
            throw new IOException("No current entry to close");
        }
        if (assemLen > 0) {
            for (int i = assemLen; i < assemBuf.length; ++i) {
                assemBuf[i] = 0;
            }

            buffer.writeRecord(assemBuf);

            currBytes += assemLen;
            assemLen = 0;
        }

        if (currBytes < currSize) {
            throw new IOException("entry '" + currName + "' closed at '"
                                  + currBytes
                                  + "' before the '" + currSize
                                  + "' bytes specified in the header were written");
        }
        haveUnclosedEntry = false;
    }

    /**
     * Writes bytes to the current tar archive entry. This method
     * is aware of the current entry and will throw an exception if
     * you attempt to write bytes past the length specified for the
     * current entry. The method is also (painfully) aware of the
     * record buffering required by TarBuffer, and manages buffers
     * that are not a multiple of recordsize in length, including
     * assembling records from small buffers.
     *
     * @param wBuf The buffer to write to the archive.
     * @param wOffset The offset in the buffer from which to get bytes.
     * @param numToWrite The number of bytes to write.
     * @throws IOException on error
     */
    @Override
    public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
        if ((currBytes + numToWrite) > currSize) {
            throw new IOException("request to write '" + numToWrite
                                  + "' bytes exceeds size in header of '"
                                  + currSize + "' bytes for entry '"
                                  + currName + "'");

            //
            // We have to deal with assembly!!!
            // The programmer can be writing little 32 byte chunks for all
            // we know, and we must assemble complete records for writing.
            // REVIEW Maybe this should be in TarBuffer? Could that help to
            // eliminate some of the buffer copying.
            //
        }

        if (assemLen > 0) {
            if ((assemLen + numToWrite) >= recordBuf.length) {
                int aLen = recordBuf.length - assemLen;

                System.arraycopy(assemBuf, 0, recordBuf, 0,
                                 assemLen);
                System.arraycopy(wBuf, wOffset, recordBuf,
                                 assemLen, aLen);
                buffer.writeRecord(recordBuf);

                currBytes += recordBuf.length;
                wOffset += aLen;
                numToWrite -= aLen;
                assemLen = 0;
            } else {
                System.arraycopy(wBuf, wOffset, assemBuf, assemLen,
                                 numToWrite);

                wOffset += numToWrite;
                assemLen += numToWrite;
                numToWrite = 0;
            }
        }

        //
        // When we get here we have EITHER:
        // o An empty "assemble" buffer.
        // o No bytes to write (numToWrite == 0)
        //
        while (numToWrite > 0) {
            if (numToWrite < recordBuf.length) {
                System.arraycopy(wBuf, wOffset, assemBuf, assemLen,
                                 numToWrite);

                assemLen += numToWrite;

                break;
            }

            buffer.writeRecord(wBuf, wOffset);

            int num = recordBuf.length;

            currBytes += num;
            numToWrite -= num;
            wOffset += num;
        }
    }

    /**
     * Writes a PAX extended header with the given map as contents.
     * @since 1.4
     */
    void writePaxHeaders(String entryName,
                         Map<String, String> headers) throws IOException {
        String name = "./PaxHeaders.X/" + stripTo7Bits(entryName);
        while (name.endsWith("/")) {
            // TarEntry's constructor would think this is a directory
            // and not allow any data to be written
            name = name.substring(0, name.length() - 1);
        }
        if (name.length() >= TarConstants.NAMELEN) {
            name = name.substring(0, TarConstants.NAMELEN - 1);
        }
        TarArchiveEntry pex = new TarArchiveEntry(name,
                                                  TarConstants.LF_PAX_EXTENDED_HEADER_LC);

        StringWriter w = new StringWriter();
        for (Map.Entry<String, String> h : headers.entrySet()) {
            String key = h.getKey();
            String value = h.getValue();
            int len = key.length() + value.length()
                + 3 /* blank, equals and newline */
                + 2 /* guess 9 < actual length < 100 */;
            String line = len + " " + key + "=" + value + "\n";
            int actualLength = line.getBytes(CharsetNames.UTF_8).length;
            while (len != actualLength) {
                // Adjust for cases where length < 10 or > 100
                // or where UTF-8 encoding isn't a single octet
                // per character.
                // Must be in loop as size may go from 99 to 100 in
                // first pass so we'd need a second.
                len = actualLength;
                line = len + " " + key + "=" + value + "\n";
                actualLength = line.getBytes(CharsetNames.UTF_8).length;
            }
            w.write(line);
        }
        byte[] data = w.toString().getBytes(CharsetNames.UTF_8);
        pex.setSize(data.length);
        putArchiveEntry(pex);
        write(data);
        closeArchiveEntry();
    }

    private String stripTo7Bits(String name) {
        final int length = name.length();
        StringBuffer result = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            char stripped = (char) (name.charAt(i) & 0x7F);
            if (stripped != 0) { // would be read as Trailing null
                result.append(stripped);
            }
        }
        return result.toString();
    }

    /**
     * Write an EOF (end of archive) record to the tar archive.
     * An EOF record consists of a record of all zeros.
     */
    private void writeEOFRecord() throws IOException {
        for (int i = 0; i < recordBuf.length; ++i) {
            recordBuf[i] = 0;
        }

        buffer.writeRecord(recordBuf);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    /** {@inheritDoc} */
    @Override
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName)
            throws IOException {
        if(finished) {
            throw new IOException("Stream has already been finished");
        }
        return new TarArchiveEntry(inputFile, entryName);
    }

    private void addPaxHeadersForBigNumbers(Map<String, String> paxHeaders,
                                            TarArchiveEntry entry) {
        addPaxHeaderForBigNumber(paxHeaders, "size", entry.getSize(),
                                 TarConstants.MAXSIZE);
        addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getGroupId(),
                                 TarConstants.MAXID);
        addPaxHeaderForBigNumber(paxHeaders, "mtime",
                                 entry.getModTime().getTime() / 1000,
                                 TarConstants.MAXSIZE);
        addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getUserId(),
                                 TarConstants.MAXID);
        // star extensions by J\u00f6rg Schilling
        addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor",
                                 entry.getDevMajor(), TarConstants.MAXID);
        addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor",
                                 entry.getDevMinor(), TarConstants.MAXID);
        // there is no PAX header for file mode
        failForBigNumber("mode", entry.getMode(), TarConstants.MAXID);
    }

    private void addPaxHeaderForBigNumber(Map<String, String> paxHeaders,
                                          String header, long value,
                                          long maxValue) {
        if (value < 0 || value > maxValue) {
            paxHeaders.put(header, String.valueOf(value));
        }
    }

    private void failForBigNumbers(TarArchiveEntry entry) {
        failForBigNumber("entry size", entry.getSize(), TarConstants.MAXSIZE);
        failForBigNumber("group id", entry.getGroupId(), TarConstants.MAXID);
        failForBigNumber("last modification time",
                         entry.getModTime().getTime() / 1000,
                         TarConstants.MAXSIZE);
        failForBigNumber("user id", entry.getUserId(), TarConstants.MAXID);
        failForBigNumber("mode", entry.getMode(), TarConstants.MAXID);
        failForBigNumber("major device number", entry.getDevMajor(),
                         TarConstants.MAXID);
        failForBigNumber("minor device number", entry.getDevMinor(),
                         TarConstants.MAXID);
    }

    private void failForBigNumber(String field, long value, long maxValue) {
        if (value < 0 || value > maxValue) {
            throw new RuntimeException(field + " '" + value
                                       + "' is too big ( > "
                                       + maxValue + " )");
        }
    }
}

```
