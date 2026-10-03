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

## src/main/java/com/fasterxml/jackson/databind/deser/CreatorProperty.java

```
package com.fasterxml.jackson.databind.deser;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * This concrete sub-class implements property that is passed
 * via Creator (constructor or static factory method).
 * It is not a full-featured implementation in that its set method
 * should usually not be called for primary mutation -- instead, value must separately passed --
 * but some aspects are still needed (specifically, injection).
 *<p>
 * Note on injectable values: unlike with other mutators, where
 * deserializer and injecting are separate, here we treat the two as related
 * things. This is necessary to add proper priority, as well as to simplify
 * coordination.
 */
public class CreatorProperty
    extends SettableBeanProperty
{
    private static final long serialVersionUID = 1L;

    /**
     * Placeholder that represents constructor parameter, when it is created
     * from actual constructor.
     * May be null when a synthetic instance is created.
     */
    protected final AnnotatedParameter _annotated;

    /**
     * Id of value to inject, if value injection should be used for this parameter
     * (in addition to, or instead of, regular deserialization).
     */
    protected final Object _injectableValueId;

    /**
     * In special cases, when implementing "updateValue", we cannot use
     * constructors or factory methods, but have to fall back on using a
     * setter (or mutable field property). If so, this refers to that fallback
     * accessor.
     *<p>
     * Mutable only to allow setting after construction, but must be strictly
     * set before any use.
     * 
     * @since 2.3
     */
    protected SettableBeanProperty _fallbackSetter;

    /**
     * @since 2.1
     */
    protected final int _creatorIndex;

    /**
     * Marker flag that may have to be set during construction, to indicate that
     * although property may have been constructed and added as a placeholder,
     * it represents something that should be ignored during deserialization.
     * This mostly concerns Creator properties which may not be easily deleted
     * during processing.
     *
     * @since 2.9.4
     */
    protected boolean _ignorable;

    /**
     * @param name Name of the logical property
     * @param type Type of the property, used to find deserializer
     * @param typeDeser Type deserializer to use for handling polymorphic type
     *    information, if one is needed
     * @param contextAnnotations Contextual annotations (usually by class that
     *    declares creator [constructor, factory method] that includes
     *    this property)
     * @param param Representation of property, constructor or factory
     *    method parameter; used for accessing annotations of the property
     * @param index Index of this property within creator invocation
     * 
     * @since 2.3
     */
    public CreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName,
            TypeDeserializer typeDeser,
            Annotations contextAnnotations, AnnotatedParameter param,
            int index, Object injectableValueId,
            PropertyMetadata metadata)
    {
        super(name, type, wrapperName, typeDeser, contextAnnotations, metadata);
        _annotated = param;
        _creatorIndex = index;
        _injectableValueId = injectableValueId;
        _fallbackSetter = null;
    }

    /**
     * @since 2.3
     */
    protected CreatorProperty(CreatorProperty src, PropertyName newName) {
        super(src, newName);
        _annotated = src._annotated;
        _injectableValueId = src._injectableValueId;
        _fallbackSetter = src._fallbackSetter;
        _creatorIndex = src._creatorIndex;
        _ignorable = src._ignorable;
    }

    protected CreatorProperty(CreatorProperty src, JsonDeserializer<?> deser,
            NullValueProvider nva) {
        super(src, deser, nva);
        _annotated = src._annotated;
        _injectableValueId = src._injectableValueId;
        _fallbackSetter = src._fallbackSetter;
        _creatorIndex = src._creatorIndex;
        _ignorable = src._ignorable;
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName) {
        return new CreatorProperty(this, newName);
    }
    
    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
        if (_valueDeserializer == deser) {
            return this;
        }
        // 07-May-2019, tatu: As per [databind#2303], must keep VD/NVP in-sync if they were
        NullValueProvider nvp = (_valueDeserializer == _nullProvider) ? deser : _nullProvider;
        return new CreatorProperty(this, deser, nvp);
    }

    @Override
    public SettableBeanProperty withNullProvider(NullValueProvider nva) {
        return new CreatorProperty(this, _valueDeserializer, nva);
    }
    
    @Override
    public void fixAccess(DeserializationConfig config) {
        if (_fallbackSetter != null) {
            _fallbackSetter.fixAccess(config);
        }
    }

    /**
     * NOTE: one exception to immutability, due to problems with CreatorProperty instances
     * being shared between Bean, separate PropertyBasedCreator
     * 
     * @since 2.6
     */
    public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
        _fallbackSetter = fallbackSetter;
    }

    @Override
    public void markAsIgnorable() {
        _ignorable = true;
    }

    @Override
    public boolean isIgnorable() {
        return _ignorable;
    }

    /*
    /**********************************************************
    /* Injection support
    /**********************************************************
     */

    /**
     * Method that can be called to locate value to be injected for this
     * property, if it is configured for this.
     */
    public Object findInjectableValue(DeserializationContext context, Object beanInstance)
        throws JsonMappingException
    {
        if (_injectableValueId == null) {
            context.reportBadDefinition(ClassUtil.classOf(beanInstance),
                    String.format("Property '%s' (type %s) has no injectable value id configured",
                    getName(), getClass().getName()));
        }
        return context.findInjectableValue(_injectableValueId, this, beanInstance);
    }

    /**
     * Method to find value to inject, and inject it to this property.
     */
    public void inject(DeserializationContext context, Object beanInstance)
        throws IOException
    {
        set(beanInstance, findInjectableValue(context, beanInstance));
    }

    /*
    /**********************************************************
    /* BeanProperty impl
    /**********************************************************
     */
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        if (_annotated == null) {
            return null;
        }
        return _annotated.getAnnotation(acls);
    }

    @Override public AnnotatedMember getMember() {  return _annotated; }

    @Override public int getCreatorIndex() {
        return _creatorIndex;
    }
    
    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */

    @Override
    public void deserializeAndSet(JsonParser p, DeserializationContext ctxt,
            Object instance) throws IOException
    {
        _verifySetter();
        _fallbackSetter.set(instance, deserialize(p, ctxt));
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p,
            DeserializationContext ctxt, Object instance) throws IOException
    {
        _verifySetter();
        return _fallbackSetter.setAndReturn(instance, deserialize(p, ctxt));
    }
    
    @Override
    public void set(Object instance, Object value) throws IOException
    {
        _verifySetter();
        _fallbackSetter.set(instance, value);
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException
    {
        _verifySetter();
        return _fallbackSetter.setAndReturn(instance, value);
    }
    
    @Override
    public Object getInjectableValueId() {
        return _injectableValueId;
    }

    @Override
    public String toString() { return "[creator property, name '"+getName()+"'; inject id '"+_injectableValueId+"']"; }

    // since 2.9
    private final void _verifySetter() throws IOException {
        if (_fallbackSetter == null) {
            _reportMissingSetter(null, null);
        }
    }

    // since 2.9
    private void _reportMissingSetter(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        final String msg = "No fallback setter/field defined for creator property '"+getName()+"'";
        // Hmmmh. Should we return quietly (NOP), or error?
        // Perhaps better to throw an exception, since it's generally an error.
        if (ctxt != null ) {
            ctxt.reportBadDefinition(getType(), msg);
        } else {
            throw InvalidDefinitionException.from(p, msg, getType());
        }
    }
}

```

## src/main/java/com/fasterxml/jackson/databind/deser/impl/FieldProperty.java

```
package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * This concrete sub-class implements property that is set
 * directly assigning to a Field.
 */
public final class FieldProperty
    extends SettableBeanProperty
{
    private static final long serialVersionUID = 1L;

    final protected AnnotatedField _annotated;

    /**
     * Actual field to set when deserializing this property.
     * Transient since there is no need to persist; only needed during
     * construction of objects.
     */
    final protected transient Field _field;

    /**
     * @since 2.9
     */
    final protected boolean _skipNulls;

    public FieldProperty(BeanPropertyDefinition propDef, JavaType type,
            TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field)
    {
        super(propDef, type, typeDeser, contextAnnotations);
        _annotated = field;
        _field = field.getAnnotated();
        _skipNulls = NullsConstantProvider.isSkipper(_nullProvider);
    }

    protected FieldProperty(FieldProperty src, JsonDeserializer<?> deser,
            NullValueProvider nva) {
        super(src, deser, nva);
        _annotated = src._annotated;
        _field = src._field;
        _skipNulls = NullsConstantProvider.isSkipper(nva);
    }

    protected FieldProperty(FieldProperty src, PropertyName newName) {
        super(src, newName);
        _annotated = src._annotated;
        _field = src._field;
        _skipNulls = src._skipNulls;
    }

    /**
     * Constructor used for JDK Serialization when reading persisted object
     */
    protected FieldProperty(FieldProperty src)
    {
        super(src);
        _annotated = src._annotated;
        Field f = _annotated.getAnnotated();
        if (f == null) {
            throw new IllegalArgumentException("Missing field (broken JDK (de)serialization?)");
        }
        _field = f;
        _skipNulls = src._skipNulls;
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName) {
        return new FieldProperty(this, newName);
    }

    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
        if (_valueDeserializer == deser) {
            return this;
        }
        // 07-May-2019, tatu: As per [databind#2303], must keep VD/NVP in-sync if they were
        NullValueProvider nvp = (_valueDeserializer == _nullProvider) ? deser : _nullProvider;
        return new FieldProperty(this, deser, nvp);
    }

    @Override
    public SettableBeanProperty withNullProvider(NullValueProvider nva) {
        return new FieldProperty(this, _valueDeserializer, nva);
    }

    @Override
    public void fixAccess(DeserializationConfig config) {
        ClassUtil.checkAndFixAccess(_field,
                config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    /*
    /**********************************************************
    /* BeanProperty impl
    /**********************************************************
     */
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return (_annotated == null) ? null : _annotated.getAnnotation(acls);
    }

    @Override public AnnotatedMember getMember() {  return _annotated; }

    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */

    @Override
    public void deserializeAndSet(JsonParser p,
    		DeserializationContext ctxt, Object instance) throws IOException
    {
        Object value;
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            if (_skipNulls) {
                return;
            }
            value = _nullProvider.getNullValue(ctxt);
        } else if (_valueTypeDeserializer == null) {
            value = _valueDeserializer.deserialize(p, ctxt);
            // 04-May-2018, tatu: [databind#2023] Coercion from String (mostly) can give null
            if (value == null) {
                if (_skipNulls) {
                    return;
                }
                value = _nullProvider.getNullValue(ctxt);
            }
        } else {
            value = _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);
        }
        try {
            _field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(p, e, value);
        }
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p,
    		DeserializationContext ctxt, Object instance) throws IOException
    {
        Object value;
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            if (_skipNulls) {
                return instance;
            }
            value = _nullProvider.getNullValue(ctxt);
        } else if (_valueTypeDeserializer == null) {
            value = _valueDeserializer.deserialize(p, ctxt);
            // 04-May-2018, tatu: [databind#2023] Coercion from String (mostly) can give null
            if (value == null) {
                if (_skipNulls) {
                    return instance;
                }
                value = _nullProvider.getNullValue(ctxt);
            }
        } else {
            value = _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);
        }
        try {
            _field.set(instance, value);
        } catch (Exception e) {
            _throwAsIOE(p, e, value);
        }
        return instance;
    }

    @Override
    public void set(Object instance, Object value) throws IOException
    {
        try {
            _field.set(instance, value);
        } catch (Exception e) {
            // 15-Sep-2015, tatu: How could we get a ref to JsonParser?
            _throwAsIOE(e, value);
        }
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException
    {
        try {
            _field.set(instance, value);
        } catch (Exception e) {
            // 15-Sep-2015, tatu: How could we get a ref to JsonParser?
            _throwAsIOE(e, value);
        }
        return instance;
    }

    /*
    /**********************************************************
    /* JDK serialization handling
    /**********************************************************
     */

    Object readResolve() {
        return new FieldProperty(this);
    }
}
```

## src/main/java/com/fasterxml/jackson/databind/deser/impl/MethodProperty.java

```
package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * This concrete sub-class implements property that is set
 * using regular "setter" method.
 */
public final class MethodProperty
    extends SettableBeanProperty
{
    private static final long serialVersionUID = 1;

    protected final AnnotatedMethod _annotated;
    
    /**
     * Setter method for modifying property value; used for
     * "regular" method-accessible properties.
     */
    protected final transient Method _setter;

    /**
     * @since 2.9
     */
    final protected boolean _skipNulls;
    
    public MethodProperty(BeanPropertyDefinition propDef,
            JavaType type, TypeDeserializer typeDeser,
            Annotations contextAnnotations, AnnotatedMethod method)
    {
        super(propDef, type, typeDeser, contextAnnotations);
        _annotated = method;
        _setter = method.getAnnotated();
        _skipNulls = NullsConstantProvider.isSkipper(_nullProvider);
    }

    protected MethodProperty(MethodProperty src, JsonDeserializer<?> deser,
            NullValueProvider nva) {
        super(src, deser, nva);
        _annotated = src._annotated;
        _setter = src._setter;
        _skipNulls = NullsConstantProvider.isSkipper(nva);
    }

    protected MethodProperty(MethodProperty src, PropertyName newName) {
        super(src, newName);
        _annotated = src._annotated;
        _setter = src._setter;
        _skipNulls = src._skipNulls;
    }

    /**
     * Constructor used for JDK Serialization when reading persisted object
     */
    protected MethodProperty(MethodProperty src, Method m) {
        super(src);
        _annotated = src._annotated;
        _setter = m;
        _skipNulls = src._skipNulls;
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName) {
        return new MethodProperty(this, newName);
    }
    
    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
        if (_valueDeserializer == deser) {
            return this;
        }
        // 07-May-2019, tatu: As per [databind#2303], must keep VD/NVP in-sync if they were
        NullValueProvider nvp = (_valueDeserializer == _nullProvider) ? deser : _nullProvider;
        return new MethodProperty(this, deser, nvp);
    }

    @Override
    public SettableBeanProperty withNullProvider(NullValueProvider nva) {
        return new MethodProperty(this, _valueDeserializer, nva);
    }

    @Override
    public void fixAccess(DeserializationConfig config) {
        _annotated.fixAccess(
                config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    /*
    /**********************************************************
    /* BeanProperty impl
    /**********************************************************
     */
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return (_annotated == null) ? null : _annotated.getAnnotation(acls);
    }

    @Override public AnnotatedMember getMember() {  return _annotated; }

    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */

    @Override
    public void deserializeAndSet(JsonParser p, DeserializationContext ctxt,
            Object instance) throws IOException
    {
        Object value;
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            if (_skipNulls) {
                return;
            }
            value = _nullProvider.getNullValue(ctxt);
        } else if (_valueTypeDeserializer == null) {
            value = _valueDeserializer.deserialize(p, ctxt);
            // 04-May-2018, tatu: [databind#2023] Coercion from String (mostly) can give null
            if (value == null) {
                if (_skipNulls) {
                    return;
                }
                value = _nullProvider.getNullValue(ctxt);
            }
        } else {
            value = _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);
        }
        try {
            _setter.invoke(instance, value);
        } catch (Exception e) {
            _throwAsIOE(p, e, value);
        }
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p,
    		DeserializationContext ctxt, Object instance) throws IOException
    {
        Object value;
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            if (_skipNulls) {
                return instance;
            }
            value = _nullProvider.getNullValue(ctxt);
        } else if (_valueTypeDeserializer == null) {
            value = _valueDeserializer.deserialize(p, ctxt);
            // 04-May-2018, tatu: [databind#2023] Coercion from String (mostly) can give null
            if (value == null) {
                if (_skipNulls) {
                    return instance;
                }
                value = _nullProvider.getNullValue(ctxt);
            }
        } else {
            value = _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);
        }
        try {
            Object result = _setter.invoke(instance, value);
            return (result == null) ? instance : result;
        } catch (Exception e) {
            _throwAsIOE(p, e, value);
            return null;
        }
    }

    @Override
    public final void set(Object instance, Object value) throws IOException
    {
        try {
            _setter.invoke(instance, value);
        } catch (Exception e) {
            // 15-Sep-2015, tatu: How could we get a ref to JsonParser?
            _throwAsIOE(e, value);
        }
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException
    {
        try {
            Object result = _setter.invoke(instance, value);
            return (result == null) ? instance : result;
        } catch (Exception e) {
            // 15-Sep-2015, tatu: How could we get a ref to JsonParser?
            _throwAsIOE(e, value);
            return null;
        }
    }

    /*
    /**********************************************************
    /* JDK serialization handling
    /**********************************************************
     */

    Object readResolve() {
        return new MethodProperty(this, _annotated.getAnnotated());
    }
}

```

## src/main/java/com/fasterxml/jackson/databind/deser/impl/ObjectIdReferenceProperty.java

```
package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferenceProperty extends SettableBeanProperty
{
    private static final long serialVersionUID = 1L;

    private final SettableBeanProperty _forward;

    public ObjectIdReferenceProperty(SettableBeanProperty forward, ObjectIdInfo objectIdInfo)
    {
        super(forward);
        _forward = forward;
        _objectIdInfo = objectIdInfo;
    }

    public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, JsonDeserializer<?> deser,
            NullValueProvider nva)
    {
        super(src, deser, nva);
        _forward = src._forward;
        _objectIdInfo = src._objectIdInfo;
    }

    public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, PropertyName newName)
    {
        super(src, newName);
        _forward = src._forward;
        _objectIdInfo = src._objectIdInfo;
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName) {
        return new ObjectIdReferenceProperty(this, newName);
    }

    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
        if (_valueDeserializer == deser) {
            return this;
        }
        // 07-May-2019, tatu: As per [databind#2303], must keep VD/NVP in-sync if they were
        NullValueProvider nvp = (_valueDeserializer == _nullProvider) ? deser : _nullProvider;
        return new ObjectIdReferenceProperty(this, deser, nvp);
    }

    @Override
    public SettableBeanProperty withNullProvider(NullValueProvider nva) {
        return new ObjectIdReferenceProperty(this, _valueDeserializer, nva);
    }
    
    @Override
    public void fixAccess(DeserializationConfig config) {
        if (_forward != null) {
            _forward.fixAccess(config);
        }
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return _forward.getAnnotation(acls);
    }

    @Override
    public AnnotatedMember getMember() {
        return _forward.getMember();
    }

    @Override
    public int getCreatorIndex() {
        return _forward.getCreatorIndex();
    }

    @Override
    public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        deserializeSetAndReturn(p, ctxt, instance);
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException
    {
        try {
            return setAndReturn(instance, deserialize(p, ctxt));
        } catch (UnresolvedForwardReference reference) {
            boolean usingIdentityInfo = (_objectIdInfo != null) || (_valueDeserializer.getObjectIdReader() != null);
            if (!usingIdentityInfo) {
                throw JsonMappingException.from(p, "Unresolved forward reference but no identity info", reference);
            }
            reference.getRoid().appendReferring(new PropertyReferring(this, reference, _type.getRawClass(), instance));
            return null;
        }
    }

    @Override
    public void set(Object instance, Object value) throws IOException {
        _forward.set(instance, value);
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException {
        return _forward.setAndReturn(instance, value);
    }

    public final static class PropertyReferring extends Referring {
        private final ObjectIdReferenceProperty _parent;
        public final Object _pojo;

        public PropertyReferring(ObjectIdReferenceProperty parent,
                UnresolvedForwardReference ref, Class<?> type, Object ob)
        {
            super(ref, type);
            _parent = parent;
            _pojo = ob;
        }

        @Override
        public void handleResolvedForwardReference(Object id, Object value) throws IOException
        {
            if (!hasId(id)) {
                throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id
                        + "] that wasn't previously seen as unresolved.");
            }
            _parent.set(_pojo, value);
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
import com.fasterxml.jackson.core.JsonToken;
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

    protected ObjectIdValueProperty(ObjectIdValueProperty src, JsonDeserializer<?> deser,
            NullValueProvider nva)
    {
        super(src, deser, nva);
        _objectIdReader = src._objectIdReader;
    }

    protected ObjectIdValueProperty(ObjectIdValueProperty src, PropertyName newName) {
        super(src, newName);
        _objectIdReader = src._objectIdReader;
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName) {
        return new ObjectIdValueProperty(this, newName);
    }

    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
        if (_valueDeserializer == deser) {
            return this;
        }
        // 07-May-2019, tatu: As per [databind#2303], must keep VD/NVP in-sync if they were
        NullValueProvider nvp = (_valueDeserializer == _nullProvider) ? deser : _nullProvider;
        return new ObjectIdValueProperty(this, deser, nvp);
    }

    @Override
    public SettableBeanProperty withNullProvider(NullValueProvider nva) {
        return new ObjectIdValueProperty(this, _valueDeserializer, nva);
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
    public void deserializeAndSet(JsonParser p, DeserializationContext ctxt,
            Object instance) throws IOException
    {
        deserializeSetAndReturn(p, ctxt, instance);
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p,
    		DeserializationContext ctxt, Object instance) throws IOException
    {
        /* 02-Apr-2015, tatu: Actually, as per [databind#742], let it be;
         *  missing or null id is needed for some cases, such as cases where id
         *  will be generated externally, at a later point, and is not available
         *  quite yet. Typical use case is with DB inserts.
         */
        // note: no null checks (unlike usually); deserializer should fail if one found
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            return null;
        }
        Object id = _valueDeserializer.deserialize(p, ctxt);
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

## src/main/java/com/fasterxml/jackson/databind/deser/impl/SetterlessProperty.java

```
package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * This concrete sub-class implements Collection or Map property that is
 * indirectly by getting the property value and directly modifying it.
 */
public final class SetterlessProperty
    extends SettableBeanProperty
{
    private static final long serialVersionUID = 1L;

    protected final AnnotatedMethod _annotated;

    /**
     * Get method for accessing property value used to access property
     * (of Collection or Map type) to modify.
     */
    protected final Method _getter;

    public SetterlessProperty(BeanPropertyDefinition propDef, JavaType type,
            TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
    {
        super(propDef, type, typeDeser, contextAnnotations);
        _annotated = method;
        _getter = method.getAnnotated();
    }

    protected SetterlessProperty(SetterlessProperty src, JsonDeserializer<?> deser,
            NullValueProvider nva) {
        super(src, deser, nva);
        _annotated = src._annotated;
        _getter = src._getter;
    }

    protected SetterlessProperty(SetterlessProperty src, PropertyName newName) {
        super(src, newName);
        _annotated = src._annotated;
        _getter = src._getter;
    }

    @Override
    public SettableBeanProperty withName(PropertyName newName) {
        return new SetterlessProperty(this, newName);
    }

    @Override
    public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
        if (_valueDeserializer == deser) {
            return this;
        }
        // 07-May-2019, tatu: As per [databind#2303], must keep VD/NVP in-sync if they were
        NullValueProvider nvp = (_valueDeserializer == _nullProvider) ? deser : _nullProvider;
        return new SetterlessProperty(this, deser, nvp);
    }

    @Override
    public SettableBeanProperty withNullProvider(NullValueProvider nva) {
        return new SetterlessProperty(this, _valueDeserializer, nva);
    }

    @Override
    public void fixAccess(DeserializationConfig config) {
        _annotated.fixAccess(
                config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    /*
    /**********************************************************
    /* BeanProperty impl
    /**********************************************************
     */
    
    @Override
    public <A extends Annotation> A getAnnotation(Class<A> acls) {
        return _annotated.getAnnotation(acls);
    }

    @Override public AnnotatedMember getMember() {  return _annotated; }

    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */
    
    @Override
    public final void deserializeAndSet(JsonParser p, DeserializationContext ctxt,
            Object instance) throws IOException
    {
        JsonToken t = p.getCurrentToken();
        if (t == JsonToken.VALUE_NULL) {
            // Hmmh. Is this a problem? We won't be setting anything, so it's
            // equivalent of empty Collection/Map in this case
            return;
        }
        // For [databind#501] fix we need to implement this but:
        if (_valueTypeDeserializer != null) {
            ctxt.reportBadDefinition(getType(), String.format(
                    "Problem deserializing 'setterless' property (\"%s\"): no way to handle typed deser with setterless yet",
                    getName()));
//            return _valueDeserializer.deserializeWithType(p, ctxt, _valueTypeDeserializer);
        }
        // Ok: then, need to fetch Collection/Map to modify:
        Object toModify;
        try {
            toModify = _getter.invoke(instance, (Object[]) null);
        } catch (Exception e) {
            _throwAsIOE(p, e);
            return; // never gets here
        }
        // Note: null won't work, since we can't then inject anything in. At least
        // that's not good in common case. However, theoretically the case where
        // we get JSON null might be compatible. If so, implementation could be changed.
        if (toModify == null) {
            ctxt.reportBadDefinition(getType(), String.format(
                    "Problem deserializing 'setterless' property '%s': get method returned null",
                    getName()));
        }
        _valueDeserializer.deserialize(p, ctxt, toModify);
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p,
    		DeserializationContext ctxt, Object instance) throws IOException
    {
        deserializeAndSet(p, ctxt, instance);
        return instance;
    }

    @Override
    public final void set(Object instance, Object value) throws IOException {
        throw new UnsupportedOperationException("Should never call `set()` on setterless property ('"+getName()+"')");
    }

    @Override
    public Object setAndReturn(Object instance, Object value) throws IOException
    {
        set(instance, value);
        return instance;
    }
}
```

## src/main/java/com/fasterxml/jackson/databind/deser/std/AtomicReferenceDeserializer.java

```
package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializer
    extends ReferenceTypeDeserializer<AtomicReference<Object>>
{
    private static final long serialVersionUID = 1L;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    /**
     * @since 2.9
     */
    public AtomicReferenceDeserializer(JavaType fullType, ValueInstantiator inst,
            TypeDeserializer typeDeser, JsonDeserializer<?> deser)
    {
        super(fullType, inst, typeDeser, deser);
    }

    /*
    /**********************************************************
    /* Abstract method implementations
    /**********************************************************
     */

    @Override
    public AtomicReferenceDeserializer withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
        return new AtomicReferenceDeserializer(_fullType, _valueInstantiator,
                typeDeser, valueDeser);
    }

    @Override
    public AtomicReference<Object> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return new AtomicReference<Object>(_valueDeserializer.getNullValue(ctxt));
    }

    @Override
    public Object getEmptyValue(DeserializationContext ctxt) {
        return new AtomicReference<Object>();
    }
    
    @Override
    public AtomicReference<Object> referenceValue(Object contents) {
        return new AtomicReference<Object>(contents);
    }

    @Override
    public Object getReferenced(AtomicReference<Object> reference) {
        return reference.get();
    }

    @Override // since 2.9
    public AtomicReference<Object> updateReference(AtomicReference<Object> reference, Object contents) {
        reference.set(contents);
        return reference;
    }

    @Override // since 2.9
    public Boolean supportsUpdate(DeserializationConfig config) {
        // yes; regardless of value deserializer reference itself may be updated
        return Boolean.TRUE;
    }
}

```
