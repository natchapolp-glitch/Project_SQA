package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {

    private Caverphone encoder;

    public CaverphoneTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        encoder = new Caverphone();
    }

    public void testCaverphoneNullEmptyAndNoLetters() {
        assertEquals("1111111111", encoder.caverphone(null));
        assertEquals("1111111111", encoder.caverphone(""));
        assertEquals("1111111111", encoder.caverphone("123"));
    }

    public void testCaverphoneThompsonCaseAndPunctuation() {
        assertEquals("TMPSN11111", encoder.caverphone("Thompson"));
        assertEquals("TMPSN11111", encoder.caverphone("THOMPSON"));
        assertEquals("TMPSN11111", encoder.caverphone("Thomp-son"));
    }

    public void testCaverphoneStevenson() {
        assertEquals("STFNSN1111", encoder.caverphone("Stevenson"));
    }

    public void testCaverphoneSpecialStarts() {
        assertEquals("KF11111111", encoder.caverphone("cough"));
        assertEquals("ANF1111111", encoder.caverphone("enough"));
    }

    public void testCaverphoneFinalVowelBecomesA() {
        assertEquals("LA11111111", encoder.caverphone("Lee"));
    }

    public void testEncodeString() {
        assertEquals(encoder.caverphone("Thompson"), encoder.encode("Thompson"));
        assertEquals("1111111111", encoder.encode((String) null));
    }

    public void testEncodeObject() throws Exception {
        assertEquals("TMPSN11111", encoder.encode((Object) "Thompson"));
        try {
            encoder.encode(Integer.valueOf(42));
            fail("EncoderException expected for non-String");
        } catch (EncoderException expected) {
            // expected
        }
        try {
            encoder.encode((Object) null);
            fail("EncoderException expected for null Object");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(encoder.isCaverphoneEqual("Stevenson", "Stephenson"));
        assertTrue(encoder.isCaverphoneEqual("Thompson", "THOMPSON"));
        assertFalse(encoder.isCaverphoneEqual("Thompson", "Smith"));
        assertTrue(encoder.isCaverphoneEqual(null, ""));
        assertTrue(encoder.isCaverphoneEqual(null, null));
    }
}
