## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="jackson-databind" default="package" basedir=".">

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

  <target name="compile" depends="jackson-databind-from-maven.compile"> </target>
  <target name="compile.tests" depends="jackson-databind-from-maven.compile-tests"> </target>

</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>com.fasterxml.jackson</groupId>
    <artifactId>jackson-parent</artifactId>
    <version>2.6.2</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.6.6.1-SNAPSHOT</version>
  <name>jackson-databind</name>
  <packaging>bundle</packaging>
  <description>General data-binding functionality for Jackson: works on core streaming API</description>
  <url>http://github.com/FasterXML/jackson</url>
  <inceptionYear>2008</inceptionYear>

  <scm>
    <connection>scm:git:git@github.com:FasterXML/jackson-databind.git</connection>
    <developerConnection>scm:git:git@github.com:FasterXML/jackson-databind.git</developerConnection>
    <url>http://github.com/FasterXML/jackson-databind</url>
    <tag>HEAD</tag>
  </scm>

  <properties>
    <osgi.export>
com.fasterxml.jackson.databind,
com.fasterxml.jackson.databind.annotation,
com.fasterxml.jackson.databind.cfg,
com.fasterxml.jackson.databind.deser,
com.fasterxml.jackson.databind.deser.impl,
com.fasterxml.jackson.databind.deser.std,
com.fasterxml.jackson.databind.exc,
com.fasterxml.jackson.databind.ext,
com.fasterxml.jackson.databind.introspect,
com.fasterxml.jackson.databind.jsonschema,
com.fasterxml.jackson.databind.jsonFormatVisitors,
com.fasterxml.jackson.databind.jsontype,
com.fasterxml.jackson.databind.jsontype.impl,
com.fasterxml.jackson.databind.module,
com.fasterxml.jackson.databind.node,
com.fasterxml.jackson.databind.ser,
com.fasterxml.jackson.databind.ser.impl,
com.fasterxml.jackson.databind.ser.std,
com.fasterxml.jackson.databind.type,
com.fasterxml.jackson.databind.util
    </osgi.export>
    <osgi.import>
com.fasterxml.jackson.annotation,
com.fasterxml.jackson.core,
com.fasterxml.jackson.core.base,
com.fasterxml.jackson.core.filter,
com.fasterxml.jackson.core.format,
com.fasterxml.jackson.core.json,
com.fasterxml.jackson.core.io,
com.fasterxml.jackson.core.util,
com.fasterxml.jackson.core.type,
org.xml.sax,org.w3c.dom, org.w3c.dom.bootstrap, org.w3c.dom.ls,
javax.xml.datatype, javax.xml.namespace, javax.xml.parsers
</osgi.import>

    <!-- Generate PackageVersion.java into this directory. -->
    <packageVersion.dir>com/fasterxml/jackson/databind/cfg</packageVersion.dir>
    <packageVersion.package>com.fasterxml.jackson.databind.cfg</packageVersion.package>
  </properties>

  <dependencies>
    <!-- Builds on core streaming API; also needs core annotations -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.6.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.6.6</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->
		<!-- Mock -->
   <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>1.6.2</version>
      <scope>test</scope>
   </dependency>
   <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>1.6.2</version>
      <scope>test</scope>
   </dependency>
    <!-- For testing TestNoClassDefFoundDeserializer -->
    <dependency>
      <groupId>javax.measure</groupId>
      <artifactId>jsr-275</artifactId>
      <version>0.9.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>18.0</version>
        <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
     <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <version>${version.plugin.surefire}</version>
        <artifactId>maven-surefire-plugin</artifactId>
        <configuration>
          <classpathDependencyExcludes>
            <exclude>javax.measure:jsr-275</exclude>
          </classpathDependencyExcludes>
          <excludes>
            <exclude>com/fasterxml/jackson/failing/*.java</exclude>
          </excludes>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>${version.plugin.javadoc}</version>
        <configuration>
<!-- Only works on Java 8:
          <additionalparam>-Xdoclint:none</additionalparam>
-->
<!-- so with Java 7, use this: -->
          <failOnError>false</failOnError>
          <links>
            <link>http://docs.oracle.com/javase/7/docs/api/</link>
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.6</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.6</link>
          </links>
        </configuration>
      </plugin>

      <!-- May want to configure debug info -->
      <plugin>
        <!-- Inherited from oss-base. Generate PackageVersion.java.-->
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
        <executions>
          <execution>
            <id>process-packageVersion</id>
            <phase>process-sources</phase>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </reporting>

  <profiles>
    <profile>
      <id>release</id>
      <properties>
        <maven.test.skip>true</maven.test.skip>
        <skipTests>true</skipTests>
      </properties>
    </profile>
  </profiles>

</project>

```

## src/main/java/com/fasterxml/jackson/databind/ser/impl/WritableObjectId.java

```
package com.fasterxml.jackson.databind.ser.impl;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;

import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Simple value container used to keep track of Object Ids during
 * serialization.
 */
public final class WritableObjectId
{
    public final ObjectIdGenerator<?> generator;

    public Object id;

    /**
     * Marker to denote whether Object Id value has been written as part of an Object,
     * to be referencible. Remains false when forward-reference is written.
     */
    protected boolean idWritten = false;

    public WritableObjectId(ObjectIdGenerator<?> generator) {
        this.generator = generator;
    }

    public boolean writeAsId(JsonGenerator gen, SerializerProvider provider, ObjectIdWriter w) throws IOException
    {
        if ((id != null) && (idWritten || w.alwaysAsId)) {
            // 03-Aug-2013, tatu: Prefer Native Object Ids if available
            if (gen.canWriteObjectId()) {
                gen.writeObjectRef(String.valueOf(id));
            } else {
                w.serializer.serialize(id, gen, provider);
            }
            return true;
        }
        return false;
    }
    
    public Object generateId(Object forPojo) {
        // 04-Jun-2016, tatu: As per [databind#1255], need to consider possibility of
        //    id being generated for "alwaysAsId", but not being written as POJO; regardless,
        //    need to use existing id if there is one:
        if (id == null) {
            id = generator.generateId(forPojo);
        }
        return id;
    }

    /**
     * Method called to output Object Id as specified.
     */
    public void writeAsField(JsonGenerator gen, SerializerProvider provider,
            ObjectIdWriter w) throws IOException
    {
        idWritten = true;

        // 03-Aug-2013, tatu: Prefer Native Object Ids if available
        if (gen.canWriteObjectId()) {
            // Need to assume String(ified) ids, for now... could add 'long' variant?
            gen.writeObjectId(String.valueOf(id));
            return;
        }
        
        SerializableString name = w.propertyName;
        if (name != null) {
            gen.writeFieldName(name);
            w.serializer.serialize(id, gen, provider);
        }
    }
}

```
