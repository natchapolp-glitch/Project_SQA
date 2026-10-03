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

## src/main/java/com/fasterxml/jackson/databind/deser/std/NumberDeserializers.java

```
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Container class for deserializers that handle core JDK primitive
 * (and matching wrapper) types, as well as standard "big" numeric types.
 * Note that this includes types such as {@link java.lang.Boolean}
 * and {@link java.lang.Character} which are not strictly numeric,
 * but are part of primitive/wrapper types.
 */
public class NumberDeserializers
{
    private final static HashSet<String> _classNames = new HashSet<String>();
    static {
        // note: can skip primitive types; other ways to check them:
        Class<?>[] numberTypes = new Class<?>[] {
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            // and more generic ones
            Number.class, BigDecimal.class, BigInteger.class
        };
        for (Class<?> cls : numberTypes) {
            _classNames.add(cls.getName());
        }
    }

    public static JsonDeserializer<?> find(Class<?> rawType, String clsName) {
        if (rawType.isPrimitive()) {
            if (rawType == Integer.TYPE) {
                return IntegerDeserializer.primitiveInstance;
            }
            if (rawType == Boolean.TYPE) {
                return BooleanDeserializer.primitiveInstance;
            }
            if (rawType == Long.TYPE) {
                return LongDeserializer.primitiveInstance;
            }
            if (rawType == Double.TYPE) {
                return DoubleDeserializer.primitiveInstance;
            }
            if (rawType == Character.TYPE) {
                return CharacterDeserializer.primitiveInstance;
            }
            if (rawType == Byte.TYPE) {
                return ByteDeserializer.primitiveInstance;
            }
            if (rawType == Short.TYPE) {
                return ShortDeserializer.primitiveInstance;
            }
            if (rawType == Float.TYPE) {
                return FloatDeserializer.primitiveInstance;
            }
        } else if (_classNames.contains(clsName)) {
            // Start with most common types; int, boolean, long, double
            if (rawType == Integer.class) {
                return IntegerDeserializer.wrapperInstance;
            }
            if (rawType == Boolean.class) {
                return BooleanDeserializer.wrapperInstance;
            }
            if (rawType == Long.class) {
                return LongDeserializer.wrapperInstance;
            }
            if (rawType == Double.class) {
                return DoubleDeserializer.wrapperInstance;
            }
            if (rawType == Character.class) {
                return CharacterDeserializer.wrapperInstance;
            }
            if (rawType == Byte.class) {
                return ByteDeserializer.wrapperInstance;
            }
            if (rawType == Short.class) {
                return ShortDeserializer.wrapperInstance;
            }
            if (rawType == Float.class) {
                return FloatDeserializer.wrapperInstance;
            }
            if (rawType == Number.class) {
                return NumberDeserializer.instance;
            }
            if (rawType == BigDecimal.class) {
                return BigDecimalDeserializer.instance;
            }
            if (rawType == BigInteger.class) {
                return BigIntegerDeserializer.instance;
            }
        } else {
            return null;
        }
        // should never occur
        throw new IllegalArgumentException("Internal error: can't find deserializer for "+rawType.getName());
    }
    
    /*
    /**********************************************************
    /* Then one intermediate base class for things that have
    /* both primitive and wrapper types
    /**********************************************************
     */

    protected abstract static class PrimitiveOrWrapperDeserializer<T>
        extends StdScalarDeserializer<T>
    {
        private static final long serialVersionUID = 1L;

        protected final T _nullValue;
        protected final boolean _primitive;

        protected PrimitiveOrWrapperDeserializer(Class<T> vc, T nvl) {
            super(vc);
            _nullValue = nvl;
            _primitive = vc.isPrimitive();
        }

        @Override
        public final T getNullValue(DeserializationContext ctxt) throws JsonMappingException
        {
            if (_primitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
                throw ctxt.mappingException(
                        "Can not map JSON null into type %s (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)",
                        handledType().toString());
            }
            return _nullValue;
        }

        @Override
        @Deprecated // remove in 2.7
        public final T getNullValue() {
            return _nullValue;
        }

        @Override
        public T getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
            // [databind#1095]: Should not allow coercion from into null from Empty String
            // either, if `null` not allowed
            if (_primitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
                throw ctxt.mappingException(
                        "Can not map Empty String as null into type %s (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)",
                        handledType().toString());
            }
            return _nullValue;
        }
    }

    /*
    /**********************************************************
    /* Then primitive/wrapper types
    /**********************************************************
     */

    @JacksonStdImpl
    public final static class BooleanDeserializer
        extends PrimitiveOrWrapperDeserializer<Boolean>
    {
        private static final long serialVersionUID = 1L;

        final static BooleanDeserializer primitiveInstance = new BooleanDeserializer(Boolean.TYPE, Boolean.FALSE);
        final static BooleanDeserializer wrapperInstance = new BooleanDeserializer(Boolean.class, null);

        public BooleanDeserializer(Class<Boolean> cls, Boolean nvl)
        {
            super(cls, nvl);
        }
        
        @Override
        public Boolean deserialize(JsonParser j, DeserializationContext ctxt) throws IOException
        {
            return _parseBoolean(j, ctxt);
        }

        // Since we can never have type info ("natural type"; String, Boolean, Integer, Double):
        // (is it an error to even call this version?)
        @Override
        public Boolean deserializeWithType(JsonParser p, DeserializationContext ctxt,
                TypeDeserializer typeDeserializer)
            throws IOException
        {
            return _parseBoolean(p, ctxt);
        }
    }

    @JacksonStdImpl
    public static class ByteDeserializer
        extends PrimitiveOrWrapperDeserializer<Byte>
    {
        private static final long serialVersionUID = 1L;

        final static ByteDeserializer primitiveInstance = new ByteDeserializer(Byte.TYPE, (byte) 0);
        final static ByteDeserializer wrapperInstance = new ByteDeserializer(Byte.class, null);
        
        public ByteDeserializer(Class<Byte> cls, Byte nvl)
        {
            super(cls, nvl);
        }

        @Override
        public Byte deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            return _parseByte(p, ctxt);
        }
    }

    @JacksonStdImpl
    public static class ShortDeserializer
        extends PrimitiveOrWrapperDeserializer<Short>
    {
        private static final long serialVersionUID = 1L;

        final static ShortDeserializer primitiveInstance = new ShortDeserializer(Short.TYPE, Short.valueOf((short)0));
        final static ShortDeserializer wrapperInstance = new ShortDeserializer(Short.class, null);
        
        public ShortDeserializer(Class<Short> cls, Short nvl)
        {
            super(cls, nvl);
        }

        @Override
        public Short deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException
        {
            return _parseShort(jp, ctxt);
        }
    }

    @JacksonStdImpl
    public static class CharacterDeserializer
        extends PrimitiveOrWrapperDeserializer<Character>
    {
        private static final long serialVersionUID = 1L;

        final static CharacterDeserializer primitiveInstance = new CharacterDeserializer(Character.TYPE, '\0');
        final static CharacterDeserializer wrapperInstance = new CharacterDeserializer(Character.class, null);
        
        public CharacterDeserializer(Class<Character> cls, Character nvl)
        {
            super(cls, nvl);
        }

        @Override
        public Character deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException
        {
            switch (p.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT: // ok iff ascii value
                int value = p.getIntValue();
                if (value >= 0 && value <= 0xFFFF) {
                    return Character.valueOf((char) value);
                }
                break;
            case JsonTokenId.ID_STRING: // this is the usual type
                // But does it have to be exactly one char?
                String text = p.getText();
                if (text.length() == 1) {
                    return Character.valueOf(text.charAt(0));
                }
                // actually, empty should become null?
                if (text.length() == 0) {
                    return (Character) getEmptyValue(ctxt);
                }               
                break;
            case JsonTokenId.ID_START_ARRAY:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    final Character C = deserialize(p, ctxt);
                    if (p.nextToken() != JsonToken.END_ARRAY) {
                        throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, 
                                "Attempted to unwrap single value array for single '" + _valueClass.getName() + "' value but there was more than a single value in the array"
                                );
                    }
                    return C;
                }
            }
            throw ctxt.mappingException(_valueClass, p.getCurrentToken());
        }
    }

    @JacksonStdImpl
    public final static class IntegerDeserializer
        extends PrimitiveOrWrapperDeserializer<Integer>
    {
        private static final long serialVersionUID = 1L;

        final static IntegerDeserializer primitiveInstance = new IntegerDeserializer(Integer.TYPE, Integer.valueOf(0));
        final static IntegerDeserializer wrapperInstance = new IntegerDeserializer(Integer.class, null);
        
        public IntegerDeserializer(Class<Integer> cls, Integer nvl) {
            super(cls, nvl);
        }

        // since 2.6, slightly faster lookups for this very common type
        @Override
        public boolean isCachable() { return true; }

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                return p.getIntValue();
            }
            return _parseInteger(p, ctxt);
        }

        // Since we can never have type info ("natural type"; String, Boolean, Integer, Double):
        // (is it an error to even call this version?)
        @Override
        public Integer deserializeWithType(JsonParser p, DeserializationContext ctxt,
                TypeDeserializer typeDeserializer) throws IOException
        {
            if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                return p.getIntValue();
            }
            return _parseInteger(p, ctxt);
        }
    }

    @JacksonStdImpl
    public final static class LongDeserializer
        extends PrimitiveOrWrapperDeserializer<Long>
    {
        private static final long serialVersionUID = 1L;

        final static LongDeserializer primitiveInstance = new LongDeserializer(Long.TYPE, Long.valueOf(0L));
        final static LongDeserializer wrapperInstance = new LongDeserializer(Long.class, null);
        
        public LongDeserializer(Class<Long> cls, Long nvl) {
            super(cls, nvl);
        }

        // since 2.6, slightly faster lookups for this very common type
        @Override
        public boolean isCachable() { return true; }
        
        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                return p.getLongValue();
            }
            return _parseLong(p, ctxt);
        }
    }

    @JacksonStdImpl
    public static class FloatDeserializer
        extends PrimitiveOrWrapperDeserializer<Float>
    {
        private static final long serialVersionUID = 1L;

        final static FloatDeserializer primitiveInstance = new FloatDeserializer(Float.TYPE, 0.f);
        final static FloatDeserializer wrapperInstance = new FloatDeserializer(Float.class, null);
        
        public FloatDeserializer(Class<Float> cls, Float nvl) {
            super(cls, nvl);
        }

        @Override
        public Float deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            return _parseFloat(p, ctxt);
        }
    }

    @JacksonStdImpl
    public static class DoubleDeserializer
        extends PrimitiveOrWrapperDeserializer<Double>
    {
        private static final long serialVersionUID = 1L;

        final static DoubleDeserializer primitiveInstance = new DoubleDeserializer(Double.TYPE, 0.d);
        final static DoubleDeserializer wrapperInstance = new DoubleDeserializer(Double.class, null);
        
        public DoubleDeserializer(Class<Double> cls, Double nvl) {
            super(cls, nvl);
        }

        @Override
        public Double deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return _parseDouble(jp, ctxt);
        }

        // Since we can never have type info ("natural type"; String, Boolean, Integer, Double):
        // (is it an error to even call this version?)
        @Override
        public Double deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                TypeDeserializer typeDeserializer) throws IOException
        {
            return _parseDouble(jp, ctxt);
        }
    }

    /**
     * For type <code>Number.class</code>, we can just rely on type
     * mappings that plain {@link JsonParser#getNumberValue} returns.
     *<p>
     * There is one additional complication: some numeric
     * types (specifically, int/Integer and double/Double) are "non-typed";
     * meaning that they will NEVER be output with type information.
     * But other numeric types may need such type information.
     * This is why {@link #deserializeWithType} must be overridden.
     */
    @SuppressWarnings("serial")
    @JacksonStdImpl
    public static class NumberDeserializer
        extends StdScalarDeserializer<Object>
    {
        public final static NumberDeserializer instance = new NumberDeserializer();
        
        public NumberDeserializer() {
            super(Number.class);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            switch (p.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT:
                if (ctxt.hasSomeOfFeatures(F_MASK_INT_COERCIONS)) {
                    return _coerceIntegral(p, ctxt);
                }
                return p.getNumberValue();

            case JsonTokenId.ID_NUMBER_FLOAT:
                if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                    return p.getDecimalValue();
                }
                return Double.valueOf(p.getDoubleValue());

            case JsonTokenId.ID_STRING:
                /* Textual values are more difficult... not parsing itself, but figuring
                 * out 'minimal' type to use 
                 */
                String text = p.getText().trim();
                if (text.length() == 0) {
                    return getEmptyValue(ctxt);
                }
                if (_hasTextualNull(text)) {
                    return getNullValue(ctxt);
                }
                if (_isPosInf(text)) {
                    return Double.POSITIVE_INFINITY;
                }
                if (_isNegInf(text)) {
                    return Double.NEGATIVE_INFINITY;
                }
                if (_isNaN(text)) {
                    return Double.NaN;
                }
                try {
                    if (!_isIntNumber(text)) {
                        if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
                            return new BigDecimal(text);
                        }
                        return new Double(text);
                    }
                    if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
                        return new BigInteger(text);
                    }
                    long value = Long.parseLong(text);
                    if (!ctxt.isEnabled(DeserializationFeature.USE_LONG_FOR_INTS)) {
                        if (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE) {
                            return Integer.valueOf((int) value);
                        }
                    }
                    return Long.valueOf(value);
                } catch (IllegalArgumentException iae) {
                    throw ctxt.weirdStringException(text, _valueClass, "not a valid number");
                }
            case JsonTokenId.ID_START_ARRAY:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    final Object value = deserialize(p, ctxt);
                    if (p.nextToken() != JsonToken.END_ARRAY) {
                        throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY, 
                                "Attempted to unwrap single value array for single '" + _valueClass.getName() + "' value but there was more than a single value in the array"
                                );
                    }
                    return value;
                }
                break;
            }
            // Otherwise, no can do:
            throw ctxt.mappingException(_valueClass, p.getCurrentToken());
        }

        /**
         * As mentioned in class Javadoc, there is additional complexity in
         * handling potentially mixed type information here. Because of this,
         * we must actually check for "raw" integers and doubles first, before
         * calling type deserializer.
         */
        @Override
        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                                          TypeDeserializer typeDeserializer)
            throws IOException
        {
            switch (jp.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT:
            case JsonTokenId.ID_NUMBER_FLOAT:
            case JsonTokenId.ID_STRING:
                // can not point to type information: hence must be non-typed (int/double)
                return deserialize(jp, ctxt);
            }
            return typeDeserializer.deserializeTypedFromScalar(jp, ctxt);
        }
    }

    /*
    /**********************************************************
    /* And then bit more complicated (but non-structured) number
    /* types
    /**********************************************************
     */

    /**
     * This is bit trickier to implement efficiently, while avoiding
     * overflow problems.
     */
    @SuppressWarnings("serial")
    @JacksonStdImpl
    public static class BigIntegerDeserializer
        extends StdScalarDeserializer<BigInteger>
    {
        public final static BigIntegerDeserializer instance = new BigIntegerDeserializer();

        public BigIntegerDeserializer() { super(BigInteger.class); }

        @SuppressWarnings("incomplete-switch")
        @Override
        public BigInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            switch (p.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT:
                switch (p.getNumberType()) {
                case INT:
                case LONG:
                case BIG_INTEGER:
                    return p.getBigIntegerValue();
                }
                break;
            case JsonTokenId.ID_NUMBER_FLOAT:
                if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
                    _failDoubleToIntCoercion(p, ctxt, "java.math.BigInteger");
                }
                return p.getDecimalValue().toBigInteger();
            case JsonTokenId.ID_START_ARRAY:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    final BigInteger value = deserialize(p, ctxt);
                    if (p.nextToken() != JsonToken.END_ARRAY) {
                        throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY,
                            "Attempted to unwrap single value array for single 'BigInteger' value but there was more than a single value in the array"
                        );
                    }
                    return value;
                }
                break;
            case JsonTokenId.ID_STRING: // let's do implicit re-parse
                String text = p.getText().trim();
                if (text.length() == 0) {
                    return null;
                }
                try {
                    return new BigInteger(text);
                } catch (IllegalArgumentException iae) {
                    throw ctxt.weirdStringException(text, _valueClass, "not a valid representation");
                }
            }
            // String is ok too, can easily convert; otherwise, no can do:
            throw ctxt.mappingException(_valueClass, p.getCurrentToken());
        }
    }
    
    @SuppressWarnings("serial")
    @JacksonStdImpl
    public static class BigDecimalDeserializer
        extends StdScalarDeserializer<BigDecimal>
    {
        public final static BigDecimalDeserializer instance = new BigDecimalDeserializer();
 
        public BigDecimalDeserializer() { super(BigDecimal.class); }

        @Override
        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException
        {
            switch (p.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT:
            case JsonTokenId.ID_NUMBER_FLOAT:
                return p.getDecimalValue();
            case JsonTokenId.ID_STRING:
                String text = p.getText().trim();
                if (text.length() == 0) {
                    return null;
                }
                try {
                    return new BigDecimal(text);
                } catch (IllegalArgumentException iae) {
                    throw ctxt.weirdStringException(text, _valueClass, "not a valid representation");
                }
            case JsonTokenId.ID_START_ARRAY:
                if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                    p.nextToken();
                    final BigDecimal value = deserialize(p, ctxt);
                    if (p.nextToken() != JsonToken.END_ARRAY) {
                        throw ctxt.wrongTokenException(p, JsonToken.END_ARRAY,
                            "Attempted to unwrap single value array for single 'BigDecimal' value but there was more than a single value in the array"
                        );
                    }
                    return value;
                }
                break;
            }
            // Otherwise, no can do:
            throw ctxt.mappingException(_valueClass, p.getCurrentToken());
        }
    }
}

```
