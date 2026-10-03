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
    <version>2.5</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.5.3-SNAPSHOT</version>
  <name>jackson-databind</name>
  <packaging>bundle</packaging>
  <description>General data-binding functionality for Jackson: works on core streaming API</description>
  <url>http://github.com/FasterXML/jackson</url>

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
      <version>2.5.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.5.1</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->
    <dependency>
      <groupId>cglib</groupId>
      <artifactId>cglib</artifactId>
      <version>3.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.codehaus.groovy</groupId>
      <artifactId>groovy</artifactId>
      <version>2.4.0</version>
      <scope>test</scope>
    </dependency>
    <!-- For testing TestNoClassDefFoundDeserializer -->
    <dependency>
      <groupId>javax.measure</groupId>
      <artifactId>jsr-275</artifactId>
      <version>0.9.2</version>
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
          <links>
            <link>http://docs.oracle.com/javase/6/docs/api/</link>
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.5/</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.5/</link>
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

## src/main/java/com/fasterxml/jackson/databind/deser/DefaultDeserializationContext.java

```
package com.fasterxml.jackson.databind.deser;

import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Complete {@link DeserializationContext} implementation that adds
 * extended API for {@link ObjectMapper} (and {@link ObjectReader})
 * to call, as well as implements certain parts that base class
 * has left abstract.
 * The remaining abstract methods ({@link #createInstance}, {@link #with})
 * are left so that custom implementations will properly implement them
 * to return intended subtype.
 */
public abstract class DefaultDeserializationContext
    extends DeserializationContext
    implements java.io.Serializable // since 2.1
{
    private static final long serialVersionUID = 1L;

    protected transient LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> _objectIds;

    private List<ObjectIdResolver> _objectIdResolvers;

    /**
     * Constructor that will pass specified deserializer factory and
     * cache: cache may be null (in which case default implementation
     * will be used), factory can not be null
     */
    protected DefaultDeserializationContext(DeserializerFactory df, DeserializerCache cache) {
        super(df, cache);
    }
    
    protected DefaultDeserializationContext(DefaultDeserializationContext src,
            DeserializationConfig config, JsonParser jp, InjectableValues values) {
        super(src, config, jp, values);
    }

    protected DefaultDeserializationContext(DefaultDeserializationContext src,
            DeserializerFactory factory) {
        super(src, factory);
    }

    /**
     * @since 2.4.4
     */
    protected DefaultDeserializationContext(DefaultDeserializationContext src) {
        super(src);
    }
    
    /**
     * Method needed to ensure that {@link ObjectMapper#copy} will work
     * properly; specifically, that caches are cleared, but settings
     * will otherwise remain identical; and that no sharing of state
     * occurs.
     * 
     * @since 2.4.4
     */
    public DefaultDeserializationContext copy() {
        throw new IllegalStateException("DefaultDeserializationContext sub-class not overriding copy()");
    }

    /*
    /**********************************************************
    /* Abstract methods impls, Object Id
    /**********************************************************
     */

    @Override
    public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType)
    {
        /* 02-Apr-2015, tatu: As per [databind#742] should allow 'null', similar to how
         *   missing id already works.
         */
        if (id == null) {
            return null;
        }

        final ObjectIdGenerator.IdKey key = gen.key(id);

        if (_objectIds == null) {
            _objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey,ReadableObjectId>();
        } else {
            ReadableObjectId entry = _objectIds.get(key);
            if (entry != null) {
                return entry;
            }
        }

        // Not seen yet, must create entry and configure resolver.
        ObjectIdResolver resolver = null;

        if (_objectIdResolvers == null) {
            _objectIdResolvers = new ArrayList<ObjectIdResolver>(8);
        } else {
            for (ObjectIdResolver res : _objectIdResolvers) {
                if (res.canUseFor(resolverType)) {
                    resolver = res;
                    break;
                }
            }
        }

        if (resolver == null) {
            resolver = resolverType.newForDeserialization(this);
            // 19-Dec-2014, tatu: For final 2.5.0, remove temporary (2.4.x) work-around
            //   needed to clear state between calls.
            // !!! 18-Jun-2014, pgelinas: Temporary fix for [#490] until real
            //    fix (for jackson-annotations, SimpleObjectIdResolver) can be added.
            /*
            if (resolverType.getClass() == SimpleObjectIdResolver.class) {
                resolver = new SimpleObjectIdResolver();
            }
            */
            _objectIdResolvers.add(resolver);
        }

        ReadableObjectId entry = new ReadableObjectId(key);
        entry.setResolver(resolver);
        _objectIds.put(key, entry);
        return entry;
    }
    
    @Deprecated // since 2.4
    @Override
    public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen) {
        return findObjectId(id, gen, new SimpleObjectIdResolver());
    }

    @Override
    public void checkUnresolvedObjectId() throws UnresolvedForwardReference
    {
        if (_objectIds == null) {
            return;
        }
        // 29-Dec-2014, tatu: As per [databind#299], may also just let unresolved refs be...
        if (!isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)) {
            return;
        }
        UnresolvedForwardReference exception = null;
        for (Entry<IdKey,ReadableObjectId> entry : _objectIds.entrySet()) {
            ReadableObjectId roid = entry.getValue();
            if (roid.hasReferringProperties()) {
                if (exception == null) {
                    exception = new UnresolvedForwardReference("Unresolved forward references for: ");
                }
                for (Iterator<Referring> iterator = roid.referringProperties(); iterator.hasNext(); ) {
                    Referring referring = iterator.next();
                    exception.addUnresolvedId(roid.getKey().key, referring.getBeanType(), referring.getLocation());
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    /*
    /**********************************************************
    /* Abstract methods impls, other factory methods
    /**********************************************************
     */
    
    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef)
        throws JsonMappingException
    {
        if (deserDef == null) {
            return null;
        }
        JsonDeserializer<?> deser;
        
        if (deserDef instanceof JsonDeserializer) {
            deser = (JsonDeserializer<?>) deserDef;
        } else {
            /* Alas, there's no way to force return type of "either class
             * X or Y" -- need to throw an exception after the fact
             */
            if (!(deserDef instanceof Class)) {
                throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type "+deserDef.getClass().getName()+"; expected type JsonDeserializer or Class<JsonDeserializer> instead");
            }
            Class<?> deserClass = (Class<?>)deserDef;
            // there are some known "no class" markers to consider too:
            if (deserClass == JsonDeserializer.None.class || ClassUtil.isBogusClass(deserClass)) {
                return null;
            }
            if (!JsonDeserializer.class.isAssignableFrom(deserClass)) {
                throw new IllegalStateException("AnnotationIntrospector returned Class "+deserClass.getName()+"; expected Class<JsonDeserializer>");
            }
            HandlerInstantiator hi = _config.getHandlerInstantiator();
            deser = (hi == null) ? null : hi.deserializerInstance(_config, ann, deserClass);
            if (deser == null) {
                deser = (JsonDeserializer<?>) ClassUtil.createInstance(deserClass,
                        _config.canOverrideAccessModifiers());
            }
        }
        // First: need to resolve
        if (deser instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) deser).resolve(this);
        }
        return (JsonDeserializer<Object>) deser;
    }

    @Override
    public final KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef)
        throws JsonMappingException
    {
        if (deserDef == null) {
            return null;
        }

        KeyDeserializer deser;
        
        if (deserDef instanceof KeyDeserializer) {
            deser = (KeyDeserializer) deserDef;
        } else {
            if (!(deserDef instanceof Class)) {
                throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type "
                        +deserDef.getClass().getName()
                        +"; expected type KeyDeserializer or Class<KeyDeserializer> instead");
            }
            Class<?> deserClass = (Class<?>)deserDef;
            // there are some known "no class" markers to consider too:
            if (deserClass == KeyDeserializer.None.class || ClassUtil.isBogusClass(deserClass)) {
                return null;
            }
            if (!KeyDeserializer.class.isAssignableFrom(deserClass)) {
                throw new IllegalStateException("AnnotationIntrospector returned Class "+deserClass.getName()
                        +"; expected Class<KeyDeserializer>");
            }
            HandlerInstantiator hi = _config.getHandlerInstantiator();
            deser = (hi == null) ? null : hi.keyDeserializerInstance(_config, ann, deserClass);
            if (deser == null) {
                deser = (KeyDeserializer) ClassUtil.createInstance(deserClass,
                        _config.canOverrideAccessModifiers());
            }
        }
        // First: need to resolve
        if (deser instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) deser).resolve(this);
        }
        return deser;
    }

    /*
    /**********************************************************
    /* Extended API
    /**********************************************************
     */

    /**
     * Fluent factory method used for constructing a blueprint instance
     * with different factory
     */
    public abstract DefaultDeserializationContext with(DeserializerFactory factory);
    
    /**
     * Method called to create actual usable per-deserialization
     * context instance.
     */
    public abstract DefaultDeserializationContext createInstance(
            DeserializationConfig config, JsonParser jp, InjectableValues values);
    
    /*
    /**********************************************************
    /* And then the concrete implementation class
    /**********************************************************
     */

    /**
     * Actual full concrete implementation
     */
    public final static class Impl extends DefaultDeserializationContext
    {
        private static final long serialVersionUID = 1L;

        /**
         * Default constructor for a blueprint object, which will use the standard
         * {@link DeserializerCache}, given factory.
         */
        public Impl(DeserializerFactory df) {
            super(df, null);
        }

        protected Impl(Impl src,
                DeserializationConfig config, JsonParser jp, InjectableValues values) {
            super(src, config, jp, values);
        }

        protected Impl(Impl src) { super(src); }
        
        protected Impl(Impl src, DeserializerFactory factory) {
            super(src, factory);
        }

        @Override
        public DefaultDeserializationContext copy() {
            if (getClass() != Impl.class) {
                return super.copy();
            }
           return new Impl(this);
        }
        
        @Override
        public DefaultDeserializationContext createInstance(DeserializationConfig config,
                JsonParser jp, InjectableValues values) {
            return new Impl(this, config, jp, values);
        }

        @Override
        public DefaultDeserializationContext with(DeserializerFactory factory) {
            return new Impl(this, factory);
        }        
    }
}

```

## src/main/java/com/fasterxml/jackson/databind/deser/impl/ObjectIdValueProperty.java

```
package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

/**
 * Specialized {@link SettableBeanProperty} implementation used
 * for virtual property that represents Object Id that is used
 * for some POJO types (or properties).
 */
public final class ObjectIdValueProperty
    extends SettableBeanProperty
{
    private static final long serialVersionUID = 1L;

    protected final ObjectIdReader _objectIdReader;

    public ObjectIdValueProperty(ObjectIdReader objectIdReader,
            PropertyMetadata metadata)
    {
        super(objectIdReader.propertyName, objectIdReader.getIdType(), metadata,
                objectIdReader.getDeserializer());
        _objectIdReader = objectIdReader;
    }

    protected ObjectIdValueProperty(ObjectIdValueProperty src, JsonDeserializer<?> deser)
    {
        super(src, deser);
        _objectIdReader = src._objectIdReader;
    }

    @Deprecated // since 2.3
    protected ObjectIdValueProperty(ObjectIdValueProperty src, PropertyName newName) {
        super(src, newName);
        _objectIdReader = src._objectIdReader;
    }
    
    @Deprecated // since 2.3
    protected ObjectIdValueProperty(ObjectIdValueProperty src, String newName) {
        this(src, new PropertyName(newName));
    }

    @Override
    public ObjectIdValueProperty withName(PropertyName newName) {
        return new ObjectIdValueProperty(this, newName);
    }

    @Override
    public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new ObjectIdValueProperty(this, deser);
    }
    
    // // // BeanProperty impl
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return null;
    }

    @Override public AnnotatedMember getMember() {  return null; }

    /*
    /**********************************************************
    /* Deserialization methods
    /**********************************************************
     */

    @Override
    public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt,
            Object instance) throws IOException
    {
        deserializeSetAndReturn(jp, ctxt, instance);
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser jp,
    		DeserializationContext ctxt, Object instance) throws IOException
    {
        // note: no null checks (unlike usually); deserializer should fail if one found
        Object id = _valueDeserializer.deserialize(jp, ctxt);

        /* 02-Apr-2015, tatu: Actually, as per [databind#742], let it be;
         *  missing or null id is needed for some cases, such as cases where id
         *  will be generated externally, at a later point, and is not available
         *  quite yet. Typical use case is with DB inserts.
         */
        if (id == null) {
            return null;
        }
        
        ReadableObjectId roid = ctxt.findObjectId(id, _objectIdReader.generator, _objectIdReader.resolver);
        roid.bindItem(instance);
        // also: may need to set a property value as well
        SettableBeanProperty idProp = _objectIdReader.idProperty;
        if (idProp != null) {
            return idProp.setAndReturn(instance, id);
        }
        return instance;
    }

    @Override
    public void set(Object instance, Object value) throws IOException {
        setAndReturn(instance, value);
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException
    {
        SettableBeanProperty idProp = _objectIdReader.idProperty;
        if (idProp == null) {
            throw new UnsupportedOperationException(
                    "Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
        }
        return idProp.setAndReturn(instance, value);
    }
}

```
