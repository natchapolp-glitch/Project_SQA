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
