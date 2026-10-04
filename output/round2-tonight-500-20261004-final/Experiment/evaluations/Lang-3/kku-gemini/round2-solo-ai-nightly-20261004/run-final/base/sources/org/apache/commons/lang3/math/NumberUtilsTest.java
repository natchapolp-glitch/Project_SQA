package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void testToInt() {
        assertEquals(0, NumberUtils.toInt(null));
        assertEquals(0, NumberUtils.toInt(""));
        assertEquals(123, NumberUtils.toInt("123"));
        assertEquals(42, NumberUtils.toInt("invalid", 42));
    }

    @Test
    public void testToLong() {
        assertEquals(0L, NumberUtils.toLong(null));
        assertEquals(0L, NumberUtils.toLong(""));
        assertEquals(123456789L, NumberUtils.toLong("123456789"));
        assertEquals(99L, NumberUtils.toLong("invalid", 99L));
    }

    @Test
    public void testToFloat() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0001f);
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.0001f);
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0001f);
        assertEquals(2.5f, NumberUtils.toFloat("invalid", 2.5f), 0.0001f);
    }

    @Test
    public void testToDouble() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0001d);
        assertEquals(0.0d, NumberUtils.toDouble(""), 0.0001d);
        assertEquals(2.5d, NumberUtils.toDouble("2.5"), 0.0001d);
        assertEquals(3.5d, NumberUtils.toDouble("invalid", 3.5d), 0.0001d);
    }

    @Test
    public void testCreateNumber() {
        assertEquals(Integer.valueOf(123), NumberUtils.createNumber("123"));
        assertEquals(Long.valueOf(123L), NumberUtils.createNumber("123L"));
        assertEquals(Float.valueOf(1.5f), NumberUtils.createNumber("1.5f"));
        assertEquals(Double.valueOf(2.5d), NumberUtils.createNumber("2.5d"));
        assertEquals(new BigInteger("12345678901234567890"), NumberUtils.createNumber("12345678901234567890"));
        assertEquals(new BigDecimal("123.456"), NumberUtils.createNumber("123.456"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalid() {
        NumberUtils.createNumber("invalid");
    }

    @Test
    public void testIsNumber() {
        assertTrue(NumberUtils.isNumber("123"));
        assertTrue(NumberUtils.isNumber("123.45"));
        assertTrue(NumberUtils.isNumber("0xFF"));
        assertTrue(NumberUtils.isNumber("1e10"));
        assertFalse(NumberUtils.isNumber(null));
        assertFalse(NumberUtils.isNumber(""));
        assertFalse(NumberUtils.isNumber("123a"));
    }

    @Test
    public void testMinMaxArray() {
        int[] intArray = {1, 5, 3, 9, 2};
        assertEquals(1, NumberUtils.min(intArray));
        assertEquals(9, NumberUtils.max(intArray));

        long[] longArray = {10L, 50L, 30L};
        assertEquals(10L, NumberUtils.min(longArray));
        assertEquals(50L, NumberUtils.max(longArray));

        double[] doubleArray = {1.5, 3.5, 0.5};
        assertEquals(0.5, NumberUtils.min(doubleArray), 0.0001);
        assertEquals(3.5, NumberUtils.max(doubleArray), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinArrayNull() {
        NumberUtils.min((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxArrayEmpty() {
        NumberUtils.max(new int[0]);
    }

    @Test
    public void testMinMaxThreeValues() {
        assertEquals(1, NumberUtils.min(3, 1, 2));
        assertEquals(3, NumberUtils.max(1, 3, 2));
        assertEquals(10L, NumberUtils.min(30L, 10L, 20L));
        assertEquals(30L, NumberUtils.max(10L, 30L, 20L));
        assertEquals(1.1, NumberUtils.min(3.3, 1.1, 2.2), 0.0001);
        assertEquals(3.3, NumberUtils.max(1.1, 3.3, 2.2), 0.0001);
    }

    @Test
    public void testConstants() {
        assertNotNull(NumberUtils.LONG_ZERO);
        assertNotNull(NumberUtils.INTEGER_ZERO);
        assertNotNull(NumberUtils.FLOAT_ZERO);
        assertNotNull(NumberUtils.DOUBLE_ZERO);
        assertEquals(0L, NumberUtils.LONG_ZERO.longValue());
        assertEquals(1, NumberUtils.INTEGER_ONE.intValue());
        assertEquals(-1, NumberUtils.INTEGER_MINUS_ONE.intValue());
    }
}
