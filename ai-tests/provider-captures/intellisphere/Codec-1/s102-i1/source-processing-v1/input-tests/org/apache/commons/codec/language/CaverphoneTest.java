// src/test/org/apache/commons/codec/language/CaverphoneTest.java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest extends TestCase {
    
    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        caverphone = new Caverphone();
    }

    public void testEncodeString() {
        assertEquals("1111111111", caverphone.encode((String) null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("STFNSN1111", caverphone.encode("Stevenson"));
        assertEquals("PTRSN111111", caverphone.encode("Peterson"));
    }

    public void testEncodeObject() throws EncoderException {
        assertEquals("STFNSN1111", caverphone.encode((Object) "Stevenson"));
        assertEquals("PTRSN111111", caverphone.encode((Object) "Peterson"));
    }

    public void testEncodeObjectNonString() {
        try {
            caverphone.encode(new Integer(123));
            fail("Expected EncoderException for non-String object");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    public void testCaverphoneNull() {
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    public void testCaverphoneEmpty() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    public void testCaverphoneStevenson() {
        assertEquals("STFNSN1111", caverphone.caverphone("Stevenson"));
    }

    public void testCaverphonePeterson() {
        assertEquals("PTRSN111111", caverphone.caverphone("Peterson"));
    }

    public void testCaverphoneName() {
        assertEquals("HNTR111111", caverphone.caverphone("Hunter"));
    }

    public void testCaverphoneThompson() {
        assertEquals("TMPSN11111", caverphone.caverphone("Thompson"));
    }

    public void testCaverphoneMackay() {
        assertEquals("MKA1111111", caverphone.caverphone("Mackay"));
    }

    public void testCaverphoneWright() {
        assertEquals("RAT1111111", caverphone.caverphone("Wright"));
    }

    public void testCaverphoneWithDigits() {
        assertEquals("TTK1111111", caverphone.caverphone("test123test"));
    }

    public void testCaverphoneUpperCase() {
        assertEquals("STFNSN1111", caverphone.caverphone("STEVENSON"));
    }

    public void testCaverphoneMixedCase() {
        assertEquals("STFNSN1111", caverphone.caverphone("StEvEnSoN"));
    }

    public void testCaverphoneSingleChar() {
        String result = caverphone.caverphone("a");
        assertNotNull(result);
        assertEquals(10, result.length());
    }

    public void testCaverphoneOutputLength() {
        assertEquals(10, caverphone.caverphone("test").length());
        assertEquals(10, caverphone.caverphone("longerword").length());
    }

    public void testIsCaverphoneEqualTrue() {
        assertTrue(caverphone.isCaverphoneEqual("Stevenson", "Stevenson"));
        assertTrue(caverphone.isCaverphoneEqual("Stevenson", "STEVENSON"));
    }

    public void testIsCaverphoneEqualFalse() {
        assertFalse(caverphone.isCaverphoneEqual("Stevenson", "Peterson"));
        assertFalse(caverphone.isCaverphoneEqual("Smith", "Jones"));
    }

    public void testIsCaverphoneEqualNullInput() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", null));
        assertTrue(caverphone.isCaverphoneEqual(null, ""));
    }

    public void testCaverphoneCoughPrefix() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
    }

    public void testCaverphoneRoughPrefix() {
        assertEquals("RF11111111", caverphone.caverphone("rough"));
    }

    public void testCaverphoneToughPrefix() {
        assertEquals("TF11111111", caverphone.caverphone("tough"));
    }

    public void testCaverphoneEnoughPrefix() {
        assertEquals("ANF1111111", caverphone.caverphone("enough"));
    }

    public void testCaverphoneGnPrefix() {
        assertEquals("N111111111", caverphone.caverphone("gnat"));
    }

    public void testCaverphoneMbPrefix() {
        assertEquals("M111111111", caverphone.caverphone("mbamba"));
    }

    public void testCaverphoneWithSpaces() {
        assertEquals("STFNSN1111", caverphone.caverphone("Ste ven son"));
    }

    public void testCaverphoneWithPunctuation() {
        assertEquals("STFNSN1111", caverphone.caverphone("Ste'venson"));
    }

    public void testCaverphoneIdenticalEncode() {
        String str = "Robertson";
        assertEquals(caverphone.caverphone(str), caverphone.encode(str));
    }
}
