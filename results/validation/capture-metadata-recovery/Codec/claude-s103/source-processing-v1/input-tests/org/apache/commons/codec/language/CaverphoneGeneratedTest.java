package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class CaverphoneGeneratedTest extends TestCase {

    private Caverphone encoder;

    protected void setUp() throws Exception {
        super.setUp();
        encoder = new Caverphone();
    }

    public void testCaverphoneNullAndEmpty() {
        assertEquals("1111111111", encoder.caverphone(null));
        assertEquals("1111111111", encoder.caverphone(""));
    }

    public void testCaverphoneKnownNames() {
        assertEquals("TMPSN11111", encoder.caverphone("Thompson"));
        assertEquals("STFNSN1111", encoder.caverphone("Stevenson"));
        // final 'e' is removed, trailing vowel code becomes 'A'
        assertEquals("LA11111111", encoder.caverphone("Lee"));
        // case insensitive
        assertEquals("TMPSN11111", encoder.caverphone("THOMPSON"));
    }

    public void testCaverphoneStartRules() {
        assertEquals("KF11111111", encoder.caverphone("Cough"));
        assertEquals("ANF1111111", encoder.caverphone("enough"));
    }

    public void testCaverphoneNonLettersIgnored() {
        assertEquals("TMPSN11111", encoder.caverphone("Th-om p.son"));
        assertEquals("1111111111", encoder.caverphone("123"));
    }

    public void testCaverphoneTrailingW() {
        assertEquals("SNA1111111", encoder.caverphone("snow"));
    }

    public void testEncodeString() {
        assertEquals("TMPSN11111", encoder.encode("Thompson"));
        assertEquals("1111111111", encoder.encode((String) null));
        assertEquals(encoder.caverphone("Stevenson"), encoder.encode("Stevenson"));
    }

    public void testEncodeObject() throws Exception {
        Object result = encoder.encode((Object) "Thompson");
        assertEquals("TMPSN11111", result);

        try {
            encoder.encode(new Integer(5));
            fail("Expected EncoderException for non-String parameter");
        } catch (EncoderException expected) {
            // expected
        }

        try {
            encoder.encode((Object) null);
            fail("Expected EncoderException for null parameter");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(encoder.isCaverphoneEqual("Thompson", "Thompson"));
        assertTrue(encoder.isCaverphoneEqual("Thompson", "THOMPSON"));
        assertFalse(encoder.isCaverphoneEqual("Lee", "Thompson"));
        assertTrue(encoder.isCaverphoneEqual(null, ""));
        assertTrue(encoder.isCaverphoneEqual(null, null));
        assertFalse(encoder.isCaverphoneEqual(null, "Thompson"));
    }
}
