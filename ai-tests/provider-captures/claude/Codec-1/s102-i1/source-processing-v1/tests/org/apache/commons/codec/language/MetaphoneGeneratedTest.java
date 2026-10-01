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
