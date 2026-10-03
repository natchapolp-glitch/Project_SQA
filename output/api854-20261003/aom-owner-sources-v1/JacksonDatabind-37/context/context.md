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
    <version>2.7</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.7.1-SNAPSHOT</version>
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
    <!-- Ok, so, Jackson 2.7 require JDK 1.7 (except for annotations/streaming). But
        we do not have strict need for Java 7 bytecode. And, unfortunately, it looks
        like we have some issues with Mockito tests if we try to go "full 1.7"...
        So that is why... this:
      -->
    <javac.src.version>1.6</javac.src.version>
    <javac.target.version>1.6</javac.target.version>

    <!-- Can not use default, since group id != Java package name here -->
    <osgi.export>com.fasterxml.jackson.databind.*;version=${project.version}</osgi.export>
    <!-- but imports should work fine with defaults -->

    <!-- Generate PackageVersion.java into this directory. -->
    <packageVersion.dir>com/fasterxml/jackson/databind/cfg</packageVersion.dir>
    <packageVersion.package>com.fasterxml.jackson.databind.cfg</packageVersion.package>
  </properties>

  <dependencies>
    <!-- Builds on core streaming API; also needs core annotations -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.7.0</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->
    <!-- Mock -->
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>1.6.3</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>1.6.3</version>
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
            <link>http://fasterxml.github.com/jackson-annotations/javadoc/2.7</link>
            <link>http://fasterxml.github.com/jackson-core/javadoc/2.7</link>
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

## src/main/java/com/fasterxml/jackson/databind/type/SimpleType.java

```
package com.fasterxml.jackson.databind.type;

import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

/**
 * Simple types are defined as anything other than one of recognized
 * container types (arrays, Collections, Maps). For our needs we
 * need not know anything further, since we have no way of dealing
 * with generic types other than Collections and Maps.
 */
public class SimpleType // note: until 2.6 was final
    extends TypeBase
{
    private static final long serialVersionUID = 1L;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    /**
     * Constructor only used by core Jackson databind functionality;
     * should never be called by application code.
     *<p>
     * As with other direct construction that by-passes {@link TypeFactory},
     * no introspection occurs with respect to super-types; caller must be
     * aware of consequences if using this method.
     */
    protected SimpleType(Class<?> cls) {
        this(cls, TypeBindings.emptyBindings(), null, null);
    }

    protected SimpleType(Class<?> cls, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts) {
        this(cls, bindings, superClass, superInts, null, null, false);
    }

    /**
     * Simple copy-constructor, usually used when upgrading/refining a simple type
     * into more specialized type.
     *
     * @since 2.7
     */
    protected SimpleType(TypeBase base) {
        super(base);
    }

    protected SimpleType(Class<?> cls, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts,
            Object valueHandler, Object typeHandler, boolean asStatic)
    {
        super(cls, bindings, superClass, superInts,
                0, valueHandler, typeHandler, asStatic);
    }

    /**
     * Pass-through constructor used by {@link ReferenceType}.
     * 
     * @since 2.6
     */
    protected SimpleType(Class<?> cls, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts, int extraHash,
            Object valueHandler, Object typeHandler, boolean asStatic)
    {
        super(cls, bindings, superClass, superInts, 
                extraHash, valueHandler, typeHandler, asStatic);
    }
    
    /**
     * Method used by core Jackson classes: NOT to be used by application code:
     * it does NOT properly handle inspection of super-types, so neither parent
     * Classes nor implemented Interfaces are accessible with resulting type
     * instance.
     *<p>
     * NOTE: public only because it is called by <code>ObjectMapper</code> which is
     * not in same package
     */
    public static SimpleType constructUnsafe(Class<?> raw) {
        return new SimpleType(raw, null,
                // 18-Oct-2015, tatu: Should be ok to omit possible super-types, right?
                null, null, null, null, false);
    }

    /**
     * Method that should NOT to be used by application code:
     * it does NOT properly handle inspection of super-types, so neither parent
     * Classes nor implemented Interfaces are accessible with resulting type
     * instance. Instead, please use {@link TypeFactory}'s <code>constructType</code>
     * methods which handle introspection appropriately.
     *<p>
     * Note that prior to 2.7, method usage was not limited and would typically
     * have worked acceptably: the problem comes from inability to resolve super-type
     * information, for which {@link TypeFactory} is needed.
     * 
     * @deprecated Since 2.7
     */
    @Deprecated
    public static SimpleType construct(Class<?> cls)
    {
        /* Let's add sanity checks, just to ensure no
         * Map/Collection entries are constructed
         */
        if (Map.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: "+cls.getName()+")");
        }
        if (Collection.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: "+cls.getName()+")");
        }
        // ... and while we are at it, not array types either
        if (cls.isArray()) {
            throw new IllegalArgumentException("Can not construct SimpleType for an array (class: "+cls.getName()+")");
        }
        return new SimpleType(cls);
    }

    @Override
    protected JavaType _narrow(Class<?> subclass)
    {
        if (_class == subclass) {
            return this;
        }
        // Should we check that there is a sub-class relationship?
        // 15-Jan-2016, tatu: Almost yes, but there are some complications with
        //    placeholder values, so no.
        /*
        if (!_class.isAssignableFrom(subclass)) {
            throw new IllegalArgumentException("Class "+subclass.getName()+" not sub-type of "
                    +_class.getName());
        }
        */
        // 15-Jan-2015, tatu: Not correct; should really re-resolve...
        return new SimpleType(subclass, _bindings, this, _superInterfaces,
                _valueHandler, _typeHandler, _asStatic);
    }
    
    @Override
    public JavaType withContentType(JavaType contentType) {
        throw new IllegalArgumentException("Simple types have no content types; can not call withContentType()");
    }
    
    @Override
    public SimpleType withTypeHandler(Object h) {
        if (_typeHandler == h) {
            return this;
        }
        return new SimpleType(_class, _bindings, _superClass, _superInterfaces, _valueHandler, h, _asStatic);
    }

    @Override
    public JavaType withContentTypeHandler(Object h) {
        // no content type, so:
        throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
    }

    @Override
    public SimpleType withValueHandler(Object h) {
        if (h == _valueHandler) {
            return this;
        }
        return new SimpleType(_class, _bindings, _superClass, _superInterfaces, h, _typeHandler, _asStatic);
    }
    
    @Override
    public  SimpleType withContentValueHandler(Object h) {
        // no content type, so:
        throw new IllegalArgumentException("Simple types have no content types; can not call withContenValueHandler()");
    }

    @Override
    public SimpleType withStaticTyping() {
        return _asStatic ? this : new SimpleType(_class, _bindings,
                _superClass, _superInterfaces, _valueHandler, _typeHandler, true);
    }

    @Override
    public JavaType refine(Class<?> rawType, TypeBindings bindings,
            JavaType superClass, JavaType[] superInterfaces) {
        // SimpleType means something not-specialized, so:
        return null;
    }
    
    @Override
    protected String buildCanonicalName()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(_class.getName());

        final int count = _bindings.size();
        if (count > 0) {
            sb.append('<');
            for (int i = 0; i < count; ++i) {
                JavaType t = containedType(i);
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(t.toCanonical());
            }
            sb.append('>');
        }
        return sb.toString();
    }

    /*
    /**********************************************************
    /* Public API
    /**********************************************************
     */

    @Override
    public boolean isContainerType() { return false; }

    @Override
    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(_class, sb, true);
    }
    
    @Override
    public StringBuilder getGenericSignature(StringBuilder sb)
    {
        _classSignature(_class, sb, false);

        final int count = _bindings.size();
        if (count > 0) {
            sb.append('<');
            for (int i = 0; i < count; ++i) {
                sb = containedType(i).getGenericSignature(sb);
            }
            sb.append('>');
        }
        sb.append(';');
        return sb;
    }

    /*
    /**********************************************************
    /* Standard methods
    /**********************************************************
     */

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(40);
        sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;

        SimpleType other = (SimpleType) o;

        // Classes must be identical... 
        if (other._class != this._class) return false;

        // And finally, generic bindings, if any
        TypeBindings b1 = _bindings;
        TypeBindings b2 = other._bindings;
        return b1.equals(b2);
    }
}

```
