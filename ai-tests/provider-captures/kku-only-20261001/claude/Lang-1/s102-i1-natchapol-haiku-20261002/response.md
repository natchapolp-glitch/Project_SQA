I understand my role as your academic assistant. Before I generate comprehensive regression tests for `NumberUtils`, let me help you understand the approach I'm taking so we can work through this together thoughtfully.

## Understanding the Task

You're asking me to create deterministic unit tests for Apache Commons Lang's `NumberUtils` class based on its fixed reference implementation. This is excellent practice for understanding test-driven development and regression testing. Let me break down what I'll be testing:

**Key Method Categories:**
1. **String-to-primitive converters** (`toInt`, `toLong`, `toFloat`, `toDouble`, `toByte`, `toShort`) with default fallback values
2. **Validation helpers** (`isDigits`, `isNumber`, `isAllZeros`, `validateArray`)
3. **Min/max operations** across different primitive types (both single values and arrays)
4. **Number creation methods** (`createInteger`, `createLong`, `createFloat`, `createDouble`, `createNumber`, `createBigDecimal`, `createBigInteger`)

**Testing Strategy:**
- **Normal cases**: Valid inputs producing expected outputs
- **Boundary cases**: Edge values (min/max for primitives, empty strings)
- **Invalid inputs**: Null values, malformed strings, empty arrays
- **Exception paths**: Methods that throw `IllegalArgumentException` or `NumberFormatException`

The build uses **JUnit 4.11**, so I'll use that API exclusively.

---

Now, here are the comprehensive regression tests:

```java
org/apache/commons/lang3/math/NumberUtilsTest.java
package org.apache.commons.lang3.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Regression tests for NumberUtils class.
 * Tests cover normal cases, boundaries, invalid inputs, exception paths and branches.
 */
public class NumberUtilsTest {

    // ==================== toInt Tests ====================

    @Test
    public void testToIntWithValidString() {
        assertEquals(1, NumberUtils.toInt("1"));
        assertEquals(0, NumberUtils.toInt("0"));
        assertEquals(-1, NumberUtils.toInt("-1"));
        assertEquals(Integer.MAX_VALUE, NumberUtils.toInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, NumberUtils.toInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testToIntWithNullReturnsZero() {
        assertEquals(0, NumberUtils.toInt(null));
    }

    @Test
    public void testToIntWithEmptyStringReturnsZero() {
        assertEquals(0, NumberUtils.toInt(""));
    }

    @Test
    public void testToIntWithInvalidStringReturnsZero() {
        assertEquals(0, NumberUtils.toInt("abc"));
        assertEquals(0, NumberUtils.toInt("1.5"));
        assertEquals(0, NumberUtils.toInt("1e5"));
    }

    @Test
    public void testToIntWithDefaultValue() {
        assertEquals(42, NumberUtils.toInt("abc", 42));
        assertEquals(42, NumberUtils.toInt(null, 42));
        assertEquals(42, NumberUtils.toInt("", 42));
    }

    @Test
    public void testToIntWithValidStringAndDefaultValue() {
        assertEquals(1, NumberUtils.toInt("1", 42));
        assertEquals(100, NumberUtils.toInt("100", 99));
    }

    // ==================== toLong Tests ====================

    @Test
    public void testToLongWithValidString() {
        assertEquals(1L, NumberUtils.toLong("1"));
        assertEquals(0L, NumberUtils.toLong("0"));
        assertEquals(-1L, NumberUtils.toLong("-1"));
        assertEquals(Long.MAX_VALUE, NumberUtils.toLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, NumberUtils.toLong(String.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void testToLongWithNullReturnsZero() {
        assertEquals(0L, NumberUtils.toLong(null));
    }

    @Test
    public void testToLongWithEmptyStringReturnsZero() {
        assertEquals(0L, NumberUtils.toLong(""));
    }

    @Test
    public void testToLongWithInvalidStringReturnsZero() {
        assertEquals(0L, NumberUtils.toLong("abc"));
        assertEquals(0L, NumberUtils.toLong("1.5"));
    }

    @Test
    public void testToLongWithDefaultValue() {
        assertEquals(42L, NumberUtils.toLong("abc", 42L));
        assertEquals(42L, NumberUtils.toLong(null, 42L));
        assertEquals(42L, NumberUtils.toLong("", 42L));
    }

    @Test
    public void testToLongWithValidStringAndDefaultValue() {
        assertEquals(1L, NumberUtils.toLong("1", 42L));
    }

    // ==================== toFloat Tests ====================

    @Test
    public void testToFloatWithValidString() {
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.001f);
        assertEquals(0.0f, NumberUtils.toFloat("0"), 0.001f);
        assertEquals(-1.5f, NumberUtils.toFloat("-1.5"), 0.001f);
        assertEquals(1.5e2f, NumberUtils.toFloat("1.5e2"), 0.001f);
    }

    @Test
    public void testToFloatWithNullReturnsZero() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.001f);
    }

    @Test
    public void testToFloatWithEmptyStringReturnsZero() {
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.001f);
    }

    @Test
    public void testToFloatWithInvalidStringReturnsZero() {
        assertEquals(0.0f, NumberUtils.toFloat("abc"), 0.001f);
    }

    @Test
    public void testToFloatWithDefaultValue() {
        assertEquals(1.1f, NumberUtils.toFloat("abc", 1.1f), 0.001f);
        assertEquals(1.1f, NumberUtils.toFloat(null, 1.1f), 0.001f);
        assertEquals(1.1f, NumberUtils.toFloat("", 1.1f), 0.001f);
    }

    @Test
    public void testToFloatWithValidStringAndDefaultValue() {
        assertEquals(1.5f, NumberUtils.toFloat("1.5", 0.0f), 0.001f);
    }

    // ==================== toDouble Tests ====================

    @Test
    public void testToDoubleWithValidString() {
        assertEquals(1.5d, NumberUtils.toDouble("1.5"), 0.001d);
        assertEquals(0.0d, NumberUtils.toDouble("0"), 0.001d);
        assertEquals(-1.5d, NumberUtils.toDouble("-1.5"), 0.001d);
        assertEquals(1.5e2d, NumberUtils.toDouble("1.5e2"), 0.001d);
    }

    @Test
    public void testToDoubleWithNullReturnsZero() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.001d);
    }

    @Test
    public void testToDoubleWithEmptyStringReturnsZero() {
        assertEquals(0.0d, NumberUtils.toDouble(""), 0.001d);
    }

    @Test
    public void testToDoubleWithInvalidStringReturnsZero() {
        assertEquals(0.0d, NumberUtils.toDouble("abc"), 0.001d);
    }

    @Test
    public void testToDoubleWithDefaultValue() {
        assertEquals(1.1d, NumberUtils.toDouble("abc", 1.1d), 0.001d);
        assertEquals(1.1d, NumberUtils.toDouble(null, 1.1d), 0.001d);
        assertEquals(1.1d, NumberUtils.toDouble("", 1.1d), 0.001d);
    }

    @Test
    public void testToDoubleWithValidStringAndDefaultValue() {
        assertEquals(1.5d, NumberUtils.toDouble("1.5", 0.0d), 0.001d);
    }

    // ==================== toByte Tests ====================

    @Test
    public void testToByteWithValidString() {
        assertEquals((byte) 1, NumberUtils.toByte("1"));
        assertEquals((byte) 0, NumberUtils.toByte("0"));
        assertEquals((byte) -1, NumberUtils.toByte("-1"));
        assertEquals(Byte.MAX_VALUE, NumberUtils.toByte(String.valueOf(Byte.MAX_VALUE)));
        assertEquals(Byte.MIN_VALUE, NumberUtils.toByte(String.valueOf(Byte.MIN_VALUE)));
    }

    @Test
    public void testToByteWithNullReturnsZero() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
    }

    @Test
    public void testToByteWithEmptyStringReturnsZero() {
        assertEquals((byte) 0, NumberUtils.toByte(""));
    }

    @Test
    public void testToByteWithInvalidStringReturnsZero() {
        assertEquals((byte) 0, NumberUtils.toByte("abc"));
    }

    @Test
    public void testToByteWithDefaultValue() {
        assertEquals((byte) 42, NumberUtils.toByte("abc", (byte) 42));
        assertEquals((byte) 42, NumberUtils.toByte(null, (byte) 42));
    }

    @Test
    public void testToByteWithValidStringAndDefaultValue() {
        assertEquals((byte) 1, NumberUtils.toByte("1", (byte) 42));
    }

    // ==================== toShort Tests ====================

    @Test
    public void testToShortWithValidString() {
        assertEquals((short) 1, NumberUtils.toShort("1"));
        assertEquals((short) 0, NumberUtils.toShort("0"));
        assertEquals((short) -1, NumberUtils.toShort("-1"));
        assertEquals(Short.MAX_VALUE, NumberUtils.toShort(String.valueOf(Short.MAX_VALUE)));
        assertEquals(Short.MIN_VALUE, NumberUtils.toShort(String.valueOf(Short.MIN_VALUE)));
    }

    @Test
    public void testToShortWithNullReturnsZero() {
        assertEquals((short) 0, NumberUtils.toShort(null));
    }

    @Test
    public void testToShortWithEmptyStringReturnsZero() {
        assertEquals((short) 0, NumberUtils.toShort(""));
    }

    @Test
    public void testToShortWithInvalidStringReturnsZero() {
        assertEquals((short) 0, NumberUtils.toShort("abc"));
    }

    @Test
    public void testToShortWithDefaultValue() {
        assertEquals((short) 42, NumberUtils.toShort("abc", (short) 42));
        assertEquals((short) 42, NumberUtils.toShort(null, (short) 42));
    }

    @Test
    public void testToShortWithValidStringAndDefaultValue() {
        assertEquals((short) 1, NumberUtils.toShort("1", (short) 42));
    }

    // ==================== isDigits Tests ====================

    @Test
    public void testIsDigitsWithValidDigitString() {
        assertTrue(NumberUtils.isDigits("123"));
        assertTrue(NumberUtils.isDigits("0"));
        assertTrue(NumberUtils.isDigits("9876543210"));
    }

    @Test
    public void testIsDigitsWithNullReturnsFalse() {
        assertFalse(NumberUtils.isDigits(null));
    }

    @Test
    public void testIsDigitsWithEmptyStringReturnsFalse() {
        assertFalse(NumberUtils.isDigits(""));
    }

    @Test
    public void testIsDigitsWithNonDigitStringReturnsFalse() {
        assertFalse(NumberUtils.isDigits("abc"));
        assertFalse(NumberUtils.isDigits("1.5"));
        assertFalse(NumberUtils.isDigits("1a"));
        assertFalse(NumberUtils.isDigits("-1"));
        assertFalse(NumberUtils.isDigits("1 2"));
    }

    // ==================== isNumber Tests ====================

    @Test
    public void testIsNumberWithValidIntegers() {
        assertTrue(NumberUtils.isNumber("1"));
        assertTrue(NumberUtils.isNumber("0"));
        assertTrue(NumberUtils.isNumber("-1"));
        assertTrue(NumberUtils.isNumber("+1"));
    }

    @Test
    public void testIsNumberWithValidFloatAndDouble() {
        assertTrue(NumberUtils.isNumber("1.5"));
        assertTrue(NumberUtils.isNumber("-1.5"));
        assertTrue(NumberUtils.isNumber("1e5"));
        assertTrue(NumberUtils.isNumber("1E5"));
        assertTrue(NumberUtils.isNumber("1.5e2"));
        assertTrue(NumberUtils.isNumber("1.5E-2"));
    }

    @Test
    public void testIsNumberWithTypeQualifiers() {
        assertTrue(NumberUtils.isNumber("1L"));
        assertTrue(NumberUtils.isNumber("1l"));
        assertTrue(NumberUtils.isNumber("1.5f"));
        assertTrue(NumberUtils.isNumber("1.5F"));
        assertTrue(NumberUtils.isNumber("1.5d"));
        assertTrue(NumberUtils.isNumber("1.5D"));
    }

    @Test
    public void testIsNumberWithHexadecimal() {
        assertTrue(NumberUtils.isNumber("0x1a"));
        assertTrue(NumberUtils.isNumber("0xFF"));
        assertTrue(NumberUtils.isNumber("-0x1a"));
    }

    @Test
    public void testIsNumberWithTrailingDecimalPoint() {
        assertTrue(NumberUtils.isNumber("1."));
    }

    @Test
    public void testIsNumberWithNullReturnsFalse() {
        assertFalse(NumberUtils.isNumber(null));
    }

    @Test
    public void testIsNumberWithEmptyStringReturnsFalse() {
        assertFalse(NumberUtils.isNumber(""));
    }

    @Test
    public void testIsNumberWithInvalidStringReturnsFalse() {
        assertFalse(NumberUtils.isNumber("abc"));
        assertFalse(NumberUtils.isNumber("1.2.3"));
        assertFalse(NumberUtils.isNumber("1e"));
        assertFalse(NumberUtils.isNumber("e1"));
        assertFalse(NumberUtils.isNumber("0x"));
        assertFalse(NumberUtils.isNumber("1l2"));
    }

    // ==================== max(byte, byte, byte) Tests ====================

    @Test
    public void testMaxThreeBytes() {
        assertEquals((byte) 3, NumberUtils.max((byte) 1, (byte) 2, (byte) 3));
        assertEquals((byte) 3, NumberUtils.max((byte) 3, (byte) 1, (byte) 2));
        assertEquals((byte) 3, NumberUtils.max((byte) 1, (byte) 3, (byte) 2));
    }

    @Test
    public void testMaxThreeBytesNegative() {
        assertEquals((byte) -1, NumberUtils.max((byte) -1, (byte) -2, (byte) -3));
    }

    @Test
    public void testMaxThreeBytesEqual() {
        assertEquals((byte) 5, NumberUtils.max((byte) 5, (byte) 5, (byte) 5));
    }

    // ==================== min(byte, byte, byte) Tests ====================

    @Test
    public void testMinThreeBytes() {
        assertEquals((byte) 1, NumberUtils.min((byte) 1, (byte) 2, (byte) 3));
        assertEquals((byte) 1, NumberUtils.min((byte) 3, (byte) 1, (byte) 2));
    }

    @Test
    public void testMinThreeBytesNegative() {
        assertEquals((byte) -3, NumberUtils.min((byte) -1, (byte) -2, (byte) -3));
    }

    // ==================== max(byte[]) Tests ====================

    @Test
    public void testMaxByteArray() {
        assertEquals((byte) 3, NumberUtils.max(new byte[]{1, 2, 3}));
        assertEquals((byte) 5, NumberUtils.max(new byte[]{5}));
    }

    @Test
    public void testMaxByteArrayNegative() {
        assertEquals((byte) -1, NumberUtils.max(new byte[]{-1, -2, -3}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxByteArrayNull() {
        NumberUtils.max((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxByteArrayEmpty() {
        NumberUtils.max(new byte[]{});
    }

    // ==================== min(byte[]) Tests ====================

    @Test
    public void testMinByteArray() {
        assertEquals((byte) 1, NumberUtils.min(new byte[]{1, 2, 3}));
        assertEquals((byte) 5, NumberUtils.min(new byte[]{5}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinByteArrayNull() {
        NumberUtils.min((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinByteArrayEmpty() {
        NumberUtils.min(new byte[]{});
    }

    // ==================== max(short, short, short) Tests ====================

    @Test
    public void testMaxThreeShorts() {
        assertEquals((short) 3, NumberUtils.max((short) 1, (short) 2, (short) 3));
    }

    // ==================== min(short, short, short) Tests ====================

    @Test
    public void testMinThreeShorts() {
        assertEquals((short) 1, NumberUtils.min((short) 1, (short) 2, (short) 3));
    }

    // ==================== max(short[]) Tests ====================

    @Test
    public void testMaxShortArray() {
        assertEquals((short) 3, NumberUtils.max(new short[]{1, 2, 3}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxShortArrayNull() {
        NumberUtils.max((short[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxShortArrayEmpty() {
        NumberUtils.max(new short[]{});
    }

    // ==================== min(short[]) Tests ====================

    @Test
    public void testMinShortArray() {
        assertEquals((short) 1, NumberUtils.min(new short[]{1, 2, 3}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArrayNull() {
        NumberUtils.min((short[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArrayEmpty() {
        NumberUtils.min(new short[]{});
    }

    // ==================== max(int, int, int) Tests ====================

    @Test
    public void testMaxThreeInts() {
        assertEquals(3, NumberUtils.max(1, 2, 3));
        assertEquals(3, NumberUtils.max(3, 1, 2));
    }

    @Test
    public void testMaxThreeIntsNegative() {
        assertEquals(-1, NumberUtils.max(-1, -2, -3));
    }

    // ==================== min(int, int, int) Tests ====================

    @Test
    public void testMinThreeInts() {
        assertEquals(1, NumberUtils.min(1, 2, 3));
    }

    @Test
    public void testMinThreeIntsNegative() {
        assertEquals(-3, NumberUtils.min(-1, -2, -3));
    }

    // ==================== max(int[]) Tests ====================

    @Test
    public void testMaxIntArray() {
        assertEquals(3, NumberUtils.max(new int[]{1, 2, 3}));
        assertEquals(100, NumberUtils.max(new int[]{100}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArrayNull() {
        NumberUtils.max((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArrayEmpty() {
        NumberUtils.max(new int[]{});
    }

    // ==================== min(int[]) Tests ====================

    @Test
    public void testMinIntArray() {
        assertEquals(1, NumberUtils.min(new int[]{1, 2, 3}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinIntArrayNull() {
        NumberUtils.min((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinIntArrayEmpty() {
        NumberUtils.min(new int[]{});
    }

    // ==================== max(long, long, long) Tests ====================

    @Test
    public void testMaxThreeLongs() {
        assertEquals(3L, NumberUtils.max(1L, 2L, 3L));
    }

    // ==================== min(long, long, long) Tests ====================

    @Test
    public void testMinThreeLongs() {
        assertEquals(1L, NumberUtils.min(1L, 2L, 3L));
    }

    // ==================== max(long[]) Tests ====================

    @Test
    public void testMaxLongArray() {
        assertEquals(3L, NumberUtils.max(new long[]{1L, 2L, 3L}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArrayNull() {
        NumberUtils.max((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArrayEmpty() {
        NumberUtils.max(new long[]{});
    }

    // ==================== min(long[]) Tests ====================

    @Test
    public void testMinLongArray() {
        assertEquals(1L, NumberUtils.min(new long[]{1L, 2L, 3L}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArrayNull() {
        NumberUtils.min((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArrayEmpty() {
        NumberUtils.min(new long[]{});
    }

    // ==================== max(float, float, float) Tests ====================

    @Test
    public void testMaxThreeFloats() {
        assertEquals(3.0f, NumberUtils.max(1.0f, 2.0f, 3.0f), 0.001f);
    }

    @Test
    public void testMaxThreeFloatsNegative() {
        assertEquals(-1.0f, NumberUtils.max(-1.0f, -2.0f, -3.0f), 0.001f);
    }

    @Test
    public void testMaxThreeFloatsWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.max(Float.NaN, 1.0f, 2.0f)));
    }

    // ==================== min(float, float, float) Tests ====================

    @Test
    public void testMinThreeFloats() {
        assertEquals(1.0f, NumberUtils.min(1.0f, 2.0f, 3.0f), 0.001f);
    }

    @Test
    public void testMinThreeFloatsWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(Float.NaN, 1.0f, 2.0f)));
    }

    // ==================== max(float[]) Tests ====================

    @Test
    public void testMaxFloatArray() {
        assertEquals(3.0f, NumberUtils.max(new float[]{1.0f, 2.0f, 3.0f}), 0.001f);
    }

    @Test
    public void testMaxFloatArrayWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.max(new float[]{Float.NaN, 1.0f, 2.0f})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxFloatArrayNull() {
        NumberUtils.max((float[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxFloatArrayEmpty() {
        NumberUtils.max(new float[]{});
    }

    // ==================== min(float[]) Tests ====================

    @Test
    public void testMinFloatArray() {
        assertEquals(1.0f, NumberUtils.min(new float[]{1.0f, 2.0f, 3.0f}), 0.001f);
    }

    @Test
    public void testMinFloatArrayWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(new float[]{Float.NaN, 1.0f, 2.0f})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinFloatArrayNull() {
        NumberUtils.min((float[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinFloatArrayEmpty() {
        NumberUtils.min(new float[]{});
    }

    // ==================== max(double, double, double) Tests ====================

    @Test
    public void testMaxThreeDoubles() {
        assertEquals(3.0d, NumberUtils.max(1.0d, 2.0d, 3.0d), 0.001d);
    }

    @Test
    public void testMaxThreeDoublesNegative() {
        assertEquals(-1.0d, NumberUtils.max(-1.0d, -2.0d, -3.0d), 0.001d);
    }

    @Test
    public void testMaxThreeDoublesWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.max(Double.NaN, 1.0d, 2.0d)));
    }

    // ==================== min(double, double, double) Tests ====================

    @Test
    public void testMinThreeDoubles() {
        assertEquals(1.0d, NumberUtils.min(1.0d, 2.0d, 3.0d), 0.001d);
    }

    @Test
    public void testMinThreeDoublesWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(Double.NaN, 1.0d, 2.0d)));
    }

    // ==================== max(double[]) Tests ====================

    @Test
    public void testMaxDoubleArray() {
        assertEquals(3.0d, NumberUtils.max(new double[]{1.0d, 2.0d, 3.0d}), 0.001d);
    }

    @Test
    public void testMaxDoubleArrayWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.max(new double[]{Double.NaN, 1.0d, 2.0d})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxDoubleArrayNull() {
        NumberUtils.max((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxDoubleArrayEmpty() {
        NumberUtils.max(new double[]{});
    }

    // ==================== min(double[]) Tests ====================

    @Test
    public void testMinDoubleArray() {
        assertEquals(1.0d, NumberUtils.min(new double[]{1.0d, 2.0d, 3.0d}), 0.001d);
    }

    @Test
    public void testMinDoubleArrayWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(new double[]{Double.NaN, 1.0d, 2.0d})));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinDoubleArrayNull() {
        NumberUtils.min((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinDoubleArrayEmpty() {
        NumberUtils.min(new double[]{});
    }

    // ==================== createInteger Tests ====================

    @Test
    public void testCreateIntegerWithValidString() {
        assertEquals(Integer.valueOf(1), NumberUtils.createInteger("1"));
        assertEquals(Integer.valueOf(0), NumberUtils.createInteger("0"));
        assertEquals(Integer.valueOf(-1), NumberUtils.createInteger("-1"));
    }

    @Test
    public void testCreateIntegerWithHex() {
        assertEquals(Integer.valueOf(0xFF), NumberUtils.createInteger("0xFF"));
        assertEquals(Integer.valueOf(255), NumberUtils.createInteger("#FF"));
    }

    @Test
    public void testCreateIntegerWithOctal() {
        assertEquals(Integer.valueOf(010), NumberUtils.createInteger("010"));
    }

    @Test
    public void testCreateIntegerWithNull() {
        assertNull(NumberUtils.createInteger(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateIntegerWithInvalidString() {
        NumberUtils.createInteger("abc");
    }

    // ==================== createLong Tests ====================

    @Test
    public void testCreateLongWithValidString() {
        assertEquals(Long.valueOf(1L), NumberUtils.createLong("1"));
        assertEquals(Long.valueOf(0L), NumberUtils.createLong("0"));
    }

    @Test
    public void testCreateLongWithHex() {
        assertEquals(Long.valueOf(0xFFL), NumberUtils.createLong("0xFF"));
    }

    @Test
    public void testCreateLongWithNull() {
        assertNull(NumberUtils.createLong(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateLongWithInvalidString() {
        NumberUtils.createLong("abc");
    }

    // ==================== createFloat Tests ====================

    @Test
    public void testCreateFloatWithValidString() {
        assertEquals(Float.valueOf(1.5f), NumberUtils.createFloat("1.5"));
        assertEquals(Float.valueOf(0.0f), NumberUtils.createFloat("0"));
    }

    @Test
    public void testCreateFloatWithNull() {
        assertNull(NumberUtils.createFloat(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateFloatWithInvalidString() {
        NumberUtils.createFloat("abc");
    }

    // ==================== createDouble Tests ====================

    @Test
    public void testCreateDoubleWithValidString() {
        assertEquals(Double.valueOf(1.5d), NumberUtils.createDouble("1.5"));
        assertEquals(Double.valueOf(0.0d), NumberUtils.createDouble("0"));
    }

    @Test
    public void testCreateDoubleWithNull() {
        assertNull(NumberUtils.createDouble(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateDoubleWithInvalidString() {
        NumberUtils.createDouble("abc");
    }

    // ==================== createBigInteger Tests ====================

    @Test
    public void testCreateBigIntegerWithValidString() {
        assertEquals(new BigInteger("1"), NumberUtils.createBigInteger("1"));
        assertEquals(new BigInteger("0"), NumberUtils.createBigInteger("0"));
        assertEquals(new BigInteger("-1"), NumberUtils.createBigInteger("-1"));
    }

    @Test
    public void testCreateBigIntegerWithHex() {
        assertEquals(new BigInteger("FF", 16), NumberUtils.createBigInteger("0xFF"));
        assertEquals(new BigInteger("FF", 16), NumberUtils.createBigInteger("#FF"));
    }

    @Test
    public