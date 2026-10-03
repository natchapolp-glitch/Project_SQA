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
    <artifactId>jackson-base</artifactId>
    <version>2.9.9-SNAPSHOT</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.9-SNAPSHOT</version>
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
    <!-- With Jackson 2.9 we will require JDK 7 (except for annotations/streaming),
         and new language features (diamond pattern) may be used.
         JDK classes are still loaded dynamically since there isn't much downside
         (small number of types); this allows use on JDK 6 platforms still (including
         Android)
      -->
    <javac.src.version>1.7</javac.src.version>
    <javac.target.version>1.7</javac.target.version>

    <!-- Can not use default, since group id != Java package name here -->
    <osgi.export>com.fasterxml.jackson.databind.*;version=${project.version}</osgi.export>
    <osgi.import> <!-- fix for databind#2299: using jackson-databind in an OSGi environment under Android --> 
        org.w3c.dom.bootstrap;resolution:=optional,
        *
    </osgi.import>

    <!-- Generate PackageVersion.java into this directory. -->
    <packageVersion.dir>com/fasterxml/jackson/databind/cfg</packageVersion.dir>
    <packageVersion.package>com.fasterxml.jackson.databind.cfg</packageVersion.package>

    <!-- since 2.9.1: NOTE! can not use packageVersion.package as is -->
    <jdk.module.name>com.fasterxml.jackson.databind</jdk.module.name>
  </properties>

  <dependencies>
    <!-- Builds on core streaming API; also needs core annotations -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <!-- 06-Mar-2017, tatu: Although bom provides for dependencies, some legacy
             usage seems to benefit from actually specifying version here in case
             it is dependent on transitively
        -->
      <version>${jackson.version.annotations}</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>${jackson.version.core}</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->

    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>1.7.4</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>1.7.4</version>
      <scope>test</scope>
    </dependency>
    <!-- For testing TestNoClassDefFoundDeserializer -->
    <dependency>
      <groupId>javax.measure</groupId>
      <artifactId>jsr-275</artifactId>
      <version>1.0.0</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <!-- Alas, need to include snapshot reference since otherwise can not find
       snapshot of parent... -->
  <repositories>
    <repository>
      <id>sonatype-nexus-snapshots</id>
      <name>Sonatype Nexus Snapshots</name>
      <url>https://repo1.maven.org/maven2</url>
      <releases><enabled>false</enabled></releases>
      <snapshots><enabled>true</enabled></snapshots>
    </repository>
  </repositories>

  <build>
     <plugins>
      <!-- Important: enable enforcer plug-in: -->
      <plugin>
        <artifactId>maven-enforcer-plugin</artifactId>
        <executions> <!-- or?  combine.children="merge"> -->
          <execution>
            <id>enforce-properties</id>
	    <phase>validate</phase>
            <goals><goal>enforce</goal></goals>
          </execution>
        </executions>
      </plugin>

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

      <!-- parent definitions should be ok, but need to add more links -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <links combine.children="append">
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.9</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.9</link>
          </links>
        </configuration>
      </plugin>

      <!-- settings are fine, but needed to trigger execution! -->
      <plugin>
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
      </plugin>

      <!-- 18-Oct-2016, tatu: Try to make coveralls work -->
      <plugin>
        <groupId>org.eluder.coveralls</groupId>
        <artifactId>coveralls-maven-plugin</artifactId>
        <version>4.3.0</version>
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

## src/main/java/com/fasterxml/jackson/databind/deser/std/StringCollectionDeserializer.java

```
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Specifically optimized version for {@link java.util.Collection}s
 * that contain String values; reason is that this is a very common
 * type and we can make use of the fact that Strings are final.
 */
@JacksonStdImpl
public final class StringCollectionDeserializer
    extends ContainerDeserializerBase<Collection<String>>
    implements ContextualDeserializer
{
    private static final long serialVersionUID = 1L;

    // // Configuration

    /**
     * Value deserializer to use, if NOT the standard one
     * (if it is, will be null).
     */
    protected final JsonDeserializer<String> _valueDeserializer;

    // // Instance construction settings:
    
    /**
     * Instantiator used in case custom handling is needed for creation.
     */
    protected final ValueInstantiator _valueInstantiator;

    /**
     * Deserializer that is used iff delegate-based creator is
     * to be used for deserializing from JSON Object.
     */
    protected final JsonDeserializer<Object> _delegateDeserializer;

    // NOTE: no PropertyBasedCreator, as JSON Arrays have no properties

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */
    
    public StringCollectionDeserializer(JavaType collectionType,
            JsonDeserializer<?> valueDeser, ValueInstantiator valueInstantiator)
    {
        this(collectionType, valueInstantiator, null, valueDeser, valueDeser, null);
    }

    @SuppressWarnings("unchecked")
    protected StringCollectionDeserializer(JavaType collectionType,
            ValueInstantiator valueInstantiator, JsonDeserializer<?> delegateDeser,
            JsonDeserializer<?> valueDeser,
            NullValueProvider nuller, Boolean unwrapSingle)
    {
        super(collectionType, nuller, unwrapSingle);
        _valueDeserializer = (JsonDeserializer<String>) valueDeser;
        _valueInstantiator = valueInstantiator;
        _delegateDeserializer = (JsonDeserializer<Object>) delegateDeser;
    }

    protected StringCollectionDeserializer withResolved(JsonDeserializer<?> delegateDeser,
            JsonDeserializer<?> valueDeser,
            NullValueProvider nuller, Boolean unwrapSingle)
    {
        if ((_unwrapSingle == unwrapSingle) && (_nullProvider == nuller)
                && (_valueDeserializer == valueDeser) && (_delegateDeserializer == delegateDeser)) {
            return this;
        }
        return new StringCollectionDeserializer(_containerType, _valueInstantiator,
                delegateDeser, valueDeser, nuller, unwrapSingle);
    }

    @Override // since 2.5
    public boolean isCachable() {
        // 26-Mar-2015, tatu: Important: prevent caching if custom deserializers via annotations
        //    are involved
        return (_valueDeserializer == null) && (_delegateDeserializer == null);
    }
    
    /*
    /**********************************************************
    /* Validation, post-processing
    /**********************************************************
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException
    {
        // May need to resolve types for delegate-based creators:
        JsonDeserializer<Object> delegate = null;
        if (_valueInstantiator != null) {
            // [databind#2324]: check both array-delegating and delegating
            AnnotatedWithParams delegateCreator = _valueInstantiator.getArrayDelegateCreator();
            if (delegateCreator != null) {
                JavaType delegateType = _valueInstantiator.getArrayDelegateType(ctxt.getConfig());
                delegate = findDeserializer(ctxt, delegateType, property);
            } else if ((delegateCreator = _valueInstantiator.getDelegateCreator()) != null) {
                JavaType delegateType = _valueInstantiator.getDelegateType(ctxt.getConfig());
                delegate = findDeserializer(ctxt, delegateType, property);
            }
        }
        JsonDeserializer<?> valueDeser = _valueDeserializer;
        final JavaType valueType = _containerType.getContentType();
        if (valueDeser == null) {
            // [databind#125]: May have a content converter
            valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
            if (valueDeser == null) {
            // And we may also need to get deserializer for String
                valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
            }
        } else { // if directly assigned, probably not yet contextual, so:
            valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
        }
        // 11-Dec-2015, tatu: Should we pass basic `Collection.class`, or more refined? Mostly
        //   comes down to "List vs Collection" I suppose... for now, pass Collection
        Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class,
                JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        NullValueProvider nuller = findContentNullProvider(ctxt, property, valueDeser);
        if (isDefaultDeserializer(valueDeser)) {
            valueDeser = null;
        }
        return withResolved(delegate, valueDeser, nuller, unwrapSingle);
    }
    
    /*
    /**********************************************************
    /* ContainerDeserializerBase API
    /**********************************************************
     */

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        JsonDeserializer<?> deser = _valueDeserializer;
        return (JsonDeserializer<Object>) deser;
    }

    @Override
    public ValueInstantiator getValueInstantiator() {
        return _valueInstantiator;
    }

    /*
    /**********************************************************
    /* JsonDeserializer API
    /**********************************************************
     */
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException
    {
        if (_delegateDeserializer != null) {
            return (Collection<String>) _valueInstantiator.createUsingDelegate(ctxt,
                    _delegateDeserializer.deserialize(p, ctxt));
        }
        final Collection<String> result = (Collection<String>) _valueInstantiator.createUsingDefault(ctxt);
        return deserialize(p, ctxt, result);
    }

    @Override
    public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt,
            Collection<String> result)
        throws IOException
    {
        // Ok: must point to START_ARRAY
        if (!p.isExpectedStartArrayToken()) {
            return handleNonArray(p, ctxt, result);
        }

        if (_valueDeserializer != null) {
            return deserializeUsingCustom(p, ctxt, result, _valueDeserializer);
        }
        try {
            while (true) {
                // First the common case:
                String value = p.nextTextValue();
                if (value != null) {
                    result.add(value);
                    continue;
                }
                JsonToken t = p.getCurrentToken();
                if (t == JsonToken.END_ARRAY) {
                    break;
                }
                if (t == JsonToken.VALUE_NULL) {
                    if (_skipNullValues) {
                        continue;
                    }
                    value = (String) _nullProvider.getNullValue(ctxt);
                } else {
                    value = _parseString(p, ctxt);
                }
                result.add(value);
            }
        } catch (Exception e) {
            throw JsonMappingException.wrapWithPath(e, result, result.size());
        }
        return result;
    }
    
    private Collection<String> deserializeUsingCustom(JsonParser p, DeserializationContext ctxt,
            Collection<String> result, final JsonDeserializer<String> deser) throws IOException
    {
        while (true) {
            /* 30-Dec-2014, tatu: This may look odd, but let's actually call method
             *   that suggest we are expecting a String; this helps with some formats,
             *   notably XML. Note, however, that while we can get String, we can't
             *   assume that's what we use due to custom deserializer
             */
            String value;
            if (p.nextTextValue() == null) {
                JsonToken t = p.getCurrentToken();
                if (t == JsonToken.END_ARRAY) {
                    break;
                }
                // Ok: no need to convert Strings, but must recognize nulls
                if (t == JsonToken.VALUE_NULL) {
                    if (_skipNullValues) {
                        continue;
                    }
                    value = (String) _nullProvider.getNullValue(ctxt);
                } else {
                    value = deser.deserialize(p, ctxt);
                }
            } else {
                value = deser.deserialize(p, ctxt);
            }
            result.add(value);
        }
        return result;
    }
    
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
            TypeDeserializer typeDeserializer) throws IOException {
        // In future could check current token... for now this should be enough:
        return typeDeserializer.deserializeTypedFromArray(p, ctxt);
    }

    /**
     * Helper method called when current token is not START_ARRAY. Will either
     * throw an exception, or try to handle value as if member of implicit
     * array, depending on configuration.
     */
    @SuppressWarnings("unchecked")
    private final Collection<String> handleNonArray(JsonParser p, DeserializationContext ctxt,
            Collection<String> result) throws IOException
    {
        // implicit arrays from single values?
        boolean canWrap = (_unwrapSingle == Boolean.TRUE) ||
                ((_unwrapSingle == null) &&
                        ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
        if (!canWrap) {
            return (Collection<String>) ctxt.handleUnexpectedToken(_containerType.getRawClass(), p);
        }
        // Strings are one of "native" (intrinsic) types, so there's never type deserializer involved
        JsonDeserializer<String> valueDes = _valueDeserializer;
        JsonToken t = p.getCurrentToken();

        String value;
        
        if (t == JsonToken.VALUE_NULL) {
            // 03-Feb-2017, tatu: Does this work?
            if (_skipNullValues) {
                return result;
            }
            value = (String) _nullProvider.getNullValue(ctxt);
        } else {
            value = (valueDes == null) ? _parseString(p, ctxt) : valueDes.deserialize(p, ctxt);
        }
        result.add(value);
        return result;
    }
}

```
