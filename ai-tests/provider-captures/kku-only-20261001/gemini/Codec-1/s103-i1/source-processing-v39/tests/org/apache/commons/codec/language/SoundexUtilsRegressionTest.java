package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsRegressionTest extends TestCase {

    public void testCleanNullAndEmpty() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLetters() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("XYZ", SoundexUtils.clean("XYZ"));
        assertEquals("HELLOWORLD", SoundexUtils.clean("HelloWORLD"));
    }

    public void testCleanMixedAndPunctuation() {
        assertEquals("ABC", SoundexUtils.clean("a-b-c"));
        assertEquals("HELLO", SoundexUtils.clean("123hello 456"));
        assertEquals("", SoundexUtils.clean("!@#$%^&*()1234567890"));
    }

    public void testDifferenceEncodedNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedMatches() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(1, SoundexUtils.differenceEncoded("A", "A"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "WXYZ"));
    }

    public void testDifferenceEncodedDifferentLengths() {
        assertEquals(2, SoundexUtils.differenceEncoded("ABCDE", "AB"));
        assertEquals(2, SoundexUtils.differenceEncoded("AB", "ABCDE"));
        assertEquals(1, SoundexUtils.differenceEncoded("ABC", "A"));
    }

    public void testDifferenceWithStringEncoder() throws Exception {
        StringEncoder encoder = new Metaphone();
        assertEquals(2, SoundexUtils.difference(encoder, "wright", "right"));
        assertEquals(2, SoundexUtils.difference(encoder, "night", "knight"));
        assertEquals(0, SoundexUtils.difference(encoder, "cat", "dog"));
    }

    public void testDifferenceWithNullInputs() throws Exception {
        StringEncoder encoder = new Metaphone();
        assertEquals(0, SoundexUtils.difference(encoder, null, "test"));
        assertEquals(0, SoundexUtils.difference(encoder, "test", null));
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }
}
