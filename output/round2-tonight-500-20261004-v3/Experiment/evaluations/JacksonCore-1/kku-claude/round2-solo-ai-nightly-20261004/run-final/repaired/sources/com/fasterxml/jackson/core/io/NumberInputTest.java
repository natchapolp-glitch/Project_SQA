package com.fasterxml.jackson.core.io;

import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigDecimal;

public class NumberInputTest {

    @Test
    public void testParseIntCharArrayAndString() {
        char[] digits = "12345".toCharArray();
        assertEquals(12345, NumberInput.parseInt(digits, 0, 5));
        assertEquals(12345, NumberInput.parseInt("12345"));
        assertEquals(-12345, NumberInput.parseInt("-12345"));
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(0, NumberInput.parseInt("0"));
    }

    @Test
    public void testParseLongVariants() {
        assertEquals(123456789012L, NumberInput.parseLong("123456789012"));
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        char[] digits = "123456789012".toCharArray();
        assertEquals(123456789012L, NumberInput.parseLong(digits, 0, digits.length));
    }

    @Test
    public void testInLongRangeBoundaries() {
        String maxLongNoSign = String.valueOf(Long.MAX_VALUE);
        assertTrue(NumberInput.inLongRange(maxLongNoSign, false));
        String minLongNoSign = String.valueOf(Long.MIN_VALUE).substring(1);
        assertTrue(NumberInput.inLongRange(minLongNoSign, true));

        String overMax = "9223372036854775808";
        assertFalse(NumberInput.inLongRange(overMax, false));

        String shortStr = "123";
        assertTrue(NumberInput.inLongRange(shortStr, false));

        char[] digits = maxLongNoSign.toCharArray();
        assertTrue(NumberInput.inLongRange(digits, 0, digits.length, false));
    }

    @Test
    public void testParseAsIntLongDoubleFallback() {
        assertEquals(42, NumberInput.parseAsInt("42", -1));
        assertEquals(-1, NumberInput.parseAsInt("notanumber", -1));
        assertEquals(42L, NumberInput.parseAsLong("42", -1L));
        assertEquals(-1L, NumberInput.parseAsLong("bad", -1L));
        assertEquals(3.14, NumberInput.parseAsDouble("3.14", -1.0), 0.0001);
        assertEquals(-1.0, NumberInput.parseAsDouble("bad", -1.0), 0.0001);
    }

    @Test
    public void testParseDoubleValidAndInvalid() {
        assertEquals(3.14, NumberInput.parseDouble("3.14"), 0.0001);
        // Verify the nasty-small-double constant can be parsed without hanging/erroring,
        // and yields a sane positive value near zero.
        double nasty = NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE);
        assertTrue(nasty > 0.0);
        assertTrue(nasty < 1.0e-300);
        try {
            NumberInput.parseDouble("not-a-double");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseBigDecimalVariants() {
        BigDecimal expected = new BigDecimal("12345.6789");
        assertEquals(expected, NumberInput.parseBigDecimal("12345.6789"));

        char[] chars = "12345.6789".toCharArray();
        assertEquals(expected, NumberInput.parseBigDecimal(chars));

        char[] padded = "XX12345.6789YY".toCharArray();
        assertEquals(expected, NumberInput.parseBigDecimal(padded, 2, 10));

        try {
            NumberInput.parseBigDecimal("not-a-number");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }
}
