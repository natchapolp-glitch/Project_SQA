// org/apache/commons/lang3/math/NumberUtilsTest.java
package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Regression tests for NumberUtils class covering conversion methods,
 * validation methods, and min/max operations on primitive types and arrays.
 */
public class NumberUtilsTest {

    // ========== toInt Tests ==========

    @Test
    public void testToIntNullReturnsZero() {
        assertEquals(0, NumberUtils.toInt(null));
    }

    @Test
    public void testToIntEmptyStringReturnsZero() {
        assertEquals(0, NumberUtils.toInt(""));
    }

    @Test
    public void testToIntValidPositive() {
        assertEquals(42, NumberUtils.toInt("42"));
    }

    @Test
    public void testToIntValidNegative() {
        assertEquals(-42, NumberUtils.toInt("-42"));
    }

    @Test
    public void testToIntInvalidStringReturnsZero() {
        assertEquals(0, NumberUtils.toInt("not_a_number"));
    }

    @Test
    public void testToIntWithDefaultNullReturnsDefault() {
        assertEquals(99, NumberUtils.toInt(null, 99));
    }

    @Test
    public void testToIntWithDefaultEmptyStringReturnsDefault() {
        assertEquals(99, NumberUtils.toInt("", 99));
    }

    @Test
    public void testToIntWithDefaultValidString() {
        assertEquals(42, NumberUtils.toInt("42", 99));
    }

    @Test
    public void testToIntWithDefaultInvalidStringReturnsDefault() {
        assertEquals(99, NumberUtils.toInt("invalid", 99));
    }

    // ========== toLong Tests ==========

    @Test
    public void testToLongNullReturnsZero() {
        assertEquals(0L, NumberUtils.toLong(null));
    }

    @Test
    public void testToLongEmptyStringReturnsZero() {
        assertEquals(0L, NumberUtils.toLong(""));
    }

    @Test
    public void testToLongValidPositive() {
        assertEquals(123456789L, NumberUtils.toLong("123456789"));
    }

    @Test
    public void testToLongValidNegative() {
        assertEquals(-123456789L, NumberUtils.toLong("-123456789"));
    }

    @Test
    public void testToLongInvalidStringReturnsZero() {
        assertEquals(0L, NumberUtils.toLong("not_a_long"));
    }

    @Test
    public void testToLongWithDefaultNullReturnsDefault() {
        assertEquals(99L, NumberUtils.toLong(null, 99L));
    }

    @Test
    public void testToLongWithDefaultValidString() {
        assertEquals(123L, NumberUtils.toLong("123", 99L));
    }

    @Test
    public void testToLongWithDefaultInvalidStringReturnsDefault() {
        assertEquals(99L, NumberUtils.toLong("invalid", 99L));
    }

    // ========== toFloat Tests ==========

    @Test
    public void testToFloatNullReturnsZero() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0f);
    }

    @Test
    public void testToFloatEmptyStringReturnsZero() {
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.0f);
    }

    @Test
    public void testToFloatValidPositive() {
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0f);
    }

    @Test
    public void testToFloatValidNegative() {
        assertEquals(-1.5f, NumberUtils.toFloat("-1.5"), 0.0f);
    }

    @Test
    public void testToFloatInvalidStringReturnsZero() {
        assertEquals(0.0f, NumberUtils.toFloat("not_a_float"), 0.0f);
    }

    @Test
    public void testToFloatWithDefaultNullReturnsDefault() {
        assertEquals(1.1f, NumberUtils.toFloat(null, 1.1f), 0.0f);
    }

    @Test
    public void testToFloatWithDefaultValidString() {
        assertEquals(1.5f, NumberUtils.toFloat("1.5", 0.0f), 0.0f);
    }

    @Test
    public void testToFloatWithDefaultInvalidStringReturnsDefault() {
        assertEquals(1.1f, NumberUtils.toFloat("invalid", 1.1f), 0.0f);
    }

    // ========== toDouble Tests ==========

    @Test
    public void testToDoubleNullReturnsZero() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0d);
    }

    @Test
    public void testToDoubleEmptyStringReturnsZero() {
        assertEquals(0.0d, NumberUtils.toDouble(""), 0.0d);
    }

    @Test
    public void testToDoubleValidPositive() {
        assertEquals(1.5d, NumberUtils.toDouble("1.5"), 0.0d);
    }

    @Test
    public void testToDoubleValidNegative() {
        assertEquals(-1.5d, NumberUtils.toDouble("-1.5"), 0.0d);
    }

    @Test
    public void testToDoubleInvalidStringReturnsZero() {
        assertEquals(0.0d, NumberUtils.toDouble("not_a_double"), 0.0d);
    }

    @Test
    public void testToDoubleWithDefaultNullReturnsDefault() {
        assertEquals(1.1d, NumberUtils.toDouble(null, 1.1d), 0.0d);
    }

    @Test
    public void testToDoubleWithDefaultValidString() {
        assertEquals(1.5d, NumberUtils.toDouble("1.5", 0.0d), 0.0d);
    }

    @Test
    public void testToDoubleWithDefaultInvalidStringReturnsDefault() {
        assertEquals(1.1d, NumberUtils.toDouble("invalid", 1.1d), 0.0d);
    }

    // ========== toByte Tests ==========

    @Test
    public void testToByteNullReturnsZero() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
    }

    @Test
    public void testToByteEmptyStringReturnsZero() {
        assertEquals((byte) 0, NumberUtils.toByte(""));
    }

    @Test
    public void testToByteValidPositive() {
        assertEquals((byte) 42, NumberUtils.toByte("42"));
    }

    @Test
    public void testToByteValidNegative() {
        assertEquals((byte) -42, NumberUtils.toByte("-42"));
    }

    @Test
    public void testToByteInvalidStringReturnsZero() {
        assertEquals((byte) 0, NumberUtils.toByte("not_a_byte"));
    }

    @Test
    public void testToByteWithDefaultNullReturnsDefault() {
        assertEquals((byte) 99, NumberUtils.toByte(null, (byte) 99));
    }

    @Test
    public void testToByteWithDefaultValidString() {
        assertEquals((byte) 42, NumberUtils.toByte("42", (byte) 99));
    }

    @Test
    public void testToByteWithDefaultInvalidStringReturnsDefault() {
        assertEquals((byte) 99, NumberUtils.toByte("invalid", (byte) 99));
    }

    // ========== toShort Tests ==========

    @Test
    public void testToShortNullReturnsZero() {
        assertEquals((short) 0, NumberUtils.toShort(null));
    }

    @Test
    public void testToShortEmptyStringReturnsZero() {
        assertEquals((short) 0, NumberUtils.toShort(""));
    }

    @Test
    public void testToShortValidPositive() {
        assertEquals((short) 42, NumberUtils.toShort("42"));
    }

    @Test
    public void testToShortValidNegative() {
        assertEquals((short) -42, NumberUtils.toShort("-42"));
    }

    @Test
    public void testToShortInvalidStringReturnsZero() {
        assertEquals((short) 0, NumberUtils.toShort("not_a_short"));
    }

    @Test
    public void testToShortWithDefaultNullReturnsDefault() {
        assertEquals((short) 99, NumberUtils.toShort(null, (short) 99));
    }

    @Test
    public void testToShortWithDefaultValidString() {
        assertEquals((short) 42, NumberUtils.toShort("42", (short) 99));
    }

    @Test
    public void testToShortWithDefaultInvalidStringReturnsDefault() {
        assertEquals((short) 99, NumberUtils.toShort("invalid", (short) 99));
    }

    // ========== createFloat Tests ==========

    @Test
    public void testCreateFloatNullReturnsNull() {
        assertNull(NumberUtils.createFloat(null));
    }

    @Test
    public void testCreateFloatValidString() {
        assertEquals(Float.valueOf(1.5f), NumberUtils.createFloat("1.5"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateFloatInvalidStringThrowsException() {
        NumberUtils.createFloat("not_a_float");
    }

    // ========== createDouble Tests ==========

    @Test
    public void testCreateDoubleNullReturnsNull() {
        assertNull(NumberUtils.createDouble(null));
    }

    @Test
    public void testCreateDoubleValidString() {
        assertEquals(Double.valueOf(1.5d), NumberUtils.createDouble("1.5"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateDoubleInvalidStringThrowsException() {
        NumberUtils.createDouble("not_a_double");
    }

    // ========== createInteger Tests ==========

    @Test
    public void testCreateIntegerNullReturnsNull() {
        assertNull(NumberUtils.createInteger(null));
    }

    @Test
    public void testCreateIntegerDecimal() {
        assertEquals(Integer.valueOf(42), NumberUtils.createInteger("42"));
    }

    @Test
    public void testCreateIntegerHexadecimal() {
        assertEquals(Integer.valueOf(0xABCD), NumberUtils.createInteger("0xABCD"));
    }

    @Test
    public void testCreateIntegerOctal() {
        assertEquals(Integer.valueOf(0777), NumberUtils.createInteger("0777"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateIntegerInvalidStringThrowsException() {
        NumberUtils.createInteger("not_an_int");
    }

    // ========== createLong Tests ==========

    @Test
    public void testCreateLongNullReturnsNull() {
        assertNull(NumberUtils.createLong(null));
    }

    @Test
    public void testCreateLongDecimal() {
        assertEquals(Long.valueOf(123456789L), NumberUtils.createLong("123456789"));
    }

    @Test
    public void testCreateLongHexadecimal() {
        assertEquals(Long.valueOf(0xABCDEFL), NumberUtils.createLong("0xABCDEF"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateLongInvalidStringThrowsException() {
        NumberUtils.createLong("not_a_long");
    }

    // ========== createBigInteger Tests ==========

    @Test
    public void testCreateBigIntegerNullReturnsNull() {
        assertNull(NumberUtils.createBigInteger(null));
    }

    @Test
    public void testCreateBigIntegerDecimal() {
        assertEquals(new BigInteger("123456789"), NumberUtils.createBigInteger("123456789"));
    }

    @Test
    public void testCreateBigIntegerHexadecimal() {
        assertEquals(new BigInteger("ABCD", 16), NumberUtils.createBigInteger("0xABCD"));
    }

    @Test
    public void testCreateBigIntegerOctal() {
        assertEquals(new BigInteger("777", 8), NumberUtils.createBigInteger("0777"));
    }

    @Test
    public void testCreateBigIntegerNegativeHex() {
        BigInteger result = NumberUtils.createBigInteger("-0x100");
        assertEquals(new BigInteger("-256"), result);
    }

    // ========== createBigDecimal Tests ==========

    @Test
    public void testCreateBigDecimalNullReturnsNull() {
        assertNull(NumberUtils.createBigDecimal(null));
    }

    @Test
    public void testCreateBigDecimalValidString() {
        assertEquals(new BigDecimal("123.45"), NumberUtils.createBigDecimal("123.45"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalBlankStringThrowsException() {
        NumberUtils.createBigDecimal("   ");
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalDoubleNegativeThrowsException() {
        NumberUtils.createBigDecimal("--123");
    }

    // ========== createNumber Tests ==========

    @Test
    public void testCreateNumberNullReturnsNull() {
        assertNull(NumberUtils.createNumber(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberBlankStringThrowsException() {
        NumberUtils.createNumber("   ");
    }

    @Test
    public void testCreateNumberDecimalInteger() {
        assertEquals(Integer.valueOf(42), NumberUtils.createNumber("42"));
    }

    @Test
    public void testCreateNumberHexadecimal() {
        Number result = NumberUtils.createNumber("0xFF");
        assertEquals(Integer.valueOf(255), result);
    }

    @Test
    public void testCreateNumberWithLongSuffix() {
        Number result = NumberUtils.createNumber("123L");
        assertEquals(Long.valueOf(123L), result);
    }

    @Test
    public void testCreateNumberWithFloatSuffix() {
        Number result = NumberUtils.createNumber("1.5F");
        assertEquals(Float.valueOf(1.5f), result);
    }

    @Test
    public void testCreateNumberWithDoubleSuffix() {
        Number result = NumberUtils.createNumber("1.5D");
        assertEquals(Double.valueOf(1.5d), result);
    }

    @Test
    public void testCreateNumberScientificNotation() {
        Number result = NumberUtils.createNumber("1.5e2");
        assertTrue(result instanceof Double || result instanceof Float);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberJustHexPrefixThrowsException() {
        NumberUtils.createNumber("0x");
    }

    // ========== isDigits Tests ==========

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
        assertTrue(NumberUtils.isDigits("12345"));
    }

    @Test
    public void testIsDigitsWithNonDigit() {
        assertFalse(NumberUtils.isDigits("123a5"));
    }

    @Test
    public void testIsDigitsWithSpace() {
        assertFalse(NumberUtils.isDigits("123 45"));
    }

    @Test
    public void testIsDigitsSingleDigit() {
        assertTrue(NumberUtils.isDigits("0"));
    }

    // ========== isNumber Tests ==========

    @Test
    public void testIsNumberNull() {
        assertFalse(NumberUtils.isNumber(null));
    }

    @Test
    public void testIsNumberEmpty() {
        assertFalse(NumberUtils.isNumber(""));
    }

    @Test
    public void testIsNumberInteger() {
        assertTrue(NumberUtils.isNumber("123"));
    }

    @Test
    public void testIsNumberNegativeInteger() {
        assertTrue(NumberUtils.isNumber("-123"));
    }

    @Test
    public void testIsNumberDecimal() {
        assertTrue(NumberUtils.isNumber("123.45"));
    }

    @Test
    public void testIsNumberScientificNotation() {
        assertTrue(NumberUtils.isNumber("1.5e2"));
    }

    @Test
    public void testIsNumberWithFloatSuffix() {
        assertTrue(NumberUtils.isNumber("1.5f"));
    }

    @Test
    public void testIsNumberWithDoubleSuffix() {
        assertTrue(NumberUtils.isNumber("1.5d"));
    }

    @Test
    public void testIsNumberWithLongSuffix() {
        assertTrue(NumberUtils.isNumber("123l"));
    }

    @Test
    public void testIsNumberHexadecimal() {
        assertTrue(NumberUtils.isNumber("0xFF"));
    }

    @Test
    public void testIsNumberNegativeHexadecimal() {
        assertTrue(NumberUtils.isNumber("-0xFF"));
    }

    @Test
    public void testIsNumberTrailingDecimalPoint() {
        assertTrue(NumberUtils.isNumber("123."));
    }

    @Test
    public void testIsNumberDoubleDecimalPointInvalid() {
        assertFalse(NumberUtils.isNumber("1.2.3"));
    }

    @Test
    public void testIsNumberDoubleExponentInvalid() {
        assertFalse(NumberUtils.isNumber("1e2e3"));
    }

    @Test
    public void testIsNumberJustDecimalPointInvalid() {
        assertFalse(NumberUtils.isNumber("."));
    }

    @Test
    public void testIsNumberExponentWithoutDigitInvalid() {
        assertFalse(NumberUtils.isNumber("1e"));
    }

    @Test
    public void testIsNumberJustHexPrefixInvalid() {
        assertFalse(NumberUtils.isNumber("0x"));
    }

    @Test
    public void testIsNumberPositiveSign() {
        assertTrue(NumberUtils.isNumber("+123"));
    }

    // ========== min(long, long, long) Tests ==========

    @Test
    public void testMinLongThreeValues() {
        assertEquals(1L, NumberUtils.min(1L, 2L, 3L));
    }

    @Test
    public void testMinLongMiddleIsMin() {
        assertEquals(2L, NumberUtils.min(5L, 2L, 8L));
    }

    @Test
    public void testMinLongLastIsMin() {
        assertEquals(1L, NumberUtils.min(5L, 8L, 1L));
    }

    @Test
    public void testMinLongNegativeValues() {
        assertEquals(-5L, NumberUtils.min(-1L, -3L, -5L));
    }

    // ========== max(long, long, long) Tests ==========

    @Test
    public void testMaxLongThreeValues() {
        assertEquals(3L, NumberUtils.max(1L, 2L, 3L));
    }

    @Test
    public void testMaxLongMiddleIsMax() {
        assertEquals(8L, NumberUtils.max(5L, 8L, 2L));
    }

    @Test
    public void testMaxLongLastIsMax() {
        assertEquals(9L, NumberUtils.max(5L, 2L, 9L));
    }

    @Test
    public void testMaxLongNegativeValues() {
        assertEquals(-1L, NumberUtils.max(-1L, -3L, -5L));
    }

    // ========== min(int, int, int) Tests ==========

    @Test
    public void testMinIntThreeValues() {
        assertEquals(1, NumberUtils.min(1, 2, 3));
    }

    @Test
    public void testMinIntMiddleIsMin() {
        assertEquals(2, NumberUtils.min(5, 2, 8));
    }

    @Test
    public void testMinIntNegativeValues() {
        assertEquals(-5, NumberUtils.min(-1, -3, -5));
    }

    // ========== max(int, int, int) Tests ==========

    @Test
    public void testMaxIntThreeValues() {
        assertEquals(3, NumberUtils.max(1, 2, 3));
    }

    @Test
    public void testMaxIntMiddleIsMax() {
        assertEquals(8, NumberUtils.max(5, 8, 2));
    }

    @Test
    public void testMaxIntNegativeValues() {
        assertEquals(-1, NumberUtils.max(-1, -3, -5));
    }

    // ========== min(short, short, short) Tests ==========

    @Test
    public void testMinShortThreeValues() {
        assertEquals((short) 1, NumberUtils.min((short) 1, (short) 2, (short) 3));
    }

    @Test
    public void testMinShortNegativeValues() {
        assertEquals((short) -5, NumberUtils.min((short) -1, (short) -3, (short) -5));
    }

    // ========== max(short, short, short) Tests ==========

    @Test
    public void testMaxShortThreeValues() {
        assertEquals((short) 3, NumberUtils.max((short) 1, (short) 2, (short) 3));
    }

    // ========== min(byte, byte, byte) Tests ==========

    @Test
    public void testMinByteThreeValues() {
        assertEquals((byte) 1, NumberUtils.min((byte) 1, (byte) 2, (byte) 3));
    }

    @Test
    public void testMinByteNegativeValues() {
        assertEquals((byte) -5, NumberUtils.min((byte) -1, (byte) -3, (byte) -5));
    }

    // ========== max(byte, byte, byte) Tests ==========

    @Test
    public void testMaxByteThreeValues() {
        assertEquals((byte) 3, NumberUtils.max((byte) 1, (byte) 2, (byte) 3));
    }

    // ========== min(double, double, double) Tests ==========

    @Test
    public void testMinDoubleThreeValues() {
        assertEquals(1.0d, NumberUtils.min(1.0d, 2.0d, 3.0d), 0.0d);
    }

    @Test
    public void testMinDoubleWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(1.0d, Double.NaN, 3.0d)));
    }

    @Test
    public void testMinDoubleNegativeInfinity() {
        assertEquals(Double.NEGATIVE_INFINITY, 
            NumberUtils.min(1.0d, Double.NEGATIVE_INFINITY, 3.0d), 0.0d);
    }

    // ========== max(double, double, double) Tests ==========

    @Test
    public void testMaxDoubleThreeValues() {
        assertEquals(3.0d, NumberUtils.max(1.0d, 2.0d, 3.0d), 0.0d);
    }

    @Test
    public void testMaxDoubleWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.max(1.0d, Double.NaN, 3.0d)));
    }

    @Test
    public void testMaxDoublePositiveInfinity() {
        assertEquals(Double.POSITIVE_INFINITY, 
            NumberUtils.max(1.0d, Double.POSITIVE_INFINITY, 3.0d), 0.0d);
    }

    // ========== min(float, float, float) Tests ==========

    @Test
    public void testMinFloatThreeValues() {
        assertEquals(1.0f, NumberUtils.min(1.0f, 2.0f, 3.0f), 0.0f);
    }

    @Test
    public void testMinFloatWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(1.0f, Float.NaN, 3.0f)));
    }

    // ========== max(float, float, float) Tests ==========

    @Test
    public void testMaxFloatThreeValues() {
        assertEquals(3.0f, NumberUtils.max(1.0f, 2.0f, 3.0f), 0.0f);
    }

    @Test
    public void testMaxFloatWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.max(1.0f, Float.NaN, 3.0f)));
    }

    // ========== min(long[]) Tests ==========

    @Test
    public void testMinLongArray() {
        assertEquals(1L, NumberUtils.min(new long[]{5L, 2L, 8L, 1L, 9L}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArrayNullThrowsException() {
        NumberUtils.min((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArrayEmptyThrowsException() {
        NumberUtils.min(new long[]{});
    }

    @Test
    public void testMinLongArraySingleElement() {
        assertEquals(42L, NumberUtils.min(new long[]{42L}));
    }

    // ========== max(long[]) Tests ==========

    @Test
    public void testMaxLongArray() {
        assertEquals(9L, NumberUtils.max(new long[]{5L, 2L, 8L, 1L, 9L}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArrayNullThrowsException() {
        NumberUtils.max((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArrayEmptyThrowsException() {
        NumberUtils.max(new long[]{});
    }

    // ========== min(int[]) Tests ==========

    @Test
    public void testMinIntArray() {
        assertEquals(1, NumberUtils.min(new int[]{5, 2, 8, 1, 9}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinIntArrayNullThrowsException() {
        NumberUtils.min((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinIntArrayEmptyThrowsException() {
        NumberUtils.min(new int[]{});
    }

    // ========== max(int[]) Tests ==========

    @Test
    public void testMaxIntArray() {
        assertEquals(9, NumberUtils.max(new int[]{5, 2, 8, 1, 9}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArrayNullThrowsException() {
        NumberUtils.max((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArrayEmptyThrowsException() {
        NumberUtils.max(new int[]{});
    }

    // ========== min(short[]) Tests ==========

    @Test
    public void testMinShortArray() {
        assertEquals((short) 1, 
            NumberUtils.min(new short[]{5, 2, 8, 1, 9}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArrayNullThrowsException() {
        NumberUtils.min((short[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArrayEmptyThrowsException() {
        NumberUtils.min(new short[]{});
    }

    // ========== max(short[]) Tests ==========

    @Test
    public void testMaxShortArray() {
        assertEquals((short) 9, 
            NumberUtils.max(new short[]{5, 2, 8, 1, 9}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxShortArrayNullThrowsException() {
        NumberUtils.max((short[]) null);
    }

    // ========== min(byte[]) Tests ==========

    @Test
    public void testMinByteArray() {
        assertEquals((byte) 1, 
            NumberUtils.min(new byte[]{5, 2, 8, 1, 9}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinByteArrayNullThrowsException() {
        NumberUtils.min((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinByteArrayEmptyThrowsException() {
        NumberUtils.min(new byte[]{});
    }

    // ========== max(byte[]) Tests ==========

    @Test
    public void testMaxByteArray() {
        assertEquals((byte) 9, 
            NumberUtils.max(new byte[]{5, 2, 8, 1, 9}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxByteArrayNullThrowsException() {
        NumberUtils.max((byte[]) null);
    }

    // ========== min(double[]) Tests ==========

    @Test
    public void testMinDoubleArray() {
        assertEquals(1.0d, 
            NumberUtils.min(new double[]{5.0d, 2.0d, 8.0d, 1.0d, 9.0d}), 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinDoubleArrayNullThrowsException() {
        NumberUtils.min((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinDoubleArrayEmptyThrowsException() {
        NumberUtils.min(new double[]{});
    }

    @Test
    public void testMinDoubleArrayWithNaN() {
        assertTrue(Double.isNaN(
            NumberUtils.min(new double[]{5.0d, Double.NaN, 1.0d})));
    }

    // ========== max(double[]) Tests ==========

    @Test
    public void testMaxDoubleArray() {
        assertEquals(9.0d, 
            NumberUtils.max(new double[]{5.0d, 2.0d, 8.0d, 1.0d, 9.0d}), 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxDoubleArrayNullThrowsException() {
        NumberUtils.max((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxDoubleArrayEmptyThrowsException() {
        NumberUtils.max(new double[]{});
    }
}
