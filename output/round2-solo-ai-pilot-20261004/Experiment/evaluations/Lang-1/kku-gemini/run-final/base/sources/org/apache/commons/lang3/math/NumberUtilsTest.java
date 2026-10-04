package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void testToInt() {
        assertEquals(0, NumberUtils.toInt(null));
        assertEquals(0, NumberUtils.toInt(""));
        assertEquals(123, NumberUtils.toInt("123"));
        assertEquals(5, NumberUtils.toInt("invalid", 5));
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
        assertEquals(2.2f, NumberUtils.toFloat("invalid", 2.2f), 0.0001f);
    }

    @Test
    public void testToDouble() {
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0001d);
        assertEquals(0.0d, NumberUtils.toDouble(""), 0.0001d);
        assertEquals(2.5d, NumberUtils.toDouble("2.5"), 0.0001d);
        assertEquals(3.3d, NumberUtils.toDouble("invalid", 3.3d), 0.0001d);
    }

    @Test
    public void testToByteAndToShort() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
        assertEquals((byte) 7, NumberUtils.toByte("invalid", (byte) 7));
        assertEquals((byte) 5, NumberUtils.toByte("5"));

        assertEquals((short) 0, NumberUtils.toShort(null));
        assertEquals((short) 9, NumberUtils.toShort("invalid", (short) 9));
        assertEquals((short) 10, NumberUtils.toShort("10"));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(Integer.valueOf(123), NumberUtils.createNumber("123"));
        assertEquals(Long.valueOf(123L), NumberUtils.createNumber("123L"));
        assertEquals(Float.valueOf(1.5f), NumberUtils.createNumber("1.5f"));
        assertEquals(Double.valueOf(2.5d), NumberUtils.createNumber("2.5d"));
        assertEquals(Integer.valueOf(0x10), NumberUtils.createNumber("0x10"));
        assertEquals(Integer.valueOf(010), NumberUtils.createNumber("010"));
        assertNotNull(NumberUtils.createNumber("1.5e2"));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalid() {
        NumberUtils.createNumber("not-a-number");
    }

    @Test
    public void testIsNumber() {
        assertTrue(NumberUtils.isNumber("123"));
        assertTrue(NumberUtils.isNumber("123.45"));
        assertTrue(NumberUtils.isNumber("0x10"));
        assertTrue(NumberUtils.isNumber("-123"));
        assertTrue(NumberUtils.isNumber("1e10"));
        assertFalse(NumberUtils.isNumber(null));
        assertFalse(NumberUtils.isNumber(""));
        assertFalse(NumberUtils.isNumber("123a"));
    }

    @Test
    public void testMinMaxArray() {
        int[] intArray = {3, 1, 4, 1, 5, 9};
        assertEquals(1, NumberUtils.min(intArray));
        assertEquals(9, NumberUtils.max(intArray));

        long[] longArray = {10L, 20L, 5L};
        assertEquals(5L, NumberUtils.min(longArray));
        assertEquals(20L, NumberUtils.max(longArray));

        double[] doubleArray = {1.1, 2.2, 0.5};
        assertEquals(0.5, NumberUtils.min(doubleArray), 0.0001);
        assertEquals(2.2, NumberUtils.max(doubleArray), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinArrayEmpty() {
        int[] empty = {};
        NumberUtils.min(empty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxArrayNull() {
        int[] nullArray = null;
        NumberUtils.max(nullArray);
    }

    @Test
    public void testMinMaxTriplets() {
        assertEquals(1, NumberUtils.min(3, 1, 2));
        assertEquals(3, NumberUtils.max(1, 3, 2));

        assertEquals(10L, NumberUtils.min(20L, 10L, 30L));
        assertEquals(30L, NumberUtils.max(20L, 10L, 30L));

        assertEquals(1.1f, NumberUtils.min(2.2f, 1.1f, 3.3f), 0.0001f);
        assertEquals(3.3f, NumberUtils.max(2.2f, 1.1f, 3.3f), 0.0001f);

        assertEquals(1.1d, NumberUtils.min(2.2d, 1.1d, 3.3d), 0.0001d);
        assertEquals(3.3d, NumberUtils.max(2.2d, 1.1d, 3.3d), 0.0001d);
    }
}
