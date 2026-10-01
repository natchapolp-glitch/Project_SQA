package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Tests for {@link Caverphone}.
 *
 * @version $Id$
 */
public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    public void setUp() {
        caverphone = new Caverphone();
    }

    public void tearDown() {
        caverphone = null;
    }

    // Test caverphone method

    public void testCaverphoneNullInput() {
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    public void testCaverphoneEmptyString() {
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    public void testCaverphoneBasicExample() {
        // Example from Caverphone 2.0 spec
        assertEquals("STFNSN1111", caverphone.caverphone("Stevenson"));
    }

    

    

    public void testCaverphoneRandomString() {
        assertNotNull(caverphone.caverphone("randomstring"));
    }

    public void testCaverphoneUpperCaseConversion() {
        assertEquals(caverphone.caverphone("Test"), caverphone.caverphone("test"));
    }

    public void testCaverphoneNumbersRemoved() {
        // Numbers should be stripped
        assertEquals(caverphone.caverphone("test"), caverphone.caverphone("test123"));
    }

    // Test encode(String) method

    public void testEncodeStringBasic() {
        assertEquals("STFNSN1111", caverphone.encode("Stevenson"));
    }

    // Test encode(Object) method

    public void testEncodeObjectWithString() throws EncoderException {
        Object result = caverphone.encode((Object) "Stevenson");
        assertTrue(result instanceof String);
        assertEquals("STFNSN1111", result);
    }

    public void testEncodeObjectWithNonString() {
        try {
            caverphone.encode(new Integer(5));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            String expectedMessage = "Parameter supplied to Caverphone encode is not of type java.lang.String";
            assertTrue(e.getMessage().contains(expectedMessage));
        }
    }

    // Test isCaverphoneEqual method

    public void testIsCaverphoneEqualTrue() {
        assertTrue(caverphone.isCaverphoneEqual("Stevenson", "Stevenson"));
    }

    public void testIsCaverphoneEqualFalse() {
        assertFalse(caverphone.isCaverphoneEqual("Stevenson", "Peterson"));
    }

    public void testIsCaverphoneEqualCaseInsensitive() {
        assertTrue(caverphone.isCaverphoneEqual("Stevenson", "stevenson"));
    }

    public void testIsCaverphoneEqualBothNull() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
    }

    public void testIsCaverphoneEqualOneNull() {
        assertFalse(caverphone.isCaverphoneEqual("Stevenson", null));
        assertFalse(caverphone.isCaverphoneEqual(null, "Stevenson"));
    }
}
