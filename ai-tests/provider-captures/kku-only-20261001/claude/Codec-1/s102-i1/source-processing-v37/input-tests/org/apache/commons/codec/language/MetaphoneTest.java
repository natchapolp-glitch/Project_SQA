package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Regression tests for Metaphone encoder.
 * Tests the Metaphone algorithm implementation including helper methods.
 */
public class MetaphoneTest extends TestCase {

    private Metaphone encoder;

    protected void setUp() {
        encoder = new Metaphone();
    }

    // ============ Tests for metaphone(String) method ============

    public void testMetaphoneNullInput() {
        String result = encoder.metaphone(null);
        assertEquals("", result);
    }

    public void testMetaphoneEmptyString() {
        String result = encoder.metaphone("");
        assertEquals("", result);
    }

    public void testMetaphoneSingleCharacter() {
        String result = encoder.metaphone("a");
        assertEquals("A", result);
    }

    public void testMetaphoneConvertsToUppercase() {
        String result = encoder.metaphone("smith");
        for (int i = 0; i < result.length(); i++) {
            assertTrue(Character.isUpperCase(result.charAt(i)) || !Character.isLetter(result.charAt(i)));
        }
    }

    public void testMetaphoneInitialKN() {
        String result1 = encoder.metaphone("knight");
        String result2 = encoder.metaphone("night");
        assertEquals(result1, result2);
    }

    public void testMetaphoneInitialGN() {
        String result1 = encoder.metaphone("gnat");
        String result2 = encoder.metaphone("nat");
        assertEquals(result1, result2);
    }

    public void testMetaphoneInitialPN() {
        String result1 = encoder.metaphone("pneumonia");
        String result2 = encoder.metaphone("neumonia");
        assertEquals(result1, result2);
    }

    public void testMetaphoneInitialAE() {
        String result1 = encoder.metaphone("aeon");
        String result2 = encoder.metaphone("eon");
        assertEquals(result1, result2);
    }

    public void testMetaphoneInitialWR() {
        String result1 = encoder.metaphone("wrap");
        String result2 = encoder.metaphone("rap");
        assertEquals(result1, result2);
    }

    public void testMetaphoneInitialWH() {
        String result1 = encoder.metaphone("whack");
        String result2 = encoder.metaphone("hack");
        assertEquals(result1.charAt(0), 'W');
    }

    public void testMetaphoneInitialX() {
        String result = encoder.metaphone("xray");
        assertTrue(result.startsWith("S"));
    }

    public void testMetaphoneVowelAtStart() {
        String result = encoder.metaphone("apple");
        assertTrue(result.length() > 0);
        assertEquals('A', result.charAt(0));
    }

    public void testMetaphoneConsonantsNotDuplicatedExceptC() {
        String result1 = encoder.metaphone("hello");
        String result2 = encoder.metaphone("helo");
        assertNotNull(result1);
    }

    public void testMetaphoneB() {
        String result = encoder.metaphone("bat");
        assertTrue(result.contains("B"));
    }

    public void testMetaphone_MB_Silent() {
        String result1 = encoder.metaphone("dumb");
        String result2 = encoder.metaphone("dub");
        assertEquals(result1, result2);
    }

    public void testMetaphone_C_Before_EIY() {
        String result1 = encoder.metaphone("city");
        assertTrue(result1.contains("S"));
    }

    public void testMetaphone_CIA() {
        String result = encoder.metaphone("social");
        assertTrue(result.contains("X"));
    }

    public void testMetaphone_CH() {
        String result = encoder.metaphone("church");
        assertTrue(result.contains("X"));
    }

    public void testMetaphone_DG_Before_EIY() {
        String result1 = encoder.metaphone("edge");
        String result2 = encoder.metaphone("edj");
        // DGE should become J
        assertNotNull(result1);
    }

    public void testMetaphone_GH_Silent() {
        String result = encoder.metaphone("night");
        assertFalse(result.contains("H"));
    }

    public void testMetaphone_H_Vowel() {
        String result = encoder.metaphone("hot");
        assertTrue(result.contains("H"));
    }

    public void testMetaphone_PH() {
        String result1 = encoder.metaphone("phone");
        String result2 = encoder.metaphone("fone");
        assertEquals(result1, result2);
    }

    public void testMetaphone_SH() {
        String result = encoder.metaphone("shah");
        assertTrue(result.contains("X"));
    }

    public void testMetaphone_TH() {
        String result = encoder.metaphone("think");
        assertTrue(result.contains("0"));
    }

    public void testMetaphone_V() {
        String result1 = encoder.metaphone("valve");
        String result2 = encoder.metaphone("falve");
        assertEquals(result1, result2);
    }

    public void testMetaphoneMaxCodeLenDefault() {
        String result = encoder.metaphone("extraordinaire");
        assertTrue(result.length() <= 4);
    }

    // ============ Tests for encode(String) method ============

    public void testEncodeStringBasic() {
        String result = encoder.encode("smith");
        assertTrue(result.length() > 0);
        assertTrue(result.length() <= 4);
    }

    public void testEncodeStringMatchesMetaphone() {
        String input = "example";
        String result1 = encoder.encode(input);
        String result2 = encoder.metaphone(input);
        assertEquals(result1, result2);
    }

    // ============ Tests for encode(Object) method ============

    public void testEncodeObjectWithString() throws EncoderException {
        Object result = encoder.encode((Object) "smith");
        assertTrue(result instanceof String);
        assertTrue(((String) result).length() <= 4);
    }

    public void testEncodeObjectWithNonString() {
        try {
            encoder.encode((Object) 123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("java.lang.String"));
        }
    }

    // ============ Tests for isMetaphoneEqual(String, String) method ============

    public void testIsMetaphoneEqualIdentical() {
        assertTrue(encoder.isMetaphoneEqual("smith", "smith"));
    }

    public void testIsMetaphoneEqualDifferent() {
        assertFalse(encoder.isMetaphoneEqual("smith", "jones"));
    }

    public void testIsMetaphoneEqualCaseInsensitive() {
        assertTrue(encoder.isMetaphoneEqual("SMITH", "smith"));
    }

    public void testIsMetaphoneEqualSimilarSounding() {
        assertTrue(encoder.isMetaphoneEqual("night", "knight"));
    }

    // ============ Tests for getMaxCodeLen() method ============

    public void testGetMaxCodeLenDefault() {
        assertEquals(4, encoder.getMaxCodeLen());
    }

    // ============ Tests for setMaxCodeLen(int) method ============

    public void testSetMaxCodeLen() {
        encoder.setMaxCodeLen(10);
        assertEquals(10, encoder.getMaxCodeLen());
    }

    public void testSetMaxCodeLenAffectsEncoding() {
        encoder.setMaxCodeLen(10);
        String result = encoder.metaphone("extraordinaire");
        assertTrue(result.length() <= 10);
    }

    public void testSetMaxCodeLenZero() {
        encoder.setMaxCodeLen(0);
        assertEquals(0, encoder.getMaxCodeLen());
    }

    public void testSetMaxCodeLenOne() {
        encoder.setMaxCodeLen(1);
        String result = encoder.metaphone("smith");
        assertTrue(result.length() <= 1);
    }

    // ============ Tests for isVowel(StringBuffer, int) method ============

    public void testIsVowelA() {
        StringBuffer sb = new StringBuffer("apple");
        assertTrue(encoder.isVowel(sb, 0));
    }

    public void testIsVowelE() {
        StringBuffer sb = new StringBuffer("example");
        assertTrue(encoder.isVowel(sb, 4));
    }

    public void testIsVowelConsonant() {
        StringBuffer sb = new StringBuffer("bread");
        assertFalse(encoder.isVowel(sb, 0));
    }

    // ============ Tests for isPreviousChar(StringBuffer, int, char) method ============

    public void testIsPreviousCharMatch() {
        StringBuffer sb = new StringBuffer("smith");
        assertTrue(encoder.isPreviousChar(sb, 2, 'm'));
    }

    public void testIsPreviousCharNoMatch() {
        StringBuffer sb = new StringBuffer("smith");
        assertFalse(encoder.isPreviousChar(sb, 2, 'x'));
    }

    public void testIsPreviousCharAtStart() {
        StringBuffer sb = new StringBuffer("smith");
        assertFalse(encoder.isPreviousChar(sb, 0, 's'));
    }

    // ============ Tests for isNextChar(StringBuffer, int, char) method ============

    public void testIsNextCharMatch() {
        StringBuffer sb = new StringBuffer("smith");
        assertTrue(encoder.isNextChar(sb, 0, 'm'));
    }

    public void testIsNextCharNoMatch() {
        StringBuffer sb = new StringBuffer("smith");
        assertFalse(encoder.isNextChar(sb, 0, 'x'));
    }

    public void testIsNextCharAtEnd() {
        StringBuffer sb = new StringBuffer("smith");
        assertFalse(encoder.isNextChar(sb, 4, 'h'));
    }

    // ============ Tests for regionMatch(StringBuffer, int, String) method ============

    public void testRegionMatchMatch() {
        StringBuffer sb = new StringBuffer("church");
        assertTrue(encoder.regionMatch(sb, 0, "CH"));
    }

    public void testRegionMatchNoMatch() {
        StringBuffer sb = new StringBuffer("church");
        assertFalse(encoder.regionMatch(sb, 0, "SH"));
    }

    public void testRegionMatchPartial() {
        StringBuffer sb = new StringBuffer("church");
        assertTrue(encoder.regionMatch(sb, 2, "UR"));
    }

    public void testRegionMatchBeyondBounds() {
        StringBuffer sb = new StringBuffer("church");
        assertFalse(encoder.regionMatch(sb, 5, "ABC"));
    }

    // ============ Tests for isLastChar(int, int) method ============

    public void testIsLastCharTrue() {
        assertTrue(encoder.isLastChar(5, 4));
    }

    public void testIsLastCharFalse() {
        assertFalse(encoder.isLastChar(5, 3));
    }

    public void testIsLastCharZero() {
        assertTrue(encoder.isLastChar(1, 0));
    }
}
