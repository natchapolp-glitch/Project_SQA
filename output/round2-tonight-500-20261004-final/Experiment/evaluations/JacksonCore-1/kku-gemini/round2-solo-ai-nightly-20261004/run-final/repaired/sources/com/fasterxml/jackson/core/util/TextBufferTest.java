package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class TextBufferTest {

    @Test
    public void testResetAndEmpty() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);
        tb.resetWithEmpty();
        assertEquals(0, tb.size());
        assertFalse(tb.hasTextAsCharacters());
        assertEquals("", tb.contentsAsString());
    }

    @Test
    public void testResetWithShared() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);
        char[] shared = "Hello World".toCharArray();
        tb.resetWithShared(shared, 6, 5);
        assertEquals(5, tb.size());
        assertEquals("World", tb.contentsAsString());
        assertNotNull(tb.getTextBuffer());
    }

    @Test
    public void testAppendAndContents() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);
        tb.resetWithEmpty();
        tb.emptyAndGetCurrentSegment();
        tb.append('A');
        tb.append('B');
        tb.append(new char[]{'C', 'D'}, 0, 2);
        tb.append("EF", 0, 2);
        assertEquals(6, tb.size());
        assertEquals("ABCDEF", tb.contentsAsString());
        assertArrayEquals(new char[]{'A', 'B', 'C', 'D', 'E', 'F'}, tb.contentsAsArray());
    }

    @Test
    public void testResetWithString() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);
        tb.resetWithString("Jackson");
        assertEquals(7, tb.size());
        assertEquals("Jackson", tb.contentsAsString());
    }

    @Test
    public void testSegmentOperations() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);
        tb.resetWithEmpty();
        char[] seg = tb.emptyAndGetCurrentSegment();
        assertNotNull(seg);
        assertTrue(tb.getCurrentSegmentSize() >= 0);
        tb.setCurrentLength(3);
        assertEquals(3, tb.getCurrentSegmentSize());
        char[] expanded = tb.expandCurrentSegment();
        assertNotNull(expanded);
    }

    @Test
    public void testReleaseBuffers() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer tb = new TextBuffer(recycler);
        tb.resetWithString("Test");
        tb.releaseBuffers();
        assertEquals(0, tb.size());
    }
}
