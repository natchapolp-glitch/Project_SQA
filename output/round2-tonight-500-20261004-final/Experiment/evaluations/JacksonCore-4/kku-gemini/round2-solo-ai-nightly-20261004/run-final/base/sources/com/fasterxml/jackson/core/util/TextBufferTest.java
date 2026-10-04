package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextBufferTest {

    @Test
    public void testResetWithEmptyAndSize() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer buffer = new TextBuffer(recycler);
        buffer.resetWithEmpty();
        assertEquals(0, buffer.size());
        assertEquals("", buffer.contentsAsString());
        assertFalse(buffer.hasTextAsCharacters());
    }

    @Test
    public void testResetWithSharedAndAccessors() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer buffer = new TextBuffer(recycler);
        char[] shared = new char[] { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
        buffer.resetWithShared(shared, 6, 5);
        assertEquals(5, buffer.size());
        assertEquals(6, buffer.getTextOffset());
        assertTrue(buffer.hasTextAsCharacters());
        assertEquals("World", buffer.contentsAsString());
        assertArrayEquals(new char[] { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' }, buffer.getTextBuffer());
    }

    @Test
    public void testResetWithCopyAndAppend() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer buffer = new TextBuffer(recycler);
        char[] src = new char[] { 'A', 'B', 'C' };
        buffer.resetWithCopy(src, 0, 3);
        assertEquals(3, buffer.size());
        assertEquals("ABC", buffer.contentsAsString());

        buffer.append('D');
        assertEquals(4, buffer.size());
        assertEquals("ABCD", buffer.contentsAsString());
    }

    @Test
    public void testResetWithString() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer buffer = new TextBuffer(recycler);
        buffer.resetWithString("Jackson");
        assertEquals(7, buffer.size());
        assertEquals("Jackson", buffer.contentsAsString());
        assertFalse(buffer.hasTextAsCharacters());
        assertNotNull(buffer.contentsAsArray());
        assertTrue(buffer.hasTextAsCharacters());
    }

    @Test
    public void testSegmentExpansion() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer buffer = new TextBuffer(recycler);
        buffer.resetWithEmpty();
        char[] seg = buffer.emptyAndGetCurrentSegment();
        assertNotNull(seg);
        assertTrue(seg.length >= TextBuffer.MIN_SEGMENT_LEN);

        char[] expanded = buffer.expandCurrentSegment();
        assertNotNull(expanded);
        assertTrue(expanded.length >= seg.length);
    }

    @Test
    public void testReleaseBuffers() {
        BufferRecycler recycler = new BufferRecycler();
        TextBuffer buffer = new TextBuffer(recycler);
        buffer.resetWithEmpty();
        buffer.emptyAndGetCurrentSegment();
        buffer.releaseBuffers();
        assertEquals(0, buffer.size());
    }
}
