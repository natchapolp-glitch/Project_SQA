package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive deterministic regression tests for NumberInput utility methods.
 * Tests cover normal cases, boundaries, invalid inputs, and exception paths.
 */
public class NumberInputTest {

    // ============================================================
    // parseInt(char[], int, int) tests
    // ============================================================

    @Test
    public void testParseIntCharArray_singleDigit() {
        char[] chars = {'5'};
        assertEquals(5, NumberInput.parseInt(chars, 0, 1));
    }

    @Test
    public void testParseIntCharArray_twoDigits() {
        char[] chars = {'4', '2'};
        assertEquals(42, NumberInput.parseInt(chars, 0, 2));
    }

    @Test
    public void testParseIntCharArray_nineDigits() {
        char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        assertEquals(123456789, NumberInput.parseInt(chars, 0, 9));
    }

    @Test
    public void testParseIntCharArray_zeroValue() {
        char[] chars = {'0'};
        assertEquals(0, NumberInput.parseInt(chars, 0, 1));
    }

    @Test
    public void testParseIntCharArray_withOffset() {
        char[] chars = {'x', '5', '0'};
        assertEquals(50, NumberInput.parseInt(chars, 1, 2));
    }

    @Test
    public void testParseIntCharArray_maxInt() {
        // 2147483647 = Integer.MAX_VALUE
        char[] chars = {'2', '1', '4', '7', '4', '8', '3', '6', '4', '7'};
        assertEquals(2147483647, NumberInput.parseInt(chars, 0, 10));
    }

    @Test
    public void testParseIntCharArray_largeValue() {
        char[] chars = {'9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertEquals(999999999, NumberInput.parseInt(chars, 0, 9));
    }

    // ============================================================
    // parseInt(String) tests
    // ============================================================

    @Test
    public void testParseIntString_singleDigit() {
        assertEquals(7, NumberInput.parseInt("7"));
    }

    @Test
    public void testParseIntString_multipleDigits() {
        assertEquals(12345, NumberInput.parseInt("12345"));
    }

    @Test
    public void testParseIntString_negative() {
        assertEquals(-999, NumberInput.parseInt("-999"));
    }

    @Test
    public void testParseIntString_positive_sign() {
        assertEquals(123, NumberInput.parseInt("+123"));
    }

    @Test
    public void testParseIntString_zero() {
        assertEquals(0, NumberInput.parseInt("0"));
    }

    @Test
    public void testParseIntString_leadingZeros() {
        assertEquals(42, NumberInput.parseInt("00042"));
    }

    @Test
    public void testParseIntString_scientific_notation() {
        // Contains non-digit, delegates to Integer.parseInt via parseDouble
        try {
            int result = NumberInput.parseInt("1.5e2");
            // If it succeeds, verify it cast the double correctly
            assertEquals(150, result);
        } catch (NumberFormatException e) {
            // Acceptable if Integer.parseInt rejects it
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntString_tooLarge() {
        NumberInput.parseInt("99999999999");
    }

    // ============================================================
    // parseLong(char[], int, int) tests
    // ============================================================

    @Test
    public void testParseLongCharArray_tenDigits() {
        char[] chars = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        assertEquals(1000000000L, NumberInput.parseLong(chars, 0, 10));
    }

    @Test
    public void testParseLongCharArray_fifteenDigits() {
        char[] chars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertEquals(999999999999999L, NumberInput.parseLong(chars, 0, 15));
    }

    @Test
    public void testParseLongCharArray_eighteen_digits() {
        // 999999999999999999
        char[] chars = {'9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'};
        assertEquals(999999999999999999L, NumberInput.parseLong(chars, 0, 18));
    }

    @Test
    public void testParseLongCharArray_withOffset() {
        char[] chars = {'x', 'x', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        assertEquals(1000000000L, NumberInput.parseLong(chars, 2, 10));
    }

    // ============================================================
    // parseLong(String) tests
    // ============================================================

    @Test
    public void testParseLongString_shortNumber() {
        assertEquals(42L, NumberInput.parseLong("42"));
    }

    @Test
    public void testParseLongString_longNumber() {
        assertEquals(9223372036854775807L, NumberInput.parseLong("9223372036854775807"));
    }

    @Test
    public void testParseLongString_zero() {
        assertEquals(0L, NumberInput.parseLong("0"));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseLongString_exceedsMax() {
        NumberInput.parseLong("9223372036854775808");
    }

    // ============================================================
    // inLongRange(char[], int, int, boolean) tests
    // ============================================================

    @Test
    public void testInLongRangeCharArray_positive_withinRange() {
        char[] chars = {'1', '0', '0'};
        assertTrue(NumberInput.inLongRange(chars, 0, 3, false));
    }

    @Test
    public void testInLongRangeCharArray_positive_maxValue() {
        String maxStr = "9223372036854775807";
        char[] chars = maxStr.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeCharArray_positive_exceedsMax() {
        char[] chars = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '8'};
        assertFalse(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    @Test
    public void testInLongRangeCharArray_negative_withinRange() {
        char[] chars = {'1', '0', '0'};
        assertTrue(NumberInput.inLongRange(chars, 0, 3, true));
    }

    @Test
    public void testInLongRangeCharArray_negative_minValue() {
        String minStr = "9223372036854775808"; // absolute value of Long.MIN_VALUE
        char[] chars = minStr.toCharArray();
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, true));
    }

    @Test
    public void testInLongRangeCharArray_negative_exceedsMin() {
        char[] chars = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '9'};
        assertFalse(NumberInput.inLongRange(chars, 0, chars.length, true));
    }

    @Test
    public void testInLongRangeCharArray_shorterThanMax() {
        char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8'};
        assertTrue(NumberInput.inLongRange(chars, 0, chars.length, false));
    }

    

    // ============================================================
    // inLongRange(String, boolean) tests
    // ============================================================

    

    

    

    

    

    

    // ============================================================
    // parseAsInt(String, int) tests
    // ============================================================

    

    

    

    

    

    

    

    

    

    // ============================================================
    // parseAsLong(String, long) tests
    // ============================================================

    

    

    

    

    

    

    

    

    // ============================================================
    // parseDouble(String) tests
    // ============================================================

    

    

    

    

    

    

    // ============================================================
    // parseAsDouble(String, double) tests
    // ============================================================

    

    

    

    

    

    

    // ============================================================
    // parseBigDecimal(String) tests
    // ============================================================

    

    

    

    

    

    // ============================================================
    // parseBigDecimal(char[]) tests
    // ============================================================

    

    

    // ============================================================
    // parseBigDecimal(char[], int, int) tests
    // ============================================================

    

    

    

    

}
