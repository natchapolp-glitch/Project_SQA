com/fasterxml/jackson/core/io/NumberInputTest.java
```java
package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

    @Test
    public void testParseIntFromChars() {
        char[] digits = "prefix123456789suffix".toCharArray();
        assertEquals(1, NumberInput.parseInt(digits, 6, 1));
        assertEquals(12, NumberInput.parseInt(digits, 6, 2));
        assertEquals(123, NumberInput.parseInt(digits, 6, 3));
        assertEquals(1234, NumberInput.parseInt(digits, 6, 4));
        assertEquals(12345, NumberInput.parseInt(digits, 6, 5));
        assertEquals(123456, NumberInput.parseInt(digits, 6, 6));
        assertEquals(1234567, NumberInput.parseInt(digits, 6, 7));
        assertEquals(12345678, NumberInput.parseInt(digits, 6, 8));
        assertEquals(123456789, NumberInput.parseInt(digits, 6, 9));
    }

    @Test
    public void testParseIntFromString() {
        assertEquals(0, NumberInput.parseInt("0"));
        assertEquals(7, NumberInput.parseInt("7"));
        assertEquals(42, NumberInput.parseInt("42"));
        assertEquals(123456789, NumberInput.parseInt("123456789"));
        assertEquals(-1, NumberInput.parseInt("-1"));
        assertEquals(-42, NumberInput.parseInt("-42"));
        assertEquals(-123456789, NumberInput.parseInt("-123456789"));
        // Boundary fallback to JDK Integer.parseInt (> 9 or 10 chars)
        assertEquals(Integer.MAX_VALUE, NumberInput.parseInt(String.valueOf(Integer.MAX_VALUE)));
        assertEquals(Integer.MIN_VALUE, NumberInput.parseInt(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test(expected = NumberFormatException.class)
    public void testParseIntFromStringInvalid() {
        NumberInput.parseInt("12a34");
    }

    @Test
    public void testParseLongFromChars() {
        String numStr = "123456789012345678";
        char[] chars = numStr.toCharArray();
        assertEquals(123456789012345678L, NumberInput.parseLong(chars, 0, chars.length));

        char[] offsetChars = ("abc" + numStr).toCharArray();
        assertEquals(123456789012345678L, NumberInput.parseLong(offsetChars, 3, chars.length));
    }

    @Test
    public void testParseLongFromString() {
        assertEquals(0L, NumberInput.parseLong("0"));
        assertEquals(123456789L, NumberInput.parseLong("123456789"));
        assertEquals(-123456789L, NumberInput.parseLong("-123456789"));
        assertEquals(1234567890123456L, NumberInput.parseLong("1234567890123456"));
        assertEquals(Long.MAX_VALUE, NumberInput.parseLong(String.valueOf(Long.MAX_VALUE)));
        assertEquals(Long.MIN_VALUE, NumberInput.parseLong(String.valueOf(Long.MIN_VALUE)));
    }

    @Test
    public void testInLongRange() {
        // Shorter than 19 digits fits in long
        assertTrue(NumberInput.inLongRange("1234567890", false));
        assertTrue(NumberInput.inLongRange("1234567890", true));

        // Longer than 19 digits does not fit in long
        assertFalse(NumberInput.inLongRange("12345678901234567890", false));
        assertFalse(NumberInput.inLongRange("12345678901234567890", true));

        // Exactly 19 digits: MAX_LONG is 9223372036854775807
        assertTrue(NumberInput.inLongRange("9223372036854775807", false));
        assertFalse(NumberInput.inLongRange("9223372036854775808", false));

        // MIN_LONG without sign is 9223372036854775808
        assertTrue(NumberInput.inLongRange("9223372036854775808", true));
        assertFalse(NumberInput.inLongRange("9223372036854775809", true));

        // Array variant
        char[] maxChars = "9223372036854775807".toCharArray();
        assertTrue(NumberInput.inLongRange(maxChars, 0, maxChars.length, false));
        char[] overMaxChars = "9223372036854775808".toCharArray();
        assertFalse(NumberInput.inLongRange(overMaxChars, 0, overMaxChars.length, false));
    }

    @Test
    public void testParseAsInt() {
        assertEquals(10, NumberInput.parseAsInt(null, 10));
        assertEquals(10, NumberInput.parseAsInt("   ", 10));
        assertEquals(123, NumberInput.parseAsInt("123", 0));
        assertEquals(123, NumberInput.parseAsInt("+123", 0));
        assertEquals(-123, NumberInput.parseAsInt("-123", 0));
        assertEquals(12, NumberInput.parseAsInt("12.85", 0));
        assertEquals(-12, NumberInput.parseAsInt("-12.85", 0));
        assertEquals(5, NumberInput.parseAsInt("not-a-number", 5));
    }

    @Test
    public void testParseAsLong() {
        assertEquals(99L, NumberInput.parseAsLong(null, 99L));
        assertEquals(99L, NumberInput.parseAsLong(" ", 99L));
        assertEquals(1234567890123L, NumberInput.parseAsLong("1234567890123", 0L));
        assertEquals(1234567890123L, NumberInput.parseAsLong("+1234567890123", 0L));
        assertEquals(-1234567890123L, NumberInput.parseAsLong("-1234567890123", 0L));
        assertEquals(1234567890L, NumberInput.parseAsLong("1234567890.99", 0L));
        assertEquals(50L, NumberInput.parseAsLong("invalid", 50L));
    }

    @Test
    public void testParseAsDouble() {
        assertEquals(1.5, NumberInput.parseAsDouble(null, 1.5), 0.0001);
        assertEquals(1.5, NumberInput.parseAsDouble("   ", 1.5), 0.0001);
        assertEquals(123.456, NumberInput.parseAsDouble("123.456", 0.0), 0.0001);
        assertEquals(3.14, NumberInput.parseAsDouble("not_a_double", 3.14), 0.0001);
    }

    @Test
    public void testParseDoubleNastyValue() {
        assertEquals(Double.MIN_VALUE, NumberInput.parseDouble(NumberInput.NASTY_SMALL_DOUBLE), 0.0);
        assertEquals(1.234, NumberInput.parseDouble("1.234"), 0.0001);
    }

    @Test
    public void testParseBigDecimal() {
        BigDecimal expected = new BigDecimal("12345.67890");
        assertEquals(expected, NumberInput.parseBigDecimal("12345.67890"));

        char[] chars = "abc12345.67890def".toCharArray();
        assertEquals(expected, NumberInput.parseBigDecimal(chars, 3, 11));
        assertEquals(expected, NumberInput.parseBigDecimal("12345.67890".toCharArray()));

        try {
            NumberInput.parseBigDecimal("invalid-decimal");
            fail("Should throw NumberFormatException");
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
    public void testInitialEmptyBuffer() {
        TextBuffer tb = new TextBuffer(null);
        assertEquals(0, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertEquals("", tb.contentsAsString());
        assertEquals("", tb.toString());
        assertNotNull(tb.contentsAsArray());
        assertEquals(0, tb.contentsAsArray().length);
    }

    @Test
    public void testResetWithShared() {
        TextBuffer tb = new TextBuffer(null);
        char[] chars = "Hello World".toCharArray();
        tb.resetWithShared(chars, 6, 5);

        assertEquals(5, tb.size());
        assertEquals(6, tb.getTextOffset());
        assertTrue(tb.hasTextAsCharacters());
        assertSame(chars, tb.getTextBuffer());
        assertEquals("World", tb.contentsAsString());
        assertArrayEquals("World".toCharArray(), tb.contentsAsArray());
    }

    @Test
    public void testResetWithCopy() {
        TextBuffer tb = new TextBuffer(null);
        char[] chars = "abcdefgh".toCharArray();
        tb.resetWithCopy(chars, 2, 4);

        assertEquals(4, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertEquals("cdef", tb.contentsAsString());
        assertArrayEquals("cdef".toCharArray(), tb.contentsAsArray());
    }

    @Test
    public void testResetWithString() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("Jackson");

        assertEquals(7, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertFalse(tb.hasTextAsCharacters());
        assertEquals("Jackson", tb.contentsAsString());
        assertArrayEquals("Jackson".toCharArray(), tb.getTextBuffer());
        assertTrue(tb.hasTextAsCharacters());
    }

    @Test
    public void testAppendChar() {
        TextBuffer tb = new TextBuffer(null);
        tb.append('a');
        tb.append('b');
        tb.append('c');

        assertEquals(3, tb.size());
        assertEquals("abc", tb.contentsAsString());
    }

    @Test
    public void testAppendCharArray() {
        TextBuffer tb = new TextBuffer(null);
        char[] part1 = "12345".toCharArray();
        char[] part2 = "67890".toCharArray();

        tb.append(part1, 0, 5);
        tb.append(part2, 0, 5);

        assertEquals(10, tb.size());
        assertEquals("1234567890", tb.contentsAsString());
    }

    @Test
    public void testAppendString() {
        TextBuffer tb = new TextBuffer(null);
        tb.append("Hello ", 0, 6);
        tb.append("beautiful world!", 0, 9);

        assertEquals(15, tb.size());
        assertEquals("Hello beautiful", tb.contentsAsString());
    }

    @Test
    public void testEnsureNotShared() {
        TextBuffer tb = new TextBuffer(null);
        char[] chars = "TestString".toCharArray();
        tb.resetWithShared(chars, 0, chars.length);
        assertEquals(0, tb.getTextOffset());

        tb.ensureNotShared();
        assertEquals(0, tb.getTextOffset());
        assertEquals("TestString", tb.contentsAsString());
        assertNotSame(chars, tb.getTextBuffer());
    }

    @Test
    public void testGetCurrentSegmentAndEmptyAndGetCurrentSegment() {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        assertNotNull(seg);
        assertTrue(seg.length >= 1000);

        seg[0] = 'X';
        seg[1] = 'Y';
        tb.setCurrentLength(2);
        assertEquals(2, tb.getCurrentSegmentSize());
        assertEquals(2, tb.size());
        assertEquals("XY", tb.contentsAsString());

        char[] curr = tb.getCurrentSegment();
        assertNotNull(curr);
    }

    @Test
    public void testFinishCurrentSegmentAndMultiSegment() {
        TextBuffer tb = new TextBuffer(null);
        char[] seg1 = tb.emptyAndGetCurrentSegment();
        seg1[0] = 'A';
        tb.setCurrentLength(1);

        char[] seg2 = tb.finishCurrentSegment();
        assertNotNull(seg2);
        seg2[0] = 'B';
        tb.setCurrentLength(1);

        assertEquals("AB", tb.contentsAsString());
        assertArrayEquals("AB".toCharArray(), tb.contentsAsArray());
    }

    @Test
    public void testExpandCurrentSegment() {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        int initialLen = seg.length;
        char[] expanded = tb.expandCurrentSegment();
        assertTrue(expanded.length > initialLen);
    }

    @Test
    public void testContentsAsDecimalAndDouble() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("123.45");

        assertEquals(new BigDecimal("123.45"), tb.contentsAsDecimal());
        assertEquals(123.45, tb.contentsAsDouble(), 0.0001);

        tb.resetWithShared("678.9".toCharArray(), 0, 5);
        assertEquals(new BigDecimal("678.9"), tb.contentsAsDecimal());
        assertEquals(678.9, tb.contentsAsDouble(), 0.0001);
    }

    @Test
    public void testReleaseBuffers() {
        TextBuffer tb = new TextBuffer(null);
        tb.append("some data", 0, 9);
        assertEquals(9, tb.size());

        tb.releaseBuffers();
        assertEquals(0, tb.size());
        assertEquals("", tb.contentsAsString());
    }
}
```

### References
- Jackson Core: https://github.com/FasterXML/jackson-core
- JUnit 4 Documentation: https://junit.org/junit4/