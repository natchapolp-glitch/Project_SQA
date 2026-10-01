package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsGeneratedTest extends TestCase {

    public void testCleanNullAndEmpty() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanUppercasesLetters() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
    }

    public void testCleanRemovesNonLetters() {
        assertEquals("ABC", SoundexUtils.clean("a-b c1"));
        assertEquals("", SoundexUtils.clean("123 -!"));
    }

    public void testDifferenceEncodedNullInputs() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "A123"));
        assertEquals(0, SoundexUtils.differenceEncoded("A123", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedCountsMatchingPositions() {
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A123"));
        assertEquals(1, SoundexUtils.differenceEncoded("A123", "A456"));
        assertEquals(0, SoundexUtils.differenceEncoded("B123", "A456"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    public void testDifferenceEncodedDifferentLengthsAndCase() {
        // only the shorter length is compared
        assertEquals(3, SoundexUtils.differenceEncoded("A12", "A1234"));
        assertEquals(3, SoundexUtils.differenceEncoded("A1234", "A12"));
        // comparison is case-sensitive
        assertEquals(0, SoundexUtils.differenceEncoded("a", "A"));
    }

    public void testDifferenceWithCaverphoneEncoder() throws Exception {
        StringEncoder caverphone = new Caverphone();
        assertEquals(10, SoundexUtils.difference(caverphone, "Thompson", "Thompson"));
        // "TMPSN11111" vs "LA11111111": only the trailing five '1' match
        assertEquals(5, SoundexUtils.difference(caverphone, "Thompson", "Lee"));
    }

    public void testDifferenceEncoderFailureAndNullEncoder() {
        StringEncoder failing = new StringEncoder() {
            public Object encode(Object o) throws EncoderException {
                throw new EncoderException("boom");
            }

            public String encode(String s) throws EncoderException {
                throw new EncoderException("boom");
            }
        };
        try {
            SoundexUtils.difference(failing, "a", "b");
            fail("Expected EncoderException");
        } catch (EncoderException expected) {
            // expected
        }

        try {
            SoundexUtils.difference(null, "a", "b");
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        } catch (EncoderException e) {
            fail("Unexpected EncoderException");
        }
    }
}
