package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() {
        caverphone = new Caverphone();
    }

    public void testCaverphoneNull() {
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    public void testCaverphoneEmptyString() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    public void testCaverphoneSimple() {
        assertEquals("STFN111111", caverphone.caverphone("Stevenson"));
    }

    public void testCaverphonePeter() {
        assertEquals("PTA1111111", caverphone.caverphone("Peter"));
    }

    public void testCaverphoneDavid() {
        assertEquals("TFT1111111", caverphone.caverphone("David"));
    }

    public void testCaverphoneWithDigitsAndPunctuation() {
        assertEquals("STFN111111", caverphone.caverphone("Stevenson!@#123"));
    }

    public void testCaverphoneCaseInsensitive() {
        assertEquals(caverphone.caverphone("hello"), caverphone.caverphone("HELLO"));
    }

    public void testEncodeObjectString() throws EncoderException {
        Object result = caverphone.encode("Stevenson");
        assertTrue(result instanceof String);
        assertEquals("STFN111111", (String) result);
    }

    public void testEncodeObjectNonString() {
        try {
            caverphone.encode(new Integer(42));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testEncodeString() {
        assertEquals("STFN111111", caverphone.encode("Stevenson"));
    }

    public void testIsCaverphoneEqualTrue() {
        assertTrue(caverphone.isCaverphoneEqual("Stevenson", "Stevenson"));
    }

    public void testIsCaverphoneEqualFalse() {
        assertFalse(caverphone.isCaverphoneEqual("Stevenson", "David"));
    }

    public void testIsCaverphoneEqualBothNull() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
    }

    public void testIsCaverphoneEqualOneNull() {
        assertFalse(caverphone.isCaverphoneEqual(null, "David"));
    }

    public void testCaverphoneFinalE() {
        assertEquals("BRN1111111", caverphone.caverphone("brown"));
    }

    public void testCaverphonePrefixCough() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
    }

    public void testCaverphonePrefixGn() {
        assertEquals("N111111111", caverphone.caverphone("gnome"));
    }

    public void testCaverphonePrefixMb() {
        assertEquals("M111111111", caverphone.caverphone("mb"));
    }

    public void testCaverphoneCq() {
        assertEquals("K111111111", caverphone.caverphone("acquit"));
    }
}
