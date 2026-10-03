## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="commons-cli" default="package" basedir=".">

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

  <target name="compile" depends="commons-cli-from-maven.compile"> </target>
  <target name="compile.tests" depends="commons-cli-from-maven.compile.tests"> </target>

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
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>37</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-cli</groupId>
  <artifactId>commons-cli</artifactId>
  <version>1.4-SNAPSHOT</version>
  <name>Apache Commons CLI</name>

  <inceptionYear>2002</inceptionYear>
  <description>
    Apache Commons CLI provides a simple API for presenting, processing and validating a command line interface.
  </description>

  <url>http://commons.apache.org/proper/commons-cli/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/CLI</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/cli/trunk/</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/cli/trunk/</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/cli/trunk/</url>
  </scm>

  <developers>
    <developer>
      <name>James Strachan</name>
      <id>jstrachan</id>
      <email>jstrachan@apache.org</email>
      <organization>SpiritSoft, Inc.</organization>
    </developer>
    <developer>
      <name>Bob McWhirter</name>
      <id>bob</id>
      <email>bob@werken.com</email>
      <organization>Werken</organization>
      <roles>
        <role>contributed ideas and code from werken.opt</role>
      </roles>
    </developer>
    <developer>
      <name>John Keyes</name>
      <id>jkeyes</id>
      <email>jbjk@mac.com</email>
      <organization>integral Source</organization>
      <roles>
        <role>contributed ideas and code from Optz</role>
      </roles>
    </developer>
    <developer>
      <name>Rob Oxspring</name>
      <id>roxspring</id>
      <email>roxspring@imapmail.org</email>
      <organization>Indigo Stone</organization>
      <roles>
        <role>designed CLI2</role>
      </roles>
    </developer>
    <developer>
      <name>Emmanuel Bourg</name>
      <id>ebourg</id>
      <email>ebourg@apache.org</email>
      <organization>Ariane Software</organization>
    </developer>
    <developer>
      <name>Thomas Neidhart</name>
      <id>tn</id>
      <email>tn@apache.org</email>
    </developer>
  </developers>

  <contributors>
    <contributor>
      <name>Beluga Behr</name>
    </contributor>
    <contributor>
      <name>Peter Donald</name>
      <roles>
        <role>contributed ideas and code from Avalon Excalibur's cli package</role>
      </roles>
    </contributor>
    <contributor>
      <name>Brian Egge</name>
      <roles>
        <role>made the 1.1 release happen</role>
      </roles>
    </contributor>
    <contributor>
      <name>Duncan Jones</name>
      <roles>
        <role>supplied patches</role>
      </roles>
    </contributor>
    <contributor>
      <name>Berin Loritsch</name>
      <email>bloritsch@apache.org</email>
      <roles>
        <role>helped in the Avalon CLI merge</role>
      </roles>
    </contributor>
    <contributor>
      <name>Peter Maddocks</name>
      <email>peter_maddocks@hp.com</email>
      <organization>Hewlett-Packard</organization>
      <roles>
        <role>supplied patch</role>
      </roles>
    </contributor>
    <contributor>
      <name>Alexandru Mocanu</name>
      <roles>
        <role>supplied patch</role>
      </roles>
    </contributor>
    <contributor>
      <name>Andrew Shirley</name>
      <roles>
        <role>lots of fixes for 1.1</role>
      </roles>
    </contributor>
    <contributor>
      <name>Greg Thomas</name>
    </contributor>
    <contributor>
      <name>Slawek Zachcial</name>
      <roles>
        <role>unit tests</role>
      </roles>
    </contributor>
    
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <maven.compiler.source>1.5</maven.compiler.source>
    <maven.compiler.target>1.5</maven.compiler.target>
    <commons.componentid>cli</commons.componentid>
    <commons.release.version>1.3</commons.release.version>
    <commons.release.name>commons-cli-${commons.release.version}</commons.release.name>
    <commons.osgi.symbolicName>org.apache.commons.cli</commons.osgi.symbolicName>
    <commons.jira.id>CLI</commons.jira.id>
    <commons.jira.pid>12310463</commons.jira.pid>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC1</commons.rc.version>

    <commons.scmPubCheckoutDirectory>site-content</commons.scmPubCheckoutDirectory>
  </properties>

  <build>
    <plugins>
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
    </plugins>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-scm-publish-plugin</artifactId>
          <configuration>
            <ignorePathsToDelete>
              <ignorePathToDelete>javadocs**</ignorePathToDelete>
            </ignorePathsToDelete>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <linksource>true</linksource>
          <links>
            <link>http://download.oracle.com/javase/6/docs/api</link>
          </links>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.15</version>
        <configuration>
          <configLocation>${basedir}/src/conf/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
          <suppressionsLocation>${basedir}/src/conf/checkstyle-suppressions.xml</suppressionsLocation>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>checkstyle</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>3.0.1</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/src/conf/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.4</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

  <distributionManagement>
    <site>
      <id>apache.website</id>
      <name>Apache Commons Site</name>
      <url>scm:svn:${commons.scmPubUrl}</url>
    </site>
  </distributionManagement>

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
            <version>1.7</version>
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
  </profiles>

</project>

```

## src/main/java/org/apache/commons/cli/Options.java

```
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Main entry-point into the library.
 * <p>
 * Options represents a collection of {@link Option} objects, which
 * describe the possible options for a command-line.
 * <p>
 * It may flexibly parse long and short options, with or without
 * values.  Additionally, it may parse only a portion of a commandline,
 * allowing for flexible multi-stage parsing.
 *
 * @see org.apache.commons.cli.CommandLine
 *
 * @version $Id$
 */
public class Options implements Serializable
{
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** a map of the options with the character key */
    private final Map<String, Option> shortOpts = new LinkedHashMap<String, Option>();

    /** a map of the options with the long key */
    private final Map<String, Option> longOpts = new LinkedHashMap<String, Option>();

    /** a map of the required options */
    // N.B. This can contain either a String (addOption) or an OptionGroup (addOptionGroup)
    // TODO this seems wrong
    private final List<Object> requiredOpts = new ArrayList<Object>();

    /** a map of the option groups */
    private final Map<String, OptionGroup> optionGroups = new HashMap<String, OptionGroup>();

    /**
     * Add the specified option group.
     *
     * @param group the OptionGroup that is to be added
     * @return the resulting Options instance
     */
    public Options addOptionGroup(OptionGroup group)
    {
        if (group.isRequired())
        {
            requiredOpts.add(group);
        }

        for (Option option : group.getOptions())
        {
            // an Option cannot be required if it is in an
            // OptionGroup, either the group is required or
            // nothing is required
            option.setRequired(false);
            addOption(option);

            optionGroups.put(option.getKey(), group);
        }

        return this;
    }

    /**
     * Lists the OptionGroups that are members of this Options instance.
     *
     * @return a Collection of OptionGroup instances.
     */
    Collection<OptionGroup> getOptionGroups()
    {
        return new HashSet<OptionGroup>(optionGroups.values());
    }

    /**
     * Add an option that only contains a short name.
     * The option does not take an argument.
     *
     * @param opt Short single-character name of the option.
     * @param description Self-documenting description
     * @return the resulting Options instance
     * @since 1.3
     */
    public Options addOption(String opt, String description)
    {
        addOption(opt, null, false, description);
        return this;
    }

    /**
     * Add an option that only contains a short-name.
     * It may be specified as requiring an argument.
     *
     * @param opt Short single-character name of the option.
     * @param hasArg flag signally if an argument is required after this option
     * @param description Self-documenting description
     * @return the resulting Options instance
     */
    public Options addOption(String opt, boolean hasArg, String description)
    {
        addOption(opt, null, hasArg, description);
        return this;
    }

    /**
     * Add an option that contains a short-name and a long-name.
     * It may be specified as requiring an argument.
     *
     * @param opt Short single-character name of the option.
     * @param longOpt Long multi-character name of the option.
     * @param hasArg flag signally if an argument is required after this option
     * @param description Self-documenting description
     * @return the resulting Options instance
     */
    public Options addOption(String opt, String longOpt, boolean hasArg, String description)
    {
        addOption(new Option(opt, longOpt, hasArg, description));
        return this;
    }

    /**
     * Adds an option instance
     *
     * @param opt the option that is to be added
     * @return the resulting Options instance
     */
    public Options addOption(Option opt)
    {
        String key = opt.getKey();

        // add it to the long option list
        if (opt.hasLongOpt())
        {
            longOpts.put(opt.getLongOpt(), opt);
        }

        // if the option is required add it to the required list
        if (opt.isRequired())
        {
            if (requiredOpts.contains(key))
            {
                requiredOpts.remove(requiredOpts.indexOf(key));
            }
            requiredOpts.add(key);
        }

        shortOpts.put(key, opt);

        return this;
    }

    /**
     * Retrieve a read-only list of options in this set
     *
     * @return read-only Collection of {@link Option} objects in this descriptor
     */
    public Collection<Option> getOptions()
    {
        return Collections.unmodifiableCollection(helpOptions());
    }

    /**
     * Returns the Options for use by the HelpFormatter.
     *
     * @return the List of Options
     */
    List<Option> helpOptions()
    {
        return new ArrayList<Option>(shortOpts.values());
    }

    /**
     * Returns the required options.
     *
     * @return read-only List of required options
     */
    public List getRequiredOptions()
    {
        return Collections.unmodifiableList(requiredOpts);
    }

    /**
     * Retrieve the {@link Option} matching the long or short name specified.
     * The leading hyphens in the name are ignored (up to 2).
     *
     * @param opt short or long name of the {@link Option}
     * @return the option represented by opt
     */
    public Option getOption(String opt)
    {
        opt = Util.stripLeadingHyphens(opt);

        if (shortOpts.containsKey(opt))
        {
            return shortOpts.get(opt);
        }

        return longOpts.get(opt);
    }

    /**
     * Returns the options with a long name starting with the name specified.
     * 
     * @param opt the partial name of the option
     * @return the options matching the partial name specified, or an empty list if none matches
     * @since 1.3
     */
    public List<String> getMatchingOptions(String opt)
    {
        opt = Util.stripLeadingHyphens(opt);
        
        List<String> matchingOpts = new ArrayList<String>();

        // for a perfect match return the single option only
        if(longOpts.keySet().contains(opt)) {
            return Collections.singletonList(opt);
        }

        for (String longOpt : longOpts.keySet())
        {
            if (longOpt.startsWith(opt))
            {
                matchingOpts.add(longOpt);
            }
        }
        
        return matchingOpts;
    }

    /**
     * Returns whether the named {@link Option} is a member of this {@link Options}.
     *
     * @param opt short or long name of the {@link Option}
     * @return true if the named {@link Option} is a member of this {@link Options}
     */
    public boolean hasOption(String opt)
    {
        opt = Util.stripLeadingHyphens(opt);

        return shortOpts.containsKey(opt) || longOpts.containsKey(opt);
    }

    /**
     * Returns whether the named {@link Option} is a member of this {@link Options}.
     *
     * @param opt long name of the {@link Option}
     * @return true if the named {@link Option} is a member of this {@link Options}
     * @since 1.3
     */
    public boolean hasLongOption(String opt)
    {
        opt = Util.stripLeadingHyphens(opt);

        return longOpts.containsKey(opt);
    }

    /**
     * Returns whether the named {@link Option} is a member of this {@link Options}.
     *
     * @param opt short name of the {@link Option}
     * @return true if the named {@link Option} is a member of this {@link Options}
     * @since 1.3
     */
    public boolean hasShortOption(String opt)
    {
        opt = Util.stripLeadingHyphens(opt);

        return shortOpts.containsKey(opt);
    }

    /**
     * Returns the OptionGroup the <code>opt</code> belongs to.
     * @param opt the option whose OptionGroup is being queried.
     *
     * @return the OptionGroup if <code>opt</code> is part
     * of an OptionGroup, otherwise return null
     */
    public OptionGroup getOptionGroup(Option opt)
    {
        return optionGroups.get(opt.getKey());
    }

    /**
     * Dump state, suitable for debugging.
     *
     * @return Stringified form of this object
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();

        buf.append("[ Options: [ short ");
        buf.append(shortOpts.toString());
        buf.append(" ] [ long ");
        buf.append(longOpts);
        buf.append(" ]");

        return buf.toString();
    }
}

```
