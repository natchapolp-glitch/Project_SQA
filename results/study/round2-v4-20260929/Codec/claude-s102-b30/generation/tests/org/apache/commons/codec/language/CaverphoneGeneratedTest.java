package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class CaverphoneGeneratedTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        caverphone = new Caverphone();
    }

    public void testNullAndEmptyReturnTenOnes() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    public void testThompson() {
        assertEquals("TMPSN11111", caverphone.caverphone("Thompson"));
    }

    public void testVowelHandlingAndFinalE() {
        assertEquals("PTA1111111", caverphone.caverphone("Peter"));
        assertEquals("PTA1111111", caverphone.caverphone("Peta"));
        assertEquals("A111111111", caverphone.caverphone("a"));
    }

    public void testSpecialStartSequences() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
        assertEquals("ANF1111111", caverphone.caverphone("enough"));
        assertEquals("TRF1111111", caverphone.caverphone("trough"));
        assertEquals("NM11111111", caverphone.caverphone("gnome"));
        assertEquals("M111111111", caverphone.caverphone("mb"));
    }

    public void testCaseAndNonLetterCharactersIgnored() {
        String expected = caverphone.caverphone("Thompson");
        assertEquals(expected, caverphone.caverphone("THOMPSON"));
        assertEquals(expected, caverphone.caverphone("Th-ompson 1"));
    }

    public void testEncodeString() {
        assertEquals("TMPSN11111", caverphone.encode("Thompson"));
        assertEquals("1111111111", caverphone.encode((String) null));
    }

    public void testEncodeObjectWithString() throws Exception {
        Object result = caverphone.encode((Object) "Thompson");
        assertEquals("TMPSN11111", result);
    }

    public void testEncodeObjectWithNonStringThrows() {
        try {
            caverphone.encode((Object) Integer.valueOf(42));
            fail("Expected EncoderException for non-String input");
        } catch (EncoderException expected) {
            // expected
        }
        try {
            caverphone.encode((Object) null);
            fail("Expected EncoderException for null input");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Peter", "Peta"));
        assertTrue(caverphone.isCaverphoneEqual("Thompson", "THOMPSON"));
        assertTrue(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("Peter", "Thompson"));
        assertFalse(caverphone.isCaverphoneEqual(null, "a"));
    }
}
