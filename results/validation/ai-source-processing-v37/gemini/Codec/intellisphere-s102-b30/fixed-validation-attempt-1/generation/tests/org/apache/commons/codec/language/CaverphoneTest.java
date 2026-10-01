package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        this.caverphone = new Caverphone();
    }

    public void testCaverphoneNullAndEmpty() {
        assertEquals("1111111111", this.caverphone.caverphone(null));
        assertEquals("1111111111", this.caverphone.caverphone(""));
        assertEquals("1111111111", this.caverphone.caverphone("   "));
        assertEquals("1111111111", this.caverphone.caverphone("12345"));
    }

    public void testCaverphoneBasic() {
        assertEquals("LA11111111", this.caverphone.caverphone("Lee"));
        assertEquals("LA11111111", this.caverphone.caverphone("Li"));
        assertEquals("ATA1111111", this.caverphone.caverphone("Peter"));
    }

    public void testCaverphoneSpecialStarts() {
        assertEquals("KF11111111", this.caverphone.caverphone("cough"));
        assertEquals("RF11111111", this.caverphone.caverphone("rough"));
        assertEquals("TF11111111", this.caverphone.caverphone("tough"));
        assertEquals("ANF1111111", this.caverphone.caverphone("enough"));
        assertEquals("NM11111111", this.caverphone.caverphone("gnome"));
        assertEquals("MPA1111111", this.caverphone.caverphone("mbaba"));
    }

    public void testCaverphoneReplacements() {
        // Final e removed, cq -> 2q -> k, d -> t
        assertEquals("ST11111111", this.caverphone.caverphone("side"));
        // ph -> fh -> F
        assertEquals("FA11111111", this.caverphone.caverphone("photo"));
    }

    public void testEncodeObject() throws Exception {
        Object result = this.caverphone.encode((Object) "Lee");
        assertTrue(result instanceof String);
        assertEquals("LA11111111", result);
        assertEquals("LA11111111", this.caverphone.encode("Lee"));
    }

    public void testEncodeObjectException() {
        try {
            this.caverphone.encode(new Integer(42));
            fail("Expected EncoderException when encoding non-String object");
        } catch (EncoderException e) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(this.caverphone.isCaverphoneEqual("Lee", "Li"));
        assertTrue(this.caverphone.isCaverphoneEqual("cough", "COUGH"));
        assertFalse(this.caverphone.isCaverphoneEqual("Peter", "Stevenson"));
    }
}
