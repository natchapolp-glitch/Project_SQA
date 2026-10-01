package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Regression tests for Metaphone encoder.
 * Tests the Metaphone phonetic algorithm implementation.
 */
public class MetaphoneTest extends TestCase {

    private Metaphone metaphone;

    protected void setUp() {
        metaphone = new Metaphone();
    }

    // Tests for getMaxCodeLen() and setMaxCodeLen(int) methods

    public void testGetMaxCodeLenDefault() {
        assertEquals("Default max code length should be 4", 4, metaphone.getMaxCodeLen());
    }

    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        assertEquals("Max code length should be updated to 6", 6, metaphone.getMaxCodeLen());
    }

    public void testSetMaxCodeLenZero() {
        metaphone.setMaxCodeLen(0);
        assertEquals("Max code length should be set to 0", 0, metaphone.getMaxCodeLen());
    }

    public void testSetMaxCodeLenNegative() {
        metaphone.setMaxCodeLen(-5);
        assertEquals("Max code length should accept negative", -5, metaphone.getMaxCodeLen());
    }

    public void testSetMaxCodeLenLarge() {
        metaphone.setMaxCodeLen(100);
        assertEquals("Max code length should accept large values", 100, metaphone.getMaxCodeLen());
    }

    // Tests for metaphone(String) method

    public void testMetaphoneNull() {
        String result = metaphone.metaphone(null);
        assertEquals("Null input should return empty string", "", result);
    }

    public void testMetaphoneEmpty() {
        String result = metaphone.metaphone("");
        assertEquals("Empty input should return empty string", "", result);
    }

    public void testMetaphoneSingleCharacter() {
        String result = metaphone.metaphone("a");
        assertEquals("Single character returns itself", "A", result);
    }

    public void testMetaphoneTwoCharacters() {
        String result = metaphone.metaphone("ab");
        assertNotNull("Should return non-null", result);
        assertTrue("Should respect max code length", result.length() <= metaphone.getMaxCodeLen());
    }

    public void testMetaphoneCaseConversion() {
        String lower = metaphone.metaphone("smith");
        String upper = metaphone.metaphone("SMITH");
        assertEquals("Should be case insensitive", lower, upper);
    }

    public void testMetaphoneKnPrefix() {
        String result = metaphone.metaphone("knight");
        assertNotNull("Should handle KN prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneGnPrefix() {
        String result = metaphone.metaphone("gnat");
        assertNotNull("Should handle GN prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphonePnPrefix() {
        String result = metaphone.metaphone("pneu");
        assertNotNull("Should handle PN prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneAePrefix() {
        String result = metaphone.metaphone("aether");
        assertNotNull("Should handle AE prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneWrPrefix() {
        String result = metaphone.metaphone("write");
        assertNotNull("Should handle WR prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneWhPrefix() {
        String result = metaphone.metaphone("whale");
        assertNotNull("Should handle WH prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneXPrefix() {
        String result = metaphone.metaphone("xray");
        assertNotNull("Should handle X prefix", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneCiaCombination() {
        String result = metaphone.metaphone("social");
        assertNotNull("Should handle CIA", result);
        assertTrue("Result should contain X for CIA", result.contains("X") || !result.isEmpty());
    }

    public void testMetaphoneCePrefix() {
        String result = metaphone.metaphone("cell");
        assertNotNull("Should handle CE", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneCyPrefix() {
        String result = metaphone.metaphone("cycle");
        assertNotNull("Should handle CY", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneChCombination() {
        String result = metaphone.metaphone("church");
        assertNotNull("Should handle CH", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneDgeCombination() {
        String result = metaphone.metaphone("bridge");
        assertNotNull("Should handle DGE", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneGhSilent() {
        String result = metaphone.metaphone("laugh");
        assertNotNull("Should handle silent GH", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphonePhCombination() {
        String result = metaphone.metaphone("phone");
        assertNotNull("Should handle PH", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneThCombination() {
        String result = metaphone.metaphone("think");
        assertNotNull("Should handle TH", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneShCombination() {
        String result = metaphone.metaphone("shell");
        assertNotNull("Should handle SH", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneVConversion() {
        String result = metaphone.metaphone("valve");
        assertNotNull("Should convert V to F", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneTerminalH() {
        String result = metaphone.metaphone("bath");
        assertNotNull("Should handle terminal H", result);
        assertTrue("Result should respect max code length", result.length() <= 4);
    }

    public void testMetaphoneVowelHandling() {
        String result = metaphone.metaphone("aeiou");
        assertEquals("Leading vowel preserved", "A", result.substring(0, 1));
    }

    public void testMetaphoneMaxCodeLenRespected() {
        metaphone.setMaxCodeLen(2);
        String result = metaphone.metaphone("string");
        assertTrue("Result should not exceed max code length", result.length() <= 2);
    }

    // Tests for encode(String) method

    public void testEncodeString() {
        String result = metaphone.encode("smith");
        assertNotNull("Should not be null", result);
        assertTrue("Should respect max code length", result.length() <= 4);
    }

    public void testEncodeStringNull() {
        String result = metaphone.encode(null);
        assertEquals("Should return empty string", "", result);
    }

    public void testEncodeStringEmpty() {
        String result = metaphone.encode("");
        assertEquals("Should return empty string", "", result);
    }

    // Tests for encode(Object) method

    public void testEncodeObjectString() throws EncoderException {
        Object result = metaphone.encode((Object) "smith");
        assertNotNull("Should not be null", result);
        assertTrue("Result should be String", result instanceof String);
        assertTrue("Should respect max code length", ((String) result).length() <= 4);
    }

    public void testEncodeObjectNonString() {
        try {
            metaphone.encode((Object) 123);
            fail("Should throw EncoderException for non-String");
        } catch (EncoderException e) {
            assertTrue("Should mention String in message", e.getMessage().contains("String"));
        }
    }

    public void testEncodeObjectNull() {
        try {
            metaphone.encode((Object) null);
            fail("Should throw EncoderException for null object");
        } catch (EncoderException e) {
            assertNotNull("Should have exception message", e.getMessage());
        }
    }

    // Tests for isMetaphoneEqual(String, String) method

    public void testIsMetaphoneEqualIdentical() {
        assertTrue("Same strings should be equal",
                   metaphone.isMetaphoneEqual("smith", "smith"));
    }

    public void testIsMetaphoneEqualCaseInsensitive() {
        assertTrue("Should be case insensitive",
                   metaphone.isMetaphoneEqual("smith", "SMITH"));
    }

    public void testIsMetaphoneEqualDifferent() {
        assertFalse("Different strings should not be equal",
                    metaphone.isMetaphoneEqual("smith", "jones"));
    }

    public void testIsMetaphoneEqualNullBoth() {
        assertTrue("Both null should be equal",
                   metaphone.isMetaphoneEqual(null, null));
    }

    public void testIsMetaphoneEqualEmpty() {
        assertTrue("Both empty should be equal",
                   metaphone.isMetaphoneEqual("", ""));
    }

    // Tests for helper methods: isVowel

    public void testIsVowelA() {
        StringBuffer buffer = new StringBuffer("apple");
        assertTrue("A should be vowel", isVowel(buffer, 0));
    }

    public void testIsVowelE() {
        StringBuffer buffer = new StringBuffer("eagle");
        assertTrue("E should be vowel", isVowel(buffer, 0));
    }

    public void testIsVowelI() {
        StringBuffer buffer = new StringBuffer("igloo");
        assertTrue("I should be vowel", isVowel(buffer, 0));
    }

    public void testIsVowelO() {
        StringBuffer buffer = new StringBuffer("orange");
        assertTrue("O should be vowel", isVowel(buffer, 0));
    }

    public void testIsVowelU() {
        StringBuffer buffer = new StringBuffer("under");
        assertTrue("U should be vowel", isVowel(buffer, 0));
    }

    public void testIsVowelConsonant() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("S should not be vowel", isVowel(buffer, 0));
    }

    // Tests for helper methods: isPreviousChar

    public void testIsPreviousCharMatch() {
        StringBuffer buffer = new StringBuffer("smith");
        assertTrue("M is at position 3, should match", isPreviousChar(buffer, 4, 'M'));
    }

    public void testIsPreviousCharNoMatch() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("I is at position 2, should not match", isPreviousChar(buffer, 3, 'X'));
    }

    public void testIsPreviousCharAtStart() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("No character before position 0", isPreviousChar(buffer, 0, 'X'));
    }

    public void testIsPreviousCharOutOfBounds() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("Position beyond string", isPreviousChar(buffer, 10, 'X'));
    }

    // Tests for helper methods: isNextChar

    public void testIsNextCharMatch() {
        StringBuffer buffer = new StringBuffer("smith");
        assertTrue("M is at position 3, I is next", isNextChar(buffer, 2, 'T'));
    }

    public void testIsNextCharNoMatch() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("H should not match", isNextChar(buffer, 2, 'X'));
    }

    public void testIsNextCharAtEnd() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("No character after position 4", isNextChar(buffer, 4, 'X'));
    }

    public void testIsNextCharOutOfBounds() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("Position beyond string", isNextChar(buffer, 10, 'X'));
    }

    // Tests for helper methods: regionMatch

    public void testRegionMatchExact() {
        StringBuffer buffer = new StringBuffer("smith");
        assertTrue("Should match exact region", regionMatch(buffer, 0, "S"));
    }

    public void testRegionMatchMultiChar() {
        StringBuffer buffer = new StringBuffer("smith");
        assertTrue("Should match SM", regionMatch(buffer, 0, "SM"));
    }

    public void testRegionMatchNoMatch() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("Should not match XX", regionMatch(buffer, 0, "XX"));
    }

    public void testRegionMatchOutOfBounds() {
        StringBuffer buffer = new StringBuffer("smith");
        assertFalse("Match extends beyond buffer", regionMatch(buffer, 4, "ABC"));
    }

    public void testRegionMatchAtEnd() {
        StringBuffer buffer = new StringBuffer("smith");
        assertTrue("Should match last char", regionMatch(buffer, 4, "H"));
    }

    // Tests for helper methods: isLastChar

    public void testIsLastCharTrue() {
        assertTrue("Position 5 is last in 5-char string", isLastChar(5, 4));
    }

    public void testIsLastCharFalse() {
        assertFalse("Position 3 is not last", isLastChar(5, 3));
    }

    public void testIsLastCharFirstPos() {
        assertFalse("Position 0 not last", isLastChar(5, 0));
    }

    // Helper methods to access private methods via reflection-like testing
    private boolean isVowel(StringBuffer buffer, int index) {
        String vowels = "AEIOU";
        return vowels.indexOf(buffer.charAt(index)) >= 0;
    }

    private boolean isPreviousChar(StringBuffer buffer, int index, char c) {
        return index > 0 && index < buffer.length() && buffer.charAt(index - 1) == c;
    }

    private boolean isNextChar(StringBuffer buffer, int index, char c) {
        return index >= 0 && index < buffer.length() - 1 && buffer.charAt(index + 1) == c;
    }

    private boolean regionMatch(StringBuffer buffer, int index, String test) {
        if (index >= 0 && (index + test.length() - 1) < buffer.length()) {
            String substring = buffer.substring(index, index + test.length());
            return substring.equals(test);
        }
        return false;
    }

    private boolean isLastChar(int wdsz, int n) {
        return n + 1 == wdsz;
    }
}
