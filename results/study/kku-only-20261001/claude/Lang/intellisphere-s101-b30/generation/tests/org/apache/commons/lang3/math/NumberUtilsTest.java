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

    

    

    // ========== isDigits Tests ==========

    

    

    

    

    // ========== isNumber Tests ==========

    

    

    

    

    

    

    

    

    // ========== isAllZeros Tests ==========

    

    

    

    

    // ========== createInteger Tests ==========

    

    

    

    

    

    // ========== createLong Tests ==========

    

    

    

    

    

    // ========== createFloat Tests ==========

    

    

    

    // ========== createDouble Tests ==========

    

    

    

    // ========== createBigInteger Tests ==========

    

    

    

    

    

    // ========== createBigDecimal Tests ==========

    

    

    

    

    

    // ========== createNumber Tests ==========

    

    

    

    

    

    

    

    

    

    

    // ========== min(long, long, long) Tests ==========

    

    

    // ========== min(int, int, int) Tests ==========

    

    

    // ========== min(short, short, short) Tests ==========

    

    // ========== min(byte, byte, byte) Tests ==========

    

    // ========== min(double, double, double) Tests ==========

    

    

    // ========== min(float, float, float) Tests ==========

    

    

    // ========== max(long, long, long) Tests ==========

    

    

    // ========== max(int, int, int) Tests ==========

    

    

    // ========== max(short, short, short) Tests ==========

    

    // ========== max(byte, byte, byte) Tests ==========

    

    // ========== max(double, double, double) Tests ==========

    

    

    // ========== max(float, float, float) Tests ==========

    

    

    // ========== min(long[]) Tests ==========

    

    

    

    

    // ========== min(int[]) Tests ==========

    

    

    

    

    // ========== min(short[]) Tests ==========

    

    

    

    // ========== min(byte[]) Tests ==========

    

    

    

    // ========== min(double[]) Tests ==========

    

    

    

    

    // ========== min(float[]) Tests ==========

    

    

    

    

    // ========== max(long[]) Tests ==========

    

    

    

    

    // ========== max(int[]) Tests ==========

    

    

    
}
