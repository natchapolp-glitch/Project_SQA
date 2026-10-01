package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsGeneratedTest extends TestCase {

    /** Returns its input unchanged. */
    private static class IdentityEncoder implements StringEncoder {
        public Object encode(Object pObject) throws EncoderException {
            return pObject;
        }

        public String encode(String pString) throws EncoderException {
            return pString;
        }
    }

    /** Always fails to encode. */
    private static class FailingEncoder implements StringEncoder {
        public Object encode(Object pObject) throws EncoderException {
            throw new EncoderException("boom");
        }

        public String encode(String pString) throws EncoderException {
            throw new EncoderException("boom");
        }
    }

    public void testCleanNullAndEmptyReturnedAsIs() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLettersIsUpperCased() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
    }

    public void testCleanRemovesNonLetters() {
        assertEquals("ABC", SoundexUtils.clean("a1b2-c"));
        assertEquals("HELLOWORLD", SoundexUtils.clean("Hello World"));
    }

    public void testCleanWithNoLettersReturnsEmpty() {
        assertEquals("", SoundexUtils.clean("123 -!"));
    }

    public void testDifferenceEncodedNullReturnsZero() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "A123"));
        assertEquals(0, SoundexUtils.differenceEncoded("A123", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedCountsMatchingPositions() {
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A123"));
        assertEquals(1, SoundexUtils.differenceEncoded("A123", "A456"));
        assertEquals(2, SoundexUtils.differenceEncoded("A123", "B124"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    public void testDifferenceEncodedDifferentLengthsUsesShorter() {
        assertEquals(3, SoundexUtils.differenceEncoded("A12", "A1234"));
        assertEquals(3, SoundexUtils.differenceEncoded("A1234", "A12"));
        assertEquals(0, SoundexUtils.differenceEncoded("", "A123"));
    }

    public void testDifferenceUsesEncoder() throws Exception {
        StringEncoder identity = new IdentityEncoder();
        assertEquals(4, SoundexUtils.difference(identity, "A123", "A123"));
        assertEquals(1, SoundexUtils.difference(identity, "A123", "A456"));

        StringEncoder caverphone = new Caverphone();
        assertEquals(10, SoundexUtils.difference(caverphone, "Peter", "Peta"));
        assertEquals(5, SoundexUtils.difference(caverphone, "Peter", "Thompson"));
    }

    public void testDifferencePropagatesEncoderException() {
        try {
            SoundexUtils.difference(new FailingEncoder(), "a", "b");
            fail("Expected EncoderException");
        } catch (EncoderException expected) {
            assertEquals("boom", expected.getMessage());
        }
    }
}
