src/test/java/org/apache/commons/lang3/math/NumberUtilsTest.java
package org.apache.commons.lang3.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Unit tests for {@link NumberUtils}.
 */
public class NumberUtilsTest {

    // ==================================================
    // Tests for isAllZeros (private, tested via behavior)
    // ==================================================

    @Test
    public void testCreateNumberAllZerosFloat() {
        Number result = NumberUtils.createNumber("0.0F");
        assertTrue("Expected a Float for '0.0F'", result instanceof Float);
        assertEquals("Value should be 0.0f", 0.0f, ((Float) result).floatValue(), 0.001f);
    }

    @Test
    public void testCreateNumberAllZerosDouble() {
        Number result = NumberUtils.createNumber("0.0D");
        assertTrue("Expected a Double for '0.0D'", result instanceof Double);
        assertEquals("Value should be 0.0d", 0.0d, ((Double) result).doubleValue(), 0.001d);
    }

    // ==================================================
    // Tests for validateArray (private, tested via array methods)
    // ==================================================

    @Test(expected = IllegalArgumentException.class)
    public void testMinNullArray() {
        NumberUtils.min((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinEmptyArray() {
        NumberUtils.min(new long[0](undefined));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxNullArray() {
        NumberUtils.max((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxEmptyArray() {
        NumberUtils.max(new long[0](undefined));
    }

    // ==================================================
    // Tests for isDigits
    // ==================================================

    @Test
    public void testIsDigitsNull() {
        assertFalse(NumberUtils.isDigits(null));
    }

    @Test
    public void testIsDigitsEmpty() {
        assertFalse(NumberUtils.isDigits(""));
    }

    @Test
    public void testIsDigitsAllDigits() {
        assertTrue(NumberUtils.isDigits("1234567890"));
    }

    @Test
    public void testIsDigitsWithLetters() {
        assertFalse(NumberUtils.isDigits("123a45"));
    }

    @Test
    public void testIsDigitsWithSpecialCharacters() {
        assertFalse(NumberUtils.isDigits("123.45"));
    }

    // ==================================================
    // Tests for isNumber
    // ==================================================

    @Test
    public void testIsNumberNull() {
        assertFalse(NumberUtils.isNumber(null));
    }

    @Test
    public void testIsNumberEmpty() {
        assertFalse(NumberUtils.isNumber(""));
    }

    @Test
    public void testIsNumberValidInteger() {
        assertTrue(NumberUtils.isNumber("123"));
    }

    @Test
    public void testIsNumberValidHex() {
        assertTrue(NumberUtils.isNumber("0xA"));
    }

    @Test
    public void testIsNumberValidNegativeHex() {
        assertTrue(NumberUtils.isNumber("-0xA"));
    }

    @Test
    public void testIsNumberInvalidHexZeroX() {
        assertFalse(NumberUtils.isNumber("0x"));
    }

    @Test
    public void testIsNumberValidScientific() {
        assertTrue(NumberUtils.isNumber("1.0E3"));
    }

    @Test
    public void testIsNumberValidFloatSuffix() {
        assertTrue(NumberUtils.isNumber("1.0F"));
    }

    @Test
    public void testIsNumberValidDoubleSuffix() {
        assertTrue(NumberUtils.isNumber("1.0D"));
    }

    @Test
    public void testIsNumberValidLongSuffix() {
        assertTrue(NumberUtils.isNumber("123L"));
    }

    @Test
    public void testIsNumberInvalidMultipleDecimals() {
        assertFalse(NumberUtils.isNumber("1.2.3"));
    }

    @Test
    public void testIsNumberInvalidMultipleExponents() {
        assertFalse(NumberUtils.isNumber("1E2E3"));
    }

    @Test
    public void testIsNumberInvalidTrailingExponent() {
        assertFalse(NumberUtils.isNumber("1E"));
    }

    // ==================================================
    // Tests for max (byte, byte, byte)
    // ==================================================

    @Test
    public void testMaxByteAllDifferent() {
        assertEquals(3, NumberUtils.max((byte) 1, (byte) 2, (byte) 3));
    }

    @Test
    public void testMaxByteTwoEqual() {
        assertEquals(3, NumberUtils.max((byte) 3, (byte) 2, (byte) 3));
    }

    @Test
    public void testMaxByteAllEqual() {
        assertEquals(1, NumberUtils.max((byte) 1, (byte) 1, (byte) 1));
    }

    // ==================================================
    // Tests for max (byte[])
    // ==================================================

    @Test
    public void testMaxByteArraySingleElement() {
        byte[] array = {5};
        assertEquals(5, NumberUtils.max(array));
    }

    @Test
    public void testMaxByteArrayMultipleElements() {
        byte[] array = {1, 5, 3};
        assertEquals(5, NumberUtils.max(array));
    }

    @Test
    public void testMaxByteArrayWithNegative() {
        byte[] array = {-1, -2, -3};
        assertEquals(-1, NumberUtils.max(array));
    }

    // ==================================================
    // Tests for min (byte, byte, byte)
    // ==================================================

    @Test
    public void testMinByteAllDifferent() {
        assertEquals(1, NumberUtils.min((byte) 1, (byte) 2, (byte) 3));
    }

    @Test
    public void testMinByteTwoEqual() {
        assertEquals(2, NumberUtils.min((byte) 3, (byte) 2, (byte) 3));
    }

    @Test
    public void testMinByteAllEqual() {
        assertEquals(5, NumberUtils.min((byte) 5, (byte) 5, (byte) 5));
    }

    // ==================================================
    // Tests for min (byte[])
    // ==================================================

    @Test
    public void testMinByteArraySingleElement() {
        byte[] array = {5};
        assertEquals(5, NumberUtils.min(array));
    }

    @Test
    public void testMinByteArrayMultipleElements() {
        byte[] array = {5, 1, 3};
        assertEquals(1, NumberUtils.min(array));
    }

    @Test
    public void testMinByteArrayWithNegative() {
        byte[] array = {-1, -2, -3};
        assertEquals(-3, NumberUtils.min(array));
    }

    // ==================================================
    // Tests for toByte (String)
    // ==================================================

    @Test
    public void testToByteNull() {
        assertEquals(0, NumberUtils.toByte(null));
    }

    @Test
    public void testToByteEmpty() {
        assertEquals(0, NumberUtils.toByte(""));
    }

    @Test
    public void testToByteValid() {
        assertEquals(1, NumberUtils.toByte("1"));
    }

    // ==================================================
    // Tests for toByte (String, byte)
    // ==================================================

    @Test
    public void testToByteNullDefault() {
        assertEquals((byte) 10, NumberUtils.toByte(null, (byte) 10));
    }

    @Test
    public void testToByteEmptyDefault() {
        assertEquals((byte) 10, NumberUtils.toByte("", (byte) 10));
    }

    @Test
    public void testToByteValidDefault() {
        assertEquals((byte) 15, NumberUtils.toByte("15", (byte) 10));
    }

    @Test
    public void testToByteInvalidDefault() {
        assertEquals((byte) 10, NumberUtils.toByte("abc", (byte) 10));
    }

    // ==================================================
    // Tests for max (double, double, double)
    // ==================================================

    @Test
    public void testMaxDoubleAllDifferent() {
        assertEquals(3.0, NumberUtils.max(1.0, 2.0, 3.0), 0.0001);
    }

    @Test
    public void testMaxDoubleNaN() {
        assertTrue(Double.isNaN(NumberUtils.max(Double.NaN, 1.0, 2.0)));
    }

    @Test
    public void testMaxDoubleWithNegativeInfinity() {
        assertEquals(2.0, NumberUtils.max(Double.NEGATIVE_INFINITY, 1.0, 2.0), 0.0001);
    }

    // ==================================================
    // Tests for max (double[])
    // ==================================================

    @Test
    public void testMaxDoubleArraySingleElement() {
        double[] array = {5.5};
        assertEquals(5.5, NumberUtils.max(array), 0.0001);
    }

    @Test
    public void testMaxDoubleArrayMultipleElements() {
        double[] array = {1.1, 5.5, 3.3};
        assertEquals(5.5, NumberUtils.max(array), 0.0001);
    }

    @Test
    public void testMaxDoubleArrayNaN() {
        double[] array = {1.0, Double.NaN, 3.0};
        assertTrue(Double.isNaN(NumberUtils.max(array)));
    }

    // ==================================================
    // Tests for min (double, double, double)
    // ==================================================

    @Test
    public void testMinDoubleAllDifferent() {
        assertEquals(1.0, NumberUtils.min(1.0, 2.0, 3.0), 0.0001);
    }

    @Test
    public void testMinDoubleNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(1.0, Double.NaN, 3.0)));
    }

    @Test
    public void testMinDoubleWithNegativeInfinity() {
        assertEquals(Double.NEGATIVE_INFINITY, NumberUtils.min(Double.NEGATIVE_INFINITY, 1.0, 2.0), 0.0001);
    }

    // ==================================================
    // Tests for min (double[])
    // ==================================================

    @Test
    public void testMinDoubleArraySingleElement() {
        double[] array = {5.5};
        assertEquals(5.5, NumberUtils.min(array), 0.0001);
    }

    @Test
    public void testMinDoubleArrayMultipleElements() {
        double[] array = {5.5, 1.1, 3.3};
        assertEquals(1.1, NumberUtils.min(array), 0.0001);
    }

    @Test
    public void testMinDoubleArrayNaN() {
        double[] array = {1.0, Double.NaN, 3.0};
        assertTrue(Double.isNaN(NumberUtils.min(array)));
    }

    // ==================================================
    // Tests for toDouble (String)
    // ==================================================

    @Test
    public void testToDoubleNull() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0001d);
    }

    @Test
    public void testToDoubleEmpty() {
        assertEquals(0.0d, NumberUtils.toDouble(""), 0.0001d);
    }

    @Test
    public void testToDoubleValid() {
        assertEquals(1.5d, NumberUtils.toDouble("1.5"), 0.0001d);
    }

    // ==================================================
    // Tests for toDouble (String, double)
    // ==================================================

    @Test
    public void testToDoubleNullDefault() {
        assertEquals(10.5d, NumberUtils.toDouble(null, 10.5d), 0.0001d);
    }

    @Test
    public void testToDoubleEmptyDefault() {
        assertEquals(10.5d, NumberUtils.toDouble("", 10.5d), 0.0001d);
    }

    @Test
    public void testToDoubleValidDefault() {
        assertEquals(15.5d, NumberUtils.toDouble("15.5", 10.5d), 0.0001d);
    }

    @Test
    public void testToDoubleInvalidDefault() {
        assertEquals(10.5d, NumberUtils.toDouble("abc", 10.5d), 0.0001d);
    }

    // ==================================================
    // Tests for max (float, float, float)
    // ==================================================

    @Test
    public void testMaxFloatAllDifferent() {
        assertEquals(3.0f, NumberUtils.max(1.0f, 2.0f, 3.0f), 0.0001f);
    }

    @Test
    public void testMaxFloatNaN() {
        assertTrue(Float.isNaN(NumberUtils.max(Float.NaN, 1.0f, 2.0f)));
    }

    @Test
    public void testMaxFloatWithNegativeInfinity() {
        assertEquals(2.0f, NumberUtils.max(Float.NEGATIVE_INFINITY, 1.0f, 2.0f), 0.0001f);
    }

    // ==================================================
    // Tests for max (float[])
    // ==================================================

    @Test
    public void testMaxFloatArraySingleElement() {
        float[] array = {5.5f};
        assertEquals(5.5f, NumberUtils.max(array), 0.0001f);
    }

    @Test
    public void testMaxFloatArrayMultipleElements() {
        float[] array = {1.1f, 5.5f, 3.3f};
        assertEquals(5.5f, NumberUtils.max(array), 0.0001f);
    }

    @Test
    public void testMaxFloatArrayNaN() {
        float[] array = {1.0f, Float.NaN, 3.0f};
        assertTrue(Float.isNaN(NumberUtils.max(array)));
    }

    // ==================================================
    // Tests for min (float, float, float)
    // ==================================================

    @Test
    public void testMinFloatAllDifferent() {
        assertEquals(1.0f, NumberUtils.min(1.0f, 2.0f, 3.0f), 0.0001f);
    }

    @Test
    public void testMinFloatNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(1.0f, Float.NaN, 3.0f)));
    }

    @Test
    public void testMinFloatWithNegativeInfinity() {
        assertEquals(Float.NEGATIVE_INFINITY, NumberUtils.min(Float.NEGATIVE_INFINITY, 1.0f, 2.0f), 0.0001f);
    }

    // ==================================================
    // Tests for min (float[])
    // ==================================================

    @Test
    public void testMinFloatArraySingleElement() {
        float[] array = {5.5f};
        assertEquals(5.5f, NumberUtils.min(array), 0.0001f);
    }

    @Test
    public void testMinFloatArrayMultipleElements() {
        float[] array = {5.5f, 1.1f, 3.3f};
        assertEquals(1.1f, NumberUtils.min(array), 0.0001f);
    }

    @Test
    public void testMinFloatArrayNaN() {
        float[] array = {1.0f, Float.NaN, 3.0f};
        assertTrue(Float.isNaN(NumberUtils.min(array)));
    }

    // ==================================================
    // Tests for toFloat (String)
    // ==================================================

    @Test
    public void testToFloatNull() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0001f);
    }

    @Test
    public void testToFloatEmpty() {
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.0001f);
    }

    @Test
    public void testToFloatValid() {
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0001f);
    }

    // ==================================================
    // Tests for toFloat (String, float)
    // ==================================================

    @Test
    public void testToFloatNullDefault() {
        assertEquals(10.5f, NumberUtils.toFloat(null, 10.5f), 0.0001f);
    }

    @Test
    public void testToFloatEmptyDefault() {
        assertEquals(10.5f, NumberUtils.toFloat("", 10.5f), 0.0001f);
    }

    @Test
    public void testToFloatValidDefault() {
        assertEquals(15.5f, NumberUtils.toFloat("15.5", 10.5f), 0.0001f);
    }

    @Test
    public void testToFloatInvalidDefault() {
        assertEquals(10.5f, NumberUtils.toFloat("abc", 10.5f), 0.0001f);
    }

    // ==================================================
    // Tests for max (int, int, int)
    // ==================================================

    @Test
    public void testMaxIntAllDifferent() {
        assertEquals(3, NumberUtils.max(1, 2, 3));
    }

    @Test
    public void testMaxIntTwoEqual() {
        assertEquals(3, NumberUtils.max(3, 2, 3));
    }

    @Test
    public void testMaxIntAllEqual() {
        assertEquals(1, NumberUtils.max(1, 1, 1));
    }

    // ==================================================
    // Tests for max (int[])
    // ==================================================

    @Test
    public void testMaxIntArraySingleElement() {
        int[] array = {5};
        assertEquals(5, NumberUtils.max(array));
    }

    @Test
    public void testMaxIntArrayMultipleElements() {
        int[] array = {1, 5, 3};
        assertEquals(5, NumberUtils.max(array));
    }

    @Test
    public void testMaxIntArrayWithNegative() {
        int[] array = {-1, -2, -3};
        assertEquals(-1, NumberUtils.max(array));
    }

    // ==================================================
    // Tests for min (int, int, int)
    // ==================================================

    @Test
    public void testMinIntAllDifferent() {
        assertEquals(1, NumberUtils.min(1, 2, 3));
    }

    @Test
    public void testMinIntTwoEqual() {
        assertEquals(2, NumberUtils.min(3, 2, 3));
    }

    @Test
    public void testMinIntAllEqual() {
        assertEquals(5, NumberUtils.min(5, 5, 5));
    }

    // ==================================================
    // Tests for min (int[])
    // ==================================================

    @Test
    public void testMinIntArraySingleElement() {
        int[] array = {5};
        assertEquals(5, NumberUtils.min(array));
    }

    @Test
    public void testMinIntArrayMultipleElements() {
        int[] array = {5, 1, 3};
        assertEquals(1, NumberUtils.min(array));
    }

    @Test
    public void testMinIntArrayWithNegative() {
        int[] array = {-1, -2, -3};
        assertEquals(-3, NumberUtils.min(array));
    }

    // ==================================================
    // Tests for toInt (String)
    // ==================================================

    @Test
    public void testToIntNull() {
        assertEquals(0, NumberUtils.toInt(null));
    }

    @Test
    public void testToIntEmpty() {
        assertEquals(0, NumberUtils.toInt(""));
    }

    @Test
    public void testToIntValid() {
        assertEquals(1, NumberUtils.toInt("1"));
    }

    // ==================================================
    // Tests for toInt (String, int)
    // ==================================================

    @Test
    public void testToIntNullDefault() {
        assertEquals(10, NumberUtils.toInt(null, 10));
    }

    @Test
    public void testToIntEmptyDefault() {
        assertEquals(10, NumberUtils.toInt("", 10));
    }

    @Test
    public void testToIntValidDefault() {
        assertEquals(15, NumberUtils.toInt("15", 10));
    }

    @Test
    public void testToIntInvalidDefault() {
        assertEquals(10, NumberUtils.toInt("abc", 10));
    }

    // ==================================================
    // Tests for createDouble
    // ==================================================

    @Test
    public void testCreateDoubleNull() {
        assertNull(NumberUtils.createDouble(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateDoubleEmpty() {
        NumberUtils.createDouble("");
    }

    @Test
    public void testCreateDoubleValid() {
        assertEquals(Double.valueOf(1.5d), NumberUtils.createDouble("1.5"));
    }

    // ==================================================
    // Tests for createFloat
    // ==================================================

    @Test
    public void testCreateFloatNull() {
        assertNull(NumberUtils.createFloat(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateFloatEmpty() {
        NumberUtils.createFloat("");
    }

    @Test
    public void testCreateFloatValid() {
        assertEquals(Float.valueOf(1.5f), NumberUtils.createFloat("1.5"));
    }

    // ==================================================
    // Tests for createInteger
    // ==================================================

    @Test
    public void testCreateIntegerNull() {
        assertNull(NumberUtils.createInteger(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateIntegerEmpty() {
        NumberUtils.createInteger("");
    }

    @Test
    public void testCreateIntegerValid() {
        assertEquals(Integer.valueOf(15), NumberUtils.createInteger("15"));
    }

    @Test
    public void testCreateIntegerHex() {
        assertEquals(Integer.valueOf(255), NumberUtils.createInteger("0xFF"));
    }

    @Test
    public void testCreateIntegerOctal() {
        assertEquals(Integer.valueOf(8), NumberUtils.createInteger("010"));
    }

    // ==================================================
    // Tests for createLong
    // ==================================================

    @Test
    public void testCreateLongNull() {
        assertNull(NumberUtils.createLong(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateLongEmpty() {
        NumberUtils.createLong("");
    }

    @Test
    public void testCreateLongValid() {
        assertEquals(Long.valueOf(15), NumberUtils.createLong("15"));
    }

    // ==================================================
    // Tests for createNumber
    // ==================================================

    @Test
    public void testCreateNumberNull() {
        assertNull(NumberUtils.createNumber(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberEmpty() {
        NumberUtils.createNumber("");
    }

    @Test
    public void testCreateNumberInteger() {
        Number result = NumberUtils.createNumber("123");
        assertTrue("Expected an Integer", result instanceof Integer);
        assertEquals(123, ((Integer) result).intValue());
    }

    @Test
    public void testCreateNumberLongSuffix() {
        Number result = NumberUtils.createNumber("123L");
        assertTrue("Expected a Long", result instanceof Long);
        assertEquals(123L, ((Long) result).longValue());
    }

    @Test
    public void testCreateNumberFloatSuffix() {
        Number result = NumberUtils.createNumber("1.5F");
        assertTrue("Expected a Float", result instanceof Float);
        assertEquals(1.5f, ((Float) result).floatValue(), 0.001f);
    }

    @Test
    public void testCreateNumberDoubleSuffix() {
        Number result = NumberUtils.createNumber("1.5D");
        assertTrue("Expected a Double", result instanceof Double);
        assertEquals(1.5d, ((Double) result).doubleValue(), 0.001d);
    }

    @Test
    public void testCreateNumberHexPrefix0x() {
        Number result = NumberUtils.createNumber("0xFF");
        assertTrue("Expected an Integer", result instanceof Integer);
        assertEquals(255, ((Integer) result).intValue());
    }

    @Test
    public void testCreateNumberHexPrefixHash() {
        Number result = NumberUtils.createNumber("#FF");
        assertTrue("Expected an Integer", result instanceof Integer);
        assertEquals(255, ((Integer) result).intValue());
    }

    @Test
    public void testCreateNumberScientificNotation() {
        Number result = NumberUtils.createNumber("1.0E3");
        assertTrue("Expected a Double or Float", result instanceof Double || result instanceof Float);
        assertEquals(1000.0, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateNumberNegativeHex() {
        Number result = NumberUtils.createNumber("-0xFF");
        assertTrue("Expected an Integer", result instanceof Integer);
        assertEquals(-255, ((Integer) result).intValue());
    }

    @Test
    public void testCreateNumberBigInteger() {
        Number result = NumberUtils.createNumber("99999999999999999999");
        assertTrue("Expected a BigInteger", result instanceof BigInteger);
        assertEquals(new BigInteger("99999999999999999999"), result);
    }

    @Test
    public void testCreateNumberBigDecimal() {
        Number result = NumberUtils.createNumber("1.5E20");
        assertTrue("Expected a BigDecimal", result instanceof BigDecimal);
        assertEquals(new BigDecimal("1.5E20"), result);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberBlankString() {
        NumberUtils.createNumber("   ");
    }

    // ==================================================
    // Tests for createBigDecimal
    // ==================================================

    @Test
    public void testCreateBigDecimalNull() {
        assertNull(NumberUtils.createBigDecimal(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalEmpty() {
        NumberUtils.createBigDecimal("");
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalBlank() {
        NumberUtils.createBigDecimal("   ");
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalDoubleMinus() {
        NumberUtils.createBigDecimal("--1.0");
    }

    @Test
    public void testCreateBigDecimalValid() {
        BigDecimal expected = new BigDecimal("1.5E20");
        assertEquals(expected, NumberUtils.createBigDecimal("1.5E20"));
    }

    // ==================================================
    // Tests for createBigInteger
    // ==================================================

    @Test
    public void testCreateBigIntegerNull() {
        assertNull(NumberUtils.createBigInteger(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigIntegerEmpty() {
        NumberUtils.createBigInteger("");
    }

    @Test
    public void testCreateBigIntegerValid() {
        BigInteger expected = new BigInteger("12345678901234567890");
        assertEquals(expected, NumberUtils.createBigInteger("12345678901234567890"));
    }

    @Test
    public void testCreateBigIntegerHex() {
        BigInteger expected = new BigInteger("FF", 16);
        assertEquals(expected, NumberUtils.createBigInteger("0xFF"));
    }

    @Test
    public void testCreateBigIntegerOctal() {
        BigInteger expected = new BigInteger("10", 8);
        assertEquals(expected, NumberUtils.createBigInteger("010"));
    }

    @Test
    public void testCreateBigIntegerNegative() {
        BigInteger expected = new BigInteger("-123");
        assertEquals(expected, NumberUtils.createBigInteger("-123"));
    }

    // ==================================================
    // Tests for max (long, long, long)
    // ==================================================

    @Test
    public void testMaxLongAllDifferent() {
        assertEquals(3L, NumberUtils.max(1L, 2L, 3L));
    }

    @Test
    public void testMaxLongTwoEqual() {
        assertEquals(3L, NumberUtils.max(3L, 2L, 3L));
    }

    @Test
    public void testMaxLongAllEqual() {
        assertEquals(1L, NumberUtils.max(1L, 1L, 1L));
    }

    // ==================================================
    // Tests for max (long[])
    // ==================================================

    @Test
    public void testMaxLongArraySingleElement() {
        long[] array = {5L};
        assertEquals(5L, NumberUtils.max(array));
    }

    @Test
    public void testMaxLongArrayMultipleElements() {
        long[] array = {1L, 5L, 3L};
        assertEquals(5L, NumberUtils.max(array));
    }

    @Test
    public void testMaxLongArrayWithNegative() {
        long[] array = {-1L, -2L, -3L};
        assertEquals(-1L, NumberUtils.max(array));
    }

    // ==================================================
    // Tests for min (long, long, long)
    // ==================================================

    @Test
    public void testMinLongAllDifferent() {
        assertEquals(1L, NumberUtils.min(1L, 2L, 3L));
    }

    @Test
    public void testMinLongTwoEqual() {
        assertEquals(2L, NumberUtils.min(3L, 2L, 3L));
    }

    @Test
    public void testMinLongAllEqual() {
        assertEquals(5L, NumberUtils.min(5L, 5L, 5L));
    }

    // ==================================================
    // Tests for min (long[])
    // ==================================================

    @Test
    public void testMinLongArraySingleElement() {
        long[] array = {5L};
        assertEquals(5L, NumberUtils.min(array));
    }

    @Test
    public void testMinLongArrayMultipleElements() {
        long[] array = {5L, 1L, 3L};
        assertEquals(1L, NumberUtils.min(array));
    }

    @Test
    public void testMinLongArrayWithNegative() {
        long[] array = {-1L, -2L, -3L};
        assertEquals(-3L, NumberUtils.min(array));
    }

    // ==================================================
    // Tests for toLong (String)
    // ==================================================

    @Test
    public void testToLongNull() {
        assertEquals(0L, NumberUtils.toLong(null));
    }

    @Test
    public void testToLongEmpty() {
        assertEquals(0L, NumberUtils.toLong(""));
    }

    @Test
    public void testToLongValid() {
        assertEquals(1L, NumberUtils.toLong("1"));
    }

    // ==================================================
    // Tests for toLong (String, long)
    // ==================================================

    @Test
    public void testToLongNullDefault() {
        assertEquals(10L, NumberUtils.toLong(null, 10L));
    }

    @Test
    public void testToLongEmptyDefault() {
        assertEquals(10L, NumberUtils.toLong("", 10L));
    }

    @Test
    public void testToLongValidDefault() {
        assertEquals(15L, NumberUtils.toLong("15", 10L));
    }

    @Test
    public void testToLongInvalidDefault() {
        assertEquals(10L, NumberUtils.toLong("abc", 10L));
    }

    // ==================================================
    // Tests for max (short, short, short)
    // ==================================================

    @Test
    public void testMaxShortAllDifferent() {
        assertEquals((short) 3, NumberUtils.max((short) 1, (short) 2, (short) 3));
    }

    @Test
    public void testMaxShortTwoEqual() {
        assertEquals((short) 3, NumberUtils.max((short) 3, (short) 2, (short) 3));
    }

    @Test
    public void testMaxShortAllEqual() {
        assertEquals((short) 1, NumberUtils.max((short) 1, (short) 1, (short) 1));
    }

    // ==================================================
    // Tests for max (short[])
    // ==================================================

    @Test
    public void testMaxShortArraySingleElement() {
        short[] array = {5};
        assertEquals(5, NumberUtils.max(array));
    }

    @Test
    public void testMaxShortArrayMultipleElements() {
        short[] array = {1, 5, 3};
        assertEquals(5, NumberUtils.max(array));
    }

    @Test
    public void testMaxShortArrayWithNegative() {
        short[] array = {-1, -2, -3};
        assertEquals(-1, NumberUtils.max(array));
    }

    // ==================================================
    // Tests for min (short, short, short)
    // ==================================================

    @Test
    public void testMinShortAllDifferent() {
        assertEquals((short) 1, NumberUtils.min((short) 1, (short) 2, (short) 3));
    }

    @Test
    public void testMinShortTwoEqual() {
        assertEquals((short) 2, NumberUtils.min((short) 3, (short) 2, (short) 3));
    }

    @Test
    public void testMinShortAllEqual() {
        assertEquals((short) 5, NumberUtils.min((short) 5, (short) 5, (short) 5));
    }

    // ==================================================
    // Tests for min (short[])
    // ==================================================

    @Test
    public void testMinShortArraySingleElement() {
        short[] array = {5};
        assertEquals(5, NumberUtils.min(array));
    }

    @Test
    public void testMinShortArrayMultipleElements() {
        short[] array = {5, 1, 3};
        assertEquals(1, NumberUtils.min(array));
    }

    @Test
    public void testMinShortArrayWithNegative() {
        short[] array = {-1, -2, -3};
        assertEquals(-3, NumberUtils.min(array));
    }

    // ==================================================
    // Tests for toShort (String)
    // ==================================================

    @Test
    public void testToShortNull() {
        assertEquals(0, NumberUtils.toShort(null));
    }

    @Test
    public void testToShortEmpty() {
        assertEquals(0, NumberUtils.toShort(""));
    }

    @Test
    public void testToShortValid() {
        assertEquals(1, NumberUtils.toShort("1"));
    }

    // ==================================================
    // Tests for toShort (String, short)
    // ==================================================

    @Test
    public void testToShortNullDefault() {
        assertEquals((short) 10, NumberUtils.toShort(null, (short) 10));
    }

    @Test
    public void testToShortEmptyDefault() {
        assertEquals((short) 10, NumberUtils.toShort("", (short) 10));
    }

    @Test
    public void testToShortValidDefault() {
        assertEquals((short) 15, NumberUtils.toShort("15", (short) 10));
    }

    @Test
    public void testToShortInvalidDefault() {
        assertEquals((short) 10, NumberUtils.toShort("abc", (short) 10));
    }
}
