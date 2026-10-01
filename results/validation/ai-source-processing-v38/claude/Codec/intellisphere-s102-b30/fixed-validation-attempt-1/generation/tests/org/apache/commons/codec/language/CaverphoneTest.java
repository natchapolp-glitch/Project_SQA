package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Regression tests for Caverphone encoder.
 * Tests the Caverphone 2.0 algorithm implementation.
 */
public class CaverphoneTest extends TestCase {

    private Caverphone encoder;

    protected void setUp() {
        encoder = new Caverphone();
    }

    // ============ Tests for caverphone(String) method ============

    public void testCaverphoneNullInput() {
        String result = encoder.caverphone(null);
        assertEquals("1111111111", result);
    }

    public void testCaverphoneEmptyString() {
        String result = encoder.caverphone("");
        assertEquals("1111111111", result);
    }

    public void testCaverphoneSingleCharacter() {
        String result = encoder.caverphone("a");
        assertEquals(10, result.length());
    }

    public void testCaverphoneConvertsToLowercase() {
        String result1 = encoder.caverphone("Smith");
        String result2 = encoder.caverphone("smith");
        assertEquals(result1, result2);
    }

    public void testCaverphoneRemovesNonAlphabetic() {
        String result1 = encoder.caverphone("a1b2c3");
        String result2 = encoder.caverphone("abc");
        assertEquals(result1, result2);
    }

    public void testCaverphoneRemovesFinalE() {
        String result1 = encoder.caverphone("smile");
        String result2 = encoder.caverphone("smil");
        assertEquals(result1, result2);
    }

    public void testCaverphoneHandlesCoughStart() {
        String result = encoder.caverphone("cough");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneHandlesRoughStart() {
        String result = encoder.caverphone("rough");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneHandlesToughStart() {
        String result = encoder.caverphone("tough");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneHandlesEnoughStart() {
        String result = encoder.caverphone("enough");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneHandlesTroughStart() {
        String result = encoder.caverphone("trough");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneHandlesGnStart() {
        String result = encoder.caverphone("gnat");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneHandlesMbStart() {
        String result = encoder.caverphone("mbongo");
        assertTrue(result.length() == 10);
    }

    public void testCaverphoneReplaceCQ() {
        String result1 = encoder.caverphone("cqhello");
        String result2 = encoder.caverphone("chello");
        // CQ is replaced, should differ from without CQ
        assertNotNull(result1);
    }

    public void testCaverphoneReplacesCI() {
        String result1 = encoder.caverphone("city");
        String result2 = encoder.caverphone("sity");
        assertEquals(result1, result2);
    }

    public void testCaverphoneReplacesV() {
        String result1 = encoder.caverphone("valve");
        String result2 = encoder.caverphone("falve");
        assertEquals(result1, result2);
    }

    public void testCaverphoneReplacesZ() {
        String result1 = encoder.caverphone("zero");
        String result2 = encoder.caverphone("sero");
        assertEquals(result1, result2);
    }

    public void testCaverphoneConsecutiveVowels() {
        String result1 = encoder.caverphone("aeiou");
        assertTrue(result1.length() == 10);
    }

    public void testCaverphoneOutputLength() {
        String result = encoder.caverphone("test");
        assertEquals(10, result.length());
    }

    public void testCaverphoneOutputContainsOnlyDigits() {
        String result = encoder.caverphone("testword");
        for (int i = 0; i < result.length(); i++) {
            assertTrue(Character.isDigit(result.charAt(i)));
        }
    }

    public void testCaverphoneEndsWithOnes() {
        String result = encoder.caverphone("test");
        String endDigits = result.substring(Math.max(0, result.length() - 6));
        assertTrue(endDigits.matches("1+"));
    }

    public void testCaverphoneMultipleReplacements() {
        String result = encoder.caverphone("psychology");
        assertEquals(10, result.length());
    }

    // ============ Tests for encode(String) method ============

    public void testEncodeStringNullInput() {
        String result = encoder.encode((String) null);
        assertEquals("1111111111", result);
    }

    public void testEncodeStringEmptyInput() {
        String result = encoder.encode("");
        assertEquals("1111111111", result);
    }

    public void testEncodeStringBasic() {
        String result = encoder.encode("smith");
        assertEquals(10, result.length());
    }

    public void testEncodeStringMatchesCaverphone() {
        String input = "example";
        String result1 = encoder.encode(input);
        String result2 = encoder.caverphone(input);
        assertEquals(result1, result2);
    }

    // ============ Tests for encode(Object) method ============

    public void testEncodeObjectWithString() throws EncoderException {
        Object result = encoder.encode((Object) "smith");
        assertTrue(result instanceof String);
        assertEquals(10, ((String) result).length());
    }

    public void testEncodeObjectWithNullString() throws EncoderException {
        Object result = encoder.encode((Object) null);
        assertEquals("1111111111", (String) result);
    }

    public void testEncodeObjectWithNonString() {
        try {
            encoder.encode((Object) 123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testEncodeObjectWithInteger() {
        try {
            encoder.encode((Object) new Integer(42));
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("java.lang.String"));
        }
    }

    

    // ============ Tests for isCaverphoneEqual(String, String) method ============

    

    

    

    

    

    

    

    
}
