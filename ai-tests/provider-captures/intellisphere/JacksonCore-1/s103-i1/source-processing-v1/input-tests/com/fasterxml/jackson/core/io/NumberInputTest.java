com/fasterxml/jackson/core/io/NumberInputTest.java
package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;

public class NumberInputTest {

    // parseInt from char array
    @Test
    public void testParseIntCharArraySingleDigit() {
        assertEquals(0, NumberInput.parseInt(new char[]{'0'}, 0, 1));
        assertEquals(9, NumberInput.parseInt(new char[]{'9'}, 0, 1));
    }

    @Test
    public void testParseIntCharArrayMax9Digits() {
        assertEquals(123456789, NumberInput.parseInt("123456789".toCharArray(), 0, 9));
    }

    @Test
    public void testParseIntCharArrayWithOffset() {
        assertEquals(42, NumberInput.parseInt("abc42".toCharArray(), 3, 2));
    }

    // parseInt from String
    @Test
    public void testParseIntStringSingleDigit() {
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(5, NumberInput.parseInt("5"));
    }

    @Test
    public void testParseIntStringNegativeMaxRange() {
        assertEquals(-2147483648, NumberInput.parseInt("-2147483648"));
    }

    @Test
    public void testParseIntStringPositiveOverflow() {
        // Delegates to Integer.parseInt which throws exception
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt("2147483648"));
    }

    @Test
    public void testParseIntStringWithSignAndSpaces() {
        // Delegates to Integer.parseInt because spaces are not digits
        assertThrows(NumberFormatException.class, () -> NumberInput.parseInt("+123"));
    }

    // parseLong from char array
    @Test
    public void testParseLongCharArrayValid() {
        long expected = ((long)123456789) * 1000000000L + 987654321L;
        assertEquals(expected, NumberInput.parseLong("123456789987654321".toCharArray(), 0, 18));
    }

    // parseLong from String
    @Test
    public void testParseLongStringFitsInt() {
        assertEquals(123L, NumberInput.parseLong("123"));
        assertEquals(-1L, NumberInput.parseLong("-1"));
    }

    @Test
    public void testParseLongStringOverInt() {
        assertEquals(10000000000L, NumberInput.parseLong("10000000000"));
    }

    @Test
    public void testParseLongStringDecimals() {
        assertThrows(NumberFormatException.class, () -> NumberInput.parseLong("1.0"));
    }

    // inLongRange char array
    @Test
    public void testInLongRangeCharArraySmallPositive() {
        assertTrue(NumberInput.inLongRange("123".toCharArray(), 0, 3, false));
    }

    @Test
    public void testInLongRangeCharArrayMaxPositive() {
        String maxLong = "9223372036854775807";
        assertTrue(NumberInput.inLongRange(maxLong.toCharArray(), 0, maxLong.length(), false));
    }

    @Test
    public void testInLongRangeCharArrayOverflowPositive() {
        String overflow = "9223372036854775808";
        assertFalse(NumberInput.inLongRange(overflow.toCharArray(), 0, overflow.length(), false));
    }

    @Test
    public void testInLongRangeCharArrayMinNegative() {
        String minLongNoSign = "9223372036854775808"; // digits for MIN_VALUE
        assertTrue(NumberInput.inLongRange(minLongNoSign.toCharArray(), 0, minLongNoSign.length(), true));
    }

    // inLongRange String
    @Test
    public void testInLongRangeStringSmallNegative() {
        assertTrue(NumberInput.inLongRange("123", true));
    }

    @Test
    public void testInLongRangeStringOverflowNegative() {
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));
    }

    // parseAsInt
    @Test
    public void testParseAsIntNull() {
        assertEquals(10, NumberInput.parseAsInt(null, 10));
    }

    @Test
    public void testParseAsIntEmptyString() {
        assertEquals(20, NumberInput.parseAsInt("", 20));
    }

    @Test
    public void testParseAsIntLeadingPlusSign() {
        assertEquals(5, NumberInput.parseAsInt("+5", 0));
    }

    @Test
    public void testParseAsIntWithDecimalCoercion() {
        assertEquals(5, NumberInput.parseAsInt("5.9", 0));
    }

    @Test
    public void testParseAsIntInvalidText() {
        assertEquals(-1, NumberInput.parseAsInt("not-a-number", -1));
    }

    // parseAsLong
    @Test
    public void testParseAsLongNull() {
        assertEquals(100L, NumberInput.parseAsLong(null, 100L));
    }

    @Test
    public void testParseAsLongLeadingMinusSign() {
        assertEquals(-50L, NumberInput.parseAsLong("-50", 0L));
    }

    @Test
    public void testParseAsLongWithDecimalCoercion() {
        assertEquals(10L, NumberInput.parseAsLong("10.1", 0L));
    }

    // parseAsDouble
    @Test
    public void testParseAsDoubleNull() {
        assertEquals(2.5, NumberInput.parseAsDouble(null, 2.5), 0.0);
    }

    @Test
    public void testParseAsDoubleWhitespace() {
        assertEquals(0.0, NumberInput.parseAsDouble("   ", 0.0), 0.0);
    }

    @Test
    public void testParseAsDoubleInvalidFormat() {
        assertEquals(-1.0, NumberInput.parseAsDouble("abc", -1.0), 0.0);
    }

    // parseDouble
    @Test
    public void testParseDoubleValid() {
        assertEquals(3.14, NumberInput.parseDouble("3.14"), 0.0);
    }

    @Test
    public void testParseDoubleNastyValue() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
    }

    // parseBigDecimal
    @Test
    public void testParseBigDecimalStringValid() {
        assertEquals(BigDecimal.valueOf(100), NumberInput.parseBigDecimal("100"));
    }

    @Test
    public void testParseBigDecimalStringInvalid() {
        assertThrows(NumberFormatException.class, () -> NumberInput.parseBigDecimal("invalid"));
    }

    @Test
    public void testParseBigDecimalCharArray() {
        assertEquals(new BigDecimal("2.5"), NumberInput.parseBigDecimal("2.5".toCharArray()));
    }

    @Test
    public void testParseBigDecimalCharArraySubArray() {
        char[] buffer = "xx12.34yy".toCharArray();
        assertEquals(new BigDecimal("12.34"), NumberInput.parseBigDecimal(buffer, 2, 5));
    }
}
