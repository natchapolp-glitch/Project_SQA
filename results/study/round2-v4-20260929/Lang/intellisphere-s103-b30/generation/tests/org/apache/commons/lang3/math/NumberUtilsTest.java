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
        NumberUtils.min(new long[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxNullArray() {
        NumberUtils.max((long[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxEmptyArray() {
        NumberUtils.max(new long[0]);
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

    

    

    

    // ==================================================
    // Tests for min (byte[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toByte (String)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toByte (String, byte)
    // ==================================================

    

    

    

    

    // ==================================================
    // Tests for max (double, double, double)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for max (double[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (double, double, double)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (double[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toDouble (String)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toDouble (String, double)
    // ==================================================

    

    

    

    

    // ==================================================
    // Tests for max (float, float, float)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for max (float[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (float, float, float)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (float[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toFloat (String)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toFloat (String, float)
    // ==================================================

    

    

    

    

    // ==================================================
    // Tests for max (int, int, int)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for max (int[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (int, int, int)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (int[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toInt (String)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toInt (String, int)
    // ==================================================

    

    

    

    

    // ==================================================
    // Tests for createDouble
    // ==================================================

    

    

    

    // ==================================================
    // Tests for createFloat
    // ==================================================

    

    

    

    // ==================================================
    // Tests for createInteger
    // ==================================================

    

    

    

    

    

    // ==================================================
    // Tests for createLong
    // ==================================================

    

    

    

    // ==================================================
    // Tests for createNumber
    // ==================================================

    

    

    

    

    

    

    

    

    

    

    

    

    

    // ==================================================
    // Tests for createBigDecimal
    // ==================================================

    

    

    

    

    

    // ==================================================
    // Tests for createBigInteger
    // ==================================================

    

    

    

    

    

    

    // ==================================================
    // Tests for max (long, long, long)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for max (long[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (long, long, long)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (long[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toLong (String)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toLong (String, long)
    // ==================================================

    

    

    

    

    // ==================================================
    // Tests for max (short, short, short)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for max (short[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (short, short, short)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for min (short[])
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toShort (String)
    // ==================================================

    

    

    

    // ==================================================
    // Tests for toShort (String, short)
    // ==================================================

    

    

    

    
}
