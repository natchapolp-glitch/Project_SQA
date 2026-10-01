# Defects4J unit test generation

You are a Java unit testing engineer. Write deterministic regression tests for the
production classes supplied below, using their documented and fixed-reference
behavior. This experiment measures test generation from fixed production source.

Produce Java test sources with assertions. Exercise normal cases, boundaries,
invalid inputs, exception paths and branches supported by the supplied source.
Inspect the supplied build configuration and use the JUnit version and dependencies
already available to this project. Defects4J projects can have different build
systems and JUnit versions; do not assume Maven or JUnit 5. Use Java 11 compatible
syntax unless the build configuration requires an older source level.

Constraints:

- Do not change production code or build files and do not add dependencies.
- Use the reference behavior to derive assertions; do not invent unsupported APIs.
- Avoid network access, external programs, wall-clock timing, random values without
  a fixed seed, machine-specific paths and environment-dependent assertions.
- Use test class names ending in `Test`, correctly matching Java file and package
  names. Place each source at its package-relative path, such as
  `org/example/GeneratedExampleTest.java`.
- Keep tests independent. Restore global state that a test changes.
- Do not ask for a bug patch, buggy revision, existing detecting test, or hidden
  evaluation results. Only the supplied reference source and build information may
  guide the initial generation.

Return each Java file in a separate fenced Java code block, preceded by its
package-relative path. Include complete imports and test class definitions. If the
context is insufficient to write a compiling test, explicitly state the missing
API or dependency rather than producing a fabricated result.

## Experiment context

Project: Lang
Fixed reference revision: 1f
Target classes:
- org.apache.commons.lang3.math.NumberUtils

## Eligible shared API declarations

Target these declarations, which exist on both evaluation revisions. Only declaration signatures were checked; no buggy behavior was supplied. Generate at most 30 independent test methods per response.

```json
[
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "isAllZeros",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "validateArray",
    "parameter_types": "java.lang.Object",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "isDigits",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "isNumber",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "byte,byte,byte",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "[B",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "byte,byte,byte",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "[B",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toByte",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toByte",
    "parameter_types": "java.lang.String,byte",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "double,double,double",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "[D",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "double,double,double",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "[D",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toDouble",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toDouble",
    "parameter_types": "java.lang.String,double",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "float,float,float",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "[F",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "float,float,float",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "[F",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toFloat",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toFloat",
    "parameter_types": "java.lang.String,float",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "int,int,int",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "[I",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "int,int,int",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "[I",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toInt",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toInt",
    "parameter_types": "java.lang.String,int",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createDouble",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createFloat",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createInteger",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createLong",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createNumber",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createBigDecimal",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "createBigInteger",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "long,long,long",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "[J",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "long,long,long",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "[J",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toLong",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toLong",
    "parameter_types": "java.lang.String,long",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "short,short,short",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "max",
    "parameter_types": "[S",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "short,short,short",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "min",
    "parameter_types": "[S",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toShort",
    "parameter_types": "java.lang.String",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.lang3.math.NumberUtils",
    "constructor_types": "",
    "method": "toShort",
    "parameter_types": "java.lang.String,short",
    "dimensions": 6
  }
]
```

## Production source src/main/java/org/apache/commons/lang3/math/NumberUtils.java

```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Provides extra functionality for Java Number classes.</p>
 *
 * @since 2.0
 * @version $Id$
 */
public class NumberUtils {
    
    /** Reusable Long constant for zero. */
    public static final Long LONG_ZERO = Long.valueOf(0L);
    /** Reusable Long constant for one. */
    public static final Long LONG_ONE = Long.valueOf(1L);
    /** Reusable Long constant for minus one. */
    public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
    /** Reusable Integer constant for zero. */
    public static final Integer INTEGER_ZERO = Integer.valueOf(0);
    /** Reusable Integer constant for one. */
    public static final Integer INTEGER_ONE = Integer.valueOf(1);
    /** Reusable Integer constant for minus one. */
    public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
    /** Reusable Short constant for zero. */
    public static final Short SHORT_ZERO = Short.valueOf((short) 0);
    /** Reusable Short constant for one. */
    public static final Short SHORT_ONE = Short.valueOf((short) 1);
    /** Reusable Short constant for minus one. */
    public static final Short SHORT_MINUS_ONE = Short.valueOf((short) -1);
    /** Reusable Byte constant for zero. */
    public static final Byte BYTE_ZERO = Byte.valueOf((byte) 0);
    /** Reusable Byte constant for one. */
    public static final Byte BYTE_ONE = Byte.valueOf((byte) 1);
    /** Reusable Byte constant for minus one. */
    public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte) -1);
    /** Reusable Double constant for zero. */
    public static final Double DOUBLE_ZERO = Double.valueOf(0.0d);
    /** Reusable Double constant for one. */
    public static final Double DOUBLE_ONE = Double.valueOf(1.0d);
    /** Reusable Double constant for minus one. */
    public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0d);
    /** Reusable Float constant for zero. */
    public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
    /** Reusable Float constant for one. */
    public static final Float FLOAT_ONE = Float.valueOf(1.0f);
    /** Reusable Float constant for minus one. */
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);

    /**
     * <p><code>NumberUtils</code> instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>NumberUtils.toInt("6");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public NumberUtils() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   NumberUtils.toInt(null) = 0
     *   NumberUtils.toInt("")   = 0
     *   NumberUtils.toInt("1")  = 1
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if
     *  conversion fails
     * @since 2.1
     */
    public static int toInt(final String str) {
        return toInt(str, 0);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   NumberUtils.toInt(null, 1) = 1
     *   NumberUtils.toInt("", 1)   = 1
     *   NumberUtils.toInt("1", 0)  = 1
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @param defaultValue  the default value
     * @return the int represented by the string, or the default if conversion fails
     * @since 2.1
     */
    public static int toInt(final String str, final int defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   NumberUtils.toLong(null) = 0L
     *   NumberUtils.toLong("")   = 0L
     *   NumberUtils.toLong("1")  = 1L
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @return the long represented by the string, or <code>0</code> if
     *  conversion fails
     * @since 2.1
     */
    public static long toLong(final String str) {
        return toLong(str, 0L);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   NumberUtils.toLong(null, 1L) = 1L
     *   NumberUtils.toLong("", 1L)   = 1L
     *   NumberUtils.toLong("1", 0L)  = 1L
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @param defaultValue  the default value
     * @return the long represented by the string, or the default if conversion fails
     * @since 2.1
     */
    public static long toLong(final String str, final long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>float</code>, returning
     * <code>0.0f</code> if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>,
     * <code>0.0f</code> is returned.</p>
     *
     * <pre>
     *   NumberUtils.toFloat(null)   = 0.0f
     *   NumberUtils.toFloat("")     = 0.0f
     *   NumberUtils.toFloat("1.5")  = 1.5f
     * </pre>
     *
     * @param str the string to convert, may be <code>null</code>
     * @return the float represented by the string, or <code>0.0f</code>
     *  if conversion fails
     * @since 2.1
     */
    public static float toFloat(final String str) {
        return toFloat(str, 0.0f);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>float</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>, the default
     * value is returned.</p>
     *
     * <pre>
     *   NumberUtils.toFloat(null, 1.1f)   = 1.0f
     *   NumberUtils.toFloat("", 1.1f)     = 1.1f
     *   NumberUtils.toFloat("1.5", 0.0f)  = 1.5f
     * </pre>
     *
     * @param str the string to convert, may be <code>null</code>
     * @param defaultValue the default value
     * @return the float represented by the string, or defaultValue
     *  if conversion fails
     * @since 2.1
     */
    public static float toFloat(final String str, final float defaultValue) {
      if (str == null) {
          return defaultValue;
      }     
      try {
          return Float.parseFloat(str);
      } catch (final NumberFormatException nfe) {
          return defaultValue;
      }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>double</code>, returning
     * <code>0.0d</code> if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>,
     * <code>0.0d</code> is returned.</p>
     *
     * <pre>
     *   NumberUtils.toDouble(null)   = 0.0d
     *   NumberUtils.toDouble("")     = 0.0d
     *   NumberUtils.toDouble("1.5")  = 1.5d
     * </pre>
     *
     * @param str the string to convert, may be <code>null</code>
     * @return the double represented by the string, or <code>0.0d</code>
     *  if conversion fails
     * @since 2.1
     */
    public static double toDouble(final String str) {
        return toDouble(str, 0.0d);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>double</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string <code>str</code> is <code>null</code>, the default
     * value is returned.</p>
     *
     * <pre>
     *   NumberUtils.toDouble(null, 1.1d)   = 1.1d
     *   NumberUtils.toDouble("", 1.1d)     = 1.1d
     *   NumberUtils.toDouble("1.5", 0.0d)  = 1.5d
     * </pre>
     *
     * @param str the string to convert, may be <code>null</code>
     * @param defaultValue the default value
     * @return the double represented by the string, or defaultValue
     *  if conversion fails
     * @since 2.1
     */
    public static double toDouble(final String str, final double defaultValue) {
      if (str == null) {
          return defaultValue;
      }
      try {
          return Double.parseDouble(str);
      } catch (final NumberFormatException nfe) {
          return defaultValue;
      }
    }

     //-----------------------------------------------------------------------
     /**
     * <p>Convert a <code>String</code> to a <code>byte</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   NumberUtils.toByte(null) = 0
     *   NumberUtils.toByte("")   = 0
     *   NumberUtils.toByte("1")  = 1
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @return the byte represented by the string, or <code>zero</code> if
     *  conversion fails
     * @since 2.5
     */
    public static byte toByte(final String str) {
        return toByte(str, (byte) 0);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>byte</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   NumberUtils.toByte(null, 1) = 1
     *   NumberUtils.toByte("", 1)   = 1
     *   NumberUtils.toByte("1", 0)  = 1
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @param defaultValue  the default value
     * @return the byte represented by the string, or the default if conversion fails
     * @since 2.5
     */
    public static byte toByte(final String str, final byte defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>short</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     *
     * <pre>
     *   NumberUtils.toShort(null) = 0
     *   NumberUtils.toShort("")   = 0
     *   NumberUtils.toShort("1")  = 1
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @return the short represented by the string, or <code>zero</code> if
     *  conversion fails
     * @since 2.5
     */
    public static short toShort(final String str) {
        return toShort(str, (short) 0);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>short</code>, returning a
     * default value if the conversion fails.</p>
     *
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     *
     * <pre>
     *   NumberUtils.toShort(null, 1) = 1
     *   NumberUtils.toShort("", 1)   = 1
     *   NumberUtils.toShort("1", 0)  = 1
     * </pre>
     *
     * @param str  the string to convert, may be null
     * @param defaultValue  the default value
     * @return the short represented by the string, or the default if conversion fails
     * @since 2.5
     */
    public static short toShort(final String str, final short defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    //-----------------------------------------------------------------------
    // must handle Long, Float, Integer, Float, Short,
    //                  BigDecimal, BigInteger and Byte
    // useful methods:
    // Byte.decode(String)
    // Byte.valueOf(String,int radix)
    // Byte.valueOf(String)
    // Double.valueOf(String)
    // Float.valueOf(String)
    // Float.valueOf(String)
    // Integer.valueOf(String,int radix)
    // Integer.valueOf(String)
    // Integer.decode(String)
    // Integer.getInteger(String)
    // Integer.getInteger(String,int val)
    // Integer.getInteger(String,Integer val)
    // Integer.valueOf(String)
    // Double.valueOf(String)
    // new Byte(String)
    // Long.valueOf(String)
    // Long.getLong(String)
    // Long.getLong(String,int)
    // Long.getLong(String,Integer)
    // Long.valueOf(String,int)
    // Long.valueOf(String)
    // Short.valueOf(String)
    // Short.decode(String)
    // Short.valueOf(String,int)
    // Short.valueOf(String)
    // new BigDecimal(String)
    // new BigInteger(String)
    // new BigInteger(String,int radix)
    // Possible inputs:
    // 45 45.5 45E7 4.5E7 Hex Oct Binary xxxF xxxD xxxf xxxd
    // plus minus everything. Prolly more. A lot are not separable.

    /**
     * <p>Turns a string value into a java.lang.Number.</p>
     *
     * <p>If the string starts with {@code 0x} or {@code -0x} (lower or upper case) or {@code #} or {@code -#}, it
     * will be interpreted as a hexadecimal Integer - or Long, if the number of digits after the
     * prefix is more than 8 - or BigInteger if there are more than 16 digits.
     * </p>
     * <p>Then, the value is examined for a type qualifier on the end, i.e. one of
     * <code>'f','F','d','D','l','L'</code>.  If it is found, it starts 
     * trying to create successively larger types from the type specified
     * until one is found that can represent the value.</p>
     *
     * <p>If a type specifier is not found, it will check for a decimal point
     * and then try successively larger types from <code>Integer</code> to
     * <code>BigInteger</code> and from <code>Float</code> to
    * <code>BigDecimal</code>.</p>
    * 
     * <p>
     * Integral values with a leading {@code 0} will be interpreted as octal; the returned number will
     * be Integer, Long or BigDecimal as appropriate.
     * </p>
     *
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * <p>This method does not trim the input string, i.e., strings with leading
     * or trailing spaces will generate NumberFormatExceptions.</p>
     *
     * @param str  String containing a number, may be null
     * @return Number created from the string (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Number createNumber(final String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        // Need to deal with all possible hex prefixes here
        final String[] hex_prefixes = {"0x", "0X", "-0x", "-0X", "#", "-#"};
        int pfxLen = 0;
        for(final String pfx : hex_prefixes) {
            if (str.startsWith(pfx)) {
                pfxLen += pfx.length();
                break;
            }
        }
        if (pfxLen > 0) { // we have a hex number
            char firstSigDigit = 0; // strip leading zeroes
            for(int i = pfxLen; i < str.length(); i++) {
                firstSigDigit = str.charAt(i);
                if (firstSigDigit == '0') { // count leading zeroes
                    pfxLen++;
                } else {
                    break;
                }
            }
            final int hexDigits = str.length() - pfxLen;
            if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) { // too many for Long
                return createBigInteger(str);
            }
            if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) { // too many for an int
                return createLong(str);
            }
            return createInteger(str);
        }
        final char lastChar = str.charAt(str.length() - 1);
        String mant;
        String dec;
        String exp;
        final int decPos = str.indexOf('.');
        final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present
        // if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)
        // and the parsing which will detect if e or E appear in a number due to using the wrong offset

        int numDecimals = 0; // Check required precision (LANG-693)
        if (decPos > -1) { // there is a decimal point

            if (expPos > -1) { // there is an exponent
                if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing IOOBE
                    throw new NumberFormatException(str + " is not a valid number.");
                }
                dec = str.substring(decPos + 1, expPos);
            } else {
                dec = str.substring(decPos + 1);
            }
            mant = str.substring(0, decPos);
            numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss of precision for floating point numbers.
        } else {
            if (expPos > -1) {
                if (expPos > str.length()) { // prevents double exponent causing IOOBE
                    throw new NumberFormatException(str + " is not a valid number.");
                }
                mant = str.substring(0, expPos);
            } else {
                mant = str;
            }
            dec = null;
        }
        if (!Character.isDigit(lastChar) && lastChar != '.') {
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring(expPos + 1, str.length() - 1);
            } else {
                exp = null;
            }
            //Requesting a specific type..
            final String numeric = str.substring(0, str.length() - 1);
            final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            switch (lastChar) {
                case 'l' :
                case 'L' :
                    if (dec == null
                        && exp == null
                        && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                        try {
                            return createLong(numeric);
                        } catch (final NumberFormatException nfe) { // NOPMD
                            // Too big for a long
                        }
                        return createBigInteger(numeric);

                    }
                    throw new NumberFormatException(str + " is not a valid number.");
                case 'f' :
                case 'F' :
                    try {
                        final Float f = NumberUtils.createFloat(numeric);
                        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                            //If it's too big for a float or the float value = 0 and the string
                            //has non-zeros in it, then float does not have the precision we want
                            return f;
                        }

                    } catch (final NumberFormatException nfe) { // NOPMD
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                case 'd' :
                case 'D' :
                    try {
                        final Double d = NumberUtils.createDouble(numeric);
                        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                            return d;
                        }
                    } catch (final NumberFormatException nfe) { // NOPMD
                        // ignore the bad number
                    }
                    try {
                        return createBigDecimal(numeric);
                    } catch (final NumberFormatException e) { // NOPMD
                        // ignore the bad number
                    }
                    //$FALL-THROUGH$
                default :
                    throw new NumberFormatException(str + " is not a valid number.");

            }
        }
        //User doesn't have a preference on the return type, so let's start
        //small and go from there...
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length());
        } else {
            exp = null;
        }
        if (dec == null && exp == null) { // no decimal point and no exponent
            //Must be an Integer, Long, Biginteger
            try {
                return createInteger(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            try {
                return createLong(str);
            } catch (final NumberFormatException nfe) { // NOPMD
                // ignore the bad number
            }
            return createBigInteger(str);
        }

        //Must be a Float, Double, BigDecimal
        final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        try {
            if(numDecimals <= 7){// If number has 7 or fewer digits past the decimal point then make it a float
                final Float f = createFloat(str);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }
        try {
            if(numDecimals <= 16){// If number has between 8 and 16 digits past the decimal point then make it a double
                final Double d = createDouble(str);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            }
        } catch (final NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

        return createBigDecimal(str);
    }

    /**
     * <p>Utility method for {@link #createNumber(java.lang.String)}.</p>
     *
     * <p>Returns <code>true</code> if s is <code>null</code>.</p>
     * 
     * @param str  the String to check
     * @return if it is all zeros or <code>null</code>
     */
    private static boolean isAllZeros(final String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        return str.length() > 0;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Convert a <code>String</code> to a <code>Float</code>.</p>
     *
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     * 
     * @param str  a <code>String</code> to convert, may be null
     * @return converted <code>Float</code> (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Float createFloat(final String str) {
        if (str == null) {
            return null;
        }
        return Float.valueOf(str);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>.</p>
     * 
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str  a <code>String</code> to convert, may be null
     * @return converted <code>Double</code> (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Double createDouble(final String str) {
        if (str == null) {
            return null;
        }
        return Double.valueOf(str);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Integer</code>, handling
     * hex and octal notations.</p>
     *
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     * 
     * @param str  a <code>String</code> to convert, may be null
     * @return converted <code>Integer</code> (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Integer createInteger(final String str) {
        if (str == null) {
            return null;
        }
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        return Integer.decode(str);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Long</code>; 
     * since 3.1 it handles hex and octal notations.</p>
     * 
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str  a <code>String</code> to convert, may be null
     * @return converted <code>Long</code> (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Long createLong(final String str) {
        if (str == null) {
            return null;
        }
        return Long.decode(str);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>BigInteger</code>;
     * since 3.2 it handles hex (0x or #) and octal (0) notations.</p>
     *
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     * 
     * @param str  a <code>String</code> to convert, may be null
     * @return converted <code>BigInteger</code> (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigInteger createBigInteger(final String str) {
        if (str == null) {
            return null;
        }
        int pos = 0; // offset within string
        int radix = 10;
        boolean negate = false; // need to negate later?
        if (str.startsWith("-")) {
            negate = true;
            pos = 1;
        }
        if (str.startsWith("0x", pos) || str.startsWith("0x", pos)) { // hex
            radix = 16;
            pos += 2;
        } else if (str.startsWith("#", pos)) { // alternative hex (allowed by Long/Integer)
            radix = 16;
            pos ++;
        } else if (str.startsWith("0", pos) && str.length() > pos + 1) { // octal; so long as there are additional digits
            radix = 8;
            pos ++;
        } // default is to treat as decimal

        final BigInteger value = new BigInteger(str.substring(pos), radix);
        return negate ? value.negate() : value;
    }

    /**
     * <p>Convert a <code>String</code> to a <code>BigDecimal</code>.</p>
     * 
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str  a <code>String</code> to convert, may be null
     * @return converted <code>BigDecimal</code> (or null if the input is null)
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigDecimal createBigDecimal(final String str) {
        if (str == null) {
            return null;
        }
        // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
        if (StringUtils.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        if (str.trim().startsWith("--")) {
            // this is protection for poorness in java.lang.BigDecimal.
            // it accepts this as a legal value, but it does not appear 
            // to be in specification of class. OS X Java parses it to 
            // a wrong value.
            throw new NumberFormatException(str + " is not a valid number.");
        }
        return new BigDecimal(str);
    }

    // Min in array
    //--------------------------------------------------------------------
    /**
     * <p>Returns the minimum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static long min(final long[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns min
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }

    /**
     * <p>Returns the minimum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static int min(final int[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns min
        int min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }
    
        return min;
    }

    /**
     * <p>Returns the minimum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static short min(final short[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns min
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }

    /**
     * <p>Returns the minimum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static byte min(final byte[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns min
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }

     /**
     * <p>Returns the minimum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     * @see IEEE754rUtils#min(double[]) IEEE754rUtils for a version of this method that handles NaN differently
     */
    public static double min(final double[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns min
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.isNaN(array[i])) {
                return Double.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }

    /**
     * <p>Returns the minimum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     * @see IEEE754rUtils#min(float[]) IEEE754rUtils for a version of this method that handles NaN differently
     */
    public static float min(final float[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns min
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Float.isNaN(array[i])) {
                return Float.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
    
        return min;
    }

    // Max in array
    //--------------------------------------------------------------------
    /**
     * <p>Returns the maximum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static long max(final long[] array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        long max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static int max(final int[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns max
        int max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
    
        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static short max(final short[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns max
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
    
        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     */
    public static byte max(final byte[] array) {
        // Validates input
        validateArray(array);
    
        // Finds and returns max
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
    
        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     * @see IEEE754rUtils#max(double[]) IEEE754rUtils for a version of this method that handles NaN differently
     */
    public static double max(final double[] array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
    
        return max;
    }

    /**
     * <p>Returns the maximum value in an array.</p>
     * 
     * @param array  an array, must not be null or empty
     * @return the minimum value in the array
     * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>array</code> is empty
     * @see IEEE754rUtils#max(float[]) IEEE754rUtils for a version of this method that handles NaN differently
     */
    public static float max(final float[] array) {
        // Validates input
        validateArray(array);

        // Finds and returns max
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }

        return max;
    }

    /**
     * Checks if the specified array is neither null nor empty.
     *
     * @param array  the array to check
     * @throws IllegalArgumentException if {@code array} is either {@code null} or empty
     */
    private static void validateArray(final Object array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (Array.getLength(array) == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        }
    }
     
    // 3 param min
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the minimum of three <code>long</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the smallest of the values
     */
    public static long min(long a, final long b, final long c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three <code>int</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the smallest of the values
     */
    public static int min(int a, final int b, final int c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three <code>short</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the smallest of the values
     */
    public static short min(short a, final short b, final short c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three <code>byte</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the smallest of the values
     */
    public static byte min(byte a, final byte b, final byte c) {
        if (b < a) {
            a = b;
        }
        if (c < a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the minimum of three <code>double</code> values.</p>
     * 
     * <p>If any value is <code>NaN</code>, <code>NaN</code> is
     * returned. Infinity is handled.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the smallest of the values
     * @see IEEE754rUtils#min(double, double, double) for a version of this method that handles NaN differently
     */
    public static double min(final double a, final double b, final double c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * <p>Gets the minimum of three <code>float</code> values.</p>
     * 
     * <p>If any value is <code>NaN</code>, <code>NaN</code> is
     * returned. Infinity is handled.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the smallest of the values
     * @see IEEE754rUtils#min(float, float, float) for a version of this method that handles NaN differently
     */
    public static float min(final float a, final float b, final float c) {
        return Math.min(Math.min(a, b), c);
    }

    // 3 param max
    //-----------------------------------------------------------------------
    /**
     * <p>Gets the maximum of three <code>long</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the largest of the values
     */
    public static long max(long a, final long b, final long c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three <code>int</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the largest of the values
     */
    public static int max(int a, final int b, final int c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three <code>short</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the largest of the values
     */
    public static short max(short a, final short b, final short c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three <code>byte</code> values.</p>
     * 
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the largest of the values
     */
    public static byte max(byte a, final byte b, final byte c) {
        if (b > a) {
            a = b;
        }
        if (c > a) {
            a = c;
        }
        return a;
    }

    /**
     * <p>Gets the maximum of three <code>double</code> values.</p>
     * 
     * <p>If any value is <code>NaN</code>, <code>NaN</code> is
     * returned. Infinity is handled.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the largest of the values
     * @see IEEE754rUtils#max(double, double, double) for a version of this method that handles NaN differently
     */
    public static double max(final double a, final double b, final double c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * <p>Gets the maximum of three <code>float</code> values.</p>
     * 
     * <p>If any value is <code>NaN</code>, <code>NaN</code> is
     * returned. Infinity is handled.</p>
     *
     * @param a  value 1
     * @param b  value 2
     * @param c  value 3
     * @return  the largest of the values
     * @see IEEE754rUtils#max(float, float, float) for a version of this method that handles NaN differently
     */
    public static float max(final float a, final float b, final float c) {
        return Math.max(Math.max(a, b), c);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Checks whether the <code>String</code> contains only
     * digit characters.</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if str contains only Unicode numeric
     */
    public static boolean isDigits(final String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks whether the String a valid Java number.</p>
     *
     * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
     * qualifier, scientific notation and numbers marked with a type
     * qualifier (e.g. 123L).</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     */
    public static boolean isNumber(final String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        final char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        final int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false; // str == "0x"
            }
            // checking hex (it can't be anything else)
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9')
                    && (chars[i] < 'a' || chars[i] > 'f')
                    && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
        sz--; // don't want to loop to the last char, check it afterwords
              // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent   
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                || chars[i] == 'L') {
                // not allowing L with an exponent or decimal point
                return foundDigit && !hasExp && !hasDecPoint;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }

}

```

## Build configuration build.xml

```text
<!--
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
 -->
<!--
   "Lang" component of the Apache Commons Subproject
   $Id$
-->
<project name="Lang" default="compile" basedir=".">

    <!-- ========== Initialize Properties ===================================== -->
    <property file="${user.home}/${component.name}.build.properties"/>
    <property file="${user.home}/build.properties"/>
    <property file="${basedir}/build.properties"/>
    <property file="${basedir}/default.properties"/>
    <property name="jdk.javadoc" value="http://download.oracle.com/javase/1.5.0/docs/api/"/>
    <property name="collections.javadoc" value="http://commons.apache.org/collections/api-release/"/>

    <!-- ========== Construct compile classpath =============================== -->
    <path id="compile.classpath">
        <pathelement location="${build.home}/classes"/>
    </path>

    <!-- ========== Construct unit test classpath ============================= -->
    <path id="test.classpath">
        <pathelement location="${build.home}/classes"/>
        <pathelement location="${build.home}/tests"/>
        <pathelement location="${junit.jar}"/>
        <pathelement location="${easymock.jar}"/>
        <pathelement location="${commons-io.jar}"/>
    </path>

    <!-- ========== Executable Targets ======================================== -->
    <target name="init" description="Initialize and evaluate conditionals">
        <echo message="-------- ${component.name} ${component.version} --------"/>
        <filter token="name" value="${component.name}"/>
        <filter token="package" value="${component.package}"/>
        <filter token="version" value="${component.version}"/>
        <filter token="compile.source" value="${compile.source}"/>
        <filter token="compile.target" value="${compile.target}"/>
        <mkdir dir="${build.home}"/>
    </target>

    <!-- ========== Compile Targets ========================================= -->
    <target name="compile" depends="init" description="Compile shareable components">
        <mkdir dir="${build.home}/classes"/>
        <javac srcdir="${source.home}" destdir="${build.home}/classes" debug="${compile.debug}" deprecation="${compile.deprecation}" target="${compile.target}" source="${compile.source}" excludes="${compile.excludes}" optimize="${compile.optimize}" includeantruntime="false" encoding="${compile.encoding}">
            <classpath refid="compile.classpath"/>
        </javac>
        <copy todir="${build.home}/classes" filtering="on">
            <fileset dir="${source.home}" excludes="**/*.java,**/*.html"/>
        </copy>
    </target>

    <target name="compile.tests" depends="compile" description="Compile unit test cases">
        <mkdir dir="${build.home}/tests"/>
        <javac srcdir="${test.home}" destdir="${build.home}/tests" debug="${compile.debug}" deprecation="off" target="${compile.target}" source="${compile.source}" optimize="${compile.optimize}" includeantruntime="false" encoding="${compile.encoding}">
            <classpath refid="test.classpath"/>
        </javac>
        <copy todir="${build.home}/tests" filtering="on">
            <fileset dir="${test.home}" excludes="**/*.java"/>
        </copy>
    </target>

    <!-- ========== Unit Tests ========================================= -->
    <target name="test" depends="compile.tests" description="Run all unit test cases">
        <echo message="Running unit tests ..."/>
        <mkdir dir="${build.home}/test-reports"/>
        <junit printsummary="true" showoutput="true" fork="yes" haltonfailure="${test.failonerror}">
            <classpath refid="test.classpath"/>
            <formatter type="plain" usefile="true" />
            <!-- If test.entry is defined, run a single test, otherwise run all valid tests -->
            <test name="${test.entry}" todir="${build.home}/test-reports" if="test.entry"/>
            <batchtest fork="yes" todir="${build.home}/test-reports" unless="test.entry">
                <fileset dir="${test.home}">
                    <include name="**/*Test.java"/>
                    <exclude name="**/Abstract*Test.java"/>
                </fileset>
            </batchtest>
        </junit>
    </target>

    <target name="clean" description="Clean build and distribution directories">
        <delete dir="${build.home}"/>
    </target>

    <target name="all" depends="clean,test,compile" description="Clean and compile all components"/>

    <!-- ========== JavaDocs ========================================= -->
    <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
        <mkdir dir="${build.home}"/>
        <mkdir dir="${build.home}/apidocs"/>
        <tstamp>
            <format property="current.year" pattern="yyyy"/>
        </tstamp>
        <javadoc sourcepath="${source.home}" 
                 destdir="${build.home}/apidocs" 
                 overview="${source.home}/org/apache/commons/lang3/overview.html" 
                 packagenames="org.apache.commons.*" 
                 excludepackagenames="${javadoc.excludepackagenames}" 
                 author="false" 
                 version="true" 
                 doctitle="&lt;h1&gt;Commons Lang ${component.version}&lt;/h1&gt;"
                 windowtitle="Lang ${component.version}" 
                 bottom="Copyright &amp;copy; 2001-${current.year} - Apache Software Foundation" 
                 use="true" 
                 encoding="${compile.encoding}"
                 source="${compile.source}">
            <classpath refid="compile.classpath"/>
            <link href="${jdk.javadoc}"/>
            <link href="${collections.javadoc}"/>
        </javadoc>
    </target>

    <!-- ========== Jar Targets ========================================= -->
    <target name="jar" depends="compile" description="Create jar">
        <mkdir dir="${build.home}/classes/META-INF"/>
        <copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
        <copy file="NOTICE.txt"  tofile="${build.home}/classes/META-INF/NOTICE.txt"/>
        <jar jarfile="${build.home}/${final.name}.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Lang"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Lang"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
                <attribute name="X-Compile-Source-JDK" value="${compile.source}"/>
                <attribute name="X-Compile-Target-JDK" value="${compile.target}"/>
            </manifest>
            <fileset dir="${build.home}/classes">
                <include name="**/*.class"/>
                <include name="**/LICENSE.txt"/>
                <include name="**/NOTICE.txt"/>
            </fileset>
        </jar>
    </target>

    <target name="javadoc-jar" depends="javadoc" description="Create JavaDoc jar">
        <jar jarfile="${build.home}/${final.name}-javadoc.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Lang API"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Lang API"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
            </manifest>
            <fileset dir="${build.home}/apidocs"/>
            <fileset dir="${basedir}">
                <include name="LICENSE.txt"/>
                <include name="NOTICE.txt"/>
            </fileset>
        </jar>
    </target>

    <target name="source-jar" depends="init" description="Create JavaDoc jar">
        <jar jarfile="${build.home}/${final.name}-sources.jar">
            <manifest>
                <attribute name="Specification-Title" value="Commons Lang Source"/>
                <attribute name="Specification-Version" value="${component.version}"/>
                <attribute name="Specification-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Title" value="Commons Lang Source"/>
                <attribute name="Implementation-Version" value="${component.version}"/> 
                <attribute name="Implementation-Vendor" value="The Apache Software Foundation"/>
                <attribute name="Implementation-Vendor-Id" value="org.apache"/>
            </manifest>
            <fileset dir="${source.home}">
                <include name="**/*.java"/>
            </fileset>
            <fileset dir="${basedir}">
                <include name="LICENSE.txt"/>
                <include name="NOTICE.txt"/>
            </fileset>
        </jar>
    </target>

    <!-- ========== Distribution ========================================= -->
    <target name="dist" depends="clean,jar,source-jar,javadoc-jar" description="Create binary distribution">

        <!-- binary distro -->
        <zip destfile="${build.home}/${final.name}.zip">
            <zipfileset dir="${basedir}" prefix="${final.name}"
                      includes="LICENSE.txt,
                                NOTICE.txt,
                                RELEASE-NOTES.txt"
             />
            <zipfileset dir="${build.home}" includes="*.jar," prefix="${final.name}"/>
            <zipfileset dir="${build.home}/apidocs" prefix="${final.name}/apidocs"/>
        </zip>
        <tar destfile="${build.home}/${final.name}.tar.gz" compression="gzip">
            <zipfileset src="${build.home}/${final.name}.zip"/>
        </tar>

        <!-- source distro -->
        <zip destfile="${build.home}/${final.name}-src.zip">
            <zipfileset dir="${basedir}" prefix="${final.name}-src"
                      includes="build.xml,
                                build.xml,
                                checkstyle.xml,
                                default.properties,
                                LICENSE.txt,
                                NOTICE.txt,
                                pom.xml,
                                RELEASE-NOTES.txt"
             />
            <zipfileset dir="${basedir}/src"   prefix="${final.name}-src/src"/>
        </zip>
        <tar destfile="${build.home}/${final.name}-src.tar.gz" compression="gzip">
            <zipfileset src="${build.home}/${final.name}-src.zip"/>
        </tar>

    </target>
</project>

```

## Build configuration pom.xml

```text
<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with 
  this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the 
  "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations 
  under the License. -->
<project
  xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>28</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.2-SNAPSHOT</version>
  <name>Commons Lang</name>

  <inceptionYear>2001</inceptionYear>
  <description>
  Commons Lang, a package of Java utility classes for the
  classes that are in java.lang's hierarchy, or are considered to be so
  standard as to justify existence in java.lang.
</description>

  <url>http://commons.apache.org/proper/commons-lang/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/LANG</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/lang/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/lang/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/lang/trunk</url>
  </scm>

  <developers>
    <developer>
      <name>Daniel Rall</name>
      <id>dlr</id>
      <email>dlr@finemaltcoding.com</email>
      <organization>CollabNet, Inc.</organization>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Stephen Colebourne</name>
      <id>scolebourne</id>
      <email>scolebourne@joda.org</email>
      <organization>SITA ATS Ltd</organization>
      <timezone>0</timezone>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Henri Yandell</name>
      <id>bayard</id>
      <email>bayard@apache.org</email>
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Steven Caswell</name>
      <id>scaswell</id>
      <email>stevencaswell@apache.org</email>
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
      <timezone>-5</timezone>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
      <email>rdonkin@apache.org</email>
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Gary D. Gregory</name>
      <id>ggregory</id>
      <email>ggregory@apache.org</email>
      <timezone>-5</timezone>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Fredrik Westermarck</name>
      <id>fredrik</id>
      <email />
      <organization />
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>James Carman</name>
      <id>jcarman</id>
      <email>jcarman@apache.org</email>
      <organization>Carman Consulting, Inc.</organization>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Niall Pemberton</name>
      <id>niallp</id>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Matt Benson</name>
      <id>mbenson</id>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Joerg Schaible</name>
      <id>joehni</id>
      <email>joerg.schaible@gmx.de</email>
      <roles>
        <role>Java Developer</role>
      </roles>
      <timezone>+1</timezone>
    </developer>
    <developer>
      <name>Oliver Heger</name>
      <id>oheger</id>
      <email>oheger@apache.org</email>
      <timezone>+1</timezone>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
    <developer>
      <name>Paul Benedict</name>
      <id>pbenedict</id>
      <email>pbenedict@apache.org</email>
      <roles>
        <role>Java Developer</role>
      </roles>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>C. Scott Ananian</name>
    </contributor>
    <contributor>
      <name>Chris Audley</name>
    </contributor>
    <contributor>
      <name>Stephane Bailliez</name>
    </contributor>
    <contributor>
      <name>Michael Becke</name>
    </contributor>
    <contributor>
      <name>Benjamin Bentmann</name>
    </contributor>
    <contributor>
      <name>Ola Berg</name>
    </contributor>
    <contributor>
      <name>Nathan Beyer</name>
    </contributor>
    <contributor>
      <name>Stefan Bodewig</name>
    </contributor>
    <contributor>
      <name>Janek Bogucki</name>
    </contributor>
    <contributor>
      <name>Mike Bowler</name>
    </contributor>
    <contributor>
      <name>Sean Brown</name>
    </contributor>
    <contributor>
      <name>Alexander Day Chaffee</name>
    </contributor>
    <contributor>
      <name>Al Chou</name>
    </contributor>
    <contributor>
      <name>Greg Coladonato</name>
    </contributor>
    <contributor>
      <name>Maarten Coene</name>
    </contributor>
    <contributor>
      <name>Justin Couch</name>
    </contributor>
    <contributor>
      <name>Michael Davey</name>
    </contributor>
    <contributor>
      <name>Norm Deane</name>
    </contributor>
    <contributor>
      <name>Morgan Delagrange</name>
    </contributor>
    <contributor>
      <name>Ringo De Smet</name>
    </contributor>
    <contributor>
      <name>Russel Dittmar</name>
    </contributor>
    <contributor>
      <name>Steve Downey</name>
    </contributor>
    <contributor>
      <name>Matthias Eichel</name>
    </contributor>
    <contributor>
      <name>Christopher Elkins</name>
    </contributor>
    <contributor>
      <name>Chris Feldhacker</name>
    </contributor>
    <contributor>
      <name>Roland Foerther</name>
    </contributor>
    <contributor>
      <name>Pete Gieser</name>
    </contributor>
    <contributor>
      <name>Jason Gritman</name>
    </contributor>
    <contributor>
      <name>Matthew Hawthorne</name>
    </contributor>
    <contributor>
      <name>Michael Heuer</name>
    </contributor>
    <contributor>
      <name>Chas Honton</name>
    </contributor>
    <contributor>
      <name>Chris Hyzer</name>
    </contributor>
    <contributor>
      <name>Paul Jack</name>
    </contributor>
    <contributor>
      <name>Marc Johnson</name>
    </contributor>
    <contributor>
      <name>Duncan Jones</name>
    </contributor>
    <contributor>
      <name>Shaun Kalley</name>
    </contributor>
    <contributor>
      <name>Tetsuya Kaneuchi</name>
    </contributor>
    <contributor>
      <name>Nissim Karpenstein</name>
    </contributor>
    <contributor>
      <name>Ed Korthof</name>
    </contributor>
    <contributor>
      <name>Holger Krauth</name>
    </contributor>
    <contributor>
      <name>Rafal Krupinski</name>
    </contributor>
    <contributor>
      <name>Rafal Krzewski</name>
    </contributor>
    <contributor>
      <name>David Leppik</name>
    </contributor>
    <contributor>
      <name>Eli Lindsey</name>
    </contributor>
    <contributor>
      <name>Sven Ludwig</name>
    </contributor>
    <contributor>
      <name>Craig R. McClanahan</name>
    </contributor>
    <contributor>
      <name>Rand McNeely</name>
    </contributor>
    <contributor>
      <name>Hendrik Maryns</name>
    </contributor>
    <contributor>
      <name>Dave Meikle</name>
    </contributor>
    <contributor>
      <name>Nikolay Metchev</name>
    </contributor>
    <contributor>
      <name>Kasper Nielsen</name>
    </contributor>
    <contributor>
      <name>Tim O'Brien</name>
    </contributor>
    <contributor>
      <name>Brian S O'Neill</name>
    </contributor>
    <contributor>
      <name>Andrew C. Oliver</name>
    </contributor>
    <contributor>
      <name>Alban Peignier</name>
    </contributor>
    <contributor>
      <name>Moritz Petersen</name>
    </contributor>
    <contributor>
      <name>Dmitri Plotnikov</name>
    </contributor>
    <contributor>
      <name>Neeme Praks</name>
    </contributor>
    <contributor>
      <name>Eric Pugh</name>
    </contributor>
    <contributor>
      <name>Stephen Putman</name>
    </contributor>
    <contributor>
      <name>Travis Reeder</name>
    </contributor>
    <contributor>
      <name>Antony Riley</name>
    </contributor>
    <contributor>
      <name>Valentin Rocher</name>
    </contributor>
    <contributor>
      <name>Scott Sanders</name>
    </contributor>
    <contributor>
      <name>Ralph Schaer</name>
    </contributor>
    <contributor>
      <name>Henning P. Schmiedehausen</name>
    </contributor>
    <contributor>
      <name>Sean Schofield</name>
    </contributor>
    <contributor>
      <name>Robert Scholte</name>
    </contributor>
    <contributor>
      <name>Reuben Sivan</name>
    </contributor>
    <contributor>
      <name>Ville Skytta</name>
    </contributor>
    <contributor>
      <name>David M. Sledge</name>
    </contributor>
    <contributor>
      <name>Michael A. Smith</name>
    </contributor>
    <contributor>
      <name>Jan Sorensen</name>
    </contributor>
    <contributor>
      <name>Glen Stampoultzis</name>
    </contributor>
    <contributor>
      <name>Scott Stanchfield</name>
    </contributor>
    <contributor>
      <name>Jon S. Stevens</name>
    </contributor>
    <contributor>
      <name>Sean C. Sullivan</name>
    </contributor>
    <contributor>
      <name>Ashwin Suresh</name>
    </contributor>
    <contributor>
      <name>Helge Tesgaard</name>
    </contributor>
    <contributor>
      <name>Arun Mammen Thomas</name>
    </contributor>
    <contributor>
      <name>Masato Tezuka</name>
    </contributor>
    <contributor>
      <name>Daniel Trebbien</name>
    </contributor>
    <contributor>
      <name>Jeff Varszegi</name>
    </contributor>
    <contributor>
      <name>Chris Webb</name>
    </contributor>
    <contributor>
      <name>Mario Winterer</name>
    </contributor>
    <contributor>
      <name>Stepan Koltsov</name>
    </contributor>
    <contributor>
      <name>Holger Hoffstatte</name>
    </contributor>
    <contributor>
      <name>Derek C. Ashmore</name>
    </contributor>
    <contributor>
      <name>Sebastien Riou</name>
    </contributor>
  </contributors>

  <!-- Lang should depend on very little -->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.4</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>org.easymock</groupId>
      <artifactId>easymock</artifactId>
      <version>3.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <distributionManagement>
    <site>
      <id>apache.website</id>
      <name>Apache Commons Site</name>
      <url>scm:svn:https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-lang/</url>
    </site>
  </distributionManagement>

  <properties>
    <project.build.sourceEncoding>ISO-8859-1</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <maven.compile.source>1.6</maven.compile.source>
    <maven.compile.target>1.6</maven.compile.target>
    <!--
       This is also  used to generate download_xxx file name.
       To override this when generating the download page:
       
       mvn commons:download-page -Dcommons.componentid=lang
       
       The above seems to change the download page name but not any other
       properties that depend on the componentid.
    -->
    <commons.componentid>lang3</commons.componentid>
    <!-- Current 3.x release series -->
    <commons.release.version>3.1</commons.release.version>
    <commons.release.desc>(Java 5.0+)</commons.release.desc>
    <!-- Previous 2.x release series -->
    <commons.release.2.version>2.6</commons.release.2.version>
    <commons.release.2.desc>(Requires Java 1.2 or later)</commons.release.2.desc>
    <!-- Override generated name -->
    <commons.release.2.name>commons-lang-${commons.release.2.version}</commons.release.2.name>
    <commons.jira.id>LANG</commons.jira.id>
    <commons.jira.pid>12310481</commons.jira.pid>

    <commons.site.path>lang</commons.site.path>
    <commons.scmPubUrl>https://svn.apache.org/repos/infra/websites/production/commons/content/proper/commons-lang</commons.scmPubUrl>
    <commons.scmPubCheckoutDirectory>site-content</commons.scmPubCheckoutDirectory>
  </properties>


  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <executions>
          <execution>
            <id>plain</id>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
              </includes>
              <runOrder>random</runOrder>
            </configuration>
          </execution>
          <!-- <execution> <id>security-manager-test</id> <phase>integration-test</phase> <goals> <goal>test</goal> </goals> <configuration> 
            <includes> <include>**/*Test.java</include> </includes> <argLine>-Djava.security.manager -Djava.security.policy=${basedir}/src/test/resources/java.policy</argLine> 
            </configuration> </execution> -->
        </executions>
      </plugin>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <configuration>
          <descriptors>
            <descriptor>src/assembly/bin.xml</descriptor>
            <descriptor>src/assembly/src.xml</descriptor>
          </descriptors>
          <tarLongFileMode>gnu</tarLongFileMode>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>test-jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-scm-publish-plugin</artifactId>
        <configuration>
          <ignorePathsToDelete>
            <ignorePathToDelete>javadocs</ignorePathToDelete>
          </ignorePathsToDelete>
        </configuration>
      </plugin>
    </plugins>

  </build>

  <reporting>
    <plugins>
      <plugin>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>2.9.1</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
        </configuration>
      </plugin>
      <!-- Requires setting 'export MAVEN_OPTS="-Xmx512m" ' -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>2.5.2</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
        <version>2.5.1</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <minSeverity>info</minSeverity>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.0.1</version>
        <configuration>
          <targetJdk>${maven.compile.target}</targetJdk>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>pmd</report>
              <report>cpd</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>taglist-maven-plugin</artifactId>
        <version>2.4</version>
        <configuration>
          <tags>
            <tag>TODO</tag>
            <tag>NOPMD</tag>
            <tag>NOTE</tag>
          </tags>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>javancss-maven-plugin</artifactId>
        <version>2.0</version>
      </plugin>
      <plugin>
        <groupId>org.apache.rat</groupId>
        <artifactId>apache-rat-plugin</artifactId>
        <configuration>
          <excludes>
            <exclude>site-content/**</exclude>
            <exclude>src/site/resources/download_lang.cgi</exclude>
            <exclude>src/site/resources/release-notes/RELEASE-NOTES-*.txt</exclude>
            <exclude>src/test/resources/lang-708-input.txt</exclude>
          </excludes>
        </configuration>
      </plugin>
    </plugins>
  </reporting>

  <profiles>
    <profile>
      <id>setup-checkout</id>
      <activation>
        <file>
          <missing>site-content</missing>
        </file>
      </activation>
      <build>
        <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <version>1.7</version>
            <executions>
              <execution>
                <id>prepare-checkout</id>
                <phase>pre-site</phase>
                <goals>
                  <goal>run</goal>
                </goals>
                <configuration>
                  <tasks>
                    <exec executable="svn">
                      <arg line="checkout --depth immediates ${commons.scmPubUrl} ${commons.scmPubCheckoutDirectory}" />
                    </exec>

                    <exec executable="svn">
                      <arg line="update --set-depth exclude ${commons.scmPubCheckoutDirectory}/javadocs" />
                    </exec>

                    <pathconvert pathsep=" " property="dirs">
                      <dirset dir="${commons.scmPubCheckoutDirectory}" includes="*" />
                    </pathconvert>
                    <exec executable="svn">
                      <arg line="update --set-depth infinity ${dirs}" />
                    </exec>
                  </tasks>
                </configuration>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>

</project>

```
