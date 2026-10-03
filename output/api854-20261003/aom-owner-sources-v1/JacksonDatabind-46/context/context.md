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

    protected ReferenceType(Class<?> cls, JavaType refType,
            Object valueHandler, Object typeHandler, boolean asStatic)
    {
        super(cls, refType.hashCode(),
                valueHandler, typeHandler, asStatic);
        _referencedType = refType;
    }

    public static ReferenceType construct(Class<?> cls, JavaType refType,
            Object valueHandler, Object typeHandler)
    {
        return new ReferenceType(cls, refType, null, null, false);
    }                                   
    
    @Override
    public ReferenceType withTypeHandler(Object h)
    {
        if (h == _typeHandler) {
            return this;
        }
        return new ReferenceType(_class, _referencedType, _valueHandler, h, _asStatic);
    }

    @Override
    public ReferenceType withContentTypeHandler(Object h)
    {
        if (h == _referencedType.<Object>getTypeHandler()) {
            return this;
        }
        return new ReferenceType(_class, _referencedType.withTypeHandler(h),
                _valueHandler, _typeHandler, _asStatic);
    }

    @Override
    public ReferenceType withValueHandler(Object h) {
        if (h == _valueHandler) {
            return this;
        }
        return new ReferenceType(_class, _referencedType, h, _typeHandler,_asStatic);
    }

    @Override
    public ReferenceType withContentValueHandler(Object h) {
        if (h == _referencedType.<Object>getValueHandler()) {
            return this;
        }
        return new ReferenceType(_class, _referencedType.withValueHandler(h),
                _valueHandler, _typeHandler, _asStatic);
    }

    @Override
    public ReferenceType withStaticTyping() {
        if (_asStatic) {
            return this;
        }
        return new ReferenceType(_class, _referencedType.withStaticTyping(),
                 _valueHandler, _typeHandler, true);
    }

    @Override
    protected String buildCanonicalName()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(_class.getName());
        sb.append('<');
        sb.append(_referencedType.toCanonical());
        return sb.toString();
    }

    /*
    /**********************************************************
    /* Narrow/widen
    /**********************************************************
     */
    
    @Override
    protected JavaType _narrow(Class<?> subclass)
    {
        // Should we check that there is a sub-class relationship?
        return new ReferenceType(subclass, _referencedType,
                _valueHandler, _typeHandler, _asStatic);
    }

    /*
    /**********************************************************
    /* Extended API
    /**********************************************************
     */
    
    @Override
    public JavaType getReferencedType() {
        return _referencedType;
    }

    @Override
    public boolean isReferenceType() {
        return true;
    }
    
    /*
    /**********************************************************
    /* Public API overrides
    /**********************************************************
     */

    @Override
    public int containedTypeCount() {
        return 1;
    }

    @Override
    public JavaType containedType(int index) {
        return (index == 0) ? _referencedType : null;
    }

    @Override
    public String containedTypeName(int index) {
        return (index == 0) ? "T" : null;
    }

    @Override
    public Class<?> getParameterSource() {
        // Hmmh. For now, assume it's the raw type
        return _class;
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
