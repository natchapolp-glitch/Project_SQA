package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;

/**
 * Regression test suite for {@link NumberUtils}.
 */
public class NumberUtilsRegressionTest {

    private static final double DELTA_DOUBLE = 0.0001d;
    private static final float DELTA_FLOAT = 0.0001f;

    @Test
    public void testConstructor() {
        assertNotNull(new NumberUtils());
    }

    @Test
    public void testToInt() {
        assertEquals(0, NumberUtils.toInt(null));
        assertEquals(0, NumberUtils.toInt(""));
        assertEquals(123, NumberUtils.toInt("123"));
        assertEquals(-45, NumberUtils.toInt("-45"));
        assertEquals(10, NumberUtils.toInt(null, 10));
        assertEquals(10, NumberUtils.toInt("invalid", 10));
        assertEquals(55, NumberUtils.toInt("55", 10));
    }

    @Test
    public void testToLong() {
        assertEquals(0L, NumberUtils.toLong(null));
        assertEquals(0L, NumberUtils.toLong(""));
        assertEquals(123456789012L, NumberUtils.toLong("123456789012"));
        assertEquals(-99L, NumberUtils.toLong("-99"));
        assertEquals(5L, NumberUtils.toLong(null, 5L));
        assertEquals(5L, NumberUtils.toLong("invalid", 5L));
        assertEquals(77L, NumberUtils.toLong("77", 5L));
    }

    @Test
    public void testToFloat() {
        assertEquals(0.0f, NumberUtils.toFloat(null), DELTA_FLOAT);
        assertEquals(0.0f, NumberUtils.toFloat(""), DELTA_FLOAT);
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), DELTA_FLOAT);
        assertEquals(-2.75f, NumberUtils.toFloat("-2.75"), DELTA_FLOAT);
        assertEquals(1.1f, NumberUtils.toFloat(null, 1.1f), DELTA_FLOAT);
        assertEquals(1.1f, NumberUtils.toFloat("invalid", 1.1f), DELTA_FLOAT);
        assertEquals(3.25f, NumberUtils.toFloat("3.25", 1.1f), DELTA_FLOAT);
    }

    @Test
    public void testToDouble() {
        assertEquals(0.0d, NumberUtils.toDouble(null), DELTA_DOUBLE);
        assertEquals(0.0d, NumberUtils.toDouble(""), DELTA_DOUBLE);
        assertEquals(1.5d, NumberUtils.toDouble("1.5"), DELTA_DOUBLE);
        assertEquals(-2.75d, NumberUtils.toDouble("-2.75"), DELTA_DOUBLE);
        assertEquals(1.1d, NumberUtils.toDouble(null, 1.1d), DELTA_DOUBLE);
        assertEquals(1.1d, NumberUtils.toDouble("invalid", 1.1d), DELTA_DOUBLE);
        assertEquals(3.25d, NumberUtils.toDouble("3.25", 1.1d), DELTA_DOUBLE);
    }

    @Test
    public void testToByteAndToShort() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
        assertEquals((byte) 0, NumberUtils.toByte(""));
        assertEquals((byte) 12, NumberUtils.toByte("12"));
        assertEquals((byte) 5, NumberUtils.toByte(null, (byte) 5));
        assertEquals((byte) 5, NumberUtils.toByte("bad", (byte) 5));

        assertEquals((short) 0, NumberUtils.toShort(null));
        assertEquals((short) 0, NumberUtils.toShort(""));
        assertEquals((short) 1234, NumberUtils.toShort("1234"));
        assertEquals((short) 9, NumberUtils.toShort(null, (short) 9));
        assertEquals((short) 9, NumberUtils.toShort("bad", (short) 9));
    }

    @Test
    public void testCreateInteger() {
        assertNull(NumberUtils.createInteger(null));
        assertEquals(Integer.valueOf(123), NumberUtils.createInteger("123"));
        assertEquals(Integer.valueOf(-123), NumberUtils.createInteger("-123"));
        assertEquals(Integer.valueOf(16), NumberUtils.createInteger("0x10"));
        assertEquals(Integer.valueOf(8), NumberUtils.createInteger("010"));
    }

    @Test
    public void testCreateLong() {
        assertNull(NumberUtils.createLong(null));
        assertEquals(Long.valueOf(123456789012L), NumberUtils.createLong("123456789012"));
        assertEquals(Long.valueOf(-123456789012L), NumberUtils.createLong("-123456789012"));
        assertEquals(Long.valueOf(16L), NumberUtils.createLong("0x10"));
        assertEquals(Long.valueOf(8L), NumberUtils.createLong("010"));
    }

    @Test
    public void testCreateBigInteger() {
        assertNull(NumberUtils.createBigInteger(null));
        assertEquals(new BigInteger("12345678901234567890"), NumberUtils.createBigInteger("12345678901234567890"));
        assertEquals(BigInteger.valueOf(16), NumberUtils.createBigInteger("0x10"));
        assertEquals(BigInteger.valueOf(-16), NumberUtils.createBigInteger("-0x10"));
        assertEquals(BigInteger.valueOf(16), NumberUtils.createBigInteger("#10"));
        assertEquals(BigInteger.valueOf(-16), NumberUtils.createBigInteger("-#10"));
        assertEquals(BigInteger.valueOf(8), NumberUtils.createBigInteger("010"));
        assertEquals(BigInteger.valueOf(-8), NumberUtils.createBigInteger("-010"));
    }

    @Test
    public void testCreateFloat() {
        assertNull(NumberUtils.createFloat(null));
        assertEquals(Float.valueOf(1.23f), NumberUtils.createFloat("1.23"));
        assertEquals(Float.valueOf(-4.56f), NumberUtils.createFloat("-4.56"));
    }

    @Test
    public void testCreateDouble() {
        assertNull(NumberUtils.createDouble(null));
        assertEquals(Double.valueOf(1.23456789d), NumberUtils.createDouble("1.23456789"));
        assertEquals(Double.valueOf(-4.56789012d), NumberUtils.createDouble("-4.56789012"));
    }

    @Test
    public void testCreateBigDecimal() {
        assertNull(NumberUtils.createBigDecimal(null));
        assertEquals(new BigDecimal("123456789.987654321"), NumberUtils.createBigDecimal("123456789.987654321"));
        try {
            NumberUtils.createBigDecimal("");
            fail("Expected NumberFormatException for empty string");
        } catch (NumberFormatException expected) {
            // Success
        }
        try {
            NumberUtils.createBigDecimal("--123");
            fail("Expected NumberFormatException for '--123'");
        } catch (NumberFormatException expected) {
            // Success
        }
    }

    @Test
    public void testCreateNumberNullAndBlank() {
        assertNull(NumberUtils.createNumber(null));
        try {
            NumberUtils.createNumber("");
            fail("Expected NumberFormatException for empty string");
        } catch (NumberFormatException expected) {
            // Success
        }
        try {
            NumberUtils.createNumber("   ");
            fail("Expected NumberFormatException for blank string");
        } catch (NumberFormatException expected) {
            // Success
        }
    }

    @Test
    public void testCreateNumberHex() {
        assertEquals(Integer.valueOf(0x1a), NumberUtils.createNumber("0x1a"));
        assertEquals(Integer.valueOf(0x1A), NumberUtils.createNumber("0X1A"));
        assertEquals(Integer.valueOf(-0x1a), NumberUtils.createNumber("-0x1a"));
        assertEquals(Integer.valueOf(-0x1A), NumberUtils.createNumber("-0X1A"));
        assertEquals(Integer.valueOf(0x1a), NumberUtils.createNumber("#1a"));
        assertEquals(Integer.valueOf(-0x1a), NumberUtils.createNumber("-#1a"));
    }

    @Test
    public void testCreateNumberHexLeadingZeroes() {
        assertEquals(Integer.valueOf(0x12), NumberUtils.createNumber("0x00000012"));
        assertEquals(Integer.valueOf(0x7fffffff), NumberUtils.createNumber("0x7fffffff"));
        assertEquals(Long.valueOf(0x80000000L), NumberUtils.createNumber("0x80000000"));
        assertEquals(Long.valueOf(0x7fffffffffffffffL), NumberUtils.createNumber("0x7fffffffffffffff"));
        assertEquals(new BigInteger("8000000000000000", 16), NumberUtils.createNumber("0x8000000000000000"));
    }

    @Test
    public void testCreateNumberDecimalsAndExponents() {
        assertEquals(Float.valueOf("1.23"), NumberUtils.createNumber("1.23"));
        assertEquals(Double.valueOf("1.23456789"), NumberUtils.createNumber("1.23456789"));
        assertEquals(new BigDecimal("1.234567890123456789"), NumberUtils.createNumber("1.234567890123456789"));
        assertEquals(Float.valueOf("1.23e2"), NumberUtils.createNumber("1.23e2"));
        assertEquals(Float.valueOf("0.0"), NumberUtils.createNumber("0.0"));
    }

    @Test
    public void testCreateNumberTypeQualifiers() {
        assertEquals(Long.valueOf(123L), NumberUtils.createNumber("123L"));
        assertEquals(Long.valueOf(123L), NumberUtils.createNumber("123l"));
        assertEquals(Float.valueOf(1.23f), NumberUtils.createNumber("1.23f"));
        assertEquals(Float.valueOf(1.23f), NumberUtils.createNumber("1.23F"));
        assertEquals(Double.valueOf(1.23d), NumberUtils.createNumber("1.23d"));
        assertEquals(Double.valueOf(1.23d), NumberUtils.createNumber("1.23D"));
        assertEquals(Float.valueOf(0.0f), NumberUtils.createNumber("0.0f"));
        assertEquals(Double.valueOf(0.0d), NumberUtils.createNumber("0.0d"));
        assertEquals(new BigInteger("123456789012345678901234567890"),
                NumberUtils.createNumber("123456789012345678901234567890L"));
    }

    @Test
    public void testCreateNumberIntegerTypes() {
        assertEquals(Integer.valueOf(123), NumberUtils.createNumber("123"));
        assertEquals(Integer.valueOf(-123), NumberUtils.createNumber("-123"));
        assertEquals(Long.valueOf(2147483648L), NumberUtils.createNumber("2147483648"));
        assertEquals(new BigInteger("9223372036854775808"), NumberUtils.createNumber("9223372036854775808"));
    }

    @Test
    public void testCreateNumberInvalid() {
        String[] invalid = {"1.2.3", "1e2e3", "1.2e", "foo", "123a", "1e+e2", "0x"};
        for (String str : invalid) {
            try {
                NumberUtils.createNumber(str);
                fail("Expected NumberFormatException for: " + str);
            } catch (NumberFormatException expected) {
                // Success
            }
        }
    }

    @Test
    public void testMinMaxInt() {
        assertEquals(1, NumberUtils.min(1, 2, 3));
        assertEquals(1, NumberUtils.min(3, 1, 2));
        assertEquals(1, NumberUtils.min(2, 3, 1));

        assertEquals(3, NumberUtils.max(1, 2, 3));
        assertEquals(3, NumberUtils.max(3, 1, 2));
        assertEquals(3, NumberUtils.max(2, 3, 1));
    }

    @Test
    public void testMinMaxLong() {
        assertEquals(10L, NumberUtils.min(10L, 20L, 30L));
        assertEquals(10L, NumberUtils.min(30L, 10L, 20L));
        assertEquals(10L, NumberUtils.min(20L, 30L, 10L));

        assertEquals(30L, NumberUtils.max(10L, 20L, 30L));
        assertEquals(30L, NumberUtils.max(30L, 10L, 20L));
        assertEquals(30L, NumberUtils.max(20L, 30L, 10L));
    }

    @Test
    public void testMinMaxShortAndByte() {
        assertEquals((short) 1, NumberUtils.min((short) 1, (short) 2, (short) 3));
        assertEquals((short) 3, NumberUtils.max((short) 1, (short) 2, (short) 3));

        assertEquals((byte) 1, NumberUtils.min((byte) 1, (byte) 2, (byte) 3));
        assertEquals((byte) 3, NumberUtils.max((byte) 1, (byte) 2, (byte) 3));
    }

    @Test
    public void testMinMaxDoubleAndFloat() {
        assertEquals(1.5d, NumberUtils.min(1.5d, 2.5d, 3.5d), DELTA_DOUBLE);
        assertEquals(3.5d, NumberUtils.max(1.5d, 2.5d, 3.5d), DELTA_DOUBLE);
        assertTrue(Double.isNaN(NumberUtils.min(1.5d, Double.NaN, 3.5d)));
        assertTrue(Double.isNaN(NumberUtils.max(1.5d, Double.NaN, 3.5d)));

        assertEquals(1.5f, NumberUtils.min(1.5f, 2.5f, 3.5f), DELTA_FLOAT);
        assertEquals(3.5f, NumberUtils.max(1.5f, 2.5f, 3.5f), DELTA_FLOAT);
        assertTrue(Float.isNaN(NumberUtils.min(1.5f, Float.NaN, 3.5f)));
        assertTrue(Float.isNaN(NumberUtils.max(1.5f, Float.NaN, 3.5f)));
    }

    @Test
    public void testMinMaxArrayLongAndInt() {
        assertEquals(2L, NumberUtils.min(new long[]{5L, 2L, 8L, 3L}));
        assertEquals(8L, NumberUtils.max(new long[]{5L, 2L, 8L, 3L}));

        assertEquals(2, NumberUtils.min(new int[]{5, 2, 8, 3}));
        assertEquals(8, NumberUtils.max(new int[]{5, 2, 8, 3}));
    }

    @Test
    public void testMinMaxArrayShortAndByte() {
        assertEquals((short) 2, NumberUtils.min(new short[]{5, 2, 8, 3}));
        assertEquals((short) 8, NumberUtils.max(new short[]{5, 2, 8, 3}));

        assertEquals((byte) 2, NumberUtils.min(new byte[]{5, 2, 8, 3}));
        assertEquals((byte) 8, NumberUtils.max(new byte[]{5, 2, 8, 3}));
    }

    @Test
    public void testMinMaxArrayDoubleAndFloat() {
        assertEquals(1.1d, NumberUtils.min(new double[]{3.3d, 1.1d, 4.4d}), DELTA_DOUBLE);
        assertEquals(4.4d, NumberUtils.max(new double[]{3.3d, 1.1d, 4.4d}), DELTA_DOUBLE);
        assertTrue(Double.isNaN(NumberUtils.min(new double[]{3.3d, Double.NaN, 4.4d})));
        assertTrue(Double.isNaN(NumberUtils.max(new double[]{3.3d, Double.NaN, 4.4d})));

        assertEquals(1.1f, NumberUtils.min(new float[]{3.3f, 1.1f, 4.4f}), DELTA_FLOAT);
        assertEquals(4.4f, NumberUtils.max(new float[]{3.3f, 1.1f, 4.4f}), DELTA_FLOAT);
        assertTrue(Float.isNaN(NumberUtils.min(new float[]{3.3f, Float.NaN, 4.4f})));
        assertTrue(Float.isNaN(NumberUtils.max(new float[]{3.3f, Float.NaN, 4.4f})));
    }

    @Test
    public void testMinMaxArrayExceptions() {
        try {
            NumberUtils.min((int[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Success
        }
        try {
            NumberUtils.min(new int[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Success
        }
        try {
            NumberUtils.max((long[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Success
        }
        try {
            NumberUtils.max(new long[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Success
        }
    }

    @Test
    public void testIsDigits() {
        assertFalse(NumberUtils.isDigits(null));
        assertFalse(NumberUtils.isDigits(""));
        assertFalse(NumberUtils.isDigits(" "));
        assertFalse(NumberUtils.isDigits("123a"));
        assertFalse(NumberUtils.isDigits("-123"));
        assertFalse(NumberUtils.isDigits("12.3"));
        assertTrue(NumberUtils.isDigits("0"));
        assertTrue(NumberUtils.isDigits("123456789"));
    }

    

    @Test
    public void testIsNumberExponentAndQualifiers() {
        assertTrue(NumberUtils.isNumber("1e2"));
        assertTrue(NumberUtils.isNumber("1E2"));
        assertTrue(NumberUtils.isNumber("1e-2"));
        assertTrue(NumberUtils.isNumber("1e+2"));
        assertFalse(NumberUtils.isNumber("1e"));
        assertFalse(NumberUtils.isNumber("1e+"));
        assertFalse(NumberUtils.isNumber("1e-"));

        assertTrue(NumberUtils.isNumber("123L"));
        assertTrue(NumberUtils.isNumber("123l"));
        assertTrue(NumberUtils.isNumber("12.3f"));
        assertTrue(NumberUtils.isNumber("12.3F"));
        assertTrue(NumberUtils.isNumber("12.3d"));
        assertTrue(NumberUtils.isNumber("12.3D"));
        assertFalse(NumberUtils.isNumber("12.3L"));
        assertFalse(NumberUtils.isNumber("1e2L"));
    }
}
