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
    <version>2.4</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.4.2-SNAPSHOT</version>
  <name>jackson-databind</name>
  <packaging>bundle</packaging>
  <description>General data-binding functionality for Jackson: works on core streaming API</description>
  <url>http://wiki.fasterxml.com/JacksonHome</url>

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
      <version>2.4.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.4.1.1</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->
    <dependency>
      <groupId>cglib</groupId>
      <artifactId>cglib</artifactId>
      <version>2.2.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.codehaus.groovy</groupId>
      <artifactId>groovy</artifactId>
      <version>1.7.9</version>
      <scope>test</scope>
    </dependency>
    <dependency> <!--  from core we just test for repackaged cglib, not hibernate proper -->
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-cglib-repack</artifactId>
      <version>2.1_3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
     <plugins>
       <plugin> <!-- parent uses 2.4.2 -->
         <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-release-plugin</artifactId>
          <version>2.5</version>
       </plugin>
     <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${surefire.version}</version>
        <configuration>
          <excludes>
            <exclude>com/fasterxml/jackson/failing/*.java</exclude>
          </excludes>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>${javadoc.version}</version>
        <configuration>
          <links>
            <link>http://docs.oracle.com/javase/6/docs/api/</link>
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.4/</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.4/</link>
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

## src/main/java/com/fasterxml/jackson/databind/deser/std/StringArrayDeserializer.java

```
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Separate implementation for serializing String arrays (instead of
 * using {@link ObjectArrayDeserializer}.
 * Used if (and only if) no custom value deserializers are used.
 */
@JacksonStdImpl
public final class StringArrayDeserializer
    extends StdDeserializer<String[]>
    implements ContextualDeserializer
{
    private static final long serialVersionUID = -7589512013334920693L;

    public final static StringArrayDeserializer instance = new StringArrayDeserializer();
    
    /**
     * Value serializer to use, if not the standard one (which is inlined)
     */
    protected JsonDeserializer<String> _elementDeserializer;

    public StringArrayDeserializer() {
        super(String[].class);
        _elementDeserializer = null;
    }

    @SuppressWarnings("unchecked")
    protected StringArrayDeserializer(JsonDeserializer<?> deser) {
        super(String[].class);
        _elementDeserializer = (JsonDeserializer<String>) deser;
    }
   
    @Override
    public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
    {
        // Ok: must point to START_ARRAY (or equivalent)
        if (!jp.isExpectedStartArrayToken()) {
            return handleNonArray(jp, ctxt);
        }
        if (_elementDeserializer != null) {
            return _deserializeCustom(jp, ctxt);
        }

        final ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object[] chunk = buffer.resetAndStart();

        int ix = 0;
        JsonToken t;

        try {
            while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                // Ok: no need to convert Strings, but must recognize nulls
                String value;
                if (t == JsonToken.VALUE_STRING) {
                    value = jp.getText();
                } else if (t == JsonToken.VALUE_NULL) {
                    value = null; // since we have established that '_elementDeserializer == null' earlier
                } else {
                    value = _parseString(jp, ctxt);
                }
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }
        } catch (Exception e) {
            // note: pass String.class, not String[].class, as we need element type for error info
            throw JsonMappingException.wrapWithPath(e, String.class, ix);
        }
        String[] result = buffer.completeAndClearBuffer(chunk, ix, String.class);
        ctxt.returnObjectBuffer(buffer);
        return result;
    }

    /**
     * Offlined version used when we do not use the default deserialization method.
     */
    protected final String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException
    {
        final ObjectBuffer buffer = ctxt.leaseObjectBuffer();
        Object[] chunk = buffer.resetAndStart();
        final JsonDeserializer<String> deser = _elementDeserializer;
        
        int ix = 0;
        JsonToken t;

        try {
            while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                // Ok: no need to convert Strings, but must recognize nulls
                String value = (t == JsonToken.VALUE_NULL) ? deser.getNullValue() : deser.deserialize(jp, ctxt);
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }
        } catch (Exception e) {
            // note: pass String.class, not String[].class, as we need element type for error info
            throw JsonMappingException.wrapWithPath(e, String.class, ix);
        }
        String[] result = buffer.completeAndClearBuffer(chunk, ix, String.class);
        ctxt.returnObjectBuffer(buffer);
        return result;
    }
    
    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private final String[] handleNonArray(JsonParser jp, DeserializationContext ctxt) throws IOException
    {
        // [JACKSON-526]: implicit arrays from single values?
        if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            // [JACKSON-620] Empty String can become null...
            if ((jp.getCurrentToken() == JsonToken.VALUE_STRING)
                    && ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
                String str = jp.getText();
                if (str.length() == 0) {
                    return null;
                }
            }
            throw ctxt.mappingException(_valueClass);
        }
        return new String[] { (jp.getCurrentToken() == JsonToken.VALUE_NULL) ? null : _parseString(jp, ctxt) };
    }

    /**
     * Contextualization is needed to see whether we can "inline" deserialization
     * of String values, or if we have to use separate value deserializer.
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException
    {
        JsonDeserializer<?> deser = _elementDeserializer;
        // #125: May have a content converter
        deser = findConvertingContentDeserializer(ctxt, property, deser);
        if (deser == null) {
            deser = ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property);
        } else { // if directly assigned, probably not yet contextual, so:
            deser = ctxt.handleSecondaryContextualization(deser, property);
        }
        // Ok ok: if all we got is the default String deserializer, can just forget about it
        if (deser != null && this.isDefaultDeserializer(deser)) {
            deser = null;
        }
        if (_elementDeserializer != deser) {
            return new StringArrayDeserializer(deser);
        }
        return this;
    }
}

```
