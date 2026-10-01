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
