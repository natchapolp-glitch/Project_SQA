package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NumberUtilsLang3AdditionalTest {

    @Test
    public void testToByte() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
        assertEquals((byte) 0, NumberUtils.toByte(""));
        assertEquals((byte) 123, NumberUtils.toByte("123"));
        assertEquals((byte) 42, NumberUtils.toByte("invalid", (byte) 42));
    }

    @Test
    public void testToShort() {
        assertEquals((short) 0, NumberUtils.toShort(null));
        assertEquals((short) 0, NumberUtils.toShort(""));
        assertEquals((short) 123, NumberUtils.toShort("123"));
        assertEquals((short) 42, NumberUtils.toShort("invalid", (short) 42));
    }

    @Test
    public void testCreateBigDecimalAndBigInteger() {
        assertNotNull(NumberUtils.createBigDecimal("123.45"));
        assertEquals(new BigDecimal("123.45"), NumberUtils.createBigDecimal("123.45"));
        assertNotNull(NumberUtils.createBigInteger("12345"));
        assertEquals(new BigInteger("12345"), NumberUtils.createBigInteger("12345"));
    }

    @Test
    public void testCreateFloatAndDouble() {
        assertEquals(Float.valueOf(1.5f), NumberUtils.createFloat("1.5"));
        assertEquals(Double.valueOf(2.5d), NumberUtils.createDouble("2.5"));
    }
}
