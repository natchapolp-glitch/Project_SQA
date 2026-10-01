package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class GeneratedNumberInputTest
{
    private static final String MAX_LONG = "9223372036854775807";
    private static final String MAX_LONG_PLUS_ONE = "9223372036854775808";

    @Test
    public void testParseIntChars()
    {
        Assert.assertEquals(7, NumberInput.parseInt("7".toCharArray(), 0, 1));
        Assert.assertEquals(12345, NumberInput.parseInt("12345".toCharArray(), 0, 5));
        Assert.assertEquals(123, NumberInput.parseInt("xx123yy".toCharArray(), 2, 3));
        Assert.assertEquals(999999999, NumberInput.parseInt("999999999".toCharArray(), 0, 9));
        Assert.assertEquals(12, NumberInput.parseInt("0012".toCharArray(), 0, 4));
    }

    @Test
    public void testParseLongChars()
    {
        Assert.assertEquals(1234567890L, NumberInput.parseLong("1234567890".toCharArray(), 0, 10));
        Assert.assertEquals(123456789012345678L,
                NumberInput.parseLong("123456789012345678".toCharArray(), 0, 18));
        Assert.assertEquals(999999999999999999L,
                NumberInput.parseLong("999999999999999999".toCharArray(), 0, 18));
        Assert.assertEquals(1000000000L, NumberInput.parseLong("zz1000000000".toCharArray(), 2, 10));
    }

    @Test
    public void testParseIntString()
    {
        Assert.assertEquals(0, NumberInput.parseInt("0"));
        Assert.assertEquals(7, NumberInput.parseInt("7"));
        Assert.assertEquals(12, NumberInput.parseInt("12"));
        Assert.assertEquals(123, NumberInput.parseInt("123"));
        Assert.assertEquals(7, NumberInput.parseInt("007"));
        Assert.assertEquals(123456789, NumberInput.parseInt("123456789"));
        Assert.assertEquals(-1, NumberInput.parseInt("-1"));
        Assert.assertEquals(-1234, NumberInput.parseInt("-1234"));
        Assert.assertEquals(-123456789, NumberInput.parseInt("-123456789"));
        Assert.assertEquals(1234567890, NumberInput.parseInt("1234567890"));
        Assert.assertEquals(Integer.MAX_VALUE, NumberInput.parseInt("2147483647"));
        Assert.assertEquals(Integer.MIN_VALUE, NumberInput.parseInt("-2147483648"));
    }

    @Test
    public void testParseIntStringInvalid()
    {
        String[] bad = new String[] { "-", "abc", "12a", "-x", "1234567890123", "-99999999999" };
        for (int i = 0; i < bad.length; ++i) {
            try {
                NumberInput.parseInt(bad[i]);
                Assert.fail("Expected NumberFormatException for '" + bad[i] + "'");
            } catch (NumberFormatException e) {
                // expected
            }
        }
        try {
            NumberInput.parseInt("");
            Assert.fail("Expected StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testParseLongString()
    {
        Assert.assertEquals(0L, NumberInput.parseLong("0"));
        Assert.assertEquals(123L, NumberInput.parseLong("123"));
        Assert.assertEquals(-5L, NumberInput.parseLong("-5"));
        Assert.assertEquals(123456789L, NumberInput.parseLong("123456789"));
        Assert.assertEquals(12345678901234L, NumberInput.parseLong("12345678901234"));
        Assert.assertEquals(-123456789012L, NumberInput.parseLong("-123456789012"));
        Assert.assertEquals(Long.MAX_VALUE, NumberInput.parseLong(MAX_LONG));
        Assert.assertEquals(Long.MIN_VALUE, NumberInput.parseLong("-9223372036854775808"));
        try {
            NumberInput.parseLong("abc");
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
        try {
            NumberInput.parseLong("12345678901x");
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testInLongRangeChars()
    {
        // positive
        Assert.assertTrue(NumberInput.inLongRange(MAX_LONG.toCharArray(), 0, 19, false));
        Assert.assertTrue(NumberInput.inLongRange("9223372036854775806".toCharArray(), 0, 19, false));
        Assert.assertFalse(NumberInput.inLongRange(MAX_LONG_PLUS_ONE.toCharArray(), 0, 19, false));
        Assert.assertTrue(NumberInput.inLongRange("999".toCharArray(), 0, 3, false));
        Assert.assertFalse(NumberInput.inLongRange("10000000000000000000".toCharArray(), 0, 20, false));
        // negative (no sign passed in)
        Assert.assertTrue(NumberInput.inLongRange(MAX_LONG_PLUS_ONE.toCharArray(), 0, 19, true));
        Assert.assertFalse(NumberInput.inLongRange("9223372036854775809".toCharArray(), 0, 19, true));
        Assert.assertTrue(NumberInput.inLongRange(MAX_LONG.toCharArray(), 0, 19, true));
        Assert.assertTrue(NumberInput.inLongRange("1".toCharArray(), 0, 1, true));
        // with offset
        Assert.assertTrue(NumberInput.inLongRange(("ab" + MAX_LONG).toCharArray(), 2, 19, false));
        Assert.assertFalse(NumberInput.inLongRange(("ab" + MAX_LONG_PLUS_ONE).toCharArray(), 2, 19, false));
    }

    @Test
    public void testInLongRangeString()
    {
        Assert.assertTrue(NumberInput.inLongRange(MAX_LONG, false));
        Assert.assertTrue(NumberInput.inLongRange("9223372036854775806", false));
        Assert.assertFalse(NumberInput.inLongRange(MAX_LONG_PLUS_ONE, false));
        Assert.assertTrue(NumberInput.inLongRange("12345", false));
        Assert.assertFalse(NumberInput.inLongRange("10000000000000000000", false));
        Assert.assertTrue(NumberInput.inLongRange(MAX_LONG_PLUS_ONE, true));
        Assert.assertFalse(NumberInput.inLongRange("9223372036854775809", true));
        Assert.assertTrue(NumberInput.inLongRange(MAX_LONG, true));
        Assert.assertFalse(NumberInput.inLongRange("99999999999999999999", true));
    }

    @Test
    public void testParseAsInt()
    {
        Assert.assertEquals(5, NumberInput.parseAsInt(null, 5));
        Assert.assertEquals(7, NumberInput.parseAsInt("", 7));
        Assert.assertEquals(7, NumberInput.parseAsInt("   ", 7));
        Assert.assertEquals(42, NumberInput.parseAsInt(" 42 ", -1));
        Assert.assertEquals(7, NumberInput.parseAsInt("+7", -1));
        Assert.assertEquals(-9, NumberInput.parseAsInt("-9", -1));
        Assert.assertEquals(3, NumberInput.parseAsInt("3.9", -1));
        Assert.assertEquals(-3, NumberInput.parseAsInt("-3.9", -1));
        Assert.assertEquals(100, NumberInput.parseAsInt("1e2", -1));
        Assert.assertEquals(-1, NumberInput.parseAsInt("abc", -1));
        Assert.assertEquals(-1, NumberInput.parseAsInt("99999999999", -1));
        Assert.assertEquals(-1, NumberInput.parseAsInt("-", -1));
        Assert.assertEquals(-1, NumberInput.parseAsInt("+", -1));
    }

    @Test
    public void testParseAsLong()
    {
        Assert.assertEquals(5L, NumberInput.parseAsLong(null, 5L));
        Assert.assertEquals(7L, NumberInput.parseAsLong("", 7L));
        Assert.assertEquals(123L, NumberInput.parseAsLong("  123 ", -1L));
        Assert.assertEquals(15L, NumberInput.parseAsLong("+15", -1L));
        Assert.assertEquals(-15L, NumberInput.parseAsLong("-15", -1L));
        Assert.assertEquals(19L, NumberInput.parseAsLong("1.9e1", -1L));
        Assert.assertEquals(12345678901L, NumberInput.parseAsLong("12345678901", -1L));
        Assert.assertEquals(Long.MAX_VALUE, NumberInput.parseAsLong("1e30", -1L));
        Assert.assertEquals(-1L, NumberInput.parseAsLong("abc", -1L));
        Assert.assertEquals(-1L, NumberInput.parseAsLong("99999999999999999999", -1L));
    }

    @Test
    public void testParseAsDouble()
    {
        Assert.assertEquals(1.5, NumberInput.parseAsDouble(null, 1.5), 0.0);
        Assert.assertEquals(1.5, NumberInput.parseAsDouble("", 1.5), 0.0);
        Assert.assertEquals(2.5, NumberInput.parseAsDouble(" 2.5 ", 1.5), 0.0);
        Assert.assertEquals(1000.0, NumberInput.parseAsDouble("1e3", 1.5), 0.0);
        Assert.assertEquals(-0.5, NumberInput.parseAsDouble("-0.5", 1.5), 0.0);
        Assert.assertEquals(1.5, NumberInput.parseAsDouble("abc", 1.5), 0.0);
    }

    @Test
    public void testParseDouble()
    {
        Assert.assertEquals(3.25, NumberInput.parseDouble("3.25"), 0.0);
        Assert.assertEquals(-1.0E-5, NumberInput.parseDouble("-1e-5"), 0.0);
        Assert.assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
        try {
            NumberInput.parseDouble("abc");
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testParseBigDecimalString()
    {
        Assert.assertEquals(new BigDecimal("123.456"), NumberInput.parseBigDecimal("123.456"));
        Assert.assertEquals(new BigDecimal("-1E+3"), NumberInput.parseBigDecimal("-1E+3"));
        Assert.assertEquals(new BigDecimal("0"), NumberInput.parseBigDecimal("0"));
    }

    @Test
    public void testParseBigDecimalChars()
    {
        Assert.assertEquals(new BigDecimal("0.001"), NumberInput.parseBigDecimal("0.001".toCharArray()));
        Assert.assertEquals(new BigDecimal("12.5"), NumberInput.parseBigDecimal("x12.5y".toCharArray(), 1, 4));
        Assert.assertEquals(new BigDecimal("-7"), NumberInput.parseBigDecimal("-7".toCharArray(), 0, 2));
    }

    @Test
    public void testParseBigDecimalInvalid()
    {
        try {
            NumberInput.parseBigDecimal("abc");
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            Assert.assertEquals("Value \"abc\" can not be represented as BigDecimal", e.getMessage());
        }
        try {
            NumberInput.parseBigDecimal("1.2.3".toCharArray());
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            Assert.assertEquals("Value \"1.2.3\" can not be represented as BigDecimal", e.getMessage());
        }
        try {
            NumberInput.parseBigDecimal("zz12ab".toCharArray(), 2, 4);
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            Assert.assertEquals("Value \"12ab\" can not be represented as BigDecimal", e.getMessage());
        }
    }
}
