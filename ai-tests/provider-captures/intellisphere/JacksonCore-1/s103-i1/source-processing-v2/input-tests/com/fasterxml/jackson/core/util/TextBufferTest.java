com/fasterxml/jackson/core/util/TextBufferTest.java
package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;

public class TextBufferTest {

    private BufferRecycler getRecycler() {
        return new BufferRecycler();
    }

    @Test
    public void testResetWithEmptyAndAppend() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.append('A');
        buf.append('B');
        assertEquals("AB", buf.contentsAsString());
    }

    @Test
    public void testResetWithSharedBuffer() {
        TextBuffer buf = new TextBuffer(getRecycler());
        char[] data = "Hello".toCharArray();
        buf.resetWithShared(data, 0, 5);
        assertEquals("Hello", buf.contentsAsString());
        assertSame(data, buf.getTextBuffer());
        assertEquals(0, buf.getTextOffset());
        assertEquals(5, buf.size());
    }

    @Test
    public void testResetWithCopy() {
        TextBuffer buf = new TextBuffer(getRecycler());
        char[] data = "World".toCharArray();
        buf.resetWithCopy(data, 1, 4);
        assertEquals("orld", buf.contentsAsString());
        assertNotSame(data, buf.getTextBuffer());
    }

    @Test
    public void testResetWithString() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithString("Test");
        assertEquals("Test", buf.toString());
        assertTrue(buf.hasTextAsCharacters() == false); // ResultString present
    }

    @Test
    public void testAppendStringLongerThanSegment() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        char[] data = new char[1200](undefined);
        for (int i = 0; i < 1200; i++) data[i] = 'x';
        buf.append(data, 0, 1200);
        assertEquals(1200, buf.size());
        String result = buf.contentsAsString();
        assertEquals(1200, result.length());
    }

    @Test
    public void testAppendStringSubstring() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.append("Hello World", 6, 5);
        assertEquals("World", buf.contentsAsString());
    }

    @Test
    public void testContentsAsDouble() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.append("3.14", 0, 4);
        assertEquals(3.14, buf.contentsAsDouble(), 0.0);
    }

    @Test
    public void testContentsAsDecimal() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.append("1.5", 0, 3);
        assertEquals(new BigDecimal("1.5"), buf.contentsAsDecimal());
    }

    @Test
    public void testBuildResultArray() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.append("ABC", 0, 3);
        assertArrayEquals(new char[]{'A','B','C'}, buf.buildResultArray());
    }

    @Test
    public void testExpandCurrentSegment() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.emptyAndGetCurrentSegment();
        int oldLen = buf.getCurrentSegment().length;
        buf.expandCurrentSegment();
        assertTrue(buf.getCurrentSegment().length > oldLen);
    }

    @Test
    public void testFinishAndGetCurrentSegment() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        char[] seg1 = buf.emptyAndGetCurrentSegment();
        seg1[0](undefined) = 'X';
        buf.setCurrentLength(1);
        char[] seg2 = buf.finishCurrentSegment();
        seg2[0](undefined) = 'Y';
        buf.setCurrentLength(1);
        assertEquals("XY", buf.contentsAsString());
    }

    @Test
    public void testReleaseBuffers() {
        BufferRecycler recycler = getRecycler();
        TextBuffer buf = new TextBuffer(recycler);
        buf.resetWithEmpty();
        buf.append("test", 0, 4);
        buf.releaseBuffers();
        // After release, buffer should be empty
        assertEquals("", buf.contentsAsString());
    }

    @Test
    public void testClearSegments() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.append("long string to force segments maybe", 0, 38);
        buf.clearSegments();
        assertEquals(0, buf.size());
    }

    @Test
    public void testSetCurrentLength() {
        TextBuffer buf = new TextBuffer(getRecycler());
        buf.resetWithEmpty();
        buf.emptyAndGetCurrentSegment();
        buf.setCurrentLength(5);
        assertEquals(5, buf.getCurrentSegmentSize());
    }

    @Test
    public void testHasTextAsCharacters() {
        TextBuffer buf1 = new TextBuffer(getRecycler());
        buf1.resetWithEmpty();
        assertTrue(buf1.hasTextAsCharacters());

        TextBuffer buf2 = new TextBuffer(getRecycler());
        buf2.resetWithString("String");
        assertFalse(buf2.hasTextAsCharacters());
    }
}
