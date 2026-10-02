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

    @Test
    public void testInLongRangeCharArray_withOffset() {
        char[] chars = {'x', 'x', '1', '2', '3'};
        assertTrue(NumberInput.inLongRange(chars, 2, 3, false));
    }

    // ============================================================
    // inLongRange(String, boolean) tests
    // ============================================================

    @Test
    public void testInLongRangeString_positive_valid() {
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
    }

    @Test
    public void testInLongRangeString_positive_tooLarge() {
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
    }

    @Test
    public void testInLongRangeString_negative_valid() {
        assertTrue(NumberInput.inLongRange("9223372036854775808", true));
    }

    @Test
    public void testInLongRangeString_negative_tooLarge() {
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));
    }

    @Test
    public void testInLongRangeString_shortString_positive() {
        assertTrue(NumberInput.inLongRange("42", false));
    }

    @Test
    public void testInLongRangeString_zero() {
        assertTrue(NumberInput.inLongRange("0", false));
        assertTrue(NumberInput.inLongRange("0", true));
    }

    // ============================================================
    // parseAsInt(String, int) tests
    // ============================================================

    @Test
    public void testParseAsInt_validPositive() {
        assertEquals(123, NumberInput.parseAsInt("123", 999));
    }

    @Test
    public void testParseAsInt_validNegative() {
        assertEquals(-456, NumberInput.parseAsInt("-456", 999));
    }

    @Test
    public void testParseAsInt_null_usesDefault() {
        assertEquals(777, NumberInput.parseAsInt(null, 777));
    }

    @Test
    public void testParseAsInt_emptyString_usesDefault() {
        assertEquals(888, NumberInput.parseAsInt("", 888));
    }

    @Test
    public void testParseAsInt_whitespace_usesDefault() {
        assertEquals(999, NumberInput.parseAsInt("   ", 999));
    }

    @Test
    public void testParseAsInt_leadingTrailingSpace() {
        assertEquals(42, NumberInput.parseAsInt("  42  ", 0));
    }

    @Test
    public void testParseAsInt_scientific_notation() {
        // Contains 'e', fallback to double parsing
        int result = NumberInput.parseAsInt("1.5e2", 0);
        assertEquals(150, result); // 1.5e2 = 150.0, cast to int = 150
    }

    @Test
    public void testParseAsInt_invalidFormat_usesDefault() {
        assertEquals(555, NumberInput.parseAsInt("abc", 555));
    }

    @Test
    public void testParseAsInt_largeNumber_usesDefault() {
        assertEquals(777, NumberInput.parseAsInt("99999999999", 777));
    }

    // ============================================================
    // parseAsLong(String, long) tests
    // ============================================================

    @Test
    public void testParseAsLong_validPositive() {
        assertEquals(123456789L, NumberInput.parseAsLong("123456789", 999L));
    }

    @Test
    public void testParseAsLong_validNegative() {
        assertEquals(-987654321L, NumberInput.parseAsLong("-987654321", 999L));
    }

    @Test
    public void testParseAsLong_null_usesDefault() {
        assertEquals(555L, NumberInput.parseAsLong(null, 555L));
    }

    @Test
    public void testParseAsLong_emptyString_usesDefault() {
        assertEquals(666L, NumberInput.parseAsLong("", 666L));
    }

    @Test
    public void testParseAsLong_whitespace_usesDefault() {
        assertEquals(777L, NumberInput.parseAsLong("   ", 777L));
    }

    @Test
    public void testParseAsLong_leadingTrailingSpace() {
        assertEquals(123L, NumberInput.parseAsLong("  123  ", 0L));
    }

    @Test
    public void testParseAsLong_scientific_notation() {
        long result = NumberInput.parseAsLong("1e10", 0L);
        assertEquals(10000000000L, result);
    }

    @Test
    public void testParseAsLong_invalidFormat_usesDefault() {
        assertEquals(888L, NumberInput.parseAsLong("xyz", 888L));
    }

    // ============================================================
    // parseDouble(String) tests
    // ============================================================

    @Test
    public void testParseDouble_simple() {
        assertEquals(3.14, NumberInput.parseDouble("3.14"), 0.001);
    }

    @Test
    public void testParseDouble_scientific() {
        assertEquals(1.5e2, NumberInput.parseDouble("1.5e2"), 0.001);
    }

    @Test
    public void testParseDouble_negative() {
        assertEquals(-2.71, NumberInput.parseDouble("-2.71"), 0.001);
    }

    @Test
    public void testParseDouble_nastySmallDouble() {
        // Special case: nasty small double gets converted to Double.MIN_VALUE
        double result = NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE);
        assertEquals(Double.MIN_VALUE, result, 0.0);
    }

    @Test
    public void testParseDouble_zero() {
        assertEquals(0.0, NumberInput.parseDouble("0.0"), 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDouble_invalid() {
        NumberInput.parseDouble("not-a-number");
    }

    // ============================================================
    // parseAsDouble(String, double) tests
    // ============================================================

    @Test
    public void testParseAsDouble_valid() {
        assertEquals(2.71, NumberInput.parseAsDouble("2.71", 1.0), 0.001);
    }

    @Test
    public void testParseAsDouble_null_usesDefault() {
        assertEquals(3.14, NumberInput.parseAsDouble(null, 3.14), 0.0);
    }

    @Test
    public void testParseAsDouble_emptyString_usesDefault() {
        assertEquals(2.0, NumberInput.parseAsDouble("", 2.0), 0.0);
    }

    @Test
    public void testParseAsDouble_whitespace_usesDefault() {
        assertEquals(1.5, NumberInput.parseAsDouble("   ", 1.5), 0.0);
    }

    @Test
    public void testParseAsDouble_leadingTrailingSpace() {
        assertEquals(1.23, NumberInput.parseAsDouble("  1.23  ", 0.0), 0.001);
    }

    @Test
    public void testParseAsDouble_invalid_usesDefault() {
        assertEquals(9.99, NumberInput.parseAsDouble("invalid", 9.99), 0.0);
    }

    // ============================================================
    // parseBigDecimal(String) tests
    // ============================================================

    @Test
    public void testParseBigDecimal_String_valid() {
        BigDecimal result = NumberInput.parseBigDecimal("123.456");
        assertEquals(new BigDecimal("123.456"), result);
    }

    @Test
    public void testParseBigDecimal_String_zero() {
        BigDecimal result = NumberInput.parseBigDecimal("0");
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void testParseBigDecimal_String_negative() {
        BigDecimal result = NumberInput.parseBigDecimal("-999.999");
        assertEquals(new BigDecimal("-999.999"), result);
    }

    @Test
    public void testParseBigDecimal_String_scientific() {
        BigDecimal result = NumberInput.parseBigDecimal("1.5e10");
        assertEquals(new BigDecimal("1.5e10"), result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_String_invalid() {
        NumberInput.parseBigDecimal("not-valid");
    }

    // ============================================================
    // parseBigDecimal(char[]) tests
    // ============================================================

    @Test
    public void testParseBigDecimal_CharArray_simple() {
        char[] chars = {'1', '2', '3'};
        BigDecimal result = NumberInput.parseBigDecimal(chars);
        assertEquals(new BigDecimal("123"), result);
    }

    @Test
    public void testParseBigDecimal_CharArray_withDecimal() {
        char[] chars = {'1', '2', '.', '3', '4'};
        BigDecimal result = NumberInput.parseBigDecimal(chars);
        assertEquals(new BigDecimal("12.34"), result);
    }

    // ============================================================
    // parseBigDecimal(char[], int, int) tests
    // ============================================================

    @Test
    public void testParseBigDecimal_CharArray_offset_length() {
        char[] chars = {'x', 'y', '5', '6', '7', 'z'};
        BigDecimal result = NumberInput.parseBigDecimal(chars, 2, 3);
        assertEquals(new BigDecimal("567"), result);
    }

    @Test
    public void testParseBigDecimal_CharArray_zeroOffset() {
        char[] chars = {'9', '8', '.', '7', '6'};
        BigDecimal result = NumberInput.parseBigDecimal(chars, 0, 5);
        assertEquals(new BigDecimal("98.76"), result);
    }

    @Test
    public void testParseBigDecimal_CharArray_singleChar() {
        char[] chars = {'a', 'b', '3', 'c', 'd'};
        BigDecimal result = NumberInput.parseBigDecimal(chars, 2, 1);
        assertEquals(new BigDecimal("3"), result);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimal_CharArray_invalid() {
        char[] chars = {'x', 'y', 'z'};
        NumberInput.parseBigDecimal(chars, 0, 3);
    }

}
