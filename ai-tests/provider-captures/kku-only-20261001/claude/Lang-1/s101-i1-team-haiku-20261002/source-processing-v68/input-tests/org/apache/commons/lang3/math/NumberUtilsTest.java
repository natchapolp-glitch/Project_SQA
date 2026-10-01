package org.apache.commons.lang3.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Regression tests for NumberUtils class.
 * Tests cover string parsing, min/max operations, number creation, and validation.
 */
public class NumberUtilsTest {

    // ========== toInt Tests ==========
    
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
        assertEquals(42, NumberUtils.toInt("42"));
        assertEquals(-100, NumberUtils.toInt("-100"));
    }

    @Test
    public void testToIntInvalid() {
        assertEquals(0, NumberUtils.toInt("abc"));
        assertEquals(0, NumberUtils.toInt("12.5"));
    }

    @Test
    public void testToIntWithDefault() {
        assertEquals(99, NumberUtils.toInt(null, 99));
        assertEquals(99, NumberUtils.toInt("", 99));
        assertEquals(42, NumberUtils.toInt("42", 99));
        assertEquals(99, NumberUtils.toInt("invalid", 99));
    }

    @Test
    public void testToIntBoundary() {
        assertEquals(Integer.MAX_VALUE, NumberUtils.toInt("2147483647"));
        assertEquals(Integer.MIN_VALUE, NumberUtils.toInt("-2147483648"));
    }

    // ========== toLong Tests ==========

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
        assertEquals(123456789L, NumberUtils.toLong("123456789"));
        assertEquals(-500L, NumberUtils.toLong("-500"));
    }

    @Test
    public void testToLongInvalid() {
        assertEquals(0L, NumberUtils.toLong("xyz"));
        assertEquals(0L, NumberUtils.toLong("45.6"));
    }

    @Test
    public void testToLongWithDefault() {
        assertEquals(88L, NumberUtils.toLong(null, 88L));
        assertEquals(88L, NumberUtils.toLong("", 88L));
        assertEquals(999L, NumberUtils.toLong("999", 88L));
        assertEquals(88L, NumberUtils.toLong("notanumber", 88L));
    }

    @Test
    public void testToLongBoundary() {
        assertEquals(Long.MAX_VALUE, NumberUtils.toLong("9223372036854775807"));
        assertEquals(Long.MIN_VALUE, NumberUtils.toLong("-9223372036854775808"));
    }

    // ========== toByte Tests ==========

    @Test
    public void testToByteNull() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
    }

    @Test
    public void testToByteEmpty() {
        assertEquals((byte) 0, NumberUtils.toByte(""));
    }

    @Test
    public void testToByteValid() {
        assertEquals((byte) 1, NumberUtils.toByte("1"));
        assertEquals((byte) 127, NumberUtils.toByte("127"));
        assertEquals((byte) -128, NumberUtils.toByte("-128"));
    }

    @Test
    public void testToByteInvalid() {
        assertEquals((byte) 0, NumberUtils.toByte("abc"));
        assertEquals((byte) 0, NumberUtils.toByte("256")); // overflow
    }

    @Test
    public void testToByteWithDefault() {
        assertEquals((byte) 55, NumberUtils.toByte(null, (byte) 55));
        assertEquals((byte) 55, NumberUtils.toByte("", (byte) 55));
        assertEquals((byte) 42, NumberUtils.toByte("42", (byte) 55));
        assertEquals((byte) 55, NumberUtils.toByte("invalid", (byte) 55));
    }

    // ========== toShort Tests ==========

    @Test
    public void testToShortNull() {
        assertEquals((short) 0, NumberUtils.toShort(null));
    }

    @Test
    public void testToShortEmpty() {
        assertEquals((short) 0, NumberUtils.toShort(""));
    }

    @Test
    public void testToShortValid() {
        assertEquals((short) 1, NumberUtils.toShort("1"));
        assertEquals((short) 32767, NumberUtils.toShort("32767"));
        assertEquals((short) -32768, NumberUtils.toShort("-32768"));
    }

    @Test
    public void testToShortInvalid() {
        assertEquals((short) 0, NumberUtils.toShort("xyz"));
        assertEquals((short) 0, NumberUtils.toShort("40000")); // overflow
    }

    @Test
    public void testToShortWithDefault() {
        assertEquals((short) 77, NumberUtils.toShort(null, (short) 77));
        assertEquals((short) 77, NumberUtils.toShort("", (short) 77));
        assertEquals((short) 100, NumberUtils.toShort("100", (short) 77));
        assertEquals((short) 77, NumberUtils.toShort("bad", (short) 77));
    }

    // ========== toFloat Tests ==========

    @Test
    public void testToFloatNull() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0f);
    }

    @Test
    public void testToFloatEmpty() {
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.0f);
    }

    @Test
    public void testToFloatValid() {
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0f);
        assertEquals(0.1f, NumberUtils.toFloat("0.1"), 0.01f);
        assertEquals(-99.9f, NumberUtils.toFloat("-99.9"), 0.01f);
    }

    @Test
    public void testToFloatInvalid() {
        assertEquals(0.0f, NumberUtils.toFloat("notafloat"), 0.0f);
    }

    @Test
    public void testToFloatWithDefault() {
        assertEquals(5.5f, NumberUtils.toFloat(null, 5.5f), 0.0f);
        assertEquals(5.5f, NumberUtils.toFloat("", 5.5f), 0.0f);
        assertEquals(2.2f, NumberUtils.toFloat("2.2", 5.5f), 0.01f);
        assertEquals(5.5f, NumberUtils.toFloat("invalid", 5.5f), 0.0f);
    }

    // ========== toDouble Tests ==========

    @Test
    public void testToDoubleNull() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0d);
    }

    @Test
    public void testToDoubleEmpty() {
        assertEquals(0.0d, NumberUtils.toDouble(""), 0.0d);
    }

    @Test
    public void testToDoubleValid() {
        assertEquals(1.5d, NumberUtils.toDouble("1.5"), 0.0d);
        assertEquals(0.001d, NumberUtils.toDouble("0.001"), 0.0001d);
        assertEquals(-123.456d, NumberUtils.toDouble("-123.456"), 0.001d);
    }

    @Test
    public void testToDoubleInvalid() {
        assertEquals(0.0d, NumberUtils.toDouble("notadouble"), 0.0d);
    }

    @Test
    public void testToDoubleWithDefault() {
        assertEquals(9.9d, NumberUtils.toDouble(null, 9.9d), 0.0d);
        assertEquals(9.9d, NumberUtils.toDouble("", 9.9d), 0.0d);
        assertEquals(3.3d, NumberUtils.toDouble("3.3", 9.9d), 0.01d);
        assertEquals(9.9d, NumberUtils.toDouble("bad", 9.9d), 0.0d);
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
    public void testIsDigitsValid() {
        assertTrue(NumberUtils.isDigits("123"));
        assertTrue(NumberUtils.isDigits("0"));
        assertTrue(NumberUtils.isDigits("9876543210"));
    }

    @Test
    public void testIsDigitsInvalid() {
        assertFalse(NumberUtils.isDigits("-123"));
        assertFalse(NumberUtils.isDigits("12.5"));
        assertFalse(NumberUtils.isDigits("12a"));
        assertFalse(NumberUtils.isDigits("abc"));
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
    public void testIsNumberValidIntegers() {
        assertTrue(NumberUtils.isNumber("123"));
        assertTrue(NumberUtils.isNumber("-456"));
        assertTrue(NumberUtils.isNumber("+789"));
        assertTrue(NumberUtils.isNumber("0"));
    }

    @Test
    public void testIsNumberValidDecimals() {
        assertTrue(NumberUtils.isNumber("12.5"));
        assertTrue(NumberUtils.isNumber("-3.14"));
        assertTrue(NumberUtils.isNumber(".5"));
        assertTrue(NumberUtils.isNumber("5."));
    }

    @Test
    public void testIsNumberValidScientific() {
        assertTrue(NumberUtils.isNumber("1.5E+10"));
        assertTrue(NumberUtils.isNumber("1E-5"));
        assertTrue(NumberUtils.isNumber("2e3"));
        assertTrue(NumberUtils.isNumber("-1.2E+5"));
    }

    @Test
    public void testIsNumberValidWithTypeQualifiers() {
        assertTrue(NumberUtils.isNumber("123L"));
        assertTrue(NumberUtils.isNumber("45.6f"));
        assertTrue(NumberUtils.isNumber("7.89d"));
        assertTrue(NumberUtils.isNumber("123l"));
        assertTrue(NumberUtils.isNumber("45.6F"));
        assertTrue(NumberUtils.isNumber("7.89D"));
    }

    @Test
    public void testIsNumberValidHex() {
        assertTrue(NumberUtils.isNumber("0x123"));
        assertTrue(NumberUtils.isNumber("-0x456"));
        assertTrue(NumberUtils.isNumber("0xABC"));
        assertTrue(NumberUtils.isNumber("0xfFf"));
    }

    @Test
    public void testIsNumberInvalid() {
        assertFalse(NumberUtils.isNumber("abc"));
        assertFalse(NumberUtils.isNumber("12.5.6"));
        assertFalse(NumberUtils.isNumber("1E"));
        assertFalse(NumberUtils.isNumber("E5"));
        assertFalse(NumberUtils.isNumber("0x"));
    }

    // ========== isAllZeros Tests ==========

    @Test
    public void testIsAllZerosNull() {
        assertTrue(NumberUtils.isAllZeros(null));
    }

    @Test
    public void testIsAllZerosValidAllZeros() {
        assertTrue(NumberUtils.isAllZeros("0"));
        assertTrue(NumberUtils.isAllZeros("000"));
        assertTrue(NumberUtils.isAllZeros("0000000"));
    }

    @Test
    public void testIsAllZerosValidNotAllZeros() {
        assertFalse(NumberUtils.isAllZeros("1"));
        assertFalse(NumberUtils.isAllZeros("100"));
        assertFalse(NumberUtils.isAllZeros("001"));
    }

    @Test
    public void testIsAllZerosEmpty() {
        assertFalse(NumberUtils.isAllZeros(""));
    }

    // ========== createInteger Tests ==========

    @Test
    public void testCreateIntegerNull() {
        assertNull(NumberUtils.createInteger(null));
    }

    @Test
    public void testCreateIntegerValid() {
        assertEquals(Integer.valueOf(123), NumberUtils.createInteger("123"));
        assertEquals(Integer.valueOf(-456), NumberUtils.createInteger("-456"));
    }

    @Test
    public void testCreateIntegerHex() {
        assertEquals(Integer.valueOf(0xABC), NumberUtils.createInteger("0xABC"));
        assertEquals(Integer.valueOf(0x123), NumberUtils.createInteger("0x123"));
    }

    @Test
    public void testCreateIntegerOctal() {
        assertEquals(Integer.valueOf(010), NumberUtils.createInteger("010"));
        assertEquals(Integer.valueOf(077), NumberUtils.createInteger("077"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateIntegerInvalid() {
        NumberUtils.createInteger("notanumber");
    }

    // ========== createLong Tests ==========

    @Test
    public void testCreateLongNull() {
        assertNull(NumberUtils.createLong(null));
    }

    @Test
    public void testCreateLongValid() {
        assertEquals(Long.valueOf(999L), NumberUtils.createLong("999"));
        assertEquals(Long.valueOf(-777L), NumberUtils.createLong("-777"));
    }

    @Test
    public void testCreateLongHex() {
        assertEquals(Long.valueOf(0x123L), NumberUtils.createLong("0x123"));
    }

    @Test
    public void testCreateLongOctal() {
        assertEquals(Long.valueOf(0755L), NumberUtils.createLong("0755"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateLongInvalid() {
        NumberUtils.createLong("badnumber");
    }

    // ========== createFloat Tests ==========

    @Test
    public void testCreateFloatNull() {
        assertNull(NumberUtils.createFloat(null));
    }

    @Test
    public void testCreateFloatValid() {
        assertEquals(Float.valueOf(1.5f), NumberUtils.createFloat("1.5"));
        assertEquals(Float.valueOf(-2.5f), NumberUtils.createFloat("-2.5"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateFloatInvalid() {
        NumberUtils.createFloat("notfloat");
    }

    // ========== createDouble Tests ==========

    @Test
    public void testCreateDoubleNull() {
        assertNull(NumberUtils.createDouble(null));
    }

    @Test
    public void testCreateDoubleValid() {
        assertEquals(Double.valueOf(3.14d), NumberUtils.createDouble("3.14"));
        assertEquals(Double.valueOf(-99.99d), NumberUtils.createDouble("-99.99"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateDoubleInvalid() {
        NumberUtils.createDouble("notdouble");
    }

    // ========== createBigInteger Tests ==========

    @Test
    public void testCreateBigIntegerNull() {
        assertNull(NumberUtils.createBigInteger(null));
    }

    @Test
    public void testCreateBigIntegerValid() {
        assertEquals(new BigInteger("123456789"), NumberUtils.createBigInteger("123456789"));
        assertEquals(new BigInteger("-987654321"), NumberUtils.createBigInteger("-987654321"));
    }

    @Test
    public void testCreateBigIntegerHex() {
        assertEquals(new BigInteger("ABC", 16), NumberUtils.createBigInteger("0xABC"));
        assertEquals(new BigInteger("123", 16), NumberUtils.createBigInteger("#123"));
    }

    @Test
    public void testCreateBigIntegerOctal() {
        assertEquals(new BigInteger("123", 8), NumberUtils.createBigInteger("0123"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigIntegerInvalid() {
        NumberUtils.createBigInteger("notabigint");
    }

    // ========== createBigDecimal Tests ==========

    @Test
    public void testCreateBigDecimalNull() {
        assertNull(NumberUtils.createBigDecimal(null));
    }

    @Test
    public void testCreateBigDecimalValid() {
        assertEquals(new BigDecimal("123.456"), NumberUtils.createBigDecimal("123.456"));
        assertEquals(new BigDecimal("-99.99"), NumberUtils.createBigDecimal("-99.99"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalBlank() {
        NumberUtils.createBigDecimal("   ");
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalDoubleMinus() {
        NumberUtils.createBigDecimal("--123");
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimalInvalid() {
        NumberUtils.createBigDecimal("notbigdec");
    }

    // ========== createNumber Tests ==========

    @Test
    public void testCreateNumberNull() {
        assertNull(NumberUtils.createNumber(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberBlank() {
        NumberUtils.createNumber("   ");
    }

    @Test
    public void testCreateNumberInteger() {
        Number n = NumberUtils.createNumber("42");
        assertEquals(Integer.valueOf(42), n);
    }

    @Test
    public void testCreateNumberLong() {
        Number n = NumberUtils.createNumber("123456789012L");
        assertTrue(n instanceof Long);
    }

    @Test
    public void testCreateNumberFloat() {
        Number n = NumberUtils.createNumber("1.5f");
        assertTrue(n instanceof Float);
    }

    @Test
    public void testCreateNumberDouble() {
        Number n = NumberUtils.createNumber("1.5d");
        assertTrue(n instanceof Double);
    }

    @Test
    public void testCreateNumberHexInteger() {
        Number n = NumberUtils.createNumber("0xFF");
        assertEquals(Integer.valueOf(0xFF), n);
    }

    @Test
    public void testCreateNumberHexLong() {
        Number n = NumberUtils.createNumber("0xFFFFFFFF");
        assertTrue(n instanceof Long);
    }

    @Test
    public void testCreateNumberOctal() {
        Number n = NumberUtils.createNumber("077");
        assertEquals(Integer.valueOf(63), n);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalid() {
        NumberUtils.createNumber("invalid");
    }

    // ========== min(long, long, long) Tests ==========

    @Test
    public void testMinLongThreeValues() {
        assertEquals(1L, NumberUtils.min(5L, 1L, 3L));
        assertEquals(-100L, NumberUtils.min(0L, 50L, -100L));
        assertEquals(Long.MIN_VALUE, NumberUtils.min(0L, Long.MIN_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void testMinLongAllSame() {
        assertEquals(42L, NumberUtils.min(42L, 42L, 42L));
    }

    // ========== min(int, int, int) Tests ==========

    @Test
    public void testMinIntThreeValues() {
        assertEquals(1, NumberUtils.min(5, 1, 3));
        assertEquals(-50, NumberUtils.min(0, 100, -50));
        assertEquals(Integer.MIN_VALUE, NumberUtils.min(0, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testMinIntAllSame() {
        assertEquals(7, NumberUtils.min(7, 7, 7));
    }

    // ========== min(short, short, short) Tests ==========

    @Test
    public void testMinShortThreeValues() {
        assertEquals((short) 2, NumberUtils.min((short) 5, (short) 2, (short) 8));
        assertEquals(Short.MIN_VALUE, NumberUtils.min((short) 0, Short.MIN_VALUE, Short.MAX_VALUE));
    }

    // ========== min(byte, byte, byte) Tests ==========

    @Test
    public void testMinByteThreeValues() {
        assertEquals((byte) 3, NumberUtils.min((byte) 10, (byte) 3, (byte) 7));
        assertEquals(Byte.MIN_VALUE, NumberUtils.min((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE));
    }

    // ========== min(double, double, double) Tests ==========

    @Test
    public void testMinDoubleThreeValues() {
        assertEquals(1.5d, NumberUtils.min(5.0d, 1.5d, 3.0d), 0.0d);
        assertEquals(-99.9d, NumberUtils.min(0.0d, 50.5d, -99.9d), 0.01d);
    }

    @Test
    public void testMinDoubleWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(5.0d, Double.NaN, 3.0d)));
    }

    // ========== min(float, float, float) Tests ==========

    @Test
    public void testMinFloatThreeValues() {
        assertEquals(0.5f, NumberUtils.min(1.0f, 0.5f, 2.0f), 0.0f);
    }

    @Test
    public void testMinFloatWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(1.0f, Float.NaN, 2.0f)));
    }

    // ========== max(long, long, long) Tests ==========

    @Test
    public void testMaxLongThreeValues() {
        assertEquals(5L, NumberUtils.max(5L, 1L, 3L));
        assertEquals(100L, NumberUtils.max(0L, 50L, 100L));
        assertEquals(Long.MAX_VALUE, NumberUtils.max(0L, Long.MIN_VALUE, Long.MAX_VALUE));
    }

    @Test
    public void testMaxLongAllSame() {
        assertEquals(99L, NumberUtils.max(99L, 99L, 99L));
    }

    // ========== max(int, int, int) Tests ==========

    @Test
    public void testMaxIntThreeValues() {
        assertEquals(5, NumberUtils.max(5, 1, 3));
        assertEquals(100, NumberUtils.max(0, 100, -50));
        assertEquals(Integer.MAX_VALUE, NumberUtils.max(0, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testMaxIntAllSame() {
        assertEquals(15, NumberUtils.max(15, 15, 15));
    }

    // ========== max(short, short, short) Tests ==========

    @Test
    public void testMaxShortThreeValues() {
        assertEquals((short) 8, NumberUtils.max((short) 5, (short) 2, (short) 8));
        assertEquals(Short.MAX_VALUE, NumberUtils.max((short) 0, Short.MIN_VALUE, Short.MAX_VALUE));
    }

    // ========== max(byte, byte, byte) Tests ==========

    @Test
    public void testMaxByteThreeValues() {
        assertEquals((byte) 10, NumberUtils.max((byte) 10, (byte) 3, (byte) 7));
        assertEquals(Byte.MAX_VALUE, NumberUtils.max((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE));
    }

    // ========== max(double, double, double) Tests ==========

    @Test
    public void testMaxDoubleThreeValues() {
        assertEquals(5.0d, NumberUtils.max(5.0d, 1.5d, 3.0d), 0.0d);
        assertEquals(50.5d, NumberUtils.max(0.0d, 50.5d, -99.9d), 0.01d);
    }

    @Test
    public void testMaxDoubleWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.max(5.0d, Double.NaN, 3.0d)));
    }

    // ========== max(float, float, float) Tests ==========

    @Test
    public void testMaxFloatThreeValues() {
        assertEquals(2.0f, NumberUtils.max(1.0f, 0.5f, 2.0f), 0.0f);
    }

    @Test
    public void testMaxFloatWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.max(1.0f, Float.NaN, 2.0f)));
    }

    // ========== min(long[]) Tests ==========

    @Test
    public void testMinLongArray() {
        assertEquals(1L, NumberUtils.min(new long[] { 5L, 1L, 3L }));
        assertEquals(-100L, NumberUtils.min(new long[] { 0L, 50L, -100L }));
    }

    @Test
    public void testMinLongArraySingleElement() {
        assertEquals(42L, NumberUtils.min(new long[] { 42L }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArrayNull() {
        NumberUtils.min((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinLongArrayEmpty() {
        NumberUtils.min(new long[] {});
    }

    // ========== min(int[]) Tests ==========

    @Test
    public void testMinIntArray() {
        assertEquals(1, NumberUtils.min(new int[] { 5, 1, 3 }));
        assertEquals(-50, NumberUtils.min(new int[] { 0, 100, -50 }));
    }

    @Test
    public void testMinIntArraySingleElement() {
        assertEquals(7, NumberUtils.min(new int[] { 7 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinIntArrayNull() {
        NumberUtils.min((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinIntArrayEmpty() {
        NumberUtils.min(new int[] {});
    }

    // ========== min(short[]) Tests ==========

    @Test
    public void testMinShortArray() {
        assertEquals((short) 2, NumberUtils.min(new short[] { 5, 2, 8 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArrayNull() {
        NumberUtils.min((short[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinShortArrayEmpty() {
        NumberUtils.min(new short[] {});
    }

    // ========== min(byte[]) Tests ==========

    @Test
    public void testMinByteArray() {
        assertEquals((byte) 3, NumberUtils.min(new byte[] { 10, 3, 7 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinByteArrayNull() {
        NumberUtils.min((byte[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinByteArrayEmpty() {
        NumberUtils.min(new byte[] {});
    }

    // ========== min(double[]) Tests ==========

    @Test
    public void testMinDoubleArray() {
        assertEquals(1.5d, NumberUtils.min(new double[] { 5.0d, 1.5d, 3.0d }), 0.0d);
    }

    @Test
    public void testMinDoubleArrayWithNaN() {
        assertTrue(Double.isNaN(NumberUtils.min(new double[] { 5.0d, Double.NaN, 3.0d })));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinDoubleArrayNull() {
        NumberUtils.min((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinDoubleArrayEmpty() {
        NumberUtils.min(new double[] {});
    }

    // ========== min(float[]) Tests ==========

    @Test
    public void testMinFloatArray() {
        assertEquals(0.5f, NumberUtils.min(new float[] { 1.0f, 0.5f, 2.0f }), 0.0f);
    }

    @Test
    public void testMinFloatArrayWithNaN() {
        assertTrue(Float.isNaN(NumberUtils.min(new float[] { 1.0f, Float.NaN, 2.0f })));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinFloatArrayNull() {
        NumberUtils.min((float[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinFloatArrayEmpty() {
        NumberUtils.min(new float[] {});
    }

    // ========== max(long[]) Tests ==========

    @Test
    public void testMaxLongArray() {
        assertEquals(5L, NumberUtils.max(new long[] { 5L, 1L, 3L }));
        assertEquals(100L, NumberUtils.max(new long[] { 0L, 50L, 100L }));
    }

    @Test
    public void testMaxLongArraySingleElement() {
        assertEquals(99L, NumberUtils.max(new long[] { 99L }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArrayNull() {
        NumberUtils.max((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxLongArrayEmpty() {
        NumberUtils.max(new long[] {});
    }

    // ========== max(int[]) Tests ==========

    @Test
    public void testMaxIntArray() {
        assertEquals(5, NumberUtils.max(new int[] { 5, 1, 3 }));
        assertEquals(100, NumberUtils.max(new int[] { 0, 100, -50 }));
    }

    @Test
    public void testMaxIntArraySingleElement() {
        assertEquals(15, NumberUtils.max(new int[] { 15 }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArrayNull() {
        NumberUtils.max((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIntArrayEmpty()
