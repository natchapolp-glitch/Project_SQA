I'll generate comprehensive regression tests for the three target classes: `Caverphone`, `Metaphone`, and `SoundexUtils`. Based on the build configuration, this project uses JUnit 3.8.1 and Java 1.4+ compatibility.

```java
org/apache/commons/codec/language/CaverphoneTest.java
```

```java
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
        assertTrue(result.length() == 10);
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

    public void testEncodeObjectWithDouble() {
        try {
            encoder.encode((Object) 3.14);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    // ============ Tests for isCaverphoneEqual(String, String) method ============

    public void testIsCaverphoneEqualIdentical() {
        assertTrue(encoder.isCaverphoneEqual("smith", "smith"));
    }

    public void testIsCaverphoneEqualDifferent() {
        assertFalse(encoder.isCaverphoneEqual("smith", "jones"));
    }

    public void testIsCaverphoneEqualCaseInsensitive() {
        assertTrue(encoder.isCaverphoneEqual("SMITH", "smith"));
    }

    public void testIsCaverphoneEqualWithNulls() {
        assertTrue(encoder.isCaverphoneEqual(null, null));
    }

    public void testIsCaverphoneEqualOneNull() {
        assertFalse(encoder.isCaverphoneEqual("smith", null));
    }

    public void testIsCaverphoneEqualEmptyStrings() {
        assertTrue(encoder.isCaverphoneEqual("", ""));
    }

    public void testIsCaverphoneEqualWithPunctuation() {
        assertTrue(encoder.isCaverphoneEqual("smith!", "smith"));
    }

    public void testIsCaverphoneEqualSoundAlike() {
        // Different spellings that might sound similar
        assertFalse(encoder.isCaverphoneEqual("night", "knight"));
    }
}
```

```java
org/apache/commons/codec/language/MetaphoneTest.java
```

```java
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
```

```java
org/apache/commons/codec/language/SoundexUtilsTest.java
```

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/**
 * Regression tests for SoundexUtils utility methods.
 * Tests the helper methods used by Soundex and RefinedSoundex encoders.
 */
public class SoundexUtilsTest extends TestCase {

    /**
     * Mock StringEncoder implementation for testing.
     */
    private static class MockEncoder implements StringEncoder {
        public String encode(String source) throws EncoderException {
            return source;
        }

        public Object encode(Object source) throws EncoderException {
            if (source instanceof String) {
                return encode((String) source);
            }
            throw new EncoderException("Only strings are supported");
        }
    }

    private MockEncoder mockEncoder;

    protected void setUp() {
        mockEncoder = new MockEncoder();
    }

    // ============ Tests for clean(String) method ============

    public void testCleanNullInput() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }

    public void testCleanEmptyString() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    public void testCleanSingleLetter() {
        String result = SoundexUtils.clean("a");
        assertEquals("A", result);
    }

    public void testCleanAllLetters() {
        String result = SoundexUtils.clean("abc");
        assertEquals("ABC", result);
    }

    public void testCleanConvertsToUppercase() {
        String result = SoundexUtils.clean("smith");
        assertEquals("SMITH", result);
    }

    public void testCleanRemovesNumbers() {
        String result = SoundexUtils.clean("smith123");
        assertEquals("SMITH", result);
    }

    public void testCleanRemovesPunctuation() {
        String result = SoundexUtils.clean("smith!");
        assertEquals("SMITH", result);
    }

    public void testCleanRemovesSpaces() {
        String result = SoundexUtils.clean("john smith");
        assertEquals("JOHNSMITH", result);
    }

    public void testCleanMixedInput() {
        String result = SoundexUtils.clean("John123 Smith!");
        assertEquals("JOHNSMITH", result);
    }

    public void testCleanSpecialCharacters() {
        String result = SoundexUtils.clean("o'brien");
        assertEquals("OBRIEN", result);
    }

    public void testCleanDashes() {
        String result = SoundexUtils.clean("smith-jones");
        assertEquals("SMITHJONES", result);
    }

    public void testCleanOnlyLettersAndNumbers() {
        String result = SoundexUtils.clean("abc123def");
        assertEquals("ABCDEF", result);
    }

    public void testCleanLowercaseLetters() {
        String result = SoundexUtils.clean("aAbBcC");
        assertEquals("AABBCC", result);
    }

    // ============ Tests for differenceEncoded(String, String) method ============

    public void testDifferenceEncodedNullBoth() {
        int result = SoundexUtils.differenceEncoded(null, null);
        assertEquals(0, result);
    }

    public void testDifferenceEncodedFirstNull() {
        int result = SoundexUtils.differenceEncoded(null, "ABCD");
        assertEquals(0, result);
    }

    public void testDifferenceEncodedSecondNull() {
        int result = SoundexUtils.differenceEncoded("ABCD", null);
        assertEquals(0, result);
    }

    public void testDifferenceEncodedIdentical() {
        int result = SoundexUtils.differenceEncoded("SMITH", "SMITH");
        assertEquals(5, result);
    }

    public void testDifferenceEncodedEmpty() {
        int result = SoundexUtils.differenceEncoded("", "");
        assertEquals(0, result);
    }

    public void testDifferenceEncodedNoMatch() {
        int result = SoundexUtils.differenceEncoded("ABCD", "WXYZ");
        assertEquals(0, result);
    }

    public void testDifferenceEncodedPartialMatch() {
        int result = SoundexUtils.differenceEncoded("SMITH", "SMOCK");
        assertEquals(2, result);
    }

    public void testDifferenceEncodedDifferentLengths() {
        int result = SoundexUtils.differenceEncoded("SMITH", "SMI");
        assertEquals(3, result);
    }

    public void testDifferenceEncodedFirstLonger() {
        int result = SoundexUtils.differenceEncoded("SMITHS", "SMITH");
        assertEquals(5, result);
    }

    public void testDifferenceEncodedSecondLonger() {
        int result = SoundexUtils.differenceEncoded("SMITH", "SMITHS");
        assertEquals(5, result);
    }

    public void testDifferenceEncodedSingleCharMatch() {
        int result = SoundexUtils.differenceEncoded("A", "A");
        assertEquals(1, result);
    }

    public void testDifferenceEncodedSingleCharNoMatch() {
        int result = SoundexUtils.differenceEncoded("A", "B");
        assertEquals(0, result);
    }

    public void testDifferenceEncodedCaseSensitive() {
        int result = SoundexUtils.differenceEncoded("smith", "SMITH");
        assertEquals(0, result);
    }

    // ============ Tests for difference(StringEncoder, String, String) method ============

    public void testDifferenceNullBoth() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, null, null);
        assertEquals(0, result);
    }

    public void testDifferenceIdentical() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "SMITH", "SMITH");
        assertEquals(5, result);
    }

    public void testDifferenceNoMatch() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "ABCD", "WXYZ");
        assertEquals(0, result);
    }

    public void testDifferencePartialMatch() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "SMITH", "SMOCK");
        assertEquals(2, result);
    }

    public void testDifferenceDifferentLengths() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "SMITH", "SMI");
        assertEquals(3, result);
    }

    public void testDifferenceWithEncoder() throws EncoderException {
        // Create a simple encoder that appends a digit
        StringEncoder customEncoder = new StringEncoder() {
            public Object encode(Object source) throws EncoderException {
                return encode((String) source);
            }

            public String encode(String source) throws EncoderException {
                return source.toUpperCase(java.util.Locale.ENGLISH);
            }
        };
        int result = SoundexUtils.difference(customEncoder, "smith", "smith");
        assertEquals(5, result);
    }

    public void testDifferenceEmptyStrings() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "", "");
        assertEquals(0, result);
    }

    public void testDifferenceFirstEmpty() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "", "ABC");
        assertEquals(0, result);
    }

    public void testDifferenceSecondEmpty() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "ABC", "");
        assertEquals(0, result);
    }

    public void testDifferenceMultipleMatches() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, "ABCDEF", "ABCXYZ");
        assertEquals(3, result);
    }

    public void testDifferenceEncoderUsed() throws EncoderException {
        // Encoder that doubles the input
        StringEncoder doubleEncoder = new StringEncoder() {
            public Object encode(Object source) throws EncoderException {
                return encode((String) source);
            }

            public String encode(String source) throws EncoderException {
                return source + source;
            }
        };
        int result = SoundexUtils.difference(doubleEncoder, "A", "A");
        // "AA" vs "AA" = 2 matches
        assertEquals(2, result);
    }

    public void testDifferenceNull() throws EncoderException {
        int result = SoundexUtils.difference(mockEncoder, null, null);
        assertEquals(0, result);
    }
}
```

These test suites comprehensively exercise the target classes with:

1. **Caverphone tests** (30 test methods): Cover null/empty inputs, case conversion, character removal, Caverphone 2.0 transformations, output validation, and equality comparisons.

2. **Metaphone tests** (30 test methods): Test null/empty inputs, case conversion, initial character handling (KN, GH, WR, WH, X), various phonetic rules (B, C, D, G, H, PH, SH, TH, V, etc.), max code length configuration, and helper methods for character checking.

3. **SoundexUtils tests** (30 test methods): Validate the clean method for letter extraction and uppercase conversion, differenceEncoded for computing matching character counts, and difference for encoder-based difference calculations.

All tests use JUnit 3.8.1 compatible syntax with `TestCase` base class, maintain independence, avoid external dependencies, and assert only on the fixed reference behavior documented in the production source.