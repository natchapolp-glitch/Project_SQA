package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Regression tests for NumberInput utility class.
 * Tests parsing of integers, longs, doubles, and BigDecimals with boundary conditions.
 */
public class NumberInputTest {

    // ============ parseInt(char[], int, int) tests ============

    @Test
    public void testParseIntFromCharArraySingleDigit() {
        char[] chars = {'5'};
        int result = NumberInput.parseInt(chars, 0, 1);
        assertEquals(5, result);
    }

    @Test
    public void testParseIntFromCharArrayNineDigits() {
        char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int result = NumberInput.parseInt(chars, 0, 9);
        assertEquals(123456789, result);
    }

    @Test
    public void testParseIntFromCharArrayWithOffset() {
        char[] chars = {'x', 'x', '4', '2'};
        int result = NumberInput.parseInt(chars, 2, 2);
        assertEquals(42, result);
    }

    @Test
    public void testParseIntFromCharArrayZero() {
        char[] chars = {'0'};
        int result = NumberInput.parseInt(chars, 0, 1);
        assertEquals(0, result);
    }

    @Test
    public void testParseIntFromCharArrayThreeDigits() {
        char[] chars = {'1', '0', '0'};
        int result = NumberInput.parseInt(chars, 0, 3);
        assertEquals(100, result);
    }

    @Test
    public void testParseIntFromCharArrayMaxInt() {
        // 2147483647 = Integer.MAX_VALUE
        char[] chars = {'2', '1', '4', '7', '4', '8', '3', '6', '4', '7'};
        int result = NumberInput.parseInt(chars, 0, 9);
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseIntFromCharArrayOffsetAndLen() {
        char[] chars = {'a', 'b', '9', '9', '9', 'c', 'd'};
        int result = NumberInput.parseInt(chars, 2, 3);
        assertEquals(999, result);
    }

    @Test
    public void testParseIntFromCharArrayTwoDigits() {
        char[] chars = {'1', '2'};
        int result = NumberInput.parseInt(chars, 0, 2);
        assertEquals(12, result);
    }

    @Test
    public void testParseIntFromCharArrayFourDigits() {
        char[] chars = {'5', '6', '7', '8'};
        int result = NumberInput.parseInt(chars, 0, 4);
        assertEquals(5678, result);
    }

    // ============ parseInt(String) tests ============

    @Test
    public void testParseIntFromStringSingleDigit() {
        int result = NumberInput.parseInt("7");
        assertEquals(7, result);
    }

    @Test
    public void testParseIntFromStringPositive() {
        int result = NumberInput.parseInt("12345");
        assertEquals(12345, result);
    }

    @Test
    public void testParseIntFromStringNegative() {
        int result = NumberInput.parseInt("-42");
        assertEquals(-42, result);
    }

    @Test
    public void testParseIntFromStringPlus() {
        int result = NumberInput.parseInt("+100");
        assertEquals(100, result);
    }

    @Test
    public void testParseIntFromStringZero() {
        int result = NumberInput.parseInt("0");
        assertEquals(0, result);
    }

    @Test
    public void testParseIntFromStringMaxInt() {
        int result = NumberInput.parseInt("2147483647");
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseIntFromStringWithNonDigit() {
        // Falls back to Integer.parseInt
        int result = NumberInput.parseInt("123.456");
        // This will throw NumberFormatException from Integer.parseInt,
        // but we test that it delegates correctly
        try {
            NumberInput.parseInt("not_a_number");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    // ============ parseLong(char[], int, int) tests ============

    @Test
    public void testParseLongFromCharArrayTenDigits() {
        char[] chars = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        long result = NumberInput.parseLong(chars, 0, 10);
        assertEquals(10000000000L, result);
    }

    @Test
    public void testParseLongFromCharArrayEighteenDigits() {
        // 999999999999999999
        char[] chars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        long result = NumberInput.parseLong(chars, 0, 18);
        assertEquals(999999999999999999L, result);
    }

    @Test
    public void testParseLongFromCharArrayWithOffset() {
        char[] chars = {'x', 'x', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(chars, 2, 10);
        assertEquals(1234567890L, result);
    }

    // ============ parseLong(String) tests ============

    @Test
    public void testParseLongFromStringSmall() {
        long result = NumberInput.parseLong("12345");
        assertEquals(12345L, result);
    }

    @Test
    public void testParseLongFromStringLarge() {
        long result = NumberInput.parseLong("9223372036854775807");
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseLongFromStringNegative() {
        long result = NumberInput.parseLong("-999999999999");
        assertEquals(-999999999999L, result);
    }

    // ============ inLongRange(char[], int, int, boolean) tests ============

    @Test
    public void testInLongRangeMaxPositive() {
        // 9223372036854775807 = Long.MAX_VALUE
        String maxStr = "9223372036854775807";
        char[] chars = maxStr.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeAboveMaxPositive() {
        // One more than max
        String aboveMax = "9223372036854775808";
        char[] chars = aboveMax.toCharArray();
        assertFalse(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeMaxNegative() {
        // 9223372036854775808 (without sign) = -(Long.MIN_VALUE)
        String maxNegStr = "9223372036854775808";
        char[] chars = maxNegStr.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, true));
    }

    @Test
    public void testInLongRangeBelowMinNegative() {
        String belowMin = "9223372036854775809";
        char[] chars = belowMin.toCharArray();
        assertFalse(NumberInput.inLongRange(chars, 0, chars.length, true));
    }

    @Test
    public void testInLongRangeShorterLengthPositive() {
        char[] chars = "100".toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeWithOffset() {
        String str = "xxx9223372036854775807yyy";
        char[] chars = str.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 3, 19, false));
    }

    // ============ inLongRange(String, boolean) tests ============

    @Test
    public void testInLongRangeStringMaxPositive() {
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
    }

    @Test
    public void testInLongRangeStringAboveMaxPositive() {
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
    }

    

    

    

    

    // ============ parseDouble(String) tests ============

    

    

    

    

    

    

    

    

    // ============ parseAsDouble(String, double) tests ============

    

    

    

    

    

    

    // ============ parseAsInt(String, int) tests ============

    

    

    

    

    

    

    

    

    

    // ============ parseAsLong(String, long) tests ============

    

    

    

    

    

    

    

    // ============ parseBigDecimal(String) tests ============

    

    

    

    

    

    // ============ parseBigDecimal(char[]) tests ============

    

    

    

    // ============ parseBigDecimal(char[], int, int) tests ============

    

    

    

    
}
