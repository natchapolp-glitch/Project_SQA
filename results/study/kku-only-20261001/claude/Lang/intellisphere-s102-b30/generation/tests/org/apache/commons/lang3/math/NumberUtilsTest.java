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

    

    

    

    // ========== toByte Tests ==========

    

    

    

    

    

    

    

    

    // ========== toShort Tests ==========

    

    

    

    

    

    

    

    

    // ========== createFloat Tests ==========

    

    

    

    // ========== createDouble Tests ==========

    

    

    

    // ========== createInteger Tests ==========

    

    

    

    

    

    // ========== createLong Tests ==========

    

    

    

    

    // ========== createBigInteger Tests ==========

    

    

    

    

    

    // ========== createBigDecimal Tests ==========

    

    

    

    

    // ========== createNumber Tests ==========

    

    

    

    

    

    

    

    

    

    // ========== isDigits Tests ==========

    

    

    

    

    

    

    // ========== isNumber Tests ==========

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    // ========== min(long, long, long) Tests ==========

    

    

    

    

    // ========== max(long, long, long) Tests ==========

    

    

    

    

    // ========== min(int, int, int) Tests ==========

    

    

    

    // ========== max(int, int, int) Tests ==========

    

    

    

    // ========== min(short, short, short) Tests ==========

    

    

    // ========== max(short, short, short) Tests ==========

    

    // ========== min(byte, byte, byte) Tests ==========

    

    

    // ========== max(byte, byte, byte) Tests ==========

    

    // ========== min(double, double, double) Tests ==========

    

    

    

    // ========== max(double, double, double) Tests ==========

    

    

    

    // ========== min(float, float, float) Tests ==========

    

    

    // ========== max(float, float, float) Tests ==========

    

    

    // ========== min(long[]) Tests ==========

    

    

    

    

    // ========== max(long[]) Tests ==========

    

    

    

    // ========== min(int[]) Tests ==========

    

    

    

    // ========== max(int[]) Tests ==========

    

    

    

    // ========== min(short[]) Tests ==========

    

    

    

    // ========== max(short[]) Tests ==========

    

    

    // ========== min(byte[]) Tests ==========

    

    

    

    // ========== max(byte[]) Tests ==========

    

    

    // ========== min(double[]) Tests ==========

    

    

    

    

    // ========== max(double[]) Tests ==========

    

    

    
}
