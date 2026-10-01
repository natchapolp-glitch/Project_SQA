```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class CaverphoneGeneratedTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() throws Exception {
        super.setUp();
        caverphone = new Caverphone();
    }

    public void testNullAndEmptyReturnTenOnes() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
    }

    public void testThompson() {
        assertEquals("TMPSN11111", caverphone.caverphone("Thompson"));
    }

    public void testVowelHandlingAndFinalE() {
        assertEquals("PTA1111111", caverphone.caverphone("Peter"));
        assertEquals("PTA1111111", caverphone.caverphone("Peta"));
        assertEquals("A111111111", caverphone.caverphone("a"));
    }

    public void testSpecialStartSequences() {
        assertEquals("KF11111111", caverphone.caverphone("cough"));
        assertEquals("ANF1111111", caverphone.caverphone("enough"));
        assertEquals("TRF1111111", caverphone.caverphone("trough"));
        assertEquals("NM11111111", caverphone.caverphone("gnome"));
        assertEquals("M111111111", caverphone.caverphone("mb"));
    }

    public void testCaseAndNonLetterCharactersIgnored() {
        String expected = caverphone.caverphone("Thompson");
        assertEquals(expected, caverphone.caverphone("THOMPSON"));
        assertEquals(expected, caverphone.caverphone("Th-ompson 1"));
    }

    public void testEncodeString() {
        assertEquals("TMPSN11111", caverphone.encode("Thompson"));
        assertEquals("1111111111", caverphone.encode((String) null));
    }

    public void testEncodeObjectWithString() throws Exception {
        Object result = caverphone.encode((Object) "Thompson");
        assertEquals("TMPSN11111", result);
    }

    public void testEncodeObjectWithNonStringThrows() {
        try {
            caverphone.encode((Object) Integer.valueOf(42));
            fail("Expected EncoderException for non-String input");
        } catch (EncoderException expected) {
            // expected
        }
        try {
            caverphone.encode((Object) null);
            fail("Expected EncoderException for null input");
        } catch (EncoderException expected) {
            // expected
        }
    }

    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("Peter", "Peta"));
        assertTrue(caverphone.isCaverphoneEqual("Thompson", "THOMPSON"));
        assertTrue(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("Peter", "Thompson"));
        assertFalse(caverphone.isCaverphoneEqual(null, "a"));
    }
}
```

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;

public class MetaphoneGeneratedTest extends TestCase {

    private Metaphone metaphone;

    protected void setUp() throws Exception {
        super.setUp();
        metaphone = new Metaphone();
    }

    public void testNullEmptyAndSingleCharacter() {
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("X", metaphone.metaphone("x"));
        assertEquals("K", metaphone.metaphone("k"));
    }

    public void testDefaultMaxCodeLen() {
        assertEquals(4, metaphone.getMaxCodeLen());
        assertEquals("0MPS", metaphone.metaphone("Thompson"));
    }

    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(10);
        assertEquals(10, metaphone.getMaxCodeLen());
        assertEquals("0MPSN", metaphone.metaphone("Thompson"));

        metaphone.setMaxCodeLen(2);
        assertEquals(2, metaphone.getMaxCodeLen());
        assertEquals("0M", metaphone.metaphone("Thompson"));
    }

    public void testInitialLetterExceptions() {
        assertEquals("NM", metaphone.metaphone("Gnome"));    // GN -> N
        assertEquals("NT", metaphone.metaphone("Knight"));   // KN -> N
        assertEquals("RT", metaphone.metaphone("Wright"));   // WR -> R
        assertEquals("WX", metaphone.metaphone("Which"));    // WH -> W
        assertEquals("EBRS", metaphone.metaphone("Aebersold")); // AE -> E
        assertEquals("SFR", metaphone.metaphone("Xavier"));  // initial X -> S
    }

    public void testSilentLetters() {
        assertEquals("0M", metaphone.metaphone("Thumb"));    // terminal MB
        assertEquals("NT", metaphone.metaphone("Night"));    // GH before consonant
        assertEquals("BB", metaphone.metaphone("Bobby"));    // duplicate B, trailing Y
    }

    public void testCVariants() {
        assertEquals("SNS", metaphone.metaphone("Science")); // SCI discarded, CE -> S
        assertEquals("KRX", metaphone.metaphone("Church"));  // initial CH + vowel -> K, later CH -> X
        assertEquals("KK", metaphone.metaphone("Quick"));    // Q -> K, C -> K, K after C dropped
    }

    public void testPhShAndTh() {
        assertEquals("FN", metaphone.metaphone("Phone"));
        assertEquals("FX", metaphone.metaphone("Fish"));
        assertEquals("0MPS", metaphone.metaphone("Thompson"));
    }

    public void testTioAndDge() {
        assertEquals("NXN", metaphone.metaphone("Nation"));
        assertEquals("BRJ", metaphone.metaphone("Bridge"));
        assertEquals("JJ", metaphone.metaphone("Judge"));
    }

    public void testCaseInsensitive() {
        assertEquals(metaphone.metaphone("Thompson"), metaphone.metaphone("THOMPSON"));
        assertEquals("0MPS", metaphone.metaphone("thompson"));
    }

    public void testEncodeStringAndIsMetaphoneEqual() {
        assertEquals("NT", metaphone.encode("Knight"));
        assertTrue(metaphone.isMetaphoneEqual("Knight", "Night"));
        assertTrue(metaphone.isMetaphoneEqual(null, ""));
        assertFalse(metaphone.isMetaphoneEqual("Knight", "Thompson"));
    }

    public void testEncodeObject() throws Exception {
        assertEquals("NT", metaphone.encode((Object) "Knight"));
        try {
            metaphone.encode((Object) Integer.valueOf(1));
            fail("Expected EncoderException for non-String input");
        } catch (EncoderException expected) {
            // expected
        }
        try {
            metaphone.encode((Object) null);
            fail("Expected EncoderException for null input");
        } catch (EncoderException expected) {
            // expected
        }
    }
}
```

```java
package org.apache.commons.codec.language;

import junit.framework.TestCase;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsGeneratedTest extends TestCase {

    /** Returns its input unchanged. */
    private static class IdentityEncoder implements StringEncoder {
        public Object encode(Object pObject) throws EncoderException {
            return pObject;
        }

        public String encode(String pString) throws EncoderException {
            return pString;
        }
    }

    /** Always fails to encode. */
    private static class FailingEncoder implements StringEncoder {
        public Object encode(Object pObject) throws EncoderException {
            throw new EncoderException("boom");
        }

        public String encode(String pString) throws EncoderException {
            throw new EncoderException("boom");
        }
    }

    public void testCleanNullAndEmptyReturnedAsIs() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }

    public void testCleanAllLettersIsUpperCased() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
    }

    public void testCleanRemovesNonLetters() {
        assertEquals("ABC", SoundexUtils.clean("a1b2-c"));
        assertEquals("HELLOWORLD", SoundexUtils.clean("Hello World"));
    }

    public void testCleanWithNoLettersReturnsEmpty() {
        assertEquals("", SoundexUtils.clean("123 -!"));
    }

    public void testDifferenceEncodedNullReturnsZero() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "A123"));
        assertEquals(0, SoundexUtils.differenceEncoded("A123", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    public void testDifferenceEncodedCountsMatchingPositions() {
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A123"));
        assertEquals(1, SoundexUtils.differenceEncoded("A123", "A456"));
        assertEquals(2, SoundexUtils.differenceEncoded("A123", "B124"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    public void testDifferenceEncodedDifferentLengthsUsesShorter() {
        assertEquals(3, SoundexUtils.differenceEncoded("A12", "A1234"));
        assertEquals(3, SoundexUtils.differenceEncoded("A1234", "A12"));
        assertEquals(0, SoundexUtils.differenceEncoded("", "A123"));
    }

    public void testDifferenceUsesEncoder() throws Exception {
        StringEncoder identity = new IdentityEncoder();
        assertEquals(4, SoundexUtils.difference(identity, "A123", "A123"));
        assertEquals(1, SoundexUtils.difference(identity, "A123", "A456"));

        StringEncoder caverphone = new Caverphone();
        assertEquals(10, SoundexUtils.difference(caverphone, "Peter", "Peta"));
        assertEquals(5, SoundexUtils.difference(caverphone, "Peter", "Thompson"));
    }

    public void testDifferencePropagatesEncoderException() {
        try {
            SoundexUtils.difference(new FailingEncoder(), "a", "b");
            fail("Expected EncoderException");
        } catch (EncoderException expected) {
            assertEquals("boom", expected.getMessage());
        }
    }
}
```