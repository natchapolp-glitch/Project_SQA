package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInputTest
{
    @Test
    public void testParseIntFromCharArrayVariousLengths() {
        char[] digits = "x123456789y".toCharArray();

        assertEquals(1, NumberInput.parseInt(digits, 1, 1));
        assertEquals(123, NumberInput.parseInt(digits, 1, 3));
        assertEquals(123456789, NumberInput.parseInt(digits, 1, 9));
    }

    @Test
    public void testParseIntStringFastPathAndFallback() {
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
        assertEquals(1234567890, NumberInput.parseInt("1234567890"));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    

    

    @Test
    public void testParseLongString() {
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        assertEquals(1234567890123L, NumberInput.parseLong("1234567890123"));
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void testInLongRangeCharArrayAtBoundaries() {
        char[] max = String.valueOf(Long.MAX_VALUE).toCharArray();
        char[] minNoSign = String.valueOf(Long.MIN_VALUE).substring(1).toCharArray();
        char[] aboveMax = "9223372036854775808".toCharArray();
        char[] belowMin = "9223372036854775809".toCharArray();

        assertTrue(NumberInput.inLongRange(max, 0, max.length, false));
        assertTrue(NumberInput.inLongRange(minNoSign, 0, minNoSign.length, true));
        assertFalse(NumberInput.inLongRange(aboveMax, 0, aboveMax.length, false));
        assertFalse(NumberInput.inLongRange(belowMin, 0, belowMin.length, true));
        assertTrue(NumberInput.inLongRange("123".toCharArray(), 0, 3, false));
        assertFalse(NumberInput.inLongRange("12345678901234567890".toCharArray(), 0, 20, false));
    }

    @Test
    public void testInLongRangeStringAtBoundaries() {
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), false));
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MIN_VALUE).substring(1), true));
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));
        assertTrue(NumberInput.inLongRange("1", false));
        assertFalse(NumberInput.inLongRange("12345678901234567890", false));
    }

    @Test
    public void testParseAsIntHandlesTrimSignsAndDoubleCoercion() {
        assertEquals(42, NumberInput.parseAsInt(" 42 ", -1));
        assertEquals(17, NumberInput.parseAsInt("+17", -1));
        assertEquals(-3, NumberInput.parseAsInt("-3", -1));
        assertEquals(12, NumberInput.parseAsInt("12.9", -1));
        assertEquals(-7, NumberInput.parseAsInt(null, -7));
        assertEquals(-7, NumberInput.parseAsInt(" ", -7));
        assertEquals(-7, NumberInput.parseAsInt("abc", -7));
    }

    @Test
    public void testParseAsLongHandlesTrimSignsAndDoubleCoercion() {
        assertEquals(42L, NumberInput.parseAsLong(" 42 ", -1L));
        assertEquals(17L, NumberInput.parseAsLong("+17", -1L));
        assertEquals(-3L, NumberInput.parseAsLong("-3", -1L));
        assertEquals(12L, NumberInput.parseAsLong("12.9", -1L));
        assertEquals(-7L, NumberInput.parseAsLong(null, -7L));
        assertEquals(-7L, NumberInput.parseAsLong(" ", -7L));
        assertEquals(-7L, NumberInput.parseAsLong("abc", -7L));
    }

    @Test
    public void testParseDoubleAndParseAsDouble() {
        assertEquals(1.25d, NumberInput.parseDouble("1.25"), 0.0d);
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0d);

        assertEquals(2.5d, NumberInput.parseAsDouble(" 2.5 ", -1.0d), 0.0d);
        assertEquals(-1.0d, NumberInput.parseAsDouble(null, -1.0d), 0.0d);
        assertEquals(-1.0d, NumberInput.parseAsDouble(" ", -1.0d), 0.0d);
        assertEquals(-1.0d, NumberInput.parseAsDouble("not-a-number", -1.0d), 0.0d);
    }

    @Test
    public void testParseBigDecimalFromStringAndCharArray() {
        BigDecimal expected = new BigDecimal("12345.67");

        assertEquals(expected, NumberInput.parseBigDecimal("12345.67"));

        char[] full = "xx12345.67yy".toCharArray();
        assertEquals(expected, NumberInput.parseBigDecimal(full, 2, 8));
        assertEquals(expected, NumberInput.parseBigDecimal("12345.67".toCharArray()));
    }

    
}
