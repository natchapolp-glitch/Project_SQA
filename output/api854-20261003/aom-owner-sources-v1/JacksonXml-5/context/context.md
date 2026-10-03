## build.xml

```
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="jackson-dataformat-xml" default="package" basedir=".">

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

  <target name="compile" depends="jackson-dataformat-xml-from-maven.compile"> </target>
  <target name="compile.tests" depends="jackson-dataformat-xml-from-maven.compile-tests"> </target>

</project>

```

## pom.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion> 
  <parent>
    <groupId>com.fasterxml.jackson</groupId>
    <artifactId>jackson-base</artifactId>
    <version>2.9.6</version>
  </parent>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
  <version>2.9.6</version>
  <name>Jackson-dataformat-XML</name>
  <packaging>bundle</packaging>
  <description>Data format extension for Jackson (http://jackson.codehaus.org) to offer
alternative support for serializing POJOs as XML and deserializing XML as pojos.
Support implemented on top of Stax API (javax.xml.stream), by implementing core Jackson Streaming API types like JsonGenerator, JsonParser and JsonFactory.
Some data-binding types overridden as well (ObjectMapper sub-classed as XmlMapper).
  </description>
  <url>http://wiki.fasterxml.com/JacksonExtensionXmlDataBinding</url>
  <scm>
    <connection>scm:git:git@github.com:FasterXML/jackson-dataformat-xml.git</connection>
    <developerConnection>scm:git:git@github.com:FasterXML/jackson-dataformat-xml.git</developerConnection>
    <url>http://github.com/FasterXML/jackson-dataformat-xml</url>    
    <tag>HEAD</tag>
  </scm>
  <properties>
    <packageVersion.dir>com/fasterxml/jackson/dataformat/xml</packageVersion.dir>
    <packageVersion.package>${project.groupId}.xml</packageVersion.package>

    <!-- Default export should work fine... -->
    <!-- And presumably import too? -->
  </properties>

  <dependencies>
    <!-- Extends Jackson (jackson-mapper); requires Stax API (and implementation on deploy time), Stax2 API.
         Also requires JAXB annotations module
      -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
    </dependency>
    <!--  JAXB annotation introspector is needed too? -->
    <dependency>
      <groupId>com.fasterxml.jackson.module</groupId>
      <artifactId>jackson-module-jaxb-annotations</artifactId>
    </dependency>
    <!-- JDK 1.6 provides stax-api (javax.xml.stream), but let's add this for documentation
      -->
    <dependency>
      <groupId>javax.xml.stream</groupId>
      <artifactId>stax-api</artifactId>
      <version>1.0-2</version>
      <scope>provided</scope>
    </dependency>
    <!--  But Stax2 API must be included -->
    <dependency>
      <groupId>org.codehaus.woodstox</groupId>
      <artifactId>stax2-api</artifactId>
      <!-- 01-Jan-2010, Stupid Maven Release plug-in mandates a non-range
            version
        -->
<!--
      <version>[3.0.4, 3.5.0)</version>
-->
      <version>3.1.4</version>
    </dependency>
    <!--  and a Stax impl is needed: SJSXP (from JDK 1.6) might work, but always   
          has odd issues. Let's default to Woodstox: caller can upgrade to Aalto
         (needs to block this dep)
      -->
    <!-- 09-May-2016, tatu: With Jackson 2.8, let's make this compile-dep to make it
        less likely users accidentally try to use Sjsxp from JDK, which leads to probs
      -->
    <dependency>
      <groupId>com.fasterxml.woodstox</groupId>
      <artifactId>woodstox-core</artifactId>
      <version>5.0.3</version>
    </dependency>

    <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.0</version>
      <scope>test</scope>
    </dependency>
    <!-- 04-Feb-2018, tatu: Finally, Java 9 does not bundle JAXB, not even API;
            at least include it for tests. May need to include actual dep to
            API for runtime too, but first for tests
      -->
    <dependency>
      <groupId>javax.xml.bind</groupId>
      <artifactId>jaxb-api</artifactId>
      <version>2.3.0</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <!-- Inherited from oss-base. Generate PackageVersion.java.-->
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
        <executions>
          <execution>
            <id>process-packageVersion</id>
            <phase>generate-sources</phase>
          </execution>
        </executions>
      </plugin>
      <!--  Need to skip known-failing tests for build... -->
      <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>${version.plugin.surefire}</version>
          <configuration>
            <excludes>
              <exclude>com/fasterxml/jackson/dataformat/xml/failing/*.java</exclude>
            </excludes>
            <includes>
              <include>**/Test*.java</include>
              <include>**/*Test.java</include>
            </includes>
          </configuration>
        </plugin>
    </plugins>
  </build>

  <profiles>
    <!--  19-Feb-2012, tatu: Since we have some failing tests, may need to force release -->
    <profile>
      <id>force-release</id>
      <properties>
        <maven.test.skip>true</maven.test.skip>
        <skipTests>true</skipTests>
      </properties>
    </profile>
  </profiles>
  <!-- NOTE: repositories from parent POM -->

</project>

```

## src/main/java/com/fasterxml/jackson/dataformat/xml/ser/XmlSerializerProvider.java

```
package com.fasterxml.jackson.dataformat.xml.ser;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * We need to override some parts of
 * {@link com.fasterxml.jackson.databind.SerializerProvider}
 * implementation to handle oddities of XML output, like "extra" root element.
 */
public class XmlSerializerProvider extends DefaultSerializerProvider
{
    // As of 2.7
    private static final long serialVersionUID = 1L;

    /**
     * If all we get to serialize is a null, there's no way to figure out
     * expected root name; so let's just default to something like "&lt;null>"...
     */
    protected final static QName ROOT_NAME_FOR_NULL = new QName("null");

    protected final XmlRootNameLookup _rootNameLookup;

    public XmlSerializerProvider(XmlRootNameLookup rootNames)
    {
        super();
        _rootNameLookup = rootNames;
    }

    public XmlSerializerProvider(XmlSerializerProvider src,
            SerializationConfig config, SerializerFactory f)
    {
        super(src, config, f);
        _rootNameLookup  = src._rootNameLookup;
    }

    /**
     * @since 2.8.9
     */
    protected XmlSerializerProvider(XmlSerializerProvider src) {
        super(src);
        // 21-May-2018, tatu: As per [dataformat-xml#282], should NOT really copy
        //    root name lookup as that may link back to diff version, configuration
        _rootNameLookup = new XmlRootNameLookup();
    }

    /*
    /**********************************************************************
    /* Overridden methods
    /**********************************************************************
     */

    @Override
    public DefaultSerializerProvider copy() {
        return new XmlSerializerProvider(this);
    }

    @Override
    public DefaultSerializerProvider createInstance(SerializationConfig config,
            SerializerFactory jsf) {
        return new XmlSerializerProvider(this, config, jsf);
    }

    @SuppressWarnings("resource")
    @Override
    public void serializeValue(JsonGenerator gen, Object value) throws IOException
    {
        if (value == null) {
            _serializeXmlNull(gen);
            return;
        }
        final Class<?> cls = value.getClass();
        final boolean asArray;
        final ToXmlGenerator xgen = _asXmlGenerator(gen);
        if (xgen == null) { // called by convertValue()
            asArray = false;
        } else {
            QName rootName = _rootNameFromConfig();
            if (rootName == null) {
                rootName = _rootNameLookup.findRootName(cls, _config);
            }
            _initWithRootName(xgen, rootName);
            asArray = TypeUtil.isIndexedType(cls);
            if (asArray) {
                _startRootArray(xgen, rootName);
            }
        }
        
        // From super-class implementation
        final JsonSerializer<Object> ser = findTypedValueSerializer(cls, true, null);
        try {
            ser.serialize(value, gen, this);
        } catch (Exception e) { // but wrap RuntimeExceptions, to get path information
            throw _wrapAsIOE(gen, e);
        }
        // end of super-class implementation

        if (asArray) {
            gen.writeEndObject();
        }
    }

    // @since 2.1
    @SuppressWarnings("resource")
    @Override
    public void serializeValue(JsonGenerator gen, Object value, JavaType rootType,
            JsonSerializer<Object> ser) throws IOException
    {
        if (value == null) {
            _serializeXmlNull(gen);
            return;
        }
        final boolean asArray;
        final ToXmlGenerator xgen = _asXmlGenerator(gen);
        if (xgen == null) { // called by convertValue()
            asArray = false;
        } else {
            QName rootName = _rootNameFromConfig();
            if (rootName == null) {
                rootName = _rootNameLookup.findRootName(rootType, _config);
            }
            _initWithRootName(xgen, rootName);
            asArray = TypeUtil.isIndexedType(rootType);
            if (asArray) {
                _startRootArray(xgen, rootName);
            }
        }
        if (ser == null) {
            ser = findTypedValueSerializer(rootType, true, null);
        }
        // From super-class implementation
        try {
            ser.serialize(value, gen, this);
        } catch (Exception e) { // but others do need to be, to get path etc
            throw _wrapAsIOE(gen, e);
        }
        // end of super-class implementation
        if (asArray) {
            gen.writeEndObject();
        }
    }

    protected void _serializeXmlNull(JsonGenerator jgen) throws IOException
    {
        // 14-Nov-2016, tatu: As per [dataformat-xml#213], we may have explicitly
        //    configured root name...
        QName rootName = _rootNameFromConfig();
        if (rootName == null) {
            rootName = ROOT_NAME_FOR_NULL;
        }
        if (jgen instanceof ToXmlGenerator) {
            _initWithRootName((ToXmlGenerator) jgen, rootName);
        }
        super.serializeValue(jgen, null);
    }
    
    protected void _startRootArray(ToXmlGenerator xgen, QName rootName) throws IOException
    {
        xgen.writeStartObject();
        // Could repeat root name, but what's the point? How to customize?
        xgen.writeFieldName("item");
    }    

    protected void _initWithRootName(ToXmlGenerator xgen, QName rootName) throws IOException
    {
        /* 28-Nov-2012, tatu: We should only initialize the root
         *  name if no name has been set, as per [dataformat-xml#42],
         *  to allow for custom serializers to work.
         */
        if (!xgen.setNextNameIfMissing(rootName)) {
            // however, if we are root, we... insist
            if (xgen.inRoot()) {
                xgen.setNextName(rootName);
            }
        }
        xgen.initGenerator();
        String ns = rootName.getNamespaceURI();
        /* [dataformat-xml#26] If we just try writing root element with namespace,
         * we will get an explicit prefix. But we'd rather use the default
         * namespace, so let's try to force that.
         */
        if (ns != null && ns.length() > 0) {
            try {
                xgen.getStaxWriter().setDefaultNamespace(ns);
            } catch (XMLStreamException e) {
                StaxUtil.throwAsGenerationException(e, xgen);
            }
        }
    }

    protected QName _rootNameFromConfig()
    {
        PropertyName name = _config.getFullRootName();
        if (name == null) {
            return null;
        }
        String ns = name.getNamespace();
        if (ns == null || ns.isEmpty()) {
            return new QName(name.getSimpleName());
        }
        return new QName(ns, name.getSimpleName());
    }

    protected ToXmlGenerator _asXmlGenerator(JsonGenerator gen)
        throws JsonMappingException
    {
        // [Issue#71]: When converting, we actually get TokenBuffer, which is fine
        if (!(gen instanceof ToXmlGenerator)) {
            // but verify
            if (!(gen instanceof TokenBuffer)) {
                throw JsonMappingException.from(gen,
                        "XmlMapper does not with generators of type other than ToXmlGenerator; got: "+gen.getClass().getName());
            }
            return null;
        }
        return (ToXmlGenerator) gen;
    }    

    protected IOException _wrapAsIOE(JsonGenerator g, Exception e) {
        if (e instanceof IOException) {
            return (IOException) e;
        }
        String msg = e.getMessage();
        if (msg == null) {
            msg = "[no message for "+e.getClass().getName()+"]";
        }
        return new JsonMappingException(g, msg, e);
    }
}

```
