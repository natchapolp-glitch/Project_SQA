package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntString() {
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-123, NumberInput.parseInt("-123"));
        assertEquals(0, NumberInput.parseInt("0"));
    }

    @Test
    public void testParseIntChars() {
        char[] chars = "  123456  ".toCharArray();
        assertEquals(123456, NumberInput.parseInt(chars, 2, 6));
    }

    @Test
    public void testParseLongString() {
        assertEquals(1234567890123L, NumberInput.parseLong("1234567890123"));
        assertEquals(42L, NumberInput.parseLong("42"));
    }

    @Test
    public void testInLongRange() {
        assertTrue(NumberInput.inLongRange("9223372036854775807".toCharArray(), 0, 19, false));
        assertFalse(NumberInput.inLongRange("9223372036854775808".toCharArray(), 0, 19, false));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseBigDecimalInvalid() {
        NumberInput.parseBigDecimal("not-a-number");
    }

    @Test
    public void testParseAsConstants() {
        assertEquals(100, NumberInput.parseAsInt("100", 10));
        assertEquals(10, NumberInput.parseAsInt("invalid", 10));
        assertEquals(200L, NumberInput.parseAsLong("200", 5L));
        assertEquals(15.5, NumberInput.parseAsDouble("15.5", 1.0), 0.001);
    }
}
