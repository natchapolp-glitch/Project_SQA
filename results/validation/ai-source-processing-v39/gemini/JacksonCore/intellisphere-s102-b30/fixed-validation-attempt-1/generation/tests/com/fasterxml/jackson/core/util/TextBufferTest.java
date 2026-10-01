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
