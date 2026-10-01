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
