package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;
import org.junit.Test;
import static org.junit.Assert.*;

public class TextBufferTest {

    @Test
    public void testEmptyBuffer() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        assertEquals(0, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertEquals("", tb.contentsAsString());
        assertEquals(0, tb.contentsAsArray().length);
    }

    @Test
    public void testResetWithShared() {
        TextBuffer tb = new TextBuffer(null);
        char[] shared = "abcdefghij".toCharArray();
        tb.resetWithShared(shared, 2, 5);

        assertEquals(5, tb.size());
        assertEquals(2, tb.getTextOffset());
        assertTrue(tb.hasTextAsCharacters());
        assertSame(shared, tb.getTextBuffer());
        assertEquals("cdefg", tb.contentsAsString());
        assertArrayEquals("cdefg".toCharArray(), tb.contentsAsArray());
    }

    @Test
    public void testResetWithCopy() {
        TextBuffer tb = new TextBuffer(null);
        char[] source = "SampleText".toCharArray();
        tb.resetWithCopy(source, 6, 4);

        assertEquals(4, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertEquals("Text", tb.contentsAsString());
    }

    @Test
    public void testResetWithString() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("JacksonParser");

        assertEquals(13, tb.size());
        assertEquals(0, tb.getTextOffset());
        assertFalse(tb.hasTextAsCharacters());
        assertEquals("JacksonParser", tb.contentsAsString());
        assertArrayEquals("JacksonParser".toCharArray(), tb.getTextBuffer());
        assertTrue(tb.hasTextAsCharacters());
    }

    @Test
    public void testEnsureNotShared() {
        TextBuffer tb = new TextBuffer(null);
        char[] shared = "sharedText".toCharArray();
        tb.resetWithShared(shared, 0, shared.length);

        tb.ensureNotShared();
        assertEquals(0, tb.getTextOffset());
        assertNotSame(shared, tb.getTextBuffer());
        assertEquals("sharedText", tb.contentsAsString());
    }

    

    

    @Test
    public void testUnshareOnAppend() {
        TextBuffer tb = new TextBuffer(null);
        char[] shared = "Prefix".toCharArray();
        tb.resetWithShared(shared, 0, shared.length);

        tb.append('-');
        tb.append("Suffix", 0, 6);

        assertEquals("Prefix-Suffix", tb.contentsAsString());
    }

    @Test
    public void testMultiSegmentAggregation() {
        TextBuffer tb = new TextBuffer(null);
        char[] firstSegment = tb.emptyAndGetCurrentSegment();
        assertNotNull(firstSegment);

        // Fill current segment completely
        int segLen = firstSegment.length;
        for (int i = 0; i < segLen; i++) {
            firstSegment[i] = 'X';
        }
        tb.setCurrentLength(segLen);

        char[] nextSegment = tb.finishCurrentSegment();
        assertNotNull(nextSegment);
        assertNotSame(firstSegment, nextSegment);

        nextSegment[0] = 'Y';
        tb.setCurrentLength(1);

        assertEquals(segLen + 1, tb.size());
        char[] combined = tb.contentsAsArray();
        assertEquals(segLen + 1, combined.length);
        assertEquals('X', combined[0]);
        assertEquals('Y', combined[segLen]);
    }

    @Test
    public void testExpandCurrentSegment() {
        TextBuffer tb = new TextBuffer(null);
        char[] seg = tb.emptyAndGetCurrentSegment();
        int initialLen = seg.length;

        char[] expanded = tb.expandCurrentSegment();
        assertTrue(expanded.length > initialLen);
        assertSame(expanded, tb.getCurrentSegment());
    }

    @Test
    public void testContentsAsDecimalAndDouble() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithString("123.456");

        assertEquals(new BigDecimal("123.456"), tb.contentsAsDecimal());
        assertEquals(123.456, tb.contentsAsDouble(), 0.0001);

        // Using shared segment
        char[] chars = "987.65".toCharArray();
        tb.resetWithShared(chars, 0, chars.length);
        assertEquals(new BigDecimal("987.65"), tb.contentsAsDecimal());
    }

    @Test
    public void testReleaseBuffersWithRecycler() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);

        char[] buf = tb.emptyAndGetCurrentSegment();
        assertNotNull(buf);
        tb.setCurrentLength(10);

        tb.releaseBuffers();
        assertEquals(0, tb.size());

        // Reacquiring segment should reuse buffer from recycler
        char[] reusedBuf = tb.emptyAndGetCurrentSegment();
        assertNotNull(reusedBuf);
    }

    @Test
    public void testGetCurrentSegmentUnshares() {
        TextBuffer tb = new TextBuffer(null);
        char[] shared = "original".toCharArray();
        tb.resetWithShared(shared, 0, shared.length);

        char[] current = tb.getCurrentSegment();
        assertNotNull(current);
        assertEquals(0, tb.getTextOffset());
        assertEquals("original", tb.contentsAsString());
    }

    
}
