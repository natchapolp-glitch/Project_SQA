package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneRegressionTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.caverphone = new Caverphone();
    }

    public void testCaverphoneNullAndEmpty() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("   "));
        assertEquals("1111111111", caverphone.caverphone("12345!@#$%"));
    }

    public void testCaverphoneEncodeObject() throws Exception {
        Object result = caverphone.encode((Object) "Lee");
        assertTrue(result instanceof String);
        assertEquals(caverphone.caverphone("Lee"), result);
    }

    public void testCaverphoneEncodeObjectInvalidType() {
        try {
            caverphone.encode(new Integer(42));
            fail("Expected EncoderException for non-String parameter");
        } catch (EncoderException e) {
            // expected
        }

        try {
            caverphone.encode((Object) null);
            fail("Expected EncoderException for null Object parameter");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testCaverphoneEncodeString() {
        assertEquals(caverphone.caverphone("Stevenson"), caverphone.encode("Stevenson"));
        assertEquals("1111111111", caverphone.encode(""));
    }

    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Lee", "Li"));
        assertTrue(caverphone.isCaverphoneEqual("Peter", "Peter"));
        assertTrue(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("Peter", "Stevenson"));
        assertFalse(caverphone.isCaverphoneEqual("Smith", "Lee"));
    }

    public void testCaverphoneStartPrefixes() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
        assertEquals(10, caverphone.caverphone("rough").length());
        assertEquals(10, caverphone.caverphone("tough").length());
        assertEquals(10, caverphone.caverphone("enough").length());
        assertEquals(10, caverphone.caverphone("trough").length());
        assertEquals(10, caverphone.caverphone("gnome").length());
        assertEquals(10, caverphone.caverphone("mbappe").length());
    }

    public void testCaverphoneReplacements() {
        assertEquals(10, caverphone.caverphone("acquire").length());
        assertEquals(10, caverphone.caverphone("circle").length());
        assertEquals(10, caverphone.caverphone("center").length());
        assertEquals(10, caverphone.caverphone("cyan").length());
        assertEquals(10, caverphone.caverphone("catch").length());
        assertEquals(10, caverphone.caverphone("phone").length());
        assertEquals(10, caverphone.caverphone("edge").length());
        assertEquals(10, caverphone.caverphone("nation").length());
        assertEquals(10, caverphone.caverphone("spatial").length());
    }

    public void testCaverphoneEndings() {
        // Final 'e' is dropped: "fade" and "fad" produce identical encoding
        assertEquals(caverphone.caverphone("fad"), caverphone.caverphone("fade"));
        assertTrue(caverphone.isCaverphoneEqual("fade", "fad"));
    }

    public void testCaverphoneNonAscii() {
        assertEquals(caverphone.caverphone("hello"), caverphone.caverphone("123hello!@#"));
    }
}
