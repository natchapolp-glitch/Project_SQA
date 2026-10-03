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
    <version>2.8</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.8.11.3-SNAPSHOT</version>
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
    <!-- With Jackson 2.8 we will require JDK 7 (except for annotations/streaming),
         and new language features (diamond pattern) may be used.
         Whether JDK dependencies are directly used is an open question as of Feb-2016
      -->
    <javac.src.version>1.7</javac.src.version>
    <javac.target.version>1.7</javac.target.version>

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
      <version>2.8.10</version>
    </dependency>

    <!-- and for testing we need a few libraries
         libs for which we use reflection for code, but direct dep for testing
      -->

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-module-junit4</artifactId>
      <version>1.6.5</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.powermock</groupId>
      <artifactId>powermock-api-mockito</artifactId>
      <version>1.6.5</version>
      <scope>test</scope>
    </dependency>
    <!-- For testing TestNoClassDefFoundDeserializer -->
    <dependency>
      <groupId>javax.measure</groupId>
      <artifactId>jsr-275</artifactId>
      <version>1.0.0</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.google.jimfs</groupId>
      <artifactId>jimfs</artifactId>
      <version>1.1</version>
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

## src/main/java/com/fasterxml/jackson/databind/type/ReferenceType.java

```
package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

/**
 * Specialized {@link SimpleType} for types that are referential types,
 * that is, values that can be dereferenced to another value (or null),
 * of different type.
 * Referenced type is accessible using {@link #getContentType()}.
 * 
 * @since 2.6
 */
public class ReferenceType extends SimpleType
{
    private static final long serialVersionUID = 1L;

    protected final JavaType _referencedType;

    /**
     * Essential type used for type ids, for example if type id is needed for
     * referencing type with polymorphic handling. Typically initialized when
     * a {@link SimpleType} is upgraded into reference type, but NOT changed
     * if being sub-classed.
     *
     * @since 2.8
     */
    protected final JavaType _anchorType;

    protected ReferenceType(Class<?> cls, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts, JavaType refType,
            JavaType anchorType,
            Object valueHandler, Object typeHandler, boolean asStatic)
    {
        super(cls, bindings, superClass, superInts, refType.hashCode(),
                valueHandler, typeHandler, asStatic);
        _referencedType = refType;
        _anchorType = (anchorType == null) ? this : anchorType;
    }

    /**
     * Constructor used when upgrading into this type (via {@link #upgradeFrom},
     * the usual way for {@link ReferenceType}s to come into existence.
     * Sets up what is considered the "base" reference type
     *
     * @since 2.7
     */
    protected ReferenceType(TypeBase base, JavaType refType)
    {
        super(base);
        _referencedType = refType;
        // we'll establish this as the anchor type
        _anchorType = this;
    }

    /**
     * Factory method that can be used to "upgrade" a basic type into collection-like
     * one; usually done via {@link TypeModifier}
     * 
     * @param baseType Resolved non-reference type (usually {@link SimpleType}) that is being upgraded
     * @param refdType Referenced type; usually the first and only type parameter, but not necessarily
     *
     * @since 2.7
     */
    public static ReferenceType upgradeFrom(JavaType baseType, JavaType refdType) {
        if (refdType == null) {
            throw new IllegalArgumentException("Missing referencedType");
        }
        // 19-Oct-2015, tatu: Not sure if and how other types could be used as base;
        //    will cross that bridge if and when need be
        if (baseType instanceof TypeBase) {
            return new ReferenceType((TypeBase) baseType, refdType);
        }
        throw new IllegalArgumentException("Can not upgrade from an instance of "+baseType.getClass());
    }

    /**
     * @since 2.7
     */
    public static ReferenceType construct(Class<?> cls, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts, JavaType refType)
    {
        return new ReferenceType(cls, bindings, superClass, superInts,
                refType, null, null, null, false);
    }

    @Deprecated // since 2.7
    public static ReferenceType construct(Class<?> cls, JavaType refType) {
        return new ReferenceType(cls, TypeBindings.emptyBindings(),
                // !!! TODO: missing supertypes
                null, null, null, refType, null, null, false);
    }

    @Override
    public JavaType withContentType(JavaType contentType) {
        if (_referencedType == contentType) {
            return this;
        }
        return new ReferenceType(_class, _bindings, _superClass, _superInterfaces,
                contentType, _anchorType, _valueHandler, _typeHandler, _asStatic);
    }

    @Override
    public ReferenceType withTypeHandler(Object h)
    {
        if (h == _typeHandler) {
            return this;
        }
        return new ReferenceType(_class, _bindings, _superClass, _superInterfaces,
                _referencedType, _anchorType, _valueHandler, h, _asStatic);
    }

    @Override
    public ReferenceType withContentTypeHandler(Object h)
    {
        if (h == _referencedType.<Object>getTypeHandler()) {
            return this;
        }
        return new ReferenceType(_class, _bindings, _superClass, _superInterfaces,
                _referencedType.withTypeHandler(h), _anchorType,
                _valueHandler, _typeHandler, _asStatic);
    }

    @Override
    public ReferenceType withValueHandler(Object h) {
        if (h == _valueHandler) {
            return this;
        }
        return new ReferenceType(_class, _bindings,
                _superClass, _superInterfaces, _referencedType, _anchorType,
                h, _typeHandler,_asStatic);
    }

    @Override
    public ReferenceType withContentValueHandler(Object h) {
        if (h == _referencedType.<Object>getValueHandler()) {
            return this;
        }
        JavaType refdType = _referencedType.withValueHandler(h);
        return new ReferenceType(_class, _bindings,
                _superClass, _superInterfaces, refdType, _anchorType,
                _valueHandler, _typeHandler, _asStatic);
    }

    @Override
    public ReferenceType withStaticTyping() {
        if (_asStatic) {
            return this;
        }
        return new ReferenceType(_class, _bindings, _superClass, _superInterfaces,
                _referencedType.withStaticTyping(), _anchorType,
                 _valueHandler, _typeHandler, true);
    }

    @Override
    public JavaType refine(Class<?> rawType, TypeBindings bindings,
            JavaType superClass, JavaType[] superInterfaces) {
        return new ReferenceType(rawType, _bindings,
                superClass, superInterfaces, _referencedType, _anchorType,
                _valueHandler, _typeHandler, _asStatic);
    }

    @Override
    protected String buildCanonicalName()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(_class.getName());
        sb.append('<');
        sb.append(_referencedType.toCanonical());
        sb.append('>');
        return sb.toString();
    }

    /*
    /**********************************************************
    /* Narrow/widen
    /**********************************************************
     */

    @Override
    @Deprecated // since 2.7
    protected JavaType _narrow(Class<?> subclass)
    {
        // Should we check that there is a sub-class relationship?
        return new ReferenceType(subclass, _bindings,
                _superClass, _superInterfaces, _referencedType, _anchorType,
                _valueHandler, _typeHandler, _asStatic);
    }

    /*
    /**********************************************************
    /* Public API overrides
    /**********************************************************
     */

    @Override
    public JavaType getContentType() {
        return _referencedType;
    }

    @Override
    public JavaType getReferencedType() {
        return _referencedType;
    }

    @Override
    public boolean hasContentType() {
        return true;
    }

    @Override
    public boolean isReferenceType() {
        return true;
    }

    @Override
    public StringBuilder getErasedSignature(StringBuilder sb) {
        return _classSignature(_class, sb, true);
    }
    
    @Override
    public StringBuilder getGenericSignature(StringBuilder sb)
    {
        _classSignature(_class, sb, false);
        sb.append('<');
        sb = _referencedType.getGenericSignature(sb);
        sb.append(">;");
        return sb;
    }

    /*
    /**********************************************************
    /* Extended API
    /**********************************************************
     */

    public JavaType getAnchorType() {
        return _anchorType;
    }

    /**
     * Convenience accessor that allows checking whether this is the anchor type
     * itself; if not, it must be one of supertypes that is also a {@link ReferenceType}
     */
    public boolean isAnchorType() {
        return (_anchorType == this);
    }

    /*
    /**********************************************************
    /* Standard methods
    /**********************************************************
     */

    @Override
    public String toString()
    {
        return new StringBuilder(40)
            .append("[reference type, class ")
            .append(buildCanonicalName())
            .append('<')
            .append(_referencedType)
            .append('>')
            .append(']')
            .toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;

        ReferenceType other = (ReferenceType) o;

        if (other._class != _class) return false;
        
        // Otherwise actually mostly worry about referenced type
        return _referencedType.equals(other._referencedType);
    }
}

```
