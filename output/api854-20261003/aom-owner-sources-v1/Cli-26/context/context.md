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
    <version>11</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-cli</groupId>
  <artifactId>commons-cli</artifactId>
  <version>1.2</version>
  <name>Commons CLI</name>

  <inceptionYear>2002</inceptionYear>
  <description>
    Commons CLI provides a simple API for presenting, processing and validating a command line interface.
  </description>

  <url>http://commons.apache.org/cli/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/CLI</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/cli/branches/cli-1.x/</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/cli/branches/cli-1.x/</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/cli/branches/cli-1.x/</url>
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
  </developers>

  <contributors>
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
      <name>Andrew Shirley</name>
      <roles>
        <role>lots of fixes for 1.1</role>
      </roles>
    </contributor>
  </contributors>

  <dependencies>
    <!-- used for unit tests -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <maven.compile.source>1.4</maven.compile.source>
    <maven.compile.target>1.4</maven.compile.target>
    <commons.componentid>cli</commons.componentid>
    <commons.release.version>1.2</commons.release.version>
    <commons.release.name>commons-cli-${commons.release.version}</commons.release.name>
    <commons.osgi.symbolicName>org.apache.commons.cli</commons.osgi.symbolicName>
    <commons.binary.suffix />
    <commons.jira.id>CLI</commons.jira.id>
    <commons.jira.pid>12310463</commons.jira.pid>
    <!-- The RC version used in the staging repository URL. -->
    <commons.rc.version>RC6</commons.rc.version>    
  </properties>

  <build>
    <sourceDirectory>src/java</sourceDirectory>
    <testSourceDirectory>src/test</testSourceDirectory>
    <resources>
      <resource>
        <directory>.</directory>
        <targetPath>META-INF</targetPath>
        <includes>
          <include>NOTICE.txt</include>
          <include>LICENSE.txt</include>
        </includes>
      </resource>
    </resources>
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
      <!--
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-ant-plugin</artifactId>
        <version>2.1.1-SNAPSHOT</version>
      </plugin>
      -->
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>1.2</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <configuration>
          <configLocation>src/conf/checkstyle.xml</configLocation>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.2</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.2.1</version>
        <configuration>
          <comparisonVersion>1.1</comparisonVersion>
        </configuration>
      </plugin>
    </plugins>
  </reporting>


  <profiles>
    <!-- Profile to let Gump choose the name of the jar file generated -->
    <profile>
      <id>gump</id>
      <activation>
        <property>
          <name>maven.final.name</name>
        </property>
      </activation>
      <build>
        <plugins>
          <plugin>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
              <finalName>${maven.final.name}</finalName>
            </configuration>
          </plugin>
        </plugins>
      </build>
    </profile>
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
  </profiles>

</project>

```

## src/java/org/apache/commons/cli/OptionBuilder.java

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

/**
 * OptionBuilder allows the user to create Options using descriptive methods.
 *
 * <p>Details on the Builder pattern can be found at
 * <a href="http://c2.com/cgi-bin/wiki?BuilderPattern">
 * http://c2.com/cgi-bin/wiki?BuilderPattern</a>.</p>
 *
 * @author John Keyes (john at integralsource.com)
 * @version $Revision$, $Date$
 * @since 1.0
 */
public final class OptionBuilder
{
    /** long option */
    private static String longopt;

    /** option description */
    private static String description;

    /** argument name */
    private static String argName;

    /** is required? */
    private static boolean required;

    /** the number of arguments */
    private static int numberOfArgs = Option.UNINITIALIZED;

    /** option type */
    private static Object type;

    /** option can have an optional argument value */
    private static boolean optionalArg;

    /** value separator for argument value */
    private static char valuesep;

    /** option builder instance */
    private static OptionBuilder instance = new OptionBuilder();

    /**
     * private constructor to prevent instances being created
     */
    private OptionBuilder()
    {
        // hide the constructor
    }

    /**
     * Resets the member variables to their default values.
     */
    private static void reset()
    {
        description = null;
        argName = "arg";
        longopt = null;
        type = null;
        required = false;
        numberOfArgs = Option.UNINITIALIZED;


        // PMM 9/6/02 - these were missing
        optionalArg = false;
        valuesep = (char) 0;
    }

    /**
     * The next Option created will have the following long option value.
     *
     * @param newLongopt the long option value
     * @return the OptionBuilder instance
     */
    public static OptionBuilder withLongOpt(String newLongopt)
    {
        OptionBuilder.longopt = newLongopt;

        return instance;
    }

    /**
     * The next Option created will require an argument value.
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasArg()
    {
        OptionBuilder.numberOfArgs = 1;

        return instance;
    }

    /**
     * The next Option created will require an argument value if
     * <code>hasArg</code> is true.
     *
     * @param hasArg if true then the Option has an argument value
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasArg(boolean hasArg)
    {
        OptionBuilder.numberOfArgs = hasArg ? 1 : Option.UNINITIALIZED;

        return instance;
    }

    /**
     * The next Option created will have the specified argument value name.
     *
     * @param name the name for the argument value
     * @return the OptionBuilder instance
     */
    public static OptionBuilder withArgName(String name)
    {
        OptionBuilder.argName = name;

        return instance;
    }

    /**
     * The next Option created will be required.
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder isRequired()
    {
        OptionBuilder.required = true;

        return instance;
    }

    /**
     * The next Option created uses <code>sep</code> as a means to
     * separate argument values.
     *
     * <b>Example:</b>
     * <pre>
     * Option opt = OptionBuilder.withValueSeparator(':')
     *                           .create('D');
     *
     * CommandLine line = parser.parse(args);
     * String propertyName = opt.getValue(0);
     * String propertyValue = opt.getValue(1);
     * </pre>
     *
     * @param sep The value separator to be used for the argument values.
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder withValueSeparator(char sep)
    {
        OptionBuilder.valuesep = sep;

        return instance;
    }

    /**
     * The next Option created uses '<code>=</code>' as a means to
     * separate argument values.
     *
     * <b>Example:</b>
     * <pre>
     * Option opt = OptionBuilder.withValueSeparator()
     *                           .create('D');
     *
     * CommandLine line = parser.parse(args);
     * String propertyName = opt.getValue(0);
     * String propertyValue = opt.getValue(1);
     * </pre>
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder withValueSeparator()
    {
        OptionBuilder.valuesep = '=';

        return instance;
    }

    /**
     * The next Option created will be required if <code>required</code>
     * is true.
     *
     * @param newRequired if true then the Option is required
     * @return the OptionBuilder instance
     */
    public static OptionBuilder isRequired(boolean newRequired)
    {
        OptionBuilder.required = newRequired;

        return instance;
    }

    /**
     * The next Option created can have unlimited argument values.
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasArgs()
    {
        OptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;

        return instance;
    }

    /**
     * The next Option created can have <code>num</code> argument values.
     *
     * @param num the number of args that the option can have
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasArgs(int num)
    {
        OptionBuilder.numberOfArgs = num;

        return instance;
    }

    /**
     * The next Option can have an optional argument.
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasOptionalArg()
    {
        OptionBuilder.numberOfArgs = 1;
        OptionBuilder.optionalArg = true;

        return instance;
    }

    /**
     * The next Option can have an unlimited number of optional arguments.
     *
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasOptionalArgs()
    {
        OptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        OptionBuilder.optionalArg = true;

        return instance;
    }

    /**
     * The next Option can have the specified number of optional arguments.
     *
     * @param numArgs - the maximum number of optional arguments
     * the next Option created can have.
     * @return the OptionBuilder instance
     */
    public static OptionBuilder hasOptionalArgs(int numArgs)
    {
        OptionBuilder.numberOfArgs = numArgs;
        OptionBuilder.optionalArg = true;

        return instance;
    }

    /**
     * The next Option created will have a value that will be an instance
     * of <code>type</code>.
     *
     * @param newType the type of the Options argument value
     * @return the OptionBuilder instance
     */
    public static OptionBuilder withType(Object newType)
    {
        OptionBuilder.type = newType;

        return instance;
    }

    /**
     * The next Option created will have the specified description
     *
     * @param newDescription a description of the Option's purpose
     * @return the OptionBuilder instance
     */
    public static OptionBuilder withDescription(String newDescription)
    {
        OptionBuilder.description = newDescription;

        return instance;
    }

    /**
     * Create an Option using the current settings and with
     * the specified Option <code>char</code>.
     *
     * @param opt the character representation of the Option
     * @return the Option instance
     * @throws IllegalArgumentException if <code>opt</code> is not
     * a valid character.  See Option.
     */
    public static Option create(char opt) throws IllegalArgumentException
    {
        return create(String.valueOf(opt));
    }

    /**
     * Create an Option using the current settings
     *
     * @return the Option instance
     * @throws IllegalArgumentException if <code>longOpt</code> has not been set.
     */
    public static Option create() throws IllegalArgumentException
    {
        if (longopt == null)
        {
            OptionBuilder.reset();
            throw new IllegalArgumentException("must specify longopt");
        }

        return create(null);
    }

    /**
     * Create an Option using the current settings and with
     * the specified Option <code>char</code>.
     *
     * @param opt the <code>java.lang.String</code> representation
     * of the Option
     * @return the Option instance
     * @throws IllegalArgumentException if <code>opt</code> is not
     * a valid character.  See Option.
     */
    public static Option create(String opt) throws IllegalArgumentException
    {
        Option option = null;
        try {
            // create the option
            option = new Option(opt, description);

            // set the option properties
            option.setLongOpt(longopt);
            option.setRequired(required);
            option.setOptionalArg(optionalArg);
            option.setArgs(numberOfArgs);
            option.setType(type);
            option.setValueSeparator(valuesep);
            option.setArgName(argName);
        } finally {
            // reset the OptionBuilder properties
            OptionBuilder.reset();
        }

        // return the Option instance
        return option;
    }
}

```
