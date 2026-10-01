package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextBufferTest
{
    @Test
    public void testResetWithEmptyAndBasicState() {
        TextBuffer buffer = new TextBuffer(null);
        buffer.resetWithEmpty();

        assertEquals(0, buffer.size());
        assertEquals(0, buffer.getTextOffset());
        assertTrue(buffer.hasTextAsCharacters());
        assertEquals("", buffer.contentsAsString());
        assertEquals("", buffer.toString());
        assertArrayEquals(new char[0], buffer.contentsAsArray());
    }

    @Test
    public void testResetWithSharedExposesSharedViewUntilUnshared() {
        TextBuffer buffer = new TextBuffer(null);
        char[] data = "xxabcdefyy".toCharArray();
        buffer.resetWithShared(data, 2, 6);

        assertEquals(6, buffer.size());
        assertEquals(2, buffer.getTextOffset());
        assertTrue(buffer.hasTextAsCharacters());
        assertSame(data, buffer.getTextBuffer());
        assertEquals("abcdef", buffer.contentsAsString());
        assertArrayEquals("abcdef".toCharArray(), buffer.contentsAsArray());
    }

    @Test
    public void testEnsureNotSharedCopiesSharedContent() {
        TextBuffer buffer = new TextBuffer(null);
        char[] data = "xxabcdefyy".toCharArray();
        buffer.resetWithShared(data, 2, 6);

        buffer.ensureNotShared();

        assertEquals(6, buffer.size());
        assertEquals(0, buffer.getTextOffset());
        assertNotSame(data, buffer.getTextBuffer());
        assertEquals("abcdef", buffer.contentsAsString());
    }

    @Test
    public void testAppendCharUnsharesAndAppends() {
        TextBuffer buffer = new TextBuffer(null);
        char[] data = "abc".toCharArray();
        buffer.resetWithShared(data, 0, 3);

        buffer.append('d');

        assertEquals(4, buffer.size());
        assertEquals(0, buffer.getTextOffset());
        assertEquals("abcd", buffer.contentsAsString());
        assertArrayEquals("abcd".toCharArray(), buffer.contentsAsArray());
    }

    @Test
    public void testResetWithCopyAndAppendVariants() {
        TextBuffer buffer = new TextBuffer(null);
        char[] src = "012345".toCharArray();

        buffer.resetWithCopy(src, 1, 3);
        assertEquals("123", buffer.contentsAsString());

        buffer.append("XYZ", 0, 3);
        buffer.append(new char[] { 'a', 'b', 'c', 'd' }, 1, 2);
        buffer.append('!');

        assertEquals("123XYZbc!", buffer.contentsAsString());
        assertEquals(9, buffer.size());
    }

    @Test
    public void testResetWithStringCachesStringAndArrayConversion() {
        TextBuffer buffer = new TextBuffer(null);
        buffer.resetWithString("hello");

        assertEquals(5, buffer.size());
        assertFalse(buffer.hasTextAsCharacters());
        assertEquals("hello", buffer.contentsAsString());
        assertEquals("hello", buffer.toString());

        char[] chars = buffer.getTextBuffer();
        assertArrayEquals("hello".toCharArray(), chars);
        assertTrue(buffer.hasTextAsCharacters());
    }

    @Test
    public void testEmptyAndGetCurrentSegmentProvidesWritableSegment() {
        TextBuffer buffer = new TextBuffer(null);
        char[] seg = buffer.emptyAndGetCurrentSegment();

        assertNotNull(seg);
        assertTrue(seg.length >= 1000);
        assertEquals(0, buffer.size());

        seg[0] = 'A';
        seg[1] = 'B';
        buffer.setCurrentLength(2);

        assertEquals(2, buffer.getCurrentSegmentSize());
        assertEquals("AB", buffer.contentsAsString());
    }

    @Test
    public void testGetCurrentSegmentAllocatesAndSetCurrentLengthAffectsContent() {
        TextBuffer buffer = new TextBuffer(null);

        char[] seg = buffer.getCurrentSegment();
        assertNotNull(seg);
        assertTrue(seg.length >= 1000);

        seg[0] = 'x';
        seg[1] = 'y';
        seg[2] = 'z';
        buffer.setCurrentLength(3);

        assertEquals(3, buffer.getCurrentSegmentSize());
        assertEquals(3, buffer.size());
        assertEquals("xyz", buffer.contentsAsString());
    }

    @Test
    public void testFinishCurrentSegmentAndContentsAcrossSegments() {
        TextBuffer buffer = new TextBuffer(null);
        char[] seg = buffer.emptyAndGetCurrentSegment();
        seg[0] = 'a';
        seg[1] = 'b';
        buffer.setCurrentLength(2);

        char[] next = buffer.finishCurrentSegment();
        assertNotNull(next);
        assertTrue(next.length >= 3);

        next[0] = 'c';
        next[1] = 'd';
        buffer.setCurrentLength(2);

        assertEquals("ab" + "cd", buffer.contentsAsString());
        assertArrayEquals("abcd".toCharArray(), buffer.contentsAsArray());
    }

    @Test
    public void testExpandCurrentSegmentGrowsArrayAndPreservesPrefix() {
        TextBuffer buffer = new TextBuffer(null);
        char[] seg = buffer.emptyAndGetCurrentSegment();
        seg[0] = 'q';
        seg[1] = 'r';
        buffer.setCurrentLength(2);

        int originalLen = seg.length;
        char[] expanded = buffer.expandCurrentSegment();

        assertSame(expanded, buffer.getCurrentSegment());
        assertTrue(expanded.length > originalLen);
        assertEquals('q', expanded[0]);
        assertEquals('r', expanded[1]);
        assertEquals("qr", buffer.contentsAsString());
    }

    @Test
    public void testContentsAsDecimalAndDoubleFromSharedAndCopiedContent() {
        TextBuffer buffer = new TextBuffer(null);
        buffer.resetWithShared("xx12.50yy".toCharArray(), 2, 5);
        assertEquals(new BigDecimal("12.50"), buffer.contentsAsDecimal());
        assertEquals(12.5d, buffer.contentsAsDouble(), 0.0d);

        buffer.resetWithCopy("34.75".toCharArray(), 0, 5);
        assertEquals(new BigDecimal("34.75"), buffer.contentsAsDecimal());
        assertEquals(34.75d, buffer.contentsAsDouble(), 0.0d);
    }

    @Test
    public void testReleaseBuffersWithNullAllocatorClearsContent() {
        TextBuffer buffer = new TextBuffer(null);
        buffer.resetWithString("data");

        buffer.releaseBuffers();

        assertEquals(0, buffer.size());
        assertEquals("", buffer.contentsAsString());
        assertTrue(buffer.hasTextAsCharacters());
    }

    @Test
    public void testContentsAsArrayFromSharedUsesSliceOnly() {
        TextBuffer buffer = new TextBuffer(null);
        char[] input = "0123456789".toCharArray();
        buffer.resetWithShared(input, 3, 4);

        char[] result = buffer.contentsAsArray();

        assertArrayEquals("3456".toCharArray(), result);
        assertNotSame(input, result);
    }
}
