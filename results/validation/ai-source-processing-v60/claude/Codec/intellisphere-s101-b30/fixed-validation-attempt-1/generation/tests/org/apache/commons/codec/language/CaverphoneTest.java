package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Regression tests for Caverphone encoder.
 * Tests the Caverphone 2.0 algorithm implementation.
 */
public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() {
        caverphone = new Caverphone();
    }

    // Tests for caverphone(String) method

    public void testCaverphoneNullInput() {
        String result = caverphone.caverphone(null);
        assertEquals("Should return default code for null", "1111111111", result);
    }

    public void testCaverphoneEmptyString() {
        String result = caverphone.caverphone("");
        assertEquals("Should return default code for empty string", "1111111111", result);
    }

    public void testCaverphoneSingleCharacter() {
        String result = caverphone.caverphone("a");
        assertNotNull("Should not be null", result);
        assertEquals("Should return 10 characters", 10, result.length());
    }

    public void testCaverphoneSimpleWord() {
        String result = caverphone.caverphone("smith");
        assertNotNull("Should not be null", result);
        assertEquals("Should return 10 character code", 10, result.length());
        // Result should contain only digits
        assertTrue("Result should be all digits", result.matches("\\d{10}"));
    }

    public void testCaverphoneCaseInsensitivity() {
        String lower = caverphone.caverphone("smith");
        String upper = caverphone.caverphone("SMITH");
        String mixed = caverphone.caverphone("SmItH");
        assertEquals("Lower and upper case should produce same result", lower, upper);
        assertEquals("Mixed case should produce same result", lower, mixed);
    }

    public void testCaverphoneRemovesNonAlpha() {
        String withNumbers = caverphone.caverphone("smith123");
        String withSpecial = caverphone.caverphone("smith@#$");
        String noNumbers = caverphone.caverphone("smith");
        assertEquals("Numbers should be removed", withNumbers, noNumbers);
        assertEquals("Special chars should be removed", withSpecial, noNumbers);
    }

    public void testCaverphoneHandlesCough() {
        String result = caverphone.caverphone("cough");
        assertNotNull("Should encode cough", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneHandlesRough() {
        String result = caverphone.caverphone("rough");
        assertNotNull("Should encode rough", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneHandlesTough() {
        String result = caverphone.caverphone("tough");
        assertNotNull("Should encode tough", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneHandlesEnough() {
        String result = caverphone.caverphone("enough");
        assertNotNull("Should encode enough", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneHandlesTrough() {
        String result = caverphone.caverphone("trough");
        assertNotNull("Should encode trough", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneHandlesGn() {
        String result = caverphone.caverphone("gnat");
        assertNotNull("Should encode gnat", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneHandlesMb() {
        String result = caverphone.caverphone("lamb");
        assertNotNull("Should encode lamb", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneRemovesFinalE() {
        String withE = caverphone.caverphone("face");
        String withoutE = caverphone.caverphone("fac");
        assertEquals("Final e should be removed", withE, withoutE);
    }

    public void testCaverphoneVowelHandling() {
        String result = caverphone.caverphone("aeiou");
        assertNotNull("Should handle vowels", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneLeadingVowel() {
        String result = caverphone.caverphone("apple");
        assertNotNull("Should handle leading vowel", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneConsecutiveConsonants() {
        String result = caverphone.caverphone("string");
        assertNotNull("Should handle consecutive consonants", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneW() {
        String result = caverphone.caverphone("walk");
        assertNotNull("Should handle W", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneH() {
        String result = caverphone.caverphone("hall");
        assertNotNull("Should handle H", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneR() {
        String result = caverphone.caverphone("read");
        assertNotNull("Should handle R", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    public void testCaverphoneL() {
        String result = caverphone.caverphone("light");
        assertNotNull("Should handle L", result);
        assertEquals("Should be 10 characters", 10, result.length());
    }

    // Tests for encode(String) method

    public void testEncodeString() {
        String result = caverphone.encode("smith");
        assertNotNull("Should not be null", result);
        assertEquals("Should return 10 character code", 10, result.length());
    }

    public void testEncodeStringNull() {
        String result = caverphone.encode(null);
        assertEquals("Encode null should delegate to caverphone", "1111111111", result);
    }

    public void testEncodeStringEmpty() {
        String result = caverphone.encode("");
        assertEquals("Encode empty should return default code", "1111111111", result);
    }

    // Tests for encode(Object) method

    public void testEncodeObjectString() throws EncoderException {
        Object result = caverphone.encode((Object) "smith");
        assertNotNull("Should not be null", result);
        assertIsInstance(result, String.class);
        assertEquals("Should return 10 character code", 10, ((String) result).length());
    }

    public void testEncodeObjectNonString() {
        try {
            caverphone.encode((Object) 123);
            fail("Should throw EncoderException for non-String object");
        } catch (EncoderException e) {
            assertTrue("Should mention String in exception", e.getMessage().contains("String"));
        }
    }

    public void testEncodeObjectInteger() {
        try {
            caverphone.encode((Object) new Integer(456));
            fail("Should throw EncoderException for Integer object");
        } catch (EncoderException e) {
            assertNotNull("Should have exception message", e.getMessage());
        }
    }

    public void testEncodeObjectNull() {
        try {
            caverphone.encode((Object) null);
            fail("Should throw EncoderException for null object");
        } catch (EncoderException e) {
            assertNotNull("Should have exception message", e.getMessage());
        }
    }

    // Tests for isCaverphoneEqual(String, String) method

    public void testIsCaverphoneEqualIdentical() {
        assertTrue("Same strings should be equal", caverphone.isCaverphoneEqual("smith", "smith"));
    }

    public void testIsCaverphoneEqualDifferent() {
        assertFalse("Different sounding strings should not be equal",
                    caverphone.isCaverphoneEqual("smith", "jones"));
    }

    

    

    

    

    

    // Helper method for instanceof checks
    private void assertIsInstance(Object obj, Class<?> clazz) {
        assertTrue("Object should be instance of " + clazz.getName(), clazz.isInstance(obj));
    }
}
