// src/test/java/com/fasterxml/jackson/core/io/NumberInputTest.java
package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigDecimal;

public class NumberInputTest {

    // ========================= parseInt(String) =========================

    @Test
    public void testParseIntStringZero() {
        assertEquals(0, NumberInput.parseInt("0"));
    }

    @Test
    public void testParseIntStringPositive() {
        assertEquals(123, NumberInput.parseInt("123"));
    }

    @Test
    public void testParseIntStringNegative() {
        assertEquals(-456, NumberInput.parseInt("-456"));
    }

    @Test
    public void testParseIntStringMaxInt() {
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
    }

    @Test
    public void testParseIntStringMinInt() {
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testParseIntStringOverflow() {
        // larger than max int should fail (but code defers to JDK, which throws)
        try {
            NumberInput.parseInt("2147483648");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseIntStringInvalidCharacters() {
        try {
            NumberInput.parseInt("12a3");
            fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    // ========================= parseInt(char[], int, int) =========================

    @Test
    public void testParseIntCharArrayOneDigit() {
        char[] digits = {'7'};
        assertEquals(7, NumberInput.parseInt(digits, 0, 1));
    }

    @Test
    public void testParseIntCharArrayMultipleDigits() {
        char[] digits = {'1','2','3','4','5','6','7','8','9'};
        assertEquals(123456789, NumberInput.parseInt(digits, 0, 9));
    }

    @Test
    public void testParseIntCharArrayOffsetAndLen() {
        char[] digits = {'x','x','4','2','x'};
        assertEquals(42, NumberInput.parseInt(digits, 2, 2));
    }

    // ========================= parseLong(String) =========================

    @Test
    public void testParseLongStringSmall() {
        assertEquals(123L, NumberInput.parseLong("123"));
    }

    @Test
    public void testParseLongStringLarge() {
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
    }

    @Test
    public void testParseLongStringMinValue() {
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    // ========================= parseLong(char[], int, int) =========================

    @Test
    public void testParseLongCharArray() {
        // len = 18, parseLong splits into 9+9 digits
        char[] digits = "123456789987654321".toCharArray();
        assertEquals(123456789987654321L, NumberInput.parseLong(digits, 0, 18));
    }

    // ========================= inLongRange =========================

    @Test
    public void testInLongRangeStringBelowMax() {
        assertTrue(NumberInput.inLongRange("922337203685477580", false));
    }

    @Test
    public void testInLongRangeStringEqualToMax() {
        assertTrue(NumberInput.inLongRange(NumberInput.MAX_LONG_STR, false));
    }

    @Test
    public void testInLongRangeStringAboveMax() {
        assertFalse(NumberInput.inLongRange("9223372036854775808", false)); // one more than max len doesn't matter here, but length equal and comparison
    }

    @Test
    public void testInLongRangeStringNegativeWithinMin() {
        assertTrue(NumberInput.inLongRange(NumberInput.MIN_LONG_STR_NO_SIGN, true));
    }

    @Test
    public void testInLongRangeCharArrayPositive() {
        assertTrue(NumberInput.inLongRange("9223372036854775807".toCharArray(), 0, 19, false));
    }

    // ========================= parseAsInt =========================

    @Test
    public void testParseAsIntNullReturnsDefault() {
        assertEquals(42, NumberInput.parseAsInt(null, 42));
    }

    @Test
    public void testParseAsIntEmptyReturnsDefault() {
        assertEquals(-1, NumberInput.parseAsInt("", -1));
    }

    @Test
    public void testParseAsIntNormal() {
        assertEquals(15, NumberInput.parseAsInt("15", 0));
    }

    @Test
    public void testParseAsIntNegative() {
        assertEquals(-100, NumberInput.parseAsInt("-100", 0));
    }

    @Test
    public void testParseAsIntWithPlusSign() {
        assertEquals(20, NumberInput.parseAsInt("+20", 0));
    }

    @Test
    public void testParseAsIntDoubleCoercion() {
        assertEquals(3, NumberInput.parseAsInt("3.14", 0));
    }

    // ========================= parseAsLong =========================

    @Test
    public void testParseAsLongNullReturnsDefault() {
        assertEquals(99L, NumberInput.parseAsLong(null, 99L));
    }

    @Test
    public void testParseAsLongEmptyReturnsDefault() {
        assertEquals(100L, NumberInput.parseAsLong("", 100L));
    }

    @Test
    public void testParseAsLongNormal() {
        assertEquals(123456789L, NumberInput.parseAsLong("123456789", 0L));
    }

    @Test
    public void testParseAsLongWithPlus() {
        assertEquals(50L, NumberInput.parseAsLong("+50", 0L));
    }

    // ========================= parseAsDouble =========================

    @Test
    public void testParseAsDoubleNullReturnsDefault() {
        assertEquals(3.14, NumberInput.parseAsDouble(null, 3.14), 0.0);
    }

    @Test
    public void testParseAsDoubleEmptyReturnsDefault() {
        assertEquals(2.71, NumberInput.parseAsDouble("", 2.71), 0.0);
    }

    @Test
    public void testParseAsDoubleNormal() {
        assertEquals(1.5, NumberInput.parseAsDouble("1.5", 0.0), 0.0);
    }

    @Test
    public void testParseAsDoubleInvalidFormat() {
        assertEquals(-1.0, NumberInput.parseAsDouble("abc", -1.0), 0.0);
    }

    // ========================= parseDouble =========================

    @Test
    public void testParseDoubleNastySmallDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseDoubleInvalidThrows() {
        NumberInput.parseDouble("not-a-number");
    }

    // ========================= parseBigDecimal =========================

    @Test
    public void testParseBigDecimalString() {
        assertEquals(BigDecimal.valueOf(123, 2), NumberInput.parseBigDecimal("1.23"));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalStringInvalid() {
        NumberInput.parseBigDecimal("xyz");
    }

    @Test
    public void testParseBigDecimalCharArray() {
        char[] buffer = {'4','.','5'};
        assertEquals(new BigDecimal("4.5"), NumberInput.parseBigDecimal(buffer, 0, 3));
    }
}
