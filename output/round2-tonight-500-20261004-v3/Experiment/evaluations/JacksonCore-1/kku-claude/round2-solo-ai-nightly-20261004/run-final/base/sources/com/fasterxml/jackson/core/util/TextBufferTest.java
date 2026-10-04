package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class TextBufferTest {

    @Test
    public void testAppendAndContentsAsString() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        tb.append('a');
        tb.append("bcdef".toCharArray(), 0, 5);
        tb.append("ghi", 0, 3);
        assertEquals("abcdefghi", tb.contentsAsString());
        assertEquals(9, tb.size());
    }

    @Test
    public void testContentsAsArrayMatchesString() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        tb.append("hello", 0, 5);
        char[] arr = tb.contentsAsArray();
        assertEquals("hello", new String(arr));
        assertEquals("hello", tb.contentsAsString());
    }

    @Test
    public void testSegmentGrowthAcrossBoundary() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            char c = (char) ('a' + (i % 26));
            tb.append(c);
            sb.append(c);
        }
        assertEquals(sb.toString(), tb.contentsAsString());
        assertEquals(sb.length(), tb.size());
    }

    @Test
    public void testContentsAsDoubleAndDecimal() {
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithEmpty();
        tb.append("3.14159", 0, 7);
        assertEquals(3.14159, tb.contentsAsDouble(), 0.00001);
        assertEquals(new java.math.BigDecimal("3.14159"), tb.contentsAsDecimal());
    }

    @Test
    public void testResetWithSharedAndUnshareOnAppend() {
        char[] shared = "shared-data".toCharArray();
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithShared(shared, 0, shared.length);
        assertEquals("shared-data", tb.contentsAsString());
        tb.append('X');
        assertEquals("shared-dataX", tb.contentsAsString());
        assertEquals("shared-data", new String(shared));
    }

    @Test
    public void testResetWithCopyIndependence() {
        char[] source = "copydata".toCharArray();
        TextBuffer tb = new TextBuffer(null);
        tb.resetWithCopy(source, 0, source.length);
        source[0] = 'Z';
        assertEquals("copydata", tb.contentsAsString());
    }
}
