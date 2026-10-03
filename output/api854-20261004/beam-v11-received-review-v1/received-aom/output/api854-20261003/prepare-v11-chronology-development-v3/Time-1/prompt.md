Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Time; fixed revision: 1f.
Modified target classes:
org.joda.time.Partial
org.joda.time.field.UnsupportedDurationField

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "getField",
    "parameter_types": "int,org.joda.time.Chronology"
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "getValues",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "size",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "toStringList",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "with",
    "parameter_types": "org.joda.time.DateTimeFieldType,int"
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "withChronologyRetainFields",
    "parameter_types": "org.joda.time.Chronology"
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "withField",
    "parameter_types": "org.joda.time.DateTimeFieldType,int"
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "",
    "method": "without",
    "parameter_types": "org.joda.time.DateTimeFieldType"
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "org.joda.time.Chronology",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.Partial",
    "constructor_types": "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "",
    "method": "getInstance",
    "parameter_types": "org.joda.time.DurationFieldType"
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "compareTo",
    "parameter_types": "org.joda.time.DurationField"
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "equals",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "getName",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "getType",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "getUnitMillis",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "isPrecise",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "isSupported",
    "parameter_types": ""
  },
  {
    "class": "org.joda.time.field.UnsupportedDurationField",
    "constructor_types": "org.joda.time.DurationFieldType",
    "method": "toString",
    "parameter_types": ""
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "org.joda.time.Chronology",
  "org.joda.time.DateMidnight",
  "org.joda.time.DateTime",
  "org.joda.time.DateTimeComparator",
  "org.joda.time.DateTimeConstants",
  "org.joda.time.DateTimeField",
  "org.joda.time.DateTimeFieldType",
  "org.joda.time.DateTimeUtils",
  "org.joda.time.DateTimeZone",
  "org.joda.time.Days",
  "org.joda.time.Duration",
  "org.joda.time.DurationField",
  "org.joda.time.DurationFieldType",
  "org.joda.time.Hours",
  "org.joda.time.IllegalFieldValueException",
  "org.joda.time.IllegalInstantException",
  "org.joda.time.Instant",
  "org.joda.time.Interval",
  "org.joda.time.JodaTimePermission",
  "org.joda.time.LocalDate",
  "org.joda.time.LocalDateTime",
  "org.joda.time.LocalTime",
  "org.joda.time.Minutes",
  "org.joda.time.MonthDay",
  "org.joda.time.Months",
  "org.joda.time.MutableDateTime",
  "org.joda.time.MutableInterval",
  "org.joda.time.MutablePeriod",
  "org.joda.time.Partial",
  "org.joda.time.Period",
  "org.joda.time.PeriodType",
  "org.joda.time.ReadWritableDateTime",
  "org.joda.time.ReadWritableInstant",
  "org.joda.time.ReadWritableInterval",
  "org.joda.time.ReadWritablePeriod",
  "org.joda.time.ReadableDateTime",
  "org.joda.time.ReadableDuration",
  "org.joda.time.ReadableInstant",
  "org.joda.time.ReadableInterval",
  "org.joda.time.ReadablePartial",
  "org.joda.time.ReadablePeriod",
  "org.joda.time.Seconds",
  "org.joda.time.TimeOfDay",
  "org.joda.time.Weeks",
  "org.joda.time.YearMonth",
  "org.joda.time.YearMonthDay",
  "org.joda.time.Years",
  "org.joda.time.base.AbstractDateTime",
  "org.joda.time.base.AbstractDuration",
  "org.joda.time.base.AbstractInstant",
  "org.joda.time.base.AbstractInterval",
  "org.joda.time.base.AbstractPartial",
  "org.joda.time.base.AbstractPeriod",
  "org.joda.time.base.BaseDateTime",
  "org.joda.time.base.BaseDuration",
  "org.joda.time.base.BaseInterval",
  "org.joda.time.base.BaseLocal",
  "org.joda.time.base.BasePartial",
  "org.joda.time.base.BasePeriod",
  "org.joda.time.base.BaseSingleFieldPeriod",
  "org.joda.time.chrono.AssembledChronology",
  "org.joda.time.chrono.BaseChronology",
  "org.joda.time.chrono.BasicChronology",
  "org.joda.time.chrono.BasicDayOfMonthDateTimeField",
  "org.joda.time.chrono.BasicDayOfYearDateTimeField",
  "org.joda.time.chrono.BasicFixedMonthChronology",
  "org.joda.time.chrono.BasicGJChronology",
  "org.joda.time.chrono.BasicMonthOfYearDateTimeField",
  "org.joda.time.chrono.BasicSingleEraDateTimeField",
  "org.joda.time.chrono.BasicWeekOfWeekyearDateTimeField",
  "org.joda.time.chrono.BasicWeekyearDateTimeField",
  "org.joda.time.chrono.BasicYearDateTimeField",
  "org.joda.time.chrono.BuddhistChronology",
  "org.joda.time.chrono.CopticChronology",
  "org.joda.time.chrono.EthiopicChronology",
  "org.joda.time.chrono.GJChronology",
  "org.joda.time.chrono.GJDayOfWeekDateTimeField",
  "org.joda.time.chrono.GJEraDateTimeField",
  "org.joda.time.chrono.GJLocaleSymbols",
  "org.joda.time.chrono.GJMonthOfYearDateTimeField",
  "org.joda.time.chrono.GJYearOfEraDateTimeField",
  "org.joda.time.chrono.GregorianChronology",
  "org.joda.time.chrono.ISOChronology",
  "org.joda.time.chrono.ISOYearOfEraDateTimeField",
  "org.joda.time.chrono.IslamicChronology",
  "org.joda.time.chrono.JulianChronology",
  "org.joda.time.chrono.LenientChronology",
  "org.joda.time.chrono.LimitChronology",
  "org.joda.time.chrono.StrictChronology",
  "org.joda.time.chrono.ZonedChronology",
  "org.joda.time.convert.AbstractConverter",
  "org.joda.time.convert.CalendarConverter",
  "org.joda.time.convert.Converter",
  "org.joda.time.convert.ConverterManager",
  "org.joda.time.convert.ConverterSet",
  "org.joda.time.convert.DateConverter",
  "org.joda.time.convert.DurationConverter",
  "org.joda.time.convert.InstantConverter",
  "org.joda.time.convert.IntervalConverter",
  "org.joda.time.convert.LongConverter",
  "org.joda.time.convert.NullConverter",
  "org.joda.time.convert.PartialConverter",
  "org.joda.time.convert.PeriodConverter",
  "org.joda.time.convert.ReadableDurationConverter",
  "org.joda.time.convert.ReadableInstantConverter",
  "org.joda.time.convert.ReadableIntervalConverter",
  "org.joda.time.convert.ReadablePartialConverter",
  "org.joda.time.convert.ReadablePeriodConverter",
  "org.joda.time.convert.StringConverter",
  "org.joda.time.field.AbstractPartialFieldProperty",
  "org.joda.time.field.AbstractReadableInstantFieldProperty",
  "org.joda.time.field.BaseDateTimeField",
  "org.joda.time.field.BaseDurationField",
  "org.joda.time.field.DecoratedDateTimeField",
  "org.joda.time.field.DecoratedDurationField",
  "org.joda.time.field.DelegatedDateTimeField",
  "org.joda.time.field.DelegatedDurationField",
  "org.joda.time.field.DividedDateTimeField",
  "org.joda.time.field.FieldUtils",
  "org.joda.time.field.ImpreciseDateTimeField",
  "org.joda.time.field.LenientDateTimeField",
  "org.joda.time.field.MillisDurationField",
  "org.joda.time.field.OffsetDateTimeField",
  "org.joda.time.field.PreciseDateTimeField",
  "org.joda.time.field.PreciseDurationDateTimeField",
  "org.joda.time.field.PreciseDurationField",
  "org.joda.time.field.RemainderDateTimeField",
  "org.joda.time.field.ScaledDurationField",
  "org.joda.time.field.SkipDateTimeField",
  "org.joda.time.field.SkipUndoDateTimeField",
  "org.joda.time.field.StrictDateTimeField",
  "org.joda.time.field.UnsupportedDateTimeField",
  "org.joda.time.field.UnsupportedDurationField",
  "org.joda.time.field.ZeroIsMaxDateTimeField",
  "org.joda.time.format.DateTimeFormat",
  "org.joda.time.format.DateTimeFormatter",
  "org.joda.time.format.DateTimeFormatterBuilder",
  "org.joda.time.format.DateTimeParser",
  "org.joda.time.format.DateTimeParserBucket",
  "org.joda.time.format.DateTimePrinter",
  "org.joda.time.format.FormatUtils",
  "org.joda.time.format.ISODateTimeFormat",
  "org.joda.time.format.ISOPeriodFormat",
  "org.joda.time.format.PeriodFormat",
  "org.joda.time.format.PeriodFormatter",
  "org.joda.time.format.PeriodFormatterBuilder",
  "org.joda.time.format.PeriodParser",
  "org.joda.time.format.PeriodPrinter",
  "org.joda.time.tz.CachedDateTimeZone",
  "org.joda.time.tz.DateTimeZoneBuilder",
  "org.joda.time.tz.DefaultNameProvider",
  "org.joda.time.tz.FixedDateTimeZone",
  "org.joda.time.tz.NameProvider",
  "org.joda.time.tz.Provider",
  "org.joda.time.tz.UTCProvider",
  "org.joda.time.tz.ZoneInfoCompiler",
  "org.joda.time.tz.ZoneInfoProvider"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

Explicit fixture policy: aom-beam-champ-chronology-fixtures-v11-development. Use the reviewed capability recipes below instead of legacy recursive/null construction.
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


Explicit fixture recipe definitions (generation support, separate from production source):
Use the same construction/projection knowledge across all four approaches. Setup failures are not target observations. Use valid receiver/dependency graphs and node kinds.
```json
{
  "fixture_policy_id": "aom-beam-champ-chronology-fixtures-v11-development",
  "schema_version": 1,
  "scope": "Same fixture construction/projection knowledge for all four approaches; no execution feedback",
  "source_sha256": {
    "algorithms/java/SqaProbe.java": "d9e08c108abcaecc3c9912c28eb87ba21d022781953434694324a1114edb9b67",
    "scripts/study/api854/fixture_policy.py": "9fcd0fb056b00c60eeec5fe783307f63cf2bf319d79e8d113b270c2445edf5ca"
  },
  "sources": {
    "algorithms/java/SqaProbe.java": "import java.lang.reflect.Array;\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.lang.reflect.Method;\nimport java.lang.reflect.Modifier;\nimport java.nio.charset.StandardCharsets;\nimport java.nio.file.Files;\nimport java.nio.file.Paths;\nimport java.security.MessageDigest;\nimport java.security.NoSuchAlgorithmException;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.util.Base64;\nimport java.util.Comparator;\nimport java.util.List;\n\n/** Fixed-revision observations for explicitly supported, deterministic Java APIs.\n * No buggy source, patch, or triggering test is used during input generation.\n * The same source is packaged with the generated JUnit suite.\n */\npublic final class SqaProbe {\n    private static final String[] STRINGS = {\n        \"\", \"0\", \"1\", \"-1\", \"null\", \"true\", \"false\", \"abc\", \"ABC\", \" \",\n        \"0x0\", \"0x1\", \"0xFFFFFFFF\", \"1.0\", \"1e3\", \"NaN\", \"Infinity\",\n        \"{}\", \"[]\", \"[1]\", \"{\\\"a\\\":1}\", \"a=b\", \"--help\", \"-x\", \"a,b\",\n        \"1970-01-01\", \"a\\\\nb\", \"a\\nb\", \"a\\tb\", \"\\u0e17\\u0e14\\u0e2a\\u0e2d\\u0e1a\"\n    };\n    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,\n        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};\n\n    private SqaProbe() { }\n\n    /** Schema scaffolding carried in the suite; no benchmark test classes. */\n    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }\n    public static class StringBinding extends GenericFixture<String> { }\n    public static class IntegerBinding extends GenericFixture<Integer> { }\n    public static class FixtureBean { public String value = \"fixture-value\"; }\n    public interface FixtureMock { String accept(String value); }\n\n    public static final String EXPLICIT_FIXTURES = \"beam-explicit-fixtures-v3-proposal\";\n    public static final String SCALAR_FIXTURES = \"beam-explicit-fixtures-v4-proposal\";\n    public static final String PILOT_FIXTURES = \"beam-explicit-fixtures-v5-proposal\";\n    public static final String BUFFER_FIXTURES = \"beam-explicit-fixtures-v6-buffer-proposal\";\n    public static final String FRACTION_FIELD_FIXTURES = \"aom-beam-fraction-field-v6-development\";\n    public static final String LANG_HELPER_FIXTURES = \"beam-explicit-fixtures-v9-buffer-lang-development\";\n    public static final String JOINT_FIXTURES = \"aom-beam-champ-joint-fixtures-v10-development\";\n    public static final String CHRONOLOGY_FIXTURES = \"aom-beam-champ-chronology-fixtures-v11-development\";\n    // Diagnostic scope only, serialized by observeChronology. Empty during all\n    // receiver setup/projection calls, so JDI cannot count setup as target entry.\n    public static String chronologyActiveCase = \"\";\n    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();\n    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();\n\n    /** A setup failure is never an observation of an uncalled target method. */\n    private static final class FixtureFailure extends RuntimeException {\n        FixtureFailure(String message, Throwable cause) { super(message, cause); }\n    }\n\n    // Production factories only: no dataset test classes, patches or buggy results.\n    // Reflection keeps the helper compilable without project-specific dependencies.\n    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();\n        while (declaring != null) {\n            try {\n                Method method = declaring.getDeclaredMethod(name, parameterTypes);\n                method.setAccessible(true);\n                return method.invoke(receiver instanceof Class ? null : receiver, values);\n            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n        }\n        throw new NoSuchMethodException(name);\n    }\n\n    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);\n        ctor.setAccessible(true);\n        return ctor.newInstance(values);\n    }\n\n    private static final class FixtureSession {\n        final String targetClass;\n        final String method;\n        final boolean pilot;\n        final boolean bufferSlices;\n        char[] outputBuffer;\n        final boolean fractionField;\n        final boolean langHelpers;\n        final boolean reviewed;\n        Object validationInput;\n        boolean constructing;\n        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;\n        org.w3c.dom.Element domRoot;\n        org.w3c.dom.Node domChild;\n        Object jdomRoot, jdomChild;\n        java.io.ByteArrayOutputStream archiveBytes;\n        Object mapper, parser, context, collectionType, collectionDeserializer;\n        Object mock, baseInvocation, actualInvocation;\n        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;\n        int cleanupNodeIndex;\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void unusedClosure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> ac = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            cleanupExterns = call(compiler, \"parseTestCode\", new Class<?>[]{String.class}, \"\");\n            cleanupScript = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                \"var unused = 1; function fixture(x) { var local = \" + (a < 0 ? \"2\" : \"3\") + \"; return x; } fixture(1);\");\n            // Normalize traverses sibling roots and requires their common parent.\n            int block = Class.forName(\"com.google.javascript.rhino.Token\").getField(\"BLOCK\").getInt(null);\n            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupExterns);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupScript);\n            Object normalize = construct(\"com.google.javascript.jscomp.Normalize\", new Class<?>[]{ac, boolean.class}, compiler, false);\n            call(normalize, \"process\", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);\n            Class<?> lifecycle = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage\");\n            call(compiler, \"setLifeCycleStage\", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, \"NORMALIZED\"));\n            closureNode = cleanupScript;\n        }\n\n        void chart(double a) throws ReflectiveOperationException {\n            if (chartDataset != null) return;\n            Class<?> dataset = Class.forName(\"org.jfree.data.category.CategoryDataset\");\n            Class<?> axis = Class.forName(\"org.jfree.chart.axis.CategoryAxis\");\n            Class<?> valueAxis = Class.forName(\"org.jfree.chart.axis.ValueAxis\");\n            Class<?> renderer = Class.forName(\"org.jfree.chart.renderer.category.CategoryItemRenderer\");\n            chartDataset = construct(\"org.jfree.data.category.DefaultCategoryDataset\", new Class<?>[]{});\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, \"row-a\", \"column-a\");\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, \"row-b\", \"column-a\");\n            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, \"Domain\");\n            Object rangeAxis = construct(\"org.jfree.chart.axis.NumberAxis\", new Class<?>[]{String.class}, \"Range\");\n            chartPlot = construct(\"org.jfree.chart.plot.CategoryPlot\", new Class<?>[]{dataset, axis, valueAxis, renderer},\n                chartDataset, chartAxis, rangeAxis, receiver);\n            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();\n            try {\n                call(receiver, \"initialise\", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,\n                    chartPlot.getClass(), dataset, Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")},\n                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);\n            } finally { graphics.dispose(); }\n        }\n\n        Object beanWriter() throws ReflectiveOperationException {\n            Object objectMapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Object provider = call(objectMapper, \"getSerializerProvider\", new Class<?>[]{});\n            provider = call(provider, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.SerializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.databind.ser.SerializerFactory\")},\n                call(objectMapper, \"getSerializationConfig\", new Class<?>[]{}), call(objectMapper, \"getSerializerFactory\", new Class<?>[]{}));\n            Object serializer = call(provider, \"findValueSerializer\", new Class<?>[]{Class.class, Class.forName(\"com.fasterxml.jackson.databind.BeanProperty\")}, FixtureBean.class, null);\n            return Array.get(field(serializer, \"_props\"), 0);\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void jacksonCollection(double a) throws ReflectiveOperationException {\n            if (mapper != null) return;\n            mapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Class<?> feature = Class.forName(\"com.fasterxml.jackson.databind.DeserializationFeature\");\n            call(mapper, \"configure\", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, \"ACCEPT_SINGLE_VALUE_AS_ARRAY\"), true);\n            Object typeFactory = call(mapper, \"getTypeFactory\", new Class<?>[]{});\n            collectionType = call(typeFactory, \"constructCollectionType\", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);\n            Object factory = call(mapper, \"getFactory\", new Class<?>[]{});\n            String input = method.equals(\"handleNonArray\") ? a < 0 ? \"\\\"alpha\\\"\" : \"\\\"beta\\\"\"\n                : a < 0 ? \"[\\\"alpha\\\",\\\"beta\\\"]\" : \"[\\\"left\\\",\\\"right\\\"]\";\n            parser = call(factory, \"createParser\", new Class<?>[]{String.class}, input);\n            call(parser, \"nextToken\", new Class<?>[]{});\n            Object blueprint = call(mapper, \"getDeserializationContext\", new Class<?>[]{});\n            context = call(blueprint, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.DeserializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.core.JsonParser\"), Class.forName(\"com.fasterxml.jackson.databind.InjectableValues\")},\n                call(mapper, \"getDeserializationConfig\", new Class<?>[]{}), parser, null);\n            collectionDeserializer = call(context, \"findRootValueDeserializer\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.JavaType\")}, collectionType);\n        }\n\n        void mockito(double a) throws ReflectiveOperationException {\n            if (mock != null) return;\n            mock = call(Class.forName(\"org.mockito.Mockito\"), \"mock\", new Class<?>[]{Class.class}, FixtureMock.class);\n            call(mock, \"accept\", new Class<?>[]{String.class}, \"alpha\");\n            call(mock, \"accept\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            Object util = construct(\"org.mockito.internal.util.MockUtil\", new Class<?>[]{});\n            Object handler = call(util, \"getMockHandler\", new Class<?>[]{Object.class}, mock);\n            Object container = call(handler, \"getInvocationContainer\", new Class<?>[]{});\n            List<?> invocations = (List<?>)call(container, \"getInvocations\", new Class<?>[]{});\n            baseInvocation = invocations.get(0);\n            actualInvocation = invocations.get(1);\n        }\n\n        FixtureSession(String targetClass, String method, String policy) {\n            this.targetClass = targetClass;\n            this.method = method;\n            this.reviewed = JOINT_FIXTURES.equals(policy) || CHRONOLOGY_FIXTURES.equals(policy);\n            this.langHelpers = LANG_HELPER_FIXTURES.equals(policy) || reviewed;\n            this.bufferSlices = BUFFER_FIXTURES.equals(policy) || langHelpers;\n            this.fractionField = FRACTION_FIELD_FIXTURES.equals(policy) || langHelpers;\n            this.pilot = PILOT_FIXTURES.equals(policy) || bufferSlices || fractionField;\n        }\n\n        Object[] langHelperArguments(Class<?>[] types, double[] vector) {\n            if (!langHelpers || constructing || !targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    || types.length != 1) return null;\n            double a = vector[0];\n            if (method.equals(\"isAllZeros\") && types[0] == String.class)\n                return new Object[]{new String[]{null, \"\", \"0\", \"000\", \"001\", \"12\", \"00 0\", \"-0\"}[bucket(a, 8)]};\n            if (method.equals(\"validateArray\") && types[0] == Object.class) {\n                Object[] arrays = {null, new int[0], new int[]{0}, new int[]{-1, 0, 7}};\n                validationInput = arrays[bucket(a, arrays.length)];\n                return new Object[]{validationInput};\n            }\n            return null;\n        }\n\n        Object[] boundedBufferArguments(Class<?>[] types, double[] vector) {\n            if (!bufferSlices || constructing || types.length == 0) return null;\n            double a = vector[0], b = vector[1 % vector.length];\n            if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && types[0] == char[].class) {\n                String text;\n                if (method.equals(\"parseLong\"))\n                    text = new String[]{\"1000000000\", \"1234567890123\", \"123456789012345678\"}[bucket(a, 3)];\n                else if (method.equals(\"parseInt\"))\n                    text = new String[]{\"0\", \"7\", \"12345\", \"999999999\"}[bucket(a, 4)];\n                else if (method.equals(\"inLongRange\"))\n                    text = new String[]{\"0\", \"9223372036854775807\", \"9223372036854775808\", \"9223372036854775809\"}[bucket(a, 4)];\n                else if (method.equals(\"parseBigDecimal\"))\n                    text = new String[]{\"0\", \"12.50\", \"-0.125\"}[bucket(a, 3)];\n                else return null;\n                if (types.length == 1) return new Object[]{text.toCharArray()};\n                char[] chars = (\"##\" + text + \"?\").toCharArray();\n                if (types.length == 4) return new Object[]{chars, 2, text.length(), b < 0};\n                return new Object[]{chars, 2, text.length()};\n            }\n            if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\") && method.equals(\"append\")\n                    && types.length == 3 && (types[0] == char[].class || types[0] == String.class)) {\n                String text = a < 0 ? \"xABCDy\" : \"p12345q\";\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, text.length() - offset - 1);\n                return new Object[]{types[0] == char[].class ? text.toCharArray() : text, offset, length};\n            }\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\") && method.equals(\"read\")\n                    && types.length == 3 && types[0] == char[].class) {\n                outputBuffer = new char[8];\n                Arrays.fill(outputBuffer, '~');\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, outputBuffer.length - offset - 1);\n                return new Object[]{outputBuffer, offset, length};\n            }\n            return null;\n        }\n\n        Object option(String name, String text) throws ReflectiveOperationException {\n            Object option = construct(\"org.apache.commons.cli.Option\",\n                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, \"fixture\");\n            call(option, \"setType\", new Class<?>[]{Object.class}, String.class);\n            call(option, \"addValue\", new Class<?>[]{String.class}, text);\n            return option;\n        }\n\n        Object archiveEntry(String name, long size) throws ReflectiveOperationException {\n            Object entry = construct(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\",\n                    new Class<?>[]{String.class}, name);\n            call(entry, \"setSize\", new Class<?>[]{long.class}, size);\n            call(entry, \"setTime\", new Class<?>[]{long.class}, 0L);\n            call(entry, \"setMode\", new Class<?>[]{long.class}, 0100644L);\n            return entry;\n        }\n\n        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {\n            if (!pilot) return value;\n            if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                call(value, \"addOption\", new Class<?>[]{Class.forName(\"org.apache.commons.cli.Option\")}, option(\"x\", a < 0 ? \"alpha\" : \"beta\"));\n                call(value, \"addArg\", new Class<?>[]{String.class}, \"positional\");\n            } else if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\")) {\n                char[] content = (a < 0 ? \"123\" : \"45.5\").toCharArray();\n                call(value, \"resetWithCopy\", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);\n            } else if (targetClass.equals(\"org.jsoup.nodes.Document\")) {\n                Object html = call(value, \"appendElement\", new Class<?>[]{String.class}, \"html\");\n                call(html, \"appendElement\", new Class<?>[]{String.class}, \"head\");\n                Object body = call(html, \"appendElement\", new Class<?>[]{String.class}, \"body\");\n                call(body, \"text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n                call(value, \"title\", new Class<?>[]{String.class}, \"Fixture\");\n            } else if (targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                call(value, \"putNextEntry\", new Class<?>[]{Class.forName(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\")},\n                        archiveEntry(\"fixture.txt\", method.equals(\"write\") ? 1 : 0));\n            } else if (targetClass.equals(\"org.joda.time.Partial\")) {\n                return call(value, \"with\", new Class<?>[]{Class.forName(\"org.joda.time.DateTimeFieldType\"), int.class},\n                        call(Class.forName(\"org.joda.time.DateTimeFieldType\"), \"hourOfDay\", new Class<?>[]{}), 10);\n            } else if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                receiver = value;\n                chart(a);\n            } else if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                // Real StAX input; getters start on a named leaf VALUE_STRING.\n                for (int i = 0; i < 8; i++) {\n                    Object token = call(value, \"nextToken\", new Class<?>[]{});\n                    if (token != null && token.toString().equals(\"VALUE_STRING\")) break;\n                }\n            }\n            return value;\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        Object nativeType(String name, boolean object) throws ReflectiveOperationException {\n            Class<?> nativeClass = Class.forName(\"com.google.javascript.rhino.jstype.JSTypeNative\");\n            Object key = Enum.valueOf((Class)nativeClass, name);\n            return call(registry, object ? \"getNativeObjectType\" : \"getNativeType\", new Class<?>[]{nativeClass}, key);\n        }\n\n        void closure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> scopeClass = Class.forName(\"com.google.javascript.jscomp.Scope\");\n            Class<?> abstractCompiler = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            registry = call(compiler, \"getTypeRegistry\", new Class<?>[]{});\n            String expression = a < 0 ? \"x + 1\" : \"x + 's'\";\n            if (method.contains(\"And\") || method.contains(\"ShortCircuit\")) expression = \"x && true\";\n            if (method.contains(\"Or\")) expression = \"x || false\";\n            if (method.equals(\"traverseArrayLiteral\")) expression = \"[x, 1]\";\n            if (method.equals(\"traverseObjectLiteral\")) expression = \"({p:x})\";\n            if (method.equals(\"traverseHook\")) expression = \"x ? 1 : 2\";\n            if (method.equals(\"traverseAssign\")) expression = \"x = 2\";\n            if (method.equals(\"traverseGetElem\")) expression = \"x['p']\";\n            if (method.equals(\"traverseGetProp\") || method.contains(\"Property\")) expression = \"x.p\";\n            if (method.equals(\"traverseName\") || method.equals(\"redeclareSimpleVar\")\n                    || method.equals(\"narrowScope\") || method.equals(\"updateScopeForTypeChange\")) expression = \"x\";\n            Object script = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                    \"function fixture(x) { return \" + expression + \"; }\");\n            Object function = call(script, \"getFirstChild\", new Class<?>[]{});\n            Object global = call(scopeClass, \"createGlobalScope\", new Class<?>[]{node}, script);\n            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);\n            Object astParameters = call(call(function, \"getFirstChild\", new Class<?>[]{}), \"getNext\", new Class<?>[]{});\n            Object name = call(astParameters, \"getFirstChild\", new Class<?>[]{});\n            call(scope, \"declare\", new Class<?>[]{String.class, node,\n                    Class.forName(\"com.google.javascript.rhino.jstype.JSType\"),\n                    Class.forName(\"com.google.javascript.jscomp.CompilerInput\")}, \"x\", name, nativeType(\"UNKNOWN_TYPE\", false), null);\n            Object body = call(function, \"getLastChild\", new Class<?>[]{});\n            Object returnNode = call(body, \"getFirstChild\", new Class<?>[]{});\n            closureNode = method.equals(\"traverseReturn\") || method.equals(\"branchedFlowThrough\")\n                    ? returnNode : call(returnNode, \"getFirstChild\", new Class<?>[]{});\n            if (method.equals(\"traverseObjectLiteral\"))\n                call(closureNode, \"setJSType\", new Class<?>[]{Class.forName(\"com.google.javascript.rhino.jstype.JSType\")}, nativeType(\"OBJECT_TYPE\", true));\n            Object analysis = construct(\"com.google.javascript.jscomp.ControlFlowAnalysis\",\n                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);\n            call(analysis, \"process\", new Class<?>[]{node, node}, null, function);\n            cfg = call(analysis, \"getCfg\", new Class<?>[]{});\n            Object convention = call(compiler, \"getCodingConvention\", new Class<?>[]{});\n            reverse = construct(\"com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter\",\n                    new Class<?>[]{Class.forName(\"com.google.javascript.jscomp.CodingConvention\"), registry.getClass()}, convention, registry);\n            flow = call(Class.forName(\"com.google.javascript.jscomp.LinkedFlowScope\"), \"createEntryLattice\",\n                    new Class<?>[]{scopeClass}, scope);\n            call(flow, \"inferSlotType\", new Class<?>[]{String.class, Class.forName(\"com.google.javascript.rhino.jstype.JSType\")},\n                    \"x\", nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false));\n        }\n\n        void dom(double a) throws Exception {\n            if (domRoot != null) return;\n            javax.xml.parsers.DocumentBuilderFactory factory = pilot\n                ? javax.xml.parsers.DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", SqaProbe.class.getClassLoader())\n                : javax.xml.parsers.DocumentBuilderFactory.newInstance();\n            factory.setNamespaceAware(true);\n            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();\n            domRoot = document.createElementNS(\"urn:sqa:root\", \"r:root\");\n            document.appendChild(domRoot);\n            domRoot.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:r\", \"urn:sqa:root\");\n            domRoot.setAttributeNS(\"http://www.w3.org/XML/1998/namespace\", \"xml:lang\", \"en\");\n            org.w3c.dom.Element element = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            domChild = element;\n            element.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:i\", \"urn:sqa:item\");\n            element.setAttribute(\"id\", a < 0 ? \"left\" : \"right\");\n            domChild.appendChild(document.createTextNode(a < 0 ? \"alpha\" : \"beta\"));\n            org.w3c.dom.Element grandchild = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            grandchild.appendChild(document.createTextNode(\"nested\"));\n            domChild.appendChild(grandchild);\n            org.w3c.dom.Element last = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            last.appendChild(document.createTextNode(\"nested-last\"));\n            domChild.appendChild(last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                domRoot.appendChild(document.createProcessingInstruction(\"fixture\", \"before\"));\n                domChild = document.createProcessingInstruction(\"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                domRoot.appendChild(document.createCDATASection(\"before\"));\n                domChild = document.createTextNode(a < 0 ? \"alpha\" : \"beta\");\n            }\n            domRoot.appendChild(domChild);\n        }\n\n        void jdom(double a) throws ReflectiveOperationException {\n            if (jdomRoot != null) return;\n            Class<?> element = Class.forName(\"org.jdom.Element\");\n            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, \"root\");\n            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(jdomChild, \"setText\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            call(jdomChild, \"setAttribute\", new Class<?>[]{String.class, String.class}, \"id\", a < 0 ? \"left\" : \"right\");\n            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(grandchild, \"setText\", new Class<?>[]{String.class}, \"nested\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, grandchild);\n            Object last = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(last, \"setText\", new Class<?>[]{String.class}, \"nested-last\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                Object before = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                Object before = construct(\"org.jdom.CDATA\", new Class<?>[]{String.class}, \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.Text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            }\n            call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, jdomChild);\n        }\n\n        void configurePointer(Object pointer) throws ReflectiveOperationException {\n            Class<?> resolverClass = Class.forName(\"org.apache.commons.jxpath.ri.NamespaceResolver\");\n            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"i\", \"urn:sqa:item\");\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"r\", \"urn:sqa:root\");\n            call(resolver, \"setNamespaceContextPointer\", new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\")}, pointer);\n            call(pointer, \"setNamespaceResolver\", new Class<?>[]{resolverClass}, resolver);\n        }\n\n        Object argument(Class<?> type, double a, double b, double c, int depth) {\n            try {\n                if (depth > 2) throw new FixtureFailure(\"Fixture recursion limit: \" + type.getName(), null);\n                String name = type.getName();\n                if (reviewed && !constructing && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                        && method.equals(\"setMaxCodeLen\") && type == int.class)\n                    return a < -8 ? 0 : a < 0 ? 1 : a < 8 ? 4 : 8;\n                if (pilot) {\n                    if (targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? \"value\" : \"items\");\n                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];\n                        if (type == java.lang.reflect.Field.class) return value;\n                        if (type == Class.class) return GenericFixture.class;\n                        if (type == java.lang.reflect.Type.class) {\n                            if (method.equals(\"getTypeInfoForArray\")) return a < 0 ? String[].class : Integer[].class;\n                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                        }\n                    }\n                    if (targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\")) {\n                        unusedClosure(a);\n                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.\n                        if (name.equals(\"com.google.javascript.jscomp.AbstractCompiler\")) return compiler;\n                        if (name.equals(\"com.google.javascript.rhino.Node\")) {\n                            if (method.equals(\"process\")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;\n                            if (method.equals(\"getFunctionArgList\")) {\n                                Object child = call(cleanupScript, \"getFirstChild\", new Class<?>[]{});\n                                while (child != null && !(Boolean)call(child, \"isFunction\", new Class<?>[]{}))\n                                    child = call(child, \"getNext\", new Class<?>[]{});\n                                if (child == null) throw new FixtureFailure(\"Missing parsed function\", null);\n                                return child;\n                            }\n                            return cleanupScript;\n                        }\n                    }\n                    if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                        chart(a);\n                        if (name.equals(\"org.jfree.data.category.CategoryDataset\")) return chartDataset;\n                        if (name.equals(\"org.jfree.chart.axis.CategoryAxis\")) return chartAxis;\n                        if (type == Comparable.class) return a < 0 ? \"row-a\" : \"column-a\";\n                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);\n                        if (name.equals(\"org.jfree.chart.util.RectangleEdge\")) return type.getField(\"BOTTOM\").get(null);\n                        if (type == int.class) return 0;\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\")) {\n                        if (name.equals(targetClass)) return beanWriter();\n                        if (name.equals(\"com.fasterxml.jackson.databind.util.NameTransformer\"))\n                            return call(type, \"simpleTransformer\", new Class<?>[]{String.class, String.class}, a < 0 ? \"left_\" : \"right_\", \"_suffix\");\n                        if (type == Object.class) return method.equals(\"get\") ? new FixtureBean() : a < 0 ? \"fixture-key\" : \"fixture-value\";\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\")) {\n                        jacksonCollection(a);\n                        if (name.equals(\"com.fasterxml.jackson.databind.JavaType\")) return collectionType;\n                        if (name.equals(\"com.fasterxml.jackson.core.JsonParser\")) return parser;\n                        if (name.equals(\"com.fasterxml.jackson.databind.DeserializationContext\")) return context;\n                        if (name.equals(\"com.fasterxml.jackson.databind.deser.ValueInstantiator\"))\n                            return call(collectionDeserializer, \"getValueInstantiator\", new Class<?>[]{});\n                        if (name.equals(\"com.fasterxml.jackson.databind.JsonDeserializer\"))\n                            return Class.forName(\"com.fasterxml.jackson.databind.deser.std.StringDeserializer\").getField(\"instance\").get(null);\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                        String xml = a < 0 ? \"<root><item>123</item><other>alpha</other></root>\" : \"<root><item>45</item><other>beta</other></root>\";\n                        if (type == int.class && constructing) return 0;\n                        if (name.equals(\"com.fasterxml.jackson.core.io.IOContext\"))\n                            return construct(name, new Class<?>[]{Class.forName(\"com.fasterxml.jackson.core.util.BufferRecycler\"), Object.class, boolean.class},\n                                construct(\"com.fasterxml.jackson.core.util.BufferRecycler\", new Class<?>[]{}), xml, false);\n                        if (name.equals(\"com.fasterxml.jackson.core.ObjectCodec\")) return construct(\"com.fasterxml.jackson.dataformat.xml.XmlMapper\", new Class<?>[]{});\n                        if (type == javax.xml.stream.XMLStreamReader.class) {\n                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));\n                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();\n                            return reader;\n                        }\n                    }\n                    if (targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")) {\n                        mockito(a);\n                        if (name.equals(\"org.mockito.invocation.Invocation\")) return constructing ? baseInvocation : actualInvocation;\n                    }\n                    if (targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) {\n                        int number = 1 + bucket(a, 8);\n                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;\n                        if (type == int.class) return number;\n                        if (type == long.class) return (long)number;\n                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);\n                        if (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\"))\n                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);\n                    }\n                    if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                        if (type == String.class) return constructing ? \"fixture\" : a < -0.33 ? \"x\" : a < 0.33 ? \"missing\" : \"extra\";\n                        if (type == char.class) return a < 0 ? 'x' : 'z';\n                        if (name.equals(\"org.apache.commons.cli.Option\")) return option(\"extra\", a < 0 ? \"left\" : \"right\");\n                    }\n                    if (targetClass.equals(\"org.jsoup.nodes.Document\") && type == String.class)\n                        return constructing ? \"https://fixture.invalid/\" : method.equals(\"createElement\") ? a < 0 ? \"span\" : \"section\"\n                            : STRINGS[bucket(a, STRINGS.length)];\n                    if (targetClass.equals(\"org.joda.time.Partial\")) {\n                        if (type == int.class) return bucket(a, 24);\n                        if (name.equals(\"org.joda.time.DateTimeFieldType\"))\n                            return call(type, \"hourOfDay\", new Class<?>[]{});\n                    }\n                    if (name.equals(\"org.joda.time.DurationFieldType\")) return call(type, a < 0 ? \"hours\" : \"days\", new Class<?>[]{});\n                    if (name.equals(\"org.joda.time.DurationField\")) return call(Class.forName(\"org.joda.time.field.UnsupportedDurationField\"),\n                        \"getInstance\", new Class<?>[]{Class.forName(\"org.joda.time.DurationFieldType\")},\n                        call(Class.forName(\"org.joda.time.DurationFieldType\"), \"hours\", new Class<?>[]{}));\n                    if (name.equals(\"com.fasterxml.jackson.core.util.BufferRecycler\")) return construct(name, new Class<?>[]{});\n                    if (type == java.io.OutputStream.class && targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                        archiveBytes = new java.io.ByteArrayOutputStream();\n                        return archiveBytes;\n                    }\n                    if (name.equals(\"org.apache.commons.compress.archivers.ArchiveEntry\") || name.equals(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\"))\n                        return archiveEntry(a < 0 ? \"next-left.txt\" : \"next-right.txt\", 0);\n                    if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && type == String.class)\n                        return new String[]{\"0\", \"1\", \"12\", \"2147483647\"}[bucket(a, 4)];\n                }\n                if (scalar(type)) {\n                    if (type == String.class && method.equals(\"getRelativePositionOfPI\")) return a < 0 ? \"fixture\" : \"other\";\n                    if (type == String.class && (method.equals(\"namespacePointer\") || method.equals(\"getNamespaceURI\")))\n                        return a < 0 ? \"r\" : \"i\";\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                }\n                if (type.isArray()) {\n                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith(\"NumberUtils\") || targetClass.endsWith(\"TypeInfoFactory\")) ? 1 + bucket(c, 4) : bucket(c, 5));\n                    for (int i = 0; i < Array.getLength(array); i++)\n                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));\n                    return array;\n                }\n                if (type == java.io.Reader.class && targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                    return new java.io.StringReader(bufferSlices ? (a < 0 ? \"A\\nBC\\nDE\" : \"12\\n345\\n\") : STRINGS[bucket(a, STRINGS.length)]);\n                if (name.startsWith(\"com.google.javascript.\")) {\n                    closure(a);\n                    if (name.endsWith(\".AbstractCompiler\")) return compiler;\n                    if (name.endsWith(\".ControlFlowGraph\")) return cfg;\n                    if (name.endsWith(\".ReverseAbstractInterpreter\")) return reverse;\n                    if (name.endsWith(\".Scope\")) return scope;\n                    if (name.endsWith(\".Scope$Var\")) return call(scope, \"getVar\", new Class<?>[]{String.class}, \"x\");\n                    if (name.endsWith(\".FlowScope\")) return flow;\n                    if (name.endsWith(\".Node\")) return closureNode;\n                    if (name.endsWith(\".JSType\")) return nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false);\n                    if (name.endsWith(\".ObjectType\")) return nativeType(\"OBJECT_TYPE\", true);\n                }\n                if (name.startsWith(\"org.w3c.dom.\")) {\n                    dom(a);\n                    if (type.isInstance(domChild)) return domChild;\n                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();\n                }\n                if (type == java.util.Locale.class) return java.util.Locale.ROOT;\n                if (name.equals(\"org.apache.commons.jxpath.ri.QName\"))\n                    return construct(name, new Class<?>[]{String.class}, method.equals(\"attributeIterator\") ? \"id\" : \"item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.compiler.NodeTest\"))\n                    return construct(\"org.apache.commons.jxpath.ri.compiler.NodeNameTest\",\n                            new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.QName\"), String.class},\n                            targetClass.contains(\".jdom.\")\n                                ? construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class}, \"item\")\n                                : construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class, String.class}, \"i\", \"item\"),\n                            targetClass.contains(\".jdom.\") ? null : \"urn:sqa:item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.model.NodePointer\")) {\n                    if (targetClass.contains(\".jdom.\")) {\n                        jdom(a);\n                        if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                            List<?> children = (List<?>)call(jdomChild, \"getContent\", new Class<?>[]{});\n                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);\n                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);\n                            configurePointer(pointer);\n                            return pointer;\n                        }\n                        Object pointer = construct(\"org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer\",\n                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    dom(a);\n                    if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();\n                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    Object pointer = construct(\"org.apache.commons.jxpath.ri.model.dom.DOMNodePointer\",\n                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);\n                    configurePointer(pointer);\n                    return pointer;\n                }\n                if (type == Object.class && targetClass.contains(\".jdom.\")\n                        && (constructing || !method.equals(\"setValue\"))) { jdom(a); return jdomChild; }\n                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();\n                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n                    return new ArrayList<Object>();\n                if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n                if (type == Object.class || type == Number.class || type == java.util.Date.class)\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                throw new FixtureFailure(\"No explicit recipe: \" + name, null);\n            } catch (FixtureFailure failure) { throw failure; }\n            catch (Exception failure) { throw new FixtureFailure(\"Fixture recipe failed: \" + type.getName()\n                    + \":\" + failure.getClass().getName() + \":\" + failure.getMessage(), failure); }\n        }\n\n        String nodeSnapshot(org.w3c.dom.Node node, int depth) {\n            if (depth > 8) return \"depth-limit\";\n            StringBuilder out = new StringBuilder(\"node:\").append(node.getNodeType()).append(':')\n                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));\n            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();\n            List<String> attrs = new ArrayList<String>();\n            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)\n                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));\n            java.util.Collections.sort(attrs);\n            out.append(attrs.toString()).append('[');\n            org.w3c.dom.NodeList children = node.getChildNodes();\n            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));\n            return out.append(\"]children:\").append(children.getLength()).toString();\n        }\n\n        Object field(Object value, String name) throws ReflectiveOperationException {\n            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {\n                try {\n                    java.lang.reflect.Field field = type.getDeclaredField(name);\n                    field.setAccessible(true);\n                    return field.get(value);\n                } catch (NoSuchFieldException missing) { }\n            }\n            throw new NoSuchFieldException(name);\n        }\n\n        String projection(Object result, int depth) throws ReflectiveOperationException {\n            if (depth > 8) throw new FixtureFailure(\"Oracle projection depth exceeded\", null);\n            if (result == null) return \"null\";\n            String name = result.getClass().getName();\n            if (fractionField && (name.equals(\"org.apache.commons.math3.fraction.BigFractionField\")\n                    || name.equals(\"org.apache.commons.math3.fraction.FractionField\")))\n                return \"fraction-field:runtime=\" + projection(call(result, \"getRuntimeClass\", new Class<?>[]{}), depth + 1)\n                    + \":zero=\" + projection(call(result, \"getZero\", new Class<?>[]{}), depth + 1)\n                    + \":one=\" + projection(call(result, \"getOne\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.lang.reflect.Type) return \"type:\" + nestedTestName(((java.lang.reflect.Type)result).getTypeName());\n            if (pilot && result instanceof Method) return \"method:\" + nestedTestName(((Method)result).toGenericString());\n            if (pilot && name.startsWith(\"com.google.gson.TypeInfo\"))\n                return \"type-info:\" + projection(call(result, \"getActualType\", new Class<?>[]{}), depth + 1);\n            if (pilot && name.equals(\"com.google.javascript.rhino.Node\")) return \"ast:\" + call(result, \"toStringTree\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.jxpath.ri.NamespaceResolver\"))\n                return \"namespaces:r=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"r\")\n                    + \":i=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"i\");\n            if (pilot && name.equals(\"org.jfree.data.Range\"))\n                return \"range:\" + call(result, \"getLowerBound\", new Class<?>[]{}) + ':' + call(result, \"getUpperBound\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItem\")) return \"legend:\" + call(result, \"getLabel\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItemCollection\")) {\n                StringBuilder out = new StringBuilder(\"legends[\");\n                int count = ((Number)call(result, \"getItemCount\", new Class<?>[]{})).intValue();\n                if (count > 256) throw new FixtureFailure(\"Legend limit exceeded\", null);\n                for (int i = 0; i < count; i++) out.append(projection(call(result, \"get\", new Class<?>[]{int.class}, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && name.startsWith(\"com.fasterxml.jackson.databind.type.\")) return \"java-type:\" + call(result, \"toCanonical\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.core.io.SerializedString\")) return \"serialized-name:\" + call(result, \"getValue\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return \"property:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + projection(call(result, \"getType\", new Class<?>[]{}), depth + 1);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")\n                    && Class.forName(\"org.mockito.invocation.Invocation\").isInstance(result))\n                return \"invocation:\" + projection(call(result, \"getMethod\", new Class<?>[]{}), depth + 1)\n                    + ':' + projection(call(result, \"getArguments\", new Class<?>[]{}), depth + 1)\n                    + \":verified=\" + call(result, \"isVerified\", new Class<?>[]{});\n            if (pilot && result.getClass().isArray()) {\n                int length = Array.getLength(result);\n                if (length > 100000) throw new FixtureFailure(\"Oracle array limit exceeded\", null);\n                StringBuilder out = new StringBuilder(\"array[\");\n                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.jsoup.nodes.Document\") || name.equals(\"org.jsoup.nodes.Element\")))\n                return \"html:\" + call(result, \"outerHtml\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.cli.Option\"))\n                return \"option:\" + call(result, \"getOpt\", new Class<?>[]{}) + ':' + projection(call(result, \"getValues\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.util.Iterator) {\n                StringBuilder out = new StringBuilder(\"iterator[\");\n                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;\n                int count = 0;\n                while (iterator.hasNext()) {\n                    if (++count > 256) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                    out.append(projection(iterator.next(), depth + 1)).append(';');\n                }\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\")))\n                return \"fraction:\" + call(result, \"getNumerator\", new Class<?>[]{}) + '/' + call(result, \"getDenominator\", new Class<?>[]{});\n            if (pilot && name.startsWith(\"org.joda.time.\")) {\n                if (name.equals(\"org.joda.time.Partial\")) return \"partial:\" + call(result, \"toStringList\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationFieldType\").isInstance(result)) return \"duration-type:\" + call(result, \"getName\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationField\").isInstance(result))\n                    return \"duration:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + call(result, \"isSupported\", new Class<?>[]{});\n            }\n            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);\n            if (reviewed && name.equals(\"org.jdom.Attribute\"))\n                return \"jdom-attribute:name=\" + projection(call(result, \"getName\", new Class<?>[]{}), depth + 1)\n                    + \":namespace=\" + projection(call(result, \"getNamespaceURI\", new Class<?>[]{}), depth + 1)\n                    + \":value=\" + projection(call(result, \"getValue\", new Class<?>[]{}), depth + 1);\n            if (name.equals(\"org.jdom.Element\") || name.equals(\"org.jdom.ProcessingInstruction\")\n                    || name.equals(\"org.jdom.Text\") || name.equals(\"org.jdom.CDATA\")) {\n                Object writer = construct(\"org.jdom.output.XMLOutputter\", new Class<?>[]{});\n                return \"xml:\" + call(writer, \"outputString\", new Class<?>[]{result.getClass()}, result);\n            }\n            if (name.equals(\"org.apache.commons.jxpath.ri.QName\")) return \"qname:\" + result.toString();\n            if (name.startsWith(\"com.google.javascript.rhino.jstype.\")) return \"js-type:\" + result.toString();\n            if (name.equals(\"com.google.javascript.jscomp.LinkedFlowScope\")) {\n                Object slot = call(result, \"getSlot\", new Class<?>[]{String.class}, \"x\");\n                return \"flow:x=\" + (slot == null ? \"absent\" : projection(call(slot, \"getType\", new Class<?>[]{}), depth + 1));\n            }\n            if (name.endsWith(\"TypeInference$BooleanOutcomePair\"))\n                return \"boolean-pair:\" + field(result, \"toBooleanOutcomes\") + ':' + field(result, \"booleanValues\")\n                    + \":left=\" + projection(field(result, \"leftScope\"), depth + 1)\n                    + \":right=\" + projection(field(result, \"rightScope\"), depth + 1);\n            if (result instanceof List) {\n                StringBuilder out = new StringBuilder(\"list[\");\n                if (((List<?>)result).size() > 256) throw new FixtureFailure(\"Oracle collection limit exceeded\", null);\n                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (result instanceof java.util.Map) {\n                java.util.Map<?,?> map = (java.util.Map<?,?>)result;\n                if (map.size() > 256) throw new FixtureFailure(\"Oracle map limit exceeded\", null);\n                List<String> entries = new ArrayList<String>();\n                for (java.util.Map.Entry<?,?> entry : map.entrySet())\n                    entries.add(projection(entry.getKey(), depth + 1) + \"=\" + projection(entry.getValue(), depth + 1));\n                java.util.Collections.sort(entries);\n                return \"map:\" + entries.toString();\n            }\n            if (name.startsWith(\"org.apache.commons.jxpath.ri.model.\")) {\n                Class<?> pointer = Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\");\n                if (pointer.isInstance(result))\n                    return \"pointer:\" + projection(call(result, \"getImmediateNode\", new Class<?>[]{}), depth + 1);\n                if (Class.forName(\"org.apache.commons.jxpath.ri.model.NodeIterator\").isInstance(result)) {\n                    StringBuilder out = new StringBuilder(\"iterator[\");\n                    for (int i = 1; i <= 9; i++) {\n                        boolean present = (Boolean)call(result, \"setPosition\", new Class<?>[]{int.class}, i);\n                        if (!present) return out.append(']').toString();\n                        if (i == 9) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                        out.append(projection(call(result, \"getNodePointer\", new Class<?>[]{}), depth + 1)).append(';');\n                    }\n                }\n            }\n            String simple = value(result);\n            if (simple.startsWith(\"object-type:\")) throw new FixtureFailure(\"No structural oracle: \" + name, null);\n            return simple;\n        }\n\n        String state() throws ReflectiveOperationException {\n            if (reviewed && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                    && method.equals(\"setMaxCodeLen\")) {\n                int limit = ((Number)call(receiver, \"getMaxCodeLen\", new Class<?>[]{})).intValue();\n                String encoded = (String)call(receiver, \"metaphone\", new Class<?>[]{String.class}, \"architecture\");\n                return \"metaphone:maxCodeLen=\" + limit + \":encoded=\" + encoded\n                    + \":maxCodeLenAfterEncoding=\" + call(receiver, \"getMaxCodeLen\", new Class<?>[]{});\n            }\n            if (langHelpers && targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && method.equals(\"validateArray\")) return \"validation-input:\" + value(validationInput);\n            if (pilot && targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\"))\n                return \"cleanup:\" + call(cleanupScript, \"toStringTree\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\"))\n                return \"chart:rows=\" + call(chartDataset, \"getRowCount\", new Class<?>[]{}) + \":columns=\" + call(chartDataset, \"getColumnCount\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return projection(receiver, 0) + \":setting=\" + projection(call(receiver, \"getInternalSetting\", new Class<?>[]{Object.class}, \"fixture-key\"), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\"))\n                return \"json-token:\" + call(parser, \"getCurrentToken\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\"))\n                return \"xml:closed=\" + call(receiver, \"isClosed\", new Class<?>[]{}) + \":token=\" + call(receiver, \"getCurrentToken\", new Class<?>[]{})\n                    + \":text=\" + projection(field(receiver, \"_currText\"), 0);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\"))\n                return projection(baseInvocation, 0) + \":candidate=\" + projection(actualInvocation, 0);\n            if (pilot && targetClass.equals(\"org.apache.commons.cli.CommandLine\"))\n                return \"cli:\" + projection(call(receiver, \"getOptions\", new Class<?>[]{}), 0) + ':' + projection(call(receiver, \"getArgs\", new Class<?>[]{}), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\"))\n                return \"text:\" + call(receiver, \"contentsAsString\", new Class<?>[]{}) + \":size=\" + call(receiver, \"size\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jsoup.nodes.Document\") && receiver != null) return projection(receiver, 0);\n            if (pilot && targetClass.endsWith(\"CpioArchiveOutputStream\")) return \"archive:\" + value(archiveBytes.toByteArray());\n            if (pilot && targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.Partial\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.field.UnsupportedDurationField\") && receiver != null) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.collections.map.Flat3Map\")) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                return \"reader:line=\" + call(receiver, \"getLineNumber\", new Class<?>[]{})\n                    + \":last=\" + call(receiver, \"readAgain\", new Class<?>[]{})\n                    + (bufferSlices && outputBuffer != null ? \":buffer=\" + value(outputBuffer) : \"\");\n            if (compiler != null) {\n                Object jsType = call(closureNode, \"getJSType\", new Class<?>[]{});\n                return \"ast:\" + call(closureNode, \"toStringTree\", new Class<?>[]{})\n                    + \":ast-type=\" + projection(jsType, 0) + ':' + projection(flow, 0);\n            }\n            if (domRoot != null) return nodeSnapshot(domRoot, 0) + \":child=\" + nodeSnapshot(domChild, 0)\n                    + \":attached=\" + (domChild.getParentNode() != null);\n            if (jdomRoot != null) return projection(jdomRoot, 0) + \":child=\" + projection(jdomChild, 0)\n                    + \":attached=\" + (call(jdomChild, \"getParent\", new Class<?>[]{}) != null);\n            return \"stateless-scalars\";\n        }\n    }\n\n    private static String quote(String value) {\n        StringBuilder out = new StringBuilder(\"\\\"\");\n        for (char c : value.toCharArray()) {\n            if (c == '\"' || c == '\\\\') out.append('\\\\').append(c);\n            else if (c < 32) out.append(String.format(\"\\\\u%04x\", (int)c));\n            else out.append(c);\n        }\n        return out.append('\"').toString();\n    }\n\n    private static String typeNames(Class<?>[] types) {\n        List<String> names = new ArrayList<String>();\n        for (Class<?> type : types) names.add(type.getName());\n        return String.join(\",\", names);\n    }\n\n    private static boolean scalar(Class<?> type) {\n        return type.isPrimitive() || type == String.class || type == Boolean.class\n            || type == Character.class || type == Byte.class || type == Short.class\n            || type == Integer.class || type == Long.class || type == Float.class\n            || type == Double.class || type.isEnum();\n    }\n\n    private static boolean supported(Class<?> type) {\n        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));\n    }\n\n    private static boolean supportedParameters(Class<?>[] types) {\n        if (types.length > 6) return false;\n        for (Class<?> type : types) if (type == void.class) return false;\n        return true;\n    }\n\n    private static Class<?> type(String name) throws ClassNotFoundException {\n        if (name.equals(\"boolean\")) return boolean.class;\n        if (name.equals(\"byte\")) return byte.class;\n        if (name.equals(\"short\")) return short.class;\n        if (name.equals(\"int\")) return int.class;\n        if (name.equals(\"long\")) return long.class;\n        if (name.equals(\"float\")) return float.class;\n        if (name.equals(\"double\")) return double.class;\n        if (name.equals(\"char\")) return char.class;\n        return Class.forName(name);\n    }\n\n    private static Class<?>[] types(String names) throws ClassNotFoundException {\n        if (names.length() == 0) return new Class<?>[0];\n        String[] split = names.split(\",\", -1);\n        Class<?>[] result = new Class<?>[split.length];\n        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);\n        return result;\n    }\n\n    private static int bucket(double coordinate, int size) {\n        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));\n        return Math.min(size - 1, (int)(unit * size));\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c) {\n        return argument(type, a, b, c, 0);\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c, int depth) {\n        FixtureSession session = FIXTURES.get();\n        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);\n    }\n\n    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {\n        if (depth > 2) return null;\n        if (type.isArray()) {\n            int length = bucket(c, 5);\n            Object array = Array.newInstance(type.getComponentType(), length);\n            for (int i = 0; i < length; i++) {\n                Array.set(array, i, argument(type.getComponentType(),\n                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));\n            }\n            return array;\n        }\n        if (!type.isPrimitive() && a < -0.96) return null;\n        if (type == String.class) {\n            int selection = bucket(a, STRINGS.length + 4);\n            if (selection < STRINGS.length) return STRINGS[selection];\n            int length = bucket(c, 33);\n            char character = \"0123456789abcdefXYZ +-_.\".charAt(bucket(b, 23));\n            char[] value = new char[length];\n            Arrays.fill(value, character);\n            return new String(value);\n        }\n        if (type == boolean.class || type == Boolean.class) return a >= 0;\n        if (type == char.class || type == Character.class) return (char)bucket(a, 128);\n        if (type.isEnum()) {\n            Object[] values = type.getEnumConstants();\n            return values.length == 0 ? null : values[bucket(a, values.length)];\n        }\n        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);\n        if (type == byte.class || type == Byte.class) return (byte)integer;\n        if (type == short.class || type == Short.class) return (short)integer;\n        if (type == int.class || type == Integer.class) return (int)integer;\n        if (type == long.class || type == Long.class) return integer;\n        double real = b < 0 ? integer : a * 1000;\n        if (type == float.class || type == Float.class) return (float)real;\n        if (type == double.class || type == Double.class) return real;\n        if (type == Number.class) return Double.valueOf(real);\n        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);\n        if (type == java.util.Date.class) return new java.util.Date(integer);\n        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n            return new java.util.ArrayList<Object>();\n        if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith(\"java.\")) {\n            Constructor<?>[] constructors = type.getDeclaredConstructors();\n            Arrays.sort(constructors, new Comparator<Constructor<?>>() {\n                public int compare(Constructor<?> left, Constructor<?> right) {\n                    int count = left.getParameterCount() - right.getParameterCount();\n                    return count != 0 ? count : left.toString().compareTo(right.toString());\n                }\n            });\n            for (Constructor<?> constructor : constructors) {\n                if (constructor.getParameterCount() > 3) continue;\n                try {\n                    constructor.setAccessible(true);\n                    Class<?>[] parameters = constructor.getParameterTypes();\n                    Object[] values = new Object[parameters.length];\n                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);\n                    return constructor.newInstance(values);\n                } catch (ReflectiveOperationException error) {\n                    // Failed fixture construction yields an explicit null boundary input.\n                } catch (RuntimeException error) {\n                    // Encapsulated/unconstructible fixture yields the same null boundary.\n                }\n            }\n        }\n        return null;\n    }\n\n    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {\n        FixtureSession explicitSession = FIXTURES.get();\n        if (explicitSession != null) {\n            Object[] helpers = explicitSession.langHelperArguments(types, vector);\n            if (helpers != null) return helpers;\n            Object[] bounded = explicitSession.boundedBufferArguments(types, vector);\n            if (bounded != null) return bounded;\n        }\n        Object[] values = new Object[types.length];\n        for (int i = 0; i < types.length; i++) {\n            int start = offset + 3 * i;\n            values[i] = argument(types[i], vector[start % vector.length],\n                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);\n        }\n        FixtureSession session = FIXTURES.get();\n        if (session != null && session.pilot && !session.constructing) {\n            if (session.targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                try {\n                    if (session.method.equals(\"getActualType\")) {\n                        values[0] = GenericFixture.class.getField(\"items\").getGenericType();\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    } else if (session.method.equals(\"extractRealTypes\")) {\n                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField(\"value\").getGenericType()};\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    }\n                } catch (NoSuchFieldException failure) { throw new FixtureFailure(\"Generic schema field missing\", failure); }\n            }\n            if (session.targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\") && session.method.equals(\"getItemMiddle\")) {\n                values[0] = \"row-a\";\n                values[1] = \"column-a\";\n            }\n        }\n        return values;\n    }\n\n    private static String value(Object value) {\n        if (value == null) return \"null\";\n        Class<?> type = value.getClass();\n        if (type.isArray()) {\n            StringBuilder out = new StringBuilder(type.getName()).append('[');\n            int length = Array.getLength(value);\n            if (length > 100000) throw new IllegalStateException(\"SQA_HARNESS oversized outcome\");\n            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');\n            return out.append(']').toString();\n        }\n        if (value instanceof Class) return \"class:\" + nestedTestName(((Class<?>)value).getName());\n        if (!scalar(type) && !(value instanceof Number)) return \"object-type:\" + type.getName();\n        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);\n        return type.getName() + \":\" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));\n    }\n\n    private static String nestedTestName(String text) {\n        // GeneratedStudyTest nests a copy of this helper, so probe-time\n        // \"SqaProbe$FixtureMock\" renders at test runtime as\n        // \"GeneratedStudyTest$SqaProbe$FixtureMock\". Oracles must compare\n        // the probe-time spelling in both phases; never edit old suites.\n        return text.replace(\"GeneratedStudyTest$SqaProbe$\", \"SqaProbe$\");\n    }\n\n    private static String snapshot(String observed) {\n        // JVM string constants are limited to 65,535 encoded bytes. Long exact\n        // observations use a deterministic digest rather than enormous literals.\n        if (observed.length() <= 16000) return observed;\n        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);\n        try {\n            byte[] digest = MessageDigest.getInstance(\"SHA-256\").digest(bytes);\n            StringBuilder hex = new StringBuilder();\n            for (byte item : digest) hex.append(String.format(\"%02x\", item & 255));\n            return \"sha256:\" + hex + \":bytes:\" + bytes.length;\n        } catch (NoSuchAlgorithmException error) {\n            throw new IllegalStateException(\"SQA_HARNESS SHA-256 unavailable\", error);\n        }\n    }\n\n    public static String observe(String className, String constructorTypes, String methodName,\n                                 String methodTypes, double[] vector) {\n        INVOKED.set(false);\n        if (vector.length == 0) throw new IllegalArgumentException(\"SQA_HARNESS empty vector\");\n        try {\n            Class<?> target = Class.forName(className);\n            Class<?>[] ctorTypes = types(constructorTypes);\n            Class<?>[] parameterTypes = types(methodTypes);\n            Object receiver = null;\n            Method method = null;\n            if (!methodName.equals(\"<init>\")) {\n                Class<?> declaring = target;\n                while (declaring != null) {\n                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }\n                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n                }\n                if (method == null) throw new NoSuchMethodException(methodName);\n                method.setAccessible(true);\n            }\n            if (method == null || !Modifier.isStatic(method.getModifiers())) {\n                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);\n                ctor.setAccessible(true);\n                FixtureSession session = FIXTURES.get();\n                if (session != null) session.constructing = true;\n                try {\n                    Object[] values = arguments(ctorTypes, vector, 0);\n                    if (method == null) INVOKED.set(true);\n                    receiver = ctor.newInstance(values);\n                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);\n                    if (session != null) session.receiver = receiver;\n                    if (session != null && className.equals(\"org.apache.commons.collections.map.Flat3Map\")) {\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-a\", \"value-a\");\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-b\", \"value-b\");\n                    }\n                    if (session != null && className.startsWith(\"org.apache.commons.jxpath.ri.model.\")) session.configurePointer(receiver);\n                } catch (InvocationTargetException error) {\n                    if (session != null && method != null)\n                        throw new FixtureFailure(\"Receiver constructor failed before method invocation\", error.getCause());\n                    throw error;\n                } finally { if (session != null) session.constructing = false; }\n            }\n            if (method == null) {\n                if (FIXTURES.get() == null) return \"constructed:\" + target.getName();\n                try { return snapshot(\"constructed:\" + target.getName() + \":state=\" + FIXTURES.get().state()); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Constructor state oracle failed\", failure); }\n            }\n            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);\n            INVOKED.set(true);\n            Object result = method.invoke(receiver, values);\n            if (FIXTURES.get() != null) {\n                FixtureSession session = FIXTURES.get();\n                try {\n                    return snapshot((method.getReturnType() == void.class ? \"void\" : \"value:\" + session.projection(result, 0))\n                            + \"|state=\" + session.state());\n                } catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Structural oracle failed\", failure); }\n            }\n            return method.getReturnType() == void.class ? \"void\" : snapshot(\"value:\" + value(result));\n        } catch (InvocationTargetException error) {\n            Throwable cause = error.getCause();\n            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)\n                throw new IllegalStateException(\"SQA_HARNESS JVM failure\", cause);\n            FixtureSession session = FIXTURES.get();\n            if (session != null && session.langHelpers && session.targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && session.method.equals(\"validateArray\")) {\n                try { return \"exception:\" + cause.getClass().getName() + \"|message=\" + value(cause.getMessage())\n                        + \"|state=\" + session.state(); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Validation boundary oracle failed\", failure); }\n            }\n            return \"exception:\" + cause.getClass().getName();\n        } catch (ReflectiveOperationException error) {\n            throw new IllegalStateException(\"SQA_HARNESS reflection failure\", error);\n        } catch (LinkageError error) {\n            throw new IllegalStateException(\"SQA_HARNESS linkage failure\", error);\n        }\n    }\n\n    public static String observeWithPolicy(String className, String constructorTypes, String methodName,\n            String methodTypes, double[] vector, String policy) {\n        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy)\n                && !PILOT_FIXTURES.equals(policy) && !BUFFER_FIXTURES.equals(policy)\n                && !FRACTION_FIELD_FIXTURES.equals(policy) && !LANG_HELPER_FIXTURES.equals(policy) && !JOINT_FIXTURES.equals(policy)\n                && !CHRONOLOGY_FIXTURES.equals(policy))\n            throw new IllegalArgumentException(\"Unknown explicit fixture policy\");\n        FIXTURES.set(new FixtureSession(className, methodName, policy));\n        try {\n            if (CHRONOLOGY_FIXTURES.equals(policy) && className.equals(\"org.joda.time.Partial\")\n                    && chronologyIdentity(constructorTypes, methodName, methodTypes))\n                return observeChronology(constructorTypes, methodName, methodTypes, vector);\n            return observe(className, constructorTypes, methodName, methodTypes, vector);\n        }\n        finally { FIXTURES.remove(); }\n    }\n\n    private static boolean chronologyIdentity(String ctor, String method, String params) {\n        if (method.equals(\"<init>\") && params.isEmpty()) return ctor.equals(\"org.joda.time.Chronology\")\n            || ctor.equals(\"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology\")\n            || ctor.equals(\"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology\")\n            || ctor.equals(\"org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I\");\n        return ctor.isEmpty() && ((method.equals(\"getField\") && params.equals(\"int,org.joda.time.Chronology\"))\n            || (method.equals(\"withChronologyRetainFields\") && params.equals(\"org.joda.time.Chronology\")));\n    }\n\n    private static String partialState(Object partial) throws ReflectiveOperationException {\n        Object chrono = call(partial,\"getChronology\",new Class<?>[]{});\n        Object zone = call(chrono,\"getZone\",new Class<?>[]{});\n        int size = (Integer)call(partial,\"size\",new Class<?>[]{});\n        List<String> names = new ArrayList<String>();\n        List<Integer> values = new ArrayList<Integer>();\n        boolean named = true;\n        for (int i=0; i<size; i++) {\n            Object type = call(partial,\"getFieldType\",new Class<?>[]{int.class},i);\n            names.add((String)call(type,\"getName\",new Class<?>[]{}));\n            Integer indexed = (Integer)call(partial,\"getValue\",new Class<?>[]{int.class},i);\n            values.add(indexed);\n            named &= indexed.equals(call(partial,\"get\",types(\"org.joda.time.DateTimeFieldType\"),type));\n        }\n        return \"partial:\"+chrono.getClass().getName()+\":\"+call(zone,\"getID\",new Class<?>[]{})\n            +\":types=\"+names+\":values=\"+values+\":named=\"+named;\n    }\n\n    /** Six bounded production identities only, separate from all historical policies.\n     * Vector[0] chooses a declared case, not arbitrary legal-domain approval.\n     * Reflection enters the exact protected/internal target on real final Partial.\n     */\n    private static synchronized String observeChronology(String ctor, String method, String params, double[] vector) {\n        INVOKED.set(false);\n        chronologyActiveCase = \"\";\n        try {\n            if (vector.length==0 || !Double.isFinite(vector[0]))\n                throw new IllegalArgumentException(\"Chronology needs a finite vector\");\n            System.setProperty(\"org.joda.time.DateTimeZone.Provider\",\"org.joda.time.tz.UTCProvider\");\n            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(\"UTC\"));\n            Class<?> partial = Class.forName(\"org.joda.time.Partial\");\n            Class<?> chronoType = Class.forName(\"org.joda.time.Chronology\");\n            Class<?> fieldType = Class.forName(\"org.joda.time.DateTimeFieldType\");\n            Class<?> zoneType = Class.forName(\"org.joda.time.DateTimeZone\");\n            Object provider = Class.forName(\"org.joda.time.tz.UTCProvider\").getDeclaredConstructor().newInstance();\n            call(zoneType,\"setProvider\",types(\"org.joda.time.tz.Provider\"),provider);\n            Object utc = zoneType.getField(\"UTC\").get(null);\n            call(zoneType,\"setDefault\",new Class<?>[]{zoneType},utc);\n            Object offset = call(zoneType,\"forOffsetHours\",new Class<?>[]{int.class},7);\n            Object iso = call(Class.forName(\"org.joda.time.chrono.ISOChronology\"),\"getInstance\",new Class<?>[]{zoneType},utc);\n            Object isoOffset = call(Class.forName(\"org.joda.time.chrono.ISOChronology\"),\"getInstance\",new Class<?>[]{zoneType},offset);\n            Object buddhist = call(Class.forName(\"org.joda.time.chrono.BuddhistChronology\"),\"getInstance\",new Class<?>[]{zoneType},offset);\n            Object year = call(fieldType,\"year\",new Class<?>[]{});\n            Object month = call(fieldType,\"monthOfYear\",new Class<?>[]{});\n            Object day = call(fieldType,\"dayOfMonth\",new Class<?>[]{});\n            Object hour = call(fieldType,\"hourOfDay\",new Class<?>[]{});\n            Object era = call(fieldType,\"era\",new Class<?>[]{});\n            // All three bounded cases occupy intervals within the generators'\n            // shared [-1,1] domain (including CMA-ES/FSCS-ART proposals).\n            int bucket = Math.min(2, (int)Math.floor((Math.max(-1.0,Math.min(1.0,vector[0]))+1.0)*1.5));\n            String caseName;\n            Object receiver = null, result = null;\n            Object[] args;\n            Object inputTypes = null; int[] inputValues = null;\n            String before = null;\n            Constructor<?> constructor = null;\n            Method targetMethod = null;\n            if (method.equals(\"<init>\")) {\n                constructor = partial.getDeclaredConstructor(types(ctor));\n                constructor.setAccessible(true);\n                if (ctor.equals(\"org.joda.time.Chronology\")) {\n                    caseName = bucket==0 ? \"empty_iso_offset\" : \"empty_null\";\n                    args = new Object[]{bucket==0 ? isoOffset : null};\n                } else if (ctor.equals(\"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology\")) {\n                    caseName = bucket==0 ? \"single_hour_iso\" : \"single_invalid_hour\";\n                    args = new Object[]{hour,bucket==0 ? 10 : 24,isoOffset};\n                } else {\n                    boolean internal = ctor.startsWith(\"org.joda.time.Chronology,\");\n                    caseName = internal ? \"internal_iso\" : bucket==0 ? \"arrays_leap_iso\"\n                        : bucket==1 ? \"arrays_invalid_date\" : \"arrays_bad_order\";\n                    inputTypes = Array.newInstance(fieldType,3);\n                    Object[] chosen = !internal && bucket==2 ? new Object[]{year,day,era} : new Object[]{year,month,day};\n                    for (int i=0;i<3;i++) Array.set(inputTypes,i,chosen[i]);\n                    inputValues = !internal && bucket==2 ? new int[]{1,1,1} : new int[]{2024,2,!internal && bucket==1 ? 30 : 29};\n                    if (internal) {\n                        Object validated = partial.getDeclaredConstructor(types(\"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology\"))\n                            .newInstance(inputTypes,inputValues,iso);\n                        inputTypes = call(validated,\"getFieldTypes\",new Class<?>[]{});\n                        inputValues = (int[])call(validated,\"getValues\",new Class<?>[]{});\n                        args = new Object[]{iso,inputTypes,inputValues};\n                    } else args = new Object[]{inputTypes,inputValues,isoOffset};\n                }\n            } else {\n                receiver = partial.getDeclaredConstructor().newInstance();\n                boolean field = method.equals(\"getField\");\n                receiver = call(receiver,\"with\",new Class<?>[]{fieldType,int.class},field ? year : hour,field ? 2024 : 10);\n                if (!field && bucket==2) receiver = call(receiver,\"withChronologyRetainFields\",new Class<?>[]{chronoType},buddhist);\n                before = partialState(receiver);\n                String expected = \"partial:org.joda.time.chrono.\"+(!field && bucket==2 ? \"BuddhistChronology\" : \"ISOChronology\")\n                    +\":UTC:types=[\"+(field ? \"year\" : \"hourOfDay\")+\"]:values=[\"+(field ? 2024 : 10)+\"]:named=true\";\n                if (!before.equals(expected)) throw new IllegalStateException(\"Default receiver seed differs\");\n                targetMethod = partial.getDeclaredMethod(method,types(params));\n                targetMethod.setAccessible(true);\n                if (field) {\n                    caseName = bucket==0 ? \"getfield_buddhist\" : \"getfield_bad_index\";\n                    args = new Object[]{bucket==0 ? 0 : 1,buddhist};\n                } else {\n                    caseName = bucket==0 ? \"withchrono_buddhist\" : bucket==1 ? \"withchrono_same\" : \"withchrono_null\";\n                    args = new Object[]{bucket==0 ? buddhist : bucket==1 ? isoOffset : null};\n                }\n            }\n            Throwable targetException = null;\n            chronologyActiveCase = caseName;\n            INVOKED.set(true);\n            try { result = constructor!=null ? constructor.newInstance(args) : targetMethod.invoke(receiver,args); }\n            catch (InvocationTargetException failure) { targetException = failure.getCause(); }\n            finally { chronologyActiveCase = \"\"; }\n            if (targetException!=null) {\n                if (targetException instanceof VirtualMachineError || targetException instanceof LinkageError || targetException instanceof ThreadDeath)\n                    throw new IllegalStateException(\"SQA_HARNESS JVM failure\",targetException);\n                String out = \"exception:\"+targetException.getClass().getName();\n                if (receiver!=null) out += \"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n                return out;\n            }\n            if (method.equals(\"getField\")) return \"field:\"+call(result,\"getName\",new Class<?>[]{})\n                +\":epoch=\"+call(result,\"get\",new Class<?>[]{long.class},0L)\n                +\":supplied-identity=\"+(result==call(buddhist,\"year\",new Class<?>[]{}))\n                +\":type-year=\"+(call(result,\"getType\",new Class<?>[]{})==year)\n                +\"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n            String out = partialState(result);\n            if (method.equals(\"withChronologyRetainFields\"))\n                return out+\":same=\"+(result==receiver)+\"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n            if (caseName.equals(\"arrays_leap_iso\")) {\n                Array.set(inputTypes,0,hour); inputValues[0]=1900;\n                boolean inputCopy = out.equals(partialState(result));\n                Object getterTypes = call(result,\"getFieldTypes\",new Class<?>[]{});\n                int[] getterValues = (int[])call(result,\"getValues\",new Class<?>[]{});\n                Array.set(getterTypes,0,hour); getterValues[0]=1900;\n                out += \":input-copy=\"+inputCopy+\":output-copy=\"+out.equals(partialState(result));\n            }\n            return out;\n        } catch (ReflectiveOperationException | RuntimeException failure) {\n            throw new FixtureFailure(\"SQA_HARNESS Chronology setup/projection failed\",failure);\n        } finally { chronologyActiveCase = \"\"; }\n    }\n\n    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }\n\n    private static String descriptor(String className, String ctor, String method, String params, int count) {\n        return \"{\\\"class\\\":\" + quote(className) + \",\\\"constructor_types\\\":\" + quote(ctor)\n            + \",\\\"method\\\":\" + quote(method) + \",\\\"parameter_types\\\":\" + quote(params)\n            + \",\\\"dimensions\\\":\" + Math.max(3, count * 3) + \"}\";\n    }\n\n    private static void discover(String[] classes, List<String> fixtureClasses) {\n        List<String> targets = new ArrayList<String>();\n        List<String> errors = new ArrayList<String>();\n        for (String className : classes) {\n            try {\n                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());\n                Class<?> receiverType = target;\n                if (Modifier.isAbstract(target.getModifiers())) {\n                    for (String name : fixtureClasses) {\n                        try {\n                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());\n                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)\n                                    && candidate.getDeclaredConstructors().length > 0) {\n                                receiverType = candidate;\n                                break;\n                            }\n                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }\n                    }\n                }\n                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();\n                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {\n                    Constructor<?>[] all = receiverType.getDeclaredConstructors();\n                    Arrays.sort(all, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }\n                    });\n                    for (Constructor<?> ctor : all) {\n                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);\n                    }\n                    // Select a constructor before generating inputs; prefer the simplest fixture.\n                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }\n                    });\n                }\n                Method[] methods = target.getDeclaredMethods();\n                Arrays.sort(methods, new Comparator<Method>() {\n                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }\n                });\n                for (Method method : methods) {\n                    if (method.isSynthetic() || method.getName().equals(\"main\")\n                        || method.isBridge() || !supportedParameters(method.getParameterTypes())\n                        ) continue;\n                    if (Modifier.isStatic(method.getModifiers())) {\n                        targets.add(descriptor(className, \"\", method.getName(),\n                            typeNames(method.getParameterTypes()), method.getParameterCount()));\n                    } else if (!constructors.isEmpty()) {\n                        Constructor<?> ctor = constructors.get(0);\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),\n                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));\n                    }\n                }\n                for (Constructor<?> ctor : constructors) {\n                    if (ctor.getParameterCount() > 0)\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), \"<init>\", \"\", ctor.getParameterCount()));\n                }\n            } catch (Throwable error) {\n                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;\n                errors.add(quote(className + \":\" + error.getClass().getName()));\n            }\n        }\n        System.out.println(\"{\\\"targets\\\":[\" + String.join(\",\", targets) + \"],\\\"errors\\\":[\" + String.join(\",\", errors) + \"]}\");\n    }\n\n    public static void main(String[] args) throws Exception {\n        if (args.length > 0 && args[0].equals(\"discover\")) {\n            int start = 1;\n            List<String> fixtures = new ArrayList<String>();\n            if (args.length > 2 && args[1].equals(\"--fixtures\")) {\n                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);\n                start = 3;\n            }\n            discover(Arrays.copyOfRange(args, start, args.length), fixtures);\n            return;\n        }\n        if ((args.length != 6 && args.length != 7) || !args[0].equals(\"observe\"))\n            throw new IllegalArgumentException(\"SQA_HARNESS expected discover classes or observe class ctor method types vector\");\n        String[] pieces = args[5].split(\",\");\n        double[] vector = new double[pieces.length];\n        for (int i = 0; i < pieces.length; i++) {\n            vector[i] = Double.parseDouble(pieces[i]);\n            if (!Double.isFinite(vector[i]))\n                throw new IllegalArgumentException(\"SQA_HARNESS nonfinite vector\");\n        }\n        String outcome;\n        try {\n            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])\n                : observe(args[1], args[2], args[3], args[4], vector);\n        } catch (FixtureFailure failure) {\n            System.out.println(\"SQA_FIXTURE_FAILURE:\" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));\n            return;\n        }\n        System.out.println(\"SQA_TRACE:{\\\"target_invoked\\\":\" + Boolean.TRUE.equals(INVOKED.get()) + \"}\");\n        System.out.println(\"SQA_RESULT:\" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));\n    }\n}\n",
    "scripts/study/api854/fixture_policy.py": "\"\"\"Predeclared explicit fixture capability filter, never selected by buggy outcomes.\"\"\"\nPOLICY = 'beam-explicit-fixtures-v3-proposal'\nRECIPE_SOURCES = ('algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py')\n\n\ndef recipe_document(source_hashes, policy=POLICY):\n    from .common import ROOT, sha256\n    expected = {name: source_hashes[name] for name in RECIPE_SOURCES}\n    if any(sha256(ROOT / name) != value for name, value in expected.items()):\n        raise ValueError('Explicit recipe source differs from protocol')\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11}:\n        raise ValueError('Unknown explicit fixture policy')\n    return {'schema_version': 1, 'fixture_policy_id': policy, 'source_sha256': expected,\n        'sources': {name: (ROOT / name).read_bytes().decode('utf-8') for name in RECIPE_SOURCES},\n        'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}\n\n\ndef validate_recipe(recipe, source_hashes=None, policy=POLICY):\n    from .preparation import digest\n    if (policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11} or not isinstance(recipe, dict) or recipe.get('fixture_policy_id') != policy\n            or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(RECIPE_SOURCES)\n            or not isinstance(recipe.get('source_sha256'), dict) or set(recipe['source_sha256']) != set(RECIPE_SOURCES)\n            or any(not isinstance(recipe['sources'][name], str)\n                   or digest(recipe['sources'][name].encode('utf-8')) != recipe['source_sha256'][name] for name in RECIPE_SOURCES)):\n        raise ValueError('Explicit recipe source bytes/hash differ')\n    if source_hashes is not None and any(source_hashes.get(name) != recipe['source_sha256'][name] for name in RECIPE_SOURCES):\n        raise ValueError('Explicit recipe source differs from frozen protocol')\n    return True\nPOLICY_V4 = 'beam-explicit-fixtures-v4-proposal'\nPOLICY_V5 = 'beam-explicit-fixtures-v5-proposal'\nPOLICY_V6 = 'aom-beam-fraction-field-v6-development'\nPOLICY_V10 = 'aom-beam-champ-joint-fixtures-v10-development'\nPOLICY_V11 = 'aom-beam-champ-chronology-fixtures-v11-development'\nCHRONOLOGY_SIGNATURES = {\n    ('org.joda.time.Partial', 'org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', 'org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', '[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', 'org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I', '<init>', ''),\n    ('org.joda.time.Partial', '', 'getField', 'int,org.joda.time.Chronology'),\n    ('org.joda.time.Partial', '', 'withChronologyRetainFields', 'org.joda.time.Chronology'),\n}\nJOINT_SIGNATURES = {\n    ('org.apache.commons.codec.language.Metaphone', '', 'setMaxCodeLen', 'int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'inLongRange', '[C,int,int,boolean'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseInt', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseLong', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', 'java.lang.String,int,int'),\n    ('org.apache.commons.csv.ExtendedBufferedReader', 'java.io.Reader', 'read', '[C,int,int'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'isAllZeros', 'java.lang.String'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'validateArray', 'java.lang.Object'),\n}\n\n# Fixed-source recipes, declared before generation/evaluation. This development\n# version deliberately preserves unsupported declarations as explicit exclusions.\nPILOT_METHODS = {\n    'org.apache.commons.lang3.math.NumberUtils': {'isDigits', 'isNumber', 'max', 'min', 'toByte', 'toDouble',\n        'toFloat', 'toInt', 'toLong', 'toShort', 'createDouble', 'createFloat', 'createInteger', 'createLong',\n        'createNumber', 'createBigDecimal', 'createBigInteger'},\n    'com.fasterxml.jackson.core.io.NumberInput': {'parseAsDouble', 'parseDouble', 'parseAsInt', 'parseInt',\n        'parseBigDecimal', 'parseAsLong', 'parseLong', 'inLongRange'},\n    'com.fasterxml.jackson.core.util.TextBuffer': {'hasTextAsCharacters', 'contentsAsArray', 'contentsAsString',\n        'getCurrentSegmentSize', 'getTextOffset', 'size', 'toString', 'append', 'resetWithEmpty', 'resetWithString'},\n    'org.apache.commons.math3.fraction.BigFraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'reduce', 'compareTo'},\n    'org.apache.commons.math3.fraction.Fraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'compareTo'},\n    'org.jsoup.nodes.Document': {'nodeName', 'outerHtml', 'title', 'normalise', 'body', 'head', 'text', 'createElement', 'createShell'},\n    'org.apache.commons.cli.CommandLine': {'hasOption', 'getOptionObject', 'getOptionValue', 'getArgs',\n        'getOptionValues', 'iterator', 'getArgList', 'getOptions', 'addArg', 'addOption'},\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream': {'ensureOpen', 'writeCString',\n        'close', 'closeArchiveEntry', 'finish', 'putArchiveEntry', 'putNextEntry', 'write'},\n    'org.joda.time.field.UnsupportedDurationField': {'equals', 'isPrecise', 'isSupported', 'getType',\n        'getName', 'toString', 'getUnitMillis', 'compareTo', 'getInstance'},\n    'org.joda.time.Partial': {'size', 'getValues', 'toStringList', 'with', 'withField', 'without'},\n    'com.google.gson.TypeInfoFactory': {'getIndex', 'getActualType', 'extractRealTypes', 'getTypeInfoForField', 'getTypeInfoForArray'},\n    'com.google.javascript.jscomp.RemoveUnusedVars': {'process', 'traverseAndRemoveUnusedReferences', 'getFunctionArgList'},\n    'org.jfree.chart.renderer.category.AreaRenderer': {'findRangeBounds', 'getRowCount', 'getColumnCount',\n        'getPassCount', 'getLegendItems', 'getLegendItem', 'getItemMiddle'},\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter': {'getName', 'getSerializedName', 'getType',\n        'getPropertyType', 'getGenericPropertyType', 'isRequired', 'willSuppressNulls', 'hasSerializer',\n        'hasNullSerializer', 'rename', 'get', 'getInternalSetting', 'setInternalSetting', 'removeInternalSetting'},\n    'com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer': {'deserialize', 'deserializeUsingCustom', 'handleNonArray'},\n    'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser': {'hasTextCharacters', 'isClosed', 'isExpectedStartArrayToken',\n        'requiresCustomCodec', 'getTextCharacters', 'nextToken', 'getText', 'getCurrentName', 'getValueAsString',\n        'nextTextValue', 'close', 'overrideCurrentName', 'setXMLTextElementName'},\n    'org.mockito.internal.invocation.InvocationMatcher': {'matches', 'hasSameMethod', 'hasSimilarMethod',\n        'getMethod', 'getInvocation', 'toString'},\n}\nPILOT_TYPES = {'com.fasterxml.jackson.core.util.BufferRecycler', 'org.apache.commons.math3.fraction.BigFraction',\n    'org.apache.commons.math3.fraction.Fraction', 'java.math.BigInteger', 'org.apache.commons.cli.Option',\n    'java.io.OutputStream', 'org.apache.commons.compress.archivers.ArchiveEntry',\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveEntry', 'org.joda.time.DurationFieldType',\n    'org.joda.time.DurationField', 'org.joda.time.DateTimeFieldType', 'java.lang.reflect.Type',\n    'java.lang.reflect.TypeVariable', '[Ljava.lang.reflect.Type;', '[Ljava.lang.reflect.TypeVariable;',\n    'java.lang.reflect.Field', 'java.lang.Class', 'com.google.javascript.jscomp.AbstractCompiler',\n    'com.google.javascript.rhino.Node', 'org.jfree.data.category.CategoryDataset', 'org.jfree.chart.axis.CategoryAxis',\n    'java.lang.Comparable', 'java.awt.geom.Rectangle2D', 'org.jfree.chart.util.RectangleEdge',\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter', 'com.fasterxml.jackson.databind.util.NameTransformer',\n    'com.fasterxml.jackson.databind.JavaType', 'com.fasterxml.jackson.databind.JsonDeserializer',\n    'com.fasterxml.jackson.databind.deser.ValueInstantiator', 'com.fasterxml.jackson.core.JsonParser',\n    'com.fasterxml.jackson.databind.DeserializationContext', 'com.fasterxml.jackson.core.io.IOContext',\n    'com.fasterxml.jackson.core.ObjectCodec', 'javax.xml.stream.XMLStreamReader', 'org.mockito.invocation.Invocation'}\n\n# Added capability recipes are fixed before any buggy evaluation. Mutators need\n# structural post-state; unsupported helpers/serialization hooks stay excluded.\nADDITIONAL_METHODS = {\n    'org.apache.commons.codec.language.Caverphone': {'isCaverphoneEqual', 'encode', 'caverphone'},\n    'org.apache.commons.codec.language.Metaphone': {'isLastChar', 'isMetaphoneEqual', 'getMaxCodeLen', 'encode', 'metaphone'},\n    'org.apache.commons.codec.language.SoundexUtils': {'differenceEncoded', 'clean'},\n    'org.apache.commons.collections.map.Flat3Map': {'containsKey', 'containsValue', 'equals', 'isEmpty', 'size',\n        'clone', 'get', 'put', 'remove', 'toString', 'clear', 'putAll'},\n    'org.apache.commons.csv.ExtendedBufferedReader': {'getLineNumber', 'lookAhead', 'readAgain', 'read', 'readLine'},\n}\n\nSCALARS = {'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char',\n           'java.lang.String', 'java.lang.Boolean', 'java.lang.Byte', 'java.lang.Short',\n           'java.lang.Integer', 'java.lang.Long', 'java.lang.Float', 'java.lang.Double',\n           'java.lang.Character', 'java.lang.Object', 'java.lang.Number', 'java.util.Date',\n           'java.util.Locale', 'java.util.List', 'java.util.Collection', 'java.lang.Iterable',\n           'java.util.Iterator', 'java.util.Map', 'java.util.Set'}\nCLOSURE = {'com.google.javascript.jscomp.AbstractCompiler', 'com.google.javascript.jscomp.ControlFlowGraph',\n           'com.google.javascript.jscomp.type.ReverseAbstractInterpreter', 'com.google.javascript.jscomp.Scope',\n           'com.google.javascript.jscomp.Scope$Var', 'com.google.javascript.jscomp.type.FlowScope',\n           'com.google.javascript.rhino.Node', 'com.google.javascript.rhino.jstype.JSType',\n           'com.google.javascript.rhino.jstype.ObjectType', 'com.google.javascript.rhino.jstype.JSTypeNative',\n           'com.google.javascript.rhino.jstype.BooleanLiteralSet'}\nJXPATH = {'org.w3c.dom.Node', 'org.w3c.dom.Document', 'org.w3c.dom.Element',\n          'org.apache.commons.jxpath.ri.QName', 'org.apache.commons.jxpath.ri.compiler.NodeTest',\n          'org.apache.commons.jxpath.ri.model.NodePointer'}\n# Methods requiring specialized AST parent/sibling/call metadata have no reviewed\n# recipe yet. This list is a structural restriction, not an outcome-based prune.\nCLOSURE_METHODS = {'createEntryLattice', 'createInitialEstimateLattice', 'flowThrough',\n    'branchedFlowThrough', 'isAddedAsNumber', 'isUnflowable', 'newBooleanOutcomePair',\n    'traverseAnd', 'traverseOr', 'traverseShortCircuitingBinOp', 'traverseWithinShortCircuitingBinOp',\n    'narrowScope', 'traverse', 'traverseAdd', 'traverseArrayLiteral', 'traverseAssign',\n    'traverseChildren', 'traverseGetElem', 'traverseGetProp', 'traverseHook', 'traverseName',\n    'traverseObjectLiteral', 'traverseReturn', 'getJSType', 'getNativeType',\n    'redeclareSimpleVar', 'updateScopeForTypeChange', 'getBooleanOutcomes'}\n\n\ndef select(targets, policy):\n    if policy is None:\n        return targets, []\n    if policy == POLICY_V11:\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V10)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | CHRONOLOGY_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11}:\n        raise ValueError('Unknown explicit fixture policy')\n    if policy == POLICY_V10:\n        # Preserve v5+Math and add only exact peer-approved identities. JDOM is a repair.\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V6)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | JOINT_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy == POLICY_V6:\n        # Keep every v5 decision, adding only the exact Champ-accepted signatures.\n        selected, excluded = select(targets, POLICY_V5)\n        accepted = lambda t: (t['class'] in {\n            'org.apache.commons.math3.fraction.BigFraction', 'org.apache.commons.math3.fraction.Fraction'}\n            and t['constructor_types'] == 'double' and t['method'] == 'getField' and t['parameter_types'] == '')\n        chosen = {tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) for t in selected}\n        return ([t for t in targets if accepted(t) or tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) in chosen],\n                [row for row in excluded if not accepted(row['target'])])\n    selected, excluded = [], []\n    for target in targets:\n        name = target['class']\n        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {\n            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',\n            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()\n        extra = policy in {POLICY_V4, POLICY_V5} and name in ADDITIONAL_METHODS\n        pilot = policy == POLICY_V5 and name in PILOT_METHODS\n        if extra:\n            family = {'java.io.Reader'}\n        if pilot:\n            family = PILOT_TYPES | {'[B', '[S', '[I', '[J', '[F', '[D', '[C'}\n        reason = None\n        if not family:\n            reason = 'explicit_project_recipe_not_reviewed'\n        elif target['method'] in {'<init>', 'hashCode'}:\n            reason = 'constructor_or_identity_oracle_not_reviewed'\n        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:\n            reason = 'specialized_ast_recipe_not_reviewed'\n        elif extra and target['method'] not in ADDITIONAL_METHODS[name]:\n            reason = 'additional_method_preconditions_or_state_not_reviewed'\n        elif extra and name.endswith('ExtendedBufferedReader') and target['parameter_types']:\n            reason = 'reader_buffer_offset_bounds_recipe_not_reviewed'\n        elif pilot and target['method'] not in PILOT_METHODS[name]:\n            reason = 'pilot_method_preconditions_or_oracle_not_reviewed'\n        elif pilot and name.endswith('NumberInput') and '[' in target['parameter_types']:\n            reason = 'numeric_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('TextBuffer') and target['method'] == 'append' and target['parameter_types'] != 'char':\n            reason = 'text_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('Document') and target['parameter_types'] == 'org.jsoup.nodes.Element':\n            reason = 'html_internal_normalise_recipe_not_reviewed'\n        elif pilot and name.endswith('CpioArchiveOutputStream') and target['method'] == 'write' and target['parameter_types'] != 'int':\n            reason = 'archive_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('BeanPropertyWriter') and target['method'] == 'isRequired' and target['parameter_types']:\n            reason = 'annotation_introspector_recipe_not_reviewed'\n        elif pilot and name.endswith('RemoveUnusedVars') and target['method'] == 'process' and target['parameter_types'] != 'com.google.javascript.rhino.Node,com.google.javascript.rhino.Node':\n            reason = 'call_site_definition_finder_recipe_not_reviewed'\n        else:\n            required = set(filter(None, (target['constructor_types'] + ',' + target['parameter_types']).split(',')))\n            missing = required - SCALARS - family\n            if missing:\n                reason = 'explicit_argument_recipe_missing:' + ','.join(sorted(missing))\n        if reason:\n            excluded.append({'target': target, 'reason': reason})\n        else:\n            selected.append(target)\n    return selected, excluded\n"
  }
}
```
