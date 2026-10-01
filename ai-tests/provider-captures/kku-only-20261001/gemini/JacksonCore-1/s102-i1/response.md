com/fasterxml/jackson/core/io/NumberInputTest.java
```java
package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntCharArray() {
        char[] chars = "123456789".toCharArray();
        assertEquals(123456789, NumberInput.parseInt(chars, 0, 9));
        assertEquals(1, NumberInput.parseInt(chars, 0, 1));
        assertEquals(23, NumberInput.parseInt(chars, 1, 2));
        assertEquals(345, NumberInput.parseInt(chars, 2, 3));
        assertEquals(4567, NumberInput.parseInt(chars, 3, 4));
        assertEquals(56789, NumberInput.parseInt(chars, 4, 5));
    }

    @Test
    public void testParseIntString() {
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(42, NumberInput.parseInt("42"));
        assertEquals(-42, NumberInput.parseInt("-42"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testParseLongCharArray() {
        char[] chars = "123456789012345678".toCharArray();
        long expected = 123456789012345678L;
        assertEquals(expected, NumberInput.parseLong(chars, 0, 18));

        char[] tenDigits = "1000000000".toCharArray();
        assertEquals(1000000000L, NumberInput.parseLong(tenDigits, 0, 10));
    }

    @Test
    public void testParseLongString() {
        assertEquals(0L, NumberInput.parseLong("0"));
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        assertEquals(-123456789L, NumberInput.parseLong("-123456789"));
        assertEquals(123456789012345L, NumberInput.parseLong("123456789012345"));
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void testInLongRangeCharArray() {
        char[] maxLong = String.valueOf(Long.MAX_VALUE).toCharArray();
        assertTrue(NumberInput.inLongRange(maxLong, 0, maxLong.length, false));

        char[] aboveMax = "9223372036854775808".toCharArray();
        assertFalse(NumberInput.inLongRange(aboveMax, 0, aboveMax.length, false));

        char[] minLongNoSign = String.valueOf(Long.MIN_VALUE).substring(1).toCharArray();
        assertTrue(NumberInput.inLongRange(minLongNoSign, 0, minLongNoSign.length, true));

        char[] aboveMin = "9223372036854775809".toCharArray();
        assertFalse(NumberInput.inLongRange(aboveMin, 0, aboveMin.length, true));

        char[] shortVal = "12345".toCharArray();
        assertTrue(NumberInput.inLongRange(shortVal, 0, shortVal.length, false));
    }

    @Test
    public void testInLongRangeString() {
        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MAX_VALUE), false));
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));

        assertTrue(NumberInput.inLongRange(String.valueOf(Long.MIN_VALUE).substring(1), true));
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));

        assertTrue(NumberInput.inLongRange("999", false));
        assertFalse(NumberInput.inLongRange("10000000000000000000", false));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(5, NumberInput.parseAsInt(null, 5));
        assertEquals(5, NumberInput.parseAsInt("", 5));
        assertEquals(5, NumberInput.parseAsInt("   ", 5));
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        assertEquals(123, NumberInput.parseAsInt("+123", 0));
        assertEquals(-123, NumberInput.parseAsInt("-123", 0));
        assertEquals(12, NumberInput.parseAsInt("12.7", 0));
        assertEquals(99, NumberInput.parseAsInt("invalid_int", 99));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(5L, NumberInput.parseAsLong(null, 5L));
        assertEquals(5L, NumberInput.parseAsLong("", 5L));
        assertEquals(5L, NumberInput.parseAsLong("   ", 5L));
        assertEquals(123456789012L, NumberInput.parseAsLong("123456789012", 0L));
        assertEquals(123456789012L, NumberInput.parseAsLong("+123456789012", 0L));
        assertEquals(-123456789012L, NumberInput.parseAsLong("-123456789012", 0L));
        assertEquals(123L, NumberInput.parseAsLong("123.89", 0L));
        assertEquals(77L, NumberInput.parseAsLong("invalid_long", 77L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(3.5, NumberInput.parseAsDouble(null, 3.5), 0.0);
        assertEquals(3.5, NumberInput.parseAsDouble("", 3.5), 0.0);
        assertEquals(3.5, NumberInput.parseAsDouble("  ", 3.5), 0.0);
        assertEquals(12.34, NumberInput.parseAsDouble("12.34", 0.0), 0.0001);
        assertEquals(-56.78, NumberInput.parseAsDouble("-56.78", 0.0), 0.0001);
        assertEquals(9.9, NumberInput.parseAsDouble("not_a_double", 9.9), 0.0);
    }

    @Test
    public void testParseDoubleAndNastyDouble() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
        assertEquals(123.456, NumberInput.parseDouble("123.456"), 0.0001);
    }

    @Test
    public void testParseBigDecimal() {
        assertEquals(new BigDecimal("123.456789"), NumberInput.parseBigDecimal("123.456789"));

        char[] chars = "test123.45extra".toCharArray();
        assertEquals(new BigDecimal("123.45"), NumberInput.parseBigDecimal(chars, 4, 6));

        char[] fullChars = "987.654".toCharArray();
        assertEquals(new BigDecimal("987.654"), NumberInput.parseBigDecimal(fullChars));

        try {
            NumberInput.parseBigDecimal("invalid");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertTrue(e.getMessage().contains("can not be represented as BigDecimal"));
        }
    }
}
```

com/fasterxml/jackson/core/util/TextBufferTest.java
```java
package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;
import org.junit.Test;

import static org.junit.Assert.*;

public class TextBufferTest {

    @Test
    public void testResetWithEmpty() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        assertEquals(0, tb.size());
        assertEquals("", tb.contentsAsString());
        assertArrayEquals(new char[0], tb.contentsAsArray());
        assertEquals(0, tb.getTextOffset());
        assertTrue(tb.hasTextAsCharacters());
    }

    @Test
    public void testResetWithShared() {
        TextBuffer tb = new TextBuffer(null);
        char[] shared = "SampleTextBuffer".toCharArray();
        tb.resetWithShared(shared, 6, 4);

        assertEquals(4, tb.size());
        assertEquals(6, tb.getTextOffset());
        assertTrue(tb.hasTextAsCharacters());
        assertEquals("Text", tb.contentsAsString());
        assertArrayEquals("Text".toCharArray(), tb.contentsAsArray());
    }

    @Test
    public void testResetWithString() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("JacksonCore");

        assertEquals(11, tb.size());
        assertFalse(tb.hasTextAsCharacters());
        assertEquals("JacksonCore", tb.contentsAsString());
        assertEquals("JacksonCore", tb.toString());
        assertArrayEquals("JacksonCore".toCharArray(), tb.getTextBuffer());
        assertTrue(tb.hasTextAsCharacters());
    }

    @Test
    public void testResetWithCopy() {
        TextBuffer tb = new TextBuffer(null);
        char[] src = "CopySourceArray".toCharArray();
        tb.resetWithCopy(src, 4, 6);

        assertEquals(6, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertEquals("Source", tb.contentsAsString());
    }

    @Test
    public void testAppendChar() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        tb.append('A');
        tb.append('B');
        tb.append('C');

        assertEquals(3, tb.size());
        assertEquals("ABC", tb.contentsAsString());
    }

    @Test
    public void testAppendCharArrayAndString() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();

        char[] prefix = "Hello ".toCharArray();
        tb.append(prefix, 0, prefix.length);

        String suffix = "World!";
        tb.append(suffix, 0, suffix.length());

        assertEquals(12, tb.size());
        assertEquals("Hello World!", tb.contentsAsString());
    }

    @Test
    public void testEnsureNotShared() {
        TextBuffer tb = new TextBuffer(null);
        char[] shared = "ShareMe".toCharArray();
        tb.resetWithShared(shared, 0, shared.length);
        assertTrue(tb.hasTextAsCharacters());

        tb.ensureNotShared();
        assertEquals(0, tb.getTextOffset());
        assertEquals("ShareMe", tb.contentsAsString());
    }

    @Test
    public void testCurrentSegmentAndFinish() {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        assertNotNull(seg);
        assertTrue(seg.length > 0);

        seg[0] = 'a';
        seg[1] = 'b';
        tb.setCurrentLength(2);
        assertEquals(2, tb.getCurrentSegmentSize());

        char[] nextSeg = tb.finishCurrentSegment();
        assertNotNull(nextSeg);
        nextSeg[0] = 'c';
        tb.setCurrentLength(1);

        assertEquals(3, tb.size());
        assertEquals("abc", tb.contentsAsString());
    }

    @Test
    public void testExpandCurrentSegment() {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        int initialLen = seg.length;

        char[] expanded = tb.expandCurrentSegment();
        assertTrue(expanded.length > initialLen);
        assertSame(tb.getCurrentSegment(), expanded);
    }

    @Test
    public void testContentsAsDecimalAndDouble() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("123.45");

        assertEquals(new BigDecimal("123.45"), tb.contentsAsDecimal());
        assertEquals(123.45, tb.contentsAsDouble(), 0.0001);
    }

    @Test
    public void testReleaseBuffers() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("TemporaryContent");
        tb.releaseBuffers();

        assertEquals(0, tb.size());
        assertEquals("", tb.contentsAsString());
    }

    @Test
    public void testLargeAppendCausingExpansion() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2500; i++) {
            sb.append('x');
        }
        String largeStr = sb.toString();
        tb.append(largeStr, 0, largeStr.length());

        assertEquals(2500, tb.size());
        assertEquals(largeStr, tb.contentsAsString());
    }
}
```