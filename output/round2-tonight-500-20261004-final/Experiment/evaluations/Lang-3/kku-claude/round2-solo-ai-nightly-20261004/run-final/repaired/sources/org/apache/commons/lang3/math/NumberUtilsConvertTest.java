package org.apache.commons.lang3.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumberUtilsConvertTest {

    @Test
    public void testToIntConversions() {
        assertEquals(0, NumberUtils.toInt(null));
        assertEquals(0, NumberUtils.toInt(""));
        assertEquals(1, NumberUtils.toInt("1"));
        assertEquals(1, NumberUtils.toInt(null, 1));
        assertEquals(1, NumberUtils.toInt("", 1));
        assertEquals(1, NumberUtils.toInt("1", 0));
        assertEquals(5, NumberUtils.toInt("abc", 5));
    }

    @Test
    public void testToLongConversions() {
        assertEquals(0L, NumberUtils.toLong(null));
        assertEquals(0L, NumberUtils.toLong(""));
        assertEquals(1L, NumberUtils.toLong("1"));
        assertEquals(1L, NumberUtils.toLong(null, 1L));
        assertEquals(1L, NumberUtils.toLong("", 1L));
        assertEquals(1L, NumberUtils.toLong("1", 0L));
    }

    @Test
    public void testToFloatAndDoubleConversions() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0f);
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.0f);
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0f);
        assertEquals(1.1f, NumberUtils.toFloat(null, 1.1f), 0.0f);
        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0d);
        assertEquals(1.5d, NumberUtils.toDouble("1.5"), 0.0d);
        assertEquals(1.1d, NumberUtils.toDouble("", 1.1d), 0.0d);
    }

    @Test
    public void testToByteAndShortConversions() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
        assertEquals((byte) 1, NumberUtils.toByte("1"));
        assertEquals((byte) 1, NumberUtils.toByte(null, (byte) 1));
        assertEquals((short) 0, NumberUtils.toShort(null));
        assertEquals((short) 1, NumberUtils.toShort("1"));
        assertEquals((short) 1, NumberUtils.toShort(null, (short) 1));
    }

    @Test
    public void testMinMaxArrays() {
        assertEquals(1, NumberUtils.min(new int[] {3, 1, 2}));
        assertEquals(3, NumberUtils.max(new int[] {3, 1, 2}));
        assertEquals(1L, NumberUtils.min(new long[] {3L, 1L, 2L}));
        assertEquals(3L, NumberUtils.max(new long[] {3L, 1L, 2L}));
    }

    @Test
    public void testIsDigitsAndIsNumber() {
        assertTrue(NumberUtils.isDigits("12345"));
        assertFalse(NumberUtils.isDigits("12.45"));
        assertTrue(NumberUtils.isNumber("1.5"));
        assertFalse(NumberUtils.isNumber("abc"));
    }
}
