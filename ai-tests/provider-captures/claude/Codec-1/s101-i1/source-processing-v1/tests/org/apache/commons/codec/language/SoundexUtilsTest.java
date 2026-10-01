package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest extends TestCase {

    public SoundexUtilsTest(String name) {
        super(name);
    }

    public void testCleanNullAndEmpty() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanRemovesNonLettersAndUppercases() {
        assertEquals("ABC", SoundexUtils.clean("a-b c1"));
        assertEquals("", SoundexUtils.clean("123 -!"));
        assertEquals("OBRIEN", SoundexUtils.clean("O'Brien"));
    }

    public void testCleanAllLettersUppercased() {
        assertEquals("HELLO", SoundexUtils.clean("Hello"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("\u00c9", SoundexUtils.clean("\u00e9"));
    }

    public void testDifferenceEncodedNullArguments() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "S530"));
        assertEquals(0, SoundexUtils.differenceEncoded("S530", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedCountsPositionalMatches() {
        assertEquals(4, SoundexUtils.differenceEncoded("S530", "S530"));
        assertEquals(3, SoundexUtils.differenceEncoded("S530", "S532"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "WXYZ"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "BCDA"));
    }

    public void testDifferenceEncodedDifferentLengths() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABCDEF"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCDEF", "ABC"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", "ABC"));
    }

    public void testDifferenceWithCaverphoneEncoder() throws Exception {
        Caverphone encoder = new Caverphone();
        assertEquals(10, SoundexUtils.difference(encoder, "Thompson", "Thompson"));
        // TMPSN11111 vs STFNSN1111 -> only the trailing four '1' characters align
        assertEquals(4, SoundexUtils.difference(encoder, "Thompson", "Stevenson"));
    }

    public void testDifferenceWithNullResultsAndNullEncoder() throws Exception {
        StringEncoder nullEncoder = new StringEncoder() {
            public Object encode(Object pObject) throws EncoderException {
                return null;
            }

            public String encode(String pString) throws EncoderException {
                return null;
            }
        };
        assertEquals(0, SoundexUtils.difference(nullEncoder, "a", "b"));
        try {
            SoundexUtils.difference(null, "a", "b");
            fail("NullPointerException expected for null encoder");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    public void testDifferencePropagatesEncoderException() {
        StringEncoder failing = new StringEncoder() {
            public Object encode(Object pObject) throws EncoderException {
                throw new EncoderException("boom");
            }

            public String encode(String pString) throws EncoderException {
                throw new EncoderException("boom");
            }
        };
        try {
            SoundexUtils.difference(failing, "a", "b");
            fail("EncoderException expected");
        } catch (EncoderException e) {
            assertEquals("boom", e.getMessage());
        }
    }
}
