`inLongRange`, `parseInt`, `parseLong` and `parseBigDecimal` (including the char[] and String overloads) are covered in the first file. `TextBuffer`'s public methods are covered in the second, and its private helpers (`_charArray`, `findBuffer`, `clearSegments`, `expand`, `unshare`, `buildResultArray`) are reached through the public API. There are 30 test methods in total: 14 in the first file and 16 in the second.

The build uses JUnit 4.8.2 at Java 1.6 source level, so the tests use `org.junit.Test` and `Assert`, try/fail/catch instead of `assertThrows`, and no diamond operator or lambdas.

Two assumptions are worth checking before you run them:
- **Fresh `TextBuffer`:** the tests rely on `new TextBuffer(null)` allocating 1000-char segments, and on a freshly constructed buffer having `_inputStart == 0` (so it counts as "shared" until it is reset).
- **`BufferRecycler`:** the tests assume `BufferRecycler` has a public no-arg constructor. If it does not compile, that is the missing API.

`com/fasterxml/jackson/core/io/GeneratedNumberInputTest.java`
```java
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
```

`com/fasterxml/jackson/core/util/GeneratedTextBufferTest.java`
```java
package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class GeneratedTextBufferTest
{
    // With a null allocator, buffers are allocated with MIN_SEGMENT_LEN (1000) chars.
    private static TextBuffer newBuffer()
    {
        TextBuffer tb = new TextBuffer(null);
        tb.emptyAndGetCurrentSegment();
        return tb;
    }

    private static char[] pattern(int len)
    {
        char[] c = new char[len];
        for (int i = 0; i < len; ++i) {
            c[i] = (char) ('a' + (i % 26));
        }
        return c;
    }

    @Test
    public void testEmptyBuffer()
    {
        TextBuffer tb = new TextBuffer(null);
        Assert.assertEquals(0, tb.size());
        Assert.assertEquals(0, tb.getTextOffset());
        Assert.assertEquals("", tb.contentsAsString());
        Assert.assertEquals("", tb.toString());
        Assert.assertTrue(tb.hasTextAsCharacters());

        tb.resetWithEmpty();
        Assert.assertEquals(0, tb.size());
        Assert.assertEquals(0, tb.getTextOffset());
        Assert.assertEquals("", tb.contentsAsString());
        Assert.assertTrue(tb.hasTextAsCharacters());
    }

    @Test
    public void testAppendCharCharArrayAndString()
    {
        TextBuffer tb = new TextBuffer(null);
        tb.append('a');
        tb.append("xbcdx".toCharArray(), 1, 3);
        tb.append("--hello--", 2, 5);
        Assert.assertEquals(9, tb.size());
        Assert.assertEquals(9, tb.getCurrentSegmentSize());
        Assert.assertEquals("abcdhello", tb.contentsAsString());
        Assert.assertEquals("abcdhello", tb.toString());
    }

    @Test
    public void testAppendLargeContentSpansSegments()
    {
        char[] src = pattern(2500);

        // char[] append, then linear array access
        TextBuffer tb1 = newBuffer();
        tb1.append(src, 0, src.length);
        Assert.assertEquals(2500, tb1.size());
        char[] arr = tb1.getTextBuffer();
        Assert.assertEquals(new String(src), new String(arr, 0, 2500));
        Assert.assertEquals(0, tb1.getTextOffset());
        Assert.assertEquals(new String(src), tb1.contentsAsString());

        // String append, then String access
        String s = new String(src);
        TextBuffer tb2 = newBuffer();
        tb2.append(s, 0, s.length());
        Assert.assertEquals(2500, tb2.size());
        Assert.assertEquals(s, tb2.contentsAsString());
        Assert.assertEquals(s, new String(tb2.contentsAsArray()));
    }

    @Test
    public void testAppendManySingleChars()
    {
        TextBuffer tb = newBuffer();
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 1001; ++i) {
            char c = (char) ('A' + (i % 26));
            tb.append(c);
            expected.append(c);
        }
        Assert.assertEquals(1001, tb.size());
        Assert.assertEquals(1, tb.getCurrentSegmentSize());
        Assert.assertEquals(expected.toString(), tb.contentsAsString());
    }

    @Test
    public void testResetWithString()
    {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("abc");
        Assert.assertEquals(3, tb.size());
        Assert.assertEquals(0, tb.getTextOffset());
        Assert.assertFalse(tb.hasTextAsCharacters());
        Assert.assertEquals("abc", tb.contentsAsString());
        Assert.assertEquals("abc", new String(tb.getTextBuffer()));
        Assert.assertTrue(tb.hasTextAsCharacters());
        Assert.assertEquals(3, tb.size());
        Assert.assertEquals("abc", new String(tb.contentsAsArray()));
    }

    @Test
    public void testResetWithShared()
    {
        char[] buf = "xxhelloyy".toCharArray();
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithShared(buf, 2, 5);
        Assert.assertEquals(5, tb.size());
        Assert.assertEquals(2, tb.getTextOffset());
        Assert.assertTrue(tb.hasTextAsCharacters());
        Assert.assertSame(buf, tb.getTextBuffer());
        Assert.assertEquals("hello", tb.contentsAsString());

        TextBuffer tb2 = new TextBuffer(null);
        tb2.resetWithShared(buf, 2, 5);
        Assert.assertEquals("hello", new String(tb2.contentsAsArray()));

        TextBuffer tb3 = new TextBuffer(null);
        tb3.resetWithShared("abc".toCharArray(), 0, 3);
        Assert.assertEquals("abc", new String(tb3.contentsAsArray()));

        TextBuffer tb4 = new TextBuffer(null);
        tb4.resetWithShared(buf, 3, 0);
        Assert.assertEquals(0, tb4.size());
        Assert.assertEquals(0, tb4.contentsAsArray().length);
        Assert.assertEquals("", tb4.contentsAsString());
    }

    @Test
    public void testResetWithCopy()
    {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithCopy("abcde".toCharArray(), 1, 3);
        Assert.assertEquals(3, tb.size());
        Assert.assertEquals(0, tb.getTextOffset());
        char[] b = tb.getTextBuffer();
        Assert.assertEquals('b', b[0]);
        Assert.assertEquals('c', b[1]);
        Assert.assertEquals('d', b[2]);
        Assert.assertEquals("bcd", tb.contentsAsString());

        tb.resetWithCopy("xyz".toCharArray(), 0, 3);
        Assert.assertEquals(3, tb.size());
        Assert.assertEquals("xyz", tb.contentsAsString());
    }

    @Test
    public void testEnsureNotSharedAndAppendToShared()
    {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithShared("hello".toCharArray(), 0, 5);
        tb.ensureNotShared();
        Assert.assertEquals(0, tb.getTextOffset());
        Assert.assertEquals(5, tb.size());
        tb.ensureNotShared(); // no-op when already unshared
        Assert.assertEquals(5, tb.size());
        Assert.assertEquals("hello", tb.contentsAsString());
        tb.append('!');
        Assert.assertEquals("hello!", tb.contentsAsString());

        char[] buf = "abcde".toCharArray();
        TextBuffer tb2 = new TextBuffer(null);
        tb2.resetWithShared(buf, 1, 3);
        tb2.append("XY", 0, 2);
        Assert.assertEquals("bcdXY", tb2.contentsAsString());
        Assert.assertEquals("abcde", new String(buf));

        TextBuffer tb3 = new TextBuffer(null);
        tb3.resetWithShared(buf, 1, 3);
        tb3.append("Z".toCharArray(), 0, 1);
        Assert.assertEquals("bcdZ", tb3.contentsAsString());
        Assert.assertEquals("abcde", new String(buf));
    }

    @Test
    public void testResetWithEmpty()
    {
        TextBuffer tb = newBuffer();
        tb.append("abc", 0, 3);
        Assert.assertEquals("abc", tb.contentsAsString());
        tb.resetWithEmpty();
        Assert.assertEquals(0, tb.size());
        Assert.assertEquals("", tb.contentsAsString());

        // reset with multiple segments in use
        tb.append(pattern(2500), 0, 2500);
        Assert.assertEquals(2500, tb.size());
        tb.resetWithEmpty();
        Assert.assertEquals(0, tb.size());
        tb.append('z');
        Assert.assertEquals(1, tb.size());
        Assert.assertEquals("z", tb.contentsAsString());
    }

    @Test
    public void testCurrentSegmentAccess()
    {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        Assert.assertEquals(1000, seg.length);
        seg[0] = 'a';
        seg[1] = 'b';
        seg[2] = 'c';
        tb.setCurrentLength(3);
        Assert.assertEquals(3, tb.size());
        Assert.assertEquals(3, tb.getCurrentSegmentSize());
        Assert.assertSame(seg, tb.getCurrentSegment());
        Assert.assertEquals("abc", tb.contentsAsString());

        char[] again = tb.emptyAndGetCurrentSegment();
        Assert.assertSame(seg, again);
        Assert.assertEquals(0, tb.size());
        Assert.assertEquals(0, tb.getCurrentSegmentSize());

        // fresh buffer: getCurrentSegment allocates
        TextBuffer fresh = new TextBuffer(null);
        char[] s2 = fresh.getCurrentSegment();
        Assert.assertNotNull(s2);
        Assert.assertEquals(1000, s2.length);
        Assert.assertEquals(0, fresh.getCurrentSegmentSize());
    }

    @Test
    public void testGetCurrentSegmentWhenFullExpands()
    {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        tb.setCurrentLength(seg.length);
        char[] next = tb.getCurrentSegment();
        Assert.assertNotSame(seg, next);
        Assert.assertEquals(1500, next.length);
        Assert.assertEquals(0, tb.getCurrentSegmentSize());
        Assert.assertEquals(1000, tb.size());
    }

    @Test
    public void testFinishCurrentSegment()
    {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        Arrays.fill(seg, 'x');
        tb.setCurrentLength(seg.length);
        char[] next = tb.finishCurrentSegment();
        Assert.assertEquals(1500, next.length);
        Assert.assertEquals(0, tb.getCurrentSegmentSize());
        Assert.assertEquals(1000, tb.size());
        next[0] = 'y';
        tb.setCurrentLength(1);
        Assert.assertEquals(1001, tb.size());
        String s = tb.contentsAsString();
        Assert.assertEquals(1001, s.length());
        Assert.assertEquals('x', s.charAt(0));
        Assert.assertEquals('x', s.charAt(999));
        Assert.assertEquals('y', s.charAt(1000));
    }

    @Test
    public void testExpandCurrentSegment()
    {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        seg[0] = 'a';
        char[] bigger = tb.expandCurrentSegment();
        Assert.assertNotSame(seg, bigger);
        Assert.assertEquals(1500, bigger.length);
        Assert.assertEquals('a', bigger[0]);

        // grow until the maximum segment length, then beyond it by one char
        while (bigger.length < 0x40000) {
            bigger = tb.expandCurrentSegment();
        }
        Assert.assertEquals(0x40000, bigger.length);
        Assert.assertEquals('a', bigger[0]);
        bigger = tb.expandCurrentSegment();
        Assert.assertEquals(0x40001, bigger.length);
        Assert.assertEquals('a', bigger[0]);
    }

    @Test
    public void testContentsAsDoubleAndDecimal()
    {
        // from String value
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("3.5");
        Assert.assertEquals(3.5, tb.contentsAsDouble(), 0.0);
        Assert.assertEquals(new BigDecimal("3.5"), tb.contentsAsDecimal());

        // from shared buffer
        TextBuffer shared = new TextBuffer(null);
        shared.resetWithShared("x12.50y".toCharArray(), 1, 5);
        Assert.assertEquals(new BigDecimal("12.50"), shared.contentsAsDecimal());
        Assert.assertEquals(12.5, shared.contentsAsDouble(), 0.0);

        // from single segment
        TextBuffer single = newBuffer();
        single.append("7.25", 0, 4);
        Assert.assertEquals(new BigDecimal("7.25"), single.contentsAsDecimal());
        Assert.assertEquals(7.25, single.contentsAsDouble(), 0.0);

        // from multiple segments (aggregated, then cached array)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1200; ++i) {
            sb.append('1');
        }
        TextBuffer multi = newBuffer();
        multi.append(sb.toString(), 0, sb.length());
        Assert.assertEquals(new BigDecimal(sb.toString()), multi.contentsAsDecimal());
        Assert.assertEquals(new BigDecimal(sb.toString()), multi.contentsAsDecimal());

        // invalid content
        TextBuffer bad = new TextBuffer(null);
        bad.resetWithString("abc");
        try {
            bad.contentsAsDouble();
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
        try {
            bad.contentsAsDecimal();
            Assert.fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testReleaseBuffers()
    {
        TextBuffer tb = new TextBuffer(new BufferRecycler());
        tb.append("abc", 0, 3);
        Assert.assertEquals("abc", tb.contentsAsString());
        tb.releaseBuffers();
        Assert.assertEquals(0, tb.size());
        Assert.assertEquals("", tb.contentsAsString());

        // still usable afterwards
        char[] seg = tb.emptyAndGetCurrentSegment();
        Assert.assertNotNull(seg);
        tb.append('q');
        Assert.assertEquals("q", tb.contentsAsString());

        // no allocator
        TextBuffer noAlloc = new TextBuffer(null);
        noAlloc.releaseBuffers();
        Assert.assertEquals(0, noAlloc.size());
        Assert.assertEquals("", noAlloc.contentsAsString());
    }

    @Test
    public void testHasTextAsCharactersReflectsCaching()
    {
        TextBuffer tb = newBuffer();
        tb.append("abc", 0, 3);
        Assert.assertTrue(tb.hasTextAsCharacters());
        Assert.assertEquals("abc", tb.contentsAsString());
        Assert.assertFalse(tb.hasTextAsCharacters()); // only a cached String exists
        tb.append('d');                                // append clears cached results
        Assert.assertTrue(tb.hasTextAsCharacters());
        Assert.assertEquals("abcd", new String(tb.contentsAsArray()));
        Assert.assertTrue(tb.hasTextAsCharacters());
        Assert.assertEquals("abcd", tb.contentsAsString());
        Assert.assertEquals(4, tb.size());
    }
}
```