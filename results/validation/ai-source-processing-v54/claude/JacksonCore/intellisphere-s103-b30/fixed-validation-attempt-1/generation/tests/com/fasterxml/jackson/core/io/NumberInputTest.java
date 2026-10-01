package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    // ============================================================================
    // parseInt(char[], int, int) tests
    // ============================================================================

    @Test
    public void testParseInt_SingleDigit() {
        char[] digits = {'5'};
        int result = NumberInput.parseInt(digits, 0, 1);
        assertEquals(5, result);
    }

    @Test
    public void testParseInt_TwoDigits() {
        char[] digits = {'4', '2'};
        int result = NumberInput.parseInt(digits, 0, 2);
        assertEquals(42, result);
    }

    @Test
    public void testParseInt_MaxNineDigits() {
        char[] digits = {'2', '1', '4', '7', '4', '8', '3', '6', '4'};
        int result = NumberInput.parseInt(digits, 0, 9);
        assertEquals(214748364, result);
    }

    @Test
    public void testParseInt_WithOffset() {
        char[] digits = {'x', 'x', '1', '2', '3'};
        int result = NumberInput.parseInt(digits, 2, 3);
        assertEquals(123, result);
    }

    @Test
    public void testParseInt_Zero() {
        char[] digits = {'0'};
        int result = NumberInput.parseInt(digits, 0, 1);
        assertEquals(0, result);
    }

    @Test
    public void testParseInt_AllNines() {
        char[] digits = {'9', '9', '9', '9', '9', '9', '9', '9', '9'};
        int result = NumberInput.parseInt(digits, 0, 9);
        assertEquals(999999999, result);
    }

    @Test
    public void testParseInt_ThreeDigits() {
        char[] digits = {'7', '7', '7'};
        int result = NumberInput.parseInt(digits, 0, 3);
        assertEquals(777, result);
    }

    // ============================================================================
    // parseInt(String) tests
    // ============================================================================

    @Test
    public void testParseIntString_Simple() {
        int result = NumberInput.parseInt("42");
        assertEquals(42, result);
    }

    @Test
    public void testParseIntString_Negative() {
        int result = NumberInput.parseInt("-42");
        assertEquals(-42, result);
    }

    @Test
    public void testParseIntString_Positive() {
        int result = NumberInput.parseInt("+42");
        assertEquals(42, result);
    }

    @Test
    public void testParseIntString_Zero() {
        int result = NumberInput.parseInt("0");
        assertEquals(0, result);
    }

    @Test
    public void testParseIntString_SingleDigit() {
        int result = NumberInput.parseInt("9");
        assertEquals(9, result);
    }

    @Test
    public void testParseIntString_NineDigits() {
        int result = NumberInput.parseInt("123456789");
        assertEquals(123456789, result);
    }

    @Test
    public void testParseIntString_NegativeNineDigits() {
        int result = NumberInput.parseInt("-123456789");
        assertEquals(-123456789, result);
    }

    @Test
    public void testParseIntString_TenDigitsUsesJDK() {
        // 10 digits should fall back to Integer.parseInt
        int result = NumberInput.parseInt("2147483647");
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testParseIntString_WithDecimalFallsBackToDouble() {
        int result = NumberInput.parseInt("42.5");
        assertEquals(42, result);
    }

    // ============================================================================
    // parseLong(char[], int, int) tests
    // ============================================================================

    @Test
    public void testParseLong_TenDigits() {
        char[] digits = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        long result = NumberInput.parseLong(digits, 0, 10);
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLong_EighteenDigits() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '7'};
        long result = NumberInput.parseLong(digits, 0, 19);
        assertEquals(9223372036854775807L, result);
    }

    @Test
    public void testParseLong_WithOffset() {
        char[] digits = {'x', 'x', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        long result = NumberInput.parseLong(digits, 2, 10);
        assertEquals(1000000000L, result);
    }

    // ============================================================================
    // parseLong(String) tests
    // ============================================================================

    @Test
    public void testParseLongString_Simple() {
        long result = NumberInput.parseLong("9223372036854775807");
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testParseLongString_NineDigits() {
        long result = NumberInput.parseLong("123456789");
        assertEquals(123456789L, result);
    }

    @Test
    public void testParseLongString_TenDigits() {
        long result = NumberInput.parseLong("1234567890");
        assertEquals(1234567890L, result);
    }

    @Test
    public void testParseLongString_Negative() {
        long result = NumberInput.parseLong("-9223372036854775808");
        assertEquals(Long.MIN_VALUE, result);
    }

    // ============================================================================
    // inLongRange(char[], int, int, boolean) tests
    // ============================================================================

    @Test
    public void testInLongRange_ShorterThanMax_Positive() {
        char[] digits = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        boolean result = NumberInput.inLongRange(digits, 0, 18, false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_ExactMax_Positive() {
        String maxStr = String.valueOf(Long.MAX_VALUE);
        char[] digits = maxStr.toCharArray();
        boolean result = NumberInput.inLongRange(digits, 0, maxStr.length(), false);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_LongerThanMax_Positive() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '8'};
        boolean result = NumberInput.inLongRange(digits, 0, 20, false);
        assertFalse(result);
    }

    @Test
    public void testInLongRange_ShorterThanMinAbs_Negative() {
        char[] digits = {'1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        boolean result = NumberInput.inLongRange(digits, 0, 18, true);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_ExactMin_Negative() {
        String minStr = String.valueOf(Long.MIN_VALUE).substring(1);
        char[] digits = minStr.toCharArray();
        boolean result = NumberInput.inLongRange(digits, 0, minStr.length(), true);
        assertTrue(result);
    }

    @Test
    public void testInLongRange_JustOverMin_Negative() {
        char[] digits = {'9', '2', '2', '3', '3', '7', '2', '0', '3', '6', '8', '5', '4', '7', '7', '5', '8', '0', '9'};
        boolean result = NumberInput.inLongRange(digits, 0, 19, true);
        assertFalse(result);
    }

    @Test
    public void testInLongRange_WithOffset_Positive() {
        String maxStr = String.valueOf(Long.MAX_VALUE);
        char[] buffer = new char[maxStr.length() + 5];
        System.arraycopy(maxStr.toCharArray(), 0, buffer, 5, maxStr.length());
        boolean result = NumberInput.inLongRange(buffer, 5, maxStr.length(), false);
        assertTrue(result);
    }

    // ============================================================================
    // inLongRange(String, boolean) tests
    // ============================================================================

    

    

    

    

    

    

    // ============================================================================
    // parseDouble(String) tests
    // ============================================================================

    

    

    

    

    

    

    // ============================================================================
    // parseAsDouble(String, double) tests
    // ============================================================================

    

    

    

    

    

    

    // ============================================================================
    // parseAsInt(String, int) tests
    // ============================================================================

    

    

    

    

    

    

    

    

    // ============================================================================
    // parseAsLong(String, long) tests
    // ============================================================================

    

    

    

    

    

    

    

    // ============================================================================
    // parseBigDecimal(String) tests
    // ============================================================================

    

    

    

    

    // ============================================================================
    // parseBigDecimal(char[]) tests
    // ============================================================================

    

    

    

    // ============================================================================
    // parseBigDecimal(char[], int, int) tests
    // ============================================================================

    

    

    

    
}
