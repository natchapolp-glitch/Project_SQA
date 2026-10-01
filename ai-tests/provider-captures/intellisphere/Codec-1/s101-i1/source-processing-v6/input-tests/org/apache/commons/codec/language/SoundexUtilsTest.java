package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest extends TestCase {

    public void testCleanNull() {
        assertNull(SoundexUtils.clean(null));
    }

    public void testCleanEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLetters() {
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
    }

    public void testCleanLowerCase() {
        assertEquals("HELLO", SoundexUtils.clean("hello"));
    }

    public void testCleanWithNonLetters() {
        assertEquals("HELLO", SoundexUtils.clean("He1llo2!@#"));
    }

    public void testCleanOnlyNonLetters() {
        assertEquals("", SoundexUtils.clean("123!@#"));
    }

    public void testCleanMixedCase() {
        assertEquals("ABC", SoundexUtils.clean("aBc"));
    }

    public void testCleanPreservesLetterOrder() {
        assertEquals("AC", SoundexUtils.clean("a1b2c"));
    }

    public void testDifferenceEncodedBothNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedFirstNull() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABC"));
    }

    public void testDifferenceEncodedSecondNull() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));
    }

    public void testDifferenceEncodedIdentical() {
        assertEquals(20, SoundexUtils.differenceEncoded("ABCDEFGHIJKLMNOPQRST", "ABCDEFGHIJKLMNOPQRST"));
    }

    public void testDifferenceEncodedNoMatch() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", "XYZ"));
    }

    public void testDifferenceEncodedLengthMismatch() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABC"));
    }

    public void testDifferenceEncodedLongerFirst() {
        assertEquals(2, SoundexUtils.differenceEncoded("AB", "ABCD"));
    }

    public void testDifferenceEncodedEmptyStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    public void testDifferenceEncodedWithEmptyString() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", ""));
    }

    public void testDifferenceWithCaVerphoneEncoder() throws EncoderException {
        stringEncoder = new Caverphone();
        assertEquals(5, SoundexUtils.difference(stringEncoder, "hello", "hello"));
    }

    public void testDifferenceWithMetaphoneEncoder() throws EncoderException {
        StringEncoder encoder = new Metaphone();
        // "Knight" and "Night" both encode to "N"
        assertEquals(1, SoundexUtils.difference(encoder, "Knight", "Night"));
    }

    private StringEncoder stringEncoder;

    public void testDifferenceWithEncoderException() {
        StringEncoder throwingEncoder = new StringEncoder() {
            public String encode(String s) throws EncoderException {
                throw new EncoderException("Test exception");
            }

            public Object encode(Object obj) throws EncoderException {
                throw new EncoderException("Test exception");
            }
        };
        try {
            SoundexUtils.difference(throwingEncoder, "hello", "world");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    public void testCleanWithSpaceOnly() {
        assertEquals("", SoundexUtils.clean("   "));
    }

    public void testCleanWithUnicodeLetter() {
        assertEquals("AE", SoundexUtils.clean("a\u00e9"));
    }
}
