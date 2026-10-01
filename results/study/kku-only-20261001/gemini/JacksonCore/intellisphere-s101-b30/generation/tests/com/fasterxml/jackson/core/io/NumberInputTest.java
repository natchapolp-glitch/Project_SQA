package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntCharArray() {
        char[] chars = "123456789".toCharArray();
        assertEquals(123456789, NumberInput.parseInt(chars, 0, 9));

        char[] offsetChars = "xx42yy".toCharArray();
        assertEquals(42, NumberInput.parseInt(offsetChars, 2, 2));

        char[] singleDigit = "0".toCharArray();
        assertEquals(0, NumberInput.parseInt(singleDigit, 0, 1));
    }

    @Test
    public void testParseIntString() {
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(123, NumberInput.parseInt("123"));
        assertEquals(-456, NumberInput.parseInt("-456"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        // Fallback paths (lengths or signs handled via Integer.parseInt)
        assertEquals(1000000000, NumberInput.parseInt("1000000000"));
        assertEquals(-1000000000, NumberInput.parseInt("-1000000000"));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntStringInvalid() {
        NumberInput.parseInt("12a34");
    }

    

    @Test
    public void testParseLongString() {
        // Length <= 9 fast-path
        assertEquals(0L, NumberInput.parseLong("0"));
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        // Length > 9
        assertEquals(123456789012345L, NumberInput.parseLong("123456789012345"));
        assertEquals(-9876543210L, NumberInput.parseLong("-9876543210"));
    }

    @Test
    public void testInLongRangeCharArray() {
        char[] maxLong = "9223372036854775807".toCharArray();
        assertTrue(NumberInput.inLongRange(maxLong, 0, maxLong.length, false));

        char[] overMax = "9223372036854775808".toCharArray();
        assertFalse(NumberInput.inLongRange(overMax, 0, overMax.length, false));
        assertTrue(NumberInput.inLongRange(overMax, 0, overMax.length, true));

        char[] overMin = "9223372036854775809".toCharArray();
        assertFalse(NumberInput.inLongRange(overMin, 0, overMin.length, true));

        char[] shortNum = "1234".toCharArray();
        assertTrue(NumberInput.inLongRange(shortNum, 0, shortNum.length, false));

        char[] longNum = "123456789012345678901".toCharArray();
        assertFalse(NumberInput.inLongRange(longNum, 0, longNum.length, false));
    }

    @Test
    public void testInLongRangeString() {
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));
        assertTrue(NumberInput.inLongRange("9223372036854775808", true));
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));
        assertTrue(NumberInput.inLongRange("1", false));
        assertFalse(NumberInput.inLongRange("10000000000000000000", false));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(-1, NumberInput.parseAsInt(null, -1));
        assertEquals(5, NumberInput.parseAsInt("   ", 5));
        assertEquals(42, NumberInput.parseAsInt("+42", 0));
        assertEquals(-42, NumberInput.parseAsInt("-42", 0));
        assertEquals(123, NumberInput.parseAsInt(" 123 ", 0));
        // Decimal coerce to int
        assertEquals(12, NumberInput.parseAsInt("12.75", 0));
        assertEquals(-12, NumberInput.parseAsInt("-12.75", 0));
        // Non-number fallback
        assertEquals(99, NumberInput.parseAsInt("not-a-number", 99));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(-1L, NumberInput.parseAsLong(null, -1L));
        assertEquals(5L, NumberInput.parseAsLong(" ", 5L));
        assertEquals(1000000000000L, NumberInput.parseAsLong("+1000000000000", 0L));
        assertEquals(-1000000000000L, NumberInput.parseAsLong("-1000000000000", 0L));
        assertEquals(1234L, NumberInput.parseAsLong(" 1234 ", 0L));
        // Decimal coerce to long
        assertEquals(123L, NumberInput.parseAsLong("123.99", 0L));
        assertEquals(999L, NumberInput.parseAsLong("invalid", 999L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(-1.0, NumberInput.parseAsDouble(null, -1.0), 0.0001);
        assertEquals(0.0, NumberInput.parseAsDouble("", 0.0), 0.0001);
        assertEquals(3.14159, NumberInput.parseAsDouble("3.14159", 0.0), 0.00001);
        assertEquals(-2.5, NumberInput.parseAsDouble(" -2.5 ", 0.0), 0.0001);
        assertEquals(10.0, NumberInput.parseAsDouble("not-a-double", 10.0), 0.0001);
    }

    @Test
    public void testParseDoubleNastyConstant() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
        assertEquals(1.234, NumberInput.parseDouble("1.234"), 0.00001);
    }

    @Test
    public void testParseBigDecimal() {
        BigDecimal bd1 = NumberInput.parseBigDecimal("12345.67890");
        assertEquals(new BigDecimal("12345.67890"), bd1);

        char[] chars = "prefix999.99suffix".toCharArray();
        BigDecimal bd2 = NumberInput.parseBigDecimal(chars, 6, 6);
        assertEquals(new BigDecimal("999.99"), bd2);

        char[] fullChars = "-42.5".toCharArray();
        BigDecimal bd3 = NumberInput.parseBigDecimal(fullChars);
        assertEquals(new BigDecimal("-42.5"), bd3);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalInvalid() {
        NumberInput.parseBigDecimal("invalid-decimal");
    }
}
