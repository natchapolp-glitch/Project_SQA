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
    <version>24</version>
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

## src/main/java/org/apache/commons/compress/archivers/zip/ZipArchiveEntry.java

```
/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;

/**
 * Extension that adds better handling of extra fields and provides
 * access to the internal and external file attributes.
 *
 * <p>The extra data is expected to follow the recommendation of
 * {@link <a href="http://www.pkware.com/documents/casestudies/APPNOTE.TXT">
 * APPNOTE.txt</a>}:</p>
 * <ul>
 *   <li>the extra byte array consists of a sequence of extra fields</li>
 *   <li>each extra fields starts by a two byte header id followed by
 *   a two byte sequence holding the length of the remainder of
 *   data.</li>
 * </ul>
 *
 * <p>Any extra data that cannot be parsed by the rules above will be
 * consumed as "unparseable" extra data and treated differently by the
 * methods of this class.  Versions prior to Apache Commons Compress
 * 1.1 would have thrown an exception if any attempt was made to read
 * or write extra data not conforming to the recommendation.</p>
 *
 * @NotThreadSafe
 */
public class ZipArchiveEntry extends java.util.zip.ZipEntry
    implements ArchiveEntry {

    public static final int PLATFORM_UNIX = 3;
    public static final int PLATFORM_FAT  = 0;
    private static final int SHORT_MASK = 0xFFFF;
    private static final int SHORT_SHIFT = 16;

    /**
     * The {@link java.util.zip.ZipEntry} base class only supports
     * the compression methods STORED and DEFLATED. We override the
     * field so that any compression methods can be used.
     * <p>
     * The default value -1 means that the method has not been specified.
     *
     * @see <a href="https://issues.apache.org/jira/browse/COMPRESS-93"
     *        >COMPRESS-93</a>
     */
    private int method = -1;

    /**
     * The {@link java.util.zip.ZipEntry#setSize} method in the base
     * class throws an IllegalArgumentException if the size is bigger
     * than 2GB for Java versions < 7.  Need to keep our own size
     * information for Zip64 support.
     */
    private long size = SIZE_UNKNOWN;

    private int internalAttributes = 0;
    private int platform = PLATFORM_FAT;
    private long externalAttributes = 0;
    private LinkedHashMap<ZipShort, ZipExtraField> extraFields = null;
    private UnparseableExtraFieldData unparseableExtra = null;
    private String name = null;
    private byte[] rawName = null;
    private GeneralPurposeBit gpb = new GeneralPurposeBit();

    /**
     * Creates a new zip entry with the specified name.
     *
     * <p>Assumes the entry represents a directory if and only if the
     * name ends with a forward slash "/".</p>
     *
     * @param name the name of the entry
     */
    public ZipArchiveEntry(String name) {
        super(name);
        setName(name);
    }

    /**
     * Creates a new zip entry with fields taken from the specified zip entry.
     *
     * <p>Assumes the entry represents a directory if and only if the
     * name ends with a forward slash "/".</p>
     *
     * @param entry the entry to get fields from
     * @throws ZipException on error
     */
    public ZipArchiveEntry(java.util.zip.ZipEntry entry) throws ZipException {
        super(entry);
        setName(entry.getName());
        byte[] extra = entry.getExtra();
        if (extra != null) {
            setExtraFields(ExtraFieldUtils.parse(extra, true,
                                                 ExtraFieldUtils
                                                 .UnparseableExtraField.READ));
        } else {
            // initializes extra data to an empty byte array
            setExtra();
        }
        setMethod(entry.getMethod());
        this.size = entry.getSize();
    }

    /**
     * Creates a new zip entry with fields taken from the specified zip entry.
     *
     * <p>Assumes the entry represents a directory if and only if the
     * name ends with a forward slash "/".</p>
     *
     * @param entry the entry to get fields from
     * @throws ZipException on error
     */
    public ZipArchiveEntry(ZipArchiveEntry entry) throws ZipException {
        this((java.util.zip.ZipEntry) entry);
        setInternalAttributes(entry.getInternalAttributes());
        setExternalAttributes(entry.getExternalAttributes());
        setExtraFields(entry.getExtraFields(true));
    }

    /**
     */
    protected ZipArchiveEntry() {
        this("");
    }

    /**
     * Creates a new zip entry taking some information from the given
     * file and using the provided name.
     *
     * <p>The name will be adjusted to end with a forward slash "/" if
     * the file is a directory.  If the file is not a directory a
     * potential trailing forward slash will be stripped from the
     * entry name.</p>
     */
    public ZipArchiveEntry(File inputFile, String entryName) {
        this(inputFile.isDirectory() && !entryName.endsWith("/") ? 
             entryName + "/" : entryName);
        if (inputFile.isFile()){
            setSize(inputFile.length());
        }
        setTime(inputFile.lastModified());
        // TODO are there any other fields we can set here?
    }

    /**
     * Overwrite clone.
     * @return a cloned copy of this ZipArchiveEntry
     */
    @Override
    public Object clone() {
        ZipArchiveEntry e = (ZipArchiveEntry) super.clone();

        e.setInternalAttributes(getInternalAttributes());
        e.setExternalAttributes(getExternalAttributes());
        e.setExtraFields(getExtraFields(true));
        return e;
    }

    /**
     * Returns the compression method of this entry, or -1 if the
     * compression method has not been specified.
     *
     * @return compression method
     *
     * @since 1.1
     */
    @Override
    public int getMethod() {
        return method;
    }

    /**
     * Sets the compression method of this entry.
     *
     * @param method compression method
     *
     * @since 1.1
     */
    @Override
    public void setMethod(int method) {
        if (method < 0) {
            throw new IllegalArgumentException(
                    "ZIP compression method can not be negative: " + method);
        }
        this.method = method;
    }

    /**
     * Retrieves the internal file attributes.
     *
     * @return the internal file attributes
     */
    public int getInternalAttributes() {
        return internalAttributes;
    }

    /**
     * Sets the internal file attributes.
     * @param value an <code>int</code> value
     */
    public void setInternalAttributes(int value) {
        internalAttributes = value;
    }

    /**
     * Retrieves the external file attributes.
     * @return the external file attributes
     */
    public long getExternalAttributes() {
        return externalAttributes;
    }

    /**
     * Sets the external file attributes.
     * @param value an <code>long</code> value
     */
    public void setExternalAttributes(long value) {
        externalAttributes = value;
    }

    /**
     * Sets Unix permissions in a way that is understood by Info-Zip's
     * unzip command.
     * @param mode an <code>int</code> value
     */
    public void setUnixMode(int mode) {
        // CheckStyle:MagicNumberCheck OFF - no point
        setExternalAttributes((mode << SHORT_SHIFT)
                              // MS-DOS read-only attribute
                              | ((mode & 0200) == 0 ? 1 : 0)
                              // MS-DOS directory flag
                              | (isDirectory() ? 0x10 : 0));
        // CheckStyle:MagicNumberCheck ON
        platform = PLATFORM_UNIX;
    }

    /**
     * Unix permission.
     * @return the unix permissions
     */
    public int getUnixMode() {
        return platform != PLATFORM_UNIX ? 0 :
            (int) ((getExternalAttributes() >> SHORT_SHIFT) & SHORT_MASK);
    }

    /**
     * Platform specification to put into the &quot;version made
     * by&quot; part of the central file header.
     *
     * @return PLATFORM_FAT unless {@link #setUnixMode setUnixMode}
     * has been called, in which case PLATORM_UNIX will be returned.
     */
    public int getPlatform() {
        return platform;
    }

    /**
     * Set the platform (UNIX or FAT).
     * @param platform an <code>int</code> value - 0 is FAT, 3 is UNIX
     */
    protected void setPlatform(int platform) {
        this.platform = platform;
    }

    /**
     * Replaces all currently attached extra fields with the new array.
     * @param fields an array of extra fields
     */
    public void setExtraFields(ZipExtraField[] fields) {
        extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
        for (ZipExtraField field : fields) {
            if (field instanceof UnparseableExtraFieldData) {
                unparseableExtra = (UnparseableExtraFieldData) field;
            } else {
                extraFields.put(field.getHeaderId(), field);
            }
        }
        setExtra();
    }

    /**
     * Retrieves all extra fields that have been parsed successfully.
     * @return an array of the extra fields
     */
    public ZipExtraField[] getExtraFields() {
        return getExtraFields(false);
    }

    /**
     * Retrieves extra fields.
     * @param includeUnparseable whether to also return unparseable
     * extra fields as {@link UnparseableExtraFieldData} if such data
     * exists.
     * @return an array of the extra fields
     *
     * @since 1.1
     */
    public ZipExtraField[] getExtraFields(boolean includeUnparseable) {
        if (extraFields == null) {
            return !includeUnparseable || unparseableExtra == null
                ? new ZipExtraField[0]
                : new ZipExtraField[] { unparseableExtra };
        }
        List<ZipExtraField> result =
            new ArrayList<ZipExtraField>(extraFields.values());
        if (includeUnparseable && unparseableExtra != null) {
            result.add(unparseableExtra);
        }
        return result.toArray(new ZipExtraField[0]);
    }

    /**
     * Adds an extra field - replacing an already present extra field
     * of the same type.
     *
     * <p>If no extra field of the same type exists, the field will be
     * added as last field.</p>
     * @param ze an extra field
     */
    public void addExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            unparseableExtra = (UnparseableExtraFieldData) ze;
        } else {
            if (extraFields == null) {
                extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
            }
            extraFields.put(ze.getHeaderId(), ze);
        }
        setExtra();
    }

    /**
     * Adds an extra field - replacing an already present extra field
     * of the same type.
     *
     * <p>The new extra field will be the first one.</p>
     * @param ze an extra field
     */
    public void addAsFirstExtraField(ZipExtraField ze) {
        if (ze instanceof UnparseableExtraFieldData) {
            unparseableExtra = (UnparseableExtraFieldData) ze;
        } else {
            LinkedHashMap<ZipShort, ZipExtraField> copy = extraFields;
            extraFields = new LinkedHashMap<ZipShort, ZipExtraField>();
            extraFields.put(ze.getHeaderId(), ze);
            if (copy != null) {
                copy.remove(ze.getHeaderId());
                extraFields.putAll(copy);
            }
        }
        setExtra();
    }

    /**
     * Remove an extra field.
     * @param type the type of extra field to remove
     */
    public void removeExtraField(ZipShort type) {
        if (extraFields == null) {
            throw new java.util.NoSuchElementException();
        }
        if (extraFields.remove(type) == null) {
            throw new java.util.NoSuchElementException();
        }
        setExtra();
    }

    /**
     * Removes unparseable extra field data.
     *
     * @since 1.1
     */
    public void removeUnparseableExtraFieldData() {
        if (unparseableExtra == null) {
            throw new java.util.NoSuchElementException();
        }
        unparseableExtra = null;
        setExtra();
    }

    /**
     * Looks up an extra field by its header id.
     *
     * @return null if no such field exists.
     */
    public ZipExtraField getExtraField(ZipShort type) {
        if (extraFields != null) {
            return extraFields.get(type);
        }
        return null;
    }

    /**
     * Looks up extra field data that couldn't be parsed correctly.
     *
     * @return null if no such field exists.
     *
     * @since 1.1
     */
    public UnparseableExtraFieldData getUnparseableExtraFieldData() {
        return unparseableExtra;
    }

    /**
     * Parses the given bytes as extra field data and consumes any
     * unparseable data as an {@link UnparseableExtraFieldData}
     * instance.
     * @param extra an array of bytes to be parsed into extra fields
     * @throws RuntimeException if the bytes cannot be parsed
     * @throws RuntimeException on error
     */
    @Override
    public void setExtra(byte[] extra) throws RuntimeException {
        try {
            ZipExtraField[] local =
                ExtraFieldUtils.parse(extra, true,
                                      ExtraFieldUtils.UnparseableExtraField.READ);
            mergeExtraFields(local, true);
        } catch (ZipException e) {
            // actually this is not possible as of Commons Compress 1.1
            throw new RuntimeException("Error parsing extra fields for entry: "
                                       + getName() + " - " + e.getMessage(), e);
        }
    }

    /**
     * Unfortunately {@link java.util.zip.ZipOutputStream
     * java.util.zip.ZipOutputStream} seems to access the extra data
     * directly, so overriding getExtra doesn't help - we need to
     * modify super's data directly.
     */
    protected void setExtra() {
        super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields(true)));
    }

    /**
     * Sets the central directory part of extra fields.
     */
    public void setCentralDirectoryExtra(byte[] b) {
        try {
            ZipExtraField[] central =
                ExtraFieldUtils.parse(b, false,
                                      ExtraFieldUtils.UnparseableExtraField.READ);
            mergeExtraFields(central, false);
        } catch (ZipException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Retrieves the extra data for the local file data.
     * @return the extra data for local file
     */
    public byte[] getLocalFileDataExtra() {
        byte[] extra = getExtra();
        return extra != null ? extra : new byte[0];
    }

    /**
     * Retrieves the extra data for the central directory.
     * @return the central directory extra data
     */
    public byte[] getCentralDirectoryExtra() {
        return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields(true));
    }

    /**
     * Get the name of the entry.
     * @return the entry name
     */
    @Override
    public String getName() {
        return name == null ? super.getName() : name;
    }

    /**
     * Is this entry a directory?
     * @return true if the entry is a directory
     */
    @Override
    public boolean isDirectory() {
        return getName().endsWith("/");
    }

    /**
     * Set the name of the entry.
     * @param name the name to use
     */
    protected void setName(String name) {
        if (name != null && getPlatform() == PLATFORM_FAT
            && name.indexOf("/") == -1) {
            name = name.replace('\\', '/');
        }
        this.name = name;
    }

    /**
     * Gets the uncompressed size of the entry data.
     * @return the entry size
     */
    @Override
    public long getSize() {
        return size;
    }

    /**
     * Sets the uncompressed size of the entry data.
     * @param size the uncompressed size in bytes
     * @exception IllegalArgumentException if the specified size is less
     *            than 0
     */
    @Override
    public void setSize(long size) {
        if (size < 0) {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = size;
    }

    /**
     * Sets the name using the raw bytes and the string created from
     * it by guessing or using the configured encoding.
     * @param name the name to use created from the raw bytes using
     * the guessed or configured encoding
     * @param rawName the bytes originally read as name from the
     * archive
     * @since 1.2
     */
    protected void setName(String name, byte[] rawName) {
        setName(name);
        this.rawName = rawName;
    }

    /**
     * Returns the raw bytes that made up the name before it has been
     * converted using the configured or guessed encoding.
     *
     * <p>This method will return null if this instance has not been
     * read from an archive.</p>
     *
     * @since 1.2
     */
    public byte[] getRawName() {
        if (rawName != null) {
            byte[] b = new byte[rawName.length];
            System.arraycopy(rawName, 0, b, 0, rawName.length);
            return b;
        }
        return null;
    }

    /**
     * Get the hashCode of the entry.
     * This uses the name as the hashcode.
     * @return a hashcode.
     */
    @Override
    public int hashCode() {
        // this method has severe consequences on performance. We cannot rely
        // on the super.hashCode() method since super.getName() always return
        // the empty string in the current implemention (there's no setter)
        // so it is basically draining the performance of a hashmap lookup
        return getName().hashCode();
    }

    /**
     * The "general purpose bit" field.
     * @since 1.1
     */
    public GeneralPurposeBit getGeneralPurposeBit() {
        return gpb;
    }

    /**
     * The "general purpose bit" field.
     * @since 1.1
     */
    public void setGeneralPurposeBit(GeneralPurposeBit b) {
        gpb = b;
    }

    /**
     * If there are no extra fields, use the given fields as new extra
     * data - otherwise merge the fields assuming the existing fields
     * and the new fields stem from different locations inside the
     * archive.
     * @param f the extra fields to merge
     * @param local whether the new fields originate from local data
     */
    private void mergeExtraFields(ZipExtraField[] f, boolean local)
        throws ZipException {
        if (extraFields == null) {
            setExtraFields(f);
        } else {
            for (ZipExtraField element : f) {
                ZipExtraField existing;
                if (element instanceof UnparseableExtraFieldData) {
                    existing = unparseableExtra;
                } else {
                    existing = getExtraField(element.getHeaderId());
                }
                if (existing == null) {
                    addExtraField(element);
                } else {
                    if (local) {
                        byte[] b = element.getLocalFileDataData();
                        existing.parseFromLocalFileData(b, 0, b.length);
                    } else {
                        byte[] b = element.getCentralDirectoryData();
                        existing.parseFromCentralDirectoryData(b, 0, b.length);
                    }
                }
            }
            setExtra();
        }
    }

    /** {@inheritDoc} */
    public Date getLastModifiedDate() {
        return new Date(getTime());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZipArchiveEntry other = (ZipArchiveEntry) obj;
        String myName = getName();
        String otherName = other.getName();
        if (myName == null) {
            if (otherName != null) {
                return false;
            }
        } else if (!myName.equals(otherName)) {
            return false;
        }
        String myComment = getComment();
        String otherComment = other.getComment();
        if (myComment == null) {
            myComment = "";
        }
        if (otherComment == null) {
            otherComment = "";
        }
        return getTime() == other.getTime()
            && myComment.equals(otherComment)
            && getInternalAttributes() == other.getInternalAttributes()
            && getPlatform() == other.getPlatform()
            && getExternalAttributes() == other.getExternalAttributes()
            && getMethod() == other.getMethod()
            && getSize() == other.getSize()
            && getCrc() == other.getCrc()
            && getCompressedSize() == other.getCompressedSize()
            && Arrays.equals(getCentralDirectoryExtra(),
                             other.getCentralDirectoryExtra())
            && Arrays.equals(getLocalFileDataExtra(),
                             other.getLocalFileDataExtra())
            && gpb.equals(other.gpb);
    }
}

```
